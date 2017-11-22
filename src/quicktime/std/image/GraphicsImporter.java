
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.GDevice;
import quicktime.qd.Pict;
import quicktime.qd.QDColor;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.UserData;
import quicktime.util.ByteEncodedImage;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.RawEncodedImage;


public final class GraphicsImporter extends Component implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(GraphicsImporter.class);
                    }
                });
            }
        }.establish();
    }

    private static final int[] savedPort = {
        0
    };

    private static final int[] savedDevice = {
        0
    };

    public static final GraphicsImporter fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws QTException {
        return new GraphicsImporter(paramGraphicsExporter);
    }

    private GraphicsImporter(GraphicsExporter paramGraphicsExporter) throws QTException {
        super(allocate(paramGraphicsExporter), null);
    }

    public GraphicsImporter(int paramInt) throws QTException {
        super(1735551344, paramInt);
    }

    public GraphicsImporter(QTFile paramQTFile) throws QTException {
        super(allocate(paramQTFile), null);
    }

    public GraphicsImporter(QTFile paramQTFile, int paramInt) throws QTException {
        super(allocate(paramQTFile, paramInt), null);
    }

    public GraphicsImporter(DataRef paramDataRef) throws QTException {
        super(allocate(paramDataRef), null);
        this.ref = paramDataRef;
    }

    public GraphicsImporter(DataRef paramDataRef, int paramInt) throws QTException {
        super(allocate(paramDataRef, paramInt), null);
        this.ref = paramDataRef;
    }

    private static int allocate(QTFile paramQTFile) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForFile(paramQTFile.getFSSpec(true, 256), arrayOfInt);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    private static int allocate(GraphicsExporter paramGraphicsExporter) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsExportGetInputGraphicsImporter(QTObject.ID(paramGraphicsExporter), arrayOfInt);

        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    private static int allocate(QTFile paramQTFile, int paramInt) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);

            i = GetGraphicsImporterForFileWithFlags(paramQTFile.getFSSpec(true, 256), arrayOfInt, paramInt);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    private static int allocate(DataRef paramDataRef) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForDataRef(QTObject.ID(paramDataRef), paramDataRef.getType(), arrayOfInt);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        if (arrayOfInt[0] == 0)
            throw new StdQTException(63524);
        return arrayOfInt[0];
    }

    private static int allocate(DataRef paramDataRef, int paramInt) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = GetGraphicsImporterForDataRefWithFlags(QTObject.ID(paramDataRef), paramDataRef.getType(), arrayOfInt, paramInt);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        if (arrayOfInt[0] == 0)
            throw new StdQTException(63524);
        return arrayOfInt[0];
    }

    private Object ref = null;

    public final void setDataReference(DataRef paramDataRef) throws StdQTException {
        int i = GraphicsImportSetDataReference(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType());
        StdQTException.checkError(i);
        this.ref = paramDataRef;
    }

    public final int getDataReferenceType() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetDataReference(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    public final void setDataFile(QTFile paramQTFile) throws QTException {
        int i = GraphicsImportSetDataFile(_ID(), paramQTFile.getFSSpec(true, 256));
        StdQTException.checkError(i);
        this.ref = null;
    }

    public final QTFile getDataFile() throws QTException {
        return QTFile.fromGraphicsImporter(this);
    }

    public final void setDataHandle(QTHandleRef paramQTHandleRef) throws StdQTException {
        int i = GraphicsImportSetDataHandle(_ID(), QTObject.ID(paramQTHandleRef));
        StdQTException.checkError(i);
        this.ref = paramQTHandleRef;
    }

    public final QTHandle getDataHandle() throws QTException {
        return QTHandle.fromGraphicsImporterData(this);
    }

    public final ImageDescription getImageDescription() throws StdQTException {
        return ImageDescription.fromGraphicsImporter(this);
    }

    public final int getDataOffset() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetDataOffsetAndSize(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0];
    }

    public final int getDataSize() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetDataOffsetAndSize(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    public final void readData(EncodedImage paramEncodedImage, int paramInt1, int paramInt2) throws StdQTException {
        int i = -50;
        if ((paramEncodedImage instanceof ByteEncodedImage)) {
            i = GraphicsImportReadData(_ID(), ((ByteEncodedImage) paramEncodedImage).getBytes(), paramInt1, paramInt2);
        } else if ((paramEncodedImage instanceof IntEncodedImage)) {
            i = GraphicsImportReadData(_ID(), ((IntEncodedImage) paramEncodedImage).getInts(), paramInt1, paramInt2);
        } else if ((paramEncodedImage instanceof RawEncodedImage))
            i = GraphicsImportReadData(_ID(), QTObject.ID((RawEncodedImage) paramEncodedImage), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public final void setClip(Region paramRegion) throws StdQTException {
        int i = GraphicsImportSetClip(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(i);
    }

    public final Region getClip() throws QTException {
        return Region.fromGraphicsImporter(this);
    }

    public final void setSourceRect(QDRect paramQDRect) throws StdQTException {
        int i = GraphicsImportSetSourceRect(_ID(), paramQDRect.getRect());
        StdQTException.checkError(i);
    }

    public final QDRect getSourceRect() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetSourceRect(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    public final QDRect getNaturalBounds() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetNaturalBounds(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    public final void draw() throws StdQTException {
        int i = GraphicsImportDraw(_ID());
        StdQTException.checkError(i);
    }

    public final void setMatrix(Matrix paramMatrix) throws StdQTException {
        int i = GraphicsImportSetMatrix(_ID(), paramMatrix.getBytes());
        StdQTException.checkError(i);
    }

    public final Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();

        int i = GraphicsImportGetMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    public final void setBoundsRect(QDRect paramQDRect) throws StdQTException {
        int i = GraphicsImportSetBoundsRect(_ID(), paramQDRect.getRect());
        StdQTException.checkError(i);
    }

    public final QDRect getBoundsRect() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetBoundsRect(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    public final void saveAsPicture(QTFile paramQTFile, int paramInt) throws QTException {
        int i = GraphicsImportSaveAsPicture(_ID(), paramQTFile.getFSSpec(false, 512), paramInt);
        StdQTException.checkError(i);
    }

    public final void saveAsQuickTimeImageFile(QTFile paramQTFile, int paramInt) throws QTException {
        int i = GraphicsImportSaveAsQuickTimeImageFile(_ID(), paramQTFile.getFSSpec(false, 512), paramInt);
        StdQTException.checkError(i);
    }

    public final void setGraphicsMode(GraphicsMode paramGraphicsMode) throws StdQTException {
        int i = GraphicsImportSetGraphicsMode(_ID(),
                                              paramGraphicsMode.getGraphicsMode(),
                                              paramGraphicsMode.getColor().getRGBColor());

        StdQTException.checkError(i);
    }

    public final GraphicsMode getGraphicsMode() throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[6];

        int i = GraphicsImportGetGraphicsMode(_ID(), arrayOfInt, arrayOfByte);

        StdQTException.checkError(i);
        return new GraphicsMode(arrayOfInt[0], QDColor.fromArray(arrayOfByte, 6));
    }

    public final void setQuality(int paramInt) throws StdQTException {
        int i = GraphicsImportSetQuality(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    public final int getQuality() throws StdQTException {
        int[] arrayOfInt = new int[1];

        int i = GraphicsImportGetQuality(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public final QTHandle getAliasedDataReference() throws QTException {
        return QTHandle.fromGraphicsImporterAlias(this);
    }

    public final int getAliasedDataType() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];

        int i = GraphicsImportGetAliasedDataReference(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    public final void setGWorld(QDGraphics paramQDGraphics, GDevice paramGDevice) throws StdQTException {
        int i = GraphicsImportSetGWorld(_ID(), QTObject.ID(paramQDGraphics), QTObject.ID(paramGDevice));

        StdQTException.checkError(i);
    }

    public final QDGraphics getGWorld() throws StdQTException {
        return QDGraphics.fromGraphicsImporter(this);
    }

    public final boolean validate() throws StdQTException {
        int[] arrayOfInt = new int[1];

        int i = GraphicsImportValidate(_ID(), arrayOfInt);
        StdQTException.checkError(i);

        return arrayOfInt[0] != 0;
    }

    public final int doesDrawAllPixels() throws StdQTException {
        short[] arrayOfShort = {
            0
        };
        int i = GraphicsImportDoesDrawAllPixels(_ID(), arrayOfShort);
        if (i == -2147450878)
            return 0;
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public Pict getAsPicture() throws QTException {
        return Pict.fromGraphicsImporter(this);
    }

    public UserData getMetaData(GraphicsImporter paramGraphicsImporter) throws QTException {
        UserData localUserData = new UserData();
        int i = GraphicsImportGetMetaData(QTObject.ID(paramGraphicsImporter), QTObject.ID(localUserData));
        StdQTException.checkError(i);
        return localUserData;
    }

    public AtomContainer getMIMETypeList() throws StdQTException {
        return AtomContainer.fromGraphicsImporterMIME(this);
    }

    public void exportImageFile(int paramInt1, int paramInt2, QTFile paramQTFile, int paramInt3) throws QTException {
        int i = GraphicsImportExportImageFile(_ID(),
                                              paramInt1,
                                              paramInt2,
                                              paramQTFile.getFSSpec(false, 512),
                                              (short) paramInt3);
        StdQTException.checkError(i);
    }

    public final AtomContainer getExportImageTypeList() throws StdQTException {
        return AtomContainer.fromGraphicsImporterExportImage(this);
    }

    public final AtomContainer getExportSettingsAsAtomContainer() throws StdQTException {
        return AtomContainer.fromGraphicsImporterExportSettings(this);
    }

    public final void setExportSettingsFromAtomContainer(AtomContainer paramAtomContainer) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        arrayOfInt[0] = QTObject.ID(paramAtomContainer);
        int i = GraphicsImportSetExportSettingsFromAtomContainer(_ID(), arrayOfInt);
        StdQTException.checkError(i);
    }

    public final GraphicsImporterInfo doExportImageFileDialog(QTFile paramQTFile, String paramString) throws QTException {
        return QTFile.fromGraphicsImporter(this, paramQTFile, paramString);
    }

    public final void setDataReferenceOffsetAndLimit(int paramInt1, int paramInt2) throws StdQTException {
        int i = GraphicsImportSetDataReferenceOffsetAndLimit(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public final int[] getDataReferenceOffsetAndLimit() throws StdQTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        int[] arrayOfInt3 = new int[2];
        int i = GraphicsImportGetDataReferenceOffsetAndLimit(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        arrayOfInt3[0] = arrayOfInt1[0];
        arrayOfInt3[1] = arrayOfInt2[0];
        return arrayOfInt3;
    }

    public final int getImageCount() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsImportGetImageCount(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public final void setImageIndex(int paramInt) throws StdQTException {
        int i = GraphicsImportSetImageIndex(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    public final int getImageIndex() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsImportGetImageIndex(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public final Matrix getDefaultMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = GraphicsImportGetDefaultMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    public final Region getDefaultClip() throws QTException {
        return Region.fromGraphicsImporterDefault(this);
    }

    public final GraphicsMode getDefaultGraphicsMode() throws QTException {
        int[] arrayOfInt = {
            0
        };
        QDColor localQDColor = new QDColor();
        int i = GraphicsImportGetDefaultGraphicsMode(_ID(), arrayOfInt, localQDColor.getRGBColor());
        StdQTException.checkError(i);
        return new GraphicsMode(arrayOfInt[0], localQDColor);
    }

    public final QDRect getDefaultSourceRect() throws QTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetDefaultSourceRect(_ID(), localQDRect.getBytes());
        return localQDRect;
    }

    public final void setDestRect(QDRect paramQDRect) throws QTException {
        int i = GraphicsImportSetDestRect(_ID(), paramQDRect.getBytes());
    }

    public final QDRect getDestRect() throws QTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsImportGetDestRect(_ID(), localQDRect.getBytes());
        return localQDRect;
    }

    public final void setFlags(int paramInt) throws QTException {
        int i = GraphicsImportSetFlags(_ID(), paramInt);
    }

    public final int getFlags() throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsImportGetFlags(_ID(), arrayOfInt);
        return arrayOfInt[0];
    }

    private static native int GraphicsExportGetInputGraphicsImporter(int paramInt, int[] paramArrayOfInt);

    private static native short GetGraphicsImporterForFile(byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native short GetGraphicsImporterForFileWithFlags(byte[] paramArrayOfByte,
                                                                    int[] paramArrayOfInt,
                                                                    int paramInt);

    private static native short GetGraphicsImporterForDataRef(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short GetGraphicsImporterForDataRefWithFlags(int paramInt1,
                                                                       int paramInt2,
                                                                       int[] paramArrayOfInt,
                                                                       int paramInt3);

    private static native int GraphicsImportSetDataReference(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsImportGetDataReference(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsImportSetDataFile(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportSetDataHandle(int paramInt1, int paramInt2);

    private static native int GraphicsImportGetDataOffsetAndSize(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsImportReadData(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int GraphicsImportReadData(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);

    private static native int GraphicsImportReadData(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3);

    private static native int GraphicsImportSetClip(int paramInt1, int paramInt2);

    private static native int GraphicsImportSetSourceRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetSourceRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetNaturalBounds(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportDraw(int paramInt);

    private static native int GraphicsImportSetMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportSetBoundsRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetBoundsRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportSaveAsPicture(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    private static native int GraphicsImportSaveAsQuickTimeImageFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    private static native int GraphicsImportSetGraphicsMode(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetGraphicsMode(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportSetQuality(int paramInt1, int paramInt2);

    private static native int GraphicsImportGetQuality(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsImportGetAliasedDataReference(int paramInt,
                                                                    int[] paramArrayOfInt1,
                                                                    int[] paramArrayOfInt2);

    private static native int GraphicsImportSetGWorld(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsImportValidate(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsImportDoesDrawAllPixels(int paramInt, short[] paramArrayOfShort);

    private static native int GraphicsImportGetMetaData(int paramInt1, int paramInt2);

    private static native short GraphicsImportExportImageFile(int paramInt1,
                                                              int paramInt2,
                                                              int paramInt3,
                                                              byte[] paramArrayOfByte,
                                                              short paramShort);

    private static native int GraphicsImportSetExportSettingsFromAtomContainer(int paramInt, int[] paramArrayOfInt);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsImportSetDataReferenceOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsImportGetDataReferenceOffsetAndLimit(int paramInt,
                                                                           int[] paramArrayOfInt1,
                                                                           int[] paramArrayOfInt2);

    private static native int GraphicsImportGetImageCount(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsImportSetImageIndex(int paramInt1, int paramInt2);

    private static native int GraphicsImportGetImageIndex(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsImportGetDefaultMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetDefaultGraphicsMode(int paramInt,
                                                                   int[] paramArrayOfInt,
                                                                   byte[] paramArrayOfByte);

    private static native int GraphicsImportGetDefaultSourceRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportSetDestRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportGetDestRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsImportSetFlags(int paramInt1, int paramInt2);

    private static native int GraphicsImportGetFlags(int paramInt, int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /GraphicsImporter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
