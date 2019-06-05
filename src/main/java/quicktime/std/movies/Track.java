
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

    public static Track fromMedia(Media paramMedia) throws QTException {
        int i = GetMediaTrack(QTObject.ID(paramMedia));
        StdQTException.checkError(GetMoviesError());
        return new Track(i, paramMedia);
    }

    public static MovieImportInfo fromMovieImporterFile(MovieImporter paramMovieImporter,
                                                        QTFile paramQTFile,
                                                        Movie paramMovie,
                                                        Track paramTrack,
                                                        int paramInt1,
                                                        int paramInt2) throws QTException {
        int[] arrayOfInt1 = paramTrack == null ? null : new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int i = MovieImportFile(QTObject.ID(paramMovieImporter),
                                paramQTFile.getFSSpec(true, 256),
                                QTObject.ID(paramMovie),
                                QTObject.ID(paramTrack),
                                arrayOfInt1,
                                paramInt1,
                                arrayOfInt2,
                                paramInt2,
                                arrayOfInt3);

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

    public static MovieImportInfo fromMovieImporterHandle(MovieImporter paramMovieImporter,
                                                          QTHandleRef paramQTHandleRef,
                                                          Movie paramMovie,
                                                          Track paramTrack,
                                                          int paramInt1,
                                                          int paramInt2) throws QTException {
        int[] arrayOfInt1 = paramTrack == null ? null : new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int i = MovieImportHandle(QTObject.ID(paramMovieImporter),
                                  QTObject.ID(paramQTHandleRef),
                                  QTObject.ID(paramMovie),
                                  QTObject.ID(paramTrack),
                                  arrayOfInt1,
                                  paramInt1,
                                  arrayOfInt2,
                                  paramInt2,
                                  arrayOfInt3);

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

    public static MovieImportInfo fromMovieImporterDataRef(MovieImporter paramMovieImporter,
                                                           DataRef paramDataRef,
                                                           Movie paramMovie,
                                                           Track paramTrack,
                                                           int paramInt1,
                                                           int paramInt2) throws QTException {
        int[] arrayOfInt1 = paramTrack == null ? null : new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int i = MovieImportDataRef(QTObject.ID(paramMovieImporter),
                                   QTObject.ID(paramDataRef),
                                   paramDataRef.getType(),
                                   QTObject.ID(paramMovie),
                                   QTObject.ID(paramTrack),
                                   arrayOfInt1,
                                   paramInt1,
                                   arrayOfInt2,
                                   paramInt2,
                                   arrayOfInt3);

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
        this(NewMovieTrack(QTObject.ID(paramMovie),
                           QTUtils.X2Fix(paramFloat1),
                           QTUtils.X2Fix(paramFloat2),
                           QTUtils.X2ShortFix(paramFloat3)),
             paramMovie);
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

    public Media getMedia() throws QTException {
        return Media.getTrackMedia(this);
    }

    public void removeMedia() throws StdQTException {
        int i = GetTrackMedia(_ID());
        StdQTException.checkError(GetMoviesError());

        DisposeTrackMedia(i);
        StdQTException.checkError(GetMoviesError());
    }

    public void setGWorld(QDGraphics paramQDGraphics, GDevice paramGDevice) throws StdQTException {
        SetTrackGWorld(_ID(), QTObject.ID(paramQDGraphics), QTObject.ID(paramGDevice), 0, 0);

        StdQTException.checkError(GetMoviesError());
    }

    public Pict getPict(int paramInt) throws QTException {
        return Pict.fromTrack(this, paramInt);
    }

    public Region getClipRgn() throws QTException {
        return Region.fromTrackClip(this);
    }

    public void setClipRgn(Region paramRegion) throws StdQTException {
        SetTrackClipRgn(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(GetMoviesError());
    }

    public Region getDisplayBoundsRgn() throws QTException {
        return Region.fromTrackDisplay(this);
    }

    public Region getMovieBoundsRgn() throws QTException {
        return Region.fromTrackMovieBounds(this);
    }

    public Region getBoundsRgn() throws QTException {
        return Region.fromTrackBounds(this);
    }

    public int getID() throws StdQTException {
        int i = GetTrackID(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public int getCreationTime() throws StdQTException {
        int i = GetTrackCreationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public int getModificationTime() throws StdQTException {
        int i = GetTrackModificationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public boolean getEnabled() throws StdQTException {
        int i = GetTrackEnabled(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    public void setEnabled(boolean paramBoolean) throws StdQTException {
        byte b = (byte) (paramBoolean ? 1 : 0);
        SetTrackEnabled(_ID(), b);
        StdQTException.checkError(GetMoviesError());
    }

    public int getUsage() throws StdQTException {
        int i = GetTrackUsage(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public void setUsage(int paramInt) throws StdQTException {
        SetTrackUsage(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    public int getDuration() throws StdQTException {
        int i = GetTrackDuration(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public int getOffset() throws StdQTException {
        int i = GetTrackOffset(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public void setOffset(int paramInt) throws StdQTException {
        SetTrackOffset(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    public int getLayer() throws StdQTException {
        int i = GetTrackLayer(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public void setLayer(int paramInt) throws StdQTException {
        SetTrackLayer(_ID(), (short) paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    public Track getAlternate() throws QTException {
        int i = GetTrackAlternate(_ID());
        StdQTException.checkError(GetMoviesError());
        return new Track(i, this.owner);
    }

    public void setAlternate(Track paramTrack) throws StdQTException {
        SetTrackAlternate(_ID(), QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
    }

    public float getVolume() throws StdQTException {
        short s = GetTrackVolume(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.ShortFix2X(s);
    }

    public void setVolume(float paramFloat) throws StdQTException {
        SetTrackVolume(_ID(), QTUtils.X2ShortFix(paramFloat));
        StdQTException.checkError(GetMoviesError());
    }

    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        GetTrackMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(GetMoviesError());
        return localMatrix;
    }

    public void setMatrix(Matrix paramMatrix) throws StdQTException {
        SetTrackMatrix(_ID(), paramMatrix.getBytes());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * @deprecated
     */
    public Dimension getDimensions() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetTrackDimensions(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new Dimension((int) QTUtils.Fix2X(arrayOfInt1[0]), (int) QTUtils.Fix2X(arrayOfInt2[0]));
    }

    /**
     * @deprecated
     */
    public void setDimensions(Dimension paramDimension) throws StdQTException {
        SetTrackDimensions(_ID(), QTUtils.X2Fix(paramDimension.width), QTUtils.X2Fix(paramDimension.height));

        StdQTException.checkError(GetMoviesError());
    }

    public QDDimension getSize() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetTrackDimensions(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new QDDimension(QTUtils.Fix2X(arrayOfInt1[0]), QTUtils.Fix2X(arrayOfInt2[0]));
    }

    public void setSize(QDDimension paramQDDimension) throws StdQTException {
        SetTrackDimensions(_ID(), QTUtils.X2Fix(paramQDDimension.getWidthF()), QTUtils.X2Fix(paramQDDimension.getHeightF()));

        StdQTException.checkError(GetMoviesError());
    }

    public UserData getUserData() throws StdQTException {
        return UserData.fromTrack(this);
    }

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

    public Matrix getDisplayMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = GetTrackDisplayMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(i);

        return localMatrix;
    }

    public QTHandle getSoundLocalizationSettings() throws QTException {
        return QTHandle.fromTrack(this);
    }

    public void setSoundLocalizationSettings(QTHandle paramQTHandle) throws StdQTException {
        int i = SetTrackSoundLocalizationSettings(_ID(), QTObject.ID(paramQTHandle));
        StdQTException.checkError(i);
    }

    public Movie getMovie() throws StdQTException {
        int i = GetTrackMovie(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Movie(i, this);
    }

    public TimeInfo getNextInterestingTime(int paramInt1, int paramInt2, float paramFloat) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];

        GetTrackNextInterestingTime(_ID(), (short) paramInt1, paramInt2, QTUtils.X2Fix(paramFloat), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());

        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    public void insertMedia(int paramInt1, int paramInt2, int paramInt3, float paramFloat) throws StdQTException {
        int i = InsertMediaIntoTrack(_ID(), paramInt1, paramInt2, paramInt3, QTUtils.X2Fix(paramFloat));
        StdQTException.checkError(i);
    }

    public void insertEmptySegment(int paramInt1, int paramInt2) throws StdQTException {
        int i = InsertEmptyTrackSegment(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public void deleteSegment(int paramInt1, int paramInt2) throws StdQTException {
        int i = DeleteTrackSegment(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public void scaleSegment(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = ScaleTrackSegment(_ID(), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    public TrackEditState newEditState() throws QTException {
        int i = NewTrackEditState(_ID());
        StdQTException.checkError(GetMoviesError());
        return new TrackEditState(this, i);
    }

    public void useEditState(TrackEditState paramTrackEditState) throws StdQTException {
        int i = UseTrackEditState(_ID(), QTObject.ID(paramTrackEditState));
        StdQTException.checkError(i);
    }

    public int addReference(Track paramTrack, int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = AddTrackReference(_ID(), QTObject.ID(paramTrack), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void deleteReference(int paramInt1, int paramInt2) throws StdQTException {
        int i = DeleteTrackReference(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public void setReference(Track paramTrack, int paramInt1, int paramInt2) throws StdQTException {
        int i = SetTrackReference(_ID(), QTObject.ID(paramTrack), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public Track getReference(int paramInt1, int paramInt2) throws QTException {
        int i = GetTrackReference(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
        return new Track(i, this.owner);
    }

    public int getNextReferenceType(int paramInt) throws StdQTException {
        int i = GetNextTrackReferenceType(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());

        return i;
    }

    public int getReferenceCount(int paramInt) throws StdQTException {
        int i = GetTrackReferenceCount(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());

        return i;
    }

    public int trackTimeToMediaTime(int paramInt) throws StdQTException {
        int i = TrackTimeToMediaTime(paramInt, _ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public float getEditRate(int paramInt) throws StdQTException {
        int i = GetTrackEditRate(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
        return QTUtils.Fix2X(i);
    }

    public int getDataSize(int paramInt1, int paramInt2) throws StdQTException {
        int i = GetTrackDataSize(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public boolean pointInMovie(QDPoint paramQDPoint) throws StdQTException {
        int i = PtInTrack(_ID(), paramQDPoint.getPoint());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    public Region getSegmentDisplayBoundsRgn(int paramInt1, int paramInt2) throws QTException {
        return Region.fromTrackSegment(this, paramInt1, paramInt2);
    }

    public int getStatus() {
        int i = GetTrackStatus(_ID());
        return i;
    }

    public void setLoadSettings(LoadSettings paramLoadSettings) throws StdQTException {
        SetTrackLoadSettings(_ID(),
                             paramLoadSettings.time,
                             paramLoadSettings.duration,
                             paramLoadSettings.preloadFlags,
                             paramLoadSettings.defaultHints);

        StdQTException.checkError(GetMoviesError());
    }

    public LoadSettings getLoadSettings() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int[] arrayOfInt4 = new int[1];
        GetTrackLoadSettings(_ID(), arrayOfInt1, arrayOfInt2, arrayOfInt3, arrayOfInt4);
        StdQTException.checkError(GetMoviesError());

        return new LoadSettings(arrayOfInt1[0], arrayOfInt2[0], arrayOfInt3[0], arrayOfInt4[0]);
    }

    public void insertSegment(Track paramTrack, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = InsertTrackSegment(_ID(), QTObject.ID(paramTrack), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    public void copySettings(Track paramTrack) throws StdQTException {
        int i = CopyTrackSettings(_ID(), QTObject.ID(paramTrack));
        StdQTException.checkError(i);
    }

    public void loadIntoRam(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = LoadTrackIntoRam(_ID(), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    public MovieImporter isScrapMovie() throws StdQTException {
        return MovieImporter.fromTrack(this);
    }

    public PixMap getMatte() throws StdQTException {
        return PixMap.fromTrack(this);
    }

    public void setMatte(PixMap paramPixMap) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            SetTrackMatte(_ID(), QTObject.ID(paramPixMap));
            StdQTException.checkError(GetMoviesError());
        }
    }

    public void copyUserDataToTrack(Track paramTrack, boolean paramBoolean) throws StdQTException {
        StdQTException
                .checkError(CopyTrackUserData(_ID(), QTObject.ID(paramTrack), paramBoolean == true ? 1919970403 : 1835361639));
    }

    public void copyUserDataFromTrack(Track paramTrack, boolean paramBoolean) throws StdQTException {
        StdQTException
                .checkError(CopyTrackUserData(QTObject.ID(paramTrack), _ID(), paramBoolean == true ? 1919970403 : 1835361639));
    }

    public static void copyUserData(Track paramTrack1, Track paramTrack2, int paramInt) throws StdQTException {
        StdQTException.checkError(CopyTrackUserData(QTObject.ID(paramTrack1), QTObject.ID(paramTrack2), paramInt));
    }

    public String toString() {
        try {
            return super.toString() + "[ID=" + getID() + "]";
        } catch (StdQTException localStdQTException) {
        }
        return super.toString() + "[ID=unable to obtain track ID]";
    }

    private static native int GetMediaTrack(int paramInt);

    private static native short GetMoviesError();

    private static native int MovieImportFile(int paramInt1,
                                              byte[] paramArrayOfByte,
                                              int paramInt2,
                                              int paramInt3,
                                              int[] paramArrayOfInt1,
                                              int paramInt4,
                                              int[] paramArrayOfInt2,
                                              int paramInt5,
                                              int[] paramArrayOfInt3);

    private static native int MovieImportHandle(int paramInt1,
                                                int paramInt2,
                                                int paramInt3,
                                                int paramInt4,
                                                int[] paramArrayOfInt1,
                                                int paramInt5,
                                                int[] paramArrayOfInt2,
                                                int paramInt6,
                                                int[] paramArrayOfInt3);

    private static native int MovieImportDataRef(int paramInt1,
                                                 int paramInt2,
                                                 int paramInt3,
                                                 int paramInt4,
                                                 int paramInt5,
                                                 int[] paramArrayOfInt1,
                                                 int paramInt6,
                                                 int[] paramArrayOfInt2,
                                                 int paramInt7,
                                                 int[] paramArrayOfInt3);

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

    private static native void GetTrackNextInterestingTime(int paramInt1,
                                                           short paramShort,
                                                           int paramInt2,
                                                           int paramInt3,
                                                           int[] paramArrayOfInt1,
                                                           int[] paramArrayOfInt2);

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

    private static native void GetTrackLoadSettings(int paramInt,
                                                    int[] paramArrayOfInt1,
                                                    int[] paramArrayOfInt2,
                                                    int[] paramArrayOfInt3,
                                                    int[] paramArrayOfInt4);

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
