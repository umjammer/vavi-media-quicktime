package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.qtcomponents.MovieImporter;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;


/**
 * The SampleDescription class implements the SampleDescription data structure of the Movie Toolbox.
 */
public class SampleDescription extends QTHandleRef implements QuickTimeLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 16;

    private static EndianDescriptor ed;

    protected static EndianDescriptor makeED() {
        EndianDescriptor ed = new EndianDescriptor(0);
        EndianFlipSpec spec = new EndianFlipSpec(0, 4, 3);
        ed.addFlipSpec(spec);
        EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(12, 2, 2);
        ed.addFlipSpec(endianFlipSpec2);
        return ed;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * Determines the sample description of the component.
     *
     * @return The sample description.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MovieImportGetSampleDescription"
     * @see quicktime.std.qtcomponents.MovieImporter#getSampleDescription()
     */
    public static SampleDescription fromMovieImporter(MovieImporter ci) throws StdQTException {
        int[] desc = new int[1];
        int[] mediaType = new int[1];
        int i = MovieImportGetSampleDescription(QTObject.ID(ci), desc, mediaType);
        StdQTException.checkError(i);
        return new SampleDescription(desc[0], ci, false);
    }

    /**
     * This constructor creates an empty SampleDescription object.
     */
    public SampleDescription(int format) throws QTException {
        this(kNativeSize, true, format);
    }

    protected SampleDescription(int nativeRep, Object ref, boolean locked) {
        super(nativeRep, ref, locked);
    }

    protected SampleDescription(int size, boolean clear, int format) throws QTException {
        super(size, true);
        setIntAt(0, size);
        setDataFormat(format);
    }

    /**
     * This method returns the size of the media sample description.
     *
     * @return the size of the sample description.
     */
    @Override
    public int getSize() {
        return getIntAt(0);
    }

    /**
     * Resizes the SampleDescription to the specified size.
     *
     * @param size the new size of the SampleDescription.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SetHandleSize"
     */
    public void setSize(int size) throws StdQTException {
        boolean bool = isLocked();
        unlock();
        SetHandleSize(_ID(), size);
        StdQTException.checkError(MemError());
        setIntAt(0, size);
        if (bool) {
            lock();
        }
    }

    /**
     * Return the data format of the sample description.
     *
     * @return the data format
     */
    public int getDataFormat() {
        return getIntAt(4);
    }

    /**
     * Sets the data format of the sample description.
     *
     * @param format the data format
     */
    public void setDataFormat(int format) {
        setIntAt(4, format);
    }

    /**
     * Gets the index of the data reference
     *
     * @return the index of the data reference
     */
    public int getDataRefIndex() {
        return getShortAt(14);
    }

    /**
     * Sets the data reference index for the description
     *
     * @param index the new data reference index
     */
    public void setDataRefIndex(int index) {
        setShortAt(14, (short) index);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    @Override
    public Object clone() {
        return new SampleDescription(makeAndCopyHandle(), null, false);
    }

    /**
     * Print information about this object.
     *
     * @return a string representation of this object.
     */
    public String toString() {
        return getClass().getName() + "[format=" + QTUtils.fromOSType(getDataFormat()) + "]";
    }

    private static int MovieImportGetSampleDescription(int ci, int[] desc, int[] mediaType) {
        return ComponentManager.MovieImportGetSampleDescription(ci, desc, mediaType);
    }

    private static void SetHandleSize(int h, int size) {
        MemoryManager.SetHandleSize(h, size);
    }

    private static short MemError() {
        return MemoryManager.MemError();
    }
}
