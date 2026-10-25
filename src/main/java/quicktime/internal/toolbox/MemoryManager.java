/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.nio.ByteBuffer;


/**
 * MemoryManager.
 * <p>
 * Java replacement for the Memory Manager functions and the QTJava memory access primitives,
 * implemented on the emulated address space {@link Heap}.
 * Primitive arrays are viewed as big endian memory addressed by byte offsets.
 */
public final class MemoryManager {

    private MemoryManager() {
    }

    // ---- Memory Manager

    public static void BlockMove(int src, int dst, int size) {
        Heap.move(src, dst, size);
    }

    public static void DisposeHandle(int h) {
        Heap.disposeHandle(h);
    }

    public static void DisposePtr(int p) {
        Heap.free(p);
    }

    public static int GetHandleSize(int h) {
        return Heap.handleSize(h);
    }

    public static void HLock(int h) {
    }

    public static void HLockHi(int h) {
    }

    public static void HUnlock(int h) {
    }

    public static void MoveHHi(int h) {
    }

    public static void ReserveMem(int size) {
    }

    /** appends the contents of h1 to h2 */
    public static short HandAndHand(int h1, int h2) {
        if (h1 == 0 || h2 == 0) {
            return Heap.nilHandleErr;
        }
        byte[] a = Heap.handleBytes(h1);
        int size = Heap.handleSize(h2);
        Heap.setHandleSize(h2, size + a.length);
        Heap.write(Heap.deref(h2) + size, a, 0, a.length);
        return Heap.memError();
    }

    public static short MemError() {
        return Heap.memError();
    }

    public static int NewEmptyHandle() {
        return Heap.newEmptyHandle();
    }

    public static int NewHandle(int size) {
        return Heap.newHandle(size);
    }

    public static int NewHandleClear(int size) {
        return Heap.newHandle(size);
    }

    public static int NewPtr(int size) {
        return Heap.newPtr(size);
    }

    public static int NewPtrClear(int size) {
        return Heap.newPtr(size);
    }

    public static short PtrToHand(int src, int[] dstHandle, int size) {
        int h = Heap.newHandle(size);
        Heap.move(src, Heap.deref(h), size);
        dstHandle[0] = h;
        return Heap.memError();
    }

    public static void SetHandleSize(int h, int size) {
        Heap.setHandleSize(h, size);
    }

    public static void SetPtrSize(int p, int size) {
        Heap.setPtrSize(p, size);
    }

    // ---- array <-> pointer copies, offsets and sizes are in bytes

    private static void toPointer(Object array, int arrayOffset, int ptr, int ptrOffset, int size) {
        byte[] b = new byte[size];
        Bytes.fromArray(array, arrayOffset, b, 0, size);
        Heap.write(ptr + ptrOffset, b, 0, size);
    }

    private static void fromPointer(int ptr, int ptrOffset, Object array, int arrayOffset, int size) {
        byte[] b = Heap.read(ptr + ptrOffset, size);
        Bytes.toArray(b, 0, array, arrayOffset, size);
    }

