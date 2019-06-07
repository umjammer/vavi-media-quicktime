package quicktime.app.players;

import java.awt.Dimension;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.audio.AudioSpec;
import quicktime.app.display.DrawingListener;
import quicktime.app.display.DrawingNotifier;
import quicktime.app.display.QTDrawable;
import quicktime.app.time.TaskAllMovies;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;
import quicktime.std.image.Matrix;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieDrawingComplete;
import quicktime.std.movies.MoviePrePreroll;

/**
 * Deprecated.
 * The MoviePlayer class allows your application to display, play and control any
 * QuickTime movie. The MoviePlayer
 * object works with the QTCanvas to be displayed in a Java AWT Container.
 *
 * @see {@link quicktime.app.display.QTCanvas},
 * {@link quicktime.std.movies.Movie}
 */
public class MoviePlayer implements QTDrawable, Playable, AudioSpec, DrawingNotifier {

    private Movie mMovie;

    private Vector listenerList;

    private MovieComplete mComplete;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This constructor creates a MoviePlayer object given a QuickTime Movie object.
     * @param mov a Movie object
     */
    public MoviePlayer(Movie paramMovie) throws StdQTException {
        this.mMovie = paramMovie;
        paramMovie.setPlayHints(256, 256);
    }

    /**
     * Deprecated.
     * This allows you to access the contained movie controller. * @deprecated since QTJava 6.1
     */
    public Movie getMovie() {
        return this.mMovie;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     * @return {@link quicktime.std.StdQTException}
     */
    public QDGraphics getGWorld() throws StdQTException {
        return this.mMovie.getGWorld();
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
     * @param {@link quicktime.std.StdQTException}
     */
    public synchronized void setGWorld(QDGraphics paramQDGraphics) throws StdQTException {
        if (getGWorld().equals(paramQDGraphics))
            return;
        this.mMovie.setGWorld(paramQDGraphics, null);
        if (QDGraphics.scratch.equals(paramQDGraphics)) {
            TaskAllMovies.removeMovie();
            setRate(0.0F);
            this.mMovie.setActive(false);
        } else {
            TaskAllMovies.addMovieAndStart();
            this.mMovie.setActive(true);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        QDRect localQDRect = this.mMovie.getNaturalBoundsRect();
        return new Dimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size.
     * @return {@link quicktime.QTException}
     */
    public QDDimension getOriginalSize() throws QTException {
        QDRect localQDRect = this.mMovie.getNaturalBoundsRect();
        return new QDDimension(localQDRect.getWidth(), localQDRect.getHeight());
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
        return this.mMovie.getMatrix();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.mMovie.setMatrix(paramMatrix);
        this.mMovie.task(0);
    }

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
            this.mMovie.invalidateRegion(paramRegion);
        } else {
            this.mMovie.invalidateRegion(new Region(this.mMovie.getBounds()));
        }
        this.mMovie.task(0);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called automatically via the QTCanvas object associated with
     * this client to set the boundary of the movie.
     *
     * @param bounds a QDRect object describing the boundary
     * @param {@link quicktime.std.StdQTException}
     */
    public synchronized void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        this.mMovie.setBounds(paramQDRect);
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
        return this.mMovie.getBounds();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method allows you to set a MoviePlayer's clipping region.
     * @param theClip a Region that defines the player's clipping region.
     * @param {@link quicktime.QTException}
     */
    public void setClip(Region paramRegion) throws QTException {
        this.mMovie.setDisplayClipRgn(paramRegion);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method allows you to obtain information describing a MoviePlayer's clipping regions.
     * @return the clipping region of the entire MoviePlayer object
     * @return {@link quicktime.QTException}
     */
    public Region getClip() throws QTException {
        return this.mMovie.getDisplayClipRgn();
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
        this.mMovie.setTimeValue(paramInt);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current time value of the object.
     * @return the current time.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getTime() throws StdQTException {
        return this.mMovie.getTime();
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
        return this.mMovie.getDuration();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the TimeBase of the media attached to the player.
     * @return the players TimeBase.
     * @return {@link quicktime.QTException}
     */
    public TimeBase getTimeBase() throws QTException {
        return this.mMovie.getTimeBase();
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
        synchronized (this) {
            this.lastRate = paramFloat;
        }
        if (paramFloat == 0.0F) {
            this.mMovie.stop();
        } else
            this.mMovie.prePreroll(this.mMovie.getTime(), paramFloat, new PrerollCallback(paramFloat));
    }

    private float lastRate = 0.0F;

    class PrerollCallback implements MoviePrePreroll {

        private float r;

        PrerollCallback(float paramFloat) {
            this.r = paramFloat;
        }

        public void execute(Movie paramMovie, int paramInt) {
            synchronized (MoviePlayer.this) {
                if (MoviePlayer.this.lastRate == 0.0F) {
                    return;
                }
                if (paramInt == 0) {
                    try {
                        paramMovie.preroll(paramMovie.getTime(), this.r);
                        paramMovie.setRate(this.r);
                    } catch (QTException localQTException) {
                        QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), paramMovie, "preroll");
                    }
                } else
                    QTRuntimeException.handleOrThrow(new QTRuntimeException(paramInt), paramMovie, "prePreroll");
            }
        }
    }

    class MovieComplete implements MovieDrawingComplete {

        public int execute(Movie paramMovie) {
            synchronized (MoviePlayer.this) {
                for (int i = 0; i < MoviePlayer.this.listenerList.size(); i++) ((DrawingListener) MoviePlayer.this.listenerList.elementAt(i)).drawingComplete(MoviePlayer.this);
            }
            return 0;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Registers an object as a listener of the MoviePlayer. The object will receive notification when the MoviePlayer draws a frame
     */
    public synchronized void addDrawingListener(DrawingListener paramDrawingListener) {
        if (this.listenerList == null) {
            this.listenerList = new Vector();
        }
        if (this.listenerList.contains(paramDrawingListener)) {
            return;
        }
        this.listenerList.addElement(paramDrawingListener);
        if (this.mComplete == null) {
            try {
                this.mComplete = new MovieComplete();
                this.mMovie.setDrawingCompleteProc(0, this.mComplete);
            } catch (StdQTException localStdQTException) {
                this.mComplete = null;
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Removes an object that is a listener of the MoviePlayer. The object will no longer receive notification when the MoviePlayer draws a frame
     */
    public synchronized void removeDrawingListener(DrawingListener paramDrawingListener) {
        this.listenerList.removeElement(paramDrawingListener);
        if (this.listenerList.isEmpty()) {
            try {
                this.mMovie.removeDrawingCompleteProc();
            } catch (StdQTException localStdQTException) {
            }
            this.mComplete = null;
            this.listenerList = null;
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
        return this.mMovie.getRate();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the time scale of the player - how many ticks per second.
     * @return the current scale.
     * @return {@link quicktime.QTException}
     */
    public int getScale() throws QTException {
        return this.mMovie.getTimeScale();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the volume setting (0 is silent, 1 is unity gain).
     * @return a float
     * @return {@link quicktime.QTException}
     */
    public float getVolume() throws QTException {
        float f = this.mMovie.getVolume();
        return Math.abs(f);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the volume.
     * @param val the new volume
     * @param {@link quicktime.QTException}
     */
    public void setVolume(float paramFloat) throws QTException {
        float f = Math.abs(paramFloat);
        if (isMuted()) {
            this.mMovie.setVolume(f * -1.0F);
        } else {
            this.mMovie.setVolume(f);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current mute status.
     * @return a boolean
     * @return {@link quicktime.QTException}
     */
    public boolean isMuted() throws QTException {
        return this.mMovie.getVolume() < 0.0F;
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
        float f = Math.abs(this.mMovie.getVolume());
        if (paramBoolean) {
            this.mMovie.setVolume(f * -1.0F);
        } else {
            this.mMovie.setVolume(f);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest.
     * @param interest the object that is to be the source of interest for the
     *  the object that implements this interface.
     */
    public void addedTo(Object paramObject) {
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest.
     * @param interest the object that was the source of interest for the
     *  the object that implements this interface.
     */
    public void removedFrom(Object paramObject) {
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * players/MoviePlayer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
