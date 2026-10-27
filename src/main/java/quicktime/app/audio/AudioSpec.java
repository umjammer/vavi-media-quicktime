package quicktime.app.audio;

import quicktime.QTException;

/**
 * @deprecated
 * AudioSpec provides a standard interface that all audio
 * capable objects can support.
 *
 * Volume values for objects that implement this interface
 * are 0 for silence, 1.0F for unity gain (generally maximum volume for a given
 * object). Values over 1.0 are not guaranteed to result in louder volumes; some objects support
 * overdriving the volume, some do not. If an object does not support a value greater than 1, then
 * it will set the volume to its maximum level.
 *
 * An object may have a volume level but because it is muted you will not hear anything.
 *
 * Unmuting an AudioSpec object will set it to its current volume setting. If an object is muted
 * and its volume is changed this will not make the audio object audible, it has to be
 * explicitly unmuted. This is inline with the way most mixing consoles work.
 */
public interface AudioSpec {

    /**
     * @deprecated
     * Returns the volume setting (0 is silent, 1 is unity gain) * @deprecated since QTJava 6.1
     */
    float getVolume() throws QTException;

    /**
     * @deprecated
     * Sets the volume. * @deprecated since QTJava 6.1
     */
    void setVolume(float paramFloat) throws QTException;

    /**
     * @deprecated
     * Returns the current mute status * @deprecated since QTJava 6.1
     */
    boolean isMuted() throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Allows the muting of the specific channel. When unmuted the AudioSpec object will return
     * to its current volume setting.
     * @param flag turn muting on - true or off - false.
     * @throws quicktime.QTException
     */
    void setMuted(boolean flag) throws QTException;
}
