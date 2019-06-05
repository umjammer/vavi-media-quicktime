
package quicktime.std.comp;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;


public class Component extends ComponentIdentifier implements QuickTimeLib {
    private static Object linkage;

    static {
        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(Component.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public Component(ComponentIdentifier paramComponentIdentifier) throws StdQTException {
        this(allocate(QTObject.ID(paramComponentIdentifier)), null);
    }

    protected Component(ComponentIdentifier paramComponentIdentifier, int paramInt) throws QTException {
        this(allocate(paramComponentIdentifier, paramInt), null);
    }

    public Component(int paramInt1, int paramInt2) throws QTException {
        this(allocate(paramInt1, paramInt2), null);
    }

    protected Component(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    protected Component(int paramInt) throws StdQTException {
        super(allocate(paramInt), null);
    }

    private static int allocate(ComponentIdentifier paramComponentIdentifier, int paramInt) throws StdQTException {
        if (paramComponentIdentifier.getInfo().getType() != paramInt)
            throw new StdQTException(62536);
        return allocate(QTObject.ID(paramComponentIdentifier));
    }

    private static int allocate(int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(OpenADefaultComponent(paramInt1, paramInt2, arrayOfInt));
        return arrayOfInt[0];
    }

    private static int allocate(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(OpenAComponent(paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    public final int count() {
        return CountComponentInstances(_ID());
    }

    private static native short OpenADefaultComponent(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short OpenAComponent(int paramInt, int[] paramArrayOfInt);

    private static native int CountComponentInstances(int paramInt);
}
