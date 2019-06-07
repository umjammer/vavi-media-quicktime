package quicktime.app.audio;

import quicktime.QTException;

/**
 * Deprecated.
 * ExtendedAudioSpec provides a standard interface that all audio
 * capable objects can support.
 *
 * Balance is defined to a range of -1.0F for maximum left, 0 for centre, 1F for maximum right.
 */
public abstract interface ExtendedAudioSpec extends AudioSpec {

    /**
     * Deprecated.
     * Returns the current balance setting (-1 is left, 1 is right). * @deprecated since QTJava 6.1
     */
    public abstract float getBalance() throws QTException;

    /**
     * Deprecated.
     * Sets the balance of the channel * @deprecated since QTJava 6.1
     */
    public abstract void setBalance(float paramFloat) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/audio
 * /ExtendedAudioSpec.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
