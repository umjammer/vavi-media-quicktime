package quicktime.app.audio;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.std.movies.media.MusicMediaHandler;
import quicktime.std.music.NoteChannel;
import quicktime.std.music.TunePlayer;

/**
 * @deprecated
 * This class provides an implementation of the ExtendedAudioSpec interface
 * for dealing with specific characteristics of the parts of the MusicMedia.
 *
 * Currently, any actions on the part do not reset the MusicMedia with which
 * they are derived from - any changes that are made to a part are only active
 * for an unpredictable period of time as they do not interact on the Media
 * object itself.
 */
public class MusicPartControl implements MusicPart {

    private final TunePlayer tp;

    private boolean muted;

    private float vol;

    private final int part;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Create a new MusicPartControl from the given MusicMediaHandler.
     * @param mh the MusicMediaHandler to extract the part from
     * @param partNumber the part number within the MusicMedia to control
     */
    public MusicPartControl(MusicMediaHandler mh, int partNumber) throws QTException {
        this.tp = mh.getIndexedTunePlayer(1);
        this.part = partNumber;
        this.muted = (getNoteChannel().getController(7) <= 0.0F);
        this.vol = getVolume();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current mute status
     * @return the current mute status
     */
    @Override
    public boolean isMuted() {
        return this.muted;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Allows the muting of the specific channel. When unmuted the AudioSpec object will return
     * to its current volume setting.
     * @param flag turn muting on - true or off - false.
     * @throws quicktime.QTException
     */
    @Override
    public void setMuted(boolean flag) throws QTException {
        this.muted = flag;
        if (this.muted) {
            getNoteChannel().setController(7, 0.0F);
        } else {
            getNoteChannel().setController(7, this.vol * 128.0F);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method will set the volume of this object.
     * @param volume the desired volume (range: 0.0F - 1.0F)
     * @throws quicktime.QTException
     */
    @Override
    public void setVolume(float volume) throws QTException {
        this.vol = volume;
        getNoteChannel().setController(7, this.vol * 128.0F);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method will get the volume of this object. (range: 0.0 - 1.0)
     * @return the current volume.
     * @throws quicktime.QTException
     */
    @Override
    public float getVolume() throws QTException {
        return this.muted ? 0.0F : getNoteChannel().getController(7) / 128.0F;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method sets the balance of this object.
     * @param val the desired balance (range: -1F -&gt; 1F)
     * @throws quicktime.QTException
     */
    @Override
    public void setBalance(float val) throws QTException {
        if (val == 0.0F) {
            getNoteChannel().setController(10, val);
        } else {
            getNoteChannel().setController(10, val / 2.0F + 1.5F);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method gets the balance of the this object. (range: -1.0 -> 1.0)
     * @return the balance
     * @throws quicktime.QTException
     */
    @Override
    public float getBalance() throws QTException {
        float f = getNoteChannel().getController(10);
        if (f == 0.0F)
            return f;
        return (f - 1.5F) * 2.0F;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the name of the current instrument for this part.
     * @return name of the current instrument for this part
     * @throws quicktime.QTException
     */
    @Override
    public String getInstrumentName() throws QTException {
        return getNoteChannel().getNoteRequest().getToneDescription().getInstrumentName();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method allows for the class to allow some selection
     * process whereby the user would choose a particular instrument
     * that will be consequently used by a MusicPart to produce the
     * sounds that it plays.
     * @param prompt - any information that the program wants to display
     *  to the user to aid in the selection process.
     * @throws quicktime.QTException
     */
    @Override
    public void selectInstrument(String prompt) throws QTException {
        getNoteChannel().pickEditInstrument(prompt, 16);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method gets the NoteChannel being controlled by this object.
     * @return the NoteChannel
     * @throws quicktime.QTException
     */
    @Override
    public final NoteChannel getNoteChannel() throws QTException {
        return this.tp.getIndexedNoteChannel(this.part);
    }
}
