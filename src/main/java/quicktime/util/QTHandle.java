package quicktime.util;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.GraphicsExportManager;
import quicktime.internal.toolbox.GraphicsImportManager;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.SoundManager;
import quicktime.internal.toolbox.TimeCodeManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.image.ImageDescription;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.SoundDescription;
import quicktime.std.qtcomponents.ImageCompressionDialog;
import quicktime.std.qtcomponents.TimeCodeDescription;
import quicktime.std.qtcomponents.TimeCodeInfo;
import quicktime.std.qtcomponents.TimeCodeTime;
import quicktime.std.qtcomponents.TimeCoder;


/**
 * The QTHandle class implements a handle that can be used in calls to QuickTime.
 */
public class QTHandle extends QTHandleRef implements QuickTimeLib {

    /**
     * Determines the data handle that is the source for input images to graphics export component
     * @see "QuickTime::GraphicsExportGetSettingsAsText"
     * @return The handle to a QuickTime data handle.
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsExporter#getInputHandle()
     */
    public static QTHandle fromGraphicsExporterText(GraphicsExporter ge) throws QTException {
        int[] h = { 0 };
        int r = GraphicsExportGetSettingsAsText(QTObject.ID(ge), h);
        StdQTException.checkError(r);
        return new QTHandle(h[0], null, false);
    }

    /**
     * Determines the data handle that is the source for input images to graphics export component
     * @see "QuickTime::GraphicsExportGetInputHandle"
     * @return The handle to a QuickTime data handle.
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsExporter#getInputHandle()
     */
    public static QTHandle fromGraphicsExporterInput(GraphicsExporter ge) throws QTException {
        int[] h = new int[1];
        int r = GraphicsExportGetInputHandle(QTObject.ID(ge), h);
        StdQTException.checkError(r);
        return new QTHandle(h[0], null, false);
    }

    /**
     * Determines the data handle that is the source for input images to graphics export component
     * @see "QuickTime::GraphicsExportGetOutputHandle"
     * @return The handle to a QuickTime data handle.
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsExporter#getOutputHandle()
     */
    public static QTHandle fromGraphicsExporterOutput(GraphicsExporter ge) throws QTException {
        int[] h = new int[1];
        int r = GraphicsExportGetOutputHandle(QTObject.ID(ge), h);
        StdQTException.checkError(r);
        return new QTHandle(h[0], null, false);
    }

    /**
     * Retrieve application specific info from a Sound Description object
     * @see "QuickTime::GetSoundDescriptionExtension"
     * @param idType an OSType identifying the information for removal
     * @return a dataref to the Extension
     * @throws quicktime.QTException
     * @see quicktime.std.movies.media.SoundDescription#getExtension(int)
     */
    public static QTHandle fromSoundDescription(SoundDescription desc, int idType) throws QTException {
        int[] h = new int[1];
        int r = GetSoundDescriptionExtension(QTObject.ID(desc), h, idType);
        StdQTException.checkError(r);
        return new QTHandle(h[0], null, false);
    }

    /**
     * Retrieve application specific info from a ImageDescription object
     * @see "QuickTime::GetImageDescriptionExtension"
     * @param idType an OSType identifying the information for removal
     * @return a dataref to the Extension
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.ImageDescription#getExtension(int, int)
     */
    public static QTHandle fromImageDescription(ImageDescription desc, int idType, int index) throws StdQTException {
        int[] h = new int[1];
        StdQTException.checkError(GetImageDescriptionExtension(QTObject.ID(desc), h, idType, index));
        return new QTHandle(h[0], null, false);
    }

    /**
     * Determines the data reference that the graphics resides in.
     * @see "QuickTime::GraphicsImportGetDataHandle"
     * @return The handle to a QuickTime data reference.
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsImporter#getDataHandle()
     */
    public static QTHandle fromGraphicsImporterData(GraphicsImporter gi) throws QTException {
        int[] h = new int[1];
        int r = GraphicsImportGetDataHandle(QTObject.ID(gi), h);
        StdQTException.checkError(r);
        return new QTHandle(h[0], null, false);
    }

