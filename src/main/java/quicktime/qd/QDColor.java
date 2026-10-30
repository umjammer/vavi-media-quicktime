package quicktime.qd;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
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

    private static final int kNativeSize = 6;

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
            EndianFlipSpec spec = new EndianFlipSpec(0, 2, 4);
            ed.addFlipSpec(spec);
            EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(8, 4, 1);
            ed.addFlipSpec(endianFlipSpec2);
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
    public static int convert8to16(int value) {
        return (0xFF & value) << 8 | 0xFF & value;
    }

    /**
     * Takes a 16bit color value and converts it into an 8bit value.
     */
    public static int convert16to8(int value) {
        return (0xFF00 & value) >> 8;
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
        QDColor color;
        float f1;
        float f2;
        float f3;
        switch(flag) {
            case 6:
                color = new QDColor();
                System.arraycopy(colorBytes, 0, color.bytes, 0, 6);
                return color;
            case 8:
                color = new QDColor();
                System.arraycopy(colorBytes, 2, color.bytes, 0, 6);
                color.setAlpha(QTUtils.UShort2Int(getShortFromArray(colorBytes, 0)));
                return color;
            case 14:
                color = new QDColor();
                System.arraycopy(colorBytes, 2, color.bytes, 0, 6);
                color.setAlpha(QTUtils.UShort2Int(getShortFromArray(colorBytes, 0)));
                color.endPercent = getIntFromArray(colorBytes, 8);
                return color;
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
    public static QDColor fromARGBColor(int argb) {
        return new QDColor(convert8to16(argb & 0xFF0000), convert8to16(argb & 0xFF00), convert8to16(argb & 0xFF), convert8to16(argb & 0xFF000000));
    }

    /**
     * Create an instance of an QDColor class - Black
     */
    public QDColor() {
        super(kNativeSize);
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

    private QDColor(byte[] bytes, int alpha) {
        super(bytes);
        this.alpha = alpha;
    }

    private static float checkRange(float value) {
        if (value < 0.0F)
            return 0.0F;
        return Math.min(value, 1.0F);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        byte[] arrayOfByte = new byte[10];
        in.read(arrayOfByte);
        for (int i = 0; i < 6; i += 2) {
            setShortInArray(getBytes(), i, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, i)));
        }
        this.alpha = EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 6));
        if (this.alpha == -1) {
            arrayOfByte = new byte[8];
            in.read(arrayOfByte);
            this.alpha = EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 0));
            this.endPercent = EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 4));
        } else {
            this.endPercent = 0;
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[18];
        for (int i = 0; i < kNativeSize; i += 2) {
            setShortInArray(movie, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }
        setIntInArray(movie, 6, -1);
        setIntInArray(movie, 10, EndianOrder.flipNativeToBigEndian32(this.alpha));
        setIntInArray(movie, 14, EndianOrder.flipNativeToBigEndian32(this.endPercent));
        out.write(movie);
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
        byte[] argbColor = new byte[8];
        setShortInArray(argbColor, 0, (short) this.alpha);
        System.arraycopy(this.bytes, 0, argbColor, 2, 6);
        return argbColor;
    }

    /**
     * Returns the color as an GradientColor value
     */
    public byte[] getGradientColor() {
        byte[] gradientColor = new byte[12];
        setShortInArray(gradientColor, 0, (short) this.alpha);
        System.arraycopy(this.bytes, 0, gradientColor, 2, 6);
        setIntInArray(gradientColor, 8, this.endPercent);
        return gradientColor;
    }

    /**
     * Returns the color as a TQ3ColorRGB value
     */
    public byte[] getTQ3ColorRGB() {
        byte[] tQ3ColorRGB = new byte[12];
        setFloatInArray(tQ3ColorRGB, 0, getRed() / 65535.0F);
        setFloatInArray(tQ3ColorRGB, 4, getGreen() / 65535.0F);
        setFloatInArray(tQ3ColorRGB, 8, getBlue() / 65535.0F);
        return tQ3ColorRGB;
    }

    /**
     * Returns the color as a TQ3ColorARGB value
     */
    public byte[] getTQ3ColorARGB() {
        byte[] tQ3ColorARGB = new byte[16];
        setFloatInArray(tQ3ColorARGB, 0, getAlpha() / 65535.0F);
        setFloatInArray(tQ3ColorARGB, 4, getRed() / 65535.0F);
        setFloatInArray(tQ3ColorARGB, 8, getGreen() / 65535.0F);
        setFloatInArray(tQ3ColorARGB, 12, getBlue() / 65535.0F);
        return tQ3ColorARGB;
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

    private void setRed(int red) {
        setShortAt(0, (short) red);
    }

    private void setGreen(int green) {
        setShortAt(2, (short) green);
    }

    private void setBlue(int blue) {
        setShortAt(4, (short) blue);
    }

    private void setAlpha(int alpha) {
        this.alpha = (0xFFFF & alpha);
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static float getFloatFromArray(byte[] array, int offset) {
        return MemoryManager.getFloatFromArray(array, offset);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }

    private static void setFloatInArray(byte[] array, int offset, float value) {
        MemoryManager.setFloatInArray(array, offset, value);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }
}
