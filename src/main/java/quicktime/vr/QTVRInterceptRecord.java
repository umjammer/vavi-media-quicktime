package quicktime.vr;

import quicktime.QTException;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.qd.QDPoint;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;


/**
 * The QTVRInterceptRecord is passed to the QTVRInterceptor's exeucte method by QuickTime.
 * The application can look at the about to occur change in the VR movie through the
 * parameters of this record and can in turn call the VR interceptor method itself with
 * any particular changes it wants to make, passing the QTVRInterceptor record to
 * this method.
 */
public class QTVRInterceptRecord extends QTPointerRef implements PrimitivesLib {

    private static final int kNativeSize = 44;

    protected QTVRInterceptRecord(int paramInt, Object paramObject) {
        super(paramInt, kNativeSize, paramObject);
        if (paramObject == null) {
            throw new RuntimeException("Must have an owner");
        }
    }

    /**
     * This returns the selector which will in turn describe the parameters
     * that this record will contain. This is a read only field.
     *
     * @return an int
     */
    public final int getSelector() {
        return getIntAt(4);
    }

    /**
     * This returns the number of parameters in a particular instance.
     * The number of parameters are dependent on the given selector.
     * This is a read only field.
     *
     * @return an int
     */
    public final int getParamCount() {
        return getIntAt(16);
    }

    /**
     * Returns the pan or tilt angle, depending on the selector.
     * If the selector is not kQTVRSetPanAngleSelector or kQTVRSetTiltAngleSelector
     * a paramError QTException is thrown.
     *
     * @return the angle in Radians.
     * @throws quicktime.QTException
     */
    public float getAngle() throws QTException {
        int i = getSelector();
        if ((i == 8192) || (i == 8193)) {
            int j = getIntAt(20);
            return getFloatFromPointer(j, 0);
        }
        throw new QTException(-50);
    }

    /**
     * Sets the pan or tilt angle, depending on the selector.
     * If the selector is not kQTVRSetPanAngleSelector or kQTVRSetTiltAngleSelector
     * a paramError QTException is thrown.
     *
     * @param angle the angle in Radians.
     * @throws quicktime.QTException
     */
    public void setAngle(float angle) throws QTException {
        int i = getSelector();
        if ((i == 8192) || (i == 8193)) {
            int j = getIntAt(20);
            setFloatInPointer(j, 0, angle);
        }
        throw new QTException(-50);
    }

    /**
     * Returns the field of view for the kQTVRSetFieldOfViewSelector.
     * If the selector is not kQTVRSetFieldOfViewSelector
     * a paramError QTException is thrown.
     *
     * @return the field of view
     * @throws quicktime.QTException
     */
    public float getFieldOfView() throws QTException {
        int i = getSelector();
        if (i != 8194)
            throw new QTException(-50);
        int j = getIntAt(20);
        return getFloatFromPointer(j, 0);
    }

    /**
     * Sets the field of view for the kQTVRSetFieldOfViewSelector.
     * If the selector is not kQTVRSetFieldOfViewSelector
     * a paramError QTException is thrown.
     *
     * @param fieldOfView the field of view
     * @throws quicktime.QTException
     */
    public void setFieldOfView(float fieldOfView) throws QTException {
        int i = getSelector();
        if (i != 8194)
            throw new QTException(-50);
        int j = getIntAt(20);
        setFloatInPointer(j, 0, fieldOfView);
    }

    /**
     * Returns the field of view for the kQTVRSetViewCenterSelector.
     * If the selector is not kQTVRSetViewCenterSelector
     * a paramError QTException is thrown.
     *
     * @return the current view center
     * @throws quicktime.QTException
     */
    public QDPoint getViewCenter() throws QTException {
        int i = getSelector();
        if (i != 8195)
            throw new QTException(-50);
        int j = getIntAt(20);
        float f1 = getFloatFromPointer(j, 0);
        float f2 = getFloatFromPointer(j, 4);
        return new QDPoint(f1, f2);
    }

