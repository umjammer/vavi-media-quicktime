package quicktime.util;

import quicktime.QTSession;

/**
 * The endian methods in this class are context sensitive, in that the big-endian methods
 * will only flip if the current platform is little endian and the little-endian methods
 * will only flip if the current platform is big endian.
 *
 * The names of the methods are provided as a means of documenting the usage of the method
 * itself. To operate as specified they rely upon the application correctly providing the
 * expected endian-ordered number to the method. Thus, the flipNativeToBigEndian and
 * flipBigEndianToNative will both flip when executed on a little-endian architecture
 * but the methods are unable to determine if the argument is actually (in this
 * case) in native little-endian order or big endian order. The application itself must
 * ensure that the provided argument is correct for these methods to return
 * their exepcted result.
 */
public final class EndianOrder {

    private static final boolean isLittleEndian = (QTSession.isCurrentOS(2)) || ((QTSession.isCurrentOS(4)) && (System.getProperty("os.arch").equals("i386")));

    /**
     * Returns true if the current runtime architecture is little endian.
     * @return a boolean
     */
    public static boolean isNativeLittleEndian() {
        return isLittleEndian;
    }

    /**
     * Returns true if the current runtime architecture is big endian.
     * @return a boolean
     */
    public static boolean isNativeBigEndian() {
        return !isLittleEndian;
    }

    /**
     * This will flip the endian order of a 16bit value iff the current runtime architecture
     * is little endian, otherwise the returned value is the same as the supplied value.
     * @param s the value to flip
     * @return the flipped value if the runtime architecture is little endian, otherwise
     *  the original value
     */
    public static short flipNativeToBigEndian16(short paramShort) {
        return isNativeLittleEndian() ? QTUtils.endianFlip16(paramShort) : paramShort;
    }

    /**
     * This will flip the endian order of a 16bit value iff the current runtime architecture
     * is little endian, otherwise the returned value is the same as the supplied value.
     * @param s the value to flip
     * @return the flipped value if the runtime architecture is little endian, otherwise
     *  the original value
     */
    public static short flipBigEndianToNative16(short paramShort) {
        return isNativeLittleEndian() ? QTUtils.endianFlip16(paramShort) : paramShort;
    }

    /**
     * This will flip the endian order of a 16bit value iff the current runtime architecture
     * is big endian, otherwise the returned value is the same as the supplied value.
     * @param s the value to flip
     * @return the flipped value if the runtime architecture is big endian, otherwise
     *  the original value
     */
    public static short flipNativeToLittleEndian16(short paramShort) {
        return isNativeBigEndian() ? QTUtils.endianFlip16(paramShort) : paramShort;
    }

    /**
     * This will flip the endian order of a 16bit value iff the current runtime architecture
     * is big endian, otherwise the returned value is the same as the supplied value.
     * @param s the value to flip
     * @return the flipped value if the runtime architecture is big endian, otherwise
     *  the original value
     */
    public static short flipLittleEndianToNative16(short paramShort) {
        return isNativeBigEndian() ? QTUtils.endianFlip16(paramShort) : paramShort;
    }

    /**
     * This will flip the endian order of a 32bit value iff the current runtime architecture
     * is little endian, otherwise the returned value is the same as the supplied value.
     * @param i the value to flip
     * @return the flipped value if the runtime architecture is little endian, otherwise
     *  the original value
     */
    public static int flipNativeToBigEndian32(int paramInt) {
        return isNativeLittleEndian() ? QTUtils.endianFlip32(paramInt) : paramInt;
    }

    /**
     * This will flip the endian order of a 32bit value iff the current runtime architecture
     * is little endian, otherwise the returned value is the same as the supplied value.
     * @param i the value to flip
     * @return the flipped value if the runtime architecture is little endian, otherwise
     *  the original value
     */
    public static int flipBigEndianToNative32(int paramInt) {
        return isNativeLittleEndian() ? QTUtils.endianFlip32(paramInt) : paramInt;
    }

