package moe.karla.usf.security;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/// Secure guard of UnsafeAccessor.
///
/// All entry methods of UnsafeAccessor will call this class.
/// This method can intercept unauthorized calls, or tracking unsafe calls.
///
/// This method doesn't offer much improvement in security.
public abstract class RootSecurity {
    // no-volatile: When RootSecurity requires it, there will always be an initialization
    // process to initialize it.
    //
    // Since initialization typically occurs only once,
    // and this field remains largely unchanged after initialization,
    // adding `volatile` to it is not very meaningful and would actually degrade
    // the library's performance.
    private static RootSecurity security;

    public static void check(RootSecurity.Type type) {
        RootSecurity instance = security;
        if (instance != null) {
            instance.checkAccess(type);
        }
    }

    public static void setSecurity(RootSecurity security) {
        synchronized (RootSecurity.class) {
            check(Type.ROOT_SECURITY_REPLACE);
            RootSecurity.security = security;
        }
    }


    public abstract void checkAccess(RootSecurity.Type type) throws SecurityException;

    protected void replaceSecurity(RootSecurity security) {
        synchronized (RootSecurity.class) {
            if (RootSecurity.security != this) {
                throw new IllegalStateException();
            }
            RootSecurity.security = security;
        }
    }


    public enum Type {
        ROOT_SECURITY(null),
        ROOT_SECURITY_REPLACE(ROOT_SECURITY),

        ROOT_ACCESS(null),
        ROOT_ACCESS_ALL(ROOT_ACCESS),
        ROOT_ACCESS_TRUSTED_LOOKUP(ROOT_ACCESS),
        ROOT_ACCESS_PRIVATE_LOOKUP(ROOT_ACCESS),
        ROOT_ACCESS_ACCESSIBLE_OBJECT(ROOT_ACCESS),
        ROOT_ACCESS_ALLOCATE_OBJECT(ROOT_ACCESS),

        UNSAFE(null),
        UNSAFE_INSTANCE(UNSAFE),
        ;

        private final Type parent;

        Type(Type parent) {
            this.parent = parent;
        }

        @Contract(pure = true)
        @Nullable
        public Type getParent() {
            return parent;
        }
    }

}
