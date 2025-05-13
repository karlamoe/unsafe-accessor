plugins {
    java
    `java-library`
    `maven-publish`
    `generate-module`
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

moduleGenerate {
    moduleName = "moe.karla.unsafe.unsafe"
    init {
        visitPackage("moe/karla/usf/unsafe")
        visitPackage("moe/karla/usf/unsafe/sunlegacy")
        visitExport("moe/karla/usf/unsafe", 0)


        visitRequire("moe.karla.unsafe.root", 0, null)
        visitRequire("moe.karla.unsafe.security", 0, null)

        visitUse("moe/karla/usf/unsafe/UnsafeProvider")
        visitProvide("moe/karla/usf/unsafe/UnsafeProvider", "moe/karla/usf/unsafe/sunlegacy/UnsafeJdk8Provider")
    }
}