    /**
     * This will flip the endian order of a 32bit value iff the current runtime architecture
     * is big endian, otherwise the returned value is the same as the supplied value.
     * @param i the value to flip
     * @return the flipped value if the runtime architecture is big endian, otherwise
     *  the original value
     */
    public static int flipNativeToLittleEndian32(int paramInt) {
        return isNativeBigEndian() ? QTUtils.endianFlip32(paramInt) : paramInt;
    }

    /**
     * This will flip the endian order of a 32bit value iff the current runtime architecture
     * is big endian, otherwise the returned value is the same as the supplied value.
     * @param i the value to flip
     * @return the flipped value if the runtime architecture is big endian, otherwise
     *  the original value
     */
    public static int flipLittleEndianToNative32(int paramInt) {
        return isNativeBigEndian() ? QTUtils.endianFlip32(paramInt) : paramInt;
    }

    /**
     * This will flip the endian order of a 64bit value iff the current runtime architecture
     * is little endian, otherwise the returned value is the same as the supplied value.
     * @param l the value to flip
     * @return the flipped value if the runtime architecture is little endian, otherwise
     *  the original value
     */
    public static long flipNativeToBigEndian64(long paramLong) {
        return isNativeLittleEndian() ? QTUtils.endianFlip64(paramLong) : paramLong;
    }

    /**
     * This will flip the endian order of a 64bit value iff the current runtime architecture
     * is little endian, otherwise the returned value is the same as the supplied value.
     * @param l the value to flip
     * @return the flipped value if the runtime architecture is little endian, otherwise
     *  the original value
     */
    public static long flipBigEndianToNative64(long paramLong) {
        return isNativeLittleEndian() ? QTUtils.endianFlip64(paramLong) : paramLong;
    }

    /**
     * This will flip the endian order of a 64bit value iff the current runtime architecture
     * is big endian, otherwise the returned value is the same as the supplied value.
     * @param l the value to flip
     * @return the flipped value if the runtime architecture is big endian, otherwise
     *  the original value
     */
    public static long flipNativeToLittleEndian64(long paramLong) {
        return isNativeBigEndian() ? QTUtils.endianFlip64(paramLong) : paramLong;
    }

    /**
     * This will flip the endian order of a 64bit value iff the current runtime architecture
     * is big endian, otherwise the returned value is the same as the supplied value.
     * @param l the value to flip
     * @return the flipped value if the runtime architecture is big endian, otherwise
     *  the original value
     */
    public static long flipLittleEndianToNative64(long paramLong) {
        return isNativeBigEndian() ? QTUtils.endianFlip64(paramLong) : paramLong;
    }

