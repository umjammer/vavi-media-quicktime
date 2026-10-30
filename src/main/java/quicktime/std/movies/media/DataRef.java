package quicktime.std.movies.media;

import java.io.File;
import java.io.IOException;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.internal.toolbox.GraphicsExportManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.io.AliasHandle;
import quicktime.io.QTFile;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.movies.Movie;
import quicktime.std.sg.SGOutput;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.EndianOrder;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;
import quicktime.util.StringHandle;


/**
 * The DataRef class implements the data reference structure of the Movie Toolbox.
 */
public final class DataRef extends QTHandle implements QuickTimeLib {

    private int dataRefType;

    /**
     * This method returns a copy of a data reference.
     * @see "QuickTime::GraphicsExportGetInputDataReference()"
     * @return a copy of a data reference from the graphics exporter component.
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsExporter#getInputDataReference
     */
    public static DataRef fromGraphicsExporterInputDataRef(GraphicsExporter ge) throws QTException {
        int[] dataRef = new int[1];
        int[] dataRefType = new int[1];
        int i = GraphicsExportGetInputDataReference(QTObject.ID(ge), dataRef, dataRefType);
        return new DataRef(dataRef[0], dataRefType[0], 0);
    }

    /**
     * This method returns a copy of a data reference.
     * @see "QuickTime::GraphicsExportGetOutputDataReference()"
     * @return a copy of a data reference from the graphics exporter component.
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsExporter#getOutputDataReference
     */
    public static DataRef fromGraphicsExporterOutputDataRef(GraphicsExporter ge) throws QTException {
        int[] dataRef = new int[1];
        int[] dataRefType = new int[1];
        int i = GraphicsExportGetOutputDataReference(QTObject.ID(ge), dataRef, dataRefType);
        StdQTException.checkError(i);
        return new DataRef(dataRef[0], dataRefType[0], 0);
    }

    /**
     * This method allows your application to get the default data reference for the movie.
     * @see "QuickTime::GetMovieDefaultDataRef()"
     * @return a DataRef object specifying the default data reference for the movie.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Movie#getDefaultDataRef()
     */
    public static DataRef fromMovie(Movie movie) throws QTException {
        int[] dataRef = new int[1];
        int[] dataRefType = new int[1];
        int i = GetMovieDefaultDataRef(QTObject.ID(movie), dataRef, dataRefType);
        StdQTException.checkError(i);
        return new DataRef(dataRef[0], dataRefType[0], 0);
    }

    /**
     * Determines the data reference that is currently assigned to a sequence grabber component.
     * @see "QuickTime::SGGetDataRef"
     * @return the dataRef
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.sg.SequenceGrabber#getDataRef()
     */
    public static DataRef fromSequenceGrabber(SequenceGrabber sg) throws StdQTException {
        int[] dataRef = new int[1];
        int[] dataRefType = new int[1];
        int[] flags = new int[1];
        int i = SGGetDataRef(QTObject.ID(sg), dataRef, dataRefType, flags);
        StdQTException.checkError(i);
        return new DataRef(dataRef[0], dataRefType[0], 0, sg);
    }

    /**
     * This method returns a copy of a specified data reference.
     * @see "QuickTime::GetMediaDataRef()"
     * @param index Identifies the data reference.
     * @return a copy of a specified data reference.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.media.Media#getDataRef(int)
     */
    public static DataRef fromMedia(Media media, int index) throws QTException {
        int[] dataRef = new int[1];
        int[] dataRefType = new int[1];
        int[] dataRefAttributes = new int[1];
        StdQTException.checkError(GetMediaDataRef(QTObject.ID(media), (short) index, dataRef, dataRefType, dataRefAttributes));
        return new DataRef(dataRef[0], dataRefType[0], dataRefAttributes[0]);
    }

    /**
     * @see "QuickTime::SGGetOutputDataReference("
     * @param sg the sequence grabber that owns this output
     * @param output the output from which to get the data ref from
     * @return the data reference associated with this sequence grabber output.
     * @throws quicktime.std.StdQTException
     */
    public static DataRef fromSGOutput(SequenceGrabber sg, SGOutput output) throws StdQTException {
        int[] dataRef = { 0 };
        int[] dataRefType = { 0 };
        int i = SGGetOutputDataReference(QTObject.ID(sg), QTObject.ID(output), dataRef, dataRefType);
        StdQTException.checkError(i);
        return new DataRef(dataRef[0], dataRefType[0], 0);
    }

