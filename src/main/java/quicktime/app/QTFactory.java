package quicktime.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDrawable;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.Media;
import quicktime.std.qtcomponents.MovieImporter;
import quicktime.util.QTHandle;
import quicktime.util.QTUtils;

/**
 * Deprecated.
 * The QTFactory class provides factory methods for creating classes that are used
 * to present media that QuickTime can import. It also provides some utility methods
 * for finding directories and files in the local file system.
 *
 * An example usage of some of the factory methods:
 *
 * File mediaFile = QTFactory.findAbsolutePath ("media/myMovie.mov");
 * QTDrawable d = QTFactory.makeDrawable(new QTFile(mediaFile));
 * myQTCanvas.setClient (d);
 * ...
 * // from an URL
 * QTDrawable d2 = QTFactory.makeDrawable ("http://web.mywebsite.com/movies/myMovie.mov");
 * myOtherQTCanvas.setClient (d2);
 */
public final class QTFactory {

    private static Vector classPaths;

    private static final boolean debug = false;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static void buildClassPathsVector() throws IOException {
        classPaths = new Vector(8);
        String str1 = System.getProperty("java.class.path");
        while (str1 != null) {
            int i = str1.indexOf(File.pathSeparatorChar);
            String str3 = null;
            if (i == -1) {
                str3 = str1;
                str1 = null;
            } else {
                str3 = str1.substring(0, i);
                str1 = str1.substring(i + 1);
            }
            str3 = returnExistingFile(str3);
            if (str3 != null) {
                str3 = str3.substring(0, str3.lastIndexOf(File.separatorChar) + 1);
                if ((!classPaths.contains(str3)) && (str3.length() > 0)) {
                    classPaths.addElement(str3);
                }
            }
        }
        String str2 = System.getProperty("user.dir");
        str2 = returnExistingFile(str2);
        if (str2 != null)
            classPaths.addElement(str2);
    }

