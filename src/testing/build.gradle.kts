plugins {
    java
}

configurations["implementation"].extendsFrom(configurations["junit5"])

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    annotationProcessor(libs.google.autoservice)
    compileOnly(libs.google.autoservice)
    implementation(libs.asm)
    implementation(libs.asm.util)
    implementation(libs.asm.commons)
}

val srcJ9 = sourceSets.register("j9") {
    compileClasspath += sourceSets.main.get().compileClasspath

    tasks.named<JavaCompile>(compileJavaTaskName) {
        sourceCompatibility = "9"
        targetCompatibility = "9"
    }
}

sourceSets.main {
    output.dir(srcJ9.map { it.output })
}

