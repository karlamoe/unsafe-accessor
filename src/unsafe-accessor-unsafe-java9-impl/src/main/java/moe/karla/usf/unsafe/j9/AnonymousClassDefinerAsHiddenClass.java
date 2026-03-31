package moe.karla.usf.unsafe.j9;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("all")
class AnonymousClassDefinerAsHiddenClass {

    static Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches) {
        MethodHandles.Lookup lookup = Unsafe9Abs.rootAccess.privateLookupIn(hostClass);

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
