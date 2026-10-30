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
    public XMLDoc parse(DataRef dataRef, boolean allowUppercase, boolean allowUnquotedAttributeValues) throws StdQTException, QTException {
        int i = 0;
        if (allowUppercase)
            i |= 0x1;
        if (allowUnquotedAttributeValues)
            i |= 0x2;
        int[] document = { 0 };
        int j = XMLParseDataRef(_ID(), QTObject.ID(dataRef), dataRef.getType(), i, document);
        StdQTException.checkError(j);
        return new XMLDoc(document[0], this);
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
        int[] document = { 0 };
        int j = XMLParseFile(_ID(), file.getFSSpec(true, 256), i, document);
        StdQTException.checkError(j);
        return new XMLDoc(document[0], this);
    }

    /**
     * Parses the XML file pointed to by dataRef, using event-based parsing. Does not return a parse tree
     * @see "QuickTime::XMLParseDataRef"
     * @param allowUppercase to set StdQTConstants5.xmlParseFlagAllowUppercase flag
     * @param allowUnquotedAttributeValues to set StdQTConstants5.xmlParseFlagAllowUnquotedAttributeValues flag
     * @throws quicktime.std.StdQTException
     * @throws quicktime.QTException
     */
    public void eventParse(DataRef dataRef, boolean allowUppercase, boolean allowUnquotedAttributeValues) throws StdQTException, QTException {
        int i = 4;
        if (allowUppercase)
            i |= 0x1;
        if (allowUnquotedAttributeValues)
            i |= 0x2;
        int j = XMLParseDataRef(_ID(), QTObject.ID(dataRef), dataRef.getType(), i, null);
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
    public int addAttribute(int elementID, int nameSpaceID, String attributeName, int attributeID) throws StdQTException {
        if (elementID == 0)
            throw new StdQTException(-50);
        int[] arrayOfInt = { attributeID };
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
        byte[] attributeValueKindInfo = { 0, 0, 0, 0 };
        for (int i = 0; i < 4; attributeValue >>= 8) {
            attributeValueKindInfo[i] = ((byte) (attributeValue & 0xFF));
            i++;
        }
        addAttributeValueKind(elementID, attributeID, 1, attributeValueKindInfo);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindPercent value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID, throws on zero attributeID
     */
    public void addAttributeValueKind(int elementID, int attributeID, float value) throws StdQTException {
        int i = (int) value;
        byte[] attributeValueKindInfo = { 0, 0, 0, 0 };
        for (int j = 0; j < 4; i >>= 8) {
            attributeValueKindInfo[j] = ((byte) (i & 0xFF));
            j++;
        }
        addAttributeValueKind(elementID, attributeID, 2, attributeValueKindInfo);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindBoolean value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID, throws on zero attributeID
     */
    public void addAttributeValueKind(int elementID, int attributeID, boolean value) throws StdQTException {
        byte[] attributeValueKindInfo = { (byte) (value ? 1 : 0) };
        addAttributeValueKind(elementID, attributeID, 4, attributeValueKindInfo);
    }

    /**
     * tell the parser that the particular attribute of an element may have an additional attributeValueKindOnOff value,
     * as specified by attributeValueKind and attributeValueKindInfo
     * @see "QuickTime::XMLParseAddAttributeValueKind"
     * @param elementID non zero unique tokenized element identifier
     * @param attributeID non zero unique tokenized attribute identifier
     * @throws quicktime.std.StdQTException throws on zero elementID, throws on zero attributeID
     */
    public void addAttributeValueKind(int elementID, int attributeID, byte value) throws StdQTException {
        byte[] attributeValueKindInfo = { (byte) (value != 0 ? 1 : 0) };
        addAttributeValueKind(elementID, attributeID, 8, attributeValueKindInfo);
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
    public void addAttributeValueKind(int elementID, int attributeID, String[] attributeList, boolean caseInsensitive) throws StdQTException {
        int i = attributeList.length;
        int j = 0;
        for (String string : attributeList) {
            j += string.length();
            j++;
        }
        j++;
        byte[] attributeValueKindInfo = new byte[j];
        int m = 0;
        for (String s : attributeList) {
            j = s.length();
            for (int i1 = 0; i1 < j; i1++) {
                attributeValueKindInfo[(m++)] = ((byte) s.charAt(i1));
            }
            attributeValueKindInfo[(m++)] = 0;
        }
        attributeValueKindInfo[m] = 0;
        int n = caseInsensitive ? 64 : 32;
        addAttributeValueKind(elementID, attributeID, n, attributeValueKindInfo);
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
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, int value) throws StdQTException {
        byte[] attributeValueKindInfo = { 0, 0, 0, 0 };
        for (int i = 0; i < 4; value >>= 8) {
            attributeValueKindInfo[i] = ((byte) (value & 0xFF));
            i++;
        }
        return addAttributeAndValue(elementID, nameSpaceID, attributeName, attributeID, 1, attributeValueKindInfo);
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
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, float value) throws StdQTException {
        int i = (int) value;
        byte[] attributeValueKindInfo = { 0, 0, 0, 0 };
        for (int j = 0; j < 4; i >>= 8) {
            attributeValueKindInfo[j] = ((byte) (i & 0xFF));
            j++;
        }
        return addAttributeAndValue(elementID, nameSpaceID, attributeName, attributeID, 2, attributeValueKindInfo);
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
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, boolean value) throws StdQTException {
        byte[] attributeValueKindInfo = { (byte) (value ? 1 : 0) };
        return addAttributeAndValue(elementID, nameSpaceID, attributeName, attributeID, 4, attributeValueKindInfo);
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
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, byte value) throws StdQTException {
        byte[] attributeValueKindInfo = { (byte) (value != 0 ? 1 : 0) };
        return addAttributeAndValue(elementID, nameSpaceID, attributeName, attributeID, 8, attributeValueKindInfo);
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
    public int addAttributeAndValue(int elementID, int nameSpaceID, String attributeName, int attributeID, String[] attributeList, boolean caseInsensitive) throws StdQTException {
        int i = attributeList.length;
        int j = 0;
        for (String string : attributeList) {
            j += string.length();
            j++;
        }
        j++;
        byte[] attributeValueKindInfo = new byte[j];
        int m = 0;
        for (String s : attributeList) {
            j = s.length();
            for (int i1 = 0; i1 < j; i1++) {
                attributeValueKindInfo[(m++)] = ((byte) s.charAt(i1));
            }
            attributeValueKindInfo[(m++)] = 0;
        }
        attributeValueKindInfo[m] = 0;
        int n = caseInsensitive ? 64 : 32;
        return addAttributeAndValue(elementID, nameSpaceID, attributeName, attributeID, n, attributeValueKindInfo);
    }

    /**
     * tell the parser of a namespace to be recognized
     * @see "QuickTime::XMLParseAddNameSpace"
     * @param nameSpaceID non zero tokenized namespace identifier
     * @throws quicktime.std.StdQTException throws on zero nameSpaceID
     */
    public void addNameSpace(int nameSpaceID, String str) throws StdQTException {
        if (nameSpaceID == 0)
            throw new StdQTException(-50);
        int[] arrayOfInt = { nameSpaceID };
        int i = XMLParseAddNameSpace(_ID(), QTUtils.String2CString(str), arrayOfInt);
        StdQTException.checkError(i);
    }

    /**
     * tell the parser of an identified namespace to be recognized
     * @see "QuickTime::XMLParseAddNameSpace"
     * @return generated non zero tokenized name space identifier
     * @throws quicktime.std.StdQTException
     */
    public int addNameSpace(String str) throws StdQTException {
        int[] nameSpaceID = { 0 };
        int i = XMLParseAddNameSpace(_ID(), QTUtils.String2CString(str), nameSpaceID);
        StdQTException.checkError(i);
        return nameSpaceID[0];
    }

    /**
     * specify the offset and limit for reading from the dataref to be used when parsing
     * @see "QuickTime::XMLParseSetOffsetAndLimit"
     */
    public void setOffsetAndLimit(int offset, int limit) throws StdQTException {
        int i = XMLParseSetOffsetAndLimit(_ID(), offset, limit);
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
    public void setCommentHandler(XMLCommentProc commentHandler) throws StdQTException {
        if (commentHandler == null) {
            removeCommentHandler();
            return;
        }
        if (this.commentUPP != null) {
            removeCommentHandler();
        }
        this.commentUPP = new XMLDispatcher(commentHandler);
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
        int[] errorLine = { 0 };
        int i = XMLParseGetDetailedParseError(_ID(), errorLine, null);
        StdQTException.checkError(i);
        return errorLine[0];
    }

    /**
     * Returns a more detailed description of the error if a file failed to parse properly
     * @see "QuickTime::XMLParseGetDetailedParseError"
     * @return description of error
     * @throws quicktime.std.StdQTException
     */
    public String getParseErrorDescription() throws StdQTException {
        byte[] errDesc = new byte[256];
        int i = XMLParseGetDetailedParseError(_ID(), null, errDesc);
        StdQTException.checkError(i);
        return QTUtils.PString2String(errDesc, 0);
    }

    private static short XMLParseDataRef(int ci, int dataRef, int dataRefType, int flags, int[] document) {
        return XMLParserManager.XMLParseDataRef(ci, dataRef, dataRefType, flags, document);
    }

    private static short XMLParseFile(int ci, byte[] fsspec, int flags, int[] document) {
        return XMLParserManager.XMLParseFile(ci, fsspec, flags, document);
    }

    private static short XMLParseGetDetailedParseError(int ci, int[] errorLine, byte[] errDesc) {
        return XMLParserManager.XMLParseGetDetailedParseError(ci, errorLine, errDesc);
    }

    private static short XMLParseAddElement(int ci, byte[] elementName, int nameSpaceID, int[] elementID, int elementFlags) {
        return XMLParserManager.XMLParseAddElement(ci, elementName, nameSpaceID, elementID, elementFlags);
    }

    private static short XMLParseAddAttribute(int ci, int elementID, int nameSpaceID, byte[] attributeName, int[] attributeID) {
        return XMLParserManager.XMLParseAddAttribute(ci, elementID, nameSpaceID, attributeName, attributeID);
    }

    private static short XMLParseAddAttributeAndValue(int ci, int elementID, int nameSpaceID, byte[] attributeName, int[] attributeID, int valueKind, byte[] valueKindInfo) {
        return XMLParserManager.XMLParseAddAttributeAndValue(ci, elementID, nameSpaceID, attributeName, attributeID, valueKind, valueKindInfo);
    }

    private static short XMLParseAddAttributeValueKind(int ci, int elementID, int attributeID, int valueKind, byte[] valueKindInfo) {
        return XMLParserManager.XMLParseAddAttributeValueKind(ci, elementID, attributeID, valueKind, valueKindInfo);
    }

    private static short XMLParseAddNameSpace(int ci, byte[] nameSpaceURL, int[] nameSpaceID) {
        return XMLParserManager.XMLParseAddNameSpace(ci, nameSpaceURL, nameSpaceID);
    }

    private static short XMLParseSetOffsetAndLimit(int ci, int offset, int limit) {
        return XMLParserManager.XMLParseSetOffsetAndLimit(ci, offset, limit);
    }

    private static short XMLParseSetStartDocumentHandler(int ci, int proc) {
        return XMLParserManager.XMLParseSetStartDocumentHandler(ci, proc);
    }

    private static short XMLParseSetEndDocumentHandler(int ci, int proc) {
        return XMLParserManager.XMLParseSetEndDocumentHandler(ci, proc);
    }

    private static short XMLParseSetStartElementHandler(int ci, int proc) {
        return XMLParserManager.XMLParseSetStartElementHandler(ci, proc);
    }

    private static short XMLParseSetEndElementHandler(int ci, int proc) {
        return XMLParserManager.XMLParseSetEndElementHandler(ci, proc);
    }

    private static short XMLParseSetCharDataHandler(int ci, int proc) {
        return XMLParserManager.XMLParseSetCharDataHandler(ci, proc);
    }

    private static short XMLParseSetPreprocessInstructionHandler(int ci, int proc) {
        return XMLParserManager.XMLParseSetPreprocessInstructionHandler(ci, proc);
    }

    private static short XMLParseSetCommentHandler(int ci, int proc) {
        return XMLParserManager.XMLParseSetCommentHandler(ci, proc);
    }
}
