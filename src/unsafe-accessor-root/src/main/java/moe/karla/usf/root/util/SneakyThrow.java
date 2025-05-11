package moe.karla.usf.root.util;

import org.jetbrains.annotations.Contract;

public class SneakyThrow {
    @SuppressWarnings("unchecked")
    private static <T extends Throwable> RuntimeException throw0(Throwable throwable) throws T {
        throw (T) throwable;
    }

    @Contract("_ -> fail")
    @SuppressWarnings("RedundantTypeArguments")
    public static RuntimeException t(Throwable throwable) {
        throw SneakyThrow.<RuntimeException>throw0(throwable);
    }
}
