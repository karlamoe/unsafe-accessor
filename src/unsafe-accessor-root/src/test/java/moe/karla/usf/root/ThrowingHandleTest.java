package moe.karla.usf.root;

import moe.karla.usf.root.util.ThrowingHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

public class ThrowingHandleTest {
    @Test
    void test() {
        Throwable test = new Throwable();
        MethodType methodType = MethodType.methodType(void.class, String.class, int[].class);
        MethodHandle mh = ThrowingHandle.makeThrow(test, methodType);

        Assertions.assertEquals(methodType, mh.type());

        ExceptionInInitializerError error = Assertions.assertThrows(ExceptionInInitializerError.class, () -> {
            mh.invoke("", null);
        });
        error.printStackTrace(System.out);

        Assertions.assertSame(test, error.getCause());

    }
}