    private static String returnExistingFile(String paramString) throws IOException {
        File localFile = new File(paramString);
        if (!localFile.exists())
            return null;
        if ((localFile.isDirectory()) && (paramString.charAt(paramString.length() - 1) != File.separatorChar)) {
            return paramString + File.separatorChar;
        }
        return localFile.getCanonicalPath();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method will take apart the path specification of the fileName and return the
     * first instance of the file (the last name in the path) that it finds within the
     * directories that are known to Java at runtime.
     * @throws java.lang.SecurityException will be returned if the Applet is not allowed access to the class path
     *  system property. Typically this is the case - you should use the codeBase or documentBase paths of the
     *  applet to position and locate files at run time.
     * @throws java.io.IOException the file cannot be found in any of the class path locations.
     */
    public static File findInSystemPaths(String paramString) throws IOException {
        if (classPaths == null)
            buildClassPathsVector();
        return _doFISP(paramString, false);
    }

    private static File _doFISP(String paramString, boolean paramBoolean) throws IOException {
        String str1 = paramString;
        int i = str1.length();
        while (i > 0) {
            i = str1.substring(0, i).lastIndexOf('/');
            String str2 = (i == -1) || (i == 0) ? str1 : str1.substring(i + 1);
            for (int j = classPaths.size() - 1; j >= 0; j--) {
                String str3 = (String) classPaths.elementAt(j);
                String str4 = returnExistingFile(str3 + str2);
                if (str4 != null) {
                    return new File(str4);
                }
            }
        }
        throw new FileNotFoundException(paramString);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method will add a directory to the internal list of directories that are searched in
     * the find methods. The incoming File object must specify a directory that exists or an IOException
     * will be thrown. If the directory is already a member of the list it will not be added.
     * Directories that are added explicitly will be searched first in the find calls.
     * @param dir a directory
     * @param java.io.IOException
     */
    public static void addDirectory(File paramFile) throws IOException {
        if (!paramFile.exists())
            throw new IOException(paramFile + ",does not exist");
        if (!paramFile.isDirectory()) {
            throw new IOException(paramFile + ",must be a directory");
        }
        if (classPaths == null) {
            buildClassPathsVector();
        }
        String str = paramFile.getCanonicalPath();
        str = str.replace('/', File.separatorChar);
        if (str.charAt(str.length() - 1) != '/') {
            str = str + "/";
        }
        if (classPaths.contains(str)) {
            return;
        }
        classPaths.addElement(str);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method will remove a directory from the internal list of directories that are searched in
     * the find methods.
     * @param dir a directory
     * @param java.io.IOException
     */
    public static void removeDirectory(File paramFile) throws IOException {
        if (!paramFile.exists())
            return;
        if (!paramFile.isDirectory()) {
            return;
        }
        if (classPaths == null) {
            return;
        }
        String str = paramFile.getCanonicalPath();
        str = str.replace('/', File.separatorChar);
        if (str.charAt(str.length() - 1) != '/') {
            str = str + "/";
        }
        classPaths.removeElement(str);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Given a filename this method will exhaustively search all paths found in the user.dir and the classPath and
     * will return a file object that contains an absolute path to the specified argument.
     * @param fileName the file to search for.
     * @return a file object that represents the absolute path to the specified path.
     * @throws java.lang.SecurityException will be returned if the Applet is not allowed access to the class path
     *  system property. Typically this is the case - you should use the codeBase or documentBase paths of the
     *  applet to position and locate files at run time.
     * @throws java.io.IOException the file cannot be found in any of the class path locations.
     */
    public static File findAbsolutePath(String paramString) throws IOException {
        paramString = paramString.replace('/', File.separatorChar);
        if (classPaths == null) {
            buildClassPathsVector();
        }
        Enumeration localEnumeration = classPaths.elements();
        while (localEnumeration.hasMoreElements()) {
            String str1 = (String) localEnumeration.nextElement();
            String str2 = returnExistingFile(str1 + paramString);
            if (str2 != null) {
                return new File(str2);
            }
        }
        throw new FileNotFoundException(paramString);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This returns a QTDrawable object from the specified file. The file must specify an absolute path.
     * It uses the default QTDrawableMaker to return an GraphicsImporterDrawer object from a GraphicsImporter and a
     * QTPlayer from a Movie.
     * @return a drawable object that can present the QT File
     * @throws quicktime.std.StdQTException If the file is of a format that is not understood by QuickTime
     *  the function will throw a StdQTException with error code invalidMovie.
     * @throws java.io.IOException is thrown if the file would violate security settings of an applet
     * @throws quicktime.QTException
     * @see {@link quicktime.app.image.GraphicsImporterDrawer},
     * {@link quicktime.app.players.QTPlayer},
     * {@link quicktime.app.QTDrawableMaker}
     */
    public static QTDrawable makeDrawable(QTFile paramQTFile) throws IOException, QTException {
        return makeDrawable(paramQTFile, new QTDrawableMaker());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This returns a QTDrawable object from the specified file. The file must specify an absolute path.
     * It uses the supplied QTDrawableMaker to return objects from a GraphicsImporter or
     * a Movie.
     * @param maker specifies which type of QTDrawable object is returned when the factory imports a
     *  Graphics or Movie based media.
     * @return a drawable object that can present the QT File
     * @throws quicktime.std.StdQTException If the file is of a format that is not understood by QuickTime
     *  the function will throw a StdQTException with error code invalidMovie.
     * @throws java.io.IOException if the file is not found
     * @throws quicktime.QTException
     * @see {@link quicktime.app.image.GraphicsImporterDrawer},
     * {@link quicktime.app.players.QTPlayer},
     * {@link quicktime.app.QTDrawableMaker}
     */
    public static QTDrawable makeDrawable(QTFile paramQTFile, QTDrawableMaker paramQTDrawableMaker) throws IOException, QTException {
        if (!paramQTFile.exists())
            throw new FileNotFoundException(paramQTFile.getName());
        String str = getFileExtension(paramQTFile.getAbsolutePath());
        if (("mid".equalsIgnoreCase(str)) || ("smf".equalsIgnoreCase(str)) || ("midi".equalsIgnoreCase(str)) || ("kar".equalsIgnoreCase(str))) {
            Object localObject = readBytesIntoHandle(new FileInputStream(paramQTFile));
            Movie localMovie = importMovie((QTHandle) localObject, QTUtils.toOSType("Midi"));
            if (localMovie != null) {
                return paramQTDrawableMaker.fromMovie(localMovie);
            }
        }
        Object localObject = openAsImage(paramQTFile);
        Movie localMovie = null;
        if (localObject != null) {
            localMovie = checkIfMovie(paramQTFile);
            if (localMovie == null) {
                return paramQTDrawableMaker.fromGraphicsImporter((GraphicsImporter) localObject);
            }
            return paramQTDrawableMaker.fromMovie(localMovie);
        }
        localMovie = openAsMovie(paramQTFile);
        if (localMovie != null) {
            return paramQTDrawableMaker.fromMovie(localMovie);
        }
        throw new QTException(63533);
    }

    /**
     * @deprecated
     */
    public static QTDrawable makeDrawable(InputStream paramInputStream, int paramInt, String paramString) throws IOException, QTException {
        return makeDrawable(paramInputStream, paramInt, paramString, new QTDrawableMaker());
    }

    /**
     * @deprecated
     */
    public static QTDrawable makeDrawable(InputStream paramInputStream, int paramInt, String paramString, QTDrawableMaker paramQTDrawableMaker) throws IOException, QTException {
        QTHandle localQTHandle = readBytesIntoHandle(paramInputStream);
        DataRef localDataRef = new DataRef(localQTHandle, paramInt, paramString);
        if ((paramString != null) && (("mid".equalsIgnoreCase(paramString)) || ("smf".equalsIgnoreCase(paramString)) || ("midi".equalsIgnoreCase(paramString)) || ("kar".equalsIgnoreCase(paramString)))) {
            Object localObject = importMovie(localQTHandle, QTUtils.toOSType("Midi"));
            if (localObject != null) {
                return paramQTDrawableMaker.fromMovie((Movie) localObject);
            }
        }
        Object localObject = importImage(localDataRef);
        Movie localMovie = null;
        if (localObject != null) {
            localMovie = checkIfMovie(localDataRef);
            if (localMovie == null) {
                return paramQTDrawableMaker.fromGraphicsImporter((GraphicsImporter) localObject);
            }
            return paramQTDrawableMaker.fromMovie(localMovie);
        }
        localMovie = openAsMovie(localDataRef);
        if (localMovie != null) {
            return paramQTDrawableMaker.fromMovie(localMovie);
        }
        throw new QTException(63533);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This returns a QTDrawable object from the specified URL.
     * It uses the default QTDrawableMaker to return an GraphicsImporterDrawer from a GraphicsImporter or
     * a QTPlayer from a Movie.
     * @param url specifies the URL to the QT source media
     * @return a drawable object that can present a QT media
     * @throws quicktime.std.StdQTException If the file is of a format that is not understood by QuickTime
     *  the function will throw a StdQTException with error code invalidMovie.
     * @throws java.lang.SecurityException is thrown if the file would violate security settings of an applet
     * @throws quicktime.QTException
     */
    public static QTDrawable makeDrawable(String paramString) throws QTException {
        return makeDrawable(paramString, new QTDrawableMaker());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This returns a QTDrawable object from the specified Universal Resource Locator.
     * It uses the supplied QTDrawableMaker to return objects from a GraphicsImporter or
     * a Movie.
     * @param url is the fully qualified url to the QuickTime media.
     * @param maker specifies which type of QTDrawable object is returned when the factory imports a
     *  Graphics or Movie based media.
     * @return a drawable object that can present the QT File
     * @throws quicktime.std.StdQTException If the file is of a format that is not understood by QuickTime
     *  the function will throw a StdQTException with error code invalidMovie.
     * @throws java.lang.SecurityException is thrown if the file would violate security settings of an applet
     * @throws quicktime.QTException
     * @see {@link quicktime.app.image.GraphicsImporterDrawer},
     * {@link quicktime.app.players.QTPlayer},
     * {@link quicktime.app.QTDrawableMaker}
     */
    public static QTDrawable makeDrawable(String paramString, QTDrawableMaker paramQTDrawableMaker) throws QTException {
        if (paramString.startsWith("file:")) {
            paramString = paramString.substring(5);
            while ((paramString.charAt(0) == '/') && (paramString.charAt(1) == '/')) {
                paramString = paramString.substring(1);
            }
            try {
                return makeDrawable(new QTFile(paramString), paramQTDrawableMaker);
            } catch (IOException localIOException) {
                throw new QTException(-43);
            }
        }
        DataRef localDataRef = new DataRef(paramString);
        GraphicsImporter localGraphicsImporter = importImage(localDataRef);
        Movie localMovie = null;
        if (localGraphicsImporter != null) {
            localMovie = checkIfMovie(localDataRef);
            if (localMovie == null) {
                return paramQTDrawableMaker.fromGraphicsImporter(localGraphicsImporter);
            }
            return paramQTDrawableMaker.fromMovie(localMovie);
        }
        localMovie = openAsMovie(localDataRef);
        if (localMovie != null) {
            return paramQTDrawableMaker.fromMovie(localMovie);
        }
        throw new QTException(63533);
    }

    private static String getFileExtension(String paramString) {
        int i = paramString.lastIndexOf('.');
        return i == -1 ? "" : paramString.substring(i + 1);
    }

    private static QTHandle readBytesIntoHandle(InputStream paramInputStream) throws IOException, QTException {
        byte[] arrayOfByte = new byte[paramInputStream.available()];
        int i = paramInputStream.read(arrayOfByte);
        if ((i < 0) || (i != arrayOfByte.length))
            throw new IOException("Problem in reading bytes from InputStream");
        return new QTHandle(arrayOfByte);
    }

    private static GraphicsImporter openAsImage(QTFile paramQTFile) throws QTException {
        try {
            return new GraphicsImporter(paramQTFile);
        } catch (StdQTException localStdQTException) {
        }
        return null;
    }

    private static GraphicsImporter openAsImage(DataRef paramDataRef) throws QTException {
        try {
            return new GraphicsImporter(paramDataRef);
        } catch (StdQTException localStdQTException) {
        }
        return null;
    }

    private static GraphicsImporter importImage(DataRef paramDataRef) throws QTException {
        try {
            return new GraphicsImporter(paramDataRef);
        } catch (QTException localQTException) {
        }
        return null;
    }

    private static Movie checkIfMovie(QTFile paramQTFile) throws QTException {
        return checkMovie(Movie.fromFile(OpenMovieFile.asRead(paramQTFile), 0, null));
    }

    private static Movie checkIfMovie(DataRef paramDataRef) throws QTException {
        try {
            return checkMovie(Movie.fromDataRef(paramDataRef, 0));
        } catch (QTException localQTException) {
        }
        return null;
    }

    private static Movie checkMovie(Movie paramMovie) throws QTException {
        try {
            if (paramMovie.getTrackCount() > 1)
                return paramMovie;
            Track localTrack = paramMovie.getIndTrack(1);
            Media localMedia = Media.getTrackMedia(localTrack);
            if (localMedia.getSampleCount() > 1) {
                paramMovie.setActive(true);
                return paramMovie;
            }
            localMedia.disposeQTObject();
            localTrack.disposeQTObject();
            paramMovie.disposeQTObject();
        } catch (QTException localQTException) {
        }
        return null;
    }

    private static Movie openAsMovie(QTFile paramQTFile) throws QTException {
        OpenMovieFile localOpenMovieFile = OpenMovieFile.asRead(paramQTFile);
        try {
            return Movie.fromFile(localOpenMovieFile);
        } catch (StdQTException localStdQTException) {
        }
        return null;
    }

    private static Movie openAsMovie(DataRef paramDataRef) throws QTException {
        try {
            return Movie.fromDataRef(paramDataRef, 1);
        } catch (QTException localQTException) {
        }
        return null;
    }

    private static Movie importMovie(QTHandle paramQTHandle, int paramInt) throws QTException {
        if (paramInt == 0)
            throw new QTException(63533);
        try {
            MovieImporter localMovieImporter = new MovieImporter(paramInt);
            Movie localMovie = new Movie(1);
            localMovie.setDefaultDataRef(new DataRef(new QTHandle()));
            localMovieImporter.fromHandle(paramQTHandle, localMovie, null, 0, 1);
            return localMovie;
        } catch (QTException localQTException) {
        }
        return null;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * QTFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
