package moe.karla.usf.unsafe.j9;

import com.google.auto.service.AutoService;
import moe.karla.usf.unsafe.Unsafe;
import moe.karla.usf.unsafe.impl.UnsafeProvider;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@AutoService(UnsafeProvider.class)
public class Unsafe9Provider implements UnsafeProvider {
    @Override
    public Unsafe initialize() throws Throwable {
        ModelCracker.doCrack();
        Class<?> target = Class.forName("moe.karla.usf.unsafe.j9.Unsafe9Loader");

        return (Unsafe) MethodHandles.lookup().findStatic(target, "load", MethodType.methodType(Unsafe.class)).invoke();
    }

    @Override
    public int priority() {
        return 1;
    }
}
