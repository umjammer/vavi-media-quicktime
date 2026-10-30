package quicktime.std.qtcomponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.qd.QDColor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public final class TCTextOptions extends QTByteObject implements PrimitivesLib {

    private static final int kNativeSize = 20;

    @Serial
    private static final long serialVersionUID = -7109590438601877043L;

    /**
     * Make a TextOptions class for the TimeCoder
     */
    public TCTextOptions() {
        super(kNativeSize);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        in.read(getBytes());
        for (int i = 0; i < kNativeSize; i += 2) {
            short s1 = getShortFromArray(getBytes(), i);
            short s2 = EndianOrder.flipBigEndianToNative16(s1);
            setShortInArray(getBytes(), i, s2);
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[kNativeSize];
        for (int i = 0; i < kNativeSize; i += 2) {
            short s1 = getShortFromArray(movie, i);
            short s2 = EndianOrder.flipNativeToBigEndian16(s1);
            setShortInArray(movie, i, s2);
        }
        out.write(movie);
    }

    /**
     * Returns the txFont field.
     *
     * @return an int
     */
    public int getTXFont() {
        return getShortAt(0);
    }

    /**
     * Sets the txFont field.
     *
     * @param txFont the new font ID
     */
    public void setTXFont(int txFont) {
        setShortAt(0, (short) txFont);
    }

    /**
     * Returns the txFace field.
     *
     * @return an int
     */
    public int getTXFace() {
        return getShortAt(2);
    }

    /**
     * Sets the txFace field.
     *
     * @param txFace the new font ID
     */
    public void setTXFace(int txFace) {
        setShortAt(2, (short) txFace);
    }

    /**
     * Returns the txSize field.
     *
     * @return an int
     */
    public int getTXSize() {
        return getShortAt(4);
    }

    /**
     * Sets the txSize field.
     *
     * @param txSize the new font ID
     */
    public void setTXSize(int txSize) {
        setShortAt(4, (short) txSize);
    }

    /**
     * Returns the foreground Color field.
     *
     * @return a QDColor
     */
    public QDColor getForeColor() {
        byte[] colorBytes = new byte[6];
        System.arraycopy(getBytes(), 8, colorBytes, 0, 6);
        return QDColor.fromArray(colorBytes, 6);
    }

    /**
     * Sets the foreground Color field.
     *
     * @param foreColor a QDColor
     */
    public void setForeColor(QDColor foreColor) {
        System.arraycopy(foreColor.getRGBColor(), 0, getBytes(), 8, 6);
    }

    /**
     * Returns the background Color field.
     *
     * @return a QDColor
     */
    public QDColor getBackColor() {
        byte[] colorBytes = new byte[6];
        System.arraycopy(getBytes(), 14, colorBytes, 0, 6);
        return QDColor.fromArray(colorBytes, 6);
    }

    /**
     * Sets the background Color field.
     */
    public void setBackColor(QDColor backColor) {
        System.arraycopy(backColor.getRGBColor(), 0, getBytes(), 14, 6);
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }
}
