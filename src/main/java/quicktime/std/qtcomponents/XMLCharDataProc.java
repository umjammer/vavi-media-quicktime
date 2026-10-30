package quicktime.std.qtcomponents;

import quicktime.QTException;

/**
 * The XMLCharDataProc corresponds to the QuickTime CharDataHandler callback
 */
public interface XMLCharDataProc {

    /**
     * The method that is called by the XMLParser for parsing CharData events.
     * If any error occurs that should be provided back to quicktime this method should
     * throw an exception with the appropriate error number.
     */
    void execute(String charData) throws QTException;
}
