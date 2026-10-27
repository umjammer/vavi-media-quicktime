package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.GraphicsExportManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.movies.Track;
import quicktime.util.QTHandleRef;
import quicktime.util.RawEncodedImage;

/**
 * A pixel map contains information about the dimensions and contents of a pixel
 * image, as well as information on the image's storage format, depth, resolution
 * and color usage.
 * Refer to PixMap in QuickDraw
 */
public final class PixMap extends QTHandleRef implements QuickTimeLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 50;

    private final boolean isOff;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(PixMap.class));
            }
        }.establish();
    }

    /**
     * A PixMap object which describes the pixels in the QDGraphics.
     * @see "QuickTime::GetGWorldPixMap()"
     * @return a PixMap object
     * @see quicktime.std.qd.QDGraphics#getPixMap
     */
    public static PixMap fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetInputPixmap(QTObject.ID(paramGraphicsExporter), arrayOfInt);
        return new PixMap(arrayOfInt[0], paramGraphicsExporter, false);
    }

    /**
     * A PixMap object which describes the pixels in the QDGraphics.
     * @see "QuickTime::GetGWorldPixMap()"
     * @param g the QDGraphics object
     * @return a PixMap object
     * @see quicktime.std.qd.QDGraphics#getPixMap
     */
    public static PixMap fromQDGraphics(QDGraphics paramQDGraphics) {
        return new PixMap(GetGWorldPixMap(QTObject.ID(paramQDGraphics)), paramQDGraphics, paramQDGraphics.isOffscreen());
    }

    /**
     * A PixMap object which describes the pixels in a GDevice.
     * @param g the GDevice object
     * @return a PixMap object
     */
    public static PixMap fromGDevice(GDevice paramGDevice) {
        return new PixMap(getIntFromHandle(QTObject.ID(paramGDevice), 22), paramGDevice, !paramGDevice.testAttribute(13));
    }

    /**
     * A PixMap object which describes the Matte of a Track.
     * @see "QuickTime::GetTrackMatte()"
     * @param t the Track object
     * @return a PixMap object, or null if no matte set
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.Track#getMatte()
     */
    public static PixMap fromTrack(Track paramTrack) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetTrackMatte(QTObject.ID(paramTrack));
            StdQTException.checkError(GetMoviesError());
        }
        if (i == 0)
            return null;
        return new PixMap(i, null, true);
    }

    private PixMap(int paramInt, Object paramObject, boolean paramBoolean) {
        super(paramInt, paramObject, true);
        doLock();
        this.isOff = paramBoolean;
    }

    /**
     * Returns true if the pixel data is from an offscreen GWorld or
     * false if it is from an onscreen CGrafPort
     */
    public boolean isOffscreen() {
        return this.isOff;
    }

    /**
     * Return the number of bytes each row of the pixel data makes up.
     * @return the byte length in each row
     */
    public int getRowBytes() {
        return getShortAt(4) & 0x3FFF;
    }

    /**
     * Return the size (how many bits) of each pixel in the pixel data
     * @return pixel size
     */
    public int getPixelSize() {
        return getShortAt(32);
    }

    /**
     * Returns the pixel format of this PixMap
     */
    public int getPixelFormat() {
        int i = getIntAt(38);
        return i != 0 ? i : getPixelSize();
    }

    /**
     * Returns the actual Pixel data itself. This can then be passed around in ICM calls
     * that deal with pixel data.
     * @return the pixels
     */
    public RawEncodedImage getPixelData() {
        return RawEncodedImage.fromPixMap(this);
    }

    /**
     * The boundary rectangle, which links the local coordinate system of a graphics
     * port to QuickDraw's global coordinate system and defines the area of the bit
     * image into which QuickDraw can draw.
     * @return a QDRect object representing the bounds of the PixMap
     */
    public QDRect getBounds() {
        if (QTSession.isCurrentOS(4)) {
            QDRect localObject = new QDRect();
            GetPixBounds(_ID(), localObject.getBytes());
            return localObject;
        }
        Object localObject = new byte[8];
        getBytesAt(6, 8, (byte[]) localObject, 0);
        return QDRect.fromArray((byte[]) localObject, 8);
    }

    /**
     * When creating a PixMap for a particular graphics device, Color QuickDraw
     * creates a ColorTable that defines the best colors available for the pixel
     * image on that particular graphics device.  Note that a reference to the
     * PixMap object will be maintained by the ColorTable object to prevent the
     * PixMap from being garbage collected before the ColorTable.
     * @return a ColorTable object
     * @see quicktime.std.qd.ColorTable
     */
    public ColorTable getColorTable() {
        return ColorTable.fromPixMap(this);
    }

    @Override
    protected void doLock() {
        LockPixels(_ID());
    }

    @Override
    protected void doUnlock() {
        UnlockPixels(_ID());
    }

    /**
     * Print information about this object.
     *
     * @return a string representing this PixMap object
     */
    public String toString() {
        return super.toString() + "[Bounds=" + getBounds() + "]";
    }

    private static int GraphicsExportGetInputPixmap(int paramInt, int[] paramArrayOfInt) {
        return GraphicsExportManager.GraphicsExportGetInputPixmap(paramInt, paramArrayOfInt);
    }

    private static int GetGWorldPixMap(int paramInt) {
        return QuickDrawManager.GetGWorldPixMap(paramInt);
    }

    private static byte LockPixels(int paramInt) {
        return QuickDrawManager.LockPixels(paramInt);
    }

    private static void UnlockPixels(int paramInt) {
        QuickDrawManager.UnlockPixels(paramInt);
    }

    private static int GetTrackMatte(int paramInt) {
        return MovieToolbox.GetTrackMatte(paramInt);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int getIntFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromHandle(paramInt1, paramInt2);
    }

    private static int GetPixBounds(int paramInt, byte[] paramArrayOfByte) {
        return QuickDrawManager.GetPixBounds(paramInt, paramArrayOfByte);
    }
}
