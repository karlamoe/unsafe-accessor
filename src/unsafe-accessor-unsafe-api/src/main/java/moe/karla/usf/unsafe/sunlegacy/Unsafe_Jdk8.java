package moe.karla.usf.unsafe.sunlegacy;

import moe.karla.usf.root.util.SneakyThrow;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.ProtectionDomain;

@SuppressWarnings({"all"})
class Unsafe_Jdk8 extends moe.karla.usf.unsafe.Unsafe {
    protected final Unsafe theUnsafe = initUnsafe();

    private static Unsafe initUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    @Override
    public Object getOriginalUnsafe() {
        return theUnsafe;
    }

    @Override
    public long objectFieldOffset(Class<?> c, String name) {
        try {
            return theUnsafe.objectFieldOffset(c.getDeclaredField(name));
        } catch (NoSuchFieldException e) {
            throw new InternalError(e);
        }
    }

    @Override
    public Object getReference(Object o, long offset) {
        return theUnsafe.getObject(o, offset);
    }

    @Override
    public Object getReferenceAcquire(Object o, long offset) {
        return theUnsafe.getObjectVolatile(o, offset);
    }

    @Override
    public boolean getBooleanAcquire(Object o, long offset) {
        return theUnsafe.getBooleanVolatile(o, offset);
    }

    @Override
    public byte getByteAcquire(Object o, long offset) {
        return theUnsafe.getByteVolatile(o, offset);
    }

    @Override
    public short getShortAcquire(Object o, long offset) {
        return theUnsafe.getShortVolatile(o, offset);
    }

    @Override
    public char getCharAcquire(Object o, long offset) {
        return theUnsafe.getCharVolatile(o, offset);
    }

    @Override
    public int getIntAcquire(Object o, long offset) {
        return theUnsafe.getIntVolatile(o, offset);
    }

    @Override
    public float getFloatAcquire(Object o, long offset) {
        return theUnsafe.getFloatVolatile(o, offset);
    }

    @Override
    public long getLongAcquire(Object o, long offset) {
        return theUnsafe.getLongVolatile(o, offset);
    }

    @Override
    public double getDoubleAcquire(Object o, long offset) {
        return theUnsafe.getDoubleVolatile(o, offset);
    }

    @Override
    public Object getReferenceOpaque(Object o, long offset) {
        return theUnsafe.getObjectVolatile(o, offset);
    }

    @Override
    public boolean getBooleanOpaque(Object o, long offset) {
        return theUnsafe.getBooleanVolatile(o, offset);
    }

    @Override
    public byte getByteOpaque(Object o, long offset) {
        return theUnsafe.getByteVolatile(o, offset);
    }

    @Override
    public short getShortOpaque(Object o, long offset) {
        return theUnsafe.getShortVolatile(o, offset);
    }

    @Override
    public char getCharOpaque(Object o, long offset) {
        return theUnsafe.getCharVolatile(o, offset);
    }

    @Override
    public int getIntOpaque(Object o, long offset) {
        return theUnsafe.getIntVolatile(o, offset);
    }

    @Override
    public float getFloatOpaque(Object o, long offset) {
        return theUnsafe.getFloatVolatile(o, offset);
    }

    @Override
    public long getLongOpaque(Object o, long offset) {
        return theUnsafe.getLongVolatile(o, offset);
    }

    @Override
    public double getDoubleOpaque(Object o, long offset) {
        return theUnsafe.getDoubleVolatile(o, offset);
    }

    @Override
    public void putReference(Object o, long offset, Object x) {
        theUnsafe.putObject(o, offset, x);
    }

    @Override
    public void putReferenceOpaque(Object o, long offset, Object x) {
        theUnsafe.putObjectVolatile(o, offset, x);
    }

    @Override
    public void putBooleanOpaque(Object o, long offset, boolean x) {
        theUnsafe.putBooleanVolatile(o, offset, x);
    }

    @Override
    public void putByteOpaque(Object o, long offset, byte x) {
        theUnsafe.putByteVolatile(o, offset, x);
    }

    @Override
    public void putShortOpaque(Object o, long offset, short x) {
        theUnsafe.putShortVolatile(o, offset, x);
    }

    @Override
    public void putCharOpaque(Object o, long offset, char x) {
        theUnsafe.putCharVolatile(o, offset, x);
    }

    @Override
    public void putIntOpaque(Object o, long offset, int x) {
        theUnsafe.putIntVolatile(o, offset, x);
    }

    @Override
    public void putFloatOpaque(Object o, long offset, float x) {
        theUnsafe.putFloatVolatile(o, offset, x);
    }

    @Override
    public void putLongOpaque(Object o, long offset, long x) {
        theUnsafe.putLongVolatile(o, offset, x);
    }

    @Override
    public void putDoubleOpaque(Object o, long offset, double x) {
        theUnsafe.putDoubleVolatile(o, offset, x);
    }

    @Override
    public void putReferenceRelease(Object o, long offset, Object x) {
        theUnsafe.putObjectVolatile(o, offset, x);
    }

    @Override
    public void putBooleanRelease(Object o, long offset, boolean x) {
        theUnsafe.putBooleanVolatile(o, offset, x);
    }

    @Override
    public void putByteRelease(Object o, long offset, byte x) {
        theUnsafe.putByteVolatile(o, offset, x);
    }

    @Override
    public void putShortRelease(Object o, long offset, short x) {
        theUnsafe.putShortVolatile(o, offset, x);
    }

    @Override
    public void putCharRelease(Object o, long offset, char x) {
        theUnsafe.putCharVolatile(o, offset, x);
    }

    @Override
    public void putIntRelease(Object o, long offset, int x) {
        theUnsafe.putIntVolatile(o, offset, x);
    }

    @Override
    public void putFloatRelease(Object o, long offset, float x) {
        theUnsafe.putFloatVolatile(o, offset, x);
    }

    @Override
    public void putLongRelease(Object o, long offset, long x) {
        theUnsafe.putLongVolatile(o, offset, x);
    }

    @Override
    public void putDoubleRelease(Object o, long offset, double x) {
        theUnsafe.putDoubleVolatile(o, offset, x);
    }

    @Override
    public void putReferenceVolatile(Object o, long offset, Object x) {
        theUnsafe.putObjectVolatile(o, offset, x);
    }

    @Override
    public int getInt(Object o, long offset) {
        return theUnsafe.getInt(o, offset);
    }

    @Override
    public void putInt(Object o, long offset, int x) {
        theUnsafe.putInt(o, offset, x);
    }

    @Override
    public Object getObject(Object o, long offset) {
        return theUnsafe.getObject(o, offset);
    }

    @Override
    public void putObject(Object o, long offset, Object x) {
        theUnsafe.putObject(o, offset, x);
    }

    @Override
    public boolean getBoolean(Object o, long offset) {
        return theUnsafe.getBoolean(o, offset);
    }

    @Override
    public void putBoolean(Object o, long offset, boolean x) {
        theUnsafe.putBoolean(o, offset, x);
    }

    @Override
    public byte getByte(Object o, long offset) {
        return theUnsafe.getByte(o, offset);
    }

    @Override
    public void putByte(Object o, long offset, byte x) {
        theUnsafe.putByte(o, offset, x);
    }

    @Override
    public short getShort(Object o, long offset) {
        return theUnsafe.getShort(o, offset);
    }

    @Override
    public void putShort(Object o, long offset, short x) {
        theUnsafe.putShort(o, offset, x);
    }

    @Override
    public char getChar(Object o, long offset) {
        return theUnsafe.getChar(o, offset);
    }

    @Override
    public void putChar(Object o, long offset, char x) {
        theUnsafe.putChar(o, offset, x);
    }

    @Override
    public long getLong(Object o, long offset) {
        return theUnsafe.getLong(o, offset);
    }

    @Override
    public void putLong(Object o, long offset, long x) {
        theUnsafe.putLong(o, offset, x);
    }

