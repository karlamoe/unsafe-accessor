import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.SimpleRemapper
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode


pluginManager.apply("java")

val compileJava = tasks.named<JavaCompile>("compileJava")

abstract class TransformJava : DefaultTask() {
    @get:InputDirectory
    abstract val classes: DirectoryProperty

    @get:Internal
    abstract val srcTask: Property<Task>

    init {
        onlyIf { srcTask.get().state.didWork }
    }

    @TaskAction
    fun transform() {
        val classesByFile = mutableMapOf<File, ClassNode>()
        val classesByName = mutableMapOf<String, ClassNode>()

        // compatibility

        val suffixes = listOf(
            "compatibility",
            "_compatibility"
        )

        val mappings = mutableMapOf<String, String>()
        val remapper = object : SimpleRemapper(mappings) {
            override fun mapFieldName(owner: String?, name: String?, descriptor: String?): String? {
                return map("$owner.$name:$descriptor") ?: name
            }
        }

        this.classes.asFileTree.files.asSequence()
            .filter { it.isFile }
            .filter { it.extension == "class" }
            .forEach { klass ->
                val node = ClassNode()
                ClassReader(klass.readBytes()).accept(node, 0)
                classesByFile[klass] = node

                classesByName[node.name] = node

                node.fields?.forEach { fieldNode ->
                    suffixes.forEach { suffix ->
                        if (fieldNode.name.endsWith(suffix, ignoreCase = true)) {
                            mappings["${node.name}.${fieldNode.name}:${fieldNode.desc}"] =
                                fieldNode.name.substring(0, fieldNode.name.length - suffix.length)

                            fieldNode.access = fieldNode.access or Opcodes.ACC_SYNTHETIC
                        }
                    }
                }
                node.methods?.forEach { methodNode ->
                    suffixes.forEach { suffix ->
                        if (methodNode.name.endsWith(suffix, ignoreCase = true)) {
                            mappings["${node.name}.${methodNode.name}${methodNode.desc}"] =
                                methodNode.name.substring(0, methodNode.name.length - suffix.length)

                            methodNode.access = methodNode.access or Opcodes.ACC_SYNTHETIC
                        }
                    }
                }
            }

        classesByName["moe/karla/usf/unsafe/j9/ArrayBaseOffsetLongUnsafe"]?.let { klass ->
            klass.methods.find { it.name == "arrayBaseOffset" }?.let { method ->
                val insns = method.instructions

                insns.find { it.opcode == Opcodes.I2L }
                    ?.let { insns.remove(it) }

                insns.forEach {
                    if (it.opcode == Opcodes.INVOKEVIRTUAL
                        && it is MethodInsnNode
                        && it.name == "arrayBaseOffset"
                    ) {
                        it.desc = "(Ljava/lang/Class;)J"
                    }
                }
            }
        }

        classesByFile.forEach { (file, node) ->
            file.writeBytes(ClassWriter(0).also {
                node.accept(ClassRemapper(it, remapper))
            }.toByteArray())
        }
    }
}

val transformJava = tasks.register<TransformJava>("transformJava") {
    classes.set(compileJava.flatMap { it.destinationDirectory })
    srcTask.set(compileJava)
}


compileJava {
    finalizedBy(transformJava)
}
