package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.StreamingManager;
import quicktime.jdirect.QTStreamingLib;
import quicktime.util.QTPointerRef;


/**
 * The Stream class represents a broadcast stream used by a Presentation object
 */
public final class Stream extends QTObject implements QTStreamingLib {

    private static final Object owner = new Object();

    static {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to broadcast with security settings when class is signed");
        }
        if (QTSession.isCurrentOS(2)) {
            throw new RuntimeException("The broadcaster API is not supported on windows");
        }
    }

    Stream(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * Create a new stream object
     *
     * @param pres     supervising Presentation object
     * @param dataType OSType specifying type of stream data
     * @param data     data
     * @see "QuickTime::QTSPresNewStream"
     */
    public Stream(Presentation pres, int dataType, QTPointerRef data) throws QTException {
        super(allocate(pres, dataType, data, 0));
    }

    /**
     * Create a new stream object
     *
     * @param pres     supervising Presentation object
     * @param dataType OSType specifying type of stream data
     * @param data     data
     * @param flags    stream flags
     * @see "QuickTime::QTSPresNewStream"
     */
    public Stream(Presentation pres, int dataType, QTPointerRef data, int flags) throws QTException {
        super(allocate(pres, dataType, data, flags));
    }

    private static int allocate(Presentation presentation, int dataType, QTPointerRef data, int flags) throws QTException {
        int[] arrayOfInt = {0};
        QTException.checkError(QTSPresNewStream(QTObject.ID(presentation), dataType, QTObject.ID(data), data.getSize(), flags, arrayOfInt[0]));
        return arrayOfInt[0];
    }

    /**
     * Retrieves the presentation object this stream is associated with
     *
     * @return Presentation the presentation object
     * @throws quicktime.QTException
     * @see " QuickTime::QTSGetStreamPresentation"
     */
    public Presentation getPresentation() throws QTException {
        return new Presentation(QTSGetStreamPresentation(_ID()), this);
    }

    private static short QTSPresNewStream(int presentation, int dataType, int data, int dataLength, int flags, int outStream) {
        return StreamingManager.QTSPresNewStream(presentation, dataType, data, dataLength, flags, outStream);
    }

    private static int QTSGetStreamPresentation(int stream) {
        return StreamingManager.QTSGetStreamPresentation(stream);
    }
}
