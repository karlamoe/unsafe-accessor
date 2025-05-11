package moe.karla.usf.root;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
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

    @Test
    public void testAllocateObject() {
        Assertions.assertInstanceOf(Object.class, RootAccess.allocateObject(Object.class));
        Assertions.assertInstanceOf(Void.class, RootAccess.allocateObject(Void.class));
        Assertions.assertInstanceOf(System.class, RootAccess.allocateObject(System.class));
    }

    @Test
    public void testGetTrustedLookup() {
        RootAccess.getTrustedLookupIn(AccessibleObject.class);
    }

    @Test
    public void testGetPrivateLookup() {
        MethodHandles.Lookup lookup = RootAccess.getPrivateLookup(System.class);
        Assertions.assertSame(System.class, lookup.lookupClass());
        Assertions.assertEquals(MethodHandles.lookup().lookupModes(), lookup.lookupModes());
    }
}
