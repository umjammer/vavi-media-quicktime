package quicktime.sound;

import quicktime.QTException;

/**
 * A General catch all class that is used to signal errors that occur from QT calls
 */
public final class SoundException extends QTException {

    /**
     * Creates an exception with a message that could contain information
     * that could be displayed to the user.
     * @param str The message that could be displayed to the user
     */
    public SoundException(String str) {
        super(str);
    }

    /**
     * Creates an exception with a specific error number
     * @param val the error number
     */
    public SoundException(int val) {
        super(val);
    }

    /**
     * This method will throw an exception if the incoming err argument is
     * a non-zero value.
     * @param err the result or error code if non-zero an exception is thrown
     * @throws quicktime.sound.SoundException
     */
    public static void checkError(int err) throws SoundException {
        if (err < 0) {
            throw new SoundException(err);
        }
    }
}

