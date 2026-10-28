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

    MediaSample(int paramInt1, int paramInt2, QTHandle paramQTHandle, int paramInt3, int paramInt4, int paramInt5, SampleDescription paramSampleDescription, int paramInt6, int paramInt7) {
        super(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramSampleDescription, paramInt6, paramInt7);
        this.data = paramQTHandle;
    }

    /**
     * Returns a string representation of this class.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[time=" + this.time + ",duration=" + this.duration + ",offset=" + this.offset + ",size=" + this.size + ",numSamples=" + this.numberOfSamples + ",flags=" + this.flags + ",description=" + this.description + ",descriptionIndex=" + this.descriptionIndex + ",data=" + this.data + "]";
    }
}
