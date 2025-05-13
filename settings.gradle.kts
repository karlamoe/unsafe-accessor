rootProject.name = "unsafe-accessor"

fun includeProject(name: String) {
    include(":$name")
    project(":$name").projectDir = rootProject.projectDir.resolve("src/$name")
}

includeProject("unsafe-accessor-security")
includeProject("unsafe-accessor-root")
includeProject("unsafe-accessor-unsafe-api")
includeProject("unsafe-accessor-unsafe-java9-prototype")
includeProject("unsafe-accessor-unsafe-java9-impl")
includeProject("unsafe-accessor-unsafe-java9")
includeProject("unsafe-accessor-unsafe")
includeProject("testing")
includeProject("module-testing")

