package usftest;

import moe.karla.usf.unsafe.Unsafe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class TestUnsafe {
    @Test
    public void testUnsafe() {
        Unsafe.getUnsafe();
    }

    @Test
    public void testDefineAnonymousClass() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "java/lang/Invoke0", null, "java/lang/Object", null);
        byte[] byteArray = cw.toByteArray();

        checkAnonymousClass(Unsafe.getUnsafe().defineAnonymousClass(Object.class, byteArray, null));
        checkAnonymousClass(Unsafe.getUnsafe().defineAnonymousClass(Object.class, byteArray, null));
        checkAnonymousClass(Unsafe.getUnsafe().defineAnonymousClass(Object.class, byteArray, null));
    }

    private static void checkAnonymousClass(Class<?> c) {
        Assertions.assertTrue(c.getName().contains("/"), c.getName());
    }


    @Test
    public void testDirectDefineClass() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "java/lang/InjectedClass", null, "java/lang/Object", null);
        byte[] byteArray = cw.toByteArray();

        Class<?> defineClass = Unsafe.getUnsafe().defineClass("java.lang.InjectedClass", byteArray, 0, byteArray.length, null, null);
        Assertions.assertNull(defineClass.getClassLoader());
    }
}
