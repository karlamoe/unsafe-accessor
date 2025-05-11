package moe.karla.usf.unsafe.j9;

import moe.karla.usf.root.RootAccess;
import moe.karla.usf.unsafe.UnsafeInitializer;

import java.lang.invoke.MethodType;

public class ModelCracker {
    static void doCrack() throws Throwable {
        UnsafeInitializer.validate();
        if (Object.class.getModule().isOpen("jdk.internal.misc", ModelCracker.class.getModule())) return;

        ModuleLayer.Controller controller = (ModuleLayer.Controller) RootAccess.getPrivateLookup(ModuleLayer.Controller.class)
                .findConstructor(ModuleLayer.Controller.class, MethodType.methodType(void.class, ModuleLayer.class))
                .invoke(Object.class.getModule().getLayer());

        controller.addOpens(Object.class.getModule(), "jdk.internal.misc", ModelCracker.class.getModule());
        assert Object.class.getModule().isOpen("jdk.internal.misc", ModelCracker.class.getModule());
    }
}
