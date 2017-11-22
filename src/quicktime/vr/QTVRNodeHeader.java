
package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTUtils;


public final class QTVRNodeHeader extends QTVRAtom {
    private static final int kNativeSize = 28;

    static final long serialVersionUID = 5440311233545515486L;

    private static EndianDescriptor ed;

    public QTVRNodeHeader() {
        super(kNativeSize);
    }

    public QTVRNodeHeader(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte, kNativeSize);
    }

    protected int getNativeSize() {
        return kNativeSize;
    }

    protected void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRNodeHeader localQTVRNodeHeader = new QTVRNodeHeader();
        paramObjectInputStream.read(localQTVRNodeHeader.getBytes());
        System.arraycopy(localQTVRNodeHeader.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    protected void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRNodeHeader localQTVRNodeHeader = (QTVRNodeHeader) clone();
        paramObjectOutputStream.write(localQTVRNodeHeader.getBytes());
    }

    public Object clone() {
        try {
            return new QTVRNodeHeader(this.bytes);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = new EndianDescriptor(0);

        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 2, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);

        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(4, 4, 6);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);
        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    public int getNodeType() {
        return getIntAt(4);
    }

    public void setNodeType(int paramInt) {
        setIntAt(4, paramInt);
    }

    public void setNodeType(String paramString) {
        setIntAt(4, QTUtils.toOSType(paramString));
    }

    public int getNodeID() {
        return getIntAt(8);
    }

    public void setNodeID(int paramInt) {
        setIntAt(8, paramInt);
    }

    public int getNameAtomID() {
        return getIntAt(12);
    }

    public void setNameAtomID(int paramInt) {
        setIntAt(12, paramInt);
    }

    public int getCommentAtomID() {
        return getIntAt(16);
    }

    public void setCommentAtomID(int paramInt) {
        setIntAt(16, paramInt);
    }

    public void setReserved1(int paramInt) {
        setIntAt(20, paramInt);
    }

    public int getReserved1() {
        return getIntAt(20);
    }

    public void setReserved2(int paramInt) {
        setIntAt(24, paramInt);
    }

    public int getReserved2() {
        return getIntAt(24);
    }

    public String toString() {
        return getClass().getName() + "[nodeType=" + QTUtils.fromOSType(getNodeType()) + ",nodeID=" + getNodeID()
               + ",nameAtomID=" + getNameAtomID() + ",commentAtomID" + getCommentAtomID() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRNodeHeader.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
