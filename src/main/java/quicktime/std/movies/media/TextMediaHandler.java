package quicktime.std.movies.media;

import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.MediaHandlerManager;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.util.QTPointerRef;


public final class TextMediaHandler extends VisualMediaHandler implements QuickTimeLib {

    TextMediaHandler(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * This method adds a single block of styled text to an existing media.
     * @see "QuickTime::TextMediaAddTextSample()"
     * @param text Contains the text to be added.
     * @param fontSize Indicates the size of the font.
     * @param textFace Indicates the typeface or style of the text (that is, bold, italic, and so on).
     * @param textColor Contains a pointer to an RGB color structure specifying the color of the text.
     * @param backColor Contains a pointer to an RGB color structure specifying the text background color.
     * @param textJustification Indicates the justification of the text.
     * @param textBox The box within which the text is to be displayed. The box is relative to the track bounds.
     * @param displayFlags Contains the text display flags.
     * @param scrollDelay Indicates the delay in scrolling associated with setting the dfScrollIn and dfScrollOut display flags.
     * @param hiliteStart Specifies the beginning of the text to be highlited.
     * @param hiliteEnd Specifies the end of the text to be highlighted.
     * @param rgbHiliteColor The color for highlighting.
     * @param duration Specifies how long the text sample should last. This duration is expressed in the media's time base.
     * @return The actual media time at which the sample was added.
     * @throws quicktime.std.StdQTException
     */
    public int addTextSample(QTPointerRef text, int fontNumber, int fontSize, int textFace, QDColor textColor, QDColor backColor, int textJustification, QDRect textBox, int displayFlags, int scrollDelay, int hiliteStart, int hiliteEnd, QDColor rgbHiliteColor, int duration) throws StdQTException {
        int[] sampleTime = new int[1];
        int i = 0;
        int[] port = { 0 };
        int[] gdh = { 0 };
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(port, gdh);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = TextMediaAddTextSample(_ID(), QTObject.ID(text), text.getSize(), (short) fontNumber, (short) fontSize, (short) textFace, textColor != null ? textColor.getRGBColor() : null, backColor != null ? backColor.getRGBColor() : null, (short) textJustification, textBox.getRect(), displayFlags, scrollDelay, (short) hiliteStart, (short) hiliteEnd, rgbHiliteColor != null ? rgbHiliteColor.getRGBColor() : null, duration, sampleTime);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(port[0], gdh[0]);
        }
        StdQTException.checkError(i);
        return sampleTime[0];
    }

    private int addHiliteSample(int hiliteStart, int hiliteEnd, QDColor color, int duration) throws StdQTException {
        int[] sampleTime = new int[1];
        int i = TextMediaAddHiliteSample(_ID(), (short) hiliteStart, (short) hiliteEnd, color != null ? color.getRGBColor() : null, duration, sampleTime);
        StdQTException.checkError(i);
        return sampleTime[0];
    }

    /**
     * This method searches for text with a specified media handler starting at a given time.
     * @see "QuickTime::TextMediaFindNextText()"
     * @param text containes the text to be searched.
     * @param findFlags Specifies the conditions of the search.
     * @param startTime Indicates the time (expressed in the movie time scale) at which to begin the search.
     * @return a FoundTextInfo object
     * @throws quicktime.std.StdQTException
     */
    public FoundTextInfo findNextText(QTPointerRef text, int findFlags, int startTime) throws StdQTException {
        int[] foundTime = new int[1];
        int[] foundDuration = new int[1];
        int[] offset = new int[1];
        int i = TextMediaFindNextText(_ID(), QTObject.ID(text), text.getSize(), (short) findFlags, startTime, foundTime, foundDuration, offset);
        StdQTException.checkError(i);
        return new FoundTextInfo(foundTime[0], foundDuration[0], offset[0]);
    }

    /**
     * This method can specify selected text to be highlighted.
     * @see "QuickTime::TextMediaHiliteTextSample()"
     * @param sampleTime Indicates a sample time (in the movie time scale) for the text to be highlighted.
     * @param hiliteStart Specifies the beginning of the text to be highlighted.
     * @param hiliteEnd Specifies the end of the text to be highlighted.
     * @param hiliteColor Contains a pointer to the RGB color for highlighting.
     * @throws quicktime.std.StdQTException
     */
    public void hiliteTextSample(int sampleTime, int hiliteStart, int hiliteEnd, QDColor hiliteColor) throws StdQTException {
        int i = TextMediaHiliteTextSample(_ID(), sampleTime, (short) hiliteStart, (short) hiliteEnd, hiliteColor != null ? hiliteColor.getRGBColor() : null);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the shadow offset for the text media.
     * @see "QuickTime::TextMediaSetTextSampleData()"
     * @param offset Specifies the offset.
     * @throws quicktime.std.StdQTException
     */
    public void setDropShadowOffset(QDPoint offset) throws StdQTException {
        int[] data = new int[1];
        data[0] = offset.getPoint();
        StdQTException.checkError(TextMediaSetTextSampleData(_ID(), data, 1685221487));
    }

    /**
     * This method allows you to specify the translucency value for the text shadows.
     * @see "QuickTime::TextMediaSetTextSampleData()"
     * @param translucency Specifies the translucency.
     * @throws quicktime.std.StdQTException
     */
    public void setDropShadowTranslucency(int translucency) throws StdQTException {
        int[] data = new int[1];
        data[0] = translucency;
        int i = TextMediaSetTextSampleData(_ID(), data, 1685221492);
        StdQTException.checkError(i);
    }

    private static void GetGWorld(int[] port, int[] gdh) {
        QuickDrawManager.GetGWorld(port, gdh);
    }

    private static void SetGWorld(int port, int gdh) {
        QuickDrawManager.SetGWorld(port, gdh);
    }

    private static int TextMediaAddTextSample(int mh, int text, int size, short fontNumber, short fontSIze, short fontFace, byte[] textColor, byte[] backColor, short textJustification, byte[] textBox, int displayFlags, int scrollDelay, short hiliteStart, short hiliteEnd, byte[] hilieColor, int duration, int[] sampleTime) {
        return MediaHandlerManager.TextMediaAddTextSample(mh, text, size, fontNumber, fontSIze, fontFace, textColor, backColor, textJustification, textBox, displayFlags, scrollDelay, hiliteStart, hiliteEnd, hilieColor, duration, sampleTime);
    }

    private static int TextMediaAddHiliteSample(int mh, short hiliteStart, short hiliteEnd, byte[] hiliteColor, int duration, int[] sampleTime) {
        return MediaHandlerManager.TextMediaAddHiliteSample(mh, hiliteStart, hiliteEnd, hiliteColor, duration, sampleTime);
    }

    private static int TextMediaFindNextText(int mh, int text, int size, short findFlags, int startTime, int[] foundTime, int[] foundDuration, int[] offset) {
        return MediaHandlerManager.TextMediaFindNextText(mh, text, size, findFlags, startTime, foundTime, foundDuration, offset);
    }

    private static int TextMediaHiliteTextSample(int mh, int sampleTime, short hiliteStart, short hiliteEnd, byte[] hiliteColor) {
        return MediaHandlerManager.TextMediaHiliteTextSample(mh, sampleTime, hiliteStart, hiliteEnd, hiliteColor);
    }

    private static int TextMediaSetTextSampleData(int mh, int[] data, int dataType) {
        return MediaHandlerManager.TextMediaSetTextSampleData(mh, data, dataType);
    }
}
