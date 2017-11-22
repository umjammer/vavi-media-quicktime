
package quicktime.vr;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.qd.QDPoint;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;


public class QTVRInterceptRecord extends QTPointerRef implements PrimitivesLib {
    private static Object linkage;

    private static final int kNativeSize = 44;

    static {
        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                  public Object run() {
                      return QTNative.linkNativeMethods(QTVRInterceptRecord.class);
                  }
              });
            }
        }
        new PrivelegedAction().establish();
    }

    protected QTVRInterceptRecord(int paramInt, Object paramObject) {
        super(paramInt, 44, paramObject);
        if (paramObject == null) {
            throw new RuntimeException("Must have an owner");
        }
    }

    public final int getSelector() {
        return getIntAt(4);
    }

    public final int getParamCount() {
        return getIntAt(16);
    }

    public float getAngle() throws QTException {
        int i = getSelector();
        if ((i == 8192) || (i == 8193)) {
            int j = getIntAt(20);
            return getFloatFromPointer(j, 0);
        }
        throw new QTException(-50);
    }

    public void setAngle(float paramFloat) throws QTException {
        int i = getSelector();
        if ((i == 8192) || (i == 8193)) {
            int j = getIntAt(20);
            setFloatInPointer(j, 0, paramFloat);
        }
        throw new QTException(-50);
    }

    public float getFieldOfView() throws QTException {
        int i = getSelector();
        if (i != 8194)
            throw new QTException(-50);
        int j = getIntAt(20);
        return getFloatFromPointer(j, 0);
    }

    public void setFieldOfView(float paramFloat) throws QTException {
        int i = getSelector();
        if (i != 8194)
            throw new QTException(-50);
        int j = getIntAt(20);
        setFloatInPointer(j, 0, paramFloat);
    }

    public QDPoint getViewCenter() throws QTException {
        int i = getSelector();
        if (i != 8195)
            throw new QTException(-50);
        int j = getIntAt(20);
        float f1 = getFloatFromPointer(j, 0);
        float f2 = getFloatFromPointer(j, 4);
        return new QDPoint(f1, f2);
    }

    public void setViewCenter(QDPoint paramQDPoint) throws QTException {
        int i = getSelector();
        if (i != 8195)
            throw new QTException(-50);
        int j = getIntAt(20);
        setFloatInPointer(j, 0, paramQDPoint.getXF());
        setFloatInPointer(j, 4, paramQDPoint.getYF());
    }

    public int getHotSpotID() throws QTException {
        int i = getSelector();
        if ((i == 8202) || (i == 8203)) {
            return getIntAt(20);
        }
        throw new QTException(-50);
    }

    public void setHotSpotID(int paramInt) throws QTException {
        int i = getSelector();
        if ((i == 8202) || (i == 8203)) {
            setIntAt(20, paramInt);
        }
        throw new QTException(-50);
    }

    public int getHotSpotType() throws QTException {
        if (getSelector() != 8203)
            throw new QTException(-50);
        int i = getIntAt(24);
        return getIntFromPointer(i, 0);
    }

    public void setHotSpotType(int paramInt) throws QTException {
        if (getSelector() != 8203)
            throw new QTException(-50);
        int i = getIntAt(24);
        setIntInPointer(i, 0, paramInt);
    }

    public String toString() {
        String str = getClass().getName();
        try {
            str = str + "[selector=0x" + Integer.toHexString(getSelector());
            str = str + ",paramCount=" + getParamCount();
            switch (getSelector()) {
            case 8192:
                str = str + ",pan angle (Radians)=" + getAngle();
                break;
            case 8193:
                str = str + ",tilt angle (Radians)=" + getAngle();
                break;
            case 8194:
                str = str + ",field of view (Radians)=" + getFieldOfView();
                break;
            case 8195:
                str = str + ",view center=" + getViewCenter();
                break;
            case 8202:
                str = str + ",hot spot ID=" + getHotSpotID();
                break;
            case 8203:
                str = str + ",hot spot ID=" + getHotSpotID();
                str = str + ",hotSpotType='" + QTUtils.fromOSType(getHotSpotType()) + "'";
            }
        } catch (QTException localQTException) {
            str = str + "MISMATCHED SELECTOR";
        }
        str = str + "]";
        return str;
    }

    private static native void setIntInPointer(int paramInt1, int paramInt2, int paramInt3);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native void setFloatInPointer(int paramInt1, int paramInt2, float paramFloat);

    private static native float getFloatFromPointer(int paramInt1, int paramInt2);
}
