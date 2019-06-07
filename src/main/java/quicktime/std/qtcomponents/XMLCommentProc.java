package quicktime.std.qtcomponents;

import quicktime.QTException;

/**
 * The XMLCommentProc corresponds to the QuickTime CommentHandler callback
 */
public abstract interface XMLCommentProc {

    /**
     * The method that is called by the XMLParser for parsing Comment events.
     * If any error occurs that should be provided back to quicktime this method should
     * throw an exception with the appropriate error number.
     */
    public abstract void execute(String paramString) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/XMLCommentProc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
