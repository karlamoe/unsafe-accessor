package moe.karla.usf.root;

import moe.karla.usf.root.util.LegacySunUnsafeHelper;
import moe.karla.usf.root.util.RunCatching;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
class ModuleCracker {
    private static void tryAsSunReflectionFactory(Class<?> ClassModule, Object moduleJavaBase, Object moduleRootAccess) throws Throwable {
        Class<?> ModuleLayer = Class.forName("java.lang.ModuleLayer");
        Class<?> ModuleLayer$Controller = Class.forName("java.lang.ModuleLayer$Controller");

        Constructor<?> controller = sun.reflect.ReflectionFactory.getReflectionFactory().newConstructorForSerialization(
                ModuleLayer$Controller,
                ModuleLayer$Controller.getDeclaredConstructor(ModuleLayer)
        );
        Object layerJavaBase = moduleJavaBase.getClass().getMethod("getLayer").invoke(moduleJavaBase);

        ModuleLayer$Controller.getMethod("addOpens", ClassModule, String.class, ClassModule)
                .invoke(
                        controller.newInstance(layerJavaBase),
                        moduleJavaBase, "java.lang.invoke", moduleRootAccess
                );
    }

    static void tryAsUnsafeFieldOffset(Class<?> ClassModule, Object moduleJavaBase, Object moduleRootAccess) throws Throwable {
        sun.misc.Unsafe unsafe = LegacySunUnsafeHelper.getLegacyUnsafe();

        Class<?> ModuleLayer$Controller = Class.forName("java.lang.ModuleLayer$Controller");
        Object layerController = unsafe.allocateInstance(ModuleLayer$Controller);

        Object layerJavaBase = moduleJavaBase.getClass().getMethod("getLayer").invoke(moduleJavaBase);
        if (layerJavaBase != null) {
            Field controllerLayerField = ModuleLayer$Controller.getDeclaredField("layer");
            unsafe.putObject(layerController, unsafe.objectFieldOffset(controllerLayerField), layerJavaBase);
        }

        ModuleLayer$Controller.getMethod("addOpens", ClassModule, String.class, ClassModule)
                .invoke(layerController, moduleJavaBase, "java.lang.invoke", moduleRootAccess);
    }

    static void tryAsInstrumentation(Class<?> ClassModule, Object moduleJavaBase, Object moduleRootAccess) throws Throwable {
        sun.misc.Unsafe unsafe = LegacySunUnsafeHelper.getLegacyUnsafe();
        Object instrumentation = unsafe.allocateInstance(Class.forName("sun.instrument.InstrumentationImpl"));
        Class.forName("java.lang.instrument.Instrumentation").getMethod(
                "redefineModule", ClassModule, Set.class, Map.class, Map.class, Set.class, Map.class
        ).invoke(instrumentation,
                /* module */  moduleJavaBase,
                /* extraReads */  Collections.emptySet(),
                /* extraExports */ Collections.emptyMap(),
                /* extraOpens */  Collections.singletonMap("java.lang.invoke", Collections.singleton(moduleRootAccess)),
                /* extraUses */ Collections.emptySet(),
                /* extraProvides */ Collections.emptyMap()
        );
    }

