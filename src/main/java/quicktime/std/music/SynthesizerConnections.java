package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.io.QTFile;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTPointer;
import quicktime.util.QTUtils;

/**
 * Describes how a MIDI device is connected to the computer.
 */
public class SynthesizerConnections extends QTByteObject implements PrimitivesLib, Cloneable {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 32;

    private QTPointer spec;

    @Serial
    private static final long serialVersionUID = -2499879404792104283L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(SynthesizerConnections.class));
            }
        }.establish();
    }

    /**
     * Create a connections class with all slots set to zero.
     */
    public SynthesizerConnections() {
        this(32);
    }

    /**
     * This is used to create a connections structure that describes
     * a sound bank file (DLS or SF2) that will be used to supply
     * sample information to a soft-synth music component.
     * @param soundBankFile the file
     */
    public SynthesizerConnections(QTFile soundBankFile) throws QTException {
        this();
        setSoundBankFile(soundBankFile);
    }

    protected SynthesizerConnections(int paramInt) {
        super(paramInt);
    }

    protected SynthesizerConnections(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        int i = paramObjectInputStream.readInt();
        this.bytes = new byte[i];
        paramObjectInputStream.read(this.bytes);
        for (int j = 0; j < 32; j += 4) {
            setIntInArray(this.bytes, j, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, j)));
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = getBytes();
        int i = getSize();
        paramObjectOutputStream.writeInt(i);
        for (int j = 0; j < 32; j += 2) {
            setIntInArray(arrayOfByte, j, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), j)));
        }
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * This is used to create a connections structure that describes
     * a sound bank file (DLS or SF2) that will be used to supply
     * sample information to a soft-synth music component.
     * @param soundBankFile the file
     * @throws quicktime.QTException
     */
    public void setSoundBankFile(QTFile soundBankFile) throws QTException {
        byte[] arrayOfByte = soundBankFile.getFSSpec(true, 256);
        this.spec = new QTPointer(arrayOfByte);
        setIntAt(24, QTObject.ID(this.spec));
    }

    /**
     * Returns a QTFile that represents
     * a sound bank file (DLS or SF2) that will be used to supply
     * sample information to a soft-synth music component (or null if none set).
     * @return the file
     * @throws quicktime.QTException
     */
    public QTFile getSoundBankFile() throws QTException {
        return QTFile.fromSynthesizerConnections(this);
    }

    /**
     * Return the MIDIManager client ID or 'OMS ' for OpenMIDISystem.
     */
    public final int getClientID() {
        return getIntAt(0);
    }

    /**
     * Set the MIDIManager client ID.
     */
    public final void setClientID(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * Return the ID of the port that receives MIDI information. (QT documents this as outputPortID).
     */
    public final int getInputID() {
        return getIntAt(8);
    }

    /**
     * Set the ID of the port that receives MIDI information. (QT documents this as outputPortID).
     */
    public final void setInputID(int paramInt) {
        setIntAt(8, paramInt);
    }

    /**
     * Return the ID of the port that sends MIDI information. (QT documents this as inputPortID).
     */
    public final int getOutputID() {
        return getIntAt(4);
    }

    /**
     * Set the ID of the port that sends MIDI information. (QT documents this as inputPortID).
     */
    public final void setOutputID(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * Return the system MIDI channel, or a slot number.
     */
    public final int getMIDIChannel() {
        return getIntAt(12);
    }

    /**
     * Set the system MIDI channel, or a slot number.
     */
    public final void setMIDIChannel(int paramInt) {
        setIntAt(12, paramInt);
    }

    /**
     * Return information about the type of connection.
     */
    public final int getFlags() {
        return getIntAt(16);
    }

    /**
     * Set information about the type of connection.
     */
    public final void setFlags(int paramInt) {
        setIntAt(16, paramInt);
    }

    /**
     * Return the unique id which may be used instead of an index to getinfo and unregister calls
     */
    public final int getUniqueID() {
        return getIntAt(20);
    }

    /**
     * Set the unique id which may be used instead of an index to getinfo and unregister calls
     */
    public final void setUniqueID(int paramInt) {
        setIntAt(20, paramInt);
    }

    /**
     * String representation.
     */
    public String toString() {
        return getClass().getName() + "[flags=" + getFlags() + (getFlags() > 0 ? ",clientID=" + QTUtils.fromOSType(getClientID()) + ",inputID=" + QTUtils.fromOSType(getInputID()) + ",outputID=" + QTUtils.fromOSType(getOutputID()) + ",uniqueID=" + QTUtils.fromOSType(getUniqueID()) + "]" : "]") + "";
    }

    /**
     * Return a copy of this object.
     */
    @Override
    public Object clone() {
        return new SynthesizerConnections(this.bytes);
    }

    private static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfByte, paramInt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort) {
        MemoryManager.setShortInArray(paramArrayOfByte, paramInt, paramShort);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}
