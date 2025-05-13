package moe.karla.testj9.tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.EngineExecutionContext;
import org.junit.platform.engine.support.hierarchical.Node;

import java.lang.module.ModuleDescriptor;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModuleInfoValidate extends AbstractTestDescriptor implements Node<EngineExecutionContext> {
    public ModuleInfoValidate(UniqueId uniqueId, String displayName) {
        super(uniqueId, displayName);
    }

    @Override
    public Type getType() {
        return Type.TEST;
    }

    @Override
    public boolean mayRegisterTests() {
        return true;
    }

    @Override
    public EngineExecutionContext execute(EngineExecutionContext context, DynamicTestExecutor dynamicTestExecutor) throws Exception {
        System.out.println("Validating module-info valid");
        List<URL> resources = ModuleInfoValidate.class.getClassLoader().resources("module-info.class")
                .filter(it -> !it.getProtocol().equals("jrt"))
                .collect(Collectors.toList());
        List<ModuleDescriptor> descriptors = new ArrayList<>();
        for (URL resource : resources) {
            ModuleDescriptor descriptor = ModuleDescriptor.read(resource.openStream());
            if (descriptor.name().startsWith("moe.karla.unsafe")) {
                System.out.println(descriptor);
                descriptors.add(descriptor);

                for (ModuleDescriptor.Provides provides : descriptor.provides()) {
                    Class.forName(provides.service());
                    for (String provider : provides.providers()) {
                        Class.forName(provider);
                    }
                }
                for (String use : descriptor.uses()) {
                    Class.forName(use);
                }


            }
        }

        for (ModuleDescriptor descriptor : descriptors) {
            for (ModuleDescriptor.Requires require : descriptor.requires()) {
                if ("java.base".equals(require.name())) continue;
                if ("jdk.unsupported".equals(require.name())) continue;

                Optional<ModuleDescriptor> req = descriptors.stream().filter(it -> it.name().equals(require.name())).findFirst();
                Assertions.assertTrue(req.isPresent(), require.name() + " not found while validating " + descriptor.name());
            }

            for (ModuleDescriptor.Provides provide : descriptor.provides()) {
                boolean hasService = descriptors.stream()
                        .flatMap(it -> it.uses().stream())
                        .anyMatch(it -> it.equals(provide.service()));
                Assertions.assertTrue(hasService, "Service " + provide.service() + " not found while validating " + descriptor.name());
            }
        }

        return context;
    }
}
