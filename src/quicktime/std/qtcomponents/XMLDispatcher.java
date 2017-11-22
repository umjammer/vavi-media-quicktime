
package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.jdirect.QTNative;


final class XMLDispatcher implements quicktime.jdirect.PrimitivesLib {
    private static Object linkage;

    private String methodName;

    private String methodSig;

    private int procInfo;

    private XMLMethodClosure mMethodClosure;

    private XMLStartDocProc startDocExecute;

    private XMLEndDocProc endDocExecute;

    private XMLStartElementProc startElementExecute;

    private XMLEndElementProc endElementExecute;

    private XMLCommentProc commentExecute;

    private XMLCharDataProc charDataExecute;

    private XMLPreprocessorProc preprocessorExecute;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(XMLDispatcher.class);
                    }
                });
            }
        }.establish();
    }

    XMLDispatcher(XMLStartDocProc paramXMLStartDocProc) {
        this.methodName = "startDocProc";
        this.methodSig = "(I)I";
        this.procInfo = 240;
        this.startDocExecute = paramXMLStartDocProc;
    }

    XMLDispatcher(XMLEndDocProc paramXMLEndDocProc) {
        this.methodName = "endDocProc";
        this.methodSig = "(I)I";
        this.procInfo = 240;
        this.endDocExecute = paramXMLEndDocProc;
    }

    XMLDispatcher(XMLStartElementProc paramXMLStartElementProc) {
        this.methodName = "startElementProc";
        this.methodSig = "(III)I";
        this.procInfo = 4080;
        this.startElementExecute = paramXMLStartElementProc;
    }

    XMLDispatcher(XMLEndElementProc paramXMLEndElementProc) {
        this.methodName = "endElementProc";
        this.methodSig = "(II)I";
        this.procInfo = 1008;
        this.endElementExecute = paramXMLEndElementProc;
    }

    XMLDispatcher(XMLCommentProc paramXMLCommentProc) {
        this.methodName = "commentProc";
        this.methodSig = "(II)I";
        this.procInfo = 1008;
        this.commentExecute = paramXMLCommentProc;
    }

    XMLDispatcher(XMLCharDataProc paramXMLCharDataProc) {
        this.methodName = "charDataProc";
        this.methodSig = "(II)I";
        this.procInfo = 1008;
        this.charDataExecute = paramXMLCharDataProc;
    }

    XMLDispatcher(XMLPreprocessorProc paramXMLPreprocessorProc) {
        this.methodName = "preprocessorProc";
        this.methodSig = "(III)I";
        this.procInfo = 4080;
        this.preprocessorExecute = paramXMLPreprocessorProc;
    }

    private int startDocProc(int paramInt) {
        try {
            this.startDocExecute.execute();
            return 0;
        } catch (QTException localQTException) {
            return localQTException.errorCode();
        }
    }

    private int endDocProc(int paramInt) {
        try {
            this.endDocExecute.execute();
            return 0;
        } catch (QTException localQTException) {
            return localQTException.errorCode();
        }
    }

    private int startElementProc(int paramInt1, int paramInt2, int paramInt3) {
        try {
            this.startElementExecute.execute(getStringFromPointer(paramInt1), getStringsFromArrayOfPointers(paramInt2));
            return 0;
        } catch (QTException localQTException) {
            return localQTException.errorCode();
        }
    }

    private int endElementProc(int paramInt1, int paramInt2) {
        try {
            this.endElementExecute.execute(getStringFromPointer(paramInt1));
            return 0;
        } catch (QTException localQTException) {
            return localQTException.errorCode();
        }
    }

    private int commentProc(int paramInt1, int paramInt2) {
        try {
            this.commentExecute.execute(getStringFromPointer(paramInt1));
            return 0;
        } catch (QTException localQTException) {
            return localQTException.errorCode();
        }
    }

    private int charDataProc(int paramInt1, int paramInt2) {
        try {
            this.charDataExecute.execute(getStringFromPointer(paramInt1));
            return 0;
        } catch (QTException localQTException) {
            return localQTException.errorCode();
        }
    }

    private int preprocessorProc(int paramInt1, int paramInt2, int paramInt3) {
        try {
            this.preprocessorExecute.execute(getStringFromPointer(paramInt1), getStringsFromArrayOfPointers(paramInt2));
            return 0;
        } catch (QTException localQTException) {
            return localQTException.errorCode();
        }
    }

    private String getStringFromPointer(int paramInt) {
        if (paramInt != 0) {
            int i = 0;
            int j = 0;
            while (getByteFromPointer(paramInt, j++) != 0) {
                i++;
            }
            if (i > 0) {
                byte[] arrayOfByte = new byte[i];
                copyPointerToArray(paramInt, 0, arrayOfByte, 0, i);
                return quicktime.util.QTUtils.CString2String(arrayOfByte, 0);
            }
        }
        return "";
    }

    private String[] getStringsFromArrayOfPointers(int paramInt) {
        String[] arrayOfString = null;
        int i = 0;
        if (paramInt != 0) {
            int j = getIntFromPointer(paramInt, 0);
            while (j != 0) {
                i++;
                j = getIntFromPointer(paramInt, 4 * i);
            }
            if (i != 0) {
                int k = i % 2;
                arrayOfString = new String[i + k];
                for (int m = 0; m < i; m++) {
                    j = getIntFromPointer(paramInt, 4 * m);
                    arrayOfString[m] = getStringFromPointer(j);
                }
                if (k == 1) {
                    arrayOfString[i] = "";
                }
            }
        }
        return arrayOfString;
    }

    int ID() {
        if (this.mMethodClosure == null)
            this.mMethodClosure = new XMLMethodClosure(this, this.methodName, this.methodSig, this.procInfo);
        return this.mMethodClosure.getClosure();
    }

    void cleanup() {
        this.mMethodClosure = null;
    }

    private static native byte getByteFromPointer(int paramInt1, int paramInt2);

    static class XMLMethodClosure extends quicktime.jdirect.MethodClosure {
        XMLMethodClosure(Object paramObject, String paramString1, String paramString2, int paramInt) {
            super(paramObject, paramString1, paramString2, paramInt);
        }

        int getClosure() {
            return this.closure;
        }
    }

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  byte[] paramArrayOfByte,
                                                  int paramInt3,
                                                  int paramInt4);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/XMLDispatcher.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
