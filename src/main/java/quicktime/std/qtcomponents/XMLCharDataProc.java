package quicktime.std.qtcomponents;

import quicktime.QTException;

/**
 * The XMLCharDataProc corresponds to the QuickTime CharDataHandler callback
 */
public abstract interface XMLCharDataProc {

    /**
     * The method that is called by the XMLParser for parsing CharData events.
     * If any error occurs that should be provided back to quicktime this method should
     * throw an exception with the appropriate error number.
     */
    public abstract void execute(String paramString) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/XMLCharDataProc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
