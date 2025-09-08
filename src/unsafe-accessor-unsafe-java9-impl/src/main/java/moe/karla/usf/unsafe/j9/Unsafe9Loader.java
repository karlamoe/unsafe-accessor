package moe.karla.usf.unsafe.j9;

import moe.karla.usf.root.util.RunCatching;
import moe.karla.usf.unsafe.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;


class Unsafe9Loader {
    static Unsafe load() throws Throwable {
        Class<?> jdkUnsafe = Class.forName("jdk.internal.misc.Unsafe");

        boolean ref = RunCatching.run(() -> {
            return jdkUnsafe.getMethod("getReference", Object.class, long.class);
        }).exceptionOrNull() == null;

        Unsafe base = (ref ? Unsafe9Ref.class : Unsafe9Obj.class).getDeclaredConstructor().newInstance();

        try {
            Class.forName("java.lang.invoke.MethodHandles$Lookup$ClassOption");
            base = new AnonymousClassFixedUnsafe(base);
        } catch (Throwable ignored) {
        }

        try {
            MethodHandles.lookup().findVirtual(jdkUnsafe, "arrayBaseOffset", MethodType.methodType(long.class, Class.class));
            base = new ArrayBaseOffsetLongUnsafe(base);
        } catch (Throwable ignored) {
        }

        return base;
    }
}
