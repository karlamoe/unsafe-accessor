package moe.karla.usf.module;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

class NopEditor extends ModuleEditor {
    @Override
    public @NotNull Object getModule(@NotNull Class<?> klass) {
        return Optional.empty();
    }

    @Override
    public void addExports(@NotNull Object module, @NotNull String pkg, @NotNull Object targetModule) {
    }

    @Override
    public void addOpens(@NotNull Object module, @NotNull String pkg, @NotNull Object targetModule) {
    }

    @Override
    public void addReads(@NotNull Object module, @NotNull Object targetModule) {
    }
}
