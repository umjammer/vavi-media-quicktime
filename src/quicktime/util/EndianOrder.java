
package quicktime.util;

import quicktime.QTSession;


public final class EndianOrder {
    private static final boolean isLittleEndian = (QTSession
            .isCurrentOS(2)) || ((QTSession.isCurrentOS(4)) && (System.getProperty("os.arch").equals("i386")));

    public static boolean isNativeLittleEndian() {
        return isLittleEndian;
    }

    public static boolean isNativeBigEndian() {
        return !isLittleEndian;
    }

    public static short flipNativeToBigEndian16(short paramShort) {
        return isNativeLittleEndian() ? QTUtils.endianFlip16(paramShort) : paramShort;
    }

    public static short flipBigEndianToNative16(short paramShort) {
        return isNativeLittleEndian() ? QTUtils.endianFlip16(paramShort) : paramShort;
    }

    public static short flipNativeToLittleEndian16(short paramShort) {
        return isNativeBigEndian() ? QTUtils.endianFlip16(paramShort) : paramShort;
    }

    public static short flipLittleEndianToNative16(short paramShort) {
        return isNativeBigEndian() ? QTUtils.endianFlip16(paramShort) : paramShort;
    }

    public static int flipNativeToBigEndian32(int paramInt) {
        return isNativeLittleEndian() ? QTUtils.endianFlip32(paramInt) : paramInt;
    }

    public static int flipBigEndianToNative32(int paramInt) {
        return isNativeLittleEndian() ? QTUtils.endianFlip32(paramInt) : paramInt;
    }

    public static int flipNativeToLittleEndian32(int paramInt) {
        return isNativeBigEndian() ? QTUtils.endianFlip32(paramInt) : paramInt;
    }

    public static int flipLittleEndianToNative32(int paramInt) {
        return isNativeBigEndian() ? QTUtils.endianFlip32(paramInt) : paramInt;
    }

    public static long flipNativeToBigEndian64(long paramLong) {
        return isNativeLittleEndian() ? QTUtils.endianFlip64(paramLong) : paramLong;
    }

    public static long flipBigEndianToNative64(long paramLong) {
        return isNativeLittleEndian() ? QTUtils.endianFlip64(paramLong) : paramLong;
    }

    public static long flipNativeToLittleEndian64(long paramLong) {
        return isNativeBigEndian() ? QTUtils.endianFlip64(paramLong) : paramLong;
    }

    public static long flipLittleEndianToNative64(long paramLong) {
        return isNativeBigEndian() ? QTUtils.endianFlip64(paramLong) : paramLong;
    }

    public static void flipNativeToBigEndian(Object paramObject,
                                             int paramInt,
                                             EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeLittleEndian()) {
            QTUtils.endianFlip(paramObject, paramInt, paramEndianDescriptor);
        }
    }

    public static void flipBigEndianToNative(Object paramObject,
                                             int paramInt,
                                             EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeLittleEndian()) {
            QTUtils.endianFlip(paramObject, paramInt, paramEndianDescriptor);
        }
    }

    public static void flipNativeToLittleEndian(Object paramObject,
                                                int paramInt,
                                                EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeBigEndian()) {
            QTUtils.endianFlip(paramObject, paramInt, paramEndianDescriptor);
        }
    }

    public static void flipLittleEndianToNative(Object paramObject,
                                                int paramInt,
                                                EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeBigEndian()) {
            QTUtils.endianFlip(paramObject, paramInt, paramEndianDescriptor);
        }
    }

    public static void flipNativeToBigEndian(Object paramObject1,
                                             int paramInt1,
                                             Object paramObject2,
                                             int paramInt2,
                                             EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeLittleEndian()) {
            QTUtils.endianFlip(paramObject1, paramInt1, paramObject2, paramInt2, paramEndianDescriptor);
        }
    }

    public static void flipBigEndianToNative(Object paramObject1,
                                             int paramInt1,
                                             Object paramObject2,
                                             int paramInt2,
                                             EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeLittleEndian()) {
            QTUtils.endianFlip(paramObject1, paramInt1, paramObject2, paramInt2, paramEndianDescriptor);
        }
    }

    public static void flipNativeToLittleEndian(Object paramObject1,
                                                int paramInt1,
                                                Object paramObject2,
                                                int paramInt2,
                                                EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeBigEndian()) {
            QTUtils.endianFlip(paramObject1, paramInt1, paramObject2, paramInt2, paramEndianDescriptor);
        }
    }

    public static void flipLittleEndianToNative(Object paramObject1,
                                                int paramInt1,
                                                Object paramObject2,
                                                int paramInt2,
                                                EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeBigEndian()) {
            QTUtils.endianFlip(paramObject1, paramInt1, paramObject2, paramInt2, paramEndianDescriptor);
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/util/
 * EndianOrder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
