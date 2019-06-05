
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

    public MovieExporter(int paramInt) throws QTException {
        super(1936746868, paramInt);
        setProgressProc(null);
    }

    public MovieExporter(ComponentIdentifier paramComponentIdentifier) throws QTException {
        super(paramComponentIdentifier, 1936746868);
        setProgressProc(null);
    }

    public void setProgressProc(MovieProgress paramMovieProgress) throws StdQTException {
        int i = 0;
        if (this.progUPP != null)
            this.progUPP.cleanupMethodClosure();
        if (paramMovieProgress != null) {
            this.progUPP = new QTCompDispatcher(paramMovieProgress);
        } else {
            this.progUPP = new QTCompDispatcher(this);
        }
        synchronized (QTNative.globalsLock) {
            i = MovieExportSetProgressProc(_ID(), this.progUPP.ID(), 0);
        }
        StdQTException.checkError(i);
    }

    public void toFile(QTFile paramQTFile,
                       Movie paramMovie,
                       Track paramTrack,
                       int paramInt1,
                       int paramInt2) throws QTException {
        int i = 0;
        if (this.progUPP != null) {
            this.progUPP.theCurrentMov = paramMovie;
        }

        synchronized (QTNative.globalsLock) {
            i = MovieExportToFile(_ID(),
                                  paramQTFile.getFSSpec(true, 512),
                                  QTObject.ID(paramMovie),
                                  QTObject.ID(paramTrack),
                                  paramInt1,
                                  paramInt2);
        }

        StdQTException.checkError(i);
    }

    public QTHandle toHandle(Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        QTHandle localQTHandle = new QTHandle();
        int i = MovieExportToHandle(_ID(),
                                    QTObject.ID(localQTHandle),
                                    QTObject.ID(paramMovie),
                                    QTObject.ID(paramTrack),
                                    paramInt1,
                                    paramInt2);

        StdQTException.checkError(i);
        return localQTHandle;
    }

    public void toDataRef(DataRef paramDataRef,
                          Movie paramMovie,
                          Track paramTrack,
                          int paramInt1,
                          int paramInt2) throws StdQTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        StdQTException.checkError(MovieExportToDataRef(_ID(),
                                                       QTObject.ID(paramDataRef),
                                                       paramDataRef.getType(),
                                                       QTObject.ID(paramMovie),
                                                       QTObject.ID(paramTrack),
                                                       paramInt1,
                                                       paramInt2));
    }

    public int getAuxiliaryData(QTHandle paramQTHandle) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieExportGetAuxiliaryData(_ID(), QTObject.ID(paramQTHandle), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void setSampleDescription(SampleDescription paramSampleDescription, int paramInt) throws StdQTException {
        int i = MovieExportSetSampleDescription(_ID(), QTObject.ID(paramSampleDescription), paramInt);
        StdQTException.checkError(i);
    }

    public boolean doUserDialog(Movie paramMovie, Track paramTrack, int paramInt1, int paramInt2) throws StdQTException {
        final byte[] arrayOfByte = new byte[1];
        final Movie localMovie = paramMovie;
        final Track localTrack = paramTrack;
        final int i = paramInt1;
        final int j = paramInt2;
        final int k = _ID();

        int m = 0;

        if (QTSession.isCurrentOS(4)) {
            m = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                public Object invoke() {
                    int l = 0;
                    synchronized (QTNative.globalsLock) {
                        l = MovieExporter.MovieExportDoUserDialog(k,
                                                                  QTObject.ID(localMovie),
                                                                  QTObject.ID(localTrack),
                                                                  i,
                                                                  j,
                                                                  arrayOfByte);
                    }

                    return l;
                }
            })).intValue();
            StdQTException.checkError(m);
        } else {
            synchronized (QTNative.globalsLock) {
                m = MovieExportDoUserDialog(_ID(),
                                            QTObject.ID(paramMovie),
                                            QTObject.ID(paramTrack),
                                            paramInt1,
                                            paramInt2,
                                            arrayOfByte);

                StdQTException.checkError(m);
            }
        }
        return arrayOfByte[0] != 0;
    }

    public int getCreatorType() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieExportGetCreatorType(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public boolean validate(Movie paramMovie, Track paramTrack) throws StdQTException {
        if (this.progUPP != null)
            this.progUPP.theCurrentMov = paramMovie;
        byte[] arrayOfByte = {
            0
        };
        StdQTException.checkError(MovieExportValidate(_ID(), QTObject.ID(paramMovie), QTObject.ID(paramTrack), arrayOfByte));

        return arrayOfByte[0] != 0;
    }

    public AtomContainer getExportSettingsFromAtomContainer() throws StdQTException {
        return AtomContainer.fromMovieExporter(this);
    }

    public void setExportSettingsFromAtomContainer(AtomContainer paramAtomContainer) throws StdQTException {
        int i = MovieExportSetSettingsFromAtomContainer(_ID(), QTObject.ID(paramAtomContainer));
        StdQTException.checkError(i);
    }

    public final int getExportFileNameExtension() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieExportGetFileNameExtension(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public final String getExportShortFileTypeString() throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int i = MovieExportGetShortFileTypeString(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

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

    private static native int MovieExportToHandle(int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4,
                                                  int paramInt5,
                                                  int paramInt6);

    private static native int MovieExportToDataRef(int paramInt1,
                                                   int paramInt2,
                                                   int paramInt3,
                                                   int paramInt4,
                                                   int paramInt5,
                                                   int paramInt6,
                                                   int paramInt7);

    private static native int MovieExportGetAuxiliaryData(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int MovieExportSetProgressProc(int paramInt1, int paramInt2, int paramInt3);

    private static native int MovieExportSetSampleDescription(int paramInt1, int paramInt2, int paramInt3);

    private static native int MovieExportDoUserDialog(int paramInt1,
                                                      int paramInt2,
                                                      int paramInt3,
                                                      int paramInt4,
                                                      int paramInt5,
                                                      byte[] paramArrayOfByte);

    private static native int MovieExportGetCreatorType(int paramInt, int[] paramArrayOfInt);

    private static native int MovieExportValidate(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

    private static native int MovieExportSetSettingsFromAtomContainer(int paramInt1, int paramInt2);

    private static native int MovieExportGetFileNameExtension(int paramInt, int[] paramArrayOfInt);

    private static native int MovieExportGetShortFileTypeString(int paramInt, byte[] paramArrayOfByte);

    private static native int MovieExportGetSourceMediaType(int paramInt, int[] paramArrayOfInt);

    private static native int MovieExportToFile(int paramInt1,
                                                byte[] paramArrayOfByte,
                                                int paramInt2,
                                                int paramInt3,
                                                int paramInt4,
                                                int paramInt5);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/MovieExporter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
