
package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.image.Compressor;
import quicktime.std.image.ImageDescription;
import quicktime.std.movies.media.SampleDescription;
import quicktime.util.QTUtils;


public final class SGVideoChannel extends VisualChannel implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SGVideoChannel.class);
                    }
                });
            }
        }.establish();
    }

    SGVideoChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
    }

    public SGVideoChannel(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramSequenceGrabber, 1986618469);
    }

    final SampleDescription makeDescription() throws QTException {
        return new ImageDescription(0);
    }

    public final ImageDescription getImageDescription() throws QTException {
        return (ImageDescription) getSampleDescription();
    }

    public void setCompressorType(int paramInt) throws StdQTException {
        int i = SGSetVideoCompressorType(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    public int getCompressorType() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SGGetVideoCompressorType(_ID(), arrayOfInt);
        StdQTException.checkError(i);

        return arrayOfInt[0];
    }

    /**
     * @deprecated
     */
    public void setCompressor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) throws StdQTException {
        int i = SGSetVideoCompressor(_ID(), (short) paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
        StdQTException.checkError(i);
    }

    public void setCompressor(int paramInt1,
                              Compressor paramCompressor,
                              int paramInt2,
                              int paramInt3,
                              int paramInt4) throws StdQTException {
        int i = SGSetVideoCompressor(_ID(), (short) paramInt1, QTObject.ID(paramCompressor), paramInt2, paramInt3, paramInt4);
        StdQTException.checkError(i);
    }

    public SGVideoCompressorInfo getCompressor() throws StdQTException {
        return Compressor.fromSGVideoChannel(this);
    }

    public VideoDigitizer getDigitizerComponent() {
        int i = SGGetVideoDigitizerComponent(_ID());
        return VideoDigitizer.makeVideoDigitizer(i, this);
    }

    public void setDigitizerComponent(VideoDigitizer paramVideoDigitizer) throws StdQTException {
        StdQTException.checkError(SGSetVideoDigitizerComponent(_ID(), QTObject.ID(paramVideoDigitizer)));
    }

    public void digitizerChanged() throws StdQTException {
        StdQTException.checkError(SGVideoDigitizerChanged(_ID()));
    }

    public void setVideoRect(QDRect paramQDRect) throws StdQTException {
        int i = SGSetVideoRect(_ID(), paramQDRect.getRect());
        StdQTException.checkError(i);
    }

    public QDRect getVideoRect() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = SGGetVideoRect(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    public QDRect getSrcVideoBounds() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = SGGetSrcVideoBounds(_ID(), localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    public void setFrameRate(float paramFloat) throws StdQTException {
        int i = SGSetFrameRate(_ID(), QTUtils.X2Fix(paramFloat));
        StdQTException.checkError(i);
    }

    public float getFrameRate() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = SGGetFrameRate(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    private static native int SGSetVideoCompressorType(int paramInt1, int paramInt2);

    private static native int SGGetVideoCompressorType(int paramInt, int[] paramArrayOfInt);

    private static native int SGSetVideoCompressor(int paramInt1,
                                                   short paramShort,
                                                   int paramInt2,
                                                   int paramInt3,
                                                   int paramInt4,
                                                   int paramInt5);

    private static native int SGGetVideoDigitizerComponent(int paramInt);

    private static native int SGSetVideoDigitizerComponent(int paramInt1, int paramInt2);

    private static native int SGVideoDigitizerChanged(int paramInt);

    private static native int SGSetVideoRect(int paramInt, byte[] paramArrayOfByte);

    private static native int SGGetVideoRect(int paramInt, byte[] paramArrayOfByte);

    private static native int SGGetSrcVideoBounds(int paramInt, byte[] paramArrayOfByte);

    private static native int SGSetFrameRate(int paramInt1, int paramInt2);

    private static native int SGGetFrameRate(int paramInt, int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/sg/
 * SGVideoChannel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
