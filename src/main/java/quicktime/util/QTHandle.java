package quicktime.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.GraphicsExportManager;
import quicktime.internal.toolbox.GraphicsImportManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.SoundManager;
import quicktime.jdirect.QTNative;
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

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(QTHandle.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Determines the data handle that is the source for input images to graphics export component
     * QuickTime::GraphicsExportGetSettingsAsText
     * @return The handle to a QuickTime data handle.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.image.GraphicsExporter#getInputHandle()}
     */
    public static QTHandle fromGraphicsExporterText(GraphicsExporter paramGraphicsExporter) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetSettingsAsText(QTObject.ID(paramGraphicsExporter), arrayOfInt);
        StdQTException.checkError(i);
        return new QTHandle(arrayOfInt[0], null, false);
    }

    /**
     * Determines the data handle that is the source for input images to graphics export component
     * QuickTime::GraphicsExportGetInputHandle
     * @return The handle to a QuickTime data handle.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.image.GraphicsExporter#getInputHandle()}
     */
    public static QTHandle fromGraphicsExporterInput(GraphicsExporter paramGraphicsExporter) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsExportGetInputHandle(QTObject.ID(paramGraphicsExporter), arrayOfInt);
        StdQTException.checkError(i);
        return new QTHandle(arrayOfInt[0], null, false);
    }

    /**
     * Determines the data handle that is the source for input images to graphics export component
     * QuickTime::GraphicsExportGetOutputHandle
     * @return The handle to a QuickTime data handle.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.image.GraphicsExporter#getOutputHandle()}
     */
    public static QTHandle fromGraphicsExporterOutput(GraphicsExporter paramGraphicsExporter) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsExportGetOutputHandle(QTObject.ID(paramGraphicsExporter), arrayOfInt);
        StdQTException.checkError(i);
        return new QTHandle(arrayOfInt[0], null, false);
    }

    /**
     * Retrieve application specific info from a Sound Description object
     * QuickTime::GetSoundDescriptionExtension
     * @param idType an OSType identifying the information for removal
     * @return a dataref to the Extension
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.media.SoundDescription#getExtension(int)}
     */
    public static QTHandle fromSoundDescription(SoundDescription paramSoundDescription, int paramInt) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = GetSoundDescriptionExtension(QTObject.ID(paramSoundDescription), arrayOfInt, paramInt);
        StdQTException.checkError(i);
        return new QTHandle(arrayOfInt[0], null, false);
    }

    /**
     * Retrieve application specific info from a ImageDescription object
     * QuickTime::GetImageDescriptionExtension
     * @param idType an OSType identifying the information for removal
     * @return a dataref to the Extension
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.image.ImageDescription#getExtension(int, int)}
     */
    public static QTHandle fromImageDescription(ImageDescription paramImageDescription, int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetImageDescriptionExtension(QTObject.ID(paramImageDescription), arrayOfInt, paramInt1, paramInt2));
        return new QTHandle(arrayOfInt[0], null, false);
    }

    /**
     * Determines the data reference that the graphics resides in.
     * QuickTime::GraphicsImportGetDataHandle
     * @return The handle to a QuickTime data reference.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.image.GraphicsImporter#getDataHandle()}
     */
    public static QTHandle fromGraphicsImporterData(GraphicsImporter paramGraphicsImporter) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsImportGetDataHandle(QTObject.ID(paramGraphicsImporter), arrayOfInt);
        StdQTException.checkError(i);
        return new QTHandle(arrayOfInt[0], null, false);
    }

    /**
     * Determines the aliased data reference.
     * QuickTime::GraphicsImportGetAliasedDataReference
     * @return The handle to the aliased data reference
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.image.GraphicsImporter#getAliasedDataReference()}
     */
    public static QTHandle fromGraphicsImporterAlias(GraphicsImporter paramGraphicsImporter) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetAliasedDataReference(QTObject.ID(paramGraphicsImporter), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return new QTHandle(arrayOfInt1[0], null, false);
    }

    /**
     * Determines the time code from the time code handler.
     * QuickTime::TCGetTimeCodeAtTime()
     * @param mediaTime The time value of the media at which to retrieve the time code information.
     * @return The information object containing the time code, its definition, and data.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.qtcomponents.TimeCoder#getAtTime(int)}
     */
    public static TimeCodeInfo fromTimeCoderTime(TimeCoder paramTimeCoder, int paramInt) throws QTException {
        int[] arrayOfInt1 = new int[1];
        TimeCodeTime localTimeCodeTime = new TimeCodeTime();
        int[] arrayOfInt2 = new int[1];
        TimeCodeInfo localTimeCodeInfo = new TimeCodeInfo();
        int i = TCGetTimeCodeAtTime(QTObject.ID(paramTimeCoder), paramInt, arrayOfInt1, localTimeCodeInfo.definition.getBytes(), localTimeCodeTime.getBytes(), arrayOfInt2);
        StdQTException.checkError(i);
        if (arrayOfInt2[0] != 0) {
            localTimeCodeInfo.userData = new QTHandle(arrayOfInt2[0], null, false);
        } else {
            localTimeCodeInfo.userData = null;
        }
        localTimeCodeInfo.counter = localTimeCodeTime.toCounter();
        localTimeCodeInfo.time = localTimeCodeTime;
        localTimeCodeInfo.frameNumber = arrayOfInt1[0];
        return localTimeCodeInfo;
    }

    /**
     * Determines the time code from the time code handler at the current time value.
     * QuickTime::TCGetCurrentTimeCode()
     * @return The information object containing the time code, its definition, and data.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.qtcomponents.TimeCoder#getCurrent()}
     */
    public static TimeCodeInfo fromTimeCoderCurrent(TimeCoder paramTimeCoder) throws QTException {
        int[] arrayOfInt1 = new int[1];
        TimeCodeTime localTimeCodeTime = new TimeCodeTime();
        int i = 0;
        int[] arrayOfInt2 = new int[1];
        TimeCodeInfo localTimeCodeInfo = new TimeCodeInfo();
        int j = TCGetCurrentTimeCode(QTObject.ID(paramTimeCoder), arrayOfInt1, localTimeCodeInfo.definition.getBytes(), localTimeCodeTime.getBytes(), arrayOfInt2);
        StdQTException.checkError(j);
        if (arrayOfInt2[0] != 0) {
            localTimeCodeInfo.userData = new QTHandle(arrayOfInt2[0], null, false);
        } else {
            localTimeCodeInfo.userData = null;
        }
        localTimeCodeInfo.counter = localTimeCodeTime.toCounter();
        localTimeCodeInfo.time = localTimeCodeTime;
        localTimeCodeInfo.frameNumber = arrayOfInt1[0];
        return localTimeCodeInfo;
    }

    /**
     * Allows you to retrieve the source information from the time code media sample.
     * QuickTime::TCGetSourceRef()
     * @param tcd the description that describes the time code reference to be changed
     * @return the source information that is found in the sample reference
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.qtcomponents.TimeCoder#getSourceRef(quicktime.std.qtcomponents.TimeCodeDescription)}
     */
    public static QTHandle fromTimeCoderSource(TimeCoder paramTimeCoder, TimeCodeDescription paramTimeCodeDescription) throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(TCGetSourceRef(QTObject.ID(paramTimeCoder), QTObject.ID(paramTimeCodeDescription), arrayOfInt));
        return new QTHandle(arrayOfInt[0], null, false);
    }

    /**
     * This method returns the sound localization settings for the track.
     * QuickTime::GetTrackSoundLocalizationSettings()
     * @return the sound localization settings for the track.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Track#getSoundLocalizationSettings()}
     */
    public static QTHandle fromTrack(Track paramTrack) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = GetTrackSoundLocalizationSettings(QTObject.ID(paramTrack), arrayOfInt);
        StdQTException.checkError(i);
        return new QTHandle(arrayOfInt[0], null, false);
    }

    /**
     * Returns EncodedImage data as a QTHandle. This will copy the data.
     * @param image the encoded image data
     * @return the encoded image data as a Handle
     * @return {@link quicktime.QTException}
     */
    public static QTHandle fromEncodedImage(EncodedImage paramEncodedImage) throws QTException {
        if ((paramEncodedImage instanceof ByteEncodedImage))
            return new QTHandle(((ByteEncodedImage) paramEncodedImage).getBytes());
        if ((paramEncodedImage instanceof IntEncodedImage)) {
            QTHandle localQTHandle = new QTHandle(paramEncodedImage.getSize(), false);
            int[] arrayOfInt = ((IntEncodedImage) paramEncodedImage).getInts();
            localQTHandle.copyFromArray(0, arrayOfInt, 0, arrayOfInt.length);
            return localQTHandle;
        }
        if ((paramEncodedImage instanceof RawEncodedImage)) {
            return new QTHandle((RawEncodedImage) paramEncodedImage, 0, paramEncodedImage.getSize());
        }
        return null;
    }

    /**
     * Use reserve memory before you allocate a handle that you intend to keep locked
     * for long periods of time.
     * QuickTime::ReserveMem
     * @param size the size of the handle you will allocate in the new Handle(size...) call
     * @param {@link quicktime.util.UtilException}
     */
    public static void reserveMemory(int paramInt) throws UtilException {
        ReserveMem(paramInt);
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
     * QuickTime::NewHandle
     * QuickTime::NewHandleClear
     * @param size the size of the handle to create.
     * @param clear clear the handle upon creation.
     */
    public QTHandle(int paramInt, boolean paramBoolean) throws QTException {
        super(paramInt, paramBoolean);
    }

    /**
     * Construct a handle from input data.
     * @param byteArray the input data.
     */
    public QTHandle(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte.length, false);
        setBytesAt(0, paramArrayOfByte.length, paramArrayOfByte, 0);
    }

    /**
     * This constructor is used to create a QTHandle object from a QTPointer.
     * The contents of the pointer will be copied into the handle. This will copy
     * size bytes from the pointer + offset. It will bounds check the size to be not
     * greater than the size of the pointer.
     * QuickTime::PtrToHand
     * @param ptr the pointer to copy.
     * @param offset you can specify an offset within the pointer from which to copy the data
     * @param size the amount of bytes to copy
     */
    public QTHandle(QTPointerRef paramQTPointerRef, int paramInt1, int paramInt2) throws QTException {
        this(copyPtr(paramQTPointerRef, paramInt1, paramInt2), null, false);
    }

    /**
     * Construct a handle from the concatenation of the two handles supplied. The First handle
     * will appear first in the concatentated result.
     * @param firstHandle the first Handle
     * @param secondHandle the second Handle
     */
    public QTHandle(QTHandleRef paramQTHandleRef1, QTHandleRef paramQTHandleRef2) throws QTException {
        this(paramQTHandleRef1.makeAndCopyHandle(), null, false);
        concatenate(paramQTHandleRef2);
    }

    protected QTHandle(int paramInt, Object paramObject, boolean paramBoolean) {
        super(paramInt, paramObject, paramBoolean);
    }

    private static int copyPtr(QTPointerRef paramQTPointerRef, int paramInt1, int paramInt2) throws UtilException {
        paramInt2 = paramInt2 + paramInt1 > paramQTPointerRef.getSize() ? paramQTPointerRef.getSize() - paramInt1 : paramInt2;
        int[] arrayOfInt = { 0 };
        arrayOfInt[0] = NewHandleClear(4);
        UtilException.checkError(PtrToHand(QTObject.ID(paramQTPointerRef) + paramInt1, arrayOfInt, paramInt2));
        return arrayOfInt[0];
    }

    /**
     * Resizes the handle to the specified size.
     * QuickTime::SetHandleSize
     * @param size the new size of the handle.
     * @param {@link quicktime.util.UtilException}
     */
    public void setSize(int paramInt) throws UtilException {
        SetHandleSize(_ID(), paramInt);
        UtilException.checkError(MemError());
    }

    /**
     * Concatenates the supplied handle to this handle, resize the size of this handle
     * to accomodate the second handle's data. Subclasses should overide this is a straight
     * concatenation of the two handles are insufficient to preserve state.
     * QuickTime::HandAndHand
     */
    public void concatenate(QTHandleRef paramQTHandleRef) throws UtilException {
        paramQTHandleRef.doLock();
        int i = HandAndHand(QTObject.ID(paramQTHandleRef), _ID());
        if (!paramQTHandleRef.isLocked())
            paramQTHandleRef.unlock();
        UtilException.checkError(i);
    }

    public static QTHandle fromImageCompressionDialog(ImageCompressionDialog paramImageCompressionDialog) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetInfo(QTObject.ID(paramImageCompressionDialog), 1667523939, arrayOfInt));
        return new QTHandle(arrayOfInt[0], null, false);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    public Object clone() {
        return new QTHandle(makeAndCopyHandle(), null, false);
    }

    private static short GetSoundDescriptionExtension(int paramInt1, int[] paramArrayOfInt, int paramInt2) {
        return SoundManager.GetSoundDescriptionExtension(paramInt1, paramArrayOfInt, paramInt2);
    }

    private static short GetImageDescriptionExtension(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3) {
        return ImageCompressionManager.GetImageDescriptionExtension(paramInt1, paramArrayOfInt, paramInt2, paramInt3);
    }

    private static int GraphicsImportGetDataHandle(int paramInt, int[] paramArrayOfInt) {
        return GraphicsImportManager.GraphicsImportGetDataHandle(paramInt, paramArrayOfInt);
    }

    private static int GraphicsImportGetAliasedDataReference(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return GraphicsImportManager.GraphicsImportGetAliasedDataReference(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int TCGetTimeCodeAtTime(int paramInt1, int paramInt2, int[] paramArrayOfInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt2) {
        return ComponentManager.TCGetTimeCodeAtTime(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfInt2);
    }

    private static int TCGetCurrentTimeCode(int paramInt, int[] paramArrayOfInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt2) {
        return ComponentManager.TCGetCurrentTimeCode(paramInt, paramArrayOfInt1, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfInt2);
    }

    private static int TCGetSourceRef(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ComponentManager.TCGetSourceRef(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short GetTrackSoundLocalizationSettings(int paramInt, int[] paramArrayOfInt) {
        return MovieToolbox.GetTrackSoundLocalizationSettings(paramInt, paramArrayOfInt);
    }

    private static void ReserveMem(int paramInt) {
        MemoryManager.ReserveMem(paramInt);
    }

    private static short MemError() {
        return MemoryManager.MemError();
    }

    private static int NewHandleClear(int paramInt) {
        return MemoryManager.NewHandleClear(paramInt);
    }

    private static short PtrToHand(int paramInt1, int[] paramArrayOfInt, int paramInt2) {
        return MemoryManager.PtrToHand(paramInt1, paramArrayOfInt, paramInt2);
    }

    private static void SetHandleSize(int paramInt1, int paramInt2) {
        MemoryManager.SetHandleSize(paramInt1, paramInt2);
    }

    private static short HandAndHand(int paramInt1, int paramInt2) {
        return MemoryManager.HandAndHand(paramInt1, paramInt2);
    }

    private static int GraphicsExportGetInputHandle(int paramInt, int[] paramArrayOfInt) {
        return GraphicsExportManager.GraphicsExportGetInputHandle(paramInt, paramArrayOfInt);
    }

    private static int GraphicsExportGetOutputHandle(int paramInt, int[] paramArrayOfInt) {
        return GraphicsExportManager.GraphicsExportGetOutputHandle(paramInt, paramArrayOfInt);
    }

    private static int GraphicsExportGetSettingsAsText(int paramInt, int[] paramArrayOfInt) {
        return GraphicsExportManager.GraphicsExportGetSettingsAsText(paramInt, paramArrayOfInt);
    }

    private static int SCGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ImageCompressionManager.SCGetInfo(paramInt1, paramInt2, paramArrayOfInt);
    }
}

