package quicktime.io;

import quicktime.QTException;


/**
 * An object of this class represents an opened movie file
 * that is opened for reading or writing.
 */
public final class OpenMovieFile extends OpenedFile {

    /**
     * Open the specified movie file for reading.
     *
     * @param f the movie file to open
     * @return the opened movie file.
     * @throws quicktime.QTException
     * @see "QuickTime::OpenMovieFile()"
     */
    public static OpenMovieFile asRead(QTFile f) throws QTException {
        return new OpenMovieFile(f, 256);
    }

    /**
     * Open the specified movie file for writing.
     *
     * @param f the movie file to open
     * @return the opened movie file.
     * @throws quicktime.QTException
     * @see "QuickTime::OpenMovieFile()"
     */
    public static OpenMovieFile asWrite(QTFile f) throws QTException {
        return new OpenMovieFile(f, 512);
    }

    private OpenMovieFile(QTFile f, int permissions) throws QTException {
        super(f, permissions, 1);
    }
}
