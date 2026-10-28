package quicktime.std.image;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.GraphicsExportManager;
import quicktime.internal.toolbox.GraphicsImportManager;
import quicktime.internal.toolbox.QuickDrawManager;
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

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    /**
     * Determines the GraphicsImporter that is the source for input images to graphics export component
     * @see "QuickTime::GraphicsExportGetInputGraphicsImporter"
     * @return a GraphicsImporter object that has the source of input images to the Graphics exporter.
     * @throws quicktime.QTException
     */
    public static GraphicsImporter fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws QTException {
        return new GraphicsImporter(paramGraphicsExporter);
    }

    private GraphicsImporter(GraphicsExporter paramGraphicsExporter) throws QTException {
        super(allocate(paramGraphicsExporter), null);
    }

    /**
     * Opens the specified graphics import component,'grip', with the given subType.
     * @see "QuickTime::OpenADefaultComponent"
     * @param subType The graphics import component sub type
     */
    public GraphicsImporter(int subType) throws QTException {
        super(1735551344, subType);
    }

    /**
     * Determines and opens the graphics import component to use for the given file.
     * @see "QuickTime::GetGraphicsImporterForFile"
     * @param file Specifies the source file.
     */
    public GraphicsImporter(QTFile file) throws QTException {
        super(allocate(file), null);
    }

    /**
     * Determines and opens the graphics import component to use for the given file.
     * @see "QuickTime::GetGraphicsImporterForFile"
     * @param file Specifies the source file.
     * @param scriptTag options passed in when opening the component
     */
    public GraphicsImporter(QTFile file, int scriptTag) throws QTException {
        super(allocate(file, scriptTag), null);
    }

    /**
     * Determines and opens the component to use for the given data reference and data type.
     * @see "QuickTime::GetGraphicsImporterForDataRef"
     * @param dataRef Specifies the data reference.
     */
    public GraphicsImporter(DataRef dataRef) throws QTException {
        super(allocate(dataRef), null);
        this.ref = dataRef;
    }

    /**
     * Locates and opens a graphics importer component that can be used to draw the specified dataRef.
     * @see "QuickTime::GetGraphicsImporterForDataRefWithFlags"
     * @param dataRef Specifies the data reference.
     * @param scriptTag options passed in when opening the component
     */
    public GraphicsImporter(DataRef dataRef, int scriptTag) throws QTException {
        super(allocate(dataRef, scriptTag), null);
        this.ref = dataRef;
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

    private static int allocate(QTFile paramQTFile, int scriptTag) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForFileWithFlags(paramQTFile.getFSSpec(true, 256), arrayOfInt, scriptTag);
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

    private static int allocate(DataRef paramDataRef, int scriptTag) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForDataRefWithFlags(QTObject.ID(paramDataRef), paramDataRef.getType(), arrayOfInt, scriptTag);
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
     * @see "QuickTime::GraphicsImportSetDataReference"
     * @param dataRef Specifies the handle to a QuickTime data reference.
     * @throws quicktime.std.StdQTException
     */
    public void setDataReference(DataRef dataRef) throws StdQTException {
        int i = GraphicsImportSetDataReference(_ID(), QTObject.ID(dataRef), dataRef.getType());
        StdQTException.checkError(i);
        this.ref = dataRef;
    }

    /**
     * Determines the type of the data reference that the graphics resides in.
     * @see "QuickTime::GraphicsImportGetDataReference"
     * @return The data reference type.
     * @throws quicktime.std.StdQTException
     */
    public int getDataReferenceType() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetDataReference(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    /**
     * Specifies the file that the graphics resides in.
     * @see "QuickTime::GraphicsImportSetDataFile"
     * @param fileIn Specifies the source file containing the graphics data.
     * @throws quicktime.QTException
     */
    public void setDataFile(QTFile fileIn) throws QTException {
        int i = GraphicsImportSetDataFile(_ID(), fileIn.getFSSpec(true, 256));
        StdQTException.checkError(i);
        this.ref = null;
    }

    /**
     * Determines the file that the graphics resides in.
     * @see "QuickTime::GraphicsImportGetDataFile"
     * @return The file that contains the graphics data.
     * @throws quicktime.QTException
     */
    public QTFile getDataFile() throws QTException {
        return QTFile.fromGraphicsImporter(this);
    }

    /**
     * Specifies the data handle that the graphics resides in.
     * @see "QuickTime::GraphicsImportSetDataHandle"
     * @param dataRef Specifies the handle to a QuickTime data reference.
     * @throws quicktime.std.StdQTException
     */
    public void setDataHandle(QTHandleRef dataRef) throws StdQTException {
        int i = GraphicsImportSetDataHandle(_ID(), QTObject.ID(dataRef));
        StdQTException.checkError(i);
        this.ref = dataRef;
    }

    /**
     * Determines the data reference that the graphics resides in.
     * @see "QuickTime::GraphicsImportGetDataHandle"
     * @return The handle to a QuickTime data reference.
     * @throws quicktime.QTException
     */
    public QTHandle getDataHandle() throws QTException {
        return QTHandle.fromGraphicsImporterData(this);
    }

    /**
     * Determines the image description information.
     * @see "QuickTime::GraphicsImportGetImageDescription"
     */
    public ImageDescription getImageDescription() throws StdQTException {
        return ImageDescription.fromGraphicsImporter(this);
    }

    /**
     * Determines the data offset.
     * @see "QuickTime::GraphicsImportGetDataOffsetAndSize"
     * @return The offset for the data.
     * @throws quicktime.std.StdQTException
     */
    public int getDataOffset() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetDataOffsetAndSize(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0];
    }

    /**
     * Determines the data size.
     * @see "QuickTime::GraphicsImportGetDataOffsetAndSize"
     * @return The size of the data.
     * @throws quicktime.std.StdQTException
     */
    public int getDataSize() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetDataOffsetAndSize(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    /**
     * Reads image data. The pointer must be at least offset + size big.
     * @see "QuickTime::GraphicsImportReadData"
     * @param data The EncodedImage that the data is read into.
     * @param dataOffset Specifies the offset within the pointer where the data is placed. This offset is in bytes.
     * @param dataSize Specifies the size of the data to be read.
     * @throws quicktime.std.StdQTException
     */
    public void readData(EncodedImage data, int dataOffset, int dataSize) throws StdQTException {
        int i = -50;
        if ((data instanceof ByteEncodedImage)) {
            i = GraphicsImportReadData(_ID(), ((ByteEncodedImage) data).getBytes(), dataOffset, dataSize);
        } else if ((data instanceof IntEncodedImage)) {
            i = GraphicsImportReadData(_ID(), ((IntEncodedImage) data).getInts(), dataOffset, dataSize);
        } else if ((data instanceof RawEncodedImage))
            i = GraphicsImportReadData(_ID(), QTObject.ID((RawEncodedImage) data), dataOffset, dataSize);
        StdQTException.checkError(i);
    }

    /**
     * Defines the clipping region for drawing.
     * @see "QuickTime::GraphicsImportSetClip"
     * @param clipRgn Specifies clipping region.
     * @throws quicktime.std.StdQTException
     */
    public void setClip(Region clipRgn) throws StdQTException {
        int i = GraphicsImportSetClip(_ID(), QTObject.ID(clipRgn));
        StdQTException.checkError(i);
    }

    /**
     * Determines the current clipping region.
     * @see "QuickTime::GraphicsImportGetClip"
     * @return The current clip region or null, if not possible
     * @throws quicktime.QTException
     */
    public Region getClip() throws QTException {
        return Region.fromGraphicsImporter(this);
    }

    /**
     * Sets the source rectangle to use for an image.
     * @see "QuickTime::GraphicsImportSetSourceRect"
     */
    public void setSourceRect(QDRect paramQDRect) throws StdQTException {
        int i = GraphicsImportSetSourceRect(_ID(), paramQDRect.getRect());
        StdQTException.checkError(i);
    }

    /**
     * Determines the current source rectangle for an image.
     * @see "QuickTime::GraphicsImportGetSourceRect"
     * @return The current source rectangle for an image.
     * @throws quicktime.std.StdQTException
     */
    public QDRect getSourceRect() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetSourceRect(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    /**
     * Determines the bounding rectangle of an image.
     * @see "QuickTime::GraphicsImportGetNaturalBounds"
     * @return The size of the bounding rectangle for an image.
     * @throws quicktime.std.StdQTException
     */
    public QDRect getNaturalBounds() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetNaturalBounds(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    /**
     * Draws an image.
     * @see "QuickTime::GraphicsImportDraw"
     */
    public void draw() throws StdQTException {
        int i = GraphicsImportDraw(_ID());
        StdQTException.checkError(i);
    }

    /**
     * Defines the matrix to use for drawing.
     * @see "QuickTime::GraphicsImportSetMatrix"
     * @param matrix Specifies the drawing matrix.
     * @throws quicktime.std.StdQTException
     */
    public void setMatrix(Matrix matrix) throws StdQTException {
        int i = GraphicsImportSetMatrix(_ID(), matrix.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * Determines the matrix to be used for drawing.
     * @see "QuickTime::GraphicsImportGetMatrix"
     * @return The matrix specification to use for drawing.
     * @throws quicktime.std.StdQTException
     */
    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = GraphicsImportGetMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    /**
     * Defines the rectangle in which to draw an image.
     * @see "QuickTime::GraphicsImportSetBoundsRect"
     * @param bounds Specifies the bounding rectangle.
     * @throws quicktime.std.StdQTException
     */
    public void setBoundsRect(QDRect bounds) throws StdQTException {
        int i = GraphicsImportSetBoundsRect(_ID(), bounds.getRect());
        StdQTException.checkError(i);
    }

    /**
     * Determines the bounding rectangle for drawing.
     * @see "QuickTime::GraphicsImportGetBoundsRect"
     * @return The bounding rectangle.
     * @throws quicktime.std.StdQTException
     */
    public QDRect getBoundsRect() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetBoundsRect(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    /**
     * Creates a QuickDraw picture file.
     * @see "QuickTime::GraphicsImportSaveAsPicture"
     * @param file The destination output file of the compressed image
     * @param scriptTag Specifies the depth at which the image is to be compressed.
     * @throws quicktime.QTException
     */
    public void saveAsPicture(QTFile file, int scriptTag) throws QTException {
        int i = GraphicsImportSaveAsPicture(_ID(), file.getFSSpec(false, 512), scriptTag);
        StdQTException.checkError(i);
    }

    /**
     * Creates a QuickDraw image file.
     * @see "QuickTime::GraphicsImportSaveAsQuickTimeImageFile"
     * @param scriptTag Specifies
     * @throws quicktime.QTException
     */
    public void saveAsQuickTimeImageFile(QTFile paramQTFile, int scriptTag) throws QTException {
        int i = GraphicsImportSaveAsQuickTimeImageFile(_ID(), paramQTFile.getFSSpec(false, 512), scriptTag);
        StdQTException.checkError(i);
    }

    /**
     * Sets the graphics mode and color for an image.
     * @see "QuickTime::GraphicsImportSetGraphicsMode"
     * @param graphicsMode Specifies the graphics transfer mode.
     * @throws quicktime.std.StdQTException
     */
    public void setGraphicsMode(GraphicsMode graphicsMode) throws StdQTException {
        int i = GraphicsImportSetGraphicsMode(_ID(), graphicsMode.getGraphicsMode(), graphicsMode.getColor().getRGBColor());
        StdQTException.checkError(i);
    }

    /**
     * Determines the graphics mode for an image.
     * @see "QuickTime::GraphicsImportGetGraphicsMode"
     * @return The graphics transfer mode.
     * @throws quicktime.std.StdQTException
     */
    public GraphicsMode getGraphicsMode() throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[6];
        int i = GraphicsImportGetGraphicsMode(_ID(), arrayOfInt, arrayOfByte);
        StdQTException.checkError(i);
        return new GraphicsMode(arrayOfInt[0], QDColor.fromArray(arrayOfByte, 6));
    }

    /**
     * Sets the image quality value.
     * @see "QuickTime::GraphicsImportSetQuality"
     * @param quality Specifies a quality CodecQ value
     * @throws quicktime.std.StdQTException
     */
    public void setQuality(int quality) throws StdQTException {
        int i = GraphicsImportSetQuality(_ID(), quality);
        StdQTException.checkError(i);
    }

    /**
     * Determines the image quality value.
     * @see "QuickTime::GraphicsImportGetQuality"
     * @return The image quality value.
     * @throws quicktime.std.StdQTException
     */
    public int getQuality() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsImportGetQuality(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Determines the aliased data reference.
     * @see "QuickTime::GraphicsImportGetAliasedDataReference"
     * @return The handle to the aliased data reference
     * @throws quicktime.QTException
     */
    public QTHandle getAliasedDataReference() throws QTException {
        return QTHandle.fromGraphicsImporterAlias(this);
    }

    /**
     * Determines the aliased data type.
     * @see "QuickTime::GraphicsImportGetAliasedDataReference"
     * @return The aliased data type
     * @throws quicktime.std.StdQTException
     */
    public int getAliasedDataType() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetAliasedDataReference(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    /**
     * Call this method to give the presentable the QDGraphics
     * object it should use to do its drawing.
     * @see "QuickTime::GraphicsImportSetGWorld"
     * @param port a QDGraphics object or null to use current port
     * @param gdh a GDevice object or null to use current device
     * @throws quicktime.std.StdQTException
     */
    public void setGWorld(QDGraphics port, GDevice gdh) throws StdQTException {
        int i = GraphicsImportSetGWorld(_ID(), QTObject.ID(port), QTObject.ID(gdh));
        StdQTException.checkError(i);
    }

    /**
     * Call this method to obtain the QDGraphics of the object.
     * @see "QuickTime::GraphicsImportGetGWorld"
     * @return The current destination graphics port
     * @throws quicktime.std.StdQTException
     */
    public QDGraphics getGWorld() throws StdQTException {
        return QDGraphics.fromGraphicsImporter(this);
    }

    /**
     * Determines if the current graphics importer data reference contains valid image data.
     * Not all graphics importers implement this method.
     * @see "QuickTime::GraphicsImportValidate"
     * @return True if a valid operation, false otherwise.
     * @throws quicktime.std.StdQTException
     */
    public boolean validate() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsImportValidate(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0] != 0;
    }

    /**
     * Determines if the GraphicsImporter will draw all the pixels that are contained in the
     * image data that it is presenting. Some image formats (for example GIF) allow a color to
     * be set as invisible or transparent.
     * @see "QuickTime::GraphicsImportDoesDrawAllPixels"
     * @return a value that indicates if this instance of the importer will draw all pixels
     * @throws quicktime.std.StdQTException
     */
    public int doesDrawAllPixels() throws StdQTException {
        short[] arrayOfShort = { 0 };
        int i = GraphicsImportDoesDrawAllPixels(_ID(), arrayOfShort);
        if (i == -2147450878)
            return 0;
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Creates a new Pict containing the image currently in use by this import component.
     * @see "QuickTime::GraphicsImportGetAsPicture"
     * @return a pict
     * @throws quicktime.QTException
     */
    public Pict getAsPicture() throws QTException {
        return Pict.fromGraphicsImporter(this);
    }

    /**
     * Gets metadata from an image file and adds it to a user data structure.
     * @see "QuickTime::GraphicsImportGetMetaData"
     * @return a UserData structure
     * @throws quicktime.QTException
     */
    public UserData getMetaData(GraphicsImporter paramGraphicsImporter) throws QTException {
        UserData localUserData = new UserData();
        int i = GraphicsImportGetMetaData(QTObject.ID(paramGraphicsImporter), QTObject.ID(localUserData));
        StdQTException.checkError(i);
        return localUserData;
    }

    /**
     * Returns a list of MIME types supported by the graphics import component.
     * @see "QuickTime::GraphicsImportGetMIMETypeList"
     * @return a QT atom container that contains a list of MIME types supported by the graphics import component.
     * @throws quicktime.std.StdQTException
     */
    public AtomContainer getMIMETypeList() throws StdQTException {
        return AtomContainer.fromGraphicsImporterMIME(this);
    }

    /**
     * Saves an image in a foreign file format.
     * @see "QuickTime::GraphicsImportExportImageFile"
     * @param fileType the file type
     * @param fileCreator the fie creator
     * @param theFile the output file to write to.
     * @throws quicktime.QTException
     */
    public void exportImageFile(int fileType, int fileCreator, QTFile theFile, int scriptTag) throws QTException {
        int i = GraphicsImportExportImageFile(_ID(), fileType, fileCreator, theFile.getFSSpec(false, 512), (short) scriptTag);
        StdQTException.checkError(i);
    }

    /**
     * Returns information about available export formats.
     * @see "QuickTime::GraphicsImportGetExportImageTypeList"
     * @return a QT atom container that contains a list of available export formats. supported by the graphics import component.
     * @throws quicktime.std.StdQTException
     */
    public AtomContainer getExportImageTypeList() throws StdQTException {
        return AtomContainer.fromGraphicsImporterExportImage(this);
    }

    /**
     * Returns the Export settings as an Atom Container.
     * @see "QuickTime::GraphicsImportGetExportSettingsAsAtomContainer"
     * @return a QT atom container that contains Export settings.
     * @throws quicktime.std.StdQTException
     */
    public AtomContainer getExportSettingsAsAtomContainer() throws StdQTException {
        return AtomContainer.fromGraphicsImporterExportSettings(this);
    }

    /**
     * Export settings from a QuickTime atom container. These settings configure how images will be saved by the
     * GraphicsImportExportImageFile function.
     * @see "QuickTime::GraphicsImportSetExportSettingsFromAtomContainer"
     * @param qtAtomContainer the atomcontainer with the export settings.
     * @throws quicktime.std.StdQTException
     */
    public void setExportSettingsFromAtomContainer(AtomContainer qtAtomContainer) throws StdQTException {
        int[] arrayOfInt = { 0 };
        arrayOfInt[0] = QTObject.ID(qtAtomContainer);
        int i = GraphicsImportSetExportSettingsFromAtomContainer(_ID(), arrayOfInt);
        StdQTException.checkError(i);
    }

    /**
     * Presents a dialog box letting the user save an image in a foreign file format.
     * @see "QuickTime::GraphicsImportDoExportImageFileDialog"
     * @param inDefaultSpec suggests the default name for the file. If you do not want to suggest a default name, pass null.
     * @param prompt A prompt that appears in the standard put dialog box; it may be nil, in which case a default string is used.
     * @throws quicktime.QTException
     */
    public GraphicsImporterInfo doExportImageFileDialog(QTFile inDefaultSpec, String prompt) throws QTException {
        return QTFile.fromGraphicsImporter(this, inDefaultSpec, prompt);
    }

    /**
     * Specifies the data reference starting offset and data size limit.
     * @see "QuickTime::GraphicsImportSetDataReferenceOffsetAndLimit"
     * @param offset The byte offset of the image data from the beginning of the data reference.
     * @param limit the offset of the byte following the last byte of the image data.
     * @throws quicktime.std.StdQTException
     */
    public void setDataReferenceOffsetAndLimit(int offset, int limit) throws StdQTException {
        int i = GraphicsImportSetDataReferenceOffsetAndLimit(_ID(), offset, limit);
        StdQTException.checkError(i);
    }

    /**
     * Returns the data reference starting offset and data size limit.
     * @see "QuickTime::GraphicsImportGetDataReferenceOffsetAndLimit"
     * @return an array of length 2 with 1st element as the offset and 2nd element as the limit.
     * @throws quicktime.std.StdQTException
     */
    public int[] getDataReferenceOffsetAndLimit() throws StdQTException {
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
     * @see "QuickTime::GraphicsImportGetImageCount"
     * @return the number of images.
     * @throws quicktime.std.StdQTException
     */
    public int getImageCount() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsImportGetImageCount(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * The default image index is 1. Image indexes are one-based; zero is considered a special
     * index by some importers, and treated the same as one by others.
     * @see "QuickTime::GraphicsImportSetImageIndex"
     * @param imageIndex Specifies the image index.
     * @throws quicktime.std.StdQTException
     */
    public void setImageIndex(int imageIndex) throws StdQTException {
        int i = GraphicsImportSetImageIndex(_ID(), imageIndex);
        StdQTException.checkError(i);
    }

    /**
     * Returns the current image index.
     * @see "QuickTime::GraphicsImportGetImageIndex"
     * @return the image index.
     * @throws quicktime.std.StdQTException
     */
    public int getImageIndex() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsImportGetImageIndex(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Returns the default matrix.
     * @see "QuickTime::GraphicsImportGetDefaultMatrix"
     * @return the default matrix
     * @throws quicktime.std.StdQTException
     */
    public Matrix getDefaultMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = GraphicsImportGetDefaultMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    /**
     * Returns the default matrix.
     * @see "QuickTime::GraphicsImportGetDefaultClip"
     * @return the default matrix
     * @throws quicktime.QTException
     */
    public Region getDefaultClip() throws QTException {
        return Region.fromGraphicsImporterDefault(this);
    }

    /**
     * Returns the default graphics mode, if one is stored in the image. If this method returns
     * badComponentSelector , you should assume a mode of ditherCopy .
     * @see "QuickTime::GraphicsImportGetDefaultGraphicsMode"
     * @return the default mode
     * @throws quicktime.QTException
     */
    public GraphicsMode getDefaultGraphicsMode() throws QTException {
        int[] arrayOfInt = { 0 };
        QDColor localQDColor = new QDColor();
        int i = GraphicsImportGetDefaultGraphicsMode(_ID(), arrayOfInt, localQDColor.getRGBColor());
        StdQTException.checkError(i);
        return new GraphicsMode(arrayOfInt[0], localQDColor);
    }

    /**
     * Returns the default source rect, if one is stored in the image. If this function returns
     * badComponentSelector , you should assume a source rect equal to the natural bounds.
     * @see "QuickTime::GraphicsImportGetDefaultSourceRect"
     * @return the default Rect
     * @throws quicktime.QTException
     */
    public QDRect getDefaultSourceRect() throws QTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetDefaultSourceRect(_ID(), localQDRect.getBytes());
        return localQDRect;
    }

    /**
     * Sets the destination rectangle. You can use this function to define the rectangle into which the extracted source
     * rectangle should be drawn.
     * @see "QuickTime::GraphicsImportSetDestRect"
     */
    public void setDestRect(QDRect paramQDRect) throws QTException {
        int i = GraphicsImportSetDestRect(_ID(), paramQDRect.getBytes());
    }

    /**
     * Returns the destination rectangle.
     * @see "QuickTime::GraphicsImportGetDestRect"
     * @return the destination rectangle.
     * @throws quicktime.QTException
     */
    public QDRect getDestRect() throws QTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetDestRect(_ID(), localQDRect.getBytes());
        return localQDRect;
    }

    /**
     * Sets the current scriptTag.
     * @see "QuickTime::GraphicsImportSetFlags"
     * @param scriptTag the flag
     * @throws quicktime.QTException
     */
    public void setFlags(int scriptTag) throws QTException {
        int i = GraphicsImportSetFlags(_ID(), scriptTag);
    }

    /**
     * Returns the current scriptTag..
     * @see "QuickTime::GraphicsImportGetFlags"
     * @return Returns the current scriptTag.
     * @throws quicktime.QTException
     */
    public int getFlags() throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsImportGetFlags(_ID(), arrayOfInt);
        return arrayOfInt[0];
    }

    private static int GraphicsExportGetInputGraphicsImporter(int scriptTag, int[] paramArrayOfInt) {
        return GraphicsExportManager.GraphicsExportGetInputGraphicsImporter(scriptTag, paramArrayOfInt);
    }

    private static short GetGraphicsImporterForFile(byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return GraphicsImportManager.GetGraphicsImporterForFile(paramArrayOfByte, paramArrayOfInt);
    }

    private static short GetGraphicsImporterForFileWithFlags(byte[] paramArrayOfByte, int[] paramArrayOfInt, int scriptTag) {
        return GraphicsImportManager.GetGraphicsImporterForFileWithFlags(paramArrayOfByte, paramArrayOfInt, scriptTag);
    }

    private static short GetGraphicsImporterForDataRef(int scriptTag1, int limit, int[] paramArrayOfInt) {
        return GraphicsImportManager.GetGraphicsImporterForDataRef(scriptTag1, limit, paramArrayOfInt);
    }

    private static short GetGraphicsImporterForDataRefWithFlags(int scriptTag1, int limit, int[] paramArrayOfInt, int paramInt3) {
        return GraphicsImportManager.GetGraphicsImporterForDataRefWithFlags(scriptTag1, limit, paramArrayOfInt, paramInt3);
    }

    private static int GraphicsImportSetDataReference(int scriptTag1, int limit, int paramInt3) {
        return GraphicsImportManager.GraphicsImportSetDataReference(scriptTag1, limit, paramInt3);
    }

    private static int GraphicsImportGetDataReference(int scriptTag, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return GraphicsImportManager.GraphicsImportGetDataReference(scriptTag, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int GraphicsImportSetDataFile(int scriptTag, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportSetDataFile(scriptTag, paramArrayOfByte);
    }

    private static int GraphicsImportSetDataHandle(int scriptTag1, int limit) {
        return GraphicsImportManager.GraphicsImportSetDataHandle(scriptTag1, limit);
    }

    private static int GraphicsImportGetDataOffsetAndSize(int scriptTag, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return GraphicsImportManager.GraphicsImportGetDataOffsetAndSize(scriptTag, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int GraphicsImportReadData(int scriptTag1, int limit, int paramInt3, int paramInt4) {
        return GraphicsImportManager.GraphicsImportReadData(scriptTag1, limit, paramInt3, paramInt4);
    }

    private static int GraphicsImportReadData(int scriptTag1, byte[] paramArrayOfByte, int limit, int paramInt3) {
        return GraphicsImportManager.GraphicsImportReadData(scriptTag1, paramArrayOfByte, limit, paramInt3);
    }

    private static int GraphicsImportReadData(int scriptTag1, int[] paramArrayOfInt, int limit, int paramInt3) {
        return GraphicsImportManager.GraphicsImportReadData(scriptTag1, paramArrayOfInt, limit, paramInt3);
    }

    private static int GraphicsImportSetClip(int scriptTag1, int limit) {
        return GraphicsImportManager.GraphicsImportSetClip(scriptTag1, limit);
    }

    private static int GraphicsImportSetSourceRect(int scriptTag, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportSetSourceRect(scriptTag, paramArrayOfByte);
    }

    private static int GraphicsImportGetSourceRect(int scriptTag, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportGetSourceRect(scriptTag, paramArrayOfByte);
    }

    private static int GraphicsImportGetNaturalBounds(int scriptTag, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportGetNaturalBounds(scriptTag, paramArrayOfByte);
    }

    private static int GraphicsImportDraw(int scriptTag) {
        return GraphicsImportManager.GraphicsImportDraw(scriptTag);
    }

    private static int GraphicsImportSetMatrix(int scriptTag, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportSetMatrix(scriptTag, paramArrayOfByte);
    }

    private static int GraphicsImportGetMatrix(int scriptTag, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportGetMatrix(scriptTag, paramArrayOfByte);
    }

    private static int GraphicsImportSetBoundsRect(int scriptTag, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportSetBoundsRect(scriptTag, paramArrayOfByte);
    }

    private static int GraphicsImportGetBoundsRect(int scriptTag, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportGetBoundsRect(scriptTag, paramArrayOfByte);
    }

    private static int GraphicsImportSaveAsPicture(int scriptTag1, byte[] paramArrayOfByte, int limit) {
        return GraphicsImportManager.GraphicsImportSaveAsPicture(scriptTag1, paramArrayOfByte, limit);
    }

    private static int GraphicsImportSaveAsQuickTimeImageFile(int scriptTag1, byte[] paramArrayOfByte, int limit) {
        return GraphicsImportManager.GraphicsImportSaveAsQuickTimeImageFile(scriptTag1, paramArrayOfByte, limit);
    }

    private static int GraphicsImportSetGraphicsMode(int scriptTag1, int limit, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportSetGraphicsMode(scriptTag1, limit, paramArrayOfByte);
    }

    private static int GraphicsImportGetGraphicsMode(int scriptTag, int[] paramArrayOfInt, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportGetGraphicsMode(scriptTag, paramArrayOfInt, paramArrayOfByte);
    }

    private static int GraphicsImportSetQuality(int scriptTag1, int limit) {
        return GraphicsImportManager.GraphicsImportSetQuality(scriptTag1, limit);
    }

    private static int GraphicsImportGetQuality(int scriptTag, int[] paramArrayOfInt) {
        return GraphicsImportManager.GraphicsImportGetQuality(scriptTag, paramArrayOfInt);
    }

    private static int GraphicsImportGetAliasedDataReference(int scriptTag, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return GraphicsImportManager.GraphicsImportGetAliasedDataReference(scriptTag, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int GraphicsImportSetGWorld(int scriptTag1, int limit, int paramInt3) {
        return GraphicsImportManager.GraphicsImportSetGWorld(scriptTag1, limit, paramInt3);
    }

    private static int GraphicsImportValidate(int scriptTag, int[] paramArrayOfInt) {
        return GraphicsImportManager.GraphicsImportValidate(scriptTag, paramArrayOfInt);
    }

    private static int GraphicsImportDoesDrawAllPixels(int scriptTag, short[] paramArrayOfShort) {
        return GraphicsImportManager.GraphicsImportDoesDrawAllPixels(scriptTag, paramArrayOfShort);
    }

    private static int GraphicsImportGetMetaData(int scriptTag1, int limit) {
        return GraphicsImportManager.GraphicsImportGetMetaData(scriptTag1, limit);
    }

    private static short GraphicsImportExportImageFile(int scriptTag1, int fileType, int fileCreator, byte[] fsspec, short scriptTag) {
        return GraphicsImportManager.GraphicsImportExportImageFile(scriptTag1, fileType, fileCreator, fsspec, scriptTag);
    }

    private static int GraphicsImportSetExportSettingsFromAtomContainer(int scriptTag, int[] paramArrayOfInt) {
        return GraphicsImportManager.GraphicsImportSetExportSettingsFromAtomContainer(scriptTag, paramArrayOfInt);
    }

    private static void SetGWorld(int scriptTag1, int limit) {
        QuickDrawManager.SetGWorld(scriptTag1, limit);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int GraphicsImportSetDataReferenceOffsetAndLimit(int scriptTag1, int limit, int paramInt3) {
        return GraphicsImportManager.GraphicsImportSetDataReferenceOffsetAndLimit(scriptTag1, limit, paramInt3);
    }

    private static int GraphicsImportGetDataReferenceOffsetAndLimit(int scriptTag, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return GraphicsImportManager.GraphicsImportGetDataReferenceOffsetAndLimit(scriptTag, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int GraphicsImportGetImageCount(int scriptTag, int[] paramArrayOfInt) {
        return GraphicsImportManager.GraphicsImportGetImageCount(scriptTag, paramArrayOfInt);
    }

    private static int GraphicsImportSetImageIndex(int scriptTag1, int limit) {
        return GraphicsImportManager.GraphicsImportSetImageIndex(scriptTag1, limit);
    }

    private static int GraphicsImportGetImageIndex(int scriptTag, int[] paramArrayOfInt) {
        return GraphicsImportManager.GraphicsImportGetImageIndex(scriptTag, paramArrayOfInt);
    }

    private static int GraphicsImportGetDefaultMatrix(int scriptTag, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportGetDefaultMatrix(scriptTag, paramArrayOfByte);
    }

    private static int GraphicsImportGetDefaultGraphicsMode(int scriptTag, int[] paramArrayOfInt, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportGetDefaultGraphicsMode(scriptTag, paramArrayOfInt, paramArrayOfByte);
    }

    private static int GraphicsImportGetDefaultSourceRect(int scriptTag, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportGetDefaultSourceRect(scriptTag, paramArrayOfByte);
    }

    private static int GraphicsImportSetDestRect(int scriptTag, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportSetDestRect(scriptTag, paramArrayOfByte);
    }

    private static int GraphicsImportGetDestRect(int scriptTag, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportGetDestRect(scriptTag, paramArrayOfByte);
    }

    private static int GraphicsImportSetFlags(int scriptTag1, int limit) {
        return GraphicsImportManager.GraphicsImportSetFlags(scriptTag1, limit);
    }

    private static int GraphicsImportGetFlags(int scriptTag, int[] paramArrayOfInt) {
        return GraphicsImportManager.GraphicsImportGetFlags(scriptTag, paramArrayOfInt);
    }
}
