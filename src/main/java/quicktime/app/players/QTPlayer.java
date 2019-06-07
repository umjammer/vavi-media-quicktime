package quicktime.app.players;

import java.awt.Dimension;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.audio.AudioSpec;
import quicktime.app.display.QTDrawable;
import quicktime.app.time.TaskThread;
import quicktime.app.time.Tasking;
import quicktime.qd.QDDimension;
import quicktime.qd.QDException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;
import quicktime.std.image.Matrix;
import quicktime.std.movies.MovieController;

/**
 * Deprecated.
 * The QTPlayer class allows your application to display, play and control any
 * QuickTime movie with the standard QuickTime movie controller.
 *
 * The QTPlayer does not handle its own events but delegates the event handling
 * to some kind of QTPlayerEventHandler object.
 * @see {@link quicktime.app.players.QTPlayerEventHandler},
 * {@link quicktime.std.movies.MovieController}
 */
public class QTPlayer extends Tasking implements QTDrawable, Playable, AudioSpec {

    MovieController controller;

    private TaskThread tt;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This constructor creates a QTPlayer object given a QuickTime MovieController object.
     * @param mc a MovieController object
     */
    public QTPlayer(MovieController paramMovieController) throws StdQTException {
        this.controller = paramMovieController;
    }

    private boolean setControllerVisibleFlag = false;

    private boolean reserveSize = true;