    @Override
    public float getFloat(Object o, long offset) {
        return theUnsafe.getFloat(o, offset);
    }

    @Override
    public void putFloat(Object o, long offset, float x) {
        theUnsafe.putFloat(o, offset, x);
    }

    @Override
    public double getDouble(Object o, long offset) {
        return theUnsafe.getDouble(o, offset);
    }

    @Override
    public void putDouble(Object o, long offset, double x) {
        theUnsafe.putDouble(o, offset, x);
    }

    @Override
    public Object getUncompressedObject(long address) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getByte(long address) {
        return theUnsafe.getByte(address);
    }

    @Override
    public void putByte(long address, byte x) {
        theUnsafe.putByte(address, x);
    }

    @Override
    public short getShort(long address) {
        return theUnsafe.getShort(address);
    }

    @Override
    public void putShort(long address, short x) {
        theUnsafe.putShort(address, x);
    }

    @Override
    public char getChar(long address) {
        return theUnsafe.getChar(address);
    }

    @Override
    public void putChar(long address, char x) {
        theUnsafe.putChar(address, x);
    }

    @Override
    public int getInt(long address) {
        return theUnsafe.getInt(address);
    }

    @Override
    public void putInt(long address, int x) {
        theUnsafe.putInt(address, x);
    }

    @Override
    public long getLong(long address) {
        return theUnsafe.getLong(address);
    }

    @Override
    public void putLong(long address, long x) {
        theUnsafe.putLong(address, x);
    }

    @Override
    public float getFloat(long address) {
        return theUnsafe.getFloat(address);
    }

    @Override
    public void putFloat(long address, float x) {
        theUnsafe.putFloat(address, x);
    }

    @Override
    public double getDouble(long address) {
        return theUnsafe.getDouble(address);
    }

    @Override
    public void putDouble(long address, double x) {
        theUnsafe.putDouble(address, x);
    }

    @Override
    public long getAddress(long address) {
        return theUnsafe.getAddress(address);
    }

    @Override
    public void putAddress(long address, long x) {
        theUnsafe.putAddress(address, x);
    }

    @Override
    public long allocateMemory(long bytes) {
        return theUnsafe.allocateMemory(bytes);
    }

    @Override
    public long reallocateMemory(long address, long bytes) {
        return theUnsafe.reallocateMemory(address, bytes);
    }

    @Override
    public void setMemory(Object o, long offset, long bytes, byte value) {
        theUnsafe.setMemory(o, offset, bytes, value);
    }

    @Override
    public void setMemory(long address, long bytes, byte value) {
        theUnsafe.setMemory(address, bytes, value);
    }

    @Override
    public void copyMemory(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes) {
        theUnsafe.copyMemory(srcBase, srcOffset, destBase, destOffset, bytes);
    }

    @Override
    public void copyMemory(long srcAddress, long destAddress, long bytes) {
        theUnsafe.copyMemory(srcAddress, destAddress, bytes);
    }

    @Override
    public void copySwapMemory(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes, long elemSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void copySwapMemory(long srcAddress, long destAddress, long bytes, long elemSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void freeMemory(long address) {
        theUnsafe.freeMemory(address);
    }

    @Override
    public long objectFieldOffset(Field f) {
        return theUnsafe.objectFieldOffset(f);
    }

    @Override
    public long staticFieldOffset(Field f) {
        return theUnsafe.staticFieldOffset(f);
    }

    @Override
    public Object staticFieldBase(Field f) {
        return theUnsafe.staticFieldBase(f);
    }

    @Override
    public boolean shouldBeInitialized(Class<?> c) {
        return theUnsafe.shouldBeInitialized(c);
    }

    @Override
    public void ensureClassInitialized(Class<?> c) {
        theUnsafe.ensureClassInitialized(c);
    }

    @Override
    public long arrayBaseOffset(Class<?> arrayClass) {
        return theUnsafe.arrayBaseOffset(arrayClass);
    }

    @Override
    public int arrayIndexScale(Class<?> arrayClass) {
        return theUnsafe.arrayIndexScale(arrayClass);
    }

    @Override
    public int addressSize() {
        return theUnsafe.addressSize();
    }

    @Override
    public int pageSize() {
        return theUnsafe.pageSize();
    }

    @Override
    public Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) {
        try {
            return (Class<?>) LegacyDefineClass0.MH_DEFINE_CLASS.invokeExact(theUnsafe, name, b, off, len, loader, protectionDomain);
        } catch (Throwable e) {
            throw SneakyThrow.t(e);
        }
    }

    @Override
    public Class<?> defineClass0(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) {
        try {
            return (Class<?>) LegacyDefineClass0.MH_DEFINE_CLASS0.invokeExact(theUnsafe, name, b, off, len, loader, protectionDomain);
        } catch (Throwable e) {
            throw SneakyThrow.t(e);
        }
    }

    @Override
    public Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches) {
        return theUnsafe.defineAnonymousClass(hostClass, data, cpPatches);
    }

    @Override
    public Object allocateInstance(Class<?> cls) throws InstantiationException {
        return theUnsafe.allocateInstance(cls);
    }

    @Override
    public Object allocateUninitializedArray(Class<?> componentType, int length) {
        if (componentType == null) {
            throw new IllegalArgumentException("Component type is null");
        }
        if (!componentType.isPrimitive()) {
            throw new IllegalArgumentException("Component type is not primitive");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Negative length");
        }
        if (componentType == byte.class)    /**/ return new byte[length];
        if (componentType == boolean.class) /**/ return new boolean[length];
        if (componentType == short.class)   /**/ return new short[length];
        if (componentType == char.class)    /**/ return new char[length];
        if (componentType == int.class)     /**/ return new int[length];
        if (componentType == float.class)   /**/ return new float[length];
        if (componentType == long.class)    /**/ return new long[length];
        if (componentType == double.class)  /**/ return new double[length];
        return null;
    }

    @Override
    public void throwException(Throwable ee) {
        theUnsafe.throwException(ee);
    }

