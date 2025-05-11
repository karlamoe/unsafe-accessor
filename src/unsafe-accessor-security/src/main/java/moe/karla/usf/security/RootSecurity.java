package moe.karla.usf.security;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public abstract class RootSecurity {
    private static RootSecurity security;

    public static void check(RootSecurity.Type type) {
        RootSecurity instance = security;
        if (instance != null) {
            instance.checkAccess(type);
        }
    }

    public static synchronized void setSecurity(RootSecurity security) {
        check(Type.ROOT_SECURITY_REPLACE);
        RootSecurity.security = security;
    }


    public abstract void checkAccess(RootSecurity.Type type) throws SecurityException;


    public enum Type {
        ROOT_SECURITY(null),
        ROOT_SECURITY_REPLACE(ROOT_SECURITY),

        ROOT_ACCESS(null),
        ROOT_ACCESS_ALL(ROOT_ACCESS),
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
