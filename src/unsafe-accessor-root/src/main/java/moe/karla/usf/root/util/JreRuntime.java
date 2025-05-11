package moe.karla.usf.root.util;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class JreRuntime {
    public static boolean JAVA_8 = !hasClass("java.lang.Module");
    public static boolean LEGACY_UNSAFE_AVAILABLE = hasClass("sun.misc.Unsafe");


    private static boolean hasClass(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
}
