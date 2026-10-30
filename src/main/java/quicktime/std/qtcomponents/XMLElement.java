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

    XMLElement(int nativeRep) throws QTException {
        super(nativeRep, owner);
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
            String name = getStringFromPointer(j);
            return name;
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
            XMLAttribute xmlAttribute = new XMLAttribute(i);
            for (int j = 0; ; j++) {
                if (xmlAttribute.getNthIdentifier(j) == 0) {
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
            XMLAttribute[] attributes = new XMLAttribute[i];
            int j = getIntFromPointer(_ID(), kAttributesOffset);
            for (int k = 0; k < i; k++) {
                attributes[k] = new XMLAttribute(j, k);
            }
            return attributes;
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
            XMLContent xmlContent = new XMLContent(i);
            for (int j = 0; ; j++) {
                if (xmlContent.getNthIdentifier(j) == 0) {
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
            XMLContent[] contents = new XMLContent[i];
            for (int k = 0; k < i; k++) {
                contents[k] = new XMLContent(j, k);
            }
            return contents;
        }
        return null;
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
