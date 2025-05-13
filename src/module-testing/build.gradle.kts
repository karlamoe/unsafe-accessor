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
}

tasks.test {
    dependsOn("run")
}
