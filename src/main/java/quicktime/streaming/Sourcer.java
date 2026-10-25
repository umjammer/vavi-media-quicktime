package quicktime.streaming;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.StreamingManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QTStreamingLib;
import quicktime.std.comp.Component;
import quicktime.std.comp.ComponentDescription;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.util.QTPointer;

/**
 * The Sourcer class implements the corresponding data structure of the QTSSourcer component
 */
public class Sourcer extends Component implements QTStreamingLib {

    private static Object linkage;

    private static Object owner = new Object();

    static {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to broadcast with security settings when class is signed");
        }
        if (QTSession.isCurrentOS(2)) {
            throw new RuntimeException("The broadcaster API is not supported on windows");
        }
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(Sourcer.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    Sourcer(int paramInt) throws QTException {
        super(paramInt);
        initialize(null);
    }

    /**
     * Creates a new Sourcer component from a specific component identifier
     * @param compID component identifier
     */
    public Sourcer(ComponentIdentifier paramComponentIdentifier) throws QTException {
        super(paramComponentIdentifier);
        initialize(null);
    }

    /**
     * Creates a new sourcer based on type, subType and manufacturer type
     * @param sourcerType integer representing type (OSType) of sourcer
     * @param subType integer representing subtype (OSType) of sourcer
     * @param manufacturerType integer representing manufacturer type (OSType) of sourcer
     */
    public Sourcer(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        super(allocate(paramInt1, paramInt2, paramInt3));
        initialize(null);
    }

    /**
     * Creates a new sourcer based on type, subType, manufacturer type, and optional init parameters
     * @param sourcerType integer representing type (OSType) of sourcer
     * @param subType integer representing subtype (OSType) of sourcer
     * @param manufacturerType integer representing manufacturer type (OSType) of sourcer
     * @param initParams sourcer initialization parameters
     */
    public Sourcer(int paramInt1, int paramInt2, int paramInt3, SourcerInitParams paramSourcerInitParams) throws QTException {
        super(allocate(paramInt1, paramInt2, paramInt3));
        initialize(paramSourcerInitParams);
    }

    private static int allocate(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        ComponentDescription localComponentDescription = new ComponentDescription(paramInt1);
        localComponentDescription.setSubType(paramInt2);
        localComponentDescription.setManufacturer(paramInt3);
        ComponentIdentifier localComponentIdentifier = ComponentIdentifier.find(localComponentDescription);
        return QTObject.ID(localComponentIdentifier);
    }

    protected void initialize(SourcerInitParams paramSourcerInitParams) throws QTException {
        if (paramSourcerInitParams == null) {
            QTException.checkError(QTSSourcerInitialize(_ID(), 0));
        } else {
            QTException.checkError(QTSSourcerInitialize(_ID(), QTObject.ID(paramSourcerInitParams)));
        }
    }

    /**
     * General purpose routine for setting information associated with the Sourcer
     * @param selector the selector specifying the type of information to be set
     * @param data data to be set
     *  For specific information setting routine, see the more specific function calls
     * @param {@link quicktime.QTException}
     * @see setCallbackParams,
     * setLoopParams,
     * QuickTime::QTSSourcerSetInfo
     */
    public void setInfo(int paramInt, QTPointer paramQTPointer) throws QTException {
        QTException.checkError(QTSSourcerSetInfo(_ID(), paramInt, QTObject.ID(paramQTPointer)));
    }

    /**
     * General purpose routine for retrieving information associated with the Sourcer
     * @param selector the selector specifying the type of information to be set
     * @return retrieved data
     *  For specific information accessor routine, see the more specific function calls
     * @return {@link quicktime.QTException}
     * @see getCallbackParams,
     * getLoopParams,
     * QuickTime::QTSSourcerGetInfo
     */
    public QTPointer getInfo(int paramInt) throws QTException {
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSSourcerGetInfo(_ID(), paramInt, arrayOfByte));
        return new QTPointer(arrayOfByte);
    }

    /**
     * Sets the callback parameters associated with the sourcer
     * @param callbackParams the sourcer callback params object specifying callback parameters
     *  QuickTime::QTSSourcerSetInfo
     * @param {@link quicktime.QTException}
     */
    public void setCallbackParams(SourcerCallbackParams paramSourcerCallbackParams) throws QTException {
        QTException.checkError(QTSSourcerSetInfo(_ID(), 1935893104, QTObject.ID(paramSourcerCallbackParams)));
    }

