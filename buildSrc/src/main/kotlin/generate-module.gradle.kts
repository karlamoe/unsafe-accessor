import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*

pluginManager.apply("java")
val theExt = extensions.create<ModuleGenerateExtension>("moduleGenerate")

val generateModule = tasks.register("generateModule") {
    val output = temporaryDir.resolve("output")
    outputs.dir(output)

    doLast {
        output.parentFile?.mkdirs()

        val classWriter = ClassWriter(0)
        classWriter.visit(V9, ACC_MODULE, "module-info", null, null, null)
        classWriter.visitSource("module-info.java", null)

        val moduleVisitor = classWriter.visitModule(theExt.moduleName, 0, project.version.toString())
        moduleVisitor.visitRequire("java.base", ACC_MANDATED, null)

        theExt.actions.forEach { it(moduleVisitor) }

        val file = output.resolve("module-info.class")
        moduleVisitor.visitEnd()
        classWriter.visitEnd()
        file.writeBytes(classWriter.toByteArray())
    }
}

configure<SourceSetContainer> {
    named("main") {
        compiledBy(generateModule)
        output.dir(generateModule.map { it.outputs })
    }
}


