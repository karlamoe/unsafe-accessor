package moe.karla.usf.unsafe.j9;

import moe.karla.usf.unsafe.Unsafe;


class Unsafe9Loader {
    static Unsafe load() throws Throwable {
        return new UnsafeDynamic();
    }
}
