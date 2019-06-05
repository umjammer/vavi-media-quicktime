
package quicktime.std;

import quicktime.QTException;


public class StdQTException extends QTException {
    public StdQTException(String paramString) {
        super(paramString);
    }

    public StdQTException(int paramInt) {
        super(paramInt);
    }

    public static void checkError(int paramInt) throws StdQTException {
        if ((paramInt < 0) && (QTException.isDrawingError(paramInt) < 0))
            throw new StdQTException(paramInt);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * StdQTException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
