
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
 */
public class NoteChannelControl implements MusicPart {
    private NoteChannel channel;

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
     */
    public NoteChannelControl(int paramInt) throws QTException {
        this(paramInt, 1);
    }

    /**
     * @deprecated
     */
    public NoteChannelControl(int paramInt1, int paramInt2) throws QTException {
        this(new NoteChannel(paramInt1, paramInt2));
    }

    /**
     * @deprecated
     */
    public NoteChannelControl(NoteChannel paramNoteChannel) throws QTException {
        this.channel = paramNoteChannel;
        this.muted = (this.channel.getController(7) <= 0.0F);
        this.vol = getVolume();
    }

    private Vector activeNotes = new Vector();

    /**
     * @deprecated
     */
    public boolean isMuted() {
        return this.muted;
    }

    /**
     * @deprecated
     */
    public void setMuted(boolean paramBoolean) throws QTException {
        this.muted = paramBoolean;
        if (this.muted) {
            this.channel.setController(7, 0.0F);
        } else {
            this.channel.setController(7, this.vol * 128.0F);
        }
    }

    /**
     * @deprecated
     */
    public void setVolume(float paramFloat) throws QTException {
        this.vol = paramFloat;
        if (!this.muted) {
            this.channel.setController(7, this.vol * 128.0F);
        }
    }

    /**
     * @deprecated
     */
    public float getVolume() throws QTException {
        return this.muted ? this.vol : this.channel.getController(7) / 128.0F;
    }

    /**
     * @deprecated
     */
    public void setBalance(float paramFloat) throws QTException {
        if (paramFloat == 0.0F) {
            this.channel.setController(10, paramFloat);
        } else {
            this.channel.setController(10, paramFloat / 2.0F + 1.5F);
        }
    }

    /**
     * @deprecated
     */
    public float getBalance() throws QTException {
        float f = this.channel.getController(10);
        if (f == 0.0F)
            return f;
        return (f - 1.5F) * 2.0F;
    }

    /**
     * @deprecated
     */
    public void selectInstrument(String paramString) throws QTException {
        this.channel.pickEditInstrument(paramString, 16);
    }

    /**
     * @deprecated
     */
    public String getInstrumentName() throws QTException {
        return this.channel.getNoteRequest().getToneDescription().getInstrumentName();
    }

    /**
     * @deprecated
     */
    public final NoteChannel getNoteChannel() {
        return this.channel;
    }

    /**
     * @deprecated
     */
    public void playNoteFor(float paramFloat, int paramInt1, int paramInt2) throws QTException {
        if (this.t == null) {
            this.t = new Timer(1, 1, new TurnOffNotes());
            this.t.setTickling(false);
            this.t.setActive(true);
            this.t.setRate(1.0F);
        } else {
            this.t.setActive(true);
        }
        int i = this.t.getTimeBase().getTime(1000);
        this.channel.playNote(paramFloat, paramInt1);

        EndNote localEndNote = new EndNote(this.t.getTimeBase(), 1000, i + paramInt2, 1, this.channel, paramFloat);
        localEndNote.callMeWhen();
        this.activeNotes.addElement(localEndNote);
    }

    class EndNote extends TimeCallBack {
        float note;

        NoteChannel nc;

        EndNote(TimeBase paramTimeBase,
                int paramInt1,
                int paramInt2,
                int paramInt3,
                NoteChannel paramNoteChannel,
                float paramFloat) throws QTException {
            super(paramTimeBase, paramInt1, paramInt2, paramInt3);
            this.note = paramFloat;
            this.nc = paramNoteChannel;
        }

        public void execute() {
            try {
                this.nc.playNote(this.note, 0);
            } catch (QTException localQTException1) {
                localQTException1.printStackTrace();
            } finally {
                NoteChannelControl.this.activeNotes.removeElement(this);
                if (NoteChannelControl.this.activeNotes.isEmpty()) {
                    try {
                        NoteChannelControl.this.t.setActive(false);
                    } catch (QTException localQTException2) {
                        NoteChannelControl.this.t = null;
                    }
                }
                cancelAndCleanup();
            }
        }

        public boolean equals(Object paramObject) {
            if (paramObject == this)
                return true;
            if ((paramObject instanceof EndNote)) {
                EndNote localEndNote = (EndNote) paramObject;
                return (this.value == localEndNote.value) && (this.note == localEndNote.note)
                       && (this.nc.equals(localEndNote.nc));
            }
            return false;
        }
    }

    class TurnOffNotes implements Ticklish {
        TurnOffNotes() {
        }

        public void timeChanged(int paramInt) throws QTException {
            Enumeration localEnumeration = NoteChannelControl.this.activeNotes.elements();
            while (localEnumeration.hasMoreElements())
                ((NoteChannelControl.EndNote) localEnumeration.nextElement()).execute();
        }

        public boolean tickle(float paramFloat, int paramInt) throws QTException {
            return false;
        }
    }

    protected final void finalize() throws Throwable {
        Enumeration localEnumeration = this.activeNotes.elements();
        while (localEnumeration.hasMoreElements())
            ((EndNote) localEnumeration.nextElement()).cancelAndCleanup();
        this.activeNotes.removeAllElements();
        super.finalize();
    }
}
