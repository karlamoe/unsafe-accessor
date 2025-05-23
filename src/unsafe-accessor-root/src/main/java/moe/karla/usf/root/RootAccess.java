package moe.karla.usf.root;

import moe.karla.usf.root.util.JreRuntime;
import moe.karla.usf.root.util.LegacySunUnsafeHelper;
import moe.karla.usf.root.util.RunCatching;
import moe.karla.usf.root.util.SneakyThrow;
import moe.karla.usf.security.RootSecurity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/// # Root Access
///
/// # <b>Please think carefully before using the methods provided by this tool.</b>
///
/// The RootAccess util provides capabilities to access java internal including
/// - Unlimited [AccessibleObject#setAccessible(boolean)] by [#accessible(AccessibleObject)],  [#access(AccessibleObject)]
/// - Direct object allocation by [#allocateObject(Class)], [#allocate(Class)]
/// - Get any private [MethodHandles.Lookup] via [#privateLookupIn(Class)], [#getPrivateLookup(Class)]
/// - Get the highest access level [MethodHandles.Lookup] via [#getTrustedLookup()], [#trustedLookup()]
///
/// ## API Note
///
/// Each function provides static and non-static method variants.
/// The difference is that static methods will perform an additional permission check.
@SuppressWarnings({"JavaReflectionInvocation", "DefaultAnnotationParam", "UnusedReturnValue", "RedundantTypeArguments"})
public class RootAccess {
    private static final RootAccess INSTANCE = new RootAccess();
    private static final MethodHandles.Lookup IMPL_LOOKUP;
    private static final MethodHandle MH_SET_ACCESSIBLE;
    private static final MethodHandle MH_ALLOCATE_OBJECT;
    private static final MethodHandle MH_PRIVATE_LOOKUP_IN;
    private static final MethodHandle MH_TRUSTED_LOOKUP_IN;

    private static MethodHandles.Lookup getTrustedFromField() throws Exception {
        Field f = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
        f.setAccessible(true);
        return (MethodHandles.Lookup) f.get(null);
    }

