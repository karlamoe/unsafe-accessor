plugins {
    java
    `java-library`
    `binary-compatibility`
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

pluginManager.apply("unsafe-impl")

val deps = project.project(":unsafe-accessor-unsafe-java9-prototype").files(
    "build/generated/codegen",
    "build/classes/java/main",
)

dependencies {
    implementation(project(":unsafe-accessor-security"))
    api(project(":unsafe-accessor-unsafe-api"))

    compileOnly(libs.google.autoservice)
    annotationProcessor(libs.google.autoservice)

    compileOnly(deps)
}

sourceSets.main {
    output.dir(deps)
}

tasks.compileJava {
    sourceCompatibility = "9"
    targetCompatibility = "9"

    dependsOn(":unsafe-accessor-unsafe-java9-prototype:classes")
}

