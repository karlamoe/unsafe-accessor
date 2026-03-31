package moe.karla.usf.unsafe.j9;

import moe.karla.usf.root.RootAccess;
import moe.karla.usf.unsafe.Unsafe;
import moe.karla.usf.unsafe.UnsafeInitializer;

public abstract class Unsafe9Abs extends Unsafe {
    static final jdk.internal.misc.Unsafe usf = jdk.internal.misc.Unsafe.getUnsafe();
    static final RootAccess rootAccess = RootAccess.getInstance();

    protected Unsafe9Abs() {
        UnsafeInitializer.validate();
    }

    @Override
    public Object getOriginalUnsafe() {
        return usf;
    }

    @Override
    public boolean isJava9() {
        return true;
    }

    @Override
    public long arrayBaseOffset(Class<?> arrayClass) {
        return usf.arrayBaseOffset(arrayClass);
    }
}
