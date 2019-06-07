package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;

/**
 * This class maps the SoundDescription record of the MovieToolbox.
 * SoundDescriptions are used when adding and getting media samples to/from Sound tracks.
 */
public final class SoundDescription extends SampleDescription implements QuickTimeLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 36;

    private static EndianDescriptor ed;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SoundDescription.class);
                    }
                });
            }
        }.establish();
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = SampleDescription.makeED();
        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(16, 2, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);
        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(20, 4, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);
        EndianFlipSpec localEndianFlipSpec3 = new EndianFlipSpec(20, 2, 4);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec3);
        EndianFlipSpec localEndianFlipSpec4 = new EndianFlipSpec(32, 4, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec4);
        return localEndianDescriptor;
    }

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * This constructor makes an empty SoundDescription object.
     * @param format the format of the sound data that the object describes
     */
    public SoundDescription(int paramInt) throws QTException {
        super(kNativeSize, true, paramInt);
    }

    private SoundDescription(int paramInt, Object paramObject) {
        super(paramInt, null, false);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    public Object clone() {
        return new SoundDescription(makeAndCopyHandle(), null);
    }

    public void setVersion(int paramInt) {
        setShortAt(16, (short) paramInt);
    }

    public int getVersion() {
        return getShortAt(16);
    }

    public void setRevLevel(int paramInt) {
        setShortAt(18, (short) paramInt);
    }

    public int getRevLevel() {
        return getShortAt(18);
    }

    public void setVendor(int paramInt) {
        setIntAt(20, paramInt);
    }

    public int getVendor() {
        return getIntAt(20);
    }

    public void setNumberOfChannels(int paramInt) {
        setNumChannels(paramInt);
    }

    public int getNumberOfChannels() {
        return getNumChannels();
    }

    public void setNumChannels(int paramInt) {
        setShortAt(24, (short) paramInt);
    }

    public int getNumChannels() {
        return getShortAt(24);
    }

    public void setSampleSize(int paramInt) {
        setShortAt(26, (short) paramInt);
    }

    public int getSampleSize() {
        return getShortAt(26);
    }

    public void setCompressionID(int paramInt) {
        setShortAt(28, (short) paramInt);
    }

    public int getCompressionID() {
        return getShortAt(28);
    }

    public void setPacketSize(int paramInt) {
        setShortAt(30, (short) paramInt);
    }

    public int getPacketSize() {
        return getShortAt(30);
    }

    public void setSampleRate(float paramFloat) {
        setIntAt(32, QTUtils.X2UFix(paramFloat));
    }

    public float getSampleRate() {
        return QTUtils.UFix2X(getIntAt(32));
    }

    public int getSampleRateRounded() {
        return (int) (QTUtils.Fix2X(getIntAt(32)) + 0.5D);
    }

    /**
     * Print information about this object.
     * @return a string representation of this object.
     */
    public String toString() {
        return getClass().getName() + "[format=" + QTUtils.fromOSType(getDataFormat()) + ",numChannels=" + getNumberOfChannels() + ",sampleSize=" + getSampleSize() + ",sampleRate=" + getSampleRate() + "]";
    }

    /**
     * Add application specific info to a Sound Description object
     * QuickTime::AddSoundDescriptionExtension
     * @param extension a QTHandle containing the info to add to the sound description
     * @param idType an OSType used to tag the information for later retrieval or removal
     * @param {@link quicktime.QTException}
     */
    public void addExtension(QTHandleRef paramQTHandleRef, int paramInt) throws QTException {
        int i = AddSoundDescriptionExtension(_ID(), QTObject.ID(paramQTHandleRef), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Remove application specific info from a Sound Description object
     * QuickTime::RemoveSoundDescriptionExtension
     * @param idType an OSType identifying the information for removal
     * @param {@link quicktime.QTException}
     */
    public void removeExtension(int paramInt) throws QTException {
        int i = RemoveSoundDescriptionExtension(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Retrieve application specific info from a Sound Description object
     * QuickTime::GetSoundDescriptionExtension
     * @param idType an OSType identifying the information for removal
     * @return a dataref to the Extension
     * @return {@link quicktime.QTException}
     */
    public QTHandle getExtension(int paramInt) throws QTException {
        return QTHandle.fromSoundDescription(this, paramInt);
    }

    private static native short AddSoundDescriptionExtension(int paramInt1, int paramInt2, int paramInt3);

    private static native short RemoveSoundDescriptionExtension(int paramInt1, int paramInt2);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SoundDescription.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
