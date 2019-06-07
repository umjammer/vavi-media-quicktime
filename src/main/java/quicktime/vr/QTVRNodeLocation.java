package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTUtils;

public final class QTVRNodeLocation extends QTVRAtom {

    private static final int kNativeSize = 24;

    private static EndianDescriptor ed;

    static final long serialVersionUID = -2055560448381571608L;

    /**
     * Creates a QTVRNodeLocation object containing no data
     */
    public QTVRNodeLocation() {
        super(kNativeSize);
    }

    public QTVRNodeLocation(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte, kNativeSize);
    }

    protected int getNativeSize() {
        return kNativeSize;
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = new EndianDescriptor(0);
        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 2, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);
        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(4, 4, 5);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);
        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    protected void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRNodeLocation localQTVRNodeLocation = new QTVRNodeLocation();
        paramObjectInputStream.read(localQTVRNodeLocation.getBytes());
        System.arraycopy(localQTVRNodeLocation.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    protected void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRNodeLocation localQTVRNodeLocation = (QTVRNodeLocation) clone();
        paramObjectOutputStream.write(localQTVRNodeLocation.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    public Object clone() {
        try {
            return new QTVRNodeLocation(this.bytes);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    /**
     * returns the nodeType
     */
    public int getNodeType() {
        return getIntAt(4);
    }

    /**
     * set the nodeType
     */
    public void setNodeType(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * set the nodeType from a string
     */
    public void setNodeType(String paramString) {
        setIntAt(4, QTUtils.toOSType(paramString));
    }

    /**
     * returns the locationFlags
     */
    public int getLocationFlags() {
        return getIntAt(8);
    }

    /**
     * set the locationFlags
     */
    public void setLocationFlags(int paramInt) {
        setIntAt(8, paramInt);
    }

    /**
     * returns the locationData
     */
    public int getLocationData() {
        return getIntAt(12);
    }

    /**
     * set the locationData
     */
    public void setLocationData(int paramInt) {
        setIntAt(12, paramInt);
    }

    /**
     * set the reserved1
     */
    public void setReserved1(int paramInt) {
        setIntAt(16, paramInt);
    }

    /**
     * returns the reserved1
     */
    public int getReserved1() {
        return getIntAt(16);
    }

    /**
     * set the reserved2
     */
    public void setReserved2(int paramInt) {
        setIntAt(20, paramInt);
    }

    /**
     * returns the reserved2
     */
    public int getReserved2() {
        return getIntAt(20);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName() + "[nodeType=" + QTUtils.fromOSType(getNodeType()) + ",locationFlags=" + getLocationFlags() + ",locationData=" + getLocationData() + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRNodeLocation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
