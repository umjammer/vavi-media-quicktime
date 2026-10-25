package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;

/**
 * #pragma options align=mac68k
 * struct XMLElement {
 * UInt32 							identifier;		// size 4		//	Tokenized identifier, if the element name was recognized by the parser
 * char *							name;			// size 4		//	Element name, only present if identifier == xmlIdentifierUnrecognized
 * XMLAttributePtr 			attributes;	// size 4		//	Array of attributes, terminated with an attribute with identifier == xmlIdentifierInvalid
 * XMLContentPtr 				contents;		// size 4		//	Array of contents, terminated with a content with kind == xmlIdentifierInvalid
 * }; // sizeof (XMLElement) = 16
 */
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

    /**
     * get tokenized identifier of the element
     * @throws if tokenized identifier is xmlIdentifierUnrecognized
     * @throws quicktime.std.StdQTException
     */
    public int getIdentifier() throws StdQTException {
        int i = getIntFromPointer(_ID(), 0);
        if (i != -1) {
            return i;
        }
        System.out.println("XMLElement.getIdentifier() called but is xmlIdentifierUnrecognized");
        throw new StdQTException(-50);
    }

    /**
     * was identifier unrecognized by XML parser
     */
    public boolean identifierUnrecognized() {
        int i = getIntFromPointer(_ID(), 0);
        return i == -1;
    }

    /**
     * get the name of the element if element was not recognized by the parser
     * @throws on tokenized identifier not xmlIdentifierUnrecognized
     * @throws quicktime.std.StdQTException
     * @throws quicktime.util.UtilException
     */
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

    /**
     * count of number of XMLAttribute items
     * @return count of attributes
     * @return {@link quicktime.std.StdQTException}
     * @return {@link quicktime.QTException}
     */
    public int countAttributes() throws StdQTException, QTException {
        int i = getIntFromPointer(_ID(), 8);
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
     * @return {@link quicktime.std.StdQTException}
     * @return {@link quicktime.QTException}
     */
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

    /**
     * count of number of XMLContent items
     * @return count of content items
     * @return {@link quicktime.std.StdQTException}
     * @return {@link quicktime.QTException}
     */
    public int countContents() throws StdQTException, QTException {
        int i = getIntFromPointer(_ID(), 12);
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
     * @return {@link quicktime.std.StdQTException}
     * @return {@link quicktime.QTException}
     */
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