    @Override
    public boolean compareAndSetReference(Object o, long offset, Object expected, Object x) {
        return theUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    @Override
    public Object compareAndExchangeReference(Object o, long offset, Object expected, Object x) {
        while (true) {
            Object witness = theUnsafe.getObjectVolatile(o, offset);
            if (witness != expected) {
                return witness;
            }
            if (theUnsafe.compareAndSwapObject(o, offset, witness, x)) {
                return witness;
            }
        }
    }

    @Override
    public Object compareAndExchangeReferenceAcquire(Object o, long offset, Object expected, Object x) {
        return compareAndExchangeReference(o, offset, expected, x);
    }

    @Override
    public Object compareAndExchangeReferenceRelease(Object o, long offset, Object expected, Object x) {
        return compareAndExchangeReference(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetReferencePlain(Object o, long offset, Object expected, Object x) {
        return theUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetReferenceAcquire(Object o, long offset, Object expected, Object x) {
        return theUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetReferenceRelease(Object o, long offset, Object expected, Object x) {
        return theUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetReference(Object o, long offset, Object expected, Object x) {
        return theUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    @Override
    public boolean compareAndSetInt(Object o, long offset, int expected, int x) {
        return theUnsafe.compareAndSwapInt(o, offset, expected, x);
    }

    @Override
    public int compareAndExchangeInt(Object o, long offset, int expected, int x) {
        while (true) {
            int witness = theUnsafe.getIntVolatile(o, offset);
            if (witness != expected) {
                return witness;
            }
            if (theUnsafe.compareAndSwapInt(o, offset, witness, x)) {
                return witness;
            }
        }
    }

    @Override
    public int compareAndExchangeIntAcquire(Object o, long offset, int expected, int x) {
        return compareAndExchangeInt(o, offset, expected, x);
    }

    @Override
    public int compareAndExchangeIntRelease(Object o, long offset, int expected, int x) {
        return compareAndExchangeInt(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetIntPlain(Object o, long offset, int expected, int x) {
        return compareAndSetInt(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetIntAcquire(Object o, long offset, int expected, int x) {
        return compareAndSetInt(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetIntRelease(Object o, long offset, int expected, int x) {
        return theUnsafe.compareAndSwapInt(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetInt(Object o, long offset, int expected, int x) {
        return theUnsafe.compareAndSwapInt(o, offset, expected, x);
    }

    @Override
    public byte compareAndExchangeByte(Object o, long offset, byte expected, byte x) {
        long wordOffset = offset & ~3;
        int shift = (int) (offset & 3) << 3;
        if (BIG_ENDIAN) {
            shift = 24 - shift;
        }
        int mask = 0xFF << shift;
        int maskedExpected = (expected & 0xFF) << shift;
        int maskedX = (x & 0xFF) << shift;
        int fullWord;
        do {
            fullWord = getIntVolatile(o, wordOffset);
            if ((fullWord & mask) != maskedExpected)
                return (byte) ((fullWord & mask) >> shift);
        } while (!weakCompareAndSetInt(o, wordOffset,
                fullWord, (fullWord & ~mask) | maskedX));
        return expected;
    }

    @Override
    public boolean compareAndSetByte(Object o, long offset, byte expected, byte x) {
        return compareAndExchangeByte(o, offset, expected, x) == expected;
    }

    @Override
    public boolean weakCompareAndSetByte(Object o, long offset, byte expected, byte x) {
        return compareAndSetByte(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetByteAcquire(Object o, long offset, byte expected, byte x) {
        return compareAndSetByte(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetByteRelease(Object o, long offset, byte expected, byte x) {
        return compareAndSetByte(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetBytePlain(Object o, long offset, byte expected, byte x) {
        return compareAndSetByte(o, offset, expected, x);
    }

    @Override
    public byte compareAndExchangeByteAcquire(Object o, long offset, byte expected, byte x) {
        return compareAndExchangeByte(o, offset, expected, x);
    }

    @Override
    public byte compareAndExchangeByteRelease(Object o, long offset, byte expected, byte x) {
        return compareAndExchangeByte(o, offset, expected, x);
    }

    @Override
    public short compareAndExchangeShort(Object o, long offset, short expected, short x) {
        if ((offset & 3) == 3) {
            throw new IllegalArgumentException("Update spans the word, not supported");
        }
        long wordOffset = offset & ~3;
        int shift = (int) (offset & 3) << 3;
        if (BIG_ENDIAN) {
            shift = 16 - shift;
        }
        int mask = 0xFFFF << shift;
        int maskedExpected = (expected & 0xFFFF) << shift;
        int maskedX = (x & 0xFFFF) << shift;
        int fullWord;
        do {
            fullWord = getIntVolatile(o, wordOffset);
            if ((fullWord & mask) != maskedExpected) {
                return (short) ((fullWord & mask) >> shift);
            }
        } while (!weakCompareAndSetInt(o, wordOffset,
                fullWord, (fullWord & ~mask) | maskedX));
        return expected;
    }

    @Override
    public boolean compareAndSetShort(Object o, long offset, short expected, short x) {
        return compareAndExchangeShort(o, offset, expected, x) == expected;
    }

    @Override
    public boolean weakCompareAndSetShort(Object o, long offset, short expected, short x) {
        return compareAndSetShort(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetShortAcquire(Object o, long offset, short expected, short x) {
        return compareAndSetShort(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetShortRelease(Object o, long offset, short expected, short x) {
        return compareAndSetShort(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetShortPlain(Object o, long offset, short expected, short x) {
        return compareAndSetShort(o, offset, expected, x);
    }

    @Override
    public short compareAndExchangeShortAcquire(Object o, long offset, short expected, short x) {
        return compareAndExchangeShort(o, offset, expected, x);
    }

    @Override
    public short compareAndExchangeShortRelease(Object o, long offset, short expected, short x) {
        return compareAndExchangeShort(o, offset, expected, x);
    }

    @Override
    public boolean compareAndSetChar(Object o, long offset, char expected, char x) {
        return compareAndSetShort(o, offset, (short) expected, (short) x);
    }

    @Override
    public char compareAndExchangeChar(Object o, long offset, char expected, char x) {
        return (char) compareAndExchangeShort(o, offset, (short) expected, (short) x);
    }

    @Override
    public char compareAndExchangeCharAcquire(Object o, long offset, char expected, char x) {
        return (char) compareAndExchangeShort(o, offset, (short) expected, (short) x);
    }

    @Override
    public char compareAndExchangeCharRelease(Object o, long offset, char expected, char x) {
        return (char) compareAndExchangeShort(o, offset, (short) expected, (short) x);
    }

    @Override
    public boolean weakCompareAndSetChar(Object o, long offset, char expected, char x) {
        return compareAndSetShort(o, offset, (short) expected, (short) x);
    }

    @Override
    public boolean weakCompareAndSetCharAcquire(Object o, long offset, char expected, char x) {
        return compareAndSetShort(o, offset, (short) expected, (short) x);
    }

    @Override
    public boolean weakCompareAndSetCharRelease(Object o, long offset, char expected, char x) {
        return compareAndSetShort(o, offset, (short) expected, (short) x);
    }

    @Override
    public boolean weakCompareAndSetCharPlain(Object o, long offset, char expected, char x) {
        return compareAndSetShort(o, offset, (short) expected, (short) x);
    }

    @Override
    public boolean compareAndSetBoolean(Object o, long offset, boolean expected, boolean x) {
        return compareAndSetByte(o, offset, expected ? (byte) 1 : 0, x ? (byte) 1 : 0);
    }

    @Override
    public boolean compareAndExchangeBoolean(Object o, long offset, boolean expected, boolean x) {
        return compareAndExchangeByte(o, offset, expected ? (byte) 1 : 0, x ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean compareAndExchangeBooleanAcquire(Object o, long offset, boolean expected, boolean x) {
        return compareAndExchangeByte(o, offset, expected ? (byte) 1 : 0, x ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean compareAndExchangeBooleanRelease(Object o, long offset, boolean expected, boolean x) {
        return compareAndExchangeByte(o, offset, expected ? (byte) 1 : 0, x ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean weakCompareAndSetBoolean(Object o, long offset, boolean expected, boolean x) {
        return compareAndSetByte(o, offset, expected ? (byte) 1 : 0, x ? (byte) 1 : 0);
    }

    @Override
    public boolean weakCompareAndSetBooleanAcquire(Object o, long offset, boolean expected, boolean x) {
        return compareAndSetByte(o, offset, expected ? (byte) 1 : 0, x ? (byte) 1 : 0);
    }

    @Override
    public boolean weakCompareAndSetBooleanRelease(Object o, long offset, boolean expected, boolean x) {
        return compareAndSetByte(o, offset, expected ? (byte) 1 : 0, x ? (byte) 1 : 0);
    }

    @Override
    public boolean weakCompareAndSetBooleanPlain(Object o, long offset, boolean expected, boolean x) {
        return compareAndSetByte(o, offset, expected ? (byte) 1 : 0, x ? (byte) 1 : 0);
    }

    @Override
    public boolean compareAndSetFloat(Object o, long offset, float expected, float x) {
        return compareAndSetInt(o, offset, Float.floatToRawIntBits(expected), Float.floatToRawIntBits(x));
    }

    @Override
    public float compareAndExchangeFloat(Object o, long offset, float expected, float x) {
        return Float.intBitsToFloat(
                compareAndExchangeInt(o, offset, Float.floatToRawIntBits(expected), Float.floatToRawIntBits(x))
        );
    }

    @Override
    public float compareAndExchangeFloatAcquire(Object o, long offset, float expected, float x) {
        return Float.intBitsToFloat(
                compareAndExchangeInt(o, offset, Float.floatToRawIntBits(expected), Float.floatToRawIntBits(x))
        );
    }

    @Override
    public float compareAndExchangeFloatRelease(Object o, long offset, float expected, float x) {
        return Float.intBitsToFloat(
                compareAndExchangeInt(o, offset, Float.floatToRawIntBits(expected), Float.floatToRawIntBits(x))
        );
    }

    @Override
    public boolean weakCompareAndSetFloatPlain(Object o, long offset, float expected, float x) {
        return compareAndSetInt(o, offset, Float.floatToRawIntBits(expected), Float.floatToRawIntBits(x));
    }

    @Override
    public boolean weakCompareAndSetFloatAcquire(Object o, long offset, float expected, float x) {
        return compareAndSetInt(o, offset, Float.floatToRawIntBits(expected), Float.floatToRawIntBits(x));
    }

    @Override
    public boolean weakCompareAndSetFloatRelease(Object o, long offset, float expected, float x) {
        return compareAndSetInt(o, offset, Float.floatToRawIntBits(expected), Float.floatToRawIntBits(x));
    }

    @Override
    public boolean weakCompareAndSetFloat(Object o, long offset, float expected, float x) {
        return compareAndSetInt(o, offset, Float.floatToRawIntBits(expected), Float.floatToRawIntBits(x));
    }

    @Override
    public boolean compareAndSetDouble(Object o, long offset, double expected, double x) {
        return compareAndSetLong(o, offset,
                Double.doubleToRawLongBits(expected),
                Double.doubleToRawLongBits(x));
    }

    @Override
    public double compareAndExchangeDouble(Object o, long offset, double expected, double x) {
        return Double.longBitsToDouble(
                compareAndExchangeLong(o, offset, Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(x))
        );
    }

    @Override
    public double compareAndExchangeDoubleAcquire(Object o, long offset, double expected, double x) {
        return Double.longBitsToDouble(
                compareAndExchangeLong(o, offset, Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(x))
        );
    }

    @Override
    public double compareAndExchangeDoubleRelease(Object o, long offset, double expected, double x) {
        return Double.longBitsToDouble(
                compareAndExchangeLong(o, offset, Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(x))
        );
    }

    @Override
    public boolean weakCompareAndSetDoublePlain(Object o, long offset, double expected, double x) {
        return compareAndSetLong(o, offset,
                Double.doubleToRawLongBits(expected),
                Double.doubleToRawLongBits(x));
    }

    @Override
    public boolean weakCompareAndSetDoubleAcquire(Object o, long offset, double expected, double x) {
        return compareAndSetLong(o, offset,
                Double.doubleToRawLongBits(expected),
                Double.doubleToRawLongBits(x));
    }

    @Override
    public boolean weakCompareAndSetDoubleRelease(Object o, long offset, double expected, double x) {
        return compareAndSetLong(o, offset,
                Double.doubleToRawLongBits(expected),
                Double.doubleToRawLongBits(x));
    }

    @Override
    public boolean weakCompareAndSetDouble(Object o, long offset, double expected, double x) {
        return compareAndSetLong(o, offset,
                Double.doubleToRawLongBits(expected),
                Double.doubleToRawLongBits(x));
    }

    @Override
    public boolean compareAndSetLong(Object o, long offset, long expected, long x) {
        return theUnsafe.compareAndSwapLong(o, offset, expected, x);
    }

    @Override
    public long compareAndExchangeLong(Object o, long offset, long expected, long x) {
        while (true) {
            long witness = theUnsafe.getLongVolatile(o, offset);
            if (witness != expected) {
                return witness;
            }
            if (theUnsafe.compareAndSwapLong(o, offset, witness, x)) {
                return witness;
            }
        }
    }

    @Override
    public long compareAndExchangeLongAcquire(Object o, long offset, long expected, long x) {
        return compareAndExchangeLong(o, offset, expected, x);
    }

    @Override
    public long compareAndExchangeLongRelease(Object o, long offset, long expected, long x) {
        return compareAndExchangeLong(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetLongPlain(Object o, long offset, long expected, long x) {
        return theUnsafe.compareAndSwapLong(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetLongAcquire(Object o, long offset, long expected, long x) {
        return theUnsafe.compareAndSwapLong(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetLongRelease(Object o, long offset, long expected, long x) {
        return theUnsafe.compareAndSwapLong(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetLong(Object o, long offset, long expected, long x) {
        return theUnsafe.compareAndSwapLong(o, offset, expected, x);
    }

    @Override
    public Object getReferenceVolatile(Object o, long offset) {
        return theUnsafe.getObjectVolatile(o, offset);
    }

    public boolean compareAndSwapObject(Object o, long offset, Object expected, Object x) {
        return theUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    public boolean compareAndSwapInt(Object o, long offset, int expected, int x) {
        return theUnsafe.compareAndSwapInt(o, offset, expected, x);
    }

    public boolean compareAndSwapLong(Object o, long offset, long expected, long x) {
        return theUnsafe.compareAndSwapLong(o, offset, expected, x);
    }

    @Override
    public Object getObjectVolatile(Object o, long offset) {
        return theUnsafe.getObjectVolatile(o, offset);
    }

    @Override
    public Object getObjectAcquire(Object o, long offset) {
        return theUnsafe.getObjectVolatile(o, offset);
    }

    @Override
    public Object getObjectOpaque(Object o, long offset) {
        return theUnsafe.getObjectVolatile(o, offset);
    }

    @Override
    public void putObjectVolatile(Object o, long offset, Object x) {
        theUnsafe.putObjectVolatile(o, offset, x);
    }

    @Override
    public void putObjectOpaque(Object o, long offset, Object x) {
        theUnsafe.putObjectVolatile(o, offset, x);
    }

    @Override
    public void putObjectRelease(Object o, long offset, Object x) {
        theUnsafe.putObjectVolatile(o, offset, x);
    }

    @Override
    public Object getAndSetObject(Object o, long offset, Object newValue) {
        return theUnsafe.getAndSetObject(o, offset, newValue);
    }

    @Override
    public Object getAndSetObjectAcquire(Object o, long offset, Object newValue) {
        return theUnsafe.getAndSetObject(o, offset, newValue);
    }

    @Override
    public Object getAndSetObjectRelease(Object o, long offset, Object newValue) {
        return theUnsafe.getAndSetObject(o, offset, newValue);
    }

    @Override
    public boolean compareAndSetObject(Object o, long offset, Object expected, Object x) {
        return theUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    @Override
    public Object compareAndExchangeObject(Object o, long offset, Object expected, Object x) {
        while (true) {
            Object witness = theUnsafe.getObjectVolatile(o, offset);
            if (witness != expected) {
                return witness;
            }
            if (theUnsafe.compareAndSwapObject(o, offset, witness, x)) {
                return witness;
            }
        }
    }

    @Override
    public Object compareAndExchangeObjectAcquire(Object o, long offset, Object expected, Object x) {
        return compareAndExchangeObject(o, offset, expected, x);
    }

    @Override
    public Object compareAndExchangeObjectRelease(Object o, long offset, Object expected, Object x) {
        return compareAndExchangeObject(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetObject(Object o, long offset, Object expected, Object x) {
        return theUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetObjectAcquire(Object o, long offset, Object expected, Object x) {
        return theUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetObjectPlain(Object o, long offset, Object expected, Object x) {
        return theUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    @Override
    public boolean weakCompareAndSetObjectRelease(Object o, long offset, Object expected, Object x) {
        return theUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    @Override
    public int getIntVolatile(Object o, long offset) {
        return theUnsafe.getIntVolatile(o, offset);
    }

    @Override
    public void putIntVolatile(Object o, long offset, int x) {
        theUnsafe.putIntVolatile(o, offset, x);
    }

    @Override
    public boolean getBooleanVolatile(Object o, long offset) {
        return theUnsafe.getBooleanVolatile(o, offset);
    }

    @Override
    public void putBooleanVolatile(Object o, long offset, boolean x) {
        theUnsafe.putBooleanVolatile(o, offset, x);
    }

    @Override
    public byte getByteVolatile(Object o, long offset) {
        return theUnsafe.getByteVolatile(o, offset);
    }

    @Override
    public void putByteVolatile(Object o, long offset, byte x) {
        theUnsafe.putByteVolatile(o, offset, x);
    }

    @Override
    public short getShortVolatile(Object o, long offset) {
        return theUnsafe.getShortVolatile(o, offset);
    }

    @Override
    public void putShortVolatile(Object o, long offset, short x) {
        theUnsafe.putShortVolatile(o, offset, x);
    }

    @Override
    public char getCharVolatile(Object o, long offset) {
        return theUnsafe.getCharVolatile(o, offset);
    }

    @Override
    public void putCharVolatile(Object o, long offset, char x) {
        theUnsafe.putCharVolatile(o, offset, x);
    }

    @Override
    public long getLongVolatile(Object o, long offset) {
        return theUnsafe.getLongVolatile(o, offset);
    }

    @Override
    public void putLongVolatile(Object o, long offset, long x) {
        theUnsafe.putLongVolatile(o, offset, x);
    }

    @Override
    public float getFloatVolatile(Object o, long offset) {
        return theUnsafe.getFloatVolatile(o, offset);
    }

    @Override
    public void putFloatVolatile(Object o, long offset, float x) {
        theUnsafe.putFloatVolatile(o, offset, x);
    }

    @Override
    public double getDoubleVolatile(Object o, long offset) {
        return theUnsafe.getDoubleVolatile(o, offset);
    }

    @Override
    public void putDoubleVolatile(Object o, long offset, double x) {
        theUnsafe.putDoubleVolatile(o, offset, x);
    }

    public void putOrderedObject(Object o, long offset, Object x) {
        theUnsafe.putOrderedObject(o, offset, x);
    }

    public void putOrderedInt(Object o, long offset, int x) {
        theUnsafe.putOrderedInt(o, offset, x);
    }

    public void putOrderedLong(Object o, long offset, long x) {
        theUnsafe.putOrderedLong(o, offset, x);
    }

    @Override
    public void unpark(Object thread) {
        theUnsafe.unpark(thread);
    }

    @Override
    public void park(boolean isAbsolute, long time) {
        theUnsafe.park(isAbsolute, time);
    }

    @Override
    public int getLoadAverage(double[] loadavg, int nelems) {
        return theUnsafe.getLoadAverage(loadavg, nelems);
    }

    @Override
    public int getAndAddInt(Object o, long offset, int delta) {
        return theUnsafe.getAndAddInt(o, offset, delta);
    }

    @Override
    public int getAndAddIntRelease(Object o, long offset, int delta) {
        return theUnsafe.getAndAddInt(o, offset, delta);
    }

    @Override
    public int getAndAddIntAcquire(Object o, long offset, int delta) {
        return theUnsafe.getAndAddInt(o, offset, delta);
    }

    @Override
    public long getAndAddLong(Object o, long offset, long delta) {
        return theUnsafe.getAndAddLong(o, offset, delta);
    }

    @Override
    public long getAndAddLongRelease(Object o, long offset, long delta) {
        return theUnsafe.getAndAddLong(o, offset, delta);
    }

    @Override
    public long getAndAddLongAcquire(Object o, long offset, long delta) {
        return theUnsafe.getAndAddLong(o, offset, delta);
    }

    @Override
    public byte getAndAddByte(Object o, long offset, byte delta) {
        byte v;
        do {
            v = getByteVolatile(o, offset);
        } while (!weakCompareAndSetByte(o, offset, v, (byte) (v + delta)));
        return v;
    }

    @Override
    public byte getAndAddByteRelease(Object o, long offset, byte delta) {
        return getAndAddByte(o, offset, delta);
    }

    @Override
    public byte getAndAddByteAcquire(Object o, long offset, byte delta) {
        return getAndAddByte(o, offset, delta);
    }

    @Override
    public short getAndAddShort(Object o, long offset, short delta) {
        short v;
        do {
            v = getShortVolatile(o, offset);
        } while (!weakCompareAndSetShort(o, offset, v, (short) (v + delta)));
        return v;
    }

    @Override
    public short getAndAddShortRelease(Object o, long offset, short delta) {
        return getAndAddShort(o, offset, delta);
    }

    @Override
    public short getAndAddShortAcquire(Object o, long offset, short delta) {
        return getAndAddShort(o, offset, delta);
    }

    @Override
    public char getAndAddChar(Object o, long offset, char delta) {
        return (char) getAndAddShort(o, offset, (short) delta);
    }

    @Override
    public char getAndAddCharRelease(Object o, long offset, char delta) {
        return (char) getAndAddShort(o, offset, (short) delta);
    }

    @Override
    public char getAndAddCharAcquire(Object o, long offset, char delta) {
        return (char) getAndAddShort(o, offset, (short) delta);
    }

    @Override
    public float getAndAddFloat(Object o, long offset, float delta) {
        int expectedBits;
        float v;
        do {
            // Load and CAS with the raw bits to avoid issues with NaNs and
            // possible bit conversion from signaling NaNs to quiet NaNs that
            // may result in the loop not terminating.
            expectedBits = getIntVolatile(o, offset);
            v = Float.intBitsToFloat(expectedBits);
        } while (!weakCompareAndSetInt(o, offset,
                expectedBits, Float.floatToRawIntBits(v + delta)));
        return v;
    }

    @Override
    public float getAndAddFloatRelease(Object o, long offset, float delta) {
        return getAndAddFloat(o, offset, delta);
    }

    @Override
    public float getAndAddFloatAcquire(Object o, long offset, float delta) {
        return getAndAddFloat(o, offset, delta);
    }

    @Override
    public double getAndAddDouble(Object o, long offset, double delta) {
        long expectedBits;
        double v;
        do {
            // Load and CAS with the raw bits to avoid issues with NaNs and
            // possible bit conversion from signaling NaNs to quiet NaNs that
            // may result in the loop not terminating.
            expectedBits = getLongVolatile(o, offset);
            v = Double.longBitsToDouble(expectedBits);
        } while (!weakCompareAndSetLong(o, offset,
                expectedBits, Double.doubleToRawLongBits(v + delta)));
        return v;
    }

    @Override
    public double getAndAddDoubleRelease(Object o, long offset, double delta) {
        return getAndAddDouble(o, offset, delta);
    }

    @Override
    public double getAndAddDoubleAcquire(Object o, long offset, double delta) {
        return getAndAddDouble(o, offset, delta);
    }

    @Override
    public int getAndSetInt(Object o, long offset, int newValue) {
        return theUnsafe.getAndSetInt(o, offset, newValue);
    }

    @Override
    public int getAndSetIntRelease(Object o, long offset, int newValue) {
        return theUnsafe.getAndSetInt(o, offset, newValue);
    }

    @Override
    public int getAndSetIntAcquire(Object o, long offset, int newValue) {
        return theUnsafe.getAndSetInt(o, offset, newValue);
    }

    @Override
    public long getAndSetLong(Object o, long offset, long newValue) {
        return theUnsafe.getAndSetLong(o, offset, newValue);
    }

    @Override
    public long getAndSetLongRelease(Object o, long offset, long newValue) {
        return theUnsafe.getAndSetLong(o, offset, newValue);
    }

    @Override
    public long getAndSetLongAcquire(Object o, long offset, long newValue) {
        return theUnsafe.getAndSetLong(o, offset, newValue);
    }

    @Override
    public Object getAndSetReference(Object o, long offset, Object newValue) {
        return theUnsafe.getAndSetObject(o, offset, newValue);
    }

    @Override
    public Object getAndSetReferenceRelease(Object o, long offset, Object newValue) {
        return theUnsafe.getAndSetObject(o, offset, newValue);
    }

    @Override
    public Object getAndSetReferenceAcquire(Object o, long offset, Object newValue) {
        return theUnsafe.getAndSetObject(o, offset, newValue);
    }

    @Override
    public byte getAndSetByte(Object o, long offset, byte newValue) {
        byte v;
        do {
            v = getByteVolatile(o, offset);
        } while (!weakCompareAndSetByte(o, offset, v, newValue));
        return v;
    }

    @Override
    public byte getAndSetByteRelease(Object o, long offset, byte newValue) {
        return getAndSetByte(o, offset, newValue);
    }

    @Override
    public byte getAndSetByteAcquire(Object o, long offset, byte newValue) {
        return getAndSetByte(o, offset, newValue);
    }

    @Override
    public boolean getAndSetBoolean(Object o, long offset, boolean newValue) {
        return getAndSetByte(o, offset, newValue ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean getAndSetBooleanRelease(Object o, long offset, boolean newValue) {
        return getAndSetByte(o, offset, newValue ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean getAndSetBooleanAcquire(Object o, long offset, boolean newValue) {
        return getAndSetByte(o, offset, newValue ? (byte) 1 : 0) != 0;
    }

    @Override
    public short getAndSetShort(Object o, long offset, short newValue) {
        short v;
        do {
            v = getShortVolatile(o, offset);
        } while (!weakCompareAndSetShort(o, offset, v, newValue));
        return v;
    }

    @Override
    public short getAndSetShortRelease(Object o, long offset, short newValue) {
        return getAndSetShort(o, offset, newValue);
    }

    @Override
    public short getAndSetShortAcquire(Object o, long offset, short newValue) {
        return getAndSetShort(o, offset, newValue);
    }

    @Override
    public char getAndSetChar(Object o, long offset, char newValue) {
        return (char) getAndSetShort(o, offset, (short) newValue);
    }

    @Override
    public char getAndSetCharRelease(Object o, long offset, char newValue) {
        return (char) getAndSetShort(o, offset, (short) newValue);
    }

    @Override
    public char getAndSetCharAcquire(Object o, long offset, char newValue) {
        return (char) getAndSetShort(o, offset, (short) newValue);
    }

    @Override
    public float getAndSetFloat(Object o, long offset, float newValue) {
        return Float.intBitsToFloat(getAndSetInt(o, offset, Float.floatToRawIntBits(newValue)));
    }

    @Override
    public float getAndSetFloatRelease(Object o, long offset, float newValue) {
        return Float.intBitsToFloat(getAndSetInt(o, offset, Float.floatToRawIntBits(newValue)));
    }

    @Override
    public float getAndSetFloatAcquire(Object o, long offset, float newValue) {
        return Float.intBitsToFloat(getAndSetInt(o, offset, Float.floatToRawIntBits(newValue)));
    }

    @Override
    public double getAndSetDouble(Object o, long offset, double newValue) {
        return Double.longBitsToDouble(getAndSetLong(o, offset, Double.doubleToRawLongBits(newValue)));
    }

    @Override
    public double getAndSetDoubleRelease(Object o, long offset, double newValue) {
        return Double.longBitsToDouble(getAndSetLong(o, offset, Double.doubleToRawLongBits(newValue)));
    }

    @Override
    public double getAndSetDoubleAcquire(Object o, long offset, double newValue) {
        return Double.longBitsToDouble(getAndSetLong(o, offset, Double.doubleToRawLongBits(newValue)));
    }

    @Override
    public boolean getAndBitwiseOrBoolean(Object o, long offset, boolean mask) {
        return getAndBitwiseOrByte(o, offset, mask ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean getAndBitwiseOrBooleanRelease(Object o, long offset, boolean mask) {
        return getAndBitwiseOrByte(o, offset, mask ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean getAndBitwiseOrBooleanAcquire(Object o, long offset, boolean mask) {
        return getAndBitwiseOrByte(o, offset, mask ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean getAndBitwiseAndBoolean(Object o, long offset, boolean mask) {
        return getAndBitwiseAndByte(o, offset, mask ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean getAndBitwiseAndBooleanRelease(Object o, long offset, boolean mask) {
        return getAndBitwiseAndByte(o, offset, mask ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean getAndBitwiseAndBooleanAcquire(Object o, long offset, boolean mask) {
        return getAndBitwiseAndByte(o, offset, mask ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean getAndBitwiseXorBoolean(Object o, long offset, boolean mask) {
        return getAndBitwiseXorByte(o, offset, mask ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean getAndBitwiseXorBooleanRelease(Object o, long offset, boolean mask) {
        return getAndBitwiseXorByte(o, offset, mask ? (byte) 1 : 0) != 0;
    }

    @Override
    public boolean getAndBitwiseXorBooleanAcquire(Object o, long offset, boolean mask) {
        return getAndBitwiseXorByte(o, offset, mask ? (byte) 1 : 0) != 0;
    }

    @Override
    public byte getAndBitwiseOrByte(Object o, long offset, byte mask) {
        byte current;
        do {
            current = getByte(o, offset);
        } while (!weakCompareAndSetByteRelease(o, offset,
                current, (byte) (current | mask)));
        return current;
    }

    @Override
    public byte getAndBitwiseOrByteRelease(Object o, long offset, byte mask) {
        return getAndBitwiseOrByte(o, offset, mask);
    }

    @Override
    public byte getAndBitwiseOrByteAcquire(Object o, long offset, byte mask) {
        return getAndBitwiseOrByte(o, offset, mask);
    }

    @Override
    public byte getAndBitwiseAndByte(Object o, long offset, byte mask) {
        byte current;
        do {
            current = getByte(o, offset);
        } while (!weakCompareAndSetByteRelease(o, offset,
                current, (byte) (current & mask)));
        return current;
    }

    @Override
    public byte getAndBitwiseAndByteRelease(Object o, long offset, byte mask) {
        return getAndBitwiseAndByte(o, offset, mask);
    }

    @Override
    public byte getAndBitwiseAndByteAcquire(Object o, long offset, byte mask) {
        return getAndBitwiseAndByte(o, offset, mask);
    }

    @Override
    public byte getAndBitwiseXorByte(Object o, long offset, byte mask) {
        byte current;
        do {
            current = getByteVolatile(o, offset);
        } while (!weakCompareAndSetByte(o, offset,
                current, (byte) (current ^ mask)));
        return current;
    }

    @Override
    public byte getAndBitwiseXorByteRelease(Object o, long offset, byte mask) {
        return getAndBitwiseXorByte(o, offset, mask);
    }

    @Override
    public byte getAndBitwiseXorByteAcquire(Object o, long offset, byte mask) {
        return getAndBitwiseXorByte(o, offset, mask);
    }

    @Override
    public char getAndBitwiseOrChar(Object o, long offset, char mask) {
        return (char) getAndBitwiseOrShort(o, offset, (short) mask);
    }

    @Override
    public char getAndBitwiseOrCharRelease(Object o, long offset, char mask) {
        return (char) getAndBitwiseOrShort(o, offset, (short) mask);
    }

    @Override
    public char getAndBitwiseOrCharAcquire(Object o, long offset, char mask) {
        return (char) getAndBitwiseOrShort(o, offset, (short) mask);
    }

    @Override
    public char getAndBitwiseAndChar(Object o, long offset, char mask) {
        return (char) getAndBitwiseAndShort(o, offset, (short) mask);
    }

    @Override
    public char getAndBitwiseAndCharRelease(Object o, long offset, char mask) {
        return (char) getAndBitwiseAndShort(o, offset, (short) mask);
    }

    @Override
    public char getAndBitwiseAndCharAcquire(Object o, long offset, char mask) {
        return (char) getAndBitwiseAndShort(o, offset, (short) mask);
    }

    @Override
    public char getAndBitwiseXorChar(Object o, long offset, char mask) {
        return (char) getAndBitwiseXorShort(o, offset, (short) mask);
    }

    @Override
    public char getAndBitwiseXorCharRelease(Object o, long offset, char mask) {
        return (char) getAndBitwiseXorShort(o, offset, (short) mask);
    }

    @Override
    public char getAndBitwiseXorCharAcquire(Object o, long offset, char mask) {
        return (char) getAndBitwiseXorShort(o, offset, (short) mask);
    }

    @Override
    public short getAndBitwiseOrShort(Object o, long offset, short mask) {
        short current;
        do {
            current = getShortVolatile(o, offset);
        } while (!weakCompareAndSetShort(o, offset,
                current, (short) (current | mask)));
        return current;
    }

    @Override
    public short getAndBitwiseOrShortRelease(Object o, long offset, short mask) {
        return getAndBitwiseOrShort(o, offset, mask);
    }

    @Override
    public short getAndBitwiseOrShortAcquire(Object o, long offset, short mask) {
        return getAndBitwiseOrShort(o, offset, mask);
    }

    @Override
    public short getAndBitwiseAndShort(Object o, long offset, short mask) {
        short current;
        do {
            current = getShortVolatile(o, offset);
        } while (!weakCompareAndSetShort(o, offset,
                current, (short) (current & mask)));
        return current;
    }

    @Override
    public short getAndBitwiseAndShortRelease(Object o, long offset, short mask) {
        return getAndBitwiseAndShort(o, offset, mask);
    }

    @Override
    public short getAndBitwiseAndShortAcquire(Object o, long offset, short mask) {
        return getAndBitwiseAndShort(o, offset, mask);
    }

    @Override
    public short getAndBitwiseXorShort(Object o, long offset, short mask) {
        short current;
        do {
            current = getShortVolatile(o, offset);
        } while (!weakCompareAndSetShort(o, offset,
                current, (short) (current ^ mask)));
        return current;
    }

    @Override
    public short getAndBitwiseXorShortRelease(Object o, long offset, short mask) {
        return getAndBitwiseXorShort(o, offset, mask);
    }

    @Override
    public short getAndBitwiseXorShortAcquire(Object o, long offset, short mask) {
        return getAndBitwiseXorShort(o, offset, mask);
    }

    @Override
    public int getAndBitwiseOrInt(Object o, long offset, int mask) {
        int current;
        do {
            current = getIntVolatile(o, offset);
        } while (!weakCompareAndSetInt(o, offset,
                current, current | mask));
        return current;
    }

    @Override
    public int getAndBitwiseOrIntRelease(Object o, long offset, int mask) {
        return getAndBitwiseOrInt(o, offset, mask);
    }

    @Override
    public int getAndBitwiseOrIntAcquire(Object o, long offset, int mask) {
        return getAndBitwiseOrInt(o, offset, mask);
    }

    @Override
    public int getAndBitwiseAndInt(Object o, long offset, int mask) {
        int current;
        do {
            current = getIntVolatile(o, offset);
        } while (!weakCompareAndSetInt(o, offset,
                current, current & mask));
        return current;
    }

    @Override
    public int getAndBitwiseAndIntRelease(Object o, long offset, int mask) {
        return getAndBitwiseAndInt(o, offset, mask);
    }

    @Override
    public int getAndBitwiseAndIntAcquire(Object o, long offset, int mask) {
        return getAndBitwiseAndInt(o, offset, mask);
    }

    @Override
    public int getAndBitwiseXorInt(Object o, long offset, int mask) {
        int current;
        do {
            current = getIntVolatile(o, offset);
        } while (!weakCompareAndSetInt(o, offset,
                current, current ^ mask));
        return current;
    }

    @Override
    public int getAndBitwiseXorIntRelease(Object o, long offset, int mask) {
        return getAndBitwiseXorInt(o, offset, mask);
    }

    @Override
    public int getAndBitwiseXorIntAcquire(Object o, long offset, int mask) {
        return getAndBitwiseXorInt(o, offset, mask);
    }

    @Override
    public long getAndBitwiseOrLong(Object o, long offset, long mask) {
        long current;
        do {
            current = getLongVolatile(o, offset);
        } while (!weakCompareAndSetLong(o, offset,
                current, current | mask));
        return current;
    }

    @Override
    public long getAndBitwiseOrLongRelease(Object o, long offset, long mask) {
        return getAndBitwiseOrLong(o, offset, mask);
    }

    @Override
    public long getAndBitwiseOrLongAcquire(Object o, long offset, long mask) {
        return getAndBitwiseOrLong(o, offset, mask);
    }

    @Override
    public long getAndBitwiseAndLong(Object o, long offset, long mask) {
        long current;
        do {
            current = getLongVolatile(o, offset);
        } while (!weakCompareAndSetLong(o, offset,
                current, current & mask));
        return current;
    }

    @Override
    public long getAndBitwiseAndLongRelease(Object o, long offset, long mask) {
        return getAndBitwiseAndLong(o, offset, mask);
    }

    @Override
    public long getAndBitwiseAndLongAcquire(Object o, long offset, long mask) {
        return getAndBitwiseAndLong(o, offset, mask);
    }

    @Override
    public long getAndBitwiseXorLong(Object o, long offset, long mask) {
        long current;
        do {
            current = getLongVolatile(o, offset);
        } while (!weakCompareAndSetLong(o, offset,
                current, current ^ mask));
        return current;
    }

    @Override
    public long getAndBitwiseXorLongRelease(Object o, long offset, long mask) {
        return getAndBitwiseXorLong(o, offset, mask);
    }

    @Override
    public long getAndBitwiseXorLongAcquire(Object o, long offset, long mask) {
        return getAndBitwiseXorLong(o, offset, mask);
    }

    @Override
    public void loadFence() {
        theUnsafe.loadFence();
    }

    @Override
    public void storeFence() {
        theUnsafe.storeFence();
    }

    @Override
    public void fullFence() {
        theUnsafe.fullFence();
    }

    @Override
    public void loadLoadFence() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void storeStoreFence() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBigEndian() {
        return BIG_ENDIAN;
    }

    private static final boolean BIG_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;

    @Override
    public boolean unalignedAccess() {
        return false;
    }

    // region unaligned

    private static int pickPos(int top, int pos) {
        return BIG_ENDIAN ? top - pos : pos;
    }

    // These methods construct integers from bytes.  The byte ordering
    // is the native endianness of this platform.
    private static long makeLong(byte i0, byte i1, byte i2, byte i3, byte i4, byte i5, byte i6, byte i7) {
        return ((toUnsignedLong(i0) << pickPos(56, 0))
                | (toUnsignedLong(i1) << pickPos(56, 8))
                | (toUnsignedLong(i2) << pickPos(56, 16))
                | (toUnsignedLong(i3) << pickPos(56, 24))
                | (toUnsignedLong(i4) << pickPos(56, 32))
                | (toUnsignedLong(i5) << pickPos(56, 40))
                | (toUnsignedLong(i6) << pickPos(56, 48))
                | (toUnsignedLong(i7) << pickPos(56, 56)));
    }

    private static long makeLong(short i0, short i1, short i2, short i3) {
        return ((toUnsignedLong(i0) << pickPos(48, 0))
                | (toUnsignedLong(i1) << pickPos(48, 16))
                | (toUnsignedLong(i2) << pickPos(48, 32))
                | (toUnsignedLong(i3) << pickPos(48, 48)));
    }

    private static long makeLong(int i0, int i1) {
        return (toUnsignedLong(i0) << pickPos(32, 0))
                | (toUnsignedLong(i1) << pickPos(32, 32));
    }

    private static int makeInt(short i0, short i1) {
        return (toUnsignedInt(i0) << pickPos(16, 0))
                | (toUnsignedInt(i1) << pickPos(16, 16));
    }

    private static int makeInt(byte i0, byte i1, byte i2, byte i3) {
        return ((toUnsignedInt(i0) << pickPos(24, 0))
                | (toUnsignedInt(i1) << pickPos(24, 8))
                | (toUnsignedInt(i2) << pickPos(24, 16))
                | (toUnsignedInt(i3) << pickPos(24, 24)));
    }

    private static short makeShort(byte i0, byte i1) {
        return (short) ((toUnsignedInt(i0) << pickPos(8, 0))
                | (toUnsignedInt(i1) << pickPos(8, 8)));
    }

    private static byte pick(byte le, byte be) {
        return BIG_ENDIAN ? be : le;
    }

    private static short pick(short le, short be) {
        return BIG_ENDIAN ? be : le;
    }

    private static int pick(int le, int be) {
        return BIG_ENDIAN ? be : le;
    }

    // These methods write integers to memory from smaller parts
    // provided by their caller.  The ordering in which these parts
    // are written is the native endianness of this platform.
    private void putLongParts(Object o, long offset, byte i0, byte i1, byte i2, byte i3, byte i4, byte i5, byte i6, byte i7) {
        putByte(o, offset + 0, pick(i0, i7));
        putByte(o, offset + 1, pick(i1, i6));
        putByte(o, offset + 2, pick(i2, i5));
        putByte(o, offset + 3, pick(i3, i4));
        putByte(o, offset + 4, pick(i4, i3));
        putByte(o, offset + 5, pick(i5, i2));
        putByte(o, offset + 6, pick(i6, i1));
        putByte(o, offset + 7, pick(i7, i0));
    }

    private void putLongParts(Object o, long offset, short i0, short i1, short i2, short i3) {
        putShort(o, offset + 0, pick(i0, i3));
        putShort(o, offset + 2, pick(i1, i2));
        putShort(o, offset + 4, pick(i2, i1));
        putShort(o, offset + 6, pick(i3, i0));
    }

    private void putLongParts(Object o, long offset, int i0, int i1) {
        putInt(o, offset + 0, pick(i0, i1));
        putInt(o, offset + 4, pick(i1, i0));
    }

    private void putIntParts(Object o, long offset, short i0, short i1) {
        putShort(o, offset + 0, pick(i0, i1));
        putShort(o, offset + 2, pick(i1, i0));
    }

    private void putIntParts(Object o, long offset, byte i0, byte i1, byte i2, byte i3) {
        putByte(o, offset + 0, pick(i0, i3));
        putByte(o, offset + 1, pick(i1, i2));
        putByte(o, offset + 2, pick(i2, i1));
        putByte(o, offset + 3, pick(i3, i0));
    }

    private void putShortParts(Object o, long offset, byte i0, byte i1) {
        putByte(o, offset + 0, pick(i0, i1));
        putByte(o, offset + 1, pick(i1, i0));
    }

    // Zero-extend an integer
    private static int toUnsignedInt(byte n) {
        return n & 0xff;
    }

    private static int toUnsignedInt(short n) {
        return n & 0xffff;
    }

    private static long toUnsignedLong(byte n) {
        return n & 0xffl;
    }

    private static long toUnsignedLong(short n) {
        return n & 0xffffl;
    }

    private static long toUnsignedLong(int n) {
        return n & 0xffffffffl;
    }

    // Maybe byte-reverse an integer
    private static char convEndian(boolean big, char n) {
        return big == BIG_ENDIAN ? n : Character.reverseBytes(n);
    }

    private static short convEndian(boolean big, short n) {
        return big == BIG_ENDIAN ? n : Short.reverseBytes(n);
    }

    private static int convEndian(boolean big, int n) {
        return big == BIG_ENDIAN ? n : Integer.reverseBytes(n);
    }

    private static long convEndian(boolean big, long n) {
        return big == BIG_ENDIAN ? n : Long.reverseBytes(n);
    }

    public final long getLongUnaligned(Object o, long offset) {
        if ((offset & 7) == 0) {
            return getLong(o, offset);
        } else if ((offset & 3) == 0) {
            return makeLong(getInt(o, offset),
                    getInt(o, offset + 4));
        } else if ((offset & 1) == 0) {
            return makeLong(getShort(o, offset),
                    getShort(o, offset + 2),
                    getShort(o, offset + 4),
                    getShort(o, offset + 6));
        } else {
            return makeLong(getByte(o, offset),
                    getByte(o, offset + 1),
                    getByte(o, offset + 2),
                    getByte(o, offset + 3),
                    getByte(o, offset + 4),
                    getByte(o, offset + 5),
                    getByte(o, offset + 6),
                    getByte(o, offset + 7));
        }
    }

    public final long getLongUnaligned(Object o, long offset, boolean bigEndian) {
        return convEndian(bigEndian, getLongUnaligned(o, offset));
    }

    public final int getIntUnaligned(Object o, long offset) {
        if ((offset & 3) == 0) {
            return getInt(o, offset);
        } else if ((offset & 1) == 0) {
            return makeInt(getShort(o, offset),
                    getShort(o, offset + 2));
        } else {
            return makeInt(getByte(o, offset),
                    getByte(o, offset + 1),
                    getByte(o, offset + 2),
                    getByte(o, offset + 3));
        }
    }

    public final int getIntUnaligned(Object o, long offset, boolean bigEndian) {
        return convEndian(bigEndian, getIntUnaligned(o, offset));
    }

    public final short getShortUnaligned(Object o, long offset) {
        if ((offset & 1) == 0) {
            return getShort(o, offset);
        } else {
            return makeShort(getByte(o, offset),
                    getByte(o, offset + 1));
        }
    }

    public final short getShortUnaligned(Object o, long offset, boolean bigEndian) {
        return convEndian(bigEndian, getShortUnaligned(o, offset));
    }

    public final char getCharUnaligned(Object o, long offset) {
        if ((offset & 1) == 0) {
            return getChar(o, offset);
        } else {
            return (char) makeShort(getByte(o, offset),
                    getByte(o, offset + 1));
        }
    }

    public final char getCharUnaligned(Object o, long offset, boolean bigEndian) {
        return convEndian(bigEndian, getCharUnaligned(o, offset));
    }

    public final void putLongUnaligned(Object o, long offset, long x) {
        if ((offset & 7) == 0) {
            putLong(o, offset, x);
        } else if ((offset & 3) == 0) {
            putLongParts(o, offset,
                    (int) (x >> 0),
                    (int) (x >>> 32));
        } else if ((offset & 1) == 0) {
            putLongParts(o, offset,
                    (short) (x >>> 0),
                    (short) (x >>> 16),
                    (short) (x >>> 32),
                    (short) (x >>> 48));
        } else {
            putLongParts(o, offset,
                    (byte) (x >>> 0),
                    (byte) (x >>> 8),
                    (byte) (x >>> 16),
                    (byte) (x >>> 24),
                    (byte) (x >>> 32),
                    (byte) (x >>> 40),
                    (byte) (x >>> 48),
                    (byte) (x >>> 56));
        }
    }

    public final void putLongUnaligned(Object o, long offset, long x, boolean bigEndian) {
        putLongUnaligned(o, offset, convEndian(bigEndian, x));
    }

    public final void putIntUnaligned(Object o, long offset, int x) {
        if ((offset & 3) == 0) {
            putInt(o, offset, x);
        } else if ((offset & 1) == 0) {
            putIntParts(o, offset,
                    (short) (x >> 0),
                    (short) (x >>> 16));
        } else {
            putIntParts(o, offset,
                    (byte) (x >>> 0),
                    (byte) (x >>> 8),
                    (byte) (x >>> 16),
                    (byte) (x >>> 24));
        }
    }

    public final void putIntUnaligned(Object o, long offset, int x, boolean bigEndian) {
        putIntUnaligned(o, offset, convEndian(bigEndian, x));
    }

    public final void putShortUnaligned(Object o, long offset, short x) {
        if ((offset & 1) == 0) {
            putShort(o, offset, x);
        } else {
            putShortParts(o, offset,
                    (byte) (x >>> 0),
                    (byte) (x >>> 8));
        }
    }

    public final void putShortUnaligned(Object o, long offset, short x, boolean bigEndian) {
        putShortUnaligned(o, offset, convEndian(bigEndian, x));
    }

    public final void putCharUnaligned(Object o, long offset, char x) {
        putShortUnaligned(o, offset, (short) x);
    }

    public final void putCharUnaligned(Object o, long offset, char x, boolean bigEndian) {
        putCharUnaligned(o, offset, convEndian(bigEndian, x));
    }

    @Override
    public void invokeCleaner(ByteBuffer directBuffer) {
    }
    // endregion

}
