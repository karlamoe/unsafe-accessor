package usftest;

import moe.karla.usf.root.RootAccess;
import moe.karla.usf.unsafe.Unsafe;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.NamedExecutable;
import org.junit.jupiter.api.TestFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

public class UnsafeMethodResolvationTest {
    @TestFactory
    public Stream<? extends DynamicNode> createTestList() {
        return DynamicTest.stream(
                Stream.of(Unsafe.class.getMethods())
                        .filter(it -> it.getDeclaringClass() != Object.class)
                        .map(UnsafeMethodVerity::new)
        );
    }

    static class UnsafeMethodVerity implements NamedExecutable {
        private final Method m;

        public UnsafeMethodVerity(Method m) {
            this.m = m;
        }

        @Override
        public void execute() throws Throwable {
            Unsafe unsafeImpl = Unsafe.getUnsafe();
            doVerify(unsafeImpl.getClass(), unsafeImpl, m.getName(), MethodType.methodType(m.getReturnType(), m.getParameterTypes()), Modifier.isStatic(m.getModifiers()));
        }

        @SuppressWarnings({"DataFlowIssue"})
        private MethodNode findMethod(Class<?> type, String name, String methodType, boolean isStatic) throws Throwable {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(type.getClassLoader().getResourceAsStream(type.getName().replace('.', '/') + ".class"));
            classReader.accept(classNode, 0);

            Optional<MethodNode> targetMethod = classNode.methods.stream()
                    .filter(it -> ((it.access & Opcodes.ACC_STATIC) == 0) == !isStatic)
                    .filter(it -> it.name.equals(name))
                    .filter(it -> it.desc.equals(methodType))
                    .findAny();
            if (targetMethod.isPresent()) {
                return targetMethod.get();
            }
            Class<?> parent = type.getSuperclass();
            if (parent == null) {
                throw new NoSuchElementException("Cannot find method node of " + name + methodType);
            }
            return findMethod(parent, name, methodType, isStatic);
        }

        private void doVerify(Class<?> type, Object instance, String name, MethodType methodType, boolean isStatic) throws Throwable {
            System.out.println("Checking " + type + "." + methodType.toMethodDescriptorString());
            checkMethodAvailable:
            {
                MethodHandles.Lookup lookup = RootAccess.getTrustedLookupIn(type);
                if (isStatic) {
                    lookup.findStatic(type, name, methodType);
                } else {
                    lookup.findVirtual(type, name, methodType);
                }
            }

            if (type.getClassLoader() == Unsafe.class.getClassLoader()) {

                MethodNode targetMethod = findMethod(type, name, methodType.toMethodDescriptorString(), isStatic);

                class GetField extends BasicValue {
                    final String name;

                    public GetField(Type type, String name) {
                        super(type);
                        this.name = name;
                    }
                }

                new Analyzer<>(new BasicInterpreter(Opcodes.ASM9) {
                    @Override
                    public BasicValue unaryOperation(AbstractInsnNode insn, BasicValue value) throws AnalyzerException {
                        if (insn.getOpcode() == Opcodes.GETFIELD) {
                            FieldInsnNode in = (FieldInsnNode) insn;
                            return new GetField(Type.getType(in.desc), in.name);
                        }
                        return super.unaryOperation(insn, value);
                    }

                    @Override
                    public BasicValue naryOperation(AbstractInsnNode insn, List<? extends BasicValue> values) throws AnalyzerException {
                        if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL && values.get(0) instanceof GetField) {
                            try {
                                GetField getFieldType = (GetField) values.get(0);
                                MethodInsnNode met = (MethodInsnNode) insn;
                                Object obj = RootAccess.getPrivateLookup(type).findGetter(type, getFieldType.name,
                                        Class.forName(getFieldType.getType().getClassName())
                                ).invoke(instance);


                                doVerify(obj.getClass(), obj, met.name, MethodType.fromMethodDescriptorString(met.desc, getClass().getClassLoader()), false);
                            } catch (Throwable t) {
                                throw new RuntimeException(t);
                            }
                        }
                        return super.naryOperation(insn, values);
                    }
                }).analyze(Type.getInternalName(type), targetMethod);
            }
        }

        @Override
        public String getName() {
            return m.getName() + Type.getMethodDescriptor(m);
        }
    }
}
