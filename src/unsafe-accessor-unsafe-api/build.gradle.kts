import org.objectweb.asm.Opcodes

plugins {
    java
    `java-library`
    `maven-publish`
    `generate-module`
    `binary-compatibility`
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
        visitPackage("moe/karla/usf/unsafe/impl")
        visitExport("moe/karla/usf/unsafe", 0)
        visitExport("moe/karla/usf/unsafe/impl", 0, "moe.karla.unsafe.unsafe.j9")


        visitRequire("moe.karla.unsafe.root", 0, null)
        visitRequire("moe.karla.unsafe.security", 0, null)
        visitRequire("jdk.unsupported", Opcodes.ACC_STATIC_PHASE, null)

        visitUse("moe/karla/usf/unsafe/impl/UnsafeProvider")
        visitProvide("moe/karla/usf/unsafe/impl/UnsafeProvider", "moe/karla/usf/unsafe/sunlegacy/UnsafeJdk8Provider")
    }
}
