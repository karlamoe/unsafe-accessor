package moe.karla.unsafe.testing;

import moe.karla.usf.definer.ClassDefiner;
import moe.karla.usf.module.ModuleEditor;
import moe.karla.usf.root.RootAccess;
import moe.karla.usf.unsafe.Unsafe;
import org.junit.jupiter.api.Assertions;

import java.lang.invoke.MethodHandles;

public class TestingRun {
    public static void main(String[] args) {
        MethodHandles.Lookup trustedLookup = RootAccess.getTrustedLookup();
        Unsafe unsafe = Unsafe.getUnsafe();

        System.out.println("Unsafe: " + unsafe);
        System.out.println("Unsafe.original: " + unsafe.getOriginalUnsafe());
        System.out.println("TrustedLookup: " + trustedLookup);

        System.out.println(RootAccess.class.getModule());
        System.out.println(Unsafe.class.getModule());
        System.out.println(ModuleEditor.class.getModule());
        System.out.println(ClassDefiner.class.getModule());

        Assertions.assertEquals(
                "moe.karla.unsafe.root", RootAccess.class.getModule().getName()
        );
        Assertions.assertEquals(
                "moe.karla.unsafe.unsafe", Unsafe.class.getModule().getName()
        );
        Assertions.assertEquals(
                "moe.karla.unsafe.module.editor", ModuleEditor.class.getModule().getName()
        );
        Assertions.assertEquals(
                "moe.karla.unsafe.definer", ClassDefiner.class.getModule().getName()
        );
    }
}
