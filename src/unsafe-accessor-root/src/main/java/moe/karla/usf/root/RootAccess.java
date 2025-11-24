package moe.karla.usf.root;

import moe.karla.usf.root.util.*;
import moe.karla.usf.security.RootSecurity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

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
    private static final Supplier<MethodHandles.Lookup> IMPL_LOOKUP;
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
        RunCatching<MethodHandles.Lookup> implLookup = RunCatching.<MethodHandles.Lookup>run(() -> {
            if (!JreRuntime.LEGACY_UNSAFE_AVAILABLE || JreRuntime.JAVA_8) {
                return null;
            }

            try {
                return getTrustedFromField();
            } catch (Throwable ignored) {
            }

            Class<?> ClassModule = Class.forName("java.lang.Module");


            Object moduleJavaBase = Class.class.getMethod("getModule").invoke(Object.class);
            Object moduleRootAccess = Class.class.getMethod("getModule").invoke(RootAccess.class);

            sun.misc.Unsafe unsafe = LegacySunUnsafeHelper.getLegacyUnsafe();
            try {
                Class<?> ModuleLayer$Controller = Class.forName("java.lang.ModuleLayer$Controller");
                Object layerController = unsafe.allocateInstance(ModuleLayer$Controller);

                Object layerJavaBase = moduleJavaBase.getClass().getMethod("getLayer").invoke(moduleJavaBase);
                if (layerJavaBase != null) {
                    Field controllerLayerField = ModuleLayer$Controller.getDeclaredField("layer");
                    unsafe.putObject(layerController, unsafe.objectFieldOffset(controllerLayerField), layerJavaBase);
                }

                ModuleLayer$Controller.getMethod("addOpens", ClassModule, String.class, ClassModule)
                        .invoke(layerController, moduleJavaBase, "java.lang.invoke", moduleRootAccess);
            } catch (Throwable errController) {
                try {
                    Object instrumentation = unsafe.allocateInstance(Class.forName("sun.instrument.InstrumentationImpl"));
                    Class.forName("java.lang.instrument.Instrumentation").getMethod(
                            "redefineModule", ClassModule, Set.class, Map.class, Map.class, Set.class, Map.class
                    ).invoke(instrumentation,
                            /* module */  moduleJavaBase,
                            /* extraReads */  Collections.emptySet(),
                            /* extraExports */ Collections.emptyMap(),
                            /* extraOpens */  Collections.singletonMap("java.lang.invoke", Collections.singleton(moduleRootAccess)),
                            /* extraUses */ Collections.emptySet(),
                            /* extraProvides */ Collections.emptyMap()
                    );
                } catch (Throwable errInstrument) {
                    errController.addSuppressed(errInstrument);
                    throw errController;
                }
            }


            return getTrustedFromField();
        }).recover(RootAccess::getTrustedFromField);

        if (implLookup.exceptionOrNull() != null) {
            IMPL_LOOKUP = ThrowingHandle.makeSupplier(implLookup.exceptionOrNull());
        } else {
            MethodHandles.Lookup lookup = implLookup.getOrThrow();
            IMPL_LOOKUP = () -> lookup;
        }

        MH_PRIVATE_LOOKUP_IN = RunCatching.run(() -> {
            MethodHandles.Lookup lookup = IMPL_LOOKUP.get();
            if (lookup.lookupClass() == MethodHandle.class) {
                // Open j9
                return lookup.findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class));
            } else {
                return MethodHandles.lookup()
                        .findVirtual(MethodHandles.Lookup.class, "in", MethodType.methodType(MethodHandles.Lookup.class, Class.class))
                        .bindTo(lookup);
            }
        }).recover(err -> {
            return ThrowingHandle.makeThrow(err, MethodType.methodType(MethodHandles.Lookup.class, Class.class));
        }).getOrThrow();
        MH_TRUSTED_LOOKUP_IN = RunCatching.run(() -> {
            MethodHandles.Lookup lookup = IMPL_LOOKUP.get();
            if (lookup.lookupClass() == MethodHandle.class) {
                // Open j9
                return MH_PRIVATE_LOOKUP_IN;
            } else {
                return MethodHandles.dropArguments(MethodHandles.constant(MethodHandles.Lookup.class, lookup), 0, Class.class);
            }
        }).recover(err -> {
            return ThrowingHandle.makeThrow(err, MethodType.methodType(MethodHandles.Lookup.class, Class.class));
        }).getOrThrow();

        MH_SET_ACCESSIBLE = RunCatching.run(() -> {
            return INSTANCE.privateLookupIn(AccessibleObject.class).findVirtual(AccessibleObject.class, "setAccessible", MethodType.methodType(void.class, boolean.class));
        }).recover(err -> {
            return ThrowingHandle.makeThrow(err, MethodType.methodType(void.class, AccessibleObject.class, boolean.class));
        }).getOrThrow();

        MH_ALLOCATE_OBJECT = RunCatching.run(() -> {
            MethodHandles.Lookup lookup = IMPL_LOOKUP.get();

//            jdk.internal.misc.Unsafe.getUnsafe().allocateInstance(Object.class);
            Class<?> jdkUnsafe = Class.forName("jdk.internal.misc.Unsafe");
            MethodHandle mhAllocate = lookup.findVirtual(
                    jdkUnsafe, "allocateInstance", MethodType.methodType(Object.class, Class.class)
            );

            return mhAllocate.bindTo(
                    lookup.findStatic(jdkUnsafe, "getUnsafe", MethodType.methodType(jdkUnsafe)).invoke()
            );
        }).recover(() -> {
            MethodHandles.Lookup lookup = IMPL_LOOKUP.get();

//            sun.misc.Unsafe.getUnsafe().allocateInstance(Object.class);
            Class<?> jdkUnsafe = Class.forName("sun.misc.Unsafe");
            MethodHandle mhAllocate = lookup.findVirtual(
                    jdkUnsafe, "allocateInstance", MethodType.methodType(Object.class, Class.class)
            );

            return mhAllocate.bindTo(LegacySunUnsafeHelper.getLegacyUnsafe());
        }).recover(err -> {
            return ThrowingHandle.makeThrow(err, MethodType.methodType(Object.class, Class.class));
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
        return IMPL_LOOKUP.get();
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
        return IMPL_LOOKUP.get();
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
