package moe.karla.usf.unsafe.j9;

import moe.karla.usf.unsafe.Unsafe;

public class ArrayBaseOffsetLongUnsafe extends DelegatingUnsafe {
    public ArrayBaseOffsetLongUnsafe(Unsafe delegate) {
        super(delegate);
    }

    public long arrayBaseOffset(Class<?> arrayClass) {
        return Unsafe9Abs.usf.arrayBaseOffset(arrayClass);
    }
}
