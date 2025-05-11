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


tasks.named<JavaCompile>("compileCodegenJava") {
    sourceCompatibility = JavaVersion.current().toString()
    targetCompatibility = JavaVersion.current().toString()
}

tasks.register<JavaExec>("codegen") {
    classpath = sourceSets.getByName("codegen").runtimeClasspath
    mainClass = "codegen.Codegen"
    workingDir = project.projectDir
}


tasks.classes { dependsOn("codegen") }


