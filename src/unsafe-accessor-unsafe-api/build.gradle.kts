plugins {
    java
    `java-library`
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


dependencies {
    implementation(project(":unsafe-accessor-security"))
    api(project(":unsafe-accessor-root"))


    compileOnly(libs.google.autoservice)
    annotationProcessor(libs.google.autoservice)
}
