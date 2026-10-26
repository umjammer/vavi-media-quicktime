package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QTNative;
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

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SGTextChannel.class);
                    }
                });
            }
        }.establish();
    }

    SGTextChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
    }

    /**
     * Creates a sequence grabber channel text and assigns a channel component to the channel.
     * The channel component is responsible for providing digitized data to the
     * sequence grabber component.
     * QuickTime::SGNewChannel
     * @param owner specifies the sequence grabber component the channel is attached to.
     */
    public SGTextChannel(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramSequenceGrabber, 1952807028);
    }

    final SampleDescription makeDescription() throws QTException {
        return new TextDescription();
    }

    /**
     * This method allows you to retrieve a music sample description from the SGChannel.
     * QuickTime::SGGetChannelSampleDescription()
     * @return a TextDescription object.
     * @return {@link quicktime.QTException}
     */
    public final TextDescription getTextDescription() throws QTException {
        return (TextDescription) getSampleDescription();
    }

    /**
     * sets the name of the font to be used to display text.
     * QuickTime::SGSetFontName
     * @param fontName a pointer to a string containing the name of the font.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setFontName(String paramString) throws StdQTException {
        int i = SGSetFontName(_ID(), QTUtils.String2PString(paramString, 255));
        StdQTException.checkError(i);
    }

    /**
     * sets the size of the font to be used to display text.
     * QuickTime::SGSetFontSize
     */
    public void setFontSize(int paramInt) throws StdQTException {
        int i = SGSetFontSize(_ID(), (short) paramInt);
        StdQTException.checkError(i);
    }

    /**
     * sets foreground text color.
     * QuickTime::SGSetTextForeColor
     * @param theColor the new foreground text color.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setForeColor(QDColor paramQDColor) throws StdQTException {
        int i = SGSetTextForeColor(_ID(), paramQDColor.getRGBColor());
        StdQTException.checkError(i);
    }

    /**
     * sets background text color.
     * QuickTime::SGSetTextBackColor
     * @param theColor the new background text color.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setBackColor(QDColor paramQDColor) throws StdQTException {
        int i = SGSetTextBackColor(_ID(), paramQDColor.getRGBColor());
        StdQTException.checkError(i);
    }

    /**
     * sets text justification.
     * QuickTime::SGSetJustification
     * @param just the text justification.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setJustification(int paramInt) throws StdQTException {
        int i = SGSetJustification(_ID(), (short) paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Gets the text return to space value.
     * QuickTime::SGGetTextReturnToSpaceValue
     */
    public int getReturnToSpaceValue() throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SGGetTextReturnToSpaceValue(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Sets the text return to space value.
     * QuickTime::SGSetTextReturnToSpaceValue
     * @param rettospace the value for the new text to space setting.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setReturnToSpaceValue(int paramInt) throws StdQTException {
        int i = SGSetTextReturnToSpaceValue(_ID(), (short) paramInt);
        StdQTException.checkError(i);
    }

    private static int SGSetFontName(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGSetFontName(paramInt, paramArrayOfByte);
    }

    private static int SGSetFontSize(int paramInt, short paramShort) {
        return SequenceGrabberManager.SGSetFontSize(paramInt, paramShort);
    }

    private static int SGSetTextForeColor(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGSetTextForeColor(paramInt, paramArrayOfByte);
    }

    private static int SGSetTextBackColor(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGSetTextBackColor(paramInt, paramArrayOfByte);
    }

    private static int SGSetJustification(int paramInt, short paramShort) {
        return SequenceGrabberManager.SGSetJustification(paramInt, paramShort);
    }

    private static int SGGetTextReturnToSpaceValue(int paramInt, short[] paramArrayOfShort) {
        return SequenceGrabberManager.SGGetTextReturnToSpaceValue(paramInt, paramArrayOfShort);
    }

    private static int SGSetTextReturnToSpaceValue(int paramInt, short paramShort) {
        return SequenceGrabberManager.SGSetTextReturnToSpaceValue(paramInt, paramShort);
    }
}

