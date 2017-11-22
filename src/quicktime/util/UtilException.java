
package quicktime.util;

import quicktime.QTException;


public class UtilException extends QTException {
    public UtilException(String paramString) {
        super(paramString);
    }

    public UtilException(int paramInt) {
        super(paramInt);
    }

    public static void checkError(int paramInt) throws UtilException {
        if (paramInt < 0) {
            throw new UtilException(paramInt);
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/util/
 * UtilException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
