package quicktime.std.qtcomponents;

import quicktime.QTException;

/**
 * The XMLPreprocessorProc corresponds to the QuickTime PreprocessInstructionHandler callback
 */
public abstract interface XMLPreprocessorProc {

    /**
     * The method that is called by the XMLParser for parsing PreprocessorInstruction events.
     * If any error occurs that should be provided back to quicktime this method should
     * throw an exception with the appropriate error number.
     * @param name the element name
     * @param attributes an array of attribute-value pair strings
     * @param {@link quicktime.QTException}
     */
    public abstract void execute(String paramString, String[] paramArrayOfString) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/XMLPreprocessorProc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
