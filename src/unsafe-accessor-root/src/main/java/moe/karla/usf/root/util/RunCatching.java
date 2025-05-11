package moe.karla.usf.root.util;

public class RunCatching<T> {

    private final Throwable error;
    private final T result;

    private RunCatching(Throwable error, T result) {
        this.error = error;
        this.result = result;
    }

    public T getOrThrow() {
        if (error != null) {
            SneakyThrow.t(error);
        }
        return result;
    }

    public Throwable exceptionOrNull() {
        return error;
    }

    public RunCatching<T> recover(AnyCallable<T> supplier) {
        if (error == null && result != null) return this;
        try {
            return successful(supplier.call());
        } catch (Throwable e) {
            if (error == null) return failure(e);

            this.error.addSuppressed(e);
            return this;
        }
    }


    public static <T> RunCatching<T> successful(T value) {
        return new RunCatching<>(null, value);
    }

    public static <T> RunCatching<T> failure(Throwable throwable) {
        return new RunCatching<>(throwable, null);
    }

    public static <T> RunCatching<T> run(AnyCallable<T> supplier) {
        try {
            return successful(supplier.call());
        } catch (Throwable e) {
            return failure(e);
        }
    }
}
