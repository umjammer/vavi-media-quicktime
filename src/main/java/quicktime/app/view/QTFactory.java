package quicktime.app.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.Media;
import quicktime.std.qtcomponents.MovieImporter;
import quicktime.util.QTHandle;

/**
 * The QTFactory class provides factory methods for creating classes that are used
 * to present media that QuickTime can import. It also provides some utility methods
 * for finding directories and files in the local file system.
 *
 * An example usage of some of the factory methods:
 *
 * File mediaFile = QTFactory.findAbsolutePath ("media/myMovie.mov");
 * Movie movie = Movie.fromFile(mediaFile);
 * Component component = QTFactory.makeQTComponent (movie);
 * frame.add (component);
 */
public final class QTFactory {

    private static Vector<String> classPaths;

    private static final boolean debug = false;

    /**
     * Accepts a movie to display in the java component.
     * @param movie the movie to be displayed
     * @return QTComponent displaying the movie, this can be added to the FullScreenWindow for displaying movie
     *  in fullscreen mode.
     * @throws quicktime.QTException
     */
    public static QTComponent makeQTComponent(Movie mov) throws QTException {
        if (QTSession.isCurrentOS(4)) {
            // the toolbox is emulated, components draw with Java 2D
            return new QTJavaJ2DCanvas(mov);
        }
        if (QTSession.isCurrentOS(4) && QTSession.getJavaVersion() >= 65540) {
            class PrivelegedAction {

                QTComponent comp;

                void establish() {
                    AccessController.doPrivileged((PrivilegedAction<QTComponent>) () -> {
                        comp = new QTJavaCocoaCanvas(mov);
                        return comp;
                    });
                }

                QTComponent getComponent() {
                    return this.comp;
                }
            }
            PrivelegedAction privelegedAction = new PrivelegedAction();
            privelegedAction.establish();
            return privelegedAction.getComponent();
        }
        return new QTJavaAWTCanvas(mov);
    }

