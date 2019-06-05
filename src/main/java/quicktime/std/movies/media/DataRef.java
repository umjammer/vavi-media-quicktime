
package quicktime.std.movies.media;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.io.AliasHandle;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.movies.Movie;
import quicktime.std.sg.SGOutput;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.EndianOrder;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;
import quicktime.util.StringHandle;


public final class DataRef extends QTHandle implements QuickTimeLib {
    private static Object linkage;

    private int dataRefType;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(DataRef.class);
                    }
                });
            }
        }.establish();
    }

    public static DataRef fromGraphicsExporterInputDataRef(GraphicsExporter paramGraphicsExporter) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsExportGetInputDataReference(QTObject.ID(paramGraphicsExporter), arrayOfInt1, arrayOfInt2);
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], 0);
    }

    public static DataRef fromGraphicsExporterOutputDataRef(GraphicsExporter paramGraphicsExporter) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsExportGetOutputDataReference(QTObject.ID(paramGraphicsExporter), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], 0);
    }

    public static DataRef fromMovie(Movie paramMovie) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GetMovieDefaultDataRef(QTObject.ID(paramMovie), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], 0);
    }

    public static DataRef fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int i = SGGetDataRef(QTObject.ID(paramSequenceGrabber), arrayOfInt1, arrayOfInt2, arrayOfInt3);

        StdQTException.checkError(i);
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], 0, paramSequenceGrabber);
    }

    public static DataRef fromMedia(Media paramMedia, int paramInt) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        StdQTException
                .checkError(GetMediaDataRef(QTObject.ID(paramMedia), (short) paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3));
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], arrayOfInt3[0]);
    }

    public static DataRef fromSGOutput(SequenceGrabber paramSequenceGrabber, SGOutput paramSGOutput) throws StdQTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };

        int i = SGGetOutputDataReference(QTObject.ID(paramSequenceGrabber),
                                         QTObject.ID(paramSGOutput),
                                         arrayOfInt1,
                                         arrayOfInt2);
        StdQTException.checkError(i);
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], 0);
    }

    public DataRef(String paramString) throws QTException {
        super(4, false);
        if (paramString.startsWith("file")) {
            QTFile localQTFile = new QTFile(paramString.substring(7).replace('/', File.separatorChar));
            setAliasHandle(localQTFile.newAlias(true));
        } else {
            setURLDataRef(QTUtils.String2CString(paramString));
        }
    }

    public DataRef(QTFile paramQTFile) throws QTException {
        super(4, false);
        setAliasHandle(paramQTFile.newAlias(true));
    }

    public DataRef(AliasHandle paramAliasHandle) throws IOException, QTException {
        super(4, false);
        setAliasHandle(paramAliasHandle);
    }

    public DataRef(QTHandleRef paramQTHandleRef) throws QTException {
        super(4, true);
        this.dataRefType = 1752065132;
        this.hdl = paramQTHandleRef;
        setIntAt(0, QTObject.ID(paramQTHandleRef));
    }

    public DataRef(QTHandleRef paramQTHandleRef, int paramInt, String paramString) throws QTException {
        super(4, true);
        this.dataRefType = 1752065132;
        this.hdl = paramQTHandleRef;
        setIntAt(0, QTObject.ID(paramQTHandleRef));

        switch (paramInt) {
        case 1718909296:
            if (QTSession.getQTVersion() == 3)
                throw new QTException(-50);
            setSize(17);
            setByteAt(4, (byte) 0);
            setIntAt(5, EndianOrder.flipNativeToBigEndian32(12));
            setIntAt(9, EndianOrder.flipNativeToBigEndian32(1718909296));
            setIntAt(13, EndianOrder.flipNativeToBigEndian32(QTUtils.toOSType(paramString)));
            break;
        case 1835625829:
            if (QTSession.getQTVersion() == 3)
                throw new QTException(-50);
            byte[] arrayOfByte = QTUtils.String2PString(paramString, 255);
            setSize(13 + arrayOfByte.length);
            setByteAt(4, (byte) 0);
            setIntAt(5, EndianOrder.flipNativeToBigEndian32(8 + arrayOfByte.length));
            setIntAt(9, EndianOrder.flipNativeToBigEndian32(1835625829));
            setBytesAt(13, arrayOfByte.length, arrayOfByte, 0);
            break;
        case 1702392864:
            concatenate(new StringHandle("X." + paramString, 2));
            break;
        default:
            throw new QTException(-50);
        }

    }

    private DataRef(int paramInt1, int paramInt2, int paramInt3, Object paramObject) {
        super(paramInt1, paramObject, false);
        this.dataRefType = paramInt2;
        this.dataRefAttributes = paramInt3;
    }

    private DataRef(int paramInt1, int paramInt2, int paramInt3) {
        super(paramInt1, null, false);
        this.dataRefType = paramInt2;
        this.dataRefAttributes = paramInt3;
    }

    private int dataRefAttributes = 0;

    private QTHandleRef hdl = null;

    private void setAliasHandle(AliasHandle paramAliasHandle) throws QTException {
        copyFromHandle(paramAliasHandle);
        this.dataRefType = 1634494835;
    }

    private void setURLDataRef(byte[] paramArrayOfByte) throws QTException {
        setSize(paramArrayOfByte.length);
        copyFromArray(0, paramArrayOfByte, 0, paramArrayOfByte.length);
        this.dataRefType = 1970433056;
        int i = GetDataHandler(_ID(), getType(), 1);
        if (i == 0)
            throw new QTException(63524);
    }

    private void copyFromHandle(QTHandleRef paramQTHandleRef) throws QTException {
        setSize(paramQTHandleRef.getSize());
        copyFromHandle(0, paramQTHandleRef, 0, paramQTHandleRef.getSize());
    }

    public int getType() {
        return this.dataRefType;
    }

    public int getAttributes() {
        return this.dataRefAttributes;
    }

    public String getUniversalResourceLocator() throws QTException {
        if (this.dataRefType == 1970433056) {
            return getCStringAt(0);
        }
        if (this.dataRefType == 1634494835) {
            AliasHandle localAliasHandle = AliasHandle.fromDataRef(this);
            QTFile localQTFile = QTFile.resolveAlias(localAliasHandle);
            return localQTFile.getPath();
        }
        return null;
    }

    public DataRef fromMovieAnchor(Movie paramMovie) throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        int[] arrayOfInt3 = {
            0
        };
        i = GetMovieAnchorDataRef(QTObject.ID(paramMovie), arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);

        if (arrayOfInt1[0] == 0) {
            return null;
        }
        return new DataRef(arrayOfInt1[0], arrayOfInt2[0], 0);
    }

    private static native short GetMediaDataRef(int paramInt,
                                                short paramShort,
                                                int[] paramArrayOfInt1,
                                                int[] paramArrayOfInt2,
                                                int[] paramArrayOfInt3);

    private static native short GetMovieDefaultDataRef(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int SGGetDataRef(int paramInt,
                                           int[] paramArrayOfInt1,
                                           int[] paramArrayOfInt2,
                                           int[] paramArrayOfInt3);

    private static native int GraphicsExportGetInputDataReference(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GraphicsExportGetOutputDataReference(int paramInt,
                                                                   int[] paramArrayOfInt1,
                                                                   int[] paramArrayOfInt2);

    private static native int GetDataHandler(int paramInt1, int paramInt2, int paramInt3);

    private static native int SGGetOutputDataReference(int paramInt1,
                                                       int paramInt2,
                                                       int[] paramArrayOfInt1,
                                                       int[] paramArrayOfInt2);

    private static native short GetMovieAnchorDataRef(int paramInt,
                                                      int[] paramArrayOfInt1,
                                                      int[] paramArrayOfInt2,
                                                      int[] paramArrayOfInt3);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/DataRef.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
