package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;
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

    private static Object linkage;

    private final ImageDescription desc;

    private final ImageCompressionDialog dialog;

    static {
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(SCSequence.class));
            }
        }
        new PrivelegedAction().establish();
    }

    SCSequence(ImageCompressionDialog paramImageCompressionDialog, QDGraphics paramQDGraphics, QDRect paramQDRect) throws QTException {
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int[] arrayOfInt3 = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(arrayOfInt1, arrayOfInt2);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = SCCompressSequenceBegin(QTObject.ID(paramImageCompressionDialog), QTObject.ID(paramQDGraphics.getPixMap()), paramQDRect != null ? paramQDRect.getRect() : null, arrayOfInt3);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
        }
        StdQTException.checkError(i);
        this.dialog = paramImageCompressionDialog;
        this.desc = new ImageDescription(arrayOfInt3[0], this) {
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
    public SCInfo compressFrame(QDGraphics paramQDGraphics, QDRect paramQDRect) throws QTException {
        return QTHandleRef.fromSCSequence(this, paramQDGraphics, paramQDRect);
    }

    @Override
    protected final void finalize() throws Throwable {
        SCCompressSequenceEnd(QTObject.ID(this.dialog));
        super.finalize();
    }

    private static int SCCompressSequenceBegin(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return ImageCompressionManager.SCCompressSequenceBegin(paramInt1, paramInt2, paramArrayOfByte, paramArrayOfInt);
    }

    private static int SCCompressSequenceEnd(int paramInt) {
        return ImageCompressionManager.SCCompressSequenceEnd(paramInt);
    }

    private static void SetGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }
}

