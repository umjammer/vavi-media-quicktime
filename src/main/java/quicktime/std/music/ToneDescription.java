package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import com.apple.mrj.macos.carbon.CarbonAccess;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.MusicManager;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;


/**
 * Provides the information needed to produce a specific musical sound.
 */
public final class ToneDescription extends QTByteObject implements QuickTimeLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 76;

    @Serial
    private static final long serialVersionUID = 2107309866399025424L;

    /**
     * Creates a ToneDescription object with no initial settings.
     */
    public ToneDescription() {
        super(kNativeSize);
    }

    /**
     * Initializes the ToneDescription instance with the appropriate
     * instrument based on the specified gmNumber.
     * @see "QuickTime::NAStuffToneDescription"
     * @param gmNumber instrument number
     */
    public ToneDescription(int gmNumber) throws QTException {
        this(NoteAllocator.getDefault(), gmNumber);
    }

    /**
     * Initializes the ToneDescription instance with the appropriate
     * instrument based on the specified gmNumber.
     * @see "QuickTime::NAStuffToneDescription"
     * @param na the NoteAllocator component instance to use
     * @param gmNumber instrument number
     */
    public ToneDescription(NoteAllocator na, int gmNumber) throws StdQTException {
        this();
        stuff(na, gmNumber);
    }

    private ToneDescription(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[76];
        in.read(getBytes());
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.write(getBytes());
    }

    /**
     * Returns the current OSType of the described instrument's synthesizer.
     * @return the OSType
     */
    public int getSynthesizerType() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(0));
    }

    /**
     * Sets the current OSType of the described instrument's synthesizer.
     * @param type the OSType
     */
    public void setSynthesizerType(int type) {
        setIntAt(0, EndianOrder.flipNativeToBigEndian32(type));
    }

    /**
     * Returns the current name of the described instrument's synthesizer.
     * @return the Synthesizer name
     * @throws quicktime.util.UtilException
     */
    public String getSynthesizerName() throws UtilException {
        return getPStringAt(4);
    }

    /**
     * Sets the current name of the described instrument's synthesizer.
     * @param name the Synthesizer name
     * @throws quicktime.util.UtilException
     */
    public void setSynthesizerName(String name) throws UtilException {
        setPStringAt(4, 31, name);
    }

    /**
     * Returns the current name of the described instrument.
     * @return the instrumentName
     * @throws quicktime.util.UtilException
     */
    public String getInstrumentName() throws UtilException {
        return getPStringAt(36);
    }

    /**
     * Sets the current name of the described instrument.
     * @param name the instrumentName
     * @throws quicktime.util.UtilException
     */
    public void setInstrumentName(String name) throws UtilException {
        setPStringAt(36, 31, name);
    }

    /**
     * Returns the current number of the described instrument.
     * @return the instrumentNumber
     */
    public int getInstrumentNumber() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(68));
    }

    /**
     * Sets the current number of the described instrument.
     * @param num the instrumentNumber
     */
    public void setInstrumentNumber(int num) {
        setIntAt(68, EndianOrder.flipNativeToBigEndian32(num));
    }

    /**
     * Returns the current gm Number of the described instrument's synthesizer.
     * @return the gm Number
     */
    public int getGMNumber() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(72));
    }

    /**
     * Sets the current gm Number of the described instrument's synthesizer.
     * @param gmNumber the gm Number
     */
    public void setGMNumber(int gmNumber) {
        setIntAt(72, EndianOrder.flipNativeToBigEndian32(gmNumber));
    }

    /**
     * Initializes the calling ToneDescription instance with the details of the
     * note channel specified by the gmNumber.
     * @see "QuickTime::NAStuffToneDescription"
     * @param na the Note Allocator component to use
     * @param gmNumber instrument number
     * @throws quicktime.std.StdQTException
     */
    public void stuff(NoteAllocator na, int gmNumber) throws StdQTException {
        StdQTException.checkError(NAStuffToneDescription(QTObject.ID(na), gmNumber, getBytes()));
    }

    /**
     * Initializes the tone description structure through providing the user with a dialog
     * that allows the user to choose the instrument.
     * @see "QuickTime::NAPickInstrument"
     * @param na the Note Allocator component to use
     * @param prompt Dialog box prompt "New Instrument..."
     * @param flags flags to limit user options
     * @throws quicktime.std.StdQTException
     */
    public void pickInstrument(NoteAllocator na, String prompt, int flags) throws StdQTException {
        int i = 0;
        if (QTSession.isCurrentOS(4)) {
            i = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                int i1 = 0;
                synchronized (QTNative.globalsLock) {
                    i1 = ToneDescription.NAPickInstrument(QTObject.ID(na), 0, QTUtils.String2PString(prompt, 255), ToneDescription.this.getBytes(), flags, 0, 0, 0);
                }
                return i1;
            });
            StdQTException.checkError(i);
        } else {
            synchronized (QTNative.globalsLock) {
                i = NAPickInstrument(QTObject.ID(na), 0, QTUtils.String2PString(prompt, 255), getBytes(), flags, 0, 0, 0);
            }
            StdQTException.checkError(i);
        }
    }

    int res = 0;

    /**
     * Returns a string representation of this object
     */
    public String toString() {
        String instrumentName = "";
        try {
            instrumentName = getInstrumentName();
        } catch (UtilException _) {
        }
        return getClass().getName() + "[synthType=" + QTUtils.fromOSType(getSynthesizerType()) + ",instNum=" + getInstrumentNumber() + ",gmNum=" + getGMNumber() + ",instName=" + instrumentName + "]";
    }

    /**
     * Makes a copy of a ToneDescription object.
     */
    @Override
    public Object clone() {
        return new ToneDescription(getBytes());
    }

    private static int NAStuffToneDescription(int na, int gmNumber, byte[] td) {
        return MusicManager.NAStuffToneDescription(na, gmNumber, td);
    }

    private static int NAPickInstrument(int na, int filterProc, byte[] prompt, byte[] sd, int flags, int refCon, int reserved1, int reserved2) {
        return MusicManager.NAPickInstrument(na, filterProc, prompt, sd, flags, refCon, reserved1, reserved2);
    }

    private static int GetMainEventLoop() {
        return PlatformManager.GetMainEventLoop();
    }
}
