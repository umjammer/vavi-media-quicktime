
package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.qd.QDPoint;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;


public final class QTVRObjectSample extends QTVRAtom {
    private static final int kNativeSize = 88;

    public static final int kQTVRObjectAnimateViewFramesOn = 1;
    public static final int kQTVRObjectPalindromeViewFramesOn = 2;
    public static final int kQTVRObjectStartFirstViewFrameOn = 4;
    public static final int kQTVRObjectAnimateViewsOn = 8;
    public static final int kQTVRObjectPalindromeViewsOn = 16;
    public static final int kQTVRObjectSyncViewToFrameRate = 32;
    public static final int kQTVRObjectDontLoopViewFramesOn = 64;
    public static final int kQTVRObjectPlayEveryViewFrameOn = 128;
    public static final int kQTVRObjectStreamingViewsOn = 256;
    public static final int kQTVRObjectWrapPanOn = 1;
    public static final int kQTVRObjectWrapTiltOn = 2;
    public static final int kQTVRObjectCanZoomOn = 4;
    public static final int kQTVRObjectReverseHControlOn = 8;
    public static final int kQTVRObjectReverseVControlOn = 16;
    public static final int kQTVRObjectSwapHVControlOn = 32;
    public static final int kQTVRObjectTranslationOn = 64;

    public static final short kGrabberScrollerUI = 1;
    public static final short kOldJoyStickUI = 2;
    public static final short kJoystickUI = 3;
    public static final short kGrabberUI = 4;
    public static final short kAbsoluteUI = 5;

    static final long serialVersionUID = 8513787618060671940L;

    private static EndianDescriptor ed;

    public QTVRObjectSample() {
        super(kNativeSize);
    }

    public QTVRObjectSample(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte, kNativeSize);
    }

    protected int getNativeSize() {
        return kNativeSize;
    }

    protected void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRObjectSample localQTVRObjectSample = new QTVRObjectSample();
        paramObjectInputStream.read(localQTVRObjectSample.getBytes());
        System.arraycopy(localQTVRObjectSample.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    protected void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRObjectSample localQTVRObjectSample = (QTVRObjectSample) clone();
        paramObjectOutputStream.write(localQTVRObjectSample.getBytes());
    }

    public Object clone() {
        try {
            return new QTVRObjectSample(this.bytes);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = new EndianDescriptor(0);

        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 2, 6);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);

        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(12, 4, 19);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);

        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    public short getMovieType() {
        return getShortAt(4);
    }

    public void setMovieType(short paramShort) {
        setShortAt(4, paramShort);
    }

    public short getViewStateCount() {
        return getShortAt(6);
    }

    public void setViewStateCount(short paramShort) {
        setShortAt(6, paramShort);
    }

    public short getDefaultViewState() {
        return getShortAt(8);
    }

    public void setDefaultViewState(short paramShort) {
        setShortAt(8, paramShort);
    }

    public short getMouseDownViewState() {
        return getShortAt(10);
    }

    public void setMouseDownViewState(short paramShort) {
        setShortAt(10, paramShort);
    }

    public int getViewDuration() {
        return getIntAt(12);
    }

    public void setViewDuration(int paramInt) {
        setIntAt(12, paramInt);
    }

    public int getColumns() {
        return getIntAt(16);
    }

    public void setColumns(int paramInt) {
        setIntAt(16, paramInt);
    }

    public int getRows() {
        return getIntAt(20);
    }

    public void setRows(int paramInt) {
        setIntAt(20, paramInt);
    }

    public float getMouseMotionScale() {
        return getFloatAt(24);
    }

    public void setMouseMotionScale(float paramFloat) {
        setFloatAt(24, paramFloat);
    }

    public float getMinPan() {
        return getFloatAt(28);
    }

    public void setMinPan(float paramFloat) {
        setFloatAt(28, paramFloat);
    }

    public float getMaxPan() {
        return getFloatAt(32);
    }

    public void setMaxPan(float paramFloat) {
        setFloatAt(32, paramFloat);
    }

    public float getDefaultPan() {
        return getFloatAt(36);
    }

    public void setDefaultPan(float paramFloat) {
        setFloatAt(36, paramFloat);
    }

    public float getMinTilt() {
        return getFloatAt(40);
    }

    public void setMinTilt(float paramFloat) {
        setFloatAt(40, paramFloat);
    }

    public float getMaxTilt() {
        return getFloatAt(44);
    }

    public void setMaxTilt(float paramFloat) {
        setFloatAt(44, paramFloat);
    }

    public float getDefaultTilt() {
        return getFloatAt(48);
    }

    public void setDefaultTilt(float paramFloat) {
        setFloatAt(48, paramFloat);
    }

    public float getMinFieldOfView() {
        return getFloatAt(52);
    }

    public void setMinFieldOfView(float paramFloat) throws QTException {
        if (paramFloat < 1.0D)
            throw new QTException(-50);
        setFloatAt(52, paramFloat);
    }

    public float getFieldOfView() {
        return getFloatAt(56);
    }

    public void setFieldOfView(float paramFloat) throws QTException {
        if (paramFloat < 1.0D)
            throw new QTException(-50);
        setFloatAt(56, paramFloat);
    }

    public float getDefaultFieldOfView() {
        return getFloatAt(60);
    }

    public void setDefaultFieldOfView(float paramFloat) {
        setFloatAt(60, paramFloat);
    }

    public QDPoint getDefaultViewCenter() {
        return new QDPoint(getFloatAt(64), getFloatAt(68));
    }

    public void setDefaultViewCenter(QDPoint paramQDPoint) {
        setFloatAt(64, paramQDPoint.getXF());
        setFloatAt(68, paramQDPoint.getYF());
    }

    public float getViewRate() {
        return getFloatAt(72);
    }

    public void setViewRate(float paramFloat) {
        setFloatAt(72, paramFloat);
    }

    public float getFrameRate() {
        return getFloatAt(76);
    }

    public void setFrameRate(float paramFloat) {
        setFloatAt(76, paramFloat);
    }

    public int getAnimationSettings() {
        return getIntAt(80);
    }

    public void setAnimationSettings(int paramInt) {
        setIntAt(80, paramInt);
    }

    public int getControlSettings() {
        return getIntAt(84);
    }

    public void setControlSettings(int paramInt) {
        setIntAt(84, paramInt);
    }

    public String toString() {
        return getClass().getName() + "[movieType=" + getMovieType() + ",viewStateCount=" + getViewStateCount()
               + ",defaultViewState=" + getDefaultViewState() + ",mouseDownViewState=" + getMouseDownViewState()
               + ",viewDuration=" + getViewDuration() + ",columns=" + getColumns() + ",rows=" + getRows() + ",mouseMotionScale="
               + getMouseMotionScale() + ",minPan=" + getMinPan() + ",maxPan=" + getMaxPan() + ",defaultPan=" + getDefaultPan()
               + ",minTilt=" + getMinTilt() + ",maxTilt=" + getMaxTilt() + ",defaultTilt=" + getDefaultTilt()
               + ",minFieldOfView=" + getMinFieldOfView() + ",fieldOfView=" + getFieldOfView() + ",defaultFieldOfView="
               + getDefaultFieldOfView() + ",defaultViewCenter=" + getDefaultViewCenter() + ",viewRate=" + getViewRate()
               + ",frameRate=" + getFrameRate() + ",animationSettings=" + getAnimationSettings() + ",controlSettings"
               + getControlSettings() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRObjectSample.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
