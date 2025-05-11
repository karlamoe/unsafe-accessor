package moe.karla.usf.unsafe;

public interface UnsafeProvider {
    Unsafe initialize() throws Throwable;

    int priority();
}
