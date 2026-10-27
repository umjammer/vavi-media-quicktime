package quicktime.streaming;

import quicktime.QTException;
import quicktime.util.QTPointerRef;
import quicktime.util.QTVector;

/**
 * This class represents an EditListHandle structure which is a vector of EditEntry objects
 */
public class EditList extends QTVector {

    /**
     * Creates a EditList which is an array of EditEntry objects.
     * @param numberOfElements This is the number of elements the Vector will hold - it is NOT growable
     */
    public EditList(int paramInt) throws QTException {
        super(20, paramInt > 0 ? paramInt : 1);
    }

    /**
     * Inserts an EditEntry into the vector at specified position.
     * {@link quicktime.QTException}
     * java.lang.ArrayIndexOutOfBoundsException
     */
    public void insert(EditEntry paramEditEntry, int paramInt) throws QTException, ArrayIndexOutOfBoundsException {
        insertElement(paramEditEntry, paramInt);
    }

    /**
     * Get a EditEntry at specified position.
     * java.lang.ArrayIndexOutOfBoundsException
     */
    public EditEntry get(int paramInt) throws ArrayIndexOutOfBoundsException, QTException {
        EditEntry localEditEntry = new EditEntry();
        getElement(localEditEntry, paramInt);
        return localEditEntry;
    }

    @Override
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
