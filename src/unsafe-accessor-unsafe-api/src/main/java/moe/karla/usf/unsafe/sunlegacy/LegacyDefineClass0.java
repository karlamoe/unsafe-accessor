package moe.karla.usf.unsafe.sunlegacy;

import moe.karla.usf.root.util.RunCatching;
import moe.karla.usf.root.util.SneakyThrow;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;

@SuppressWarnings("JavaLangInvokeHandleSignature")
class LegacyDefineClass0 {
    static final MethodHandle MH_DEFINE_CLASS;
    static final MethodHandle MH_DEFINE_CLASS0;

    static {
        try {
            MethodType methodType = MethodType.methodType(
                    Class.class,
                    String.class, byte[].class, int.class, int.class, ClassLoader.class, ProtectionDomain.class
            );

            //     public native Class<?> defineClass(String name, byte[] b, int off, int len,
            //                                       ClassLoader loader,
            //                                       ProtectionDomain protectionDomain);


            MH_DEFINE_CLASS = MethodHandles.lookup().findVirtual(sun.misc.Unsafe.class, "defineClass", methodType);
            MH_DEFINE_CLASS0 = RunCatching.run(() -> {
                return MethodHandles.lookup().findVirtual(sun.misc.Unsafe.class, "defineClass0", methodType);
            }).recover(() -> MH_DEFINE_CLASS).getOrThrow();
        } catch (Throwable t) {
            throw SneakyThrow.t(t);
        }
    }
}
