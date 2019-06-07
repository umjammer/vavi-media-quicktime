package quicktime.std.movies;

import java.awt.Dimension;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.GDevice;
import quicktime.qd.Pict;
import quicktime.qd.PixMap;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.Matrix;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.Media;
import quicktime.std.movies.media.UserData;
import quicktime.std.qtcomponents.MovieImportInfo;
import quicktime.std.qtcomponents.MovieImporter;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;

/**
 * The Track class implements the corresponding data structure of the Movie Toolbox.
 * A QuickTime movie may contain several tracks. Each track refers to a media that contains references
 * to the movie data, which may be stored as images or sound on hard disks, floppy disks, compact discs, or other devices.
 * Specifies the track for an operation. Your application obtains a track identifier from
 * such Movie Toolbox functions as NewMovieTrack and GetMovieTrack (described on page 2-151 of
 * Inside Macintosh: QuickTime and page 2-204 of Inside Macintosh: QuickTime, respectively).
 * You make a new Track object only through addTrack or getTrack methods on a movie object.
 */
public final class Track extends QTObject implements QuickTimeLib {

    private static Object linkage;

    Object owner;

    static {
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(Track.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * This method allows you to determine the track that uses a specified media.
     * QuickTime::GetMediaTrack()
     * @return a Track object of the track that uses the media.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.media.Media#getTrack()}
     */
    public static Track fromMedia(Media paramMedia) throws QTException {
        int i = GetMediaTrack(QTObject.ID(paramMedia));
        StdQTException.checkError(GetMoviesError());
        return new Track(i, paramMedia);
    }

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
     * @see {@link quicktime.std.qtcomponents.MovieImporter#fromFile(quicktime.io.QTFile, quicktime.std.movies.Movie, quicktime.std.movies.Track, int, int)}
     */
    public static MovieImportInfo fromMovieImporterFile(MovieImporter paramMovieImporter, QTFile paramQTFile, Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        int[] arrayOfInt1 = paramTrack == null ? null : new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int i = MovieImportFile(QTObject.ID(paramMovieImporter), paramQTFile.getFSSpec(true, 256), QTObject.ID(paramMovie), QTObject.ID(paramTrack), arrayOfInt1, paramInt1, arrayOfInt2, paramInt2, arrayOfInt3);
        StdQTException.checkError(i);
        int j = 0;
        if ((paramInt2 & 0x4) != 0)
            if (paramTrack != null) {
                j = QTObject.ID(paramTrack);
            } else
                j = arrayOfInt1[0];
        Track localTrack = j == 0 ? null : new Track(j, paramMovie);
        return new MovieImportInfo(localTrack, arrayOfInt2[0], arrayOfInt3[0]);
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
     * @see {@link quicktime.std.qtcomponents.MovieImporter#fromHandle(quicktime.util.QTHandleRef, quicktime.std.movies.Movie, quicktime.std.movies.Track, int, int)}
     */
    public static MovieImportInfo fromMovieImporterHandle(MovieImporter paramMovieImporter, QTHandleRef paramQTHandleRef, Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        int[] arrayOfInt1 = paramTrack == null ? null : new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int i = MovieImportHandle(QTObject.ID(paramMovieImporter), QTObject.ID(paramQTHandleRef), QTObject.ID(paramMovie), QTObject.ID(paramTrack), arrayOfInt1, paramInt1, arrayOfInt2, paramInt2, arrayOfInt3);
        StdQTException.checkError(i);
        int j = 0;
        if ((paramInt2 & 0x4) != 0)
            if (paramTrack != null) {
                j = QTObject.ID(paramTrack);
            } else
                j = arrayOfInt1[0];
        Track localTrack = j == 0 ? null : new Track(j, paramMovie);
        return new MovieImportInfo(localTrack, arrayOfInt2[0], arrayOfInt3[0]);
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
     * @see {@link quicktime.std.qtcomponents.MovieImporter#fromDataRef(quicktime.std.movies.media.DataRef, quicktime.std.movies.Movie, quicktime.std.movies.Track, int, int)}
     */
    public static MovieImportInfo fromMovieImporterDataRef(MovieImporter paramMovieImporter, DataRef paramDataRef, Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        int[] arrayOfInt1 = paramTrack == null ? null : new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int i = MovieImportDataRef(QTObject.ID(paramMovieImporter), QTObject.ID(paramDataRef), paramDataRef.getType(), QTObject.ID(paramMovie), QTObject.ID(paramTrack), arrayOfInt1, paramInt1, arrayOfInt2, paramInt2, arrayOfInt3);
        StdQTException.checkError(i);
        int j = 0;
        if ((paramInt2 & 0x4) != 0)
            if (paramTrack != null) {
                j = QTObject.ID(paramTrack);
            } else
                j = arrayOfInt1[0];
        Track localTrack = j == 0 ? null : new Track(j, paramMovie);
        return new MovieImportInfo(localTrack, arrayOfInt2[0], arrayOfInt3[0]);
    }

    Track(Movie paramMovie, float paramFloat1, float paramFloat2, float paramFloat3) throws QTException {
        this(NewMovieTrack(QTObject.ID(paramMovie), QTUtils.X2Fix(paramFloat1), QTUtils.X2Fix(paramFloat2), QTUtils.X2ShortFix(paramFloat3)), paramMovie);
        StdQTException.checkError(GetMoviesError());
    }

    Track(int paramInt, Object paramObject) throws QTException {
        super(paramInt, paramObject);
        if (paramInt == 0)
            throw new StdQTException(63527);
        this.owner = paramObject;
    }

    void _setNR(int paramInt) {
        setNR(paramInt);
    }

    /**
     * Returns a Media class that corresponds to the media that contains a track's data.
     * QuickTime::GetTrackMedia()
     * @return a Media subclass
     * @return {@link quicktime.QTException}
     */
    public Media getMedia() throws QTException {
        return Media.getTrackMedia(this);
    }

    /**
     * Removes a track's media.
     * QuickTime::DisposeTrackMedia()
     */
    public void removeMedia() throws StdQTException {
        int i = GetTrackMedia(_ID());
        StdQTException.checkError(GetMoviesError());
        DisposeTrackMedia(i);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method sets a new QDGraphics for the track.
     * QuickTime::SetTrackGWorld()
     * @param port the new destination port or null to use the movies port
     * @param gdh the new device or null to use the current device
     * @param {@link quicktime.std.StdQTException}
     */
    public void setGWorld(QDGraphics paramQDGraphics, GDevice paramGDevice) throws StdQTException {
        SetTrackGWorld(_ID(), QTObject.ID(paramQDGraphics), QTObject.ID(paramGDevice), 0, 0);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method creates a QuickDraw picture from the specified track at the specified time.
     * QuickTime::GetTrackPict()
     * @param time Specifies the track image for the picture. The time parameter contains the time from which the image is taken.
     * @return a Pict object for the specified time.
     * @return {@link quicktime.QTException}
     */
    public Pict getPict(int paramInt) throws QTException {
        return Pict.fromTrack(this, paramInt);
    }

    /**
     * This method allows your application to determine the clipping region of a track.
     * QuickTime::GetTrackClipRgn()
     * @return a Region object specifying the clip region for this track.
     * @return {@link quicktime.QTException}
     */
    public Region getClipRgn() throws QTException {
        return Region.fromTrackClip(this);
    }

    /**
     * This method allows your application to set the clipping region of a track.
     * QuickTime::SetTrackClipRgn()
     * @param theClip Specifies the track's clipping region.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setClipRgn(Region paramRegion) throws StdQTException {
        SetTrackClipRgn(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine the region a track occupies in a movie's graphics world.
     * QuickTime::GetTrackDisplayBoundsRgn()
     * @return a Region object specifying the track's bounds region.
     * @return {@link quicktime.QTException}
     */
    public Region getDisplayBoundsRgn() throws QTException {
        return Region.fromTrackDisplay(this);
    }

    /**
     * This method allows your application to determine the region the track occupies in a movie's boundary region.
     * QuickTime::GetTrackMovieBoundsRgn()
     * @return a Region object specifying the region the track occupies in a movie's boundary region.
     * @return {@link quicktime.QTException}
     */
    public Region getMovieBoundsRgn() throws QTException {
        return Region.fromTrackMovieBounds(this);
    }

    /**
     * This method allows the media to limit the size of the track boundary rectangle.  Therefore, the region
     * returned by GetTrackBoundsRgn may not be rectangular and may be smaller than the track boundary region.
     * QuickTime::GetTrackBoundsRgn()
     * @return a Region object specifying the region track's media uses to do drawing.
     * @return {@link quicktime.QTException}
     */
    public Region getBoundsRgn() throws QTException {
        return Region.fromTrackBounds(this);
    }

    /**
     * This method allows your application to determine a track's unique track ID value.
     * QuickTime::GetTrackID()
     * @return the track's ID value.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getID() throws StdQTException {
        int i = GetTrackID(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a track's creation date.
     * QuickTime::GetTrackCreationTime()
     * @return the track's creation date.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getCreationTime() throws StdQTException {
        int i = GetTrackCreationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a track's modification date.
     * QuickTime::GetTrackModificationTime()
     * @return the track's modification date.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getModificationTime() throws StdQTException {
        int i = GetTrackModificationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to determine whether a track is currently enabled.
     * QuickTime::GetTrackEnabled()
     * @return true if the track is enabled and false if the track is disabled.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean getEnabled() throws StdQTException {
        int i = GetTrackEnabled(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method allows your application to enable and disable a track.
     * QuickTime::SetTrackEnabled()
     * @param enabled Enables or disables the track. Set this parameter to true
     *  to enable the track. Set this parameter to false to disable the track.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setEnabled(boolean paramBoolean) throws StdQTException {
        byte b = (byte) (paramBoolean ? 1 : 0);
        SetTrackEnabled(_ID(), b);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine whether a track is used in a movie,
     * its preview, its poster, or a combination of these.
     * QuickTime::GetTrackUsage()
     * @return flags indicating the track's usage.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getUsage() throws StdQTException {
        int i = GetTrackUsage(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to specify whether a track is used in a movie,
     * its preview, its poster, or a combination of these.
     * QuickTime::SetTrackUsage()
     * @param usage Contains flags that specify how the track is to be used.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setUsage(int paramInt) throws StdQTException {
        SetTrackUsage(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns the duration of a track.
     * QuickTime::GetTrackDuration()
     * @return the duration of a track.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getDuration() throws StdQTException {
        int i = GetTrackDuration(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to determine the time difference between the start of a track
     * and the start of the movie that contains the track.
     * QuickTime::GetTrackOffset()
     * @return the track's offset from the start of the movie
     * @return {@link quicktime.std.StdQTException}
     */
    public int getOffset() throws StdQTException {
        int i = GetTrackOffset(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method modifies the duration of the empty space that lies at the beginning of
     * the track, thus changing the duration of the entire track.
     * QuickTime::SetTrackOffset()
     * @param movieOffsetTime Specifies the track's offset from the start of the movie, and must be expressed
     *  in the time scale of the movie that contains the track.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setOffset(int paramInt) throws StdQTException {
        SetTrackOffset(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to retrieve a track's layer.
     * QuickTime::GetTrackLayer()
     * @return the track's layer number.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getLayer() throws StdQTException {
        int i = GetTrackLayer(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to set a track's layer.
     * QuickTime::SetTrackLayer()
     * @param layer Specifies the track's layer number.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setLayer(int paramInt) throws StdQTException {
        SetTrackLayer(_ID(), (short) paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine all the tracks in an alternate group.
     * QuickTime::GetTrackAlternate()
     * @return the Track object of the next track in the group.
     * @return {@link quicktime.QTException}
     */
    public Track getAlternate() throws QTException {
        int i = GetTrackAlternate(_ID());
        StdQTException.checkError(GetMoviesError());
        return new Track(i, this.owner);
    }

    /**
     * This method allows your application to add tracks to or remove tracks from alternate groups.
     * QuickTime::SetTrackAlternate()
     * @param alternateT Controls whether the function adds the track to a group or removes it from a group.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setAlternate(Track paramTrack) throws StdQTException {
        SetTrackAlternate(_ID(), QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns a track's current volume setting.
     * QuickTime::GetTrackVolume()
     * @return a track's current volume setting.
     * @return {@link quicktime.std.StdQTException}
     */
    public float getVolume() throws StdQTException {
        short s = GetTrackVolume(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.ShortFix2X(s);
    }

    /**
     * This method allows your application to set a track's current volume.
     * QuickTime::SetTrackVolume()
     * @param volume Specifies the current volume setting of the track.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setVolume(float paramFloat) throws StdQTException {
        SetTrackVolume(_ID(), QTUtils.X2ShortFix(paramFloat));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to retrieve a track's transformation matrix.
     * QuickTime::GetTrackMatrix()
     * @return a Matrix object specifying the track's transformation matrix.
     * @return {@link quicktime.std.StdQTException}
     */
    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        GetTrackMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(GetMoviesError());
        return localMatrix;
    }

    /**
     * This method allows your application to establish a track's transformation matrix.
     * QuickTime::SetTrackMatrix()
     * @param matrix Specifies the track's new matrix.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setMatrix(Matrix paramMatrix) throws StdQTException {
        SetTrackMatrix(_ID(), paramMatrix.getBytes());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0 see getSize</I>
     * This method allows your application to determine a track's source, or display, rectangle.
     * QuickTime::GetTrackDimensions()
     * @return a QDDimension object containing the track's width and height in pixels.
     * @return {@link quicktime.std.StdQTException}
     */
    public Dimension getDimensions() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetTrackDimensions(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new Dimension((int) QTUtils.Fix2X(arrayOfInt1[0]), (int) QTUtils.Fix2X(arrayOfInt2[0]));
    }

    /**
     * Deprecated.
     * <I>as of QTJava 4.0 see setSize</I>
     * This method allows your application to establish a track's source, or display, rectangle.
     * QuickTime::SetTrackDimensions()
     * @param d a Dimension object containing the width and height, in pixels, of the track's rectangle.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDimensions(Dimension paramDimension) throws StdQTException {
        SetTrackDimensions(_ID(), QTUtils.X2Fix(paramDimension.width), QTUtils.X2Fix(paramDimension.height));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine a track's source, or display, rectangle.
     * QuickTime::GetTrackDimensions()
     * @return a QDDimension object containing the track's width and height in pixels.
     * @return {@link quicktime.std.StdQTException}
     */
    public QDDimension getSize() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetTrackDimensions(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new QDDimension(QTUtils.Fix2X(arrayOfInt1[0]), QTUtils.Fix2X(arrayOfInt2[0]));
    }

    /**
     * This method allows your application to establish a track's source, or display, rectangle.
     * QuickTime::SetTrackDimensions()
     * @param d a Dimension object containing the width and height, in pixels, of the track's rectangle.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSize(QDDimension paramQDDimension) throws StdQTException {
        SetTrackDimensions(_ID(), QTUtils.X2Fix(paramQDDimension.getWidthF()), QTUtils.X2Fix(paramQDDimension.getHeightF()));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to obtain access to a track's user data list.
     * QuickTime::GetTrackUserData()
     * @return a UserData object referencing the track's user data list.
     * @return {@link quicktime.std.StdQTException}
     */
    public UserData getUserData() throws StdQTException {
        return UserData.fromTrack(this);
    }

    /**
     * This method allows your application to obtain access to a track's 'name' user data item.
     * QuickTime::GetTrackUserData()
     * @return a String object referencing the track's 'name'; an empty string the track has no UserDataName
     * @return {@link quicktime.std.StdQTException}
     */
    public String getName() throws StdQTException {
        UserData localUserData = UserData.fromTrack(this);
        try {
            QTHandle localQTHandle = localUserData.getData(1851878757, 1);
            int i = localQTHandle.getSize();
            byte[] arrayOfByte = new byte[i];
            localQTHandle.copyToArray(0, arrayOfByte, 0, i);
            return new String(arrayOfByte);
        } catch (QTException localQTException) {
            int i = localQTException.errorCode();
            if (i != 63510)
                throw new StdQTException(i);
        }
        return new String();
    }

    /**
     * This method allows your application to set a track's 'name' user data item.
     * QuickTime::SetTrackUserData()
     * @param name String object containing the track's name. Pass null or empty removes the UserDataName
     * @param {@link quicktime.QTException}
     */
    public void setName(String paramString) throws QTException {
        UserData localUserData = UserData.fromTrack(this);
        int i = 0;
        if (paramString != null) {
            i = paramString.length();
        }
        if (i == 0) {
            try {
                localUserData.removeData(1851878757, 1);
            } catch (StdQTException localStdQTException) {
                int j = localStdQTException.errorCode();
                if (j != 63510) {
                    throw new StdQTException(j);
                }
            }
        } else {
            QTHandle localQTHandle = new QTHandle(i, false);
            localQTHandle.copyFromArray(0, paramString.getBytes(), 0, i);
            localUserData.setDataItem(localQTHandle.toQTPointer(), 1851878757, 1);
        }
    }

    /**
     * This method returns the track's display matrix.
     * QuickTime::GetTrackDisplayMatrix()
     * @return the track's display matrix.
     * @return {@link quicktime.std.StdQTException}
     */
    public Matrix getDisplayMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = GetTrackDisplayMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    /**
     * This method returns the sound localization settings for the track.
     * QuickTime::GetTrackSoundLocalizationSettings()
     * @return the sound localization settings for the track.
     * @return {@link quicktime.QTException}
     */
    public QTHandle getSoundLocalizationSettings() throws QTException {
        return QTHandle.fromTrack(this);
    }

    /**
     * This method allows your application to set the sound localization settings for the track.
     * QuickTime::SetTrackSoundLocalizationSettings()
     * @param newSettings Specifies the new sound localization settings for the track
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSoundLocalizationSettings(QTHandle paramQTHandle) throws StdQTException {
        int i = SetTrackSoundLocalizationSettings(_ID(), QTObject.ID(paramQTHandle));
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to determine the movie that contains a specified track.
     * QuickTime::GetTrackMovie()
     * @return a Movie object that corresponds to the movie that contains the track.
     * @return {@link quicktime.std.StdQTException}
     */
    public Movie getMovie() throws StdQTException {
        int i = GetTrackMovie(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Movie(i, this);
    }

    /**
     * This method searches for times of interest in a track.
     * QuickTime::GetTrackNextInterestingTime()
     * @param interestingTimeFlags Specifies the search criteria.
     * @param time Specifies a time value that establishes the starting point for the search.
     * @param rate Contains the search direction.
     * @return a TimeInfo object specifiying the first time value it finds
     *  that meets the search criteria specified in the flags parameter.
     * @return {@link quicktime.std.StdQTException}
     */
    public TimeInfo getNextInterestingTime(int paramInt1, int paramInt2, float paramFloat) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetTrackNextInterestingTime(_ID(), (short) paramInt1, paramInt2, QTUtils.X2Fix(paramFloat), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    /**
     * This method inserts a reference to a media segment into a track.
     * QuickTime::InsertMediaIntoTrack()
     * @param trackStart Contains a time value specifying where the segment is to be inserted.
     * @param mediaTime Contains a time value specifying the starting point of the segment in the media.
     * @param mediaDuration Contains a time value specifying the duration of the media's segment.
     * @param mediaRate Specifies the media's rate.
     * @param {@link quicktime.std.StdQTException}
     */
    public void insertMedia(int paramInt1, int paramInt2, int paramInt3, float paramFloat) throws StdQTException {
        int i = InsertMediaIntoTrack(_ID(), paramInt1, paramInt2, paramInt3, QTUtils.X2Fix(paramFloat));
        StdQTException.checkError(i);
    }

    /**
     * This method adds an empty segment to a track.
     * QuickTime::InsertEmptyTrackSegment()
     * @param dstIn Contains a time value specifying where the segment is to be inserted.
     * @param dstDuration Contains a time value that specifies the duration of the segment to be added.
     * @param {@link quicktime.std.StdQTException}
     */
    public void insertEmptySegment(int paramInt1, int paramInt2) throws StdQTException {
        int i = InsertEmptyTrackSegment(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * This method removes a specified segment from a track.
     * QuickTime::DeleteTrackSegment()
     * @param startTime Contains a time value specifying the starting point of the segment to be deleted
     * @param duration Contains a time value that specifies the duration of the segment to be deleted.
     * @param {@link quicktime.std.StdQTException}
     */
    public void deleteSegment(int paramInt1, int paramInt2) throws StdQTException {
        int i = DeleteTrackSegment(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * This method changes the duration of a segment of a track.
     * QuickTime::ScaleTrackSegment()
     * @param startTime Specifies the start of the segment.
     * @param oldDuration Specifies the duration of the segment.
     * @param newDuration Specifies the new duration of the segment.
     * @param {@link quicktime.std.StdQTException}
     */
    public void scaleSegment(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = ScaleTrackSegment(_ID(), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    /**
     * You can create an edit state by calling the NewTrackEditState function.
     * QuickTime::NewTrackEditState()
     * @return the TrackEditState.
     * @return {@link quicktime.QTException}
     */
    public TrackEditState newEditState() throws QTException {
        int i = NewTrackEditState(_ID());
        StdQTException.checkError(GetMoviesError());
        return new TrackEditState(this, i);
    }

    /**
     * This method returns a track to its condition according to an edit state you created previously.
     * QuickTime::UseTrackEditState()
     * @param state Specifies the edit state for this operation.
     * @param {@link quicktime.std.StdQTException}
     */
    public void useEditState(TrackEditState paramTrackEditState) throws StdQTException {
        int i = UseTrackEditState(_ID(), QTObject.ID(paramTrackEditState));
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to add a new track reference to a track.
     * QuickTime::AddTrackReference()
     * @param refTrack Specifies the track to be identified in the track reference.
     * @param refType Specifies the type of reference.
     * @return the index of the added reference.
     * @return {@link quicktime.std.StdQTException}
     */
    public int addReference(Track paramTrack, int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = AddTrackReference(_ID(), QTObject.ID(paramTrack), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method allows you to remove a track reference from a track.
     * QuickTime::DeleteTrackReference()
     * @param refType Specifies the type of reference.
     * @param index Specifies the index value of the reference to be deleted.
     * @param {@link quicktime.std.StdQTException}
     */
    public void deleteReference(int paramInt1, int paramInt2) throws StdQTException {
        int i = DeleteTrackReference(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to modify an existing track reference.
     * QuickTime::SetTrackReference()
     * @param refTrack Specifies the track to be identified in the track reference.
     * @param refType Specifies the type of reference.
     * @param index Specifies the index value of the reference to be changed.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setReference(Track paramTrack, int paramInt1, int paramInt2) throws StdQTException {
        int i = SetTrackReference(_ID(), QTObject.ID(paramTrack), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to retrieve the track identifier contained in an existing track reference.
     * QuickTime::GetTrackReference()
     * @param refType Specifies the type of reference.
     * @param index Specifies the index value of the reference to be changed.
     * @param {@link quicktime.QTException}
     */
    public Track getReference(int paramInt1, int paramInt2) throws QTException {
        int i = GetTrackReference(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
        return new Track(i, this.owner);
    }

    /**
     * This method allows you to determine all of the track reference types that are defined for a given track.
     * QuickTime::GetNextTrackReferenceType()
     * @param refType Specifies the type of reference.
     * @param {@link quicktime.std.StdQTException}
     */
    public int getNextReferenceType(int paramInt) throws StdQTException {
        int i = GetNextTrackReferenceType(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to determine how many track references of a given type exist for a track.
     * QuickTime::GetTrackReferenceCount()
     * @param refType Specifies the type of reference.
     * @param {@link quicktime.std.StdQTException}
     */
    public int getReferenceCount(int paramInt) throws StdQTException {
        int i = GetTrackReferenceCount(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to convert a track's time value to a time value
     * that is appropriate to the track's media using the track's edit list.
     * QuickTime::TrackTimeToMediaTime()
     * @param value Specifies the track's time value; must be expressed in the time scale of the movie that contains the track.
     * @param {@link quicktime.std.StdQTException}
     */
    public int trackTimeToMediaTime(int paramInt) throws StdQTException {
        int i = TrackTimeToMediaTime(paramInt, _ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns the rate of the track edit of a specified track at an indicated time.
     * QuickTime::GetTrackEditRate()
     * @param atTime Indicates a time value at which the rate of a track edit
     *  (of a track identified in the parameter theTrack) is to be determined.
     * @return the track's edit rate.
     * @return {@link quicktime.std.StdQTException}
     */
    public float getEditRate(int paramInt) throws StdQTException {
        int i = GetTrackEditRate(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
        return QTUtils.Fix2X(i);
    }

    /**
     * This method allows your application to determine the size, in bytes,
     * of the sample data in a segment of a track.
     * QuickTime::GetTrackDataSize()
     * @param startTime Contains a time value specifying the starting point of the segment.
     * @param duration Contains a time value that specifies the duration of the segment.
     * @param {@link quicktime.std.StdQTException}
     */
    public int getDataSize(int paramInt1, int paramInt2) throws StdQTException {
        int i = GetTrackDataSize(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to determine whether a specified point lies in the region
     * defined by a track's display boundary region after
     * it has been clipped by the movie's final display clipping region.
     * QuickTime::PtInTrack()
     * @param pt Specifies the point to be checked.
     * @param {@link quicktime.std.StdQTException}
     */
    public boolean pointInMovie(QDPoint paramQDPoint) throws StdQTException {
        int i = PtInTrack(_ID(), paramQDPoint.getPoint());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method allows your application to determine the region a track occupies in a movie's graphics
     * world during a specified segment.
     * QuickTime::GetTrackSegmentDisplayBoundsRgn()
     * @param time Specifies the starting time of the track segment to consider.
     * @param duration Specifies the length of the segment to consider. Set this parameter to 0 to
     *  consider an instant in time.
     * @param {@link quicktime.QTException}
     */
    public Region getSegmentDisplayBoundsRgn(int paramInt1, int paramInt2) throws QTException {
        return Region.fromTrackSegment(this, paramInt1, paramInt2);
    }

    /**
     * This method returns the value of the last error the media encountered while playing a specified track.
     * QuickTime::GetTrackStatus()
     * @return the value of the last error the media encountered while playing a specified track.
     */
    public int getStatus() {
        int i = GetTrackStatus(_ID());
        return i;
    }

    /**
     * This method allows you to specify a portion of a track that is to be loaded into memory whenever it is played.
     * QuickTime::SetTrackLoadSettings()
     * @param settings a LoadSettings object.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setLoadSettings(LoadSettings paramLoadSettings) throws StdQTException {
        SetTrackLoadSettings(_ID(), paramLoadSettings.time, paramLoadSettings.duration, paramLoadSettings.preloadFlags, paramLoadSettings.defaultHints);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows you to retrieve a track's preload information.
     * QuickTime::GetTrackLoadSettings()
     * @return a LoadSettings object.
     * @return {@link quicktime.std.StdQTException}
     */
    public LoadSettings getLoadSettings() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int[] arrayOfInt4 = new int[1];
        GetTrackLoadSettings(_ID(), arrayOfInt1, arrayOfInt2, arrayOfInt3, arrayOfInt4);
        StdQTException.checkError(GetMoviesError());
        return new LoadSettings(arrayOfInt1[0], arrayOfInt2[0], arrayOfInt3[0], arrayOfInt4[0]);
    }

    /**
     * This method copies part of one track to another.
     * QuickTime::InsertTrackSegment()
     * @param dstTrack Specifies the destination track for this operation.
     * @param srcIn Specifies the start of the segment in the source track.
     * @param srcDuration Specifies the duration of the segment in the source track.
     * @param dstIn Contains a time value specifying where the segment is to be inserted.
     * @param {@link quicktime.std.StdQTException}
     */
    public void insertSegment(Track paramTrack, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = InsertTrackSegment(_ID(), QTObject.ID(paramTrack), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    /**
     * This method copies many settings from one track to another, overwriting the destination settings.
     * QuickTime::CopyTrackSettings()
     * @param dstTrack Specifies the destination track for this operation.
     * @param {@link quicktime.std.StdQTException}
     */
    public void copySettings(Track paramTrack) throws StdQTException {
        int i = CopyTrackSettings(_ID(), QTObject.ID(paramTrack));
        StdQTException.checkError(i);
    }

    /**
     * This method loads a track's data into memory.
     * QuickTime::LoadTrackIntoRam()
     * @param time Allows you to specify a portion of the track to load.
     * @param duration Allows you to specify a portion of the track to load.
     * @param flags Gives you explicit control over what is loaded into memory and how long to keep it around.
     * @param {@link quicktime.std.StdQTException}
     */
    public void loadIntoRam(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = LoadTrackIntoRam(_ID(), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    /**
     * Looks on the System scrap to find out if it can translate any of the track data into a movie.
     * QuickTime::IsScrapMovie()
     */
    public MovieImporter isScrapMovie() throws StdQTException {
        return MovieImporter.fromTrack(this);
    }

    /**
     * A PixMap object which describes the Matte of a Track.
     * QuickTime::GetTrackMatte()
     * @return a PixMap object, or null if no matte set
     * @return {@link quicktime.std.StdQTException}
     */
    public PixMap getMatte() throws StdQTException {
        return PixMap.fromTrack(this);
    }

    /**
     * A PixMap object which will describe the Matte of a Track.
     * QuickTime::SetTrackMatte()
     * @return a PixMap object, or null if no matte set
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.Track#getMatte()}
     */
    public void setMatte(PixMap paramPixMap) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            SetTrackMatte(_ID(), QTObject.ID(paramPixMap));
            StdQTException.checkError(GetMoviesError());
        }
    }

    /**
     * Copies the user data in the track to the destination track
     * QuickTime::CopyTrackUserData()
     * @param destTrack destination track for user data
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @param {@link quicktime.std.StdQTException}
     */
    public void copyUserDataToTrack(Track paramTrack, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(CopyTrackUserData(_ID(), QTObject.ID(paramTrack), paramBoolean == true ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified track into this track
     * QuickTime::CopyTrackUserData()
     * @param srcTrack source track for user data
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @param {@link quicktime.std.StdQTException}
     */
    public void copyUserDataFromTrack(Track paramTrack, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(CopyTrackUserData(QTObject.ID(paramTrack), _ID(), paramBoolean == true ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified track into the destination track
     * QuickTime::CopyTrackUserData()
     * @param srcTrack source track for user data
     * @param destTrack destination track for user data
     * @param copyRule constant for merge or replace
     *     use StdQTConstants5_1.kQTCopyUserDataReplace or StdQTConstants5_1.kQTCopyUserDataMerge
     * @param {@link quicktime.std.StdQTException}
     */
    public static void copyUserData(Track paramTrack1, Track paramTrack2, int paramInt) throws StdQTException {
        StdQTException.checkError(CopyTrackUserData(QTObject.ID(paramTrack1), QTObject.ID(paramTrack2), paramInt));
    }

    /**
     * String representation of this class.
     */
    public String toString() {
        try {
            return super.toString() + "[ID=" + getID() + "]";
        } catch (StdQTException localStdQTException) {
        }
        return super.toString() + "[ID=unable to obtain track ID]";
    }

    private static native int GetMediaTrack(int paramInt);

    private static native short GetMoviesError();

    private static native int MovieImportFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int paramInt4, int[] paramArrayOfInt2, int paramInt5, int[] paramArrayOfInt3);

    private static native int MovieImportHandle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt1, int paramInt5, int[] paramArrayOfInt2, int paramInt6, int[] paramArrayOfInt3);

    private static native int MovieImportDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt1, int paramInt6, int[] paramArrayOfInt2, int paramInt7, int[] paramArrayOfInt3);

    private static native int NewMovieTrack(int paramInt1, int paramInt2, int paramInt3, short paramShort);

    private static native int GetTrackMedia(int paramInt);

    private static native void DisposeTrackMedia(int paramInt);

    private static native void SetTrackGWorld(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native void SetTrackClipRgn(int paramInt1, int paramInt2);

    private static native int GetTrackID(int paramInt);

    private static native int GetTrackCreationTime(int paramInt);

    private static native int GetTrackModificationTime(int paramInt);

    private static native byte GetTrackEnabled(int paramInt);

    private static native void SetTrackEnabled(int paramInt, byte paramByte);

    private static native int GetTrackUsage(int paramInt);

    private static native void SetTrackUsage(int paramInt1, int paramInt2);

    private static native int GetTrackDuration(int paramInt);

    private static native int GetTrackOffset(int paramInt);

    private static native void SetTrackOffset(int paramInt1, int paramInt2);

    private static native short GetTrackLayer(int paramInt);

    private static native void SetTrackLayer(int paramInt, short paramShort);

    private static native int GetTrackAlternate(int paramInt);

    private static native void SetTrackAlternate(int paramInt1, int paramInt2);

    private static native short GetTrackVolume(int paramInt);

    private static native void SetTrackVolume(int paramInt, short paramShort);

    private static native void GetTrackMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native void SetTrackMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native void GetTrackDimensions(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void SetTrackDimensions(int paramInt1, int paramInt2, int paramInt3);

    private static native short GetTrackDisplayMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native short SetTrackSoundLocalizationSettings(int paramInt1, int paramInt2);

    private static native int GetTrackMovie(int paramInt);

    private static native void GetTrackNextInterestingTime(int paramInt1, short paramShort, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native short InsertMediaIntoTrack(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native short InsertEmptyTrackSegment(int paramInt1, int paramInt2, int paramInt3);

    private static native short DeleteTrackSegment(int paramInt1, int paramInt2, int paramInt3);

    private static native short ScaleTrackSegment(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int NewTrackEditState(int paramInt);

    private static native short UseTrackEditState(int paramInt1, int paramInt2);

    private static native short AddTrackReference(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native short DeleteTrackReference(int paramInt1, int paramInt2, int paramInt3);

    private static native short SetTrackReference(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int GetTrackReference(int paramInt1, int paramInt2, int paramInt3);

    private static native int GetNextTrackReferenceType(int paramInt1, int paramInt2);

    private static native int GetTrackReferenceCount(int paramInt1, int paramInt2);

    private static native int TrackTimeToMediaTime(int paramInt1, int paramInt2);

    private static native int GetTrackEditRate(int paramInt1, int paramInt2);

    private static native int GetTrackDataSize(int paramInt1, int paramInt2, int paramInt3);

    private static native byte PtInTrack(int paramInt1, int paramInt2);

    private static native int GetTrackStatus(int paramInt);

    private static native void SetTrackLoadSettings(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native void GetTrackLoadSettings(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4);

    private static native short InsertTrackSegment(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native short CopyTrackSettings(int paramInt1, int paramInt2);

    private static native short LoadTrackIntoRam(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native void SetTrackMatte(int paramInt1, int paramInt2);

    private static native short CopyTrackUserData(int paramInt1, int paramInt2, int paramInt3);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/Track.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
