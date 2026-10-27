package quicktime.std.qtcomponents;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.XMLParserManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;


/**
 * XMLDoc class corresponds to the QuickTime XMLDoc
 */
public final class XMLDoc extends QTObject implements PrimitivesLib, QuickTimeLib {

    private static final int kXMLDocDataSize = 20;
    private static final int kRootElementOffset = 4;

    private XMLParser owner;

    XMLDoc(int paramInt, XMLParser paramXMLParser) throws QTException {
        super(paramInt, paramXMLParser);
        this.owner = paramXMLParser;
    }

    /**
     * Disposes of a XMLDoc parse tree
     * @see "QuickTime::XMLParseDisposeXMLDoc"
     */
    public void dispose() throws StdQTException {
        if (this.owner != null) {
            int i = XMLParseDisposeXMLDoc(QTObject.ID(this.owner), _ID());
            this.owner = null;
            StdQTException.checkError(i);
        }
    }

    /**
     * get the root element of the xml parse tree
     * {@link quicktime.std.StdQTException}
     */
    public XMLElement getRootElement() throws StdQTException, QTException {
        return new XMLElement(_ID() + 4);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static short XMLParseDisposeXMLDoc(int paramInt1, int paramInt2) {
        return XMLParserManager.XMLParseDisposeXMLDoc(paramInt1, paramInt2);
    }
}