    private QTPlayerEventHandler eventHandler;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest. The QTPlayer expects that
     * the incoming object is some kind of java.awt.Component - in which
     * case it registers its interest in mouse events from that Component source.
     * If the incoming object is not some kind of Component then nothing is done.
     * @param interest the object that is to be the source of interest for the
     *  the object that implements this interface.
     */
    public void addedTo(Object paramObject) {
        if (this.eventHandler == null)
            this.eventHandler = QTPlayerEventHandler.make(this, paramObject);
        if (this.eventHandler != null) {
            this.eventHandler.addedTo();
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest. The QTPlayer expects that
     * the incoming object is some kind of java.awt.Component - in which
     * case it removes its interest in mouse events from that Component source.
     * If the incoming object is not some kind of Component then nothing is done.
     * @param interest the object that was the source of interest for the
     *  the object that implements this interface.
     */
    public void removedFrom(Object paramObject) {
        if (this.eventHandler != null)
            this.eventHandler.removedFrom();
        this.eventHandler = null;
    }

    /**
     * Deprecated.
     * This allows you to access the contained movie controller. * @deprecated since QTJava 6.1
     */
    public MovieController getMovieController() {
        return this.controller;
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        QDDimension localQDDimension = getOriginalSize();
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size of the movie and its controller
     * @return {@link quicktime.QTException}
     */
    public QDDimension getOriginalSize() throws QTException {
        QDDimension localQDDimension = this.controller.getRequiredSize();
        QDRect localQDRect = this.controller.getMovie().getNaturalBoundsRect();
        return new QDDimension(localQDRect.getWidth() + localQDDimension.getWidth(), localQDRect.getHeight() + localQDDimension.getHeight());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will return the client's current display transformations
     *
     * @return the display transformations of the client
     * @return {@link quicktime.std.StdQTException}
     */
    public Matrix getMatrix() throws StdQTException {
        return this.controller.getMovie().getMatrix();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.controller.getMovie().setMatrix(paramMatrix);
        this.controller.movieChanged();
        this.controller.idle();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     * @return {@link quicktime.std.StdQTException}
     */
    public QDGraphics getGWorld() throws StdQTException {
        return this.controller.getPort();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param cgp a QDGraphics object
     * @param {@link quicktime.qd.QDException}
     * @param {@link quicktime.std.StdQTException}
     */
    public synchronized void setGWorld(QDGraphics paramQDGraphics) throws QDException, StdQTException {
        if (getGWorld().equals(paramQDGraphics))
            return;
        this.controller.setPort(paramQDGraphics);
        if (!QDGraphics.scratch.equals(paramQDGraphics)) {
            startTasking();
            this.redrawRequired = true;
            this.controller.getMovie().setActive(true);
        } else {
            setRate(0.0F);
            stopTasking();
            this.controller.getMovie().setActive(false);
        }
    }

    private boolean redrawRequired = false;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when the client should redraw itself.
     * If the canvas is able to discern that only a part of the client's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * client should redraw itself entirely.
     * @param invalidRgn the invalidRgn that the client should redraw
     * @param {@link quicktime.QTException}
     */
    public final void redraw(Region paramRegion) throws QTException {
        if (paramRegion != null) {
            try {
                this.controller.invalidate(getGWorld(), paramRegion);
                this.controller.idle();
            } catch (StdQTException localStdQTException) {
                this.controller.draw();
            }
        } else {
            this.controller.draw();
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called automatically via the QTCanvas object associated with
     * this client to set the boundary of the movie.
     *
     * @param bounds a QDRect object describing the boundary
     * @param {@link quicktime.QTException}
     */
    public synchronized void setDisplayBounds(QDRect paramQDRect) throws QTException {
        QDDimension localQDDimension = this.controller.getRequiredSize();
        if ((paramQDRect.getHeight() < localQDDimension.getHeight()) || (paramQDRect.getWidth() < localQDDimension.getWidth())) {
            if (this.controller.getVisible()) {
                this.controller.setVisible(false);
                this.setControllerVisibleFlag = true;
            }
            paramQDRect.setHeight(paramQDRect.getHeight() + localQDDimension.getHeight());
            paramQDRect.setWidth(paramQDRect.getWidth() + localQDDimension.getWidth());
        } else {
            if (this.setControllerVisibleFlag) {
                this.controller.setVisible(true);
                this.setControllerVisibleFlag = false;
            }
            if ((!this.reserveSize) && (!this.controller.getVisible())) {
                paramQDRect.setHeight(paramQDRect.getHeight() + localQDDimension.getHeight());
                paramQDRect.setWidth(paramQDRect.getWidth() + localQDDimension.getWidth());
            }
        }
        this.controller.setBounds(paramQDRect);
        if (this.redrawRequired) {
            this.redrawRequired = false;
            if (this.controller.getVisible()) {
                redraw(null);
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * A convenience method to set the position of a QTDrawable object.
     * @param x the new x location
     * @param y the new y location
     * @param {@link quicktime.QTException}
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(paramInt1);
        localQDRect.setY(paramInt2);
        setDisplayBounds(localQDRect);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called automatically via the QTCanvas object associated with
     * this client to set the boundary of the movie.
     *
     * @return the display boundary of the client
     * @return {@link quicktime.std.StdQTException}
     */
    public QDRect getDisplayBounds() throws StdQTException {
        return this.reserveSize ? this.controller.getBounds() : this.controller.getVisible() ? this.controller.getBounds() : this.controller.getMovie().getBounds();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method performs idle processing for the QTPlayer and will be automatically
     * called if this object is added to the TaskThread object.
     * @see quicktime.util.TaskThread
     */
    public final void task() throws QTException {
        this.controller.idle();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method allows you to set a QTPlayer's clipping region.
     * @param theClip a Region that defines the player's clipping region.
     * @param {@link quicktime.QTException}
     */
    public void setClip(Region paramRegion) throws QTException {
        this.controller.setClip(paramRegion);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method allows you to obtain information describing a QTPlayer's clipping regions.
     * @return the clipping region of the entire QTPlayer object
     * @return {@link quicktime.QTException}
     */
    public Region getClip() throws QTException {
        return this.controller.getClip();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the player to the time specified. This time value at this level
     * is a totally arbitrary value - what time that means in "real time" is
     * determined by the implementor of the interface, typically the scale value
     * will determine what a "real" time of 1 second is.
     * @param time the new time value
     * @param {@link quicktime.std.StdQTException}
     */
    public void setTime(int paramInt) throws StdQTException {
        this.controller.getMovie().setTimeValue(paramInt);
        this.controller.movieChanged();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current time value of the object.
     * @return the current time.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getTime() throws StdQTException {
        return this.controller.getMovie().getTime();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the duration of the media attached to the player. If
     * the player has no media, or does not know the duration of its media
     * it can return the constant kDurationUnknown.
     * @return the media duration.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getDuration() throws StdQTException {
        return this.controller.getMovie().getDuration();
    }

    /**
     * Deprecated.
     * Returns the current time base that provides the time foundation
     * of this object.
     * @return a TimeBase
     * @return {@link quicktime.QTException}
     */
    public TimeBase getTimeBase() throws QTException {
        return this.controller.getMovie().getTimeBase();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the player to the playback rate. Typically a rate of zero will
     * mean that the player is stopped, a positive value that the player
     * is playing forwards, a negative value that the player is playing backwards.
     * @param rate the new playback rate.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setRate(float paramFloat) throws StdQTException {
        if (paramFloat != 0.0F) {
            this.controller.prerollAndPlay(paramFloat);
        } else {
            this.controller.play(paramFloat);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current rate of the player.
     * @return the playback rate
     * @return {@link quicktime.std.StdQTException}
     */
    public float getRate() throws StdQTException {
        return this.controller.getMovie().getRate();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the time scale of the player - how many ticks per second.
     * @return the current scale.
     * @return {@link quicktime.QTException}
     */
    public int getScale() throws QTException {
        return this.controller.getMovie().getTimeScale();
    }

    /**
     * Deprecated.
     * Returns the volume setting (0 is silent or muted, 1 is unity gain) * @deprecated since QTJava 6.1
     */
    public float getVolume() throws QTException {
        float f = this.controller.getVolume();
        return Math.abs(f);
    }

    /**
     * Deprecated.
     * Sets the volume. * @deprecated since QTJava 6.1
     */
    public void setVolume(float paramFloat) throws QTException {
        float f = Math.abs(paramFloat);
        if (isMuted()) {
            this.controller.setVolume(f * -1.0F);
        } else {
            this.controller.setVolume(f);
        }
    }

    /**
     * Deprecated.
     * Returns the current mute status * @deprecated since QTJava 6.1
     */
    public boolean isMuted() throws QTException {
        return this.controller.getVolume() < 0.0F;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Allows the muting of the specific channel. When unmuted the AudioSpec object will return
     * to its current volume setting.
     * @param flag turn muting on - true or off - false.
     * @param {@link quicktime.QTException}
     */
    public void setMuted(boolean paramBoolean) throws QTException {
        float f = Math.abs(this.controller.getVolume());
        if (paramBoolean) {
            this.controller.setVolume(f * -1.0F);
        } else {
            this.controller.setVolume(f);
        }
    }

    /**
     * Deprecated.
     * If true makes the MovieController visible, otherwise makes it invisible * @deprecated since QTJava 6.1
     */
    public void setControllerVisible(boolean paramBoolean) throws QTException {
        this.controller.setVisible(paramBoolean);
        if ((this.setControllerVisibleFlag) && (!paramBoolean)) {
            this.setControllerVisibleFlag = false;
        }
    }

    /**
     * Deprecated.
     * Returns the visible state of the controller. * @deprecated since QTJava 6.1
     */
    public boolean isControllerVisible() throws QTException {
        return this.controller.getVisible();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets parameters on the resize of the controller. If true then the size of
     * controller and its movie will be set with the setDisplayBounds. If false then
     * setDisplayBounds will resize the movie to the specified rectangle. Setting this
     * flag will cause the movie and its controller to be resized.
     * @param flag the resize flag
     * @param {@link quicktime.QTException}
     */
    public void setControllerSizeReserved(boolean paramBoolean) throws QTException {
        this.reserveSize = paramBoolean;
        setDisplayBounds(getDisplayBounds());
    }

    /**
     * Deprecated.
     * Returns true if display space is being reserved for the size of the controller * @deprecated since QTJava 6.1
     */
    public boolean isControllerSizeReserved() {
        return this.reserveSize;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * players/QTPlayer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
