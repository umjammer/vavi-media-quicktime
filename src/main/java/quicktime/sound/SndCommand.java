package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;

/**
 * This class represents a SndCommand as used by SndChannel's doCommand() and doImmediate methods.
 * There are various convenience methods added BUT you must make sure that the cmd field has been
 * set appropriately before using those methods - those calls fail silently if you do the wrong thing.
 */
public final class SndCommand extends QTPointerRef implements PrimitivesLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 8;

    private ExtScheduledSoundHeader keepBuffer;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SndCommand.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Creates a SndCommand object and performs initialization
     * @param cmd the sound command (from StdQTConstants)
     */
    public SndCommand(int paramInt) throws QTException {
        super(8, true);
        setCmd(paramInt);
    }

    /**
     * Creates a SndCommand object and performs initialization
     * @param cmd the sound command (from SoundConstants)
     * @param param1 optional command parameter
     * @param param2 optional command parameter
     */
    public SndCommand(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        super(8, true);
        setCmd(paramInt1);
        setParam1(paramInt2);
        setParam2(paramInt3);
    }

    /**
     * Sets the command.
     * @param cmd the sound command (from StdQTConstants)
     */
    public void setCmd(int paramInt) {
        setShortAt(0, (short) paramInt);
    }

    /**
     * Retrieves the command.
     * @return cmd the sound command
     */
    public int getCmd() {
        return QTUtils.UShort2Int(getShortAt(0));
    }

    /**
     * Sets the first optional parameter.
     * @param param1 optional command parameter
     */
    public void setParam1(int paramInt) {
        setShortAt(2, (short) paramInt);
    }

    /**
     * Retrieves the first optional parameter.
     * @return optional command parameter
     */
    public int getParam1() {
        return getShortAt(2);
    }

    /**
     * Sets the second optional parameter.
     * @param param2 optional command parameter
     */
    public void setParam2(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * Retrieves the second optional parameter.
     * @return optional command parameter
     */
    public int getParam2() {
        return getIntAt(4);
    }

    public void setBuffer(ExtScheduledSoundHeader paramExtScheduledSoundHeader) {
        int i = getCmd();
        if ((i == 81) || (i == 80) || (i == 52)) {
            setIntAt(4, QTObject.ID(paramExtScheduledSoundHeader));
            this.keepBuffer = paramExtScheduledSoundHeader;
        }
    }

    public ExtScheduledSoundHeader getBuffer() {
        int i = getCmd();
        if ((i == 81) || (i == 80) || (i == 52)) {
            return this.keepBuffer;
        }
        return null;
    }

    /**
     * Gets the value of the right volume IFF the cmd field is volumeCmd
     * otherwise this call returns.
     */
    public void setVolumes(float paramFloat1, float paramFloat2) {
        int i = getCmd();
        if (i != 46) {
            return;
        }
        int j = QTUtils.X2ShortFix(paramFloat2) << 16;
        int k = QTUtils.X2ShortFix(paramFloat1);
        setIntAt(4, j | k);
    }

    /**
     * Gets the value of the right volume IFF the cmd field is getVolumeCmd or volumeCmd
     * otherwise this call returns 0.
     * @return the right volume value
     */
    public float getRightVolume() {
        int i = getCmd();
        int j = getParam2();
        if (i == 47) {
            int k = getIntFromPointer(j, 0);
            return QTUtils.ShortFix2X((short) (k >>> 16));
        }
        if (i == 46) {
            return QTUtils.ShortFix2X((short) (j >>> 16));
        }
        return 0.0F;
    }

    /**
     * Gets the value of the left volume IFF the cmd field is getVolumeCmd or volumeCmd
     * otherwise this call returns 0.
     * @return the right volume value
     */
    public float getLeftVolume() {
        int i = getCmd();
        int j = getParam2();
        if (i == 47) {
            int k = getIntFromPointer(j, 0);
            return QTUtils.ShortFix2X((short) k);
        }
        if (i == 46) {
            return QTUtils.ShortFix2X((short) j);
        }
        return 0.0F;
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[cmd=" + getCmd() + ",param1=" + getParam1() + ",param2=" + getParam2() + "]";
    }

    private static native short getShortFromPointer(int paramInt1, int paramInt2);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);
}

