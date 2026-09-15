package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsMode;
import quicktime.util.QTPointer;

public class VisualMediaHandler extends MediaHandler implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(VisualMediaHandler.class);
                    }
                });
            }
        }.establish();
    }

    protected VisualMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * Allows you to set the graphics mode and blend color of the video media handler.
     * QuickTime::MediaSetGraphicsMode
     * @param graphicsMode the graphics mode.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setGraphicsMode(GraphicsMode paramGraphicsMode) throws StdQTException {
        StdQTException.checkError(MediaSetGraphicsMode(_ID(), paramGraphicsMode.getGraphicsMode(), paramGraphicsMode.getColor().getRGBColor()));
    }

    /**
     * Obtain the transfer mode and blend or transparent color values.
     * QuickTime::MediaGetGraphicsMode
     * currently used by the video media handler.
     * @return the blend or transparent color
     * @return {@link quicktime.std.StdQTException}
     */
    public final GraphicsMode getGraphicsMode() throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[6];
        StdQTException.checkError(MediaGetGraphicsMode(_ID(), arrayOfInt, arrayOfByte));
        return new GraphicsMode(arrayOfInt[0], QDColor.fromArray(arrayOfByte, 6));
    }

    /**
     * Determine whether the visual media of the track has opaque drawing settings in its presentation.
     * QuickTime::MediaGetTrackOpaque
     * @return true if some part of the track's media is opaque.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean getTrackOpaque() throws StdQTException {
        byte[] arrayOfByte = { 0 };
        StdQTException.checkError(MediaGetTrackOpaque(_ID(), arrayOfByte));
        return arrayOfByte[0] != 0;
    }

    /**
     * You can use to hit test a flash, sprite, text and 3D track and get a refCon for a button or symbol that was hit.
     * QuickTime::MediaHitTestForTargetRefCon
     * @return the target RefCon at the specified point
     * @return {@link quicktime.std.StdQTException}
     */
    public final int hitTestForTargetRefCon(int paramInt, QDPoint paramQDPoint) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(MediaHitTestForTargetRefCon(_ID(), paramInt, paramQDPoint.getPoint(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * You can use to hit test a flash, sprite, text and 3D track for a given button or symbol.
     * QuickTime::MediaHitTestTargetRefCon
     * @return true if some part of the target RefCon is hit.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean hitTestTargetRefCon(int paramInt1, int paramInt2, QDPoint paramQDPoint) throws StdQTException {
        byte[] arrayOfByte = { 0 };
        StdQTException.checkError(MediaHitTestTargetRefCon(_ID(), paramInt1, paramInt2, paramQDPoint.getPoint(), arrayOfByte));
        return arrayOfByte[0] != 0;
    }

    /**
     * This routine is an accessor for the media handler's public info
     * QuickTime:MediaSetPublicInfo
     */
    public void setPublicInfo(int paramInt, QTPointer paramQTPointer) throws StdQTException {
        StdQTException.checkError(MediaSetPublicInfo(_ID(), paramInt, QTObject.ID(paramQTPointer), paramQTPointer.getSize()));
    }

    /**
     * This routine is an accessor for the media handler's public info
     * QuickTime:MediaGetPublicInfo
     */
    public QTPointer getPublicInfo(int paramInt) throws QTException {
        byte[] arrayOfByte = { 0 };
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(MediaGetPublicInfo(_ID(), paramInt, arrayOfByte, arrayOfInt));
        return new QTPointer(arrayOfByte);
    }

    private static native int MediaHitTestForTargetRefCon(int paramInt1, long paramLong, int paramInt2, int[] paramArrayOfInt);

    private static native int MediaHitTestTargetRefCon(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte);

    private static native int MediaSetGraphicsMode(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int MediaGetGraphicsMode(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte);

    private static native int MediaGetTrackOpaque(int paramInt, byte[] paramArrayOfByte);

    private static native int MediaSetPublicInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int MediaGetPublicInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int[] paramArrayOfInt);
}

