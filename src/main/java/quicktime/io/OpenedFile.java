package quicktime.io;

import java.io.IOException;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.FileManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.jdirect.QuickTimeLib;


/**
 * This class represents an OpenedFile. Subclasses define the type
 * of file open procedures that are used
 */
public abstract class OpenedFile extends QTObject implements QuickTimeLib {

    private static final byte rPerm = 1;
    private static final byte wPerm = 2;

    static final int kMovie = 1;
    static final int kStd = 2;

    OpenedFile(QTFile f, int permissions, int fileType) throws QTException {
        super(allocate(f, permissions, fileType));
        this.f = f;
        this.fileType = fileType;
    }

    private static int allocate(QTFile qtFile, int permissions, int fileType) throws QTIOException {
        try {
            QTFile.checkSecurity(qtFile.getCanonicalPath(), permissions);
        } catch (IOException e) {
            throw new QTIOException(e.getMessage());
        }
        short[] resRefNum = { 0 };
        byte b = 0;
        if ((permissions & 0x100) != 0) {
            b = 1;
        } else if ((permissions & 0x200) != 0)
            b = 2;
        else {
            throw new SecurityException(qtFile.getPath());
        }
        int i;
        if (fileType == 1) {
            i = OpenMovieFile(qtFile.getFSSpec(true, permissions), resRefNum, b);
        } else if (fileType == 2) {
            i = FSpOpenDF(qtFile.getFSSpec(true, permissions), b, resRefNum);
        } else {
            throw new QTIOException(-50, qtFile.getPath() + ":Unknown File Type=" + fileType);
        }
        QTIOException.checkError(i, qtFile.getPath());
        return resRefNum[0];
    }

    private boolean closed = false;

    private final QTFile f;

    private final int fileType;

    public final QTFile getFile() {
        return this.f;
    }

    /**
     * This method closes an open stream and the associated movie file.
     * @see "QuickTime::CloseMovieFile()"
     * @see "QuickTime::FSClose()"
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

    @Override
    protected final void _dispose() throws QTIOException {
        close();
    }

    private static short OpenMovieFile(byte[] fsspec, short[] resRefNum, byte permission) {
        return MovieToolbox.OpenMovieFile(fsspec, resRefNum, permission);
    }

    private static short CloseMovieFile(short resRefNum) {
        return MovieToolbox.CloseMovieFile(resRefNum);
    }

    private static short FSpOpenDF(byte[] fsspec, byte permission, short[] refNum) {
        return FileManager.FSpOpenDF(fsspec, permission, refNum);
    }

    private static short FSClose(short refNum) {
        return FileManager.FSClose(refNum);
    }
}
