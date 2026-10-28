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

    void _setNumSamples(int paramInt) {
        this.mNumElements = paramInt;
    }

    /**
     * Inserts a SampleReference into the vector at specified position.
     * {@link quicktime.QTException}
     * java.lang.ArrayIndexOutOfBoundsException
     */
    public void insert(SampleReference paramSampleReference, int paramInt) throws QTException, ArrayIndexOutOfBoundsException {
        insertElement(paramSampleReference, paramInt);
    }

    /**
     * Get a SampleReference at specified position.
     * java.lang.ArrayIndexOutOfBoundsException
     */
    public SampleReference get(int paramInt) throws ArrayIndexOutOfBoundsException, QTException {
        SampleReference localSampleReference = new SampleReference();
        getElement(localSampleReference, paramInt);
        return localSampleReference;
    }

    @Override
    protected QTPointerRef makeElement() {
        SampleReference localSampleReference = null;
        try {
            localSampleReference = new SampleReference();
        } catch (Throwable localThrowable) {
            localSampleReference = null;
        }
        return localSampleReference;
    }
}

