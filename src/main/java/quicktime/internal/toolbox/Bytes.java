/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * Big endian accessors for byte arrays and Java primitive arrays viewed as memory.
 * <p>
 * The QuickTime for Java classes marshal native structures through byte arrays and primitive arrays,
 * addressing them with byte offsets ({@code getShortFromArray(int[], byteOffset)} etc.).
 */
public final class Bytes {

    private Bytes() {
    }

    /** classic Mac OS text encoding */
    public static final Charset MAC_ROMAN = Charset.isSupported("x-MacRoman") ? Charset.forName("x-MacRoman") : StandardCharsets.ISO_8859_1;

    public static short getShort(byte[] b, int o) {
        return (short) (((b[o] & 0xff) << 8) | (b[o + 1] & 0xff));
    }

    public static void putShort(byte[] b, int o, int v) {
        b[o] = (byte) (v >> 8);
        b[o + 1] = (byte) v;
    }

    public static int getInt(byte[] b, int o) {
        return ((b[o] & 0xff) << 24) | ((b[o + 1] & 0xff) << 16) | ((b[o + 2] & 0xff) << 8) | (b[o + 3] & 0xff);
    }

    public static void putInt(byte[] b, int o, int v) {
        b[o] = (byte) (v >> 24);
        b[o + 1] = (byte) (v >> 16);
        b[o + 2] = (byte) (v >> 8);
        b[o + 3] = (byte) v;
    }

    public static long getLong(byte[] b, int o) {
        return ((long) getInt(b, o) << 32) | (getInt(b, o + 4) & 0xffff_ffffL);
    }

    public static void putLong(byte[] b, int o, long v) {
        putInt(b, o, (int) (v >> 32));
        putInt(b, o + 4, (int) v);
    }

    /** 16.16 fixed to double */
    public static double fixedToDouble(int f) {
        return f / 65536.0;
    }

    /** double to 16.16 fixed */
    public static int doubleToFixed(double d) {
        return (int) Math.round(d * 65536.0);
    }

    /** @return an OSType from 4 characters */
    public static int fourCC(String s) {
        byte[] b = s.getBytes(MAC_ROMAN);
        return ((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16) | ((b[2] & 0xff) << 8) | (b[3] & 0xff);
    }

    /** @return 4 characters from an OSType */
    public static String fourCC(int t) {
        return new String(new byte[] {(byte) (t >> 24), (byte) (t >> 16), (byte) (t >> 8), (byte) t}, MAC_ROMAN);
    }

    /** @return a Pascal string read at the offset */
    public static String getPString(byte[] b, int o) {
        int len = b[o] & 0xff;
        return new String(b, o + 1, Math.min(len, b.length - o - 1), MAC_ROMAN);
    }

    /** writes a Pascal string, truncated to max - 1 characters */
    public static void putPString(byte[] b, int o, String s, int max) {
        byte[] t = s.getBytes(MAC_ROMAN);
        int len = Math.min(Math.min(t.length, 255), max - 1);
        b[o] = (byte) len;
        System.arraycopy(t, 0, b, o + 1, len);
    }

    // ---- primitive arrays as big endian memory

    /** @return a big endian byte view of the array */
    static ByteBuffer view(Object array) {
        return switch (array) {
            case byte[] a -> ByteBuffer.wrap(a);
            case short[] a -> {
                ByteBuffer bb = ByteBuffer.allocate(a.length * 2);
                bb.asShortBuffer().put(a);
                yield bb;
            }
            case char[] a -> {
                ByteBuffer bb = ByteBuffer.allocate(a.length * 2);
                bb.asCharBuffer().put(a);
                yield bb;
            }
            case int[] a -> {
                ByteBuffer bb = ByteBuffer.allocate(a.length * 4);
                bb.asIntBuffer().put(a);
                yield bb;
            }
            case float[] a -> {
                ByteBuffer bb = ByteBuffer.allocate(a.length * 4);
                bb.asFloatBuffer().put(a);
                yield bb;
            }
            case long[] a -> {
                ByteBuffer bb = ByteBuffer.allocate(a.length * 8);
                bb.asLongBuffer().put(a);
                yield bb;
            }
            case double[] a -> {
                ByteBuffer bb = ByteBuffer.allocate(a.length * 8);
                bb.asDoubleBuffer().put(a);
                yield bb;
            }
            default -> throw new IllegalArgumentException(String.valueOf(array));
        };
    }

    /** writes a modified view back into the array */
    static void commit(ByteBuffer bb, Object array) {
        bb.order(ByteOrder.BIG_ENDIAN).rewind();
        switch (array) {
            case byte[] a -> { /* direct */ }
            case short[] a -> bb.asShortBuffer().get(a);
            case char[] a -> bb.asCharBuffer().get(a);
            case int[] a -> bb.asIntBuffer().get(a);
            case float[] a -> bb.asFloatBuffer().get(a);
            case long[] a -> bb.asLongBuffer().get(a);
            case double[] a -> bb.asDoubleBuffer().get(a);
            default -> throw new IllegalArgumentException(String.valueOf(array));
        }
    }

    /** copies bytes out of a primitive array viewed as memory */
    static void fromArray(Object array, int byteOffset, byte[] dst, int dstOffset, int length) {
        if (array instanceof byte[] a) {
            System.arraycopy(a, byteOffset, dst, dstOffset, length);
        } else {
            view(array).get(byteOffset, dst, dstOffset, length);
        }
    }

    /** copies bytes into a primitive array viewed as memory */
    static void toArray(byte[] src, int srcOffset, Object array, int byteOffset, int length) {
        if (array instanceof byte[] a) {
            System.arraycopy(src, srcOffset, a, byteOffset, length);
        } else {
            ByteBuffer bb = view(array);
            bb.put(byteOffset, src, srcOffset, length);
            commit(bb, array);
        }
    }
}
