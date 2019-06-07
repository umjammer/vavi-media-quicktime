package quicktime.app.audio;

import quicktime.std.movies.media.Media;

/**
 * Deprecated.
 * This is a very simple implementation of the ExtendedAudioSpec interface
 * to control Media that has an audio media component.
 */
public class AudioMediaControl implements ExtendedAudioSpec {

    /**
     * Deprecated.
     * This is the audioMedia object that is represented by this class * @deprecated since QTJava 6.1
     */
    protected Media audioMedia;

    /**
     * Deprecated.
     * This the AudioMediaHandler that is the MediaHandler for the AudioMedia * @deprecated since QTJava 6.1
     */
    protected quicktime.std.movies.media.AudioMediaHandler audioHandler;

    private boolean muted;

    static {
        if ((quicktime.QTSession.isCurrentOS(4)) && (quicktime.QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The constructor that deals with Media objects.
     * The media object must have an AudioMediaHandler as its media handler
     * or an exception will be thrown
     * @param media the Media object to control
     */
    public AudioMediaControl(Media paramMedia) throws quicktime.QTException {
        this(paramMedia, (quicktime.std.movies.media.AudioMediaHandler) paramMedia.getHandler());
    }

    private AudioMediaControl(Media paramMedia, quicktime.std.movies.media.AudioMediaHandler paramAudioMediaHandler) throws quicktime.QTException {
        this.audioMedia = paramMedia;
        this.audioHandler = paramAudioMediaHandler;
        this.muted = (this.audioMedia.getTrack().getVolume() < 0.0F);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the media controlled by this object
     * @return the media
     */
    public Media getMedia() {
        return this.audioMedia;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns whether or not the media is currently muted.
     * @return the muted state of the media
     */
    public boolean isMuted() {
        return this.muted;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the muted state of this media.
     * @param mute the desired muted state
     * @param {@link quicktime.QTException}
     */
    public void setMuted(boolean paramBoolean) throws quicktime.QTException {
        float f = this.audioMedia.getTrack().getVolume();
        if (paramBoolean != this.muted) {
            this.audioMedia.getTrack().setVolume(-f);
            this.muted = paramBoolean;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current volume of the media (range: 0.0 - 1.0)
     * @return the current volume.
     * @return {@link quicktime.QTException}
     */
    public float getVolume() throws quicktime.QTException {
        float f = this.audioMedia.getTrack().getVolume();
        return Math.abs(f);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the volume of the media (range: 0.0 - 1.0)
     * @param vol the desired volume
     * @param {@link quicktime.QTException}
     */
    public void setVolume(float paramFloat) throws quicktime.QTException {
        if (paramFloat < 0.0F)
            throw new quicktime.QTException("Illegal volume");
        if (false == this.muted) {
            this.audioMedia.getTrack().setVolume(paramFloat);
        } else {
            this.audioMedia.getTrack().setVolume(-paramFloat);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method gets the current balance for this media. Values range from
     * -1.0 (left) to 0.0 (center) to 1.0 (right)
     * @return the current balance
     * @return {@link quicktime.QTException}
     */
    public float getBalance() throws quicktime.QTException {
        return this.audioHandler.getBalance();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the balance for this media.  Valid values range from
     * -1.0 (left) to 0.0 (center) to 1.0 (right)
     * @param val the desired balance
     * @param {@link quicktime.QTException}
     */
    public void setBalance(float paramFloat) throws quicktime.QTException {
        this.audioHandler.setBalance(paramFloat);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/audio
 * /AudioMediaControl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
