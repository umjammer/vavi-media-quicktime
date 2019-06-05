
package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.movies.media.DataRef;
import quicktime.util.QTUtils;


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

    public XMLDoc parse(DataRef paramDataRef, boolean paramBoolean1, boolean paramBoolean2) throws StdQTException, QTException {
        int i = 0;
        if (paramBoolean1)
            i |= 0x1;
        if (paramBoolean2)
            i |= 0x2;
        int[] arrayOfInt = {
            0
        };
        int j = XMLParseDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), i, arrayOfInt);
        StdQTException.checkError(j);
        return new XMLDoc(arrayOfInt[0], this);
    }

    public XMLDoc parse(QTFile paramQTFile, boolean paramBoolean1, boolean paramBoolean2) throws StdQTException, QTException {
        int i = 0;
        if (paramBoolean1)
            i |= 0x1;
        if (paramBoolean2)
            i |= 0x2;
        int[] arrayOfInt = {
            0
        };
        int j = XMLParseFile(_ID(), paramQTFile.getFSSpec(true, 256), i, arrayOfInt);
        StdQTException.checkError(j);
        return new XMLDoc(arrayOfInt[0], this);
    }

    public void eventParse(DataRef paramDataRef, boolean paramBoolean1, boolean paramBoolean2) throws StdQTException,
                                                                                               QTException {
        int i = 4;
        if (paramBoolean1)
            i |= 0x1;
        if (paramBoolean2)
            i |= 0x2;
        int j = XMLParseDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), i, null);
        StdQTException.checkError(j);
    }

    public void eventParse(QTFile paramQTFile, boolean paramBoolean1, boolean paramBoolean2) throws StdQTException,
                                                                                             QTException {
        int i = 4;
        if (paramBoolean1)
            i |= 0x1;
        if (paramBoolean2)
            i |= 0x2;
        int j = XMLParseFile(_ID(), paramQTFile.getFSSpec(true, 256), i, null);
        StdQTException.checkError(j);
    }

    public int addElement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int[] arrayOfInt = {
            paramInt2
        };
        int i = XMLParseAddElement(_ID(), QTUtils.String2CString(paramString), paramInt1, arrayOfInt, paramInt3);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public int addAttribute(int paramInt1, int paramInt2, String paramString, int paramInt3) throws StdQTException {
        if (paramInt1 == 0)
            throw new StdQTException(-50);
        int[] arrayOfInt = {
            paramInt3
        };

        int i = XMLParseAddAttribute(_ID(), paramInt1, paramInt2, QTUtils.String2CString(paramString), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void addAttributeValueKind(int paramInt1,
                                      int paramInt2,
                                      int paramInt3,
                                      byte[] paramArrayOfByte) throws StdQTException {
        if (paramInt1 == 0)
            throw new StdQTException(-50);
        if (paramInt2 == 0)
            throw new StdQTException(-50);
        int i = XMLParseAddAttributeValueKind(_ID(), paramInt1, paramInt2, paramInt3, paramArrayOfByte);
        StdQTException.checkError(i);
    }

    public void addAttributeValueKind(int paramInt1, int paramInt2, String paramString) throws StdQTException {
        addAttributeValueKind(paramInt1, paramInt2, 0, QTUtils.String2CString(paramString));
    }

    public void addAttributeValueKind(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        byte[] arrayOfByte = {
            0, 0, 0, 0
        };
        for (int i = 0; i < 4; paramInt3 >>= 8) {
            arrayOfByte[i] = ((byte) (paramInt3 & 0xFF));
            i++;
        }
        addAttributeValueKind(paramInt1, paramInt2, 1, arrayOfByte);
    }

    public void addAttributeValueKind(int paramInt1, int paramInt2, float paramFloat) throws StdQTException {
        int i = (int) paramFloat;
        byte[] arrayOfByte = {
            0, 0, 0, 0
        };
        for (int j = 0; j < 4; i >>= 8) {
            arrayOfByte[j] = ((byte) (i & 0xFF));
            j++;
        }
        addAttributeValueKind(paramInt1, paramInt2, 2, arrayOfByte);
    }

    public void addAttributeValueKind(int paramInt1, int paramInt2, boolean paramBoolean) throws StdQTException {
        byte[] arrayOfByte = {
            (byte) (paramBoolean ? 1 : 0)
        };
        addAttributeValueKind(paramInt1, paramInt2, 4, arrayOfByte);
    }

    public void addAttributeValueKind(int paramInt1, int paramInt2, byte paramByte) throws StdQTException {
        byte[] arrayOfByte = {
            (byte) (paramByte != 0 ? 1 : 0)
        };
        addAttributeValueKind(paramInt1, paramInt2, 8, arrayOfByte);
    }

    public void addAttributeValueKind(int paramInt1, int paramInt2, QDColor paramQDColor) throws StdQTException {
        addAttributeValueKind(paramInt1, paramInt2, 16, paramQDColor.getRGBColor());
    }

    public void addAttributeValueKind(int paramInt1,
                                      int paramInt2,
                                      String[] paramArrayOfString,
                                      boolean paramBoolean) throws StdQTException {
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

    public int addAttributeAndValue(int paramInt1,
                                    int paramInt2,
                                    String paramString,
                                    int paramInt3,
                                    int paramInt4,
                                    byte[] paramArrayOfByte) throws StdQTException {
        if (paramInt1 == 0)
            throw new StdQTException(-50);
        int[] arrayOfInt = {
            paramInt3
        };
        int i = XMLParseAddAttributeAndValue(_ID(),
                                             paramInt1,
                                             paramInt2,
                                             QTUtils.String2CString(paramString),
                                             arrayOfInt,
                                             paramInt4,
                                             paramArrayOfByte);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public int addAttributeAndValue(int paramInt1,
                                    int paramInt2,
                                    String paramString1,
                                    int paramInt3,
                                    String paramString2) throws StdQTException {
        return addAttributeAndValue(paramInt1, paramInt2, paramString1, paramInt3, 0, QTUtils.String2CString(paramString2));
    }

    public int addAttributeAndValue(int paramInt1,
                                    int paramInt2,
                                    String paramString,
                                    int paramInt3,
                                    int paramInt4) throws StdQTException {
        byte[] arrayOfByte = {
            0, 0, 0, 0
        };

        for (int i = 0; i < 4; paramInt4 >>= 8) {
            arrayOfByte[i] = ((byte) (paramInt4 & 0xFF));
            i++;
        }
        return addAttributeAndValue(paramInt1, paramInt2, paramString, paramInt3, 1, arrayOfByte);
    }

    public int addAttributeAndValue(int paramInt1,
                                    int paramInt2,
                                    String paramString,
                                    int paramInt3,
                                    float paramFloat) throws StdQTException {
        int i = (int) paramFloat;
        byte[] arrayOfByte = {
            0, 0, 0, 0
        };

        for (int j = 0; j < 4; i >>= 8) {
            arrayOfByte[j] = ((byte) (i & 0xFF));
            j++;
        }
        return addAttributeAndValue(paramInt1, paramInt2, paramString, paramInt3, 2, arrayOfByte);
    }

    public int addAttributeAndValue(int paramInt1,
                                    int paramInt2,
                                    String paramString,
                                    int paramInt3,
                                    boolean paramBoolean) throws StdQTException {
        byte[] arrayOfByte = {
            (byte) (paramBoolean ? 1 : 0)
        };
        return addAttributeAndValue(paramInt1, paramInt2, paramString, paramInt3, 4, arrayOfByte);
    }

    public int addAttributeAndValue(int paramInt1,
                                    int paramInt2,
                                    String paramString,
                                    int paramInt3,
                                    byte paramByte) throws StdQTException {
        byte[] arrayOfByte = {
            (byte) (paramByte != 0 ? 1 : 0)
        };
        return addAttributeAndValue(paramInt1, paramInt2, paramString, paramInt3, 8, arrayOfByte);
    }

    public int addAttributeAndValue(int paramInt1,
                                    int paramInt2,
                                    String paramString,
                                    int paramInt3,
                                    QDColor paramQDColor) throws StdQTException {
        return addAttributeAndValue(paramInt1, paramInt2, paramString, paramInt3, 16, paramQDColor.getRGBColor());
    }

    public int addAttributeAndValue(int paramInt1,
                                    int paramInt2,
                                    String paramString,
                                    int paramInt3,
                                    String[] paramArrayOfString,
                                    boolean paramBoolean) throws StdQTException {
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

    public void addNameSpace(int paramInt, String paramString) throws StdQTException {
        if (paramInt == 0)
            throw new StdQTException(-50);
        int[] arrayOfInt = {
            paramInt
        };
        int i = XMLParseAddNameSpace(_ID(), QTUtils.String2CString(paramString), arrayOfInt);
        StdQTException.checkError(i);
    }

    public int addNameSpace(String paramString) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = XMLParseAddNameSpace(_ID(), QTUtils.String2CString(paramString), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void setOffsetAndLimit(int paramInt1, int paramInt2) throws StdQTException {
        int i = XMLParseSetOffsetAndLimit(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

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

    public void removeStartDocumentHandler() throws StdQTException {
        int i = XMLParseSetStartDocumentHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.startDocUPP != null) {
            this.startDocUPP.cleanup();
            this.startDocUPP = null;
        }
    }

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

    public void removeEndDocumentHandler() throws StdQTException {
        int i = XMLParseSetEndDocumentHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.endDocUPP != null) {
            this.endDocUPP.cleanup();
            this.endDocUPP = null;
        }
    }

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

    public void removeStartElementHandler() throws StdQTException {
        int i = XMLParseSetStartElementHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.startElementUPP != null) {
            this.startElementUPP.cleanup();
            this.startElementUPP = null;
        }
    }

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

    public void removeEndElementHandler() throws StdQTException {
        int i = XMLParseSetEndElementHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.endElementUPP != null) {
            this.endElementUPP.cleanup();
            this.endElementUPP = null;
        }
    }

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

    public void removeCommentHandler() throws StdQTException {
        int i = XMLParseSetCommentHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.commentUPP != null) {
            this.commentUPP.cleanup();
            this.commentUPP = null;
        }
    }

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

    public void removeCharDataHandler() throws StdQTException {
        int i = XMLParseSetCharDataHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.charDataUPP != null) {
            this.charDataUPP.cleanup();
            this.charDataUPP = null;
        }
    }

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

    public void removePreprocessorHandler() throws StdQTException {
        int i = XMLParseSetPreprocessInstructionHandler(_ID(), 0);
        StdQTException.checkError(i);
        if (this.preprocessorUPP != null) {
            this.preprocessorUPP.cleanup();
            this.preprocessorUPP = null;
        }
    }

    public int getParseErrorLine() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = XMLParseGetDetailedParseError(_ID(), arrayOfInt, null);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public String getParseErrorDescription() throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int i = XMLParseGetDetailedParseError(_ID(), null, arrayOfByte);
        StdQTException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    private static native short XMLParseDataRef(int paramInt1,
                                                int paramInt2,
                                                int paramInt3,
                                                int paramInt4,
                                                int[] paramArrayOfInt);

    private static native short XMLParseFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt);

    private static native short XMLParseGetDetailedParseError(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte);

    private static native short XMLParseAddElement(int paramInt1,
                                                   byte[] paramArrayOfByte,
                                                   int paramInt2,
                                                   int[] paramArrayOfInt,
                                                   int paramInt3);

    private static native short XMLParseAddAttribute(int paramInt1,
                                                     int paramInt2,
                                                     int paramInt3,
                                                     byte[] paramArrayOfByte,
                                                     int[] paramArrayOfInt);

    private static native short XMLParseAddAttributeAndValue(int paramInt1,
                                                             int paramInt2,
                                                             int paramInt3,
                                                             byte[] paramArrayOfByte1,
                                                             int[] paramArrayOfInt,
                                                             int paramInt4,
                                                             byte[] paramArrayOfByte2);

    private static native short XMLParseAddAttributeValueKind(int paramInt1,
                                                              int paramInt2,
                                                              int paramInt3,
                                                              int paramInt4,
                                                              byte[] paramArrayOfByte);

    private static native short XMLParseAddNameSpace(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native short XMLParseSetOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3);

    private static native short XMLParseSetStartDocumentHandler(int paramInt1, int paramInt2);

    private static native short XMLParseSetEndDocumentHandler(int paramInt1, int paramInt2);

    private static native short XMLParseSetStartElementHandler(int paramInt1, int paramInt2);

    private static native short XMLParseSetEndElementHandler(int paramInt1, int paramInt2);

    private static native short XMLParseSetCharDataHandler(int paramInt1, int paramInt2);

    private static native short XMLParseSetPreprocessInstructionHandler(int paramInt1, int paramInt2);

    private static native short XMLParseSetCommentHandler(int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/XMLParser.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
