package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;

/**
 * Contains information about state of a knob.
 */
public final class InstKnob extends QTByteObject {

    private static final int kNativeSize = 8;

    @Serial
    private static final long serialVersionUID = 3340534534429409660L;

    /**
     * Makes an instance of this class
     */
    public InstKnob() {
        super(kNativeSize);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        paramObjectInputStream.read(getBytes());
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        paramObjectOutputStream.write(getBytes());
    }

    /**
     * The number of the knob.
     * @return <DD/>
     */
    public int getNumber() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(0));
    }

    /**
     * The number of the knob.
     * @param number -
     */
    public void setNumber(int number) {
        setIntAt(0, EndianOrder.flipNativeToBigEndian32(number));
    }

    void _setNumber(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * The value of the knob.
     * @return <DD/>
     */
    public int getValue() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(4));
    }

    /**
     * The value of the knob.
     * @param value -
     */
    public void setValue(int value) {
        setIntAt(4, EndianOrder.flipNativeToBigEndian32(value));
    }

    void _setValue(int paramInt) {
        setIntAt(4, paramInt);
    }
}
