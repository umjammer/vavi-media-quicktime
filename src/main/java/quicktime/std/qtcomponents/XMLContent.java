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

    XMLContent(int nativeRep) throws QTException {
        super(nativeRep, owner);
    }

    XMLContent(int ptr, int index) throws QTException {
        super(ptr + 20 * index, owner);
    }

    int getNthIdentifier(int index) {
        int i = 20 * index;
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

    private String getStringFromPointer(int ptr) {
        if (ptr != 0) {
            int i = 0;
            int j = 0;
            while (getByteFromPointer(ptr, j++) != 0) {
                i++;
            }
            byte[] cString = new byte[i];
            copyPointerToArray(ptr, 0, cString, 0, i);
            return QTUtils.CString2String(cString, 0);
        }
        return "";
    }

    private static byte getByteFromPointer(int ptr, int offset) {
        return MemoryManager.getByteFromPointer(ptr, offset);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }
}
