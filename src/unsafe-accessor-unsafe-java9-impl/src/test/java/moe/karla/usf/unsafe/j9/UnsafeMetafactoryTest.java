package moe.karla.usf.unsafe.j9;

import moe.karla.usf.unsafe.Unsafe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@EnabledForJreRange(min = JRE.JAVA_9)
class UnsafeMetafactoryTest {
    @Test
    void validateAllMethodExists() throws Throwable {
        Unsafe.getUnsafe();

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        for (Method method : Unsafe.class.getMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (method.getName().equals("invokeCleaner")) continue;
            if (!Modifier.isAbstract(method.getModifiers())) continue;

            UnsafeMetafactory.bootstrap(lookup, method.getName(), MethodType.methodType(
                    method.getReturnType(), method.getParameterTypes()
            ));
        }
    }
}