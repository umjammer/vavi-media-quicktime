package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MediaHandlerManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsMode;
import quicktime.util.QTPointer;


public class VisualMediaHandler extends MediaHandler implements QuickTimeLib {

    protected VisualMediaHandler(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * Allows you to set the graphics mode and blend color of the video media handler.
     * @see "QuickTime::MediaSetGraphicsMode"
     * @param graphicsMode the graphics mode.
     * @throws quicktime.std.StdQTException
     */
    public final void setGraphicsMode(GraphicsMode graphicsMode) throws StdQTException {
        StdQTException.checkError(MediaSetGraphicsMode(_ID(), graphicsMode.getGraphicsMode(), graphicsMode.getColor().getRGBColor()));
    }

    /**
     * Obtain the transfer mode and blend or transparent color values.
     * @see "QuickTime::MediaGetGraphicsMode"
     * currently used by the video media handler.
     * @return the blend or transparent color
     * @throws quicktime.std.StdQTException
     */
    public final GraphicsMode getGraphicsMode() throws StdQTException {
        int[] mode = new int[1];
        byte[] opColor = new byte[6];
        StdQTException.checkError(MediaGetGraphicsMode(_ID(), mode, opColor));
        return new GraphicsMode(mode[0], QDColor.fromArray(opColor, 6));
    }

    /**
     * Determine whether the visual media of the track has opaque drawing settings in its presentation.
     * @see "QuickTime::MediaGetTrackOpaque"
     * @return true if some part of the track's media is opaque.
     * @throws quicktime.std.StdQTException
     */
    public final boolean getTrackOpaque() throws StdQTException {
        byte[] opaque = { 0 };
        StdQTException.checkError(MediaGetTrackOpaque(_ID(), opaque));
        return opaque[0] != 0;
    }

    /**
     * You can use to hit test a flash, sprite, text and 3D track and get a refCon for a button or symbol that was hit.
     * @see "QuickTime::MediaHitTestForTargetRefCon"
     * @return the target RefCon at the specified point
     * @throws quicktime.std.StdQTException
     */
    public final int hitTestForTargetRefCon(int flags, QDPoint point) throws StdQTException {
        int[] targetRefCon = { 0 };
        StdQTException.checkError(MediaHitTestForTargetRefCon(_ID(), flags, point.getPoint(), targetRefCon));
        return targetRefCon[0];
    }

    /**
     * You can use to hit test a flash, sprite, text and 3D track for a given button or symbol.
     * @see "QuickTime::MediaHitTestTargetRefCon"
     * @return true if some part of the target RefCon is hit.
     * @throws quicktime.std.StdQTException
     */
    public final boolean hitTestTargetRefCon(int targetRefCon, int flags, QDPoint point) throws StdQTException {
        byte[] wasHit = { 0 };
        StdQTException.checkError(MediaHitTestTargetRefCon(_ID(), targetRefCon, flags, point.getPoint(), wasHit));
        return wasHit[0] != 0;
    }

    /**
     * This routine is an accessor for the media handler's public info
     * QuickTime:MediaSetPublicInfo
     */
    public void setPublicInfo(int infoSelector, QTPointer info) throws StdQTException {
        StdQTException.checkError(MediaSetPublicInfo(_ID(), infoSelector, QTObject.ID(info), info.getSize()));
    }

    /**
     * This routine is an accessor for the media handler's public info
     * QuickTime:MediaGetPublicInfo
     */
    public QTPointer getPublicInfo(int infoSelector) throws QTException {
        byte[] info = { 0 };
        int[] size = { 0 };
        StdQTException.checkError(MediaGetPublicInfo(_ID(), infoSelector, info, size));
        return new QTPointer(info);
    }

    private static int MediaHitTestForTargetRefCon(int mh, long flags, int loc, int[] targetRefCon) {
        return MediaHandlerManager.MediaHitTestForTargetRefCon(mh, flags, loc, targetRefCon);
    }

    private static int MediaHitTestTargetRefCon(int mh, int targetRefCon, int flags, int loc, byte[] wasHit) {
        return MediaHandlerManager.MediaHitTestTargetRefCon(mh, targetRefCon, flags, loc, wasHit);
    }

    private static int MediaSetGraphicsMode(int mh, int mode, byte[] opColor) {
        return MediaHandlerManager.MediaSetGraphicsMode(mh, mode, opColor);
    }

    private static int MediaGetGraphicsMode(int mh, int[] mode, byte[] opColor) {
        return MediaHandlerManager.MediaGetGraphicsMode(mh, mode, opColor);
    }

    private static int MediaGetTrackOpaque(int mh, byte[] opaque) {
        return MediaHandlerManager.MediaGetTrackOpaque(mh, opaque);
    }

    private static int MediaSetPublicInfo(int mh, int infoSelector, int info, int size) {
        return MediaHandlerManager.MediaSetPublicInfo(mh, infoSelector, info, size);
    }

    private static int MediaGetPublicInfo(int mh, int infoSelector, byte[] info, int[] size) {
        return MediaHandlerManager.MediaGetPublicInfo(mh, infoSelector, info, size);
    }
}
