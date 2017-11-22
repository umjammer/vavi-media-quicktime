
package quicktime.std.comp;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTUtils;


public class ComponentIdentifier extends QTObject implements QuickTimeLib {
    private static Object linkage;

    private ComponentDescription cacheInfo;

    static {
        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(ComponentIdentifier.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public static final ComponentIdentifier find(ComponentDescription paramComponentDescription) throws QTException {
        return find(null, paramComponentDescription);
    }

    public static final ComponentIdentifier find(ComponentIdentifier paramComponentIdentifier,
                                                 ComponentDescription paramComponentDescription) throws QTException {
        int i = 0;
        if (paramComponentIdentifier == null)
            i = FindNextComponent(0, paramComponentDescription.getBytes());
        else {
            i = FindNextComponent(QTObject.ID(paramComponentIdentifier), paramComponentDescription.getBytes());
        }
        return i == 0 ? null : new ComponentIdentifier(i);
    }

    private ComponentIdentifier(int paramInt) throws QTException {
        this(paramInt, null);
    }

    protected ComponentIdentifier(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public final ComponentDescription getInfo() throws StdQTException {
        if (this.cacheInfo == null) {
            this.cacheInfo = new ComponentDescription();
            int i = NewEmptyHandle();
            int j = NewEmptyHandle();
            StdQTException.checkError(GetComponentInfo(_ID(), this.cacheInfo.getBytes(), i, j, 0));

            if (GetHandleSize(i) > 0) {
                this.cacheInfo.name = getPStringFrom(i);
            }
            DisposeHandle(i);

            if (GetHandleSize(j) > 0) {
                this.cacheInfo.info = getPStringFrom(j);
            }
            DisposeHandle(j);
        }
        return this.cacheInfo;
    }

    private static String getPStringFrom(int paramInt) {
        int i = getByteFromHandle(paramInt, 0);
        int j = 1;
        if (i > 0) {
            byte[] arrayOfByte = new byte[i];
            for (int k = 1; k <= arrayOfByte.length; k++)
                arrayOfByte[(k - 1)] = getByteFromHandle(paramInt, k);
            return new String(arrayOfByte);
        }

        return null;
    }

    public String toString() {
        String str = super.toString();
        try {
            str = str + "[type=" + QTUtils.fromOSType(EndianOrder.flipNativeToBigEndian32(getInfo().getType())) + ",subType="
                  + QTUtils.fromOSType(EndianOrder.flipNativeToBigEndian32(getInfo().getSubType())) + "]";
        } catch (QTException localQTException) {
        }
        return str;
    }

    private static native int FindNextComponent(int paramInt, byte[] paramArrayOfByte);

    private static native short GetComponentInfo(int paramInt1,
                                                 byte[] paramArrayOfByte,
                                                 int paramInt2,
                                                 int paramInt3,
                                                 int paramInt4);

    private static native int GetHandleSize(int paramInt);

    private static native int NewHandle(int paramInt);

    private static native int NewEmptyHandle();

    private static native void DisposeHandle(int paramInt);

    private static native byte getByteFromHandle(int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/comp/
 * ComponentIdentifier.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
