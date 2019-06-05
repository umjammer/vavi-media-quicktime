
package quicktime.app.audio;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.std.movies.media.MusicMediaHandler;
import quicktime.std.music.NoteChannel;
import quicktime.std.music.TunePlayer;


/**
 * @deprecated
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
     * @deprecated
     */
    public MusicPartControl(MusicMediaHandler paramMusicMediaHandler, int paramInt) throws QTException {
        this.tp = paramMusicMediaHandler.getIndexedTunePlayer(1);
        this.part = paramInt;
        this.muted = (getNoteChannel().getController(7) <= 0.0F);
        this.vol = getVolume();
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
    public void setMuted(boolean paramBoolean) throws QTException {
        this.muted = paramBoolean;
        if (this.muted) {
            getNoteChannel().setController(7, 0.0F);
        } else {
            getNoteChannel().setController(7, this.vol * 128.0F);
        }
    }

    /**
     * @deprecated
     */
    public void setVolume(float paramFloat) throws QTException {
        this.vol = paramFloat;
        getNoteChannel().setController(7, this.vol * 128.0F);
    }

    /**
     * @deprecated
     */
    public float getVolume() throws QTException {
        return this.muted ? 0.0F : getNoteChannel().getController(7) / 128.0F;
    }

    /**
     * @deprecated
     */
    public void setBalance(float paramFloat) throws QTException {
        if (paramFloat == 0.0F) {
            getNoteChannel().setController(10, paramFloat);
        } else {
            getNoteChannel().setController(10, paramFloat / 2.0F + 1.5F);
        }
    }

    /**
     * @deprecated
     */
    public float getBalance() throws QTException {
        float f = getNoteChannel().getController(10);
        if (f == 0.0F)
            return f;
        return (f - 1.5F) * 2.0F;
    }

    /**
     * @deprecated
     */
    public String getInstrumentName() throws QTException {
        return getNoteChannel().getNoteRequest().getToneDescription().getInstrumentName();
    }

    /**
     * @deprecated
     */
    public void selectInstrument(String paramString) throws QTException {
        getNoteChannel().pickEditInstrument(paramString, 16);
    }

    /**
     * @deprecated
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
