package quicktime.vr;

import quicktime.QTException;
import quicktime.util.QTPointerRef;

public final class QTVRString extends QTPointerRef {

    /**
     * Creates a QTVRString object from a String
     */
    public QTVRString(String str) throws QTException {
        super(4 + str.length(), true);
        setShortAt(0, (short) 0);
        setShortAt(2, (short) str.length());
        System.arraycopy(str.getBytes(), 0, getBytes(), 4, str.length());
    }

    /**
     * Creates a QTVRString object from a byte array
     * @throws if the byte array does not conform to valid QTVRString data
     * @throws quicktime.QTException
     */
    public QTVRString(byte[] bytes) throws QTException {
        super(bytes.length, true);
        if (bytes.length < 4)
            throw new QTException(-50);
        int i = (short) ((bytes[2] << 8) + bytes[3]);
        if (4 + i != bytes.length)
            throw new QTException(-50);
        System.arraycopy(bytes, 0, getBytes(), 0, bytes.length);
    }

    /**
     * returns the string Usage
     */
    public short getStringUsage() {
        return getShortAt(0);
    }

    /**
     * sets the string Usage
     */
    public void setStringUsage(short stringUsage) {
        setShortAt(0, stringUsage);
    }

    /**
     * returns the string Length
     */
    public short getStringLength() {
        return getShortAt(2);
    }

    /**
     * returns the string
     */
    public String getString() {
        int i = getShortAt(2);
        byte[] arrayOfByte = new byte[i];
        getBytesAt(4, i, arrayOfByte, 0);
        return new String(arrayOfByte);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName() + "[" + getString() + "]";
    }
}
