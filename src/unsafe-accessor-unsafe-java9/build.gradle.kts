plugins {
    java
    `java-library`
    `maven-publish`
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
