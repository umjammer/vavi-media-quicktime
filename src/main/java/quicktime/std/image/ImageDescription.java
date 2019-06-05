
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.ColorTable;
import quicktime.qd.PixMap;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.anim.Sprite;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.qtcomponents.ImageCompressionDialog;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;
import quicktime.util.RawEncodedImage;
import quicktime.util.UtilException;


public class ImageDescription extends SampleDescription implements QuickTimeLib {

    private static Object linkage;

    private static EndianDescriptor ed;

    public static final int kNativeSize = 86;

    private static final int savedPort[] = {
        0
    };

    private static final int savedDevice[] = {
        0
    };

    protected static EndianDescriptor makeED() {
        EndianDescriptor endiandescriptor = SampleDescription.makeED();
        EndianFlipSpec endianflipspec = new EndianFlipSpec(16, 2, 2);
        endiandescriptor.addFlipSpec(endianflipspec);
        EndianFlipSpec endianflipspec1 = new EndianFlipSpec(20, 4, 3);
        endiandescriptor.addFlipSpec(endianflipspec1);
        EndianFlipSpec endianflipspec2 = new EndianFlipSpec(32, 2, 2);
        endiandescriptor.addFlipSpec(endianflipspec2);
        EndianFlipSpec endianflipspec3 = new EndianFlipSpec(36, 4, 2);
        endiandescriptor.addFlipSpec(endianflipspec3);
        EndianFlipSpec endianflipspec4 = new EndianFlipSpec(44, 4, 1);
        endiandescriptor.addFlipSpec(endianflipspec4);
        EndianFlipSpec endianflipspec5 = new EndianFlipSpec(48, 2, 1);
        endiandescriptor.addFlipSpec(endianflipspec5);
        EndianFlipSpec endianflipspec6 = new EndianFlipSpec(82, 2, 2);
        endiandescriptor.addFlipSpec(endianflipspec6);
        return endiandescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    public static ImageDescription fromGraphicsExporter(GraphicsExporter graphicsexporter) throws StdQTException {
        int ai[] = new int[1];
        int i = GraphicsExportGetInputImageDescription(QTObject.ID(graphicsexporter), ai);
        StdQTException.checkError(i);
        return new ImageDescription(ai[0], null);
    }

    public static ImageDescription fromGraphicsExporterCompressor(GraphicsExporter graphicsexporter,
                                                                  AtomContainer atomcontainer) throws StdQTException {
        int ai[] = {
            0
        };
        int i = GraphicsExportDoUseCompressor(QTObject.ID(graphicsexporter), QTObject.ID(atomcontainer), ai);
        StdQTException.checkError(i);
        return new ImageDescription(ai[0], graphicsexporter);
    }

    public static ImageDescription fromGraphicsImporter(GraphicsImporter graphicsimporter) throws StdQTException {
        int ai[] = new int[1];
        int i = GraphicsImportGetImageDescription(QTObject.ID(graphicsimporter), ai);
        StdQTException.checkError(i);
        return new ImageDescription(ai[0], null);
    }

    public static ImageDescription fromSprite(Sprite sprite) throws StdQTException {
        int ai[] = {
            0
        };
        short word0 = GetSpriteProperty(QTObject.ID(sprite), 2, ai);
        StdQTException.checkError(word0);
        return new ImageDescription(ai[0], sprite);
    }

    public static QTImage fromImageCompressionDialog(ImageCompressionDialog imagecompressiondialog,
                                                     QDGraphics qdgraphics,
                                                     QDRect qdrect) throws QTException {
        byte abyte0[] = qdrect == null ? null : qdrect.getRect();
        int ai[] = {
            0
        };
        int ai1[] = {
            0
        };
        if (qdgraphics == null)
            throw new StdQTException(-50);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if (QTSession.isCurrentOS(1) || QTSession.isCurrentOS(4))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(qdgraphics), 0);
            i = SCCompressImage(QTObject.ID(imagecompressiondialog), QTObject.ID(qdgraphics.getPixMap()), abyte0, ai, ai1);
            if (QTSession.isCurrentOS(1) || QTSession.isCurrentOS(4))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        if (i == 1) {
            throw new StdQTException(-128);
        } else {
            StdQTException.checkError(i);
            class _cls1ImageHdl extends QTHandleRef {

                _cls1ImageHdl(int i, Object obj) {
                    super(i, obj, false);
                }
            }

            _cls1ImageHdl _lcls1imagehdl = new _cls1ImageHdl(ai1[0], null);
            _lcls1imagehdl.lockHigh();
            return new QTImage(new ImageDescription(ai[0], null), RawEncodedImage.fromQTHandle(_lcls1imagehdl));
        }
    }

