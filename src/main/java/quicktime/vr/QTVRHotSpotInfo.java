package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTUtils;

public final class QTVRHotSpotInfo extends QTVRAtom {

    private static final int kNativeSize = 68;

    static final long serialVersionUID = -7062349173192562621L;

    private static EndianDescriptor ed;

    /**
     * Creates a QTVRHotSpotInfo object containing no data.
     */
    public QTVRHotSpotInfo() {
        super(kNativeSize);
    }

    /**
     * Creates a QTVRHotSpotInfo object from a byte array
     * Used in cloning
     */
    public QTVRHotSpotInfo(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte, kNativeSize);
    }

    protected int getNativeSize() {
        return 68;
    }

    protected void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRHotSpotInfo localQTVRHotSpotInfo = new QTVRHotSpotInfo();
        paramObjectInputStream.read(localQTVRHotSpotInfo.getBytes());
        System.arraycopy(localQTVRHotSpotInfo.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    protected void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRHotSpotInfo localQTVRHotSpotInfo = (QTVRHotSpotInfo) clone();
        paramObjectOutputStream.write(localQTVRHotSpotInfo.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    public Object clone() {
        try {
            return new QTVRHotSpotInfo(this.bytes);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = new EndianDescriptor(0);
        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 2, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);
        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(4, 4, 16);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);
        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * returns the hotSpotType
     */
    public int getHotSpotType() {
        return getIntAt(4);
    }

    /**
     * set the hotSpotType
     * 'link' 'undf' 'url '
     */
    public void setHotSpotType(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * sets the hotSpotType using a string
     * "link" "undf" "url "
     */
    public void setHotSpotType(String paramString) {
        setIntAt(4, QTUtils.toOSType(paramString));
    }

    /**
     * returns the nameAtomID
     */
    public int getNameAtomID() {
        return getIntAt(8);
    }

    /**
     * set the nameAtomID
     * ID of the QTVRString atom
     * zero means no name atom
     */
    public void setNameAtomID(int paramInt) {
        setIntAt(8, paramInt);
    }

    /**
     * returns the commentAtomID
     */
    public int getCommentAtomID() {
        return getIntAt(12);
    }

    /**
     * set the commentAtomID
     * ID of the QTVRString atom
     * zero means no comment atom
     */
    public void setCommentAtomID(int paramInt) {
        setIntAt(12, paramInt);
    }

    /**
     * returns the mouseOver cursorID
     */
    public int getMouseOverCursorID() {
        return getIntAt(16);
    }

    /**
     * set the mouseOver cursorID
     */
    public void setMouseOverCursorID(int paramInt) {
        setIntAt(16, paramInt);
    }

    /**
     * returns the mouseDown cursorID
     */
    public int getMouseDownCursorID() {
        return getIntAt(20);
    }

    /**
     * set the mouseDown cursorID
     */
    public void setMouseDownCursorID(int paramInt) {
        setIntAt(20, paramInt);
    }

    /**
     * returns the mouseUp cursorID
     */
    public int getMouseUpCursorID() {
        return getIntAt(24);
    }

    /**
     * set the mouseUp cursorID
     */
    public void setMouseUpCursorID(int paramInt) {
        setIntAt(24, paramInt);
    }

    /**
     * returns the bestPan
     */
    public float getBestPan() {
        return getFloatAt(28);
    }

    /**
     * set the bestPan
     */
    public void setBestPan(float paramFloat) {
        setFloatAt(28, paramFloat);
    }

    /**
     * returns the bestTilt
     */
    public float getBestTilt() {
        return getFloatAt(32);
    }

    /**
     * set the bestTilt
     */
    public void setBestTilt(float paramFloat) {
        setFloatAt(32, paramFloat);
    }

    /**
     * returns the bestFOV
     */
    public float getBestFOV() {
        return getFloatAt(36);
    }

    /**
     * set the bestFOV
     */
    public void setBestFOV(float paramFloat) {
        setFloatAt(36, paramFloat);
    }

    /**
     * returns the bestViewCenter
     */
    public QDPoint getBestViewCenter() {
        return new QDPoint(getFloatAt(40), getFloatAt(44));
    }

    /**
     * set the bestViewCenter
     */
    public void setBestViewCenter(QDPoint paramQDPoint) {
        setFloatAt(40, paramQDPoint.getXF());
        setFloatAt(44, paramQDPoint.getYF());
    }

    /**
     * returns the hotSpotRect
     */
    public QDRect getHotSpotRect() {
        return new QDRect(getIntAt(50), getIntAt(48), getIntAt(54) - getIntAt(50), getIntAt(52) - getIntAt(48));
    }

    /**
     * set the hotSpotRect
     */
    public void setHotSpotRect(QDRect paramQDRect) {
        setIntAt(48, paramQDRect.getY());
        setIntAt(50, paramQDRect.getX());
        setIntAt(52, paramQDRect.getY() + paramQDRect.getHeight());
        setIntAt(54, paramQDRect.getX() + paramQDRect.getWidth());
    }

    /**
     * returns the flags
     */
    public int getFlags() {
        return getIntAt(56);
    }

    /**
     * set the flags
     */
    public void setFlags(int paramInt) {
        setIntAt(56, paramInt);
    }

    /**
     * set the reserved1
     */
    public void setReserved1(int paramInt) {
        setIntAt(60, paramInt);
    }

    /**
     * returns the reserved1
     */
    public int getReserved1() {
        return getIntAt(60);
    }

    /**
     * set the reserved2
     */
    public void setReserved2(int paramInt) {
        setIntAt(64, paramInt);
    }

    /**
     * returns the reserved2
     */
    public int getReserved2() {
        return getIntAt(64);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName() + "[hotSpotType=" + QTUtils.fromOSType(getHotSpotType()) + ",nameAtomID=" + getNameAtomID() + ",commentAtomID=" + getCommentAtomID() + ",bestPan" + getBestPan() + ",bestTilt" + getBestTilt() + ",bestFOV" + getBestFOV() + ",bestViewCenter" + getBestViewCenter() + ",hotSpotRect" + getHotSpotRect() + ",flags" + getFlags() + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRHotSpotInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
