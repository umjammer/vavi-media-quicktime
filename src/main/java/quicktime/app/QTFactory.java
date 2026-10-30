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
 * The QTFactory class provides factory methods for creating classes that are used
 * to present media that QuickTime can import. It also provides some utility methods
 * for finding directories and files in the local file system.
 * <p>
 * An example usage of some of the factory methods:
 * <pre>
 * File mediaFile = QTFactory.findAbsolutePath ("media/myMovie.mov");
 * QTDrawable d = QTFactory.makeDrawable(new QTFile(mediaFile));
 * myQTCanvas.setClient (d);
 * ...
 * // from an URL
 * QTDrawable d2 = QTFactory.makeDrawable ("http://web.mywebsite.com/movies/myMovie.mov");
 * myOtherQTCanvas.setClient (d2);
 * </pre>
 *
 * @deprecated
 */
public final class QTFactory {

    private static Vector<String> classPaths;

    private static final boolean debug = false;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static void buildClassPathsVector() throws IOException {
        classPaths = new Vector<>(8);
        String property = System.getProperty("java.class.path");
        while (property != null) {
            int i = property.indexOf(File.pathSeparatorChar);
            String path = null;
            if (i == -1) {
                path = property;
                property = null;
            } else {
                path = property.substring(0, i);
                property = property.substring(i + 1);
            }
            path = returnExistingFile(path);
            if (path != null) {
                path = path.substring(0, path.lastIndexOf(File.separatorChar) + 1);
                if ((!classPaths.contains(path)) && (!path.isEmpty())) {
                    classPaths.addElement(path);
                }
            }
        }
        String str2 = System.getProperty("user.dir");
        str2 = returnExistingFile(str2);
        if (str2 != null)
            classPaths.addElement(str2);
    }

