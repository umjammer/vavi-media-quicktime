
package quicktime.sound;

import quicktime.QTException;


public final class SoundException extends QTException {
    public SoundException(String paramString) {
        super(paramString);
    }

    public SoundException(int paramInt) {
        super(paramInt);
    }

    public static void checkError(int paramInt) throws SoundException {
        if (paramInt < 0) {
            throw new SoundException(paramInt);
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/sound/
 * SoundException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
