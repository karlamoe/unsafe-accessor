plugins {
    java
    `java-library`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(project(":unsafe-accessor-unsafe-api"))
    implementation(project(":unsafe-accessor-unsafe-java9"))

    testImplementation(libs.asm)
}

