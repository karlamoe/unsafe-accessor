package generator;

import moe.karla.asm.generator.ClassGenerator;
import moe.karla.asm.generator.GeneratorContext;
import moe.karla.asm.util.AsmUtil;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;

public class JdkUnsafeImplGenerator extends ClassGenerator implements Opcodes {
    @Override
    public void generate(GeneratorContext context) throws Throwable {
        var baseType = Type.getObjectType("moe/karla/usf/unsafe/Unsafe");
        var absType = Type.getObjectType("moe/karla/usf/unsafe/j9/Unsafe9Abs");
        var nameJdkUnsafe = "jdk/internal/misc/Unsafe";
        var baseClass = AsmUtil.readClass(baseType.getClassName());

        var templateName = Type.getObjectType("moe/karla/usf/unsafe/j9/UsfTemplate");

        var template = new ClassNode();
        template.visit(
                V9,
                ACC_PUBLIC,
                templateName.getInternalName(),
                null,
                absType.getInternalName(),
                null
        );
        generateConstructor(template, false, absType.getInternalName());

        for (var method : baseClass.methods) {
            if ((method.access & ACC_ABSTRACT) == 0) continue;
            if ((method.access & ACC_STATIC) != 0) continue;

            if ("getOriginalUnsafe".equals(method.name)) continue;
            if ("isJava9".equals(method.name)) continue;
            if ("arrayBaseOffset".equals(method.name)) continue;
            if ("<init>".equals(method.name)) continue;


            var mv = template.visitMethod(
                    Opcodes.ACC_PUBLIC | (method.access & ACC_SYNTHETIC),
                    method.name,
                    method.desc, method.signature, null
            );

            mv.visitFieldInsn(
                    GETSTATIC,
                    absType.getInternalName(),
                    "usf",
                    "Ljdk/internal/misc/Unsafe;"
            );
            AsmUtil.pushArguments(mv, 1, Type.getArgumentTypes(method.desc));
            mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    nameJdkUnsafe, method.name, method.desc, false
            );
            mv.visitInsn(Type.getReturnType(method.desc).getOpcode(IRETURN));
            mv.visitMaxs(0, 0);
        }


        context.addClass(cv -> {
            cv = new ClassRemapper(cv, new SimpleRemapper(Opcodes.ASM9,
                    "moe/karla/usf/unsafe/j9/UsfTemplate",
                    "moe/karla/usf/unsafe/j9/Unsafe9Ref"
            ) {
                @Override
                public String mapMethodName(String owner, String name, String descriptor) {
                    if (nameJdkUnsafe.equals(owner)) {
                        return name.replace("Object", "Reference");
                    }
                    return super.mapMethodName(owner, name, descriptor);
                }
            });
            template.accept(cv);
        });
        context.addClass(cv -> {
            cv = new ClassRemapper(cv, new SimpleRemapper(Opcodes.ASM9,
                    "moe/karla/usf/unsafe/j9/UsfTemplate",
                    "moe/karla/usf/unsafe/j9/Unsafe9Obj"
            ) {
                @Override
                public String mapMethodName(String owner, String name, String descriptor) {
                    if (nameJdkUnsafe.equals(owner)) {
                        return name.replace("Reference", "Object");
                    }
                    return super.mapMethodName(owner, name, descriptor);
                }
            });
            template.accept(cv);
        });
    }
}
