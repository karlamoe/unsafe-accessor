package moe.karla.test;

import com.google.auto.service.AutoService;
import org.junit.platform.engine.FilterResult;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.launcher.PostDiscoveryFilter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AutoService(PostDiscoveryFilter.class)
public class CustomPostFilter implements PostDiscoveryFilter {
    @Override
    public FilterResult apply(TestDescriptor object) {
        if (object instanceof EngineDescriptor) {
            EngineDescriptor engineDescriptor = (EngineDescriptor) object;
            Set<? extends TestDescriptor> children = new HashSet<>(engineDescriptor.getChildren());

            children.forEach(engineDescriptor::removeChild);
            WrapperTestDescriptor wrapper = new WrapperTestDescriptor(
                    engineDescriptor.getUniqueId().appendEngine("wrapper"),
                    Optional.ofNullable(System.getenv("USF_TEST_CATEGORY")).orElse("<UNKNOWN>")
            );
            children.forEach(wrapper::addChild);

            engineDescriptor.addChild(wrapper);
        }
        return FilterResult.included("CustomPostFilter");
    }

    public static class WrapperTestDescriptor extends AbstractTestDescriptor {
        public WrapperTestDescriptor(UniqueId uniqueId, String displayName) {
            super(uniqueId, displayName);
        }

        @Override
        public Type getType() {
            return Type.CONTAINER;
        }

        @Override
        public boolean mayRegisterTests() {
            return true;
        }
    }
}
