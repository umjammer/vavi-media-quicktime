
package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;


public final class QTVRWorldHeader extends QTVRAtom {
    private static final int kNativeSize = 24;

    static final long serialVersionUID = -8701506970872651584L;

    private static EndianDescriptor ed;

    public QTVRWorldHeader() {
        super(kNativeSize);
    }

    public QTVRWorldHeader(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte, kNativeSize);
    }

    protected int getNativeSize() {
        return kNativeSize;
    }

    protected void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRWorldHeader localQTVRWorldHeader = new QTVRWorldHeader();
        paramObjectInputStream.read(localQTVRWorldHeader.getBytes());
        System.arraycopy(localQTVRWorldHeader.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    protected void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRWorldHeader localQTVRWorldHeader = (QTVRWorldHeader) clone();
        paramObjectOutputStream.write(localQTVRWorldHeader.getBytes());
    }

    public Object clone() {
        try {
            return new QTVRWorldHeader(this.bytes);
        } catch (QTException e) {
            throw new QTRuntimeException(e);
        }
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = new EndianDescriptor(0);

        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 2, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);

        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(4, 4, 5);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);

        return localEndianDescriptor;
    }

    public int getNameAtomID() {
        return getIntAt(4);
    }

    public void setNameAtomID(int paramInt) {
        setIntAt(4, paramInt);
    }

    public int getDefaultNodeID() {
        return getIntAt(8);
    }

    public void setDefaultNodeID(int paramInt) {
        setIntAt(8, paramInt);
    }

    public int getVRWorldFlags() {
        return getIntAt(12);
    }

    public void setVRWorldFlags(int paramInt) {
        setIntAt(12, paramInt);
    }

    public void setReserved1(int paramInt) {
        setIntAt(16, paramInt);
    }

    public int getReserved1() {
        return getIntAt(16);
    }

    public void setReserved2(int paramInt) {
        setIntAt(20, paramInt);
    }

    public int getReserved2() {
        return getIntAt(20);
    }

    public String toString() {
        return getClass().getName() + "[nameAtomID=" + getNameAtomID() + ",defaultNodeID=" + getDefaultNodeID()
               + ",vrWorldFlags=" + getVRWorldFlags() + "]";
    }
}
