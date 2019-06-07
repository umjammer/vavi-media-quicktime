package quicktime.std.qtcomponents;

import quicktime.QTException;

/**
 * The XMLEndDocProc corresponds to the QuickTime EndDocumentHandler callback
 */
public abstract interface XMLEndDocProc {

    /**
     * The method that is called by the XMLParser for parsing EndDocument events.
     * If any error occurs that should be provided back to quicktime this method should
     * throw an exception with the appropriate error number.
     */
    public abstract void execute() throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/XMLEndDocProc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
