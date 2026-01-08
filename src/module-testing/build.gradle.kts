plugins {
    java
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_9
    targetCompatibility = JavaVersion.VERSION_1_9
}


application {
    mainModule.set("moe.karla.unsafe.testing")
    mainClass.set("moe.karla.unsafe.testing.TestingRun")
}


dependencies {
    implementation(project(":unsafe-accessor-unsafe"))
    implementation(project(":unsafe-accessor-module-editor"))
}

configurations.implementation {
    extendsFrom(configurations.junit5.get())
}

tasks.test {
    dependsOn("run")
}
