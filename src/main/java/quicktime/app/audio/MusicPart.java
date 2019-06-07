package quicktime.app.audio;

import quicktime.QTException;
import quicktime.std.music.NoteChannel;

/**
 * Deprecated.
 * MusicParts extend the capability of audio control objects as they provide
 * an instrument that will play or render the control information (ie. notes, etc)
 * of that particular part. The analogy is an instrument in an orchestra that plays a
 * particular part in a score.
 *
 * The NoteChannel is responsible for producing the sound.
 *
 * In its completeness a MusicPart has a sense of it volume, its balance location
 * in a left-right stereo field and an instrument that is the sound it produces.
 * @see {@link quicktime.std.music.NoteChannel}
 */
public abstract interface MusicPart extends ExtendedAudioSpec {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the name of the current instrument for this part.
     * @return name of the current instrument for this part
     * @return {@link quicktime.QTException}
     */
    public abstract String getInstrumentName() throws QTException;

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
    public abstract void selectInstrument(String paramString) throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method gets the NoteChannel being controlled by this object.
     * @return the NoteChannel
     * @return {@link quicktime.QTException}
     */
    public abstract NoteChannel getNoteChannel() throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/audio
 * /MusicPart.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
