package quicktime.std.qtcomponents;

import quicktime.QTException;

/**
 * The XMLPreprocessorProc corresponds to the QuickTime PreprocessInstructionHandler callback
 */
public abstract interface XMLPreprocessorProc {

    public abstract void execute(String paramString, String[] paramArrayOfString) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/XMLPreprocessorProc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