    /**
     * Sets the field of view for the kQTVRSetViewCenterSelector.
     * If the selector is not kQTVRSetViewCenterSelector
     * a paramError QTException is thrown.
     *
     * @param viewCenter the new view center
     * @throws quicktime.QTException
     */
    public void setViewCenter(QDPoint viewCenter) throws QTException {
        int i = getSelector();
        if (i != 8195)
            throw new QTException(-50);
        int j = getIntAt(20);
        setFloatInPointer(j, 0, viewCenter.getXF());
        setFloatInPointer(j, 4, viewCenter.getYF());
    }

    /**
     * Gets the hot spot ID.
     * If the selector is not kQTVRTriggerHotSpotSelector or kQTVRGetHotSpotTypeSelector
     * a paramError QTException is thrown.
     *
     * @return the hot spot ID
     * @throws quicktime.QTException
     */
    public int getHotSpotID() throws QTException {
        int i = getSelector();
        if ((i == 8202) || (i == 8203)) {
            return getIntAt(20);
        }
        throw new QTException(-50);
    }

    /**
     * Sets the hot spot ID.
     * If the selector is not kQTVRTriggerHotSpotSelector or kQTVRGetHotSpotTypeSelector
     * a paramError QTException is thrown.
     *
     * @param hotSpotID the new hot spot ID
     * @throws quicktime.QTException
     */
    public void setHotSpotID(int hotSpotID) throws QTException {
        int i = getSelector();
        if ((i == 8202) || (i == 8203)) {
            setIntAt(20, hotSpotID);
        }
        throw new QTException(-50);
    }

    /**
     * Gets the hot spot type.
     * If the selector is not kQTVRGetHotSpotTypeSelector
     * a paramError QTException is thrown.
     *
     * @return the hot spot type
     * @throws quicktime.QTException
     */
    public int getHotSpotType() throws QTException {
        if (getSelector() != 8203)
            throw new QTException(-50);
        int i = getIntAt(24);
        return getIntFromPointer(i, 0);
    }

    /**
     * Sets the hot spot type.
     * If the selector is not kQTVRGetHotSpotTypeSelector
     * a paramError QTException is thrown.
     *
     * @param hotSpotType the new hot spot type
     * @throws quicktime.QTException
     */
    public void setHotSpotType(int hotSpotType) throws QTException {
        if (getSelector() != 8203)
            throw new QTException(-50);
        int i = getIntAt(24);
        setIntInPointer(i, 0, hotSpotType);
    }

    /**
     * Returns a String representation of this object.
     *
     * @return a String
     */
    public String toString() {
        String str = getClass().getName();
        try {
            str = str + "[selector=0x" + Integer.toHexString(getSelector());
            str = str + ",paramCount=" + getParamCount();
            str = switch (getSelector()) {
                case 8192 -> str + ",pan angle (Radians)=" + getAngle();
                case 8193 -> str + ",tilt angle (Radians)=" + getAngle();
                case 8194 -> str + ",field of view (Radians)=" + getFieldOfView();
                case 8195 -> str + ",view center=" + getViewCenter();
                case 8202 -> str + ",hot spot ID=" + getHotSpotID();
                case 8203 -> {
                    str = str + ",hot spot ID=" + getHotSpotID();
                    yield str + ",hotSpotType='" + QTUtils.fromOSType(getHotSpotType()) + "'";
                }
                default -> str;
            };
        } catch (QTException localQTException) {
            str = str + "MISMATCHED SELECTOR";
        }
        str = str + "]";
        return str;
    }

    private static void setIntInPointer(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.setIntInPointer(paramInt1, paramInt2, paramInt3);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static void setFloatInPointer(int paramInt1, int paramInt2, float paramFloat) {
        MemoryManager.setFloatInPointer(paramInt1, paramInt2, paramFloat);
    }

    private static float getFloatFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getFloatFromPointer(paramInt1, paramInt2);
    }
}
