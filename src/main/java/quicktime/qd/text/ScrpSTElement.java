package quicktime.qd.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.qd.QDColor;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


/**
 * This class maps the ScrpSTElement record of the TextToolbox.
 * ScrpSTElement are used as part of the TextDescription
 */
public final class ScrpSTElement extends QTByteObject implements PrimitivesLib {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 20;

    private static EndianDescriptor ed;

    @Serial
    private static final long serialVersionUID = 939259602280929910L;

    private static EndianDescriptor makeED() {
        EndianDescriptor ed = new EndianDescriptor(0);
        EndianFlipSpec spec = new EndianFlipSpec(0, 4, 1);
        ed.addFlipSpec(spec);
        EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(4, 2, 3);
        ed.addFlipSpec(endianFlipSpec2);
        EndianFlipSpec endianFlipSpec3 = new EndianFlipSpec(12, 2, 1);
        ed.addFlipSpec(endianFlipSpec3);
        EndianFlipSpec endianFlipSpec4 = new EndianFlipSpec(14, 2, 3);
        ed.addFlipSpec(endianFlipSpec4);
        return ed;
    }

    /**
     * Returns the EndianDescriptor for this class.
     *
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * This constructor makes a new ScrpSTElement object.
     */
    public ScrpSTElement() {
        super(kNativeSize);
    }

    private ScrpSTElement(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        byte[] arrayOfByte = new byte[kNativeSize];
        in.read(arrayOfByte);
        setIntInArray(this.bytes, 0, EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 0)));
        setShortInArray(this.bytes, 4, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 4)));
        setShortInArray(this.bytes, 6, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 6)));
        setShortInArray(this.bytes, 8, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 8)));
        setShortInArray(this.bytes, 12, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 12)));
        setShortInArray(this.bytes, 14, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 14)));
        setShortInArray(this.bytes, 16, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 16)));
        setShortInArray(this.bytes, 18, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, 18)));
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[kNativeSize];
        setIntInArray(movie, 0, EndianOrder.flipNativeToBigEndian32(getIntFromArray(this.bytes, 0)));
        setShortInArray(movie, 4, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 4)));
        setShortInArray(movie, 6, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 6)));
        setShortInArray(movie, 8, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 8)));
        setShortInArray(movie, 12, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 12)));
        setShortInArray(movie, 14, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 14)));
        setShortInArray(movie, 16, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 16)));
        setShortInArray(movie, 18, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, 18)));
        out.write(movie);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    @Override
    public Object clone() {
        return new ScrpSTElement(getBytes());
    }

    public void setColor(QDColor color) {
        if (color == null) {
            setShortAt(14, (short) -1);
            setShortAt(16, (short) -1);
            setShortAt(18, (short) -1);
        } else {
            System.arraycopy(color.getRGBColor(), 0, this.bytes, 14, 6);
        }
    }

    public QDColor getColor() {
        byte[] colorBytes = new byte[6];
        System.arraycopy(this.bytes, 14, colorBytes, 0, 6);
        return QDColor.fromArray(colorBytes, 6);
    }

    public void setScriptSize(int scriptSize) {
        setShortAt(12, (short) scriptSize);
    }

    public int getScriptSize() {
        return getShortAt(12);
    }

    public void setFace(int face) {
        setByteAt(10, (byte) face);
        setByteAt(11, (byte) 0);
    }

    public int getFace() {
        return QTUtils.UByte2Int(getByteAt(10));
    }

    public void setFont(int font) {
        setShortAt(8, (short) font);
    }

    public int getFont() {
        return getShortAt(8);
    }

    public void setAscent(int ascent) {
        setShortAt(6, (short) ascent);
    }

    public int getAscent() {
        return getShortAt(6);
    }

    public void setHeight(int height) {
        setShortAt(4, (short) height);
    }

    public int getHeight() {
        return getShortAt(4);
    }

    public void setStartChar(int startChar) {
        setIntAt(0, startChar);
    }

    public int getStartChar() {
        return getIntAt(0);
    }

    /**
     * Return a string representation of this object
     */
    public String toString() {
        return getClass().getName() + "[" + ",startChar=" + getStartChar() + ",getHeight=" + getHeight() + ",getAscent=" + getAscent() + ",getFont=" + getFont() + ",getFace=" + getFace() + ",getSize=" + getSize() + ",getColor=" + getColor() + "]";
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }
}
