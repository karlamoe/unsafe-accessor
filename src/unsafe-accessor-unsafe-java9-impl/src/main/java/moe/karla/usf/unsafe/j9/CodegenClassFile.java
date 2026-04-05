package moe.karla.usf.unsafe.j9;

import moe.karla.usf.unsafe.Unsafe;

import java.lang.classfile.Annotation;
import java.lang.classfile.ClassFile;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.TypeKind;
import java.lang.classfile.attribute.RuntimeVisibleAnnotationsAttribute;
import java.lang.constant.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@SuppressWarnings("all")
class CodegenClassFile {
    static void emitArguments(CodeBuilder code, Method method) {

        int slot = 1;
        for (Class<?> type : method.getParameterTypes()) {
            TypeKind kind = TypeKind.fromDescriptor(type.descriptorString());
            code.loadLocal(kind, slot);

            slot += kind.slotSize();
        }
    }

    static byte[] generate() throws Throwable {
        ClassFile cf = ClassFile.of();
        String className = "moe/karla/usf/unsafe/j9/UnsafeDynamic$$ClassFileGenerator";
        return cf.build(ClassDesc.ofInternalName(className), cb -> {
            cb.withFlags(AccessFlag.PUBLIC, AccessFlag.FINAL);
            ClassDesc superKlass = ClassDesc.ofInternalName("moe/karla/usf/unsafe/j9/Unsafe9Abs");
            cb.withSuperclass(superKlass);

            cb.withMethodBody("<init>", MethodTypeDesc.ofDescriptor("()V"), Modifier.PUBLIC, code -> {
                code.aload(0);
                code.invokespecial(superKlass, "<init>", MethodTypeDesc.ofDescriptor("()V"));
                code.return_();
            });

            for (Method method : Unsafe.class.getDeclaredMethods()) {
                if (Modifier.isStatic(method.getModifiers())) continue;
                if (!Modifier.isAbstract(method.getModifiers())) continue;


                MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
                cb.withMethod(method.getName(),
                        MethodTypeDesc.ofDescriptor(methodType.toMethodDescriptorString()),
                        Modifier.PUBLIC | Modifier.FINAL,
                        methodBuilder -> {
                            methodBuilder.with(
                                    RuntimeVisibleAnnotationsAttribute.of(
                                            Annotation.of(ClassDesc.ofInternalName("jdk/internal/vm/annotation/ForceInline"))
                                    )
                            );

                            methodBuilder.withCode(code -> {

                                MethodHandleInfo handleInfo = null;
                                try {
                                    final MethodHandles.Lookup lookup = MethodHandles.lookup();

                                    MethodHandle handle = UnsafeMetafactory.findMethod(lookup, method.getName(), methodType, false, false);
                                    if (handle != null) {
                                        handleInfo = lookup.revealDirect(handle);
                                    }
                                } catch (Throwable ignored) {
                                }

                                if (handleInfo != null && handleInfo.getReferenceKind() == MethodHandleInfo.REF_invokeVirtual && handleInfo.getDeclaringClass() == jdk.internal.misc.Unsafe.class) {
                                    ClassDesc jdkUnsafe = ClassDesc.ofInternalName("jdk/internal/misc/Unsafe");
                                    code.getstatic(superKlass, "usf", jdkUnsafe);

                                    emitArguments(code, method);

                                    code.invokevirtual(jdkUnsafe, handleInfo.getName(), MethodTypeDesc.ofDescriptor(
                                            methodType.descriptorString()
                                    ));
                                } else {
                                    emitArguments(code, method);
                                    code.invokedynamic(DynamicCallSiteDesc.of(
                                            MethodHandleDesc.ofMethod(
                                                    DirectMethodHandleDesc.Kind.STATIC,
                                                    ClassDesc.ofInternalName("moe/karla/usf/unsafe/j9/UnsafeMetafactory"),
                                                    "bootstrap",
                                                    MethodTypeDesc.ofDescriptor("(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;")
                                            ),
                                            method.getName(),
                                            MethodTypeDesc.ofDescriptor(methodType.descriptorString())
                                    ));
                                }


                                code.return_(TypeKind.fromDescriptor(method.getReturnType().descriptorString()));

                            });
                        });
            }

        });
    }
}
