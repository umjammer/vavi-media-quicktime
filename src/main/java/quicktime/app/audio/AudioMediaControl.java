
package quicktime.app.audio;

import quicktime.std.movies.media.Media;


/**
 * @deprecated
 */
public class AudioMediaControl implements ExtendedAudioSpec {
    protected Media audioMedia;

    protected quicktime.std.movies.media.AudioMediaHandler audioHandler;

    private boolean muted;

    static {
        if ((quicktime.QTSession.isCurrentOS(4)) && (quicktime.QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public AudioMediaControl(Media paramMedia) throws quicktime.QTException {
        this(paramMedia, (quicktime.std.movies.media.AudioMediaHandler) paramMedia.getHandler());
    }

    private AudioMediaControl(Media paramMedia,
            quicktime.std.movies.media.AudioMediaHandler paramAudioMediaHandler) throws quicktime.QTException {
        this.audioMedia = paramMedia;
        this.audioHandler = paramAudioMediaHandler;
        this.muted = (this.audioMedia.getTrack().getVolume() < 0.0F);
    }

    /**
     * @deprecated
     */
    public Media getMedia() {
        return this.audioMedia;
    }

    /**
     * @deprecated
     */
    public boolean isMuted() {
        return this.muted;
    }

    /**
     * @deprecated
     */
    public void setMuted(boolean paramBoolean) throws quicktime.QTException {
        float f = this.audioMedia.getTrack().getVolume();
        if (paramBoolean != this.muted) {
            this.audioMedia.getTrack().setVolume(-f);
            this.muted = paramBoolean;
        }
    }

    /**
     * @deprecated
     */
    public float getVolume() throws quicktime.QTException {
        float f = this.audioMedia.getTrack().getVolume();
        return Math.abs(f);
    }

    /**
     * @deprecated
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
     * @deprecated
     */
    public float getBalance() throws quicktime.QTException {
        return this.audioHandler.getBalance();
    }

    /**
     * @deprecated
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
