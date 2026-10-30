package quicktime.std.movies.media;

import quicktime.std.movies.TimeInfo;

/**
 * The SampleInfo class contains information about the reference information of a media sample.
 */
public class SampleInfo extends TimeInfo {

    /**
     * The media sample's offset.
     */
    public final int offset;

    /**
     * The media sample's data size.
     */
    public final int size;

    /**
     * The number of samples.
     */
    public final int numberOfSamples;

    /**
     * The media sample's sample description. This will generally be a subclass of SampleDescription
     * according to the type of media the sample deals with. This may also be null if the media sample
     * does not require sample description information.
     */
    public final SampleDescription description;

    /**
     * The flags for the media samples.
     */
    public final int flags;

    /**
     * The media sample's sample description index.
     */
    public final int descriptionIndex;

    SampleInfo(int time, int duration, int offset, int size, int numberOfSamples, SampleDescription description, int descriptionIndex, int flags) {
        super(time, duration);
        this.offset = offset;
        this.size = size;
        this.numberOfSamples = numberOfSamples;
        this.description = description;
        this.flags = flags;
        this.descriptionIndex = descriptionIndex;
    }

    /**
     * Returns a string representation of this class.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[time=" + this.time + ",duration=" + this.duration + ",offset=" + this.offset + ",size=" + this.size + ",numSamples=" + this.numberOfSamples + ",flags=" + this.flags + ",description=" + this.description + ",descriptionIndex=" + this.descriptionIndex + "]";
    }
}
