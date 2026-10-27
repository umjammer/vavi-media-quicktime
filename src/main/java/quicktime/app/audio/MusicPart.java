package quicktime.app.audio;

import quicktime.QTException;
import quicktime.std.music.NoteChannel;

/**
 * @deprecated
 * MusicParts extend the capability of audio control objects as they provide
 * an instrument that will play or render the control information (ie. notes, etc)
 * of that particular part. The analogy is an instrument in an orchestra that plays a
 * particular part in a score.
 *
 * The NoteChannel is responsible for producing the sound.
 *
 * In its completeness a MusicPart has a sense of it volume, its balance location
 * in a left-right stereo field and an instrument that is the sound it produces.
 * @see quicktime.std.music.NoteChannel
 */
public interface MusicPart extends ExtendedAudioSpec {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the name of the current instrument for this part.
     * @return name of the current instrument for this part
     * @throws quicktime.QTException
     */
    String getInstrumentName() throws QTException;

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
    void selectInstrument(String paramString) throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method gets the NoteChannel being controlled by this object.
     * @return the NoteChannel
     * @throws quicktime.QTException
     */
    NoteChannel getNoteChannel() throws QTException;
}
