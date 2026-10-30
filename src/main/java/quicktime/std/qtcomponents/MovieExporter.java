package quicktime.std.qtcomponents;

import com.apple.mrj.macos.carbon.CarbonAccess;
import quicktime.QTException;
import quicktime.QTNullPointerException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.ComponentManager;
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
import quicktime.util.QTHandle;
import quicktime.util.QTUtils;


/**
 * Assists in exporting movie data and data conversion operations.
 * The MovieDataImportComponent implements methods for a corresponding QuickTime
 * ComponentInstance structure.
 * refer to Movie Data Exchange in QuickTime
 */
public final class MovieExporter extends Component implements QuickTimeLib {

    private QTCompDispatcher progUPP;

    /**
     * Opens the specified movie export component,'spit', with the given subType.
     * @see "QuickTime::OpenADefaultComponent"
     * @param subType The movie export component sub type
     */
    public MovieExporter(int subType) throws QTException {
        super(1936746868, subType);
        setProgressProc(null);
    }

    /**
     * Given a component identifier it opens the specified component. The component type
     * must be 'spit' or an exception will be thrown as it is not a MovieExporter.
     * This constructor allows an application to find the appropriate export component
     * and then have that component opened.
     * @see "QuickTime::OpenAComponent"
     * @param comp the component identifier to open
     */
    public MovieExporter(ComponentIdentifier comp) throws QTException {
        super(comp, 1936746868);
        setProgressProc(null);
    }

    /**
     * This method establishes a MovieProgress object for the MovieExporter
     * that will be called for you by the Movie Toolbox whenever it
     * has to do a lengthy movie export process with this MovieExporter
     * @see "QuickTime::MovieExportSetProgressProc"
     * @param mp the MovieProgress object, or null for no progress object
     * @throws quicktime.std.StdQTException
     */
    public void setProgressProc(MovieProgress mp) throws StdQTException {
        int i = 0;
        if (this.progUPP != null)
            this.progUPP.cleanupMethodClosure();
        if (mp != null) {
            this.progUPP = new QTCompDispatcher(mp);
        } else {
            this.progUPP = new QTCompDispatcher(this);
        }
        synchronized (QTNative.globalsLock) {
            i = MovieExportSetProgressProc(_ID(), this.progUPP.ID(), 0);
        }
        StdQTException.checkError(i);
    }

    /**
     * This method is used to export movie data into a file.
     * @see "QuickTime::MovieExportToFile"
     * @param theFile the file that is to receive the converted movie data
     * @param theMovie specifies the movie to export.
     * @param onlyThisTrack Specifies the track used to reference.
     * @param startTime Specifies the start time value of the time base.
     * @param duration Specifies the elapsed time value of the timebase.
     * @return The file containing the converted movie data.
     * @throws quicktime.QTException
     */
    public void toFile(QTFile theFile, Movie theMovie, Track onlyThisTrack, int startTime, int duration) throws QTException {
        int i = 0;
        if (this.progUPP != null) {
            this.progUPP.theCurrentMov = theMovie;
        }
        synchronized (QTNative.globalsLock) {
            i = MovieExportToFile(_ID(), theFile.getFSSpec(true, 512), QTObject.ID(theMovie), QTObject.ID(onlyThisTrack), startTime, duration);
        }
        StdQTException.checkError(i);
    }

