plugins {
    `java-base`
}

fun Provider<in FileCollection>.toFileCollection(): FileCollection {
    return objects.fileCollection().also {
        it.from(this@toFileCollection)
    }
}

fun copyTest(project: Project, name: String, action: Action<in Test>) {
    val newTest = project.tasks.register<Test>("test${name.capitalize()}") {
        val originTask = project.provider { project.tasks.named<Test>("test") }.flatMap { it }

        this.classpath = originTask.map { it.classpath }.toFileCollection()
        this.testClassesDirs = originTask.map { it.testClassesDirs }.toFileCollection()

        action.execute(this)
    }
    project.tasks.check { dependsOn(newTest) }
}

fun Test.setTestCategory(name: String) {
    environment("USF_TEST_CATEGORY", "${project.path}:$name")
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        // outputs.upToDateWhen { false }
        useJUnitPlatform()
    }

    pluginManager.withPlugin("java") {
        val junit5 = configurations.register("junit5")
        configurations["testImplementation"].extendsFrom(junit5.get())


        dependencies {
            "junit5"(platform("org.junit:junit-bom:5.12.2"))
            "junit5"("org.junit.jupiter:junit-jupiter")
            "junit5"("org.junit.platform:junit-platform-launcher")

            "compileOnly"(libs.jetbrains.annotations)
        }

        if (project.name != "testing") {
            dependencies {
                "testImplementation"(project(":testing"))
            }
        }

        tasks.named<Test>("test") {
            setTestCategory("normal-test")
        }

        copyTest(project, "java8") {
            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(8))
            })
            setTestCategory("java8")
        }

        JreSearcher.searchJavaRuntimes().forEach { jre ->
            copyTest(project, jre.name) {
                setTestCategory(jre.name)
                this.executable = jre.executable.toString()
            }
        }
    }

}

