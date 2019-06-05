
package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTByteObject;


public final class QTVRTrackRefEntry extends QTByteObject implements Cloneable {
    private static final int kNativeSize = 10;

    static final long serialVersionUID = 7513389675945192418L;

    private static EndianDescriptor ed;

    public QTVRTrackRefEntry() {
        super(kNativeSize);
    }

    public QTVRTrackRefEntry(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
        if (paramArrayOfByte.length != kNativeSize) {
            throw new QTException(-50);
        }
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRTrackRefEntry localQTVRTrackRefEntry = new QTVRTrackRefEntry();
        paramObjectInputStream.read(localQTVRTrackRefEntry.getBytes());
        System.arraycopy(localQTVRTrackRefEntry.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRTrackRefEntry localQTVRTrackRefEntry = (QTVRTrackRefEntry) clone();
        paramObjectOutputStream.write(localQTVRTrackRefEntry.getBytes());
    }

    protected Object clone() {
        try {
            return new QTVRTrackRefEntry(this.bytes);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = new EndianDescriptor(0);

        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 4, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);

        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(4, 2, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);

        EndianFlipSpec localEndianFlipSpec3 = new EndianFlipSpec(6, 4, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec3);

        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    public int getTrackRefType() {
        return getIntAt(0);
    }

    public void setTrackRefType(int paramInt) {
        setIntAt(0, paramInt);
    }

    public short getTrackResolution() {
        return getShortAt(4);
    }

    public void setTrackResolution(short paramShort) {
        setShortAt(4, paramShort);
    }

    public int getTrackRefIndex() {
        return getIntAt(6);
    }

    public void setTrackRefIndex(int paramInt) {
        setIntAt(6, paramInt);
    }

    public String toString() {
        return getClass().getName() + "[trackRefType=" + getTrackRefType() + ",trackResolution=" + getTrackResolution()
               + ",trackRefIndex=" + getTrackRefIndex() + "]";
    }
}
