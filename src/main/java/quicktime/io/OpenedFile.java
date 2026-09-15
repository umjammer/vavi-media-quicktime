package quicktime.io;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
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

    private static int nextFileId = 1;

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
        return nextFileId++;
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
        this.closed = true;
    }

    protected final void _dispose() throws QTIOException {
        close();
    }

    private static short OpenMovieFile(byte[] paramArrayOfByte, short[] paramArrayOfShort, byte paramByte) { return 0; }

    private static short CloseMovieFile(short paramShort) { return 0; }

    private static short FSpOpenDF(byte[] paramArrayOfByte, byte paramByte, short[] paramArrayOfShort) { return 0; }

    private static short FSClose(short paramShort) { return 0; }
}