    public static void copyArrayToPointer(byte[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        Heap.write(ptr + ptrOffset, array, arrayOffset, size);
    }

    public static void copyArrayToPointer(char[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        toPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    public static void copyArrayToPointer(double[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        toPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    public static void copyArrayToPointer(float[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        toPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    public static void copyArrayToPointer(int[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        toPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    public static void copyArrayToPointer(long[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        toPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    public static void copyArrayToPointer(short[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        toPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    public static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        Heap.read(ptr + ptrOffset, array, arrayOffset, size);
    }

    public static void copyPointerToArray(int ptr, int ptrOffset, char[] array, int arrayOffset, int size) {
        fromPointer(ptr, ptrOffset, array, arrayOffset, size);
    }

    public static void copyPointerToArray(int ptr, int ptrOffset, double[] array, int arrayOffset, int size) {
        fromPointer(ptr, ptrOffset, array, arrayOffset, size);
    }

    public static void copyPointerToArray(int ptr, int ptrOffset, float[] array, int arrayOffset, int size) {
        fromPointer(ptr, ptrOffset, array, arrayOffset, size);
    }

    public static void copyPointerToArray(int ptr, int ptrOffset, int[] array, int arrayOffset, int size) {
        fromPointer(ptr, ptrOffset, array, arrayOffset, size);
    }

    public static void copyPointerToArray(int ptr, int ptrOffset, long[] array, int arrayOffset, int size) {
        fromPointer(ptr, ptrOffset, array, arrayOffset, size);
    }

    public static void copyPointerToArray(int ptr, int ptrOffset, short[] array, int arrayOffset, int size) {
        fromPointer(ptr, ptrOffset, array, arrayOffset, size);
    }

    // ---- arrays

    public static byte getByteFromArray(byte[] array, int offset) {
        return array[offset];
    }

    public static byte getByteFromArray(int[] array, int offset) {
        return Bytes.view(array).get(offset);
    }

    public static short getShortFromArray(byte[] array, int offset) {
        return Bytes.getShort(array, offset);
    }

    public static short getShortFromArray(float[] array, int offset) {
        return Bytes.view(array).getShort(offset);
    }

    public static short getShortFromArray(int[] array, int offset) {
        return Bytes.view(array).getShort(offset);
    }

    public static int getIntFromArray(byte[] array, int offset) {
        return Bytes.getInt(array, offset);
    }

    public static int getIntFromArray(float[] array, int offset) {
        return Bytes.view(array).getInt(offset);
    }

    public static int getIntFromArray(int[] array, int offset) {
        return (offset & 3) == 0 ? array[offset >> 2] : Bytes.view(array).getInt(offset);
    }

    public static long getLongFromArray(byte[] array, int offset) {
        return Bytes.getLong(array, offset);
    }

    public static long getLongFromArray(float[] array, int offset) {
        return Bytes.view(array).getLong(offset);
    }

    public static long getLongFromArray(int[] array, int offset) {
        return Bytes.view(array).getLong(offset);
    }

    public static float getFloatFromArray(byte[] array, int offset) {
        return Float.intBitsToFloat(Bytes.getInt(array, offset));
    }

    public static double getDoubleFromArray(byte[] array, int offset) {
        return Double.longBitsToDouble(Bytes.getLong(array, offset));
    }

    public static void setShortInArray(byte[] array, int offset, short value) {
        Bytes.putShort(array, offset, value);
    }

    public static void setShortInArray(float[] array, int offset, short value) {
        ByteBuffer bb = Bytes.view(array);
        bb.putShort(offset, value);
        Bytes.commit(bb, array);
    }

    public static void setShortInArray(int[] array, int offset, short value) {
        ByteBuffer bb = Bytes.view(array);
        bb.putShort(offset, value);
        Bytes.commit(bb, array);
    }

    public static void setIntInArray(byte[] array, int offset, int value) {
        Bytes.putInt(array, offset, value);
    }

    public static void setIntInArray(float[] array, int offset, int value) {
        ByteBuffer bb = Bytes.view(array);
        bb.putInt(offset, value);
        Bytes.commit(bb, array);
    }

    public static void setIntInArray(int[] array, int offset, int value) {
        if ((offset & 3) == 0) {
            array[offset >> 2] = value;
        } else {
            ByteBuffer bb = Bytes.view(array);
            bb.putInt(offset, value);
            Bytes.commit(bb, array);
        }
    }

    public static void setLongInArray(byte[] array, int offset, long value) {
        Bytes.putLong(array, offset, value);
    }

    public static void setLongInArray(float[] array, int offset, long value) {
        ByteBuffer bb = Bytes.view(array);
        bb.putLong(offset, value);
        Bytes.commit(bb, array);
    }

    public static void setLongInArray(int[] array, int offset, long value) {
        ByteBuffer bb = Bytes.view(array);
        bb.putLong(offset, value);
        Bytes.commit(bb, array);
    }

    public static void setFloatInArray(byte[] array, int offset, float value) {
        Bytes.putInt(array, offset, Float.floatToRawIntBits(value));
    }

    public static void setDoubleInArray(byte[] array, int offset, double value) {
        Bytes.putLong(array, offset, Double.doubleToRawLongBits(value));
    }

    // ---- pointers

    public static byte getByteFromPointer(int ptr, int offset) {
        return Heap.getByte(ptr + offset);
    }

    public static short getShortFromPointer(int ptr, int offset) {
        return Heap.getShort(ptr + offset);
    }

    public static int getIntFromPointer(int ptr, int offset) {
        return Heap.getInt(ptr + offset);
    }

    public static long getLongFromPointer(int ptr, int offset) {
        return Heap.getLong(ptr + offset);
    }

    public static float getFloatFromPointer(int ptr, int offset) {
        return Float.intBitsToFloat(Heap.getInt(ptr + offset));
    }

    public static double getDoubleFromPointer(int ptr, int offset) {
        return Double.longBitsToDouble(Heap.getLong(ptr + offset));
    }

    public static void setByteInPointer(int ptr, int offset, byte value) {
        Heap.putByte(ptr + offset, value);
    }

    public static void setShortInPointer(int ptr, int offset, short value) {
        Heap.putShort(ptr + offset, value);
    }

    public static void setIntInPointer(int ptr, int offset, int value) {
        Heap.putInt(ptr + offset, value);
    }

    public static void setLongInPointer(int ptr, int offset, long value) {
        Heap.putLong(ptr + offset, value);
    }

    public static void setFloatInPointer(int ptr, int offset, float value) {
        Heap.putInt(ptr + offset, Float.floatToRawIntBits(value));
    }

    public static void setDoubleInPointer(int ptr, int offset, double value) {
        Heap.putLong(ptr + offset, Double.doubleToRawLongBits(value));
    }

    // ---- handles

    public static byte getByteFromHandle(int h, int offset) {
        return Heap.getByte(Heap.deref(h) + offset);
    }

    public static short getShortFromHandle(int h, int offset) {
        return Heap.getShort(Heap.deref(h) + offset);
    }

    public static int getIntFromHandle(int h, int offset) {
        return Heap.getInt(Heap.deref(h) + offset);
    }

    public static long getLongFromHandle(int h, int offset) {
        return Heap.getLong(Heap.deref(h) + offset);
    }

    public static float getFloatFromHandle(int h, int offset) {
        return Float.intBitsToFloat(Heap.getInt(Heap.deref(h) + offset));
    }

    public static double getDoubleFromHandle(int h, int offset) {
        return Double.longBitsToDouble(Heap.getLong(Heap.deref(h) + offset));
    }

    public static void setByteInHandle(int h, int offset, byte value) {
        Heap.putByte(Heap.deref(h) + offset, value);
    }

    public static void setShortInHandle(int h, int offset, short value) {
        Heap.putShort(Heap.deref(h) + offset, value);
    }

    public static void setIntInHandle(int h, int offset, int value) {
        Heap.putInt(Heap.deref(h) + offset, value);
    }

    public static void setLongInHandle(int h, int offset, long value) {
        Heap.putLong(Heap.deref(h) + offset, value);
    }

    public static void setFloatInHandle(int h, int offset, float value) {
        Heap.putInt(Heap.deref(h) + offset, Float.floatToRawIntBits(value));
    }

    public static void setDoubleInHandle(int h, int offset, double value) {
        Heap.putLong(Heap.deref(h) + offset, Double.doubleToRawLongBits(value));
    }
}
