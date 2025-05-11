package moe.karla.usf.unsafe;

import java.util.*;

public class UnsafeInitializer {
    static volatile Thread initializeThread;

    static void initialize() {
        try {
            initializeThread = Thread.currentThread();
            initialize0();
        } finally {
            initializeThread = null;
        }
    }

    public static void validate() {
        if (Thread.currentThread() != initializeThread) {
            throw new IllegalStateException("Unsafe instance can only be allocated by UnsafeInitializer");
        }
    }

    static void initialize0() {
        ServiceLoader<UnsafeProvider> providers = ServiceLoader.load(UnsafeProvider.class, UnsafeInitializer.class.getClassLoader());
        List<Throwable> errors = new ArrayList<>();
        List<UnsafeProvider> availableProviders = new ArrayList<>();

        Iterator<UnsafeProvider> providerIterator = providers.iterator();
        while (providerIterator.hasNext()) {
            UnsafeProvider provider;
            try {
                provider = providerIterator.next();
            } catch (Throwable t) {
                errors.add(t);
                continue;
            }
            availableProviders.add(provider);
        }

        availableProviders.sort(Comparator.comparingInt(UnsafeProvider::priority).reversed());


        for (UnsafeProvider provider : availableProviders) {
            try {
                Unsafe.theUnsafe = provider.initialize();
                if (Unsafe.theUnsafe != null) return;
            } catch (Throwable e) {
                errors.add(e);
                throw thrown(errors);
            }
        }

        throw thrown(errors);
    }

    private static RuntimeException thrown(List<Throwable> errors) {
        RuntimeException thrown = new RuntimeException("No provider can load unsafe instance.");
        errors.forEach(thrown::addSuppressed);

        throw thrown;
    }
}
