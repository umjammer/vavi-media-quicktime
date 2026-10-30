package quicktime.std.music;

import quicktime.QTException;

/**
 * Provides all the information required to initialise a NoteChannel.
 * The constructors for a NoteRequest will correclty fill in the the default
 * values for the typicalPolyphony field (1.0) and flags will be set to zero.
 */
public final class NoteRequest extends NoteRequestInfo implements Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 84;

    /**
     * Creates a NoteRequest instance with the default values.
     * Polyphony is set to 1.
     */
    public NoteRequest() {
        super(kNativeSize, null);
    }

    /**
     * Create a NoteRequest from the given ToneDescription value.
     * @param td a ToneDescription that describes the tone characteristics of the NoteRequest
     */
    public NoteRequest(ToneDescription td) {
        this();
        setToneDescription(td);
    }

    /**
     * Create a NoteRequest from the given ToneDescription and polyphony values.
     * @param td a ToneDescription that describes the tone characteristics of the NoteRequest.
     * @param poly the most number of simulateous notes maybe played at the same time. This
     *  value is a suggestion to the music toolbox that is used to estimate the resources
     *  a NoteChannel will be required to utilise.
     */
    public NoteRequest(ToneDescription td, int poly) {
        this();
        setToneDescription(td);
        setPolyphony(poly);
    }

    /**
     * Create a NoteRequest from the given gmNumber instrument and polyphony values.
     * @param gmNumber the general MIDI number of the requested instrument
     * @param poly the most number of simulateous notes maybe played at the same time. This
     *  value is a suggestion to the music toolbox that is used to estimate the resources
     *  a NoteChannel will be required to utilise.
     */
    public NoteRequest(int gmNumber, int poly) throws QTException {
        this();
        setToneDescription(new ToneDescription(gmNumber));
        setPolyphony(poly);
    }

    private NoteRequest(byte[] bytes) {
        super(bytes);
    }

    /**
     * Sets the ToneDescription.
     */
    public void setToneDescription(ToneDescription toneDescription) {
        byte[] srcArray = toneDescription.getBytes();
        setBytesAt(8, srcArray.length, srcArray, 0);
    }

    /**
     * Returns a copy of the current contained ToneDescription
     */
    public ToneDescription getToneDescription() {
        ToneDescription toneDescription = new ToneDescription();
        getBytesAt(8, toneDescription.getBytes().length, toneDescription.getBytes(), 0);
        return toneDescription;
    }

    /**
     * Returns a string representation of this object
     */
    public String toString() {
        return getClass().getName() + "[flags=" + getFlags() + ",poly=" + getPolyphony() + ",typPoly=" + getTypicalPolyphony() + "," + getToneDescription() + "]";
    }

    /**
     * Makes a copy of a NoteRequest object.
     * @return a NoteRequestInfo
     */
    @Override
    public Object clone() {
        return new NoteRequest(getBytes());
    }
}
