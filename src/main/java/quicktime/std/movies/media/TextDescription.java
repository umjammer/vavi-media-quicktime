package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.qd.QDColor;
import quicktime.qd.QDRect;
import quicktime.qd.text.ScrpSTElement;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;

/**
 * This class maps the TextDescription record of the MovieToolbox.
 * TextDescription are used when adding and getting media samples to/from Text tracks.
 */
public final class TextDescription extends SampleDescription {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 58;

    private static EndianDescriptor ed;

    protected static EndianDescriptor makeED() {
        EndianDescriptor ed = SampleDescription.makeED();
        EndianFlipSpec spec = new EndianFlipSpec(16, 4, 2);
        ed.addFlipSpec(spec);
        EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(24, 2, 3);
        ed.addFlipSpec(endianFlipSpec2);
        EndianFlipSpec endianFlipSpec3 = new EndianFlipSpec(30, 2, 4);
        ed.addFlipSpec(endianFlipSpec3);
        EndianFlipSpec endianFlipSpec4 = new EndianFlipSpec(38, 4, 1);
        ed.addFlipSpec(endianFlipSpec4);
        EndianFlipSpec endianFlipSpec5 = new EndianFlipSpec(42, 2, 3);
        ed.addFlipSpec(endianFlipSpec5);
        EndianFlipSpec endianFlipSpec6 = new EndianFlipSpec(47, 2, 1);
        ed.addFlipSpec(endianFlipSpec6);
        EndianFlipSpec endianFlipSpec7 = new EndianFlipSpec(50, 2, 3);
        ed.addFlipSpec(endianFlipSpec7);
        return ed;
    }

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * This constructor makes a new TextDescription object.
     */
    public TextDescription() throws QTException {
        super(58, true, 1952807028);
    }

    private TextDescription(int nativeRep) {
        super(nativeRep, null, false);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    @Override
    public Object clone() {
        return new TextDescription(makeAndCopyHandle());
    }

    /**
     * Set the default style.
     * @param style the new default style.
     */
    public void setDefaultStyle(ScrpSTElement style) {
        copyFromArray(38, style.getBytes(), 0, style.getSize());
    }

    /**
     * Get the default style.
     * @return the default style.
     */
    public ScrpSTElement getDefaultStyle() {
        ScrpSTElement defaultStyle = new ScrpSTElement();
        copyToArray(38, defaultStyle.getBytes(), 0, defaultStyle.getSize());
        return defaultStyle;
    }

    public void setDisplayFlags(int displayFlags) {
        setIntAt(16, displayFlags);
    }

    public int getDisplayFlags() {
        return getIntAt(16);
    }

    public void setTextJustification(int textJustification) {
        setIntAt(20, textJustification);
    }

    public int getTextJustification() {
        return getIntAt(20);
    }

    public void setBackgroundColor(QDColor backgroundColor) {
        setBytesAt(24, 6, backgroundColor.getRGBColor(), 0);
    }

    public QDColor getBackgroundColor() {
        byte[] destArray = new byte[6];
        getBytesAt(24, 6, destArray, 0);
        return QDColor.fromArray(destArray, 6);
    }

    public void setDefaultTextBox(QDRect defaultTextBox) {
        setBytesAt(30, 8, defaultTextBox.getRect(), 0);
    }

    public QDRect getDefaultTextBox() {
        byte[] destArray = new byte[8];
        getBytesAt(30, 8, destArray, 0);
        return QDRect.fromArray(destArray, 8);
    }

    public void setDefaultFontName(String defaultFontName) throws QTException {
        if (getSize() < 58 + defaultFontName.length() + 1)
            setSize(58 + defaultFontName.length() + 1);
        setPStringAt(58, 255, defaultFontName);
    }

    public String getDefaultFontName() throws QTException {
        return getPStringAt(58);
    }
}