    /**
     * This will flip iff the current runtime architecture
     * is little endian the supplied src object's fields according to the characteristics of the supplied
     * EndianDescriptor. The EndianDescriptor will be applied to the src object at the specified (srcOffset)
     * number of bytes from the beginning of the src object. The flipping is done in place, so the srcObject
     * will be flipped upon the completion of this method.  Any fields that are not described or accounted for
     * by the EndianDescriptor will not be flipped.
     * @param src the object to flip iff the current runtime architecture is little endian
     * @param srcOffset the number of bytes from the start of the src object that the supplied endian descriptor
     *  will be applied.
     * @param ed the EndianDescriptor that will describe how the object should be flipped. This may NOT be null.
     * @param {@link quicktime.util.UtilException}
     * @see {@link quicktime.util.EndianOrder}
     */
    public static void flipNativeToBigEndian(Object paramObject, int paramInt, EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeLittleEndian()) {
            QTUtils.endianFlip(paramObject, paramInt, paramEndianDescriptor);
        }
    }

    /**
     * This will flip, iff the current runtime architecture
     * is little endian, the supplied src object's fields according to the characteristics of the supplied
     * EndianDescriptor. It the current runtime architecture is big endian the src object is left unchanged.
     * The EndianDescriptor will be applied to the src object at the specified (srcOffset)
     * number of bytes from the beginning of the src object. The flipping is done in place, so the srcObject
     * will be flipped upon the completion of this method.  Any fields that are not described or accounted for
     * by the EndianDescriptor will not be flipped.
     * @param src the object to flip iff the current runtime architecture is little endian
     * @param srcOffset the number of bytes from the start of the src object that the supplied endian descriptor
     *  will be applied.
     * @param ed the EndianDescriptor that will describe how the object should be flipped. This may NOT be null.
     * @param {@link quicktime.util.UtilException}
     * @see {@link quicktime.util.EndianOrder}
     */
    public static void flipBigEndianToNative(Object paramObject, int paramInt, EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeLittleEndian()) {
            QTUtils.endianFlip(paramObject, paramInt, paramEndianDescriptor);
        }
    }

    /**
     * This will flip, iff the current runtime architecture
     * is big endian, the supplied src object's fields according to the characteristics of the supplied
     * EndianDescriptor. It the current runtime architecture is little endian the src object is left unchanged.
     * The EndianDescriptor will be applied to the src object at the specified (srcOffset)
     * number of bytes from the beginning of the src object. The flipping is done in place, so the srcObject
     * will be flipped upon the completion of this method.  Any fields that are not described or accounted for
     * by the EndianDescriptor will not be flipped.
     * @param src the object to flip iff the current runtime architecture is big endian
     * @param srcOffset the number of bytes from the start of the src object that the supplied endian descriptor
     *  will be applied.
     * @param ed the EndianDescriptor that will describe how the object should be flipped. This may NOT be null.
     * @param {@link quicktime.util.UtilException}
     */
    public static void flipNativeToLittleEndian(Object paramObject, int paramInt, EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeBigEndian()) {
            QTUtils.endianFlip(paramObject, paramInt, paramEndianDescriptor);
        }
    }

    /**
     * This will flip, iff the current runtime architecture
     * is big endian, the supplied src object's fields according to the characteristics of the supplied
     * EndianDescriptor. It the current runtime architecture is little endian the src object is left unchanged.
     * The EndianDescriptor will be applied to the src object at the specified (srcOffset)
     * number of bytes from the beginning of the src object. The flipping is done in place, so the srcObject
     * will be flipped upon the completion of this method.  Any fields that are not described or accounted for
     * by the EndianDescriptor will not be flipped.
     * @param src the object to flip iff the current runtime architecture is big endian
     * @param srcOffset the number of bytes from the start of the src object that the supplied endian descriptor
     *  will be applied.
     * @param ed the EndianDescriptor that will describe how the object should be flipped. This may NOT be null.
     * @param {@link quicktime.util.UtilException}
     */
    public static void flipLittleEndianToNative(Object paramObject, int paramInt, EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeBigEndian()) {
            QTUtils.endianFlip(paramObject, paramInt, paramEndianDescriptor);
        }
    }

    /**
     * This will flip, iff the current runtime architecture
     * is little endian, the supplied src object's fields according to the characteristics of the supplied
     * EndianDescriptor and copy these flipped values to the destination object.
     * It the current runtime architecture is big endian the src object is left unchanged.
     * The EndianDescriptor will be applied to the src object at the specified (srcOffset)
     * number of bytes from the beginning of the src object and the flipped values will be copied to the specified
     * (destOffset) number of bytes into the destination object. Any fields that are not described or accounted for
     * by the EndianDescriptor will not be flipped or copied.
     * @param src the object to flip iff the current runtime architecture is little endian
     * @param srcOffset the number of bytes from the start of the src object that the supplied endian descriptor
     *  will be applied.
     * @param dest the object that will be the flipped version of the source object.
     * @param destOffset the number of bytes from the start of the dest object that the flipped values will be copied too.
     * @param ed the EndianDescriptor that will describe how the object should be flipped. This may NOT be null.
     * @param {@link quicktime.util.UtilException}
     */
    public static void flipNativeToBigEndian(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2, EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeLittleEndian()) {
            QTUtils.endianFlip(paramObject1, paramInt1, paramObject2, paramInt2, paramEndianDescriptor);
        }
    }

    /**
     * This will flip, iff the current runtime architecture
     * is little endian, the supplied src object's fields according to the characteristics of the supplied
     * EndianDescriptor and copy these flipped values to the destination object.
     * It the current runtime architecture is big endian the src object is left unchanged.
     * The EndianDescriptor will be applied to the src object at the specified (srcOffset)
     * number of bytes from the beginning of the src object and the flipped values will be copied to the specified
     * (destOffset) number of bytes into the destination object. Any fields that are not described or accounted for
     * by the EndianDescriptor will not be flipped or copied.
     * @param src the object to flip iff the current runtime architecture is little endian
     * @param srcOffset the number of bytes from the start of the src object that the supplied endian descriptor
     *  will be applied.
     * @param dest the object that will be the flipped version of the source object.
     * @param destOffset the number of bytes from the start of the dest object that the flipped values will be copied too.
     * @param ed the EndianDescriptor that will describe how the object should be flipped. This may NOT be null.
     * @param {@link quicktime.util.UtilException}
     */
    public static void flipBigEndianToNative(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2, EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeLittleEndian()) {
            QTUtils.endianFlip(paramObject1, paramInt1, paramObject2, paramInt2, paramEndianDescriptor);
        }
    }

    /**
     * This will flip, iff the current runtime architecture
     * is big endian, the supplied src object's fields according to the characteristics of the supplied
     * EndianDescriptor and copy these flipped values to the destination object.
     * It the current runtime architecture is little endian the src object is left unchanged.
     * The EndianDescriptor will be applied to the src object at the specified (srcOffset)
     * number of bytes from the beginning of the src object and the flipped values will be copied to the specified
     * (destOffset) number of bytes into the destination object. Any fields that are not described or accounted for
     * by the EndianDescriptor will not be flipped or copied.
     * @param src the object to flip iff the current runtime architecture is big endian
     * @param srcOffset the number of bytes from the start of the src object that the supplied endian descriptor
     *  will be applied.
     * @param dest the object that will be the flipped version of the source object.
     * @param destOffset the number of bytes from the start of the dest object that the flipped values will be copied too.
     * @param ed the EndianDescriptor that will describe how the object should be flipped. This may NOT be null.
     * @param {@link quicktime.util.UtilException}
     */
    public static void flipNativeToLittleEndian(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2, EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (isNativeBigEndian()) {
            QTUtils.endianFlip(paramObject1, paramInt1, paramObject2, paramInt2, paramEndianDescriptor);
        }
    }

    /**
     * This will flip, iff the current runtime architecture
     * is big endian, the supplied src object's fields according to the characteristics of the supplied
     * EndianDescriptor and copy these flipped values to the destination object.
     * It the current runtime architecture is little endian the src object is left unchanged.
     * The EndianDescriptor will be applied to the src object at the specified (srcOffset)
     * number of bytes from the beginning of the src object and the flipped values will be copied to the specified
     * (destOffset) number of bytes into the destination object. Any fields that are not described or accounted for
     * by the EndianDescriptor will not be flipped or copied.
     * @param src the object to flip iff the current runtime architecture is big endian
     * @param srcOffset the number of bytes from the start of the src object that the supplied endian descriptor
     *  will be applied.
     * @param dest the object that will be the flipped version of the source object.
     * @param destOffset the number of bytes from the start of the dest object that the flipped values will be copied too.
     * @param ed the EndianDescriptor that will describe how the object should be flipped. This may NOT be null.
     * @param {@link quicktime.util.UtilException}
     */
    public static void flipLittleEndianToNative(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2, EndianDescriptor paramEndianDescriptor) throws UtilException {
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
