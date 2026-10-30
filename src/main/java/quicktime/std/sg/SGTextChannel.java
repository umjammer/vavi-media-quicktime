package quicktime.std.sg;

import quicktime.QTException;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.std.StdQTException;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.movies.media.TextDescription;
import quicktime.util.QTUtils;


/**
 * Assists in importing video into a movie.
 * The SGChannel implements methods for a corresponding QuickTime
 * ComponentInstance structure.
 * Refer to SGChannel in QuickTime Components
 */
public final class SGTextChannel extends VisualChannel implements QuickTimeLib {

    SGTextChannel(int type, SequenceGrabber owner) throws StdQTException {
        super(type, owner);
    }

    /**
     * Creates a sequence grabber channel text and assigns a channel component to the channel.
     * The channel component is responsible for providing digitized data to the
     * sequence grabber component.
     *
     * @param owner specifies the sequence grabber component the channel is attached to.
     * @see "QuickTime::SGNewChannel"
     */
    public SGTextChannel(SequenceGrabber owner) throws StdQTException {
        super(owner, 1952807028);
    }

    @Override
    SampleDescription makeDescription() throws QTException {
        return new TextDescription();
    }

    /**
     * This method allows you to retrieve a music sample description from the SGChannel.
     *
     * @return a TextDescription object.
     * @throws quicktime.QTException
     * @see "QuickTime::SGGetChannelSampleDescription()"
     */
    public TextDescription getTextDescription() throws QTException {
        return (TextDescription) getSampleDescription();
    }

    /**
     * sets the name of the font to be used to display text.
     *
     * @param fontName a pointer to a string containing the name of the font.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetFontName"
     */
    public void setFontName(String fontName) throws StdQTException {
        int i = SGSetFontName(_ID(), QTUtils.String2PString(fontName, 255));
        StdQTException.checkError(i);
    }

    /**
     * sets the size of the font to be used to display text.
     *
     * @see "QuickTime::SGSetFontSize"
     */
    public void setFontSize(int fontSize) throws StdQTException {
        int i = SGSetFontSize(_ID(), (short) fontSize);
        StdQTException.checkError(i);
    }

    /**
     * sets foreground text color.
     *
     * @param theColor the new foreground text color.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetTextForeColor"
     */
    public void setForeColor(QDColor theColor) throws StdQTException {
        int i = SGSetTextForeColor(_ID(), theColor.getRGBColor());
        StdQTException.checkError(i);
    }

    /**
     * sets background text color.
     *
     * @param theColor the new background text color.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetTextBackColor"
     */
    public void setBackColor(QDColor theColor) throws StdQTException {
        int i = SGSetTextBackColor(_ID(), theColor.getRGBColor());
        StdQTException.checkError(i);
    }

    /**
     * sets text justification.
     *
     * @param just the text justification.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetJustification"
     */
    public void setJustification(int just) throws StdQTException {
        int i = SGSetJustification(_ID(), (short) just);
        StdQTException.checkError(i);
    }

    /**
     * Gets the text return to space value.
     *
     * @see "QuickTime::SGGetTextReturnToSpaceValue"
     */
    public int getReturnToSpaceValue() throws StdQTException {
        short[] value = new short[1];
        int i = SGGetTextReturnToSpaceValue(_ID(), value);
        StdQTException.checkError(i);
        return value[0];
    }

    /**
     * Sets the text return to space value.
     *
     * @param rettospace the value for the new text to space setting.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetTextReturnToSpaceValue"
     */
    public void setReturnToSpaceValue(int rettospace) throws StdQTException {
        int i = SGSetTextReturnToSpaceValue(_ID(), (short) rettospace);
        StdQTException.checkError(i);
    }

    private static int SGSetFontName(int ref, byte[] name) {
        return SequenceGrabberManager.SGSetFontName(ref, name);
    }

    private static int SGSetFontSize(int ref, short size) {
        return SequenceGrabberManager.SGSetFontSize(ref, size);
    }

    private static int SGSetTextForeColor(int ref, byte[] color) {
        return SequenceGrabberManager.SGSetTextForeColor(ref, color);
    }

    private static int SGSetTextBackColor(int ref, byte[] color) {
        return SequenceGrabberManager.SGSetTextBackColor(ref, color);
    }

    private static int SGSetJustification(int ref, short justification) {
        return SequenceGrabberManager.SGSetJustification(ref, justification);
    }

    private static int SGGetTextReturnToSpaceValue(int ref, short[] value) {
        return SequenceGrabberManager.SGGetTextReturnToSpaceValue(ref, value);
    }

    private static int SGSetTextReturnToSpaceValue(int ref, short value) {
        return SequenceGrabberManager.SGSetTextReturnToSpaceValue(ref, value);
    }
}
