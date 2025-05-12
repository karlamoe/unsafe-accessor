plugins {
    java
    `java-library`
}

pluginManager.apply("unsafe-impl")


java {
    sourceCompatibility = JavaVersion.VERSION_1_9
    targetCompatibility = JavaVersion.VERSION_1_9
}


dependencies {
    implementation(project(":unsafe-accessor-security"))
    api(project(":unsafe-accessor-unsafe-api"))
}

sourceSets.register("codegen") {
    compileClasspath = sourceSets.main.get().compileClasspath
    runtimeClasspath += sourceSets.main.get().compileClasspath
}

val codegenVersion = JavaLanguageVersion.of(24)


tasks.named<JavaCompile>("compileCodegenJava") {
    // TODO java 25 LTS
    javaCompiler.set(javaToolchains.compilerFor {
        languageVersion.set(codegenVersion)
    })
    sourceCompatibility = codegenVersion.toString()
    targetCompatibility = codegenVersion.toString()
}

tasks.register<JavaExec>("codegen") {
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(codegenVersion)
    })
    classpath = sourceSets.getByName("codegen").runtimeClasspath
    mainClass = "codegen.Codegen"
    workingDir = project.projectDir
}


tasks.classes { dependsOn("codegen") }


