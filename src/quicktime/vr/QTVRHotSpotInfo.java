
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

    public QTVRHotSpotInfo() {
        super(kNativeSize);
    }

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

    public int getHotSpotType() {
        return getIntAt(4);
    }

    public void setHotSpotType(int paramInt) {
        setIntAt(4, paramInt);
    }

    public void setHotSpotType(String paramString) {
        setIntAt(4, QTUtils.toOSType(paramString));
    }

    public int getNameAtomID() {
        return getIntAt(8);
    }

    public void setNameAtomID(int paramInt) {
        setIntAt(8, paramInt);
    }

    public int getCommentAtomID() {
        return getIntAt(12);
    }

    public void setCommentAtomID(int paramInt) {
        setIntAt(12, paramInt);
    }

    public int getMouseOverCursorID() {
        return getIntAt(16);
    }

    public void setMouseOverCursorID(int paramInt) {
        setIntAt(16, paramInt);
    }

    public int getMouseDownCursorID() {
        return getIntAt(20);
    }

    public void setMouseDownCursorID(int paramInt) {
        setIntAt(20, paramInt);
    }

    public int getMouseUpCursorID() {
        return getIntAt(24);
    }

    public void setMouseUpCursorID(int paramInt) {
        setIntAt(24, paramInt);
    }

    public float getBestPan() {
        return getFloatAt(28);
    }

    public void setBestPan(float paramFloat) {
        setFloatAt(28, paramFloat);
    }

    public float getBestTilt() {
        return getFloatAt(32);
    }

    public void setBestTilt(float paramFloat) {
        setFloatAt(32, paramFloat);
    }

    public float getBestFOV() {
        return getFloatAt(36);
    }

    public void setBestFOV(float paramFloat) {
        setFloatAt(36, paramFloat);
    }

    public QDPoint getBestViewCenter() {
        return new QDPoint(getFloatAt(40), getFloatAt(44));
    }

    public void setBestViewCenter(QDPoint paramQDPoint) {
        setFloatAt(40, paramQDPoint.getXF());
        setFloatAt(44, paramQDPoint.getYF());
    }

    public QDRect getHotSpotRect() {
        return new QDRect(getIntAt(50), getIntAt(48), getIntAt(54) - getIntAt(50), getIntAt(52) - getIntAt(48));
    }

    public void setHotSpotRect(QDRect paramQDRect) {
        setIntAt(48, paramQDRect.getY());
        setIntAt(50, paramQDRect.getX());
        setIntAt(52, paramQDRect.getY() + paramQDRect.getHeight());
        setIntAt(54, paramQDRect.getX() + paramQDRect.getWidth());
    }

    public int getFlags() {
        return getIntAt(56);
    }

    public void setFlags(int paramInt) {
        setIntAt(56, paramInt);
    }

    public void setReserved1(int paramInt) {
        setIntAt(60, paramInt);
    }

    public int getReserved1() {
        return getIntAt(60);
    }

    public void setReserved2(int paramInt) {
        setIntAt(64, paramInt);
    }

    public int getReserved2() {
        return getIntAt(64);
    }

    public String toString() {
        return getClass().getName() + "[hotSpotType=" + QTUtils.fromOSType(getHotSpotType()) + ",nameAtomID=" + getNameAtomID()
               + ",commentAtomID=" + getCommentAtomID() + ",bestPan" + getBestPan() + ",bestTilt" + getBestTilt() + ",bestFOV"
               + getBestFOV() + ",bestViewCenter" + getBestViewCenter() + ",hotSpotRect" + getHotSpotRect() + ",flags"
               + getFlags() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRHotSpotInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
