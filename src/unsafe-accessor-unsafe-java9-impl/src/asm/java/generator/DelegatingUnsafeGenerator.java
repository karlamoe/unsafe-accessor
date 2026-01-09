package generator;

import moe.karla.asm.generator.ClassGenerator;
import moe.karla.asm.generator.GeneratorContext;
import moe.karla.asm.util.AsmUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class DelegatingUnsafeGenerator extends ClassGenerator implements Opcodes {
    @Override
    public void generate(GeneratorContext context) throws Throwable {
        context.addClass(this::generate);
    }

    private void generate(ClassVisitor cv) throws Throwable {
        var token = Type.getObjectType("moe/karla/usf/unsafe/j9/DelegatingUnsafe");
        var baseType = Type.getObjectType("moe/karla/usf/unsafe/Unsafe");
        var baseClass = AsmUtil.readClass(baseType.getInternalName());

        cv.visit(
                V1_8,
                ACC_PUBLIC,
                token.getInternalName(),
                null,
                baseType.getInternalName(),
                null
        );
        cv.visitField(
                ACC_PROTECTED | ACC_FINAL,
                "delegate",
                baseType.getDescriptor(),
                null, null
        );
        {
            var init = cv.visitMethod(ACC_PROTECTED, "<init>",
                    "(" + baseType.getDescriptor() + ")V", null, null
            );
            init.visitVarInsn(ALOAD, 0);
            init.visitMethodInsn(
                    INVOKESPECIAL, baseType.getInternalName(),
                    "<init>", "()V", false
            );
            init.visitVarInsn(ALOAD, 0);
            init.visitVarInsn(ALOAD, 1);
            init.visitFieldInsn(PUTFIELD, token.getInternalName(), "delegate", baseType.getDescriptor());
            init.visitInsn(RETURN);
            init.visitMaxs(0, 0);
            init.visitEnd();
        }

        for (var method : baseClass.methods) {
            if ((method.access & ACC_FINAL) != 0) continue;
            if ((method.access & ACC_STATIC) != 0) continue;
            if ("<init>".equals(method.name)) continue;


            var met = cv.visitMethod(
                    Opcodes.ACC_PUBLIC | (method.access & ACC_SYNTHETIC),
                    method.name,
                    method.desc,
                    method.signature,
                    null
            );

            met.visitVarInsn(ALOAD, 0);
            met.visitFieldInsn(GETFIELD, token.getInternalName(), "delegate", baseType.getDescriptor());
            AsmUtil.pushArguments(met, 1, Type.getArgumentTypes(method.desc));
            met.visitMethodInsn(
                    INVOKEVIRTUAL,
                    baseType.getInternalName(), method.name, method.desc, false
            );
            met.visitInsn(Type.getReturnType(method.desc).getOpcode(IRETURN));
            met.visitMaxs(0, 0);
        }
    }
}
