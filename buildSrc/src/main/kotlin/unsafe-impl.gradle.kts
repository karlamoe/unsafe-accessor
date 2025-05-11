apply<JavaPlugin>()

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.named<JavaCompile>("compileJava") {
    options.compilerArgs.addAll(listOf("--add-exports", "java.base/jdk.internal.misc=ALL-UNNAMED"))
}