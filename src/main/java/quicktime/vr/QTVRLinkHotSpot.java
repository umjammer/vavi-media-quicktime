package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.qd.QDPoint;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;

public final class QTVRLinkHotSpot extends QTVRAtom {

    private static final int kNativeSize = 72;

    @Serial
    private static final long serialVersionUID = 2642915607141217566L;

    private static EndianDescriptor ed;

    /**
     * Creates a QTVRLinkHotSpot object containing no data
     */
    public QTVRLinkHotSpot() {
        super(kNativeSize);
    }

    /**
     * Creates a QTVRLinkHotSpot object from a byte array
     * Used in cloning
     */
    public QTVRLinkHotSpot(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte, kNativeSize);
    }

    @Override
    protected int getNativeSize() {
        return 72;
    }

    @Override
    protected void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRLinkHotSpot localQTVRLinkHotSpot = new QTVRLinkHotSpot();
        paramObjectInputStream.read(localQTVRLinkHotSpot.getBytes());
        System.arraycopy(localQTVRLinkHotSpot.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    @Override
    protected void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRLinkHotSpot localQTVRLinkHotSpot = (QTVRLinkHotSpot) clone();
        paramObjectOutputStream.write(localQTVRLinkHotSpot.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        try {
            return new QTVRLinkHotSpot(this.bytes);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    private static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = new EndianDescriptor(0);
        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 2, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);
        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(4, 4, 17);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);
        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * returns the toNodeID
     */
    public int getToNodeID() {
        return getIntAt(4);
    }

    /**
     * set the toNodeID
     */
    public void setToNodeID(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * returns the fromValidFlags
     */
    public int getFromValidFlags() {
        return getIntAt(8);
    }

    /**
     * set the fromValidFlags
     */
    public void setFromValidFlags(int paramInt) {
        setIntAt(8, paramInt);
    }

    /**
     * returns the fromPan
     */
    public float getFromPan() {
        return getFloatAt(12);
    }

    /**
     * set the fromPan
     */
    public void setFromPan(float paramFloat) {
        setFloatAt(12, paramFloat);
    }

    /**
     * returns the fromTilt
     */
    public float getFromTilt() {
        return getFloatAt(16);
    }

    /**
     * set the fromTilt
     */
    public void setFromTilt(float paramFloat) {
        setFloatAt(16, paramFloat);
    }

    /**
     * returns the fromFOV
     */
    public float getFromFOV() {
        return getFloatAt(20);
    }

    /**
     * set the fromFOV
     */
    public void setFromFOV(float paramFloat) {
        setFloatAt(20, paramFloat);
    }

    /**
     * returns the fromViewCenter
     */
    public QDPoint getFromViewCenter() {
        return new QDPoint(getFloatAt(24), getFloatAt(28));
    }

    /**
     * set the fromViewCenter
     */
    public void setFromViewCenter(QDPoint paramQDPoint) {
        setFloatAt(24, paramQDPoint.getXF());
        setFloatAt(28, paramQDPoint.getYF());
    }

    /**
     * returns the toValidFlags
     */
    public int getToValidFlags() {
        return getIntAt(32);
    }

    /**
     * set the toValidFlags
     */
    public void setToValidFlags(int paramInt) {
        setIntAt(32, paramInt);
    }

    /**
     * returns the toPan
     */
    public float getToPan() {
        return getFloatAt(36);
    }

    /**
     * set the toPan
     */
    public void setToPan(float paramFloat) {
        setFloatAt(36, paramFloat);
    }

    /**
     * returns the toTilt
     */
    public float getToTilt() {
        return getFloatAt(40);
    }

    /**
     * set the toTilt
     */
    public void setToTilt(float paramFloat) {
        setFloatAt(40, paramFloat);
    }

    /**
     * returns the toFOV
     */
    public float getToFOV() {
        return getFloatAt(44);
    }

    /**
     * set the toFOV
     */
    public void setToFOV(float paramFloat) {
        setFloatAt(44, paramFloat);
    }

    /**
     * returns the toViewCenter
     */
    public QDPoint getToViewCenter() {
        return new QDPoint(getFloatAt(48), getFloatAt(52));
    }

    /**
     * set the toViewCenter
     */
    public void setToViewCenter(QDPoint paramQDPoint) {
        setFloatAt(48, paramQDPoint.getXF());
        setFloatAt(52, paramQDPoint.getYF());
    }

    /**
     * returns the distance
     */
    public float getDistance() {
        return getFloatAt(56);
    }

    /**
     * set the distance
     */
    public void setDistance(float paramFloat) {
        setFloatAt(56, paramFloat);
    }

    /**
     * returns the flags
     */
    public int getFlags() {
        return getIntAt(60);
    }

    /**
     * set the flags
     */
    public void setFlags(int paramInt) {
        setIntAt(60, paramInt);
    }

    /**
     * set the reserved1
     */
    public void setReserved1(int paramInt) {
        setIntAt(64, paramInt);
    }

    /**
     * returns the reserved1
     */
    public int getReserved1() {
        return getIntAt(64);
    }

    /**
     * set the reserved2
     */
    public void setReserved2(int paramInt) {
        setIntAt(68, paramInt);
    }

    /**
     * returns the reserved2
     */
    public int getReserved2() {
        return getIntAt(68);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName() + "[toNodeID=" + getToNodeID() + ",fromValidFlags=" + getFromValidFlags() + ",fromPan=" + getFromPan() + ",fromTilt" + getFromTilt() + ",fromFOV" + getFromFOV() + ",fromViewCenter" + getFromViewCenter() + ",toValidFlags" + getToValidFlags() + ",toPan" + getToPan() + ",toTilt" + getToTilt() + ",toFOV" + getToFOV() + ",toViewCenter" + getToViewCenter() + ",distance" + getDistance() + ",flags" + getFlags() + "]";
    }
}
