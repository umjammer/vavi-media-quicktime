
package quicktime.std.image;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.Errors;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.Pict;
import quicktime.qd.PixMap;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.UserData;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTPointer;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;


public final class GraphicsExporter extends Component implements QuickTimeLib, Errors {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(GraphicsExporter.class);
                    }
                });
            }
        }.establish();
    }

    public GraphicsExporter(int paramInt) throws QTException {
        super(1735550328, paramInt);
    }

    public int doExport() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsExportDoExport(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void doStandaloneExport() throws QTException {
        int i = GraphicsExportDoStandaloneExport(_ID());
        StdQTException.checkError(i);
    }

    public void setInputDataReference(DataRef paramDataRef, ImageDescription paramImageDescription) throws QTException {
        int i = paramDataRef.getType();
        int j = GraphicsExportSetInputDataReference(_ID(),
                                                    QTObject.ID(paramDataRef),
                                                    paramDataRef.getType(),
                                                    QTObject.ID(paramImageDescription));
        StdQTException.checkError(j);
    }

    public DataRef getInputDataReference() throws QTException {
        return DataRef.fromGraphicsExporterInputDataRef(this);
    }

    public void setInputFile(QTFile paramQTFile, ImageDescription paramImageDescription) throws QTException {
        int i = GraphicsExportSetInputFile(_ID(), paramQTFile.getFSSpec(true, 256), QTObject.ID(paramImageDescription));
        StdQTException.checkError(i);
    }

    public QTFile getInputFile() throws QTException {
        return QTFile.fromGraphicsExporterInput(this);
    }

    public void setInputHandle(QTHandle paramQTHandle, ImageDescription paramImageDescription) throws QTException {
        int i = GraphicsExportSetInputHandle(_ID(), QTObject.ID(paramQTHandle), QTObject.ID(paramImageDescription));
        StdQTException.checkError(i);
    }

    public QTHandle getInputHandle() throws QTException {
        return QTHandle.fromGraphicsExporterInput(this);
    }

    public void setInputPtr(QTPointerRef paramQTPointerRef, ImageDescription paramImageDescription) throws QTException {
        int i = GraphicsExportSetInputPtr(_ID(),
                                          QTObject.ID(paramQTPointerRef),
                                          paramImageDescription.getDataSize(),
                                          QTObject.ID(paramImageDescription));
        StdQTException.checkError(i);
    }

    public QTPointerRef getInputPtr() throws QTException {
        return QTPointer.fromGraphicsExporter(this);
    }

    public void setInputGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws QTException {
        int i = GraphicsExportSetInputGraphicsImporter(_ID(), QTObject.ID(paramGraphicsImporter));
        StdQTException.checkError(i);
    }

    public GraphicsImporter getInputGraphicsImporter() throws QTException {
        return GraphicsImporter.fromGraphicsExporter(this);
    }

    public void setInputPicture(Pict paramPict) throws QTException {
        int i = GraphicsExportSetInputPicture(_ID(), QTObject.ID(paramPict));
        StdQTException.checkError(i);
    }

    public Pict getInputPicture() throws QTException {
        return Pict.fromGraphicsExporter(this);
    }

    public void setInputGWorld(QDGraphics paramQDGraphics) throws QTException {
        int i = GraphicsExportSetInputGWorld(_ID(), QTObject.ID(paramQDGraphics));
        StdQTException.checkError(i);
    }

    public QDGraphics getInputGWorld() throws QTException {
        return QDGraphics.fromGraphicsExporter(this);
    }

    public void setInputPixmap(QDGraphics paramQDGraphics) throws StdQTException {
        if (paramQDGraphics == null)
            throw new StdQTException(-50);
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = GraphicsExportSetInputPixmap(_ID(), QTObject.ID(paramQDGraphics.getPixMap()));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
    }

    private static final int[] savedPort = {
        0
    };

    private static final int[] savedDevice = {
        0
    };

    public PixMap getInputPixmap() throws QTException {
        return PixMap.fromGraphicsExporter(this);
    }

    public void setOutputDataReference(DataRef paramDataRef) throws QTException {
        int i = paramDataRef.getType();
        int j = GraphicsExportSetOutputDataReference(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType());
        StdQTException.checkError(j);
    }

    public DataRef getOutputDataReference() throws QTException {
        return DataRef.fromGraphicsExporterOutputDataRef(this);
    }

    public void setOutputFile(QTFile paramQTFile) throws QTException {
        int i = GraphicsExportSetOutputFile(_ID(), paramQTFile.getFSSpec(false, 512));
        StdQTException.checkError(i);
    }

    public QTFile getOutputFile() throws QTException {
        return QTFile.fromGraphicsExporterOutput(this);
    }

    public void setOutputHandle(QTHandleRef paramQTHandleRef) throws QTException {
        int i = GraphicsExportSetOutputHandle(_ID(), QTObject.ID(paramQTHandleRef));
        StdQTException.checkError(i);
    }

    public QTHandle getOutputHandle() throws QTException {
        return QTHandle.fromGraphicsExporterOutput(this);
    }

    public void setOutputOffsetAndMaxSize(int paramInt1, int paramInt2, boolean paramBoolean) throws QTException {
        int i = GraphicsExportSetOutputOffsetAndMaxSize(_ID(), paramInt1, paramInt2, (byte) (!paramBoolean ? 0 : 1));
        StdQTException.checkError(i);
    }

    public int getOutputOffset() throws StdQTException {
        int[] arrayOfInt = {
            0
        };

        int i = GraphicsExportGetOutputOffsetAndMaxSize(_ID(), arrayOfInt, null, null);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public int getOutputMaxSize() throws StdQTException {
        int[] arrayOfInt = {
            0
        };

        int i = GraphicsExportGetOutputOffsetAndMaxSize(_ID(), null, arrayOfInt, null);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public boolean getOutputTruncateFile() throws StdQTException {
        byte[] arrayOfByte = {
            0
        };

        int i = GraphicsExportGetOutputOffsetAndMaxSize(_ID(), null, null, arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    public void setOutputFileTypeAndCreator(int paramInt1, int paramInt2) throws QTException {
        int i = GraphicsExportSetOutputFileTypeAndCreator(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public int getOutputFileType() throws QTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };

        int i = GraphicsExportGetOutputFileTypeAndCreator(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0];
    }

    public int getOutputFileCreator() throws QTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };

        int i = GraphicsExportGetOutputFileTypeAndCreator(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    public boolean mayExporterReadInputData() throws QTException {
        byte[] arrayOfByte = {
            0
        };
        int i = GraphicsExportMayExporterReadInputData(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    public int getInputDataSize() throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsExportGetInputDataSize(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public byte[] readInputData(int paramInt1, int paramInt2) throws StdQTException {
        byte[] arrayOfByte = new byte[paramInt2];
        int i = GraphicsExportReadInputData(_ID(), arrayOfByte, paramInt1, paramInt2);
        StdQTException.checkError(i);
        return arrayOfByte;
    }

    public ImageDescription getInputImageDescription() throws QTException {
        return ImageDescription.fromGraphicsExporter(this);
    }

    public QDRect getInputImageDimensions() throws QTException {
        QDRect localQDRect = new QDRect();
        int i = GraphicsExportGetInputImageDimensions(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    public int getInputImageDepth() throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsExportGetInputImageDepth(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void drawInputImage(QDGraphics paramQDGraphics, QDRect paramQDRect1, QDRect paramQDRect2) throws QTException {
        int i = GraphicsExportDrawInputImage(_ID(),
                                             QTObject.ID(paramQDGraphics),
                                             0,
                                             paramQDRect1.getRect(),
                                             paramQDRect2.getRect());
        StdQTException.checkError(i);
    }

    public void setInputOffsetAndLimit(int paramInt1, int paramInt2) throws QTException {
        int i = GraphicsExportSetInputOffsetAndLimit(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public int getInputOffset() throws QTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        int i = GraphicsExportGetInputOffsetAndLimit(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0];
    }

    public int getInputLimit() throws QTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        int i = GraphicsExportGetInputOffsetAndLimit(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    public void requestSettings() throws QTException {
        int j;
        if (QTSession.isCurrentOS(4)) {
            j = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                public Object invoke() {
                    int i = 0;
                    synchronized (QTNative.globalsLock) {
                        i = GraphicsExporter.GraphicsExportRequestSettings(_ID(), 0, 0);
                    }
                    return new Integer(i);
                }
            })).intValue();
            StdQTException.checkError(j);
        } else {
            synchronized (QTNative.globalsLock) {
                j = GraphicsExportRequestSettings(_ID(), 0, 0);
                if (j == 1)
                    throw new StdQTException(-128);
                StdQTException.checkError(j);
            }
        }
    }

    public void setSettingsFromAtomContainer(AtomContainer paramAtomContainer) throws QTException {
        int i = GraphicsExportSetSettingsFromAtomContainer(_ID(), paramAtomContainer.getBytes());
        StdQTException.checkError(i);
    }

    public AtomContainer getSettingsAsAtomContainer() throws QTException {
        return AtomContainer.fromGraphicsExporter(this);
    }

    public QTHandle getSettingsAsText() throws QTException {
        return QTHandle.fromGraphicsExporterText(this);
    }

    public String getDefaultFileNameExtension() throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsExportGetDefaultFileNameExtension(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return QTUtils.fromOSType(arrayOfInt[0]);
    }

    public AtomContainer getMIMETypeList() throws QTException {
        return AtomContainer.fromGraphicsExporterMIME(this);
    }

    public String getDefaultFileType() throws QTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        int i = GraphicsExportGetDefaultFileTypeAndCreator(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return QTUtils.fromOSType(arrayOfInt1[0]);
    }

    public String getDefaultFileCreator() throws QTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        int i = GraphicsExportGetDefaultFileTypeAndCreator(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return QTUtils.fromOSType(arrayOfInt2[0]);
    }

    public boolean canTranscode() throws QTException {
        byte[] arrayOfByte = {
            0
        };
        int i = GraphicsExportCanTranscode(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    public void doTranscode() throws QTException {
        int i = GraphicsExportDoTranscode(_ID());
        StdQTException.checkError(i);
    }

    public GECompressorInfo canUseCompressor() throws QTException {
        return AtomContainer.fromGraphicsExporterCompressor(this);
    }

    public ImageDescription doUseCompressor(AtomContainer paramAtomContainer) throws QTException {
        return ImageDescription.fromGraphicsExporterCompressor(this, paramAtomContainer);
    }

    public void setDontRecompress(boolean paramBoolean) throws QTException {
        int i = GraphicsExportSetDontRecompress(_ID(), (byte) (paramBoolean ? 1 : 0));
        StdQTException.checkError(i);
    }

    public boolean getDontRecompress() throws QTException {
        byte[] arrayOfByte = {
            0
        };
        int i = GraphicsExportGetDontRecompress(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    public void setInterlaceStyle(int paramInt) throws QTException {
        int i = GraphicsExportSetInterlaceStyle(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    public int getInterlaceStyle() throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsExportGetInterlaceStyle(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void setMetaData(UserData paramUserData) throws QTException {
        int i = GraphicsExportSetMetaData(_ID(), QTObject.ID(paramUserData));
        StdQTException.checkError(i);
    }

    public UserData getMetaData() throws QTException {
        return UserData.fromGraphicsExporter(this);
    }

    public void setTargetDataSize(int paramInt) throws QTException {
        int i = GraphicsExportSetTargetDataSize(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    public int getTargetDataSize() throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsExportGetTargetDataSize(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void setCompressionMethod(int paramInt) throws QTException {
        int i = GraphicsExportSetCompressionMethod(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    public int getCompressionMethod() throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsExportGetCompressionMethod(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void setCompressionQuality(int paramInt) throws QTException {
        int i = GraphicsExportSetCompressionQuality(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    public int getCompressionQuality() throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsExportGetCompressionQuality(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void setResolution(int paramInt1, int paramInt2) throws QTException {
        int i = GraphicsExportSetResolution(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public int getHorizontalResolution() throws QTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };

        int i = GraphicsExportGetResolution(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0];
    }

    public int getVerticalResolution() throws QTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };

        int i = GraphicsExportGetResolution(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt2[0];
    }

    public void setDepth(int paramInt) throws QTException {
        int i = GraphicsExportSetDepth(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    public int getDepth() throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsExportGetDepth(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void GraphicsExportWriteOutputData(byte[] paramArrayOfByte, int paramInt) throws StdQTException {
        int i = GraphicsExportWriteOutputData(_ID(), paramArrayOfByte, paramInt);
        StdQTException.checkError(i);
    }

    public byte[] GraphicsExportReadOutputData(int paramInt1, int paramInt2) throws StdQTException {
        byte[] arrayOfByte = new byte[paramInt2];
        int i = GraphicsExportReadOutputData(_ID(), arrayOfByte, paramInt1, paramInt2);
        StdQTException.checkError(i);
        return arrayOfByte;
    }

    public void setExifEnabled(boolean paramBoolean) throws StdQTException {
        int i = GraphicsExportSetExifEnabled(_ID(), (byte) (paramBoolean ? 0 : 1));
        StdQTException.checkError(i);
    }

    public boolean getExifEnabled() throws StdQTException {
        byte[] arrayOfByte = {
            0
        };
        int i = GraphicsExportGetExifEnabled(_ID(), arrayOfByte);
        StdQTException.checkError(i);

        return arrayOfByte[0] != 0;
    }

    public void setThumbnailEnabled(boolean paramBoolean, int paramInt1, int paramInt2) throws StdQTException {
        int i = GraphicsExportSetThumbnailEnabled(_ID(), (byte) (paramBoolean ? 1 : 0), paramInt1, paramInt2);

        StdQTException.checkError(i);
    }

    public boolean getThumbnailEnabled() throws StdQTException {
        byte[] arrayOfByte = {
            0
        };
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };

        int i = GraphicsExportGetThumbnailEnabled(_ID(), arrayOfByte, arrayOfInt1, arrayOfInt2);

        StdQTException.checkError(i);

        return arrayOfByte[0] != 0;
    }

    public QDDimension getThumbnailDimension() throws StdQTException {
        byte[] arrayOfByte = {
            0
        };
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };

        int i = GraphicsExportGetThumbnailEnabled(_ID(), arrayOfByte, arrayOfInt1, arrayOfInt2);

        StdQTException.checkError(i);

        return new QDDimension(arrayOfInt1[0], arrayOfInt2[0]);
    }

    private static native int GraphicsExportSetExifEnabled(int paramInt, byte paramByte);

    private static native int GraphicsExportGetExifEnabled(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportSetThumbnailEnabled(int paramInt1, byte paramByte, int paramInt2, int paramInt3);

    private static native int GraphicsExportGetThumbnailEnabled(int paramInt,
                                                                byte[] paramArrayOfByte,
                                                                int[] paramArrayOfInt1,
                                                                int[] paramArrayOfInt2);

    private static native int GraphicsExportDoExport(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportSetInputDataReference(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int GraphicsExportSetInputFile(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    private static native int GraphicsExportSetInputHandle(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsExportSetInputPtr(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int GraphicsExportSetInputGraphicsImporter(int paramInt1, int paramInt2);

    private static native int GraphicsExportSetInputPicture(int paramInt1, int paramInt2);

    private static native int GraphicsExportSetInputGWorld(int paramInt1, int paramInt2);

    private static native int GraphicsExportSetInputPixmap(int paramInt1, int paramInt2);

    private static native int GraphicsExportSetOutputDataReference(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsExportSetOutputFile(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportSetOutputHandle(int paramInt1, int paramInt2);

    private static native int GraphicsExportSetOutputOffsetAndMaxSize(int paramInt1,
                                                                      int paramInt2,
                                                                      int paramInt3,
                                                                      byte paramByte);

    private static native int GraphicsExportGetOutputOffsetAndMaxSize(int paramInt,
                                                                      int[] paramArrayOfInt1,
                                                                      int[] paramArrayOfInt2,
                                                                      byte[] paramArrayOfByte);

    private static native int GraphicsExportSetOutputFileTypeAndCreator(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsExportGetOutputFileTypeAndCreator(int paramInt,
                                                                        int[] paramArrayOfInt1,
                                                                        int[] paramArrayOfInt2);

    private static native int GraphicsExportMayExporterReadInputData(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportGetInputDataSize(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportReadInputData(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);

    private static native int GraphicsExportGetInputImageDimensions(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportGetInputImageDepth(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportDrawInputImage(int paramInt1,
                                                           int paramInt2,
                                                           int paramInt3,
                                                           byte[] paramArrayOfByte1,
                                                           byte[] paramArrayOfByte2);

    private static native int GraphicsExportSetInputOffsetAndLimit(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsExportGetInputOffsetAndLimit(int paramInt,
                                                                   int[] paramArrayOfInt1,
                                                                   int[] paramArrayOfInt2);

    private static native int GraphicsExportRequestSettings(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsExportSetSettingsFromAtomContainer(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportGetDefaultFileTypeAndCreator(int paramInt,
                                                                         int[] paramArrayOfInt1,
                                                                         int[] paramArrayOfInt2);

    private static native int GraphicsExportGetDefaultFileNameExtension(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportGetMIMETypeList(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportCanTranscode(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportDoTranscode(int paramInt);

    private static native int GraphicsExportCanUseCompressor(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native int GraphicsExportDoStandaloneExport(int paramInt);

    private static native int GraphicsExportSetDontRecompress(int paramInt, byte paramByte);

    private static native int GraphicsExportGetDontRecompress(int paramInt, byte[] paramArrayOfByte);

    private static native int GraphicsExportSetInterlaceStyle(int paramInt1, int paramInt2);

    private static native int GraphicsExportGetInterlaceStyle(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportSetMetaData(int paramInt1, int paramInt2);

    private static native int GraphicsExportGetMetaData(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportSetTargetDataSize(int paramInt1, int paramInt2);

    private static native int GraphicsExportGetTargetDataSize(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportSetCompressionMethod(int paramInt1, int paramInt2);

    private static native int GraphicsExportGetCompressionMethod(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportSetCompressionQuality(int paramInt1, int paramInt2);

    private static native int GraphicsExportGetCompressionQuality(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportSetResolution(int paramInt1, int paramInt2, int paramInt3);

    private static native int GraphicsExportGetResolution(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsExportSetDepth(int paramInt1, int paramInt2);

    private static native int GraphicsExportGetDepth(int paramInt, int[] paramArrayOfInt);

    private static native int GraphicsExportWriteOutputData(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    private static native int GraphicsExportReadOutputData(int paramInt1,
                                                           byte[] paramArrayOfByte,
                                                           int paramInt2,
                                                           int paramInt3);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /GraphicsExporter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
