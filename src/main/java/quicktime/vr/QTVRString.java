package quicktime.vr;

import quicktime.QTException;
import quicktime.util.QTPointerRef;

public final class QTVRString extends QTPointerRef {

    /**
     * Creates a QTVRString object from a String
     */
    public QTVRString(String paramString) throws QTException {
        super(4 + paramString.length(), true);
        setShortAt(0, (short) 0);
        setShortAt(2, (short) paramString.length());
        System.arraycopy(paramString.getBytes(), 0, getBytes(), 4, paramString.length());
    }

    /**
     * Creates a QTVRString object from a byte array
     * @throws if the byte array does not conform to valid QTVRString data
     * @throws quicktime.QTException
     */
    public QTVRString(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte.length, true);
        if (paramArrayOfByte.length < 4)
            throw new QTException(-50);
        int i = (short) ((paramArrayOfByte[2] << 8) + paramArrayOfByte[3]);
        if (4 + i != paramArrayOfByte.length)
            throw new QTException(-50);
        System.arraycopy(paramArrayOfByte, 0, getBytes(), 0, paramArrayOfByte.length);
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
    public void setStringUsage(short paramShort) {
        setShortAt(0, paramShort);
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
