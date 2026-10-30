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
 * The QTPlayer class allows your application to display, play and control any
 * QuickTime movie with the standard QuickTime movie controller.
 * <p>
 * The QTPlayer does not handle its own events but delegates the event handling
 * to some kind of QTPlayerEventHandler object.
 *
 * @see quicktime.app.players.QTPlayerEventHandler,
 * {@link quicktime.std.movies.MovieController}
 * @deprecated
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
     * This constructor creates a QTPlayer object given a QuickTime MovieController object.
     *
     * @param mc a MovieController object
     * @since QTJava 6.1
     * @deprecated
     */
    public QTPlayer(MovieController mc) throws StdQTException {
        this.controller = mc;
    }

    private boolean setControllerVisibleFlag = false;

    private boolean reserveSize = true;

    private QTPlayerEventHandler eventHandler;

    /**
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest. The QTPlayer expects that
     * the incoming object is some kind of java.awt.Component - in which
     * case it registers its interest in mouse events from that Component source.
     * If the incoming object is not some kind of Component then nothing is done.
     *
     * @param interest the object that is to be the source of interest for the
     *                 the object that implements this interface.
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void addedTo(Object interest) {
        if (this.eventHandler == null)
            this.eventHandler = QTPlayerEventHandler.make(this, interest);
        if (this.eventHandler != null) {
            this.eventHandler.addedTo();
        }
    }

    /**
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest. The QTPlayer expects that
     * the incoming object is some kind of java.awt.Component - in which
     * case it removes its interest in mouse events from that Component source.
     * If the incoming object is not some kind of Component then nothing is done.
     *
     * @param interest the object that was the source of interest for the
     *                 the object that implements this interface.
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void removedFrom(Object interest) {
        if (this.eventHandler != null)
            this.eventHandler.removedFrom();
        this.eventHandler = null;
    }

    /**
     * This allows you to access the contained movie controller. * @deprecated since QTJava 6.1
     *
     * @deprecated
     */
    public MovieController getMovieController() {
        return this.controller;
    }

    /**
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     *
     * @see ()
     * @since QTJava 4.0
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        QDDimension originalSize = getOriginalSize();
        return new Dimension(originalSize.getWidth(), originalSize.getHeight());
    }

    /**
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     *
     * @return the original size of the movie and its controller
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public QDDimension getOriginalSize() throws QTException {
        QDDimension requiredSize = this.controller.getRequiredSize();
        QDRect naturalBoundsRect = this.controller.getMovie().getNaturalBoundsRect();
        return new QDDimension(naturalBoundsRect.getWidth() + requiredSize.getWidth(), naturalBoundsRect.getHeight() + requiredSize.getHeight());
    }

    /**
     * This will return the client's current display transformations
     *
     * @return the display transformations of the client
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public Matrix getMatrix() throws StdQTException {
        return this.controller.getMovie().getMatrix();
    }

    /**
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void setMatrix(Matrix matrix) throws QTException {
        this.controller.getMovie().setMatrix(matrix);
        this.controller.movieChanged();
        this.controller.idle();
    }

    /**
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     *
     * @return a QDGraphics object or null
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public QDGraphics getGWorld() throws StdQTException {
        return this.controller.getPort();
    }

    /**
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param cgp a QDGraphics object
     * @throws quicktime.qd.QDException
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public synchronized void setGWorld(QDGraphics cgp) throws QDException, StdQTException {
        if (getGWorld().equals(cgp))
            return;
        this.controller.setPort(cgp);
        if (!QDGraphics.scratch.equals(cgp)) {
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
     * QTCanvas calls this method when the client should redraw itself.
     * If the canvas is able to discern that only a part of the client's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * client should redraw itself entirely.
     *
     * @param invalidRgn the invalidRgn that the client should redraw
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public final void redraw(Region invalidRgn) throws QTException {
        if (invalidRgn != null) {
            try {
                this.controller.invalidate(getGWorld(), invalidRgn);
                this.controller.idle();
            } catch (StdQTException e) {
                this.controller.draw();
            }
        } else {
            this.controller.draw();
        }
    }

    /**
     * This method is called automatically via the QTCanvas object associated with
     * this client to set the boundary of the movie.
     *
     * @param bounds a QDRect object describing the boundary
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public synchronized void setDisplayBounds(QDRect bounds) throws QTException {
        QDDimension requiredSize = this.controller.getRequiredSize();
        if ((bounds.getHeight() < requiredSize.getHeight()) || (bounds.getWidth() < requiredSize.getWidth())) {
            if (this.controller.getVisible()) {
                this.controller.setVisible(false);
                this.setControllerVisibleFlag = true;
            }
            bounds.setHeight(bounds.getHeight() + requiredSize.getHeight());
            bounds.setWidth(bounds.getWidth() + requiredSize.getWidth());
        } else {
            if (this.setControllerVisibleFlag) {
                this.controller.setVisible(true);
                this.setControllerVisibleFlag = false;
            }
            if ((!this.reserveSize) && (!this.controller.getVisible())) {
                bounds.setHeight(bounds.getHeight() + requiredSize.getHeight());
                bounds.setWidth(bounds.getWidth() + requiredSize.getWidth());
            }
        }
        this.controller.setBounds(bounds);
        if (this.redrawRequired) {
            this.redrawRequired = false;
            if (this.controller.getVisible()) {
                redraw(null);
            }
        }
    }

    /**
     * A convenience method to set the position of a QTDrawable object.
     *
     * @param x the new x location
     * @param y the new y location
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void setLocation(int x, int y) throws QTException {
        QDRect bounds = getDisplayBounds();
        bounds.setX(x);
        bounds.setY(y);
        setDisplayBounds(bounds);
    }

    /**
     * This method is called automatically via the QTCanvas object associated with
     * this client to set the boundary of the movie.
     *
     * @return the display boundary of the client
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public QDRect getDisplayBounds() throws StdQTException {
        return this.reserveSize ? this.controller.getBounds() : this.controller.getVisible() ? this.controller.getBounds() : this.controller.getMovie().getBounds();
    }

    /**
     * This method performs idle processing for the QTPlayer and will be automatically
     * called if this object is added to the TaskThread object.
     *
     * @see "quicktime.util.TaskThread"
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public final void task() throws QTException {
        this.controller.idle();
    }

    /**
     * This method allows you to set a QTPlayer's clipping region.
     *
     * @param theClip a Region that defines the player's clipping region.
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void setClip(Region theClip) throws QTException {
        this.controller.setClip(theClip);
    }

    /**
     * This method allows you to obtain information describing a QTPlayer's clipping regions.
     *
     * @return the clipping region of the entire QTPlayer object
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public Region getClip() throws QTException {
        return this.controller.getClip();
    }

    /**
     * Sets the player to the time specified. This time value at this level
     * is a totally arbitrary value - what time that means in "real time" is
     * determined by the implementor of the interface, typically the scale value
     * will determine what a "real" time of 1 second is.
     *
     * @param time the new time value
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void setTime(int time) throws StdQTException {
        this.controller.getMovie().setTimeValue(time);
        this.controller.movieChanged();
    }

    /**
     * Returns the current time value of the object.
     *
     * @return the current time.
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public int getTime() throws StdQTException {
        return this.controller.getMovie().getTime();
    }

    /**
     * Returns the duration of the media attached to the player. If
     * the player has no media, or does not know the duration of its media
     * it can return the constant kDurationUnknown.
     *
     * @return the media duration.
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public int getDuration() throws StdQTException {
        return this.controller.getMovie().getDuration();
    }

    /**
     * Returns the current time base that provides the time foundation
     * of this object.
     *
     * @return a TimeBase
     * @throws quicktime.QTException
     * @deprecated
     */
    @Override
    public TimeBase getTimeBase() throws QTException {
        return this.controller.getMovie().getTimeBase();
    }

    /**
     * Sets the player to the playback rate. Typically a rate of zero will
     * mean that the player is stopped, a positive value that the player
     * is playing forwards, a negative value that the player is playing backwards.
     *
     * @param rate the new playback rate.
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void setRate(float rate) throws StdQTException {
        if (rate != 0.0F) {
            this.controller.prerollAndPlay(rate);
        } else {
            this.controller.play(rate);
        }
    }

    /**
     * Returns the current rate of the player.
     *
     * @return the playback rate
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public float getRate() throws StdQTException {
        return this.controller.getMovie().getRate();
    }

    /**
     * Returns the time scale of the player - how many ticks per second.
     *
     * @return the current scale.
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public int getScale() throws QTException {
        return this.controller.getMovie().getTimeScale();
    }

    /**
     * Returns the volume setting (0 is silent or muted, 1 is unity gain)
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public float getVolume() throws QTException {
        float f = this.controller.getVolume();
        return Math.abs(f);
    }

    /**
     * Sets the volume.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void setVolume(float vol) throws QTException {
        float f = Math.abs(vol);
        if (isMuted()) {
            this.controller.setVolume(f * -1.0F);
        } else {
            this.controller.setVolume(f);
        }
    }

    /**
     * Returns the current mute status
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean isMuted() throws QTException {
        return this.controller.getVolume() < 0.0F;
    }

    /**
     * Allows the muting of the specific channel. When unmuted the AudioSpec object will return
     * to its current volume setting.
     *
     * @param flag turn muting on - true or off - false.
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void setMuted(boolean flag) throws QTException {
        float f = Math.abs(this.controller.getVolume());
        if (flag) {
            this.controller.setVolume(f * -1.0F);
        } else {
            this.controller.setVolume(f);
        }
    }

    /**
     * If true makes the MovieController visible, otherwise makes it invisible
     *
     * @since QTJava 6.1
     * @deprecated
     */
    public void setControllerVisible(boolean controllerVisible) throws QTException {
        this.controller.setVisible(controllerVisible);
        if ((this.setControllerVisibleFlag) && (!controllerVisible)) {
            this.setControllerVisibleFlag = false;
        }
    }

    /**
     * Returns the visible state of the controller.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    public boolean isControllerVisible() throws QTException {
        return this.controller.getVisible();
    }

    /**
     * Sets parameters on the resize of the controller. If true then the size of
     * controller and its movie will be set with the setDisplayBounds. If false then
     * setDisplayBounds will resize the movie to the specified rectangle. Setting this
     * flag will cause the movie and its controller to be resized.
     *
     * @param flag the resize flag
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    public void setControllerSizeReserved(boolean flag) throws QTException {
        this.reserveSize = flag;
        setDisplayBounds(getDisplayBounds());
    }

    /**
     * Returns true if display space is being reserved for the size of the controller
     *
     * @since QTJava 6.1
     * @deprecated
     */
    public boolean isControllerSizeReserved() {
        return this.reserveSize;
    }
}