    /**
     * Exports the movie's track data into a handle, returning the handle the data is exported too.
     * @see "QuickTime::MovieExportToHandle"
     * @param theMovie Specifies the movie.
     * @param onlyThisTrack Specifies the track used to reference.
     * @param startTime Specifies the start time of the time base.
     * @param duration Specifies the time value of the timebase.
     * @return The handle containing the converted movie data.
     * @throws quicktime.QTException
     */
    public QTHandle toHandle(Movie theMovie, Track onlyThisTrack, int startTime, int duration) throws QTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = theMovie;
        QTHandle h = new QTHandle();
        int i = MovieExportToHandle(_ID(), QTObject.ID(h), QTObject.ID(theMovie), QTObject.ID(onlyThisTrack), startTime, duration);
        StdQTException.checkError(i);
        return h;
    }

    /**
     * Exports the movie's track data to the specified data reference for the movie export component.
     * Determines and opens the component to use for the given data reference and data type.
     * @see "QuickTime::MovieExportToDataRef"
     * @param theMovie Specifies the movie.
     * @param duration Specifies the time value of the timebase.
     * @return The handle containing the converted movie data.
     * @throws quicktime.std.StdQTException
     */
    public void toDataRef(DataRef dataRef, Movie theMovie, Track onlyThisTrack, int startTime, int duration) throws StdQTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = theMovie;
        StdQTException.checkError(MovieExportToDataRef(_ID(), QTObject.ID(dataRef), dataRef.getType(), QTObject.ID(theMovie), QTObject.ID(onlyThisTrack), startTime, duration));
    }

    /**
     * Determines the data type for the component.
     * @see "QuickTime::MovieExportGetAuxiliaryData"
     * @param dataH Specifies the data handle.
     * @return The handle type.
     * @throws quicktime.std.StdQTException
     */
    public int getAuxiliaryData(QTHandle dataH) throws StdQTException {
        int[] handleType = new int[1];
        int i = MovieExportGetAuxiliaryData(_ID(), QTObject.ID(dataH), handleType);
        StdQTException.checkError(i);
        return handleType[0];
    }

    /**
     * Specifies the SampleDescription and media type for the component.
     * @see "QuickTime::MovieExportSetSampleDescription"
     * @param desc Specifies the sample description.
     * @param mediaType Specifies the media type.
     * @throws quicktime.std.StdQTException
     */
    public void setSampleDescription(SampleDescription desc, int mediaType) throws StdQTException {
        int i = MovieExportSetSampleDescription(_ID(), QTObject.ID(desc), mediaType);
        StdQTException.checkError(i);
    }

    /**
     * Determines the data reference handle for the movie export component.
     * @see "QuickTime::MovieExportDoUserDialog"
     * @param theMovie Specifies the movie.
     * @param onlyThisTrack Specifies the track used to reference.
     * @param startTime Specifies the start time of the time base.
     * @param duration Specifies the time value of the timebase.
     * @return True, if the dialog operation was canceled, otherwise false.
     * @throws quicktime.std.StdQTException
     */
    public boolean doUserDialog(Movie theMovie, Track onlyThisTrack, int startTime, int duration) throws StdQTException {
        byte[] canceled = new byte[1];
        Movie movie = theMovie;
        Track track = onlyThisTrack;
        int i = startTime;
        int j = duration;
        int k = _ID();
        int m = 0;
        if (QTSession.isCurrentOS(4)) {
            m = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                int l = 0;
                synchronized (QTNative.globalsLock) {
                    l = MovieExporter.MovieExportDoUserDialog(k, QTObject.ID(movie), QTObject.ID(track), i, j, canceled);
                }
                return l;
            });
            StdQTException.checkError(m);
        } else {
            synchronized (QTNative.globalsLock) {
                m = MovieExportDoUserDialog(_ID(), QTObject.ID(theMovie), QTObject.ID(onlyThisTrack), startTime, duration, canceled);
                StdQTException.checkError(m);
            }
        }
        return canceled[0] != 0;
    }

    /**
     * Determines the file type creator for the component.
     * @see "QuickTime::MovieExportGetCreatorType"
     * @return The creator file type.
     * @throws quicktime.std.StdQTException
     */
    public int getCreatorType() throws StdQTException {
        int[] creator = new int[1];
        int i = MovieExportGetCreatorType(_ID(), creator);
        StdQTException.checkError(i);
        return creator[0];
    }

    /**
     * Use this method to determine whether the movie or track could be exported by this exporter.
     * @see "QuickTime::MovieExportValidate"
     * @param theMovie the movie for this operation.
     * @param onlyThisTrack identifies a track that is to be converted or null. If supplied the component
     *  will only convert the specified track.
     * @return true if the export operation can occur, otherwise false.
     * @throws quicktime.std.StdQTException
     */
    public boolean validate(Movie theMovie, Track onlyThisTrack) throws StdQTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = theMovie;
        byte[] valid = { 0 };
        StdQTException.checkError(MovieExportValidate(_ID(), QTObject.ID(theMovie), QTObject.ID(onlyThisTrack), valid));
        return valid[0] != 0;
    }

    /**
     * Retrieves the current settings from the movie export component..
     * @see "QuickTime::MovieExportGetSettingsAsAtomContainer"
     * @return an AtomContainer with the current settings.
     * @throws quicktime.std.StdQTException
     */
    public AtomContainer getExportSettingsFromAtomContainer() throws StdQTException {
        return AtomContainer.fromMovieExporter(this);
    }

    /**
     * Sets the movie export componentÕs current configuration from the passed settings data.
     * @see "QuickTime::MovieExportSetSettingsFromAtomContainer"
     */
    public void setExportSettingsFromAtomContainer(AtomContainer exportSettingsFromAtomContainer) throws StdQTException {
        int i = MovieExportSetSettingsFromAtomContainer(_ID(), QTObject.ID(exportSettingsFromAtomContainer));
        StdQTException.checkError(i);
    }

    /**
     * Gets the movie export components current FileNameExtension.
     * @see "QuickTime::MovieExportGetFileNameExtension"
     * @return and ostype of the current filename extension.
     * @throws quicktime.std.StdQTException
     */
    public int getExportFileNameExtension() throws StdQTException {
        int[] extension = new int[1];
        int i = MovieExportGetFileNameExtension(_ID(), extension);
        StdQTException.checkError(i);
        return extension[0];
    }

    /**
     * No Documentation.
     * @see "QuickTime::MovieExportGetShortFileTypeString"
     * @return a java string.
     * @throws quicktime.std.StdQTException
     */
    public String getExportShortFileTypeString() throws StdQTException {
        byte[] typeString = new byte[256];
        int i = MovieExportGetShortFileTypeString(_ID(), typeString);
        StdQTException.checkError(i);
        return QTUtils.PString2String(typeString, 0);
    }

    /**
     * This routine returns an OSType value through its mediaType parameter, which
     * is interpreted in exactly the same way that the componentManufacturer was
     * previously interpreted. If the export component requires a particular
     * type of track to exist in a movie, it returns that media handler type
     * (e.g., VideoMediaType, SoundMediaType, etc.)
     * @see "QuickTime::MovieExportGetSourceMediaType"
     * @return and ostype.
     * @throws quicktime.std.StdQTException
     */
    public int getExportSourceMediaType() throws StdQTException {
        int[] mediaType = new int[1];
        int i = MovieExportGetSourceMediaType(_ID(), mediaType);
        StdQTException.checkError(i);
        return mediaType[0];
    }

    @Override
    protected void _dispose() throws QTException {
        try {
            synchronized (QTNative.globalsLock) {
                MovieExportSetProgressProc(_ID(), 0, 0);
            }
        } catch (QTNullPointerException e) {
        }
        if (this.progUPP != null)
            this.progUPP.cleanupMethodClosure();
        this.progUPP = null;
    }

    private static int MovieExportToHandle(int ci, int h, int theMovie, int onlyThisTrack, int startTime, int duration) {
        return ComponentManager.MovieExportToHandle(ci, h, theMovie, onlyThisTrack, startTime, duration);
    }

    private static int MovieExportToDataRef(int ci, int dataRef, int dataRefType, int theMovie, int onlyThisTrack, int startTime, int duration) {
        return ComponentManager.MovieExportToDataRef(ci, dataRef, dataRefType, theMovie, onlyThisTrack, startTime, duration);
    }

    private static int MovieExportGetAuxiliaryData(int ci, int dataH, int[] handleType) {
        return ComponentManager.MovieExportGetAuxiliaryData(ci, dataH, handleType);
    }

    private static int MovieExportSetProgressProc(int ci, int proc, int refCon) {
        return ComponentManager.MovieExportSetProgressProc(ci, proc, refCon);
    }

    private static int MovieExportSetSampleDescription(int ci, int desc, int mediaType) {
        return ComponentManager.MovieExportSetSampleDescription(ci, desc, mediaType);
    }

    private static int MovieExportDoUserDialog(int ci, int theMovie, int onlyThisTrack, int startTime, int duration, byte[] canceled) {
        return ComponentManager.MovieExportDoUserDialog(ci, theMovie, onlyThisTrack, startTime, duration, canceled);
    }

    private static int MovieExportGetCreatorType(int ci, int[] creator) {
        return ComponentManager.MovieExportGetCreatorType(ci, creator);
    }

    private static int MovieExportValidate(int ci, int theMovie, int onlyThisTrack, byte[] valid) {
        return ComponentManager.MovieExportValidate(ci, theMovie, onlyThisTrack, valid);
    }

    private static int MovieExportSetSettingsFromAtomContainer(int ci, int settings) {
        return ComponentManager.MovieExportSetSettingsFromAtomContainer(ci, settings);
    }

    private static int MovieExportGetFileNameExtension(int ci, int[] extension) {
        return ComponentManager.MovieExportGetFileNameExtension(ci, extension);
    }

    private static int MovieExportGetShortFileTypeString(int ci, byte[] typeString) {
        return ComponentManager.MovieExportGetShortFileTypeString(ci, typeString);
    }

    private static int MovieExportGetSourceMediaType(int ci, int[] mediaType) {
        return ComponentManager.MovieExportGetSourceMediaType(ci, mediaType);
    }

    private static int MovieExportToFile(int ci, byte[] fsspec, int theMovie, int onlyThisTrack, int startTime, int duration) {
        return ComponentManager.MovieExportToFile(ci, fsspec, theMovie, onlyThisTrack, startTime, duration);
    }
}
