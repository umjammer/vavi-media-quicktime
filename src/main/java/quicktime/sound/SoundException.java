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
    public SoundException(String paramString) {
        super(paramString);
    }

    /**
     * Creates an exception with a specific error number
     * @param val the error number
     */
    public SoundException(int paramInt) {
        super(paramInt);
    }

    /**
     * This method will throw an exception if the incoming err argument is
     * a non-zero value.
     * @param err the result or error code if non-zero an exception is thrown
     * @param {@link quicktime.sound.SoundException}
     */
    public static void checkError(int paramInt) throws SoundException {
        if (paramInt < 0) {
            throw new SoundException(paramInt);
        }
    }
}

