
package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public final class InstKnob extends QTByteObject {
    private static final int kNativeSize = 8;

    static final long serialVersionUID = 3340534534429409660L;

    public InstKnob() {
        super(kNativeSize);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        paramObjectInputStream.read(getBytes());
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        paramObjectOutputStream.write(getBytes());
    }

    public int getNumber() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(0));
    }

    public void setNumber(int paramInt) {
        setIntAt(0, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    void _setNumber(int paramInt) {
        setIntAt(0, paramInt);
    }

    public int getValue() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(4));
    }

    public void setValue(int paramInt) {
        setIntAt(4, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    void _setValue(int paramInt) {
        setIntAt(4, paramInt);
    }
}
