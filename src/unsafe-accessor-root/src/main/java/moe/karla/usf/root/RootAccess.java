package moe.karla.usf.root;

import moe.karla.usf.root.util.JreRuntime;
import moe.karla.usf.root.util.LegacySunUnsafeHelper;
import moe.karla.usf.root.util.RunCatching;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

@SuppressWarnings("JavaReflectionInvocation")
public class RootAccess {
    private static final RootAccess INSTANCE = new RootAccess();
    private static final MethodHandles.Lookup IMPL_LOOKUP;

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
    }


    public static RootAccess getInstance() {
        return INSTANCE;
    }
}
