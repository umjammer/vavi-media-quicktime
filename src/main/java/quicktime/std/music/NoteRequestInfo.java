package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

/**
 * Provides the basic information that is used to create a NoteChannel.
 * Typically an application uses the complete NoteRequest (subclass) when using QT Music
 * services.
 */
public class NoteRequestInfo extends QTByteObject implements PrimitivesLib, Cloneable {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 8;

    private static final long serialVersionUID = -8181311485902882832L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(NoteRequestInfo.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Creates a NoteRequestInfo instance with the default values.
     * Polyphony is set to 1.
     */
    public NoteRequestInfo() {
        this(8, null);
    }

    /**
     * Create a NoteRequestInfo from the given polyphony value.
     * @param poly the most number of simulateous notes maybe played at the same time. This
     *  value is a suggestion to the music toolbox that is used to estimate the resources
     *  a NoteChannel will be required to utilise.
     */
    public NoteRequestInfo(int paramInt) throws QTException {
        this(8, null);
        setPolyphony(paramInt);
    }

    NoteRequestInfo(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    NoteRequestInfo(int paramInt, Object paramObject) {
        super(paramInt);
        setPolyphony(1);
        setTypicalPolyphony(1.0F);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        int i = paramObjectInputStream.readInt();
        this.bytes = new byte[i];
        paramObjectInputStream.read(this.bytes);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = getBytes();
        int i = getSize();
        paramObjectOutputStream.writeInt(i);
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * Gets the flags of the NoteRequestInfo
     * @return flags
     */
    public final int getFlags() {
        return getByteAt(0);
    }

    /**
     * Sets the flags of the NoteRequestInfo
     * @param flags flags to set
     */
    public final void setFlags(int paramInt) {
        setByteAt(0, (byte) paramInt);
    }

    /**
     * Gets the MIDIChannel - a value of zero indicates an unassigned channel
     * and QTMA will then use the first free channel if required.
     * @return MIDIChannel
     */
    public final int getMIDIChannel() {
        int i = getByteAt(1);
        if ((i & 0x80) == 0)
            return 0;
        i &= 0x7F;
        if (i > 16)
            return 0;
        return i;
    }

    /**
     * Sets the MIDIChannel - a value of zero indicates an unassigned channel
     * and QTMA will then use the first free channel if required.
     * @param chan MIDIChannel
     */
    public final void setMIDIChannel(int paramInt) {
        paramInt |= 0x80;
        setByteAt(1, (byte) paramInt);
    }

    /**
     * Gets the polyphony of the NoteRequestInfo
     * @return polyphony
     */
    public final int getPolyphony() {
        return EndianOrder.flipBigEndianToNative16(getShortAt(2));
    }

    /**
     * Sets the polyphony of the NoteRequestInfo
     * @param poly polyphony to set
     */
    public final void setPolyphony(int paramInt) {
        setShortAt(2, EndianOrder.flipNativeToBigEndian16((short) paramInt));
    }

    /**
     * Gets the typicalPolyphony of the NoteRequestInfo
     * @return typicalPolyphony
     */
    public final float getTypicalPolyphony() {
        return QTUtils.Fix2X(EndianOrder.flipBigEndianToNative32(getIntAt(4)));
    }

    /**
     * Sets the typicalPolyphony of the NoteRequestInfo
     * @param poly typicalPolyphony to set
     */
    public final void setTypicalPolyphony(float paramFloat) {
        setIntAt(4, EndianOrder.flipNativeToBigEndian32(QTUtils.X2Fix(paramFloat)));
    }

    /**
     * Returns a string representation of this object
     */
    public String toString() {
        return getClass().getName() + "[flags=" + getFlags() + ",poly=" + getPolyphony() + ",typPoly=" + getTypicalPolyphony() + "]";
    }

    /**
     * Makes a copy of a NoteRequestInfo object.
     * @return a NoteRequestInfo
     */
    public Object clone() {
        return new NoteRequestInfo(getBytes());
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
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /NoteRequestInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
