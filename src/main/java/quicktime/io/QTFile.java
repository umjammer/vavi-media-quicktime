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
    public static final int[] kStandardQTFileTypes = {1299148630, 1903454566};

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
     *
     * @return The file that contains the graphics data.
     * @throws quicktime.QTException
     * @see "QuickTime::GraphicsImportGetDataFile"
     * @see quicktime.std.image.GraphicsImporter#getDataFile()
     */
    public static QTFile fromGraphicsImporter(GraphicsImporter gi) throws QTException {
        byte[] fsspec = makeFSSpec();
        int i = GraphicsImportGetDataFile(QTObject.ID(gi), fsspec);
        StdQTException.checkError(i);
        return new QTFile(fsspec);
    }

    /**
     * Presents a dialog box letting the user save an image in a foreign file format.
     *
     * @param inDefaultSpec suggests the default name for the file. If you do not want to suggest a default name, pass null.
     * @param prompt        A prompt that appears in the standard put dialog box; it may be nil, in which case a default string is used.
     * @throws quicktime.QTException
     * @see "QuickTime::GraphicsImportDoExportImageFileDialog"
     * @see quicktime.std.image.GraphicsImporter#doExportImageFileDialog(quicktime.io.QTFile, java.lang.String)
     */
    public static GraphicsImporterInfo fromGraphicsImporter(GraphicsImporter gi, QTFile inDefaultSpec, String prompt) throws QTException {
        int[] exportedType = new int[1];
        byte[] exportedSpec = makeFSSpec();
        short[] outScriptTag = new short[1];
        int i = GraphicsImportDoExportImageFileDialog(QTObject.ID(gi), inDefaultSpec != null ? inDefaultSpec.getFSSpec(true, 512) : null, prompt != null ? QTUtils.String2PString(prompt, 255) : null, 0, exportedType, exportedSpec, outScriptTag);
        StdQTException.checkError(i);
        return new GraphicsImporterInfo(exportedType[0], new QTFile(exportedSpec), outScriptTag[0]);
    }

    /**
     * Determines the file that is the source of input images to the Graphics exporter.
     *
     * @return The QTFile object
     * @throws quicktime.QTException
     * @see "QuickTime::GraphicsExportGetInputFile"
     * @see quicktime.std.image.GraphicsExporter#getInputFile()
     */
    public static QTFile fromGraphicsExporterInput(GraphicsExporter ge) throws QTException {
        byte[] fsspec = makeFSSpec();
        int i = GraphicsExportGetInputFile(QTObject.ID(ge), fsspec);
        StdQTException.checkError(i);
        return new QTFile(fsspec);
    }

    /**
     * Determines the file that is the destination of Output images to the Graphics exporter.
     *
     * @return The QTFile object
     * @throws quicktime.QTException
     * @see "QuickTime::GraphicsExportGetOutputFile"
     * @see quicktime.std.image.GraphicsExporter#getOutputFile()
     */
    public static QTFile fromGraphicsExporterOutput(GraphicsExporter ge) throws QTException {
        byte[] fsspec = makeFSSpec();
        int i = GraphicsExportGetOutputFile(QTObject.ID(ge), fsspec);
        StdQTException.checkError(i);
        return new QTFile(fsspec);
    }

    /**
     * Allows you to determine the movie file that is currently assigned to a sequence grabber component.
     *
     * @return The QTFile movie file.
     * @throws quicktime.QTException
     * @see "QuickTime::SGGetDataOutput"
     * @see quicktime.std.sg.SequenceGrabber#getDataOutputFile()
     */
    public static QTFile fromSequenceGrabber(SequenceGrabber sg) throws QTException {
        byte[] fsspec = makeFSSpec();
        int[] flags = {0};
        int i = SGGetDataOutput(QTObject.ID(sg), fsspec, flags);
        StdQTException.checkError(i);
        return new QTFile(fsspec);
    }

    /**
     * Return a QTFile (or null if none) of a sound bank file that is used by
     * a MusicComponent as specified in that component's SynthesizerConnections
     *
     * @param conns the Synthesiser connections
     * @return a QTFile
     * @throws quicktime.QTException
     */
    public static QTFile fromSynthesizerConnections(SynthesizerConnections conns) throws QTException {
        int i = getIntFromArray(conns.getBytes(), 24);
        if (i == 0)
            return null;
        byte[] fileSpec = makeFSSpec();
        copyPointerToArray(i, 0, fileSpec, 0, fileSpec.length);
        return new QTFile(fileSpec);
    }

    /**
     * This method will assume that the incoming path is a full path specification for
     * a file and will check the security manager settings to see if the current
     * security settings allow this file to be read from or written to. If the
     * requested security access is dissallowed a SecurityException is thrown.
     *
     * @param fileName   a full path file specification path
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
     *
     * @param alias the alias to resolve
     * @return the QTFile object of the resolved alias
     * @throws quicktime.QTException
     * @see "QuickTime::ResolveAlias"
     */
    public static QTFile resolveAlias(AliasHandle alias) throws QTException {
        byte[] target = makeFSSpec();
        byte[] wasChanged = {0};
        int i = ResolveAlias(null, QTObject.ID(alias), target, wasChanged);
        QTIOException.checkError(i, QTUtils.PString2String(target, 6));
        QTFile qtFile = new QTFile(target);
        try {
            checkSecurity(qtFile.getCanonicalPath(), 256);
        } catch (IOException e) {
            throw new QTIOException(e.getMessage());
        }
        return qtFile;
    }

    private static int getNumTypes(int[] fileTypes) {
        if (fileTypes == null)
            return -1;
        int i = fileTypes.length;
        if (i == 0)
            return -1;
        if (i > 4)
            return 4;
        return i;
    }

    /**
     * Present a standard open file panel to the user to retrieve a file.
     *
     * @param fileTypes the particular file types you want the user to see. No fileTypes assumes all files. Max of 4.
     * @return The file object.
     * @throws quicktime.QTException
     * @see "QuickTime::StandardGetFilePreview()"
     */
    public static QTFile standardGetFilePreview(int[] fileTypes) throws QTException {
        byte[] fileSpec;
        if (QTSession.isCurrentOS(4)) {
            FileDialog fileDialog = new FileDialog(new java.awt.Frame(), "Open File...");
            fileDialog.show();
            if (fileDialog.getFile() == null)
                throw new QTIOException(-128, "");
            fileSpec = path2Spec(fileDialog.getDirectory() + fileDialog.getFile(), true);
            return new QTFile(fileDialog.getDirectory() + fileDialog.getFile(), fileSpec);
        }
        synchronized (QTNative.globalsLock) {
            byte[] reply = makeStdFileReply();
            StandardGetFilePreview(0, (short) getNumTypes(fileTypes), fileTypes, reply);
            if (reply[0] == 0)
                throw new QTIOException(-128, "");
            fileSpec = makeFSSpec();
            System.arraycopy(reply, 6, fileSpec, 0, fileSpec.length);
        }
        return new QTFile(fileSpec);
    }

    /**
     * Used to return a QTFile object from a full path specification
     *
     * @param pathName the full path name of the file
     */
    public QTFile(String pathName) {
        super(pathName);
    }

    /**
     * Used to return a QTFile object from a Java file object.
     *
     * @param file a java File object
     */
    public QTFile(File file) {
        this(file.getPath());
    }

    private QTFile(byte[] fileSpec) throws QTIOException {
        this(spec2Path(fileSpec), fileSpec);
    }

    private QTFile(String path, byte[] fileSpec) {
        super(path);
        this.fileSpec = fileSpec;
    }

    /**
     * Returns the byte array containing the fsspec.
     *
     * @param permissions you must specify whether the file will be reading or writing.
     * @return the byte array
     * @throws quicktime.io.QTIOException
     */
    public byte[] getFSSpec(boolean mustExist, int permissions) throws QTIOException {
        if (this.fileSpec == null) {
            try {
                this.fileSpec = path2Spec(getCanonicalPath(), mustExist);
            } catch (IOException e) {
                throw new QTIOException(e.getMessage());
            }
        }
        try {
            checkSecurity(getCanonicalPath(), permissions);
        } catch (IOException e) {
            throw new QTIOException(e.getMessage());
        }
        byte[] fsSpec = new byte[this.fileSpec.length];
        System.arraycopy(this.fileSpec, 0, fsSpec, 0, fsSpec.length);
        return fsSpec;
    }

    /**
     * This method takes a specified file and converts it to a movie file.
     *
     * @param fCreator Specifies the creator value for the file if it is a new one.
     * @param inFlags  Controls movie file conversion flags.
     * @throws quicktime.QTException
     * @see "QuickTime::ConvertFileToMovieFile()"
     */
    public void convertToMovieFile(QTFile outputFile, int fCreator, int inFlags) throws QTException {
        convertToMovieFile(outputFile, fCreator, inFlags, null, null);
    }

    /**
     * This method takes a specified file and converts it to a movie file.
     * The specified files must be full paths or a fnfErr exception will be thrown
     *
     * @param outputFile the destination movie file.
     * @param fCreator   Specifies the creator value for the file if it is a new one.
     * @param inFlags    Controls movie file conversion flags.
     * @throws quicktime.QTException
     * @see "QuickTime::ConvertFileToMovieFile()"
     */
    public void convertToMovieFile(QTFile outputFile, int fCreator, int inFlags, MovieImporter userComp) throws QTException {
        convertToMovieFile(outputFile, fCreator, inFlags, userComp, null);
    }

    /**
     * This method takes a specified file and converts it to a movie file.
     * The specified files must be full paths or a fnfErr exception will be thrown
     *
     * @param outputFile the destination movie file.
     * @param fCreator   Specifies the creator value for the file if it is a new one.
     * @param flags      Controls movie file conversion flags.
     * @param mp         Specifies the movie progress object or null otherwise.
     * @throws quicktime.QTException
     * @see "QuickTime::ConvertFileToMovieFile()"
     */
    public void convertToMovieFile(QTFile outputFile, int fCreator, int flags, MovieImporter userComp, MovieProgress mp) throws QTException {
        byte[] inputFile = getFSSpec(true, 256);
        byte[] fsSpec = outputFile.getFSSpec(false, 512);
        if ((flags & 0x80000000) != 0) {
            try {
                checkSecurity(outputFile.getCanonicalPath(), 1024);
            } catch (IOException e) {
                throw new QTIOException(e.getMessage());
            }
        }
        IODispatcher ioDispatcher = null;
        int i = -1;
        if (mp != null) {
            ioDispatcher = new IODispatcher(mp);
            i = ioDispatcher.ID();
        }
        int j = ConvertFileToMovieFile(inputFile, fsSpec, fCreator, (short) -1, null, flags, QTObject.ID(userComp), i, 0);
        if (ioDispatcher != null)
            ioDispatcher.cleanupMethodClosure();
        QTIOException.checkError(j, getPath() + "," + outputFile.getPath());
    }

    /**
     * Creates a file as specified by this QTFile.
     * This call will not open the created movie file.
     *
     * @param fCreator Specifies the creator of the file.
     * @param flags    Specifies the behaviour when creating the file.
     * @throws quicktime.io.QTIOException
     * @see "QuickTime::CreateMovieFile()"
     */
    public void createMovieFile(int fCreator, int flags) throws QTIOException {
        if ((flags & 0x80000000) != 0) {
            try {
                checkSecurity(getCanonicalPath(), 1024);
            } catch (IOException e) {
                throw new QTIOException(e.getMessage());
            }
        }
        int i = CreateMovieFile(getFSSpec(false, 512), fCreator, (short) -1, flags | 0x20000000 | 0x40000000, null, null);
        QTIOException.checkError(i, getPath());
    }

    /**
     * Creates a short cut movie file as specified by this QTFile. A Shortcut movie just
     * contains a data reference which refers to another movie.
     *
     * @param creator              Specifies the creator of the file.
     * @param scriptTag            the system script identifier
     * @param createMovieFileFlags Specifies the behaviour when creating the file.
     * @param targetDataRef        this is a data reference to the location of the data that is referred to by
     *                             the resultant movie that this call creates.
     * @throws quicktime.QTException
     * @see "QuickTime::CreateShortcutMovieFile()"
     */
    public void createShortcutMovieFile(int creator, int scriptTag, int createMovieFileFlags, DataRef targetDataRef) throws QTException {
        if (QTSession.getQTMajorVersion() < 4) {
            throw new QTException(-50);
        }
        if ((createMovieFileFlags & 0x80000000) != 0) {
            try {
                checkSecurity(getCanonicalPath(), 1024);
            } catch (IOException e) {
                throw new QTIOException(e.getMessage());
            }
        }
        int i = CreateShortcutMovieFile(getFSSpec(false, 512), creator, (short) scriptTag, createMovieFileFlags, QTObject.ID(targetDataRef), targetDataRef.getType());
        QTIOException.checkError(i, getPath());
    }

    /**
     * Creates an alias record that describes the specified target.
     *
     * @param minimal if true then the alias is a minimal alias.
     * @return an AliasHandle that represents the QTFile
     * @throws quicktime.QTException
     * @see "QuickTime::QTNewAlias"
     */
    public AliasHandle newAlias(boolean minimal) throws QTException {
        return AliasHandle.fromQTFile(this, minimal);
    }

    /**
     * Creates a relative alias record that describes the specified target with respect to the given file.
     *
     * @param relativeToFile the file from which the target is relative to.
     * @param minimal        if true then the alias is a minimal alias.
     * @return an AliasHandle that represents the QTFile
     * @throws quicktime.QTException
     * @see "QuickTime::NewAlias"
     */
    public AliasHandle relativeAlias(QTFile relativeToFile, boolean minimal) throws QTException {
        return AliasHandle.fromTargetQTFile(relativeToFile, this, minimal);
    }

    /**
     * Returns the target FSSpec passed in
     *
     * @param resolveAliasChains set to true if all aliases to be resolved in a chain.
     * @throws quicktime.io.QTIOException
     * @see "QuickTime::ResolveAliasFile"
     */
    public QTFile resolveAlias(boolean resolveAliasChains) throws QTIOException {
        byte b = (byte) (resolveAliasChains ? 1 : 0);
        byte[] targetIsFolder = {0};
        byte[] wasAliased = {0};
        byte[] spec = getFSSpec(true, 256);
        int i = ResolveAliasFile(spec, b, targetIsFolder, wasAliased);
        QTIOException.checkError(i, QTUtils.PString2String(spec, 6));
        return new QTFile(spec);
    }

    /**
     * This method deletes a movie file.
     *
     * @return true if the file is successfully deleted;
     * false otherwise.
     * @see "QuickTime::DeleteMovieFile()"
     */
    @Override
    public boolean delete() {
        boolean bool = false;
        try {
            byte[] fsspec = getFSSpec(true, 1024);
            int i = DeleteMovieFile(fsspec);
            QTIOException.checkError(i, getCanonicalPath());
            bool = true;
        } catch (IOException e) {
            throw new SecurityException(e.getMessage());
        } catch (QTException e) {
            return super.delete();
        }
        return bool;
    }

    private static byte[] makeStdFileReply() {
        return new byte[QTSession.isCurrentOS(1) ? 'X' : 280];
    }

    private static byte[] makeFSSpec() {
        return new byte[QTSession.isCurrentOS(1) ? 'F' : 262];
    }

    private static byte[] path2Spec(String path, boolean mustExist) throws QTIOException {
        File file = new File(path).getAbsoluteFile();
        if (mustExist && !file.exists()) {
            throw new QTIOException(-43, path);
        }
        return FileManager.pathToSpec(file.getPath());
    }

    private static String spec2Path(byte[] spec) throws QTIOException {
        String path = FileManager.specToPath(spec);
        if (path == null) {
            throw new QTIOException(-43, QTUtils.PString2String(spec, 6));
        }
        return path;
    }

    private static byte[] makeFSSpec(byte[] pString, boolean mustExist) throws QTIOException {
        byte[] spec = makeFSSpec();
        int i = FSMakeFSSpec((short) 0, 0, pString, spec);
        if (i != 0) {
            if (i == -43) {
                if (mustExist) {
                    QTIOException.checkError(i, QTUtils.PString2String(pString, 0));
                }
            } else {
                QTIOException.checkError(i, QTUtils.PString2String(pString, 0));
            }
        }
        return spec;
    }

    private static String pathName2QT(String pathName) throws QTIOException {
        if (pathName.charAt(0) == '/') {
            pathName = pathName.substring(1);
        }
        if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
            pathName = MacFSSpec_pathName2QT(pathName);
            pathName = cleanUpPathName(pathName);
            pathName = MacFSSpec_checkAndResolveAlias(pathName);
        } else if (QTSession.isCurrentOS(2)) {
            pathName = pathName.replace('/', File.separatorChar);
        }
        return pathName;
    }

    private static String cleanUpPathName(String pathName) {
        int i = pathName.length();
        StringBuilder localStringBuffer = new StringBuilder();
        for (int j = 0; j < i; j++) {
            if (pathName.charAt(j) == '%') {
                String str = pathName.substring(j + 1, j + 3);
                localStringBuffer.append((char) Integer.parseInt(str, 16));
                j += 2;
            } else {
                localStringBuffer.append(pathName.charAt(j));
            }
        }
        return new String(localStringBuffer);
    }

    private static String MacFSSpec_pathName2QT(String pathName) {
        if ((pathName.charAt(0) == ':') || (pathName.charAt(0) == '/')) {
            pathName = pathName.substring(1);
        }
        return pathName.replace('/', ':');
    }

    private static String MacFSSpec_checkAndResolveAlias(String path) throws QTIOException {
        try {
            QTUtils.checkFreeMemory();
        } catch (QTException e) {
            throw new QTIOException(-108, path);
        }
        byte[] fileName = new byte[path.length() + 1];
        byte[] bytes = path.getBytes();
        int i;
        if (QTSession.isCurrentOS(1)) {
            i = bytes.length;
        } else {
            i = path.length();
        }
        System.arraycopy(bytes, 0, fileName, 1, bytes.length);
        fileName[0] = ((byte) i);
        byte[] spec = new byte[70];
        if (FSMakeFSSpec((short) 0, 0, fileName, spec) == -120) {
            String str1 = path.substring(0, path.lastIndexOf(':'));
            bytes = path.getBytes();
            System.arraycopy(bytes, 0, fileName, 1, i);
            if (QTSession.isCurrentOS(1)) {
                fileName[0] = ((byte) str1.getBytes().length);
            } else
                fileName[0] = ((byte) str1.length());
            FSMakeFSSpec((short) 0, 0, fileName, spec);
            int[] arrayOfInt = {0};
            byte[] targetIsFolder = {0};
            byte[] wasAliased = {0};
            int j = ResolveAliasFile(spec, (byte) 1, targetIsFolder, wasAliased);
            if ((j == 0) || (j == -43)) {
                if (wasAliased[0] != 0) {
                    String pathName = "";
                    pathName = MacFSSpec_getFullName(spec);
                    pathName = MacFSSpec_pathName2QT(pathName);
                    pathName = pathName + path.substring(path.lastIndexOf(':'));
                    return pathName;
                }
            } else
                throw new QTIOException(j, path);
        }
        return path;
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

        void setVRefNum(short vRefNum) {
            setShortAt(22, vRefNum);
        }

        void setNamePtr(String namePtr) throws QTIOException {
            if (namePtr.length() > 255)
                throw new QTIOException(-50, namePtr);
            int i = QTFile.NewPtrClear(256);
            if (i == 0) {
                throw new QTIOException(-108, namePtr);
            }
            byte[] bytes = namePtr.getBytes();
            int j;
            if (QTSession.isCurrentOS(1)) {
                j = bytes.length;
            } else
                j = namePtr.length();
            setByteInPointer(i, 0, (byte) j);
            copyArrayToPointer(bytes, 0, i, 1, j);
            setByteInPointer(i, 0, (byte) j);
            setIntAt(18, i);
        }

        void setFileDirID(int fileDirID) {
            setIntAt(48, fileDirID);
        }

        void setFileDirIndex(short fileDirIndex) {
            setShortAt(28, fileDirIndex);
        }

        protected final void dispose() throws Throwable {
            if (getIntAt(18) != 0)
                QTFile.DisposePtr(getIntAt(18));
        }

        private String getPString(int ptr) {
            int i = getByteFromPointer(ptr, 0);
            byte[] arrayOfByte = new byte[i];
            copyPointerToArray(ptr, 1, arrayOfByte, 0, i);
            return new String(arrayOfByte);
        }

        private static void copyArrayToPointer(byte[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
            MemoryManager.copyArrayToPointer(array, arrayOffset, ptr, ptrOffset, size);
        }

        private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
            MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
        }

        private static void setByteInPointer(int ptr, int offset, byte value) {
            MemoryManager.setByteInPointer(ptr, offset, value);
        }

        private static byte getByteFromPointer(int ptr, int offset) {
            return MemoryManager.getByteFromPointer(ptr, offset);
        }
    }

    private static String MacFSSpec_getFullName(byte[] bytes) throws QTIOException {
        CInfoPBRec cInfoPBRec = new CInfoPBRec();
        cInfoPBRec.setVRefNum(getShortFromArray(bytes, 0));
        cInfoPBRec.setNamePtr(getPStringAt(bytes, 6));
        cInfoPBRec.setFileDirIndex((short) -1);
        cInfoPBRec.setFileDirID(getIntFromArray(bytes, 2));
        String path = getPStringAt(bytes, 6);
        int i = getIntFromArray(bytes, 2);
        for (; ; ) {
            String name = "";
            cInfoPBRec.setFileDirID(i);
            int j = PBGetCatInfoSync(cInfoPBRec.getBytes());
            QTIOException.checkError(j, path);
            if (cInfoPBRec.getFileDirID() == 2) {
                name = cInfoPBRec.getName();
                name = name + File.separator;
                name = name + path;
                path = name;
                name = File.separator;
                name = name + path;
                path = name;
                break;
            }
            i = cInfoPBRec.getDirParID();
            name = cInfoPBRec.getName();
            name = name + File.separator;
            name = name + path;
            path = name;
        }
        return path;
    }

    private static String getPStringAt(byte[] bytes, int offset) {
        int i = bytes[(offset++)];
        byte[] arrayOfByte = new byte[i];
        System.arraycopy(bytes, offset, arrayOfByte, 0, i);
        return new String(arrayOfByte);
    }

    private static short QTNewDataReferenceFromFullPathUnicode(byte[] utf16Path, int length, int pathStyle, int flags, int[] dataRef, int[] dataRefType) {
        return FileManager.QTNewDataReferenceFromFullPathUnicode(utf16Path, length, pathStyle, flags, dataRef, dataRefType);
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static int NewPtrClear(int size) {
        return MemoryManager.NewPtrClear(size);
    }

    private static void DisposePtr(int p) {
        MemoryManager.DisposePtr(p);
    }

    private static short FSMakeFSSpec(short vRefNum, int dirID, byte[] fileName, byte[] spec) {
        return FileManager.FSMakeFSSpec(vRefNum, dirID, fileName, spec);
    }

    private static short PBGetCatInfoSync(byte[] block) {
        return FileManager.PBGetCatInfoSync(block);
    }

    private static short ResolveAlias(byte[] fromFile, int alias, byte[] target, byte[] wasChanged) {
        return FileManager.ResolveAlias(fromFile, alias, target, wasChanged);
    }

    private static short ResolveAliasFile(byte[] spec, byte resolveAliasChains, byte[] targetIsFolder, byte[] wasAliased) {
        return FileManager.ResolveAliasFile(spec, resolveAliasChains, targetIsFolder, wasAliased);
    }

    private static short FSPathMakeRef(byte[] cPath, byte[] fsRef, byte[] isDirectory) {
        return FileManager.FSPathMakeRef(cPath, fsRef, isDirectory);
    }

    private static int FSGetCatalogInfo(byte[] fsRef, int whichInfo, int catalogInfo, int outName, byte[] fsSpec, int parentRef) {
        return FileManager.FSGetCatalogInfo(fsRef, whichInfo, catalogInfo, outName, fsSpec, parentRef);
    }

    private static short FSMakeFSRefUnicode(byte[] parentRef, int nameLength, byte[] name, int textEncodingHint, byte[] newRef) {
        return FileManager.FSMakeFSRefUnicode(parentRef, nameLength, name, textEncodingHint, newRef);
    }

    private static int GraphicsImportGetDataFile(int gi, byte[] fsspec) {
        return GraphicsImportManager.GraphicsImportGetDataFile(gi, fsspec);
    }

    private static int GraphicsExportGetInputFile(int ge, byte[] fsspec) {
        return GraphicsExportManager.GraphicsExportGetInputFile(ge, fsspec);
    }

    private static int GraphicsExportGetOutputFile(int ge, byte[] fsspec) {
        return GraphicsExportManager.GraphicsExportGetOutputFile(ge, fsspec);
    }

    private static int GraphicsImportDoExportImageFileDialog(int gi, byte[] defaultSpec, byte[] prompt, int filterProc, int[] exportedType, byte[] exportedSpec, short[] outScriptTag) {
        return GraphicsImportManager.GraphicsImportDoExportImageFileDialog(gi, defaultSpec, prompt, filterProc, exportedType, exportedSpec, outScriptTag);
    }

    private static int SGGetDataOutput(int sg, byte[] fsspec, int[] flags) {
        return SequenceGrabberManager.SGGetDataOutput(sg, fsspec, flags);
    }

    private static short ConvertFileToMovieFile(byte[] inputFile, byte[] outputFile, int creator, short scriptTag, short[] resId, int flags, int userComp, int proc, int refCon) {
        return MovieToolbox.ConvertFileToMovieFile(inputFile, outputFile, creator, scriptTag, resId, flags, userComp, proc, refCon);
    }

    private static void StandardGetFilePreview(int fileFilter, short numTypes, int[] typeList, byte[] reply) {
        FileManager.StandardGetFilePreview(fileFilter, numTypes, typeList, reply);
    }

    private static short DeleteMovieFile(byte[] fsspec) {
        return MovieToolbox.DeleteMovieFile(fsspec);
    }

    private static short CreateMovieFile(byte[] fsspec, int creator, short scriptTag, int flags, short[] resRefNum, int[] newMovie) {
        return MovieToolbox.CreateMovieFile(fsspec, creator, scriptTag, flags, resRefNum, newMovie);
    }

    private static short CreateShortcutMovieFile(byte[] fsspec, int creator, short scriptTag, int flags, int targetDataRef, int targetDataRefType) {
        return MovieToolbox.CreateShortcutMovieFile(fsspec, creator, scriptTag, flags, targetDataRef, targetDataRefType);
    }
}
