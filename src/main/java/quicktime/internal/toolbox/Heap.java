/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.util.Map;
import java.util.TreeMap;


/**
 * Emulated 32 bit address space of the classic Mac OS Memory Manager.
 * <p>
 * QuickTime for Java passes pointers, handles and opaque references around as {@code int}s,
 * so every block lives at a virtual address. Memory is big endian, which matches the
 * byte order {@link quicktime.util.EndianOrder} assumes on non i386 Mac OS X and the byte order
 * of QuickTime structures on disk.
 * <p>
 * A handle is the address of a 4 byte master pointer block that holds the address of the data block
 * (or 0 for an empty handle), so {@code *handle} works like on the real Memory Manager.
 * <p>
 * Opaque toolbox objects (movies, tracks, time bases, ...) are blocks too, with a Java peer
 * attached; see {@link #newObject(Object, int)} and {@link #peer(int, Class)}.
 */
public final class Heap {

    private Heap() {
    }

    /** a memory block */
    static final class Block {
        final int address;
        /** the region reserved in the address space, data may grow in place up to here */
        final int capacity;
        byte[] data;
        int size;
        /** Java peer of an opaque toolbox object, may be null */
        Object peer;

        Block(int address, int capacity, int size) {
            this.address = address;
            this.capacity = capacity;
            this.size = size;
            this.data = new byte[size];
        }

        void ensure(int newSize) {
            if (newSize > data.length) {
                byte[] d = new byte[Math.max(newSize, Math.min(capacity, data.length * 2))];
                System.arraycopy(data, 0, d, 0, size);
                data = d;
            }
        }
    }

    /** first address handed out, low memory stays unmapped so that small integers are never valid pointers */
    private static final int BASE = 0x0010_0000;

    /** last address (exclusive), keep addresses positive */
    private static final long LIMIT = 0x7fff_0000L;

    private static final int ALIGN = 16;

    private static final TreeMap<Integer, Block> blocks = new TreeMap<>();

    private static long next = BASE;

    /** memFullErr */
    public static final short memFullErr = -108;

    /** nilHandleErr */
    public static final short nilHandleErr = -109;

    /** memWZErr, free block or invalid address */
    public static final short memWZErr = -111;

    private static final ThreadLocal<Short> memError = ThreadLocal.withInitial(() -> (short) 0);

    /** @return the result of the last Memory Manager call on this thread */
    public static short memError() {
        return memError.get();
    }

    static void setMemError(int err) {
        memError.set((short) err);
    }

    private static int reserve(int size) {
        long cap = ((Math.max(size, 4) + ALIGN - 1) / ALIGN) * ALIGN + ALIGN;
        if (next + cap < LIMIT) {
            long a = next;
            next += cap;
            return (int) a;
        }
        // address space wrapped, find a gap
        long prevEnd = BASE;
        for (Map.Entry<Integer, Block> e : blocks.entrySet()) {
            if (e.getKey() - prevEnd >= cap) {
                return (int) prevEnd;
            }
            prevEnd = (long) e.getKey() + e.getValue().capacity;
        }
        if (LIMIT - prevEnd >= cap) {
            return (int) prevEnd;
        }
        throw new OutOfMemoryError("emulated address space exhausted");
    }

    /**
     * Allocates a block.
     * @param slack extra address space reserved after the block so that it can grow in place
     * @return the address of the new block
     */
    static synchronized int alloc(int size, int slack) {
        if (size < 0) {
            setMemError(memFullErr);
            return 0;
        }
        int capacity = (int) Math.min(Integer.MAX_VALUE / 2, (long) size + slack);
        int address = reserve(capacity);
        Block b = new Block(address, ((Math.max(capacity, 4) + ALIGN - 1) / ALIGN) * ALIGN, size);
        blocks.put(address, b);
        setMemError(0);
        return address;
    }

    /** @return the address of a new block */
    public static int newPtr(int size) {
        return alloc(size, size + 256);
    }

    /** disposes the block at the address */
    public static synchronized void free(int address) {
        if (address == 0) {
            setMemError(0);
            return;
        }
        Block b = blocks.remove(address);
        setMemError(b == null ? memWZErr : 0);
    }

