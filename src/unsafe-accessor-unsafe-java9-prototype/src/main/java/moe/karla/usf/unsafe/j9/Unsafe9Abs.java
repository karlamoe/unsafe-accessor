package moe.karla.usf.unsafe.j9;

import moe.karla.usf.root.RootAccess;
import moe.karla.usf.unsafe.Unsafe;
import moe.karla.usf.unsafe.UnsafeInitializer;

import java.lang.invoke.MethodHandles;

public abstract class Unsafe9Abs extends Unsafe {
    protected static final jdk.internal.misc.Unsafe usf = jdk.internal.misc.Unsafe.getUnsafe();
    private final RootAccess rootAccess = RootAccess.getInstance();

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
    public Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches) {
        MethodHandles.Lookup lookup = rootAccess.privateLookupIn(hostClass);

        try {
            if (cpPatches == null) {
                return lookup.defineHiddenClass(data, false, MethodHandles.Lookup.ClassOption.NESTMATE).lookupClass();
            } else {
                return lookup.defineHiddenClassWithClassData(data, cpPatches[0], false, MethodHandles.Lookup.ClassOption.NESTMATE).lookupClass();
            }
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }
}
