
package quicktime.std.movies;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.media.Media;
import quicktime.std.movies.media.ThreeDMediaHandler;
import quicktime.std.qtcomponents.CompressionDialog;
import quicktime.std.qtcomponents.MovieImporter;
import quicktime.util.QTByteObject;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTPointer;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;
import quicktime.vr.QTVRException;
import quicktime.vr.QTVRInstance;


public class AtomContainer extends QTHandleRef implements quicktime.jdirect.QuickTimeLib, quicktime.jdirect.QuickTimeVRLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(AtomContainer.class);
                    }
                });
            }
        }.establish();
    }

    public static AtomContainer fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(GraphicsExportGetSettingsAsAtomContainer(QTObject.ID(paramGraphicsExporter), arrayOfInt));

        return new AtomContainer(arrayOfInt[0], null, false);
    }

    public static quicktime.std.image.GECompressorInfo fromGraphicsExporterCompressor(GraphicsExporter paramGraphicsExporter) throws QTException {
        byte[] arrayOfByte = new byte[1];
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GraphicsExportCanUseCompressor(QTObject.ID(paramGraphicsExporter), arrayOfByte, arrayOfInt));

        AtomContainer localAtomContainer = arrayOfByte[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;

        return new quicktime.std.image.GECompressorInfo(arrayOfByte[0] != 0, localAtomContainer);
    }

    public static AtomContainer fromGraphicsExporterMIME(GraphicsExporter paramGraphicsExporter) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(GraphicsExportGetMIMETypeList(QTObject.ID(paramGraphicsExporter), arrayOfInt));

        return new AtomContainer(arrayOfInt[0], null, false);
    }

    public static AtomContainer fromQTVRInstanceWorld(QTVRInstance paramQTVRInstance) throws QTVRException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetVRWorld(QTObject.ID(paramQTVRInstance), arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    public static AtomContainer fromQTVRInstanceNode(QTVRInstance paramQTVRInstance, int paramInt) throws QTVRException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetNodeInfo(QTObject.ID(paramQTVRInstance), paramInt, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    public static AtomContainer fromQTHandle(QTHandleRef paramQTHandleRef) {
        if (QTObject.ID(paramQTHandleRef) != 0) {
            return new AtomContainer(QTObject.ID(paramQTHandleRef), paramQTHandleRef, paramQTHandleRef.isLocked());
        }
        return null;
    }

    public static AtomContainer fromGraphicsImporterMIME(GraphicsImporter paramGraphicsImporter) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(GraphicsImportGetMIMETypeList(QTObject.ID(paramGraphicsImporter), arrayOfInt));

        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    public static AtomContainer fromGraphicsImporterExportImage(GraphicsImporter paramGraphicsImporter) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(GraphicsImportGetExportImageTypeList(QTObject.ID(paramGraphicsImporter), arrayOfInt));

        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    public static AtomContainer fromGraphicsImporterExportSettings(GraphicsImporter paramGraphicsImporter) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException
                .checkError(GraphicsImportGetExportSettingsAsAtomContainer(QTObject.ID(paramGraphicsImporter), arrayOfInt));

        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    public static AtomContainer fromMediaInput(Media paramMedia) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetMediaInputMap(QTObject.ID(paramMedia), arrayOfInt));

        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    public static AtomContainer fromMediaProperty(Media paramMedia) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetMediaPropertyAtom(QTObject.ID(paramMedia), arrayOfInt));

        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    public static AtomContainer fromMovieExporter(quicktime.std.qtcomponents.MovieExporter paramMovieExporter) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(MovieExportGetSettingsAsAtomContainer(QTObject.ID(paramMovieExporter), arrayOfInt));

        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    public static AtomContainer fromMovieImporterMIME(MovieImporter paramMovieImporter) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(MovieImportGetMIMETypeList(QTObject.ID(paramMovieImporter), arrayOfInt));

        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    public static AtomContainer fromMovieImporterSettings(MovieImporter paramMovieImporter) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(MovieImportGetSettingsAsAtomContainer(QTObject.ID(paramMovieImporter), arrayOfInt));

        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    public static AtomContainer fromThreeDMediaHandlerObject(ThreeDMediaHandler paramThreeDMediaHandler) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(Media3DGetNamedObjectList(QTObject.ID(paramThreeDMediaHandler), arrayOfInt));

        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    public static AtomContainer fromThreeDMediaHandlerRenderer(ThreeDMediaHandler paramThreeDMediaHandler) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(Media3DGetRendererList(QTObject.ID(paramThreeDMediaHandler), arrayOfInt));

        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    public static AtomContainer fromCompressionDialog(CompressionDialog paramCompressionDialog) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SCGetSettingsAsAtomContainer(QTObject.ID(paramCompressionDialog), arrayOfInt));

        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    protected AtomContainer(int paramInt, Object paramObject, boolean paramBoolean) {
        super(paramInt, paramObject, paramBoolean);
    }

    public AtomContainer() throws QTException {
        super(allocate(), null, false);
    }

    private static int allocate() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = QTNewAtomContainer(arrayOfInt);
        if (i != 0)
            throw new StdQTException(i);
        return arrayOfInt[0];
    }

    void _setNR(int paramInt) {
        setNR(paramInt);
    }

    public AtomData getAtomData(Atom paramAtom) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        StdQTException.checkError(QTGetAtomDataPtr(_ID(), paramAtom.getAtom(), arrayOfInt1, arrayOfInt2));

        return new AtomData(arrayOfInt2[0], arrayOfInt1[0], this);
    }

    public int getAtomDataSize(Atom paramAtom) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        StdQTException.checkError(QTGetAtomDataPtr(_ID(), paramAtom.getAtom(), arrayOfInt1, arrayOfInt2));

        return arrayOfInt1[0];
    }

    public int getNextChildType(Atom paramAtom, int paramInt) throws StdQTException {
        int i = QTGetNextChildType(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt);

        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public int countChildrenOfType(Atom paramAtom, int paramInt) throws StdQTException {
        int i = QTCountChildrenOfType(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt);

        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public Atom findChildByIndex_Atom(Atom paramAtom, int paramInt1, int paramInt2) throws StdQTException {
        int i = QTFindChildByIndex(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt1, (short) paramInt2, null);

        StdQTException.checkError(GetMoviesError());
        return i != 0 ? new Atom(i) : null;
    }

    public int findChildByIndex_id(Atom paramAtom, int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = new int[1];
        QTFindChildByIndex(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt1, (short) paramInt2, arrayOfInt);

        StdQTException.checkError(GetMoviesError());
        return arrayOfInt[0];
    }

    public Atom findChildByID_Atom(Atom paramAtom, int paramInt1, int paramInt2) throws StdQTException {
        int i = QTFindChildByID(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt1, paramInt2, null);

        StdQTException.checkError(GetMoviesError());
        return i != 0 ? new Atom(i) : null;
    }

    public int findChildByID_index(Atom paramAtom, int paramInt1, int paramInt2) throws StdQTException {
        short[] arrayOfShort = new short[1];
        QTFindChildByID(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt1, paramInt2, arrayOfShort);

        StdQTException.checkError(GetMoviesError());
        return arrayOfShort[0];
    }

    public Atom nextChildAnyType(Atom paramAtom1, Atom paramAtom2) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = paramAtom1 != null ? paramAtom1.getAtom() : 0;
        StdQTException.checkError(QTNextChildAnyType(_ID(), i, paramAtom2 != null ? paramAtom2.getAtom() : 0, arrayOfInt));

        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
    }

    public void setAtomData(Atom paramAtom, QTHandleRef paramQTHandleRef) throws StdQTException {
        QTPointerRef localQTPointerRef = paramQTHandleRef.toQTPointer();
        int i = QTSetAtomData(_ID(), paramAtom.getAtom(), paramQTHandleRef.getSize(), QTObject.ID(localQTPointerRef));
        if (!isLocked())
            paramQTHandleRef.unlock();
        StdQTException.checkError(i);
    }

    public void setAtomData(Atom paramAtom, QTPointerRef paramQTPointerRef) throws StdQTException {
        StdQTException
                .checkError(QTSetAtomData(_ID(),
                                          paramAtom.getAtom(),
                                          paramQTPointerRef.getSize(),
                                          QTObject.ID(paramQTPointerRef)));
    }

    public void setAtomData(Atom paramAtom, byte[] paramArrayOfByte) throws StdQTException {
        StdQTException.checkError(QTSetAtomData(_ID(), paramAtom.getAtom(), paramArrayOfByte.length, paramArrayOfByte));
    }

    public void setAtomData(Atom paramAtom, QTByteObject paramQTByteObject) throws StdQTException {
        StdQTException
                .checkError(QTSetAtomData(_ID(),
                                          paramAtom.getAtom(),
                                          paramQTByteObject.getSize(),
                                          paramQTByteObject.getBytes()));
    }

    public void setAtomData(Atom paramAtom, int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            paramInt
        };
        StdQTException.checkError(QTSetAtomData(_ID(), paramAtom.getAtom(), 4, arrayOfInt));
    }

    public void setAtomData(Atom paramAtom, short paramShort) throws StdQTException {
        short[] arrayOfShort = {
            paramShort
        };
        StdQTException.checkError(QTSetAtomData(_ID(), paramAtom.getAtom(), 2, arrayOfShort));
    }

    public void setAtomData(Atom paramAtom, byte paramByte) throws StdQTException {
        byte[] arrayOfByte = {
            paramByte
        };
        StdQTException.checkError(QTSetAtomData(_ID(), paramAtom.getAtom(), 1, arrayOfByte));
    }

    public QTHandle copyAtomDataToHandle(Atom paramAtom) throws QTException {
        QTHandle localQTHandle = new QTHandle();
        StdQTException.checkError(QTCopyAtomDataToHandle(_ID(), paramAtom.getAtom(), QTObject.ID(localQTHandle)));

        return localQTHandle;
    }

    public int copyAtomDataToPtr(Atom paramAtom, boolean paramBoolean, QTPointer paramQTPointer) throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte b = (byte) (paramBoolean ? 1 : 0);
        StdQTException.checkError(QTCopyAtomDataToPtr(_ID(),
                                                      paramAtom.getAtom(),
                                                      b,
                                                      paramQTPointer.getSize(),
                                                      QTObject.ID(paramQTPointer),
                                                      arrayOfInt));

        return arrayOfInt[0];
    }

    public byte[] copyAtomDataToArray(Atom paramAtom) throws StdQTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        StdQTException.checkError(QTGetAtomDataPtr(_ID(), paramAtom.getAtom(), arrayOfInt1, arrayOfInt2));

        byte[] arrayOfByte = new byte[arrayOfInt1[0]];
        int[] arrayOfInt3 = {
            0
        };
        StdQTException
                .checkError(QTCopyAtomDataToPtr(_ID(),
                                                paramAtom.getAtom(),
                                                (byte) 0,
                                                arrayOfByte.length,
                                                arrayOfByte,
                                                arrayOfInt3));

        return arrayOfByte;
    }

    public int getAtomType(Atom paramAtom) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTGetAtomTypeAndID(_ID(), paramAtom.getAtom(), arrayOfInt, null));

        return arrayOfInt[0];
    }

    public int getAtomID(Atom paramAtom) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTGetAtomTypeAndID(_ID(), paramAtom.getAtom(), null, arrayOfInt));

        return arrayOfInt[0];
    }

    public AtomContainer copyAtom(Atom paramAtom) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTCopyAtom(_ID(), paramAtom.getAtom(), arrayOfInt));

        return new AtomContainer(arrayOfInt[0], null, false);
    }

    protected final void doLock() {
        QTLockContainer(_ID());
    }

    protected final void doUnlock() {
        QTUnlockContainer(_ID());
    }

    public Atom insertChild(Atom paramAtom, int paramInt1, int paramInt2, int paramInt3) throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTInsertChild(_ID(),
                                                paramAtom != null ? paramAtom.getAtom() : 0,
                                                paramInt1,
                                                paramInt2,
                                                (short) paramInt3,
                                                0,
                                                0,
                                                arrayOfInt));

        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
    }

    public Atom insertChild(Atom paramAtom,
                            int paramInt1,
                            int paramInt2,
                            int paramInt3,
                            QTByteObject paramQTByteObject) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTInsertChild(_ID(),
                                                paramAtom != null ? paramAtom.getAtom() : 0,
                                                paramInt1,
                                                paramInt2,
                                                (short) paramInt3,
                                                paramQTByteObject.getSize(),
                                                paramQTByteObject.getBytes(),
                                                arrayOfInt));

        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
    }

    public Atom insertChild(Atom paramAtom,
                            int paramInt1,
                            int paramInt2,
                            int paramInt3,
                            byte[] paramArrayOfByte) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException
                .checkError(QTInsertChild(_ID(),
                                          paramAtom != null ? paramAtom.getAtom() : 0,
                                          paramInt1,
                                          paramInt2,
                                          (short) paramInt3,
                                          paramArrayOfByte.length,
                                          paramArrayOfByte,
                                          arrayOfInt));

        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
    }

    public Atom insertChild(Atom paramAtom,
                            int paramInt1,
                            int paramInt2,
                            int paramInt3,
                            QTPointerRef paramQTPointerRef) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTInsertChild(_ID(),
                                                paramAtom != null ? paramAtom.getAtom() : 0,
                                                paramInt1,
                                                paramInt2,
                                                (short) paramInt3,
                                                paramQTPointerRef.getSize(),
                                                QTObject.ID(paramQTPointerRef),
                                                arrayOfInt));

        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
    }

    public Atom insertChild(Atom paramAtom,
                            int paramInt1,
                            int paramInt2,
                            int paramInt3,
                            QTHandleRef paramQTHandleRef) throws StdQTException {
        int[] arrayOfInt = new int[1];
        QTPointerRef localQTPointerRef = paramQTHandleRef.toQTPointer();
        int i = QTInsertChild(_ID(),
                              paramAtom != null ? paramAtom.getAtom() : 0,
                              paramInt1,
                              paramInt2,
                              (short) paramInt3,
                              paramQTHandleRef.getSize(),
                              QTObject.ID(localQTPointerRef),
                              arrayOfInt);

        if (!paramQTHandleRef.isLocked()) {
            paramQTHandleRef.unlock();
        }
        StdQTException.checkError(i);
        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
    }

    public Atom insertChild(Atom paramAtom, int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = {
            paramInt4
        };
        StdQTException.checkError(QTInsertChild(_ID(),
                                                paramAtom != null ? paramAtom.getAtom() : 0,
                                                paramInt1,
                                                paramInt2,
                                                (short) paramInt3,
                                                4,
                                                arrayOfInt2,
                                                arrayOfInt1));

        return arrayOfInt1[0] != 0 ? new Atom(arrayOfInt1[0]) : null;
    }

    public Atom insertChild(Atom paramAtom,
                            int paramInt1,
                            int paramInt2,
                            int paramInt3,
                            short paramShort) throws StdQTException {
        int[] arrayOfInt = new int[1];
        short[] arrayOfShort = {
            paramShort
        };
        StdQTException.checkError(QTInsertChild(_ID(),
                                                paramAtom != null ? paramAtom.getAtom() : 0,
                                                paramInt1,
                                                paramInt2,
                                                (short) paramInt3,
                                                2,
                                                arrayOfShort,
                                                arrayOfInt));

        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
    }

    public Atom insertChild(Atom paramAtom, int paramInt1, int paramInt2, int paramInt3, byte paramByte) throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = {
            paramByte
        };
        StdQTException.checkError(QTInsertChild(_ID(),
                                                paramAtom != null ? paramAtom.getAtom() : 0,
                                                paramInt1,
                                                paramInt2,
                                                (short) paramInt3,
                                                1,
                                                arrayOfByte,
                                                arrayOfInt));

        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
    }

    public void insertChildren(Atom paramAtom, AtomContainer paramAtomContainer) throws StdQTException {
        if (QTObject.ID(paramAtomContainer) == 0) {
            throw new StdQTException(63429);
        }
        StdQTException
                .checkError(QTInsertChildren(_ID(),
                                             paramAtom != null ? paramAtom.getAtom() : 0,
                                             QTObject.ID(paramAtomContainer)));
    }

    public void removeAtom(Atom paramAtom) throws StdQTException {
        StdQTException.checkError(QTRemoveAtom(_ID(), paramAtom.getAtom()));
    }

    public void removeChildren(Atom paramAtom) throws StdQTException {
        StdQTException.checkError(QTRemoveChildren(_ID(), paramAtom.getAtom()));
    }

    public void replaceAtom(Atom paramAtom1, AtomContainer paramAtomContainer, Atom paramAtom2) throws StdQTException {
        if (QTObject.ID(paramAtomContainer) == 0) {
            throw new StdQTException(63429);
        }
        StdQTException
                .checkError(QTReplaceAtom(_ID(), paramAtom1.getAtom(), QTObject.ID(paramAtomContainer), paramAtom2.getAtom()));
    }

    public void swapAtoms(Atom paramAtom1, Atom paramAtom2) throws StdQTException {
        StdQTException.checkError(QTSwapAtoms(_ID(), paramAtom1.getAtom(), paramAtom2.getAtom()));
    }

    public void setAtomID(Atom paramAtom, int paramInt) throws StdQTException {
        StdQTException.checkError(QTSetAtomID(_ID(), paramAtom.getAtom(), paramInt));
    }

    public void iTextAddString(Atom paramAtom, int paramInt, String paramString) throws StdQTException {
        StdQTException.checkError(ITextAddString(_ID(),
                                                 paramAtom != null ? paramAtom.getAtom() : 0,
                                                 (short) paramInt,
                                                 QTUtils.String2PString(paramString, 255)));
    }

    public void iTextRemoveString(Atom paramAtom, int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(ITextRemoveString(_ID(),
                                                    paramAtom != null ? paramAtom.getAtom() : 0,
                                                    (short) paramInt1,
                                                    paramInt2));
    }

    public IStringInfo iTextGetString(Atom paramAtom, int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        byte[] arrayOfByte = new byte[256];
        StdQTException.checkError(ITextGetString(_ID(),
                                                 paramAtom != null ? paramAtom.getAtom() : 0,
                                                 (short) paramInt,
                                                 arrayOfShort,
                                                 arrayOfByte));

        return new IStringInfo(QTUtils.PString2String(arrayOfByte, 0), arrayOfShort[0]);
    }

    public Atom getParent(Atom paramAtom) throws StdQTException {
        int i = QTGetAtomParent(_ID(), paramAtom.getAtom());
        if (i < 0)
            throw new StdQTException(i);
        return new Atom(i);
    }

    private static native int GraphicsExportGetSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportCanUseCompressor(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native int GraphicsExportGetMIMETypeList(int paramInt, int[] paramArrayOfInt);

    private static native short QTVRGetVRWorld(int paramInt, int[] paramArrayOfInt);

    private static native short QTVRGetNodeInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int GraphicsImportGetMIMETypeList(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsImportGetExportImageTypeList(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsImportGetExportSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt);

    private static native short GetMediaInputMap(int paramInt, int[] paramArrayOfInt);

    private static native short GetMediaPropertyAtom(int paramInt, int[] paramArrayOfInt);

    private static native int MovieExportGetSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt);

    private static native int MovieImportGetMIMETypeList(int paramInt, int[] paramArrayOfInt);

    private static native int MovieImportGetSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt);

    private static native int Media3DGetNamedObjectList(int paramInt, int[] paramArrayOfInt);

    private static native int Media3DGetRendererList(int paramInt, int[] paramArrayOfInt);

    private static native int SCGetSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt);

    private static native short QTNewAtomContainer(int[] paramArrayOfInt);

    private static native short QTGetAtomDataPtr(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int QTGetNextChildType(int paramInt1, int paramInt2, int paramInt3);

    private static native short GetMoviesError();

    private static native short QTCountChildrenOfType(int paramInt1, int paramInt2, int paramInt3);

    private static native int QTFindChildByIndex(int paramInt1,
                                                 int paramInt2,
                                                 int paramInt3,
                                                 short paramShort,
                                                 int[] paramArrayOfInt);

    private static native int QTFindChildByID(int paramInt1,
                                              int paramInt2,
                                              int paramInt3,
                                              int paramInt4,
                                              short[] paramArrayOfShort);

    private static native short QTNextChildAnyType(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native short QTSetAtomData(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

    private static native short QTSetAtomData(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTSetAtomData(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native short QTSetAtomData(int paramInt1, int paramInt2, int paramInt3, short[] paramArrayOfShort);

    private static native short QTCopyAtomDataToHandle(int paramInt1, int paramInt2, int paramInt3);

    private static native short QTCopyAtomDataToPtr(int paramInt1,
                                                    int paramInt2,
                                                    byte paramByte,
                                                    int paramInt3,
                                                    int paramInt4,
                                                    int[] paramArrayOfInt);

    private static native short QTCopyAtomDataToPtr(int paramInt1,
                                                    int paramInt2,
                                                    byte paramByte,
                                                    int paramInt3,
                                                    byte[] paramArrayOfByte,
                                                    int[] paramArrayOfInt);

    private static native short QTGetAtomTypeAndID(int paramInt1,
                                                   int paramInt2,
                                                   int[] paramArrayOfInt1,
                                                   int[] paramArrayOfInt2);

    private static native short QTCopyAtom(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short QTLockContainer(int paramInt);

    private static native short QTUnlockContainer(int paramInt);

    private static native short QTInsertChild(int paramInt1,
                                              int paramInt2,
                                              int paramInt3,
                                              int paramInt4,
                                              short paramShort,
                                              int paramInt5,
                                              byte[] paramArrayOfByte,
                                              int[] paramArrayOfInt);

    private static native short QTInsertChild(int paramInt1,
                                              int paramInt2,
                                              int paramInt3,
                                              int paramInt4,
                                              short paramShort,
                                              int paramInt5,
                                              int paramInt6,
                                              int[] paramArrayOfInt);

    private static native short QTInsertChild(int paramInt1,
                                              int paramInt2,
                                              int paramInt3,
                                              int paramInt4,
                                              short paramShort,
                                              int paramInt5,
                                              int[] paramArrayOfInt1,
                                              int[] paramArrayOfInt2);

    private static native short QTInsertChild(int paramInt1,
                                              int paramInt2,
                                              int paramInt3,
                                              int paramInt4,
                                              short paramShort,
                                              int paramInt5,
                                              short[] paramArrayOfShort,
                                              int[] paramArrayOfInt);

    private static native short QTInsertChildren(int paramInt1, int paramInt2, int paramInt3);

    private static native short QTRemoveAtom(int paramInt1, int paramInt2);

    private static native short QTRemoveChildren(int paramInt1, int paramInt2);

    private static native short QTReplaceAtom(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTSwapAtoms(int paramInt1, int paramInt2, int paramInt3);

    private static native short QTSetAtomID(int paramInt1, int paramInt2, int paramInt3);

    private static native short ITextAddString(int paramInt1, int paramInt2, short paramShort, byte[] paramArrayOfByte);

    private static native short ITextRemoveString(int paramInt1, int paramInt2, short paramShort, int paramInt3);

    private static native short ITextGetString(int paramInt1,
                                               int paramInt2,
                                               short paramShort,
                                               short[] paramArrayOfShort,
                                               byte[] paramArrayOfByte);

    private static native int QTGetAtomParent(int paramInt1, int paramInt2);
}
