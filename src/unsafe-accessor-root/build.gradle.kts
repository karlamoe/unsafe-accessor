plugins {
    java
    `java-library`
    `maven-publish`
    `generate-module`
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


dependencies {
    implementation(project(":unsafe-accessor-security"))
}

moduleGenerate {
    moduleName = "moe.karla.unsafe.root"
    init {
        visitPackage("moe/karla/usf/root")
        visitPackage("moe/karla/usf/root/util")

        visitExport("moe/karla/usf/root", 0)
        visitExport("moe/karla/usf/root/util", 0)

        visitRequire("moe.karla.unsafe.security", 0, null)
    }
}
