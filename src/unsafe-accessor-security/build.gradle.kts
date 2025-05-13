plugins {
    java
    `maven-publish`
    `generate-module`
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

moduleGenerate {
    moduleName = "moe.karla.unsafe.security"
    init {
        visitPackage("moe/karla/usf/security")
        visitExport("moe/karla/usf/security", 0)
    }
}
