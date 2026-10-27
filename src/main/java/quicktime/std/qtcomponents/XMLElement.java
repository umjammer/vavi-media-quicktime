package quicktime.std.qtcomponents;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;


/**
 * <pre>
 * #pragma options align=mac68k
 * struct XMLElement {
 *   UInt32          identifier;  // size 4  // Tokenized identifier, if the element name was recognized by the parser
 *   char *          name;        // size 4  // Element name, only present if identifier == xmlIdentifierUnrecognized
 *   XMLAttributePtr attributes;  // size 4  // Array of attributes, terminated with an attribute with identifier == xmlIdentifierInvalid
 *   XMLContentPtr   contents;    // size 4  // Array of contents, terminated with a content with kind == xmlIdentifierInvalid
 * }; // sizeof (XMLElement) = 16
 * </pre>
 */
public final class XMLElement extends QTObject implements PrimitivesLib {

    private static final Logger logger = System.getLogger(XMLElement.class.getName());

    private static final Object owner = new Object();

    private static final int kXMLElementSize = 16;
    private static final int kIdentifierOffset = 0;
    private static final int kNameOffset = 4;
    private static final int kAttributesOffset = 8;
    private static final int kContentsOffset = 12;

    XMLElement(int paramInt) throws QTException {
        super(paramInt, owner);
    }

    /**
     * get tokenized identifier of the element
     * @throws if tokenized identifier is xmlIdentifierUnrecognized
     * @throws quicktime.std.StdQTException
     */
    public int getIdentifier() throws StdQTException {
        int i = getIntFromPointer(_ID(), kIdentifierOffset);
        if (i != -1) {
            return i;
        }
        logger.log(Level.DEBUG, "XMLElement.getIdentifier() called but is xmlIdentifierUnrecognized");
        throw new StdQTException(-50);
    }

    /**
     * was identifier unrecognized by XML parser
     */
    public boolean identifierUnrecognized() {
        int i = getIntFromPointer(_ID(), kIdentifierOffset);
        return i == -1;
    }

    /**
     * get the name of the element if element was not recognized by the parser
     * @throws quicktime.std.StdQTException throws on tokenized identifier not xmlIdentifierUnrecognized
     * @throws quicktime.util.UtilException
     */
    public String getName() throws StdQTException, UtilException {
        int i = getIntFromPointer(_ID(), kIdentifierOffset);
        if (i == -1) {
            int j = getIntFromPointer(_ID(), kNameOffset);
            String str = getStringFromPointer(j);
            return str;
        }
        logger.log(Level.DEBUG, "XMLElement.getName() called but not xmlIdentifierUnrecognized");
        throw new StdQTException(-50);
    }

    /**
     * count of number of XMLAttribute items
     * @return count of attributes
     * @throws quicktime.std.StdQTException
     * @throws quicktime.QTException
     */
    public int countAttributes() throws StdQTException, QTException {
        int i = getIntFromPointer(_ID(), kAttributesOffset);
        if (i != 0) {
            XMLAttribute localXMLAttribute = new XMLAttribute(i);
            for (int j = 0; ; j++) {
                if (localXMLAttribute.getNthIdentifier(j) == 0) {
                    return j;
                }
            }
        }
        return 0;
    }

    /**
     * get the attributes
     * @return array of XMLAttribute
     * @throws quicktime.std.StdQTException
     * @throws quicktime.QTException
     */
    public XMLAttribute[] getAttributes() throws StdQTException, QTException {
        int i = countAttributes();
        if (i != 0) {
            XMLAttribute[] arrayOfXMLAttribute = new XMLAttribute[i];
            int j = getIntFromPointer(_ID(), kAttributesOffset);
            for (int k = 0; k < i; k++) {
                arrayOfXMLAttribute[k] = new XMLAttribute(j, k);
            }
            return arrayOfXMLAttribute;
        }
        return null;
    }

    /**
     * count of number of XMLContent items
     * @return count of content items
     * @throws quicktime.std.StdQTException
     * @throws quicktime.QTException
     */
    public int countContents() throws StdQTException, QTException {
        int i = getIntFromPointer(_ID(), kContentsOffset);
        if (i != 0) {
            XMLContent localXMLContent = new XMLContent(i);
            for (int j = 0; ; j++) {
                if (localXMLContent.getNthIdentifier(j) == 0) {
                    return j;
                }
            }
        }
        return 0;
    }

    /**
     * get the content items of the XMLElement
     * @return array of XMLContent
     * @throws quicktime.std.StdQTException
     * @throws quicktime.QTException
     */
    public XMLContent[] getContents() throws StdQTException, QTException {
        int i = countContents();
        if (i != 0) {
            int j = getIntFromPointer(_ID(), kContentsOffset);
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
