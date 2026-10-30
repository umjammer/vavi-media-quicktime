package quicktime.std.qtcomponents;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.image.ImageDescription;
import quicktime.util.QTHandleRef;


public final class SCSequence implements QuickTimeLib {

    private final ImageDescription desc;

    private final ImageCompressionDialog dialog;

    SCSequence(ImageCompressionDialog dialog, QDGraphics port, QDRect rect) throws QTException {
        if (port == null) {
            throw new StdQTException(-50);
        }
        int[] arrayOfInt1 = { 0 };
        int[] gdh = { 0 };
        int[] desc = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(arrayOfInt1, gdh);
            SetGWorld(QTObject.ID(port), 0);
            i = SCCompressSequenceBegin(QTObject.ID(dialog), QTObject.ID(port.getPixMap()), rect != null ? rect.getRect() : null, desc);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(arrayOfInt1[0], gdh[0]);
        }
        StdQTException.checkError(i);
        this.dialog = dialog;
        this.desc = new ImageDescription(desc[0], this) {
        };
    }

    private final boolean ended = false;

    /**
     * Return the description
     */
    public ImageDescription getDescription() {
        return this.desc;
    }

    /**
     * Return the SCDialog
     */
    public ImageCompressionDialog getDialog() {
        return this.dialog;
    }

    /**
     * Compresses a frame that is sourced by a PixMap from the supplied QDGraphics, returning
     * the compressed data.
     * @see "QuickTime::SCCompressImage()"
     * @param src the source PixMap from the QDGraphics.
     * @param srcRect you can specify to compress just a part of the pix map by specifying
     *  a rectangle - or pass in null to compress the entire pix map.
     * @return an image data object.
     * @throws quicktime.QTException
     */
    public SCInfo compressFrame(QDGraphics src, QDRect srcRect) throws QTException {
        return QTHandleRef.fromSCSequence(this, src, srcRect);
    }

    private void dispose() throws Throwable {
        SCCompressSequenceEnd(QTObject.ID(this.dialog));
    }

    private static int SCCompressSequenceBegin(int ci, int src, byte[] srcRect, int[] desc) {
        return ImageCompressionManager.SCCompressSequenceBegin(ci, src, srcRect, desc);
    }

    private static int SCCompressSequenceEnd(int ci) {
        return ImageCompressionManager.SCCompressSequenceEnd(ci);
    }

    private static void SetGWorld(int port, int gdh) {
        QuickDrawManager.SetGWorld(port, gdh);
    }

    private static void GetGWorld(int[] port, int[] gdh) {
        QuickDrawManager.GetGWorld(port, gdh);
    }
}
