package quicktime.io;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.FileManager;
import quicktime.internal.toolbox.GraphicsExportManager;
import quicktime.internal.toolbox.GraphicsImportManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.image.GraphicsImporterInfo;
import quicktime.std.movies.MovieProgress;
import quicktime.std.movies.media.DataRef;
import quicktime.std.music.SynthesizerConnections;
import quicktime.std.qtcomponents.MovieImporter;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


/**
 * The QTFile class represents a file for QuickTime movies.
 */
public final class QTFile extends File implements QuickTimeLib {

    /**
     * Standard types used in get file.
     */
    public static final int[] kStandardQTFileTypes = { 1299148630, 1903454566 };

    /**
     * Permission flags used to test security accessibility of a file path
     */
    public static final int kReadPermission = 256;

    /**
     * Permission flags used to test security accessibility of a file path
     */
    public static final int kWritePermission = 512;

    /**
     * Permission flags used to test security accessibility of a file path
     */
    public static final int kDeletePermission = 1024;

    private transient byte[] fileSpec;

    /**
     * Determines the file that the graphics resides in.
     * @see "QuickTime::GraphicsImportGetDataFile"
     * @return The file that contains the graphics data.
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsImporter#getDataFile()
     */
    public static QTFile fromGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws QTException {
        byte[] arrayOfByte = makeFSSpec();
        int i = GraphicsImportGetDataFile(QTObject.ID(paramGraphicsImporter), arrayOfByte);
        StdQTException.checkError(i);
        return new QTFile(arrayOfByte);
    }

