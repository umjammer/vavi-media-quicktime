package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.util.QTPointerRef;

public final class TextMediaHandler extends VisualMediaHandler implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(TextMediaHandler.class);
                    }
                });
            }
        }.establish();
    }

    TextMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * This method adds a single block of styled text to an existing media.
     * QuickTime::TextMediaAddTextSample()
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
     * @return {@link quicktime.std.StdQTException}
     */
    public int addTextSample(QTPointerRef paramQTPointerRef, int paramInt1, int paramInt2, int paramInt3, QDColor paramQDColor1, QDColor paramQDColor2, int paramInt4, QDRect paramQDRect, int paramInt5, int paramInt6, int paramInt7, int paramInt8, QDColor paramQDColor3, int paramInt9) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int i = 0;
        int[] arrayOfInt2 = { 0 };
        int[] arrayOfInt3 = { 0 };
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(arrayOfInt2, arrayOfInt3);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = TextMediaAddTextSample(_ID(), QTObject.ID(paramQTPointerRef), paramQTPointerRef.getSize(), (short) paramInt1, (short) paramInt2, (short) paramInt3, paramQDColor1 != null ? paramQDColor1.getRGBColor() : null, paramQDColor2 != null ? paramQDColor2.getRGBColor() : null, (short) paramInt4, paramQDRect.getRect(), paramInt5, paramInt6, (short) paramInt7, (short) paramInt8, paramQDColor3 != null ? paramQDColor3.getRGBColor() : null, paramInt9, arrayOfInt1);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(arrayOfInt2[0], arrayOfInt3[0]);
        }
        StdQTException.checkError(i);
        return arrayOfInt1[0];
    }

    private int addHiliteSample(int paramInt1, int paramInt2, QDColor paramQDColor, int paramInt3) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = TextMediaAddHiliteSample(_ID(), (short) paramInt1, (short) paramInt2, paramQDColor != null ? paramQDColor.getRGBColor() : null, paramInt3, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method searches for text with a specified media handler starting at a given time.
     * QuickTime::TextMediaFindNextText()
     * @param text containes the text to be searched.
     * @param findFlags Specifies the conditions of the search.
     * @param startTime Indicates the time (expressed in the movie time scale) at which to begin the search.
     * @return a FoundTextInfo object
     * @return {@link quicktime.std.StdQTException}
     */
    public FoundTextInfo findNextText(QTPointerRef paramQTPointerRef, int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int i = TextMediaFindNextText(_ID(), QTObject.ID(paramQTPointerRef), paramQTPointerRef.getSize(), (short) paramInt1, paramInt2, arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);
        return new FoundTextInfo(arrayOfInt1[0], arrayOfInt2[0], arrayOfInt3[0]);
    }

    /**
     * This method can specify selected text to be highlighted.
     * QuickTime::TextMediaHiliteTextSample()
     * @param sampleTime Indicates a sample time (in the movie time scale) for the text to be highlighted.
     * @param hiliteStart Specifies the beginning of the text to be highlighted.
     * @param hiliteEnd Specifies the end of the text to be highlighted.
     * @param hiliteColor Contains a pointer to the RGB color for highlighting.
     * @param {@link quicktime.std.StdQTException}
     */
    public void hiliteTextSample(int paramInt1, int paramInt2, int paramInt3, QDColor paramQDColor) throws StdQTException {
        int i = TextMediaHiliteTextSample(_ID(), paramInt1, (short) paramInt2, (short) paramInt3, paramQDColor != null ? paramQDColor.getRGBColor() : null);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to specify the shadow offset for the text media.
     * QuickTime::TextMediaSetTextSampleData()
     * @param offset Specifies the offset.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDropShadowOffset(QDPoint paramQDPoint) throws StdQTException {
        int[] arrayOfInt = new int[1];
        arrayOfInt[0] = paramQDPoint.getPoint();
        StdQTException.checkError(TextMediaSetTextSampleData(_ID(), arrayOfInt, 1685221487));
    }

    /**
     * This method allows you to specify the translucency value for the text shadows.
     * QuickTime::TextMediaSetTextSampleData()
     * @param translucency Specifies the translucency.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDropShadowTranslucency(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        arrayOfInt[0] = paramInt;
        int i = TextMediaSetTextSampleData(_ID(), arrayOfInt, 1685221492);
        StdQTException.checkError(i);
    }

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native int TextMediaAddTextSample(int paramInt1, int paramInt2, int paramInt3, short paramShort1, short paramShort2, short paramShort3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort4, byte[] paramArrayOfByte3, int paramInt4, int paramInt5, short paramShort5, short paramShort6, byte[] paramArrayOfByte4, int paramInt6, int[] paramArrayOfInt);

    private static native int TextMediaAddHiliteSample(int paramInt1, short paramShort1, short paramShort2, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt);

    private static native int TextMediaFindNextText(int paramInt1, int paramInt2, int paramInt3, short paramShort, int paramInt4, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3);

    private static native int TextMediaHiliteTextSample(int paramInt1, int paramInt2, short paramShort1, short paramShort2, byte[] paramArrayOfByte);

    private static native int TextMediaSetTextSampleData(int paramInt1, int[] paramArrayOfInt, int paramInt2);
}

