package quicktime.io;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.FileManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;

/**
 * This class represents an OpenedFile. Subclasses define the type
 * of file open procedures that are used
 */
public abstract class OpenedFile extends QTObject implements QuickTimeLib {

    private static Object linkage;

    private static final byte rPerm = 1;

    private static final byte wPerm = 2;

    static final int kMovie = 1;

    static final int kStd = 2;

    static {
        class PrivelegedAction {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(OpenedFile.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    OpenedFile(QTFile paramQTFile, int paramInt1, int paramInt2) throws QTException {
        super(allocate(paramQTFile, paramInt1, paramInt2));
        this.f = paramQTFile;
        this.fileType = paramInt2;
    }

    private static int allocate(QTFile paramQTFile, int paramInt1, int paramInt2) throws QTIOException {
        try {
            QTFile.checkSecurity(paramQTFile.getCanonicalPath(), paramInt1);
        } catch (IOException localIOException) {
            throw new QTIOException(localIOException.getMessage());
        }
        short[] arrayOfShort = { 0 };
        byte b = 0;
        if ((paramInt1 & 0x100) != 0) {
            b = 1;
        } else if ((paramInt1 & 0x200) != 0)
            b = 2;
        else {
            throw new SecurityException(paramQTFile.getPath());
        }
        int i;
        if (paramInt2 == 1) {
            i = OpenMovieFile(paramQTFile.getFSSpec(true, paramInt1), arrayOfShort, b);
        } else if (paramInt2 == 2) {
            i = FSpOpenDF(paramQTFile.getFSSpec(true, paramInt1), b, arrayOfShort);
        } else {
            throw new QTIOException(-50, paramQTFile.getPath() + ":Unknown File Type=" + paramInt2);
        }
        QTIOException.checkError(i, paramQTFile.getPath());
        return arrayOfShort[0];
    }

    private boolean closed = false;

    private QTFile f;

    private int fileType;

    public final QTFile getFile() {
        return this.f;
    }

    /**
     * This method closes an open stream and the associated movie file.
     * QuickTime::CloseMovieFile()
     * QuickTime::FSClose()
     */
    public final void close() throws QTIOException {
        if (this.closed)
            return;
        if (QTObject.ID(this) == 0)
            return;
        int i;
        if (this.fileType == 1) {
            i = CloseMovieFile((short) _ID());
        } else if (this.fileType == 2) {
            i = FSClose((short) _ID());
        } else {
            throw new QTIOException(-50, this.f.getPath() + ":Unknown File Type:" + this.fileType);
        }
        QTIOException.checkError(i, this.f.getPath());
        this.closed = true;
    }

    protected final void _dispose() throws QTIOException {
        close();
    }

    private static short OpenMovieFile(byte[] paramArrayOfByte, short[] paramArrayOfShort, byte paramByte) {
        return MovieToolbox.OpenMovieFile(paramArrayOfByte, paramArrayOfShort, paramByte);
    }

    private static short CloseMovieFile(short paramShort) {
        return MovieToolbox.CloseMovieFile(paramShort);
    }

    private static short FSpOpenDF(byte[] paramArrayOfByte, byte paramByte, short[] paramArrayOfShort) {
        return FileManager.FSpOpenDF(paramArrayOfByte, paramByte, paramArrayOfShort);
    }

    private static short FSClose(short paramShort) {
        return FileManager.FSClose(paramShort);
    }
}

