
package quicktime.qd3d.camera;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;


public final class OrthographicCameraData extends CameraData implements PrimitivesLib {
    private static Object linkage;

    public static final int kNativeSize = 76;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }

        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(OrthographicCameraData.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public OrthographicCameraData() {
        super(76);
    }

    public OrthographicCameraData(CameraData paramCameraData,
            float paramFloat1,
            float paramFloat2,
            float paramFloat3,
            float paramFloat4) {
        this();

        setCameraBytes(paramCameraData);
        setLeft(paramFloat1);
        setTop(paramFloat2);
        setRight(paramFloat3);
        setBottom(paramFloat4);
    }

    private OrthographicCameraData(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    public float getLeft() {
        return getFloatAt(60);
    }

    public float getTop() {
        return getFloatAt(64);
    }

    public float getRight() {
        return getFloatAt(68);
    }

    public float getBottom() {
        return getFloatAt(72);
    }

    public void setLeft(float paramFloat) {
        setFloatAt(60, paramFloat);
    }

    public void setTop(float paramFloat) {
        setFloatAt(64, paramFloat);
    }

    public void setRight(float paramFloat) {
        setFloatAt(68, paramFloat);
    }

    public void setBottom(float paramFloat) {
        setFloatAt(72, paramFloat);
    }

    public Object clone() {
        return new OrthographicCameraData(this.bytes);
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/
 * camera/OrthographicCameraData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
