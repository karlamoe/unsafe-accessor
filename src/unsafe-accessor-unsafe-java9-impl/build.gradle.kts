plugins {
    java
    `java-library`
    `binary-compatibility`
    id("moe.karla.asm")
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

pluginManager.apply("unsafe-impl")


dependencies {
    asm(project(":unsafe-accessor-unsafe-api"))

    implementation(project(":unsafe-accessor-security"))
    api(project(":unsafe-accessor-unsafe-api"))

    compileOnly(libs.google.autoservice)
    annotationProcessor(libs.google.autoservice)
}


tasks.compileJava {
    sourceCompatibility = "9"
    targetCompatibility = "9"

    options.compilerArgs.add("-Xdiags:verbose")
}

