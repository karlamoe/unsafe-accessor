package moe.karla.usf.unsafe.j9;

import moe.karla.usf.root.RootAccess;
import moe.karla.usf.unsafe.Unsafe;

import java.lang.invoke.MethodHandles;

public class AnonymousClassFixedUnsafe extends DelegatingUnsafe {
    private final RootAccess rootAccess = RootAccess.getInstance();

    public AnonymousClassFixedUnsafe(Unsafe delegate) {
        super(delegate);
    }

    // @Override
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