    /**
     * Determines the aliased data reference.
     * @see "QuickTime::GraphicsImportGetAliasedDataReference"
     * @return The handle to the aliased data reference
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsImporter#getAliasedDataReference()
     */
    public static QTHandle fromGraphicsImporterAlias(GraphicsImporter gi) throws QTException {
        int[] h1 = new int[1];
        int[] h2 = new int[1];
        int r = GraphicsImportGetAliasedDataReference(QTObject.ID(gi), h1, h2);
        StdQTException.checkError(r);
        return new QTHandle(h1[0], null, false);
    }

    /**
     * Determines the time code from the time code handler.
     * @see "QuickTime::TCGetTimeCodeAtTime()"
     * @param mediaTime The time value of the media at which to retrieve the time code information.
     * @return The information object containing the time code, its definition, and data.
     * @throws quicktime.QTException
     * @see quicktime.std.qtcomponents.TimeCoder#getAtTime(int)
     */
    public static TimeCodeInfo fromTimeCoderTime(TimeCoder mh, int mediaTime) throws QTException {
        int[] frameNumber = new int[1];
        TimeCodeTime tct = new TimeCodeTime();
        int[] srcRef = new int[1];
        TimeCodeInfo tci = new TimeCodeInfo();
        int i = TCGetTimeCodeAtTime(QTObject.ID(mh), mediaTime, frameNumber, tci.definition.getBytes(), tct.getBytes(), srcRef);
        StdQTException.checkError(i);
        if (srcRef[0] != 0) {
            tci.userData = new QTHandle(srcRef[0], null, false);
        } else {
            tci.userData = null;
        }
        tci.counter = tct.toCounter();
        tci.time = tct;
        tci.frameNumber = frameNumber[0];
        return tci;
    }

    /**
     * Determines the time code from the time code handler at the current time value.
     * @see "QuickTime::TCGetCurrentTimeCode()"
     * @return The information object containing the time code, its definition, and data.
     * @throws quicktime.QTException
     * @see quicktime.std.qtcomponents.TimeCoder#getCurrent()
     */
    public static TimeCodeInfo fromTimeCoderCurrent(TimeCoder mh) throws QTException {
        int[] frameNumber = new int[1];
        TimeCodeTime timeCodeTime = new TimeCodeTime();
        int i = 0;
        int[] srcRef = new int[1];
        TimeCodeInfo timeCodeInfo = new TimeCodeInfo();
        int j = TCGetCurrentTimeCode(QTObject.ID(mh), frameNumber, timeCodeInfo.definition.getBytes(), timeCodeTime.getBytes(), srcRef);
        StdQTException.checkError(j);
        if (srcRef[0] != 0) {
            timeCodeInfo.userData = new QTHandle(srcRef[0], null, false);
        } else {
            timeCodeInfo.userData = null;
        }
        timeCodeInfo.counter = timeCodeTime.toCounter();
        timeCodeInfo.time = timeCodeTime;
        timeCodeInfo.frameNumber = frameNumber[0];
        return timeCodeInfo;
    }

    /**
     * Allows you to retrieve the source information from the time code media sample.
     * @see "QuickTime::TCGetSourceRef()"
     * @param tcd the description that describes the time code reference to be changed
     * @return the source information that is found in the sample reference
     * @throws quicktime.QTException
     * @see quicktime.std.qtcomponents.TimeCoder#getSourceRef(quicktime.std.qtcomponents.TimeCodeDescription)
     */
    public static QTHandle fromTimeCoderSource(TimeCoder mh, TimeCodeDescription tcd) throws QTException {
        int[] srefH = new int[1];
        StdQTException.checkError(TCGetSourceRef(QTObject.ID(mh), QTObject.ID(tcd), srefH));
        return new QTHandle(srefH[0], null, false);
    }

