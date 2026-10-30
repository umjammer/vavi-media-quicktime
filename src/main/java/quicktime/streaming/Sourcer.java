package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.StreamingManager;
import quicktime.jdirect.QTStreamingLib;
import quicktime.std.comp.Component;
import quicktime.std.comp.ComponentDescription;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.util.QTPointer;


/**
 * The Sourcer class implements the corresponding data structure of the QTSSourcer component
 */
public class Sourcer extends Component implements QTStreamingLib {

    private static final Object owner = new Object();

    static {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to broadcast with security settings when class is signed");
        }
        if (QTSession.isCurrentOS(2)) {
            throw new RuntimeException("The broadcaster API is not supported on windows");
        }
    }

    Sourcer(int component) throws QTException {
        super(component);
        initialize(null);
    }

    /**
     * Creates a new Sourcer component from a specific component identifier
     * @param compID component identifier
     */
    public Sourcer(ComponentIdentifier compID) throws QTException {
        super(compID);
        initialize(null);
    }

    /**
     * Creates a new sourcer based on type, subType and manufacturer type
     * @param sourcerType integer representing type (OSType) of sourcer
     * @param subType integer representing subtype (OSType) of sourcer
     * @param manufacturerType integer representing manufacturer type (OSType) of sourcer
     */
    public Sourcer(int sourcerType, int subType, int manufacturerType) throws QTException {
        super(allocate(sourcerType, subType, manufacturerType));
        initialize(null);
    }

    /**
     * Creates a new sourcer based on type, subType, manufacturer type, and optional init parameters
     * @param sourcerType integer representing type (OSType) of sourcer
     * @param subType integer representing subtype (OSType) of sourcer
     * @param manufacturerType integer representing manufacturer type (OSType) of sourcer
     * @param initParams sourcer initialization parameters
     */
    public Sourcer(int sourcerType, int subType, int manufacturerType, SourcerInitParams initParams) throws QTException {
        super(allocate(sourcerType, subType, manufacturerType));
        initialize(initParams);
    }

    private static int allocate(int type, int subType, int manufacturer) throws QTException {
        ComponentDescription desc = new ComponentDescription(type);
        desc.setSubType(subType);
        desc.setManufacturer(manufacturer);
        ComponentIdentifier componentIdentifier = ComponentIdentifier.find(desc);
        return QTObject.ID(componentIdentifier);
    }

    protected void initialize(SourcerInitParams inInitParams) throws QTException {
        if (inInitParams == null) {
            QTException.checkError(QTSSourcerInitialize(_ID(), 0));
        } else {
            QTException.checkError(QTSSourcerInitialize(_ID(), QTObject.ID(inInitParams)));
        }
    }

    /**
     * General purpose routine for setting information associated with the Sourcer
     * @param selector the selector specifying the type of information to be set
     * @param data data to be set
     *  For specific information setting routine, see the more specific function calls
     * @throws quicktime.QTException
     * @see #setCallbackParams
     * @see #setLoopParams
     * @see "QuickTime::QTSSourcerSetInfo"
     */
    public void setInfo(int selector, QTPointer data) throws QTException {
        QTException.checkError(QTSSourcerSetInfo(_ID(), selector, QTObject.ID(data)));
    }

    /**
     * General purpose routine for retrieving information associated with the Sourcer
     * @param selector the selector specifying the type of information to be set
     * @return retrieved data
     *  For specific information accessor routine, see the more specific function calls
     * @throws quicktime.QTException
     * @see #getCallbackParams
     * @see #getLoopParams
     * @see "QuickTime::QTSSourcerGetInfo"
     */
    public QTPointer getInfo(int selector) throws QTException {
        byte[] ioParams = { 0 };
        QTException.checkError(QTSSourcerGetInfo(_ID(), selector, ioParams));
        return new QTPointer(ioParams);
    }

    /**
     * Sets the callback parameters associated with the sourcer
     * @param callbackParams the sourcer callback params object specifying callback parameters
     * @see " QuickTime::QTSSourcerSetInfo"
     * @throws quicktime.QTException
     */
    public void setCallbackParams(SourcerCallbackParams callbackParams) throws QTException {
        QTException.checkError(QTSSourcerSetInfo(_ID(), 1935893104, QTObject.ID(callbackParams)));
    }

    /**
     * Gets the callback parameters associated with the sourcer
     * @return sourcer callback params object specifying callback parameters
     * @see " QuickTime::QTSSourcerGetInfo"
     * @throws quicktime.QTException
     */
    public SourcerCallbackParams getCallbackParams() throws QTException {
        byte[] ioParams = { 0 };
        QTException.checkError(QTSSourcerGetInfo(_ID(), 1935893104, ioParams));
        return new SourcerCallbackParams(ioParams);
    }

    /**
     * Sets the loop parameters associated with the sourcer
     * @param loopParams the sourcer loop params object specifying loop settings
     * @see " QuickTime::QTSSourcerSetInfo"
     * @throws quicktime.QTException
     */
    public void setLoopParams(SourcerLoopParams loopParams) throws QTException {
        QTException.checkError(QTSSourcerSetInfo(_ID(), 1819242352, QTObject.ID(loopParams)));
    }

    /**
     * Gets the loop parameters associated with the sourcer
     * @return sourcer loop params object specifying loop settings
     * @see " QuickTime::QTSSourcerGetInfo"
     * @throws quicktime.QTException
     */
    public SourcerLoopParams getLoopParams() throws QTException {
        byte[] ioParams = { 0 };
        QTException.checkError(QTSSourcerGetInfo(_ID(), 1819242352, ioParams));
        return new SourcerLoopParams(ioParams);
    }

    /**
     * Sets the track parameters associated with the sourcer
     * @param SourcerTrackParams the track params object specifying track settings
     * @see " QuickTime::QTSSourcerSetInfo"
     * @throws quicktime.QTException
     */
    public void setSourcerTrackParams(SourcerTrackParams SourcerTrackParams) throws QTException {
        QTException.checkError(QTSSourcerSetInfo(_ID(), 1953653099, QTObject.ID(SourcerTrackParams)));
    }

    /**
     * Gets the track parameters associated with the sourcer
     * @return track params object specifying track settings
     * @see " QuickTime::QTSSourcerGetInfo"
     * @throws quicktime.QTException
     */
    public SourcerTrackParams getSourcerTrackParams() throws QTException {
        byte[] ioParams = { 0 };
        QTException.checkError(QTSSourcerGetInfo(_ID(), 1953653099, ioParams));
        return new SourcerTrackParams(ioParams);
    }

    /**
     * Idles the sourcer object
     * @param inTime -
     * @param flags idle flags
     * @see " QuickTime::QTSSourcerIdle"
     * @throws quicktime.QTException
     */
    public int idle(long inTime, int flags) throws QTException {
        int[] outFlags = { 0 };
        QTException.checkError(QTSSourcerIdle(_ID(), inTime, flags, outFlags));
        return outFlags[0];
    }

    /**
     * Enables (or disables) the sourcer object
     * @param enabled true is the sourcer is enabled
     * @see " QuickTime::QTSSourcerSetEnable"
     * @throws quicktime.QTException
     */
    public void setEnable(boolean enabled) throws QTException {
        setEnable(enabled, 0);
    }

    /**
     * Enables (or disables) the sourcer object
     * @param enabled true is the sourcer is enabled
     * @param flags enable flags for the sourcer
     * @see " QuickTime::QTSSourcerSetEnable"
     * @throws quicktime.QTException
     */
    public void setEnable(boolean enabled, int flags) throws QTException {
        QTException.checkError(QTSSourcerSetEnable(_ID(), (byte) (enabled == true ? 1 : 0), flags));
    }

    /**
     * Returns the enabled state of the sourcer
     * @return true if the sourcer is enabled, false otherwise
     * @see " QuickTime::QTSSourcerGetEnable"
     * @throws quicktime.QTException
     */
    public boolean isEnable() throws QTException {
        return isEnable(0);
    }

    /**
     * Returns the enabled state of the sourcer
     * @param flags flags
     * @return true if the sourcer is enabled, false otherwise
     * @see " QuickTime::QTSSourcerGetEnable"
     * @throws quicktime.QTException
     */
    public boolean isEnable(int flags) throws QTException {
        byte[] enableMode = { 0 };
        QTException.checkError(QTSSourcerGetEnable(_ID(), enableMode, flags));
        return enableMode[0] != 0;
    }

    /**
     * Sets the time scale of the sourcer object
     * @param scale the scale of the sourcer
     * @see " QuickTime::QTSSourcerSetTimeScale"
     * @throws quicktime.QTException
     */
    public void setTimeScale(int scale) throws QTException {
        QTException.checkError(QTSSourcerSetTimeScale(_ID(), scale));
    }

    /**
     * Gets the time scale of the sourcer object
     * @return the scale of the sourcer
     * @see " QuickTime::QTSSourcerGetTimeScale"
     * @throws quicktime.QTException
     */
    public int getTimeScale() throws QTException {
        int[] timeScale = { 0 };
        QTException.checkError(QTSSourcerGetTimeScale(_ID(), timeScale));
        return timeScale[0];
    }

    public static int QTSSourcerInitialize(int sourcer, int inInitParams) {
        return StreamingManager.QTSSourcerInitialize(sourcer, inInitParams);
    }

    public static int QTSSourcerSetInfo(int sourcer, int selector, int ioParams) {
        return StreamingManager.QTSSourcerSetInfo(sourcer, selector, ioParams);
    }

    public static int QTSSourcerGetInfo(int sourcer, int selector, byte[] ioParams) {
        return StreamingManager.QTSSourcerGetInfo(sourcer, selector, ioParams);
    }

    public static int QTSSourcerIdle(int sourcer, long inTime, int inFlags, int[] outFlags) {
        return StreamingManager.QTSSourcerIdle(sourcer, inTime, inFlags, outFlags);
    }

    public static int QTSSourcerSetEnable(int sourcer, byte enableMode, int flags) {
        return StreamingManager.QTSSourcerSetEnable(sourcer, enableMode, flags);
    }

    public static int QTSSourcerGetEnable(int sourcer, byte[] enableMode, int flags) {
        return StreamingManager.QTSSourcerGetEnable(sourcer, enableMode, flags);
    }

    public static int QTSSourcerSetTimeScale(int sourcer, int timeScale) {
        return StreamingManager.QTSSourcerSetTimeScale(sourcer, timeScale);
    }

    public static int QTSSourcerGetTimeScale(int sourcer, int[] timeScale) {
        return StreamingManager.QTSSourcerGetTimeScale(sourcer, timeScale);
    }
}
