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

    TextMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
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
        int[] arrayOfInt2 = { 0 };
        int[] arrayOfInt3 = { 0 };
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(arrayOfInt2, arrayOfInt3);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = TextMediaAddTextSample(_ID(), QTObject.ID(text), text.getSize(), (short) fontNumber, (short) fontSize, (short) textFace, textColor != null ? textColor.getRGBColor() : null, backColor != null ? backColor.getRGBColor() : null, (short) textJustification, textBox.getRect(), displayFlags, scrollDelay, (short) hiliteStart, (short) hiliteEnd, rgbHiliteColor != null ? rgbHiliteColor.getRGBColor() : null, duration, sampleTime);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(arrayOfInt2[0], arrayOfInt3[0]);
        }
        StdQTException.checkError(i);
        return sampleTime[0];
    }

    private int addHiliteSample(int paramInt1, int paramInt2, QDColor paramQDColor, int paramInt3) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = TextMediaAddHiliteSample(_ID(), (short) paramInt1, (short) paramInt2, paramQDColor != null ? paramQDColor.getRGBColor() : null, paramInt3, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
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
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int i = TextMediaFindNextText(_ID(), QTObject.ID(text), text.getSize(), (short) findFlags, startTime, arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);
        return new FoundTextInfo(arrayOfInt1[0], arrayOfInt2[0], arrayOfInt3[0]);
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
        int[] arrayOfInt = new int[1];
        arrayOfInt[0] = offset.getPoint();
        StdQTException.checkError(TextMediaSetTextSampleData(_ID(), arrayOfInt, 1685221487));
    }

    /**
     * This method allows you to specify the translucency value for the text shadows.
     * @see "QuickTime::TextMediaSetTextSampleData()"
     * @param translucency Specifies the translucency.
     * @throws quicktime.std.StdQTException
     */
    public void setDropShadowTranslucency(int translucency) throws StdQTException {
        int[] arrayOfInt = new int[1];
        arrayOfInt[0] = translucency;
        int i = TextMediaSetTextSampleData(_ID(), arrayOfInt, 1685221492);
        StdQTException.checkError(i);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void SetGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }

    private static int TextMediaAddTextSample(int mh, int text, int size, short fontNumber, short fontSIze, short fontFace, byte[] textColor, byte[] backColor, short textJustification, byte[] textBox, int displayFlags, int scrollDelay, short hiliteStart, short hiliteEnd, byte[] hilieColor, int duration, int[] sampleTime) {
        return MediaHandlerManager.TextMediaAddTextSample(mh, text, size, fontNumber, fontSIze, fontFace, textColor, backColor, textJustification, textBox, displayFlags, scrollDelay, hiliteStart, hiliteEnd, hilieColor, duration, sampleTime);
    }

    private static int TextMediaAddHiliteSample(int paramInt1, short paramShort1, short paramShort2, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt) {
        return MediaHandlerManager.TextMediaAddHiliteSample(paramInt1, paramShort1, paramShort2, paramArrayOfByte, paramInt2, paramArrayOfInt);
    }

    private static int TextMediaFindNextText(int paramInt1, int paramInt2, int paramInt3, short paramShort, int paramInt4, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        return MediaHandlerManager.TextMediaFindNextText(paramInt1, paramInt2, paramInt3, paramShort, paramInt4, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3);
    }

    private static int TextMediaHiliteTextSample(int paramInt1, int paramInt2, short paramShort1, short paramShort2, byte[] paramArrayOfByte) {
        return MediaHandlerManager.TextMediaHiliteTextSample(paramInt1, paramInt2, paramShort1, paramShort2, paramArrayOfByte);
    }

    private static int TextMediaSetTextSampleData(int paramInt1, int[] paramArrayOfInt, int paramInt2) {
        return MediaHandlerManager.TextMediaSetTextSampleData(paramInt1, paramArrayOfInt, paramInt2);
    }
}
