package moe.karla.usf.unsafe.j9;

import moe.karla.usf.root.util.RunCatching;
import moe.karla.usf.unsafe.Unsafe;


class Unsafe9Loader {
    static Unsafe load() throws Throwable {
        Class<?> jdkUnsafe = Class.forName("jdk.internal.misc.Unsafe");

        boolean ref = RunCatching.run(() -> {
            return jdkUnsafe.getMethod("getReference", Object.class, long.class);
        }).exceptionOrNull() == null;

        return (ref ? Unsafe9Ref.class : Unsafe9Obj.class).getDeclaredConstructor().newInstance();
    }
}
