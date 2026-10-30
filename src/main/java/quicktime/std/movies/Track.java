package quicktime.std.movies;

import java.awt.Dimension;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.internal.toolbox.MovieToolbox;
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

    Object owner;

    /**
     * This method allows you to determine the track that uses a specified media.
     * @see "QuickTime::GetMediaTrack()"
     * @return a Track object of the track that uses the media.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.media.Media#getTrack()
     */
    public static Track fromMedia(Media media) throws QTException {
        int i = GetMediaTrack(QTObject.ID(media));
        StdQTException.checkError(GetMoviesError());
        return new Track(i, media);
    }

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
     * @see quicktime.std.qtcomponents.MovieImporter#fromFile(quicktime.io.QTFile, quicktime.std.movies.Movie, quicktime.std.movies.Track, int, int)
     */
    public static MovieImportInfo fromMovieImporterFile(MovieImporter ci, QTFile fileIn, Movie theMovie, Track targetTrack, int atTime, int inFlags) throws QTException {
        int[] usedTrack = targetTrack == null ? null : new int[1];
        int[] addedDuration = new int[1];
        int[] outFlags = new int[1];
        int i = MovieImportFile(QTObject.ID(ci), fileIn.getFSSpec(true, 256), QTObject.ID(theMovie), QTObject.ID(targetTrack), usedTrack, atTime, addedDuration, inFlags, outFlags);
        StdQTException.checkError(i);
        int j = 0;
        if ((inFlags & 0x4) != 0)
            if (targetTrack != null) {
                j = QTObject.ID(targetTrack);
            } else
                j = usedTrack[0];
        Track t = j == 0 ? null : new Track(j, theMovie);
        return new MovieImportInfo(t, addedDuration[0], outFlags[0]);
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
     * @see quicktime.std.qtcomponents.MovieImporter#fromHandle(quicktime.util.QTHandleRef, quicktime.std.movies.Movie, quicktime.std.movies.Track, int, int)
     */
    public static MovieImportInfo fromMovieImporterHandle(MovieImporter ci, QTHandleRef dataRef, Movie theMovie, Track targetTrack, int atTime, int inFlags) throws QTException {
        int[] usedTrack = targetTrack == null ? null : new int[1];
        int[] addedDuration = new int[1];
        int[] outFlags = new int[1];
        int i = MovieImportHandle(QTObject.ID(ci), QTObject.ID(dataRef), QTObject.ID(theMovie), QTObject.ID(targetTrack), usedTrack, atTime, addedDuration, inFlags, outFlags);
        StdQTException.checkError(i);
        int j = 0;
        if ((inFlags & 0x4) != 0)
            if (targetTrack != null) {
                j = QTObject.ID(targetTrack);
            } else
                j = usedTrack[0];
        Track t = j == 0 ? null : new Track(j, theMovie);
        return new MovieImportInfo(t, addedDuration[0], outFlags[0]);
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
     * @see quicktime.std.qtcomponents.MovieImporter#fromDataRef(quicktime.std.movies.media.DataRef, quicktime.std.movies.Movie, quicktime.std.movies.Track, int, int)
     */
    public static MovieImportInfo fromMovieImporterDataRef(MovieImporter ci, DataRef dataRef, Movie theMovie, Track targetTrack, int atTime, int inFlags) throws QTException {
        int[] usedTrack = targetTrack == null ? null : new int[1];
        int[] addedDuration = new int[1];
        int[] outFlags = new int[1];
        int i = MovieImportDataRef(QTObject.ID(ci), QTObject.ID(dataRef), dataRef.getType(), QTObject.ID(theMovie), QTObject.ID(targetTrack), usedTrack, atTime, addedDuration, inFlags, outFlags);
        StdQTException.checkError(i);
        int j = 0;
        if ((inFlags & 0x4) != 0)
            if (targetTrack != null) {
                j = QTObject.ID(targetTrack);
            } else
                j = usedTrack[0];
        Track t = j == 0 ? null : new Track(j, theMovie);
        return new MovieImportInfo(t, addedDuration[0], outFlags[0]);
    }

    Track(Movie movie, float width, float height, float volume) throws QTException {
        this(NewMovieTrack(QTObject.ID(movie), QTUtils.X2Fix(width), QTUtils.X2Fix(height), QTUtils.X2ShortFix(volume)), movie);
        StdQTException.checkError(GetMoviesError());
    }

    Track(int nativeRep, Object owner) throws QTException {
        super(nativeRep, owner);
        if (nativeRep == 0)
            throw new StdQTException(63527);
        this.owner = owner;
    }

    void _setNR(int nr) {
        setNR(nr);
    }

    /**
     * Returns a Media class that corresponds to the media that contains a track's data.
     * @see "QuickTime::GetTrackMedia()"
     * @return a Media subclass
     * @throws quicktime.QTException
     */
    public Media getMedia() throws QTException {
        return Media.getTrackMedia(this);
    }

    /**
     * Removes a track's media.
     * @see "QuickTime::DisposeTrackMedia()"
     */
    public void removeMedia() throws StdQTException {
        int i = GetTrackMedia(_ID());
        StdQTException.checkError(GetMoviesError());
        DisposeTrackMedia(i);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method sets a new QDGraphics for the track.
     * @see "QuickTime::SetTrackGWorld()"
     * @param port the new destination port or null to use the movies port
     * @param gdh the new device or null to use the current device
     * @throws quicktime.std.StdQTException
     */
    public void setGWorld(QDGraphics port, GDevice gdh) throws StdQTException {
        SetTrackGWorld(_ID(), QTObject.ID(port), QTObject.ID(gdh), 0, 0);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method creates a QuickDraw picture from the specified track at the specified time.
     * @see "QuickTime::GetTrackPict()"
     * @param time Specifies the track image for the picture. The time parameter contains the time from which the image is taken.
     * @return a Pict object for the specified time.
     * @throws quicktime.QTException
     */
    public Pict getPict(int time) throws QTException {
        return Pict.fromTrack(this, time);
    }

    /**
     * This method allows your application to determine the clipping region of a track.
     * @see "QuickTime::GetTrackClipRgn()"
     * @return a Region object specifying the clip region for this track.
     * @throws quicktime.QTException
     */
    public Region getClipRgn() throws QTException {
        return Region.fromTrackClip(this);
    }

    /**
     * This method allows your application to set the clipping region of a track.
     * @see "QuickTime::SetTrackClipRgn()"
     * @param theClip Specifies the track's clipping region.
     * @throws quicktime.std.StdQTException
     */
    public void setClipRgn(Region theClip) throws StdQTException {
        SetTrackClipRgn(_ID(), QTObject.ID(theClip));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine the region a track occupies in a movie's graphics world.
     * @see "QuickTime::GetTrackDisplayBoundsRgn()"
     * @return a Region object specifying the track's bounds region.
     * @throws quicktime.QTException
     */
    public Region getDisplayBoundsRgn() throws QTException {
        return Region.fromTrackDisplay(this);
    }

    /**
     * This method allows your application to determine the region the track occupies in a movie's boundary region.
     * @see "QuickTime::GetTrackMovieBoundsRgn()"
     * @return a Region object specifying the region the track occupies in a movie's boundary region.
     * @throws quicktime.QTException
     */
    public Region getMovieBoundsRgn() throws QTException {
        return Region.fromTrackMovieBounds(this);
    }

    /**
     * This method allows the media to limit the size of the track boundary rectangle.  Therefore, the region
     * returned by GetTrackBoundsRgn may not be rectangular and may be smaller than the track boundary region.
     * @see "QuickTime::GetTrackBoundsRgn()"
     * @return a Region object specifying the region track's media uses to do drawing.
     * @throws quicktime.QTException
     */
    public Region getBoundsRgn() throws QTException {
        return Region.fromTrackBounds(this);
    }

    /**
     * This method allows your application to determine a track's unique track ID value.
     * @see "QuickTime::GetTrackID()"
     * @return the track's ID value.
     * @throws quicktime.std.StdQTException
     */
    public int getID() throws StdQTException {
        int i = GetTrackID(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a track's creation date.
     * @see "QuickTime::GetTrackCreationTime()"
     * @return the track's creation date.
     * @throws quicktime.std.StdQTException
     */
    public int getCreationTime() throws StdQTException {
        int i = GetTrackCreationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a track's modification date.
     * @see "QuickTime::GetTrackModificationTime()"
     * @return the track's modification date.
     * @throws quicktime.std.StdQTException
     */
    public int getModificationTime() throws StdQTException {
        int i = GetTrackModificationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to determine whether a track is currently enabled.
     * @see "QuickTime::GetTrackEnabled()"
     * @return true if the track is enabled and false if the track is disabled.
     * @throws quicktime.std.StdQTException
     */
    public boolean getEnabled() throws StdQTException {
        int i = GetTrackEnabled(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method allows your application to enable and disable a track.
     * @see "QuickTime::SetTrackEnabled()"
     * @param enabled Enables or disables the track. Set this parameter to true
     *  to enable the track. Set this parameter to false to disable the track.
     * @throws quicktime.std.StdQTException
     */
    public void setEnabled(boolean enabled) throws StdQTException {
        byte b = (byte) (enabled ? 1 : 0);
        SetTrackEnabled(_ID(), b);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine whether a track is used in a movie,
     * its preview, its poster, or a combination of these.
     * @see "QuickTime::GetTrackUsage()"
     * @return flags indicating the track's usage.
     * @throws quicktime.std.StdQTException
     */
    public int getUsage() throws StdQTException {
        int i = GetTrackUsage(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to specify whether a track is used in a movie,
     * its preview, its poster, or a combination of these.
     * @see "QuickTime::SetTrackUsage()"
     * @param usage Contains flags that specify how the track is to be used.
     * @throws quicktime.std.StdQTException
     */
    public void setUsage(int usage) throws StdQTException {
        SetTrackUsage(_ID(), usage);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns the duration of a track.
     * @see "QuickTime::GetTrackDuration()"
     * @return the duration of a track.
     * @throws quicktime.std.StdQTException
     */
    public int getDuration() throws StdQTException {
        int i = GetTrackDuration(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to determine the time difference between the start of a track
     * and the start of the movie that contains the track.
     * @see "QuickTime::GetTrackOffset()"
     * @return the track's offset from the start of the movie
     * @throws quicktime.std.StdQTException
     */
    public int getOffset() throws StdQTException {
        int offset = GetTrackOffset(_ID());
        StdQTException.checkError(GetMoviesError());
        return offset;
    }

    /**
     * This method modifies the duration of the empty space that lies at the beginning of
     * the track, thus changing the duration of the entire track.
     * @see "QuickTime::SetTrackOffset()"
     * @param movieOffsetTime Specifies the track's offset from the start of the movie, and must be expressed
     *  in the time scale of the movie that contains the track.
     * @throws quicktime.std.StdQTException
     */
    public void setOffset(int movieOffsetTime) throws StdQTException {
        SetTrackOffset(_ID(), movieOffsetTime);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to retrieve a track's layer.
     * @see "QuickTime::GetTrackLayer()"
     * @return the track's layer number.
     * @throws quicktime.std.StdQTException
     */
    public int getLayer() throws StdQTException {
        int layer = GetTrackLayer(_ID());
        StdQTException.checkError(GetMoviesError());
        return layer;
    }

    /**
     * This method allows your application to set a track's layer.
     * @see "QuickTime::SetTrackLayer()"
     * @param layer Specifies the track's layer number.
     * @throws quicktime.std.StdQTException
     */
    public void setLayer(int layer) throws StdQTException {
        SetTrackLayer(_ID(), (short) layer);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine all the tracks in an alternate group.
     * @see "QuickTime::GetTrackAlternate()"
     * @return the Track object of the next track in the group.
     * @throws quicktime.QTException
     */
    public Track getAlternate() throws QTException {
        int alternate = GetTrackAlternate(_ID());
        StdQTException.checkError(GetMoviesError());
        return new Track(alternate, this.owner);
    }

    /**
     * This method allows your application to add tracks to or remove tracks from alternate groups.
     * @see "QuickTime::SetTrackAlternate()"
     * @param alternateT Controls whether the function adds the track to a group or removes it from a group.
     * @throws quicktime.std.StdQTException
     */
    public void setAlternate(Track alternateT) throws StdQTException {
        SetTrackAlternate(_ID(), QTObject.ID(alternateT));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns a track's current volume setting.
     * @see "QuickTime::GetTrackVolume()"
     * @return a track's current volume setting.
     * @throws quicktime.std.StdQTException
     */
    public float getVolume() throws StdQTException {
        short s = GetTrackVolume(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.ShortFix2X(s);
    }

    /**
     * This method allows your application to set a track's current volume.
     * @see "QuickTime::SetTrackVolume()"
     * @param volume Specifies the current volume setting of the track.
     * @throws quicktime.std.StdQTException
     */
    public void setVolume(float volume) throws StdQTException {
        SetTrackVolume(_ID(), QTUtils.X2ShortFix(volume));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to retrieve a track's transformation matrix.
     * @see "QuickTime::GetTrackMatrix()"
     * @return a Matrix object specifying the track's transformation matrix.
     * @throws quicktime.std.StdQTException
     */
    public Matrix getMatrix() throws StdQTException {
        Matrix matrix = new Matrix();
        GetTrackMatrix(_ID(), matrix.getBytes());
        StdQTException.checkError(GetMoviesError());
        return matrix;
    }

    /**
     * This method allows your application to establish a track's transformation matrix.
     * @see "QuickTime::SetTrackMatrix()"
     * @param matrix Specifies the track's new matrix.
     * @throws quicktime.std.StdQTException
     */
    public void setMatrix(Matrix matrix) throws StdQTException {
        SetTrackMatrix(_ID(), matrix.getBytes());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * @deprecated
     * <I>since QTJava 4.0 see getSize</I>
     * This method allows your application to determine a track's source, or display, rectangle.
     * @see "QuickTime::GetTrackDimensions()"
     * @return a QDDimension object containing the track's width and height in pixels.
     * @throws quicktime.std.StdQTException
     */
    public Dimension getDimensions() throws StdQTException {
        int[] width = new int[1];
        int[] height = new int[1];
        GetTrackDimensions(_ID(), width, height);
        StdQTException.checkError(GetMoviesError());
        return new Dimension((int) QTUtils.Fix2X(width[0]), (int) QTUtils.Fix2X(height[0]));
    }

    /**
     * @deprecated
     * <I>as of QTJava 4.0 see setSize</I>
     * This method allows your application to establish a track's source, or display, rectangle.
     * @see "QuickTime::SetTrackDimensions()"
     * @param d a Dimension object containing the width and height, in pixels, of the track's rectangle.
     * @throws quicktime.std.StdQTException
     */
    public void setDimensions(Dimension d) throws StdQTException {
        SetTrackDimensions(_ID(), QTUtils.X2Fix(d.width), QTUtils.X2Fix(d.height));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine a track's source, or display, rectangle.
     * @see "QuickTime::GetTrackDimensions()"
     * @return a QDDimension object containing the track's width and height in pixels.
     * @throws quicktime.std.StdQTException
     */
    public QDDimension getSize() throws StdQTException {
        int[] width = new int[1];
        int[] height = new int[1];
        GetTrackDimensions(_ID(), width, height);
        StdQTException.checkError(GetMoviesError());
        return new QDDimension(QTUtils.Fix2X(width[0]), QTUtils.Fix2X(height[0]));
    }

    /**
     * This method allows your application to establish a track's source, or display, rectangle.
     * @see "QuickTime::SetTrackDimensions()"
     * @param d a Dimension object containing the width and height, in pixels, of the track's rectangle.
     * @throws quicktime.std.StdQTException
     */
    public void setSize(QDDimension d) throws StdQTException {
        SetTrackDimensions(_ID(), QTUtils.X2Fix(d.getWidthF()), QTUtils.X2Fix(d.getHeightF()));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to obtain access to a track's user data list.
     * @see "QuickTime::GetTrackUserData()"
     * @return a UserData object referencing the track's user data list.
     * @throws quicktime.std.StdQTException
     */
    public UserData getUserData() throws StdQTException {
        return UserData.fromTrack(this);
    }

    /**
     * This method allows your application to obtain access to a track's 'name' user data item.
     * @see "QuickTime::GetTrackUserData()"
     * @return a String object referencing the track's 'name'; an empty string the track has no UserDataName
     * @throws quicktime.std.StdQTException
     */
    public String getName() throws StdQTException {
        UserData userData = UserData.fromTrack(this);
        try {
            QTHandle data = userData.getData(1851878757, 1);
            int i = data.getSize();
            byte[] destArray = new byte[i];
            data.copyToArray(0, destArray, 0, i);
            return new String(destArray);
        } catch (QTException e) {
            int i = e.errorCode();
            if (i != 63510)
                throw new StdQTException(i);
        }
        return "";
    }

    /**
     * This method allows your application to set a track's 'name' user data item.
     * @see "QuickTime::SetTrackUserData()"
     * @param name String object containing the track's name. Pass null or empty removes the UserDataName
     * @throws quicktime.QTException
     */
    public void setName(String name) throws QTException {
        UserData userData = UserData.fromTrack(this);
        int i = 0;
        if (name != null) {
            i = name.length();
        }
        if (i == 0) {
            try {
                userData.removeData(1851878757, 1);
            } catch (StdQTException e) {
                int j = e.errorCode();
                if (j != 63510) {
                    throw new StdQTException(j);
                }
            }
        } else {
            QTHandle qtHandle = new QTHandle(i, false);
            qtHandle.copyFromArray(0, name.getBytes(), 0, i);
            userData.setDataItem(qtHandle.toQTPointer(), 1851878757, 1);
        }
    }

    /**
     * This method returns the track's display matrix.
     * @see "QuickTime::GetTrackDisplayMatrix()"
     * @return the track's display matrix.
     * @throws quicktime.std.StdQTException
     */
    public Matrix getDisplayMatrix() throws StdQTException {
        Matrix displayMatrix = new Matrix();
        int i = GetTrackDisplayMatrix(_ID(), displayMatrix.getBytes());
        StdQTException.checkError(i);
        return displayMatrix;
    }

    /**
     * This method returns the sound localization settings for the track.
     * @see "QuickTime::GetTrackSoundLocalizationSettings()"
     * @return the sound localization settings for the track.
     * @throws quicktime.QTException
     */
    public QTHandle getSoundLocalizationSettings() throws QTException {
        return QTHandle.fromTrack(this);
    }

    /**
     * This method allows your application to set the sound localization settings for the track.
     * @see "QuickTime::SetTrackSoundLocalizationSettings()"
     * @param newSettings Specifies the new sound localization settings for the track
     * @throws quicktime.std.StdQTException
     */
    public void setSoundLocalizationSettings(QTHandle newSettings) throws StdQTException {
        int i = SetTrackSoundLocalizationSettings(_ID(), QTObject.ID(newSettings));
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to determine the movie that contains a specified track.
     * @see "QuickTime::GetTrackMovie()"
     * @return a Movie object that corresponds to the movie that contains the track.
     * @throws quicktime.std.StdQTException
     */
    public Movie getMovie() throws StdQTException {
        int i = GetTrackMovie(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Movie(i, this);
    }

    /**
     * This method searches for times of interest in a track.
     * @see "QuickTime::GetTrackNextInterestingTime()"
     * @param interestingTimeFlags Specifies the search criteria.
     * @param time Specifies a time value that establishes the starting point for the search.
     * @param rate Contains the search direction.
     * @return a TimeInfo object specifiying the first time value it finds
     *  that meets the search criteria specified in the flags parameter.
     * @throws quicktime.std.StdQTException
     */
    public TimeInfo getNextInterestingTime(int interestingTimeFlags, int time, float rate) throws StdQTException {
        int[] interestingTime = new int[1];
        int[] interestingDuration = new int[1];
        GetTrackNextInterestingTime(_ID(), (short) interestingTimeFlags, time, QTUtils.X2Fix(rate), interestingTime, interestingDuration);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(interestingTime[0], interestingDuration[0]);
    }

    /**
     * This method inserts a reference to a media segment into a track.
     * @see "QuickTime::InsertMediaIntoTrack()"
     * @param trackStart Contains a time value specifying where the segment is to be inserted.
     * @param mediaTime Contains a time value specifying the starting point of the segment in the media.
     * @param mediaDuration Contains a time value specifying the duration of the media's segment.
     * @param mediaRate Specifies the media's rate.
     * @throws quicktime.std.StdQTException
     */
    public void insertMedia(int trackStart, int mediaTime, int mediaDuration, float mediaRate) throws StdQTException {
        int i = InsertMediaIntoTrack(_ID(), trackStart, mediaTime, mediaDuration, QTUtils.X2Fix(mediaRate));
        StdQTException.checkError(i);
    }

    /**
     * This method adds an empty segment to a track.
     * @see "QuickTime::InsertEmptyTrackSegment()"
     * @param dstIn Contains a time value specifying where the segment is to be inserted.
     * @param dstDuration Contains a time value that specifies the duration of the segment to be added.
     * @throws quicktime.std.StdQTException
     */
    public void insertEmptySegment(int dstIn, int dstDuration) throws StdQTException {
        int i = InsertEmptyTrackSegment(_ID(), dstIn, dstDuration);
        StdQTException.checkError(i);
    }

    /**
     * This method removes a specified segment from a track.
     * @see "QuickTime::DeleteTrackSegment()"
     * @param startTime Contains a time value specifying the starting point of the segment to be deleted
     * @param duration Contains a time value that specifies the duration of the segment to be deleted.
     * @throws quicktime.std.StdQTException
     */
    public void deleteSegment(int startTime, int duration) throws StdQTException {
        int i = DeleteTrackSegment(_ID(), startTime, duration);
        StdQTException.checkError(i);
    }

    /**
     * This method changes the duration of a segment of a track.
     * @see "QuickTime::ScaleTrackSegment()"
     * @param startTime Specifies the start of the segment.
     * @param oldDuration Specifies the duration of the segment.
     * @param newDuration Specifies the new duration of the segment.
     * @throws quicktime.std.StdQTException
     */
    public void scaleSegment(int startTime, int oldDuration, int newDuration) throws StdQTException {
        int i = ScaleTrackSegment(_ID(), startTime, oldDuration, newDuration);
        StdQTException.checkError(i);
    }

    /**
     * You can create an edit state by calling the NewTrackEditState function.
     * @see "QuickTime::NewTrackEditState()"
     * @return the TrackEditState.
     * @throws quicktime.QTException
     */
    public TrackEditState newEditState() throws QTException {
        int i = NewTrackEditState(_ID());
        StdQTException.checkError(GetMoviesError());
        return new TrackEditState(this, i);
    }

    /**
     * This method returns a track to its condition according to an edit state you created previously.
     * @see "QuickTime::UseTrackEditState()"
     * @param state Specifies the edit state for this operation.
     * @throws quicktime.std.StdQTException
     */
    public void useEditState(TrackEditState state) throws StdQTException {
        int i = UseTrackEditState(_ID(), QTObject.ID(state));
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to add a new track reference to a track.
     * @see "QuickTime::AddTrackReference()"
     * @param refTrack Specifies the track to be identified in the track reference.
     * @param refType Specifies the type of reference.
     * @return the index of the added reference.
     * @throws quicktime.std.StdQTException
     */
    public int addReference(Track refTrack, int refType) throws StdQTException {
        int[] addedIndex = new int[1];
        int i = AddTrackReference(_ID(), QTObject.ID(refTrack), refType, addedIndex);
        StdQTException.checkError(i);
        return addedIndex[0];
    }

    /**
     * This method allows you to remove a track reference from a track.
     * @see "QuickTime::DeleteTrackReference()"
     * @param refType Specifies the type of reference.
     * @param index Specifies the index value of the reference to be deleted.
     * @throws quicktime.std.StdQTException
     */
    public void deleteReference(int refType, int index) throws StdQTException {
        int i = DeleteTrackReference(_ID(), refType, index);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to modify an existing track reference.
     * @see "QuickTime::SetTrackReference()"
     * @param refTrack Specifies the track to be identified in the track reference.
     * @param refType Specifies the type of reference.
     * @param index Specifies the index value of the reference to be changed.
     * @throws quicktime.std.StdQTException
     */
    public void setReference(Track refTrack, int refType, int index) throws StdQTException {
        int i = SetTrackReference(_ID(), QTObject.ID(refTrack), refType, index);
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to retrieve the track identifier contained in an existing track reference.
     * @see "QuickTime::GetTrackReference()"
     * @param refType Specifies the type of reference.
     * @param index Specifies the index value of the reference to be changed.
     * @throws quicktime.QTException
     */
    public Track getReference(int refType, int index) throws QTException {
        int i = GetTrackReference(_ID(), refType, index);
        StdQTException.checkError(GetMoviesError());
        return new Track(i, this.owner);
    }

    /**
     * This method allows you to determine all of the track reference types that are defined for a given track.
     * @see "QuickTime::GetNextTrackReferenceType()"
     * @param refType Specifies the type of reference.
     * @throws quicktime.std.StdQTException
     */
    public int getNextReferenceType(int refType) throws StdQTException {
        int i = GetNextTrackReferenceType(_ID(), refType);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to determine how many track references of a given type exist for a track.
     * @see "QuickTime::GetTrackReferenceCount()"
     * @param refType Specifies the type of reference.
     * @throws quicktime.std.StdQTException
     */
    public int getReferenceCount(int refType) throws StdQTException {
        int i = GetTrackReferenceCount(_ID(), refType);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to convert a track's time value to a time value
     * that is appropriate to the track's media using the track's edit list.
     * @see "QuickTime::TrackTimeToMediaTime()"
     * @param value Specifies the track's time value; must be expressed in the time scale of the movie that contains the track.
     * @throws quicktime.std.StdQTException
     */
    public int trackTimeToMediaTime(int value) throws StdQTException {
        int i = TrackTimeToMediaTime(value, _ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns the rate of the track edit of a specified track at an indicated time.
     * @see "QuickTime::GetTrackEditRate()"
     * @param atTime Indicates a time value at which the rate of a track edit
     *  (of a track identified in the parameter theTrack) is to be determined.
     * @return the track's edit rate.
     * @throws quicktime.std.StdQTException
     */
    public float getEditRate(int atTime) throws StdQTException {
        int i = GetTrackEditRate(_ID(), atTime);
        StdQTException.checkError(GetMoviesError());
        return QTUtils.Fix2X(i);
    }

    /**
     * This method allows your application to determine the size, in bytes,
     * of the sample data in a segment of a track.
     * @see "QuickTime::GetTrackDataSize()"
     * @param startTime Contains a time value specifying the starting point of the segment.
     * @param duration Contains a time value that specifies the duration of the segment.
     * @throws quicktime.std.StdQTException
     */
    public int getDataSize(int startTime, int duration) throws StdQTException {
        int i = GetTrackDataSize(_ID(), startTime, duration);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to determine whether a specified point lies in the region
     * defined by a track's display boundary region after
     * it has been clipped by the movie's final display clipping region.
     * @see "QuickTime::PtInTrack()"
     * @param pt Specifies the point to be checked.
     * @throws quicktime.std.StdQTException
     */
    public boolean pointInMovie(QDPoint pt) throws StdQTException {
        int i = PtInTrack(_ID(), pt.getPoint());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method allows your application to determine the region a track occupies in a movie's graphics
     * world during a specified segment.
     * @see "QuickTime::GetTrackSegmentDisplayBoundsRgn()"
     * @param time Specifies the starting time of the track segment to consider.
     * @param duration Specifies the length of the segment to consider. Set this parameter to 0 to
     *  consider an instant in time.
     * @throws quicktime.QTException
     */
    public Region getSegmentDisplayBoundsRgn(int time, int duration) throws QTException {
        return Region.fromTrackSegment(this, time, duration);
    }

    /**
     * This method returns the value of the last error the media encountered while playing a specified track.
     * @see "QuickTime::GetTrackStatus()"
     * @return the value of the last error the media encountered while playing a specified track.
     */
    public int getStatus() {
        int i = GetTrackStatus(_ID());
        return i;
    }

    /**
     * This method allows you to specify a portion of a track that is to be loaded into memory whenever it is played.
     * @see "QuickTime::SetTrackLoadSettings()"
     * @param settings a LoadSettings object.
     * @throws quicktime.std.StdQTException
     */
    public void setLoadSettings(LoadSettings settings) throws StdQTException {
        SetTrackLoadSettings(_ID(), settings.time, settings.duration, settings.preloadFlags, settings.defaultHints);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows you to retrieve a track's preload information.
     * @see "QuickTime::GetTrackLoadSettings()"
     * @return a LoadSettings object.
     * @throws quicktime.std.StdQTException
     */
    public LoadSettings getLoadSettings() throws StdQTException {
        int[] preloadTime = new int[1];
        int[] preloadDuration = new int[1];
        int[] preloadFlags = new int[1];
        int[] defaultHints = new int[1];
        GetTrackLoadSettings(_ID(), preloadTime, preloadDuration, preloadFlags, defaultHints);
        StdQTException.checkError(GetMoviesError());
        return new LoadSettings(preloadTime[0], preloadDuration[0], preloadFlags[0], defaultHints[0]);
    }

    /**
     * This method copies part of one track to another.
     * @see "QuickTime::InsertTrackSegment()"
     * @param dstTrack Specifies the destination track for this operation.
     * @param srcIn Specifies the start of the segment in the source track.
     * @param srcDuration Specifies the duration of the segment in the source track.
     * @param dstIn Contains a time value specifying where the segment is to be inserted.
     * @throws quicktime.std.StdQTException
     */
    public void insertSegment(Track dstTrack, int srcIn, int srcDuration, int dstIn) throws StdQTException {
        int i = InsertTrackSegment(_ID(), QTObject.ID(dstTrack), srcIn, srcDuration, dstIn);
        StdQTException.checkError(i);
    }

    /**
     * This method copies many settings from one track to another, overwriting the destination settings.
     * @see "QuickTime::CopyTrackSettings()"
     * @param dstTrack Specifies the destination track for this operation.
     * @throws quicktime.std.StdQTException
     */
    public void copySettings(Track dstTrack) throws StdQTException {
        int i = CopyTrackSettings(_ID(), QTObject.ID(dstTrack));
        StdQTException.checkError(i);
    }

    /**
     * This method loads a track's data into memory.
     * @see "QuickTime::LoadTrackIntoRam()"
     * @param time Allows you to specify a portion of the track to load.
     * @param duration Allows you to specify a portion of the track to load.
     * @param flags Gives you explicit control over what is loaded into memory and how long to keep it around.
     * @throws quicktime.std.StdQTException
     */
    public void loadIntoRam(int time, int duration, int flags) throws StdQTException {
        int i = LoadTrackIntoRam(_ID(), time, duration, flags);
        StdQTException.checkError(i);
    }

    /**
     * Looks on the System scrap to find out if it can translate any of the track data into a movie.
     * @see "QuickTime::IsScrapMovie()"
     */
    public MovieImporter isScrapMovie() throws StdQTException {
        return MovieImporter.fromTrack(this);
    }

    /**
     * A PixMap object which describes the Matte of a Track.
     * @see "QuickTime::GetTrackMatte()"
     * @return a PixMap object, or null if no matte set
     * @throws quicktime.std.StdQTException
     */
    public PixMap getMatte() throws StdQTException {
        return PixMap.fromTrack(this);
    }

    /**
     * A PixMap object which will describe the Matte of a Track.
     * @see "QuickTime::SetTrackMatte()"
     * @return a PixMap object, or null if no matte set
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.Track#getMatte()
     */
    public void setMatte(PixMap matte) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            SetTrackMatte(_ID(), QTObject.ID(matte));
            StdQTException.checkError(GetMoviesError());
        }
    }

    /**
     * Copies the user data in the track to the destination track
     * @see "QuickTime::CopyTrackUserData()"
     * @param destTrack destination track for user data
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @throws quicktime.std.StdQTException
     */
    public void copyUserDataToTrack(Track destTrack, boolean replace) throws StdQTException {
        StdQTException.checkError(CopyTrackUserData(_ID(), QTObject.ID(destTrack), replace ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified track into this track
     * @see "QuickTime::CopyTrackUserData()"
     * @param srcTrack source track for user data
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @throws quicktime.std.StdQTException
     */
    public void copyUserDataFromTrack(Track srcTrack, boolean replace) throws StdQTException {
        StdQTException.checkError(CopyTrackUserData(QTObject.ID(srcTrack), _ID(), replace ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified track into the destination track
     * @see "QuickTime::CopyTrackUserData()"
     * @param srcTrack source track for user data
     * @param destTrack destination track for user data
     * @param copyRule constant for merge or replace
     *     use StdQTConstants5_1.kQTCopyUserDataReplace or StdQTConstants5_1.kQTCopyUserDataMerge
     * @throws quicktime.std.StdQTException
     */
    public static void copyUserData(Track srcTrack, Track destTrack, int copyRule) throws StdQTException {
        StdQTException.checkError(CopyTrackUserData(QTObject.ID(srcTrack), QTObject.ID(destTrack), copyRule));
    }

    /**
     * String representation of this class.
     */
    public String toString() {
        try {
            return super.toString() + "[ID=" + getID() + "]";
        } catch (StdQTException _) {
        }
        return super.toString() + "[ID=unable to obtain track ID]";
    }

    private static int GetMediaTrack(int media) {
        return MovieToolbox.GetMediaTrack(media);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int MovieImportFile(int ci, byte[] fsspec, int theMovie, int targetTrack, int[] usedTrack, int atTime, int[] addedDuration, int inFlags, int[] outFlags) {
        return ComponentManager.MovieImportFile(ci, fsspec, theMovie, targetTrack, usedTrack, atTime, addedDuration, inFlags, outFlags);
    }

    private static int MovieImportHandle(int ci, int h, int theMovie, int targetTrack, int[] usedTrack, int atTime, int[] addedDuration, int inFlags, int[] outFlags) {
        return ComponentManager.MovieImportHandle(ci, h, theMovie, targetTrack, usedTrack, atTime, addedDuration, inFlags, outFlags);
    }

    private static int MovieImportDataRef(int ci, int dataRef, int dataRefType, int theMovie, int targetTrack, int[] usedTrack, int atTime, int[] addedDuration, int inFlags, int[] outFlags) {
        return ComponentManager.MovieImportDataRef(ci, dataRef, dataRefType, theMovie, targetTrack, usedTrack, atTime, addedDuration, inFlags, outFlags);
    }

    private static int NewMovieTrack(int movie, int width, int height, short volume) {
        return MovieToolbox.NewMovieTrack(movie, width, height, volume);
    }

    private static int GetTrackMedia(int track) {
        return MovieToolbox.GetTrackMedia(track);
    }

    private static void DisposeTrackMedia(int media) {
        MovieToolbox.DisposeTrackMedia(media);
    }

    private static void SetTrackGWorld(int track, int port, int gdh, int proc, int refCon) {
        MovieToolbox.SetTrackGWorld(track, port, gdh, proc, refCon);
    }

    private static void SetTrackClipRgn(int track, int rgn) {
        MovieToolbox.SetTrackClipRgn(track, rgn);
    }

    private static int GetTrackID(int track) {
        return MovieToolbox.GetTrackID(track);
    }

    private static int GetTrackCreationTime(int track) {
        return MovieToolbox.GetTrackCreationTime(track);
    }

    private static int GetTrackModificationTime(int track) {
        return MovieToolbox.GetTrackModificationTime(track);
    }

    private static byte GetTrackEnabled(int track) {
        return MovieToolbox.GetTrackEnabled(track);
    }

    private static void SetTrackEnabled(int track, byte enabled) {
        MovieToolbox.SetTrackEnabled(track, enabled);
    }

    private static int GetTrackUsage(int track) {
        return MovieToolbox.GetTrackUsage(track);
    }

    private static void SetTrackUsage(int track, int usage) {
        MovieToolbox.SetTrackUsage(track, usage);
    }

    private static int GetTrackDuration(int track) {
        return MovieToolbox.GetTrackDuration(track);
    }

    private static int GetTrackOffset(int track) {
        return MovieToolbox.GetTrackOffset(track);
    }

    private static void SetTrackOffset(int track, int offset) {
        MovieToolbox.SetTrackOffset(track, offset);
    }

    private static short GetTrackLayer(int track) {
        return MovieToolbox.GetTrackLayer(track);
    }

    private static void SetTrackLayer(int track, short layer) {
        MovieToolbox.SetTrackLayer(track, layer);
    }

    private static int GetTrackAlternate(int track) {
        return MovieToolbox.GetTrackAlternate(track);
    }

    private static void SetTrackAlternate(int track, int alternate) {
        MovieToolbox.SetTrackAlternate(track, alternate);
    }

    private static short GetTrackVolume(int track) {
        return MovieToolbox.GetTrackVolume(track);
    }

    private static void SetTrackVolume(int track, short volume) {
        MovieToolbox.SetTrackVolume(track, volume);
    }

    private static void GetTrackMatrix(int track, byte[] matrix) {
        MovieToolbox.GetTrackMatrix(track, matrix);
    }

    private static void SetTrackMatrix(int track, byte[] matrix) {
        MovieToolbox.SetTrackMatrix(track, matrix);
    }

    private static void GetTrackDimensions(int track, int[] width, int[] height) {
        MovieToolbox.GetTrackDimensions(track, width, height);
    }

    private static void SetTrackDimensions(int track, int width, int height) {
        MovieToolbox.SetTrackDimensions(track, width, height);
    }

    private static short GetTrackDisplayMatrix(int track, byte[] matrix) {
        return MovieToolbox.GetTrackDisplayMatrix(track, matrix);
    }

    private static short SetTrackSoundLocalizationSettings(int track, int settings) {
        return MovieToolbox.SetTrackSoundLocalizationSettings(track, settings);
    }

    private static int GetTrackMovie(int track) {
        return MovieToolbox.GetTrackMovie(track);
    }

    private static void GetTrackNextInterestingTime(int track, short flags, int time, int rate, int[] interestingTime, int[] interestingDuration) {
        MovieToolbox.GetTrackNextInterestingTime(track, flags, time, rate, interestingTime, interestingDuration);
    }

    private static short InsertMediaIntoTrack(int track, int trackStart, int mediaTime, int mediaDuration, int mediaRate) {
        return MovieToolbox.InsertMediaIntoTrack(track, trackStart, mediaTime, mediaDuration, mediaRate);
    }

    private static short InsertEmptyTrackSegment(int track, int dstIn, int duration) {
        return MovieToolbox.InsertEmptyTrackSegment(track, dstIn, duration);
    }

    private static short DeleteTrackSegment(int track, int start, int duration) {
        return MovieToolbox.DeleteTrackSegment(track, start, duration);
    }

    private static short ScaleTrackSegment(int track, int start, int oldDuration, int newDuration) {
        return MovieToolbox.ScaleTrackSegment(track, start, oldDuration, newDuration);
    }

    private static int NewTrackEditState(int track) {
        return MovieToolbox.NewTrackEditState(track);
    }

    private static short UseTrackEditState(int track, int state) {
        return MovieToolbox.UseTrackEditState(track, state);
    }

    private static short AddTrackReference(int track, int refTrack, int refType, int[] addedIndex) {
        return MovieToolbox.AddTrackReference(track, refTrack, refType, addedIndex);
    }

    private static short DeleteTrackReference(int track, int refType, int index) {
        return MovieToolbox.DeleteTrackReference(track, refType, index);
    }

    private static short SetTrackReference(int track, int refTrack, int refType, int index) {
        return MovieToolbox.SetTrackReference(track, refTrack, refType, index);
    }

    private static int GetTrackReference(int track, int refType, int index) {
        return MovieToolbox.GetTrackReference(track, refType, index);
    }

    private static int GetNextTrackReferenceType(int track, int refType) {
        return MovieToolbox.GetNextTrackReferenceType(track, refType);
    }

    private static int GetTrackReferenceCount(int track, int refType) {
        return MovieToolbox.GetTrackReferenceCount(track, refType);
    }

    private static int TrackTimeToMediaTime(int value, int track) {
        return MovieToolbox.TrackTimeToMediaTime(value, track);
    }

    private static int GetTrackEditRate(int track, int time) {
        return MovieToolbox.GetTrackEditRate(track, time);
    }

    private static int GetTrackDataSize(int track, int time, int duration) {
        return MovieToolbox.GetTrackDataSize(track, time, duration);
    }

    private static byte PtInTrack(int track, int pt) {
        return MovieToolbox.PtInTrack(track, pt);
    }

    private static int GetTrackStatus(int track) {
        return MovieToolbox.GetTrackStatus(track);
    }

    private static void SetTrackLoadSettings(int track, int preloadTime, int preloadDuration, int preloadFlags, int defaultHints) {
        MovieToolbox.SetTrackLoadSettings(track, preloadTime, preloadDuration, preloadFlags, defaultHints);
    }

    private static void GetTrackLoadSettings(int track, int[] preloadTime, int[] preloadDuration, int[] preloadFlags, int[] defaultHints) {
        MovieToolbox.GetTrackLoadSettings(track, preloadTime, preloadDuration, preloadFlags, defaultHints);
    }

    private static short InsertTrackSegment(int srcTrack, int dstTrack, int srcIn, int srcDuration, int dstIn) {
        return MovieToolbox.InsertTrackSegment(srcTrack, dstTrack, srcIn, srcDuration, dstIn);
    }

    private static short CopyTrackSettings(int src, int dst) {
        return MovieToolbox.CopyTrackSettings(src, dst);
    }

    private static short LoadTrackIntoRam(int track, int time, int duration, int flags) {
        return MovieToolbox.LoadTrackIntoRam(track, time, duration, flags);
    }

    private static void SetTrackMatte(int track, int matte) {
        MovieToolbox.SetTrackMatte(track, matte);
    }

    private static short CopyTrackUserData(int src, int dst, int copyRule) {
        return MovieToolbox.CopyTrackUserData(src, dst, copyRule);
    }
}
