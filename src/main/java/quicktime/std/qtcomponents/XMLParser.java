package quicktime.std.qtcomponents;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.XMLParserManager;
import quicktime.io.QTFile;
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

    private XMLDispatcher startDocUPP;

    private XMLDispatcher endDocUPP;

    private XMLDispatcher startElementUPP;

    private XMLDispatcher endElementUPP;

    private XMLDispatcher commentUPP;

    private XMLDispatcher charDataUPP;

    private XMLDispatcher preprocessorUPP;

    public XMLParser() throws QTException {
        super(1885434483, 2020437024);
    }

    /**
     * parses the XML file pointed to by dataRef, returning a XMLParseTree parse tree
     * @see "QuickTime::XMLParseDataRef"
     * @param allowUppercase to set StdQTConstants5.xmlParseFlagAllowUppercase flag
     * @param allowUnquotedAttributeValues to set StdQTConstants5.xmlParseFlagAllowUnquotedAttributeValues flag
     * @return XMLParseTree
     * @throws quicktime.std.StdQTException
     * @throws quicktime.QTException
     */
    public XMLDoc parse(DataRef paramDataRef, boolean allowUppercase, boolean allowUnquotedAttributeValues) throws StdQTException, QTException {
        int i = 0;
        if (allowUppercase)
            i |= 0x1;
        if (allowUnquotedAttributeValues)
            i |= 0x2;
        int[] arrayOfInt = { 0 };
        int j = XMLParseDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), i, arrayOfInt);
        StdQTException.checkError(j);
        return new XMLDoc(arrayOfInt[0], this);
    }

    /**
     * Parses the XML file pointed to by file, returning a XMLDoc parse tree
     * @see "QuickTime::XMLParseFile"
     * @param file open for read
     * @param allowUppercase to set StdQTConstants5.xmlParseFlagAllowUppercase flag
     * @param allowUnquotedAttributeValues to set StdQTConstants5.xmlParseFlagAllowUnquotedAttributeValues flag
     * @return XMLDoc
     * @throws quicktime.std.StdQTException
     * @throws quicktime.QTException
     */
    public XMLDoc parse(QTFile file, boolean allowUppercase, boolean allowUnquotedAttributeValues) throws StdQTException, QTException {
        int i = 0;
        if (allowUppercase)
            i |= 0x1;
        if (allowUnquotedAttributeValues)
            i |= 0x2;
        int[] arrayOfInt = { 0 };
        int j = XMLParseFile(_ID(), file.getFSSpec(true, 256), i, arrayOfInt);
        StdQTException.checkError(j);
        return new XMLDoc(arrayOfInt[0], this);
    }

    /**
     * Parses the XML file pointed to by dataRef, using event-based parsing. Does not return a parse tree
     * @see "QuickTime::XMLParseDataRef"
     * @param allowUppercase to set StdQTConstants5.xmlParseFlagAllowUppercase flag
     * @param allowUnquotedAttributeValues to set StdQTConstants5.xmlParseFlagAllowUnquotedAttributeValues flag
     * @throws quicktime.std.StdQTException
     * @throws quicktime.QTException
     */
    public void eventParse(DataRef paramDataRef, boolean allowUppercase, boolean allowUnquotedAttributeValues) throws StdQTException, QTException {
        int i = 4;
        if (allowUppercase)
            i |= 0x1;
        if (allowUnquotedAttributeValues)
            i |= 0x2;
        int j = XMLParseDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), i, null);
        StdQTException.checkError(j);
    }

    /**
     * Parses the XML file pointed to by file, using event-based parsing. Does not return a parse tree
     * @see "QuickTime::XMLParseFile"
     * @param file open for read
     * @param allowUppercase to set StdQTConstants5.xmlParseFlagAllowUppercase flag
     * @param allowUnquotedAttributeValues to set StdQTConstants5.xmlParseFlagAllowUnquotedAttributeValues flag
     * @throws quicktime.std.StdQTException
     * @throws quicktime.QTException
     */
    public void eventParse(QTFile file, boolean allowUppercase, boolean allowUnquotedAttributeValues) throws StdQTException, QTException {
        int i = 4;
        if (allowUppercase)
            i |= 0x1;
        if (allowUnquotedAttributeValues)
            i |= 0x2;
        int j = XMLParseFile(_ID(), file.getFSSpec(true, 256), i, null);
        StdQTException.checkError(j);
    }

    /**
     * tell the parser of an element to be recognized in the specified namespace
     * @see "QuickTime::XMLParseAddElement"
     * @param elementName name
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param elementID tokenized identifier, zero or unique identifier
     * @param elementFlags flags
     * @return tokenized element identifier  if elementID non zero, is elementID else generated unique non zero element identifier
     * @throws quicktime.std.StdQTException
     */
    public int addElement(String elementName, int nameSpaceID, int elementID, int elementFlags) throws StdQTException {
        int[] arrayOfInt = { elementID };
        int i = XMLParseAddElement(_ID(), QTUtils.String2CString(elementName), nameSpaceID, arrayOfInt, elementFlags);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * tell the parser of an attribute for the specified element in the specified namespace
     * @see "QuickTime::XMLParseAddAttribute"
     * @param elementID non zero unique tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @return attribute identifier token  if attributeID non zero, is elementID else generated unique non zero attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID
     */
    public int addAttribute(int elementID, int nameSpaceID, String attributeName, int paramInt3) throws StdQTException {
        if (elementID == 0)
            throw new StdQTException(-50);
        int[] arrayOfInt = { paramInt3 };
        int i = XMLParseAddAttribute(_ID(), elementID, nameSpaceID, QTUtils.String2CString(attributeName), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional kind of value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeValueKind -
     * @param attributeValueKindInfo -
     * @throws quicktime.std.StdQTException throws on zero elementID, throws on zero attributeID
     */
    public void addAttributeValueKind(int elementID, int attributeID, int attributeValueKind, byte[] attributeValueKindInfo) throws StdQTException {
        if (elementID == 0)
            throw new StdQTException(-50);
        if (attributeID == 0)
            throw new StdQTException(-50);
        int i = XMLParseAddAttributeValueKind(_ID(), elementID, attributeID, attributeValueKind, attributeValueKindInfo);
        StdQTException.checkError(i);
    }

    /**
     * tell the parser of an enumerated attribute, of type attributeValueKindCharString, for the specified element
     * @see "QuickTime::XMLParseAddAttributeAndValue"
     * @param elementID non zero tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeStringValue String
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID
     */
    public void addAttributeValueKind(int elementID, int attributeID, String attributeStringValue) throws StdQTException {
        addAttributeValueKind(elementID, attributeID, 0, QTUtils.String2CString(attributeStringValue));
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindInteger value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeValue int
     * @throws quicktime.std.StdQTException throws on zero elementID, throws on zero attributeID
     */
    public void addAttributeValueKind(int elementID, int attributeID, int attributeValue) throws StdQTException {
        byte[] arrayOfByte = { 0, 0, 0, 0 };
        for (int i = 0; i < 4; attributeValue >>= 8) {
            arrayOfByte[i] = ((byte) (attributeValue & 0xFF));
            i++;
        }
        addAttributeValueKind(elementID, attributeID, 1, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindPercent value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID, throws on zero attributeID
     */
    public void addAttributeValueKind(int elementID, int attributeID, float paramFloat) throws StdQTException {
        int i = (int) paramFloat;
        byte[] arrayOfByte = { 0, 0, 0, 0 };
        for (int j = 0; j < 4; i >>= 8) {
            arrayOfByte[j] = ((byte) (i & 0xFF));
            j++;
        }
        addAttributeValueKind(elementID, attributeID, 2, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindBoolean value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID, throws on zero attributeID
     */
    public void addAttributeValueKind(int elementID, int attributeID, boolean paramBoolean) throws StdQTException {
        byte[] arrayOfByte = { (byte) (paramBoolean ? 1 : 0) };
        addAttributeValueKind(elementID, attributeID, 4, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindOnOff value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID, throws on zero attributeID
     */
    public void addAttributeValueKind(int elementID, int attributeID, byte paramByte) throws StdQTException {
        byte[] arrayOfByte = { (byte) (paramByte != 0 ? 1 : 0) };
        addAttributeValueKind(elementID, attributeID, 8, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindColor value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeColorValue qd.QDColor
     * @throws quicktime.std.StdQTException throws on zero elementID, throws on zero attributeID
     */
    public void addAttributeValueKind(int elementID, int attributeID, QDColor attributeColorValue) throws StdQTException {
        addAttributeValueKind(elementID, attributeID, 16, attributeColorValue.getRGBColor());
    }

    /**
     * tell the parser of an enumerated attribute, of type attributeValueKindEnum or attributeValueKindCaseSensEnum, for the specified element
     * @see "QuickTime::XMLParseAddAttributeAndValue"
     * @param elementID non zero tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeList String[] of attributes
     *  attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID
     */
    public void addAttributeValueKind(int elementID, int attributeID, String[] attributeList, boolean paramBoolean) throws StdQTException {
        int i = attributeList.length;
        int j = 0;
        for (String string : attributeList) {
            j += string.length();
            j++;
        }
        j++;
        byte[] arrayOfByte = new byte[j];
        int m = 0;
        for (String s : attributeList) {
            j = s.length();
            for (int i1 = 0; i1 < j; i1++) {
                arrayOfByte[(m++)] = ((byte) s.charAt(i1));
            }
            arrayOfByte[(m++)] = 0;
        }
        arrayOfByte[m] = 0;
        int n = paramBoolean ? 64 : 32;
        addAttributeValueKind(elementID, attributeID, n, arrayOfByte);
    }

    /**
     * tell the parser of an attribute, which may have a particular type of value, for the specified element
     * @see "QuickTime::XMLParseAddAttributeAndValue"
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeValueKind -
     * @param attributeValueKindInfo -
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID
     */
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, int attributeValueKind, byte[] attributeValueKindInfo) throws StdQTException {
        if (elementID == 0)
            throw new StdQTException(-50);
        int[] arrayOfInt = { attributeID };
        int i = XMLParseAddAttributeAndValue(_ID(), elementID, nameSpaceID, QTUtils.String2CString(attributeName), arrayOfInt, attributeValueKind, attributeValueKindInfo);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * tell the parser of an attribute, of type attributeValueKindCharString, for the specified element
     * @see "QuickTime::XMLParseAddAttributeAndValue"
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeValue String
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID
     */
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, String attributeValue) throws StdQTException {
        return addAttributeAndValue(elementID, nameSpaceID, attributeName, attributeID, 0, QTUtils.String2CString(attributeValue));
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindInteger value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID
     */
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, int paramInt4) throws StdQTException {
        byte[] arrayOfByte = { 0, 0, 0, 0 };
        for (int i = 0; i < 4; paramInt4 >>= 8) {
            arrayOfByte[i] = ((byte) (paramInt4 & 0xFF));
            i++;
        }
        return addAttributeAndValue(elementID, nameSpaceID, attributeName, attributeID, 1, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindPercent value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws quicktime.std.StdQTException on zero elementID
     */
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, float paramFloat) throws StdQTException {
        int i = (int) paramFloat;
        byte[] arrayOfByte = { 0, 0, 0, 0 };
        for (int j = 0; j < 4; i >>= 8) {
            arrayOfByte[j] = ((byte) (i & 0xFF));
            j++;
        }
        return addAttributeAndValue(elementID, nameSpaceID, attributeName, attributeID, 2, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindBoolean value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID
     */
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, boolean paramBoolean) throws StdQTException {
        byte[] arrayOfByte = { (byte) (paramBoolean ? 1 : 0) };
        return addAttributeAndValue(elementID, nameSpaceID, attributeName, attributeID, 4, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindOnOff value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID
     */
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, byte paramByte) throws StdQTException {
        byte[] arrayOfByte = { (byte) (paramByte != 0 ? 1 : 0) };
        return addAttributeAndValue(elementID, nameSpaceID, attributeName, attributeID, 8, arrayOfByte);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindColor value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeColorValue qd.QDColor
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID
     */
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, QDColor attributeColorValue) throws StdQTException {
        return addAttributeAndValue(elementID, nameSpaceID, attributeName, attributeID, 16, attributeColorValue.getRGBColor());
    }

    /**
     * tell the parser of an enumerated attribute, of type attributeValueKindEnum or attributeValueKindCaseSensEnum, for the specified element
     * @see "QuickTime::XMLParseAddAttributeAndValue"
     * @param elementID non zero tokenized element identifier
     * @param nameSpaceID tokenized identifier, can be nameSpaceIDNone
     * @param attributeName name
     * @param attributeID non zero unique tokenized attribute identifier
     * @param attributeList String[] of attributes
     * @return attribute identifier token  if attributeID non zero else generated unique non zero attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID
     */
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, String[] attributeList, boolean paramBoolean) throws StdQTException {
        int i = attributeList.length;
        int j = 0;
        for (String string : attributeList) {
            j += string.length();
            j++;
        }
        j++;
        byte[] arrayOfByte = new byte[j];
        int m = 0;
        for (String s : attributeList) {
            j = s.length();
            for (int i1 = 0; i1 < j; i1++) {
                arrayOfByte[(m++)] = ((byte) s.charAt(i1));
            }
            arrayOfByte[(m++)] = 0;
        }
        arrayOfByte[m] = 0;
        int n = paramBoolean ? 64 : 32;
        return addAttributeAndValue(elementID, nameSpaceID, attributeName, attributeID, n, arrayOfByte);
    }

    /**
     * tell the parser of a namespace to be recognized
     * @see "QuickTime::XMLParseAddNameSpace"
     * @param nameSpaceID non zero tokenized namespace identifier
     * @throws quicktime.std.StdQTException throws on zero nameSpaceID
     */
    public void addNameSpace(int nameSpaceID, String paramString) throws StdQTException {
        if (nameSpaceID == 0)
            throw new StdQTException(-50);
        int[] arrayOfInt = { nameSpaceID };
        int i = XMLParseAddNameSpace(_ID(), QTUtils.String2CString(paramString), arrayOfInt);
        StdQTException.checkError(i);
    }

    /**
     * tell the parser of an identified namespace to be recognized
     * @see "QuickTime::XMLParseAddNameSpace"
     * @return generated non zero tokenized name space identifier
     * @throws quicktime.std.StdQTException
     */
    public int addNameSpace(String paramString) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = XMLParseAddNameSpace(_ID(), QTUtils.String2CString(paramString), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * specify the offset and limit for reading from the dataref to be used when parsing
     * @see "QuickTime::XMLParseSetOffsetAndLimit"
     */
    public void setOffsetAndLimit(int paramInt1, int paramInt2) throws StdQTException {
        int i = XMLParseSetOffsetAndLimit(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * Set the callback handler for start document events
     * @see "QuickTime::XMLParseSetStartDocumentHandler"
     * @param proc implements XMLStartDocProc
     * @throws quicktime.std.StdQTException
     */
    public void setStartDocumentHandler(XMLStartDocProc proc) throws StdQTException {
        if (proc == null) {
            removeStartDocumentHandler();
            return;
        }
        if (this.startDocUPP != null) {
            removeStartDocumentHandler();
        }
        this.startDocUPP = new XMLDispatcher(proc);
        int i = XMLParseSetStartDocumentHandler(_ID(), this.startDocUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered callback handler for start document events
     * @see "QuickTime::XMLParseSetStartDocumentHandler"
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
     * @see "QuickTime::XMLParseSetEndDocumentHandler"
     * @param proc implements XMLEndDocProc
     * @throws quicktime.std.StdQTException
     */
    public void setEndDocumentHandler(XMLEndDocProc proc) throws StdQTException {
        if (proc == null) {
            removeEndDocumentHandler();
            return;
        }
        if (this.endDocUPP != null) {
            removeEndDocumentHandler();
        }
        this.endDocUPP = new XMLDispatcher(proc);
        int i = XMLParseSetEndDocumentHandler(_ID(), this.endDocUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered callback handler for end document events
     * @see "QuickTime::XMLParseSetEndDocumentHandler"
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
     * @see "QuickTime::XMLParseSetStartElementHandler"
     * @param proc implements XMLStartElementProc
     * @throws quicktime.std.StdQTException
     */
    public void setStartElementHandler(XMLStartElementProc proc) throws StdQTException {
        if (proc == null) {
            removeStartElementHandler();
            return;
        }
        if (this.startElementUPP != null) {
            removeStartElementHandler();
        }
        this.startElementUPP = new XMLDispatcher(proc);
        int i = XMLParseSetStartElementHandler(_ID(), this.startElementUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered start element proc.
     * @see "QuickTime::XMLParseSetStartElementHandler"
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
     * @see "QuickTime::XMLParseSetEndElementHandler"
     * @param proc implements XMLEndElementProc
     * @throws quicktime.std.StdQTException
     */
    public void setEndElementHandler(XMLEndElementProc proc) throws StdQTException {
        if (proc == null) {
            removeEndElementHandler();
            return;
        }
        if (this.endElementUPP != null) {
            removeEndElementHandler();
        }
        this.endElementUPP = new XMLDispatcher(proc);
        int i = XMLParseSetEndElementHandler(_ID(), this.endElementUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered end element proc.
     * @see "QuickTime::XMLParseSetEndElementHandler"
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
     * @see "QuickTime::XMLParseSetCommentHandler"
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
     * @see "QuickTime::XMLParseSetCommentHandler"
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
     * @see "QuickTime::XMLParseSetCharDataHandler"
     * @param proc implements XMLCharDataProc
     * @throws quicktime.std.StdQTException
     */
    public void setCharDataHandler(XMLCharDataProc proc) throws StdQTException {
        if (proc == null) {
            removeCharDataHandler();
            return;
        }
        if (this.charDataUPP != null) {
            removeCharDataHandler();
        }
        this.charDataUPP = new XMLDispatcher(proc);
        int i = XMLParseSetCharDataHandler(_ID(), this.charDataUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered callback handler for character data events.
     * @see "QuickTime::XMLParseSetCharDataHandler"
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
     * @see "QuickTime::XMLParseSetPreprocessInstructionHandler"
     * @param proc implements XMLPreprocessorProc
     * @throws quicktime.std.StdQTException
     */
    public void setPreprocessorHandler(XMLPreprocessorProc proc) throws StdQTException {
        if (proc == null) {
            removePreprocessorHandler();
            return;
        }
        if (this.preprocessorUPP != null) {
            removePreprocessorHandler();
        }
        this.preprocessorUPP = new XMLDispatcher(proc);
        int i = XMLParseSetPreprocessInstructionHandler(_ID(), this.preprocessorUPP.ID());
        StdQTException.checkError(i);
    }

    /**
     * Remove the currently registered callback handler for preprocessor instruction events
     * @see "QuickTime::XMLParseSetPreprocessInstructionHandler"
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
     * @see "QuickTime::XMLParseGetDetailedParseError"
     * @return line number of the error
     * @throws quicktime.std.StdQTException
     */
    public int getParseErrorLine() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = XMLParseGetDetailedParseError(_ID(), arrayOfInt, null);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Returns a more detailed description of the error if a file failed to parse properly
     * @see "QuickTime::XMLParseGetDetailedParseError"
     * @return description of error
     * @throws quicktime.std.StdQTException
     */
    public String getParseErrorDescription() throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int i = XMLParseGetDetailedParseError(_ID(), null, arrayOfByte);
        StdQTException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    private static short XMLParseDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        return XMLParserManager.XMLParseDataRef(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
    }

    private static short XMLParseFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt) {
        return XMLParserManager.XMLParseFile(paramInt1, paramArrayOfByte, paramInt2, paramArrayOfInt);
    }

    private static short XMLParseGetDetailedParseError(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte) {
        return XMLParserManager.XMLParseGetDetailedParseError(paramInt, paramArrayOfInt, paramArrayOfByte);
    }

    private static short XMLParseAddElement(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
        return XMLParserManager.XMLParseAddElement(paramInt1, paramArrayOfByte, paramInt2, paramArrayOfInt, paramInt3);
    }

    private static short XMLParseAddAttribute(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return XMLParserManager.XMLParseAddAttribute(paramInt1, paramInt2, paramInt3, paramArrayOfByte, paramArrayOfInt);
    }

    private static short XMLParseAddAttributeAndValue(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, int[] paramArrayOfInt, int paramInt4, byte[] paramArrayOfByte2) {
        return XMLParserManager.XMLParseAddAttributeAndValue(paramInt1, paramInt2, paramInt3, paramArrayOfByte1, paramArrayOfInt, paramInt4, paramArrayOfByte2);
    }

    private static short XMLParseAddAttributeValueKind(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte) {
        return XMLParserManager.XMLParseAddAttributeValueKind(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte);
    }

    private static short XMLParseAddNameSpace(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return XMLParserManager.XMLParseAddNameSpace(paramInt, paramArrayOfByte, paramArrayOfInt);
    }

    private static short XMLParseSetOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3) {
        return XMLParserManager.XMLParseSetOffsetAndLimit(paramInt1, paramInt2, paramInt3);
    }

    private static short XMLParseSetStartDocumentHandler(int paramInt1, int paramInt2) {
        return XMLParserManager.XMLParseSetStartDocumentHandler(paramInt1, paramInt2);
    }

    private static short XMLParseSetEndDocumentHandler(int paramInt1, int paramInt2) {
        return XMLParserManager.XMLParseSetEndDocumentHandler(paramInt1, paramInt2);
    }

    private static short XMLParseSetStartElementHandler(int paramInt1, int paramInt2) {
        return XMLParserManager.XMLParseSetStartElementHandler(paramInt1, paramInt2);
    }

    private static short XMLParseSetEndElementHandler(int paramInt1, int paramInt2) {
        return XMLParserManager.XMLParseSetEndElementHandler(paramInt1, paramInt2);
    }

    private static short XMLParseSetCharDataHandler(int paramInt1, int paramInt2) {
        return XMLParserManager.XMLParseSetCharDataHandler(paramInt1, paramInt2);
    }

    private static short XMLParseSetPreprocessInstructionHandler(int paramInt1, int paramInt2) {
        return XMLParserManager.XMLParseSetPreprocessInstructionHandler(paramInt1, paramInt2);
    }

    private static short XMLParseSetCommentHandler(int paramInt1, int paramInt2) {
        return XMLParserManager.XMLParseSetCommentHandler(paramInt1, paramInt2);
    }
}

