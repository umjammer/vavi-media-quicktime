package quicktime.std.qtcomponents;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTNullPointerException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.io.AliasHandle;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieProgress;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.SampleDescription;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;
import quicktime.util.StringHandle;

/**
 * Assists in importing movie data.
 * The MovieImporter implements methods for a corresponding QuickTime
 * ComponentInstance structure.
 * Refer to Movie Data Exchange in QuickTime
 */
public final class MovieImporter extends Component implements QuickTimeLib {

    private static Object linkage;

    private QTCompDispatcher progUPP;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(MovieExporter.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Looks on the System scrap to find out if it can translate any of the track data into a movie.
     * QuickTime::IsScrapMovie()
     * @see {@link quicktime.std.movies.Track#isScrapMovie()}
     */
    public static MovieImporter fromTrack(Track paramTrack) throws StdQTException {
        int i = IsScrapMovie(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return new MovieImporter(i, null);
    }

    private MovieImporter(int paramInt, Object paramObject) throws StdQTException {
        super(paramInt, paramObject);
    }

    /**
     * Opens the specified movie import component,'eat ', with any subType.
     * QuickTime::OpenADefaultComponent
     */
    public MovieImporter() throws QTException {
        this(0);
    }

    /**
     * Opens the specified movie import component,'eat ', with the given subType.
     * QuickTime::OpenADefaultComponent
     * @param subType The movie import component sub type
     */
    public MovieImporter(int paramInt) throws QTException {
        super(1700885536, paramInt);
    }

    /**
     * Given a component identifier it opens the specified component. The component type
     * must be 'eat ' or an exception will be thrown as it is not a MovieImporter.
     * QuickTime::OpenAComponent
     * @param comp the component identifier to open
     */
    public MovieImporter(ComponentIdentifier paramComponentIdentifier) throws QTException {
        super(paramComponentIdentifier, 1700885536);
    }

    /**
     * This returns an appropriate instance of the MovieImporter for the DataRef object.
     * QuickTime::GetMovieImporterForDataRef()
     * @param flags flags that control the kinds of importer components that are searched by this call
     */
    public MovieImporter(DataRef paramDataRef, int paramInt) throws QTException {
        this(allocate(paramDataRef, paramInt), dontOpenComponent);
    }

    /**
     * This returns an appropriate instance of the MovieImporter for the DataRef object.
     * QuickTime::GetMovieImporterForDataRef()
     * @param flags flags that control the kinds of importer components that are searched by this call
     * @param fileExt file extension for the source of the data. This is useful to provide the
     *  importer as a guide for it to know about the data format.
     */
    public MovieImporter(DataRef paramDataRef, int paramInt, String paramString) throws QTException {
        this(allocate(paramDataRef, paramInt, paramString), dontOpenComponent);
    }

    private static int allocate(DataRef paramDataRef, int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(GetMovieImporterForDataRef(paramDataRef.getType(), QTObject.ID(paramDataRef), paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    private static int allocate(DataRef paramDataRef, int paramInt, String paramString) throws QTException {
        int[] arrayOfInt = { 0 };
        StringHandle localStringHandle = new StringHandle("X." + paramString, 2);
        paramDataRef.concatenate(localStringHandle);
        StdQTException.checkError(GetMovieImporterForDataRef(paramDataRef.getType(), QTObject.ID(paramDataRef), paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    private static Object dontOpenComponent = new Object();

    /**
     * Allows the MovieToolbox to import data from a file using the movie import component.
     * QuickTime::MovieImportFile
     * @param fileIn Specifies the source file to the importing data.
     * @param theMovie Specifies the movie to receive the data.
     * @param targetTrack Specifies the track to receive the data, if to be recipent, then inFlags use movieImportMustUseTrack.
     * @param atTime Specifies the start time of the movie time base to place the imported data.
     * @param inFlags Specifies the control flags governing the operation.
     * @return The results pertinent to the operation.
     * @return {@link quicktime.QTException}
     */
    public MovieImportInfo fromFile(QTFile paramQTFile, Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        return Track.fromMovieImporterFile(this, paramQTFile, paramMovie, paramTrack, paramInt1, paramInt2);
    }

    /**
     * Allows the MovieToolbox to import data from a handle using the movie import component.
     * QuickTime::MovieImportHandle
     * @param dataRef Specifies the handle to the importing data.
     * @param theMovie Specifies the movie to receive the data.
     * @param targetTrack Specifies the track to receive the data, if to be recipent, then inFlags use movieImportMustUseTrack.
     * @param atTime Specifies the start time of the movie time base to place the imported data.
     * @param inFlags Specifies the control flags governing the operation.
     * @return The results pertinent to the operation.
     * @return {@link quicktime.QTException}
     */
    public MovieImportInfo fromHandle(QTHandleRef paramQTHandleRef, Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        return Track.fromMovieImporterHandle(this, paramQTHandleRef, paramMovie, paramTrack, paramInt1, paramInt2);
    }

    /**
     * Determines information revelant to the movie import component.
     * The info kept are the track used, time value added, and flags describing the operation.
     * QuickTime::MovieImportDataRef
     * @param dataRef Specifies the data reference.
     * @param theMovie Specifies the movie.
     * @param targetTrack Specifies the track to receive the data, if to be recipent, then inFlags use movieImportMustUseTrack.
     * @param atTime Specifies the start time of the movie time base.
     * @param inFlags Specifies the flags in for the operation.
     * @return The results returned from the query operation.
     * @return {@link quicktime.QTException}
     */
    public MovieImportInfo fromDataRef(DataRef paramDataRef, Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        return Track.fromMovieImporterDataRef(this, paramDataRef, paramMovie, paramTrack, paramInt1, paramInt2);
    }

    /**
     * Specifies the duration for the component.
     * QuickTime::MovieImportSetSampleDuration
     * @param duration Specifies the time base.
     * @param scale Specifies the time base scale.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSampleDuration(int paramInt1, int paramInt2) throws StdQTException {
        int i = MovieImportSetSampleDuration(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * Specifies the SampleDescription and media type for the component.
     * QuickTime::MovieImportSetSampleDescription
     * @param desc Specifies the sample description.
     * @param mediaType Specifies the media type.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSampleDescription(SampleDescription paramSampleDescription, int paramInt) throws StdQTException {
        int i = MovieImportSetSampleDescription(_ID(), QTObject.ID(paramSampleDescription), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Allows an application to specify a media file that is to receive the imported movie data.
     * QuickTime::MovieImportSetMediaFile
     * @param alias the media file that is to receive the imported movie data.
     * @param {@link quicktime.QTException}
     */
    public void setMediaFile(AliasHandle paramAliasHandle) throws QTException {
        int i = MovieImportSetMediaFile(_ID(), QTObject.ID(paramAliasHandle));
        StdQTException.checkError(i);
    }

    /**
     * Specifies the dimensions for the component.
     * QuickTime::MovieImportSetDimensions
     * @param width Specifies the width.
     * @param height Specifies the height.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDimensions(float paramFloat1, float paramFloat2) throws StdQTException {
        int i = MovieImportSetDimensions(_ID(), QTUtils.X2Fix(paramFloat1), QTUtils.X2Fix(paramFloat2));
        StdQTException.checkError(i);
    }

    /**
     * Specifies the size to allocate for the component.
     * QuickTime::MovieImportSetChunkSize
     * @param chunkSize Specifies the size.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setChunkSize(int paramInt) throws StdQTException {
        int i = MovieImportSetChunkSize(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * This method establishes a MovieProgress object for the MovieImporter
     * that will be called for you by the Movie Toolbox whenever it
     * has to do a lengthy movie import process with this MovieImporter
     * QuickTime::MovieImportSetProgressProc
     * @param mp the MovieProgress object, or null for no progress object
     * @param {@link quicktime.std.StdQTException}
     */
    public void setProgressProc(MovieProgress paramMovieProgress) throws StdQTException {
        if (paramMovieProgress == null) {
            removeProgressProc();
            return;
        }
        if (this.progUPP != null) {
            removeProgressProc();
        }
        this.progUPP = new QTCompDispatcher(paramMovieProgress);
        int i = MovieImportSetProgressProc(_ID(), this.progUPP.ID(), 0);
        StdQTException.checkError(i);
    }

    /**
     * This method removes a MovieProgress callback for the MovieImporter.
     * QuickTime::MovieImportSetProgressProc
     */
    public void removeProgressProc() throws StdQTException {
        int i = MovieImportSetProgressProc(_ID(), 0, 0);
        if (this.progUPP != null) {
            this.progUPP.cleanupMethodClosure();
            this.progUPP = null;
        }
        StdQTException.checkError(i);
    }

    /**
     * Specifies the handle and its type for the component.
     * QuickTime::MovieImportSetAuxiliaryData
     * @param data Specifies the data handle.
     * @param handleType Specifies the handle type.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setAuxiliaryData(QTHandleRef paramQTHandleRef, int paramInt) throws StdQTException {
        int i = MovieImportSetAuxiliaryData(_ID(), QTObject.ID(paramQTHandleRef), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Specifies the scrap for the component.
     * QuickTime::MovieImportSetFromScrap
     * @param fromScrap Specifies weather to use the scrap manager.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setFromScrap(boolean paramBoolean) throws StdQTException {
        byte b = (byte) (paramBoolean ? 1 : 0);
        int i = MovieImportSetFromScrap(_ID(), b);
        StdQTException.checkError(i);
    }

    int res = 0;

    /**
     * Allows an application to request that your component display its user dialog box.
     * QuickTime::MovieImportDoUserDialog
     * @param fileIn Specifies the source file.
     * @return True, if the user canceled the operation, false otherwise.
     * @return {@link quicktime.QTException}
     */
    public boolean doUserDialog(QTFile paramQTFile) throws QTException {
        final byte[] arrayOfByte1 = new byte[1];
        final byte[] arrayOfByte2 = paramQTFile.getFSSpec(true, 256);
        final int i = _ID();
        int j = 0;
        if (QTSession.isCurrentOS(4)) {
            j = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                public Object invoke() {
                    int i = 0;
                    synchronized (QTNative.globalsLock) {
                        i = MovieImporter.MovieImportDoUserDialog(i, arrayOfByte2, 0, arrayOfByte1);
                    }
                    return new Integer(i);
                }
            })).intValue();
            StdQTException.checkError(j);
            return arrayOfByte1[0] != 0;
        }
        synchronized (QTNative.globalsLock) {
            j = MovieImportDoUserDialog(_ID(), paramQTFile.getFSSpec(true, 256), 0, arrayOfByte1);
            StdQTException.checkError(j);
        }
        return arrayOfByte1[0] != 0;
    }

    /**
     * Specifies the use of.
     * QuickTime::MovieImportDoUserDialog
     * @param theData Specifies the data handle.
     * @return True, if the user canceled the operation, false otherwise.
     * @return {@link quicktime.QTException}
     */
    public boolean doUserDialog(QTHandleRef paramQTHandleRef) throws QTException {
        byte[] arrayOfByte = new byte[1];
        synchronized (QTNative.globalsLock) {
            int i = MovieImportDoUserDialog(_ID(), null, QTObject.ID(paramQTHandleRef), arrayOfByte);
            StdQTException.checkError(i);
        }
        return arrayOfByte[0] != 0;
    }

    /**
     * Specifies the duration for the component.
     * QuickTime::MovieImportSetDuration
     * @param duration Specifies the time base duration.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDuration(int paramInt) throws StdQTException {
        int i = MovieImportSetDuration(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Determines the data type for the component.
     * QuickTime::MovieImportGetAuxiliaryDataType
     * @return The handle type.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getAuxiliaryDataType() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieImportGetAuxiliaryDataType(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Determines the validity of the file to be imported.
     * QuickTime::MovieImportValidate
     * @param fileIn Specifies the source file.
     * @return True, if the handle data or file from stream is valid, otherwise false.
     * @return {@link quicktime.QTException}
     */
    public boolean validate(QTFile paramQTFile) throws QTException {
        byte[] arrayOfByte = new byte[1];
        int i = MovieImportValidate(_ID(), paramQTFile.getFSSpec(true, 256), 0, arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * Determines the validity of the data in the handle to be imported.
     * QuickTime::MovieImportValidate
     * @param theData Specifies the data handle.
     * @return True, if the handle data or file from stream is valid, otherwise false.
     * @return {@link quicktime.QTException}
     */
    public boolean validate(QTHandleRef paramQTHandleRef) throws QTException {
        byte[] arrayOfByte = new byte[1];
        int i = MovieImportValidate(_ID(), null, QTObject.ID(paramQTHandleRef), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * Determines the file type for the component.
     * QuickTime::MovieImportGetFileType
     * @return The file type.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getFileType() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieImportGetFileType(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Determines the sample description of the component.
     * QuickTime::MovieImportGetSampleDescription
     * @return The sample description.
     * @return {@link quicktime.std.StdQTException}
     */
    public SampleDescription getSampleDescription() throws StdQTException {
        return SampleDescription.fromMovieImporter(this);
    }

    /**
     * Determines the media type of the component.
     * QuickTime::MovieImportGetSampleDescription
     * @return The media type.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getMediaType() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = MovieImportGetSampleDescription(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    /**
     * Returns a list of MIME types supported by the movie import component.
     * QuickTime::MovieImportGetMIMETypeList
     * @return an AtomContainer that contains a list of MIME types
     * @return {@link quicktime.std.StdQTException}
     */
    public AtomContainer getMIMETypeList() throws StdQTException {
        return AtomContainer.fromMovieImporterMIME(this);
    }

    /**
     * NO QT DOCUMENTATION.
     * QuickTime::MovieImportSetOffsetAndLimit
     * @param offset the offset within the source data from which to start importing
     * @param limit how many bytes from the offset to import.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setOffsetAndLimit(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(MovieImportSetOffsetAndLimit(_ID(), paramInt1, paramInt2));
    }

    /**
     * Sets the movie import componentÕs current configuration from the passed settings data.
     * QuickTime::MovieImportSetSettingsFromAtomContainer
     */
    public void setImportSettingsFromAtomContainer(AtomContainer paramAtomContainer) throws StdQTException {
        int i = MovieImportSetSettingsFromAtomContainer(_ID(), QTObject.ID(paramAtomContainer));
        StdQTException.checkError(i);
    }

    /**
     * Retrieves the current settings from the movie import component..
     * QuickTime::MovieImportGetSettingsAsAtomContainer
     * @return an AtomContainer with the current settings.
     * @return {@link quicktime.std.StdQTException}
     */
    public AtomContainer getImportSettingsFromAtomContainer() throws StdQTException {
        return AtomContainer.fromMovieImporterSettings(this);
    }

    /**
     * Implemented by a movie import component to determine the flags originally passed to NewMovieFromDataRef and from a file.
     * QuickTime::MovieImportSetNewMovieFlags
     * @param newMovieFlags Flags that control the characteristics of the new imported movie.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setNewMovieFlags(int paramInt) throws StdQTException {
        int i = MovieImportSetNewMovieFlags(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    protected void _dispose() throws QTException {
        try {
            MovieImportSetProgressProc(_ID(), 0, 0);
        } catch (QTNullPointerException localQTNullPointerException) {
        }
        if (this.progUPP != null)
            this.progUPP.cleanupMethodClosure();
        this.progUPP = null;
    }

    private static native int IsScrapMovie(int paramInt);

    private static native short GetMoviesError();

    private static native short GetMovieImporterForDataRef(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native int MovieImportSetSampleDuration(int paramInt1, int paramInt2, int paramInt3);

    private static native int MovieImportSetSampleDescription(int paramInt1, int paramInt2, int paramInt3);

    private static native int MovieImportSetMediaFile(int paramInt1, int paramInt2);

    private static native int MovieImportSetDimensions(int paramInt1, int paramInt2, int paramInt3);

    private static native int MovieImportSetChunkSize(int paramInt1, int paramInt2);

    private static native int MovieImportSetProgressProc(int paramInt1, int paramInt2, int paramInt3);

    private static native int MovieImportSetAuxiliaryData(int paramInt1, int paramInt2, int paramInt3);

    private static native int MovieImportSetFromScrap(int paramInt, byte paramByte);

    private static native int MovieImportDoUserDialog(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2);

    private static native int MovieImportSetDuration(int paramInt1, int paramInt2);

    private static native int MovieImportGetAuxiliaryDataType(int paramInt, int[] paramArrayOfInt);

    private static native int MovieImportValidate(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2);

    private static native int MovieImportGetFileType(int paramInt, int[] paramArrayOfInt);

    private static native int MovieImportGetSampleDescription(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int MovieImportSetOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3);

    private static native int MovieImportSetSettingsFromAtomContainer(int paramInt1, int paramInt2);

    private static native int MovieImportSetNewMovieFlags(int paramInt1, int paramInt2);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/MovieImporter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
