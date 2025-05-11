/*
 * Copyright (c) 2000, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

//package jdk.internal.misc;
package moe.karla.usf.unsafe;

import moe.karla.usf.security.RootSecurity;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;


/**
 * A collection of methods for performing low-level, unsafe operations.
 * Although the class and all methods are public, use of this class is
 * limited because only trusted code can obtain instances of it.
 *
 * <em>Note:</em> It is the resposibility of the caller to make sure
 * arguments are checked before methods of this class are
 * called. While some rudimentary checks are performed on the input,
 * the checks are best effort and when performance is an overriding
 * priority, as when methods of this class are optimized by the
 * runtime compiler, some or all checks (if any) may be elided. Hence,
 * the caller must not rely on the checks and corresponding
 * exceptions!
 *
 * @author John R. Rose
 * @see #getUnsafe0
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "RedundantSuppression", "Since15", "DefaultAnnotationParam", "JavaDoc"})
public abstract class Unsafe {
    static Unsafe theUnsafe;
    static final Unsafe INSTANCE = getUnsafe0();

    /**
     * is Java9+
     *
     * @return true if providing jdk.internal.misc.Unsafe
     * @since 1.1.0
     */
    @Contract(pure = true)
    public boolean isJava9() {
        return false;
    }

    /**
     * Get the original unsafe object.
     *
     * @since 1.7.0
     */
    @Contract(pure = true)
    public abstract Object getOriginalUnsafe();

    /**
     * Provides the caller with the capability of performing unsafe
     * operations.
     *
     * <p>The returned {@code Unsafe} object should be carefully guarded
     * by the caller, since it can be used to read and write data at arbitrary
     * memory addresses.  It must never be passed to untrusted code.
     *
     * <p>Most methods in this class are very low-level, and correspond to a
     * small number of hardware instructions (on typical machines).  Compilers
     * are encouraged to optimize these methods accordingly.
     *
     * <p>Here is a suggested idiom for using unsafe operations:
     *
     * <pre> {@code
     * class MyTrustedClass {
     *   private static abstract Unsafe unsafe = Unsafe.getUnsafe();
     *   ...
     *   private long myCountAddress = ...;
     *   public int getCount() { return unsafe.getByte(myCountAddress); }
     * }}</pre>
     * <p>
     * (It may assist compilers to make the local variable {@code final}.)
     */
    @Contract(pure = false)
    public static Unsafe getUnsafe() {
        RootSecurity.check(RootSecurity.Type.UNSAFE_INSTANCE);
        return INSTANCE;
    }

    protected Unsafe() {
    }

    static Unsafe getUnsafe0() {
        if (theUnsafe == null) {
            UnsafeInitializer.initialize();
        }
        return theUnsafe;
    }

    /// peek and poke operations
    /// (compilers should optimize these to memory ops)

    // These work on object fields in the Java heap.
    // They will not work on elements of packed arrays.

    /**
     * Fetches a value from a given Java variable.
     * More specifically, fetches a field or array element within the given
     * object {@code o} at the given offset, or (if {@code o} is null)
     * from the memory address whose numerical value is the given offset.
     * <p>
     * The results are undefined unless one of the following cases is true:
     * <ul>
     * <li>The offset was obtained from {@link #objectFieldOffset} on
     * the {@link Field} of some Java field and the object
     * referred to by {@code o} is of a class compatible with that
     * field's class.
     *
     * <li>The offset and object reference {@code o} (either null or
     * non-null) were both obtained via {@link #staticFieldOffset}
     * and {@link #staticFieldBase} (respectively) from the
     * reflective {@link Field} representation of some Java field.
     *
     * <li>The object referred to by {@code o} is an array, and the offset
     * is an integer of the form {@code B+N*S}, where {@code N} is
     * a valid index into the array, and {@code B} and {@code S} are
     * the values obtained by {@link #arrayBaseOffset} and {@link
     * #arrayIndexScale} (respectively) from the array's class.  The value
     * referred to is the {@code N}<em>th</em> element of the array.
     *
     * </ul>
     * <p>
     * If one of the above cases is true, the call references a specific Java
     * variable (field or array element).  However, the results are undefined
     * if that variable is not in fact of the type returned by this method.
     * <p>
     * This method refers to a variable by means of two parameters, and so
     * it provides (in effect) a <em>double-register</em> addressing mode
     * for Java variables.  When the object reference is null, this method
     * uses its offset as an absolute address.  This is similar in operation
     * to methods such as {@link #getInt(long)}, which provide (in effect) a
     * <em>single-register</em> addressing mode for non-Java variables.
     * However, because Java variables may have a different layout in memory
     * from non-Java variables, programmers should not assume that these
     * two addressing modes are ever equivalent.  Also, programmers should
     * remember that offsets from the double-register addressing mode cannot
     * be portably confused with longs used in the single-register addressing
     * mode.
     *
     * @param o      Java heap object in which the variable resides, if any, else
     *               null
     * @param offset indication of where the variable resides in a Java heap
     *               object, if any, else a memory address locating the variable
     *               statically
     * @return the value fetched from the indicated Java variable
     * @throws RuntimeException No defined exceptions are thrown, not even
     *                          {@link NullPointerException}
     */
    public abstract int getInt(Object o, long offset);

    /**
     * Stores a value into a given Java variable.
     * <p>
     * The first two parameters are interpreted exactly as with
     * {@link #getInt(Object, long)} to refer to a specific
     * Java variable (field or array element).  The given value
     * is stored into that variable.
     * <p>
     * The variable must be of the same type as the method
     * parameter {@code x}.
     *
     * @param o      Java heap object in which the variable resides, if any, else
     *               null
     * @param offset indication of where the variable resides in a Java heap
     *               object, if any, else a memory address locating the variable
     *               statically
     * @param x      the value to store into the indicated Java variable
     * @throws RuntimeException No defined exceptions are thrown, not even
     *                          {@link NullPointerException}
     */
    public abstract void putInt(Object o, long offset, int x);

    /**
     * Fetches a reference value from a given Java variable.
     *
     * @see #getInt(Object, long)
     */
    public abstract Object getReference(Object o, long offset);

    /**
     * Stores a reference value into a given Java variable.
     * <p>
     * Unless the reference {@code x} being stored is either null
     * or matches the field type, the results are undefined.
     * If the reference {@code o} is non-null, card marks or
     * other store barriers for that object (if the VM requires them)
     * are updated.
     *
     * @see #putInt(Object, long, int)
     */
    public abstract void putReference(Object o, long offset, Object x);

    /**
     * @see #getInt(Object, long)
     */
    public abstract boolean getBoolean(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public abstract void putBoolean(Object o, long offset, boolean x);

    /**
     * @see #getInt(Object, long)
     */
    public abstract byte getByte(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public abstract void putByte(Object o, long offset, byte x);

    /**
     * @see #getInt(Object, long)
     */
    public abstract short getShort(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public abstract void putShort(Object o, long offset, short x);

    /**
     * @see #getInt(Object, long)
     */
    public abstract char getChar(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public abstract void putChar(Object o, long offset, char x);

    /**
     * @see #getInt(Object, long)
     */
    public abstract long getLong(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public abstract void putLong(Object o, long offset, long x);

    /**
     * @see #getInt(Object, long)
     */
    public abstract float getFloat(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public abstract void putFloat(Object o, long offset, float x);

    /**
     * @see #getInt(Object, long)
     */
    public abstract double getDouble(Object o, long offset);

    /**
     * @see #putInt(Object, long, int)
     */
    public abstract void putDouble(Object o, long offset, double x);

    /**
     * Fetches a abstract pointer from a given memory address.  If the address is
     * zero, or does not point into a block obtained from {@link
     * #allocateMemory}, the results are undefined.
     *
     * <p>If the abstract pointer is less than 64 bits wide, it is extended as
     * an unsigned number to a Java long.  The pointer may be indexed by any
     * given byte offset, simply by adding that offset (as a simple integer) to
     * the long representing the pointer.  The number of bytes actually read
     * from the target address may be determined by consulting {@link
     * #addressSize}.
     *
     * @see #allocateMemory
     * @see #getInt(Object, long)
     */
    public long getAddress(Object o, long offset) {
        if (ADDRESS_SIZE == 4) {
            return Integer.toUnsignedLong(getInt(o, offset));
        } else {
            return getLong(o, offset);
        }
    }

    /**
     * Stores a abstract pointer into a given memory address.  If the address is
     * zero, or does not point into a block obtained from {@link
     * #allocateMemory}, the results are undefined.
     *
     * <p>The number of bytes actually written at the target address may be
     * determined by consulting {@link #addressSize}.
     *
     * @see #allocateMemory
     * @see #putInt(Object, long, int)
     */
    public void putAddress(Object o, long offset, long x) {
        if (ADDRESS_SIZE == 4) {
            putInt(o, offset, (int) x);
        } else {
            putLong(o, offset, x);
        }
    }

    // These read VM internal data.

    /**
     * Fetches an uncompressed reference value from a given abstract variable
     * ignoring the VM's compressed references mode.
     *
     * @param address a memory address locating the variable
     * @return the value fetched from the indicated abstract variable
     */
    public abstract Object getUncompressedObject(long address);

    // These work on values in the C heap.

    /**
     * Fetches a value from a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #allocateMemory
     */
    public abstract byte getByte(long address);

    /**
     * Stores a value into a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #getByte(long)
     */
    public abstract void putByte(long address, byte x);

    /**
     * @see #getByte(long)
     */
    public abstract short getShort(long address);

    /**
     * @see #putByte(long, byte)
     */
    public abstract void putShort(long address, short x);

    /**
     * @see #getByte(long)
     */
    public abstract char getChar(long address);

    /**
     * @see #putByte(long, byte)
     */
    public abstract void putChar(long address, char x);

    /**
     * @see #getByte(long)
     */
    public abstract int getInt(long address);

    /**
     * @see #putByte(long, byte)
     */
    public abstract void putInt(long address, int x);

    /**
     * @see #getByte(long)
     */
    public abstract long getLong(long address);

    /**
     * @see #putByte(long, byte)
     */
    public abstract void putLong(long address, long x);

    /**
     * @see #getByte(long)
     */
    public abstract float getFloat(long address);

    /**
     * @see #putByte(long, byte)
     */
    public abstract void putFloat(long address, float x);

    /**
     * @see #getByte(long)
     */
    public abstract double getDouble(long address);

    /**
     * @see #putByte(long, byte)
     */
    public abstract void putDouble(long address, double x);

    /**
     * @see #getAddress(Object, long)
     */
    public abstract long getAddress(long address);

    /**
     * @see #putAddress(Object, long, long)
     */
    public abstract void putAddress(long address, long x);


    /// wrappers for malloc, realloc, free:

    /**
     * Allocates a new block of abstract memory, of the given size in bytes.  The
     * contents of the memory are uninitialized; they will generally be
     * garbage.  The resulting abstract pointer will never be zero, and will be
     * aligned for all value types.  Dispose of this memory by calling {@link
     * #freeMemory}, or resize it with {@link #reallocateMemory}.
     *
     * <em>Note:</em> It is the resposibility of the caller to make
     * sure arguments are checked before the methods are called. While
     * some rudimentary checks are performed on the input, the checks
     * are best effort and when performance is an overriding priority,
     * as when methods of this class are optimized by the runtime
     * compiler, some or all checks (if any) may be elided. Hence, the
     * caller must not rely on the checks and corresponding
     * exceptions!
     *
     * @throws RuntimeException if the size is negative or too large
     *                          for the abstract size_t type
     * @throws OutOfMemoryError if the allocation is refused by the system
     * @see #getByte(long)
     * @see #putByte(long, byte)
     */
    public abstract long allocateMemory(long bytes);

    /**
     * Resizes a new block of abstract memory, to the given size in bytes.  The
     * contents of the new block past the size of the old block are
     * uninitialized; they will generally be garbage.  The resulting native
     * pointer will be zero if and only if the requested size is zero.  The
     * resulting abstract pointer will be aligned for all value types.  Dispose
     * of this memory by calling {@link #freeMemory}, or resize it with {@link
     * #reallocateMemory}.  The address passed to this method may be null, in
     * which case an allocation will be performed.
     *
     * <em>Note:</em> It is the resposibility of the caller to make
     * sure arguments are checked before the methods are called. While
     * some rudimentary checks are performed on the input, the checks
     * are best effort and when performance is an overriding priority,
     * as when methods of this class are optimized by the runtime
     * compiler, some or all checks (if any) may be elided. Hence, the
     * caller must not rely on the checks and corresponding
     * exceptions!
     *
     * @throws RuntimeException if the size is negative or too large
     *                          for the abstract size_t type
     * @throws OutOfMemoryError if the allocation is refused by the system
     * @see #allocateMemory
     */
    public abstract long reallocateMemory(long address, long bytes);


    /**
     * Sets all bytes in a given block of memory to a fixed value
     * (usually zero).
     *
     * <p>This method determines a block's base address by means of two parameters,
     * and so it provides (in effect) a <em>double-register</em> addressing mode,
     * as discussed in {@link #getInt(Object, long)}.  When the object reference is null,
     * the offset supplies an absolute base address.
     *
     * <p>The stores are in coherent (atomic) units of a size determined
     * by the address and length parameters.  If the effective address and
     * length are all even modulo 8, the stores take place in 'long' units.
     * If the effective address and length are (resp.) even modulo 4 or 2,
     * the stores take place in units of 'int' or 'short'.
     *
     * <em>Note:</em> It is the resposibility of the caller to make
     * sure arguments are checked before the methods are called. While
     * some rudimentary checks are performed on the input, the checks
     * are best effort and when performance is an overriding priority,
     * as when methods of this class are optimized by the runtime
     * compiler, some or all checks (if any) may be elided. Hence, the
     * caller must not rely on the checks and corresponding
     * exceptions!
     *
     * @throws RuntimeException if any of the arguments is invalid
     * @since 1.7
     */
    public abstract void setMemory(Object o, long offset, long bytes, byte value);

    /**
     * Sets all bytes in a given block of memory to a fixed value
     * (usually zero).  This provides a <em>single-register</em> addressing mode,
     * as discussed in {@link #getInt(Object, long)}.
     *
     * <p>Equivalent to {@code setMemory(null, address, bytes, value)}.
     */
    public abstract void setMemory(long address, long bytes, byte value);


    /**
     * Sets all bytes in a given block of memory to a copy of another
     * block.
     *
     * <p>This method determines each block's base address by means of two parameters,
     * and so it provides (in effect) a <em>double-register</em> addressing mode,
     * as discussed in {@link #getInt(Object, long)}.  When the object reference is null,
     * the offset supplies an absolute base address.
     *
     * <p>The transfers are in coherent (atomic) units of a size determined
     * by the address and length parameters.  If the effective addresses and
     * length are all even modulo 8, the transfer takes place in 'long' units.
     * If the effective addresses and length are (resp.) even modulo 4 or 2,
     * the transfer takes place in units of 'int' or 'short'.
     *
     * <em>Note:</em> It is the resposibility of the caller to make
     * sure arguments are checked before the methods are called. While
     * some rudimentary checks are performed on the input, the checks
     * are best effort and when performance is an overriding priority,
     * as when methods of this class are optimized by the runtime
     * compiler, some or all checks (if any) may be elided. Hence, the
     * caller must not rely on the checks and corresponding
     * exceptions!
     *
     * @throws RuntimeException if any of the arguments is invalid
     * @since 1.7
     */
    public abstract void copyMemory(Object srcBase, long srcOffset,
                                    Object destBase, long destOffset,
                                    long bytes);

    /**
     * Sets all bytes in a given block of memory to a copy of another
     * block.  This provides a <em>single-register</em> addressing mode,
     * as discussed in {@link #getInt(Object, long)}.
     * <p>
     * Equivalent to {@code copyMemory(null, srcAddress, null, destAddress, bytes)}.
     */
    public abstract void copyMemory(long srcAddress, long destAddress, long bytes);

    /**
     * Copies all elements from one block of memory to another block,
     * *unconditionally* byte swapping the elements on the fly.
     *
     * <p>This method determines each block's base address by means of two parameters,
     * and so it provides (in effect) a <em>double-register</em> addressing mode,
     * as discussed in {@link #getInt(Object, long)}.  When the object reference is null,
     * the offset supplies an absolute base address.
     *
     * <em>Note:</em> It is the resposibility of the caller to make
     * sure arguments are checked before the methods are called. While
     * some rudimentary checks are performed on the input, the checks
     * are best effort and when performance is an overriding priority,
     * as when methods of this class are optimized by the runtime
     * compiler, some or all checks (if any) may be elided. Hence, the
     * caller must not rely on the checks and corresponding
     * exceptions!
     *
     * @throws RuntimeException if any of the arguments is invalid
     * @since 9
     */
    public abstract void copySwapMemory(Object srcBase, long srcOffset,
                                        Object destBase, long destOffset,
                                        long bytes, long elemSize);

    /**
     * Copies all elements from one block of memory to another block, byte swapping the
     * elements on the fly.
     * <p>
     * This provides a <em>single-register</em> addressing mode, as
     * discussed in {@link #getInt(Object, long)}.
     * <p>
     * Equivalent to {@code copySwapMemory(null, srcAddress, null, destAddress, bytes, elemSize)}.
     */
    public abstract void copySwapMemory(long srcAddress, long destAddress, long bytes, long elemSize);

    /**
     * Disposes of a block of abstract memory, as obtained from {@link
     * #allocateMemory} or {@link #reallocateMemory}.  The address passed to
     * this method may be null, in which case no action is taken.
     *
     * <em>Note:</em> It is the resposibility of the caller to make
     * sure arguments are checked before the methods are called. While
     * some rudimentary checks are performed on the input, the checks
     * are best effort and when performance is an overriding priority,
     * as when methods of this class are optimized by the runtime
     * compiler, some or all checks (if any) may be elided. Hence, the
     * caller must not rely on the checks and corresponding
     * exceptions!
     *
     * @throws RuntimeException if any of the arguments is invalid
     * @see #allocateMemory
     */
    public abstract void freeMemory(long address);

    /// random queries

    /**
     * This constant differs from all results that will ever be returned from
     * {@link #staticFieldOffset}, {@link #objectFieldOffset},
     * or {@link #arrayBaseOffset}.
     */
    public static final int INVALID_FIELD_OFFSET = -1;

    /**
     * Reports the location of a given field in the storage allocation of its
     * class.  Do not expect to perform any sort of arithmetic on this offset;
     * it is just a cookie which is passed to the unsafe heap memory accessors.
     *
     * <p>Any given field will always have the same offset and base, and no
     * two distinct fields of the same class will ever have the same offset
     * and base.
     *
     * <p>As of 1.4.1, offsets for fields are represented as long values,
     * although the Sun JVM does not use the most significant 32 bits.
     * However, JVM implementations which store static fields at absolute
     * addresses can use long offsets and null base pointers to express
     * the field locations in a form usable by {@link #getInt(Object, long)}.
     * Therefore, code which will be ported to such JVMs on 64-bit platforms
     * must preserve all bits of static field offsets.
     *
     * @see #getInt(Object, long)
     */
    public abstract long objectFieldOffset(Field f);

    /**
     * Reports the location of the field with a given name in the storage
     * allocation of its class.
     *
     * @throws NullPointerException if any parameter is {@code null}.
     * @throws InternalError        if there is no field named {@code name} declared
     *                              in class {@code c}, i.e., if {@code c.getDeclaredField(name)}
     *                              would throw {@code java.lang.NoSuchFieldException}.
     * @see #objectFieldOffset(Field)
     */
    public abstract long objectFieldOffset(Class<?> c, String name);

    /**
     * Reports the location of a given static field, in conjunction with {@link
     * #staticFieldBase}.
     * <p>Do not expect to perform any sort of arithmetic on this offset;
     * it is just a cookie which is passed to the unsafe heap memory accessors.
     *
     * <p>Any given field will always have the same offset, and no two distinct
     * fields of the same class will ever have the same offset.
     *
     * <p>As of 1.4.1, offsets for fields are represented as long values,
     * although the Sun JVM does not use the most significant 32 bits.
     * It is hard to imagine a JVM technology which needs more than
     * a few bits to encode an offset within a non-array object,
     * However, for consistency with other methods in this class,
     * this method reports its result as a long value.
     *
     * @see #getInt(Object, long)
     */
    public abstract long staticFieldOffset(Field f);

    /**
     * Reports the location of a given static field, in conjunction with {@link
     * #staticFieldOffset}.
     * <p>Fetch the base "Object", if any, with which static fields of the
     * given class can be accessed via methods like {@link #getInt(Object,
     * long)}.  This value may be null.  This value may refer to an object
     * which is a "cookie", not guaranteed to be a real Object, and it should
     * not be used in any way except as argument to the get and put routines in
     * this class.
     */
    public abstract Object staticFieldBase(Field f);

    /**
     * Detects if the given class may need to be initialized. This is often
     * needed in conjunction with obtaining the static field base of a
     * class.
     *
     * @return false only if a call to {@code ensureClassInitialized} would have no effect
     */
    public abstract boolean shouldBeInitialized(Class<?> c);

    /**
     * Ensures the given class has been initialized. This is often
     * needed in conjunction with obtaining the static field base of a
     * class.
     */
    public abstract void ensureClassInitialized(Class<?> c);

    /**
     * Reports the offset of the first element in the storage allocation of a
     * given array class.  If {@link #arrayIndexScale} returns a non-zero value
     * for the same class, you may use that scale factor, together with this
     * base offset, to form new offsets to access elements of arrays of the
     * given class.
     *
     * @see #getInt(Object, long)
     * @see #putInt(Object, long, int)
     */
    public abstract int arrayBaseOffset(Class<?> arrayClass);


    /**
     * The value of {@code arrayBaseOffset(boolean[].class)}
     */
    public static final int ARRAY_BOOLEAN_BASE_OFFSET
            = getUnsafe0().arrayBaseOffset(boolean[].class);

    /**
     * The value of {@code arrayBaseOffset(byte[].class)}
     */
    public static final int ARRAY_BYTE_BASE_OFFSET
            = getUnsafe0().arrayBaseOffset(byte[].class);

    /**
     * The value of {@code arrayBaseOffset(short[].class)}
     */
    public static final int ARRAY_SHORT_BASE_OFFSET
            = getUnsafe0().arrayBaseOffset(short[].class);

    /**
     * The value of {@code arrayBaseOffset(char[].class)}
     */
    public static final int ARRAY_CHAR_BASE_OFFSET
            = getUnsafe0().arrayBaseOffset(char[].class);

    /**
     * The value of {@code arrayBaseOffset(int[].class)}
     */
    public static final int ARRAY_INT_BASE_OFFSET
            = getUnsafe0().arrayBaseOffset(int[].class);

    /**
     * The value of {@code arrayBaseOffset(long[].class)}
     */
    public static final int ARRAY_LONG_BASE_OFFSET
            = getUnsafe0().arrayBaseOffset(long[].class);

    /**
     * The value of {@code arrayBaseOffset(float[].class)}
     */
    public static final int ARRAY_FLOAT_BASE_OFFSET
            = getUnsafe0().arrayBaseOffset(float[].class);

    /**
     * The value of {@code arrayBaseOffset(double[].class)}
     */
    public static final int ARRAY_DOUBLE_BASE_OFFSET
            = getUnsafe0().arrayBaseOffset(double[].class);

    /**
     * The value of {@code arrayBaseOffset(Object[].class)}
     */
    public static final int ARRAY_OBJECT_BASE_OFFSET
            = getUnsafe0().arrayBaseOffset(Object[].class);

    /**
     * Reports the scale factor for addressing elements in the storage
     * allocation of a given array class.  However, arrays of "narrow" types
     * will generally not work properly with accessors like {@link
     * #getByte(Object, long)}, so the scale factor for such classes is reported
     * as zero.
     *
     * @see #arrayBaseOffset
     * @see #getInt(Object, long)
     * @see #putInt(Object, long, int)
     */
    public abstract int arrayIndexScale(Class<?> arrayClass);


    /**
     * The value of {@code arrayIndexScale(boolean[].class)}
     */
    public static final int ARRAY_BOOLEAN_INDEX_SCALE
            = getUnsafe0().arrayIndexScale(boolean[].class);

    /**
     * The value of {@code arrayIndexScale(byte[].class)}
     */
    public static final int ARRAY_BYTE_INDEX_SCALE
            = getUnsafe0().arrayIndexScale(byte[].class);

    /**
     * The value of {@code arrayIndexScale(short[].class)}
     */
    public static final int ARRAY_SHORT_INDEX_SCALE
            = getUnsafe0().arrayIndexScale(short[].class);

    /**
     * The value of {@code arrayIndexScale(char[].class)}
     */
    public static final int ARRAY_CHAR_INDEX_SCALE
            = getUnsafe0().arrayIndexScale(char[].class);

    /**
     * The value of {@code arrayIndexScale(int[].class)}
     */
    public static final int ARRAY_INT_INDEX_SCALE
            = getUnsafe0().arrayIndexScale(int[].class);

    /**
     * The value of {@code arrayIndexScale(long[].class)}
     */
    public static final int ARRAY_LONG_INDEX_SCALE
            = getUnsafe0().arrayIndexScale(long[].class);

    /**
     * The value of {@code arrayIndexScale(float[].class)}
     */
    public static final int ARRAY_FLOAT_INDEX_SCALE
            = getUnsafe0().arrayIndexScale(float[].class);

    /**
     * The value of {@code arrayIndexScale(double[].class)}
     */
    public static final int ARRAY_DOUBLE_INDEX_SCALE
            = getUnsafe0().arrayIndexScale(double[].class);

    /**
     * The value of {@code arrayIndexScale(Object[].class)}
     */
    public static final int ARRAY_OBJECT_INDEX_SCALE
            = getUnsafe0().arrayIndexScale(Object[].class);

    /**
     * Reports the size in bytes of a abstract pointer, as stored via {@link
     * #putAddress}.  This value will be either 4 or 8.  Note that the sizes of
     * other primitive types (as stored in abstract memory blocks) is determined
     * fully by their information content.
     */
    public abstract int addressSize();

    /**
     * The value of {@code addressSize()}
     */
    public static final int ADDRESS_SIZE = getUnsafe0().addressSize();

    /**
     * Reports the size in bytes of a abstract memory page (whatever that is).
     * This value will always be a power of two.
     */
    public abstract int pageSize();

    /// random trusted operations from JNI:

    /**
     * Tells the VM to define a class, without security checks.  By default, the
     * class loader and protection domain come from the caller's class.
     */
    public abstract Class<?> defineClass(String name, byte[] b, int off, int len,
                                         ClassLoader loader,
                                         ProtectionDomain protectionDomain);

    public Class<?> defineClass0(
            String name, byte[] b, int off, int len,
            ClassLoader loader,
            ProtectionDomain protectionDomain
    ) {
        return defineClass(name, b, off, len, loader, protectionDomain);
    }

    /**
     * Defines a class but does not make it known to the class loader or system dictionary.
     * <p>
     * For each CP entry, the corresponding CP patch must either be null or have
     * the a format that matches its tag:
     * <ul>
     * <li>Integer, Long, Float, Double: the corresponding wrapper object type from java.lang
     * <li>Utf8: a string (must have suitable syntax if used as signature or name)
     * <li>Class: any java.lang.Class object
     * <li>String: any object (not just a java.lang.String)
     * <li>InterfaceMethodRef: (NYI) a method handle to invoke on that call site's arguments
     * </ul>
     *
     * @param hostClass context for linkage, access control, protection domain, and class loader
     * @param data      bytes of a class file
     * @param cpPatches where non-null entries exist, they replace corresponding CP entries in data
     */
    public abstract Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches);

    /**
     * Allocates an instance but does not run any constructor.
     * Initializes the class if it has not yet been.
     */
    public abstract Object allocateInstance(Class<?> cls)
            throws InstantiationException;

    /**
     * Allocates an array of a given type, but does not do zeroing.
     * <p>
     * This method should only be used in the very rare cases where a high-performance code
     * overwrites the destination array completely, and compilers cannot assist in zeroing elimination.
     * In an overwhelming majority of cases, a normal Java allocation should be used instead.
     * <p>
     * Users of this method are <b>required</b> to overwrite the initial (garbage) array contents
     * before allowing untrusted code, or code in other threads, to observe the reference
     * to the newly allocated array. In addition, the publication of the array reference must be
     * safe according to the Java Memory Model requirements.
     * <p>
     * The safest approach to deal with an uninitialized array is to keep the reference to it in local
     * variable at least until the initialization is complete, and then publish it <b>once</b>, either
     * by writing it to a <em>volatile</em> field, or storing it into a <em>final</em> field in constructor,
     * or issuing a {@link #storeFence} before publishing the reference.
     * <p>
     *
     * @param componentType array component type to allocate
     * @param length        array size to allocate
     * @throws IllegalArgumentException if component type is null, or not a primitive class;
     *                                  or the length is negative
     * @implnote This method can only allocate primitive arrays, to avoid garbage reference
     * elements that could break heap integrity.
     */
    public abstract Object allocateUninitializedArray(Class<?> componentType, int length);

    /**
     * Throws the exception without telling the verifier.
     */
    public abstract void throwException(Throwable ee);

    /**
     * Atomically updates Java variable to {@code x} if it is currently
     * holding {@code expected}.
     *
     * <p>This operation has memory semantics of a {@code volatile} read
     * and write.  Corresponds to C11 atomic_compare_exchange_strong.
     *
     * @return {@code true} if successful
     */
    public abstract boolean compareAndSetReference(Object o, long offset,
                                                   Object expected,
                                                   Object x);

    public abstract Object compareAndExchangeReference(Object o, long offset,
                                                       Object expected,
                                                       Object x);

    public abstract Object compareAndExchangeReferenceAcquire(Object o, long offset,
                                                              Object expected,
                                                              Object x);

    public abstract Object compareAndExchangeReferenceRelease(Object o, long offset,
                                                              Object expected,
                                                              Object x);

    public abstract boolean weakCompareAndSetReferencePlain(Object o, long offset,
                                                            Object expected,
                                                            Object x);

    public abstract boolean weakCompareAndSetReferenceAcquire(Object o, long offset,
                                                              Object expected,
                                                              Object x);

    public abstract boolean weakCompareAndSetReferenceRelease(Object o, long offset,
                                                              Object expected,
                                                              Object x);

    public abstract boolean weakCompareAndSetReference(Object o, long offset,
                                                       Object expected,
                                                       Object x);

    /**
     * Atomically updates Java variable to {@code x} if it is currently
     * holding {@code expected}.
     *
     * <p>This operation has memory semantics of a {@code volatile} read
     * and write.  Corresponds to C11 atomic_compare_exchange_strong.
     *
     * @return {@code true} if successful
     */
    public abstract boolean compareAndSetInt(Object o, long offset,
                                             int expected,
                                             int x);

    public abstract int compareAndExchangeInt(Object o, long offset,
                                              int expected,
                                              int x);

    public abstract int compareAndExchangeIntAcquire(Object o, long offset,
                                                     int expected,
                                                     int x);

    public abstract int compareAndExchangeIntRelease(Object o, long offset,
                                                     int expected,
                                                     int x);

    public abstract boolean weakCompareAndSetIntPlain(Object o, long offset,
                                                      int expected,
                                                      int x);

    public abstract boolean weakCompareAndSetIntAcquire(Object o, long offset,
                                                        int expected,
                                                        int x);

    public abstract boolean weakCompareAndSetIntRelease(Object o, long offset,
                                                        int expected,
                                                        int x);

    public abstract boolean weakCompareAndSetInt(Object o, long offset,
                                                 int expected,
                                                 int x);

    public abstract byte compareAndExchangeByte(Object o, long offset,
                                                byte expected,
                                                byte x);

    public abstract boolean compareAndSetByte(Object o, long offset,
                                              byte expected,
                                              byte x);

    public abstract boolean weakCompareAndSetByte(Object o, long offset,
                                                  byte expected,
                                                  byte x);

    public abstract boolean weakCompareAndSetByteAcquire(Object o, long offset,
                                                         byte expected,
                                                         byte x);

    public abstract boolean weakCompareAndSetByteRelease(Object o, long offset,
                                                         byte expected,
                                                         byte x);

    public abstract boolean weakCompareAndSetBytePlain(Object o, long offset,
                                                       byte expected,
                                                       byte x);

    public abstract byte compareAndExchangeByteAcquire(Object o, long offset,
                                                       byte expected,
                                                       byte x);

    public abstract byte compareAndExchangeByteRelease(Object o, long offset,
                                                       byte expected,
                                                       byte x);

    public abstract short compareAndExchangeShort(Object o, long offset,
                                                  short expected,
                                                  short x);

    public abstract boolean compareAndSetShort(Object o, long offset,
                                               short expected,
                                               short x);

    public abstract boolean weakCompareAndSetShort(Object o, long offset,
                                                   short expected,
                                                   short x);

    public abstract boolean weakCompareAndSetShortAcquire(Object o, long offset,
                                                          short expected,
                                                          short x);

    public abstract boolean weakCompareAndSetShortRelease(Object o, long offset,
                                                          short expected,
                                                          short x);

    public abstract boolean weakCompareAndSetShortPlain(Object o, long offset,
                                                        short expected,
                                                        short x);

    public abstract short compareAndExchangeShortAcquire(Object o, long offset,
                                                         short expected,
                                                         short x);

    public abstract short compareAndExchangeShortRelease(Object o, long offset,
                                                         short expected,
                                                         short x);

    public abstract boolean compareAndSetChar(Object o, long offset,
                                              char expected,
                                              char x);

    public abstract char compareAndExchangeChar(Object o, long offset,
                                                char expected,
                                                char x);

    public abstract char compareAndExchangeCharAcquire(Object o, long offset,
                                                       char expected,
                                                       char x);

    public abstract char compareAndExchangeCharRelease(Object o, long offset,
                                                       char expected,
                                                       char x);

    public abstract boolean weakCompareAndSetChar(Object o, long offset,
                                                  char expected,
                                                  char x);

    public abstract boolean weakCompareAndSetCharAcquire(Object o, long offset,
                                                         char expected,
                                                         char x);

    public abstract boolean weakCompareAndSetCharRelease(Object o, long offset,
                                                         char expected,
                                                         char x);

    public abstract boolean weakCompareAndSetCharPlain(Object o, long offset,
                                                       char expected,
                                                       char x);


    public abstract boolean compareAndSetBoolean(Object o, long offset,
                                                 boolean expected,
                                                 boolean x);

    public abstract boolean compareAndExchangeBoolean(Object o, long offset,
                                                      boolean expected,
                                                      boolean x);

    public abstract boolean compareAndExchangeBooleanAcquire(Object o, long offset,
                                                             boolean expected,
                                                             boolean x);

    public abstract boolean compareAndExchangeBooleanRelease(Object o, long offset,
                                                             boolean expected,
                                                             boolean x);

    public abstract boolean weakCompareAndSetBoolean(Object o, long offset,
                                                     boolean expected,
                                                     boolean x);

    public abstract boolean weakCompareAndSetBooleanAcquire(Object o, long offset,
                                                            boolean expected,
                                                            boolean x);

    public abstract boolean weakCompareAndSetBooleanRelease(Object o, long offset,
                                                            boolean expected,
                                                            boolean x);

    public abstract boolean weakCompareAndSetBooleanPlain(Object o, long offset,
                                                          boolean expected,
                                                          boolean x);

    /**
     * Atomically updates Java variable to {@code x} if it is currently
     * holding {@code expected}.
     *
     * <p>This operation has memory semantics of a {@code volatile} read
     * and write.  Corresponds to C11 atomic_compare_exchange_strong.
     *
     * @return {@code true} if successful
     */
    public abstract boolean compareAndSetFloat(Object o, long offset,
                                               float expected,
                                               float x);

    public abstract float compareAndExchangeFloat(Object o, long offset,
                                                  float expected,
                                                  float x);

    public abstract float compareAndExchangeFloatAcquire(Object o, long offset,
                                                         float expected,
                                                         float x);

    public abstract float compareAndExchangeFloatRelease(Object o, long offset,
                                                         float expected,
                                                         float x);

    public abstract boolean weakCompareAndSetFloatPlain(Object o, long offset,
                                                        float expected,
                                                        float x);

    public abstract boolean weakCompareAndSetFloatAcquire(Object o, long offset,
                                                          float expected,
                                                          float x);

    public abstract boolean weakCompareAndSetFloatRelease(Object o, long offset,
                                                          float expected,
                                                          float x);

    public abstract boolean weakCompareAndSetFloat(Object o, long offset,
                                                   float expected,
                                                   float x);

    /**
     * Atomically updates Java variable to {@code x} if it is currently
     * holding {@code expected}.
     *
     * <p>This operation has memory semantics of a {@code volatile} read
     * and write.  Corresponds to C11 atomic_compare_exchange_strong.
     *
     * @return {@code true} if successful
     */
    public abstract boolean compareAndSetDouble(Object o, long offset,
                                                double expected,
                                                double x);

    public abstract double compareAndExchangeDouble(Object o, long offset,
                                                    double expected,
                                                    double x);

    public abstract double compareAndExchangeDoubleAcquire(Object o, long offset,
                                                           double expected,
                                                           double x);

    public abstract double compareAndExchangeDoubleRelease(Object o, long offset,
                                                           double expected,
                                                           double x);

    public abstract boolean weakCompareAndSetDoublePlain(Object o, long offset,
                                                         double expected,
                                                         double x);

    public abstract boolean weakCompareAndSetDoubleAcquire(Object o, long offset,
                                                           double expected,
                                                           double x);

    public abstract boolean weakCompareAndSetDoubleRelease(Object o, long offset,
                                                           double expected,
                                                           double x);

    public abstract boolean weakCompareAndSetDouble(Object o, long offset,
                                                    double expected,
                                                    double x);

    /**
     * Atomically updates Java variable to {@code x} if it is currently
     * holding {@code expected}.
     *
     * <p>This operation has memory semantics of a {@code volatile} read
     * and write.  Corresponds to C11 atomic_compare_exchange_strong.
     *
     * @return {@code true} if successful
     */
    public abstract boolean compareAndSetLong(Object o, long offset,
                                              long expected,
                                              long x);

    public abstract long compareAndExchangeLong(Object o, long offset,
                                                long expected,
                                                long x);

    public abstract long compareAndExchangeLongAcquire(Object o, long offset,
                                                       long expected,
                                                       long x);

    public abstract long compareAndExchangeLongRelease(Object o, long offset,
                                                       long expected,
                                                       long x);

    public abstract boolean weakCompareAndSetLongPlain(Object o, long offset,
                                                       long expected,
                                                       long x);

    public abstract boolean weakCompareAndSetLongAcquire(Object o, long offset,
                                                         long expected,
                                                         long x);

    public abstract boolean weakCompareAndSetLongRelease(Object o, long offset,
                                                         long expected,
                                                         long x);

    public abstract boolean weakCompareAndSetLong(Object o, long offset,
                                                  long expected,
                                                  long x);

    /**
     * Fetches a reference value from a given Java variable, with volatile
     * load semantics. Otherwise identical to {@link #getReference(Object, long)}
     */
    public abstract Object getReferenceVolatile(Object o, long offset);

    /**
     * Stores a reference value into a given Java variable, with
     * volatile store semantics. Otherwise identical to {@link #putReference(Object, long, Object)}
     */
    public abstract void putReferenceVolatile(Object o, long offset, Object x);

    /**
     * Volatile version of {@link #getInt(Object, long)}
     */
    public abstract int getIntVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putInt(Object, long, int)}
     */
    public abstract void putIntVolatile(Object o, long offset, int x);

    /**
     * Volatile version of {@link #getBoolean(Object, long)}
     */
    public abstract boolean getBooleanVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putBoolean(Object, long, boolean)}
     */
    public abstract void putBooleanVolatile(Object o, long offset, boolean x);

    /**
     * Volatile version of {@link #getByte(Object, long)}
     */
    public abstract byte getByteVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putByte(Object, long, byte)}
     */
    public abstract void putByteVolatile(Object o, long offset, byte x);

    /**
     * Volatile version of {@link #getShort(Object, long)}
     */
    public abstract short getShortVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putShort(Object, long, short)}
     */
    public abstract void putShortVolatile(Object o, long offset, short x);

    /**
     * Volatile version of {@link #getChar(Object, long)}
     */
    public abstract char getCharVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putChar(Object, long, char)}
     */
    public abstract void putCharVolatile(Object o, long offset, char x);

    /**
     * Volatile version of {@link #getLong(Object, long)}
     */
    public abstract long getLongVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putLong(Object, long, long)}
     */
    public abstract void putLongVolatile(Object o, long offset, long x);

    /**
     * Volatile version of {@link #getFloat(Object, long)}
     */
    public abstract float getFloatVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putFloat(Object, long, float)}
     */
    public abstract void putFloatVolatile(Object o, long offset, float x);

    /**
     * Volatile version of {@link #getDouble(Object, long)}
     */
    public abstract double getDoubleVolatile(Object o, long offset);

    /**
     * Volatile version of {@link #putDouble(Object, long, double)}
     */
    public abstract void putDoubleVolatile(Object o, long offset, double x);


    /**
     * Acquire version of {@link #getReferenceVolatile(Object, long)}
     */
    public abstract Object getReferenceAcquire(Object o, long offset);

    /**
     * Acquire version of {@link #getBooleanVolatile(Object, long)}
     */
    public abstract boolean getBooleanAcquire(Object o, long offset);

    /**
     * Acquire version of {@link #getByteVolatile(Object, long)}
     */
    public abstract byte getByteAcquire(Object o, long offset);

    /**
     * Acquire version of {@link #getShortVolatile(Object, long)}
     */
    public abstract short getShortAcquire(Object o, long offset);

    /**
     * Acquire version of {@link #getCharVolatile(Object, long)}
     */
    public abstract char getCharAcquire(Object o, long offset);

    /**
     * Acquire version of {@link #getIntVolatile(Object, long)}
     */
    public abstract int getIntAcquire(Object o, long offset);

    /**
     * Acquire version of {@link #getFloatVolatile(Object, long)}
     */
    public abstract float getFloatAcquire(Object o, long offset);

    /**
     * Acquire version of {@link #getLongVolatile(Object, long)}
     */
    public abstract long getLongAcquire(Object o, long offset);

    /**
     * Acquire version of {@link #getDoubleVolatile(Object, long)}
     */
    public abstract double getDoubleAcquire(Object o, long offset);

    /*
     * Versions of {@link #putReferenceVolatile(Object, long, Object)}
     * that do not guarantee immediate visibility of the store to
     * other threads. This method is generally only useful if the
     * underlying field is a Java volatile (or if an array cell, one
     * that is otherwise only accessed using volatile accesses).
     *
     * Corresponds to C11 atomic_store_explicit(..., memory_order_release).
     */

    /**
     * Release version of {@link #putReferenceVolatile(Object, long, Object)}
     */
    public abstract void putReferenceRelease(Object o, long offset, Object x);

    /**
     * Release version of {@link #putBooleanVolatile(Object, long, boolean)}
     */
    public abstract void putBooleanRelease(Object o, long offset, boolean x);

    /**
     * Release version of {@link #putByteVolatile(Object, long, byte)}
     */
    public abstract void putByteRelease(Object o, long offset, byte x);

    /**
     * Release version of {@link #putShortVolatile(Object, long, short)}
     */
    public abstract void putShortRelease(Object o, long offset, short x);

    /**
     * Release version of {@link #putCharVolatile(Object, long, char)}
     */
    public abstract void putCharRelease(Object o, long offset, char x);

    /**
     * Release version of {@link #putIntVolatile(Object, long, int)}
     */
    public abstract void putIntRelease(Object o, long offset, int x);

    /**
     * Release version of {@link #putFloatVolatile(Object, long, float)}
     */
    public abstract void putFloatRelease(Object o, long offset, float x);


    /**
     * Release version of {@link #putLongVolatile(Object, long, long)}
     */
    public abstract void putLongRelease(Object o, long offset, long x);

    /**
     * Release version of {@link #putDoubleVolatile(Object, long, double)}
     */
    public abstract void putDoubleRelease(Object o, long offset, double x);

    // ------------------------------ Opaque --------------------------------------

    /**
     * Opaque version of {@link #getReferenceVolatile(Object, long)}
     */
    public abstract Object getReferenceOpaque(Object o, long offset);

    /**
     * Opaque version of {@link #getBooleanVolatile(Object, long)}
     */
    public abstract boolean getBooleanOpaque(Object o, long offset);

    /**
     * Opaque version of {@link #getByteVolatile(Object, long)}
     */
    public abstract byte getByteOpaque(Object o, long offset);

    /**
     * Opaque version of {@link #getShortVolatile(Object, long)}
     */
    public abstract short getShortOpaque(Object o, long offset);

    /**
     * Opaque version of {@link #getCharVolatile(Object, long)}
     */
    public abstract char getCharOpaque(Object o, long offset);

    /**
     * Opaque version of {@link #getIntVolatile(Object, long)}
     */
    public abstract int getIntOpaque(Object o, long offset);

    /**
     * Opaque version of {@link #getFloatVolatile(Object, long)}
     */
    public abstract float getFloatOpaque(Object o, long offset);

    /**
     * Opaque version of {@link #getLongVolatile(Object, long)}
     */
    public abstract long getLongOpaque(Object o, long offset);

    /**
     * Opaque version of {@link #getDoubleVolatile(Object, long)}
     */
    public abstract double getDoubleOpaque(Object o, long offset);

    /**
     * Opaque version of {@link #putReferenceVolatile(Object, long, Object)}
     */
    public abstract void putReferenceOpaque(Object o, long offset, Object x);

    /**
     * Opaque version of {@link #putBooleanVolatile(Object, long, boolean)}
     */
    public abstract void putBooleanOpaque(Object o, long offset, boolean x);

    /**
     * Opaque version of {@link #putByteVolatile(Object, long, byte)}
     */
    public abstract void putByteOpaque(Object o, long offset, byte x);

    /**
     * Opaque version of {@link #putShortVolatile(Object, long, short)}
     */
    public abstract void putShortOpaque(Object o, long offset, short x);

    /**
     * Opaque version of {@link #putCharVolatile(Object, long, char)}
     */
    public abstract void putCharOpaque(Object o, long offset, char x);

    /**
     * Opaque version of {@link #putIntVolatile(Object, long, int)}
     */
    public abstract void putIntOpaque(Object o, long offset, int x);

    /**
     * Opaque version of {@link #putFloatVolatile(Object, long, float)}
     */
    public abstract void putFloatOpaque(Object o, long offset, float x);

    /**
     * Opaque version of {@link #putLongVolatile(Object, long, long)}
     */
    public abstract void putLongOpaque(Object o, long offset, long x);

    /**
     * Opaque version of {@link #putDoubleVolatile(Object, long, double)}
     */
    public abstract void putDoubleOpaque(Object o, long offset, double x);

    /**
     * Unblocks the given thread blocked on {@code park}, or, if it is
     * not blocked, causes the subsequent call to {@code park} not to
     * block.  Note: this operation is "unsafe" solely because the
     * caller must somehow ensure that the thread has not been
     * destroyed. Nothing special is usually required to ensure this
     * when called from Java (in which there will ordinarily be a live
     * reference to the thread) but this is not nearly-automatically
     * so when calling from abstract code.
     *
     * @param thread the thread to unpark.
     */
    public abstract void unpark(Object thread);

    /**
     * Blocks current thread, returning when a balancing
     * {@code unpark} occurs, or a balancing {@code unpark} has
     * already occurred, or the thread is interrupted, or, if not
     * absolute and time is not zero, the given time nanoseconds have
     * elapsed, or if absolute, the given deadline in milliseconds
     * since Epoch has passed, or spuriously (i.e., returning for no
     * "reason"). Note: This operation is in the Unsafe class only
     * because {@code unpark} is, so it would be strange to place it
     * elsewhere.
     */
    public abstract void park(boolean isAbsolute, long time);

    /**
     * Gets the load average in the system run queue assigned
     * to the available processors averaged over various periods of time.
     * This method retrieves the given {@code nelem} samples and
     * assigns to the elements of the given {@code loadavg} array.
     * The system imposes a maximum of 3 samples, representing
     * averages over the last 1,  5,  and  15 minutes, respectively.
     *
     * @param loadavg an array of double of size nelems
     * @param nelems  the number of samples to be retrieved and
     *                must be 1 to 3.
     * @return the number of samples actually retrieved; or -1
     * if the load average is unobtainable.
     */
    public abstract int getLoadAverage(double[] loadavg, int nelems);
    // The following contain CAS-based Java implementations used on
    // platforms not supporting abstract instructions

    /**
     * Atomically adds the given value to the current value of a field
     * or array element within the given object {@code o}
     * at the given {@code offset}.
     *
     * @param o      object/array to update the field/element in
     * @param offset field/element offset
     * @param delta  the value to add
     * @return the previous value
     * @since 1.8
     */
    public abstract int getAndAddInt(Object o, long offset, int delta);

    public abstract int getAndAddIntRelease(Object o, long offset, int delta);

    public abstract int getAndAddIntAcquire(Object o, long offset, int delta);

    /**
     * Atomically adds the given value to the current value of a field
     * or array element within the given object {@code o}
     * at the given {@code offset}.
     *
     * @param o      object/array to update the field/element in
     * @param offset field/element offset
     * @param delta  the value to add
     * @return the previous value
     * @since 1.8
     */
    public abstract long getAndAddLong(Object o, long offset, long delta);

    public abstract long getAndAddLongRelease(Object o, long offset, long delta);

    public abstract long getAndAddLongAcquire(Object o, long offset, long delta);

    public abstract byte getAndAddByte(Object o, long offset, byte delta);

    public abstract byte getAndAddByteRelease(Object o, long offset, byte delta);

    public abstract byte getAndAddByteAcquire(Object o, long offset, byte delta);

    public abstract short getAndAddShort(Object o, long offset, short delta);

    public abstract short getAndAddShortRelease(Object o, long offset, short delta);

    public abstract short getAndAddShortAcquire(Object o, long offset, short delta);

    public abstract char getAndAddChar(Object o, long offset, char delta);

    public abstract char getAndAddCharRelease(Object o, long offset, char delta);

    public abstract char getAndAddCharAcquire(Object o, long offset, char delta);

    public abstract float getAndAddFloat(Object o, long offset, float delta);

    public abstract float getAndAddFloatRelease(Object o, long offset, float delta);

    public abstract float getAndAddFloatAcquire(Object o, long offset, float delta);

    public abstract double getAndAddDouble(Object o, long offset, double delta);

    public abstract double getAndAddDoubleRelease(Object o, long offset, double delta);

    public abstract double getAndAddDoubleAcquire(Object o, long offset, double delta);

    /**
     * Atomically exchanges the given value with the current value of
     * a field or array element within the given object {@code o}
     * at the given {@code offset}.
     *
     * @param o        object/array to update the field/element in
     * @param offset   field/element offset
     * @param newValue new value
     * @return the previous value
     * @since 1.8
     */
    public abstract int getAndSetInt(Object o, long offset, int newValue);

    public abstract int getAndSetIntRelease(Object o, long offset, int newValue);

    public abstract int getAndSetIntAcquire(Object o, long offset, int newValue);

    /**
     * Atomically exchanges the given value with the current value of
     * a field or array element within the given object {@code o}
     * at the given {@code offset}.
     *
     * @param o        object/array to update the field/element in
     * @param offset   field/element offset
     * @param newValue new value
     * @return the previous value
     * @since 1.8
     */
    public abstract long getAndSetLong(Object o, long offset, long newValue);

    public abstract long getAndSetLongRelease(Object o, long offset, long newValue);

    public abstract long getAndSetLongAcquire(Object o, long offset, long newValue);

    /**
     * Atomically exchanges the given reference value with the current
     * reference value of a field or array element within the given
     * object {@code o} at the given {@code offset}.
     *
     * @param o        object/array to update the field/element in
     * @param offset   field/element offset
     * @param newValue new value
     * @return the previous value
     * @since 1.8
     */
    public abstract Object getAndSetReference(Object o, long offset, Object newValue);

    public abstract Object getAndSetReferenceRelease(Object o, long offset, Object newValue);

    public abstract Object getAndSetReferenceAcquire(Object o, long offset, Object newValue);

    public abstract byte getAndSetByte(Object o, long offset, byte newValue);

    public abstract byte getAndSetByteRelease(Object o, long offset, byte newValue);

    public abstract byte getAndSetByteAcquire(Object o, long offset, byte newValue);

    public abstract boolean getAndSetBoolean(Object o, long offset, boolean newValue);

    public abstract boolean getAndSetBooleanRelease(Object o, long offset, boolean newValue);

    public abstract boolean getAndSetBooleanAcquire(Object o, long offset, boolean newValue);

    public abstract short getAndSetShort(Object o, long offset, short newValue);

    public abstract short getAndSetShortRelease(Object o, long offset, short newValue);

    public abstract short getAndSetShortAcquire(Object o, long offset, short newValue);

    public abstract char getAndSetChar(Object o, long offset, char newValue);

    public abstract char getAndSetCharRelease(Object o, long offset, char newValue);

    public abstract char getAndSetCharAcquire(Object o, long offset, char newValue);

    public abstract float getAndSetFloat(Object o, long offset, float newValue);

    public abstract float getAndSetFloatRelease(Object o, long offset, float newValue);

    public abstract float getAndSetFloatAcquire(Object o, long offset, float newValue);

    public abstract double getAndSetDouble(Object o, long offset, double newValue);

    public abstract double getAndSetDoubleRelease(Object o, long offset, double newValue);

    public abstract double getAndSetDoubleAcquire(Object o, long offset, double newValue);

    // The following contain CAS-based Java implementations used on
    // platforms not supporting abstract instructions

    public abstract boolean getAndBitwiseOrBoolean(Object o, long offset, boolean mask);

    public abstract boolean getAndBitwiseOrBooleanRelease(Object o, long offset, boolean mask);

    public abstract boolean getAndBitwiseOrBooleanAcquire(Object o, long offset, boolean mask);

    public abstract boolean getAndBitwiseAndBoolean(Object o, long offset, boolean mask);

    public abstract boolean getAndBitwiseAndBooleanRelease(Object o, long offset, boolean mask);

    public abstract boolean getAndBitwiseAndBooleanAcquire(Object o, long offset, boolean mask);

    public abstract boolean getAndBitwiseXorBoolean(Object o, long offset, boolean mask);

    public abstract boolean getAndBitwiseXorBooleanRelease(Object o, long offset, boolean mask);

    public abstract boolean getAndBitwiseXorBooleanAcquire(Object o, long offset, boolean mask);


    public abstract byte getAndBitwiseOrByte(Object o, long offset, byte mask);

    public abstract byte getAndBitwiseOrByteRelease(Object o, long offset, byte mask);

    public abstract byte getAndBitwiseOrByteAcquire(Object o, long offset, byte mask);

    public abstract byte getAndBitwiseAndByte(Object o, long offset, byte mask);

    public abstract byte getAndBitwiseAndByteRelease(Object o, long offset, byte mask);

    public abstract byte getAndBitwiseAndByteAcquire(Object o, long offset, byte mask);

    public abstract byte getAndBitwiseXorByte(Object o, long offset, byte mask);

    public abstract byte getAndBitwiseXorByteRelease(Object o, long offset, byte mask);

    public abstract byte getAndBitwiseXorByteAcquire(Object o, long offset, byte mask);

    public abstract char getAndBitwiseOrChar(Object o, long offset, char mask);

    public abstract char getAndBitwiseOrCharRelease(Object o, long offset, char mask);

    public abstract char getAndBitwiseOrCharAcquire(Object o, long offset, char mask);

    public abstract char getAndBitwiseAndChar(Object o, long offset, char mask);

    public abstract char getAndBitwiseAndCharRelease(Object o, long offset, char mask);

    public abstract char getAndBitwiseAndCharAcquire(Object o, long offset, char mask);

    public abstract char getAndBitwiseXorChar(Object o, long offset, char mask);

    public abstract char getAndBitwiseXorCharRelease(Object o, long offset, char mask);

    public abstract char getAndBitwiseXorCharAcquire(Object o, long offset, char mask);


    public abstract short getAndBitwiseOrShort(Object o, long offset, short mask);

    public abstract short getAndBitwiseOrShortRelease(Object o, long offset, short mask);

    public abstract short getAndBitwiseOrShortAcquire(Object o, long offset, short mask);

    public abstract short getAndBitwiseAndShort(Object o, long offset, short mask);

    public abstract short getAndBitwiseAndShortRelease(Object o, long offset, short mask);

    public abstract short getAndBitwiseAndShortAcquire(Object o, long offset, short mask);

    public abstract short getAndBitwiseXorShort(Object o, long offset, short mask);

    public abstract short getAndBitwiseXorShortRelease(Object o, long offset, short mask);

    public abstract short getAndBitwiseXorShortAcquire(Object o, long offset, short mask);


    public abstract int getAndBitwiseOrInt(Object o, long offset, int mask);

    public abstract int getAndBitwiseOrIntRelease(Object o, long offset, int mask);

    public abstract int getAndBitwiseOrIntAcquire(Object o, long offset, int mask);

    /**
     * Atomically replaces the current value of a field or array element within
     * the given object with the result of bitwise AND between the current value
     * and mask.
     *
     * @param o      object/array to update the field/element in
     * @param offset field/element offset
     * @param mask   the mask value
     * @return the previous value
     * @since 9
     */
    public abstract int getAndBitwiseAndInt(Object o, long offset, int mask);

    public abstract int getAndBitwiseAndIntRelease(Object o, long offset, int mask);

    public abstract int getAndBitwiseAndIntAcquire(Object o, long offset, int mask);

    public abstract int getAndBitwiseXorInt(Object o, long offset, int mask);

    public abstract int getAndBitwiseXorIntRelease(Object o, long offset, int mask);

    public abstract int getAndBitwiseXorIntAcquire(Object o, long offset, int mask);

    public abstract long getAndBitwiseOrLong(Object o, long offset, long mask);

    public abstract long getAndBitwiseOrLongRelease(Object o, long offset, long mask);

    public abstract long getAndBitwiseOrLongAcquire(Object o, long offset, long mask);

    public abstract long getAndBitwiseAndLong(Object o, long offset, long mask);

    public abstract long getAndBitwiseAndLongRelease(Object o, long offset, long mask);

    public abstract long getAndBitwiseAndLongAcquire(Object o, long offset, long mask);

    public abstract long getAndBitwiseXorLong(Object o, long offset, long mask);

    public abstract long getAndBitwiseXorLongRelease(Object o, long offset, long mask);

    public abstract long getAndBitwiseXorLongAcquire(Object o, long offset, long mask);


    /**
     * Ensures that loads before the fence will not be reordered with loads and
     * stores after the fence; a "LoadLoad plus LoadStore barrier".
     * <p>
     * Corresponds to C11 atomic_thread_fence(memory_order_acquire)
     * (an "acquire fence").
     * <p>
     * A pure LoadLoad fence is not provided, since the addition of LoadStore
     * is almost always desired, and most current hardware instructions that
     * provide a LoadLoad barrier also provide a LoadStore barrier for free.
     *
     * @since 1.8
     */
    public abstract void loadFence();

    /**
     * Ensures that loads and stores before the fence will not be reordered with
     * stores after the fence; a "StoreStore plus LoadStore barrier".
     * <p>
     * Corresponds to C11 atomic_thread_fence(memory_order_release)
     * (a "release fence").
     * <p>
     * A pure StoreStore fence is not provided, since the addition of LoadStore
     * is almost always desired, and most current hardware instructions that
     * provide a StoreStore barrier also provide a LoadStore barrier for free.
     *
     * @since 1.8
     */
    public abstract void storeFence();

    /**
     * Ensures that loads and stores before the fence will not be reordered
     * with loads and stores after the fence.  Implies the effects of both
     * loadFence() and storeFence(), and in addition, the effect of a StoreLoad
     * barrier.
     * <p>
     * Corresponds to C11 atomic_thread_fence(memory_order_seq_cst).
     *
     * @since 1.8
     */
    public abstract void fullFence();

    /**
     * Ensures that loads before the fence will not be reordered with
     * loads after the fence.
     */
    public abstract void loadLoadFence();

    /**
     * Ensures that stores before the fence will not be reordered with
     * stores after the fence.
     */
    public abstract void storeStoreFence();


    /**
     * @return Returns true if the abstract byte ordering of this
     * platform is big-endian, false if it is little-endian.
     */
    public abstract boolean isBigEndian();

    /**
     * @return Returns true if this platform is capable of performing
     * accesses at addresses which are not aligned for the type of the
     * primitive type being accessed, false otherwise.
     */
    public abstract boolean unalignedAccess();

    /**
     * Fetches a value at some byte offset into a given Java object.
     * More specifically, fetches a value within the given object
     * <code>o</code> at the given offset, or (if <code>o</code> is
     * null) from the memory address whose numerical value is the
     * given offset.  <p>
     * <p>
     * The specification of this method is the same as {@link
     * #getLong(Object, long)} except that the offset does not need to
     * have been obtained from {@link #objectFieldOffset} on the
     * {@link Field} of some Java field.  The value
     * in memory is raw data, and need not correspond to any Java
     * variable.  Unless <code>o</code> is null, the value accessed
     * must be entirely within the allocated object.  The endianness
     * of the value in memory is the endianness of the abstract platform.
     *
     * <p> The read will be atomic with respect to the largest power
     * of two that divides the GCD of the offset and the storage size.
     * For example, getLongUnaligned will make atomic reads of 2-, 4-,
     * or 8-byte storage units if the offset is zero mod 2, 4, or 8,
     * respectively.  There are no other guarantees of atomicity.
     * <p>
     * 8-byte atomicity is only guaranteed on platforms on which
     * support atomic accesses to longs.
     *
     * @param o      Java heap object in which the value resides, if any, else
     *               null
     * @param offset The offset in bytes from the start of the object
     * @return the value fetched from the indicated object
     * @throws RuntimeException No defined exceptions are thrown, not even
     *                          {@link NullPointerException}
     * @since 9
     */
    public abstract long getLongUnaligned(Object o, long offset);

    /**
     * As {@link #getLongUnaligned(Object, long)} but with an
     * additional argument which specifies the endianness of the value
     * as stored in memory.
     *
     * @param o         Java heap object in which the variable resides
     * @param offset    The offset in bytes from the start of the object
     * @param bigEndian The endianness of the value
     * @return the value fetched from the indicated object
     * @since 9
     */
    public abstract long getLongUnaligned(Object o, long offset, boolean bigEndian);

    /**
     * @see #getLongUnaligned(Object, long)
     */
    public abstract int getIntUnaligned(Object o, long offset);

    /**
     * @see #getLongUnaligned(Object, long, boolean)
     */
    public abstract int getIntUnaligned(Object o, long offset, boolean bigEndian);

    /**
     * @see #getLongUnaligned(Object, long)
     */
    public abstract short getShortUnaligned(Object o, long offset);

    /**
     * @see #getLongUnaligned(Object, long, boolean)
     */
    public abstract short getShortUnaligned(Object o, long offset, boolean bigEndian);

    /**
     * @see #getLongUnaligned(Object, long)
     */
    public abstract char getCharUnaligned(Object o, long offset);

    /**
     * @see #getLongUnaligned(Object, long, boolean)
     */
    public abstract char getCharUnaligned(Object o, long offset, boolean bigEndian);

    /**
     * Stores a value at some byte offset into a given Java object.
     * <p>
     * The specification of this method is the same as {@link
     * #getLong(Object, long)} except that the offset does not need to
     * have been obtained from {@link #objectFieldOffset} on the
     * {@link Field} of some Java field.  The value
     * in memory is raw data, and need not correspond to any Java
     * variable.  The endianness of the value in memory is the
     * endianness of the abstract platform.
     * <p>
     * The write will be atomic with respect to the largest power of
     * two that divides the GCD of the offset and the storage size.
     * For example, putLongUnaligned will make atomic writes of 2-, 4-,
     * or 8-byte storage units if the offset is zero mod 2, 4, or 8,
     * respectively.  There are no other guarantees of atomicity.
     * <p>
     * 8-byte atomicity is only guaranteed on platforms on which
     * support atomic accesses to longs.
     *
     * @param o      Java heap object in which the value resides, if any, else
     *               null
     * @param offset The offset in bytes from the start of the object
     * @param x      the value to store
     * @throws RuntimeException No defined exceptions are thrown, not even
     *                          {@link NullPointerException}
     * @since 9
     */
    public abstract void putLongUnaligned(Object o, long offset, long x);

    /**
     * As {@link #putLongUnaligned(Object, long, long)} but with an additional
     * argument which specifies the endianness of the value as stored in memory.
     *
     * @param o         Java heap object in which the value resides
     * @param offset    The offset in bytes from the start of the object
     * @param x         the value to store
     * @param bigEndian The endianness of the value
     * @throws RuntimeException No defined exceptions are thrown, not even
     *                          {@link NullPointerException}
     * @since 9
     */
    public abstract void putLongUnaligned(Object o, long offset, long x, boolean bigEndian);

    /**
     * @see #putLongUnaligned(Object, long, long)
     */
    public abstract void putIntUnaligned(Object o, long offset, int x);

    /**
     * @see #putLongUnaligned(Object, long, long, boolean)
     */
    public abstract void putIntUnaligned(Object o, long offset, int x, boolean bigEndian);

    /**
     * @see #putLongUnaligned(Object, long, long)
     */
    public abstract void putShortUnaligned(Object o, long offset, short x);

    /**
     * @see #putLongUnaligned(Object, long, long, boolean)
     */
    public abstract void putShortUnaligned(Object o, long offset, short x, boolean bigEndian);

    /**
     * @see #putLongUnaligned(Object, long, long)
     */
    public abstract void putCharUnaligned(Object o, long offset, char x);

    /**
     * @see #putLongUnaligned(Object, long, long, boolean)
     */
    public abstract void putCharUnaligned(Object o, long offset, char x, boolean bigEndian);

    /**
     * Invokes the given direct byte buffer's cleaner, if any.
     *
     * @param directBuffer a direct byte buffer
     * @throws NullPointerException     if {@code directBuffer} is null
     * @throws IllegalArgumentException if {@code directBuffer} is non-direct,
     *                                  or is a {@link java.nio.Buffer#slice slice}, or is a
     *                                  {@link java.nio.Buffer#duplicate duplicate}
     */
    public abstract void invokeCleaner(java.nio.ByteBuffer directBuffer);

    // The following deprecated methods are used by JSR 166.

    @Deprecated(since = "12", forRemoval = true)
    public abstract Object getObject(Object o, long offset);

    @Deprecated(since = "12", forRemoval = true)
    public abstract Object getObjectVolatile(Object o, long offset);

    @Deprecated(since = "12", forRemoval = true)
    public abstract Object getObjectAcquire(Object o, long offset);

    @Deprecated(since = "12", forRemoval = true)
    public abstract Object getObjectOpaque(Object o, long offset);


    @Deprecated(since = "12", forRemoval = true)
    public abstract void putObject(Object o, long offset, Object x);

    @Deprecated(since = "12", forRemoval = true)
    public abstract void putObjectVolatile(Object o, long offset, Object x);

    @Deprecated(since = "12", forRemoval = true)
    public abstract void putObjectOpaque(Object o, long offset, Object x);

    @Deprecated(since = "12", forRemoval = true)
    public abstract void putObjectRelease(Object o, long offset, Object x);

    @Deprecated(since = "12", forRemoval = true)
    public abstract Object getAndSetObject(Object o, long offset, Object newValue);

    @Deprecated(since = "12", forRemoval = true)
    public abstract Object getAndSetObjectAcquire(Object o, long offset, Object newValue);

    @Deprecated(since = "12", forRemoval = true)
    public abstract Object getAndSetObjectRelease(Object o, long offset, Object newValue);


    @Deprecated(since = "12", forRemoval = true)
    public abstract boolean compareAndSetObject(Object o, long offset, Object expected, Object x);

    @Deprecated(since = "12", forRemoval = true)
    public abstract Object compareAndExchangeObject(Object o, long offset, Object expected, Object x);

    @Deprecated(since = "12", forRemoval = true)
    public abstract Object compareAndExchangeObjectAcquire(Object o, long offset, Object expected, Object x);

    @Deprecated(since = "12", forRemoval = true)
    public abstract Object compareAndExchangeObjectRelease(Object o, long offset, Object expected, Object x);

    @Deprecated(since = "12", forRemoval = true)
    public abstract boolean weakCompareAndSetObject(Object o, long offset, Object expected, Object x);

    @Deprecated(since = "12", forRemoval = true)
    public abstract boolean weakCompareAndSetObjectAcquire(Object o, long offset, Object expected, Object x);

    @Deprecated(since = "12", forRemoval = true)
    public abstract boolean weakCompareAndSetObjectPlain(Object o, long offset, Object expected, Object x);

    @Deprecated(since = "12", forRemoval = true)
    public abstract boolean weakCompareAndSetObjectRelease(Object o, long offset, Object expected, Object x);
}