    /**
     * Presents a dialog box letting the user save an image in a foreign file format.
     * @see "QuickTime::GraphicsImportDoExportImageFileDialog"
     * @param inDefaultSpec suggests the default name for the file. If you do not want to suggest a default name, pass null.
     * @param prompt A prompt that appears in the standard put dialog box; it may be nil, in which case a default string is used.
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsImporter#doExportImageFileDialog(quicktime.io.QTFile, java.lang.String)
     */
    public static GraphicsImporterInfo fromGraphicsImporter(GraphicsImporter paramGraphicsImporter, QTFile inDefaultSpec, String prompt) throws QTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = makeFSSpec();
        short[] arrayOfShort = new short[1];
        int i = GraphicsImportDoExportImageFileDialog(QTObject.ID(paramGraphicsImporter), inDefaultSpec != null ? inDefaultSpec.getFSSpec(true, 512) : null, prompt != null ? QTUtils.String2PString(prompt, 255) : null, 0, arrayOfInt, arrayOfByte, arrayOfShort);
        StdQTException.checkError(i);
        return new GraphicsImporterInfo(arrayOfInt[0], new QTFile(arrayOfByte), arrayOfShort[0]);
    }

    /**
     * Determines the file that is the source of input images to the Graphics exporter.
     * @see "QuickTime::GraphicsExportGetInputFile"
     * @return The QTFile object
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsExporter#getInputFile()
     */
    public static QTFile fromGraphicsExporterInput(GraphicsExporter paramGraphicsExporter) throws QTException {
        byte[] arrayOfByte = makeFSSpec();
        int i = GraphicsExportGetInputFile(QTObject.ID(paramGraphicsExporter), arrayOfByte);
        StdQTException.checkError(i);
        return new QTFile(arrayOfByte);
    }

    /**
     * Determines the file that is the destination of Output images to the Graphics exporter.
     * @see "QuickTime::GraphicsExportGetOutputFile"
     * @return The QTFile object
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsExporter#getOutputFile()
     */
    public static QTFile fromGraphicsExporterOutput(GraphicsExporter paramGraphicsExporter) throws QTException {
        byte[] arrayOfByte = makeFSSpec();
        int i = GraphicsExportGetOutputFile(QTObject.ID(paramGraphicsExporter), arrayOfByte);
        StdQTException.checkError(i);
        return new QTFile(arrayOfByte);
    }

    /**
     * Allows you to determine the movie file that is currently assigned to a sequence grabber component.
     * @see "QuickTime::SGGetDataOutput"
     * @return The QTFile movie file.
     * @throws quicktime.QTException
     * @see quicktime.std.sg.SequenceGrabber#getDataOutputFile()
     */
    public static QTFile fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) throws QTException {
        byte[] arrayOfByte = makeFSSpec();
        int[] arrayOfInt = { 0 };
        int i = SGGetDataOutput(QTObject.ID(paramSequenceGrabber), arrayOfByte, arrayOfInt);
        StdQTException.checkError(i);
        return new QTFile(arrayOfByte);
    }

    /**
     * Return a QTFile (or null if none) of a sound bank file that is used by
     * a MusicComponent as specified in that component's SynthesizerConnections
     * @param conns the Synthesiser connections
     * @return a QTFile
     * @throws quicktime.QTException
     */
    public static QTFile fromSynthesizerConnections(SynthesizerConnections conns) throws QTException {
        int i = getIntFromArray(conns.getBytes(), 24);
        if (i == 0)
            return null;
        byte[] arrayOfByte = makeFSSpec();
        copyPointerToArray(i, 0, arrayOfByte, 0, arrayOfByte.length);
        return new QTFile(arrayOfByte);
    }

    /**
     * This method will assume that the incoming path is a full path specification for
     * a file and will check the security manager settings to see if the current
     * security settings allow this file to be read from or written to. If the
     * requested security access is dissallowed a SecurityException is thrown.
     * @param fileName a full path file specification path
     * @param permission the requested permission.
     */
    public static void checkSecurity(String fileName, int permission) {
        Path path = Path.of(fileName);
        int i = 0;
        if ((permission & 0x100) != 0) {
            Files.isReadable(path);
            i = 1;
        }
        if ((permission & 0x200) != 0) {
            Files.isWritable(path);
            i = 1;
        }
        if ((permission & 0x400) != 0) {
            Files.isExecutable(path);
            i = 1;
        }
        if (i == 0) {
            throw new SecurityException(fileName + ",Unknown permission request:0x" + Integer.toHexString(permission));
        }
    }

    /**
     * This method will take an alias and will resolve it,
     * returning the full path name of the resolved file. You must have
     * read permission to the resultant file path or a SecurityException will be thrown.
     * @see "QuickTime::ResolveAlias"
     * @param alias the alias to resolve
     * @return the QTFile object of the resolved alias
     * @throws quicktime.QTException
     */
    public static QTFile resolveAlias(AliasHandle alias) throws QTException {
        byte[] arrayOfByte1 = makeFSSpec();
        byte[] arrayOfByte2 = { 0 };
        int i = ResolveAlias(null, QTObject.ID(alias), arrayOfByte1, arrayOfByte2);
        QTIOException.checkError(i, QTUtils.PString2String(arrayOfByte1, 6));
        QTFile localQTFile = new QTFile(arrayOfByte1);
        try {
            checkSecurity(localQTFile.getCanonicalPath(), 256);
        } catch (IOException localIOException) {
            throw new QTIOException(localIOException.getMessage());
        }
        return localQTFile;
    }

    private static int getNumTypes(int[] paramArrayOfInt) {
        if (paramArrayOfInt == null)
            return -1;
        int i = paramArrayOfInt.length;
        if (i == 0)
            return -1;
        if (i > 4)
            return 4;
        return i;
    }

    /**
     * Present a standard open file panel to the user to retrieve a file.
     * @see "QuickTime::StandardGetFilePreview()"
     * @param fileTypes the particular file types you want the user to see. No fileTypes assumes all files. Max of 4.
     * @return The file object.
     * @throws quicktime.QTException
     */
    public static QTFile standardGetFilePreview(int[] fileTypes) throws QTException {
        byte[] arrayOfByte1;
        if (QTSession.isCurrentOS(4)) {
            FileDialog localFileDialog = new FileDialog(new java.awt.Frame(), "Open File...");
            localFileDialog.show();
            if (localFileDialog.getFile() == null)
                throw new QTIOException(-128, "");
            arrayOfByte1 = path2Spec(localFileDialog.getDirectory() + localFileDialog.getFile(), true);
            return new QTFile(localFileDialog.getDirectory() + localFileDialog.getFile(), arrayOfByte1);
        }
        synchronized (QTNative.globalsLock) {
            byte[] arrayOfByte2 = makeStdFileReply();
            StandardGetFilePreview(0, (short) getNumTypes(fileTypes), fileTypes, arrayOfByte2);
            if (arrayOfByte2[0] == 0)
                throw new QTIOException(-128, "");
            arrayOfByte1 = makeFSSpec();
            System.arraycopy(arrayOfByte2, 6, arrayOfByte1, 0, arrayOfByte1.length);
        }
        return new QTFile(arrayOfByte1);
    }

    /**
     * Used to return a QTFile object from a full path specification
     * @param pathName the full path name of the file
     */
    public QTFile(String pathName) {
        super(pathName);
    }

    /**
     * Used to return a QTFile object from a Java file object.
     * @param file a java File object
     */
    public QTFile(File file) {
        this(file.getPath());
    }

    private QTFile(byte[] paramArrayOfByte) throws QTIOException {
        this(spec2Path(paramArrayOfByte), paramArrayOfByte);
    }

    private QTFile(String paramString, byte[] paramArrayOfByte) {
        super(paramString);
        this.fileSpec = paramArrayOfByte;
    }

    /**
     * Returns the byte array containing the fsspec.
     * @param permissions you must specify whether the file will be reading or writing.
     * @return the byte array
     * @throws quicktime.io.QTIOException
     */
    public final byte[] getFSSpec(boolean paramBoolean, int permissions) throws QTIOException {
        if (this.fileSpec == null) {
            try {
                this.fileSpec = path2Spec(getCanonicalPath(), paramBoolean);
            } catch (IOException localIOException1) {
                throw new QTIOException(localIOException1.getMessage());
            }
        }
        try {
            checkSecurity(getCanonicalPath(), permissions);
        } catch (IOException localIOException2) {
            throw new QTIOException(localIOException2.getMessage());
        }
        byte[] arrayOfByte = new byte[this.fileSpec.length];
        System.arraycopy(this.fileSpec, 0, arrayOfByte, 0, arrayOfByte.length);
        return arrayOfByte;
    }

    /**
     * This method takes a specified file and converts it to a movie file.
     * @see "QuickTime::ConvertFileToMovieFile()"
     * @param fCreator Specifies the creator value for the file if it is a new one.
     * @param inFlags Controls movie file conversion flags.
     * @throws quicktime.QTException
     */
    public void convertToMovieFile(QTFile paramQTFile, int fCreator, int inFlags) throws QTException {
        convertToMovieFile(paramQTFile, fCreator, inFlags, null, null);
    }

    /**
     * This method takes a specified file and converts it to a movie file.
     * The specified files must be full paths or a fnfErr exception will be thrown
     * @see "QuickTime::ConvertFileToMovieFile()"
     * @param outputFile the destination movie file.
     * @param fCreator Specifies the creator value for the file if it is a new one.
     * @param inFlags Controls movie file conversion flags.
     * @throws quicktime.QTException
     */
    public void convertToMovieFile(QTFile outputFile, int fCreator, int inFlags, MovieImporter paramMovieImporter) throws QTException {
        convertToMovieFile(outputFile, fCreator, inFlags, paramMovieImporter, null);
    }

    /**
     * This method takes a specified file and converts it to a movie file.
     * The specified files must be full paths or a fnfErr exception will be thrown
     * @see "QuickTime::ConvertFileToMovieFile()"
     * @param outputFile the destination movie file.
     * @param fCreator Specifies the creator value for the file if it is a new one.
     * @param flags Controls movie file conversion flags.
     * @param mp Specifies the movie progress object or null otherwise.
     * @throws quicktime.QTException
     */
    public void convertToMovieFile(QTFile outputFile, int fCreator, int flags, MovieImporter paramMovieImporter, MovieProgress mp) throws QTException {
        byte[] arrayOfByte1 = getFSSpec(true, 256);
        byte[] arrayOfByte2 = outputFile.getFSSpec(false, 512);
        if ((flags & 0x80000000) != 0) {
            try {
                checkSecurity(outputFile.getCanonicalPath(), 1024);
            } catch (IOException localIOException) {
                throw new QTIOException(localIOException.getMessage());
            }
        }
        IODispatcher localIODispatcher = null;
        int i = -1;
        if (mp != null) {
            localIODispatcher = new IODispatcher(mp);
            i = localIODispatcher.ID();
        }
        int j = ConvertFileToMovieFile(arrayOfByte1, arrayOfByte2, fCreator, (short) -1, null, flags, QTObject.ID(paramMovieImporter), i, 0);
        if (localIODispatcher != null)
            localIODispatcher.cleanupMethodClosure();
        QTIOException.checkError(j, getPath() + "," + outputFile.getPath());
    }

    /**
     * Creates a file as specified by this QTFile.
     * This call will not open the created movie file.
     * @see "QuickTime::CreateMovieFile()"
     * @param fCreator Specifies the creator of the file.
     * @param flags Specifies the behaviour when creating the file.
     * @throws quicktime.io.QTIOException
     */
    public void createMovieFile(int fCreator, int flags) throws QTIOException {
        if ((flags & 0x80000000) != 0) {
            try {
                checkSecurity(getCanonicalPath(), 1024);
            } catch (IOException localIOException) {
                throw new QTIOException(localIOException.getMessage());
            }
        }
        int i = CreateMovieFile(getFSSpec(false, 512), fCreator, (short) -1, flags | 0x20000000 | 0x40000000, null, null);
        QTIOException.checkError(i, getPath());
    }

    /**
     * Creates a short cut movie file as specified by this QTFile. A Shortcut movie just
     * contains a data reference which refers to another movie.
     * @see "QuickTime::CreateShortcutMovieFile()"
     * @param creator Specifies the creator of the file.
     * @param scriptTag the system script identifier
     * @param createMovieFileFlags Specifies the behaviour when creating the file.
     * @param targetDataRef this is a data reference to the location of the data that is referred to by
     *  the resultant movie that this call creates.
     * @throws quicktime.QTException
     */
    public void createShortcutMovieFile(int creator, int scriptTag, int createMovieFileFlags, DataRef targetDataRef) throws QTException {
        if (QTSession.getQTMajorVersion() < 4) {
            throw new QTException(-50);
        }
        if ((createMovieFileFlags & 0x80000000) != 0) {
            try {
                checkSecurity(getCanonicalPath(), 1024);
            } catch (IOException localIOException) {
                throw new QTIOException(localIOException.getMessage());
            }
        }
        int i = CreateShortcutMovieFile(getFSSpec(false, 512), creator, (short) scriptTag, createMovieFileFlags, QTObject.ID(targetDataRef), targetDataRef.getType());
        QTIOException.checkError(i, getPath());
    }

    /**
     * Creates an alias record that describes the specified target.
     * @see "QuickTime::QTNewAlias"
     * @param minimal if true then the alias is a minimal alias.
     * @return an AliasHandle that represents the QTFile
     * @throws quicktime.QTException
     */
    public AliasHandle newAlias(boolean minimal) throws QTException {
        return AliasHandle.fromQTFile(this, minimal);
    }

    /**
     * Creates a relative alias record that describes the specified target with respect to the given file.
     * @see "QuickTime::NewAlias"
     * @param relativeToFile the file from which the target is relative to.
     * @param minimal if true then the alias is a minimal alias.
     * @return an AliasHandle that represents the QTFile
     * @throws quicktime.QTException
     */
    public AliasHandle relativeAlias(QTFile relativeToFile, boolean minimal) throws QTException {
        return AliasHandle.fromTargetQTFile(relativeToFile, this, minimal);
    }

    /**
     * Returns the target FSSpec passed in
     * @see "QuickTime::ResolveAliasFile"
     * @param resolveAliasChains set to true if all aliases to be resolved in a chain.
     * @throws quicktime.io.QTIOException
     */
    public QTFile resolveAlias(boolean resolveAliasChains) throws QTIOException {
        byte b = (byte) (resolveAliasChains ? 1 : 0);
        byte[] arrayOfByte1 = { 0 };
        byte[] arrayOfByte2 = { 0 };
        byte[] arrayOfByte3 = getFSSpec(true, 256);
        int i = ResolveAliasFile(arrayOfByte3, b, arrayOfByte1, arrayOfByte2);
        QTIOException.checkError(i, QTUtils.PString2String(arrayOfByte3, 6));
        return new QTFile(arrayOfByte3);
    }

    /**
     * This method deletes a movie file.
     * @see "QuickTime::DeleteMovieFile()"
     * @return true if the file is successfully deleted;
     *              false otherwise.
     */
    @Override
    public boolean delete() {
        boolean bool = false;
        try {
            byte[] arrayOfByte = getFSSpec(true, 1024);
            int i = DeleteMovieFile(arrayOfByte);
            QTIOException.checkError(i, getCanonicalPath());
            bool = true;
        } catch (IOException localIOException) {
            throw new SecurityException(localIOException.getMessage());
        } catch (QTException localQTException) {
            return super.delete();
        }
        return bool;
    }

    private static final byte[] makeStdFileReply() {
        return new byte[QTSession.isCurrentOS(1) ? 'X' : 280];
    }

    private static final byte[] makeFSSpec() {
        return new byte[QTSession.isCurrentOS(1) ? 'F' : 262];
    }

    private static byte[] path2Spec(String paramString, boolean paramBoolean) throws QTIOException {
        File file = new File(paramString).getAbsoluteFile();
        if (paramBoolean && !file.exists()) {
            throw new QTIOException(-43, paramString);
        }
        return FileManager.pathToSpec(file.getPath());
    }

    private static String spec2Path(byte[] paramArrayOfByte) throws QTIOException {
        String path = FileManager.specToPath(paramArrayOfByte);
        if (path == null) {
            throw new QTIOException(-43, QTUtils.PString2String(paramArrayOfByte, 6));
        }
        return path;
    }

    private static final byte[] makeFSSpec(byte[] paramArrayOfByte, boolean paramBoolean) throws QTIOException {
        byte[] arrayOfByte = makeFSSpec();
        int i = FSMakeFSSpec((short) 0, 0, paramArrayOfByte, arrayOfByte);
        if (i != 0) {
            if (i == -43) {
                if (paramBoolean) {
                    QTIOException.checkError(i, QTUtils.PString2String(paramArrayOfByte, 0));
                }
            } else {
                QTIOException.checkError(i, QTUtils.PString2String(paramArrayOfByte, 0));
            }
        }
        return arrayOfByte;
    }

    private static String pathName2QT(String paramString) throws QTIOException {
        if (paramString.charAt(0) == '/') {
            paramString = paramString.substring(1);
        }
        if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
            paramString = MacFSSpec_pathName2QT(paramString);
            paramString = cleanUpPathName(paramString);
            paramString = MacFSSpec_checkAndResolveAlias(paramString);
        } else if (QTSession.isCurrentOS(2)) {
            paramString = paramString.replace('/', File.separatorChar);
        }
        return paramString;
    }

    private static final String cleanUpPathName(String paramString) {
        int i = paramString.length();
        StringBuilder localStringBuffer = new StringBuilder();
        for (int j = 0; j < i; j++) {
            if (paramString.charAt(j) == '%') {
                String str = paramString.substring(j + 1, j + 3);
                localStringBuffer.append((char) Integer.parseInt(str, 16));
                j += 2;
            } else {
                localStringBuffer.append(paramString.charAt(j));
            }
        }
        return new String(localStringBuffer);
    }

    private static String MacFSSpec_pathName2QT(String paramString) {
        if ((paramString.charAt(0) == ':') || (paramString.charAt(0) == '/')) {
            paramString = paramString.substring(1);
        }
        return paramString.replace('/', ':');
    }

    private static String MacFSSpec_checkAndResolveAlias(String paramString) throws QTIOException {
        try {
            QTUtils.checkFreeMemory();
        } catch (QTException localQTException) {
            throw new QTIOException(-108, paramString);
        }
        byte[] arrayOfByte1 = new byte[paramString.length() + 1];
        byte[] arrayOfByte2 = paramString.getBytes();
        int i;
        if (QTSession.isCurrentOS(1)) {
            i = arrayOfByte2.length;
        } else {
            i = paramString.length();
        }
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 1, arrayOfByte2.length);
        arrayOfByte1[0] = ((byte) i);
        byte[] arrayOfByte3 = new byte[70];
        if (FSMakeFSSpec((short) 0, 0, arrayOfByte1, arrayOfByte3) == -120) {
            String str1 = paramString.substring(0, paramString.lastIndexOf(':'));
            arrayOfByte2 = paramString.getBytes();
            System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 1, i);
            if (QTSession.isCurrentOS(1)) {
                arrayOfByte1[0] = ((byte) str1.getBytes().length);
            } else
                arrayOfByte1[0] = ((byte) str1.length());
            FSMakeFSSpec((short) 0, 0, arrayOfByte1, arrayOfByte3);
            int[] arrayOfInt = { 0 };
            byte[] arrayOfByte4 = { 0 };
            byte[] arrayOfByte5 = { 0 };
            int j = ResolveAliasFile(arrayOfByte3, (byte) 1, arrayOfByte4, arrayOfByte5);
            if ((j == 0) || (j == -43)) {
                if (arrayOfByte5[0] != 0) {
                    String str2 = "";
                    str2 = MacFSSpec_getFullName(arrayOfByte3);
                    str2 = MacFSSpec_pathName2QT(str2);
                    str2 = str2 + paramString.substring(paramString.lastIndexOf(':'));
                    return str2;
                }
            } else
                throw new QTIOException(j, paramString);
        }
        return paramString;
    }

    private static class CInfoPBRec extends QTByteObject implements quicktime.jdirect.PrimitivesLib {

        static {
            if (!QTSession.isCurrentOS(1)) {
                throw new QTRuntimeException("Class Not Required");
            }
        }

        CInfoPBRec() {
            super(108);
        }

        short getVRefNum() {
            return getShortAt(22);
        }

        String getName() throws QTIOException {
            return getPString(getIntAt(18));
        }

        int getFileDirID() {
            return getIntAt(48);
        }

        int getDirParID() {
            return getIntAt(100);
        }

        void setVRefNum(short paramShort) {
            setShortAt(22, paramShort);
        }

        void setNamePtr(String paramString) throws QTIOException {
            if (paramString.length() > 255)
                throw new QTIOException(-50, paramString);
            int i = QTFile.NewPtrClear(256);
            if (i == 0) {
                throw new QTIOException(-108, paramString);
            }
            byte[] arrayOfByte = paramString.getBytes();
            int j;
            if (QTSession.isCurrentOS(1)) {
                j = arrayOfByte.length;
            } else
                j = paramString.length();
            setByteInPointer(i, 0, (byte) j);
            copyArrayToPointer(arrayOfByte, 0, i, 1, j);
            setByteInPointer(i, 0, (byte) j);
            setIntAt(18, i);
        }

        void setFileDirID(int paramInt) {
            setIntAt(48, paramInt);
        }

        void setFileDirIndex(short paramShort) {
            setShortAt(28, paramShort);
        }

        protected final void dispose() throws Throwable {
            if (getIntAt(18) != 0)
                QTFile.DisposePtr(getIntAt(18));
        }

        private String getPString(int paramInt) {
            int i = getByteFromPointer(paramInt, 0);
            byte[] arrayOfByte = new byte[i];
            copyPointerToArray(paramInt, 1, arrayOfByte, 0, i);
            return new String(arrayOfByte);
        }

        private static void copyArrayToPointer(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
            MemoryManager.copyArrayToPointer(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4);
        }

        private static void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4) {
            MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4);
        }

        private static void setByteInPointer(int paramInt1, int paramInt2, byte paramByte) {
            MemoryManager.setByteInPointer(paramInt1, paramInt2, paramByte);
        }

        private static byte getByteFromPointer(int paramInt1, int paramInt2) {
            return MemoryManager.getByteFromPointer(paramInt1, paramInt2);
        }
    }

    private static String MacFSSpec_getFullName(byte[] paramArrayOfByte) throws QTIOException {
        CInfoPBRec localCInfoPBRec = new CInfoPBRec();
        localCInfoPBRec.setVRefNum(getShortFromArray(paramArrayOfByte, 0));
        localCInfoPBRec.setNamePtr(getPStringAt(paramArrayOfByte, 6));
        localCInfoPBRec.setFileDirIndex((short) -1);
        localCInfoPBRec.setFileDirID(getIntFromArray(paramArrayOfByte, 2));
        String localObject = getPStringAt(paramArrayOfByte, 6);
        int i = getIntFromArray(paramArrayOfByte, 2);
        for (; ; ) {
            String str = "";
            localCInfoPBRec.setFileDirID(i);
            int j = PBGetCatInfoSync(localCInfoPBRec.getBytes());
            QTIOException.checkError(j, localObject);
            if (localCInfoPBRec.getFileDirID() == 2) {
                str = localCInfoPBRec.getName();
                str = str + File.separator;
                str = str + localObject;
                localObject = str;
                str = File.separator;
                str = str + localObject;
                localObject = str;
                break;
            }
            i = localCInfoPBRec.getDirParID();
            str = localCInfoPBRec.getName();
            str = str + File.separator;
            str = str + localObject;
            localObject = str;
        }
        return localObject;
    }

    private static String getPStringAt(byte[] paramArrayOfByte, int paramInt) {
        int i = paramArrayOfByte[(paramInt++)];
        byte[] arrayOfByte = new byte[i];
        System.arraycopy(paramArrayOfByte, paramInt, arrayOfByte, 0, i);
        return new String(arrayOfByte);
    }

    private static short QTNewDataReferenceFromFullPathUnicode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return FileManager.QTNewDataReferenceFromFullPathUnicode(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfByte, paramInt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4);
    }

    private static int NewPtrClear(int paramInt) {
        return MemoryManager.NewPtrClear(paramInt);
    }

    private static void DisposePtr(int paramInt) {
        MemoryManager.DisposePtr(paramInt);
    }

    private static short FSMakeFSSpec(short paramShort, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return FileManager.FSMakeFSSpec(paramShort, paramInt, paramArrayOfByte1, paramArrayOfByte2);
    }

    private static short PBGetCatInfoSync(byte[] paramArrayOfByte) {
        return FileManager.PBGetCatInfoSync(paramArrayOfByte);
    }

    private static short ResolveAlias(byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return FileManager.ResolveAlias(paramArrayOfByte1, paramInt, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static short ResolveAliasFile(byte[] paramArrayOfByte1, byte paramByte, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return FileManager.ResolveAliasFile(paramArrayOfByte1, paramByte, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static short FSPathMakeRef(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return FileManager.FSPathMakeRef(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static int FSGetCatalogInfo(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2, int paramInt4) {
        return FileManager.FSGetCatalogInfo(paramArrayOfByte1, paramInt1, paramInt2, paramInt3, paramArrayOfByte2, paramInt4);
    }

    private static short FSMakeFSRefUnicode(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, byte[] paramArrayOfByte3) {
        return FileManager.FSMakeFSRefUnicode(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2, paramArrayOfByte3);
    }

    private static int GraphicsImportGetDataFile(int paramInt, byte[] paramArrayOfByte) {
        return GraphicsImportManager.GraphicsImportGetDataFile(paramInt, paramArrayOfByte);
    }

    private static int GraphicsExportGetInputFile(int paramInt, byte[] paramArrayOfByte) {
        return GraphicsExportManager.GraphicsExportGetInputFile(paramInt, paramArrayOfByte);
    }

    private static int GraphicsExportGetOutputFile(int paramInt, byte[] paramArrayOfByte) {
        return GraphicsExportManager.GraphicsExportGetOutputFile(paramInt, paramArrayOfByte);
    }

    private static int GraphicsImportDoExportImageFileDialog(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2, int[] paramArrayOfInt, byte[] paramArrayOfByte3, short[] paramArrayOfShort) {
        return GraphicsImportManager.GraphicsImportDoExportImageFileDialog(paramInt1, paramArrayOfByte1, paramArrayOfByte2, paramInt2, paramArrayOfInt, paramArrayOfByte3, paramArrayOfShort);
    }

    private static int SGGetDataOutput(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetDataOutput(paramInt, paramArrayOfByte, paramArrayOfInt);
    }

    private static short ConvertFileToMovieFile(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, short paramShort, short[] paramArrayOfShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return MovieToolbox.ConvertFileToMovieFile(paramArrayOfByte1, paramArrayOfByte2, paramInt1, paramShort, paramArrayOfShort, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static void StandardGetFilePreview(int paramInt, short paramShort, int[] paramArrayOfInt, byte[] paramArrayOfByte) {
        FileManager.StandardGetFilePreview(paramInt, paramShort, paramArrayOfInt, paramArrayOfByte);
    }

    private static short DeleteMovieFile(byte[] paramArrayOfByte) {
        return MovieToolbox.DeleteMovieFile(paramArrayOfByte);
    }

    private static short CreateMovieFile(byte[] paramArrayOfByte, int paramInt1, short paramShort, int paramInt2, short[] paramArrayOfShort, int[] paramArrayOfInt) {
        return MovieToolbox.CreateMovieFile(paramArrayOfByte, paramInt1, paramShort, paramInt2, paramArrayOfShort, paramArrayOfInt);
    }

    private static short CreateShortcutMovieFile(byte[] paramArrayOfByte, int paramInt1, short paramShort, int paramInt2, int paramInt3, int paramInt4) {
        return MovieToolbox.CreateShortcutMovieFile(paramArrayOfByte, paramInt1, paramShort, paramInt2, paramInt3, paramInt4);
    }
}
