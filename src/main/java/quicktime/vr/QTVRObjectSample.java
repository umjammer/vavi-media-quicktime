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

    /**
     * ObjectUITypes
     */
    public static final short kGrabberScrollerUI = 1;

    /**
     * ObjectUITypes
     */
    public static final short kOldJoyStickUI = 2;

    /**
     * ObjectUITypes
     */
    public static final short kJoystickUI = 3;

    /**
     * ObjectUITypes
     */
    public static final short kGrabberUI = 4;

    /**
     * ObjectUITypes
     */
    public static final short kAbsoluteUI = 5;

    static final long serialVersionUID = 8513787618060671940L;

    private static EndianDescriptor ed;

    /**
     * Creates a QTVRObjectSample object containing no data
     */
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

    /**
     * Returns a copy of this class.
     */
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

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * @return movieType kGrabberScrollerUI, kOldJoyStickUI, kJoystickUI, kGrabberUI or kAbsoluteUI
     */
    public short getMovieType() {
        return getShortAt(4);
    }

    /**
     * @param movieType One of kGrabberScrollerUI, kOldJoyStickUI, kJoystickUI, kGrabberUI or kAbsoluteUI
     */
    public void setMovieType(short paramShort) {
        setShortAt(4, paramShort);
    }

    /**
     * @return the number of view states 1 based
     */
    public short getViewStateCount() {
        return getShortAt(6);
    }

    /**
     * @param viewStateCount The number of view states 1 based
     */
    public void setViewStateCount(short paramShort) {
        setShortAt(6, paramShort);
    }

    /**
     * @return the default view state number 1 based
     */
    public short getDefaultViewState() {
        return getShortAt(8);
    }

    /**
     * @param defaultViewState the default view state number 1 based.  The number must be 1 to viewStateCount
     */
    public void setDefaultViewState(short paramShort) {
        setShortAt(8, paramShort);
    }

    /**
     * @return The mouse down view state 1 based
     */
    public short getMouseDownViewState() {
        return getShortAt(10);
    }

    /**
     * @param mouseDownViewState The mouse down view state 1 based. The number must be 1 to viewStateCount
     */
    public void setMouseDownViewState(short paramShort) {
        setShortAt(10, paramShort);
    }

    /**
     * @return The duration of each view including all animation frames in a view
     */
    public int getViewDuration() {
        return getIntAt(12);
    }

    /**
     * @param viewDuration The duration of each view including all animation frames in a view
     */
    public void setViewDuration(int paramInt) {
        setIntAt(12, paramInt);
    }

    /**
     * @return The number of columns in the VRObject
     */
    public int getColumns() {
        return getIntAt(16);
    }

    /**
     * @param columns The number of columns in the VRObject
     */
    public void setColumns(int paramInt) {
        setIntAt(16, paramInt);
    }

    /**
     * @return The number of rows in the VRObject
     */
    public int getRows() {
        return getIntAt(20);
    }

    /**
     * @param rows The number of rows the VRObject
     */
    public void setRows(int paramInt) {
        setIntAt(20, paramInt);
    }

    /**
     * @return 180.0 for kStandardObject or kQTVRObjectInScene, actual degrees for kOldNavigableMovieScene
     */
    public float getMouseMotionScale() {
        return getFloatAt(24);
    }

    /**
     * @param mouseMotionScale 180.0 for kStandardObject or kQTVRObjectInScene, actual degrees for kOldNavigableMovieScene
     */
    public void setMouseMotionScale(float paramFloat) {
        setFloatAt(24, paramFloat);
    }

    /**
     * @return Start horizontal pan angle in degrees
     */
    public float getMinPan() {
        return getFloatAt(28);
    }

    /**
     * @param minPan Start horizontal pan angle in degrees
     */
    public void setMinPan(float paramFloat) {
        setFloatAt(28, paramFloat);
    }

    /**
     * @return End horizontal pan angle in degrees
     */
    public float getMaxPan() {
        return getFloatAt(32);
    }

    /**
     * @param maxPan End horizontal pan angle in degrees
     */
    public void setMaxPan(float paramFloat) {
        setFloatAt(32, paramFloat);
    }

    /**
     * @return Initial horizontal pan angle in degrees (poster view)
     */
    public float getDefaultPan() {
        return getFloatAt(36);
    }

    /**
     * @param defaultPan Initial horizontal pan angle in degrees (poster view)
     */
    public void setDefaultPan(float paramFloat) {
        setFloatAt(36, paramFloat);
    }

    /**
     * @return Start vertical pan angle in degrees
     */
    public float getMinTilt() {
        return getFloatAt(40);
    }

    /**
     * @param minTilt Start vertical pan angle in degrees
     */
    public void setMinTilt(float paramFloat) {
        setFloatAt(40, paramFloat);
    }

    /**
     * @return End vertical pan angle in degrees
     */
    public float getMaxTilt() {
        return getFloatAt(44);
    }

    /**
     * @param maxTilt End vertical pan angle in degrees
     */
    public void setMaxTilt(float paramFloat) {
        setFloatAt(44, paramFloat);
    }

    /**
     * @return Initial vertical pan angle in degrees (poster view)
     */
    public float getDefaultTilt() {
        return getFloatAt(48);
    }

    /**
     * @param defaultTilt Initial vertical pan angle in degrees (poster view)
     */
    public void setDefaultTilt(float paramFloat) {
        setFloatAt(48, paramFloat);
    }

    /**
     * @return minimum field of view setting (appears as the maximum zoom effect)
     */
    public float getMinFieldOfView() {
        return getFloatAt(52);
    }

    /**
     * @param minFieldOfView field of view setting (appears as the maximum zoom effect)
     * @throws minFieldOfView must be &gt;= 1
     * @throws quicktime.QTException
     */
    public void setMinFieldOfView(float paramFloat) throws QTException {
        if (paramFloat < 1.0D)
            throw new QTException(-50);
        setFloatAt(52, paramFloat);
    }

    /**
     * @return the field of view range
     */
    public float getFieldOfView() {
        return getFloatAt(56);
    }

    /**
     * @param fieldOfView the field of view range
     * @throws fieldOfView must be &gt;= 1
     * @throws quicktime.QTException
     */
    public void setFieldOfView(float paramFloat) throws QTException {
        if (paramFloat < 1.0D)
            throw new QTException(-50);
        setFloatAt(56, paramFloat);
    }

    /**
     * @return the default field of view
     */
    public float getDefaultFieldOfView() {
        return getFloatAt(60);
    }

    /**
     * @param defaultFieldOfView the default field of view
     */
    public void setDefaultFieldOfView(float paramFloat) {
        setFloatAt(60, paramFloat);
    }

    /**
     * @return the default view center
     */
    public QDPoint getDefaultViewCenter() {
        return new QDPoint(getFloatAt(64), getFloatAt(68));
    }

    /**
     * @param defaultViewCenter the default view center
     */
    public void setDefaultViewCenter(QDPoint paramQDPoint) {
        setFloatAt(64, paramQDPoint.getXF());
        setFloatAt(68, paramQDPoint.getYF());
    }

    /**
     * @return the view rate
     */
    public float getViewRate() {
        return getFloatAt(72);
    }

    /**
     * @param viewRate the view rate
     */
    public void setViewRate(float paramFloat) {
        setFloatAt(72, paramFloat);
    }

    /**
     * @return the frame rate
     */
    public float getFrameRate() {
        return getFloatAt(76);
    }

    /**
     * @param frameRate the frame rate
     */
    public void setFrameRate(float paramFloat) {
        setFloatAt(76, paramFloat);
    }

    /**
     * @return the Animation Settings (reserved)
     */
    public int getAnimationSettings() {
        return getIntAt(80);
    }

    /**
     * @param animationSettings The Animation Settings (reserved)
     */
    public void setAnimationSettings(int paramInt) {
        setIntAt(80, paramInt);
    }

    /**
     * @return the Control Settings (reserved)
     */
    public int getControlSettings() {
        return getIntAt(84);
    }

    /**
     * @param controlSettings The Control Settings (reserved)
     */
    public void setControlSettings(int paramInt) {
        setIntAt(84, paramInt);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName() + "[movieType=" + getMovieType() + ",viewStateCount=" + getViewStateCount() + ",defaultViewState=" + getDefaultViewState() + ",mouseDownViewState=" + getMouseDownViewState() + ",viewDuration=" + getViewDuration() + ",columns=" + getColumns() + ",rows=" + getRows() + ",mouseMotionScale=" + getMouseMotionScale() + ",minPan=" + getMinPan() + ",maxPan=" + getMaxPan() + ",defaultPan=" + getDefaultPan() + ",minTilt=" + getMinTilt() + ",maxTilt=" + getMaxTilt() + ",defaultTilt=" + getDefaultTilt() + ",minFieldOfView=" + getMinFieldOfView() + ",fieldOfView=" + getFieldOfView() + ",defaultFieldOfView=" + getDefaultFieldOfView() + ",defaultViewCenter=" + getDefaultViewCenter() + ",viewRate=" + getViewRate() + ",frameRate=" + getFrameRate() + ",animationSettings=" + getAnimationSettings() + ",controlSettings" + getControlSettings() + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRObjectSample.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