    /**
     * This constructor creates a DataRef that represents any URL.
     * If there is no DataHandler for this URL an invalidDataRef exception is thrown
     * @param url the URL that is used to create a DataReference.
     */
    public DataRef(String url) throws QTException {
        super(4, false);
        if (url.startsWith("file")) {
            QTFile qtFile = new QTFile(url.substring(7).replace('/', File.separatorChar));
            setAliasHandle(qtFile.newAlias(true));
        } else {
            setURLDataRef(QTUtils.String2CString(url));
        }
    }

    /**
     * This constructor creates a DataRef that represents a QTFile.
     */
    public DataRef(QTFile qtFile) throws QTException {
        super(4, false);
        setAliasHandle(qtFile.newAlias(true));
    }

    /**
     * This constructor creates a DataRef that represents an AliasHandle.
     * @param a the AliasHandle that is reference by this DataRef.
     */
    public DataRef(AliasHandle a) throws IOException, QTException {
        super(4, false);
        setAliasHandle(a);
    }

    /**
     * This constructor creates a DataRef that references the given QTHandle
     * so the dataRefType is a handle DataRef ('hndl').
     * @param handle Specifies the data (a handle) that this reference should point to.
     */
    public DataRef(QTHandleRef handle) throws QTException {
        super(4, true);
        this.dataRefType = 1752065132;
        this.hdl = handle;
        setIntAt(0, QTObject.ID(handle));
    }

    /**
     * This constructor creates a DataRef that references the given QTHandle
     * so the dataRefType is a handle DataRef ('hndl'). Hints can be given to
     * QuickTime (and should if at all possible) about the format of the data
     * that is contained within the handle. Hints can take one of three forms
     *
     * MIME Type -> kDataRefMimeTypeTag
     * MacOS File Type -> kDataRefQTFileTypeTag
     * File extension -> kDataRefFileExtensionTag
     *
     * The preferred usage is to pass in either the MacOS File Type (which will also work on non-MacOS platforms)
     * or the MIME type information. The File extension is a more arbitrary indicator and should be the least
     * referred to. If the hint information is not specified a paramErr exception is thrown
     * @param handle Specifies the data (a handle) that this reference should point to.
     * @param hintFlag describes the hint data that is used
     * @param hintString contains the hint information
     */
    public DataRef(QTHandleRef handle, int hintFlag, String hintString) throws QTException {
        super(4, true);
        this.dataRefType = 1752065132;
        this.hdl = handle;
        setIntAt(0, QTObject.ID(handle));
        switch(hintFlag) {
            case 1718909296:
                if (QTSession.getQTVersion() == 3)
                    throw new QTException(-50);
                setSize(17);
                setByteAt(4, (byte) 0);
                setIntAt(5, EndianOrder.flipNativeToBigEndian32(12));
                setIntAt(9, EndianOrder.flipNativeToBigEndian32(1718909296));
                setIntAt(13, EndianOrder.flipNativeToBigEndian32(QTUtils.toOSType(hintString)));
                break;
            case 1835625829:
                if (QTSession.getQTVersion() == 3)
                    throw new QTException(-50);
                byte[] destArray = QTUtils.String2PString(hintString, 255);
                setSize(13 + destArray.length);
                setByteAt(4, (byte) 0);
                setIntAt(5, EndianOrder.flipNativeToBigEndian32(8 + destArray.length));
                setIntAt(9, EndianOrder.flipNativeToBigEndian32(1835625829));
                setBytesAt(13, destArray.length, destArray, 0);
                break;
            case 1702392864:
                concatenate(new StringHandle("X." + hintString, 2));
                break;
            default:
                throw new QTException(-50);
        }
    }

    private DataRef(int nativeRep, int dataRefType, int dataRefAttributes, Object ref) {
        super(nativeRep, ref, false);
        this.dataRefType = dataRefType;
        this.dataRefAttributes = dataRefAttributes;
    }

    private DataRef(int nativeRep, int dataRefType, int dataRefAttributes) {
        super(nativeRep, null, false);
        this.dataRefType = dataRefType;
        this.dataRefAttributes = dataRefAttributes;
    }

    private int dataRefAttributes = 0;

    private QTHandleRef hdl = null;

    private void setAliasHandle(AliasHandle aliasHandle) throws QTException {
        copyFromHandle(aliasHandle);
        this.dataRefType = 1634494835;
    }

