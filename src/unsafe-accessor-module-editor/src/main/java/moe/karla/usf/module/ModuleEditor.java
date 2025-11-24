package moe.karla.usf.module;

import moe.karla.usf.root.util.RunCatching;
import moe.karla.usf.root.util.ThrowingHandle;
import moe.karla.usf.security.RootSecurity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

/// ModuleEditor is a utility for unrestrict limitations of Java Module System.
///
/// This class provides three main methods ([#addOpens(Object, String, Object)],
/// [#addExports(Object, String, Object)] and [#addReads(Object, Object)]
/// ), as well as one additional method, [#getModule(Class)].
///
/// For java 8 compatibility, [Object] is used to refer to the [java.lang.Module] object.
/// This means that the Object parameter defined in this tool only accepts Module objects;
/// passing parameters of other types will only result in a [ClassCastException].
///
@SuppressWarnings("Convert2MethodRef")
public abstract class ModuleEditor {
    private static final Supplier<ModuleEditor> INSTANCE = RunCatching.run(() -> {
        try {
            Class.forName("java.lang.Module");
        } catch (ClassNotFoundException ignored) {
            return new NopEditor();
        }
        return new ModuleEditorImpl();
    }).map(result -> {
        return (Supplier<ModuleEditor>) () -> result;
    }).recover(err -> {
        return ThrowingHandle.makeSupplier(err);
    }).getOrThrow();

    /// Get instance of [ModuleEditor]
    ///
    /// Permission [RootSecurity.Type#ROOT_ACCESS_TRUSTED_LOOKUP] required.
    public static ModuleEditor getInstance() {
        RootSecurity.check(RootSecurity.Type.ROOT_ACCESS_TRUSTED_LOOKUP);
        return INSTANCE.get();
    }


    /// Java 8 compatible [Class#getModule()].
    ///
    /// The purpose of this method is solely to provide Java 8 compatibility.
    /// Use [Class#getModule()] directly for better readability.
    ///
    /// @apiNote Under Java 8, returns {@link Optional#empty()} as a replacement.
    @Contract(pure = true)
    public abstract @NotNull Object getModule(@NotNull Class<?> klass);

    /**
     * {@link java.lang.Module#addOpens(String, java.lang.Module)}
     */
    public abstract void addOpens(@NotNull Object module, @NotNull String pkg, @NotNull Object targetModule);

    /**
     * {@link java.lang.Module#addExports(String, java.lang.Module)}
     */
    public abstract void addExports(@NotNull Object module, @NotNull String pkg, @NotNull Object targetModule);

    /**
     * {@link java.lang.Module#addReads(java.lang.Module)}
     */
    public abstract void addReads(@NotNull Object module, @NotNull Object targetModule);
}
