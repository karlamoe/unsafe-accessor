package moe.karla.usf.root;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class RootAccessTest {
    @Test
    public void getRoot() {
        RootAccess.getInstance();
    }

    @Test
    public void assertTrusted() {
        RootAccess.getTrustedLookup().in(MethodHandles.class);
    }

    @Test
    public void testAccessible() throws Throwable {
        Method testMethod = Object.class.getMethod("toString");
        Assertions.assertFalse(testMethod.isAccessible());
        RootAccess.accessible(testMethod);
        Assertions.assertTrue(testMethod.isAccessible());
    }
}
