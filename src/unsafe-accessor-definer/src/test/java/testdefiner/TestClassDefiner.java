package testdefiner;

import moe.karla.usf.definer.ClassDefiner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TestClassDefiner {
    public static byte[] makeHelloWorld(String name) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(
                Opcodes.V1_8,
                Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                name,
                null,
                "java/lang/Object",
                new String[]{"java/lang/Runnable"}
        );
        MethodVisitor mv = cw.visitMethod(
                Opcodes.ACC_PUBLIC,
                "<init>",
                "()V",
                null, null
        );
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);


        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "run", "()V", null, null);
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("Hello, World!");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);

        return cw.toByteArray();
    }

    void runDefine(ClassLoader loader, String name) throws Throwable {
        byte[] code = makeHelloWorld(name);
        String javaName = name.replace('/', '.');
        Class<?> klass = ClassDefiner.getInstance().defineClass(
                javaName,
                code, 0, code.length,
                loader, null
        );
        Assertions.assertEquals(javaName, klass.getName());
        Assertions.assertSame(loader, klass.getClassLoader());
        Assertions.assertSame(klass, Class.forName(javaName, false, loader));

        klass.asSubclass(Runnable.class).newInstance().run();
    }

    @Test
    void testDefineInBootstrap() throws Throwable {
        runDefine(null, "java/lang/HelloClassDefiner");
        runDefine(null, "java/lang/OtherClassDefiner");
    }

    @Test
    void testDefineInCustomClassLoader() throws Throwable {
        runDefine(getClass().getClassLoader(), "moe/karla/HelloClassDefiner");
        runDefine(ClassLoader.getSystemClassLoader().getParent(), "moe/karla/HHelloPlatformDefiner");
    }
}
