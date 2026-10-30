package quicktime.std.image;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ClockManager;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDGraphics;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;


/**
 * This class defines methods which are used during compression and decompression of
 * image sequences.
 * @see quicktime.std.image.CSequence,
 * {@link quicktime.std.image.DSequence}
 */
public class CDSequence extends QTObject implements QuickTimeLib {

    ImageDescription mImageDescription;

    protected final QDGraphics cdPort;

    CDSequence(int nativeRep, QDGraphics cdPort) throws QTException {
        super(nativeRep);
        this.cdPort = cdPort;
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
     * @see "QuickTime::CDSequenceSetTimeBase"
     * @param base the TimeBase for the sequence
     * @throws quicktime.std.StdQTException
     */
    public final void setTimeBase(TimeBase base) throws StdQTException {
        StdQTException.checkError(CDSequenceSetTimeBase(_ID(), QTObject.ID(base)));
    }

    /**
     * Your application may call this method to check the status of an asynchronous
     * compression or decompression operation.
     * @see "QuickTime::CDSequenceBusy"
     * @return true if an asynchronous operation is in progress, false otherwise
     * @throws quicktime.std.StdQTException
     */
    public boolean busy() throws StdQTException {
        int i = CDSequenceBusy(_ID());
        if (i < 0)
            StdQTException.checkError(i);
        return i != 0;
    }

    /**
     * Reports whether two image descriptions are the same.
     * @see "QuickTime::CDSequenceEquivalentImageDescription"
     * @param newDesc an ImageDescription object that describes a compressed image
     * @return true if the descriptions are equivalent, false otherwise
     * @throws quicktime.std.StdQTException
     */
    public final boolean equivalentImageDescription(ImageDescription newDesc) throws StdQTException {
        byte[] equivalent = new byte[1];
        StdQTException.checkError(CDSequenceEquivalentImageDescription(_ID(), QTObject.ID(newDesc), equivalent));
        return equivalent[0] != 0;
    }

    private static short CDSequenceSetTimeBase(int sequenceID, int timeBase) {
        return ClockManager.CDSequenceSetTimeBase(sequenceID, timeBase);
    }

    private static short CDSequenceBusy(int seqID) {
        return ImageCompressionManager.CDSequenceBusy(seqID);
    }

    private static short CDSequenceEquivalentImageDescription(int seqID, int newDesc, byte[] equivalent) {
        return ImageCompressionManager.CDSequenceEquivalentImageDescription(seqID, newDesc, equivalent);
    }
}