    /** @return the block containing the address */
    static synchronized Block block(int address) {
        Map.Entry<Integer, Block> e = blocks.floorEntry(address);
        if (e == null) {
            throw new IllegalArgumentException("invalid address: 0x" + Integer.toHexString(address));
        }
        Block b = e.getValue();
        if (address - b.address > b.size) {
            throw new IllegalArgumentException("invalid address: 0x" + Integer.toHexString(address) + " (block 0x" + Integer.toHexString(b.address) + ", size " + b.size + ")");
        }
        return b;
    }

    /** @return true if the address points into an allocated block */
    public static synchronized boolean isValid(int address) {
        Map.Entry<Integer, Block> e = blocks.floorEntry(address);
        return e != null && address - e.getValue().address <= e.getValue().size;
    }

    /** @return the size of the block starting at the address */
    public static synchronized int ptrSize(int address) {
        Block b = blocks.get(address);
        if (b == null) {
            setMemError(memWZErr);
            return 0;
        }
        setMemError(0);
        return b.size;
    }

    /** resizes the block in place, fails with memFullErr if it cannot grow in place */
    public static synchronized void setPtrSize(int address, int size) {
        Block b = blocks.get(address);
        if (b == null) {
            setMemError(memWZErr);
            return;
        }
        if (size < 0 || size > b.capacity) {
            setMemError(memFullErr);
            return;
        }
        b.ensure(size);
        if (size < b.size) {
            java.util.Arrays.fill(b.data, size, b.size, (byte) 0);
        }
        b.size = size;
        setMemError(0);
    }

    // ---- handles

    /** @return a new handle with a data block of the size */
    public static synchronized int newHandle(int size) {
        if (size < 0) {
            setMemError(memFullErr);
            return 0;
        }
        int h = alloc(4, 0);
        int p = alloc(size, size / 2 + 64);
        putInt(h, p);
        setMemError(0);
        return h;
    }

    /** @return a new handle with no data block */
    public static synchronized int newEmptyHandle() {
        int h = alloc(4, 0);
        putInt(h, 0);
        return h;
    }

    /** @return a new handle holding a copy of the bytes */
    public static synchronized int newHandle(byte[] bytes) {
        int h = newHandle(bytes.length);
        write(deref(h), bytes, 0, bytes.length);
        return h;
    }

    /** @return the data address of the handle */
    public static synchronized int deref(int handle) {
        if (handle == 0) {
            throw new IllegalArgumentException("nil handle");
        }
        return getInt(handle);
    }

    public static synchronized int handleSize(int handle) {
        if (handle == 0) {
            setMemError(nilHandleErr);
            return 0;
        }
        int p = getInt(handle);
        setMemError(0);
        return p == 0 ? 0 : block(p).size;
    }

    public static synchronized void setHandleSize(int handle, int size) {
        if (handle == 0) {
            setMemError(nilHandleErr);
            return;
        }
        if (size < 0) {
            setMemError(memFullErr);
            return;
        }
        int p = getInt(handle);
        if (p == 0) {
            p = alloc(size, size / 2 + 64);
            putInt(handle, p);
            setMemError(0);
            return;
        }
        Block b = block(p);
        if (size <= b.capacity) {
            b.ensure(size);
            if (size < b.size) {
                java.util.Arrays.fill(b.data, size, b.size, (byte) 0);
            }
            b.size = size;
        } else {
            // relocate, like the real Memory Manager does with unlocked handles
            int q = alloc(size, size / 2 + 64);
            Block c = block(q);
            System.arraycopy(b.data, 0, c.data, 0, b.size);
            blocks.remove(p);
            putInt(handle, q);
        }
        setMemError(0);
    }

    public static synchronized void disposeHandle(int handle) {
        if (handle == 0) {
            setMemError(nilHandleErr);
            return;
        }
        int p = getInt(handle);
        if (p != 0) {
            blocks.remove(p);
        }
        blocks.remove(handle);
        setMemError(0);
    }

    /** @return a copy of the handle's data */
    public static synchronized byte[] handleBytes(int handle) {
        int size = handleSize(handle);
        byte[] bytes = new byte[size];
        if (size > 0) {
            read(deref(handle), bytes, 0, size);
        }
        return bytes;
    }

