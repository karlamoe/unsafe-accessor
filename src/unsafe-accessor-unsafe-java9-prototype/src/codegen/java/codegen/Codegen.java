package codegen;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.classfile.*;
import java.lang.classfile.attribute.SignatureAttribute;
import java.lang.classfile.instruction.InvokeInstruction;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Modifier;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class Codegen {
    private static Path output(String path) throws IOException {
        var base = Path.of("build/generated/codegen");
        var result = base.resolve(path);
        if (result.getParent() != null) {
            Files.createDirectories(result.getParent());
        }
        return result;
    }

    public static void main(String[] args) throws Throwable {
        var result = Path.of("build/generated/codegen");
        if (Files.exists(result)) {
            Files.walkFileTree(result, new SimpleFileVisitor<>() {
                @Override
                public @NotNull FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public @NotNull FileVisitResult postVisitDirectory(@NotNull Path dir, @Nullable IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        Files.createDirectories(result);

        var classAbs = ClassDesc.of("moe.karla.usf.unsafe.j9.Unsafe9Abs");
        var classUsf = ClassDesc.of("jdk.internal.misc.Unsafe");
        var classMoeUsf = ClassDesc.of("moe.karla.usf.unsafe.Unsafe");

        var cf = ClassFile.of();
        var model = cf.parse(Codegen.class.getResourceAsStream("/moe/karla/usf/unsafe/Unsafe.class").readAllBytes());

        var template = cf.build(
                ClassDesc.of("moe.karla.usf.unsafe.j9.Unsafe9Template"),
                classBuilder -> {

                    classBuilder.withVersion(ClassFile.JAVA_8_VERSION, 0);
                    classBuilder.withFlags(AccessFlag.PUBLIC);
                    classBuilder.withSuperclass(classAbs);

                    classBuilder.withMethod("<init>", MethodTypeDesc.ofDescriptor("()V"), Modifier.PUBLIC, mh -> {
                        mh.withCode(code -> {
                            code.aload(0);
                            code.invokespecial(classAbs, "<init>", MethodTypeDesc.ofDescriptor("()V"), false);
                            code.return_();
                        });
                    });


                    for (ClassElement elm : model) {
                        if (!(elm instanceof MethodModel met)) continue;
                        if (!met.flags().has(AccessFlag.ABSTRACT)) continue;
                        if (met.flags().has(AccessFlag.STATIC)) continue;
                        if ("getOriginalUnsafe".equals(met.methodName().stringValue())) continue;
                        if ("isJava9".equals(met.methodName().stringValue())) continue;
                        if ("arrayBaseOffset".equals(met.methodName().stringValue())) continue;

                        classBuilder.withMethod(met.methodName(), met.methodType(), Modifier.PUBLIC, mh -> {
                            mh.withCode(code -> {
                                code.getstatic(classAbs, "usf", classUsf);
                                var descriptor = MethodTypeDesc.ofDescriptor(met.methodType().stringValue());
                                var slotCounter = new AtomicInteger(1);
                                descriptor.parameterList().forEach(param -> {
                                    var type = TypeKind.from(param);
                                    code.loadLocal(type, slotCounter.getAndAdd(type.slotSize()));
                                });
                                code.invokevirtual(classUsf, met.methodName().stringValue(), descriptor);
                                code.return_(TypeKind.from(descriptor.returnType()));
                            });
                        });
                    }
                });

        Files.write(
                output("moe/karla/usf/unsafe/j9/Unsafe9Ref.class"),
                cf.transformClass(cf.parse(template), ClassDesc.of("moe.karla.usf.unsafe.j9.Unsafe9Ref"), ClassTransform.transformingMethodBodies((code, elm) -> {
                    if (elm instanceof InvokeInstruction invoke && invoke.opcode() == Opcode.INVOKEVIRTUAL) {
                        code.invokevirtual(
                                invoke.owner().asSymbol(),
                                invoke.method().name().stringValue().replace("Object", "Reference"),
                                MethodTypeDesc.ofDescriptor(invoke.method().type().toString())
                        );
                    } else {
                        code.with(elm);
                    }
                }))
        );
        Files.write(
                output("moe/karla/usf/unsafe/j9/Unsafe9Obj.class"),
                cf.transformClass(cf.parse(template), ClassDesc.of("moe.karla.usf.unsafe.j9.Unsafe9Obj"), ClassTransform.transformingMethodBodies((code, elm) -> {
                    if (elm instanceof InvokeInstruction invoke && invoke.opcode() == Opcode.INVOKEVIRTUAL) {
                        code.invokevirtual(
                                invoke.owner().asSymbol(),
                                invoke.method().name().stringValue().replace("Reference", "Object"),
                                MethodTypeDesc.ofDescriptor(invoke.method().type().toString())
                        );
                    } else {
                        code.with(elm);
                    }
                }))
        );

        var classDelegate = ClassDesc.of("moe.karla.usf.unsafe.j9.DelegatingUnsafe");

        cf.buildTo(
                output("moe/karla/usf/unsafe/j9/DelegatingUnsafe.class"),
                classDelegate,
                classBuilder -> {
                    classBuilder.withVersion(ClassFile.JAVA_8_VERSION, 0);
                    classBuilder.withSuperclass(classMoeUsf);
                    classBuilder.withFlags(AccessFlag.PUBLIC);
                    classBuilder.withField("delegate", classMoeUsf, Modifier.PROTECTED | Modifier.FINAL);
                    classBuilder.withMethod("<init>", MethodTypeDesc.of(ClassDesc.ofDescriptor("V"), classMoeUsf), Modifier.PROTECTED, method -> {
                        method.withCode(code -> {
                            code.aload(0);
                            code.invokespecial(classMoeUsf, "<init>", MethodTypeDesc.ofDescriptor("()V"), false);
                            code.aload(0);
                            code.aload(1);
                            code.putfield(classDelegate, "delegate", classMoeUsf);
                            code.return_();
                        });
                    });

                    for (ClassElement elm : model) {
                        if (!(elm instanceof MethodModel met)) continue;
                        if (met.flags().has(AccessFlag.STATIC)) continue;
                        if (met.flags().has(AccessFlag.SYNTHETIC)) continue;
                        if ("<init>".equals(met.methodName().stringValue())) continue;

                        classBuilder.withMethod(met.methodName(), met.methodType(), Modifier.PUBLIC, mh -> {
                            met.elementStream().filter(it -> it instanceof SignatureAttribute).forEach(mh::with);

                            mh.withCode(code -> {
                                code.aload(0).getfield(classDelegate, "delegate", classMoeUsf);

                                var descriptor = MethodTypeDesc.ofDescriptor(met.methodType().stringValue());
                                var slotCounter = new AtomicInteger(1);
                                descriptor.parameterList().forEach(param -> {
                                    var type = TypeKind.from(param);
                                    code.loadLocal(type, slotCounter.getAndAdd(type.slotSize()));
                                });
                                code.invokevirtual(classMoeUsf, met.methodName().stringValue(), descriptor);
                                code.return_(TypeKind.from(descriptor.returnType()));
                            });
                        });
                    }
                }
        );
    }
}
