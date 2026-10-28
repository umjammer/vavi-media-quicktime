package quicktime.std.qtcomponents;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;


/**
 * The XMLContent implements java wrapper of pointer to QuickTime XMLContent
 */
public final class XMLContent extends QTObject implements PrimitivesLib, QuickTimeLib {

    private static final Object owner = new Object();

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

    /**
     * get type of content
     * @return StdQTConstants5.xmlContentTypeInvalid, StdQTConstants5.xmlContentTypeElement or StdQTConstants5.xmlContentTypeCharData
     * @throws quicktime.std.StdQTException
     */
    public int getKind() throws StdQTException {
        return getIntFromPointer(_ID(), 0);
    }

    /**
     * get element kind content
     * @return XMLElement if element kind is StdQTConstants5.xmlContentTypeElement
     * @throws quicktime.QTException throws on element kind not StdQTConstants5.xmlContentTypeElement
     */
    public XMLElement getElement() throws QTException {
        if (getKind() == 1) {
            int i = getIntFromPointer(_ID(), 4);
            return new XMLElement(i);
        }
        throw new StdQTException(-50);
    }

    /**
     * get string kind content
     * @return String if element kind is StdQTConstants5.xmlContentTypeCharData
     * @throws quicktime.std.StdQTException throws on element kind not StdQTConstants5.xmlContentTypeCharData
     * @throws quicktime.util.UtilException
     */
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

    private static byte getByteFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getByteFromPointer(paramInt1, paramInt2);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4);
    }
}
