package quicktime.std.image;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.Errors;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.Pict;
import quicktime.qd.PixMap;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.UserData;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTPointer;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;

/**
 * Graphics exporter components provide a standard interface for exporting graphics to image files
 * in a variety of formats. QuickTime selects a graphic exporter component based on the desired
 * output format, such as TIFF or PNG. Different image formats support a wide range of configurable
 * features, such as depth, resolution and compression quality.
 * This class implements methods for a corresponding QuickTime ComponentInstance structure.
 * refer to GraphicsExportComponent in QuickTime
 * The base exporter communicates with data handler components to negotiate access to image file data.
 * If necessary, it calls the Image Compression Manager to perform compression operations.
 * There are three modes that format-specific exporters can operate in:
 *
 * -  Transcode. Data is transferred from one image file to another without decompressing and
 * recompressing. (The data may be modified along the way, if appropriate.)
 * -  Using a Compressor. The graphics exporter provides an atom container identifying which
 * compressor to use, and any settings to be passed to that compressor.
 * -  Standalone Export. The graphics exporter component does all the work itself, without using an image
 * compressor.
 */
public final class GraphicsExporter extends Component implements QuickTimeLib, Errors {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(GraphicsExporter.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Opens the specified graphics export component,'grex', with the given subType.
     * QuickTime::OpenADefaultComponent
     * @param subType The graphics export component sub type
     */
    public GraphicsExporter(int paramInt) throws QTException {
        super(1735550328, paramInt);
    }

    /**
     * You can use the GraphicsExportDoExport function to perform an export operation. Before calling
     * GraphicsExportDoExport , you must specify an input image (using a GraphicsExportSetInput
     * call) and a destination for the output image file (using a GraphicsExportSetOutput call).
     * QuickTime::GraphicsExportDoExport
     * @return the actual size of the image exported
     * @return {@link quicktime.std.StdQTException}
     */
    public int doExport() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportDoExport(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * If both CanTranscode and CanUseCompressor reply false , the base graphics exporter makes this call
     * of the format-specific exporter to perform the call.
     * QuickTime::GraphicsExportDoStandaloneExport
     */
    public void doStandaloneExport() throws QTException {
        int i = GraphicsExportDoStandaloneExport(_ID());
        StdQTException.checkError(i);
    }

    /**
     * Sets the specified DataRef as the source for input images to graphics export component.
     * QuickTime::GraphicsExportSetInputDataReference
     * @param dataRef The DataRef for input images to export component sub type
     * @param desc the ImageDescription handle
     * @param {@link quicktime.QTException}
     */
    public void setInputDataReference(DataRef paramDataRef, ImageDescription paramImageDescription) throws QTException {
        int i = paramDataRef.getType();
        int j = GraphicsExportSetInputDataReference(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), QTObject.ID(paramImageDescription));
        StdQTException.checkError(j);
    }

    /**
     * Gets the DataRef of the source for input images to graphics export component.
     * QuickTime::GraphicsExportGetInputDataReference
     * @return a DataRef object specifying the source of input images to the Graphics exporter.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.util.QTHandle#fromGraphicsExporterInput(quicktime.std.image.GraphicsExporter)}
     */
    public DataRef getInputDataReference() throws QTException {
        return DataRef.fromGraphicsExporterInputDataRef(this);
    }

    /**
     * Sets the specified QTFile as the source for input images to graphics export component.
     * QuickTime::GraphicsExportSetInputFile
     * @param theFile The QTFile that points to the source of input images to export component
     * @param desc the ImageDescription handle
     * @param {@link quicktime.QTException}
     */
    public void setInputFile(QTFile paramQTFile, ImageDescription paramImageDescription) throws QTException {
        int i = GraphicsExportSetInputFile(_ID(), paramQTFile.getFSSpec(true, 256), QTObject.ID(paramImageDescription));
        StdQTException.checkError(i);
    }

    /**
     * Gets the file that is the source for input images to graphics export component.
     * QuickTime::GraphicsExportGetInputFile
     * @return a QTFile object that has the source of input images to the Graphics exporter.
     * @return {@link quicktime.QTException}
     * @see quicktime.io.QTFile.fromGraphicsExporterInput
     */
    public QTFile getInputFile() throws QTException {
        return QTFile.fromGraphicsExporterInput(this);
    }

    /**
     * Sets the data handle as the source for input images to graphics export component.
     * QuickTime::GraphicsExportSetInputHandle
     * @param h The data handle that points to the source of input images to export component
     * @param desc the ImageDescription handle
     * @param {@link quicktime.QTException}
     */
    public void setInputHandle(QTHandle paramQTHandle, ImageDescription paramImageDescription) throws QTException {
        int i = GraphicsExportSetInputHandle(_ID(), QTObject.ID(paramQTHandle), QTObject.ID(paramImageDescription));
        StdQTException.checkError(i);
    }

    /**
     * Determines the data reference that is the source for input images to graphics export component.
     * QuickTime::GraphicsExportGetInputHandle
     * @return a QTHandle object that has the source of input images to the Graphics exporter.
     * @return {@link quicktime.QTException}
     * @see quicktime.std.util.QTHandle.fromGraphicsExporter
     */
    public QTHandle getInputHandle() throws QTException {
        return QTHandle.fromGraphicsExporterInput(this);
    }

    /**
     * Sets the data handle reference as the source for input images to graphics export component.
     * QuickTime::GraphicsExportSetInputPtr
     * @param p The pointer that points to the source of input images of export component
     * @param desc the ImageDescription handle
     * @param {@link quicktime.QTException}
     */
    public void setInputPtr(QTPointerRef paramQTPointerRef, ImageDescription paramImageDescription) throws QTException {
        int i = GraphicsExportSetInputPtr(_ID(), QTObject.ID(paramQTPointerRef), paramImageDescription.getDataSize(), QTObject.ID(paramImageDescription));
        StdQTException.checkError(i);
    }

    /**
     * Determines the data reference that is the source for input images to graphics export component.
     * QuickTime::GraphicsExportGetInputPtr
     * @return a QTPointerRef object that has the source of input images to the Graphics exporter.
     * @return {@link quicktime.QTException}
     */
    public QTPointerRef getInputPtr() throws QTException {
        return QTPointer.fromGraphicsExporter(this);
    }

    /**
     * Sets the GraphicsImporter as the source for input images to graphics export component.
     * QuickTime::GraphicsExportSetInputGraphicsImporter
     * @param grip The GraphicsImporter that points to the source of input images to export component
     * @param {@link quicktime.QTException}
     */
    public void setInputGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws QTException {
        int i = GraphicsExportSetInputGraphicsImporter(_ID(), QTObject.ID(paramGraphicsImporter));
        StdQTException.checkError(i);
    }

    /**
     * Determines the GraphicsImporter that is the source for input images to graphics export component.
     * QuickTime::GraphicsExportGetInputGraphicsImporter
     * @return a GraphicsImporter object that has the source of input images to the Graphics exporter.
     * @return {@link quicktime.QTException}
     */
    public GraphicsImporter getInputGraphicsImporter() throws QTException {
        return GraphicsImporter.fromGraphicsExporter(this);
    }

    /**
     * Sets the Pict as the source for input images to graphics export component.
     * QuickTime::GraphicsExportSetInputPicture
     * @param picture The Pict that points to the source of input images to export component
     * @param {@link quicktime.QTException}
     */
    public void setInputPicture(Pict paramPict) throws QTException {
        int i = GraphicsExportSetInputPicture(_ID(), QTObject.ID(paramPict));
        StdQTException.checkError(i);
    }

    /**
     * Determines the Pict that is the source for input images to graphics export component.
     * QuickTime::GraphicsExportGetInputPicture
     * @return a Pict object that has the source of input images to the Graphics exporter.
     * @return {@link quicktime.QTException}
     * @see quicktime.std.qd.Pict.fromGraphicsExporter
     */
    public Pict getInputPicture() throws QTException {
        return Pict.fromGraphicsExporter(this);
    }

    /**
     * Sets the QDGraphics as the source for input images to graphics export component.
     * QuickTime::GraphicsExportSetInputGWorld
     * @param gworld The QDGraphics that points to the source of input images to export component
     * @param {@link quicktime.QTException}
     */
    public void setInputGWorld(QDGraphics paramQDGraphics) throws QTException {
        int i = GraphicsExportSetInputGWorld(_ID(), QTObject.ID(paramQDGraphics));
        StdQTException.checkError(i);
    }

    /**
     * Determines the QDGraphics that is the source for input images to graphics export component.
     * QuickTime::GraphicsExportGetInputGWorld
     * @return a QDGraphics object that has the source of input images to the Graphics exporter.
     * @return {@link quicktime.QTException}
     * @see quicktime.qd.QDGraphics.fromGraphicsExporter
     */
    public QDGraphics getInputGWorld() throws QTException {
        return QDGraphics.fromGraphicsExporter(this);
    }

    /**
     * Sets the QDGraphics as the source for input images to graphics export component.
     * QuickTime::GraphicsExportSetInputPixmap
     * @param gworld The QDGraphics that points to the source of input images to export component
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInputPixmap(QDGraphics paramQDGraphics) throws StdQTException {
        if (paramQDGraphics == null)
            throw new StdQTException(-50);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = GraphicsExportSetInputPixmap(_ID(), QTObject.ID(paramQDGraphics.getPixMap()));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
    }

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    /**
     * Determines the QDGraphics that is the source for input images to graphics export component.
     * QuickTime::GraphicsExportGetInputPixmap
     * @return a QDGraphics object that has the source of input images to the Graphics exporter.
     * @return {@link quicktime.QTException}
     * @see quicktime.qd.QDGraphics.fromGraphicsExporter
     */
    public PixMap getInputPixmap() throws QTException {
        return PixMap.fromGraphicsExporter(this);
    }

    /**
     * Used to specify destination for output images.
     * Sets the specified DataRef as the  destination for output images.
     * QuickTime::GraphicsExportSetOutputDataReference
     * @param dataRef The DataRef for output images to export
     * @param {@link quicktime.QTException}
     */
    public void setOutputDataReference(DataRef paramDataRef) throws QTException {
        int i = paramDataRef.getType();
        int j = GraphicsExportSetOutputDataReference(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType());
        StdQTException.checkError(j);
    }

    /**
     * Gets the DataRef of the destination for output images.
     * QuickTime::GraphicsExportGetOutputDataReference
     * @return a DataRef object specifying the destination for output images.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.util.QTHandle#fromGraphicsExporterOutput(quicktime.std.image.GraphicsExporter)}
     */
    public DataRef getOutputDataReference() throws QTException {
        return DataRef.fromGraphicsExporterOutputDataRef(this);
    }

    /**
     * Sets the specified QTFile as the destination for output images.
     * QuickTime::GraphicsExportSetOutputFile
     * @param theFile The QTFile that points to destination for output images.
     * @param {@link quicktime.QTException}
     */
    public void setOutputFile(QTFile paramQTFile) throws QTException {
        int i = GraphicsExportSetOutputFile(_ID(), paramQTFile.getFSSpec(false, 512));
        StdQTException.checkError(i);
    }

    /**
     * Gets the file that is the destination for output images.
     * QuickTime::GraphicsExportGetOutputFile
     * @return a QTFile object that has the destination for output images.
     * @return {@link quicktime.QTException}
     * @see quicktime.io.QTFile.fromGraphicsExporter
     */
    public QTFile getOutputFile() throws QTException {
        return QTFile.fromGraphicsExporterOutput(this);
    }

    /**
     * Sets the data handle as the destination for output images.
     * QuickTime::GraphicsExportSetOutputHandle
     * @param h The data handle that points to the destination for output images.
     * @param {@link quicktime.QTException}
     */
    public void setOutputHandle(QTHandleRef paramQTHandleRef) throws QTException {
        int i = GraphicsExportSetOutputHandle(_ID(), QTObject.ID(paramQTHandleRef));
        StdQTException.checkError(i);
    }

    /**
     * Determines the data reference that is the destination for output images.
     * QuickTime::GraphicsExportGetOutputHandle
     * @return a QTHandle object that has the destination for output images.
     * @return {@link quicktime.QTException}
     * @see quicktime.std.util.QTHandle.fromGraphicsExporter
     */
    public QTHandle getOutputHandle() throws QTException {
        return QTHandle.fromGraphicsExporterOutput(this);
    }

    /**
     * Specifies the output starting offset and maximum size limit.
     * QuickTime::GraphicsExportSetOutputOffsetAndMaxSize
     * @param offset The byte offset of the image data from the beginning of the data reference.
     * @param maxSize A value describing the maximum size limit.
     * @param truncateFile A Boolean value specifying whether to truncate the file.
     * @param {@link quicktime.QTException}
     */
    public void setOutputOffsetAndMaxSize(int paramInt1, int paramInt2, boolean paramBoolean) throws QTException {
        int i = GraphicsExportSetOutputOffsetAndMaxSize(_ID(), paramInt1, paramInt2, (byte) (!paramBoolean ? 0 : 1));
        StdQTException.checkError(i);
    }

    /**
     * Gets the output starting offset and maximum size limit.
     * QuickTime::GraphicsExportGetOutputOffsetAndMaxSize
     * @return a  starting offset.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getOutputOffset() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetOutputOffsetAndMaxSize(_ID(), arrayOfInt, null, null);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Gets the output starting offset and maximum size limit.
     * QuickTime::GraphicsExportGetOutputOffsetAndMaxSize
     * @return maximum size limit.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getOutputMaxSize() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetOutputOffsetAndMaxSize(_ID(), null, arrayOfInt, null);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Gets the output starting offset and maximum size limit.
     * QuickTime::GraphicsExportGetOutputOffsetAndMaxSize
     * @return maximum size limit.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean getOutputTruncateFile() throws StdQTException {
        byte[] arrayOfByte = { 0 };
        int i = GraphicsExportGetOutputOffsetAndMaxSize(_ID(), null, null, arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * Specifies the output starting offset and maximum size limit.
     * QuickTime::GraphicsExportSetOutputFileTypeAndCreator
     * @param fileType The file type for the new image file, such as `JPEG' .
     * @param fileCreator The file creator for the new image file. This parameter may be 0, in which case a default file
     *  creator for this file type is used.
     * @param {@link quicktime.QTException}
     */
    public void setOutputFileTypeAndCreator(int paramInt1, int paramInt2) throws QTException {
        int i = GraphicsExportSetOutputFileTypeAndCreator(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * Gets the Output file type.
     * QuickTime::GraphicsExportGetOutputFileTypeAndCreator
     * @return fileType
     * @return {@link quicktime.QTException}
     */
    public int getOutputFileType() throws QTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = GraphicsExportGetOutputFileTypeAndCreator(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0];
    }

    /**
     * Gets the Output file Creator.
     * QuickTime::GraphicsExportGetOutputFileTypeAndCreator
     * @return fileCreator
     * @return {@link quicktime.QTException}
     */
    public int getOutputFileCreator() throws QTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = GraphicsExportGetOutputFileTypeAndCreator(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    /**
     * Used by format-specific graphics exporters when transcoding:
     * Asks whether the image source is in a form that can be read.
     * QuickTime::GraphicsExportMayExporterReadInputData
     * return true if the exporter is allowed to read the input data
     */
    public boolean mayExporterReadInputData() throws QTException {
        byte[] arrayOfByte = { 0 };
        int i = GraphicsExportMayExporterReadInputData(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * Used by format-specific graphics exporters when transcoding:
     * Returns the number of bytes of original image data that can be read.
     * QuickTime::GraphicsExportGetInputDataSize
     * @return returns the size in bytes.
     * @return {@link quicktime.QTException}
     */
    public int getInputDataSize() throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetInputDataSize(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public byte[] readInputData(int paramInt1, int paramInt2) throws StdQTException {
        byte[] arrayOfByte = new byte[paramInt2];
        int i = GraphicsExportReadInputData(_ID(), arrayOfByte, paramInt1, paramInt2);
        StdQTException.checkError(i);
        return arrayOfByte;
    }

    /**
     * Used by format-specific graphics exporters, especially when doing standalone export:
     * Returns the number of bytes of original image data that can be read.
     * QuickTime::GraphicsExportGetInputImageDescription
     * @return returns the ImageDescription
     * @return {@link quicktime.QTException}
     */
    public ImageDescription getInputImageDescription() throws QTException {
        return ImageDescription.fromGraphicsExporter(this);
    }

    /**
     * Used by format-specific graphics exporters, especially when doing standalone export:
     * Returns the dimensions of the input image.
     * QuickTime::GraphicsExportGetInputImageDimensions
     * @return Returns the rectangle that has the dimensions of the input image.
     * @return {@link quicktime.QTException}
     */
    public QDRect getInputImageDimensions() throws QTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsExportGetInputImageDimensions(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    /**
     * Used by format-specific graphics exporters, especially when doing standalone export:
     * Returns the depth of the input image.
     * QuickTime::GraphicsExportGetInputImageDepth
     * @return Returns the depth of the input image..
     * @return {@link quicktime.QTException}
     */
    public int getInputImageDepth() throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetInputImageDepth(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Used by format-specific graphics exporters, especially when doing standalone export:
     * Draws a rectangular portion of the input image.
     * srcRect and dstRect need not be the same width and height; you can use this function to scale the
     * image portion. This would be useful, for example, if you were writing a graphics exporter for a
     * multiple-resolution format.
     * QuickTime::GraphicsExportDrawInputImage
     */
    public void drawInputImage(QDGraphics paramQDGraphics, QDRect paramQDRect1, QDRect paramQDRect2) throws QTException {
        int i = GraphicsExportDrawInputImage(_ID(), QTObject.ID(paramQDGraphics), 0, paramQDRect1.getRect(), paramQDRect2.getRect());
        StdQTException.checkError(i);
    }

    /**
     * Only applicable when the input is a data reference, file, handle or ptr:
     * Specifies the portion of an input data reference, file, handle or pointer that a graphics exporter is
     * permitted to read. This would be useful if, for example, the source was a JPEG image embedded within
     * a larger file.
     * QuickTime::GraphicsExportSetInputOffsetAndLimit
     * @param offset The byte offset of the input image data from the beginning of the data reference.
     * @param limit The offset of the byte following the last byte of the input image data. (If you do not need to apply any limit unsigned int -1
     * @param {@link quicktime.QTException}
     */
    public void setInputOffsetAndLimit(int paramInt1, int paramInt2) throws QTException {
        int i = GraphicsExportSetInputOffsetAndLimit(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * Only applicable when the input is a data reference, file, handle or ptr:
     * Specifies the portion of an input data reference, file, handle or pointer that a graphics exporter is
     * permitted to read. This would be useful if, for example, the source was a JPEG image embedded within
     * a larger file.
     * QuickTime::GraphicsExportGetInputOffsetAndLimit
     * @return offset of input image data
     * @return {@link quicktime.QTException}
     */
    public int getInputOffset() throws QTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = GraphicsExportGetInputOffsetAndLimit(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0];
    }

    /**
     * Only applicable when the input is a data reference, file, handle or ptr:
     * Specifies the portion of an input data reference, file, handle or pointer that a graphics exporter is
     * permitted to read. This would be useful if, for example, the source was a JPEG image embedded within
     * a larger file.
     * QuickTime::GraphicsExportGetInputOffsetAndLimit
     * @return offset of the byte following the last byte of the input image data
     * @return {@link quicktime.QTException}
     */
    public int getInputLimit() throws QTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = GraphicsExportGetInputOffsetAndLimit(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    /**
     * These functions are used for obtaining graphics exporter settings and displaying a settings dialog box.
     * Some graphics exporters don't support settings dialogs, and so do not implement this call. To find out
     * whether a graphics exporter implements this call, use CallComponentCanDo (
     * myGraphicsExporter , kGraphicsExportRequestSettingsSelect ).
     * QuickTime::GraphicsExportRequestSettings
     */
    public void requestSettings() throws QTException {
        int j;
        if (QTSession.isCurrentOS(4)) {
            j = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                public Object invoke() {
                    int i = 0;
                    synchronized (QTNative.globalsLock) {
                        i = GraphicsExporter.GraphicsExportRequestSettings(_ID(), 0, 0);
                    }
                    return new Integer(i);
                }
            })).intValue();
            StdQTException.checkError(j);
        } else {
            synchronized (QTNative.globalsLock) {
                j = GraphicsExportRequestSettings(_ID(), 0, 0);
                if (j == 1)
                    throw new StdQTException(-128);
                StdQTException.checkError(j);
            }
        }
    }

    /**
     * These functions are used for obtaining graphics exporter settings and displaying a settings dialog box.
     * Sets the graphics exporter component's current configuration to match the settings in the passed atom
     * container.
     * QuickTime::GraphicsExportSetSettingsFromAtomContainer
     * @param qtAtomContainer A QuickTime atom container containing settings.
     * @param {@link quicktime.QTException}
     */
    public void setSettingsFromAtomContainer(AtomContainer paramAtomContainer) throws QTException {
        int i = GraphicsExportSetSettingsFromAtomContainer(_ID(), paramAtomContainer.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * These functions are used for obtaining graphics exporter settings and displaying a settings dialog box.
     * Retrieves the current settings from the graphics exporter component
     * QuickTime::GraphicsExportGetSettingsAsAtomContainer
     * @return AtomContainer containing settings.
     * @return {@link quicktime.QTException}
     */
    public AtomContainer getSettingsAsAtomContainer() throws QTException {
        return AtomContainer.fromGraphicsExporter(this);
    }

    /**
     * This function is used for obtaining graphics exporter settings and displaying a settings dialog box.
     * Retrieves the current settings in a user-readable format.
     * QuickTime::GraphicsExportGetSettingsAsText
     * @return QTHandle containing the text.
     * @return {@link quicktime.QTException}
     */
    public QTHandle getSettingsAsText() throws QTException {
        return QTHandle.fromGraphicsExporterText(this);
    }

    /**
     * Returns the suggested file name extension.
     * QuickTime::GraphicsExportGetDefaultFileNameExtension
     * @return Returns the suggested file name extension.
     * @return {@link quicktime.QTException}
     */
    public String getDefaultFileNameExtension() throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetDefaultFileNameExtension(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return QTUtils.fromOSType(arrayOfInt[0]);
    }

    /**
     * Returns MIME types and other information about the graphics format.
     * QuickTime::GraphicsExportGetMIMETypeList
     * The format's name, as a string in an atom of type kMimeInfoDescriptionTag .
     * Optionally, the MIME type, as a string in an atom of type kMimeInfoMimeTypeTag .
     * @return Returns AtomContainer containing information about the graphics format.
     * @return {@link quicktime.QTException}
     */
    public AtomContainer getMIMETypeList() throws QTException {
        return AtomContainer.fromGraphicsExporterMIME(this);
    }

    /**
     * Returns the default file Type  for the new image file format.
     * QuickTime::GraphicsExportGetDefaultFileTypeAndCreator
     * @return Returns the default file Type  for the new image file format
     * @return {@link quicktime.QTException}
     */
    public String getDefaultFileType() throws QTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = GraphicsExportGetDefaultFileTypeAndCreator(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return QTUtils.fromOSType(arrayOfInt1[0]);
    }

    /**
     * Returns file creator for the new image file format.
     * QuickTime::GraphicsExportGetDefaultFileTypeAndCreator
     * @return Returns file creator for the new image file format
     * @return {@link quicktime.QTException}
     */
    public String getDefaultFileCreator() throws QTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = GraphicsExportGetDefaultFileTypeAndCreator(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return QTUtils.fromOSType(arrayOfInt2[0]);
    }

    /**
     * Format-specific exporters may delegate this call; the base graphics exporter's implementation gives a reply
     * of false .Use this call to ask a graphics exporter whether it can transcode the current input with the current settings.
     * QuickTime::GraphicsExportCanTranscode
     * @return Returns true if current export operation can be performed by transcoding.
     * @return {@link quicktime.QTException}
     */
    public boolean canTranscode() throws QTException {
        byte[] arrayOfByte = { 0 };
        int i = GraphicsExportCanTranscode(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * The base graphics exporter makes this call of the format-specific graphics exporter to perform a
     * transcoding export.
     * QuickTime::GraphicsExportDoTranscode
     */
    public void doTranscode() throws QTException {
        int i = GraphicsExportDoTranscode(_ID());
        StdQTException.checkError(i);
    }

    /**
     * The base graphics exporter makes this call of the format-specific graphics exporter to ask whether the
     * current export operation should be done by using an image compressor.
     * QuickTime::GraphicsExportCanUseCompressor
     * @return true if allowed to use a compressor.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.AtomContainer#fromGraphicsExporterCompressor(quicktime.std.image.GraphicsExporter)}
     */
    public GECompressorInfo canUseCompressor() throws QTException {
        return AtomContainer.fromGraphicsExporterCompressor(this);
    }

    /**
     * The base graphics exporter makes this call of the format-specific graphics exporter to ask whether the
     * current export operation should be done by using an image compressor.
     * QuickTime::GraphicsExportDoUseCompressor
     * @return ImageDescription object describing the compressed image.
     * @return {@link quicktime.QTException}
     */
    public ImageDescription doUseCompressor(AtomContainer paramAtomContainer) throws QTException {
        return ImageDescription.fromGraphicsExporterCompressor(this, paramAtomContainer);
    }

    /**
     * Requests that the original compressed data not be decompressed and recompressed, but be copied
     * (possibly with modifications) through to the output file.
     * QuickTime::GraphicsExportSetDontRecompress
     * @param dontRecompress true if not allowed to recompressor.
     * @param {@link quicktime.QTException}
     */
    public void setDontRecompress(boolean paramBoolean) throws QTException {
        int i = GraphicsExportSetDontRecompress(_ID(), (byte) (paramBoolean ? 1 : 0));
        StdQTException.checkError(i);
    }

    /**
     * Requests that the original compressed data not be decompressed and recompressed, but be copied
     * (possibly with modifications) through to the output file.
     * QuickTime::GraphicsExportSetDontRecompress
     * @return dontRecompress true if not allowed to recompressor.
     * @return {@link quicktime.QTException}
     */
    public boolean getDontRecompress() throws QTException {
        byte[] arrayOfByte = { 0 };
        int i = GraphicsExportGetDontRecompress(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * Defines the interlace style.
     * QuickTime::GraphicsExportSetInterlaceStyle
     * @param interlaceStyle A value describing the style of the image data .
     * @param {@link quicktime.QTException}
     */
    public void setInterlaceStyle(int paramInt) throws QTException {
        int i = GraphicsExportSetInterlaceStyle(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Returns the interlace style.
     * QuickTime::GraphicsExportGetInterlaceStyle
     * @return the interlace style.
     * @return {@link quicktime.QTException}
     */
    public int getInterlaceStyle() throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetInterlaceStyle(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Defines supplemental data, such as textual copyright information.
     * In QuickTime 4, none of the supplied graphics exporters support setting MetaData .
     * QuickTime::GraphicsExportSetMetaData
     * @param userData object .
     * @param {@link quicktime.QTException}
     */
    public void setMetaData(UserData paramUserData) throws QTException {
        int i = GraphicsExportSetMetaData(_ID(), QTObject.ID(paramUserData));
        StdQTException.checkError(i);
    }

    /**
     * Returns supplemental data, such as textual copyright information.
     * In QuickTime 4, none of the supplied graphics exporters support setting MetaData .
     * QuickTime::GraphicsExportGetMetaData
     * @return user data list
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.media.UserData#fromGraphicsExporter(quicktime.std.image.GraphicsExporter)}
     */
    public UserData getMetaData() throws QTException {
        return UserData.fromGraphicsExporter(this);
    }

    /**
     * Defines a desired maximum data size and asks for a quality that does not exceed that size.
     * QuickTime::GraphicsExportSetTargetDataSize
     * @param targetDataSize A value describing the maximum size of the image data in bytes.
     * @param {@link quicktime.QTException}
     */
    public void setTargetDataSize(int paramInt) throws QTException {
        int i = GraphicsExportSetTargetDataSize(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Specifies a desired maximum data size and asks for a quality that does not exceed that size.
     * QuickTime::GraphicsExportGetTargetDataSize
     * @return A value describing the maximum size of the image data in bytes.
     * @return {@link quicktime.QTException}
     */
    public int getTargetDataSize() throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetTargetDataSize(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Defines the compression method to use.
     * QuickTime::GraphicsExportSetCompressionMethod
     * @param compressionMethod A value describing the compression algorithm to be used by the graphics exporter.
     * @param {@link quicktime.QTException}
     */
    public void setCompressionMethod(int paramInt) throws QTException {
        int i = GraphicsExportSetCompressionMethod(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Defines the compression method to use.
     * QuickTime::GraphicsExportGetCompressionMethod
     * @return a value describing the compression algorithm used by the graphics exporter.
     * @return {@link quicktime.QTException}
     */
    public int getCompressionMethod() throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetCompressionMethod(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Defines the compression quality.
     * QuickTime::GraphicsExportSetCompressionQuality
     * @param spatialQuality The currently specified quality value
     * @param {@link quicktime.QTException}
     */
    public void setCompressionQuality(int paramInt) throws QTException {
        int i = GraphicsExportSetCompressionQuality(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Defines the compression quality to use.
     * QuickTime::GraphicsExportGetCompressionQuality
     * @return a value describing the compression quality used by the graphics exporter.
     * @return {@link quicktime.QTException}
     */
    public int getCompressionQuality() throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetCompressionQuality(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Defines the resolution to save and store.
     * QuickTime::GraphicsExportSetResolution
     */
    public void setResolution(int paramInt1, int paramInt2) throws QTException {
        int i = GraphicsExportSetResolution(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * Gets the resolution to saved and stored.
     * QuickTime::GraphicsExportGetResolution
     * @return a value describing the horizontal resolution of the image.
     * @return {@link quicktime.QTException}
     */
    public int getHorizontalResolution() throws QTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = GraphicsExportGetResolution(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0];
    }

    /**
     * Defines the compression quality to use.
     * QuickTime::GraphicsExportGetResolution
     * @return a value describing the vertical resolution of the image.
     * @return {@link quicktime.QTException}
     */
    public int getVerticalResolution() throws QTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = GraphicsExportGetResolution(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    /**
     * Defines the depth to use. Some image file formats support more than one pixel depth.
     * QuickTime::GraphicsExportSetDepth
     */
    public void setDepth(int paramInt) throws QTException {
        int i = GraphicsExportSetDepth(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Defines the depth to use. Some image file formats support more than one pixel depth.
     * QuickTime::GraphicsExportGetDepth
     * @return A value describing the depth of the image data as an OSType specifying the pixel format
     * @return {@link quicktime.QTException}
     */
    public int getDepth() throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetDepth(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void GraphicsExportWriteOutputData(byte[] paramArrayOfByte, int paramInt) throws StdQTException {
        int i = GraphicsExportWriteOutputData(_ID(), paramArrayOfByte, paramInt);
        StdQTException.checkError(i);
    }

    public byte[] GraphicsExportReadOutputData(int paramInt1, int paramInt2) throws StdQTException {
        byte[] arrayOfByte = new byte[paramInt2];
        int i = GraphicsExportReadOutputData(_ID(), arrayOfByte, paramInt1, paramInt2);
        StdQTException.checkError(i);
        return arrayOfByte;
    }

    /**
     * Sets whether or not the graphics exporter component should create Exif files.
     * This routine is only supported by the TIFF and JPEG graphics exporters.
     * Requires QuickTime 6
     * @param enableExif Indicates whether to turn Exif export on or off. set true if on.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setExifEnabled(boolean paramBoolean) throws StdQTException {
        int i = GraphicsExportSetExifEnabled(_ID(), (byte) (paramBoolean ? 0 : 1));
        StdQTException.checkError(i);
    }

    /**
     * Returns the current Exif export setting.
     * This routine is only supported by the TIFF and JPEG graphics exporters.
     * Requires QuickTime 6
     * @return the current Exif export setting. true if Exif is on.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean getExifEnabled() throws StdQTException {
        byte[] arrayOfByte = { 0 };
        int i = GraphicsExportGetExifEnabled(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * Sets whether or not the graphics exporter component should create an
     * embedded thumbnail inside the exported file.
     * This routine is only supported by the TIFF and JPEG graphics exporters.
     * The JPEG graphics exporter can only create thumbnails when writing Exif files.
     * Requires QuickTime 6
     * @param enableThumbnail Indicates whether thumbnail creation should be turned on or off.
     * @param maxThumbnailWidth The maximum width for created thumbnails.
     * @param maxThumbnailHeight The maximum height for created thumbnails. If one maximum dimension is zero,
     *  only the other will be used. If both maximum dimensions are 0, the graphics exporter will decide for itself.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setThumbnailEnabled(boolean paramBoolean, int paramInt1, int paramInt2) throws StdQTException {
        int i = GraphicsExportSetThumbnailEnabled(_ID(), (byte) (paramBoolean ? 1 : 0), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * Returns the current thumbnail export settings.
     * This routine is only supported by the TIFF and JPEG graphics exporters.
     * Requires QuickTime 6
     * @return the current thumbnail export settings. true if enabled.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean getThumbnailEnabled() throws StdQTException {
        byte[] arrayOfByte = { 0 };
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = GraphicsExportGetThumbnailEnabled(_ID(), arrayOfByte, arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * Returns the current thumbnail export settings.
     * This routine is only supported by the TIFF and JPEG graphics exporters.
     * Requires QuickTime 6
     * @return the current thumbnail dimensions, maxThumbnailWidth and maxThumbnailHeight
     * @return {@link quicktime.std.StdQTException}
     */
    public QDDimension getThumbnailDimension() throws StdQTException {
        byte[] arrayOfByte = { 0 };
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = GraphicsExportGetThumbnailEnabled(_ID(), arrayOfByte, arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return new QDDimension(arrayOfInt1[0], arrayOfInt2[0]);
    }

    private static native int GraphicsExportSetExifEnabled(int paramInt, byte paramByte);

    private static native int GraphicsExportGetExifEnabled(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportSetThumbnailEnabled(int paramInt1, byte paramByte, int paramInt2, int paramInt3);

    private static native int GraphicsExportGetThumbnailEnabled(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsExportDoExport(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportSetInputDataReference(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int GraphicsExportSetInputFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    private static native int GraphicsExportSetInputHandle(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsExportSetInputPtr(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int GraphicsExportSetInputGraphicsImporter(int paramInt1, int paramInt2);

    private static native int GraphicsExportSetInputPicture(int paramInt1, int paramInt2);

    private static native int GraphicsExportSetInputGWorld(int paramInt1, int paramInt2);

    private static native int GraphicsExportSetInputPixmap(int paramInt1, int paramInt2);

    private static native int GraphicsExportSetOutputDataReference(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsExportSetOutputFile(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportSetOutputHandle(int paramInt1, int paramInt2);

    private static native int GraphicsExportSetOutputOffsetAndMaxSize(int paramInt1, int paramInt2, int paramInt3, byte paramByte);

    private static native int GraphicsExportGetOutputOffsetAndMaxSize(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, byte[] paramArrayOfByte);

    private static native int GraphicsExportSetOutputFileTypeAndCreator(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsExportGetOutputFileTypeAndCreator(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsExportMayExporterReadInputData(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportGetInputDataSize(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportReadInputData(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);

    private static native int GraphicsExportGetInputImageDimensions(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportGetInputImageDepth(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportDrawInputImage(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int GraphicsExportSetInputOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsExportGetInputOffsetAndLimit(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsExportRequestSettings(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsExportSetSettingsFromAtomContainer(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportGetDefaultFileTypeAndCreator(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsExportGetDefaultFileNameExtension(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportGetMIMETypeList(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportCanTranscode(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportDoTranscode(int paramInt);

    private static native int GraphicsExportCanUseCompressor(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native int GraphicsExportDoStandaloneExport(int paramInt);

    private static native int GraphicsExportSetDontRecompress(int paramInt, byte paramByte);

    private static native int GraphicsExportGetDontRecompress(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportSetInterlaceStyle(int paramInt1, int paramInt2);

    private static native int GraphicsExportGetInterlaceStyle(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportSetMetaData(int paramInt1, int paramInt2);

    private static native int GraphicsExportGetMetaData(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportSetTargetDataSize(int paramInt1, int paramInt2);

    private static native int GraphicsExportGetTargetDataSize(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportSetCompressionMethod(int paramInt1, int paramInt2);

    private static native int GraphicsExportGetCompressionMethod(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportSetCompressionQuality(int paramInt1, int paramInt2);

    private static native int GraphicsExportGetCompressionQuality(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportSetResolution(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsExportGetResolution(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsExportSetDepth(int paramInt1, int paramInt2);

    private static native int GraphicsExportGetDepth(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportWriteOutputData(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    private static native int GraphicsExportReadOutputData(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /GraphicsExporter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
