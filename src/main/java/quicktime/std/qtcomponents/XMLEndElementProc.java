package quicktime.std.qtcomponents;

import quicktime.QTException;

/**
 * The XMLEndElementProc corresponds to the QuickTime EndElementHandler callback
 */
public abstract interface XMLEndElementProc {

    /**
     * The method that is called by the XMLParser for parsing EndElement events.
     * If any error occurs that should be provided back to quicktime this method should
     * throw an exception with the appropriate error number.
     */
    public abstract void execute(String paramString) throws QTException;
}

