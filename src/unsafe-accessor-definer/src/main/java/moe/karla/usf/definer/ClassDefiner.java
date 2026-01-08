package moe.karla.usf.definer;

import moe.karla.usf.security.RootSecurity;

import java.security.ProtectionDomain;

/// The helper for defining class at any classloader.
public abstract class ClassDefiner {
    private static final ClassDefiner INSTANCE = new ClassDefinerImpl();

    /// Get the instance, [RootSecurity.Type#ROOT_ACCESS_ALL] is required.
    public static ClassDefiner getInstance() {
        RootSecurity.check(RootSecurity.Type.ROOT_ACCESS_ALL);
        return INSTANCE;
    }


    public abstract Class<?> defineClass(
            String name,
            byte[] b, int off, int len,
            ClassLoader classLoader,
            ProtectionDomain domain
    );
}
