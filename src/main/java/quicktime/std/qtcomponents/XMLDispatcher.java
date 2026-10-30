
package quicktime.std.qtcomponents;

import quicktime.QTException;
import quicktime.internal.toolbox.MemoryManager;


final class XMLDispatcher implements quicktime.jdirect.PrimitivesLib {

    private final String methodName;

    private final String methodSig;

    private final int procInfo;

    private XMLMethodClosure mMethodClosure;

    private XMLStartDocProc startDocExecute;

    private XMLEndDocProc endDocExecute;

    private XMLStartElementProc startElementExecute;

    private XMLEndElementProc endElementExecute;

    private XMLCommentProc commentExecute;

    private XMLCharDataProc charDataExecute;

    private XMLPreprocessorProc preprocessorExecute;

    XMLDispatcher(XMLStartDocProc startDocExecute) {
        this.methodName = "startDocProc";
        this.methodSig = "(I)I";
        this.procInfo = 240;
        this.startDocExecute = startDocExecute;
    }

    XMLDispatcher(XMLEndDocProc endDocExecute) {
        this.methodName = "endDocProc";
        this.methodSig = "(I)I";
        this.procInfo = 240;
        this.endDocExecute = endDocExecute;
    }

    XMLDispatcher(XMLStartElementProc startElementExecute) {
        this.methodName = "startElementProc";
        this.methodSig = "(III)I";
        this.procInfo = 4080;
        this.startElementExecute = startElementExecute;
    }

    XMLDispatcher(XMLEndElementProc endElementExecute) {
        this.methodName = "endElementProc";
        this.methodSig = "(II)I";
        this.procInfo = 1008;
        this.endElementExecute = endElementExecute;
    }

    XMLDispatcher(XMLCommentProc commentExecute) {
        this.methodName = "commentProc";
        this.methodSig = "(II)I";
        this.procInfo = 1008;
        this.commentExecute = commentExecute;
    }

    XMLDispatcher(XMLCharDataProc charDataExecute) {
        this.methodName = "charDataProc";
        this.methodSig = "(II)I";
        this.procInfo = 1008;
        this.charDataExecute = charDataExecute;
    }

    XMLDispatcher(XMLPreprocessorProc preprocessorExecute) {
        this.methodName = "preprocessorProc";
        this.methodSig = "(III)I";
        this.procInfo = 4080;
        this.preprocessorExecute = preprocessorExecute;
    }

    private int startDocProc(int refCon) {
        try {
            this.startDocExecute.execute();
            return 0;
        } catch (QTException e) {
            return e.errorCode();
        }
    }

    private int endDocProc(int refCon) {
        try {
            this.endDocExecute.execute();
            return 0;
        } catch (QTException e) {
            return e.errorCode();
        }
    }

    private int startElementProc(int name, int attributes, int refCon) {
        try {
            this.startElementExecute.execute(getStringFromPointer(name), getStringsFromArrayOfPointers(attributes));
            return 0;
        } catch (QTException e) {
            return e.errorCode();
        }
    }

    private int endElementProc(int name, int refCon) {
        try {
            this.endElementExecute.execute(getStringFromPointer(name));
            return 0;
        } catch (QTException e) {
            return e.errorCode();
        }
    }

    private int commentProc(int comment, int refCon) {
        try {
            this.commentExecute.execute(getStringFromPointer(comment));
            return 0;
        } catch (QTException e) {
            return e.errorCode();
        }
    }

    private int charDataProc(int charData, int refCon) {
        try {
            this.charDataExecute.execute(getStringFromPointer(charData));
            return 0;
        } catch (QTException e) {
            return e.errorCode();
        }
    }

    private int preprocessorProc(int name, int attributes, int refCon) {
        try {
            this.preprocessorExecute.execute(getStringFromPointer(name), getStringsFromArrayOfPointers(attributes));
            return 0;
        } catch (QTException e) {
            return e.errorCode();
        }
    }

    private String getStringFromPointer(int ptr) {
        if (ptr != 0) {
            int i = 0;
            int j = 0;
            while (getByteFromPointer(ptr, j++) != 0) {
                i++;
            }
            if (i > 0) {
                byte[] cString = new byte[i];
                copyPointerToArray(ptr, 0, cString, 0, i);
                return quicktime.util.QTUtils.CString2String(cString, 0);
            }
        }
        return "";
    }

    private String[] getStringsFromArrayOfPointers(int ptr) {
        String[] arrayOfString = null;
        int i = 0;
        if (ptr != 0) {
            int j = getIntFromPointer(ptr, 0);
            while (j != 0) {
                i++;
                j = getIntFromPointer(ptr, 4 * i);
            }
            if (i != 0) {
                int k = i % 2;
                arrayOfString = new String[i + k];
                for (int m = 0; m < i; m++) {
                    j = getIntFromPointer(ptr, 4 * m);
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

    private static byte getByteFromPointer(int ptr, int offset) {
        return MemoryManager.getByteFromPointer(ptr, offset);
    }

    static class XMLMethodClosure extends quicktime.jdirect.MethodClosure {
        XMLMethodClosure(Object target, String methodName, String descriptor, int procInfo) {
            super(target, methodName, descriptor, procInfo);
        }

        int getClosure() {
            return this.closure;
        }
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }
}
