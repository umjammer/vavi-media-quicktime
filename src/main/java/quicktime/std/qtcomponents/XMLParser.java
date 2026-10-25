package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.movies.media.DataRef;
import quicktime.util.QTUtils;

/**
 * The XMLParser corresponds to the QuickTime XMLParse component, type 'pars' subtype 'xml '
 */
public final class XMLParser extends Component implements QuickTimeLib {

    private static Object linkage;

    private XMLDispatcher startDocUPP;

    private XMLDispatcher endDocUPP;

    private XMLDispatcher startElementUPP;

    private XMLDispatcher endElementUPP;

    private XMLDispatcher commentUPP;

    private XMLDispatcher charDataUPP;

    private XMLDispatcher preprocessorUPP;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(XMLParser.class);
                    }
                });
            }
        }.establish();
    }

    public XMLParser() throws QTException {
        super(1885434483, 2020437024);
    }

    /**
     * parses the XML file pointed to by dataRef, returning a XMLParseTree parse tree
     * QuickTime::XMLParseDataRef
     * @param allowUppercase to set StdQTConstants5.xmlParseFlagAllowUppercase flag
     * @param allowUnquotedAttributeValues to set StdQTConstants5.xmlParseFlagAllowUnquotedAttributeValues flag
     * @return XMLParseTree
     * @return {@link quicktime.std.StdQTException}
     * @return {@link quicktime.QTException}
     */
    public XMLDoc parse(DataRef paramDataRef, boolean paramBoolean1, boolean paramBoolean2) throws StdQTException, QTException {
        int i = 0;
        if (paramBoolean1)
            i |= 0x1;
        if (paramBoolean2)
            i |= 0x2;
        int[] arrayOfInt = { 0 };
        int j = XMLParseDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), i, arrayOfInt);
        StdQTException.checkError(j);
        return new XMLDoc(arrayOfInt[0], this);
    }

    /**
     * Parses the XML file pointed to by file, returning a XMLDoc parse tree
     * QuickTime::XMLParseFile
     * @param file open for read
     * @param allowUppercase to set StdQTConstants5.xmlParseFlagAllowUppercase flag
     * @param allowUnquotedAttributeValues to set StdQTConstants5.xmlParseFlagAllowUnquotedAttributeValues flag
     * @return XMLDoc
     * @return {@link quicktime.std.StdQTException}
     * @return {@link quicktime.QTException}
     */
    public XMLDoc parse(QTFile paramQTFile, boolean paramBoolean1, boolean paramBoolean2) throws StdQTException, QTException {
        int i = 0;
        if (paramBoolean1)
            i |= 0x1;
        if (paramBoolean2)
            i |= 0x2;
        int[] arrayOfInt = { 0 };
        int j = XMLParseFile(_ID(), paramQTFile.getFSSpec(true, 256), i, arrayOfInt);
        StdQTException.checkError(j);
        return new XMLDoc(arrayOfInt[0], this);
    }

    /**
     * Parses the XML file pointed to by dataRef, using event-based parsing. Does not return a parse tree
     * QuickTime::XMLParseDataRef
     * @param allowUppercase to set StdQTConstants5.xmlParseFlagAllowUppercase flag
     * @param allowUnquotedAttributeValues to set StdQTConstants5.xmlParseFlagAllowUnquotedAttributeValues flag
     * @param {@link quicktime.std.StdQTException}
     * @param {@link quicktime.QTException}
     */
    public void eventParse(DataRef paramDataRef, boolean paramBoolean1, boolean paramBoolean2) throws StdQTException, QTException {
        int i = 4;
        if (paramBoolean1)
            i |= 0x1;
        if (paramBoolean2)
            i |= 0x2;
        int j = XMLParseDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), i, null);
        StdQTException.checkError(j);
    }

    /**
     * Parses the XML file pointed to by file, using event-based parsing. Does not return a parse tree
     * QuickTime::XMLParseFile
     * @param file open for read
     * @param allowUppercase to set StdQTConstants5.xmlParseFlagAllowUppercase flag
     * @param allowUnquotedAttributeValues to set StdQTConstants5.xmlParseFlagAllowUnquotedAttributeValues flag
     * @param {@link quicktime.std.StdQTException}
     * @param {@link quicktime.QTException}
     */
    public void eventParse(QTFile paramQTFile, boolean paramBoolean1, boolean paramBoolean2) throws StdQTException, QTException {
        int i = 4;
        if (paramBoolean1)
            i |= 0x1;
        if (paramBoolean2)
            i |= 0x2;
        int j = XMLParseFile(_ID(), paramQTFile.getFSSpec(true, 256), i, null);
        StdQTException.checkError(j);
    }

    /**
     * tell the parser of an element to be recognized in the specified namespace
     * QuickTime::XMLParseAddElement
     * @param elementName name
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param elementID tokenized identifier, zero or unique identifier
     * @param elementFlags flags
     * @return tokenized element identifier  if elementID non zero, is elementID else generated unique non zero element identifier
     * @return {@link quicktime.std.StdQTException}
     */
    public int addElement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int[] arrayOfInt = { paramInt2 };
        int i = XMLParseAddElement(_ID(), QTUtils.String2CString(paramString), paramInt1, arrayOfInt, paramInt3);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * tell the parser of an attribute for the specified element in the specified namespace
     * QuickTime::XMLParseAddAttribute
     * @param elementID non zero unique tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @return attribute identifier token  if attributeID non zero, is elementID else generated unique non zero attribute identifier
     * @throws on zero elementID
     * @throws quicktime.std.StdQTException
     */
    public int addAttribute(int paramInt1, int paramInt2, String paramString, int paramInt3) throws StdQTException {
        if (paramInt1 == 0)
            throw new StdQTException(-50);
        int[] arrayOfInt = { paramInt3 };
        int i = XMLParseAddAttribute(_ID(), paramInt1, paramInt2, QTUtils.String2CString(paramString), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional kind of value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * QuickTime::XMLParseAddAttributeValueKind
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeValueKind -
     * @param attributeValueKindInfo -
     * @throws on zero elementID
     * @throws on zero attributeID
     * @throws quicktime.std.StdQTException
     */
    public void addAttributeValueKind(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) throws StdQTException {
        if (paramInt1 == 0)
            throw new StdQTException(-50);
        if (paramInt2 == 0)
            throw new StdQTException(-50);
        int i = XMLParseAddAttributeValueKind(_ID(), paramInt1, paramInt2, paramInt3, paramArrayOfByte);
        StdQTException.checkError(i);
    }

    /**
     * tell the parser of an enumerated attribute, of type attributeValueKindCharString, for the specified element
     * QuickTime::XMLParseAddAttributeAndValue
     * @param elementID non zero tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeStringValue String
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws on zero elementID
     * @throws quicktime.std.StdQTException
     */
    public void addAttributeValueKind(int paramInt1, int paramInt2, String paramString) throws StdQTException {
        addAttributeValueKind(paramInt1, paramInt2, 0, QTUtils.String2CString(paramString));
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindInteger value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * QuickTime::XMLParseAddAttributeValueKind
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeValue int
     * @throws on zero elementID
     * @throws on zero attributeID
     * @throws quicktime.std.StdQTException
     */
    public void addAttributeValueKind(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        byte[] arrayOfByte = { 0, 0, 0, 0 };
        for (int i = 0; i < 4; paramInt3 >>= 8) {
            arrayOfByte[i] = ((byte) (paramInt3 & 0xFF));
            i++;
        }
        addAttributeValueKind(paramInt1, paramInt2, 1, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindPercent value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * QuickTime::XMLParseAddAttributeValueKind
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @throws on zero elementID
     * @throws on zero attributeID
     * @throws quicktime.std.StdQTException
     */
    public void addAttributeValueKind(int paramInt1, int paramInt2, float paramFloat) throws StdQTException {
        int i = (int) paramFloat;
        byte[] arrayOfByte = { 0, 0, 0, 0 };
        for (int j = 0; j < 4; i >>= 8) {
            arrayOfByte[j] = ((byte) (i & 0xFF));
            j++;
        }
        addAttributeValueKind(paramInt1, paramInt2, 2, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindBoolean value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * QuickTime::XMLParseAddAttributeValueKind
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @throws on zero elementID
     * @throws on zero attributeID
     * @throws quicktime.std.StdQTException
     */
    public void addAttributeValueKind(int paramInt1, int paramInt2, boolean paramBoolean) throws StdQTException {
        byte[] arrayOfByte = { (byte) (paramBoolean ? 1 : 0) };
        addAttributeValueKind(paramInt1, paramInt2, 4, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindOnOff value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * QuickTime::XMLParseAddAttributeValueKind
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @throws on zero elementID
     * @throws on zero attributeID
     * @throws quicktime.std.StdQTException
     */
    public void addAttributeValueKind(int paramInt1, int paramInt2, byte paramByte) throws StdQTException {
        byte[] arrayOfByte = { (byte) (paramByte != 0 ? 1 : 0) };
        addAttributeValueKind(paramInt1, paramInt2, 8, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindColor value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * QuickTime::XMLParseAddAttributeValueKind
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeColorValue qd.QDColor
     * @throws on zero elementID
     * @throws on zero attributeID
     * @throws quicktime.std.StdQTException
     */
    public void addAttributeValueKind(int paramInt1, int paramInt2, QDColor paramQDColor) throws StdQTException {
        addAttributeValueKind(paramInt1, paramInt2, 16, paramQDColor.getRGBColor());
    }

    /**
     * tell the parser of an enumerated attribute, of type attributeValueKindEnum or attributeValueKindCaseSensEnum, for the specified element
     * QuickTime::XMLParseAddAttributeAndValue
     * @param elementID non zero tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeList String[] of attributes
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws on zero elementID
     * @throws quicktime.std.StdQTException
     */
    public void addAttributeValueKind(int paramInt1, int paramInt2, String[] paramArrayOfString, boolean paramBoolean) throws StdQTException {
        int i = paramArrayOfString.length;
        int j = 0;
        for (int k = 0; k < i; k++) {
            j += paramArrayOfString[k].length();
            j++;
        }
        j++;
        byte[] arrayOfByte = new byte[j];
        int m = 0;
        for (int n = 0; n < i; n++) {
            j = paramArrayOfString[n].length();
            for (int i1 = 0; i1 < j; i1++) {
                arrayOfByte[(m++)] = ((byte) paramArrayOfString[n].charAt(i1));
            }
            arrayOfByte[(m++)] = 0;
        }
        arrayOfByte[m] = 0;
        int n = paramBoolean ? 64 : 32;
        addAttributeValueKind(paramInt1, paramInt2, n, arrayOfByte);
    }

    /**
     * tell the parser of an attribute, which may have a particular type of value, for the specified element
     * QuickTime::XMLParseAddAttributeAndValue
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeValueKind -
     * @param attributeValueKindInfo -
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws on zero elementID
     * @throws quicktime.std.StdQTException
     */
    public int addAttributeAndValue(int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4, byte[] paramArrayOfByte) throws StdQTException {
        if (paramInt1 == 0)
            throw new StdQTException(-50);
        int[] arrayOfInt = { paramInt3 };
        int i = XMLParseAddAttributeAndValue(_ID(), paramInt1, paramInt2, QTUtils.String2CString(paramString), arrayOfInt, paramInt4, paramArrayOfByte);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * tell the parser of an attribute, of type attributeValueKindCharString, for the specified element
     * QuickTime::XMLParseAddAttributeAndValue
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeValue String
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws on zero elementID
     * @throws quicktime.std.StdQTException
     */
    public int addAttributeAndValue(int paramInt1, int paramInt2, String paramString1, int paramInt3, String paramString2) throws StdQTException {
        return addAttributeAndValue(paramInt1, paramInt2, paramString1, paramInt3, 0, QTUtils.String2CString(paramString2));
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindInteger value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * QuickTime::XMLParseAddAttributeValueKind
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws on zero elementID
     * @throws quicktime.std.StdQTException
     */
    public int addAttributeAndValue(int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4) throws StdQTException {
        byte[] arrayOfByte = { 0, 0, 0, 0 };
        for (int i = 0; i < 4; paramInt4 >>= 8) {
            arrayOfByte[i] = ((byte) (paramInt4 & 0xFF));
            i++;
        }
        return addAttributeAndValue(paramInt1, paramInt2, paramString, paramInt3, 1, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindPercent value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * QuickTime::XMLParseAddAttributeValueKind
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws on zero elementID
     * @throws quicktime.std.StdQTException
     */
    public int addAttributeAndValue(int paramInt1, int paramInt2, String paramString, int paramInt3, float paramFloat) throws StdQTException {
        int i = (int) paramFloat;
        byte[] arrayOfByte = { 0, 0, 0, 0 };
        for (int j = 0; j < 4; i >>= 8) {
            arrayOfByte[j] = ((byte) (i & 0xFF));
            j++;
        }
        return addAttributeAndValue(paramInt1, paramInt2, paramString, paramInt3, 2, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindBoolean value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * QuickTime::XMLParseAddAttributeValueKind
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws on zero elementID
     * @throws quicktime.std.StdQTException
     */
    public int addAttributeAndValue(int paramInt1, int paramInt2, String paramString, int paramInt3, boolean paramBoolean) throws StdQTException {
        byte[] arrayOfByte = { (byte) (paramBoolean ? 1 : 0) };
        return addAttributeAndValue(paramInt1, paramInt2, paramString, paramInt3, 4, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindOnOff value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * QuickTime::XMLParseAddAttributeValueKind
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws on zero elementID
     * @throws quicktime.std.StdQTException
     */
    public int addAttributeAndValue(int paramInt1, int paramInt2, String paramString, int paramInt3, byte paramByte) throws StdQTException {
        byte[] arrayOfByte = { (byte) (paramByte != 0 ? 1 : 0) };
        return addAttributeAndValue(paramInt1, paramInt2, paramString, paramInt3, 8, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindColor value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * QuickTime::XMLParseAddAttributeValueKind
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeColorValue qd.QDColor
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws on zero elementID
     * @throws quicktime.std.StdQTException
     */
    public int addAttributeAndValue(int paramInt1, int paramInt2, String paramString, int paramInt3, QDColor paramQDColor) throws StdQTException {
        return addAttributeAndValue(paramInt1, paramInt2, paramString, paramInt3, 16, paramQDColor.getRGBColor());
    }

    /**
     * tell the parser of an enumerated attribute, of type attributeValueKindEnum or attributeValueKindCaseSensEnum, for the specified element
     * QuickTime::XMLParseAddAttributeAndValue
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeList String[] of attributes
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws on zero elementID
     * @throws quicktime.std.StdQTException
     */
    public int addAttributeAndValue(int paramInt1, int paramInt2, String paramString, int paramInt3, String[] paramArrayOfString, boolean paramBoolean) throws StdQTException {
        int i = paramArrayOfString.length;
        int j = 0;
        for (int k = 0; k < i; k++) {
            j += paramArrayOfString[k].length();
            j++;
        }
        j++;
        byte[] arrayOfByte = new byte[j];
        int m = 0;
        for (int n = 0; n < i; n++) {
            j = paramArrayOfString[n].length();
            for (int i1 = 0; i1 < j; i1++) {
                arrayOfByte[(m++)] = ((byte) paramArrayOfString[n].charAt(i1));
            }
            arrayOfByte[(m++)] = 0;
        }
        arrayOfByte[m] = 0;
        int n = paramBoolean ? 64 : 32;
        return addAttributeAndValue(paramInt1, paramInt2, paramString, paramInt3, n, arrayOfByte);
    }

    /**
     * tell the parser of a namespace to be recognized
     * QuickTime::XMLParseAddNameSpace
     * @param nameSpaceID non zero tokenized namespace identifier
     * @throws on zero nameSpaceID
     * @throws quicktime.std.StdQTException
     */
    public void addNameSpace(int paramInt, String paramString) throws StdQTException {
        if (paramInt == 0)
            throw new StdQTException(-50);
        int[] arrayOfInt = { paramInt };
        int i = XMLParseAddNameSpace(_ID(), QTUtils.String2CString(paramString), arrayOfInt);
        StdQTException.checkError(i);
    }

    /**
     * tell the parser of an identified namespace to be recognized
     * QuickTime::XMLParseAddNameSpace
     * @return generated non zero tokenized name space identifier
     * @return {@link quicktime.std.StdQTException}
     */
    public int addNameSpace(String paramString) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = XMLParseAddNameSpace(_ID(), QTUtils.String2CString(paramString), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * specify the offset and limit for reading from the dataref to be used when parsing
     * QuickTime::XMLParseSetOffsetAndLimit
     */
    public void setOffsetAndLimit(int paramInt1, int paramInt2) throws StdQTException {
        int i = XMLParseSetOffsetAndLimit(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * Set the callback handler for start document events
     * QuickTime::XMLParseSetStartDocumentHandler
     * @param proc implements XMLStartDocProc
     * @param {@link quicktime.std.StdQTException}
     */
    public void setStartDocumentHandler(XMLStartDocProc paramXMLStartDocProc) throws StdQTException {
        if (paramXMLStartDocProc == null) {
            removeStartDocumentHandler();
            return;
        }
        if (this.startDocUPP != null) {
            removeStartDocumentHandler();
        }
        this.startDocUPP = new XMLDispatcher(paramXMLStartDocProc);
        int i = XMLParseSetStartDocumentHandler(_ID(), this.startDocUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered callback handler for start document events
     * QuickTime::XMLParseSetStartDocumentHandler
     */
    public void removeStartDocumentHandler() throws StdQTException {
        int i = XMLParseSetStartDocumentHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.startDocUPP != null) {
            this.startDocUPP.cleanup();
            this.startDocUPP = null;
        }
    }

    /**
     * Set the callback handler for end document events
     * QuickTime::XMLParseSetEndDocumentHandler
     * @param proc implements XMLEndDocProc
     * @param {@link quicktime.std.StdQTException}
     */
    public void setEndDocumentHandler(XMLEndDocProc paramXMLEndDocProc) throws StdQTException {
        if (paramXMLEndDocProc == null) {
            removeEndDocumentHandler();
            return;
        }
        if (this.endDocUPP != null) {
            removeEndDocumentHandler();
        }
        this.endDocUPP = new XMLDispatcher(paramXMLEndDocProc);
        int i = XMLParseSetEndDocumentHandler(_ID(), this.endDocUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered callback handler for end document events
     * QuickTime::XMLParseSetEndDocumentHandler
     */
    public void removeEndDocumentHandler() throws StdQTException {
        int i = XMLParseSetEndDocumentHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.endDocUPP != null) {
            this.endDocUPP.cleanup();
            this.endDocUPP = null;
        }
    }

    /**
     * set the start element handler UPP for event parsing
     * QuickTime::XMLParseSetStartElementHandler
     * @param proc implements XMLStartElementProc
     * @param {@link quicktime.std.StdQTException}
     */
    public void setStartElementHandler(XMLStartElementProc paramXMLStartElementProc) throws StdQTException {
        if (paramXMLStartElementProc == null) {
            removeStartElementHandler();
            return;
        }
        if (this.startElementUPP != null) {
            removeStartElementHandler();
        }
        this.startElementUPP = new XMLDispatcher(paramXMLStartElementProc);
        int i = XMLParseSetStartElementHandler(_ID(), this.startElementUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered start element proc.
     * QuickTime::XMLParseSetStartElementHandler
     */
    public void removeStartElementHandler() throws StdQTException {
        int i = XMLParseSetStartElementHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.startElementUPP != null) {
            this.startElementUPP.cleanup();
            this.startElementUPP = null;
        }
    }

    /**
     * set the end element handler UPP for event parsing
     * QuickTime::XMLParseSetEndElementHandler
     * @param proc implements XMLEndElementProc
     * @param {@link quicktime.std.StdQTException}
     */
    public void setEndElementHandler(XMLEndElementProc paramXMLEndElementProc) throws StdQTException {
        if (paramXMLEndElementProc == null) {
            removeEndElementHandler();
            return;
        }
        if (this.endElementUPP != null) {
            removeEndElementHandler();
        }
        this.endElementUPP = new XMLDispatcher(paramXMLEndElementProc);
        int i = XMLParseSetEndElementHandler(_ID(), this.endElementUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered end element proc.
     * QuickTime::XMLParseSetEndElementHandler
     */
    public void removeEndElementHandler() throws StdQTException {
        int i = XMLParseSetEndElementHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.endElementUPP != null) {
            this.endElementUPP.cleanup();
            this.endElementUPP = null;
        }
    }

    /**
     * Set the callback handler for comment events
     * QuickTime::XMLParseSetCommentHandler
     */
    public void setCommentHandler(XMLCommentProc paramXMLCommentProc) throws StdQTException {
        if (paramXMLCommentProc == null) {
            removeCommentHandler();
            return;
        }
        if (this.commentUPP != null) {
            removeCommentHandler();
        }
        this.commentUPP = new XMLDispatcher(paramXMLCommentProc);
        int i = XMLParseSetCommentHandler(_ID(), this.commentUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered callback handler for comment events
     * QuickTime::XMLParseSetCommentHandler
     */
    public void removeCommentHandler() throws StdQTException {
        int i = XMLParseSetCommentHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.commentUPP != null) {
            this.commentUPP.cleanup();
            this.commentUPP = null;
        }
    }

    /**
     * Set the callback handler for character data events
     * QuickTime::XMLParseSetCharDataHandler
     * @param proc implements XMLCharDataProc
     * @param {@link quicktime.std.StdQTException}
     */
    public void setCharDataHandler(XMLCharDataProc paramXMLCharDataProc) throws StdQTException {
        if (paramXMLCharDataProc == null) {
            removeCharDataHandler();
            return;
        }
        if (this.charDataUPP != null) {
            removeCharDataHandler();
        }
        this.charDataUPP = new XMLDispatcher(paramXMLCharDataProc);
        int i = XMLParseSetCharDataHandler(_ID(), this.charDataUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered callback handler for character data events.
     * QuickTime::XMLParseSetCharDataHandler
     */
    public void removeCharDataHandler() throws StdQTException {
        int i = XMLParseSetCharDataHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.charDataUPP != null) {
            this.charDataUPP.cleanup();
            this.charDataUPP = null;
        }
    }

    /**
     * Set the callback handler for preprocessor instruction events
     * QuickTime::XMLParseSetPreprocessInstructionHandler
     * @param proc implements XMLPreprocessorProc
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPreprocessorHandler(XMLPreprocessorProc paramXMLPreprocessorProc) throws StdQTException {
        if (paramXMLPreprocessorProc == null) {
            removePreprocessorHandler();
            return;
        }
        if (this.preprocessorUPP != null) {
            removePreprocessorHandler();
        }
        this.preprocessorUPP = new XMLDispatcher(paramXMLPreprocessorProc);
        int i = XMLParseSetPreprocessInstructionHandler(_ID(), this.preprocessorUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered callback handler for preprocessor instruction events
     * QuickTime::XMLParseSetPreprocessInstructionHandler
     */
    public void removePreprocessorHandler() throws StdQTException {
        int i = XMLParseSetPreprocessInstructionHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.preprocessorUPP != null) {
            this.preprocessorUPP.cleanup();
            this.preprocessorUPP = null;
        }
    }

    /**
     * Returns the line number the error if a file failed to parse properly
     * QuickTime::XMLParseGetDetailedParseError
     * @return line number of the error
     * @return {@link quicktime.std.StdQTException}
     */
    public int getParseErrorLine() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = XMLParseGetDetailedParseError(_ID(), arrayOfInt, null);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Returns a more detailed description of the error if a file failed to parse properly
     * QuickTime::XMLParseGetDetailedParseError
     * @return description of error
     * @return {@link quicktime.std.StdQTException}
     */
    public String getParseErrorDescription() throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int i = XMLParseGetDetailedParseError(_ID(), null, arrayOfByte);
        StdQTException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    private static short XMLParseDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        return ComponentManager.XMLParseDataRef(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
    }

    private static short XMLParseFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt) {
        return ComponentManager.XMLParseFile(paramInt1, paramArrayOfByte, paramInt2, paramArrayOfInt);
    }

    private static short XMLParseGetDetailedParseError(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte) {
        return ComponentManager.XMLParseGetDetailedParseError(paramInt, paramArrayOfInt, paramArrayOfByte);
    }

    private static short XMLParseAddElement(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
        return ComponentManager.XMLParseAddElement(paramInt1, paramArrayOfByte, paramInt2, paramArrayOfInt, paramInt3);
    }

    private static short XMLParseAddAttribute(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return ComponentManager.XMLParseAddAttribute(paramInt1, paramInt2, paramInt3, paramArrayOfByte, paramArrayOfInt);
    }

    private static short XMLParseAddAttributeAndValue(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, int[] paramArrayOfInt, int paramInt4, byte[] paramArrayOfByte2) {
        return ComponentManager.XMLParseAddAttributeAndValue(paramInt1, paramInt2, paramInt3, paramArrayOfByte1, paramArrayOfInt, paramInt4, paramArrayOfByte2);
    }

    private static short XMLParseAddAttributeValueKind(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte) {
        return ComponentManager.XMLParseAddAttributeValueKind(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte);
    }

    private static short XMLParseAddNameSpace(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return ComponentManager.XMLParseAddNameSpace(paramInt, paramArrayOfByte, paramArrayOfInt);
    }

    private static short XMLParseSetOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3) {
        return ComponentManager.XMLParseSetOffsetAndLimit(paramInt1, paramInt2, paramInt3);
    }

    private static short XMLParseSetStartDocumentHandler(int paramInt1, int paramInt2) {
        return ComponentManager.XMLParseSetStartDocumentHandler(paramInt1, paramInt2);
    }

    private static short XMLParseSetEndDocumentHandler(int paramInt1, int paramInt2) {
        return ComponentManager.XMLParseSetEndDocumentHandler(paramInt1, paramInt2);
    }

    private static short XMLParseSetStartElementHandler(int paramInt1, int paramInt2) {
        return ComponentManager.XMLParseSetStartElementHandler(paramInt1, paramInt2);
    }

    private static short XMLParseSetEndElementHandler(int paramInt1, int paramInt2) {
        return ComponentManager.XMLParseSetEndElementHandler(paramInt1, paramInt2);
    }

    private static short XMLParseSetCharDataHandler(int paramInt1, int paramInt2) {
        return ComponentManager.XMLParseSetCharDataHandler(paramInt1, paramInt2);
    }

    private static short XMLParseSetPreprocessInstructionHandler(int paramInt1, int paramInt2) {
        return ComponentManager.XMLParseSetPreprocessInstructionHandler(paramInt1, paramInt2);
    }

    private static short XMLParseSetCommentHandler(int paramInt1, int paramInt2) {
        return ComponentManager.XMLParseSetCommentHandler(paramInt1, paramInt2);
    }
}

