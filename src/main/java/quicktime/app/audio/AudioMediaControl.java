package quicktime.app.audio;

import quicktime.std.movies.media.Media;

/**
 * @deprecated
 * This is a very simple implementation of the ExtendedAudioSpec interface
 * to control Media that has an audio media component.
 */
public class AudioMediaControl implements ExtendedAudioSpec {

    /**
     * @deprecated
     * This is the audioMedia object that is represented by this class * @deprecated since QTJava 6.1
     */
    protected Media audioMedia;

    /**
     * @deprecated
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
     * @deprecated
     * @since QTJava 6.1
     * The constructor that deals with Media objects.
     * The media object must have an AudioMediaHandler as its media handler
     * or an exception will be thrown
     * @param media the Media object to control
     */
    public AudioMediaControl(Media media) throws quicktime.QTException {
        this(media, (quicktime.std.movies.media.AudioMediaHandler) media.getHandler());
    }

    private AudioMediaControl(Media paramMedia, quicktime.std.movies.media.AudioMediaHandler paramAudioMediaHandler) throws quicktime.QTException {
        this.audioMedia = paramMedia;
        this.audioHandler = paramAudioMediaHandler;
        this.muted = (this.audioMedia.getTrack().getVolume() < 0.0F);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the media controlled by this object
     * @return the media
     */
    public Media getMedia() {
        return this.audioMedia;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns whether or not the media is currently muted.
     * @return the muted state of the media
     */
    @Override
    public boolean isMuted() {
        return this.muted;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the muted state of this media.
     * @param mute the desired muted state
     * @throws quicktime.QTException
     */
    @Override
    public void setMuted(boolean mute) throws quicktime.QTException {
        float f = this.audioMedia.getTrack().getVolume();
        if (mute != this.muted) {
            this.audioMedia.getTrack().setVolume(-f);
            this.muted = mute;
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current volume of the media (range: 0.0 - 1.0)
     * @return the current volume.
     * @throws quicktime.QTException
     */
    @Override
    public float getVolume() throws quicktime.QTException {
        float f = this.audioMedia.getTrack().getVolume();
        return Math.abs(f);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method sets the volume of the media (range: 0.0 - 1.0)
     * @param vol the desired volume
     * @throws quicktime.QTException
     */
    @Override
    public void setVolume(float vol) throws quicktime.QTException {
        if (vol < 0.0F)
            throw new quicktime.QTException("Illegal volume");
        if (false == this.muted) {
            this.audioMedia.getTrack().setVolume(vol);
        } else {
            this.audioMedia.getTrack().setVolume(-vol);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method gets the current balance for this media. Values range from
     * -1.0 (left) to 0.0 (center) to 1.0 (right)
     * @return the current balance
     * @throws quicktime.QTException
     */
    @Override
    public float getBalance() throws quicktime.QTException {
        return this.audioHandler.getBalance();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method sets the balance for this media.  Valid values range from
     * -1.0 (left) to 0.0 (center) to 1.0 (right)
     * @param val the desired balance
     * @throws quicktime.QTException
     */
    @Override
    public void setBalance(float val) throws quicktime.QTException {
        this.audioHandler.setBalance(val);
    }
}
