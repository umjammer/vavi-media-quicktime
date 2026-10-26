package quicktime.std.music;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
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

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 76;

    static final long serialVersionUID = 2107309866399025424L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(ToneDescription.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Creates a ToneDescription object with no initial settings.
     */
    public ToneDescription() {
        super(kNativeSize);
    }

    /**
     * Initializes the ToneDescription instance with the appropriate
     * instrument based on the specified gmNumber.
     * QuickTime::NAStuffToneDescription
     * @param gmNumber instrument number
     */
    public ToneDescription(int paramInt) throws QTException {
        this(NoteAllocator.getDefault(), paramInt);
    }

    /**
     * Initializes the ToneDescription instance with the appropriate
     * instrument based on the specified gmNumber.
     * QuickTime::NAStuffToneDescription
     * @param na the NoteAllocator component instance to use
     * @param gmNumber instrument number
     */
    public ToneDescription(NoteAllocator paramNoteAllocator, int paramInt) throws StdQTException {
        this();
        stuff(paramNoteAllocator, paramInt);
    }

    private ToneDescription(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[76];
        paramObjectInputStream.read(getBytes());
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        paramObjectOutputStream.write(getBytes());
    }

    /**
     * Returns the current OSType of the described instrument's synthesiser.
     * @return the OSType
     */
    public final int getSynthesizerType() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(0));
    }

    /**
     * Sets the current OSType of the described instrument's synthesiser.
     * @param type the OSType
     */
    public final void setSynthesizerType(int paramInt) {
        setIntAt(0, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    /**
     * Returns the current name of the described instrument's synthesiser.
     * @return the Synthesizer name
     * @return {@link quicktime.util.UtilException}
     */
    public final String getSynthesizerName() throws UtilException {
        return getPStringAt(4);
    }

    /**
     * Sets the current name of the described instrument's synthesiser.
     * @param name the Synthesizer name
     * @param {@link quicktime.util.UtilException}
     */
    public final void setSynthesizerName(String paramString) throws UtilException {
        setPStringAt(4, 31, paramString);
    }

    /**
     * Returns the current name of the described instrument.
     * @return the instrumentName
     * @return {@link quicktime.util.UtilException}
     */
    public final String getInstrumentName() throws UtilException {
        return getPStringAt(36);
    }

    /**
     * Sets the current name of the described instrument.
     * @param name the instrumentName
     * @param {@link quicktime.util.UtilException}
     */
    public final void setInstrumentName(String paramString) throws UtilException {
        setPStringAt(36, 31, paramString);
    }

    /**
     * Returns the current number of the described instrument.
     * @return the instrumentNumber
     */
    public final int getInstrumentNumber() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(68));
    }

    /**
     * Sets the current number of the described instrument.
     * @param num the instrumentNumber
     */
    public final void setInstrumentNumber(int paramInt) {
        setIntAt(68, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    /**
     * Returns the current gm Number of the described instrument's synthesiser.
     * @return the gm Number
     */
    public final int getGMNumber() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(72));
    }

    /**
     * Sets the current gm Number of the described instrument's synthesiser.
     * @param gmNumber the gm Number
     */
    public final void setGMNumber(int paramInt) {
        setIntAt(72, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    /**
     * Initializes the calling ToneDescription instance with the details of the
     * note channel specified by the gmNumber.
     * QuickTime::NAStuffToneDescription
     * @param na the Note Allocator component to use
     * @param gmNumber instrument number
     * @param {@link quicktime.std.StdQTException}
     */
    public final void stuff(NoteAllocator paramNoteAllocator, int paramInt) throws StdQTException {
        StdQTException.checkError(NAStuffToneDescription(QTObject.ID(paramNoteAllocator), paramInt, getBytes()));
    }

    /**
     * Initialises the tone description structure through providing the user with a dialog
     * that allows the user to choose the instrument.
     * QuickTime::NAPickInstrument
     * @param na the Note Allocator component to use
     * @param prompt Dialog box prompt "New Instrument..."
     * @param flags flags to limit user options
     * @param {@link quicktime.std.StdQTException}
     */
    public final void pickInstrument(final NoteAllocator na, final String prompt, final int flags) throws StdQTException {
        int i = 0;
        if (QTSession.isCurrentOS(4)) {
            i = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                public Object invoke() {
                    int i = 0;
                    synchronized (QTNative.globalsLock) {
                        i = ToneDescription.NAPickInstrument(QTObject.ID(na), 0, QTUtils.String2PString(prompt, 255), ToneDescription.this.getBytes(), flags, 0, 0, 0);
                    }
                    return i;
                }
            })).intValue();
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
        String str = "";
        try {
            str = getInstrumentName();
        } catch (UtilException localUtilException) {
        }
        return getClass().getName() + "[synthType=" + QTUtils.fromOSType(getSynthesizerType()) + ",instNum=" + Integer.toString(getInstrumentNumber()) + ",gmNum=" + Integer.toString(getGMNumber()) + ",instName=" + str + "]";
    }

    /**
     * Makes a copy of a ToneDescription object.
     */
    public Object clone() {
        return new ToneDescription(getBytes());
    }

    private static int NAStuffToneDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MusicManager.NAStuffToneDescription(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int NAPickInstrument(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        return MusicManager.NAPickInstrument(paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2, paramInt3, paramInt4, paramInt5, paramInt6);
    }

    private static int GetMainEventLoop() {
        return PlatformManager.GetMainEventLoop();
    }
}
