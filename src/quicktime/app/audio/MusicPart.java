
package quicktime.app.audio;

import quicktime.QTException;
import quicktime.std.music.NoteChannel;


/**
 * @deprecated
 */
public abstract interface MusicPart extends ExtendedAudioSpec {
    /**
     * @deprecated
     */
    public abstract String getInstrumentName() throws QTException;

    /**
     * @deprecated
     */
    public abstract void selectInstrument(String paramString) throws QTException;

    /**
     * @deprecated
     */
    public abstract NoteChannel getNoteChannel() throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/audio
 * /MusicPart.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
