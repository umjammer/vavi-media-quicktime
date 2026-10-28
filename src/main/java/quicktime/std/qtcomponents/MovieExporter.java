package quicktime.std.qtcomponents;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;
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

    private static Object linkage;

    private QTCompDispatcher progUPP;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(MovieExporter.class));
            }
        }.establish();
    }

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
        QTHandle localQTHandle = new QTHandle();
        int i = MovieExportToHandle(_ID(), QTObject.ID(localQTHandle), QTObject.ID(theMovie), QTObject.ID(onlyThisTrack), startTime, duration);
        StdQTException.checkError(i);
        return localQTHandle;
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
    public void toDataRef(DataRef paramDataRef, Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws StdQTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        StdQTException.checkError(MovieExportToDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), QTObject.ID(paramMovie), QTObject.ID(paramTrack), paramInt1, paramInt2));
    }

    /**
     * Determines the data type for the component.
     * @see "QuickTime::MovieExportGetAuxiliaryData"
     * @param dataH Specifies the data handle.
     * @return The handle type.
     * @throws quicktime.std.StdQTException
     */
    public int getAuxiliaryData(QTHandle dataH) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieExportGetAuxiliaryData(_ID(), QTObject.ID(dataH), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
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
        byte[] arrayOfByte = new byte[1];
        Movie localMovie = theMovie;
        Track localTrack = onlyThisTrack;
        int i = startTime;
        int j = duration;
        int k = _ID();
        int m = 0;
        if (QTSession.isCurrentOS(4)) {
            m = (Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                @Override
                public Object invoke() {
                    int l = 0;
                    synchronized (QTNative.globalsLock) {
                        l = MovieExporter.MovieExportDoUserDialog(k, QTObject.ID(localMovie), QTObject.ID(localTrack), i, j, arrayOfByte);
                    }
                    return l;
                }
            });
            StdQTException.checkError(m);
        } else {
            synchronized (QTNative.globalsLock) {
                m = MovieExportDoUserDialog(_ID(), QTObject.ID(theMovie), QTObject.ID(onlyThisTrack), startTime, duration, arrayOfByte);
                StdQTException.checkError(m);
            }
        }
        return arrayOfByte[0] != 0;
    }

    /**
     * Determines the file type creator for the component.
     * @see "QuickTime::MovieExportGetCreatorType"
     * @return The creator file type.
     * @throws quicktime.std.StdQTException
     */
    public int getCreatorType() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieExportGetCreatorType(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
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
        byte[] arrayOfByte = { 0 };
        StdQTException.checkError(MovieExportValidate(_ID(), QTObject.ID(theMovie), QTObject.ID(onlyThisTrack), arrayOfByte));
        return arrayOfByte[0] != 0;
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
    public void setExportSettingsFromAtomContainer(AtomContainer paramAtomContainer) throws StdQTException {
        int i = MovieExportSetSettingsFromAtomContainer(_ID(), QTObject.ID(paramAtomContainer));
        StdQTException.checkError(i);
    }

    /**
     * Gets the movie export components current FileNameExtension.
     * @see "QuickTime::MovieExportGetFileNameExtension"
     * @return and ostype of the current filename extension.
     * @throws quicktime.std.StdQTException
     */
    public final int getExportFileNameExtension() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieExportGetFileNameExtension(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * No Documentation.
     * @see "QuickTime::MovieExportGetShortFileTypeString"
     * @return a java string.
     * @throws quicktime.std.StdQTException
     */
    public final String getExportShortFileTypeString() throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int i = MovieExportGetShortFileTypeString(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
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
    public final int getExportSourceMediaType() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieExportGetSourceMediaType(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    @Override
    protected void _dispose() throws QTException {
        try {
            synchronized (QTNative.globalsLock) {
                MovieExportSetProgressProc(_ID(), 0, 0);
            }
        } catch (QTNullPointerException localQTNullPointerException) {
        }
        if (this.progUPP != null)
            this.progUPP.cleanupMethodClosure();
        this.progUPP = null;
    }

    private static int MovieExportToHandle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        return ComponentManager.MovieExportToHandle(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
    }

    private static int MovieExportToDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
        return ComponentManager.MovieExportToDataRef(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7);
    }

    private static int MovieExportGetAuxiliaryData(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ComponentManager.MovieExportGetAuxiliaryData(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int MovieExportSetProgressProc(int paramInt1, int paramInt2, int paramInt3) {
        return ComponentManager.MovieExportSetProgressProc(paramInt1, paramInt2, paramInt3);
    }

    private static int MovieExportSetSampleDescription(int paramInt1, int paramInt2, int paramInt3) {
        return ComponentManager.MovieExportSetSampleDescription(paramInt1, paramInt2, paramInt3);
    }

    private static int MovieExportDoUserDialog(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte) {
        return ComponentManager.MovieExportDoUserDialog(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfByte);
    }

    private static int MovieExportGetCreatorType(int paramInt, int[] paramArrayOfInt) {
        return ComponentManager.MovieExportGetCreatorType(paramInt, paramArrayOfInt);
    }

    private static int MovieExportValidate(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        return ComponentManager.MovieExportValidate(paramInt1, paramInt2, paramInt3, paramArrayOfByte);
    }

    private static int MovieExportSetSettingsFromAtomContainer(int paramInt1, int paramInt2) {
        return ComponentManager.MovieExportSetSettingsFromAtomContainer(paramInt1, paramInt2);
    }

    private static int MovieExportGetFileNameExtension(int paramInt, int[] paramArrayOfInt) {
        return ComponentManager.MovieExportGetFileNameExtension(paramInt, paramArrayOfInt);
    }

    private static int MovieExportGetShortFileTypeString(int paramInt, byte[] paramArrayOfByte) {
        return ComponentManager.MovieExportGetShortFileTypeString(paramInt, paramArrayOfByte);
    }

    private static int MovieExportGetSourceMediaType(int paramInt, int[] paramArrayOfInt) {
        return ComponentManager.MovieExportGetSourceMediaType(paramInt, paramArrayOfInt);
    }

    private static int MovieExportToFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return ComponentManager.MovieExportToFile(paramInt1, paramArrayOfByte, paramInt2, paramInt3, paramInt4, paramInt5);
    }
}

