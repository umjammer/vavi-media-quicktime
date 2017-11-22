
package quicktime.app.audio;

import quicktime.QTException;


/**
 * @deprecated
 */
public abstract interface ExtendedAudioSpec extends AudioSpec {
    public abstract float getBalance() throws QTException;

    public abstract void setBalance(float paramFloat) throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/audio
 * /ExtendedAudioSpec.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
