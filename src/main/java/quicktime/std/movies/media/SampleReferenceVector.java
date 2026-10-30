package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.util.QTPointerRef;
import quicktime.util.QTVector;

/**
 * This class represents a SampleReferenceVector which is an array of SampleReference objects.
 */
public class SampleReferenceVector extends QTVector {

    /**
     * Creates a SampleReferenceVector which is an array of SampleReference objects.
     * @param numberOfElements This is the number of elements the Vector will hold - it is NOT growable
     */
    public SampleReferenceVector(int numberOfElements) throws QTException {
        super(18, numberOfElements > 0 ? numberOfElements : 1);
    }

    void _setNumSamples(int numElements) {
        this.mNumElements = numElements;
    }

    /**
     * Inserts a SampleReference into the vector at specified position.
     * {@link quicktime.QTException}
     * java.lang.ArrayIndexOutOfBoundsException
     */
    public void insert(SampleReference sampleReference, int index) throws QTException, ArrayIndexOutOfBoundsException {
        insertElement(sampleReference, index);
    }

    /**
     * Get a SampleReference at specified position.
     * java.lang.ArrayIndexOutOfBoundsException
     */
    public SampleReference get(int index) throws ArrayIndexOutOfBoundsException, QTException {
        SampleReference sampleReference = new SampleReference();
        getElement(sampleReference, index);
        return sampleReference;
    }

    @Override
    protected QTPointerRef makeElement() {
        SampleReference element = null;
        try {
            element = new SampleReference();
        } catch (Throwable e) {
            element = null;
        }
        return element;
    }
}