    private void setURLDataRef(byte[] urlDataRef) throws QTException {
        setSize(urlDataRef.length);
        copyFromArray(0, urlDataRef, 0, urlDataRef.length);
        this.dataRefType = 1970433056;
        int i = GetDataHandler(_ID(), getType(), 1);
        if (i == 0)
            throw new QTException(63524);
    }

    private void copyFromHandle(QTHandleRef qtHandleRef) throws QTException {
        setSize(qtHandleRef.getSize());
        copyFromHandle(0, qtHandleRef, 0, qtHandleRef.getSize());
    }

    /**
     * This method returns the data reference type.
     * @return the data reference type.
     */
    public int getType() {
        return this.dataRefType;
    }

    /**
     * This method returns the data reference attributes.
     * @return the data reference attributes.
     */
    public int getAttributes() {
        return this.dataRefAttributes;
    }

    /**
     * Returns a string representating the location of this object. If the DataRef points
     * to a File or URL the path will be printed, if not then an empty string is returned.
     */
    public String getUniversalResourceLocator() throws QTException {
        if (this.dataRefType == 1970433056) {
            return getCStringAt(0);
        }
        if (this.dataRefType == 1634494835) {
            AliasHandle alias = AliasHandle.fromDataRef(this);
            QTFile qtFile = QTFile.resolveAlias(alias);
            return qtFile.getPath();
        }
        return null;
    }

    /**
     * To resolve relative data references, a method was needed to determine the original location from which a movie was opened or imported.
     * This data reference indicates from where the movie was originally opened or imported but still allows the movie's default data reference
     * to remain nil in the import-in-place case.
     * This routine retrieves a movie's anchor data reference.
     * If the movie has setAnchorDataRef called on it, the routine will return a copy of the data reference and type.
     * If there is no anchor data reference associated with the movie, then this routine will return a copy of the default
     * data reference and type along with setting the following bit of the long integer referenced through the flags parameter.
     * @param theMovie the movie whose anchor data reference is returned.
     * @return movie's anchor data reference.
     * @throws quicktime.std.StdQTException
     */
    public DataRef fromMovieAnchor(Movie theMovie) throws StdQTException {
        int i = 0;
        int[] dataRef = { 0 };
        int[] dataRefType = { 0 };
        int[] outFlags = { 0 };
        i = GetMovieAnchorDataRef(QTObject.ID(theMovie), dataRef, dataRefType, outFlags);
        StdQTException.checkError(i);
        if (dataRef[0] == 0) {
            return null;
        }
        return new DataRef(dataRef[0], dataRefType[0], 0);
    }

    private static short GetMediaDataRef(int media, short index, int[] dataRef, int[] dataRefType, int[] dataRefAttributes) {
        return MovieToolbox.GetMediaDataRef(media, index, dataRef, dataRefType, dataRefAttributes);
    }

    private static short GetMovieDefaultDataRef(int movie, int[] dataRef, int[] dataRefType) {
        return MovieToolbox.GetMovieDefaultDataRef(movie, dataRef, dataRefType);
    }

    private static int SGGetDataRef(int sg, int[] dataRef, int[] dataRefType, int[] flags) {
        return SequenceGrabberManager.SGGetDataRef(sg, dataRef, dataRefType, flags);
    }

    private static int GraphicsExportGetInputDataReference(int ge, int[] dataRef, int[] dataRefType) {
        return GraphicsExportManager.GraphicsExportGetInputDataReference(ge, dataRef, dataRefType);
    }

    private static int GraphicsExportGetOutputDataReference(int ge, int[] dataRef, int[] dataRefType) {
        return GraphicsExportManager.GraphicsExportGetOutputDataReference(ge, dataRef, dataRefType);
    }

    private static int GetDataHandler(int dataRef, int dataHandlerSubType, int flags) {
        return ComponentManager.GetDataHandler(dataRef, dataHandlerSubType, flags);
    }

    private static int SGGetOutputDataReference(int sg, int output, int[] dataRef, int[] dataRefType) {
        return SequenceGrabberManager.SGGetOutputDataReference(sg, output, dataRef, dataRefType);
    }

    private static short GetMovieAnchorDataRef(int movie, int[] dataRef, int[] dataRefType, int[] outFlags) {
        return MovieToolbox.GetMovieAnchorDataRef(movie, dataRef, dataRefType, outFlags);
    }
}
