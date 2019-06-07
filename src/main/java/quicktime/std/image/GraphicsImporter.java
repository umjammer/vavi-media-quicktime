package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.GDevice;
import quicktime.qd.Pict;
import quicktime.qd.QDColor;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.UserData;
import quicktime.util.ByteEncodedImage;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.RawEncodedImage;

/**
 * Assists in managing still images. Allows display and manipulation of various
 * types of image data. This is regardless of file format or compression used
 * in the document. The QIF(QuickTime File Format, 'qtif') accepts gif, jfif,
 * pict, photoshop, and sgi.rgb files. This class implements methods for a
 * corresponding QuickTime ComponentInstance structure.
 * refer to GraphicsImportComponent in QuickTime
 */
public final class GraphicsImporter extends Component implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(GraphicsImporter.class);
                    }
                });
            }
        }.establish();
    }

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    /**
     * Determines the GraphicsImporter that is the source for input images to graphics export component
     * QuickTime::GraphicsExportGetInputGraphicsImporter
     * @return a GraphicsImporter object that has the source of input images to the Graphics exporter.
     * @return {@link quicktime.QTException}
     */
    public static final GraphicsImporter fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws QTException {
        return new GraphicsImporter(paramGraphicsExporter);
    }

    private GraphicsImporter(GraphicsExporter paramGraphicsExporter) throws QTException {
        super(allocate(paramGraphicsExporter), null);
    }

    /**
     * Opens the specified graphics import component,'grip', with the given subType.
     * QuickTime::OpenADefaultComponent
     * @param subType The graphics import component sub type
     */
    public GraphicsImporter(int paramInt) throws QTException {
        super(1735551344, paramInt);
    }

    /**
     * Determines and opens the graphics import component to use for the given file.
     * QuickTime::GetGraphicsImporterForFile
     * @param file Specifies the source file.
     */
    public GraphicsImporter(QTFile paramQTFile) throws QTException {
        super(allocate(paramQTFile), null);
    }

    /**
     * Determines and opens the graphics import component to use for the given file.
     * QuickTime::GetGraphicsImporterForFile
     * @param file Specifies the source file.
     * @param flags options passed in when opening the component
     */
    public GraphicsImporter(QTFile paramQTFile, int paramInt) throws QTException {
        super(allocate(paramQTFile, paramInt), null);
    }

    /**
     * Determines and opens the component to use for the given data reference and data type.
     * QuickTime::GetGraphicsImporterForDataRef
     * @param dataRef Specifies the data reference.
     */
    public GraphicsImporter(DataRef paramDataRef) throws QTException {
        super(allocate(paramDataRef), null);
        this.ref = paramDataRef;
    }

    /**
     * Locates and opens a graphics importer component that can be used to draw the specified dataRef.
     * QuickTime::GetGraphicsImporterForDataRefWithFlags
     * @param dataRef Specifies the data reference.
     * @param flags options passed in when opening the component
     */
    public GraphicsImporter(DataRef paramDataRef, int paramInt) throws QTException {
        super(allocate(paramDataRef, paramInt), null);
        this.ref = paramDataRef;
    }

    private static int allocate(QTFile paramQTFile) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForFile(paramQTFile.getFSSpec(true, 256), arrayOfInt);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    private static int allocate(GraphicsExporter paramGraphicsExporter) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetInputGraphicsImporter(QTObject.ID(paramGraphicsExporter), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    private static int allocate(QTFile paramQTFile, int paramInt) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForFileWithFlags(paramQTFile.getFSSpec(true, 256), arrayOfInt, paramInt);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    private static int allocate(DataRef paramDataRef) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForDataRef(QTObject.ID(paramDataRef), paramDataRef.getType(), arrayOfInt);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        if (arrayOfInt[0] == 0)
            throw new StdQTException(63524);
        return arrayOfInt[0];
    }

    private static int allocate(DataRef paramDataRef, int paramInt) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForDataRefWithFlags(QTObject.ID(paramDataRef), paramDataRef.getType(), arrayOfInt, paramInt);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        if (arrayOfInt[0] == 0)
            throw new StdQTException(63524);
        return arrayOfInt[0];
    }

    private Object ref = null;

    /**
     * Specifies the data reference that the graphics resides in.
     * QuickTime::GraphicsImportSetDataReference
     * @param dataRef Specifies the handle to a QuickTime data reference.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setDataReference(DataRef paramDataRef) throws StdQTException {
        int i = GraphicsImportSetDataReference(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType());
        StdQTException.checkError(i);
        this.ref = paramDataRef;
    }

    /**
     * Determines the type of the data reference that the graphics resides in.
     * QuickTime::GraphicsImportGetDataReference
     * @return The data reference type.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getDataReferenceType() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetDataReference(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    /**
     * Specifies the file that the graphics resides in.
     * QuickTime::GraphicsImportSetDataFile
     * @param fileIn Specifies the source file containing the graphics data.
     * @param {@link quicktime.QTException}
     */
    public final void setDataFile(QTFile paramQTFile) throws QTException {
        int i = GraphicsImportSetDataFile(_ID(), paramQTFile.getFSSpec(true, 256));
        StdQTException.checkError(i);
        this.ref = null;
    }

    /**
     * Determines the file that the graphics resides in.
     * QuickTime::GraphicsImportGetDataFile
     * @return The file that contains the graphics data.
     * @return {@link quicktime.QTException}
     */
    public final QTFile getDataFile() throws QTException {
        return QTFile.fromGraphicsImporter(this);
    }

    /**
     * Specifies the data handle that the graphics resides in.
     * QuickTime::GraphicsImportSetDataHandle
     * @param dataRef Specifies the handle to a QuickTime data reference.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setDataHandle(QTHandleRef paramQTHandleRef) throws StdQTException {
        int i = GraphicsImportSetDataHandle(_ID(), QTObject.ID(paramQTHandleRef));
        StdQTException.checkError(i);
        this.ref = paramQTHandleRef;
    }

    /**
     * Determines the data reference that the graphics resides in.
     * QuickTime::GraphicsImportGetDataHandle
     * @return The handle to a QuickTime data reference.
     * @return {@link quicktime.QTException}
     */
    public final QTHandle getDataHandle() throws QTException {
        return QTHandle.fromGraphicsImporterData(this);
    }

    /**
     * Determines the image description information.
     * QuickTime::GraphicsImportGetImageDescription
     */
    public final ImageDescription getImageDescription() throws StdQTException {
        return ImageDescription.fromGraphicsImporter(this);
    }

    /**
     * Determines the data offset.
     * QuickTime::GraphicsImportGetDataOffsetAndSize
     * @return The offset for the data.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getDataOffset() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetDataOffsetAndSize(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0];
    }

    /**
     * Determines the data size.
     * QuickTime::GraphicsImportGetDataOffsetAndSize
     * @return The size of the data.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getDataSize() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetDataOffsetAndSize(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    /**
     * Reads image data. The pointer must be at least offset + size big.
     * QuickTime::GraphicsImportReadData
     * @param data The EncodedImage that the data is read into.
     * @param dataOffset Specifies the offset within the pointer where the data is placed. This offset is in bytes.
     * @param dataSize Specifies the size of the data to be read.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void readData(EncodedImage paramEncodedImage, int paramInt1, int paramInt2) throws StdQTException {
        int i = -50;
        if ((paramEncodedImage instanceof ByteEncodedImage)) {
            i = GraphicsImportReadData(_ID(), ((ByteEncodedImage) paramEncodedImage).getBytes(), paramInt1, paramInt2);
        } else if ((paramEncodedImage instanceof IntEncodedImage)) {
            i = GraphicsImportReadData(_ID(), ((IntEncodedImage) paramEncodedImage).getInts(), paramInt1, paramInt2);
        } else if ((paramEncodedImage instanceof RawEncodedImage))
            i = GraphicsImportReadData(_ID(), QTObject.ID((RawEncodedImage) paramEncodedImage), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * Defines the clipping region for drawing.
     * QuickTime::GraphicsImportSetClip
     * @param clipRgn Specifies clipping region.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setClip(Region paramRegion) throws StdQTException {
        int i = GraphicsImportSetClip(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(i);
    }

    /**
     * Determines the current clipping region.
     * QuickTime::GraphicsImportGetClip
     * @return The current clip region or null, if not possible
     * @return {@link quicktime.QTException}
     */
    public final Region getClip() throws QTException {
        return Region.fromGraphicsImporter(this);
    }

    /**
     * Sets the source rectangle to use for an image.
     * QuickTime::GraphicsImportSetSourceRect
     */
    public final void setSourceRect(QDRect paramQDRect) throws StdQTException {
        int i = GraphicsImportSetSourceRect(_ID(), paramQDRect.getRect());
        StdQTException.checkError(i);
    }

    /**
     * Determines the current source rectangle for an image.
     * QuickTime::GraphicsImportGetSourceRect
     * @return The current source rectangle for an image.
     * @return {@link quicktime.std.StdQTException}
     */
    public final QDRect getSourceRect() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetSourceRect(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    /**
     * Determines the bounding rectangle of an image.
     * QuickTime::GraphicsImportGetNaturalBounds
     * @return The size of the bounding rectangle for an image.
     * @return {@link quicktime.std.StdQTException}
     */
    public final QDRect getNaturalBounds() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetNaturalBounds(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    /**
     * Draws an image.
     * QuickTime::GraphicsImportDraw
     */
    public final void draw() throws StdQTException {
        int i = GraphicsImportDraw(_ID());
        StdQTException.checkError(i);
    }

    /**
     * Defines the matrix to use for drawing.
     * QuickTime::GraphicsImportSetMatrix
     * @param matrix Specifies the drawing matrix.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setMatrix(Matrix paramMatrix) throws StdQTException {
        int i = GraphicsImportSetMatrix(_ID(), paramMatrix.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * Determines the matrix to be used for drawing.
     * QuickTime::GraphicsImportGetMatrix
     * @return The matrix specification to use for drawing.
     * @return {@link quicktime.std.StdQTException}
     */
    public final Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = GraphicsImportGetMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    /**
     * Defines the rectangle in which to draw an image.
     * QuickTime::GraphicsImportSetBoundsRect
     * @param bounds Specifies the bounding rectangle.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setBoundsRect(QDRect paramQDRect) throws StdQTException {
        int i = GraphicsImportSetBoundsRect(_ID(), paramQDRect.getRect());
        StdQTException.checkError(i);
    }

    /**
     * Determines the bounding rectangle for drawing.
     * QuickTime::GraphicsImportGetBoundsRect
     * @return The bounding rectangle.
     * @return {@link quicktime.std.StdQTException}
     */
    public final QDRect getBoundsRect() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetBoundsRect(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    /**
     * Creates a QuickDraw picture file.
     * QuickTime::GraphicsImportSaveAsPicture
     * @param file The destination output file of the compressed image
     * @param scriptTag Specifies the depth at which the image is to be compressed.
     * @param {@link quicktime.QTException}
     */
    public final void saveAsPicture(QTFile paramQTFile, int paramInt) throws QTException {
        int i = GraphicsImportSaveAsPicture(_ID(), paramQTFile.getFSSpec(false, 512), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Creates a QuickDraw image file.
     * QuickTime::GraphicsImportSaveAsQuickTimeImageFile
     * @param scriptTag Specifies
     * @param {@link quicktime.QTException}
     */
    public final void saveAsQuickTimeImageFile(QTFile paramQTFile, int paramInt) throws QTException {
        int i = GraphicsImportSaveAsQuickTimeImageFile(_ID(), paramQTFile.getFSSpec(false, 512), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Sets the graphics mode and color for an image.
     * QuickTime::GraphicsImportSetGraphicsMode
     * @param graphicsMode Specifies the graphics transfer mode.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setGraphicsMode(GraphicsMode paramGraphicsMode) throws StdQTException {
        int i = GraphicsImportSetGraphicsMode(_ID(), paramGraphicsMode.getGraphicsMode(), paramGraphicsMode.getColor().getRGBColor());
        StdQTException.checkError(i);
    }

    /**
     * Determines the graphics mode for an image.
     * QuickTime::GraphicsImportGetGraphicsMode
     * @return The graphics transfer mode.
     * @return {@link quicktime.std.StdQTException}
     */
    public final GraphicsMode getGraphicsMode() throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[6];
        int i = GraphicsImportGetGraphicsMode(_ID(), arrayOfInt, arrayOfByte);
        StdQTException.checkError(i);
        return new GraphicsMode(arrayOfInt[0], QDColor.fromArray(arrayOfByte, 6));
    }

    /**
     * Sets the image quality value.
     * QuickTime::GraphicsImportSetQuality
     * @param quality Specifies a quality CodecQ value
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setQuality(int paramInt) throws StdQTException {
        int i = GraphicsImportSetQuality(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Determines the image quality value.
     * QuickTime::GraphicsImportGetQuality
     * @return The image quality value.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getQuality() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsImportGetQuality(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Determines the aliased data reference.
     * QuickTime::GraphicsImportGetAliasedDataReference
     * @return The handle to the aliased data reference
     * @return {@link quicktime.QTException}
     */
    public final QTHandle getAliasedDataReference() throws QTException {
        return QTHandle.fromGraphicsImporterAlias(this);
    }

    /**
     * Determines the aliased data type.
     * QuickTime::GraphicsImportGetAliasedDataReference
     * @return The aliased data type
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getAliasedDataType() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetAliasedDataReference(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    /**
     * Call this method to give the presentable the QDGraphics
     * object it should use to do its drawing.
     * QuickTime::GraphicsImportSetGWorld
     * @param port a QDGraphics object or null to use current port
     * @param gdh a GDevice object or null to use current device
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setGWorld(QDGraphics paramQDGraphics, GDevice paramGDevice) throws StdQTException {
        int i = GraphicsImportSetGWorld(_ID(), QTObject.ID(paramQDGraphics), QTObject.ID(paramGDevice));
        StdQTException.checkError(i);
    }

    /**
     * Call this method to obtain the QDGraphics of the object.
     * QuickTime::GraphicsImportGetGWorld
     * @return The current destination graphics port
     * @return {@link quicktime.std.StdQTException}
     */
    public final QDGraphics getGWorld() throws StdQTException {
        return QDGraphics.fromGraphicsImporter(this);
    }

    /**
     * Determines if the current graphics importer data reference contains valid image data.
     * Not all graphics importers implement this method.
     * QuickTime::GraphicsImportValidate
     * @return True if a valid operation, false otherwise.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean validate() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsImportValidate(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0] != 0;
    }

    /**
     * Determines if the GraphicsImporter will draw all the pixels that are contained in the
     * image data that it is presenting. Some image formats (for example GIF) allow a color to
     * be set as invisible or transparent.
     * QuickTime::GraphicsImportDoesDrawAllPixels
     * @return a value that indicates if this instance of the importer will draw all pixels
     * @return {@link quicktime.std.StdQTException}
     */
    public final int doesDrawAllPixels() throws StdQTException {
        short[] arrayOfShort = { 0 };
        int i = GraphicsImportDoesDrawAllPixels(_ID(), arrayOfShort);
        if (i == -2147450878)
            return 0;
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Creates a new Pict containing the image currently in use by this import component.
     * QuickTime::GraphicsImportGetAsPicture
     * @return a pict
     * @return {@link quicktime.QTException}
     */
    public Pict getAsPicture() throws QTException {
        return Pict.fromGraphicsImporter(this);
    }

    /**
     * Gets metadata from an image file and adds it to a user data structure.
     * QuickTime::GraphicsImportGetMetaData
     * @return a UserData structure
     * @return {@link quicktime.QTException}
     */
    public UserData getMetaData(GraphicsImporter paramGraphicsImporter) throws QTException {
        UserData localUserData = new UserData();
        int i = GraphicsImportGetMetaData(QTObject.ID(paramGraphicsImporter), QTObject.ID(localUserData));
        StdQTException.checkError(i);
        return localUserData;
    }

    /**
     * Returns a list of MIME types supported by the graphics import component.
     * QuickTime::GraphicsImportGetMIMETypeList
     * @return a QT atom container that contains a list of MIME types supported by the graphics import component.
     * @return {@link quicktime.std.StdQTException}
     */
    public AtomContainer getMIMETypeList() throws StdQTException {
        return AtomContainer.fromGraphicsImporterMIME(this);
    }

    /**
     * Saves an image in a foreign file format.
     * QuickTime::GraphicsImportExportImageFile
     * @param fileType the file type
     * @param fileCreator the fie creator
     * @param theFile the output file to write to.
     * @param {@link quicktime.QTException}
     */
    public void exportImageFile(int paramInt1, int paramInt2, QTFile paramQTFile, int paramInt3) throws QTException {
        int i = GraphicsImportExportImageFile(_ID(), paramInt1, paramInt2, paramQTFile.getFSSpec(false, 512), (short) paramInt3);
        StdQTException.checkError(i);
    }

    /**
     * Returns information about available export formats.
     * QuickTime::GraphicsImportGetExportImageTypeList
     * @return a QT atom container that contains a list of available export formats. supported by the graphics import component.
     * @return {@link quicktime.std.StdQTException}
     */
    public final AtomContainer getExportImageTypeList() throws StdQTException {
        return AtomContainer.fromGraphicsImporterExportImage(this);
    }

    /**
     * Returns the Export settings as an Atom Container.
     * QuickTime::GraphicsImportGetExportSettingsAsAtomContainer
     * @return a QT atom container that contains Export settings.
     * @return {@link quicktime.std.StdQTException}
     */
    public final AtomContainer getExportSettingsAsAtomContainer() throws StdQTException {
        return AtomContainer.fromGraphicsImporterExportSettings(this);
    }

    /**
     * Export settings from a QuickTime atom container. These settings configure how images will be saved by the
     * GraphicsImportExportImageFile function..
     * QuickTime::GraphicsImportSetExportSettingsFromAtomContainer
     * @param qtAtomContainer the atomcontainer with the export settings.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setExportSettingsFromAtomContainer(AtomContainer paramAtomContainer) throws StdQTException {
        int[] arrayOfInt = { 0 };
        arrayOfInt[0] = QTObject.ID(paramAtomContainer);
        int i = GraphicsImportSetExportSettingsFromAtomContainer(_ID(), arrayOfInt);
        StdQTException.checkError(i);
    }

    /**
     * Presents a dialog box letting the user save an image in a foreign file format.
     * QuickTime::GraphicsImportDoExportImageFileDialog
     * @param inDefaultSpec suggests the default name for the file. If you do not want to suggest a default name, pass null.
     * @param prompt A prompt that appears in the standard put dialog box; it may be nil, in which case a default string is used.
     * @param {@link quicktime.QTException}
     */
    public final GraphicsImporterInfo doExportImageFileDialog(QTFile paramQTFile, String paramString) throws QTException {
        return QTFile.fromGraphicsImporter(this, paramQTFile, paramString);
    }

    /**
     * Specifies the data reference starting offset and data size limit.
     * QuickTime::GraphicsImportSetDataReferenceOffsetAndLimit
     * @param offset The byte offset of the image data from the beginning of the data reference.
     * @param limit the offset of the byte following the last byte of the image data.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setDataReferenceOffsetAndLimit(int paramInt1, int paramInt2) throws StdQTException {
        int i = GraphicsImportSetDataReferenceOffsetAndLimit(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public final int[] getDataReferenceOffsetAndLimit() throws StdQTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int[] arrayOfInt3 = new int[2];
        int i = GraphicsImportGetDataReferenceOffsetAndLimit(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        arrayOfInt3[0] = arrayOfInt1[0];
        arrayOfInt3[1] = arrayOfInt2[0];
        return arrayOfInt3;
    }

    /**
     * Returns the number of images in the image file.
     * The base graphics importer returns a count of 1. Format-specific importers for
     * multiple-image formats should override this function; other importers should delegate it.
     * QuickTime::GraphicsImportGetImageCount
     * @return the number of images.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getImageCount() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsImportGetImageCount(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * The default image index is 1. Image indexes are one-based; zero is considered a special
     * index by some importers, and treated the same as one by others.
     * QuickTime::GraphicsImportSetImageIndex
     * @param imageIndex Specifies the image index.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setImageIndex(int paramInt) throws StdQTException {
        int i = GraphicsImportSetImageIndex(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Returns the current image index.
     * QuickTime::GraphicsImportGetImageIndex
     * @return the image index.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getImageIndex() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsImportGetImageIndex(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Returns the default matrix.
     * QuickTime::GraphicsImportGetDefaultMatrix
     * @return the default matrix
     * @return {@link quicktime.std.StdQTException}
     */
    public final Matrix getDefaultMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = GraphicsImportGetDefaultMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    /**
     * Returns the default matrix.
     * QuickTime::GraphicsImportGetDefaultClip
     * @return the default matrix
     * @return {@link quicktime.QTException}
     */
    public final Region getDefaultClip() throws QTException {
        return Region.fromGraphicsImporterDefault(this);
    }

    /**
     * Returns the default graphics mode, if one is stored in the image. If this method returns
     * badComponentSelector , you should assume a mode of ditherCopy .
     * QuickTime::GraphicsImportGetDefaultGraphicsMode
     * @return the default mode
     * @return {@link quicktime.QTException}
     */
    public final GraphicsMode getDefaultGraphicsMode() throws QTException {
        int[] arrayOfInt = { 0 };
        QDColor localQDColor = new QDColor();
        int i = GraphicsImportGetDefaultGraphicsMode(_ID(), arrayOfInt, localQDColor.getRGBColor());
        StdQTException.checkError(i);
        return new GraphicsMode(arrayOfInt[0], localQDColor);
    }

    /**
     * Returns the default source rect, if one is stored in the image. If this function returns
     * badComponentSelector , you should assume a source rect equal to the natural bounds.
     * QuickTime::GraphicsImportGetDefaultSourceRect
     * @return the default Rect
     * @return {@link quicktime.QTException}
     */
    public final QDRect getDefaultSourceRect() throws QTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetDefaultSourceRect(_ID(), localQDRect.getBytes());
        return localQDRect;
    }

    /**
     * Sets the destination rectangle. You can use this function to define the rectangle into which the extracted source
     * rectangle should be drawn.
     * QuickTime::GraphicsImportSetDestRect
     */
    public final void setDestRect(QDRect paramQDRect) throws QTException {
        int i = GraphicsImportSetDestRect(_ID(), paramQDRect.getBytes());
    }

    /**
     * Returns the destination rectangle.
     * QuickTime::GraphicsImportGetDestRect
     * @return the destination rectangle.
     * @return {@link quicktime.QTException}
     */
    public final QDRect getDestRect() throws QTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetDestRect(_ID(), localQDRect.getBytes());
        return localQDRect;
    }

    /**
     * Sets the current flags.
     * QuickTime::GraphicsImportSetFlags
     * @param flags the flag
     * @param {@link quicktime.QTException}
     */
    public final void setFlags(int paramInt) throws QTException {
        int i = GraphicsImportSetFlags(_ID(), paramInt);
    }

    /**
     * Returns the current flags..
     * QuickTime::GraphicsImportGetFlags
     * @return Returns the current flags.
     * @return {@link quicktime.QTException}
     */
    public final int getFlags() throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsImportGetFlags(_ID(), arrayOfInt);
        return arrayOfInt[0];
    }

    private static native int GraphicsExportGetInputGraphicsImporter(int paramInt, int[] paramArrayOfInt);

    private static native short GetGraphicsImporterForFile(byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native short GetGraphicsImporterForFileWithFlags(byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt);

    private static native short GetGraphicsImporterForDataRef(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short GetGraphicsImporterForDataRefWithFlags(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);

    private static native int GraphicsImportSetDataReference(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsImportGetDataReference(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsImportSetDataFile(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportSetDataHandle(int paramInt1, int paramInt2);

    private static native int GraphicsImportGetDataOffsetAndSize(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsImportReadData(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int GraphicsImportReadData(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);

    private static native int GraphicsImportReadData(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3);

    private static native int GraphicsImportSetClip(int paramInt1, int paramInt2);

    private static native int GraphicsImportSetSourceRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetSourceRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetNaturalBounds(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportDraw(int paramInt);

    private static native int GraphicsImportSetMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportSetBoundsRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetBoundsRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportSaveAsPicture(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    private static native int GraphicsImportSaveAsQuickTimeImageFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    private static native int GraphicsImportSetGraphicsMode(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetGraphicsMode(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportSetQuality(int paramInt1, int paramInt2);

    private static native int GraphicsImportGetQuality(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsImportGetAliasedDataReference(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsImportSetGWorld(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsImportValidate(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsImportDoesDrawAllPixels(int paramInt, short[] paramArrayOfShort);

    private static native int GraphicsImportGetMetaData(int paramInt1, int paramInt2);

    private static native short GraphicsImportExportImageFile(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, short paramShort);

    private static native int GraphicsImportSetExportSettingsFromAtomContainer(int paramInt, int[] paramArrayOfInt);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsImportSetDataReferenceOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsImportGetDataReferenceOffsetAndLimit(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsImportGetImageCount(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsImportSetImageIndex(int paramInt1, int paramInt2);

    private static native int GraphicsImportGetImageIndex(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsImportGetDefaultMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetDefaultGraphicsMode(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetDefaultSourceRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportSetDestRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetDestRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportSetFlags(int paramInt1, int paramInt2);

    private static native int GraphicsImportGetFlags(int paramInt, int[] paramArrayOfInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /GraphicsImporter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
