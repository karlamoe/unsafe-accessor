package moe.karla.usf.unsafe.j9;

import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.VisibleForTesting;

import java.lang.invoke.*;
import java.util.ArrayList;
import java.util.List;

abstract class UnsafeMetafactory {
    private static final List<Resolver> RESOLVERS = new ArrayList<>();

    interface Resolver {
        MethodHandle resolve(MethodHandles.Lookup lookup, String name, MethodType type, boolean binding) throws Throwable;
    }

    static CallSite bootstrap(
            MethodHandles.Lookup lookup,
            String name, MethodType type
    ) throws Throwable {
        return new ConstantCallSite(findMethod(lookup, name, type, true, true));
    }


    @VisibleForTesting
    static @UnknownNullability MethodHandle findMethod(
            MethodHandles.Lookup lookup,
            String name, MethodType type,
            boolean failing, boolean binding
    ) throws Throwable {
        for (Resolver f : RESOLVERS) {
            MethodHandle mh = f.resolve(lookup, name, type, binding);
            if (mh != null) {
                return mh;
            }
        }
        if (failing) {
            throw new RuntimeException("Cannot resolve " + name + type);
        }
        return null;
    }


    static {

        RESOLVERS.add(UnsafeMetafactory::resolveSpecial);
        RESOLVERS.add(UnsafeMetafactory::resolveAsJdk);

        try {
            Class.forName("java.lang.invoke.MethodHandles$Lookup$ClassOption");
            RESOLVERS.add(UnsafeMetafactory::resolveAnonymousClassDefiner);
        } catch (Throwable ignored) {
        }

        try {
            jdk.internal.misc.Unsafe.class.getMethod("getReference", Object.class, long.class);
            RESOLVERS.add(UnsafeMetafactory::resolveAsJdkReference);
        } catch (Throwable ignored) {
            RESOLVERS.add(UnsafeMetafactory::resolveAsJdkObject);
        }

        try {
            MethodHandles.lookup().findVirtual(jdk.internal.misc.Unsafe.class, "arrayBaseOffset", MethodType.methodType(int.class, Class.class));
            RESOLVERS.add(UnsafeMetafactory::resolveArrayBaseOffset);
        } catch (Throwable ignored) {
        }

        RESOLVERS.add(UnsafeMetafactory::resolveNoModifier);
    }


    private static MethodHandle resolveSpecial(MethodHandles.Lookup lookup, String name, MethodType type, boolean binding) throws Throwable {
        if (name.equals("isJava9")) {
            return MethodHandles.constant(boolean.class, true);
        }
        if (name.equals("getOriginalUnsafe")) {
            return MethodHandles.constant(Object.class, Unsafe9Abs.usf);
        }
        return null;
    }

    private static MethodHandle resolveAnonymousClassDefiner(MethodHandles.Lookup lookup, String name, MethodType type, boolean binding) throws Throwable {
        if (name.equals("defineAnonymousClass")) {
            return lookup.findStatic(
                    Class.forName("moe.karla.usf.unsafe.j9.AnonymousClassDefinerAsHiddenClass"),
                    name,
                    type
            );
        }
        return null;
    }

    private static MethodHandle resolveAsJdk(MethodHandles.Lookup lookup, String name, MethodType type, boolean binding) throws Throwable {
        try {
            MethodHandle handle = lookup.findVirtual(jdk.internal.misc.Unsafe.class, name, type);
            return binding ? handle.bindTo(Unsafe9Abs.usf) : handle;
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    private static MethodHandle resolveAsJdkReference(MethodHandles.Lookup lookup, String name, MethodType type, boolean binding) throws Throwable {
        String newName = name.replace("Object", "Reference");
        if (!newName.equals(name)) {
            return findMethod(lookup, newName, type, false, binding);
        }
        return null;
    }

    private static MethodHandle resolveAsJdkObject(MethodHandles.Lookup lookup, String name, MethodType type, boolean binding) throws Throwable {
        String newName = name.replace("Reference", "Object");
        if (!newName.equals(name)) {
            return findMethod(lookup, newName, type, false, binding);
        }
        return null;
    }


    private static MethodHandle resolveArrayBaseOffset(MethodHandles.Lookup lookup, String name, MethodType type, boolean binding) throws Throwable {
        if (name.equals("arrayBaseOffset")) {
            return lookup.findVirtual(jdk.internal.misc.Unsafe.class, "arrayBaseOffset", MethodType.methodType(int.class, Class.class))
                    .bindTo(Unsafe9Abs.usf)
                    .asType(MethodType.methodType(long.class, Class.class));
        }
        return null;
    }

    private static MethodHandle resolveNoModifier(MethodHandles.Lookup lookup, String name, MethodType type, boolean binding) throws Throwable {
        if (name.endsWith("Acquire")) {
            return findMethod(lookup, name.substring(0, name.length() - "Acquire".length()), type, true, binding);
        }
        return null;
    }
}
