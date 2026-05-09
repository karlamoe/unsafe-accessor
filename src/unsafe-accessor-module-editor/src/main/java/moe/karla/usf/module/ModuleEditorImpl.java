package moe.karla.usf.module;

import moe.karla.usf.root.RootAccess;
import moe.karla.usf.root.util.RunCatching;
import moe.karla.usf.root.util.SneakyThrow;
import moe.karla.usf.root.util.ThrowingHandle;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

class ModuleEditorImpl extends ModuleEditor {
    private static final MethodHandle MH_Class$getModule;
    private static final MethodHandle MH_Module$getLayer;
    private static final MethodHandle MH_Module$isNamed;
    private static final MethodHandle MH_Controller$addReads;
    private static final MethodHandle MH_Controller$addExports;
    private static final MethodHandle MH_Controller$addOpens;
    private static final MethodHandle MH_Controller$new;
    private static final MethodHandle MH_Module$enableNativeAccess;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            Class<?> moduleClass = Object.class;
            Class<?> layerClass = Object.class;
            Class<?> layerControllerClass = Object.class;
            try {
                moduleClass = Class.forName("java.lang.Module");
                layerClass = Class.forName("java.lang.ModuleLayer");
                layerControllerClass = Class.forName("java.lang.ModuleLayer$Controller");
            } catch (Throwable ignored) {
            }

            Class<?> finalModuleClass = moduleClass;
            MH_Class$getModule = RunCatching.run(() -> {
                return lookup.findVirtual(Class.class, "getModule", MethodType.methodType(finalModuleClass));
            }).recover(err -> {
                return ThrowingHandle.makeThrow(err, MethodType.methodType(finalModuleClass, Class.class));
            }).getOrThrow();

            MH_Module$isNamed = RunCatching.run(() -> {
                return lookup.findVirtual(finalModuleClass, "isNamed", MethodType.methodType(boolean.class));
            }).recover(err -> {
                return ThrowingHandle.makeThrow(err, MethodType.methodType(boolean.class, finalModuleClass));
            }).getOrThrow();


            Class<?> finalLayerControllerClass = layerControllerClass;

            MH_Controller$addReads = RunCatching.run(() -> {
                return lookup.findVirtual(finalLayerControllerClass, "addReads", MethodType.methodType(finalLayerControllerClass, finalModuleClass, finalModuleClass));
            }).recover(err -> {
                return ThrowingHandle.makeThrow(err, MethodType.methodType(Object.class, Object.class, Object.class));
            }).getOrThrow();


            MH_Controller$addExports = RunCatching.run(() -> {
                return lookup.findVirtual(finalLayerControllerClass, "addExports", MethodType.methodType(finalLayerControllerClass, finalModuleClass, String.class, finalModuleClass));
            }).recover(err -> {
                return ThrowingHandle.makeThrow(err, MethodType.methodType(Object.class, Object.class, Object.class, Object.class));
            }).getOrThrow();


            MH_Controller$addOpens = RunCatching.run(() -> {
                return lookup.findVirtual(finalLayerControllerClass, "addOpens", MethodType.methodType(finalLayerControllerClass, finalModuleClass, String.class, finalModuleClass));
            }).recover(err -> {
                return ThrowingHandle.makeThrow(err, MethodType.methodType(Object.class, Object.class, Object.class, Object.class));
            }).getOrThrow();


            Class<?> finalLayerClass = layerClass;
            MH_Controller$new = RunCatching.run(() -> {
                return RootAccess.getPrivateLookup(finalLayerControllerClass).findConstructor(
                        finalLayerControllerClass,
                        MethodType.methodType(void.class, finalLayerClass)
                );
            }).recover(err -> {
                return ThrowingHandle.makeThrow(err, MethodType.methodType(Object.class, Object.class));
            }).getOrThrow();

            MH_Module$getLayer = RunCatching.run(() -> {
                return lookup.findVirtual(
                        finalModuleClass,
                        "getLayer",
                        MethodType.methodType(finalLayerClass)
                );
            }).recover(err -> {
                return ThrowingHandle.makeThrow(err, MethodType.methodType(Object.class, Object.class));
            }).getOrThrow();

