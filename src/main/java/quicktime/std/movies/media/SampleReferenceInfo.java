package quicktime.std.movies.media;

/**
 * The SampleReferenceInfo class contains information about media samples.
 */
public final class SampleReferenceInfo {

    /**
     * The sample time.
     */
    public int sampleTime;

    /**
     * The sample description index.
     */
    public int descriptionIndex;

    /**
     * The sample description object for the media samples of this sample reference.
     */
    public SampleDescription sampleDescription;

    /**
     * A vector of sample reference objects.
     */
    public SampleReferenceVector sampleRefs;

    SampleReferenceInfo(int paramInt1, SampleDescription paramSampleDescription, int paramInt2, SampleReferenceVector paramSampleReferenceVector) {
        this.sampleRefs = paramSampleReferenceVector;
        this.sampleTime = paramInt1;
        this.descriptionIndex = paramInt2;
        this.sampleDescription = paramSampleDescription;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SampleReferenceInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
