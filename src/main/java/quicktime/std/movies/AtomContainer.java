package quicktime.std.movies;

import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

/**
 * The AtomContainer corresponds to a QuickTime atom container.
 */
public class AtomContainer extends QTHandleRef implements quicktime.jdirect.QuickTimeLib, quicktime.jdirect.QuickTimeVRLib {

    private static class AtomRecord {
        int atomHandle;
        int atomType;
        int atomId;
        byte[] data;
        AtomRecord parent;
        List<AtomRecord> children = new ArrayList<>();

        AtomRecord(int atomHandle, int atomType, int atomId, byte[] data, AtomRecord parent) {
            this.atomHandle = atomHandle;
            this.atomType = atomType;
            this.atomId = atomId;
            this.data = data != null ? data : new byte[0];
            this.parent = parent;
        }
    }

    private static int nextAtomHandle = 1;
    private static int nextContainerId = 1;
    private final Map<Integer, AtomRecord> atoms = new HashMap<>();
    private final List<AtomRecord> rootChildren = new ArrayList<>();

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

    /**
     * Retrieves the current settings from the movie export component..
     * QuickTime::MovieExportGetSettingsAsAtomContainer
     * @return an AtomContainer with the current settings.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.image.GraphicsExporter#getSettingsAsAtomContainer()}
     */
    public static AtomContainer fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(GraphicsExportGetSettingsAsAtomContainer(QTObject.ID(paramGraphicsExporter), arrayOfInt));
        return new AtomContainer(arrayOfInt[0], null, false);
    }

    /**
     * The base graphics exporter makes this call of the format-specific graphics exporter to ask whether the
     * current export operation should be done by using an image compressor.
     * QuickTime::GraphicsExportCanUseCompressor
     * @return true if allowed to use a compressor.
     * @return {@link quicktime.QTException}
     */
    public static quicktime.std.image.GECompressorInfo fromGraphicsExporterCompressor(GraphicsExporter paramGraphicsExporter) throws QTException {
        byte[] arrayOfByte = new byte[1];
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GraphicsExportCanUseCompressor(QTObject.ID(paramGraphicsExporter), arrayOfByte, arrayOfInt));
        AtomContainer localAtomContainer = arrayOfByte[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
        return new quicktime.std.image.GECompressorInfo(arrayOfByte[0] != 0, localAtomContainer);
    }

    /**
     * Returns a list of MIME types supported by the graphics export component.
     * QuickTime::GraphicsExportGetMIMETypeList
     * @return a QT atom container that contains a list of MIME types supported by the graphics import component.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.image.GraphicsExporter#getMIMETypeList()}
     */
    public static AtomContainer fromGraphicsExporterMIME(GraphicsExporter paramGraphicsExporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(GraphicsExportGetMIMETypeList(QTObject.ID(paramGraphicsExporter), arrayOfInt));
        return new AtomContainer(arrayOfInt[0], null, false);
    }

    /**
     * The QTVRGetVRWorld function returns an atom
     * container that contains general scene information about the QuickTime VR movie
     * specified, as well as a list of all the nodes in that movie.
     * QuickTime::QTVRGetVRWorld
     * @return the VRWorld AtomContainer
     * @return {@link quicktime.vr.QTVRException}
     * @see {@link quicktime.vr.QTVRInstance#getVRWorld()}
     */
    public static AtomContainer fromQTVRInstanceWorld(QTVRInstance paramQTVRInstance) throws QTVRException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetVRWorld(QTObject.ID(paramQTVRInstance), arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * You can use the QTVRGetNodeInfo function to get the node information atom container
     * that describes a node and all the hot spots in the node.
     * QuickTime::QTVRGetNodeInfo
     * @param nodeID Set this parameter to kQTVRCurrentNode to receive information
     *        about the current node
     * @return an atom container that contains information about the
     *        specified node.
     * @return {@link quicktime.vr.QTVRException}
     * @see {@link quicktime.vr.QTVRInstance#getNodeInfo(int)}
     */
    public static AtomContainer fromQTVRInstanceNode(QTVRInstance paramQTVRInstance, int paramInt) throws QTVRException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetNodeInfo(QTObject.ID(paramQTVRInstance), paramInt, arrayOfInt);
        }
        QTVRException.checkError(i);
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * This converts a QTHandle based object into an AtomContainer. This call
     * does not make a copy of the handle, but allows you to access the handles
     * data as if it were an AtomContainer, thus any changes you make to
     * returned AtomContainer will be made to the handle itself. Essentially this
     * allows you to view and change the contents of a handle with the assumption
     * that it is an AtomContainer.
     * @param handle the handle to convert to an AtomContainer.
     * @return an AtomContainer
     */
    public static AtomContainer fromQTHandle(QTHandleRef paramQTHandleRef) {
        if (QTObject.ID(paramQTHandleRef) != 0) {
            return new AtomContainer(QTObject.ID(paramQTHandleRef), paramQTHandleRef, paramQTHandleRef.isLocked());
        }
        return null;
    }

    /**
     * Returns a list of MIME types supported by the graphics import component.
     * QuickTime::GraphicsImportGetMIMETypeList
     * @return a QT atom container that contains a list of MIME types supported by the graphics import component.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.image.GraphicsImporter#getMIMETypeList()}
     */
    public static AtomContainer fromGraphicsImporterMIME(GraphicsImporter paramGraphicsImporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(GraphicsImportGetMIMETypeList(QTObject.ID(paramGraphicsImporter), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Returns information about available export formats.
     * QuickTime::GraphicsImportGetExportImageTypeList
     * @return a QT atom container that contains a list of available export formats. supported by the graphics import component.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.image.GraphicsImporter#getExportImageTypeList()}
     */
    public static AtomContainer fromGraphicsImporterExportImage(GraphicsImporter paramGraphicsImporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(GraphicsImportGetExportImageTypeList(QTObject.ID(paramGraphicsImporter), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Returns the Export settings as an Atom Container.
     * QuickTime::GraphicsImportGetExportSettingsAsAtomContainer
     * @return a QT atom container that contains Export settings.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.image.GraphicsImporter#getExportSettingsAsAtomContainer()}
     */
    public static AtomContainer fromGraphicsImporterExportSettings(GraphicsImporter paramGraphicsImporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(GraphicsImportGetExportSettingsAsAtomContainer(QTObject.ID(paramGraphicsImporter), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * This method returns you the input map of the media.
     * QuickTime::GetMediaInputMap()
     * @return an AtomContainer object for media's input map.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.media.Media#getInputMap()}
     */
    public static AtomContainer fromMediaInput(Media paramMedia) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetMediaInputMap(QTObject.ID(paramMedia), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Retrieves the properties from a media handler.
     * QuickTime::GetMediaPropertyAtom()
     * @return Contains the property atoms for the track associated with the media handler.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.media.Media#getPropertyAtom()}
     */
    public static AtomContainer fromMediaProperty(Media paramMedia) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetMediaPropertyAtom(QTObject.ID(paramMedia), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Retrieves the current settings from the movie export component..
     * QuickTime::MovieExportGetSettingsAsAtomContainer
     * @return an AtomContainer with the current settings.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.qtcomponents.MovieExporter#getExportSettingsFromAtomContainer()}
     */
    public static AtomContainer fromMovieExporter(quicktime.std.qtcomponents.MovieExporter paramMovieExporter) throws StdQTException {
        try {
            return new AtomContainer();
        } catch (QTException e) {
            throw new StdQTException(e.errorCode());
        }
    }

    /**
     * Returns a list of MIME types supported by the movie import component.
     * QuickTime::MovieImportGetMIMETypeList
     * @return an AtomContainer that contains a list of MIME types
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.qtcomponents.MovieImporter#getMIMETypeList()}
     */
    public static AtomContainer fromMovieImporterMIME(MovieImporter paramMovieImporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(MovieImportGetMIMETypeList(QTObject.ID(paramMovieImporter), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Retrieves the current settings from the movie import component..
     * QuickTime::MovieImportGetSettingsAsAtomContainer
     * @return an AtomContainer with the current settings.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.qtcomponents.MovieImporter#getImportSettingsFromAtomContainer()}
     */
    public static AtomContainer fromMovieImporterSettings(MovieImporter paramMovieImporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(MovieImportGetSettingsAsAtomContainer(QTObject.ID(paramMovieImporter), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * No QT Documentation.
     * QuickTime::Media3DGetNamedObjectList()
     * @return an atom container
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.media.ThreeDMediaHandler#getNamedObjectList()}
     */
    public static AtomContainer fromThreeDMediaHandlerObject(ThreeDMediaHandler paramThreeDMediaHandler) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(Media3DGetNamedObjectList(QTObject.ID(paramThreeDMediaHandler), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * No QT Documentation.
     * QuickTime::Media3DGetRendererList()
     * @return an atom container
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.media.ThreeDMediaHandler#getRendererList()}
     */
    public static AtomContainer fromThreeDMediaHandlerRenderer(ThreeDMediaHandler paramThreeDMediaHandler) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(Media3DGetRendererList(QTObject.ID(paramThreeDMediaHandler), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Retrieves the current configuration from the dialog
     * QuickTime::SCGetSettingsAsAtomContainer()
     * @return the settings
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.qtcomponents.CompressionDialog#getSettings()}
     */
    public static AtomContainer fromCompressionDialog(CompressionDialog paramCompressionDialog) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetSettingsAsAtomContainer(QTObject.ID(paramCompressionDialog), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    protected AtomContainer(int paramInt, Object paramObject, boolean paramBoolean) {
        super(paramInt, paramObject, paramBoolean);
    }

    /**
     * Create a new disposable AtomContainer.
     * QuickTime::QTNewAtomContainer
     */
    public AtomContainer() throws QTException {
        super(allocate(), null, false);
    }

    private static int allocate() throws StdQTException {
        return nextContainerId++;
    }

    void _setNR(int paramInt) {
        setNR(paramInt);
    }

    /**
     * Retrieves a pointer to the atom data for the specifiec leaf atom.
     * QuickTime::QTGetAtomDataPtr
     * @param atom (offset) for AtomContainers this is an "atom" that acts as the offset
     * @return the actual atom data attached to the specified atom
     * @return {@link quicktime.std.StdQTException}
     */
    public AtomData getAtomData(Atom paramAtom) throws StdQTException {
        if (paramAtom == null) return new AtomData(new byte[0]);
        AtomRecord rec = atoms.get(paramAtom.getAtom());
        return new AtomData(rec != null ? rec.data : new byte[0]);
    }

    /**
     * Retrieves a pointer to the atom data for the specifiec leaf atom.
     * QuickTime::QTGetAtomDataPtr
     * @param atom (offset) for AtomContainers this is an "atom" that acts as the offset
     * @return the size of the atom data attached to the specified atom
     * @return {@link quicktime.std.StdQTException}
     */
    public int getAtomDataSize(Atom paramAtom) throws StdQTException {
        if (paramAtom == null) return 0;
        AtomRecord rec = atoms.get(paramAtom.getAtom());
        return rec != null && rec.data != null ? rec.data.length : 0;
    }

    /**
     * This method allows you to retrieve the next atom data type in a specified atom's child list.
     * QuickTime::QTGetNextChildType
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param currentChildType Specifies an atom data type.
     * @return the found child type.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getNextChildType(Atom paramAtom, int paramInt) throws StdQTException {
        int i = QTGetNextChildType(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to determine the number of items of a given type in a parent atom's child list.
     * QuickTime::QTCountChildrenOfType
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container.
     * @param childType Specifies the type.
     * @return the number fo children.
     * @return {@link quicktime.std.StdQTException}
     */
    public int countChildrenOfType(Atom paramAtom, int paramInt) throws StdQTException {
        int i = QTCountChildrenOfType(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to search for a particular atom within an atom's child list.
     * QuickTime::QTFindChildByIndex
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container.
     * @param atomType Specifies the type of the child to be found.
     * @param index Specifies the index of a child of type atomType to be found.
     * @return the found atom.
     * @return {@link quicktime.std.StdQTException}
     */
    public Atom findChildByIndex_Atom(Atom paramAtom, int paramInt1, int paramInt2) throws StdQTException {
        int i = QTFindChildByIndex(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt1, (short) paramInt2, null);
        StdQTException.checkError(GetMoviesError());
        return i != 0 ? new Atom(i) : null;
    }

    /**
     * This method allows you to search for a particular atom within an atom's child list.
     * QuickTime::QTFindChildByIndex
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container.
     * @param atomType Specifies the type of the child to be found.
     * @param index Specifies the index of a child of type atomType to be found.
     * @return the atom's id if it is found.
     * @return {@link quicktime.std.StdQTException}
     */
    public int findChildByIndex_id(Atom paramAtom, int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = new int[1];
        QTFindChildByIndex(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt1, (short) paramInt2, arrayOfInt);
        StdQTException.checkError(GetMoviesError());
        return arrayOfInt[0];
    }

    /**
     * This method allows you to search for a particular atom within an atom's child list.
     * QuickTime::QTFindChildByID
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container.
     * @param atomType Specifies the type of the child to be found.
     * @param id Specifies the ID of a child of type atomType to be found.
     * @return the found atom.
     * @return {@link quicktime.std.StdQTException}
     */
    public Atom findChildByID_Atom(Atom paramAtom, int paramInt1, int paramInt2) throws StdQTException {
        int i = QTFindChildByID(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt1, paramInt2, null);
        StdQTException.checkError(GetMoviesError());
        return i != 0 ? new Atom(i) : null;
    }

    /**
     * This method allows you to search for a particular atom within an atom's child list.
     * QuickTime::QTFindChildByID
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container.
     * @param atomType Specifies the type of the child to be found.
     * @param id Specifies the ID of a child of type atomType to be found.
     * @return the atom's index if it is found.
     * @return {@link quicktime.std.StdQTException}
     */
    public int findChildByID_index(Atom paramAtom, int paramInt1, int paramInt2) throws StdQTException {
        short[] arrayOfShort = new short[1];
        QTFindChildByID(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt1, paramInt2, arrayOfShort);
        StdQTException.checkError(GetMoviesError());
        return arrayOfShort[0];
    }

    /**
     * This method allows you to retrieve all of a parent atom's children one by one, regardless of their type's and id's.
     * QuickTime::QTNextChildAnyType
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container.
     * @param currentChild Specifies the current child atom of parentAtom. Null implies the parentAtom.
     * @return the QTAtom which is used to return the next child following currentChild in parentAtom's child list.
     * @return {@link quicktime.std.StdQTException}
     */
    public Atom nextChildAnyType(Atom paramAtom1, Atom paramAtom2) throws StdQTException {
        List<AtomRecord> list;
        if (paramAtom1 == null || paramAtom1.getAtom() == 0) {
            list = rootChildren;
        } else {
            AtomRecord parentRec = atoms.get(paramAtom1.getAtom());
            if (parentRec == null) {
                return null;
            }
            list = parentRec.children;
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (paramAtom2 == null || paramAtom2.getAtom() == 0) {
            return new Atom(list.get(0).atomHandle);
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).atomHandle == paramAtom2.getAtom()) {
                if (i + 1 < list.size()) {
                    return new Atom(list.get(i + 1).atomHandle);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * This method allows you to set a leaf atom's data.
     * QuickTime::QTSetAtomData
     * @param atom Specifies the atom within this container. Use Atom.kParentIsContainer if atom is container.
     * @param data Contains the data for the atom.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setAtomData(Atom paramAtom, QTHandleRef paramQTHandleRef) throws StdQTException {
        setAtomData(paramAtom, paramQTHandleRef != null ? paramQTHandleRef.getBytes() : null);
    }

    /**
     * This method allows you to set a leaf atom's data.
     * QuickTime::QTSetAtomData
     * @param atom Specifies the atom within this container. Use Atom.kParentIsContainer if atom is container.
     * @param data Contains the data for the atom.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setAtomData(Atom paramAtom, QTPointerRef paramQTPointerRef) throws StdQTException {
        setAtomData(paramAtom, paramQTPointerRef != null ? paramQTPointerRef.getBytes() : null);
    }

    public void setAtomData(Atom paramAtom, byte[] paramArrayOfByte) throws StdQTException {
        if (paramAtom != null) {
            AtomRecord rec = atoms.get(paramAtom.getAtom());
            if (rec != null) {
                rec.data = paramArrayOfByte != null ? paramArrayOfByte.clone() : new byte[0];
            }
        }
    }

    /**
     * This method allows you to set a leaf atom's data.
     * QuickTime::QTSetAtomData
     * @param atom Specifies the atom within this container.
     * @param data Contains the data for the atom.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setAtomData(Atom paramAtom, QTByteObject paramQTByteObject) throws StdQTException {
        setAtomData(paramAtom, paramQTByteObject != null ? paramQTByteObject.getBytes() : null);
    }

    /**
     * This method allows you to set a leaf atom's data.
     * QuickTime::QTSetAtomData
     * @param atom Specifies the atom within this container.
     * @param data Contains the data for the atom. This value will be endian flipped if required.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setAtomData(Atom paramAtom, int paramInt) throws StdQTException {
        int[] arrayOfInt = { paramInt };
        StdQTException.checkError(QTSetAtomData(_ID(), paramAtom.getAtom(), 4, arrayOfInt));
    }

    /**
     * This method allows you to set a leaf atom's data.
     * QuickTime::QTSetAtomData
     * @param atom Specifies the atom within this container.
     * @param data Contains the data for the atom. This value will be endian flipped if required.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setAtomData(Atom paramAtom, short paramShort) throws StdQTException {
        short[] arrayOfShort = { paramShort };
        StdQTException.checkError(QTSetAtomData(_ID(), paramAtom.getAtom(), 2, arrayOfShort));
    }

    /**
     * This method allows you to set a leaf atom's data.
     * QuickTime::QTSetAtomData
     * @param atom Specifies the atom within this container.
     * @param data Contains the data for the atom. This value will be endian flipped if required.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setAtomData(Atom paramAtom, byte paramByte) throws StdQTException {
        byte[] arrayOfByte = { paramByte };
        StdQTException.checkError(QTSetAtomData(_ID(), paramAtom.getAtom(), 1, arrayOfByte));
    }

    /**
     * This method returns a leaf atom's data.
     * QuickTime::QTCopyAtomDataToHandle
     * @param atom Specifies the atom within this container.
     * @return A QTHandle object containing the leaf atom's data.
     * @return {@link quicktime.QTException}
     */
    public QTHandle copyAtomDataToHandle(Atom paramAtom) throws QTException {
        QTHandle localQTHandle = new QTHandle();
        StdQTException.checkError(QTCopyAtomDataToHandle(_ID(), paramAtom.getAtom(), QTObject.ID(localQTHandle)));
        return localQTHandle;
    }

    /**
     * This method returns a leaf atom's data to the buffer pointed to by the data object.
     * QuickTime::QTCopyAtomDataToPtr
     * @param atom Specifies the atom within this container.
     * @param sizeOrLessOK True indicates that it is OK to receive less data than specified by the size of the data Pointer.
     * @param data A QTPointer object containing the buffer for the leaf atom's data.
     * @return the actual number of bytes retrieved.
     * @return {@link quicktime.std.StdQTException}
     */
    public int copyAtomDataToPtr(Atom paramAtom, boolean paramBoolean, QTPointer paramQTPointer) throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte b = (byte) (paramBoolean ? 1 : 0);
        StdQTException.checkError(QTCopyAtomDataToPtr(_ID(), paramAtom.getAtom(), b, paramQTPointer.getSize(), QTObject.ID(paramQTPointer), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Copies the atom data for the specifiec leaf atom.
     * QuickTime::QTGetAtomDataPtr
     * @param atom (offset) for AtomContainers this is an "atom" that acts as the offset
     * @param {@link quicktime.std.StdQTException}
     */
    public byte[] copyAtomDataToArray(Atom paramAtom) throws StdQTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        StdQTException.checkError(QTGetAtomDataPtr(_ID(), paramAtom.getAtom(), arrayOfInt1, arrayOfInt2));
        byte[] arrayOfByte = new byte[arrayOfInt1[0]];
        int[] arrayOfInt3 = { 0 };
        StdQTException.checkError(QTCopyAtomDataToPtr(_ID(), paramAtom.getAtom(), (byte) 0, arrayOfByte.length, arrayOfByte, arrayOfInt3));
        return arrayOfByte;
    }

    /**
     * This method returns the type of a given atom.
     * QuickTime::QTGetAtomTypeAndID
     * @param atom Specifies the atom within this container.
     * @return the type of a given atom
     * @return {@link quicktime.std.StdQTException}
     */
    public int getAtomType(Atom paramAtom) throws StdQTException {
        if (paramAtom == null) return 0;
        AtomRecord rec = atoms.get(paramAtom.getAtom());
        return rec != null ? rec.atomType : 0;
    }

    /**
     * This method returns the id of a given atom.
     * QuickTime::QTGetAtomTypeAndID
     * @param atom Specifies the atom within this container.
     * @return the id of a given atom.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getAtomID(Atom paramAtom) throws StdQTException {
        if (paramAtom == null) return 0;
        AtomRecord rec = atoms.get(paramAtom.getAtom());
        return rec != null ? rec.atomId : 0;
    }

    /**
     * This method allows a complete copy of an atom and all of its children to be extracted
     * from its container and be placed in an new atom data structure.
     * QuickTime::QTCopyAtom
     * @param atom Specifies the atom within this container.
     * @return a new AtomContainer that contains the copy of the specifed atom..
     * @return {@link quicktime.std.StdQTException}
     */
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

    /**
     * This method creates a new empty child atom for parentAtom.
     * QuickTime::QTInsertChild
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @return The new atom.
     * @return {@link quicktime.QTException}
     */
    public Atom insertChild(Atom paramAtom, int paramInt1, int paramInt2, int paramInt3) throws QTException {
        return insertChild(paramAtom, paramInt1, paramInt2, paramInt3, (byte[]) null);
    }

    /**
     * This method creates a new child atom for parentAtom.
     * QuickTime::QTInsertChild
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @param data A QTByteObject that contains the data of the specifed atom.
     * @return The new atom.
     * @return {@link quicktime.std.StdQTException}
     */
    public Atom insertChild(Atom paramAtom, int paramInt1, int paramInt2, int paramInt3, QTByteObject paramQTByteObject) throws StdQTException {
        return insertChild(paramAtom, paramInt1, paramInt2, paramInt3, paramQTByteObject != null ? paramQTByteObject.getBytes() : null);
    }

    public Atom insertChild(Atom paramAtom, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) throws StdQTException {
        int handle = nextAtomHandle++;
        AtomRecord parentRec = (paramAtom == null || paramAtom.getAtom() == 0) ? null : atoms.get(paramAtom.getAtom());
        AtomRecord rec = new AtomRecord(handle, paramInt1, paramInt2, paramArrayOfByte, parentRec);
        atoms.put(handle, rec);
        List<AtomRecord> targetList = (parentRec != null) ? parentRec.children : rootChildren;
        if (paramInt3 > 0 && paramInt3 <= targetList.size()) {
            targetList.add(paramInt3 - 1, rec);
        } else {
            targetList.add(rec);
        }
        return new Atom(handle);
    }

    /**
     * This method creates a new child atom for parentAtom.
     * QuickTime::QTInsertChild
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @param data A QTPointerRef object contains the data of the specifed atom.
     * @return The new atom.
     * @return {@link quicktime.std.StdQTException}
     */
    public Atom insertChild(Atom paramAtom, int paramInt1, int paramInt2, int paramInt3, QTPointerRef paramQTPointerRef) throws StdQTException {
        return insertChild(paramAtom, paramInt1, paramInt2, paramInt3, paramQTPointerRef != null ? paramQTPointerRef.getBytes() : null);
    }

    /**
     * This method creates a new child atom for parentAtom.
     * QuickTime::QTInsertChild
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @param data A QTHandleRef object contains the data of the specifed atom.
     * @return The new atom.
     * @return {@link quicktime.std.StdQTException}
     */
    public Atom insertChild(Atom paramAtom, int paramInt1, int paramInt2, int paramInt3, QTHandleRef paramQTHandleRef) throws StdQTException {
        return insertChild(paramAtom, paramInt1, paramInt2, paramInt3, paramQTHandleRef != null ? paramQTHandleRef.getBytes() : null);
    }

    /**
     * This method creates a new child atom for parentAtom.
     * QuickTime::QTInsertChild
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @param intValue Specifies the integer data of the specified atom. This value will be endian flipped if required.
     * @return The new atom.
     * @return {@link quicktime.std.StdQTException}
     */
    public Atom insertChild(Atom paramAtom, int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws StdQTException {
        byte[] bytes = new byte[4];
        ByteBuffer.wrap(bytes).putInt(paramInt4);
        return insertChild(paramAtom, paramInt1, paramInt2, paramInt3, bytes);
    }

    /**
     * This method creates a new child atom for parentAtom.
     * QuickTime::QTInsertChild
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @param shortValue Specifies the integer data of the specified atom. This value will be endian flipped if required.
     * @return The new atom.
     * @return {@link quicktime.std.StdQTException}
     */
    public Atom insertChild(Atom paramAtom, int paramInt1, int paramInt2, int paramInt3, short paramShort) throws StdQTException {
        byte[] bytes = new byte[2];
        ByteBuffer.wrap(bytes).putShort(paramShort);
        return insertChild(paramAtom, paramInt1, paramInt2, paramInt3, bytes);
    }

    /**
     * This method creates a new child atom for parentAtom.
     * QuickTime::QTInsertChild
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @return The new atom.
     * @return {@link quicktime.std.StdQTException}
     */
    public Atom insertChild(Atom paramAtom, int paramInt1, int paramInt2, int paramInt3, byte paramByte) throws StdQTException {
        return insertChild(paramAtom, paramInt1, paramInt2, paramInt3, new byte[] { paramByte });
    }

    /**
     * This method inserts all of the atoms in childrenContainer as child atoms of the atom specified by the parentAtom.
     * QuickTime::QTInsertChildren
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param childrenContainer Specifies the atom data structure which contains all of the children to be inserted.
     * @throws invalidAtomContainerErr if childrenContainer is null
     * @throws quicktime.std.StdQTException
     */
    public void insertChildren(Atom paramAtom, AtomContainer paramAtomContainer) throws StdQTException {
        if (QTObject.ID(paramAtomContainer) == 0) {
            throw new StdQTException(63429);
        }
        StdQTException.checkError(QTInsertChildren(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, QTObject.ID(paramAtomContainer)));
    }

    /**
     * This method removes an atom and all of its children from an atom data structure.
     * QuickTime::QTRemoveAtom
     * @param atom Specifies the atom within this atom container which is to be removed.
     * @param {@link quicktime.std.StdQTException}
     */
    public void removeAtom(Atom paramAtom) throws StdQTException {
        StdQTException.checkError(QTRemoveAtom(_ID(), paramAtom.getAtom()));
    }

    /**
     * This method removes all of the children of an atom.
     * QuickTime::QTRemoveChildren
     * @param atom Specifies the atom within this atom container which is to be removed.
     * @param {@link quicktime.std.StdQTException}
     */
    public void removeChildren(Atom paramAtom) throws StdQTException {
        StdQTException.checkError(QTRemoveChildren(_ID(), paramAtom.getAtom()));
    }

    /**
     * This method replaces an atom and all of its children with a different atom and all of its children.
     * QuickTime::QTReplaceAtom
     * @param targetAtom Specifies the atom within this atom container which is to be replaced.
     * @param replacementContainer Specifies the atom data structure which contains replacementAtom.
     * @param replacementAtom Specifies the atom within replacementContainer which replaces targetAtom.
     * @throws invalidAtomContainerErr if targetAtom, replacementContainer or replacementAtom are null
     * @throws quicktime.std.StdQTException
     */
    public void replaceAtom(Atom paramAtom1, AtomContainer paramAtomContainer, Atom paramAtom2) throws StdQTException {
        if (QTObject.ID(paramAtomContainer) == 0) {
            throw new StdQTException(63429);
        }
        StdQTException.checkError(QTReplaceAtom(_ID(), paramAtom1.getAtom(), QTObject.ID(paramAtomContainer), paramAtom2.getAtom()));
    }

    /**
     * This method swaps two atoms within an atomData structure.
     * QuickTime::QTSwapAtoms
     * @param atom1 Specifies an atom within this container which is to be swapped with atom2.
     * @param atom2 Specifies the atom within this container which is to be swapped with atom1.
     * @param {@link quicktime.std.StdQTException}
     */
    public void swapAtoms(Atom paramAtom1, Atom paramAtom2) throws StdQTException {
        StdQTException.checkError(QTSwapAtoms(_ID(), paramAtom1.getAtom(), paramAtom2.getAtom()));
    }

    /**
     * This method allows you to change the ID of an atom.
     * QuickTime::QTSetAtomID
     * @param atom Specifies an atom within this container which is to be changed.
     * @param newID Specifies the new ID for the atom.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setAtomID(Atom paramAtom, int paramInt) throws StdQTException {
        StdQTException.checkError(QTSetAtomID(_ID(), paramAtom.getAtom(), paramInt));
    }

    /**
     * No QT Documentation.
     * QuickTime::ITextAddString
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param theRegionCode Specifies the region code. Use 0 for verUS
     * @param theString a String must be specified.
     * @param {@link quicktime.std.StdQTException}
     */
    public void iTextAddString(Atom paramAtom, int paramInt, String paramString) throws StdQTException {
        StdQTException.checkError(ITextAddString(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, (short) paramInt, QTUtils.String2PString(paramString, 255)));
    }

    /**
     * No QT Documentation.
     * QuickTime::ITextRemoveString
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container
     * @param theRegionCode Specifies the region code. Use 0 for verUS
     * @param {@link quicktime.std.StdQTException}
     */
    public void iTextRemoveString(Atom paramAtom, int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(ITextRemoveString(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, (short) paramInt1, paramInt2));
    }

    /**
     * No QT Documentation.
     * QuickTime::ITextGetString
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container
     * @param theRegionCode Specifies the region code. Use 0 for verUS
     * @param {@link quicktime.std.StdQTException}
     */
    public IStringInfo iTextGetString(Atom paramAtom, int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        byte[] arrayOfByte = new byte[256];
        StdQTException.checkError(ITextGetString(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, (short) paramInt, arrayOfShort, arrayOfByte));
        return new IStringInfo(QTUtils.PString2String(arrayOfByte, 0), arrayOfShort[0]);
    }

    /**
     * This method returns the parent atom of the specified child atom
     * QuickTime::QTGetAtomParent
     * @param childAtom Specifies the child atom within this container.
     * @return The parent atom.
     * @return {@link quicktime.std.StdQTException}
     */
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

    private static native int QTFindChildByIndex(int paramInt1, int paramInt2, int paramInt3, short paramShort, int[] paramArrayOfInt);

    private static native int QTFindChildByID(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[] paramArrayOfShort);

    private static native short QTNextChildAnyType(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native short QTSetAtomData(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

    private static native short QTSetAtomData(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTSetAtomData(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native short QTSetAtomData(int paramInt1, int paramInt2, int paramInt3, short[] paramArrayOfShort);

    private static native short QTCopyAtomDataToHandle(int paramInt1, int paramInt2, int paramInt3);

    private static native short QTCopyAtomDataToPtr(int paramInt1, int paramInt2, byte paramByte, int paramInt3, int paramInt4, int[] paramArrayOfInt);

    private static native short QTCopyAtomDataToPtr(int paramInt1, int paramInt2, byte paramByte, int paramInt3, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native short QTGetAtomTypeAndID(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native short QTCopyAtom(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short QTLockContainer(int paramInt);

    private static native short QTUnlockContainer(int paramInt);

    private static native short QTInsertChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort, int paramInt5, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native short QTInsertChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort, int paramInt5, int paramInt6, int[] paramArrayOfInt);

    private static native short QTInsertChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort, int paramInt5, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native short QTInsertChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort, int paramInt5, short[] paramArrayOfShort, int[] paramArrayOfInt);

    private static native short QTInsertChildren(int paramInt1, int paramInt2, int paramInt3);

    private static native short QTRemoveAtom(int paramInt1, int paramInt2);

    private static native short QTRemoveChildren(int paramInt1, int paramInt2);

    private static native short QTReplaceAtom(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short QTSwapAtoms(int paramInt1, int paramInt2, int paramInt3);

    private static native short QTSetAtomID(int paramInt1, int paramInt2, int paramInt3);

    private static native short ITextAddString(int paramInt1, int paramInt2, short paramShort, byte[] paramArrayOfByte);

    private static native short ITextRemoveString(int paramInt1, int paramInt2, short paramShort, int paramInt3);

    private static native short ITextGetString(int paramInt1, int paramInt2, short paramShort, short[] paramArrayOfShort, byte[] paramArrayOfByte);

    private static native int QTGetAtomParent(int paramInt1, int paramInt2);

    @Override
    protected void _dispose() throws QTException {
    }
}
