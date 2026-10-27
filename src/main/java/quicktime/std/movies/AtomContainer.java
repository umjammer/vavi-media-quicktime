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
    public static AtomContainer fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(GraphicsExportGetSettingsAsAtomContainer(QTObject.ID(paramGraphicsExporter), arrayOfInt));
        return new AtomContainer(arrayOfInt[0], null, false);
    }

    /**
     * The base graphics exporter makes this call of the format-specific graphics exporter to ask whether the
     * current export operation should be done by using an image compressor.
     * @see "QuickTime::GraphicsExportCanUseCompressor"
     * //@return true if allowed to use a compressor.
     * @throws quicktime.QTException
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
     * @see "QuickTime::GraphicsExportGetMIMETypeList"
     * @return a QT atom container that contains a list of MIME types supported by the graphics import component.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.GraphicsExporter#getMIMETypeList()
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
     * @see "QuickTime::QTVRGetVRWorld"
     * @return the VRWorld AtomContainer
     * @throws quicktime.vr.QTVRException
     * @see quicktime.vr.QTVRInstance#getVRWorld()
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
     * @see "QuickTime::QTVRGetNodeInfo"
     * @param nodeID Set this parameter to kQTVRCurrentNode to receive information
     *        about the current node
     * @return an atom container that contains information about the
     *        specified node.
     * @throws quicktime.vr.QTVRException
     * @see quicktime.vr.QTVRInstance#getNodeInfo(int)
     */
    public static AtomContainer fromQTVRInstanceNode(QTVRInstance paramQTVRInstance, int nodeID) throws QTVRException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = QTVRGetNodeInfo(QTObject.ID(paramQTVRInstance), nodeID, arrayOfInt);
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
    public static AtomContainer fromGraphicsImporterMIME(GraphicsImporter paramGraphicsImporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(GraphicsImportGetMIMETypeList(QTObject.ID(paramGraphicsImporter), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Returns information about available export formats.
     * @see "QuickTime::GraphicsImportGetExportImageTypeList"
     * @return a QT atom container that contains a list of available export formats. supported by the graphics import component.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.GraphicsImporter#getExportImageTypeList()
     */
    public static AtomContainer fromGraphicsImporterExportImage(GraphicsImporter paramGraphicsImporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(GraphicsImportGetExportImageTypeList(QTObject.ID(paramGraphicsImporter), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Returns the Export settings as an Atom Container.
     * @see "QuickTime::GraphicsImportGetExportSettingsAsAtomContainer"
     * @return a QT atom container that contains Export settings.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.GraphicsImporter#getExportSettingsAsAtomContainer()
     */
    public static AtomContainer fromGraphicsImporterExportSettings(GraphicsImporter paramGraphicsImporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(GraphicsImportGetExportSettingsAsAtomContainer(QTObject.ID(paramGraphicsImporter), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * This method returns you the input map of the media.
     * @see "QuickTime::GetMediaInputMap()"
     * @return an AtomContainer object for media's input map.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.media.Media#getInputMap()
     */
    public static AtomContainer fromMediaInput(Media paramMedia) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetMediaInputMap(QTObject.ID(paramMedia), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Retrieves the properties from a media handler.
     * @see "QuickTime::GetMediaPropertyAtom()"
     * @return Contains the property atoms for the track associated with the media handler.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.media.Media#getPropertyAtom()
     */
    public static AtomContainer fromMediaProperty(Media paramMedia) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetMediaPropertyAtom(QTObject.ID(paramMedia), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Retrieves the current settings from the movie export component.
     * @see "QuickTime::MovieExportGetSettingsAsAtomContainer"
     * @return an AtomContainer with the current settings.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.qtcomponents.MovieExporter#getExportSettingsFromAtomContainer()
     */
    public static AtomContainer fromMovieExporter(quicktime.std.qtcomponents.MovieExporter paramMovieExporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(MovieExportGetSettingsAsAtomContainer(QTObject.ID(paramMovieExporter), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Returns a list of MIME types supported by the movie import component.
     * @see "QuickTime::MovieImportGetMIMETypeList"
     * @return an AtomContainer that contains a list of MIME types
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.qtcomponents.MovieImporter#getMIMETypeList()
     */
    public static AtomContainer fromMovieImporterMIME(MovieImporter paramMovieImporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(MovieImportGetMIMETypeList(QTObject.ID(paramMovieImporter), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Retrieves the current settings from the movie import component.
     * @see "QuickTime::MovieImportGetSettingsAsAtomContainer"
     * @return an AtomContainer with the current settings.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.qtcomponents.MovieImporter#getImportSettingsFromAtomContainer()
     */
    public static AtomContainer fromMovieImporterSettings(MovieImporter paramMovieImporter) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(MovieImportGetSettingsAsAtomContainer(QTObject.ID(paramMovieImporter), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::Media3DGetNamedObjectList()"
     * @return an atom container
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.media.ThreeDMediaHandler#getNamedObjectList()
     */
    public static AtomContainer fromThreeDMediaHandlerObject(ThreeDMediaHandler paramThreeDMediaHandler) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(Media3DGetNamedObjectList(QTObject.ID(paramThreeDMediaHandler), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::Media3DGetRendererList()"
     * @return an atom container
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.media.ThreeDMediaHandler#getRendererList()
     */
    public static AtomContainer fromThreeDMediaHandlerRenderer(ThreeDMediaHandler paramThreeDMediaHandler) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(Media3DGetRendererList(QTObject.ID(paramThreeDMediaHandler), arrayOfInt));
        return arrayOfInt[0] != 0 ? new AtomContainer(arrayOfInt[0], null, false) : null;
    }

    /**
     * Retrieves the current configuration from the dialog
     * @see "QuickTime::SCGetSettingsAsAtomContainer()"
     * @return the settings
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.qtcomponents.CompressionDialog#getSettings()
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
     * @see "QuickTime::QTNewAtomContainer"
     */
    public AtomContainer() throws QTException {
        super(allocate(), null, false);
    }

    private static int allocate() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = QTNewAtomContainer(arrayOfInt);
        if (i != 0)
            throw new StdQTException(i);
        return arrayOfInt[0];
    }

    void _setNR(int paramInt) {
        setNR(paramInt);
    }

    /**
     * Retrieves a pointer to the atom data for the specifiec leaf atom.
     * @see "QuickTime::QTGetAtomDataPtr"
     * @param atom (offset) for AtomContainers this is an "atom" that acts as the offset
     * @return the actual atom data attached to the specified atom
     * @throws quicktime.std.StdQTException
     */
    public AtomData getAtomData(Atom atom) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        StdQTException.checkError(QTGetAtomDataPtr(_ID(), atom.getAtom(), arrayOfInt1, arrayOfInt2));
        return new AtomData(arrayOfInt2[0], arrayOfInt1[0], this);
    }

    /**
     * Retrieves a pointer to the atom data for the specifiec leaf atom.
     * @see "QuickTime::QTGetAtomDataPtr"
     * @param atom (offset) for AtomContainers this is an "atom" that acts as the offset
     * @return the size of the atom data attached to the specified atom
     * @throws quicktime.std.StdQTException
     */
    public int getAtomDataSize(Atom atom) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        StdQTException.checkError(QTGetAtomDataPtr(_ID(), atom.getAtom(), arrayOfInt1, arrayOfInt2));
        return arrayOfInt1[0];
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
        int[] arrayOfInt = new int[1];
        QTFindChildByIndex(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, (short) index, arrayOfInt);
        StdQTException.checkError(GetMoviesError());
        return arrayOfInt[0];
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
        short[] arrayOfShort = new short[1];
        QTFindChildByID(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, arrayOfShort);
        StdQTException.checkError(GetMoviesError());
        return arrayOfShort[0];
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
        int[] arrayOfInt = new int[1];
        int i = parentAtom != null ? parentAtom.getAtom() : 0;
        StdQTException.checkError(QTNextChildAnyType(_ID(), i, currentChild != null ? currentChild.getAtom() : 0, arrayOfInt));
        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
    }

    /**
     * This method allows you to set a leaf atom's data.
     * @see "QuickTime::QTSetAtomData"
     * @param atom Specifies the atom within this container. Use Atom.kParentIsContainer if atom is container.
     * @param data Contains the data for the atom.
     * @throws quicktime.std.StdQTException
     */
    public void setAtomData(Atom atom, QTHandleRef data) throws StdQTException {
        QTPointerRef localQTPointerRef = data.toQTPointer();
        int i = QTSetAtomData(_ID(), atom.getAtom(), data.getSize(), QTObject.ID(localQTPointerRef));
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

    public void setAtomData(Atom paramAtom, byte[] paramArrayOfByte) throws StdQTException {
        StdQTException.checkError(QTSetAtomData(_ID(), paramAtom.getAtom(), paramArrayOfByte.length, paramArrayOfByte));
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
        QTHandle localQTHandle = new QTHandle();
        StdQTException.checkError(QTCopyAtomDataToHandle(_ID(), atom.getAtom(), QTObject.ID(localQTHandle)));
        return localQTHandle;
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
        int[] arrayOfInt = new int[1];
        byte b = (byte) (sizeOrLessOK ? 1 : 0);
        StdQTException.checkError(QTCopyAtomDataToPtr(_ID(), atom.getAtom(), b, data.getSize(), QTObject.ID(data), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Copies the atom data for the specifiec leaf atom.
     * @see "QuickTime::QTGetAtomDataPtr"
     * @param atom (offset) for AtomContainers this is an "atom" that acts as the offset
     * @throws quicktime.std.StdQTException
     */
    public byte[] copyAtomDataToArray(Atom atom) throws StdQTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        StdQTException.checkError(QTGetAtomDataPtr(_ID(), atom.getAtom(), arrayOfInt1, arrayOfInt2));
        byte[] arrayOfByte = new byte[arrayOfInt1[0]];
        int[] arrayOfInt3 = { 0 };
        StdQTException.checkError(QTCopyAtomDataToPtr(_ID(), atom.getAtom(), (byte) 0, arrayOfByte.length, arrayOfByte, arrayOfInt3));
        return arrayOfByte;
    }

    /**
     * This method returns the type of a given atom.
     * @see "QuickTime::QTGetAtomTypeAndID"
     * @param atom Specifies the atom within this container.
     * @return the type of a given atom
     * @throws quicktime.std.StdQTException
     */
    public int getAtomType(Atom atom) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTGetAtomTypeAndID(_ID(), atom.getAtom(), arrayOfInt, null));
        return arrayOfInt[0];
    }

    /**
     * This method returns the id of a given atom.
     * @see "QuickTime::QTGetAtomTypeAndID"
     * @param atom Specifies the atom within this container.
     * @return the id of a given atom.
     * @throws quicktime.std.StdQTException
     */
    public int getAtomID(Atom atom) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTGetAtomTypeAndID(_ID(), atom.getAtom(), null, arrayOfInt));
        return arrayOfInt[0];
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
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTCopyAtom(_ID(), atom.getAtom(), arrayOfInt));
        return new AtomContainer(arrayOfInt[0], null, false);
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
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, 0, 0, arrayOfInt));
        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
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
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, data.getSize(), data.getBytes(), arrayOfInt));
        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
    }

    public Atom insertChild(Atom paramAtom, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTInsertChild(_ID(), paramAtom != null ? paramAtom.getAtom() : 0, paramInt1, paramInt2, (short) paramInt3, paramArrayOfByte.length, paramArrayOfByte, arrayOfInt));
        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
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
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, data.getSize(), QTObject.ID(data), arrayOfInt));
        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
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
        int[] arrayOfInt = new int[1];
        QTPointerRef localQTPointerRef = data.toQTPointer();
        int i = QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, data.getSize(), QTObject.ID(localQTPointerRef), arrayOfInt);
        if (!data.isLocked()) {
            data.unlock();
        }
        StdQTException.checkError(i);
        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
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
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = { intValue };
        StdQTException.checkError(QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, 4, arrayOfInt2, arrayOfInt1));
        return arrayOfInt1[0] != 0 ? new Atom(arrayOfInt1[0]) : null;
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
        int[] arrayOfInt = new int[1];
        short[] arrayOfShort = { shortValue };
        StdQTException.checkError(QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, 2, arrayOfShort, arrayOfInt));
        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
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
    public Atom insertChild(Atom parentAtom, int atomType, int id, int index, byte paramByte) throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = { paramByte };
        StdQTException.checkError(QTInsertChild(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, atomType, id, (short) index, 1, arrayOfByte, arrayOfInt));
        return arrayOfInt[0] != 0 ? new Atom(arrayOfInt[0]) : null;
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
    public void iTextRemoveString(Atom parentAtom, int theRegionCode, int paramInt2) throws StdQTException {
        StdQTException.checkError(ITextRemoveString(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, (short) theRegionCode, paramInt2));
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::ITextGetString"
     * @param parentAtom Specifies the parent atom within this container. Null implies parentAtom is the container
     * @param theRegionCode Specifies the region code. Use 0 for verUS
     * @throws quicktime.std.StdQTException
     */
    public IStringInfo iTextGetString(Atom parentAtom, int theRegionCode) throws StdQTException {
        short[] arrayOfShort = new short[1];
        byte[] arrayOfByte = new byte[256];
        StdQTException.checkError(ITextGetString(_ID(), parentAtom != null ? parentAtom.getAtom() : 0, (short) theRegionCode, arrayOfShort, arrayOfByte));
        return new IStringInfo(QTUtils.PString2String(arrayOfByte, 0), arrayOfShort[0]);
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

    private static int GraphicsExportGetSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt) {
        return GraphicsExportManager.GraphicsExportGetSettingsAsAtomContainer(paramInt, paramArrayOfInt);
    }

    private static int GraphicsExportCanUseCompressor(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return GraphicsExportManager.GraphicsExportCanUseCompressor(paramInt, paramArrayOfByte, paramArrayOfInt);
    }

    private static int GraphicsExportGetMIMETypeList(int paramInt, int[] paramArrayOfInt) {
        return GraphicsExportManager.GraphicsExportGetMIMETypeList(paramInt, paramArrayOfInt);
    }

    private static short QTVRGetVRWorld(int paramInt, int[] paramArrayOfInt) {
        return VRManager.QTVRGetVRWorld(paramInt, paramArrayOfInt);
    }

    private static short QTVRGetNodeInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return VRManager.QTVRGetNodeInfo(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int GraphicsImportGetMIMETypeList(int paramInt, int[] paramArrayOfInt) {
        return GraphicsImportManager.GraphicsImportGetMIMETypeList(paramInt, paramArrayOfInt);
    }

    private static int GraphicsImportGetExportImageTypeList(int paramInt, int[] paramArrayOfInt) {
        return GraphicsImportManager.GraphicsImportGetExportImageTypeList(paramInt, paramArrayOfInt);
    }

    private static int GraphicsImportGetExportSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt) {
        return GraphicsImportManager.GraphicsImportGetExportSettingsAsAtomContainer(paramInt, paramArrayOfInt);
    }

    private static short GetMediaInputMap(int paramInt, int[] paramArrayOfInt) {
        return MovieToolbox.GetMediaInputMap(paramInt, paramArrayOfInt);
    }

    private static short GetMediaPropertyAtom(int paramInt, int[] paramArrayOfInt) {
        return MovieToolbox.GetMediaPropertyAtom(paramInt, paramArrayOfInt);
    }

    private static int MovieExportGetSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt) {
        return ComponentManager.MovieExportGetSettingsAsAtomContainer(paramInt, paramArrayOfInt);
    }

    private static int MovieImportGetMIMETypeList(int paramInt, int[] paramArrayOfInt) {
        return ComponentManager.MovieImportGetMIMETypeList(paramInt, paramArrayOfInt);
    }

    private static int MovieImportGetSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt) {
        return ComponentManager.MovieImportGetSettingsAsAtomContainer(paramInt, paramArrayOfInt);
    }

    private static int Media3DGetNamedObjectList(int paramInt, int[] paramArrayOfInt) {
        return MediaHandlerManager.Media3DGetNamedObjectList(paramInt, paramArrayOfInt);
    }

    private static int Media3DGetRendererList(int paramInt, int[] paramArrayOfInt) {
        return MediaHandlerManager.Media3DGetRendererList(paramInt, paramArrayOfInt);
    }

    private static int SCGetSettingsAsAtomContainer(int paramInt, int[] paramArrayOfInt) {
        return ImageCompressionManager.SCGetSettingsAsAtomContainer(paramInt, paramArrayOfInt);
    }

    private static short QTNewAtomContainer(int[] paramArrayOfInt) {
        return AtomManager.QTNewAtomContainer(paramArrayOfInt);
    }

    private static short QTGetAtomDataPtr(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return AtomManager.QTGetAtomDataPtr(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int QTGetNextChildType(int paramInt1, int paramInt2, int paramInt3) {
        return AtomManager.QTGetNextChildType(paramInt1, paramInt2, paramInt3);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static short QTCountChildrenOfType(int paramInt1, int paramInt2, int paramInt3) {
        return AtomManager.QTCountChildrenOfType(paramInt1, paramInt2, paramInt3);
    }

    private static int QTFindChildByIndex(int paramInt1, int paramInt2, int paramInt3, short paramShort, int[] paramArrayOfInt) {
        return AtomManager.QTFindChildByIndex(paramInt1, paramInt2, paramInt3, paramShort, paramArrayOfInt);
    }

    private static int QTFindChildByID(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[] paramArrayOfShort) {
        return AtomManager.QTFindChildByID(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfShort);
    }

    private static short QTNextChildAnyType(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return AtomManager.QTNextChildAnyType(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static short QTSetAtomData(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        return AtomManager.QTSetAtomData(paramInt1, paramInt2, paramInt3, paramArrayOfByte);
    }

    private static short QTSetAtomData(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return AtomManager.QTSetAtomData(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTSetAtomData(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return AtomManager.QTSetAtomData(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static short QTSetAtomData(int paramInt1, int paramInt2, int paramInt3, short[] paramArrayOfShort) {
        return AtomManager.QTSetAtomData(paramInt1, paramInt2, paramInt3, paramArrayOfShort);
    }

    private static short QTCopyAtomDataToHandle(int paramInt1, int paramInt2, int paramInt3) {
        return AtomManager.QTCopyAtomDataToHandle(paramInt1, paramInt2, paramInt3);
    }

    private static short QTCopyAtomDataToPtr(int paramInt1, int paramInt2, byte paramByte, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        return AtomManager.QTCopyAtomDataToPtr(paramInt1, paramInt2, paramByte, paramInt3, paramInt4, paramArrayOfInt);
    }

    private static short QTCopyAtomDataToPtr(int paramInt1, int paramInt2, byte paramByte, int paramInt3, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return AtomManager.QTCopyAtomDataToPtr(paramInt1, paramInt2, paramByte, paramInt3, paramArrayOfByte, paramArrayOfInt);
    }

    private static short QTGetAtomTypeAndID(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return AtomManager.QTGetAtomTypeAndID(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short QTCopyAtom(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return AtomManager.QTCopyAtom(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short QTLockContainer(int paramInt) {
        return AtomManager.QTLockContainer(paramInt);
    }

    private static short QTUnlockContainer(int paramInt) {
        return AtomManager.QTUnlockContainer(paramInt);
    }

    private static short QTInsertChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort, int paramInt5, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return AtomManager.QTInsertChild(paramInt1, paramInt2, paramInt3, paramInt4, paramShort, paramInt5, paramArrayOfByte, paramArrayOfInt);
    }

    private static short QTInsertChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort, int paramInt5, int paramInt6, int[] paramArrayOfInt) {
        return AtomManager.QTInsertChild(paramInt1, paramInt2, paramInt3, paramInt4, paramShort, paramInt5, paramInt6, paramArrayOfInt);
    }

    private static short QTInsertChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort, int paramInt5, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return AtomManager.QTInsertChild(paramInt1, paramInt2, paramInt3, paramInt4, paramShort, paramInt5, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short QTInsertChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short paramShort, int paramInt5, short[] paramArrayOfShort, int[] paramArrayOfInt) {
        return AtomManager.QTInsertChild(paramInt1, paramInt2, paramInt3, paramInt4, paramShort, paramInt5, paramArrayOfShort, paramArrayOfInt);
    }

    private static short QTInsertChildren(int paramInt1, int paramInt2, int paramInt3) {
        return AtomManager.QTInsertChildren(paramInt1, paramInt2, paramInt3);
    }

    private static short QTRemoveAtom(int paramInt1, int paramInt2) {
        return AtomManager.QTRemoveAtom(paramInt1, paramInt2);
    }

    private static short QTRemoveChildren(int paramInt1, int paramInt2) {
        return AtomManager.QTRemoveChildren(paramInt1, paramInt2);
    }

    private static short QTReplaceAtom(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return AtomManager.QTReplaceAtom(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTSwapAtoms(int paramInt1, int paramInt2, int paramInt3) {
        return AtomManager.QTSwapAtoms(paramInt1, paramInt2, paramInt3);
    }

    private static short QTSetAtomID(int paramInt1, int paramInt2, int paramInt3) {
        return AtomManager.QTSetAtomID(paramInt1, paramInt2, paramInt3);
    }

    private static short ITextAddString(int paramInt1, int paramInt2, short paramShort, byte[] paramArrayOfByte) {
        return QuickDrawManager.ITextAddString(paramInt1, paramInt2, paramShort, paramArrayOfByte);
    }

    private static short ITextRemoveString(int paramInt1, int paramInt2, short paramShort, int paramInt3) {
        return QuickDrawManager.ITextRemoveString(paramInt1, paramInt2, paramShort, paramInt3);
    }

    private static short ITextGetString(int paramInt1, int paramInt2, short paramShort, short[] paramArrayOfShort, byte[] paramArrayOfByte) {
        return QuickDrawManager.ITextGetString(paramInt1, paramInt2, paramShort, paramArrayOfShort, paramArrayOfByte);
    }

    private static int QTGetAtomParent(int paramInt1, int paramInt2) {
        return AtomManager.QTGetAtomParent(paramInt1, paramInt2);
    }
}
