package quicktime.std.qtcomponents;

import quicktime.QTException;

/**
 * The XMLStartDocProc corresponds to the QuickTime StartDocumentHandler callback
 */
public abstract interface XMLStartDocProc {

    /**
     * The method that is called by the XMLParser for parsing the StartDocument event.
     * If any error occurs that should be provided back to quicktime this method should
     * throw an exception with the appropriate error number.
     */
    public abstract void execute() throws QTException;
}

