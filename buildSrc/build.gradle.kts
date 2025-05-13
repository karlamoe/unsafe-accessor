plugins {
    `kotlin-dsl`
}


repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.asm.tree)
    implementation(libs.asm.util)
    implementation(libs.asm.commons)
}