    /**
     * Accepts a movie and a controller as its parameters to display in the java component.
     * @param ctlr the MovieController attached to the movie, if this is NULL the class creates its own controller object.
     *  you can use the getMovieController() call of the component controller to get this movie controller.
     * @return QTComponent displaying the movie and the controller attached to it.
     * @throws quicktime.QTException
     */
    public static QTComponent makeQTComponent(MovieController controller) throws QTException {
        if (QTSession.isCurrentOS(4)) {
            return new QTJavaJ2DCanvas(controller);
        }
        if (QTSession.isCurrentOS(4) && QTSession.getJavaVersion() >= 65540) {
            class PrivelegedAction {

                QTComponent comp;

                void establish() {
                    AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                        try {
                            comp = new QTJavaCocoaCanvas(controller);
                        } catch (QTException e) {
                        }
                        return comp;
                    });
                }

                QTComponent getComponent() {
                    return this.comp;
                }
            }
            PrivelegedAction privelegedAction = new PrivelegedAction();
            privelegedAction.establish();
            return privelegedAction.getComponent();
        }
        return new QTJavaAWTCanvas(controller);
    }

    /**
     * Accepts a GraphicsImporter as its parameters to display in the java component.
     * @param graphicsImporter the image to be displayed
     * @return QTComponent displaying the image represented by the GraphicsImporter component.
     * @throws quicktime.QTException
     */
    public static QTComponent makeQTComponent(GraphicsImporter gi) throws QTException {
        if (QTSession.isCurrentOS(4)) {
            return new QTJavaJ2DCanvas(gi);
        }
        if (QTSession.isCurrentOS(4) && QTSession.getJavaVersion() >= 65540) {
            class PrivelegedAction {

                QTComponent comp;

                void establish() {
                    AccessController.doPrivileged((PrivilegedAction<QTComponent>) () -> {
                        comp = new QTJavaCocoaCanvas(gi);
                        return comp;
                    });
                }

                QTComponent getComponent() {
                    return this.comp;
                }
            }
            PrivelegedAction privelegedAction = new PrivelegedAction();
            privelegedAction.establish();
            return privelegedAction.getComponent();
        }
        return new QTJavaAWTCanvas(gi);
    }

    /**
     * Accepts a GraphicsImporter as its parameters to display in the java component.
     * @return javax.swing.JComponent displaying the image represented by the GraphicsImporter component.
     * @throws quicktime.QTException
     */
    public static QTJComponent makeQTJComponent(GraphicsImporterDrawer paramGraphicsImporterDrawer) throws QTException {
        return new JQTCanvas(paramGraphicsImporterDrawer);
    }

    /**
     * Accepts a MoviePlayer object as its parameters to display in the java component.
     * @param moviePlayer the movie to be displayed
     * @return javax.swing.JComponent displaying the movie represented by the MoviePlayer object.
     * @throws quicktime.QTException
     */
    public static QTJComponent makeQTJComponent(MoviePlayer paramMoviePlayer) throws QTException {
        return new JQTCanvas(paramMoviePlayer);
    }

    /**
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

    /**
     * This method will add a directory to the internal list of directories that are searched in
     * the find methods. The incoming File object must specify a directory that exists or an IOException
     * will be thrown. If the directory is already a member of the list it will not be added.
     * Directories that are added explicitly will be searched first in the find calls.
     * @param dir a directory
     * @param java.io.IOException
     */
    public static void addDirectory(File file) throws IOException {
        if (!file.exists())
            throw new IOException(file + ",does not exist");
        if (!file.isDirectory()) {
            throw new IOException(file + ",must be a directory");
        }
        if (classPaths == null) {
            buildClassPathsVector();
        }
        String path = file.getCanonicalPath();
        path = path.replace('/', File.separatorChar);
        if (path.charAt(path.length() - 1) != '/') {
            path = path + "/";
        }
        if (classPaths.contains(path)) {
            return;
        }
        classPaths.addElement(path);
    }

    /**
     * This method will remove a directory from the internal list of directories that are searched in
     * the find methods.
     * @param dir a directory
     * @param java.io.IOException
     */
    public static void removeDirectory(File file) throws IOException {
        if (!file.exists())
            return;
        if (!file.isDirectory()) {
            return;
        }
        if (classPaths == null) {
            return;
        }
        String path = file.getCanonicalPath();
        path = path.replace('/', File.separatorChar);
        if (path.charAt(path.length() - 1) != '/') {
            path = path + "/";
        }
        classPaths.removeElement(path);
    }

    /**
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
        Enumeration<String> e = classPaths.elements();
        while (e.hasMoreElements()) {
            String path = e.nextElement();
            String file = returnExistingFile(path + paramString);
            if (file != null) {
                return new File(file);
            }
        }
        throw new FileNotFoundException(paramString);
    }

    private static File _doFISP(String paramString, boolean paramBoolean) throws IOException {
        String str1 = paramString;
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
        throw new FileNotFoundException(paramString);
    }

    private static void buildClassPathsVector() throws IOException {
        classPaths = new Vector<>(8);
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
                if ((!classPaths.contains(str3)) && (!str3.isEmpty())) {
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
        } catch (StdQTException e) {
        }
        return null;
    }

    private static GraphicsImporter openAsImage(DataRef paramDataRef) throws QTException {
        try {
            return new GraphicsImporter(paramDataRef);
        } catch (StdQTException e) {
        }
        return null;
    }

    private static GraphicsImporter importImage(DataRef paramDataRef) throws QTException {
        try {
            return new GraphicsImporter(paramDataRef);
        } catch (QTException e) {
        }
        return null;
    }

    private static Movie checkIfMovie(QTFile paramQTFile) throws QTException {
        return checkMovie(Movie.fromFile(OpenMovieFile.asRead(paramQTFile), 0, null));
    }

    private static Movie checkIfMovie(DataRef paramDataRef) throws QTException {
        try {
            return checkMovie(Movie.fromDataRef(paramDataRef, 0));
        } catch (QTException e) {
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
        } catch (QTException e) {
        }
        return null;
    }

    private static Movie openAsMovie(QTFile paramQTFile) throws QTException {
        OpenMovieFile localOpenMovieFile = OpenMovieFile.asRead(paramQTFile);
        try {
            return Movie.fromFile(localOpenMovieFile);
        } catch (StdQTException e) {
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
        } catch (QTException e) {
        }
        return null;
    }
}
