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
    public static GraphicsImporter fromGraphicsExporter(GraphicsExporter graphicsExporter) throws QTException {
        return new GraphicsImporter(graphicsExporter);
    }

    private GraphicsImporter(GraphicsExporter graphicsExporter) throws QTException {
        super(allocate(graphicsExporter), null);
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

    private static int allocate(QTFile qtFile) throws QTException {
        int[] gi = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForFile(qtFile.getFSSpec(true, 256), gi);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return gi[0];
    }

    private static int allocate(GraphicsExporter graphicsExporter) throws QTException {
        int[] gi = { 0 };
        int i = GraphicsExportGetInputGraphicsImporter(QTObject.ID(graphicsExporter), gi);
        StdQTException.checkError(i);
        return gi[0];
    }

    private static int allocate(QTFile qtFile, int scriptTag) throws QTException {
        int[] gi = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForFileWithFlags(qtFile.getFSSpec(true, 256), gi, scriptTag);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return gi[0];
    }

    private static int allocate(DataRef dataRef) throws QTException {
        int[] gi = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForDataRef(QTObject.ID(dataRef), dataRef.getType(), gi);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        if (gi[0] == 0)
            throw new StdQTException(63524);
        return gi[0];
    }

    private static int allocate(DataRef dataRef, int scriptTag) throws QTException {
        int[] gi = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForDataRefWithFlags(QTObject.ID(dataRef), dataRef.getType(), gi, scriptTag);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        if (gi[0] == 0)
            throw new StdQTException(63524);
        return gi[0];
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
        int[] dataRef = new int[1];
        int[] dataRefType = new int[1];
        int i = GraphicsImportGetDataReference(_ID(), dataRef, dataRefType);
        StdQTException.checkError(i);
        return dataRefType[0];
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
        int[] offset = new int[1];
        int[] size = new int[1];
        int i = GraphicsImportGetDataOffsetAndSize(_ID(), offset, size);
        StdQTException.checkError(i);
        return offset[0];
    }

    /**
     * Determines the data size.
     * @see "QuickTime::GraphicsImportGetDataOffsetAndSize"
     * @return The size of the data.
     * @throws quicktime.std.StdQTException
     */
    public int getDataSize() throws StdQTException {
        int[] offset = new int[1];
        int[] size = new int[1];
        int i = GraphicsImportGetDataOffsetAndSize(_ID(), offset, size);
        StdQTException.checkError(i);
        return size[0];
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
    public void setSourceRect(QDRect sourceRect) throws StdQTException {
        int i = GraphicsImportSetSourceRect(_ID(), sourceRect.getRect());
        StdQTException.checkError(i);
    }

    /**
     * Determines the current source rectangle for an image.
     * @see "QuickTime::GraphicsImportGetSourceRect"
     * @return The current source rectangle for an image.
     * @throws quicktime.std.StdQTException
     */
    public QDRect getSourceRect() throws StdQTException {
        QDRect sourceRect = new QDRect();
        int i = GraphicsImportGetSourceRect(_ID(), sourceRect.getRect());
        StdQTException.checkError(i);
        return sourceRect;
    }

    /**
     * Determines the bounding rectangle of an image.
     * @see "QuickTime::GraphicsImportGetNaturalBounds"
     * @return The size of the bounding rectangle for an image.
     * @throws quicktime.std.StdQTException
     */
    public QDRect getNaturalBounds() throws StdQTException {
        QDRect naturalBounds = new QDRect();
        int i = GraphicsImportGetNaturalBounds(_ID(), naturalBounds.getRect());
        StdQTException.checkError(i);
        return naturalBounds;
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
        Matrix matrix = new Matrix();
        int i = GraphicsImportGetMatrix(_ID(), matrix.getBytes());
        StdQTException.checkError(i);
        return matrix;
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
        QDRect boundsRect = new QDRect();
        int i = GraphicsImportGetBoundsRect(_ID(), boundsRect.getRect());
        StdQTException.checkError(i);
        return boundsRect;
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
    public void saveAsQuickTimeImageFile(QTFile qtFile, int scriptTag) throws QTException {
        int i = GraphicsImportSaveAsQuickTimeImageFile(_ID(), qtFile.getFSSpec(false, 512), scriptTag);
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
        int[] mode = new int[1];
        byte[] opColor = new byte[6];
        int i = GraphicsImportGetGraphicsMode(_ID(), mode, opColor);
        StdQTException.checkError(i);
        return new GraphicsMode(mode[0], QDColor.fromArray(opColor, 6));
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
        int[] quality = new int[1];
        int i = GraphicsImportGetQuality(_ID(), quality);
        StdQTException.checkError(i);
        return quality[0];
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
        int[] dataRef = new int[1];
        int[] dataRefType = new int[1];
        int i = GraphicsImportGetAliasedDataReference(_ID(), dataRef, dataRefType);
        StdQTException.checkError(i);
        return dataRefType[0];
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
        int[] valid = new int[1];
        int i = GraphicsImportValidate(_ID(), valid);
        StdQTException.checkError(i);
        return valid[0] != 0;
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
        short[] drawsAllPixels = { 0 };
        int i = GraphicsImportDoesDrawAllPixels(_ID(), drawsAllPixels);
        if (i == -2147450878)
            return 0;
        StdQTException.checkError(i);
        return drawsAllPixels[0];
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
    public UserData getMetaData(GraphicsImporter graphicsImporter) throws QTException {
        UserData limit = new UserData();
        int i = GraphicsImportGetMetaData(QTObject.ID(graphicsImporter), QTObject.ID(limit));
        StdQTException.checkError(i);
        return limit;
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
        int[] container = { 0 };
        container[0] = QTObject.ID(qtAtomContainer);
        int i = GraphicsImportSetExportSettingsFromAtomContainer(_ID(), container);
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
        int[] offset = { 0 };
        int[] limit = { 0 };
        int[] dataReferenceOffsetAndLimit = new int[2];
        int i = GraphicsImportGetDataReferenceOffsetAndLimit(_ID(), offset, limit);
        StdQTException.checkError(i);
        dataReferenceOffsetAndLimit[0] = offset[0];
        dataReferenceOffsetAndLimit[1] = limit[0];
        return dataReferenceOffsetAndLimit;
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
        int[] count = { 0 };
        int i = GraphicsImportGetImageCount(_ID(), count);
        StdQTException.checkError(i);
        return count[0];
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
        int[] index = { 0 };
        int i = GraphicsImportGetImageIndex(_ID(), index);
        StdQTException.checkError(i);
        return index[0];
    }

    /**
     * Returns the default matrix.
     * @see "QuickTime::GraphicsImportGetDefaultMatrix"
     * @return the default matrix
     * @throws quicktime.std.StdQTException
     */
    public Matrix getDefaultMatrix() throws StdQTException {
        Matrix defaultMatrix = new Matrix();
        int i = GraphicsImportGetDefaultMatrix(_ID(), defaultMatrix.getBytes());
        StdQTException.checkError(i);
        return defaultMatrix;
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
        int[] mode = { 0 };
        QDColor opColor = new QDColor();
        int i = GraphicsImportGetDefaultGraphicsMode(_ID(), mode, opColor.getRGBColor());
        StdQTException.checkError(i);
        return new GraphicsMode(mode[0], opColor);
    }

    /**
     * Returns the default source rect, if one is stored in the image. If this function returns
     * badComponentSelector , you should assume a source rect equal to the natural bounds.
     * @see "QuickTime::GraphicsImportGetDefaultSourceRect"
     * @return the default Rect
     * @throws quicktime.QTException
     */
    public QDRect getDefaultSourceRect() throws QTException {
        QDRect defaultSourceRect = new QDRect();
        int i = GraphicsImportGetDefaultSourceRect(_ID(), defaultSourceRect.getBytes());
        return defaultSourceRect;
    }

    /**
     * Sets the destination rectangle. You can use this function to define the rectangle into which the extracted source
     * rectangle should be drawn.
     * @see "QuickTime::GraphicsImportSetDestRect"
     */
    public void setDestRect(QDRect destRect) throws QTException {
        int i = GraphicsImportSetDestRect(_ID(), destRect.getBytes());
    }

    /**
     * Returns the destination rectangle.
     * @see "QuickTime::GraphicsImportGetDestRect"
     * @return the destination rectangle.
     * @throws quicktime.QTException
     */
    public QDRect getDestRect() throws QTException {
        QDRect destRect = new QDRect();
        int i = GraphicsImportGetDestRect(_ID(), destRect.getBytes());
        return destRect;
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
        int[] flags = { 0 };
        int i = GraphicsImportGetFlags(_ID(), flags);
        return flags[0];
    }

    private static int GraphicsExportGetInputGraphicsImporter(int scriptTag, int[] gi) {
        return GraphicsExportManager.GraphicsExportGetInputGraphicsImporter(scriptTag, gi);
    }

    private static short GetGraphicsImporterForFile(byte[] fsspec, int[] gi) {
        return GraphicsImportManager.GetGraphicsImporterForFile(fsspec, gi);
    }

    private static short GetGraphicsImporterForFileWithFlags(byte[] fsspec, int[] gi, int scriptTag) {
        return GraphicsImportManager.GetGraphicsImporterForFileWithFlags(fsspec, gi, scriptTag);
    }

    private static short GetGraphicsImporterForDataRef(int scriptTag1, int limit, int[] gi) {
        return GraphicsImportManager.GetGraphicsImporterForDataRef(scriptTag1, limit, gi);
    }

    private static short GetGraphicsImporterForDataRefWithFlags(int scriptTag1, int limit, int[] gi, int flags) {
        return GraphicsImportManager.GetGraphicsImporterForDataRefWithFlags(scriptTag1, limit, gi, flags);
    }

    private static int GraphicsImportSetDataReference(int scriptTag1, int limit, int dataRefType) {
        return GraphicsImportManager.GraphicsImportSetDataReference(scriptTag1, limit, dataRefType);
    }

    private static int GraphicsImportGetDataReference(int scriptTag, int[] dataRef, int[] dataRefType) {
        return GraphicsImportManager.GraphicsImportGetDataReference(scriptTag, dataRef, dataRefType);
    }

    private static int GraphicsImportSetDataFile(int scriptTag, byte[] fsspec) {
        return GraphicsImportManager.GraphicsImportSetDataFile(scriptTag, fsspec);
    }

    private static int GraphicsImportSetDataHandle(int scriptTag1, int limit) {
        return GraphicsImportManager.GraphicsImportSetDataHandle(scriptTag1, limit);
    }

    private static int GraphicsImportGetDataOffsetAndSize(int scriptTag, int[] offset, int[] size) {
        return GraphicsImportManager.GraphicsImportGetDataOffsetAndSize(scriptTag, offset, size);
    }

    private static int GraphicsImportReadData(int scriptTag1, int limit, int offset, int size) {
        return GraphicsImportManager.GraphicsImportReadData(scriptTag1, limit, offset, size);
    }

    private static int GraphicsImportReadData(int scriptTag1, byte[] dst, int limit, int size) {
        return GraphicsImportManager.GraphicsImportReadData(scriptTag1, dst, limit, size);
    }

    private static int GraphicsImportReadData(int scriptTag1, int[] dst, int limit, int size) {
        return GraphicsImportManager.GraphicsImportReadData(scriptTag1, dst, limit, size);
    }

    private static int GraphicsImportSetClip(int scriptTag1, int limit) {
        return GraphicsImportManager.GraphicsImportSetClip(scriptTag1, limit);
    }

    private static int GraphicsImportSetSourceRect(int scriptTag, byte[] rect) {
        return GraphicsImportManager.GraphicsImportSetSourceRect(scriptTag, rect);
    }

    private static int GraphicsImportGetSourceRect(int scriptTag, byte[] rect) {
        return GraphicsImportManager.GraphicsImportGetSourceRect(scriptTag, rect);
    }

    private static int GraphicsImportGetNaturalBounds(int scriptTag, byte[] rect) {
        return GraphicsImportManager.GraphicsImportGetNaturalBounds(scriptTag, rect);
    }

    private static int GraphicsImportDraw(int scriptTag) {
        return GraphicsImportManager.GraphicsImportDraw(scriptTag);
    }

    private static int GraphicsImportSetMatrix(int scriptTag, byte[] matrix) {
        return GraphicsImportManager.GraphicsImportSetMatrix(scriptTag, matrix);
    }

    private static int GraphicsImportGetMatrix(int scriptTag, byte[] matrix) {
        return GraphicsImportManager.GraphicsImportGetMatrix(scriptTag, matrix);
    }

    private static int GraphicsImportSetBoundsRect(int scriptTag, byte[] rect) {
        return GraphicsImportManager.GraphicsImportSetBoundsRect(scriptTag, rect);
    }

    private static int GraphicsImportGetBoundsRect(int scriptTag, byte[] rect) {
        return GraphicsImportManager.GraphicsImportGetBoundsRect(scriptTag, rect);
    }

    private static int GraphicsImportSaveAsPicture(int scriptTag1, byte[] fsspec, int limit) {
        return GraphicsImportManager.GraphicsImportSaveAsPicture(scriptTag1, fsspec, limit);
    }

    private static int GraphicsImportSaveAsQuickTimeImageFile(int scriptTag1, byte[] fsspec, int limit) {
        return GraphicsImportManager.GraphicsImportSaveAsQuickTimeImageFile(scriptTag1, fsspec, limit);
    }

    private static int GraphicsImportSetGraphicsMode(int scriptTag1, int limit, byte[] opColor) {
        return GraphicsImportManager.GraphicsImportSetGraphicsMode(scriptTag1, limit, opColor);
    }

    private static int GraphicsImportGetGraphicsMode(int scriptTag, int[] mode, byte[] opColor) {
        return GraphicsImportManager.GraphicsImportGetGraphicsMode(scriptTag, mode, opColor);
    }

    private static int GraphicsImportSetQuality(int scriptTag1, int limit) {
        return GraphicsImportManager.GraphicsImportSetQuality(scriptTag1, limit);
    }

    private static int GraphicsImportGetQuality(int scriptTag, int[] quality) {
        return GraphicsImportManager.GraphicsImportGetQuality(scriptTag, quality);
    }

    private static int GraphicsImportGetAliasedDataReference(int scriptTag, int[] dataRef, int[] dataRefType) {
        return GraphicsImportManager.GraphicsImportGetAliasedDataReference(scriptTag, dataRef, dataRefType);
    }

    private static int GraphicsImportSetGWorld(int scriptTag1, int limit, int gdh) {
        return GraphicsImportManager.GraphicsImportSetGWorld(scriptTag1, limit, gdh);
    }

    private static int GraphicsImportValidate(int scriptTag, int[] valid) {
        return GraphicsImportManager.GraphicsImportValidate(scriptTag, valid);
    }

    private static int GraphicsImportDoesDrawAllPixels(int scriptTag, short[] drawsAllPixels) {
        return GraphicsImportManager.GraphicsImportDoesDrawAllPixels(scriptTag, drawsAllPixels);
    }

    private static int GraphicsImportGetMetaData(int scriptTag1, int limit) {
        return GraphicsImportManager.GraphicsImportGetMetaData(scriptTag1, limit);
    }

    private static short GraphicsImportExportImageFile(int scriptTag1, int fileType, int fileCreator, byte[] fsspec, short scriptTag) {
        return GraphicsImportManager.GraphicsImportExportImageFile(scriptTag1, fileType, fileCreator, fsspec, scriptTag);
    }

    private static int GraphicsImportSetExportSettingsFromAtomContainer(int scriptTag, int[] container) {
        return GraphicsImportManager.GraphicsImportSetExportSettingsFromAtomContainer(scriptTag, container);
    }

    private static void SetGWorld(int scriptTag1, int limit) {
        QuickDrawManager.SetGWorld(scriptTag1, limit);
    }

    private static void GetGWorld(int[] port, int[] gdh) {
        QuickDrawManager.GetGWorld(port, gdh);
    }

    private static int GraphicsImportSetDataReferenceOffsetAndLimit(int gi, int offset, int limit) {
        return GraphicsImportManager.GraphicsImportSetDataReferenceOffsetAndLimit(gi, offset, limit);
    }

    private static int GraphicsImportGetDataReferenceOffsetAndLimit(int scriptTag, int[] offset, int[] limit) {
        return GraphicsImportManager.GraphicsImportGetDataReferenceOffsetAndLimit(scriptTag, offset, limit);
    }

    private static int GraphicsImportGetImageCount(int scriptTag, int[] count) {
        return GraphicsImportManager.GraphicsImportGetImageCount(scriptTag, count);
    }

    private static int GraphicsImportSetImageIndex(int scriptTag1, int limit) {
        return GraphicsImportManager.GraphicsImportSetImageIndex(scriptTag1, limit);
    }

    private static int GraphicsImportGetImageIndex(int scriptTag, int[] index) {
        return GraphicsImportManager.GraphicsImportGetImageIndex(scriptTag, index);
    }

    private static int GraphicsImportGetDefaultMatrix(int scriptTag, byte[] matrix) {
        return GraphicsImportManager.GraphicsImportGetDefaultMatrix(scriptTag, matrix);
    }

    private static int GraphicsImportGetDefaultGraphicsMode(int scriptTag, int[] mode, byte[] opColor) {
        return GraphicsImportManager.GraphicsImportGetDefaultGraphicsMode(scriptTag, mode, opColor);
    }

    private static int GraphicsImportGetDefaultSourceRect(int scriptTag, byte[] rect) {
        return GraphicsImportManager.GraphicsImportGetDefaultSourceRect(scriptTag, rect);
    }

    private static int GraphicsImportSetDestRect(int scriptTag, byte[] rect) {
        return GraphicsImportManager.GraphicsImportSetDestRect(scriptTag, rect);
    }

    private static int GraphicsImportGetDestRect(int scriptTag, byte[] rect) {
        return GraphicsImportManager.GraphicsImportGetDestRect(scriptTag, rect);
    }

    private static int GraphicsImportSetFlags(int scriptTag1, int limit) {
        return GraphicsImportManager.GraphicsImportSetFlags(scriptTag1, limit);
    }

    private static int GraphicsImportGetFlags(int scriptTag, int[] flags) {
        return GraphicsImportManager.GraphicsImportGetFlags(scriptTag, flags);
    }
}
