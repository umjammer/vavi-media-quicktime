package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.io.QTFile;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTPointer;
import quicktime.util.QTUtils;


/**
 * Describes how a MIDI device is connected to the computer.
 */
public class SynthesizerConnections extends QTByteObject implements PrimitivesLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 32;

    private QTPointer spec;

    @Serial
    private static final long serialVersionUID = -2499879404792104283L;

    /**
     * Create a connections class with all slots set to zero.
     */
    public SynthesizerConnections() {
        this(kNativeSize);
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

    protected SynthesizerConnections(int size) {
        super(size);
    }

    protected SynthesizerConnections(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        int i = in.readInt();
        this.bytes = new byte[i];
        in.read(this.bytes);
        for (int j = 0; j < 32; j += 4) {
            setIntInArray(this.bytes, j, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, j)));
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = getBytes();
        int i = getSize();
        out.writeInt(i);
        for (int j = 0; j < 32; j += 2) {
            setIntInArray(movie, j, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), j)));
        }
        out.write(movie);
    }

    /**
     * This is used to create a connections structure that describes
     * a sound bank file (DLS or SF2) that will be used to supply
     * sample information to a soft-synth music component.
     * @param soundBankFile the file
     * @throws quicktime.QTException
     */
    public void setSoundBankFile(QTFile soundBankFile) throws QTException {
        byte[] byteArray = soundBankFile.getFSSpec(true, 256);
        this.spec = new QTPointer(byteArray);
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
    public final void setClientID(int clientID) {
        setIntAt(0, clientID);
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
    public final void setInputID(int inputID) {
        setIntAt(8, inputID);
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
    public final void setOutputID(int outputID) {
        setIntAt(4, outputID);
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
    public final void setMIDIChannel(int midiChannel) {
        setIntAt(12, midiChannel);
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
    public final void setFlags(int flags) {
        setIntAt(16, flags);
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
    public final void setUniqueID(int uniqueID) {
        setIntAt(20, uniqueID);
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

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }
}
