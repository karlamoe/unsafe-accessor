package moe.karla.unsafe.testing;

import moe.karla.usf.root.RootAccess;
import moe.karla.usf.unsafe.Unsafe;

import java.lang.invoke.MethodHandles;

public class TestingRun {
    public static void main(String[] args) {
        MethodHandles.Lookup trustedLookup = RootAccess.getTrustedLookup();
        Unsafe unsafe = Unsafe.getUnsafe();

        System.out.println("Unsafe: " + unsafe);
        System.out.println("Unsafe.original: " + unsafe.getOriginalUnsafe());
        System.out.println("TrustedLookup: " + trustedLookup);
    }
}
