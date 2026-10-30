package quicktime.app.audio;

import java.util.Enumeration;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.time.Ticklish;
import quicktime.app.time.Timer;
import quicktime.std.clocks.TimeBase;
import quicktime.std.clocks.TimeCallBack;
import quicktime.std.music.NoteChannel;

/**
 * @deprecated
 * This class provides an implementation of the MusicPart interface for NoteChannel
 * objects.
 */
public class NoteChannelControl implements MusicPart {

    private final NoteChannel channel;

    private boolean muted;

    private float vol;

    private Timer t;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Create a new NoteChannelControl and a NoteChannel
     * with the specified instrument and polyphony of 1. The resultant
     * NoteChannel maybe able to play more than one note simultaneously.
     * @param gmNumber the specified general MIDI instrument number
     */
    public NoteChannelControl(int gmNumber) throws QTException {
        this(gmNumber, 1);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Create a new NoteChannelControl and a NoteChannel
     * with the specified instrument and polyphony.
     * @param gmNumber the specified general MIDI instrument number
     * @param poly the maximum number of notes that are expected to be played
     *  by the NoteChannel at the same time.
     */
    public NoteChannelControl(int gmNumber, int poly) throws QTException {
        this(new NoteChannel(gmNumber, poly));
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Create a new NoteChannelControl from the given NoteChannel
     * @param nc the given NoteChannel
     */
    public NoteChannelControl(NoteChannel nc) throws QTException {
        this.channel = nc;
        this.muted = (this.channel.getController(7) <= 0.0F);
        this.vol = getVolume();
    }

    private final Vector<EndNote> activeNotes = new Vector<>();

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
            this.channel.setController(7, 0.0F);
        } else {
            this.channel.setController(7, this.vol * 128.0F);
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
        if (!this.muted) {
            this.channel.setController(7, this.vol * 128.0F);
        }
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
        return this.muted ? this.vol : this.channel.getController(7) / 128.0F;
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
            this.channel.setController(10, val);
        } else {
            this.channel.setController(10, val / 2.0F + 1.5F);
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
        float f = this.channel.getController(10);
        if (f == 0.0F)
            return f;
        return (f - 1.5F) * 2.0F;
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
        this.channel.pickEditInstrument(prompt, 16);
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
        return this.channel.getNoteRequest().getToneDescription().getInstrumentName();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method gets the NoteChannel being controlled by this object.
     * @return the NoteChannel
     */
    @Override
    public final NoteChannel getNoteChannel() {
        return this.channel;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Plays the supplied note for the given duration.
     * @param note the note to play
     * @param velocity the intial volume of the note - how hard it is struck.
     * @param duration the number of milliseconds the note should play for
     * @throws quicktime.QTException
     */
    public void playNoteFor(float note, int velocity, int duration) throws QTException {
        if (this.t == null) {
            this.t = new Timer(1, 1, new TurnOffNotes());
            this.t.setTickling(false);
            this.t.setActive(true);
            this.t.setRate(1.0F);
        } else {
            this.t.setActive(true);
        }
        int i = this.t.getTimeBase().getTime(1000);
        this.channel.playNote(note, velocity);
        EndNote endNote = new EndNote(this.t.getTimeBase(), 1000, i + duration, 1, this.channel, note);
        endNote.callMeWhen();
        this.activeNotes.addElement(endNote);
    }

    class EndNote extends TimeCallBack {

        float note;

        NoteChannel nc;

        EndNote(TimeBase tb, int scale, int value, int flags, NoteChannel nc, float note) throws QTException {
            super(tb, scale, value, flags);
            this.note = note;
            this.nc = nc;
        }

        @Override
        public void execute() {
            try {
                this.nc.playNote(this.note, 0);
            } catch (QTException e) {
                e.printStackTrace();
            } finally {
                NoteChannelControl.this.activeNotes.removeElement(this);
                if (NoteChannelControl.this.activeNotes.isEmpty()) {
                    try {
                        NoteChannelControl.this.t.setActive(false);
                    } catch (QTException e) {
                        NoteChannelControl.this.t = null;
                    }
                }
                cancelAndCleanup();
            }
        }

        public boolean equals(Object qtObject) {
            if (qtObject == this)
                return true;
            if ((qtObject instanceof EndNote endNote)) {
                return (this.value == endNote.value) && (this.note == endNote.note) && (this.nc.equals(endNote.nc));
            }
            return false;
        }
    }

    class TurnOffNotes implements Ticklish {

        TurnOffNotes() {
        }

        @Override
        public void timeChanged(int newTime) throws QTException {
            Enumeration<EndNote> enumeration = NoteChannelControl.this.activeNotes.elements();
            while (enumeration.hasMoreElements()) enumeration.nextElement().execute();
        }

        @Override
        public boolean tickle(float er, int time) throws QTException {
            return false;
        }
    }

    protected final void dispose() throws Throwable {
        Enumeration<EndNote> enumeration = this.activeNotes.elements();
        while (enumeration.hasMoreElements()) enumeration.nextElement().cancelAndCleanup();
        this.activeNotes.removeAllElements();
    }
}
