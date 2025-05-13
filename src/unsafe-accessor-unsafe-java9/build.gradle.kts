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

pluginManager.apply("unsafe-impl")

val impl = project.project(":unsafe-accessor-unsafe-java9-impl")
val implOutputs = impl.provider {
    impl.sourceSets.main
}.flatMap { it }

sourceSets.main {
    output.dir(implOutputs.map { it.output })
}
tasks.classes {
    outputs.upToDateWhen { false }
    dependsOn(impl.provider { impl.tasks.named("jar") })
}

moduleGenerate {
    moduleName = "moe.karla.unsafe.unsafe.j9"
    init {
        visitPackage("moe/karla/usf/unsafe/j9")


        visitRequire("moe.karla.unsafe.root", 0, null)
        visitRequire("moe.karla.unsafe.security", 0, null)
        visitRequire("moe.karla.unsafe.unsafe", 0, null)

        visitProvide("moe/karla/usf/unsafe/impl/UnsafeProvider", "moe/karla/usf/unsafe/j9/Unsafe9Provider")
    }
}
