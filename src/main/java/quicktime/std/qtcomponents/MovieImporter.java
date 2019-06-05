
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

    public static MovieImporter fromTrack(Track paramTrack) throws StdQTException {
        int i = IsScrapMovie(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return new MovieImporter(i, null);
    }

    private MovieImporter(int paramInt, Object paramObject) throws StdQTException {
        super(paramInt, paramObject);
    }

    public MovieImporter() throws QTException {
        this(0);
    }

    public MovieImporter(int paramInt) throws QTException {
        super(1700885536, paramInt);
    }

    public MovieImporter(ComponentIdentifier paramComponentIdentifier) throws QTException {
        super(paramComponentIdentifier, 1700885536);
    }

    public MovieImporter(DataRef paramDataRef, int paramInt) throws QTException {
        this(allocate(paramDataRef, paramInt), dontOpenComponent);
    }

    public MovieImporter(DataRef paramDataRef, int paramInt, String paramString) throws QTException {
        this(allocate(paramDataRef, paramInt, paramString), dontOpenComponent);
    }

    private static int allocate(DataRef paramDataRef, int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(GetMovieImporterForDataRef(paramDataRef.getType(),
                                                             QTObject.ID(paramDataRef),
                                                             paramInt,
                                                             arrayOfInt));

        return arrayOfInt[0];
    }

    private static int allocate(DataRef paramDataRef, int paramInt, String paramString) throws QTException {
        int[] arrayOfInt = {
            0
        };
        StringHandle localStringHandle = new StringHandle("X." + paramString, 2);
        paramDataRef.concatenate(localStringHandle);
        StdQTException.checkError(GetMovieImporterForDataRef(paramDataRef.getType(),
                                                             QTObject.ID(paramDataRef),
                                                             paramInt,
                                                             arrayOfInt));

        return arrayOfInt[0];
    }

    private static Object dontOpenComponent = new Object();

    public MovieImportInfo fromFile(QTFile paramQTFile,
                                    Movie paramMovie,
                                    Track paramTrack,
                                    int paramInt1,
                                    int paramInt2) throws QTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        return Track.fromMovieImporterFile(this, paramQTFile, paramMovie, paramTrack, paramInt1, paramInt2);
    }

    public MovieImportInfo fromHandle(QTHandleRef paramQTHandleRef,
                                      Movie paramMovie,
                                      Track paramTrack,
                                      int paramInt1,
                                      int paramInt2) throws QTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        return Track.fromMovieImporterHandle(this, paramQTHandleRef, paramMovie, paramTrack, paramInt1, paramInt2);
    }

    public MovieImportInfo fromDataRef(DataRef paramDataRef,
                                       Movie paramMovie,
                                       Track paramTrack,
                                       int paramInt1,
                                       int paramInt2) throws QTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        return Track.fromMovieImporterDataRef(this, paramDataRef, paramMovie, paramTrack, paramInt1, paramInt2);
    }

    public void setSampleDuration(int paramInt1, int paramInt2) throws StdQTException {
        int i = MovieImportSetSampleDuration(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public void setSampleDescription(SampleDescription paramSampleDescription, int paramInt) throws StdQTException {
        int i = MovieImportSetSampleDescription(_ID(), QTObject.ID(paramSampleDescription), paramInt);

        StdQTException.checkError(i);
    }

    public void setMediaFile(AliasHandle paramAliasHandle) throws QTException {
        int i = MovieImportSetMediaFile(_ID(), QTObject.ID(paramAliasHandle));
        StdQTException.checkError(i);
    }

    public void setDimensions(float paramFloat1, float paramFloat2) throws StdQTException {
        int i = MovieImportSetDimensions(_ID(), QTUtils.X2Fix(paramFloat1), QTUtils.X2Fix(paramFloat2));

        StdQTException.checkError(i);
    }

    public void setChunkSize(int paramInt) throws StdQTException {
        int i = MovieImportSetChunkSize(_ID(), paramInt);
        StdQTException.checkError(i);
    }

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

    public void removeProgressProc() throws StdQTException {
        int i = MovieImportSetProgressProc(_ID(), 0, 0);
        if (this.progUPP != null) {
            this.progUPP.cleanupMethodClosure();
            this.progUPP = null;
        }
        StdQTException.checkError(i);
    }

    public void setAuxiliaryData(QTHandleRef paramQTHandleRef, int paramInt) throws StdQTException {
        int i = MovieImportSetAuxiliaryData(_ID(), QTObject.ID(paramQTHandleRef), paramInt);

        StdQTException.checkError(i);
    }

    public void setFromScrap(boolean paramBoolean) throws StdQTException {
        byte b = (byte) (paramBoolean ? 1 : 0);
        int i = MovieImportSetFromScrap(_ID(), b);
        StdQTException.checkError(i);
    }

    int res = 0;

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

    public boolean doUserDialog(QTHandleRef paramQTHandleRef) throws QTException {
        byte[] arrayOfByte = new byte[1];
        synchronized (QTNative.globalsLock) {
            int i = MovieImportDoUserDialog(_ID(), null, QTObject.ID(paramQTHandleRef), arrayOfByte);

            StdQTException.checkError(i);
        }
        return arrayOfByte[0] != 0;
    }

    public void setDuration(int paramInt) throws StdQTException {
        int i = MovieImportSetDuration(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    public int getAuxiliaryDataType() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieImportGetAuxiliaryDataType(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public boolean validate(QTFile paramQTFile) throws QTException {
        byte[] arrayOfByte = new byte[1];
        int i = MovieImportValidate(_ID(), paramQTFile.getFSSpec(true, 256), 0, arrayOfByte);

        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    public boolean validate(QTHandleRef paramQTHandleRef) throws QTException {
        byte[] arrayOfByte = new byte[1];
        int i = MovieImportValidate(_ID(), null, QTObject.ID(paramQTHandleRef), arrayOfByte);

        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    public int getFileType() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieImportGetFileType(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public SampleDescription getSampleDescription() throws StdQTException {
        return SampleDescription.fromMovieImporter(this);
    }

    public int getMediaType() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = MovieImportGetSampleDescription(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    public AtomContainer getMIMETypeList() throws StdQTException {
        return AtomContainer.fromMovieImporterMIME(this);
    }

    public void setOffsetAndLimit(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(MovieImportSetOffsetAndLimit(_ID(), paramInt1, paramInt2));
    }

    public void setImportSettingsFromAtomContainer(AtomContainer paramAtomContainer) throws StdQTException {
        int i = MovieImportSetSettingsFromAtomContainer(_ID(), QTObject.ID(paramAtomContainer));
        StdQTException.checkError(i);
    }

    public AtomContainer getImportSettingsFromAtomContainer() throws StdQTException {
        return AtomContainer.fromMovieImporterSettings(this);
    }

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

    private static native int MovieImportDoUserDialog(int paramInt1,
                                                      byte[] paramArrayOfByte1,
                                                      int paramInt2,
                                                      byte[] paramArrayOfByte2);

    private static native int MovieImportSetDuration(int paramInt1, int paramInt2);

    private static native int MovieImportGetAuxiliaryDataType(int paramInt, int[] paramArrayOfInt);

    private static native int MovieImportValidate(int paramInt1,
                                                  byte[] paramArrayOfByte1,
                                                  int paramInt2,
                                                  byte[] paramArrayOfByte2);

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
