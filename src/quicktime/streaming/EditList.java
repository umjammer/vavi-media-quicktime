
package quicktime.streaming;

import quicktime.QTException;
import quicktime.util.QTPointerRef;
import quicktime.util.QTVector;


public class EditList extends QTVector {
    public EditList(int paramInt) throws QTException {
        super(20, paramInt > 0 ? paramInt : 1);
    }

    public void insert(EditEntry paramEditEntry, int paramInt) throws QTException, ArrayIndexOutOfBoundsException {
        insertElement(paramEditEntry, paramInt);
    }

    public EditEntry get(int paramInt) throws ArrayIndexOutOfBoundsException, QTException {
        EditEntry localEditEntry = new EditEntry();
        getElement(localEditEntry, paramInt);
        return localEditEntry;
    }

    protected QTPointerRef makeElement() {
        EditEntry localEditEntry = null;
        try {
            localEditEntry = new EditEntry();
        } catch (QTException localQTException) {
            localEditEntry = null;
        }
        return localEditEntry;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /EditList.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
