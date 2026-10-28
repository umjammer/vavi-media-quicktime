package quicktime.std.movies.media;

/**
 * The SampleReferenceInfo class contains information about media samples.
 */
public final class SampleReferenceInfo {

    /**
     * The sample time.
     */
    public final int sampleTime;

    /**
     * The sample description index.
     */
    public final int descriptionIndex;

    /**
     * The sample description object for the media samples of this sample reference.
     */
    public final SampleDescription sampleDescription;

    /**
     * A vector of sample reference objects.
     */
    public final SampleReferenceVector sampleRefs;

    SampleReferenceInfo(int paramInt1, SampleDescription paramSampleDescription, int paramInt2, SampleReferenceVector paramSampleReferenceVector) {
        this.sampleRefs = paramSampleReferenceVector;
        this.sampleTime = paramInt1;
        this.descriptionIndex = paramInt2;
        this.sampleDescription = paramSampleDescription;
    }
}
