package quicktime.std.movies.media;

import quicktime.std.movies.TimeInfo;

/**
 * The SampleInfo class contains information about the reference information of a media sample.
 */
public class SampleInfo extends TimeInfo {

    /**
     * The media sample's offset.
     */
    public int offset;

    /**
     * The media sample's data size.
     */
    public int size;

    /**
     * The number of samples.
     */
    public int numberOfSamples;

    /**
     * The media sample's sample description. This will generally be a subclass of SampleDescription
     * according to the type of media the sample deals with. This may also be null if the media sample
     * does not require sample description information.
     */
    public SampleDescription description;

    /**
     * The flags for the media samples.
     */
    public int flags;

    /**
     * The media sample's sample description index.
     */
    public int descriptionIndex;

    SampleInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, SampleDescription paramSampleDescription, int paramInt6, int paramInt7) {
        super(paramInt1, paramInt2);
        this.offset = paramInt3;
        this.size = paramInt4;
        this.numberOfSamples = paramInt5;
        this.description = paramSampleDescription;
        this.flags = paramInt7;
        this.descriptionIndex = paramInt6;
    }

    /**
     * Returns a string representation of this class.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[time=" + this.time + ",duration=" + this.duration + ",offset=" + this.offset + ",size=" + this.size + ",numSamples=" + this.numberOfSamples + ",flags=" + this.flags + ",description=" + this.description + ",descriptionIndex=" + this.descriptionIndex + "]";
    }
}