    public static ImageDescription getJavaDefaultPixelDescription(int i, int j) throws QTException {
        ImageDescription imagedescription = null;
        if (QTSession.isCurrentOS(2))
            imagedescription = new ImageDescription(QDGraphics.kDefaultPixelFormat);
        else
            imagedescription = new ImageDescription(0x72617720);
        imagedescription.setDepth(32);
        imagedescription.setSpatialQuality(512);
        imagedescription.setWidth(i);
        imagedescription.setHeight(j);
        imagedescription.setHRes(72F);
        imagedescription.setVRes(72F);
        imagedescription.setFrameCount(1);
        imagedescription.setClutID(-1);
        return (ImageDescription) imagedescription.clone();
    }

    public static ImageDescription forEffect(int i) throws QTException {
        return new ImageDescription(allocate(i), null);
    }

    public ImageDescription(int i) throws QTException {
        super(86, true, i);
    }

    public ImageDescription(PixMap pixmap) throws QTException {
        this(allocate(pixmap), null);
    }

    protected ImageDescription(int i, Object obj) {
        super(i, obj, false);
    }

    private static int allocate(PixMap pixmap) throws QTException {
        int ai[] = {
            0
        };
        StdQTException.checkError(MakeImageDescriptionForPixMap(QTObject.ID(pixmap), ai));
        return ai[0];
    }

    private static int allocate(int i) throws QTException {
        int ai[] = {
            0
        };
        StdQTException.checkError(MakeImageDescriptionForEffect(i, ai));
        return ai[0];
    }

    public Object clone() {
        return new ImageDescription(makeAndCopyHandle(), null);
    }

    public void setCTable(ColorTable colortable) throws StdQTException {
        StdQTException.checkError(SetImageDescriptionCTable(_ID(), QTObject.ID(colortable)));
    }

    public ColorTable getCTable() throws QTException {
        return ColorTable.fromImageDescription(this);
    }

    public int countExtensionType(int i) throws StdQTException {
        int ai[] = new int[1];
        StdQTException.checkError(CountImageDescriptionExtensionType(_ID(), i, ai));
        return ai[0];
    }

    public int getNextExtensionType() throws StdQTException {
        int ai[] = new int[1];
        StdQTException.checkError(GetNextImageDescriptionExtensionType(_ID(), ai));
        return ai[0];
    }

    public void addExtension(int i, QTHandle qthandle) throws StdQTException {
        StdQTException.checkError(AddImageDescriptionExtension(_ID(), QTObject.ID(qthandle), i));
    }

    public void removeExtension(int i, int j) throws StdQTException {
        StdQTException.checkError(RemoveImageDescriptionExtension(_ID(), i, j));
    }

    @Deprecated
    public QTHandle getExtension(int i, int j) throws StdQTException {
        return QTHandle.fromImageDescription(this, i, j);
    }

    public QDGraphics newGWorld(int i) throws QTException {
        return new QDGraphics(this, i);
    }

    public int getIdSize() {
        return getIntAt(0);
    }

    public int getCType() {
        return getIntAt(4);
    }

    public int getVersion() {
        return getShortAt(16);
    }

