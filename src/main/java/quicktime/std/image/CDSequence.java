package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDGraphics;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;

/**
 * This class defines methods which are used during compression and decompression of
 * image sequences.
 * @see {@link quicktime.std.image.CSequence},
 * {@link quicktime.std.image.DSequence}
 */
public class CDSequence extends QTObject implements QuickTimeLib {

    private static Object linkage;

    ImageDescription mImageDescription;

    protected QDGraphics cdPort;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(CDSequence.class);
                    }
                });
            }
        }.establish();
    }

    CDSequence(int paramInt, QDGraphics paramQDGraphics) throws QTException {
        super(paramInt);
        this.cdPort = paramQDGraphics;
    }

    /**
     * Returns the ImageDescription object associated with this sequence.
     * @return the image description
     */
    public final ImageDescription getDescription() {
        return this.mImageDescription;
    }

    /**
     * Set the time base for the CDSequence object.
     * QuickTime::CDSequenceSetTimeBase
     * @param base the TimeBase for the sequence
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setTimeBase(TimeBase paramTimeBase) throws StdQTException {
        StdQTException.checkError(CDSequenceSetTimeBase(_ID(), QTObject.ID(paramTimeBase)));
    }

    /**
     * Your application may call this method to check the status of an asynchronous
     * compression or decompression operation.
     * QuickTime::CDSequenceBusy
     * @return true if an asynchronous operation is in progress, false otherwise
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean busy() throws StdQTException {
        int i = CDSequenceBusy(_ID());
        if (i < 0)
            StdQTException.checkError(i);
        return i != 0;
    }

    /**
     * Reports whether two image descriptions are the same.
     * QuickTime::CDSequenceEquivalentImageDescription
     * @param newDesc an ImageDescription object that describes a compressed image
     * @return true if the descriptions are equivalent, false otherwise
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean equivalentImageDescription(ImageDescription paramImageDescription) throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        StdQTException.checkError(CDSequenceEquivalentImageDescription(_ID(), QTObject.ID(paramImageDescription), arrayOfByte));
        return arrayOfByte[0] != 0;
    }

    private static native short CDSequenceSetTimeBase(int paramInt1, int paramInt2);

    private static native short CDSequenceBusy(int paramInt);

    private static native short CDSequenceEquivalentImageDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte);
}
