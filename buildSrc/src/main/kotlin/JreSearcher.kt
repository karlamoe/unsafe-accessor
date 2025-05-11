import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.*
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.pathString

object JreSearcher {
    data class JavaRuntime(
        val name: String,
        val executable: Path,
    )

    fun searchJavaRuntimes(): List<JavaRuntime> {
        val result = mutableListOf<Path>()
        result.addAll(searchFromPath())

        System.getenv("JAVA_HOME")?.let { home ->
            result.add(Path.of(home).resolve("bin/java"))
        }

        System.getProperty("user.home")?.let { userHome ->
            // intellij idea
            Path.of(userHome, ".jdks").listDirectoryEntries().forEach { entry ->
                result.add(entry.resolve("bin/java"))
            }
        }

        if (System.getProperty("os.name").contains("Windows", ignoreCase = true)) {
            result.toList()
                .also { result.clear() }
                .map { it.resolveSibling(it.name + ".exe") }
                .forEach { result.add(it) }
        }
        result.removeIf { !Files.isExecutable(it) }

        return result.toSet().map { it.toRuntime() }
    }

    private fun Path.toRuntime(): JavaRuntime {
        val normalizedPath = this.pathString.replace('\\', '/')
        val regex = Regex("([^/]+)/bin/java(?:\\.exe)?$")
        return JavaRuntime(
            name = regex.find(normalizedPath)?.groups?.get(1)?.value ?: normalizedPath.psha1(),
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