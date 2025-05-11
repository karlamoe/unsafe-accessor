package moe.karla.usf.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RootSecurityTest {
    protected class TestSecurity extends RootSecurity {
        @Override
        public void checkAccess(Type type) throws SecurityException {
            throw new SecurityException(type.toString());
        }

        @Override
        protected void replaceSecurity(RootSecurity security) {
            super.replaceSecurity(security);
        }
    }

    @Test
    public void testSetupAndReplace() {
        TestSecurity ourSecurity = new TestSecurity();

        RootSecurity.setSecurity(ourSecurity);
        Assertions.assertEquals("ROOT_SECURITY", Assertions.assertThrowsExactly(SecurityException.class, () -> {
            RootSecurity.check(RootSecurity.Type.ROOT_SECURITY);
        }).getMessage());

        Assertions.assertEquals("ROOT_SECURITY_REPLACE", Assertions.assertThrowsExactly(SecurityException.class, () -> {
            RootSecurity.setSecurity(ourSecurity);
        }).getMessage());

        ourSecurity.replaceSecurity(null);
    }
}
