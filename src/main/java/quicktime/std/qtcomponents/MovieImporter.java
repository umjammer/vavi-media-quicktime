package quicktime.std.qtcomponents;

import com.apple.mrj.macos.carbon.CarbonAccess;
import quicktime.QTException;
import quicktime.QTNullPointerException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.internal.toolbox.MovieToolbox;
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

    private QTCompDispatcher progUPP;

    /**
     * Looks on the System scrap to find out if it can translate any of the track data into a movie.
     * @see "QuickTime::IsScrapMovie()"
     * @see quicktime.std.movies.Track#isScrapMovie()
     */
    public static MovieImporter fromTrack(Track targetTrack) throws StdQTException {
        int i = IsScrapMovie(QTObject.ID(targetTrack));
        StdQTException.checkError(GetMoviesError());
        return new MovieImporter(i, null);
    }

    private MovieImporter(int nativeRep, Object ref) throws StdQTException {
        super(nativeRep, ref);
    }

    /**
     * Opens the specified movie import component,'eat ', with any subType.
     * @see "QuickTime::OpenADefaultComponent"
     */
    public MovieImporter() throws QTException {
        this(0);
    }

    /**
     * Opens the specified movie import component,'eat ', with the given subType.
     * @see "QuickTime::OpenADefaultComponent"
     * @param subType The movie import component sub type
     */
    public MovieImporter(int subType) throws QTException {
        super(1700885536, subType);
    }

    /**
     * Given a component identifier it opens the specified component. The component type
     * must be 'eat ' or an exception will be thrown as it is not a MovieImporter.
     * @see "QuickTime::OpenAComponent"
     * @param comp the component identifier to open
     */
    public MovieImporter(ComponentIdentifier comp) throws QTException {
        super(comp, 1700885536);
    }

    /**
     * This returns an appropriate instance of the MovieImporter for the DataRef object.
     * @see "QuickTime::GetMovieImporterForDataRef()"
     * @param flags flags that control the kinds of importer components that are searched by this call
     */
    public MovieImporter(DataRef dataRef, int flags) throws QTException {
        this(allocate(dataRef, flags), dontOpenComponent);
    }

    /**
     * This returns an appropriate instance of the MovieImporter for the DataRef object.
     * @see "QuickTime::GetMovieImporterForDataRef()"
     * @param flags flags that control the kinds of importer components that are searched by this call
     * @param fileExt file extension for the source of the data. This is useful to provide the
     *  importer as a guide for it to know about the data format.
     */
    public MovieImporter(DataRef dataRef, int flags, String fileExt) throws QTException {
        this(allocate(dataRef, flags, fileExt), dontOpenComponent);
    }

    private static int allocate(DataRef dataRef, int flags) throws StdQTException {
        int[] importer = { 0 };
        StdQTException.checkError(GetMovieImporterForDataRef(dataRef.getType(), QTObject.ID(dataRef), flags, importer));
        return importer[0];
    }

    private static int allocate(DataRef dataRef, int flags, String extension) throws QTException {
        int[] importer = { 0 };
        StringHandle stringHandle = new StringHandle("X." + extension, 2);
        dataRef.concatenate(stringHandle);
        StdQTException.checkError(GetMovieImporterForDataRef(dataRef.getType(), QTObject.ID(dataRef), flags, importer));
        return importer[0];
    }

    private static final Object dontOpenComponent = new Object();

    /**
     * Allows the MovieToolbox to import data from a file using the movie import component.
     * @see "QuickTime::MovieImportFile"
     * @param fileIn Specifies the source file to the importing data.
     * @param theMovie Specifies the movie to receive the data.
     * @param targetTrack Specifies the track to receive the data, if to be recipent, then inFlags use movieImportMustUseTrack.
     * @param atTime Specifies the start time of the movie time base to place the imported data.
     * @param inFlags Specifies the control flags governing the operation.
     * @return The results pertinent to the operation.
     * @throws quicktime.QTException
     */
    public MovieImportInfo fromFile(QTFile fileIn, Movie theMovie, Track targetTrack, int atTime, int inFlags) throws QTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = theMovie;
        return Track.fromMovieImporterFile(this, fileIn, theMovie, targetTrack, atTime, inFlags);
    }

    /**
     * Allows the MovieToolbox to import data from a handle using the movie import component.
     * @see "QuickTime::MovieImportHandle"
     * @param dataRef Specifies the handle to the importing data.
     * @param theMovie Specifies the movie to receive the data.
     * @param targetTrack Specifies the track to receive the data, if to be recipent, then inFlags use movieImportMustUseTrack.
     * @param atTime Specifies the start time of the movie time base to place the imported data.
     * @param inFlags Specifies the control flags governing the operation.
     * @return The results pertinent to the operation.
     * @throws quicktime.QTException
     */
    public MovieImportInfo fromHandle(QTHandleRef dataRef, Movie theMovie, Track targetTrack, int atTime, int inFlags) throws QTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = theMovie;
        return Track.fromMovieImporterHandle(this, dataRef, theMovie, targetTrack, atTime, inFlags);
    }

    /**
     * Determines information revelant to the movie import component.
     * The info kept are the track used, time value added, and flags describing the operation.
     * @see "QuickTime::MovieImportDataRef"
     * @param dataRef Specifies the data reference.
     * @param theMovie Specifies the movie.
     * @param targetTrack Specifies the track to receive the data, if to be recipent, then inFlags use movieImportMustUseTrack.
     * @param atTime Specifies the start time of the movie time base.
     * @param inFlags Specifies the flags in for the operation.
     * @return The results returned from the query operation.
     * @throws quicktime.QTException
     */
    public MovieImportInfo fromDataRef(DataRef dataRef, Movie theMovie, Track targetTrack, int atTime, int inFlags) throws QTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = theMovie;
        return Track.fromMovieImporterDataRef(this, dataRef, theMovie, targetTrack, atTime, inFlags);
    }

    /**
     * Specifies the duration for the component.
     * @see "QuickTime::MovieImportSetSampleDuration"
     * @param duration Specifies the time base.
     * @param scale Specifies the time base scale.
     * @throws quicktime.std.StdQTException
     */
    public void setSampleDuration(int duration, int scale) throws StdQTException {
        int i = MovieImportSetSampleDuration(_ID(), duration, scale);
        StdQTException.checkError(i);
    }

    /**
     * Specifies the SampleDescription and media type for the component.
     * @see "QuickTime::MovieImportSetSampleDescription"
     * @param desc Specifies the sample description.
     * @param mediaType Specifies the media type.
     * @throws quicktime.std.StdQTException
     */
    public void setSampleDescription(SampleDescription desc, int mediaType) throws StdQTException {
        int i = MovieImportSetSampleDescription(_ID(), QTObject.ID(desc), mediaType);
        StdQTException.checkError(i);
    }

    /**
     * Allows an application to specify a media file that is to receive the imported movie data.
     * @see "QuickTime::MovieImportSetMediaFile"
     * @param alias the media file that is to receive the imported movie data.
     * @throws quicktime.QTException
     */
    public void setMediaFile(AliasHandle alias) throws QTException {
        int i = MovieImportSetMediaFile(_ID(), QTObject.ID(alias));
        StdQTException.checkError(i);
    }

    /**
     * Specifies the dimensions for the component.
     * @see "QuickTime::MovieImportSetDimensions"
     * @param width Specifies the width.
     * @param height Specifies the height.
     * @throws quicktime.std.StdQTException
     */
    public void setDimensions(float width, float height) throws StdQTException {
        int i = MovieImportSetDimensions(_ID(), QTUtils.X2Fix(width), QTUtils.X2Fix(height));
        StdQTException.checkError(i);
    }

    /**
     * Specifies the size to allocate for the component.
     * @see "QuickTime::MovieImportSetChunkSize"
     * @param chunkSize Specifies the size.
     * @throws quicktime.std.StdQTException
     */
    public void setChunkSize(int chunkSize) throws StdQTException {
        int i = MovieImportSetChunkSize(_ID(), chunkSize);
        StdQTException.checkError(i);
    }

    /**
     * This method establishes a MovieProgress object for the MovieImporter
     * that will be called for you by the Movie Toolbox whenever it
     * has to do a lengthy movie import process with this MovieImporter
     * @see "QuickTime::MovieImportSetProgressProc"
     * @param mp the MovieProgress object, or null for no progress object
     * @throws quicktime.std.StdQTException
     */
    public void setProgressProc(MovieProgress mp) throws StdQTException {
        if (mp == null) {
            removeProgressProc();
            return;
        }
        if (this.progUPP != null) {
            removeProgressProc();
        }
        this.progUPP = new QTCompDispatcher(mp);
        int i = MovieImportSetProgressProc(_ID(), this.progUPP.ID(), 0);
        StdQTException.checkError(i);
    }

    /**
     * This method removes a MovieProgress callback for the MovieImporter.
     * @see "QuickTime::MovieImportSetProgressProc"
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
     * @see "QuickTime::MovieImportSetAuxiliaryData"
     * @param data Specifies the data handle.
     * @param handleType Specifies the handle type.
     * @throws quicktime.std.StdQTException
     */
    public void setAuxiliaryData(QTHandleRef data, int handleType) throws StdQTException {
        int i = MovieImportSetAuxiliaryData(_ID(), QTObject.ID(data), handleType);
        StdQTException.checkError(i);
    }

    /**
     * Specifies the scrap for the component.
     * @see "QuickTime::MovieImportSetFromScrap"
     * @param fromScrap Specifies weather to use the scrap manager.
     * @throws quicktime.std.StdQTException
     */
    public void setFromScrap(boolean fromScrap) throws StdQTException {
        byte b = (byte) (fromScrap ? 1 : 0);
        int i = MovieImportSetFromScrap(_ID(), b);
        StdQTException.checkError(i);
    }

    int res = 0;

    /**
     * Allows an application to request that your component display its user dialog box.
     * @see "QuickTime::MovieImportDoUserDialog"
     * @param fileIn Specifies the source file.
     * @return True, if the user canceled the operation, false otherwise.
     * @throws quicktime.QTException
     */
    public boolean doUserDialog(QTFile fileIn) throws QTException {
        byte[] canceled = new byte[1];
        byte[] fsspec = fileIn.getFSSpec(true, 256);
        int i = _ID();
        int j = 0;
        if (QTSession.isCurrentOS(4)) {
            j = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                int i1 = 0;
                synchronized (QTNative.globalsLock) {
                    i1 = MovieImporter.MovieImportDoUserDialog(i1, fsspec, 0, canceled);
                }
                return i1;
            });
            StdQTException.checkError(j);
            return canceled[0] != 0;
        }
        synchronized (QTNative.globalsLock) {
            j = MovieImportDoUserDialog(_ID(), fileIn.getFSSpec(true, 256), 0, canceled);
            StdQTException.checkError(j);
        }
        return canceled[0] != 0;
    }

    /**
     * Specifies the use of.
     * @see "QuickTime::MovieImportDoUserDialog"
     * @param theData Specifies the data handle.
     * @return True, if the user canceled the operation, false otherwise.
     * @throws quicktime.QTException
     */
    public boolean doUserDialog(QTHandleRef theData) throws QTException {
        byte[] canceled = new byte[1];
        synchronized (QTNative.globalsLock) {
            int i = MovieImportDoUserDialog(_ID(), null, QTObject.ID(theData), canceled);
            StdQTException.checkError(i);
        }
        return canceled[0] != 0;
    }

    /**
     * Specifies the duration for the component.
     * @see "QuickTime::MovieImportSetDuration"
     * @param duration Specifies the time base duration.
     * @throws quicktime.std.StdQTException
     */
    public void setDuration(int duration) throws StdQTException {
        int i = MovieImportSetDuration(_ID(), duration);
        StdQTException.checkError(i);
    }

    /**
     * Determines the data type for the component.
     * @see "QuickTime::MovieImportGetAuxiliaryDataType"
     * @return The handle type.
     * @throws quicktime.std.StdQTException
     */
    public int getAuxiliaryDataType() throws StdQTException {
        int[] auxType = new int[1];
        int i = MovieImportGetAuxiliaryDataType(_ID(), auxType);
        StdQTException.checkError(i);
        return auxType[0];
    }

    /**
     * Determines the validity of the file to be imported.
     * @see "QuickTime::MovieImportValidate"
     * @param fileIn Specifies the source file.
     * @return True, if the handle data or file from stream is valid, otherwise false.
     * @throws quicktime.QTException
     */
    public boolean validate(QTFile fileIn) throws QTException {
        byte[] valid = new byte[1];
        int i = MovieImportValidate(_ID(), fileIn.getFSSpec(true, 256), 0, valid);
        StdQTException.checkError(i);
        return valid[0] != 0;
    }

    /**
     * Determines the validity of the data in the handle to be imported.
     * @see "QuickTime::MovieImportValidate"
     * @param theData Specifies the data handle.
     * @return True, if the handle data or file from stream is valid, otherwise false.
     * @throws quicktime.QTException
     */
    public boolean validate(QTHandleRef theData) throws QTException {
        byte[] valid = new byte[1];
        int i = MovieImportValidate(_ID(), null, QTObject.ID(theData), valid);
        StdQTException.checkError(i);
        return valid[0] != 0;
    }

    /**
     * Determines the file type for the component.
     * @see "QuickTime::MovieImportGetFileType"
     * @return The file type.
     * @throws quicktime.std.StdQTException
     */
    public int getFileType() throws StdQTException {
        int[] fileType = new int[1];
        int i = MovieImportGetFileType(_ID(), fileType);
        StdQTException.checkError(i);
        return fileType[0];
    }

    /**
     * Determines the sample description of the component.
     * @see "QuickTime::MovieImportGetSampleDescription"
     * @return The sample description.
     * @throws quicktime.std.StdQTException
     */
    public SampleDescription getSampleDescription() throws StdQTException {
        return SampleDescription.fromMovieImporter(this);
    }

    /**
     * Determines the media type of the component.
     * @see "QuickTime::MovieImportGetSampleDescription"
     * @return The media type.
     * @throws quicktime.std.StdQTException
     */
    public int getMediaType() throws StdQTException {
        int[] desc = new int[1];
        int[] mediaType = new int[1];
        int i = MovieImportGetSampleDescription(_ID(), desc, mediaType);
        StdQTException.checkError(i);
        return mediaType[0];
    }

    /**
     * Returns a list of MIME types supported by the movie import component.
     * @see "QuickTime::MovieImportGetMIMETypeList"
     * @return an AtomContainer that contains a list of MIME types
     * @throws quicktime.std.StdQTException
     */
    public AtomContainer getMIMETypeList() throws StdQTException {
        return AtomContainer.fromMovieImporterMIME(this);
    }

    /**
     * NO QT DOCUMENTATION.
     * @see "QuickTime::MovieImportSetOffsetAndLimit"
     * @param offset the offset within the source data from which to start importing
     * @param limit how many bytes from the offset to import.
     * @throws quicktime.std.StdQTException
     */
    public void setOffsetAndLimit(int offset, int limit) throws StdQTException {
        StdQTException.checkError(MovieImportSetOffsetAndLimit(_ID(), offset, limit));
    }

    /**
     * Sets the movie import componentÕs current configuration from the passed settings data.
     * @see "QuickTime::MovieImportSetSettingsFromAtomContainer"
     */
    public void setImportSettingsFromAtomContainer(AtomContainer importSettingsFromAtomContainer) throws StdQTException {
        int i = MovieImportSetSettingsFromAtomContainer(_ID(), QTObject.ID(importSettingsFromAtomContainer));
        StdQTException.checkError(i);
    }

    /**
     * Retrieves the current settings from the movie import component..
     * @see "QuickTime::MovieImportGetSettingsAsAtomContainer"
     * @return an AtomContainer with the current settings.
     * @throws quicktime.std.StdQTException
     */
    public AtomContainer getImportSettingsFromAtomContainer() throws StdQTException {
        return AtomContainer.fromMovieImporterSettings(this);
    }

    /**
     * Implemented by a movie import component to determine the flags originally passed to NewMovieFromDataRef and from a file.
     * @see "QuickTime::MovieImportSetNewMovieFlags"
     * @param newMovieFlags Flags that control the characteristics of the new imported movie.
     * @throws quicktime.std.StdQTException
     */
    public void setNewMovieFlags(int newMovieFlags) throws StdQTException {
        int i = MovieImportSetNewMovieFlags(_ID(), newMovieFlags);
        StdQTException.checkError(i);
    }

    @Override
    protected void _dispose() throws QTException {
        try {
            MovieImportSetProgressProc(_ID(), 0, 0);
        } catch (QTNullPointerException e) {
        }
        if (this.progUPP != null)
            this.progUPP.cleanupMethodClosure();
        this.progUPP = null;
    }

    private static int IsScrapMovie(int targetTrack) {
        return MovieToolbox.IsScrapMovie(targetTrack);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static short GetMovieImporterForDataRef(int dataRefType, int dataRef, int flags, int[] importer) {
        return MovieToolbox.GetMovieImporterForDataRef(dataRefType, dataRef, flags, importer);
    }

    private static int MovieImportSetSampleDuration(int ci, int duration, int scale) {
        return ComponentManager.MovieImportSetSampleDuration(ci, duration, scale);
    }

    private static int MovieImportSetSampleDescription(int ci, int desc, int mediaType) {
        return ComponentManager.MovieImportSetSampleDescription(ci, desc, mediaType);
    }

    private static int MovieImportSetMediaFile(int ci, int alias) {
        return ComponentManager.MovieImportSetMediaFile(ci, alias);
    }

    private static int MovieImportSetDimensions(int ci, int width, int height) {
        return ComponentManager.MovieImportSetDimensions(ci, width, height);
    }

    private static int MovieImportSetChunkSize(int ci, int chunkSize) {
        return ComponentManager.MovieImportSetChunkSize(ci, chunkSize);
    }

    private static int MovieImportSetProgressProc(int ci, int proc, int refCon) {
        return ComponentManager.MovieImportSetProgressProc(ci, proc, refCon);
    }

    private static int MovieImportSetAuxiliaryData(int ci, int data, int handleType) {
        return ComponentManager.MovieImportSetAuxiliaryData(ci, data, handleType);
    }

    private static int MovieImportSetFromScrap(int ci, byte fromScrap) {
        return ComponentManager.MovieImportSetFromScrap(ci, fromScrap);
    }

    private static int MovieImportDoUserDialog(int ci, byte[] fsspec, int h, byte[] canceled) {
        return ComponentManager.MovieImportDoUserDialog(ci, fsspec, h, canceled);
    }

    private static int MovieImportSetDuration(int ci, int duration) {
        return ComponentManager.MovieImportSetDuration(ci, duration);
    }

    private static int MovieImportGetAuxiliaryDataType(int ci, int[] auxType) {
        return ComponentManager.MovieImportGetAuxiliaryDataType(ci, auxType);
    }

    private static int MovieImportValidate(int ci, byte[] fsspec, int h, byte[] valid) {
        return ComponentManager.MovieImportValidate(ci, fsspec, h, valid);
    }

    private static int MovieImportGetFileType(int ci, int[] fileType) {
        return ComponentManager.MovieImportGetFileType(ci, fileType);
    }

    private static int MovieImportGetSampleDescription(int ci, int[] desc, int[] mediaType) {
        return ComponentManager.MovieImportGetSampleDescription(ci, desc, mediaType);
    }

    private static int MovieImportSetOffsetAndLimit(int ci, int offset, int limit) {
        return ComponentManager.MovieImportSetOffsetAndLimit(ci, offset, limit);
    }

    private static int MovieImportSetSettingsFromAtomContainer(int ci, int settings) {
        return ComponentManager.MovieImportSetSettingsFromAtomContainer(ci, settings);
    }

    private static int MovieImportSetNewMovieFlags(int ci, int flags) {
        return ComponentManager.MovieImportSetNewMovieFlags(ci, flags);
    }
}
