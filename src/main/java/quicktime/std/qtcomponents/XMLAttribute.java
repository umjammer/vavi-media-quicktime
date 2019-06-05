
package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;


public final class XMLAttribute extends QTObject implements PrimitivesLib, QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(XMLAttribute.class);
                    }
                });
            }
        }.establish();
    }

    private static Object owner = new Object();

    private static final boolean debug = false;

    private static final int kXMLAttributeSize = 22;
    private static final int kIdentifierOffset = 0;
    private static final int kNameOffset = 4;
    private static final int kValueKindOffset = 8;
    private static final int kValueOffset = 12;
    private static final int kValueStrOffset = 18;

    XMLAttribute(int paramInt) throws QTException {
        super(paramInt, new Object());
    }

    XMLAttribute(int paramInt1, int paramInt2) throws QTException {
        super(paramInt1 + 22 * paramInt2, owner);
    }

    public boolean identifierUnrecognized() {
        int i = getIntFromPointer(_ID(), 0);
        return i == -1;
    }

    public int getIdentifier() throws StdQTException {
        int i = getIntFromPointer(_ID(), 0);
        if (i != -1) {
            return i;
        }
        throw new StdQTException(-50);
    }

    int getNthIdentifier(int paramInt) {
        int i = getIntFromPointer(_ID(), 22 * paramInt);
        return i;
    }

    public String getName() throws StdQTException, UtilException {
        int i = getIntFromPointer(_ID(), 0);
        if (i == -1) {
            int j = getIntFromPointer(_ID(), 4);
            return getStringFromPointer(j);
        }

        throw new StdQTException(-50);
    }

    public int getValueKind() throws StdQTException {
        int i = getIdentifier();
        int j = getIntFromPointer(_ID(), 8);
        return j;
    }

    public String getValueString() throws StdQTException, UtilException {
        int i = getIntFromPointer(_ID(), 18);
        return getStringFromPointer(i);
    }

    public String getCharString() throws StdQTException {
        int i = getValueKind();
        if (i == 0) {
            int j = getIntFromPointer(_ID(), 12);
            return getStringFromPointer(j);
        }
        throw new StdQTException("XMLAttribute.getCharString() : kind not attributeValueKindCharString");
    }

    public int getInteger() throws StdQTException {
        int i = getValueKind();
        if (i == 1) {
            return getIntFromPointer(_ID(), 12);
        }
        throw new StdQTException("XMLAttribute.getInteger() : kind not attributeValueKindInteger");
    }

    public int getPercent() throws StdQTException {
        int i = getValueKind();
        if (i == 2) {
            return getIntFromPointer(_ID(), 12);
        }
        throw new StdQTException("XMLAttribute.getPercent() : kind not attributeValueKindPercent");
    }

    public boolean getBoolean() throws StdQTException {
        int i = getValueKind();
        if (i == 4) {
            return getIntFromPointer(_ID(), 12) != 0;
        }
        throw new StdQTException("XMLAttribute.getBoolean() : kind not attributeValueKindBoolean");
    }

    public boolean getOnOff() throws StdQTException {
        int i = getValueKind();
        if (i == 8) {
            return getIntFromPointer(_ID(), 12) != 0;
        }
        throw new StdQTException("XMLAttribute.getOnOff() : kind not attributeValueKindOnOff");
    }

    public QDColor getRGBColor() throws StdQTException {
        int i = getValueKind();
        if (i == 16) {
            byte[] arrayOfByte = new byte[12];
            for (int j = 0; j < 12; j++) {
                arrayOfByte[j] = getByteFromPointer(_ID(), 12 + j);
            }
            return QDColor.fromArray(arrayOfByte, 6);
        }

        throw new StdQTException(-50);
    }

    public int getEnum() throws StdQTException {
        int i = getValueKind();
        switch (i) {
        case 32:
        case 64:
            return getIntFromPointer(_ID(), 12);
        }

        throw new StdQTException(-50);
    }

    private String getStringFromPointer(int paramInt) {
        if (paramInt != 0) {
            int i = 0;
            int j = 0;
            while (getByteFromPointer(paramInt, j++) != 0)
                i++;
            byte[] arrayOfByte = new byte[i];
            copyPointerToArray(paramInt, 0, arrayOfByte, 0, i);
            return QTUtils.CString2String(arrayOfByte, 0);
        }
        return "";
    }

    private static native byte getByteFromPointer(int paramInt1, int paramInt2);

    private static native short getShortFromPointer(int paramInt1, int paramInt2);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  byte[] paramArrayOfByte,
                                                  int paramInt3,
                                                  int paramInt4);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/XMLAttribute.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
