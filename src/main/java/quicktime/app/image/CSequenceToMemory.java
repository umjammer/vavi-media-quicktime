
package quicktime.app.image;

import java.util.Vector;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.qd.ColorTable;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.image.CSequence;
import quicktime.std.image.CodecComponent;
import quicktime.std.image.CompressedFrameInfo;
import quicktime.std.image.ImageDescription;
import quicktime.util.RawEncodedImage;


/**
 * @deprecated
 */
public class CSequenceToMemory {
    private Vector frames;

    private CSequence seq;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public CSequenceToMemory(int paramInt) {
        this.frames = new Vector(paramInt);
    }

    /**
     * @deprecated
     */
    public CSequenceToMemory(QDGraphics paramQDGraphics,
            QDRect paramQDRect,
            int paramInt1,
            int paramInt2,
            CodecComponent paramCodecComponent,
            int paramInt3,
            int paramInt4,
            int paramInt5,
            ColorTable paramColorTable,
            int paramInt6) throws QTException {
        this.seq = new CSequence(paramQDGraphics,
                                 paramQDRect,
                                 paramInt1,
                                 paramInt2,
                                 paramCodecComponent,
                                 paramInt3,
                                 paramInt4,
                                 paramInt5,
                                 paramColorTable,
                                 paramInt6);

        this.frames = new Vector(16);
    }

    /**
     * @deprecated
     */
    Vector getFrames() {
        return this.frames;
    }

    /**
     * @deprecated
     */
    public void begin(QDGraphics paramQDGraphics,
                      QDRect paramQDRect,
                      int paramInt1,
                      int paramInt2,
                      CodecComponent paramCodecComponent,
                      int paramInt3,
                      int paramInt4,
                      int paramInt5,
                      ColorTable paramColorTable,
                      int paramInt6) throws QTException {
        if (this.frames != null) {
            this.frames.removeAllElements();
        }
        this.seq = new CSequence(paramQDGraphics,
                                 paramQDRect,
                                 paramInt1,
                                 paramInt2,
                                 paramCodecComponent,
                                 paramInt3,
                                 paramInt4,
                                 paramInt5,
                                 paramColorTable,
                                 paramInt6);
    }

    /**
     * @deprecated
     */
    public int compressFrame(QDGraphics paramQDGraphics, QDRect paramQDRect, int paramInt) throws QTException {
        RawEncodedImage localRawEncodedImage = new RawEncodedImage(this.seq.getMaxCompressionSize(paramQDGraphics), true);

        CompressedFrameInfo localCompressedFrameInfo = this.seq.compressFrame(paramQDGraphics,
                                                                              paramQDRect,
                                                                              paramInt,
                                                                              localRawEncodedImage);

        localRawEncodedImage.setSize(localCompressedFrameInfo.getDataSize());

        try {
            int i = this.seq.getFrameNumber() - 1;

            if (i < this.frames.size()) {
                this.frames.setElementAt(localRawEncodedImage, i);
            } else if (i > this.frames.size()) {
                this.frames.setSize(i);
                this.frames.setElementAt(localRawEncodedImage, i);
            } else {
                this.frames.addElement(localRawEncodedImage);
            }
        } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
            throw new QTException(getClass().getName() + ": Unable to grow internal Vector object.");
        }

        return localCompressedFrameInfo.getSimilarity();
    }

    /**
     * @deprecated
     */
    public ImageDescription getDescription() throws QTException {
        return this.seq.getDescription();
    }

    /**
     * @deprecated
     */
    public CSequence getSequence() {
        return this.seq;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /CSequenceToMemory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