    /**
     * Gets the callback parameters associated with the sourcer
     * @return sourcer callback params object specifying callback parameters
     *  QuickTime::QTSSourcerGetInfo
     * @return {@link quicktime.QTException}
     */
    public SourcerCallbackParams getCallbackParams() throws QTException {
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSSourcerGetInfo(_ID(), 1935893104, arrayOfByte));
        return new SourcerCallbackParams(arrayOfByte);
    }

    /**
     * Sets the loop parameters associated with the sourcer
     * @param loopParams the sourcer loop params object specifying loop settings
     *  QuickTime::QTSSourcerSetInfo
     * @param {@link quicktime.QTException}
     */
    public void setLoopParams(SourcerLoopParams paramSourcerLoopParams) throws QTException {
        QTException.checkError(QTSSourcerSetInfo(_ID(), 1819242352, QTObject.ID(paramSourcerLoopParams)));
    }

    /**
     * Gets the loop parameters associated with the sourcer
     * @return sourcer loop params object specifying loop settings
     *  QuickTime::QTSSourcerGetInfo
     * @return {@link quicktime.QTException}
     */
    public SourcerLoopParams getLoopParams() throws QTException {
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSSourcerGetInfo(_ID(), 1819242352, arrayOfByte));
        return new SourcerLoopParams(arrayOfByte);
    }

    /**
     * Sets the track parameters associated with the sourcer
     * @param SourcerTrackParams the track params object specifying track settings
     *  QuickTime::QTSSourcerSetInfo
     * @param {@link quicktime.QTException}
     */
    public void setSourcerTrackParams(SourcerTrackParams paramSourcerTrackParams) throws QTException {
        QTException.checkError(QTSSourcerSetInfo(_ID(), 1953653099, QTObject.ID(paramSourcerTrackParams)));
    }

    /**
     * Gets the track parameters associated with the sourcer
     * @return track params object specifying track settings
     *  QuickTime::QTSSourcerGetInfo
     * @return {@link quicktime.QTException}
     */
    public SourcerTrackParams getSourcerTrackParams() throws QTException {
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSSourcerGetInfo(_ID(), 1953653099, arrayOfByte));
        return new SourcerTrackParams(arrayOfByte);
    }

    /**
     * Idles the sourcer object
     * @param inTime -
     * @param flags idle flags
     *  QuickTime::QTSSourcerIdle
     * @param {@link quicktime.QTException}
     */
    public int idle(long paramLong, int paramInt) throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSSourcerIdle(_ID(), paramLong, paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Enables (or disables) the sourcer object
     * @param enabled true is the sourcer is enabled
     *  QuickTime::QTSSourcerSetEnable
     * @param {@link quicktime.QTException}
     */
    public void setEnable(boolean paramBoolean) throws QTException {
        setEnable(paramBoolean, 0);
    }

    /**
     * Enables (or disables) the sourcer object
     * @param enabled true is the sourcer is enabled
     * @param flags enable flags for the sourcer
     *  QuickTime::QTSSourcerSetEnable
     * @param {@link quicktime.QTException}
     */
    public void setEnable(boolean paramBoolean, int paramInt) throws QTException {
        QTException.checkError(QTSSourcerSetEnable(_ID(), (byte) (paramBoolean == true ? 1 : 0), paramInt));
    }

    /**
     * Returns the enabled state of the sourcer
     * @return true if the sourcer is enabled, false otherwise
     *  QuickTime::QTSSourcerGetEnable
     * @return {@link quicktime.QTException}
     */
    public boolean isEnable() throws QTException {
        return isEnable(0);
    }

    /**
     * Returns the enabled state of the sourcer
     * @param flags flags
     * @return true if the sourcer is enabled, false otherwise
     *  QuickTime::QTSSourcerGetEnable
     * @return {@link quicktime.QTException}
     */
    public boolean isEnable(int paramInt) throws QTException {
        byte[] arrayOfByte = { 0 };
        QTException.checkError(QTSSourcerGetEnable(_ID(), arrayOfByte, paramInt));
        return arrayOfByte[0] != 0;
    }

    /**
     * Sets the time scale of the sourcer object
     * @param scale the scale of the sourcer
     *  QuickTime::QTSSourcerSetTimeScale
     * @param {@link quicktime.QTException}
     */
    public void setTimeScale(int paramInt) throws QTException {
        QTException.checkError(QTSSourcerSetTimeScale(_ID(), paramInt));
    }

    /**
     * Gets the time scale of the sourcer object
     * @return the scale of the sourcer
     *  QuickTime::QTSSourcerGetTimeScale
     * @return {@link quicktime.QTException}
     */
    public int getTimeScale() throws QTException {
        int[] arrayOfInt = { 0 };
        QTException.checkError(QTSSourcerGetTimeScale(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    public static int QTSSourcerInitialize(int paramInt1, int paramInt2) {
        return StreamingManager.QTSSourcerInitialize(paramInt1, paramInt2);
    }

    public static int QTSSourcerSetInfo(int paramInt1, int paramInt2, int paramInt3) {
        return StreamingManager.QTSSourcerSetInfo(paramInt1, paramInt2, paramInt3);
    }

    public static int QTSSourcerGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return StreamingManager.QTSSourcerGetInfo(paramInt1, paramInt2, paramArrayOfByte);
    }

    public static int QTSSourcerIdle(int paramInt1, long paramLong, int paramInt2, int[] paramArrayOfInt) {
        return StreamingManager.QTSSourcerIdle(paramInt1, paramLong, paramInt2, paramArrayOfInt);
    }

    public static int QTSSourcerSetEnable(int paramInt1, byte paramByte, int paramInt2) {
        return StreamingManager.QTSSourcerSetEnable(paramInt1, paramByte, paramInt2);
    }

    public static int QTSSourcerGetEnable(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        return StreamingManager.QTSSourcerGetEnable(paramInt1, paramArrayOfByte, paramInt2);
    }

    public static int QTSSourcerSetTimeScale(int paramInt1, int paramInt2) {
        return StreamingManager.QTSSourcerSetTimeScale(paramInt1, paramInt2);
    }

    public static int QTSSourcerGetTimeScale(int paramInt, int[] paramArrayOfInt) {
        return StreamingManager.QTSSourcerGetTimeScale(paramInt, paramArrayOfInt);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /Sourcer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
