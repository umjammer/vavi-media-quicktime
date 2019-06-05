
package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;


public final class XMLElement extends QTObject implements PrimitivesLib {

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(XMLElement.class);
                    }
                });
            }
        }.establish();
    }

    private static Object owner = new Object();

    private static Object linkage;

    private static final int kXMLElementSize = 16;
    private static final int kIdentifierOffset = 0;
    private static final int kNameOffset = 4;
    private static final int kAttributesOffset = 8;
    private static final int kContentsOffset = 12;

    XMLElement(int paramInt) throws QTException {
        super(paramInt, owner);
    }

    public int getIdentifier() throws StdQTException {
        int i = getIntFromPointer(_ID(), 0);
        if (i != -1) {
            return i;
        }
        System.out.println("XMLElement.getIdentifier() called but is xmlIdentifierUnrecognized");
        throw new StdQTException(-50);
    }

    public boolean identifierUnrecognized() {
        int i = getIntFromPointer(_ID(), 0);
        return i == -1;
    }

    public String getName() throws StdQTException, UtilException {
        int i = getIntFromPointer(_ID(), 0);
        if (i == -1) {
            int j = getIntFromPointer(_ID(), 4);
            String str = getStringFromPointer(j);
            return str;
        }
        System.out.println("XMLElement.getName() called but not xmlIdentifierUnrecognized");
        throw new StdQTException(-50);
    }

    public int countAttributes() throws StdQTException, QTException {
        int i = getIntFromPointer(_ID(), 8);
        if (i != 0) {
            XMLAttribute localXMLAttribute = new XMLAttribute(i);
            for (int j = 0;; j++) {
                if (localXMLAttribute.getNthIdentifier(j) == 0) {
                    return j;
                }
            }
        }
        return 0;
    }

    public XMLAttribute[] getAttributes() throws StdQTException, QTException {
        int i = countAttributes();
        if (i != 0) {
            XMLAttribute[] arrayOfXMLAttribute = new XMLAttribute[i];
            int j = getIntFromPointer(_ID(), 8);
            for (int k = 0; k < i; k++) {
                arrayOfXMLAttribute[k] = new XMLAttribute(j, k);
            }
            return arrayOfXMLAttribute;
        }
        return null;
    }

    public int countContents() throws StdQTException, QTException {
        int i = getIntFromPointer(_ID(), 12);
        if (i != 0) {
            XMLContent localXMLContent = new XMLContent(i);
            for (int j = 0;; j++) {
                if (localXMLContent.getNthIdentifier(j) == 0) {
                    return j;
                }
            }
        }
        return 0;
    }

    public XMLContent[] getContents() throws StdQTException, QTException {
        int i = countContents();
        if (i != 0) {
            int j = getIntFromPointer(_ID(), 12);
            XMLContent[] arrayOfXMLContent = new XMLContent[i];
            for (int k = 0; k < i; k++) {
                arrayOfXMLContent[k] = new XMLContent(j, k);
            }
            return arrayOfXMLContent;
        }
        return null;
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
