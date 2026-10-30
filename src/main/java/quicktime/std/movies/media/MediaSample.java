package quicktime.std.movies.media;

import quicktime.util.QTHandle;

/**
 * The MediaSample class contains media information and data.
 * The fields in the MediaSample describes how the raw data in the handle is to be interpreted
 */
public final class MediaSample extends SampleInfo {

    /**
     * The media sample's data handle.
     */
    public final QTHandle data;

    MediaSample(int time, int duration, QTHandle data, int offset, int size, int numberOfSamples, SampleDescription description, int descriptionIndex, int flags) {
        super(time, duration, offset, size, numberOfSamples, description, descriptionIndex, flags);
        this.data = data;
    }

    /**
     * Returns a string representation of this class.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[time=" + this.time + ",duration=" + this.duration + ",offset=" + this.offset + ",size=" + this.size + ",numSamples=" + this.numberOfSamples + ",flags=" + this.flags + ",description=" + this.description + ",descriptionIndex=" + this.descriptionIndex + ",data=" + this.data + "]";
    }
}