    static {
        IMPL_LOOKUP = RunCatching.<MethodHandles.Lookup>run(() -> {
            if (!JreRuntime.LEGACY_UNSAFE_AVAILABLE || JreRuntime.JAVA_8) {
                return null;
            }

            Class<?> ModuleLayer$Controller = Class.forName("java.lang.ModuleLayer$Controller");
            Class<?> ClassModule = Class.forName("java.lang.Module");
            sun.misc.Unsafe unsafe = LegacySunUnsafeHelper.getLegacyUnsafe();
            Object layerController = unsafe.allocateInstance(ModuleLayer$Controller);


            Object moduleJavaBase = Class.class.getMethod("getModule").invoke(Object.class);
            Object moduleRootAccess = Class.class.getMethod("getModule").invoke(RootAccess.class);

            Object layerJavaBase = moduleJavaBase.getClass().getMethod("getLayer").invoke(moduleJavaBase);
            if (layerJavaBase != null) {
                Field controllerLayerField = ModuleLayer$Controller.getDeclaredField("layer");
                unsafe.putObject(layerController, unsafe.objectFieldOffset(controllerLayerField), layerJavaBase);
            }

            ModuleLayer$Controller.getMethod("addOpens", ClassModule, String.class, ClassModule)
                    .invoke(layerController, moduleJavaBase, "java.lang.invoke", moduleRootAccess);


            return getTrustedFromField();
        }).recover(RootAccess::getTrustedFromField).getOrThrow();
        MH_PRIVATE_LOOKUP_IN = RunCatching.run(() -> {
            if (IMPL_LOOKUP.lookupClass() == MethodHandle.class) {
                // Open j9
                return IMPL_LOOKUP.findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class));
            } else {
                return MethodHandles.lookup()
                        .findVirtual(MethodHandles.Lookup.class, "in", MethodType.methodType(MethodHandles.Lookup.class, Class.class))
                        .bindTo(IMPL_LOOKUP);
            }
        }).getOrThrow();
        MH_TRUSTED_LOOKUP_IN = RunCatching.run(() -> {
            if (IMPL_LOOKUP.lookupClass() == MethodHandle.class) {
                // Open j9
                return MH_PRIVATE_LOOKUP_IN;
            } else {
                return MethodHandles.dropArguments(MethodHandles.constant(MethodHandles.Lookup.class, IMPL_LOOKUP), 0, Class.class);
            }
        }).getOrThrow();

        MH_SET_ACCESSIBLE = RunCatching.run(() -> {
            return INSTANCE.privateLookupIn(AccessibleObject.class).findVirtual(AccessibleObject.class, "setAccessible", MethodType.methodType(void.class, boolean.class));
        }).getOrThrow();

        MH_ALLOCATE_OBJECT = RunCatching.run(() -> {
//            jdk.internal.misc.Unsafe.getUnsafe().allocateInstance(Object.class);
            Class<?> jdkUnsafe = Class.forName("jdk.internal.misc.Unsafe");
            MethodHandle mhAllocate = IMPL_LOOKUP.findVirtual(
                    jdkUnsafe, "allocateInstance", MethodType.methodType(Object.class, Class.class)
            );

            return mhAllocate.bindTo(
                    IMPL_LOOKUP.findStatic(jdkUnsafe, "getUnsafe", MethodType.methodType(jdkUnsafe)).invoke()
            );
        }).recover(() -> {
//            sun.misc.Unsafe.getUnsafe().allocateInstance(Object.class);
            Class<?> jdkUnsafe = Class.forName("sun.misc.Unsafe");
            MethodHandle mhAllocate = IMPL_LOOKUP.findVirtual(
                    jdkUnsafe, "allocateInstance", MethodType.methodType(Object.class, Class.class)
            );

            return mhAllocate.bindTo(LegacySunUnsafeHelper.getLegacyUnsafe());
        }).getOrThrow();
    }


    @Contract(pure = false)
    public static RootAccess getInstance() {
        RootSecurity.check(RootSecurity.Type.ROOT_ACCESS_ALL);
        return INSTANCE;
    }

    //region TrustedLookup
    public static @NotNull MethodHandles.Lookup getTrustedLookup() {
        RootSecurity.check(RootSecurity.Type.ROOT_ACCESS_TRUSTED_LOOKUP);
        return IMPL_LOOKUP;
    }

    public static @NotNull MethodHandles.Lookup getTrustedLookupIn(Class<?> target) {
        RootSecurity.check(RootSecurity.Type.ROOT_ACCESS_TRUSTED_LOOKUP);
        return INSTANCE.trustedLookupIn(target);
    }

    public static @NotNull MethodHandles.Lookup getPrivateLookup(Class<?> target) {
        RootSecurity.check(RootSecurity.Type.ROOT_ACCESS_PRIVATE_LOOKUP);
        return INSTANCE.privateLookupIn(target);
    }

    @Contract(pure = true)
    public @NotNull MethodHandles.Lookup trustedLookup() {
        return IMPL_LOOKUP;
    }

    @Contract(pure = true)
    public @NotNull MethodHandles.Lookup trustedLookupIn(Class<?> target) {
        try {
            return (MethodHandles.Lookup) MH_TRUSTED_LOOKUP_IN.invokeExact(target);
        } catch (Throwable t) {
            throw SneakyThrow.t(t);
        }
    }

    @Contract(pure = true)
    public @NotNull MethodHandles.Lookup privateLookupIn(Class<?> target) {
        try {
            return (MethodHandles.Lookup) MH_PRIVATE_LOOKUP_IN.invokeExact(target);
        } catch (Throwable t) {
            throw SneakyThrow.t(t);
        }
    }
    //endregion

    //region AccessibleObject.setAccessible
    public static <T extends AccessibleObject> T accessible(T target) {
        if (target == null) throw new IllegalArgumentException("target is null");
        RootSecurity.check(RootSecurity.Type.ROOT_ACCESS_ACCESSIBLE_OBJECT);
        INSTANCE.access(target);
        return target;
    }

    public <T extends AccessibleObject> T access(T target) {
        if (target == null) throw new IllegalArgumentException("target is null");
        try {
            MH_SET_ACCESSIBLE.invokeExact((AccessibleObject) target, true);
        } catch (Throwable t) {
            SneakyThrow.t(t);
        }
        return target;
    }
    //endregion

    //region Unsafe.allocateObject()
    public static <T> T allocateObject(Class<T> klass) {
        if (klass == null) throw new IllegalArgumentException("klass is null");
        RootSecurity.check(RootSecurity.Type.ROOT_ACCESS_ALLOCATE_OBJECT);

        return INSTANCE.allocate(klass);
    }

    @SuppressWarnings("unchecked")
    public <T> T allocate(Class<T> klass) {
        if (klass == null) throw new IllegalArgumentException("klass is null");

        try {
            return (T) (Object) MH_ALLOCATE_OBJECT.invokeExact(klass);
        } catch (Throwable t) {
            throw SneakyThrow.t(t);
        }
    }
    //endregion
}