    private static String returnExistingFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists())
            return null;
        if ((file.isDirectory()) && (path.charAt(path.length() - 1) != File.separatorChar)) {
            return path + File.separatorChar;
        }
        return file.getCanonicalPath();
    }

    /**
     * This method will take apart the path specification of the fileName and return the
     * first instance of the file (the last name in the path) that it finds within the
     * directories that are known to Java at runtime.
     *
     * @throws java.lang.SecurityException will be returned if the Applet is not allowed access to the class path
     *                                     system property. Typically this is the case - you should use the codeBase or documentBase paths of the
     *                                     applet to position and locate files at run time.
     * @throws java.io.IOException         the file cannot be found in any of the class path locations.
     * @since QTJava 6.1
     * @deprecated
     */
    public static File findInSystemPaths(String fileName) throws IOException {
        if (classPaths == null)
            buildClassPathsVector();
        return _doFISP(fileName, false);
    }

    private static File _doFISP(String fileName, boolean searchSubDirs) throws IOException {
        String str1 = fileName;
        int i = str1.length();
        while (i > 0) {
            i = str1.substring(0, i).lastIndexOf('/');
            String str2 = (i == -1) || (i == 0) ? str1 : str1.substring(i + 1);
            for (int j = classPaths.size() - 1; j >= 0; j--) {
                String str3 = classPaths.elementAt(j);
                String str4 = returnExistingFile(str3 + str2);
                if (str4 != null) {
                    return new File(str4);
                }
            }
        }
        throw new FileNotFoundException(fileName);
    }

    /**
     * This method will add a directory to the internal list of directories that are searched in
     * the find methods. The incoming File object must specify a directory that exists or an IOException
     * will be thrown. If the directory is already a member of the list it will not be added.
     * Directories that are added explicitly will be searched first in the find calls.
     *
     * @param dir a directory
     * @rgrows java.io.IOException
     * @since QTJava 6.1
     * @deprecated
     */
    public static void addDirectory(File dir) throws IOException {
        if (!dir.exists())
            throw new IOException(dir + ",does not exist");
        if (!dir.isDirectory()) {
            throw new IOException(dir + ",must be a directory");
        }
        if (classPaths == null) {
            buildClassPathsVector();
        }
        String canonicalPath = dir.getCanonicalPath();
        canonicalPath = canonicalPath.replace('/', File.separatorChar);
        if (canonicalPath.charAt(canonicalPath.length() - 1) != '/') {
            canonicalPath = canonicalPath + "/";
        }
        if (classPaths.contains(canonicalPath)) {
            return;
        }
        classPaths.addElement(canonicalPath);
    }

    /**
     * This method will remove a directory from the internal list of directories that are searched in
     * the find methods.
     *
     * @param dir a directory
     * @rgrows java.io.IOException
     * @since QTJava 6.1
     * @deprecated
     */
    public static void removeDirectory(File dir) throws IOException {
        if (!dir.exists())
            return;
        if (!dir.isDirectory()) {
            return;
        }
        if (classPaths == null) {
            return;
        }
        String canonicalPath = dir.getCanonicalPath();
        canonicalPath = canonicalPath.replace('/', File.separatorChar);
        if (canonicalPath.charAt(canonicalPath.length() - 1) != '/') {
            canonicalPath = canonicalPath + "/";
        }
        classPaths.removeElement(canonicalPath);
    }

    /**
     * Given a filename this method will exhaustively search all paths found in the user.dir and the classPath and
     * will return a file object that contains an absolute path to the specified argument.
     *
     * @param fileName the file to search for.
     * @return a file object that represents the absolute path to the specified path.
     * @throws java.lang.SecurityException will be returned if the Applet is not allowed access to the class path
     *                                     system property. Typically this is the case - you should use the codeBase or documentBase paths of the
     *                                     applet to position and locate files at run time.
     * @throws java.io.IOException         the file cannot be found in any of the class path locations.
     * @since QTJava 6.1
     * @deprecated
     */
    public static File findAbsolutePath(String fileName) throws IOException {
        fileName = fileName.replace('/', File.separatorChar);
        if (classPaths == null) {
            buildClassPathsVector();
        }
        Enumeration enumeration = classPaths.elements();
        while (enumeration.hasMoreElements()) {
            String str1 = (String) enumeration.nextElement();
            String str2 = returnExistingFile(str1 + fileName);
            if (str2 != null) {
                return new File(str2);
            }
        }
        throw new FileNotFoundException(fileName);
    }

    /**
     * This returns a QTDrawable object from the specified file. The file must specify an absolute path.
     * It uses the default QTDrawableMaker to return an GraphicsImporterDrawer object from a GraphicsImporter and a
     * QTPlayer from a Movie.
     *
     * @return a drawable object that can present the QT File
     * @throws quicktime.std.StdQTException If the file is of a format that is not understood by QuickTime
     *                                      the function will throw a StdQTException with error code invalidMovie.
     * @throws java.io.IOException          is thrown if the file would violate security settings of an applet
     * @throws quicktime.QTException
     * @see quicktime.app.image.GraphicsImporterDrawer
     * @see quicktime.app.players.QTPlayer
     * @see quicktime.app.QTDrawableMaker
     * @since QTJava 6.1
     * @deprecated
     */
    public static QTDrawable makeDrawable(QTFile file) throws IOException, QTException {
        return makeDrawable(file, new QTDrawableMaker());
    }

    /**
     * This returns a QTDrawable object from the specified file. The file must specify an absolute path.
     * It uses the supplied QTDrawableMaker to return objects from a GraphicsImporter or
     * a Movie.
     *
     * @param maker specifies which type of QTDrawable object is returned when the factory imports a
     *              Graphics or Movie based media.
     * @return a drawable object that can present the QT File
     * @throws quicktime.std.StdQTException If the file is of a format that is not understood by QuickTime
     *                                      the function will throw a StdQTException with error code invalidMovie.
     * @throws java.io.IOException          if the file is not found
     * @throws quicktime.QTException
     * @see quicktime.app.image.GraphicsImporterDrawer
     * @see quicktime.app.players.QTPlayer
     * @see quicktime.app.QTDrawableMaker
     * @since QTJava 6.1
     * @deprecated
     */
    public static QTDrawable makeDrawable(QTFile file, QTDrawableMaker maker) throws IOException, QTException {
        if (!file.exists())
            throw new FileNotFoundException(file.getName());
        String fileExtension = getFileExtension(file.getAbsolutePath());
        if (("mid".equalsIgnoreCase(fileExtension)) || ("smf".equalsIgnoreCase(fileExtension)) || ("midi".equalsIgnoreCase(fileExtension)) || ("kar".equalsIgnoreCase(fileExtension))) {
            QTHandle qtHandle = readBytesIntoHandle(new FileInputStream(file));
            Movie movie = importMovie(qtHandle, QTUtils.toOSType("Midi"));
            if (movie != null) {
                return maker.fromMovie(movie);
            }
        }
        GraphicsImporter asImage = openAsImage(file);
        Movie asMovie = null;
        if (asImage != null) {
            asMovie = checkIfMovie(file);
            if (asMovie == null) {
                return maker.fromGraphicsImporter(asImage);
            }
            return maker.fromMovie(asMovie);
        }
        asMovie = openAsMovie(file);
        if (asMovie != null) {
            return maker.fromMovie(asMovie);
        }
        throw new QTException(63533);
    }

    /**
     * This returns a QTDrawable object from the specified input stream.
     * The input stream will be read in its entirety to a byte array which is then used as the
     * source of the data to import. The hints are used by the importers to determine
     * the format of the data contained within the input stream. Hints must be given to
     * QuickTime about the format of the data
     * that is contained within the InputStream. Hints can take one of three forms
     * <pree>
     * MIME Type -> kDataRefMimeTypeTag
     * MacOS File Type -> kDataRefQTFileTypeTag
     * File extension -> kDataRefFileExtensionTag
     * </pree>
     * The preferred usage is to pass in either the MacOS File Type (which will also work on non-MacOS platforms)
     * or the MIME type information. The File extension is a more arbitrary indicator and should be the least
     * referred to. It uses the default QTDrawableMaker to return objects from a GraphicsImporter or
     * a Movie.
     *
     * @param is         the input stream from which the bytes will be read
     * @param hintString contains the hint information
     * @return a drawable object that can present the QT File
     * @throws quicktime.std.StdQTException If the data is of a format that is not understood by QuickTime
     *                                      the function will throw a StdQTException with error code invalidMovie.
     * @throws java.io.IOException          is thrown if the input stream is unable to aquire all the data
     * @throws quicktime.QTException
     * @see quicktime.app.image.GraphicsImporterDrawer
     * @see quicktime.app.players.QTPlayer
     * @see quicktime.app.QTDrawableMaker
     * @since QTJava 6.1
     * @deprecated
     */
    public static QTDrawable makeDrawable(InputStream is, int hintFlag, String hintString) throws IOException, QTException {
        return makeDrawable(is, hintFlag, hintString, new QTDrawableMaker());
    }

    /**
     * This returns a QTDrawable object from the specified input stream.
     * The input stream will be read in its entirety to a byte array which is then used as the
     * source of the data to import. The hints are used by the importers to determine
     * the format of the data contained within the input stream. Hints must be given to
     * QuickTime about the format of the data
     * that is contained within the InputStream. Hints can take one of three forms
     * <p>
     * MIME Type -> kDataRefMimeTypeTag
     * MacOS File Type -> kDataRefQTFileTypeTag
     * File extension -> kDataRefFileExtensionTag
     * <p>
     * The preferred usage is to pass in either the MacOS File Type (which will also work on non-MacOS platforms)
     * or the MIME type information. The File extension is a more arbitrary indicator and should be the least
     * referred to. It uses the default QTDrawableMaker to return objects from a GraphicsImporter or
     * a Movie.
     *
     * @param hintString contains the hint information
     * @param maker      specifies which type of QTDrawable object is returned when the factory imports a
     *                   Graphics or Movie based media.
     * @return a drawable object that can present the QT File
     * @throws quicktime.std.StdQTException If the data is of a format that is not understood by QuickTime
     *                                      the function will throw a StdQTException with error code invalidMovie.
     * @throws java.io.IOException          is thrown if the input stream is unable to aquire all the data
     * @throws quicktime.QTException
     * @see quicktime.app.image.GraphicsImporterDrawer,
     * {@link quicktime.app.players.QTPlayer},
     * {@link quicktime.app.QTDrawableMaker}
     * @since QTJava 6.1
     * @deprecated
     */
    public static QTDrawable makeDrawable(InputStream inputStream, int hintFlag, String hintString, QTDrawableMaker maker) throws IOException, QTException {
        QTHandle qtHandle = readBytesIntoHandle(inputStream);
        DataRef defaultDataRef = new DataRef(qtHandle, hintFlag, hintString);
        if ((hintString != null) && (("mid".equalsIgnoreCase(hintString)) || ("smf".equalsIgnoreCase(hintString)) || ("midi".equalsIgnoreCase(hintString)) || ("kar".equalsIgnoreCase(hintString)))) {
            Movie movie = importMovie(qtHandle, QTUtils.toOSType("Midi"));
            if (movie != null) {
                return maker.fromMovie(movie);
            }
        }
        GraphicsImporter graphicsImporter = importImage(defaultDataRef);
        Movie asMovie = null;
        if (graphicsImporter != null) {
            asMovie = checkIfMovie(defaultDataRef);
            if (asMovie == null) {
                return maker.fromGraphicsImporter(graphicsImporter);
            }
            return maker.fromMovie(asMovie);
        }
        asMovie = openAsMovie(defaultDataRef);
        if (asMovie != null) {
            return maker.fromMovie(asMovie);
        }
        throw new QTException(63533);
    }

    /**
     * This returns a QTDrawable object from the specified URL.
     * It uses the default QTDrawableMaker to return an GraphicsImporterDrawer from a GraphicsImporter or
     * a QTPlayer from a Movie.
     *
     * @param url specifies the URL to the QT source media
     * @return a drawable object that can present a QT media
     * @throws quicktime.std.StdQTException If the file is of a format that is not understood by QuickTime
     *                                      the function will throw a StdQTException with error code invalidMovie.
     * @throws java.lang.SecurityException  is thrown if the file would violate security settings of an applet
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    public static QTDrawable makeDrawable(String url) throws QTException {
        return makeDrawable(url, new QTDrawableMaker());
    }

    /**
     * This returns a QTDrawable object from the specified Universal Resource Locator.
     * It uses the supplied QTDrawableMaker to return objects from a GraphicsImporter or
     * a Movie.
     *
     * @param url   is the fully qualified url to the QuickTime media.
     * @param maker specifies which type of QTDrawable object is returned when the factory imports a
     *              Graphics or Movie based media.
     * @return a drawable object that can present the QT File
     * @throws quicktime.std.StdQTException If the file is of a format that is not understood by QuickTime
     *                                      the function will throw a StdQTException with error code invalidMovie.
     * @throws java.lang.SecurityException  is thrown if the file would violate security settings of an applet
     * @throws quicktime.QTException
     * @see quicktime.app.image.GraphicsImporterDrawer,
     * {@link quicktime.app.players.QTPlayer},
     * {@link quicktime.app.QTDrawableMaker}
     * @since QTJava 6.1
     * @deprecated
     */
    public static QTDrawable makeDrawable(String url, QTDrawableMaker maker) throws QTException {
        if (url.startsWith("file:")) {
            url = url.substring(5);
            while ((url.charAt(0) == '/') && (url.charAt(1) == '/')) {
                url = url.substring(1);
            }
            try {
                return makeDrawable(new QTFile(url), maker);
            } catch (IOException e) {
                throw new QTException(-43);
            }
        }
        DataRef defaultDataRef = new DataRef(url);
        GraphicsImporter graphicsImporter = importImage(defaultDataRef);
        Movie asMovie = null;
        if (graphicsImporter != null) {
            asMovie = checkIfMovie(defaultDataRef);
            if (asMovie == null) {
                return maker.fromGraphicsImporter(graphicsImporter);
            }
            return maker.fromMovie(asMovie);
        }
        asMovie = openAsMovie(defaultDataRef);
        if (asMovie != null) {
            return maker.fromMovie(asMovie);
        }
        throw new QTException(63533);
    }

    private static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        return i == -1 ? "" : fileName.substring(i + 1);
    }

    private static QTHandle readBytesIntoHandle(InputStream inputStream) throws IOException, QTException {
        byte[] byteArray = new byte[inputStream.available()];
        int i = inputStream.read(byteArray);
        if ((i < 0) || (i != byteArray.length))
            throw new IOException("Problem in reading bytes from InputStream");
        return new QTHandle(byteArray);
    }

    private static GraphicsImporter openAsImage(QTFile file) throws QTException {
        try {
            return new GraphicsImporter(file);
        } catch (StdQTException _) {
        }
        return null;
    }

    private static GraphicsImporter openAsImage(DataRef dataRef) throws QTException {
        try {
            return new GraphicsImporter(dataRef);
        } catch (StdQTException _) {
        }
        return null;
    }

    private static GraphicsImporter importImage(DataRef dataRef) throws QTException {
        try {
            return new GraphicsImporter(dataRef);
        } catch (QTException _) {
        }
        return null;
    }

    private static Movie checkIfMovie(QTFile qtFile) throws QTException {
        return checkMovie(Movie.fromFile(OpenMovieFile.asRead(qtFile), 0, null));
    }

    private static Movie checkIfMovie(DataRef defaultDataRef) throws QTException {
        try {
            return checkMovie(Movie.fromDataRef(defaultDataRef, 0));
        } catch (QTException _) {
        }
        return null;
    }

    private static Movie checkMovie(Movie movie) throws QTException {
        try {
            if (movie.getTrackCount() > 1)
                return movie;
            Track theTrack = movie.getIndTrack(1);
            Media trackMedia = Media.getTrackMedia(theTrack);
            if (trackMedia.getSampleCount() > 1) {
                movie.setActive(true);
                return movie;
            }
            trackMedia.disposeQTObject();
            theTrack.disposeQTObject();
            movie.disposeQTObject();
        } catch (QTException _) {
        }
        return null;
    }

    private static Movie openAsMovie(QTFile qtFile) throws QTException {
        OpenMovieFile fileIn = OpenMovieFile.asRead(qtFile);
        try {
            return Movie.fromFile(fileIn);
        } catch (StdQTException _) {
        }
        return null;
    }

    private static Movie openAsMovie(DataRef defaultDataRef) throws QTException {
        try {
            return Movie.fromDataRef(defaultDataRef, 1);
        } catch (QTException _) {
        }
        return null;
    }

    private static Movie importMovie(QTHandle qtHandle, int subType) throws QTException {
        if (subType == 0)
            throw new QTException(63533);
        try {
            MovieImporter movieImporter = new MovieImporter(subType);
            Movie theMovie = new Movie(1);
            theMovie.setDefaultDataRef(new DataRef(new QTHandle()));
            movieImporter.fromHandle(qtHandle, theMovie, null, 0, 1);
            return theMovie;
        } catch (QTException _) {
        }
        return null;
    }
}
