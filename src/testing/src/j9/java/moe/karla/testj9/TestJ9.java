package moe.karla.testj9;

import moe.karla.testj9.tasks.ModuleInfoValidate;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

public class TestJ9 extends AbstractTestDescriptor {
    public TestJ9(UniqueId uniqueId, String displayName) {
        super(uniqueId, displayName);

        addChild(new ModuleInfoValidate(
                uniqueId.append("j9", "module-info"),
                "module-info.class validate"
        ));
    }

    @Override
    public Type getType() {
        return Type.CONTAINER_AND_TEST;
    }

    @Override
    public boolean mayRegisterTests() {
        return true;
    }
}
