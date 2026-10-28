package quicktime.qd;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

/**
 * Represents color as 16bit values for each component and alpha channel.
 * Once created this class is a read only color. The static color variables
 * are the same as the java.awt.Color colors, but in 16bit format. The default
 * format for the class (ie. the bytes that are returned by getBytes) is RGBColor.
 */
public final class QDColor extends QTByteObject implements QuickTimeLib, Cloneable {

    private static Object linkage;

    private static final int kNativeSize = 6;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(QDColor.class));
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

    /**
     * This is a convenience value - it represents the largest value that a colour component
     * can have.
     */
    public static final int kMaximumValue = 65535;

    /**
     * These constants are used in the fromArray call to indicate
     * which of the 4 formats the byte array is encoded in.
     */
    public static final int kIsRGBColor = 6;

    /**
     * These constants are used in the fromArray call to indicate
     * which of the 4 formats the byte array is encoded in.
     */
    public static final int kIsARGBColor = 8;

    /**
     * These constants are used in the fromArray call to indicate
     * which of the 4 formats the byte array is encoded in.
     */
    public static final int kIsTQ3ColorRGB = 12;

    /**
     * These constants are used in the fromArray call to indicate
     * which of the 4 formats the byte array is encoded in.
     */
    public static final int kIsGradientColor = 14;

    /**
     * These constants are used in the fromArray call to indicate
     * which of the 4 formats the byte array is encoded in.
     */
    public static final int kIsTQ3ColorARGB = 16;

    private static EndianDescriptor ed;

    private transient int alpha;

    private transient int endPercent;

    @Serial
    private static final long serialVersionUID = -6088195721752787215L;

    /**
     * Returns the EndianDescriptor for RGBColor color.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptorRGBColor() {
        return EndianDescriptor.flipAll16;
    }

    /**
     * Returns the EndianDescriptor for ARGBColor color.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptorARGBColor() {
        return EndianDescriptor.flipAll16;
    }

    /**
     * Returns the EndianDescriptor for ARGBColor color.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptorGradientColor() {
        if (ed == null) {
            ed = new EndianDescriptor(0);
            EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 2, 4);
            ed.addFlipSpec(localEndianFlipSpec1);
            EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(8, 4, 1);
            ed.addFlipSpec(localEndianFlipSpec2);
        }
        return ed;
    }

    /**
     * Returns the EndianDescriptor for TQ3Color color.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptorTQ3Color() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Returns the EndianDescriptor for TQ3ColorARGB color.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptorTQ3ColorARGB() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Takes an 8bit color value and converts it into a 16bit value.
     */
    public static int convert8to16(int paramInt) {
        return (0xFF & paramInt) << 8 | 0xFF & paramInt;
    }

    /**
     * Takes a 16bit color value and converts it into an 8bit value.
     */
    public static int convert16to8(int paramInt) {
        return (0xFF00 & paramInt) >> 8;
    }

    /**
     * Takes a byte array and converts it to a QDColor. The flag tells the
     * call what format the bytes are in. The length of the array must equate
     * to at least the required length of bytes.
     *
     * kIsRGBColorBytes, length == 6, r, g, b of 2 bytes each (unsigned short)(range 0->65535 for each component)
     * kIsARGBColorBytes, length == 8, a, r, g, b of 2 bytes each (unsigned short)(range 0->65535 for each component)
     * kIsTQ3ColorRGBBytes, length == 12, r, g, b of 4 bytes each (float)(range 0->1 for each component)
     * kIsTQ3ColorARGBBytes, length == 16, a, r, g, b of 4 bytes each (float)(range 0->1 for each component)
     *
     * @param colorBytes the byte array containing the color values
     * @param flag flag that describes the format of the byte array
     * @return a QDColor
     * @throws java.lang.IllegalArgumentException if mismatch between minimum length of byte array and format flag
     */
    public static QDColor fromArray(byte[] colorBytes, int flag) {
        if (flag > colorBytes.length)
            throw new IllegalArgumentException("Mismatch between flag and supplied colorBytes");
        QDColor localQDColor;
        float f1;
        float f2;
        float f3;
        switch(flag) {
            case 6:
                localQDColor = new QDColor();
                System.arraycopy(colorBytes, 0, localQDColor.bytes, 0, 6);
                return localQDColor;
            case 8:
                localQDColor = new QDColor();
                System.arraycopy(colorBytes, 2, localQDColor.bytes, 0, 6);
                localQDColor.setAlpha(QTUtils.UShort2Int(getShortFromArray(colorBytes, 0)));
                return localQDColor;
            case 14:
                localQDColor = new QDColor();
                System.arraycopy(colorBytes, 2, localQDColor.bytes, 0, 6);
                localQDColor.setAlpha(QTUtils.UShort2Int(getShortFromArray(colorBytes, 0)));
                localQDColor.endPercent = getIntFromArray(colorBytes, 8);
                return localQDColor;
            case 12:
                f1 = getFloatFromArray(colorBytes, 0);
                f2 = getFloatFromArray(colorBytes, 4);
                f3 = getFloatFromArray(colorBytes, 8);
                return new QDColor(f1, f2, f3);
            case 16:
                f1 = getFloatFromArray(colorBytes, 0);
                f2 = getFloatFromArray(colorBytes, 4);
                f3 = getFloatFromArray(colorBytes, 8);
                float f4 = getFloatFromArray(colorBytes, 12);
                return new QDColor(f2, f3, f4, f1);
        }
        throw new IllegalArgumentException("Unknown color format");
    }

    /**
     * Creates a QDColor with the specified ARGB value, where the alpha
     * component is in bits 24-32 of the argument, where the red
     * component is in bits 16-23 of the argument, the green
     * component is in bits 8-15 of the argument, and the blue
     * component is in bits 0-7. The value zero indicates no
     * contribution from the primary color component.
     */
    public static QDColor fromARGBColor(int paramInt) {
        return new QDColor(convert8to16(paramInt & 0xFF0000), convert8to16(paramInt & 0xFF00), convert8to16(paramInt & 0xFF), convert8to16(paramInt & 0xFF000000));
    }

    /**
     * Create an instance of an QDColor class - Black
     */
    public QDColor() {
        super(6);
    }

    /**
     * Create an instance of an QDColor class. Alpha is set to default (0xFFFF - opaque).
     * @param red the 16bit red value
     * @param green the 16bit green value
     * @param blue the 16bit blue value
     */
    public QDColor(int red, int green, int blue) {
        this(red, green, blue, 65535);
    }

    /**
     * Create an instance of an QDColor class.
     * @param red the 16bit red value
     * @param green the 16bit green value
     * @param blue the 16bit blue value
     * @param alpha the 16bit alpha value
     */
    public QDColor(int red, int green, int blue, int alpha) {
        this();
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setAlpha(alpha);
    }

    /**
     * Create an instance of an QDColor class. Alpha is set to default (0xFFFF - opaque).
     * @param red the 0 to 1F red value
     * @param green the 0 to 1F green value
     * @param blue the 0 to 1F blue value
     */
    public QDColor(float red, float green, float blue) {
        this(red, green, blue, 1.0F);
    }

    /**
     * Create an instance of an QDColor class.
     * @param red the 0 to 1F red value
     * @param green the 0 to 1F green value
     * @param blue the 0 to 1F blue value
     * @param alpha the 0 to 1F alpha value
     */
    public QDColor(float red, float green, float blue, float alpha) {
        this();
        red = checkRange(red);
        green = checkRange(green);
        blue = checkRange(blue);
        alpha = checkRange(alpha);
        setRed((int) (red * 65535.0F + 0.5F));
        setGreen((int) (green * 65535.0F + 0.5F));
        setBlue((int) (blue * 65535.0F + 0.5F));
        setAlpha((int) (alpha * 65535.0F + 0.5F));
    }

    /**
     * Create an instance of an QDColor class.
     * @param red the 0 to 1F red value
     * @param green the 0 to 1F green value
     * @param blue the 0 to 1F blue value
     * @param alpha the 0 to 1F alpha value
     * @param endingPercentage Specifies the percentage of the gradient (expressed as value between
     *  0 and 1 , where 0 is the beginning of the gradient) at which the specified color begins.
     */
    public QDColor(float red, float green, float blue, float alpha, float endingPercentage) {
        this(red, green, blue, alpha);
        setAlpha((int) (alpha * 65535.0F + 0.5F));
        this.endPercent = QTUtils.X2Fix(endingPercentage);
    }

    /**
     * @deprecated
     * <I>as of QTJava 4.0</I>
     * Create an instance of an QDColor class
     * @param color the java.awt.Color class to convert
     */
    public QDColor(Color color) {
        this(convert8to16(color.getRed()), convert8to16(color.getGreen()), convert8to16(color.getBlue()));
    }

    private QDColor(byte[] paramArrayOfByte, int paramInt) {
        super(paramArrayOfByte);
        this.alpha = paramInt;
    }

    private static float checkRange(float paramFloat) {
        if (paramFloat < 0.0F)
            return 0.0F;
        return Math.min(paramFloat, 1.0F);
    }

    @Serial
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

    @Serial
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

    /**
     * Returns the color as an RGBColor value
     */
    public byte[] getRGBColor() {
        return this.bytes;
    }

    /**
     * Returns the color as an ARGBColor value
     */
    public byte[] getARGBColor() {
        byte[] arrayOfByte = new byte[8];
        setShortInArray(arrayOfByte, 0, (short) this.alpha);
        System.arraycopy(this.bytes, 0, arrayOfByte, 2, 6);
        return arrayOfByte;
    }

    /**
     * Returns the color as an GradientColor value
     */
    public byte[] getGradientColor() {
        byte[] arrayOfByte = new byte[12];
        setShortInArray(arrayOfByte, 0, (short) this.alpha);
        System.arraycopy(this.bytes, 0, arrayOfByte, 2, 6);
        setIntInArray(arrayOfByte, 8, this.endPercent);
        return arrayOfByte;
    }

    /**
     * Returns the color as a TQ3ColorRGB value
     */
    public byte[] getTQ3ColorRGB() {
        byte[] arrayOfByte = new byte[12];
        setFloatInArray(arrayOfByte, 0, getRed() / 65535.0F);
        setFloatInArray(arrayOfByte, 4, getGreen() / 65535.0F);
        setFloatInArray(arrayOfByte, 8, getBlue() / 65535.0F);
        return arrayOfByte;
    }

    /**
     * Returns the color as a TQ3ColorARGB value
     */
    public byte[] getTQ3ColorARGB() {
        byte[] arrayOfByte = new byte[16];
        setFloatInArray(arrayOfByte, 0, getAlpha() / 65535.0F);
        setFloatInArray(arrayOfByte, 4, getRed() / 65535.0F);
        setFloatInArray(arrayOfByte, 8, getGreen() / 65535.0F);
        setFloatInArray(arrayOfByte, 12, getBlue() / 65535.0F);
        return arrayOfByte;
    }

    /**
     * Return value of red component
     */
    public int getRed() {
        return QTUtils.UShort2Int(getShortAt(0));
    }

    /**
     * Return value of red component as float
     */
    public float getRedF() {
        return QTUtils.UShort2Int(getShortAt(0)) / 65535.0F;
    }

    /**
     * Return value of green component
     */
    public int getGreen() {
        return QTUtils.UShort2Int(getShortAt(2));
    }

    /**
     * Return value of green component as float
     */
    public float getGreenF() {
        return QTUtils.UShort2Int(getShortAt(2)) / 65535.0F;
    }

    /**
     * Return value of blue component
     */
    public int getBlue() {
        return QTUtils.UShort2Int(getShortAt(4));
    }

    /**
     * Return value of blue component as float
     */
    public float getBlueF() {
        return QTUtils.UShort2Int(getShortAt(4)) / 65535.0F;
    }

    /**
     * Return value of alpha component
     */
    public int getAlpha() {
        return this.alpha;
    }

    /**
     * Return value of alpha component as float
     */
    public float getAlphaF() {
        return this.alpha / 65535.0F;
    }

    /**
     * Return value of ending percentage component
     */
    public float getEndingPercentage() {
        return QTUtils.Fix2X(this.endPercent);
    }

    /**
     * Returns a copy of this object
     */
    @Override
    public Object clone() {
        return new QDColor(getRGBColor(), getAlpha());
    }

    /**
     * Converts to a packed int according to the pixel depth supplied.
     * @return packed int of 8bit depth for each a, r, g, b component
     */
    public int getARGB() {
        return convert16to8(getAlpha()) << 24 | convert16to8(getRed()) << 16 | convert16to8(getGreen()) << 8 | convert16to8(getBlue());
    }

    /**
     * Converts to a packed int according to the pixel depth supplied.
     * Alpha channel is 0 (the high 8 bits).
     * @return packed int of 8bit depth for each r, g, b component
     */
    public int getRGB() {
        return 0x0 | convert16to8(getRed()) << 16 | convert16to8(getGreen()) << 8 | convert16to8(getBlue());
    }

    /**
     * Returns the String representation of this object
     */
    public String toString() {
        return super.toString() + "[red=0x" + Integer.toHexString(getRed()) + ",green=0x" + Integer.toHexString(getGreen()) + ",blue=0x" + Integer.toHexString(getBlue()) + ",alpha=0x" + Integer.toHexString(getAlpha()) + "]";
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

    private static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfByte, paramInt);
    }

    private static float getFloatFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getFloatFromArray(paramArrayOfByte, paramInt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort) {
        MemoryManager.setShortInArray(paramArrayOfByte, paramInt, paramShort);
    }

    private static void setFloatInArray(byte[] paramArrayOfByte, int paramInt, float paramFloat) {
        MemoryManager.setFloatInArray(paramArrayOfByte, paramInt, paramFloat);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}

