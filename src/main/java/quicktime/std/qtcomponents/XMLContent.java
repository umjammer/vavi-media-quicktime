
package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;


public final class XMLContent extends QTObject implements PrimitivesLib, QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(XMLContent.class);
                    }
                });
            }
        }.establish();
    }

    private static Object owner = new Object();

    private static final int kXMLElementContentDataSize = 20;
    private static final int kKindOffset = 0;
    private static final int kElementOffset = 4;
    private static final int kDataOffset = 4;

    XMLContent(int paramInt) throws QTException {
        super(paramInt, owner);
    }

    XMLContent(int paramInt1, int paramInt2) throws QTException {
        super(paramInt1 + 20 * paramInt2, owner);
    }

    int getNthIdentifier(int paramInt) {
        int i = 20 * paramInt;
        int j = getIntFromPointer(_ID(), i);
        return j;
    }

    public int getKind() throws StdQTException {
        return getIntFromPointer(_ID(), 0);
    }

    public XMLElement getElement() throws QTException {
        if (getKind() == 1) {
            int i = getIntFromPointer(_ID(), 4);
            return new XMLElement(i);
        }
        throw new StdQTException(-50);
    }

    public String getString() throws StdQTException, UtilException {
        if (getKind() == 2) {
            int i = getIntFromPointer(_ID(), 4);
            return getStringFromPointer(i);
        }
        throw new StdQTException(-50);
    }

    private String getStringFromPointer(int paramInt) {
        if (paramInt != 0) {
            int i = 0;
            int j = 0;
            while (getByteFromPointer(paramInt, j++) != 0) {
                i++;
            }
            byte[] arrayOfByte = new byte[i];
            copyPointerToArray(paramInt, 0, arrayOfByte, 0, i);
            return QTUtils.CString2String(arrayOfByte, 0);
        }
        return "";
    }

    private static native byte getByteFromPointer(int paramInt1, int paramInt2);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  byte[] paramArrayOfByte,
                                                  int paramInt3,
                                                  int paramInt4);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/XMLContent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
