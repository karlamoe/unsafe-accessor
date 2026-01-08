package moe.karla.usf.definer;

import moe.karla.usf.root.RootAccess;
import moe.karla.usf.root.util.RunCatching;
import moe.karla.usf.root.util.SneakyThrow;
import moe.karla.usf.root.util.ThrowingHandle;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;

class ClassDefinerImpl extends ClassDefiner {
    private final MethodHandle MH$defineClass;

    ClassDefinerImpl() {
        final RootAccess instance = RootAccess.getInstance();

        MethodType defineClassDesc = MethodType.methodType(
                Class.class,
                String.class,
                byte[].class, int.class, int.class,
                ClassLoader.class, ProtectionDomain.class
        );

        MH$defineClass = RunCatching.run(() -> {
            Class<?> jdkInternalUnsafe = Class.forName("jdk.internal.misc.Unsafe");
            MethodHandles.Lookup lookup = instance.privateLookupIn(jdkInternalUnsafe);

            Object unsafe = lookup.findStatic(jdkInternalUnsafe, "getUnsafe", MethodType.methodType(jdkInternalUnsafe)).invoke();

            return lookup.findVirtual(
                    jdkInternalUnsafe,
                    "defineClass",
                    defineClassDesc
            ).bindTo(unsafe);

        }).recover(() -> {
            Class<?> sunUnsafe = Class.forName("sun.misc.Unsafe");
            MethodHandles.Lookup lookup = instance.privateLookupIn(sunUnsafe);

            Object unsafe = lookup.findStaticGetter(sunUnsafe, "theUnsafe", sunUnsafe).invoke();

            return lookup.findVirtual(
                    sunUnsafe,
                    "defineClass",
                    defineClassDesc
            ).bindTo(unsafe);
        }).recover(() -> {
            MethodHandles.Lookup lookup = instance.privateLookupIn(ClassLoader.class);

            MethodHandle mh = lookup.findStatic(
                    ClassLoader.class,
                    "defineClass1",
                    MethodType.methodType(Class.class,
                            ClassLoader.class, String.class,
                            byte[].class, int.class, int.class,
                            ProtectionDomain.class,
                            String.class
                    )
            );

            mh = MethodHandles.insertArguments(mh, 6, "JVM_Define");
            mh = MethodHandles.permuteArguments(mh, defineClassDesc,
                    4, 0, 1, 2, 3, 5
            );
            return mh;
        }).recover(erro -> {
            return ThrowingHandle.makeThrow(erro, defineClassDesc);
        }).getOrThrow();
    }

    @Override
    public Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader classLoader, ProtectionDomain domain) {
        try {
            return (Class<?>) MH$defineClass.invoke(name, b, off, len, classLoader, domain);
        } catch (Throwable e) {
            throw SneakyThrow.t(e);
        }

    }
}
