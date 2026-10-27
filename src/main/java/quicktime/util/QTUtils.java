package quicktime.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;

/**
 * This provides a set of routines to convert between types that the
 * QT calls expect and the basic Java classes and types
 *
 * Some of the structures in QT are kept in their native BigEndian format.
 * QTStructs with BigEndian formats:
 * ToneDescription
 * NoteRequest
 *
 * These conversions are generally done for you where they are required in the
 * get/set methods of the classes concerned.
 */
public final class QTUtils implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(QTUtils.class));
            }
        }.establish();
    }

    private static final boolean isWin = (!QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4));

    private static final int kIsPointer = 1;

    private static final int kIsByteArray = 16;

    private static final int kIsIntArray = 256;

    private static final int kIsFloatArray = 4096;

    private static final int kShiftModShiftMask = 9;

    private static final int kControlModShiftMask = 12;

    private static final int kCommandModShiftMask = 8;

    private static final int kOptionModShiftMask = 11;

    /**
     * Convert the modifier values in java InputEvents to those expected by QT.
     */
    public static int convertEventModifier(int paramInt) {
        if (paramInt == 0)
            return 0;
        int i = 0;
        if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
            if ((paramInt & 0x1) != 0)
                i = 1 << kShiftModShiftMask;
            if ((paramInt & 0x2) != 0)
                i |= 1 << kControlModShiftMask;
            if ((paramInt & 0x4) != 0)
                i |= 1 << kCommandModShiftMask;
            if ((paramInt & 0x8) != 0)
                i |= 1 << kOptionModShiftMask;
        } else if (QTSession.isCurrentOS(2)) {
            if (((paramInt & 0x2) != 0) && ((paramInt & 0x8) != 0)) {
                i = 1 << kOptionModShiftMask;
            } else if ((paramInt & 0x2) != 0) {
                i = 1 << kCommandModShiftMask;
            } else if ((paramInt & 0x8) != 0) {
                i = 1 << kControlModShiftMask;
            }
            if ((paramInt & 0x1) != 0)
                i |= 1 << kShiftModShiftMask;
        }
        return i;
    }

    /**
     * Convert the key values in java KeyEvents to those expected by QT.
     */
    public static int convertKeyValue(int paramInt) {
        return switch (paramInt) {
            case 10 -> 13;
            case 37 -> 28;
            case 39 -> 29;
            case 38 -> 30;
            case 40 -> 31;
            case 36 -> 1;
            case 35 -> 4;
            case 33 -> 11;
            case 34 -> 12;
            default -> paramInt;
        };
    }

    /**
     * Convert a float to a 16.16 Fixed format
     */
    public static int X2Fix(float paramFloat) {
        return (int) (paramFloat * 65536.0F);
    }

    /**
     * Convert a float to a 16.16 Fixed format
     */
    public static float Fix2X(int paramInt) {
        return (float) (paramInt / 65536.0D);
    }

    /**
     * Convert a 16.16 Unsigned Fixed format to a float
     */
    public static int X2UFix(float paramFloat) {
        return (int) (long) (paramFloat * 65536.0D);
    }

    /**
     * Convert a 16.16 Unsigned Fixed format to a float
     */
    public static float UFix2X(int paramInt) {
        return (float) ((paramInt & 0xFFFFFFFFL) / 65536.0D);
    }

    /**
     * Converts a float to a 2.30 Fract format
     */
    public static int X2Fract(float paramFloat) {
        return (int) (paramFloat * 1.07374182E9F);
    }

    /**
     * Convert a 2.30 Fract format to a float
     */
    public static float Fract2X(int paramInt) {
        return (float) (paramInt / 1.073741824E9D);
    }

    /**
     * Convert a float to a 8.8 Fixed format
     */
    public static short X2ShortFix(float paramFloat) {
        return (short) (int) (paramFloat * 256.0F);
    }

    /**
     * Convert a 8.8 Fixed format to a float
     */
    public static float ShortFix2X(short paramShort) {
        return (float) (paramShort / 256.0D);
    }

    /**
     * converts an unsigned byte to int.
     */
    public static int UByte2Int(byte paramByte) {
        return paramByte & 0xFF;
    }

    /**
     * converts an unsigned short to int.
     */
    public static int UShort2Int(short paramShort) {
        return paramShort & 0xFFFF;
    }

    /**
     * converts an unsigned int to long.
     */
    public static long UInt2Long(int paramInt) {
        return paramInt & 0xFFFFFFFFL;
    }

    /**
     * Convert a java.lang.String to a CString byte array.
     * @param str the String to convert
     */
    public static byte[] String2CString(String paramString) {
        byte[] arrayOfByte1 = new byte[paramString.length() + 1];
        byte[] arrayOfByte2 = paramString.getBytes();
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, paramString.length());
        arrayOfByte1[paramString.length()] = 0;
        return arrayOfByte1;
    }

    /**
     * Convert a java.lang.String to a PString byte array.
     * @param str the String to convert
     * @param len the maximum length of the Pstring, max length is 255.
     */
    public static byte[] String2PString(String paramString, int paramInt) {
        byte[] arrayOfByte1 = paramString.getBytes();
        int i;
        if (QTSession.isCurrentOS(1)) {
            i = arrayOfByte1.length > paramInt ? paramInt : paramInt > 255 ? 255 : arrayOfByte1.length;
        } else
            i = paramString.length() > paramInt ? paramInt : paramInt > 255 ? 255 : paramString.length();
        byte[] arrayOfByte2 = new byte[i + 1];
        arrayOfByte2[0] = ((byte) i);
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 1, i);
        return arrayOfByte2;
    }

    /**
     * Converts a CString into a java.lang.String. The offset specifies where the C string
     * begins and it terminates at the first 0 value that it finds.
     */
    public static String CString2String(byte[] paramArrayOfByte, int paramInt) {
        int i;
        for (i = paramInt; i < paramArrayOfByte.length; i++) if (paramArrayOfByte[i] == 0)
            break;
        byte[] arrayOfByte = new byte[i - paramInt];
        System.arraycopy(paramArrayOfByte, paramInt, arrayOfByte, 0, arrayOfByte.length);
        return new String(arrayOfByte);
    }

    /**
     * Converts a PString into a java.lang.String. The offset specifies where the count byte
     * is within the byte array that is passed in - typically 0.
     */
    public static String PString2String(byte[] paramArrayOfByte, int paramInt) {
        byte[] arrayOfByte = new byte[paramArrayOfByte[paramInt]];
        System.arraycopy(paramArrayOfByte, paramInt + 1, arrayOfByte, 0, paramArrayOfByte[paramInt]);
        return new String(arrayOfByte);
    }

    /**
     * Converts an OSType (4 character) string into an int.
     */
    public static int toOSType(String paramString) {
        StringBuilder paramStringBuilder = new StringBuilder(paramString);
        while (paramStringBuilder.length() < 4) paramStringBuilder.append(" ");
        paramString = paramStringBuilder.toString();
        return toOSType(paramString.charAt(0), paramString.charAt(1), paramString.charAt(2), paramString.charAt(3));
    }

    /**
     * Converts four characters into an OSType int
     */
    public static int toOSType(char paramChar1, char paramChar2, char paramChar3, char paramChar4) {
        return paramChar1 << '\030' | (paramChar2 & 0xFF) << '\020' | (paramChar3 & 0xFF) << '\b' | paramChar4 & 0xFF;
    }

    /**
     * Converts an int into a 4 character string representing an OSType.
     * The OSType int is presumed to be the right endian layout.
     */
    public static String fromOSType(int paramInt) {
        char[] arrayOfChar = { (char) ((paramInt & 0xFF000000) >>> 24), (char) ((paramInt & 0xFF0000) >>> 16), (char) ((paramInt & 0xFF00) >>> 8), (char) (paramInt & 0xFF) };
        return new String(arrayOfChar);
    }

    /**
     * Reclaim as much memory as possible and ensure that all finalize methods are
     * called on objects which have been collected.
     */
    public static void reclaimMemory() {
        Runtime localRuntime = Runtime.getRuntime();
        localRuntime.gc();
        localRuntime.gc();
        localRuntime.gc();
        localRuntime.runFinalization();
    }

    /**
     * This call will ensure that a reserved amount of application heap memory
     * on the MacOS is free. If this amount of memory is not free a memFullErr
     * will be thrown.
     */
    public static void checkFreeMemory() throws UtilException {
        if (!isWin) {
            int i = NewHandle(131072);
            if (i == 0) {
                reclaimMemory();
                i = NewHandle(131072);
                if (i == 0)
                    throw new UtilException(-108);
            }
            DisposeHandle(i);
        }
    }

    /**
     * This will flip the endian order of a 16bit value
     * @param s the value to flip
     * @return the flipped value
     * @see quicktime.util.EndianOrder
     */
    public static short endianFlip16(short paramShort) {
        return (short) (paramShort << 8 & 0xFF00 | paramShort >>> 8 & 0xFF);
    }

    /**
     * This will flip the endian order of a 32bit value
     * @param i the value to flip
     * @return the flipped value
     * @see quicktime.util.EndianOrder
     */
    public static int endianFlip32(int paramInt) {
        return paramInt << 24 & 0xFF000000 | paramInt << 8 & 0xFF0000 | paramInt >>> 8 & 0xFF00 | paramInt >>> 24 & 0xFF;
    }

    /**
     * This will flip the endian order of a 64bit value
     * @param l the value to flip
     * @return the flipped value
     * @see quicktime.util.EndianOrder
     */
    public static long endianFlip64(long paramLong) {
        return paramLong << 56 & 0xFF00000000000000L | paramLong << 40 & 0xFF000000000000L | paramLong << 24 & 0xFF0000000000L | paramLong << 8 & 0xFF00000000L | paramLong >>> 8 & 0xFF000000 | paramLong >>> 24 & 0xFF0000 | paramLong >>> 40 & 0xFF00 | paramLong >>> 56 & 0xFF;
    }

    /**
     * This will flip the supplied src object's fields according to the characteristics of the supplied
     * EndianDescriptor. The EndianDescriptor will be applied to the src object at the specified (srcOffset)
     * number of bytes from the beginning of the src object. The flipping is done in place, so the srcObject
     * will be flipped upon the completion of this method.  Any fields that are not described or accounted for
     * by the EndianDescriptor will not be flipped.
     * @param src the object to flip
     * @param srcOffset the number of bytes from the start of the src object that the supplied endian descriptor
     *  will be applied.
     * @param ed the EndianDescriptor that will describe how the object should be flipped. This may NOT be null.
     * @throws quicktime.util.UtilException
     * @see quicktime.util.EndianOrder
     */
    public static void endianFlip(Object paramObject, int paramInt, EndianDescriptor paramEndianDescriptor) throws UtilException {
        endianFlip(paramObject, paramInt, paramObject, paramInt, paramEndianDescriptor);
    }

    /**
     * This will flip the supplied src object's fields according to the characteristics of the supplied
     * EndianDescriptor and copy these flipped values to the destination object.
     * The EndianDescriptor will be applied to the src object at the specified (srcOffset)
     * number of bytes from the beginning of the src object and the flipped values will be copied to the specified
     * (destOffset) number of bytes into the destination object. Any fields that are not described or accounted for
     * by the EndianDescriptor will not be flipped or copied.
     * @param src the object to flip
     * @param srcOffset the number of bytes from the start of the src object that the supplied endian descriptor
     *  will be applied.
     * @param dest the object that will be the flipped version of the source object.
     * @param destOffset the number of bytes from the start of the dest object that the flipped values will be copied too.
     * @param ed the EndianDescriptor that will describe how the object should be flipped. This may NOT be null.
     * @throws quicktime.util.UtilException
     * @see quicktime.util.EndianOrder
     */
    public static void endianFlip(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2, EndianDescriptor paramEndianDescriptor) throws UtilException {
        if (paramEndianDescriptor == null) {
            throw new UtilException("EndianDescriptor is not specified");
        }
        int i = 0;
        int j = 0;
        int k = 0;
        if ((paramObject1 instanceof QTPointerRef)) {
            if (!(paramObject2 instanceof QTPointerRef))
                throw new UtilException("src and dest must be same Type");
            i = 1;
            j = ((QTPointerRef) paramObject1).getSize();
            k = ((QTPointerRef) paramObject2).getSize();
        } else if ((paramObject1 instanceof QTHandleRef)) {
            if (!(paramObject2 instanceof QTHandleRef))
                throw new UtilException("src and dest must be same Type");
            i = 1;
            j = ((QTHandleRef) paramObject1).getSize();
            k = ((QTHandleRef) paramObject2).getSize();
            paramObject1 = ((QTHandleRef) paramObject1).toQTPointer();
            paramObject2 = ((QTHandleRef) paramObject2).toQTPointer();
        } else if ((paramObject1 instanceof QTByteObject)) {
            if (!(paramObject2 instanceof QTByteObject))
                throw new UtilException("src and dest must be same Type");
            i = 16;
            j = ((QTByteObject) paramObject1).getSize();
            k = ((QTByteObject) paramObject2).getSize();
            paramObject1 = ((QTByteObject) paramObject1).getBytes();
            paramObject2 = ((QTByteObject) paramObject2).getBytes();
        } else if ((paramObject1 instanceof byte[])) {
            if (!(paramObject2 instanceof byte[]))
                throw new UtilException("src and dest must be same Type");
            i = 16;
            j = ((byte[]) paramObject1).length;
            k = ((byte[]) paramObject2).length;
        } else if ((paramObject1 instanceof int[])) {
            if (!(paramObject2 instanceof int[]))
                throw new UtilException("src and dest must be same Type");
            i = 256;
            j = ((int[]) paramObject1).length;
            k = ((int[]) paramObject2).length;
        } else if ((paramObject1 instanceof IntEncodedImage)) {
            if (!(paramObject2 instanceof IntEncodedImage))
                throw new UtilException("src and dest must be same Type");
            i = 256;
            j = ((IntEncodedImage) paramObject1).getInts().length;
            k = ((IntEncodedImage) paramObject2).getInts().length;
            paramObject1 = ((IntEncodedImage) paramObject1).getInts();
            paramObject2 = ((IntEncodedImage) paramObject2).getInts();
        } else if ((paramObject1 instanceof float[])) {
            if (!(paramObject2 instanceof float[]))
                throw new UtilException("src and dest must be same Type");
            i = 4096;
            j = ((float[]) paramObject1).length;
            k = ((float[]) paramObject2).length;
        }
        if (i == 0)
            throw new UtilException("unknown format");
        int i1;
        int m;
        switch(paramEndianDescriptor.getFormatFlag()) {
            case 0:
                Enumeration<EndianFlipSpec> e = paramEndianDescriptor.flipSpecs();
                if (e.hasMoreElements()) {
                    EndianFlipSpec localEndianFlipSpec = e.nextElement();
                    int i2;
                    int i3;
                    switch(localEndianFlipSpec.sizeFlag) {
                        case 2:
                            for (i1 = 0; i1 < localEndianFlipSpec.num; i1++) {
                                i2 = paramInt1 + i1 * 2 + localEndianFlipSpec.offset;
                                i3 = paramInt2 + i1 * 2 + localEndianFlipSpec.offset;
                                if ((i2 + 2 > j) || (i3 + 2 > k)) {
                                    throw new UtilException("Attempt to read or write beyond size");
                                }
                                flip16(paramObject1, i2, paramObject2, i3, i);
                            }
                        case 4:
                            for (i1 = 0; i1 < localEndianFlipSpec.num; i1++) {
                                i2 = paramInt1 + i1 * 4 + localEndianFlipSpec.offset;
                                i3 = paramInt2 + i1 * 4 + localEndianFlipSpec.offset;
                                if ((i2 + 4 > j) || (i3 + 4 > k)) {
                                    throw new UtilException("Attempt to read or write beyond size");
                                }
                                flip32(paramObject1, i2, paramObject2, i3, i);
                            }
                        case 8:
                            for (i1 = 0; i1 < localEndianFlipSpec.num; i1++) {
                                i2 = paramInt1 + i1 * 8 + localEndianFlipSpec.offset;
                                i3 = paramInt2 + i1 * 8 + localEndianFlipSpec.offset;
                                if ((i2 + 8 > j) || (i3 + 8 > k)) {
                                    throw new UtilException("Attempt to read or write beyond size");
                                }
                                flip64(paramObject1, i2, paramObject2, i3, i);
                            }
                    }
                    throw new UtilException("Unknown Endian Flip Spec value");
                }
                break;
            case 1:
                for (m = 0; m < (j - paramInt1) / 2; ) {
                    int n = paramInt1 + m * 2;
                    i1 = paramInt2 + m * 2;
                    if ((n + 2 > j) || (i1 + 2 > k)) {
                        throw new UtilException("Attempt to read or write beyond size");
                    }
                    flip16(paramObject1, n, paramObject2, i1, i);
                    m++;
                }
                break;
            case 16:
                for (m = 0; m < (j - paramInt1) / 4; ) {
                    int n = paramInt1 + m * 4;
                    i1 = paramInt2 + m * 4;
                    if ((n + 4 > j) || (i1 + 4 > k)) {
                        throw new UtilException("Attempt to read or write beyond size");
                    }
                    flip32(paramObject1, n, paramObject2, i1, i);
                    m++;
                }
                break;
            case 256:
                for (m = 0; m < (j - paramInt1) / 8; ) {
                    int n = paramInt1 + m * 8;
                    i1 = paramInt2 + m * 8;
                    if ((n + 8 > j) || (i1 + 8 > k)) {
                        throw new UtilException("Attempt to read or write beyond size");
                    }
                    flip64(paramObject1, n, paramObject2, i1, i);
                    m++;
                }
                break;
            case 4096:
                break;
            default:
                throw new UtilException("Unknown Format flag");
        }
    }

    private static void flip16(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2, int paramInt3) throws UtilException {
        short s;
        switch(paramInt3) {
            case 1:
                s = getShortFromPointer(QTObject.ID((QTPointerRef) paramObject1), paramInt1);
                s = endianFlip16(s);
                setShortInPointer(QTObject.ID((QTPointerRef) paramObject2), paramInt2, s);
                break;
            case 16:
                s = getShortFromArray((byte[]) paramObject1, paramInt1);
                s = endianFlip16(s);
                setShortInArray((byte[]) paramObject2, paramInt2, s);
                break;
            case 256:
                s = getShortFromArray((int[]) paramObject1, paramInt1);
                s = endianFlip16(s);
                setShortInArray((int[]) paramObject2, paramInt2, s);
                break;
            case 4096:
                s = getShortFromArray((float[]) paramObject1, paramInt1);
                s = endianFlip16(s);
                setShortInArray((float[]) paramObject2, paramInt2, s);
                break;
            default:
                throw new UtilException("Unknown Format");
        }
    }

    private static void flip32(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2, int paramInt3) throws UtilException {
        int i;
        switch(paramInt3) {
            case 1:
                i = getIntFromPointer(QTObject.ID((QTPointerRef) paramObject1), paramInt1);
                i = endianFlip32(i);
                setIntInPointer(QTObject.ID((QTPointerRef) paramObject2), paramInt2, i);
                break;
            case 16:
                i = getIntFromArray((byte[]) paramObject1, paramInt1);
                i = endianFlip32(i);
                setIntInArray((byte[]) paramObject2, paramInt2, i);
                break;
            case 256:
                i = getIntFromArray((int[]) paramObject1, paramInt1);
                i = endianFlip32(i);
                setIntInArray((int[]) paramObject2, paramInt2, i);
                break;
            case 4096:
                i = getIntFromArray((float[]) paramObject1, paramInt1);
                i = endianFlip32(i);
                setIntInArray((float[]) paramObject2, paramInt2, i);
                break;
            default:
                throw new UtilException("Unknown Format");
        }
    }

    private static void flip64(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2, int paramInt3) throws UtilException {
        long l;
        switch(paramInt3) {
            case 1:
                l = getLongFromPointer(QTObject.ID((QTPointerRef) paramObject1), paramInt1);
                l = endianFlip64(l);
                setLongInPointer(QTObject.ID((QTPointerRef) paramObject2), paramInt2, l);
                break;
            case 16:
                l = getLongFromArray((byte[]) paramObject1, paramInt1);
                l = endianFlip64(l);
                setLongInArray((byte[]) paramObject2, paramInt2, l);
                break;
            case 256:
                l = getLongFromArray((int[]) paramObject1, paramInt1);
                l = endianFlip64(l);
                setLongInArray((int[]) paramObject2, paramInt2, l);
                break;
            case 4096:
                l = getLongFromArray((float[]) paramObject1, paramInt1);
                l = endianFlip64(l);
                setLongInArray((float[]) paramObject2, paramInt2, l);
                break;
            default:
                throw new UtilException("Unknown Format");
        }
    }

    static void doBoundsChecks(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        if ((paramInt1 + paramInt3 * paramInt4 > paramInt2) || (paramInt5 + paramInt3 > paramInt6) || (paramInt1 < 0) || (paramInt5 < 0)) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private static void BlockMove(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.BlockMove(paramInt1, paramInt2, paramInt3);
    }

    private static int NewHandle(int paramInt) {
        return MemoryManager.NewHandle(paramInt);
    }

    private static void DisposeHandle(int paramInt) {
        MemoryManager.DisposeHandle(paramInt);
    }

    private static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfByte, paramInt);
    }

    private static short getShortFromArray(int[] paramArrayOfInt, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfInt, paramInt);
    }

    private static short getShortFromArray(float[] paramArrayOfFloat, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfFloat, paramInt);
    }

    private static short getShortFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getShortFromPointer(paramInt1, paramInt2);
    }

    private static void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort) {
        MemoryManager.setShortInArray(paramArrayOfByte, paramInt, paramShort);
    }

    private static void setShortInArray(int[] paramArrayOfInt, int paramInt, short paramShort) {
        MemoryManager.setShortInArray(paramArrayOfInt, paramInt, paramShort);
    }

    private static void setShortInArray(float[] paramArrayOfFloat, int paramInt, short paramShort) {
        MemoryManager.setShortInArray(paramArrayOfFloat, paramInt, paramShort);
    }

    private static void setShortInPointer(int paramInt1, int paramInt2, short paramShort) {
        MemoryManager.setShortInPointer(paramInt1, paramInt2, paramShort);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static int getIntFromArray(int[] paramArrayOfInt, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfInt, paramInt);
    }

    private static int getIntFromArray(float[] paramArrayOfFloat, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfFloat, paramInt);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }

    private static void setIntInArray(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfInt, paramInt1, paramInt2);
    }

    private static void setIntInArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfFloat, paramInt1, paramInt2);
    }

    private static void setIntInPointer(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.setIntInPointer(paramInt1, paramInt2, paramInt3);
    }

    private static long getLongFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getLongFromArray(paramArrayOfByte, paramInt);
    }

    private static long getLongFromArray(int[] paramArrayOfInt, int paramInt) {
        return MemoryManager.getLongFromArray(paramArrayOfInt, paramInt);
    }

    private static long getLongFromArray(float[] paramArrayOfFloat, int paramInt) {
        return MemoryManager.getLongFromArray(paramArrayOfFloat, paramInt);
    }

    private static long getLongFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getLongFromPointer(paramInt1, paramInt2);
    }

    private static void setLongInArray(byte[] paramArrayOfByte, int paramInt, long paramLong) {
        MemoryManager.setLongInArray(paramArrayOfByte, paramInt, paramLong);
    }

    private static void setLongInArray(int[] paramArrayOfInt, int paramInt, long paramLong) {
        MemoryManager.setLongInArray(paramArrayOfInt, paramInt, paramLong);
    }

    private static void setLongInArray(float[] paramArrayOfFloat, int paramInt, long paramLong) {
        MemoryManager.setLongInArray(paramArrayOfFloat, paramInt, paramLong);
    }

    private static void setLongInPointer(int paramInt1, int paramInt2, long paramLong) {
        MemoryManager.setLongInPointer(paramInt1, paramInt2, paramLong);
    }
}