    /**
     * This method returns the sound localization settings for the track.
     * @see "QuickTime::GetTrackSoundLocalizationSettings()"
     * @return the sound localization settings for the track.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Track#getSoundLocalizationSettings()
     */
    public static QTHandle fromTrack(Track track) throws QTException {
        int[] settings = new int[1];
        int i = GetTrackSoundLocalizationSettings(QTObject.ID(track), settings);
        StdQTException.checkError(i);
        return new QTHandle(settings[0], null, false);
    }

    /**
     * Returns EncodedImage data as a QTHandle. This will copy the data.
     * @param image the encoded image data
     * @return the encoded image data as a Handle
     * @throws quicktime.QTException
     */
    public static QTHandle fromEncodedImage(EncodedImage image) throws QTException {
        if ((image instanceof ByteEncodedImage))
            return new QTHandle(((ByteEncodedImage) image).getBytes());
        if ((image instanceof IntEncodedImage)) {
            QTHandle qtHandle = new QTHandle(image.getSize(), false);
            int[] srcArray = ((IntEncodedImage) image).getInts();
            qtHandle.copyFromArray(0, srcArray, 0, srcArray.length);
            return qtHandle;
        }
        if ((image instanceof RawEncodedImage)) {
            return new QTHandle((RawEncodedImage) image, 0, image.getSize());
        }
        return null;
    }

    /**
     * Use reserve memory before you allocate a handle that you intend to keep locked
     * for long periods of time.
     * @see "QuickTime::ReserveMem"
     * @param size the size of the handle you will allocate in the new Handle(size...) call
     * @throws quicktime.util.UtilException
     */
    public static void reserveMemory(int size) throws UtilException {
        ReserveMem(size);
        UtilException.checkError(MemError());
    }

    /**
     * Allocate a handle of minimum size for general use.
     */
    public QTHandle() throws QTException {
        this(4, true);
    }

    /**
     * Allocate a handle for general use.
     * @see "QuickTime::NewHandle"
     * @see "QuickTime::NewHandleClear"
     * @param size the size of the handle to create.
     * @param clear clear the handle upon creation.
     */
    public QTHandle(int size, boolean clear) throws QTException {
        super(size, clear);
    }

    /**
     * Construct a handle from input data.
     * @param byteArray the input data.
     */
    public QTHandle(byte[] byteArray) throws QTException {
        super(byteArray.length, false);
        setBytesAt(0, byteArray.length, byteArray, 0);
    }

    /**
     * This constructor is used to create a QTHandle object from a QTPointer.
     * The contents of the pointer will be copied into the handle. This will copy
     * size bytes from the pointer + offset. It will bounds check the size to be not
     * greater than the size of the pointer.
     * @see "QuickTime::PtrToHand"
     * @param ptr the pointer to copy.
     * @param offset you can specify an offset within the pointer from which to copy the data
     * @param size the amount of bytes to copy
     */
    public QTHandle(QTPointerRef ptr, int offset, int size) throws QTException {
        this(copyPtr(ptr, offset, size), null, false);
    }

    /**
     * Construct a handle from the concatenation of the two handles supplied. The First handle
     * will appear first in the concatentated result.
     * @param firstHandle the first Handle
     * @param secondHandle the second Handle
     */
    public QTHandle(QTHandleRef firstHandle, QTHandleRef secondHandle) throws QTException {
        this(firstHandle.makeAndCopyHandle(), null, false);
        concatenate(secondHandle);
    }

    protected QTHandle(int nativeRep, Object ref, boolean locked) {
        super(nativeRep, ref, locked);
    }

    private static int copyPtr(QTPointerRef qtPointerRef, int offset, int size) throws UtilException {
        size = size + offset > qtPointerRef.getSize() ? qtPointerRef.getSize() - offset : size;
        int[] dstHandle = { 0 };
        dstHandle[0] = NewHandleClear(4);
        UtilException.checkError(PtrToHand(QTObject.ID(qtPointerRef) + offset, dstHandle, size));
        return dstHandle[0];
    }

    /**
     * Resizes the handle to the specified size.
     * @see "QuickTime::SetHandleSize"
     * @param size the new size of the handle.
     * @throws quicktime.util.UtilException
     */
    public void setSize(int size) throws UtilException {
        SetHandleSize(_ID(), size);
        UtilException.checkError(MemError());
    }

