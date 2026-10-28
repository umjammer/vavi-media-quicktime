package quicktime.std.qtcomponents;

import quicktime.QTException;

/**
 * The XMLPreprocessorProc corresponds to the QuickTime PreprocessInstructionHandler callback
 */
public interface XMLPreprocessorProc {

    /**
     * The method that is called by the XMLParser for parsing PreprocessorInstruction events.
     * If any error occurs that should be provided back to quicktime this method should
     * throw an exception with the appropriate error number.
     * @param name the element name
     * @param attributes an array of attribute-value pair strings
     * @throws quicktime.QTException
     */
    void execute(String name, String[] attributes) throws QTException;
}
