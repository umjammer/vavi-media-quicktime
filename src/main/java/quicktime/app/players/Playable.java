package quicktime.app.players;

import quicktime.QTException;
import quicktime.app.time.Timeable;

/**
 * @deprecated
 * This interface defines a protocol for dealing with time based media. It
 * allows you to get the duration, set/get the current time position of playback
 * of the media, retrieve the time scale (how many ticks per second) of the media
 * and set/get the playback rate.
 */
public interface Playable extends Timeable {

    /**
     * @deprecated
     */
    int kDurationUnknown = Integer.MIN_VALUE;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the player to the time specified. This time value at this level
     * is a totally arbitrary value - what time that means in "real time" is
     * determined by the implementor of the interface, typically the scale value
     * will determine what a "real" time of 1 second is.
     * @param time the new time value
     * @throws quicktime.QTException
     */
    void setTime(int paramInt) throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current time value of the object.
     * @return the current time.
     * @throws quicktime.QTException
     */
    int getTime() throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the time scale of the player - how many ticks per second.
     * @return the current scale.
     * @throws quicktime.QTException
     */
    int getScale() throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the duration of the media attached to the player. If
     * the player has no media, or does not know the duration of its media
     * it can return the constant kDurationUnknown.
     * @return the media duration.
     * @throws quicktime.QTException
     */
    int getDuration() throws QTException;
}

