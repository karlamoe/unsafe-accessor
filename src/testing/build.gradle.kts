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
