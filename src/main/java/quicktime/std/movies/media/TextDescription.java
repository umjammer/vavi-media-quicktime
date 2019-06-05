
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.qd.QDColor;
import quicktime.qd.QDRect;
import quicktime.qd.text.ScrpSTElement;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;


public final class TextDescription extends SampleDescription {
    public static final int kNativeSize = 58;

    private static EndianDescriptor ed;

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = SampleDescription.makeED();

        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(16, 4, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);

        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(24, 2, 3);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);

        EndianFlipSpec localEndianFlipSpec3 = new EndianFlipSpec(30, 2, 4);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec3);

        EndianFlipSpec localEndianFlipSpec4 = new EndianFlipSpec(38, 4, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec4);

        EndianFlipSpec localEndianFlipSpec5 = new EndianFlipSpec(42, 2, 3);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec5);

        EndianFlipSpec localEndianFlipSpec6 = new EndianFlipSpec(47, 2, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec6);

        EndianFlipSpec localEndianFlipSpec7 = new EndianFlipSpec(50, 2, 3);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec7);

        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    public TextDescription() throws QTException {
        super(58, true, 1952807028);
    }

    private TextDescription(int paramInt) {
        super(paramInt, null, false);
    }

    public Object clone() {
        return new TextDescription(makeAndCopyHandle());
    }

    public void setDefaultStyle(ScrpSTElement paramScrpSTElement) {
        copyFromArray(38, paramScrpSTElement.getBytes(), 0, paramScrpSTElement.getSize());
    }

    public ScrpSTElement getDefaultStyle() {
        ScrpSTElement localScrpSTElement = new ScrpSTElement();
        copyToArray(38, localScrpSTElement.getBytes(), 0, localScrpSTElement.getSize());
        return localScrpSTElement;
    }

    public void setDisplayFlags(int paramInt) {
        setIntAt(16, paramInt);
    }

    public int getDisplayFlags() {
        return getIntAt(16);
    }

    public void setTextJustification(int paramInt) {
        setIntAt(20, paramInt);
    }

    public int getTextJustification() {
        return getIntAt(20);
    }

    public void setBackgroundColor(QDColor paramQDColor) {
        setBytesAt(24, 6, paramQDColor.getRGBColor(), 0);
    }

    public QDColor getBackgroundColor() {
        byte[] arrayOfByte = new byte[6];
        getBytesAt(24, 6, arrayOfByte, 0);
        return QDColor.fromArray(arrayOfByte, 6);
    }

    public void setDefaultTextBox(QDRect paramQDRect) {
        setBytesAt(30, 8, paramQDRect.getRect(), 0);
    }

    public QDRect getDefaultTextBox() {
        byte[] arrayOfByte = new byte[8];
        getBytesAt(30, 8, arrayOfByte, 0);
        return QDRect.fromArray(arrayOfByte, 8);
    }

    public void setDefaultFontName(String paramString) throws QTException {
        if (getSize() < 58 + paramString.length() + 1)
            setSize(58 + paramString.length() + 1);
        setPStringAt(58, 255, paramString);
    }

    public String getDefaultFontName() throws QTException {
        return getPStringAt(58);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/TextDescription.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
