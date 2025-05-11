package moe.karla.usf.root.util;

import org.jetbrains.annotations.Contract;

public class SneakyThrow {
    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throw0(Throwable throwable) throws T {
        throw (T) throwable;
    }

    @Contract("_ -> fail")
    @SuppressWarnings("RedundantTypeArguments")
    public static void t(Throwable throwable) {
        SneakyThrow.<RuntimeException>throw0(throwable);
    }
}
