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
    public EditList(int numberOfElements) throws QTException {
        super(20, numberOfElements > 0 ? numberOfElements : 1);
    }

    /**
     * Inserts an EditEntry into the vector at specified position.
     * {@link quicktime.QTException}
     * java.lang.ArrayIndexOutOfBoundsException
     */
    public void insert(EditEntry editEntry, int index) throws QTException, ArrayIndexOutOfBoundsException {
        insertElement(editEntry, index);
    }

    /**
     * Get a EditEntry at specified position.
     * java.lang.ArrayIndexOutOfBoundsException
     */
    public EditEntry get(int index) throws ArrayIndexOutOfBoundsException, QTException {
        EditEntry editEntry = new EditEntry();
        getElement(editEntry, index);
        return editEntry;
    }

    @Override
    protected QTPointerRef makeElement() {
        EditEntry element = null;
        try {
            element = new EditEntry();
        } catch (QTException e) {
            element = null;
        }
        return element;
    }
}
