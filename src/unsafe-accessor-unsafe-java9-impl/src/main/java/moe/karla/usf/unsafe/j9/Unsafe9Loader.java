package moe.karla.usf.unsafe.j9;

import moe.karla.usf.unsafe.Unsafe;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;


class Unsafe9Loader {
    static byte[] loadClassBytecode() throws Throwable {

        try {
            Class.forName("java.lang.classfile.ClassFile");
            return (byte[]) Class.forName("moe.karla.usf.unsafe.j9.CodegenClassFile").getDeclaredMethod("generate").invoke(null);
        } catch (Throwable ignored) {
        }


        try (InputStream stream = Unsafe9Loader.class.getResourceAsStream("UnsafeDynamic.class")) {
            assert stream != null;
            return stream.readAllBytes();
        }
    }

    static Unsafe load() throws Throwable {

        try {
            MethodHandles.Lookup lookupLookup = Unsafe9Abs.rootAccess.trustedLookupIn(MethodHandles.Lookup.class);

            byte[] code = loadClassBytecode();

//     static native Class<?> defineClass0(ClassLoader loader,
//                                        Class<?> lookup,
//                                        String name,
//                                        byte[] b, int off, int len,
//                                        ProtectionDomain pd,
//                                        boolean initialize,
//                                        int flags,
//                                        Object classData);
            Class<?> vmConstants = Class.forName("java.lang.invoke.MethodHandleNatives$Constants");
            int flags = 0;
            flags |= (int) lookupLookup.findStaticGetter(vmConstants, "HIDDEN_CLASS", int.class).invoke();
            flags |= (int) lookupLookup.findStaticGetter(vmConstants, "ACCESS_VM_ANNOTATIONS", int.class).invoke();

            MethodHandles.Lookup classLoader = Unsafe9Abs.rootAccess.trustedLookupIn(ClassLoader.class);
            MethodHandle definer = classLoader.findStatic(
                    ClassLoader.class,
                    "defineClass0",
                    MethodType.methodType(Class.class, ClassLoader.class, Class.class, String.class, byte[].class, int.class, int.class, ProtectionDomain.class, boolean.class, int.class, Object.class)
            );
            Class<?> result = (Class<?>) definer.invoke(
                    Unsafe9Loader.class.getClassLoader(),
                    Unsafe9Loader.class,
                    "moe/karla/usf/unsafe/j9/UnsafeDynamic",
                    code, 0, code.length,
                    Unsafe9Loader.class.getProtectionDomain(),
                    true, flags, null
            );


            MethodHandles.Lookup resultLookup = Unsafe9Abs.rootAccess.trustedLookupIn(result);
            return (Unsafe) resultLookup.findConstructor(result, MethodType.methodType(void.class)).invoke();
        } catch (Throwable ignored) {
        }


        return new UnsafeDynamic();
    }
}
