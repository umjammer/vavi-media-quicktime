
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


public final class QTFactory {
    private static Vector<String> classPaths;

    private static final boolean debug = false;

    public static QTComponent makeQTComponent(final Movie mov) throws QTException {
        if (QTSession.isCurrentOS(4) && QTSession.getJavaVersion() >= 65540) {

            class PrivelegedAction {
                QTComponent comp;

                void establish() {
                    AccessController.doPrivileged(new PrivilegedAction<QTComponent>() {
                        public QTComponent run() {
                            comp = new QTJavaCocoaCanvas(mov);
                            return comp;
                        }
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

    public static QTComponent makeQTComponent(final MovieController controller) throws QTException {
        if (QTSession.isCurrentOS(4) && QTSession.getJavaVersion() >= 65540) {

            class PrivelegedAction {
                QTComponent comp;

                void establish() {
                    AccessController.doPrivileged(new PrivilegedAction<Object>() {
                        public Object run() {
                            try {
                                comp = new QTJavaCocoaCanvas(controller);
                            } catch (QTException e) {
                            }
                            return comp;
                        }
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

    public static QTComponent makeQTComponent(final GraphicsImporter gi) throws QTException {
        if (QTSession.isCurrentOS(4) && QTSession.getJavaVersion() >= 65540) {

            class PrivelegedAction {
                QTComponent comp;

                void establish() {
                    AccessController.doPrivileged(new PrivilegedAction<QTComponent>() {
                        public QTComponent run() {
                            comp = new QTJavaCocoaCanvas(gi);
                            return comp;
                        }
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

    public static QTJComponent makeQTJComponent(GraphicsImporterDrawer paramGraphicsImporterDrawer) throws QTException {
        return new JQTCanvas(paramGraphicsImporterDrawer);
    }

    public static QTJComponent makeQTJComponent(MoviePlayer paramMoviePlayer) throws QTException {
        return new JQTCanvas(paramMoviePlayer);
    }

    public static File findInSystemPaths(String paramString) throws IOException {
        if (classPaths == null)
            buildClassPathsVector();
        return _doFISP(paramString, false);
    }

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
