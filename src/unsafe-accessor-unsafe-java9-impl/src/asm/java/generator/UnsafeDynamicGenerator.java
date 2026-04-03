package generator;

import moe.karla.asm.generator.ClassGenerator;
import moe.karla.asm.generator.GeneratorContext;
import moe.karla.asm.util.AsmUtil;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public class UnsafeDynamicGenerator extends ClassGenerator implements Opcodes {
    @Override
    public void generate(GeneratorContext context) throws Throwable {
        var baseType = Type.getObjectType("moe/karla/usf/unsafe/Unsafe");
        var absType = Type.getObjectType("moe/karla/usf/unsafe/j9/Unsafe9Abs");
        var baseClass = AsmUtil.readClass(baseType.getClassName());


        var targetName = Type.getObjectType("moe/karla/usf/unsafe/j9/UnsafeDynamic");

        var template = new ClassNode();
        template.visit(
                V9,
                ACC_PUBLIC,
                targetName.getInternalName(),
                null,
                absType.getInternalName(),
                null
        );
        generateConstructor(template, false, absType.getInternalName());

        for (var method : baseClass.methods) {
            if ((method.access & ACC_ABSTRACT) == 0) continue;
            if ((method.access & ACC_STATIC) != 0) continue;

            if ("<init>".equals(method.name)) continue;


            var mv = template.visitMethod(
                    Opcodes.ACC_PUBLIC | (method.access & ACC_SYNTHETIC),
                    method.name,
                    method.desc, method.signature, null
            );
            mv.visitAnnotation("Ljdk/internal/vm/annotation/ForceInline;", true);

            AsmUtil.pushArguments(mv, 1, Type.getArgumentTypes(method.desc));

            mv.visitInvokeDynamicInsn(
                    method.name, method.desc,
                    new Handle(
                            H_INVOKESTATIC,
                            "moe/karla/usf/unsafe/j9/UnsafeMetafactory",
                            "bootstrap",
                            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
                            false)
            );

            mv.visitInsn(Type.getReturnType(method.desc).getOpcode(IRETURN));
            mv.visitMaxs(0, 0);
        }

        context.addClass(template::accept);
    }
}
