package moe.karla.usf.root.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class LegacySunUnsafeHelper {
    public static sun.misc.Unsafe getLegacyUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        return (sun.misc.Unsafe) theUnsafe.get(null);
    }
}
