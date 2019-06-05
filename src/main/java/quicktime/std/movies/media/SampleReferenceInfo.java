
package quicktime.std.movies.media;

public final class SampleReferenceInfo {
    public int sampleTime;

    public int descriptionIndex;

    public SampleDescription sampleDescription;

    public SampleReferenceVector sampleRefs;

    SampleReferenceInfo(int paramInt1,
            SampleDescription paramSampleDescription,
            int paramInt2,
            SampleReferenceVector paramSampleReferenceVector) {
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
