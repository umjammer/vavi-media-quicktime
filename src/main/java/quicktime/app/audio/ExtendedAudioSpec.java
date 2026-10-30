package quicktime.app.audio;

import quicktime.QTException;

/**
 * @deprecated
 * ExtendedAudioSpec provides a standard interface that all audio
 * capable objects can support.
 *
 * Balance is defined to a range of -1.0F for maximum left, 0 for centre, 1F for maximum right.
 */
public interface ExtendedAudioSpec extends AudioSpec {

    /**
     * @deprecated
     * Returns the current balance setting (-1 is left, 1 is right). * @deprecated since QTJava 6.1
     */
    float getBalance() throws QTException;

    /**
     * @deprecated
     * Sets the balance of the channel * @deprecated since QTJava 6.1
     */
    void setBalance(float val) throws QTException;
}
