package quicktime.app.players;

import quicktime.QTException;
import quicktime.app.time.Timeable;

/**
 * Deprecated.
 * This interface defines a protocol for dealing with time based media. It
 * allows you to get the duration, set/get the current time position of playback
 * of the media, retrieve the time scale (how many ticks per second) of the media
 * and set/get the playback rate.
 */
public abstract interface Playable extends Timeable {

    /**
     * Deprecated.
     */
    public static final int kDurationUnknown = Integer.MIN_VALUE;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the player to the time specified. This time value at this level
     * is a totally arbitrary value - what time that means in "real time" is
     * determined by the implementor of the interface, typically the scale value
     * will determine what a "real" time of 1 second is.
     * @param time the new time value
     * @param {@link quicktime.QTException}
     */
    public abstract void setTime(int paramInt) throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current time value of the object.
     * @return the current time.
     * @return {@link quicktime.QTException}
     */
    public abstract int getTime() throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the time scale of the player - how many ticks per second.
     * @return the current scale.
     * @return {@link quicktime.QTException}
     */
    public abstract int getScale() throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the duration of the media attached to the player. If
     * the player has no media, or does not know the duration of its media
     * it can return the constant kDurationUnknown.
     * @return the media duration.
     * @return {@link quicktime.QTException}
     */
    public abstract int getDuration() throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * players/Playable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