    public int getRevisionLevel() {
        return getShortAt(18);
    }

    public int getVendor() {
        return getIntAt(20);
    }

    public int getTemporalQuality() {
        return getIntAt(24);
    }

    public int getSpatialQuality() {
        return getIntAt(28);
    }

    public int getWidth() {
        return getShortAt(32);
    }

    public int getHeight() {
        return getShortAt(34);
    }

    public QDRect getBounds() {
        return new QDRect(0, 0, getShortAt(32), getShortAt(34));
    }

    public float getHRes() {
        return QTUtils.Fix2X(getIntAt(36));
    }

    public float getVRes() {
        return QTUtils.Fix2X(getIntAt(40));
    }

    public int getDataSize() {
        return getIntAt(44);
    }

    public int getFrameCount() {
        return getShortAt(48);
    }

    public String getName() throws UtilException {
        return getPStringAt(50);
    }

    public int getDepth() {
        return getShortAt(82);
    }

    public int getClutID() {
        return getShortAt(84);
    }

    public void setCType(int i) {
        setIntAt(4, i);
    }

    public void setVersion(int i) {
        setShortAt(16, (short) i);
    }

    public void setRevisionLevel(int i) {
        setShortAt(18, (short) i);
    }

    public void setVendor(int i) {
        setIntAt(20, i);
    }

    public void setTemporalQuality(int i) {
        setIntAt(24, i);
    }

    public void setSpatialQuality(int i) {
        setIntAt(28, i);
    }

    public void setWidth(int i) {
        setShortAt(32, (short) i);
    }

    public void setHeight(int i) {
        setShortAt(34, (short) i);
    }

    public void setHRes(float f) {
        setIntAt(36, QTUtils.X2Fix(f));
    }

    public void setVRes(float f) {
        setIntAt(40, QTUtils.X2Fix(f));
    }

    public void setDataSize(int i) {
        setIntAt(44, i);
    }

    public void setFrameCount(int i) {
        setShortAt(48, (short) i);
    }

    public void setName(String s) throws UtilException {
        setPStringAt(50, 31, s);
    }

    public void setDepth(int i) {
        setShortAt(82, (short) i);
    }

    public void setClutID(int i) {
        setShortAt(84, (short) i);
    }

    public String toString() {
        String s = "";
        try {
            s = getName();
        } catch (QTException qtexception) {
        }
        return getClass().getName() + "[cType=" + QTUtils.fromOSType(getCType()) + ",temporalQuality=" + getTemporalQuality()
               + ",spatialQulity=" + getSpatialQuality() + ",width=" + getWidth() + ",height=" + getHeight() + ",dataSize="
               + getDataSize() + ",frameCount=" + getFrameCount() + ",name=" + s + ",depth=" + getDepth() + "]";
    }

    private static native int GraphicsExportDoUseCompressor(int i, int j, int ai[]);

    private static native int GraphicsExportGetInputImageDescription(int i, int ai[]);

    private static native int GraphicsImportGetImageDescription(int i, int ai[]);

    private static native short GetSpriteProperty(int i, int j, int ai[]);

    private static native int SCCompressImage(int i, int j, byte abyte0[], int ai[], int ai1[]);

    private static native short MakeImageDescriptionForPixMap(int i, int ai[]);

    private static native short SetImageDescriptionCTable(int i, int j);

    private static native short GetImageDescriptionExtension(int i, int ai[], int j, int k);

    private static native short AddImageDescriptionExtension(int i, int j, int k);

    private static native short RemoveImageDescriptionExtension(int i, int j, int k);

    private static native short CountImageDescriptionExtensionType(int i, int j, int ai[]);

    private static native short GetNextImageDescriptionExtensionType(int i, int ai[]);

    private static native short MakeImageDescriptionForEffect(int i, int ai[]);

    private static native void SetGWorld(int i, int j);

    private static native void GetGWorld(int ai[], int ai1[]);

    static {
        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(ImageDescription.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }
}
