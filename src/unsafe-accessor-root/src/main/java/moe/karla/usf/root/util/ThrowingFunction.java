package moe.karla.usf.root.util;

public interface ThrowingFunction<P, R> {
    R apply(P p) throws Throwable;
}
