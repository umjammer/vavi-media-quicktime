package quicktime.std.movies.media;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.io.AliasHandle;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
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

    private static Object linkage;

    private int dataRefType;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(DataRef.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * This method returns a copy of a data reference.
     * QuickTime::GraphicsExportGetInputDataReference()
     * @return a copy of a data reference from the graphics exporter component.
     * @return {@link quicktime.QTException}
     * @see quicktime.std.image.GraphicsExporter.getInputDataReference
     */
    public static DataRef fromGraphicsExporterInputDataRef(GraphicsExporter paramGraphicsExporter) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsExportGetInputDataReference(QTObject.ID(paramGraphicsExporter), arrayOfInt1, arrayOfInt2);
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], 0);
    }

    /**
     * This method returns a copy of a data reference.
     * QuickTime::GraphicsExportGetOutputDataReference()
     * @return a copy of a data reference from the graphics exporter component.
     * @return {@link quicktime.QTException}
     * @see quicktime.std.image.GraphicsExporter.getOutputDataReference
     */
    public static DataRef fromGraphicsExporterOutputDataRef(GraphicsExporter paramGraphicsExporter) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsExportGetOutputDataReference(QTObject.ID(paramGraphicsExporter), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], 0);
    }

    /**
     * This method allows your application to get the default data reference for the movie.
     * QuickTime::GetMovieDefaultDataRef()
     * @return a DataRef object specifiying the default data reference for the movie.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Movie#getDefaultDataRef()}
     */
    public static DataRef fromMovie(Movie paramMovie) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GetMovieDefaultDataRef(QTObject.ID(paramMovie), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], 0);
    }

    /**
     * Determines the data reference that is currently assigned to a sequence grabber component.
     * QuickTime::SGGetDataRef
     * @return the dataRef
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.sg.SequenceGrabber#getDataRef()}
     */
    public static DataRef fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int i = SGGetDataRef(QTObject.ID(paramSequenceGrabber), arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], 0, paramSequenceGrabber);
    }

    /**
     * This method returns a copy of a specified data reference.
     * QuickTime::GetMediaDataRef()
     * @param index Identifies the data reference.
     * @return a copy of a specified data reference.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.media.Media#getDataRef(int)}
     */
    public static DataRef fromMedia(Media paramMedia, int paramInt) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        StdQTException.checkError(GetMediaDataRef(QTObject.ID(paramMedia), (short) paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3));
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], arrayOfInt3[0]);
    }

    /**
     * QuickTime::SGGetOutputDataReference(
     * @param sg the sequence grabber that owns this output
     * @param output the output from which to get the data ref from
     * @return the data reference associated with this sequence grabber output.
     * @return {@link quicktime.std.StdQTException}
     */
    public static DataRef fromSGOutput(SequenceGrabber paramSequenceGrabber, SGOutput paramSGOutput) throws StdQTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int i = SGGetOutputDataReference(QTObject.ID(paramSequenceGrabber), QTObject.ID(paramSGOutput), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], 0);
    }

    /**
     * This constructor creates a DataRef that represents any URL.
     * If there is no DataHandler for this URL an invalidDataRef exception is thrown
     * @param url the URL that is used to create a DataReference.
     */
    public DataRef(String paramString) throws QTException {
        super(4, false);
        if (paramString.startsWith("file")) {
            QTFile localQTFile = new QTFile(paramString.substring(7).replace('/', File.separatorChar));
            setAliasHandle(localQTFile.newAlias(true));
        } else {
            setURLDataRef(QTUtils.String2CString(paramString));
        }
    }

    /**
     * This constructor creates a DataRef that represents a QTFile.
     */
    public DataRef(QTFile paramQTFile) throws QTException {
        super(4, false);
        setAliasHandle(paramQTFile.newAlias(true));
    }

    /**
     * This constructor creates a DataRef that represents an AliasHandle.
     * @param a the AliasHandle that is reference by this DataRef.
     */
    public DataRef(AliasHandle paramAliasHandle) throws IOException, QTException {
        super(4, false);
        setAliasHandle(paramAliasHandle);
    }

    /**
     * This constructor creates a DataRef that references the given QTHandle
     * so the dataRefType is a handle DataRef ('hndl').
     * @param handle Specifies the data (a handle) that this reference should point to.
     */
    public DataRef(QTHandleRef paramQTHandleRef) throws QTException {
        super(4, true);
        this.dataRefType = 1752065132;
        this.hdl = paramQTHandleRef;
        setIntAt(0, QTObject.ID(paramQTHandleRef));
    }

    /**
     * This constructor creates a DataRef that references the given QTHandle
     * so the dataRefType is a handle DataRef ('hndl'). Hints can be given to
     * QuickTime (and should if at all possible) about the format of the data
     * that is contained within the handle. Hints can take one of three forms
     *
     * MIME Type	-> kDataRefMimeTypeTag
     * MacOS File Type	-> kDataRefQTFileTypeTag
     * File extension	-> kDataRefFileExtensionTag
     *
     * The preferred usage is to pass in either the MacOS File Type (which will also work on non-MacOS platforms)
     * or the MIME type information. The File extension is a more arbitrary indicator and should be the least
     * referred to. If the hint information is not specified a paramErr exception is thrown
     * @param handle Specifies the data (a handle) that this reference should point to.
     * @param hintFlag describes the hint data that is used
     * @param hintString contains the hint information
     */
    public DataRef(QTHandleRef paramQTHandleRef, int paramInt, String paramString) throws QTException {
        super(4, true);
        this.dataRefType = 1752065132;
        this.hdl = paramQTHandleRef;
        setIntAt(0, QTObject.ID(paramQTHandleRef));
        switch(paramInt) {
            case 1718909296:
                if (QTSession.getQTVersion() == 3)
                    throw new QTException(-50);
                setSize(17);
                setByteAt(4, (byte) 0);
                setIntAt(5, EndianOrder.flipNativeToBigEndian32(12));
                setIntAt(9, EndianOrder.flipNativeToBigEndian32(1718909296));
                setIntAt(13, EndianOrder.flipNativeToBigEndian32(QTUtils.toOSType(paramString)));
                break;
            case 1835625829:
                if (QTSession.getQTVersion() == 3)
                    throw new QTException(-50);
                byte[] arrayOfByte = QTUtils.String2PString(paramString, 255);
                setSize(13 + arrayOfByte.length);
                setByteAt(4, (byte) 0);
                setIntAt(5, EndianOrder.flipNativeToBigEndian32(8 + arrayOfByte.length));
                setIntAt(9, EndianOrder.flipNativeToBigEndian32(1835625829));
                setBytesAt(13, arrayOfByte.length, arrayOfByte, 0);
                break;
            case 1702392864:
                concatenate(new StringHandle("X." + paramString, 2));
                break;
            default:
                throw new QTException(-50);
        }
    }

    private DataRef(int paramInt1, int paramInt2, int paramInt3, Object paramObject) {
        super(paramInt1, paramObject, false);
        this.dataRefType = paramInt2;
        this.dataRefAttributes = paramInt3;
    }

    private DataRef(int paramInt1, int paramInt2, int paramInt3) {
        super(paramInt1, null, false);
        this.dataRefType = paramInt2;
        this.dataRefAttributes = paramInt3;
    }

    private int dataRefAttributes = 0;

    private QTHandleRef hdl = null;

    private void setAliasHandle(AliasHandle paramAliasHandle) throws QTException {
        copyFromHandle(paramAliasHandle);
        this.dataRefType = 1634494835;
    }

    private void setURLDataRef(byte[] paramArrayOfByte) throws QTException {
        setSize(paramArrayOfByte.length);
        copyFromArray(0, paramArrayOfByte, 0, paramArrayOfByte.length);
        this.dataRefType = 1970433056;
        int i = GetDataHandler(_ID(), getType(), 1);
        if (i == 0)
            throw new QTException(63524);
    }

    private void copyFromHandle(QTHandleRef paramQTHandleRef) throws QTException {
        setSize(paramQTHandleRef.getSize());
        copyFromHandle(0, paramQTHandleRef, 0, paramQTHandleRef.getSize());
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
            AliasHandle localAliasHandle = AliasHandle.fromDataRef(this);
            QTFile localQTFile = QTFile.resolveAlias(localAliasHandle);
            return localQTFile.getPath();
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
     * @return {@link quicktime.std.StdQTException}
     */
    public DataRef fromMovieAnchor(Movie paramMovie) throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int[] arrayOfInt3 = { 0 };
        i = GetMovieAnchorDataRef(QTObject.ID(paramMovie), arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);
        if (arrayOfInt1[0] == 0) {
            return null;
        }
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], 0);
    }

    private static short GetMediaDataRef(int paramInt, short paramShort, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        return MovieToolbox.GetMediaDataRef(paramInt, paramShort, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3);
    }

    private static short GetMovieDefaultDataRef(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return MovieToolbox.GetMovieDefaultDataRef(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int SGGetDataRef(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        return SequenceGrabberManager.SGGetDataRef(paramInt, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3);
    }

    private static int GraphicsExportGetInputDataReference(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return ImageCompressionManager.GraphicsExportGetInputDataReference(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int GraphicsExportGetOutputDataReference(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return ImageCompressionManager.GraphicsExportGetOutputDataReference(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int GetDataHandler(int paramInt1, int paramInt2, int paramInt3) {
        return ComponentManager.GetDataHandler(paramInt1, paramInt2, paramInt3);
    }

    private static int SGGetOutputDataReference(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return SequenceGrabberManager.SGGetOutputDataReference(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short GetMovieAnchorDataRef(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        return MovieToolbox.GetMovieAnchorDataRef(paramInt, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3);
    }
}

