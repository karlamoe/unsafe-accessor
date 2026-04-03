import java.io.ByteArrayOutputStream

plugins {
    java
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_9
    targetCompatibility = JavaVersion.VERSION_1_9
}


application {
    mainModule.set("moe.karla.unsafe.testing")
    mainClass.set("moe.karla.unsafe.testing.TestingRun")
}


dependencies {
    implementation(project(":unsafe-accessor-unsafe"))
    implementation(project(":unsafe-accessor-module-editor"))
    implementation(project(":unsafe-accessor-definer"))
}

configurations.implementation {
    extendsFrom(configurations.junit5.get())
}

val testRuntimeClasspath = objects.fileCollection().apply {
    from(configurations.runtimeClasspath)
    from(sourceSets.main.map { it.output })
}

interface Services {
    @get:Inject
    val execOperation: ExecOperations
}


tasks.test {
    dependsOn("run")
}

val runAsAll = tasks.register("runAsAll")

JreSearcher.searchJavaRuntimes(project).forEach { runtime ->
    val newTask = tasks.register<JavaExec>("runAs${runtime.name}") {
        executable(runtime.executable.toFile().absolutePath)

        classpath = testRuntimeClasspath

        mainModule.set("moe.karla.unsafe.testing")
        mainClass.set("moe.karla.unsafe.testing.TestingRun")

        val execOperations = objects.newInstance<Services>().execOperation

        setOnlyIf {
            val outputStream = ByteArrayOutputStream()
            execOperations.exec {
                commandLine(runtime.executable.toFile().absolutePath, "-version")
                isIgnoreExitValue = true
                errorOutput = outputStream
            }
            val str = outputStream.toString("UTF-8")
            // println(str)
            !str.contains("1.8.")
        }

//        jvmArgumentProviders.add {
//            listOf("--limit-modules", "java.base")
//        }
    }

    runAsAll {
        dependsOn(newTask)
    }
}


tasks.test {
    dependsOn(runAsAll)
}
