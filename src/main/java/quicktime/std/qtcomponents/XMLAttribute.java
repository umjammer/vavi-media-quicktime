package quicktime.std.qtcomponents;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;


/**
 * The XMLAttribute corresponds to the QuickTime XMLAttribute
 */
public final class XMLAttribute extends QTObject implements PrimitivesLib, QuickTimeLib {

    private static final Object owner = new Object();

    private static final int kXMLAttributeSize = 22;
    private static final int kIdentifierOffset = 0;
    private static final int kNameOffset = 4;
    private static final int kValueKindOffset = 8;
    private static final int kValueOffset = 12;
    private static final int kValueStrOffset = 18;

    XMLAttribute(int nativeRep) throws QTException {
        super(nativeRep, new Object());
    }

    XMLAttribute(int ptr, int index) throws QTException {
        super(ptr + 22 * index, owner);
    }

    /**
     * returns true if the attribute name of the XMLAttribute was not recognized by the parser
     */
    public boolean identifierUnrecognized() {
        int i = getIntFromPointer(_ID(), 0);
        return i == -1;
    }

    /**
     * returns the tokenized identifier of the XMLAttribute if the attribute name was recognized by the parser
     * @throws if tokenized identifier is xmlIdentifierUnrecognized
     * @throws quicktime.std.StdQTException
     */
    public int getIdentifier() throws StdQTException {
        int i = getIntFromPointer(_ID(), 0);
        if (i != -1) {
            return i;
        }
        throw new StdQTException(-50);
    }

    int getNthIdentifier(int index) {
        int i = getIntFromPointer(_ID(), 22 * index);
        return i;
    }

    /**
     * returns the attribute name of the XMLAttribute if not recognized by the parser
     * @throws if tokenized identifier is not xmlIdentifierUnrecognized
     * @throws quicktime.std.StdQTException
     * @throws quicktime.util.UtilException
     */
    public String getName() throws StdQTException, UtilException {
        int i = getIntFromPointer(_ID(), 0);
        if (i == -1) {
            int j = getIntFromPointer(_ID(), 4);
            return getStringFromPointer(j);
        }
        throw new StdQTException(-50);
    }

    /**
     * returns the valueKind of the XMLAttribute
     * should be called before a getter for a particular type
     */
    public int getValueKind() throws StdQTException {
        int i = getIdentifier();
        int j = getIntFromPointer(_ID(), 8);
        return j;
    }

    /**
     * returns the valueString of the XMLAttribute
     * always available
     * {@link quicktime.std.StdQTException}
     */
    public String getValueString() throws StdQTException, UtilException {
        int i = getIntFromPointer(_ID(), 18);
        return getStringFromPointer(i);
    }

    /**
     * returns the string of the XMLAttribute for valueKind attributeValueKindCharString
     * @throws if value kind is not attributeValueKindCharString
     * @throws quicktime.std.StdQTException
     */
    public String getCharString() throws StdQTException {
        int i = getValueKind();
        if (i == 0) {
            int j = getIntFromPointer(_ID(), 12);
            return getStringFromPointer(j);
        }
        throw new StdQTException("XMLAttribute.getCharString() : kind not attributeValueKindCharString");
    }

    /**
     * returns the int value of the XMLAttribute for valueKind attributeValueKindInteger
     * @throws if value kind is not attributeValueKindInteger
     * @throws quicktime.std.StdQTException
     */
    public int getInteger() throws StdQTException {
        int i = getValueKind();
        if (i == 1) {
            return getIntFromPointer(_ID(), 12);
        }
        throw new StdQTException("XMLAttribute.getInteger() : kind not attributeValueKindInteger");
    }

    /**
     * jcs* Is this a integer percent or some weird Fixed / float rep ???
     * returns the percent value of the XMLAttribute for valueKind attributeValueKindPercent
     * @throws if value kind is not attributeValueKindPercent
     * @throws quicktime.std.StdQTException
     */
    public int getPercent() throws StdQTException {
        int i = getValueKind();
        if (i == 2) {
            return getIntFromPointer(_ID(), 12);
        }
        throw new StdQTException("XMLAttribute.getPercent() : kind not attributeValueKindPercent");
    }

    /**
     * returns the boolean value of the XMLAttribute for valueKind attributeValueKindBoolean
     * @throws if value kind is not attributeValueKindBoolean
     * @throws quicktime.std.StdQTException
     */
    public boolean getBoolean() throws StdQTException {
        int i = getValueKind();
        if (i == 4) {
            return getIntFromPointer(_ID(), 12) != 0;
        }
        throw new StdQTException("XMLAttribute.getBoolean() : kind not attributeValueKindBoolean");
    }

    /**
     * returns the boolean value of the XMLAttribute for valueKind attributeValueKindOnOff
     * @throws if value kind is not attributeValueKindOnOff
     * @throws quicktime.std.StdQTException
     */
    public boolean getOnOff() throws StdQTException {
        int i = getValueKind();
        if (i == 8) {
            return getIntFromPointer(_ID(), 12) != 0;
        }
        throw new StdQTException("XMLAttribute.getOnOff() : kind not attributeValueKindOnOff");
    }

    /**
     * returns a QDColor of the XMLAttribute for value kind attributeValueKindColor
     * @throws if value kind is not attributeValueKindColor
     * @throws quicktime.std.StdQTException
     */
    public QDColor getRGBColor() throws StdQTException {
        int i = getValueKind();
        if (i == 16) {
            byte[] colorBytes = new byte[12];
            for (int j = 0; j < 12; j++) {
                colorBytes[j] = getByteFromPointer(_ID(), 12 + j);
            }
            return QDColor.fromArray(colorBytes, 6);
        }
        throw new StdQTException(-50);
    }

    /**
     * returns the enumerated value of the XMLAttribute for value kind attributeValueKindEnum or
     * @throws if value kind is not attributeValueKindEnum nor attributeValueKindCaseSensEnum
     * @throws quicktime.std.StdQTException
     */
    public int getEnum() throws StdQTException {
        int i = getValueKind();
        return switch (i) {
            case 32, 64 -> getIntFromPointer(_ID(), 12);
            default -> throw new StdQTException(-50);
        };
    }

    private String getStringFromPointer(int ptr) {
        if (ptr != 0) {
            int i = 0;
            int j = 0;
            while (getByteFromPointer(ptr, j++) != 0) i++;
            byte[] cString = new byte[i];
            copyPointerToArray(ptr, 0, cString, 0, i);
            return QTUtils.CString2String(cString, 0);
        }
        return "";
    }

    private static byte getByteFromPointer(int ptr, int offset) {
        return MemoryManager.getByteFromPointer(ptr, offset);
    }

    private static short getShortFromPointer(int ptr, int offset) {
        return MemoryManager.getShortFromPointer(ptr, offset);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }
}
