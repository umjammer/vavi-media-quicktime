package quicktime.app.audio;

import quicktime.QTException;

/**
 * Deprecated.
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
public abstract interface AudioSpec {

    /**
     * Deprecated.
     * Returns the volume setting (0 is silent, 1 is unity gain) * @deprecated since QTJava 6.1
     */
    public abstract float getVolume() throws QTException;

    /**
     * Deprecated.
     * Sets the volume. * @deprecated since QTJava 6.1
     */
    public abstract void setVolume(float paramFloat) throws QTException;

    /**
     * Deprecated.
     * Returns the current mute status * @deprecated since QTJava 6.1
     */
    public abstract boolean isMuted() throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Allows the muting of the specific channel. When unmuted the AudioSpec object will return
     * to its current volume setting.
     * @param flag turn muting on - true or off - false.
     * @param {@link quicktime.QTException}
     */
    public abstract void setMuted(boolean paramBoolean) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/audio
 * /AudioSpec.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
