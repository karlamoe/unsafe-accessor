package moe.karla.usf.root.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Supplier;

public class ThrowingHandle {
    private static final MethodHandle MH_ctr_ExceptionInInitializerError;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MH_ctr_ExceptionInInitializerError = lookup.findConstructor(
                    ExceptionInInitializerError.class,
                    MethodType.methodType(void.class, Throwable.class)
            );
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static MethodHandle makeThrow(Throwable error, MethodType methodType) {

        return MethodHandles.dropArguments(
                MethodHandles.filterArguments(
                        MethodHandles.throwException(methodType.returnType(), ExceptionInInitializerError.class),
                        0,
                        MH_ctr_ExceptionInInitializerError
                ).bindTo(error),
                0,
                methodType.parameterList()
        );

    }

    public static <T> Supplier<T> makeSupplier(Throwable error) {
        return () -> {
            throw new ExceptionInInitializerError(error);
        };
    }
}
