package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;

/**
 * XMLDoc class corresponds to the QuickTime XMLDoc
 */
public final class XMLDoc extends QTObject implements PrimitivesLib, QuickTimeLib {

    private static Object linkage;

    private static final int kXMLDocDataSize = 20;

    private static final int kRootElementOffset = 4;

    private XMLParser owner;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(XMLDoc.class);
                    }
                });
            }
        }.establish();
    }

    XMLDoc(int paramInt, XMLParser paramXMLParser) throws QTException {
        super(paramInt, paramXMLParser);
        this.owner = paramXMLParser;
    }

    /**
     * Disposes of a XMLDoc parse tree
     * QuickTime::XMLParseDisposeXMLDoc
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

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native short XMLParseDisposeXMLDoc(int paramInt1, int paramInt2);
}