            boolean isNativeRestrictUnavailable = RunCatching.run(() -> {
                return finalLayerControllerClass.getDeclaredMethod("enableNativeAccess", finalModuleClass);
            }).isFailure();
            if (isNativeRestrictUnavailable) {
                // unavailable, noop

                MH_Module$enableNativeAccess = MethodHandles.dropArguments(
                        MethodHandles.constant(Object.class, null),
                        0,
                        Object.class
                ).asType(MethodType.methodType(void.class, Object.class));
            } else {
                final MethodType expectedType = MethodType.methodType(void.class, finalModuleClass);
                final MethodHandles.Lookup moduleLookup = RootAccess.getPrivateLookup(finalModuleClass);

                // region acquire the actual enableNativeAccess handle
                final MethodHandle enableNativeImpl = RunCatching.run(() -> {
                    // java.lang.Module
                    return moduleLookup.findVirtual(
                            finalModuleClass,
                            "implAddEnableNativeAccess",
                            MethodType.methodType(finalModuleClass)
                    ).asType(expectedType);
                }).recover(err -> {
                    return MethodHandles.insertArguments(moduleLookup.findSetter(
                            finalModuleClass,
                            "enableNativeAccess",
                            boolean.class
                    ), 1, true);
                }).recover(err -> {

                    // fallback via ModuleLayer$Controller

                    // (Controller,Module)V
                    MethodHandle handle = moduleLookup.findVirtual(
                            finalLayerControllerClass,
                            "enableNativeAccess",
                            MethodType.methodType(finalLayerControllerClass, finalModuleClass)
                    );

                    // (ModuleLayer,Module)V
                    handle = MethodHandles.filterArguments(
                            handle, 0, MH_Controller$new
                    );

                    // (Module,Module)V
                    handle = MethodHandles.filterArguments(
                            handle, 0, MH_Module$getLayer
                    );

                    handle = handle.asType(handle.type().changeReturnType(void.class));

                    return MethodHandles.permuteArguments(
                            handle,
                            expectedType,
                            0, 0
                    );
                }).recover(err -> {
                    return ThrowingHandle.makeThrow(err, expectedType);
                }).getOrThrow();
                // endregion

                // region construct enableNativeAccess API
                MH_Module$enableNativeAccess = RunCatching.run(() -> {
                    // moduleForNativeAccess
                    return moduleLookup.findVirtual(finalModuleClass, "moduleForNativeAccess", MethodType.methodType(finalModuleClass));
                }).map(mh_moduleForNativeAccess -> {
                    return MethodHandles.filterArguments(enableNativeImpl, 0, mh_moduleForNativeAccess);
                }).recover(() -> {
                    MethodHandle noop = MethodHandles.dropArguments(
                            MethodHandles.constant(Object.class, null),
                            0,
                            finalModuleClass
                    ).asType(MethodType.methodType(void.class, finalModuleClass));

                    Object allUnnamed = moduleLookup.findStaticGetter(finalModuleClass, "ALL_UNNAMED_MODULE", finalModuleClass).invoke();

                    MethodHandle additional = MethodHandles.guardWithTest(
                            MH_Module$isNamed,
                            noop,
                            MethodHandles.dropArguments(
                                    enableNativeImpl.bindTo(allUnnamed),
                                    0,
                                    finalModuleClass
                            )
                    );

                    return MethodHandles.foldArguments(additional, enableNativeImpl);
                }).recover(err -> {
                    return ThrowingHandle.makeThrow(err, expectedType);
                }).getOrThrow();
                // endregion
            }

        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public @NotNull Object getModule(@NotNull Class<?> klass) {
        try {
            return MH_Class$getModule.invoke(klass);
        } catch (Throwable e) {
            throw SneakyThrow.t(e);
        }
    }

    @Override
    public void addOpens(@NotNull Object module, @NotNull String pkg, @NotNull Object targetModule) {
        try {
            Object layer = MH_Module$getLayer.invoke(module);
            if (layer == null) return; // unnamed

            MH_Controller$addOpens.invoke(
                    MH_Controller$new.invoke(layer),
                    module, pkg, targetModule
            );

        } catch (Throwable e) {
            throw SneakyThrow.t(e);
        }
    }

    @Override
    public void addExports(@NotNull Object module, @NotNull String pkg, @NotNull Object targetModule) {
        try {
            Object layer = MH_Module$getLayer.invoke(module);
            if (layer == null) return; // unnamed

            MH_Controller$addExports.invoke(
                    MH_Controller$new.invoke(layer),
                    module, pkg, targetModule
            );

        } catch (Throwable e) {
            throw SneakyThrow.t(e);
        }
    }

    @Override
    public void addReads(@NotNull Object module, @NotNull Object targetModule) {
        try {
            Object layer = MH_Module$getLayer.invoke(module);
            if (layer == null) return; // unnamed

            MH_Controller$addReads.invoke(
                    MH_Controller$new.invoke(layer),
                    module, targetModule
            );

        } catch (Throwable e) {
            throw SneakyThrow.t(e);
        }
    }

    @Override
    public void enableNativeAccess(@NotNull Object module) {
        try {
            MH_Module$enableNativeAccess.invoke(module);
        } catch (Throwable e) {
            throw SneakyThrow.t(e);
        }
    }
}
