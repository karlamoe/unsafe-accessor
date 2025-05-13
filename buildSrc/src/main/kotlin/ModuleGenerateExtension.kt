import org.objectweb.asm.ModuleVisitor

open class ModuleGenerateExtension {
    val actions = mutableListOf<ModuleVisitor.() -> Unit>()
    var moduleName = ""

    fun init(block: ModuleVisitor.() -> Unit) {
        actions += block
    }
}