    /**
     * Concatenates the supplied handle to this handle, resize the size of this handle
     * to accomodate the second handle's data. Subclasses should overide this is a straight
     * concatenation of the two handles are insufficient to preserve state.
     * @see "QuickTime::HandAndHand"
     */
    public void concatenate(QTHandleRef h1) throws UtilException {
        h1.doLock();
        int i = HandAndHand(QTObject.ID(h1), _ID());
        if (!h1.isLocked())
            h1.unlock();
        UtilException.checkError(i);
    }

    public static QTHandle fromImageCompressionDialog(ImageCompressionDialog ci) throws StdQTException {
        int[] destArray = { 0 };
        StdQTException.checkError(SCGetInfo(QTObject.ID(ci), 1667523939, destArray));
        return new QTHandle(destArray[0], null, false);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    @Override
    public Object clone() {
        return new QTHandle(makeAndCopyHandle(), null, false);
    }

    private static short GetSoundDescriptionExtension(int desc, int[] extension, int idType) {
        return SoundManager.GetSoundDescriptionExtension(desc, extension, idType);
    }

    private static short GetImageDescriptionExtension(int desc, int[] extension, int idType, int index) {
        return ImageCompressionManager.GetImageDescriptionExtension(desc, extension, idType, index);
    }

    private static int GraphicsImportGetDataHandle(int gi, int[] h) {
        return GraphicsImportManager.GraphicsImportGetDataHandle(gi, h);
    }

    private static int GraphicsImportGetAliasedDataReference(int gi, int[] dataRef, int[] dataRefType) {
        return GraphicsImportManager.GraphicsImportGetAliasedDataReference(gi, dataRef, dataRefType);
    }

    private static int TCGetTimeCodeAtTime(int mh, int mediaTime, int[] frameNumber, byte[] tcdef, byte[] tcrec, int[] srcRef) {
        return TimeCodeManager.TCGetTimeCodeAtTime(mh, mediaTime, frameNumber, tcdef, tcrec, srcRef);
    }

    private static int TCGetCurrentTimeCode(int mh, int[] frameNumber, byte[] tcdef, byte[] tcrec, int[] srcRef) {
        return TimeCodeManager.TCGetCurrentTimeCode(mh, frameNumber, tcdef, tcrec, srcRef);
    }

    private static int TCGetSourceRef(int mh, int tcdH, int[] srefH) {
        return TimeCodeManager.TCGetSourceRef(mh, tcdH, srefH);
    }

    private static short GetTrackSoundLocalizationSettings(int track, int[] settings) {
        return MovieToolbox.GetTrackSoundLocalizationSettings(track, settings);
    }

    private static void ReserveMem(int size) {
        MemoryManager.ReserveMem(size);
    }

    private static short MemError() {
        return MemoryManager.MemError();
    }

    private static int NewHandleClear(int size) {
        return MemoryManager.NewHandleClear(size);
    }

    private static short PtrToHand(int src, int[] dstHandle, int size) {
        return MemoryManager.PtrToHand(src, dstHandle, size);
    }

    private static void SetHandleSize(int h, int size) {
        MemoryManager.SetHandleSize(h, size);
    }

    private static short HandAndHand(int h1, int h2) {
        return MemoryManager.HandAndHand(h1, h2);
    }

    private static int GraphicsExportGetInputHandle(int ge, int[] h) {
        return GraphicsExportManager.GraphicsExportGetInputHandle(ge, h);
    }

    private static int GraphicsExportGetOutputHandle(int ge, int[] h) {
        return GraphicsExportManager.GraphicsExportGetOutputHandle(ge, h);
    }

    private static int GraphicsExportGetSettingsAsText(int ge, int[] text) {
        return GraphicsExportManager.GraphicsExportGetSettingsAsText(ge, text);
    }

    private static int SCGetInfo(int ci, int type, int[] destArray) {
        return ImageCompressionManager.SCGetInfo(ci, type, destArray);
    }
}
