import org.gradle.api.Project
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.*
import kotlin.io.path.*

object JreSearcher {
    data class JavaRuntime(
        val name: String,
        val executable: Path,
    )

    fun searchJavaRuntimes(project: Project): List<JavaRuntime> {
        val result = mutableListOf<Path>()
        result.addAll(searchFromPath())

        System.getenv("JAVA_HOME")?.let { home ->
            result.add(Path.of(home).resolve("bin/java"))
        }

        System.getProperty("user.home")?.let { userHome ->
            // intellij idea
            val jdks = Path.of(userHome, ".jdks")
            if (jdks.exists()) {
                jdks.listDirectoryEntries().forEach { entry ->
                    result.add(entry.resolve("bin/java"))
                }
            }
        }
        Path.of("/opt/hostedtoolcache").takeIf { it.exists() }?.let { allChain ->
            Files.walk(allChain)
                .filter { it.isRegularFile() }
                .filter { it.name.removeSuffix(".exe") == "java" }
                .forEach { result.add(it) }
        }
        result.removeIf { it.absolutePathString().startsWith("/opt/hostedtoolcache/CodeQL") }

        project.rootProject.file("jdks.txt").toPath()
            .takeIf { it.exists() }
            ?.readText().orEmpty()
            .splitToSequence('\n')
            .filter { it.isNotBlank() }
            .filter { !it.startsWith("#") }
            .map { Path.of(it.trim()) }
            .filter { it.exists() }
            .flatMap { path ->
                Files.walk(path)
                    .filter { it.isRegularFile() }
                    .filter { it.name.removeSuffix(".exe") == "java" }
                    .iterator()
                    .asSequence()
            }
            .forEach { result.add(it) }


        if (System.getProperty("os.name").contains("Windows", ignoreCase = true)) {
            result.toList()
                .also { result.clear() }
                .map { it.resolveSibling(it.name.removeSuffix(".exe") + ".exe") }
                .forEach { result.add(it) }
        }
        result.removeIf { !Files.isExecutable(it) }

        val runtimes = result.toSet().map { it.toRuntime() }
        val resultRuntimes = mutableListOf<JavaRuntime>()
        val runtimeNames = mutableSetOf<String>()
        runtimes.forEach { runtime ->
            if (runtimeNames.add(runtime.name)) {
                resultRuntimes.add(runtime)
            } else {
                resultRuntimes.add(
                    JavaRuntime(
                        runtime.executable.toString().psha1(),
                        runtime.executable,
                    )
                )
            }
        }
        return resultRuntimes
    }

    private fun Path.toRuntime(): JavaRuntime {
        val normalizedPath = this.pathString.replace('\\', '/')
        val regexps = listOf(
            "^/opt/hostedtoolcache/(.+)/x64/bin/java$".toRegex(),
            "([^/]+)/bin/java(?:\\.exe)?$".toRegex(),
        )
        return JavaRuntime(
            name = regexps.asSequence().mapNotNull { regex ->
                regex.find(normalizedPath)?.groups?.get(1)?.value
            }.firstOrNull()?.replace('/', '.') ?: normalizedPath.psha1(),
            executable = this,
        )
    }

    private fun String.psha1(): String {
        return MessageDigest.getInstance("SHA-1").digest(this.toByteArray()).let {
            "jre_" + HexFormat.of().formatHex(it)
        }
    }

    private fun searchFromPath() = System.getenv().entries
        .find { it.key.equals("PATH", ignoreCase = true) }
        ?.value
        .orEmpty()
        .split(File.pathSeparator)
        .map { Path.of("$it/java") }
}