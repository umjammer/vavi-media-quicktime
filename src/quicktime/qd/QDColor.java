
package quicktime.qd;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public final class QDColor extends QTByteObject implements QuickTimeLib, Cloneable {
    private static Object linkage;

    private static final int kNativeSize = 6;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(QDColor.class);
                    }
                });
            }
        }.establish();
    }

    public static final QDColor black = new QDColor(0, 0, 0);
    public static final QDColor white = new QDColor(65535, 65535, 65535);
    public static final QDColor red = new QDColor(65535, 0, 0);
    public static final QDColor green = new QDColor(0, 65535, 0);
    public static final QDColor blue = new QDColor(0, 0, 65535);
    public static final QDColor yellow = new QDColor(65535, 65535, 0);
    public static final QDColor magenta = new QDColor(65535, 0, 65535);
    public static final QDColor cyan = new QDColor(0, 65535, 65535);
    public static final QDColor darkGray = new QDColor(16383, 16383, 16383);
    public static final QDColor gray = new QDColor(32767, 32767, 32767);
    public static final QDColor lightGray = new QDColor(49151, 49151, 49151);
    public static final QDColor pink = new QDColor(65535, 44976, 44976);
    public static final QDColor orange = new QDColor(65535, 51400, 0);
    public static final QDColor brown = new QDColor(32767, 16383, 0);

    public static final int kMaximumValue = 65535;
    public static final int kIsRGBColor = 6;
    public static final int kIsARGBColor = 8;
    public static final int kIsTQ3ColorRGB = 12;
    public static final int kIsGradientColor = 14;
    public static final int kIsTQ3ColorARGB = 16;

    private static EndianDescriptor ed;

    private transient int alpha;

    private transient int endPercent;

    static final long serialVersionUID = -6088195721752787215L;

    public static final EndianDescriptor getEndianDescriptorRGBColor() {
        return EndianDescriptor.flipAll16;
    }

    public static final EndianDescriptor getEndianDescriptorARGBColor() {
        return EndianDescriptor.flipAll16;
    }

    public static final EndianDescriptor getEndianDescriptorGradientColor() {
        if (ed == null) {
            ed = new EndianDescriptor(0);

            EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 2, 4);
            ed.addFlipSpec(localEndianFlipSpec1);

            EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(8, 4, 1);
            ed.addFlipSpec(localEndianFlipSpec2);
        }
        return ed;
    }

    public static final EndianDescriptor getEndianDescriptorTQ3Color() {
        return EndianDescriptor.flipAll32;
    }

    public static final EndianDescriptor getEndianDescriptorTQ3ColorARGB() {
        return EndianDescriptor.flipAll32;
    }

    public static int convert8to16(int paramInt) {
        return (0xFF & paramInt) << 8 | 0xFF & paramInt;
    }

    public static int convert16to8(int paramInt) {
        return (0xFF00 & paramInt) >> 8;
    }

    public static QDColor fromArray(byte[] paramArrayOfByte, int paramInt) {
        if (paramInt > paramArrayOfByte.length)
            throw new IllegalArgumentException("Mismatch between flag and supplied colorBytes");
        QDColor localQDColor;
        float f1;
        float f2;
        float f3;
        switch (paramInt) {
        case 6:
            localQDColor = new QDColor();
            System.arraycopy(paramArrayOfByte, 0, localQDColor.bytes, 0, 6);
            return localQDColor;

        case 8:
            localQDColor = new QDColor();
            System.arraycopy(paramArrayOfByte, 2, localQDColor.bytes, 0, 6);
            localQDColor.setAlpha(QTUtils.UShort2Int(getShortFromArray(paramArrayOfByte, 0)));
            return localQDColor;

        case 14:
            localQDColor = new QDColor();
            System.arraycopy(paramArrayOfByte, 2, localQDColor.bytes, 0, 6);
            localQDColor.setAlpha(QTUtils.UShort2Int(getShortFromArray(paramArrayOfByte, 0)));
            localQDColor.endPercent = getIntFromArray(paramArrayOfByte, 8);
            return localQDColor;

        case 12:
            f1 = getFloatFromArray(paramArrayOfByte, 0);
            f2 = getFloatFromArray(paramArrayOfByte, 4);
            f3 = getFloatFromArray(paramArrayOfByte, 8);
            return new QDColor(f1, f2, f3);

        case 16:
            f1 = getFloatFromArray(paramArrayOfByte, 0);
            f2 = getFloatFromArray(paramArrayOfByte, 4);
            f3 = getFloatFromArray(paramArrayOfByte, 8);
            float f4 = getFloatFromArray(paramArrayOfByte, 12);
            return new QDColor(f2, f3, f4, f1);
        }

        throw new IllegalArgumentException("Unknown color format");
    }

    public static QDColor fromARGBColor(int paramInt) {
        return new QDColor(convert8to16(paramInt & 0xFF0000),
                           convert8to16(paramInt & 0xFF00),
                           convert8to16(paramInt & 0xFF),
                           convert8to16(paramInt & 0xFF000000));
    }

    public QDColor() {
        super(6);
    }

    public QDColor(int paramInt1, int paramInt2, int paramInt3) {
        this(paramInt1, paramInt2, paramInt3, 65535);
    }

    public QDColor(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this();
        setRed(paramInt1);
        setGreen(paramInt2);
        setBlue(paramInt3);
        setAlpha(paramInt4);
    }

    public QDColor(float paramFloat1, float paramFloat2, float paramFloat3) {
        this(paramFloat1, paramFloat2, paramFloat3, 1.0F);
    }

    public QDColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        this();
        paramFloat1 = checkRange(paramFloat1);
        paramFloat2 = checkRange(paramFloat2);
        paramFloat3 = checkRange(paramFloat3);
        paramFloat4 = checkRange(paramFloat4);

        setRed((int) (paramFloat1 * 65535.0F + 0.5F));
        setGreen((int) (paramFloat2 * 65535.0F + 0.5F));
        setBlue((int) (paramFloat3 * 65535.0F + 0.5F));
        setAlpha((int) (paramFloat4 * 65535.0F + 0.5F));
    }

    public QDColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
        this(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
        setAlpha((int) (paramFloat4 * 65535.0F + 0.5F));
        this.endPercent = QTUtils.X2Fix(paramFloat5);
    }

    /**
     * @deprecated
     */
    public QDColor(Color paramColor) {
        this(convert8to16(paramColor.getRed()), convert8to16(paramColor.getGreen()), convert8to16(paramColor.getBlue()));
    }

    private QDColor(byte[] paramArrayOfByte, int paramInt) {
        super(paramArrayOfByte);
        this.alpha = paramInt;
    }

    private static float checkRange(float paramFloat) {
        if (paramFloat < 0.0F)
            return 0.0F;
        if (paramFloat > 1.0F)
            return 1.0F;
        return paramFloat;
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[6];

        byte[] arrayOfByte = new byte[10];
        paramObjectInputStream.read(arrayOfByte);

        for (int i = 0; i < 6; i += 2) {
            setShortInArray(getBytes(), i, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, i)));
        }
        this.alpha = EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 6));
        if (this.alpha == -1) {
            arrayOfByte = new byte[8];
            paramObjectInputStream.read(arrayOfByte);
            this.alpha = EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 0));
            this.endPercent = EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 4));
        } else {
            this.endPercent = 0;
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[18];

        for (int i = 0; i < 6; i += 2) {
            setShortInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }

        setIntInArray(arrayOfByte, 6, -1);

        setIntInArray(arrayOfByte, 10, EndianOrder.flipNativeToBigEndian32(this.alpha));
        setIntInArray(arrayOfByte, 14, EndianOrder.flipNativeToBigEndian32(this.endPercent));

        paramObjectOutputStream.write(arrayOfByte);
    }

    public byte[] getRGBColor() {
        return this.bytes;
    }

    public byte[] getARGBColor() {
        byte[] arrayOfByte = new byte[8];
        setShortInArray(arrayOfByte, 0, (short) this.alpha);
        System.arraycopy(this.bytes, 0, arrayOfByte, 2, 6);
        return arrayOfByte;
    }

    public byte[] getGradientColor() {
        byte[] arrayOfByte = new byte[12];
        setShortInArray(arrayOfByte, 0, (short) this.alpha);
        System.arraycopy(this.bytes, 0, arrayOfByte, 2, 6);
        setIntInArray(arrayOfByte, 8, this.endPercent);
        return arrayOfByte;
    }

    public byte[] getTQ3ColorRGB() {
        byte[] arrayOfByte = new byte[12];
        setFloatInArray(arrayOfByte, 0, getRed() / 65535.0F);
        setFloatInArray(arrayOfByte, 4, getGreen() / 65535.0F);
        setFloatInArray(arrayOfByte, 8, getBlue() / 65535.0F);
        return arrayOfByte;
    }

    public byte[] getTQ3ColorARGB() {
        byte[] arrayOfByte = new byte[16];
        setFloatInArray(arrayOfByte, 0, getAlpha() / 65535.0F);
        setFloatInArray(arrayOfByte, 4, getRed() / 65535.0F);
        setFloatInArray(arrayOfByte, 8, getGreen() / 65535.0F);
        setFloatInArray(arrayOfByte, 12, getBlue() / 65535.0F);
        return arrayOfByte;
    }

    public int getRed() {
        return QTUtils.UShort2Int(getShortAt(0));
    }

    public float getRedF() {
        return QTUtils.UShort2Int(getShortAt(0)) / 65535.0F;
    }

    public int getGreen() {
        return QTUtils.UShort2Int(getShortAt(2));
    }

    public float getGreenF() {
        return QTUtils.UShort2Int(getShortAt(2)) / 65535.0F;
    }

    public int getBlue() {
        return QTUtils.UShort2Int(getShortAt(4));
    }

    public float getBlueF() {
        return QTUtils.UShort2Int(getShortAt(4)) / 65535.0F;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public float getAlphaF() {
        return this.alpha / 65535.0F;
    }

    public float getEndingPercentage() {
        return QTUtils.Fix2X(this.endPercent);
    }

    public Object clone() {
        return new QDColor(getRGBColor(), getAlpha());
    }

    public int getARGB() {
        return convert16to8(getAlpha()) << 24 | convert16to8(getRed()) << 16 | convert16to8(getGreen()) << 8
               | convert16to8(getBlue());
    }

    public int getRGB() {
        return 0x0 | convert16to8(getRed()) << 16 | convert16to8(getGreen()) << 8 | convert16to8(getBlue());
    }

    public String toString() {
        return super.toString() + "[red=0x" + Integer.toHexString(getRed()) + ",green=0x" + Integer.toHexString(getGreen())
               + ",blue=0x" + Integer.toHexString(getBlue()) + ",alpha=0x" + Integer.toHexString(getAlpha()) + "]";
    }

    private void setRed(int paramInt) {
        setShortAt(0, (short) paramInt);
    }

    private void setGreen(int paramInt) {
        setShortAt(2, (short) paramInt);
    }

    private void setBlue(int paramInt) {
        setShortAt(4, (short) paramInt);
    }

    private void setAlpha(int paramInt) {
        this.alpha = (0xFFFF & paramInt);
    }

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native float getFloatFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native void setFloatInArray(byte[] paramArrayOfByte, int paramInt, float paramFloat);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/
 * QDColor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