    static void tryAsJdkProxy(Class<?> ClassModule, Object moduleJavaBase, Object moduleRootAccess) throws Throwable {

        String[] names = {
                "jdk.internal.access.JavaLangAccess",
                "jdk.internal.misc.JavaLangAccess",
        };
        Class<?> targetClass = null;
        for (String name : names) {
            try {
                targetClass = Class.forName(name);
                break;
            } catch (ClassNotFoundException ignored) {
            }
        }
        if (targetClass == null) {
            throw new IllegalStateException("Failed to find JavaLangAccess");
        }
        Class<?> SharedSecrets = Class.forName(targetClass.getName().substring(0, targetClass.getName().lastIndexOf('.') + 1) + "SharedSecrets");

        class MyCl extends ClassLoader {
            MyCl(ClassLoader parent) {
                super(parent);
            }

            Class<?> load(byte[] code) {
                return defineClass(null, code, 0, code.length);
            }
        }
        MyCl cl = new MyCl(ModuleCracker.class.getClassLoader());

        Class<?> proxyClass = java.lang.reflect.Proxy.newProxyInstance(cl, new Class[]{targetClass},
                (proxy, $$, args) -> null
        ).getClass();

        ByteArrayOutputStream classGen = new ByteArrayOutputStream();
        DataOutputStream wo = new DataOutputStream(classGen);

        byte[] template = Base64.getDecoder().decode("yv66vgAAADQAJgEACW15L29iamVjdAcAAQEAEGphdmEvbGFuZy9PYmplY3QHAAMBAAZsb29rdXABACkoKUxqYXZhL2xhbmcvaW52b2tlL01ldGhvZEhhbmRsZXMkTG9va3VwOwEAHmphdmEvbGFuZy9pbnZva2UvTWV0aG9kSGFuZGxlcwcABwwABQAGCgAIAAkBAAg8Y2xpbml0PgEAAygpVgEAD2phdmEvbGFuZy9DbGFzcwcADQEACWdldE1vZHVsZQEAFCgpTGphdmEvbGFuZy9Nb2R1bGU7DAAPABAKAA4AEQEADmdldFBhY2thZ2VOYW1lAQAUKClMamF2YS9sYW5nL1N0cmluZzsMABMAFAoADgAVAQAOZ2V0Q2xhc3NMb2FkZXIBABkoKUxqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7DAAXABgKAA4AGQEACGdldENsYXNzAQATKClMamF2YS9sYW5nL0NsYXNzOwwAGwAcCgAEAB0BABBqYXZhL2xhbmcvTW9kdWxlBwAfAQAIYWRkT3BlbnMBADgoTGphdmEvbGFuZy9TdHJpbmc7TGphdmEvbGFuZy9Nb2R1bGU7KUxqYXZhL2xhbmcvTW9kdWxlOwwAIQAiCgAgACMBAARDb2RlAAEAAgAEAAAAAAACAAkABQAGAAEAJQAAABAAAQABAAAABLgACrAAAAAAAAgACwAMAAEAJQAAACUAAwACAAAAGRICtgASEgK2ABYSArYAGrYAHrYAErYAJLEAAAAAAAA=");
        classGen.write(template, 0, 0xb);
        wo.writeUTF((proxyClass.getName() + "$$UnsafeAccessorInjected").replace('.', '/'));
        classGen.write(template, 0x16, template.length - 0x16);

//        System.out.println(Base64.getEncoder().encodeToString(classGen.toByteArray()));

        Class<?> klass = cl.load(classGen.toByteArray());
        Class.forName(klass.getName(), true, cl);

        MethodHandles.Lookup lookup = (MethodHandles.Lookup) klass.getDeclaredMethod("lookup").invoke(null);

        Object javaLangModuleAccess = lookup.findStatic(
                SharedSecrets,
                "getJavaLangAccess",
                MethodType.methodType(targetClass)
        ).invoke();
        lookup.findVirtual(
                targetClass,
                "addOpens",
                MethodType.methodType(void.class, ClassModule, String.class, ClassModule)
        ).invoke(javaLangModuleAccess, moduleJavaBase, "java.lang.invoke", moduleRootAccess);
    }

    static void doCrack() throws Throwable {

        Class<?> ClassModule = Class.forName("java.lang.Module");


        Object moduleJavaBase = Class.class.getMethod("getModule").invoke(Object.class);
        Object moduleRootAccess = Class.class.getMethod("getModule").invoke(RootAccess.class);

        RunCatching.run(() -> {
            tryAsSunReflectionFactory(ClassModule, moduleJavaBase, moduleRootAccess);
            return moduleJavaBase;
        }).recover(() -> {
            tryAsJdkProxy(ClassModule, moduleJavaBase, moduleRootAccess);
            return moduleJavaBase;
        }).recover(() -> {
            tryAsUnsafeFieldOffset(ClassModule, moduleJavaBase, moduleRootAccess);
            return moduleJavaBase;
        }).recover(() -> {
            tryAsInstrumentation(ClassModule, moduleJavaBase, moduleRootAccess);
            return moduleJavaBase;
        }).getOrThrow();

    }
}
