package quicktime.std.movies;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.AtomManager;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.internal.toolbox.GraphicsExportManager;
import quicktime.internal.toolbox.GraphicsImportManager;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MediaHandlerManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.internal.toolbox.VRManager;
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

    /**
     * Retrieves the current settings from the movie export component..
     * @see "QuickTime::MovieExportGetSettingsAsAtomContainer"
     * @return an AtomContainer with the current settings.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.GraphicsExporter#getSettingsAsAtomContainer()
     */
    public static AtomContainer fromGraphicsExporter(GraphicsExporter ge) throws StdQTException {
        int[] container = { 0 };
        StdQTException.checkError(GraphicsExportGetSettingsAsAtomContainer(QTObject.ID(ge), container));
        return new AtomContainer(container[0], null, false);
    }

    /**
     * The base graphics exporter makes this call of the format-specific graphics exporter to ask whether the
     * current export operation should be done by using an image compressor.
     * @see "QuickTime::GraphicsExportCanUseCompressor"
     * //@return true if allowed to use a compressor.
     * @throws quicktime.QTException
     */
    public static quicktime.std.image.GECompressorInfo fromGraphicsExporterCompressor(GraphicsExporter ge) throws QTException {
        byte[] use = new byte[1];
        int[] codecSettings = new int[1];
        StdQTException.checkError(GraphicsExportCanUseCompressor(QTObject.ID(ge), use, codecSettings));
        AtomContainer atomContainer = use[0] != 0 ? new AtomContainer(codecSettings[0], null, false) : null;
        return new quicktime.std.image.GECompressorInfo(use[0] != 0, atomContainer);
    }

    /**
     * Returns a list of MIME types supported by the graphics export component.
     * @see "QuickTime::GraphicsExportGetMIMETypeList"
     * @return a QT atom container that contains a list of MIME types supported by the graphics import component.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.GraphicsExporter#getMIMETypeList()
     */
    public static AtomContainer fromGraphicsExporterMIME(GraphicsExporter ge) throws StdQTException {
        int[] container = { 0 };
        StdQTException.checkError(GraphicsExportGetMIMETypeList(QTObject.ID(ge), container));
        return new AtomContainer(container[0], null, false);
    }

    /**
     * The QTVRGetVRWorld function returns an atom
     * container that contains general scene information about the QuickTime VR movie
     * specified, as well as a list of all the nodes in that movie.
     * @see "QuickTime::QTVRGetVRWorld"
     * @return the VRWorld AtomContainer
     * @throws quicktime.vr.QTVRException
     * @see quicktime.vr.QTVRInstance#getVRWorld()
     */
    public static AtomContainer fromQTVRInstanceWorld(QTVRInstance qtvr) throws QTVRException {
        int[] vrWorld = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetVRWorld(QTObject.ID(qtvr), vrWorld);
        }
        QTVRException.checkError(i);
        return vrWorld[0] != 0 ? new AtomContainer(vrWorld[0], null, false) : null;
    }

    /**
     * You can use the QTVRGetNodeInfo function to get the node information atom container
     * that describes a node and all the hot spots in the node.
     * @see "QuickTime::QTVRGetNodeInfo"
     * @param nodeID Set this parameter to kQTVRCurrentNode to receive information
     *        about the current node
     * @return an atom container that contains information about the
     *        specified node.
     * @throws quicktime.vr.QTVRException
     * @see quicktime.vr.QTVRInstance#getNodeInfo(int)
     */
    public static AtomContainer fromQTVRInstanceNode(QTVRInstance qtvr, int nodeID) throws QTVRException {
        int[] nodeInfo = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetNodeInfo(QTObject.ID(qtvr), nodeID, nodeInfo);
        }
        QTVRException.checkError(i);
        return nodeInfo[0] != 0 ? new AtomContainer(nodeInfo[0], null, false) : null;
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
    public static AtomContainer fromQTHandle(QTHandleRef handle) {
        if (QTObject.ID(handle) != 0) {
            return new AtomContainer(QTObject.ID(handle), handle, handle.isLocked());
        }
        return null;
    }

    /**
     * Returns a list of MIME types supported by the graphics import component.
     * @see "QuickTime::GraphicsImportGetMIMETypeList"
     * @return a QT atom container that contains a list of MIME types supported by the graphics import component.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.GraphicsImporter#getMIMETypeList()
     */
    public static AtomContainer fromGraphicsImporterMIME(GraphicsImporter gi) throws StdQTException {
        int[] container = { 0 };
        StdQTException.checkError(GraphicsImportGetMIMETypeList(QTObject.ID(gi), container));
        return container[0] != 0 ? new AtomContainer(container[0], null, false) : null;
    }

    /**
     * Returns information about available export formats.
     * @see "QuickTime::GraphicsImportGetExportImageTypeList"
     * @return a QT atom container that contains a list of available export formats. supported by the graphics import component.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.GraphicsImporter#getExportImageTypeList()
     */
    public static AtomContainer fromGraphicsImporterExportImage(GraphicsImporter gi) throws StdQTException {
        int[] container = { 0 };
        StdQTException.checkError(GraphicsImportGetExportImageTypeList(QTObject.ID(gi), container));
        return container[0] != 0 ? new AtomContainer(container[0], null, false) : null;
    }

    /**
     * Returns the Export settings as an Atom Container.
     * @see "QuickTime::GraphicsImportGetExportSettingsAsAtomContainer"
     * @return a QT atom container that contains Export settings.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.GraphicsImporter#getExportSettingsAsAtomContainer()
     */
    public static AtomContainer fromGraphicsImporterExportSettings(GraphicsImporter gi) throws StdQTException {
        int[] container = { 0 };
        StdQTException.checkError(GraphicsImportGetExportSettingsAsAtomContainer(QTObject.ID(gi), container));
        return container[0] != 0 ? new AtomContainer(container[0], null, false) : null;
    }

    /**
     * This method returns you the input map of the media.
     * @see "QuickTime::GetMediaInputMap()"
     * @return an AtomContainer object for media's input map.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.media.Media#getInputMap()
     */
    public static AtomContainer fromMediaInput(Media media) throws StdQTException {
        int[] inputMap = new int[1];
        StdQTException.checkError(GetMediaInputMap(QTObject.ID(media), inputMap));
        return inputMap[0] != 0 ? new AtomContainer(inputMap[0], null, false) : null;
    }

    /**
     * Retrieves the properties from a media handler.
     * @see "QuickTime::GetMediaPropertyAtom()"
     * @return Contains the property atoms for the track associated with the media handler.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.media.Media#getPropertyAtom()
     */
    public static AtomContainer fromMediaProperty(Media media) throws StdQTException {
        int[] propertyAtom = new int[1];
        StdQTException.checkError(GetMediaPropertyAtom(QTObject.ID(media), propertyAtom));
        return propertyAtom[0] != 0 ? new AtomContainer(propertyAtom[0], null, false) : null;
    }

    /**
     * Retrieves the current settings from the movie export component.
     * @see "QuickTime::MovieExportGetSettingsAsAtomContainer"
     * @return an AtomContainer with the current settings.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.qtcomponents.MovieExporter#getExportSettingsFromAtomContainer()
     */
    public static AtomContainer fromMovieExporter(quicktime.std.qtcomponents.MovieExporter ci) throws StdQTException {
        int[] settings = { 0 };
        StdQTException.checkError(MovieExportGetSettingsAsAtomContainer(QTObject.ID(ci), settings));
        return settings[0] != 0 ? new AtomContainer(settings[0], null, false) : null;
    }

    /**
     * Returns a list of MIME types supported by the movie import component.
     * @see "QuickTime::MovieImportGetMIMETypeList"
     * @return an AtomContainer that contains a list of MIME types
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.qtcomponents.MovieImporter#getMIMETypeList()
     */
    public static AtomContainer fromMovieImporterMIME(MovieImporter ci) throws StdQTException {
        int[] list = { 0 };
        StdQTException.checkError(MovieImportGetMIMETypeList(QTObject.ID(ci), list));
        return list[0] != 0 ? new AtomContainer(list[0], null, false) : null;
    }

    /**
     * Retrieves the current settings from the movie import component.
     * @see "QuickTime::MovieImportGetSettingsAsAtomContainer"
     * @return an AtomContainer with the current settings.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.qtcomponents.MovieImporter#getImportSettingsFromAtomContainer()
     */
    public static AtomContainer fromMovieImporterSettings(MovieImporter ci) throws StdQTException {
        int[] settings = { 0 };
        StdQTException.checkError(MovieImportGetSettingsAsAtomContainer(QTObject.ID(ci), settings));
        return settings[0] != 0 ? new AtomContainer(settings[0], null, false) : null;
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::Media3DGetNamedObjectList()"
     * @return an atom container
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.media.ThreeDMediaHandler#getNamedObjectList()
     */
    public static AtomContainer fromThreeDMediaHandlerObject(ThreeDMediaHandler mh) throws StdQTException {
        int[] list = new int[1];
        StdQTException.checkError(Media3DGetNamedObjectList(QTObject.ID(mh), list));
        return list[0] != 0 ? new AtomContainer(list[0], null, false) : null;
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::Media3DGetRendererList()"
     * @return an atom container
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.media.ThreeDMediaHandler#getRendererList()
     */
    public static AtomContainer fromThreeDMediaHandlerRenderer(ThreeDMediaHandler mh) throws StdQTException {
        int[] list = new int[1];
        StdQTException.checkError(Media3DGetRendererList(QTObject.ID(mh), list));
        return list[0] != 0 ? new AtomContainer(list[0], null, false) : null;
    }

    /**
     * Retrieves the current configuration from the dialog
     * @see "QuickTime::SCGetSettingsAsAtomContainer()"
     * @return the settings
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.qtcomponents.CompressionDialog#getSettings()
     */
    public static AtomContainer fromCompressionDialog(CompressionDialog ci) throws StdQTException {
        int[] settings = { 0 };
        StdQTException.checkError(SCGetSettingsAsAtomContainer(QTObject.ID(ci), settings));
        return settings[0] != 0 ? new AtomContainer(settings[0], null, false) : null;
    }

    protected AtomContainer(int nativeRep, Object ref, boolean locked) {
        super(nativeRep, ref, locked);
    }

    /**
     * Create a new disposable AtomContainer.
     * @see "QuickTime::QTNewAtomContainer"
     */
    public AtomContainer() throws QTException {
        super(allocate(), null, false);
    }

    private static int allocate() throws StdQTException {
        int[] container = { 0 };
        int i = QTNewAtomContainer(container);
        if (i != 0)
            throw new StdQTException(i);
        return container[0];
    }

    void _setNR(int nr) {
        setNR(nr);
    }

    /**
     * Retrieves a pointer to the atom data for the specifiec leaf atom.
     * @see "QuickTime::QTGetAtomDataPtr"
     * @param atom (offset) for AtomContainers this is an "atom" that acts as the offset
     * @return the actual atom data attached to the specified atom
     * @throws quicktime.std.StdQTException
     */
    public AtomData getAtomData(Atom atom) throws StdQTException {
        int[] size = new int[1];
        int[] ptr = new int[1];
        StdQTException.checkError(QTGetAtomDataPtr(_ID(), atom.getAtom(), size, ptr));
        return new AtomData(ptr[0], size[0], this);
    }

    /**
     * Retrieves a pointer to the atom data for the specifiec leaf atom.
     * @see "QuickTime::QTGetAtomDataPtr"
     * @param atom (offset) for AtomContainers this is an "atom" that acts as the offset
     * @return the size of the atom data attached to the specified atom
     * @throws quicktime.std.StdQTException
     */
    public int getAtomDataSize(Atom atom) throws StdQTException {
        int[] size = new int[1];
        int[] ptr = new int[1];
        StdQTException.checkError(QTGetAtomDataPtr(_ID(), atom.getAtom(), size, ptr));
        return size[0];
    }

    /**
     * This method allows you to retrieve the next atom data type in a specified atom's child list.
     * @see "QuickTime::QTGetNextChildType"
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param currentChildType Specifies an atom data type.
     * @return the found child type.
     * @throws quicktime.std.StdQTException
     */
    public int getNextChildType(Atom parentAtom, int currentChildType) throws StdQTException {
        int i = QTGetNextChildType(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, currentChildType);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to determine the number of items of a given type in a parent atom's child list.
     * @see "QuickTime::QTCountChildrenOfType"
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container.
     * @param childType Specifies the type.
     * @return the number fo children.
     * @throws quicktime.std.StdQTException
     */
    public int countChildrenOfType(Atom parentAtom, int childType) throws StdQTException {
        int i = QTCountChildrenOfType(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, childType);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows you to search for a particular atom within an atom's child list.
     * @see "QuickTime::QTFindChildByIndex"
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container.
     * @param atomType Specifies the type of the child to be found.
     * @param index Specifies the index of a child of type atomType to be found.
     * @return the found atom.
     * @throws quicktime.std.StdQTException
     */
    public Atom findChildByIndex_Atom(Atom parentAtom, int atomType, int index) throws StdQTException {
        int i = QTFindChildByIndex(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, (short) index, null);
        StdQTException.checkError(GetMoviesError());
        return i != 0 ? new Atom(i) : null;
    }

    /**
     * This method allows you to search for a particular atom within an atom's child list.
     * @see "QuickTime::QTFindChildByIndex"
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container.
     * @param atomType Specifies the type of the child to be found.
     * @param index Specifies the index of a child of type atomType to be found.
     * @return the atom's id if it is found.
     * @throws quicktime.std.StdQTException
     */
    public int findChildByIndex_id(Atom parentAtom, int atomType, int index) throws StdQTException {
        int[] id = new int[1];
        QTFindChildByIndex(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, (short) index, id);
        StdQTException.checkError(GetMoviesError());
        return id[0];
    }

    /**
     * This method allows you to search for a particular atom within an atom's child list.
     * @see "QuickTime::QTFindChildByID"
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container.
     * @param atomType Specifies the type of the child to be found.
     * @param id Specifies the ID of a child of type atomType to be found.
     * @return the found atom.
     * @throws quicktime.std.StdQTException
     */
    public Atom findChildByID_Atom(Atom parentAtom, int atomType, int id) throws StdQTException {
        int i = QTFindChildByID(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, null);
        StdQTException.checkError(GetMoviesError());
        return i != 0 ? new Atom(i) : null;
    }

    /**
     * This method allows you to search for a particular atom within an atom's child list.
     * @see "QuickTime::QTFindChildByID"
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container.
     * @param atomType Specifies the type of the child to be found.
     * @param id Specifies the ID of a child of type atomType to be found.
     * @return the atom's index if it is found.
     * @throws quicktime.std.StdQTException
     */
    public int findChildByID_index(Atom parentAtom, int atomType, int id) throws StdQTException {
        short[] index = new short[1];
        QTFindChildByID(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, index);
        StdQTException.checkError(GetMoviesError());
        return index[0];
    }

    /**
     * This method allows you to retrieve all of a parent atom's children one by one, regardless of their type's and id's.
     * @see "QuickTime::QTNextChildAnyType"
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container.
     * @param currentChild Specifies the current child atom of parentAtom. Null implies the parentAtom.
     * @return the QTAtom which is used to return the next child following currentChild in parentAtom's child list.
     * @throws quicktime.std.StdQTException
     */
    public Atom nextChildAnyType(Atom parentAtom, Atom currentChild) throws StdQTException {
        int[] nextChild = new int[1];
        int i = parentAtom != null ? parentAtom.getAtom() : 0;
        StdQTException.checkError(QTNextChildAnyType(_ID(), i, currentChild != null ? currentChild.getAtom() : 0, nextChild));
        return nextChild[0] != 0 ? new Atom(nextChild[0]) : null;
    }

    /**
     * This method allows you to set a leaf atom's data.
     * @see "QuickTime::QTSetAtomData"
     * @param atom Specifies the atom within this container. Use Atom.kParentIsContainer if atom is container.
     * @param data Contains the data for the atom.
     * @throws quicktime.std.StdQTException
     */
    public void setAtomData(Atom atom, QTHandleRef data) throws StdQTException {
        QTPointerRef qtPointerRef = data.toQTPointer();
        int i = QTSetAtomData(_ID(), atom.getAtom(), data.getSize(), QTObject.ID(qtPointerRef));
        if (!isLocked())
            data.unlock();
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to set a leaf atom's data.
     * @see "QuickTime::QTSetAtomData"
     * @param atom Specifies the atom within this container. Use Atom.kParentIsContainer if atom is container.
     * @param data Contains the data for the atom.
     * @throws quicktime.std.StdQTException
     */
    public void setAtomData(Atom atom, QTPointerRef data) throws StdQTException {
        StdQTException.checkError(QTSetAtomData(_ID(), atom.getAtom(), data.getSize(), QTObject.ID(data)));
    }

    public void setAtomData(Atom atom, byte[] data) throws StdQTException {
        StdQTException.checkError(QTSetAtomData(_ID(), atom.getAtom(), data.length, data));
    }

    /**
     * This method allows you to set a leaf atom's data.
     * @see "QuickTime::QTSetAtomData"
     * @param atom Specifies the atom within this container.
     * @param data Contains the data for the atom.
     * @throws quicktime.std.StdQTException
     */
    public void setAtomData(Atom atom, QTByteObject data) throws StdQTException {
        StdQTException.checkError(QTSetAtomData(_ID(), atom.getAtom(), data.getSize(), data.getBytes()));
    }

    /**
     * This method allows you to set a leaf atom's data.
     * @see "QuickTime::QTSetAtomData"
     * @param atom Specifies the atom within this container.
     * @param data Contains the data for the atom. This value will be endian flipped if required.
     * @throws quicktime.std.StdQTException
     */
    public void setAtomData(Atom atom, int data) throws StdQTException {
        int[] arrayOfInt = { data };
        StdQTException.checkError(QTSetAtomData(_ID(), atom.getAtom(), 4, arrayOfInt));
    }

    /**
     * This method allows you to set a leaf atom's data.
     * @see "QuickTime::QTSetAtomData"
     * @param atom Specifies the atom within this container.
     * @param data Contains the data for the atom. This value will be endian flipped if required.
     * @throws quicktime.std.StdQTException
     */
    public void setAtomData(Atom atom, short data) throws StdQTException {
        short[] arrayOfShort = { data };
        StdQTException.checkError(QTSetAtomData(_ID(), atom.getAtom(), 2, arrayOfShort));
    }

    /**
     * This method allows you to set a leaf atom's data.
     * @see "QuickTime::QTSetAtomData"
     * @param atom Specifies the atom within this container.
     * @param data Contains the data for the atom. This value will be endian flipped if required.
     * @throws quicktime.std.StdQTException
     */
    public void setAtomData(Atom atom, byte data) throws StdQTException {
        byte[] arrayOfByte = { data };
        StdQTException.checkError(QTSetAtomData(_ID(), atom.getAtom(), 1, arrayOfByte));
    }

    /**
     * This method returns a leaf atom's data.
     * @see "QuickTime::QTCopyAtomDataToHandle"
     * @param atom Specifies the atom within this container.
     * @return A QTHandle object containing the leaf atom's data.
     * @throws quicktime.QTException
     */
    public QTHandle copyAtomDataToHandle(Atom atom) throws QTException {
        QTHandle handle = new QTHandle();
        StdQTException.checkError(QTCopyAtomDataToHandle(_ID(), atom.getAtom(), QTObject.ID(handle)));
        return handle;
    }

    /**
     * This method returns a leaf atom's data to the buffer pointed to by the data object.
     * @see "QuickTime::QTCopyAtomDataToPtr"
     * @param atom Specifies the atom within this container.
     * @param sizeOrLessOK True indicates that it is OK to receive less data than specified by the size of the data Pointer.
     * @param data A QTPointer object containing the buffer for the leaf atom's data.
     * @return the actual number of bytes retrieved.
     * @throws quicktime.std.StdQTException
     */
    public int copyAtomDataToPtr(Atom atom, boolean sizeOrLessOK, QTPointer data) throws StdQTException {
        int[] actualSize = new int[1];
        byte b = (byte) (sizeOrLessOK ? 1 : 0);
        StdQTException.checkError(QTCopyAtomDataToPtr(_ID(), atom.getAtom(), b, data.getSize(), QTObject.ID(data), actualSize));
        return actualSize[0];
    }

    /**
     * Copies the atom data for the specifiec leaf atom.
     * @see "QuickTime::QTGetAtomDataPtr"
     * @param atom (offset) for AtomContainers this is an "atom" that acts as the offset
     * @throws quicktime.std.StdQTException
     */
    public byte[] copyAtomDataToArray(Atom atom) throws StdQTException {
        int[] size = { 0 };
        int[] ptr = { 0 };
        StdQTException.checkError(QTGetAtomDataPtr(_ID(), atom.getAtom(), size, ptr));
        byte[] dst = new byte[size[0]];
        int[] actualSize = { 0 };
        StdQTException.checkError(QTCopyAtomDataToPtr(_ID(), atom.getAtom(), (byte) 0, dst.length, dst, actualSize));
        return dst;
    }

    /**
     * This method returns the type of a given atom.
     * @see "QuickTime::QTGetAtomTypeAndID"
     * @param atom Specifies the atom within this container.
     * @return the type of a given atom
     * @throws quicktime.std.StdQTException
     */
    public int getAtomType(Atom atom) throws StdQTException {
        int[] type = new int[1];
        StdQTException.checkError(QTGetAtomTypeAndID(_ID(), atom.getAtom(), type, null));
        return type[0];
    }

    /**
     * This method returns the id of a given atom.
     * @see "QuickTime::QTGetAtomTypeAndID"
     * @param atom Specifies the atom within this container.
     * @return the id of a given atom.
     * @throws quicktime.std.StdQTException
     */
    public int getAtomID(Atom atom) throws StdQTException {
        int[] id = new int[1];
        StdQTException.checkError(QTGetAtomTypeAndID(_ID(), atom.getAtom(), null, id));
        return id[0];
    }

    /**
     * This method allows a complete copy of an atom and all of its children to be extracted
     * from its container and be placed in an new atom data structure.
     * @see "QuickTime::QTCopyAtom"
     * @param atom Specifies the atom within this container.
     * @return a new AtomContainer that contains the copy of the specifed atom..
     * @throws quicktime.std.StdQTException
     */
    public AtomContainer copyAtom(Atom atom) throws StdQTException {
        int[] targetContainer = new int[1];
        StdQTException.checkError(QTCopyAtom(_ID(), atom.getAtom(), targetContainer));
        return new AtomContainer(targetContainer[0], null, false);
    }

    @Override
    protected final void doLock() {
        QTLockContainer(_ID());
    }

    @Override
    protected final void doUnlock() {
        QTUnlockContainer(_ID());
    }

    /**
     * This method creates a new empty child atom for parentAtom.
     * @see "QuickTime::QTInsertChild"
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @return The new atom.
     * @throws quicktime.QTException
     */
    public Atom insertChild(Atom parentAtom, int atomType, int id, int index) throws QTException {
        int[] newAtom = new int[1];
        StdQTException.checkError(QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, 0, 0, newAtom));
        return newAtom[0] != 0 ? new Atom(newAtom[0]) : null;
    }

    /**
     * This method creates a new child atom for parentAtom.
     * @see "QuickTime::QTInsertChild"
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @param data A QTByteObject that contains the data of the specifed atom.
     * @return The new atom.
     * @throws quicktime.std.StdQTException
     */
    public Atom insertChild(Atom parentAtom, int atomType, int id, int index, QTByteObject data) throws StdQTException {
        int[] newAtom = new int[1];
        StdQTException.checkError(QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, data.getSize(), data.getBytes(), newAtom));
        return newAtom[0] != 0 ? new Atom(newAtom[0]) : null;
    }

    public Atom insertChild(Atom atom, int type, int id, int index, byte[] data) throws StdQTException {
        int[] newAtom = new int[1];
        StdQTException.checkError(QTInsertChild(_ID(), atom != null ? atom.getAtom() : 0, type, id, (short) index, data.length, data, newAtom));
        return newAtom[0] != 0 ? new Atom(newAtom[0]) : null;
    }

    /**
     * This method creates a new child atom for parentAtom.
     * @see "QuickTime::QTInsertChild"
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @param data A QTPointerRef object contains the data of the specifed atom.
     * @return The new atom.
     * @throws quicktime.std.StdQTException
     */
    public Atom insertChild(Atom parentAtom, int atomType, int id, int index, QTPointerRef data) throws StdQTException {
        int[] newAtom = new int[1];
        StdQTException.checkError(QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, data.getSize(), QTObject.ID(data), newAtom));
        return newAtom[0] != 0 ? new Atom(newAtom[0]) : null;
    }

    /**
     * This method creates a new child atom for parentAtom.
     * @see "QuickTime::QTInsertChild"
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @param data A QTHandleRef object contains the data of the specifed atom.
     * @return The new atom.
     * @throws quicktime.std.StdQTException
     */
    public Atom insertChild(Atom parentAtom, int atomType, int id, int index, QTHandleRef data) throws StdQTException {
        int[] newAtom = new int[1];
        QTPointerRef qtPointerRef = data.toQTPointer();
        int i = QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, data.getSize(), QTObject.ID(qtPointerRef), newAtom);
        if (!data.isLocked()) {
            data.unlock();
        }
        StdQTException.checkError(i);
        return newAtom[0] != 0 ? new Atom(newAtom[0]) : null;
    }

    /**
     * This method creates a new child atom for parentAtom.
     * @see "QuickTime::QTInsertChild"
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @param intValue Specifies the integer data of the specified atom. This value will be endian flipped if required.
     * @return The new atom.
     * @throws quicktime.std.StdQTException
     */
    public Atom insertChild(Atom parentAtom, int atomType, int id, int index, int intValue) throws StdQTException {
        int[] newAtom = new int[1];
        int[] data = { intValue };
        StdQTException.checkError(QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, 4, data, newAtom));
        return newAtom[0] != 0 ? new Atom(newAtom[0]) : null;
    }

    /**
     * This method creates a new child atom for parentAtom.
     * @see "QuickTime::QTInsertChild"
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @param shortValue Specifies the integer data of the specified atom. This value will be endian flipped if required.
     * @return The new atom.
     * @throws quicktime.std.StdQTException
     */
    public Atom insertChild(Atom parentAtom, int atomType, int id, int index, short shortValue) throws StdQTException {
        int[] newAtom = new int[1];
        short[] data = { shortValue };
        StdQTException.checkError(QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, 2, data, newAtom));
        return newAtom[0] != 0 ? new Atom(newAtom[0]) : null;
    }

    /**
     * This method creates a new child atom for parentAtom.
     * @see "QuickTime::QTInsertChild"
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param atomType Specifies the type of the new atom which is to be inserted.
     * @param id Specifies the ID of the new atom which is to be inserted.
     * @param index Specifies the index of the new atom for its type within the parent atom's child list.
     * @return The new atom.
     * @throws quicktime.std.StdQTException
     */
    public Atom insertChild(Atom parentAtom, int atomType, int id, int index, byte value) throws StdQTException {
        int[] newAtom = new int[1];
        byte[] data = { value };
        StdQTException.checkError(QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, 1, data, newAtom));
        return newAtom[0] != 0 ? new Atom(newAtom[0]) : null;
    }

    /**
     * This method inserts all of the atoms in childrenContainer as child atoms of the atom specified by the parentAtom.
     * @see "QuickTime::QTInsertChildren"
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param childrenContainer Specifies the atom data structure which contains all of the children to be inserted.
     * @throws StdQTException invalidAtomContainerErr if childrenContainer is null
     * @throws quicktime.std.StdQTException
     */
    public void insertChildren(Atom parentAtom, AtomContainer childrenContainer) throws StdQTException {
        if (QTObject.ID(childrenContainer) == 0) {
            throw new StdQTException(63429);
        }
        StdQTException.checkError(QTInsertChildren(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, QTObject.ID(childrenContainer)));
    }

    /**
     * This method removes an atom and all of its children from an atom data structure.
     * @see "QuickTime::QTRemoveAtom"
     * @param atom Specifies the atom within this atom container which is to be removed.
     * @throws quicktime.std.StdQTException
     */
    public void removeAtom(Atom atom) throws StdQTException {
        StdQTException.checkError(QTRemoveAtom(_ID(), atom.getAtom()));
    }

    /**
     * This method removes all of the children of an atom.
     * @see "QuickTime::QTRemoveChildren"
     * @param atom Specifies the atom within this atom container which is to be removed.
     * @throws quicktime.std.StdQTException
     */
    public void removeChildren(Atom atom) throws StdQTException {
        StdQTException.checkError(QTRemoveChildren(_ID(), atom.getAtom()));
    }

    /**
     * This method replaces an atom and all of its children with a different atom and all of its children.
     * @see "QuickTime::QTReplaceAtom"
     * @param targetAtom Specifies the atom within this atom container which is to be replaced.
     * @param replacementContainer Specifies the atom data structure which contains replacementAtom.
     * @param replacementAtom Specifies the atom within replacementContainer which replaces targetAtom.
     * @throws StdQTException invalidAtomContainerErr if targetAtom, replacementContainer or replacementAtom are null
     * @throws quicktime.std.StdQTException
     */
    public void replaceAtom(Atom targetAtom, AtomContainer replacementContainer, Atom replacementAtom) throws StdQTException {
        if (QTObject.ID(replacementContainer) == 0) {
            throw new StdQTException(63429);
        }
        StdQTException.checkError(QTReplaceAtom(_ID(), targetAtom.getAtom(), QTObject.ID(replacementContainer), replacementAtom.getAtom()));
    }

    /**
     * This method swaps two atoms within an atomData structure.
     * @see "QuickTime::QTSwapAtoms"
     * @param atom1 Specifies an atom within this container which is to be swapped with atom2.
     * @param atom2 Specifies the atom within this container which is to be swapped with atom1.
     * @throws quicktime.std.StdQTException
     */
    public void swapAtoms(Atom atom1, Atom atom2) throws StdQTException {
        StdQTException.checkError(QTSwapAtoms(_ID(), atom1.getAtom(), atom2.getAtom()));
    }

    /**
     * This method allows you to change the ID of an atom.
     * @see "QuickTime::QTSetAtomID"
     * @param atom Specifies an atom within this container which is to be changed.
     * @param newID Specifies the new ID for the atom.
     * @throws quicktime.std.StdQTException
     */
    public void setAtomID(Atom atom, int newID) throws StdQTException {
        StdQTException.checkError(QTSetAtomID(_ID(), atom.getAtom(), newID));
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::ITextAddString"
     * @param parentAtom Specifies the parent atom within this container. Null implies the parent atom is the container.
     * @param theRegionCode Specifies the region code. Use 0 for verUS
     * @param theString a String must be specified.
     * @throws quicktime.std.StdQTException
     */
    public void iTextAddString(Atom parentAtom, int theRegionCode, String theString) throws StdQTException {
        StdQTException.checkError(ITextAddString(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, (short) theRegionCode, QTUtils.String2PString(theString, 255)));
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::ITextRemoveString"
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container
     * @param theRegionCode Specifies the region code. Use 0 for verUS
     * @throws quicktime.std.StdQTException
     */
    public void iTextRemoveString(Atom parentAtom, int theRegionCode, int flags) throws StdQTException {
        StdQTException.checkError(ITextRemoveString(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, (short) theRegionCode, flags));
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::ITextGetString"
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container
     * @param theRegionCode Specifies the region code. Use 0 for verUS
     * @throws quicktime.std.StdQTException
     */
    public IStringInfo iTextGetString(Atom parentAtom, int theRegionCode) throws StdQTException {
        short[] foundTag = new short[1];
        byte[] text = new byte[256];
        StdQTException.checkError(ITextGetString(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, (short) theRegionCode, foundTag, text));
        return new IStringInfo(QTUtils.PString2String(text, 0), foundTag[0]);
    }

    /**
     * This method returns the parent atom of the specified child atom
     * @see "QuickTime::QTGetAtomParent"
     * @param childAtom Specifies the child atom within this container.
     * @return The parent atom.
     * @throws quicktime.std.StdQTException
     */
    public Atom getParent(Atom childAtom) throws StdQTException {
        int i = QTGetAtomParent(_ID(), childAtom.getAtom());
        if (i < 0)
            throw new StdQTException(i);
        return new Atom(i);
    }

    private static int GraphicsExportGetSettingsAsAtomContainer(int ge, int[] container) {
        return GraphicsExportManager.GraphicsExportGetSettingsAsAtomContainer(ge, container);
    }

    private static int GraphicsExportCanUseCompressor(int ge, byte[] use, int[] codecSettings) {
        return GraphicsExportManager.GraphicsExportCanUseCompressor(ge, use, codecSettings);
    }

    private static int GraphicsExportGetMIMETypeList(int ge, int[] container) {
        return GraphicsExportManager.GraphicsExportGetMIMETypeList(ge, container);
    }

    private static short QTVRGetVRWorld(int qtvr, int[] vrWorld) {
        return VRManager.QTVRGetVRWorld(qtvr, vrWorld);
    }

    private static short QTVRGetNodeInfo(int qtvr, int nodeID, int[] nodeInfo) {
        return VRManager.QTVRGetNodeInfo(qtvr, nodeID, nodeInfo);
    }

    private static int GraphicsImportGetMIMETypeList(int gi, int[] container) {
        return GraphicsImportManager.GraphicsImportGetMIMETypeList(gi, container);
    }

    private static int GraphicsImportGetExportImageTypeList(int gi, int[] container) {
        return GraphicsImportManager.GraphicsImportGetExportImageTypeList(gi, container);
    }

    private static int GraphicsImportGetExportSettingsAsAtomContainer(int gi, int[] container) {
        return GraphicsImportManager.GraphicsImportGetExportSettingsAsAtomContainer(gi, container);
    }

    private static short GetMediaInputMap(int media, int[] inputMap) {
        return MovieToolbox.GetMediaInputMap(media, inputMap);
    }

    private static short GetMediaPropertyAtom(int media, int[] propertyAtom) {
        return MovieToolbox.GetMediaPropertyAtom(media, propertyAtom);
    }

    private static int MovieExportGetSettingsAsAtomContainer(int ci, int[] settings) {
        return ComponentManager.MovieExportGetSettingsAsAtomContainer(ci, settings);
    }

    private static int MovieImportGetMIMETypeList(int ci, int[] list) {
        return ComponentManager.MovieImportGetMIMETypeList(ci, list);
    }

    private static int MovieImportGetSettingsAsAtomContainer(int ci, int[] settings) {
        return ComponentManager.MovieImportGetSettingsAsAtomContainer(ci, settings);
    }

    private static int Media3DGetNamedObjectList(int mh, int[] list) {
        return MediaHandlerManager.Media3DGetNamedObjectList(mh, list);
    }

    private static int Media3DGetRendererList(int mh, int[] list) {
        return MediaHandlerManager.Media3DGetRendererList(mh, list);
    }

    private static int SCGetSettingsAsAtomContainer(int ci, int[] settings) {
        return ImageCompressionManager.SCGetSettingsAsAtomContainer(ci, settings);
    }

    private static short QTNewAtomContainer(int[] container) {
        return AtomManager.QTNewAtomContainer(container);
    }

    private static short QTGetAtomDataPtr(int container, int atom, int[] size, int[] ptr) {
        return AtomManager.QTGetAtomDataPtr(container, atom, size, ptr);
    }

    private static int QTGetNextChildType(int container, int parent, int currentType) {
        return AtomManager.QTGetNextChildType(container, parent, currentType);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static short QTCountChildrenOfType(int container, int parent, int type) {
        return AtomManager.QTCountChildrenOfType(container, parent, type);
    }

    private static int QTFindChildByIndex(int container, int parent, int type, short index, int[] id) {
        return AtomManager.QTFindChildByIndex(container, parent, type, index, id);
    }

    private static int QTFindChildByID(int container, int parent, int type, int id, short[] index) {
        return AtomManager.QTFindChildByID(container, parent, type, id, index);
    }

    private static short QTNextChildAnyType(int container, int parent, int currentChild, int[] nextChild) {
        return AtomManager.QTNextChildAnyType(container, parent, currentChild, nextChild);
    }

    private static short QTSetAtomData(int container, int atom, int size, byte[] data) {
        return AtomManager.QTSetAtomData(container, atom, size, data);
    }

    private static short QTSetAtomData(int container, int atom, int size, int dataPtr) {
        return AtomManager.QTSetAtomData(container, atom, size, dataPtr);
    }

    private static short QTSetAtomData(int container, int atom, int size, int[] data) {
        return AtomManager.QTSetAtomData(container, atom, size, data);
    }

    private static short QTSetAtomData(int container, int atom, int size, short[] data) {
        return AtomManager.QTSetAtomData(container, atom, size, data);
    }

    private static short QTCopyAtomDataToHandle(int container, int atom, int handle) {
        return AtomManager.QTCopyAtomDataToHandle(container, atom, handle);
    }

    private static short QTCopyAtomDataToPtr(int container, int atom, byte sizeOrLessOK, int size, int dstPtr, int[] actualSize) {
        return AtomManager.QTCopyAtomDataToPtr(container, atom, sizeOrLessOK, size, dstPtr, actualSize);
    }

    private static short QTCopyAtomDataToPtr(int container, int atom, byte sizeOrLessOK, int size, byte[] dst, int[] actualSize) {
        return AtomManager.QTCopyAtomDataToPtr(container, atom, sizeOrLessOK, size, dst, actualSize);
    }

    private static short QTGetAtomTypeAndID(int container, int atom, int[] type, int[] id) {
        return AtomManager.QTGetAtomTypeAndID(container, atom, type, id);
    }

    private static short QTCopyAtom(int container, int atom, int[] targetContainer) {
        return AtomManager.QTCopyAtom(container, atom, targetContainer);
    }

    private static short QTLockContainer(int container) {
        return AtomManager.QTLockContainer(container);
    }

    private static short QTUnlockContainer(int container) {
        return AtomManager.QTUnlockContainer(container);
    }

    private static short QTInsertChild(int container, int parent, int type, int id, short index, int size, byte[] data, int[] newAtom) {
        return AtomManager.QTInsertChild(container, parent, type, id, index, size, data, newAtom);
    }

    private static short QTInsertChild(int container, int parent, int type, int id, short index, int size, int dataPtr, int[] newAtom) {
        return AtomManager.QTInsertChild(container, parent, type, id, index, size, dataPtr, newAtom);
    }

    private static short QTInsertChild(int container, int parent, int type, int id, short index, int size, int[] data, int[] newAtom) {
        return AtomManager.QTInsertChild(container, parent, type, id, index, size, data, newAtom);
    }

    private static short QTInsertChild(int container, int parent, int type, int id, short index, int size, short[] data, int[] newAtom) {
        return AtomManager.QTInsertChild(container, parent, type, id, index, size, data, newAtom);
    }

    private static short QTInsertChildren(int container, int parent, int childrenContainer) {
        return AtomManager.QTInsertChildren(container, parent, childrenContainer);
    }

    private static short QTRemoveAtom(int container, int atom) {
        return AtomManager.QTRemoveAtom(container, atom);
    }

    private static short QTRemoveChildren(int container, int atom) {
        return AtomManager.QTRemoveChildren(container, atom);
    }

    private static short QTReplaceAtom(int target, int targetAtom, int replacement, int replacementAtom) {
        return AtomManager.QTReplaceAtom(target, targetAtom, replacement, replacementAtom);
    }

    private static short QTSwapAtoms(int container, int atom1, int atom2) {
        return AtomManager.QTSwapAtoms(container, atom1, atom2);
    }

    private static short QTSetAtomID(int container, int atom, int newID) {
        return AtomManager.QTSetAtomID(container, atom, newID);
    }

    private static short ITextAddString(int itext, int str, short regionTag, byte[] text) {
        return QuickDrawManager.ITextAddString(itext, str, regionTag, text);
    }

    private static short ITextRemoveString(int itext, int str, short regionTag, int flags) {
        return QuickDrawManager.ITextRemoveString(itext, str, regionTag, flags);
    }

    private static short ITextGetString(int itext, int str, short regionTag, short[] foundTag, byte[] text) {
        return QuickDrawManager.ITextGetString(itext, str, regionTag, foundTag, text);
    }

    private static int QTGetAtomParent(int container, int child) {
        return AtomManager.QTGetAtomParent(container, child);
    }
}