    /** replaces the handle's data */
    public static synchronized void setHandleBytes(int handle, byte[] bytes) {
        setHandleSize(handle, bytes.length);
        write(deref(handle), bytes, 0, bytes.length);
    }

    // ---- objects

    /**
     * Allocates an opaque toolbox object.
     * @param size size of the native structure the object exposes, at least 4 bytes are allocated
     * @return the address used as the object reference
     */
    public static synchronized int newObject(Object peer, int size) {
        int address = alloc(Math.max(4, size), 0);
        blocks.get(address).peer = peer;
        return address;
    }

    /** @return the Java peer of an opaque object, null if the reference is 0 */
    public static synchronized <T> T peer(int address, Class<T> clazz) {
        if (address == 0) {
            return null;
        }
        Block b = blocks.get(address);
        if (b == null || !clazz.isInstance(b.peer)) {
            throw new IllegalArgumentException("not a " + clazz.getSimpleName() + ": 0x" + Integer.toHexString(address));
        }
        return clazz.cast(b.peer);
    }

    /** @return the Java peer of an opaque object, or null if the address is not one of the class */
    public static synchronized <T> T peerOrNull(int address, Class<T> clazz) {
        if (address == 0) {
            return null;
        }
        Block b = blocks.get(address);
        return b != null && clazz.isInstance(b.peer) ? clazz.cast(b.peer) : null;
    }

    /**
     * @return the backing array of the block starting at the address, for rasters sharing the memory.
     *         the block must not be resized while the array is in use
     */
    public static synchronized byte[] backing(int address) {
        Block b = blocks.get(address);
        if (b == null) {
            throw new IllegalArgumentException("not a block: 0x" + Integer.toHexString(address));
        }
        if (b.data.length != b.size) {
            b.data = java.util.Arrays.copyOf(b.data, b.size);
        }
        return b.data;
    }

    // ---- access

    private static Block checked(int address, int length) {
        Block b = block(address);
        int offset = address - b.address;
        if (length < 0 || offset + length > b.size) {
            throw new IndexOutOfBoundsException("access out of block: 0x" + Integer.toHexString(address) + "+" + length + " (block 0x" + Integer.toHexString(b.address) + ", size " + b.size + ")");
        }
        return b;
    }

    public static synchronized void read(int address, byte[] dst, int offset, int length) {
        if (length == 0) {
            return;
        }
        Block b = checked(address, length);
        System.arraycopy(b.data, address - b.address, dst, offset, length);
    }

    public static synchronized byte[] read(int address, int length) {
        byte[] bytes = new byte[length];
        read(address, bytes, 0, length);
        return bytes;
    }

    public static synchronized void write(int address, byte[] src, int offset, int length) {
        if (length == 0) {
            return;
        }
        Block b = checked(address, length);
        System.arraycopy(src, offset, b.data, address - b.address, length);
    }

    /** BlockMove, overlapping regions are handled */
    public static synchronized void move(int src, int dst, int length) {
        if (length <= 0) {
            return;
        }
        Block s = checked(src, length);
        Block d = checked(dst, length);
        System.arraycopy(s.data, src - s.address, d.data, dst - d.address, length);
    }

    public static synchronized byte getByte(int address) {
        Block b = checked(address, 1);
        return b.data[address - b.address];
    }

    public static synchronized void putByte(int address, byte v) {
        Block b = checked(address, 1);
        b.data[address - b.address] = v;
    }

    public static synchronized short getShort(int address) {
        Block b = checked(address, 2);
        return Bytes.getShort(b.data, address - b.address);
    }

    public static synchronized void putShort(int address, short v) {
        Block b = checked(address, 2);
        Bytes.putShort(b.data, address - b.address, v);
    }

    public static synchronized int getInt(int address) {
        Block b = checked(address, 4);
        return Bytes.getInt(b.data, address - b.address);
    }

    public static synchronized void putInt(int address, int v) {
        Block b = checked(address, 4);
        Bytes.putInt(b.data, address - b.address, v);
    }

    public static synchronized long getLong(int address) {
        Block b = checked(address, 8);
        return Bytes.getLong(b.data, address - b.address);
    }

    public static synchronized void putLong(int address, long v) {
        Block b = checked(address, 8);
        Bytes.putLong(b.data, address - b.address, v);
    }
}
