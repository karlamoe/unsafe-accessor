plugins {
    java
    `java-library`
    `maven-publish`
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


dependencies {
    implementation(project(":unsafe-accessor-security"))
}
