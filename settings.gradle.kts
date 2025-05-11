rootProject.name = "unsafe-accessor"

fun includeProject(name: String) {
    include(":$name")
    project(":$name").projectDir = rootProject.projectDir.resolve("src/$name")
}

includeProject("unsafe-accessor-security")
includeProject("unsafe-accessor-root")
includeProject("testing")

