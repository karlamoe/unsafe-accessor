plugins {
    java
    `maven-publish`
    `generate-module`
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
dependencies {
    implementation(project(":unsafe-accessor-security"))
    implementation(project(":unsafe-accessor-root"))
}

moduleGenerate {
    moduleName = "moe.karla.unsafe.module.editor"
    init {
        visitPackage("moe/karla/usf/module")

        visitExport("moe/karla/usf/module", 0)

        visitRequire("moe.karla.unsafe.security", 0, null)
        visitRequire("moe.karla.unsafe.root", 0, null)
    }
}
