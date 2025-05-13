package moe.karla.usf.unsafe.impl;

import moe.karla.usf.unsafe.Unsafe;

public interface UnsafeProvider {
    Unsafe initialize() throws Throwable;

    int priority();
}
