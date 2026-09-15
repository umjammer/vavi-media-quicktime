package quicktime.std.qtcomponents;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTNullPointerException;
import quicktime.QTObject;
import quicktime.QTSession;
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
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(MovieExporter.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Opens the specified movie export component,'spit', with the given subType.
     * QuickTime::OpenADefaultComponent
     * @param subType The movie export component sub type
     */
    public MovieExporter(int paramInt) throws QTException {
        super(1936746868, paramInt);
        setProgressProc(null);
    }

    /**
     * Given a component identifier it opens the specified component. The component type
     * must be 'spit' or an exception will be thrown as it is not a MovieExporter.
     * This constructor allows an application to find the appropriate export component
     * and then have that component opened.
     * QuickTime::OpenAComponent
     * @param comp the component identifier to open
     */
    public MovieExporter(ComponentIdentifier paramComponentIdentifier) throws QTException {
        super(paramComponentIdentifier, 1936746868);
        setProgressProc(null);
    }

    private AtomContainer settings;

    /**
     * This method establishes a MovieProgress object for the MovieExporter
     * that will be called for you by the Movie Toolbox whenever it
     * has to do a lengthy movie export process with this MovieExporter
     * QuickTime::MovieExportSetProgressProc
     * @param mp the MovieProgress object, or null for no progress object
     * @param {@link quicktime.std.StdQTException}
     */
    public void setProgressProc(MovieProgress paramMovieProgress) throws StdQTException {
    }

    /**
     * This method is used to export movie data into a file.
     * QuickTime::MovieExportToFile
     * @param theFile the file that is to receive the converted movie data
     * @param theMovie specifies the movie to export.
     * @param onlyThisTrack Specifies the track used to reference.
     * @param startTime Specifies the start time value of the time base.
     * @param duration Specifies the elapsed time value of the timebase.
     * @return The file containing the converted movie data.
     * @return {@link quicktime.QTException}
     */
    public void toFile(QTFile paramQTFile, Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        int i = 0;
        if (this.progUPP != null) {
            this.progUPP.theCurrentMov = paramMovie;
        }
        synchronized (QTNative.globalsLock) {
            i = MovieExportToFile(_ID(), paramQTFile.getFSSpec(true, 512), QTObject.ID(paramMovie), QTObject.ID(paramTrack), paramInt1, paramInt2);
        }
        StdQTException.checkError(i);
    }

    /**
     * Exports the movie's track data into a handle, returning the handle the data is exported too.
     * QuickTime::MovieExportToHandle
     * @param theMovie Specifies the movie.
     * @param onlyThisTrack Specifies the track used to reference.
     * @param startTime Specifies the start time of the time base.
     * @param duration Specifies the time value of the timebase.
     * @return The handle containing the converted movie data.
     * @return {@link quicktime.QTException}
     */
    public QTHandle toHandle(Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        QTHandle localQTHandle = new QTHandle();
        int i = MovieExportToHandle(_ID(), QTObject.ID(localQTHandle), QTObject.ID(paramMovie), QTObject.ID(paramTrack), paramInt1, paramInt2);
        StdQTException.checkError(i);
        return localQTHandle;
    }

    /**
     * Exports the movie's track data to the specified data reference for the movie export component.
     * Determines and opens the component to use for the given data reference and data type.
     * QuickTime::MovieExportToDataRef
     * @param theMovie Specifies the movie.
     * @param duration Specifies the time value of the timebase.
     * @return The handle containing the converted movie data.
     * @return {@link quicktime.std.StdQTException}
     */
    public void toDataRef(DataRef paramDataRef, Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws StdQTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        StdQTException.checkError(MovieExportToDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), QTObject.ID(paramMovie), QTObject.ID(paramTrack), paramInt1, paramInt2));
    }

    /**
     * Determines the data type for the component.
     * QuickTime::MovieExportGetAuxiliaryData
     * @param dataH Specifies the data handle.
     * @return The handle type.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getAuxiliaryData(QTHandle paramQTHandle) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieExportGetAuxiliaryData(_ID(), QTObject.ID(paramQTHandle), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Specifies the SampleDescription and media type for the component.
     * QuickTime::MovieExportSetSampleDescription
     * @param desc Specifies the sample description.
     * @param mediaType Specifies the media type.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSampleDescription(SampleDescription paramSampleDescription, int paramInt) throws StdQTException {
        int i = MovieExportSetSampleDescription(_ID(), QTObject.ID(paramSampleDescription), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Determines the data reference handle for the movie export component.
     * QuickTime::MovieExportDoUserDialog
     * @param theMovie Specifies the movie.
     * @param onlyThisTrack Specifies the track used to reference.
     * @param startTime Specifies the start time of the time base.
     * @param duration Specifies the time value of the timebase.
     * @return True, if the dialog operation was canceled, otherwise false.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean doUserDialog(Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws StdQTException {
        return false;
    }

    /**
     * Determines the file type creator for the component.
     * QuickTime::MovieExportGetCreatorType
     * @return The creator file type.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getCreatorType() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieExportGetCreatorType(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Use this method to determine whether the movie or track could be exported by this exporter.
     * QuickTime::MovieExportValidate
     * @param theMovie the movie for this operation.
     * @param onlyThisTrack identifies a track that is to be converted or null. If supplied the component
     *  will only convert the specified track.
     * @return true if the export operation can occur, otherwise false.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean validate(Movie paramMovie, Track paramTrack) throws StdQTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        byte[] arrayOfByte = { 0 };
        StdQTException.checkError(MovieExportValidate(_ID(), QTObject.ID(paramMovie), QTObject.ID(paramTrack), arrayOfByte));
        return arrayOfByte[0] != 0;
    }

    /**
     * Retrieves the current settings from the movie export component..
     * QuickTime::MovieExportGetSettingsAsAtomContainer
     * @return an AtomContainer with the current settings.
     * @return {@link quicktime.std.StdQTException}
     */
    public AtomContainer getExportSettingsFromAtomContainer() throws StdQTException {
        return this.settings != null ? this.settings : AtomContainer.fromMovieExporter(this);
    }

    /**
     * Sets the movie export componentÕs current configuration from the passed settings data.
     * QuickTime::MovieExportSetSettingsFromAtomContainer
     */
    public void setExportSettingsFromAtomContainer(AtomContainer paramAtomContainer) throws StdQTException {
        this.settings = paramAtomContainer;
    }

    /**
     * Gets the movie export components current FileNameExtension.
     * QuickTime::MovieExportGetFileNameExtension
     * @return and ostype of the current filename extension.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getExportFileNameExtension() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieExportGetFileNameExtension(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * No Documentation.
     * QuickTime::MovieExportGetShortFileTypeString
     * @return a java string.
     * @return {@link quicktime.std.StdQTException}
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
     * QuickTime::MovieExportGetSourceMediaType
     * @return and ostype.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getExportSourceMediaType() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieExportGetSourceMediaType(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

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

    private static native int MovieExportToHandle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);

    private static native int MovieExportToDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);

    private static native int MovieExportGetAuxiliaryData(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int MovieExportSetProgressProc(int paramInt1, int paramInt2, int paramInt3);

    private static native int MovieExportSetSampleDescription(int paramInt1, int paramInt2, int paramInt3);

    private static native int MovieExportDoUserDialog(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte);

    private static native int MovieExportGetCreatorType(int paramInt, int[] paramArrayOfInt);

    private static native int MovieExportValidate(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

    private static native int MovieExportSetSettingsFromAtomContainer(int paramInt1, int paramInt2);

    private static native int MovieExportGetFileNameExtension(int paramInt, int[] paramArrayOfInt);

    private static native int MovieExportGetShortFileTypeString(int paramInt, byte[] paramArrayOfByte);

    private static native int MovieExportGetSourceMediaType(int paramInt, int[] paramArrayOfInt);

    private static native int MovieExportToFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
}

