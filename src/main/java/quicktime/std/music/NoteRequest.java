
package quicktime.std.music;

import quicktime.QTException;


public final class NoteRequest extends NoteRequestInfo implements Cloneable {
    public static final int kNativeSize = 84;

    public NoteRequest() {
        super(kNativeSize, null);
    }

    public NoteRequest(ToneDescription paramToneDescription) {
        this();
        setToneDescription(paramToneDescription);
    }

    public NoteRequest(ToneDescription paramToneDescription, int paramInt) {
        this();
        setToneDescription(paramToneDescription);
        setPolyphony(paramInt);
    }

    public NoteRequest(int paramInt1, int paramInt2) throws QTException {
        this();
        setToneDescription(new ToneDescription(paramInt1));
        setPolyphony(paramInt2);
    }

    private NoteRequest(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    public void setToneDescription(ToneDescription paramToneDescription) {
        byte[] arrayOfByte = paramToneDescription.getBytes();
        setBytesAt(8, arrayOfByte.length, arrayOfByte, 0);
    }

    public ToneDescription getToneDescription() {
        ToneDescription localToneDescription = new ToneDescription();
        getBytesAt(8, localToneDescription.getBytes().length, localToneDescription.getBytes(), 0);
        return localToneDescription;
    }

    public String toString() {
        return getClass().getName() + "[flags=" + getFlags() + ",poly=" + getPolyphony() + ",typPoly=" + getTypicalPolyphony()
               + "," + getToneDescription() + "]";
    }

    public Object clone() {
        return new NoteRequest(getBytes());
    }
}
