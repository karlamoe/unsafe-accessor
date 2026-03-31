import me.champeau.jmh.JMHTask
import java.io.OutputStream
import java.util.*

plugins {
    java
    id("jmh-plugin")
}

dependencies {
    implementation(project(":unsafe-accessor-unsafe"))
}

val localDebug = false


if (localDebug) {
    val configurationRuntime = objects.fileCollection().apply {
        from(configurations.jmhRuntimeClasspath)
        from(configurations.runtimeClasspath)
        from(tasks.jmhJar.flatMap { it.archiveFile })

        builtBy(tasks.jmhJar)
    }

    tasks.register<JavaExec>("startJITWatcher") {
        mainClass = "-jar"
        args = listOf("D:\\protect/jitwatch-ui-1.4.9-shaded-win-x64.jar")
        dependsOn(configurationRuntime)

        javaLauncher.set(javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(25))
        })

        workingDir(file("jitwatch"))
        val propertiesFile = file("jitwatch/jitwatch.properties")
        doFirst {
            file("jitwatch").mkdirs()

            val properties = Properties()
            if (propertiesFile.exists()) {
                propertiesFile.reader().use { properties.load(it) }
            }
            properties.setProperty("Classes", configurationRuntime.joinToString(","))

            propertiesFile.writer().use {
                properties.store(it, null)
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}


val outputDir = layout.buildDirectory.dir("generated/jmh-reports")
JreSearcher.searchJavaRuntimes(project).forEach { runtime ->
    val taskName = "jmh${runtime.name}"
    project.tasks.register<JMHTask>(taskName) {
        executablePath.set(runtime.executable.toFile())
        resultsFile.set(outputDir.map { it.file("$taskName.txt") })

        if (localDebug) {
            jvmArgs.set(
                listOf(
                    "-XX:+UnlockDiagnosticVMOptions",
                    "-XX:LogFile=jitwatch/execute.log",
                    "-XX:+LogCompilation",
//                "-Xlog:class+load=info",
                    "-XX:+DebugNonSafepoints",
                    "-XX:+PrintAssembly",
                )
            )
            javaExecSpec.set {
                standardOutput = OutputStream.nullOutputStream()
            }
        }
    }
}

val jmhAll = tasks.register("jmhAll") {
    group = "jmh"
}

tasks.withType<JMHTask>().names.forEach { subTask ->
    jmhAll.configure {
        dependsOn(tasks.named(subTask))
    }
}

