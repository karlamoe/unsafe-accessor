package jmh;

import moe.karla.usf.unsafe.Unsafe;
import org.openjdk.jmh.annotations.*;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 1)
@Measurement(iterations = 4)
@Fork(1)
@Threads(8)
public class MemoryAccessSunMiscUnsafe {
    private static final long TEST_SIZE = 4 * 100 * 1024;

    private long address;
    private static final Unsafe unsafe = Unsafe.getUnsafe();

    static class Impl {
        private static final sun.misc.Unsafe MY_UNSAFE;

        static {
            try {
                Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                MY_UNSAFE = (sun.misc.Unsafe) field.get(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        static void memPutInt(long address, int value) {
            MY_UNSAFE.putInt(address, value);
        }
    }

    @Setup(Level.Trial)
    public void setup() {
        address = unsafe.allocateMemory(TEST_SIZE);
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        unsafe.freeMemory(address);
        address = 0;
    }


    @Benchmark
    public void task40() {
        for (long j = 0; j < 4 * 100 * 1024; j += 4 * 40) {
            long baseAddress = address + j;
            Impl.memPutInt(baseAddress, 1);
            Impl.memPutInt(baseAddress + 4, 1);
            Impl.memPutInt(baseAddress + 8, 1);
            Impl.memPutInt(baseAddress + 12, 1);
            Impl.memPutInt(baseAddress + 16, 1);
            Impl.memPutInt(baseAddress + 20, 1);
            Impl.memPutInt(baseAddress + 24, 1);
            Impl.memPutInt(baseAddress + 28, 1);
            Impl.memPutInt(baseAddress + 32, 1);
            Impl.memPutInt(baseAddress + 36, 1);
            Impl.memPutInt(baseAddress + 40, 1);
            Impl.memPutInt(baseAddress + 44, 1);
            Impl.memPutInt(baseAddress + 48, 1);
            Impl.memPutInt(baseAddress + 52, 1);
            Impl.memPutInt(baseAddress + 56, 1);
            Impl.memPutInt(baseAddress + 60, 1);
            Impl.memPutInt(baseAddress + 64, 1);
            Impl.memPutInt(baseAddress + 68, 1);
            Impl.memPutInt(baseAddress + 72, 1);
            Impl.memPutInt(baseAddress + 76, 1);
            Impl.memPutInt(baseAddress + 80, 1);
            Impl.memPutInt(baseAddress + 84, 1);
            Impl.memPutInt(baseAddress + 88, 1);
            Impl.memPutInt(baseAddress + 92, 1);
            Impl.memPutInt(baseAddress + 96, 1);
            Impl.memPutInt(baseAddress + 100, 1);
            Impl.memPutInt(baseAddress + 104, 1);
            Impl.memPutInt(baseAddress + 108, 1);
            Impl.memPutInt(baseAddress + 112, 1);
            Impl.memPutInt(baseAddress + 116, 1);
            Impl.memPutInt(baseAddress + 120, 1);
            Impl.memPutInt(baseAddress + 124, 1);
            Impl.memPutInt(baseAddress + 128, 1);
            Impl.memPutInt(baseAddress + 132, 1);
            Impl.memPutInt(baseAddress + 136, 1);
            Impl.memPutInt(baseAddress + 140, 1);
            Impl.memPutInt(baseAddress + 144, 1);
            Impl.memPutInt(baseAddress + 148, 1);
            Impl.memPutInt(baseAddress + 152, 1);
            Impl.memPutInt(baseAddress + 156, 1);
        }
    }
}
