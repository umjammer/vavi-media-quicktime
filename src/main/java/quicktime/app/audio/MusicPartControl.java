package quicktime.app.audio;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.std.movies.media.MusicMediaHandler;
import quicktime.std.music.NoteChannel;
import quicktime.std.music.TunePlayer;

/**
 * Deprecated.
 * This class provides an implementation of the ExtendedAudioSpec interface
 * for dealing with specific charactertics of the parts of the MusicMedia.
 *
 * Currently any actions on the part do not reset the MusicMedia with which
 * they are derived from - any changes that are made to a part are only active
 * for an unpredicatable period of time as they do not interact on the Media
 * object itself.
 */
public class MusicPartControl implements MusicPart {

    private TunePlayer tp;

    private boolean muted;

    private float vol;

    private int part;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create a new MusicPartControl from the given MusicMediaHandler.
     * @param mh the MusicMediaHandler to extract the part from
     * @param partNumber the part number within the MusicMedia to control
     */
    public MusicPartControl(MusicMediaHandler paramMusicMediaHandler, int paramInt) throws QTException {
        this.tp = paramMusicMediaHandler.getIndexedTunePlayer(1);
        this.part = paramInt;
        this.muted = (getNoteChannel().getController(7) <= 0.0F);
        this.vol = getVolume();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current mute status
     * @return the current mute status
     */
    public boolean isMuted() {
        return this.muted;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Allows the muting of the specific channel. When unmuted the AudioSpec object will return
     * to its current volume setting.
     * @param flag turn muting on - true or off - false.
     * @param {@link quicktime.QTException}
     */
    public void setMuted(boolean paramBoolean) throws QTException {
        this.muted = paramBoolean;
        if (this.muted) {
            getNoteChannel().setController(7, 0.0F);
        } else {
            getNoteChannel().setController(7, this.vol * 128.0F);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method will set the volume of this object.
     * @param volume the desired volume (range: 0.0F - 1.0F)
     * @param {@link quicktime.QTException}
     */
    public void setVolume(float paramFloat) throws QTException {
        this.vol = paramFloat;
        getNoteChannel().setController(7, this.vol * 128.0F);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method will get the volume of this object. (range: 0.0 - 1.0)
     * @return the current volume.
     * @return {@link quicktime.QTException}
     */
    public float getVolume() throws QTException {
        return this.muted ? 0.0F : getNoteChannel().getController(7) / 128.0F;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the balance of this object.
     * @param val the desired balance (range: -1F -&gt; 1F)
     * @param {@link quicktime.QTException}
     */
    public void setBalance(float paramFloat) throws QTException {
        if (paramFloat == 0.0F) {
            getNoteChannel().setController(10, paramFloat);
        } else {
            getNoteChannel().setController(10, paramFloat / 2.0F + 1.5F);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method gets the balance of the this object. (range: -1.0 -> 1.0)
     * @return the balance
     * @return {@link quicktime.QTException}
     */
    public float getBalance() throws QTException {
        float f = getNoteChannel().getController(10);
        if (f == 0.0F)
            return f;
        return (f - 1.5F) * 2.0F;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the name of the current instrument for this part.
     * @return name of the current instrument for this part
     * @return {@link quicktime.QTException}
     */
    public String getInstrumentName() throws QTException {
        return getNoteChannel().getNoteRequest().getToneDescription().getInstrumentName();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method allows for the class to allow some selection
     * process whereby the user would choose a particular instrument
     * that will be consequently used by a MusicPart to produce the
     * sounds that it plays.
     * @param prompt - any information that the program wants to display
     *  to the user to aid in the selection process.
     * @param {@link quicktime.QTException}
     */
    public void selectInstrument(String paramString) throws QTException {
        getNoteChannel().pickEditInstrument(paramString, 16);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method gets the NoteChannel being controlled by this object.
     * @return the NoteChannel
     * @return {@link quicktime.QTException}
     */
    public final NoteChannel getNoteChannel() throws QTException {
        return this.tp.getIndexedNoteChannel(this.part);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/audio
 * /MusicPartControl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
