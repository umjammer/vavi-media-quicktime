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

