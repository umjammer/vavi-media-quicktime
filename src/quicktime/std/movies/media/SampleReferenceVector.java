
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.util.QTPointerRef;
import quicktime.util.QTVector;


public class SampleReferenceVector extends QTVector {
    public SampleReferenceVector(int paramInt) throws QTException {
        super(18, paramInt > 0 ? paramInt : 1);
    }

    void _setNumSamples(int paramInt) {
        this.mNumElements = paramInt;
    }

    public void insert(SampleReference paramSampleReference, int paramInt) throws QTException, ArrayIndexOutOfBoundsException {
        insertElement(paramSampleReference, paramInt);
    }

    public SampleReference get(int paramInt) throws ArrayIndexOutOfBoundsException, QTException {
        SampleReference localSampleReference = new SampleReference();
        getElement(localSampleReference, paramInt);
        return localSampleReference;
    }

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

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SampleReferenceVector.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
