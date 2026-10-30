package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.std.movies.Track;
import quicktime.util.QTPointer;

/**
 * This class provides a representation the native QTSSourcerTrackParams structure that
 * specifies track parameters for the sourcer
 */
public final class SourcerTrackParams extends QTPointer {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 32;

    SourcerTrackParams() throws QTException {
        super(32, true);
    }

    SourcerTrackParams(byte[] byteArray) throws QTException {
        super(byteArray);
    }

    /**
     * Constructs a SourcerTrackParams object based on the native QTSSourcerTrackParams structure
     * @param flags the flags of the track
     * @param track the track associated with the sourcer
     */
    public SourcerTrackParams(int flags, Track track) throws QTException {
        this(1, flags, track, 0L, 0L, null);
    }

    /**
     * Constructs a SourcerTrackParams object based on the native QTSSourcerTrackParams structure
     * @param flags the flags of the track
     * @param track the track associated with the sourcer
     * @param trackStartOffset the offset of the start of the track (use 0 for beginning)
     * @param duration the limit of the track length (use 0 for no limit)
     * @param loopParams the loop properties of the track (use null for no looping)
     */
    public SourcerTrackParams(int flags, Track track, long trackStartOffset, long duration, SourcerLoopParams loopParams) throws QTException {
        this(1, flags, track, trackStartOffset, duration, loopParams);
    }

    /**
     * Constructs a SourcerTrackParams object based on the native QTSSourcerTrackParams structure
     * @param version version of the SourcerTrackParams object
     * @param flags the flags of the track
     * @param track the track associated with the sourcer
     * @param trackStartOffset the offset of the start of the track (use 0 for beginning)
     * @param duration the limit of the track length (use 0 for no limit)
     * @param loopParams the loop properties of the track (use null for no looping)
     */
    public SourcerTrackParams(int version, int flags, Track track, long trackStartOffset, long duration, SourcerLoopParams loopParams) throws QTException {
        super(32, true);
        setVersion(version);
        setFlags(flags);
        setTrack(track);
        setTrackStartOffset(trackStartOffset);
        setDuration(duration);
        setLoopParams(loopParams);
    }

    /**
     * Sets the version of the track params object
     * @param vers version
     */
    public void setVersion(int vers) {
        setIntAt(0, vers);
    }

    /**
     * Gets the version of the track params object
     * @return version
     */
    public int getVersion() {
        return getIntAt(0);
    }

    /**
     * Sets the flags associated with the track params object
     * @param flags flags
     */
    public void setFlags(int flags) {
        setIntAt(4, flags);
    }

    /**
     * Gets the flags of the track params object
     * @return flags
     */
    public int getFlags() {
        return getIntAt(4);
    }

    /**
     * Sets the track of the track params object
     * @param track track (should not be null)
     * @throws quicktime.QTRuntimeException
     */
    public void setTrack(Track track) throws QTRuntimeException {
        if (track != null) {
            setIntAt(8, QTObject.ID(track));
            this.itsTrack = track;
        } else {
            throw new QTRuntimeException("Track must not be null");
        }
    }

    /**
     * Gets the track
     * @return track
     */
    public Track getTrack() {
        return this.itsTrack;
    }

    /**
     * Sets the track starting offset (0 indicates the start of the track)
     */
    public void setTrackStartOffset(long trackStartOffset) {
        setLongAt(12, trackStartOffset);
    }

    /**
     * Gets the track start offset
     */
    public long getTrackStartOffset() {
        return getLongAt(12);
    }

    /**
     * Sets the duration limit of the track (0 indicates no limit)
     * @param duration limit of the track duration
     */
    public void setDuration(long duration) {
        setLongAt(20, duration);
    }

    /**
     * Gets the duration limit
     */
    public long getDuration() {
        return getLongAt(20);
    }

    /**
     * Sets the loop parameters of the track parameters object
     * @param params the loop parameters
     */
    public void setLoopParams(SourcerLoopParams params) {
        if (params != null) {
            setIntAt(28, QTObject.ID(params));
            this.itsParams = params;
        } else {
            setIntAt(28, 0);
            this.itsParams = null;
        }
    }

    /**
     * Gets the loop paramters object
     */
    public SourcerLoopParams getLoopParams() {
        return this.itsParams;
    }

    private Track itsTrack = null;

    private SourcerLoopParams itsParams = null;

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",flags=" + getFlags() + ",track=" + getTrack() + ",trackStartOffset" + getTrackStartOffset() + ",duration" + getDuration() + ",loopParams" + getLoopParams() + "]";
    }
}
