package quicktime.app.players;

import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.actions.Invalidator;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;
import quicktime.app.audio.AudioSpec;
import quicktime.app.display.DrawingListener;
import quicktime.app.display.DrawingNotifier;
import quicktime.app.image.DynamicImage;
import quicktime.app.image.ImagePresenter;
import quicktime.app.time.TaskAllMovies;
import quicktime.qd.PixMap;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;
import quicktime.std.image.ImageDescription;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieDrawingComplete;
import quicktime.std.movies.MoviePrePreroll;

/**
 * Deprecated.
 * The MoviePresenter is used to present typically a movie
 * as a member of a SWCompositor. It will create an Invalidator to innvalidate its
 * TwoDSprite that is presenting the movie in the SWCompositor
 * when the movie redraws.
 *
 * Outside of the use in the SWCompositor this Presenter also gives the ability
 * to apply a GraphicsMode to the overall output of a movie and can be used
 * also for easy manipulation and control of a movie's display characteristics,
 * location and transformations. For example if you are moving a movie around the
 * screen (for example bouncing it around) you will get better performance of the
 * visual output using a MoviePresenter than doing these kinds of activities
 * with a standard MoviePlayer presentation.
 *
 * It uses the MovieDrawingComplete callback to either invalidate its presenting
 * TwoDSprite or to redraw itself to the destination QDGraphics.
 * @see quicktime.app.display.SWCompositor
 */
public class MoviePresenter extends ImagePresenter implements AudioSpec, Playable, DynamicImage, MovieDrawingComplete, DrawingNotifier {

    private QDGraphics g;

    private Movie m;

    private boolean gModeOpaque;

    /**
     * Deprecated.
     */
    protected TwoDSprite spritePresenter;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    class MPInvalidator extends Invalidator {

        MPInvalidator(TwoDSprite paramTwoDSprite) {
            super(paramTwoDSprite);
        }

        public boolean tickle(float paramFloat, int paramInt) throws QTException {
            MoviePresenter.this.m.task(0);
            if (MoviePresenter.this.mChanged) {
                MoviePresenter.this.spritePresenter.invalidate();
                MoviePresenter.this.mChanged = false;
            }
            return true;
        }
    }

    private static QDRect moveToZero(QDRect paramQDRect) {
        paramQDRect.move(0, 0);
        return paramQDRect;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create a MoviePresenter with the supplied movie. The movie
     * will be rendered into a default offscreen QDGraphics and the
     * pixel data of the QDGraphics buffer becomes the image data
     * that this presenter presents.
     * @param m the movie that is rendered
     */
    public MoviePresenter(Movie paramMovie) throws QTException {
        this(paramMovie, new QDGraphics(32, paramMovie.getBounds(), 4096));
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create a MoviePresenter with the supplied movie. The movie
     * will be rendered into the supplied offscreen QDGraphics and the
     * pixel data of the QDGraphics buffer becomes the image data
     * that this presenter presents.
     * @param m the movie that is rendered
     * @param g the offscreen QDGraphics that the effect is rendered into.
     */
    public MoviePresenter(Movie paramMovie, QDGraphics paramQDGraphics) throws QTException {
        super(moveToZero(paramMovie.getBounds()));
        this.m = paramMovie;
        this.g = paramQDGraphics;
        paramMovie.setGWorld(paramQDGraphics, null);
        PixMap localPixMap = paramQDGraphics.getPixMap();
        setImageData(localPixMap.getPixelData(), new ImageDescription(localPixMap));
        this.gModeOpaque = true;
        paramMovie.task(0);
    }

    private boolean mChanged = false;

    private boolean isPres = false;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param cgp a QDGraphics object
     * @param {@link quicktime.QTException}
     */
    public void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        if (getGWorld().equals(paramQDGraphics))
            return;
        super.setGWorld(paramQDGraphics);
        if (QDGraphics.scratch.equals(paramQDGraphics)) {
            TaskAllMovies.removeMovie();
            this.isPres = false;
            this.m.removeDrawingCompleteProc();
            this.m.setActive(false);
        } else {
            if (QDGraphics.validScratch.equals(paramQDGraphics)) {
                this.isPres = false;
            } else
                this.isPres = true;
            this.m.setDrawingCompleteProc(0, this);
            TaskAllMovies.addMovieAndStart();
            this.m.setActive(true);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the MoviePresenter is presenting its movie to a destination
     * QDGraphics.
     * @return a boolean
     */
    public boolean isPresenting() {
        return this.isPres;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Implements the execute method on the MovieDrawingComplete interface.
     * This method will set the movie changed state to true, so that a consequent
     * call to the TwoDSprite Invalidator will invalidate its presenting sprite.
     * If the MoviePresenter is presenting a movie to a destination QDGraphics
     * it will redraw the movie to that destination.
     *
     * The callback will be triggered as a result of a call to Movie.task and
     * is fired whenever the Movie draws to its internal QDGraphics - the offscreen buffer
     * that the presenter presents.
     * @param m the Movie that is presented by this presenter
     * @return an error code or 0 if no error
     * @see {@link quicktime.std.clocks.QTCallBack},
     * {@link quicktime.std.movies.Movie#setDrawingCompleteProc(int, quicktime.std.movies.MovieDrawingComplete)}
     */
    public int execute(Movie paramMovie) {
        try {
            if (this.isPres)
                doDraw();
            this.mChanged = true;
            if (this.listenerList != null) {
                synchronized (this) {
                    for (int i = 0; i < this.listenerList.size(); i++) ((DrawingListener) this.listenerList.elementAt(i)).drawingComplete(this);
                }
            }
        } catch (QTException localQTException) {
            return localQTException.errorCode();
        }
        return 0;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return the movie that is being presented.
     * @return a movie
     */
    public Movie getMovie() {
        return this.m;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the offscreen buffer that the movie of the MoviePresenter is drawing into.
     * @return a QDGraphics.
     */
    public QDGraphics getOffscreenBuffer() {
        return this.g;
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
    public void redraw(Region paramRegion) throws QTException {
        if (paramRegion != null) {
            this.m.invalidateRegion(paramRegion);
        } else
            this.m.invalidateRegion(new Region(this.m.getBounds()));
        this.m.task(0);
        if ((this.spritePresenter != null) && (this.spritePresenter.isValid())) {
            this.spritePresenter.invalidate();
        }
        super.redraw(paramRegion);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current time base that provides the time foundation
     * of this object.
     * @return a TimeBase
     * @return {@link quicktime.QTException}
     */
    public TimeBase getTimeBase() throws QTException {
        return this.m.getTimeBase();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the Timeable's playback rate. Typically a rate of zero will
     * mean that the player is stopped, a positive value that the player
     * is playing forwards, a negative value that the time values are decreasing.
     * @param rate the new playback rate.
     * @param {@link quicktime.QTException}
     */
    public void setRate(float paramFloat) throws QTException {
        synchronized (this) {
            this.lastRate = paramFloat;
        }
        if (paramFloat == 0.0F) {
            this.m.stop();
        } else
            this.m.prePreroll(this.m.getTime(), paramFloat, new PrerollCallback(paramFloat));
    }

    private float lastRate = 0.0F;

    private Vector listenerList;

    class PrerollCallback implements MoviePrePreroll {

        PrerollCallback(float paramFloat) {
            this.r = paramFloat;
        }

        private float r;

        public void execute(Movie paramMovie, int paramInt) {
            synchronized (MoviePresenter.this) {
                if (MoviePresenter.this.lastRate == 0.0F) {
                    return;
                }
                if (paramInt == 0) {
                    try {
                        paramMovie.preroll(paramMovie.getTime(), this.r);
                        paramMovie.setRate(this.r);
                    } catch (QTException localQTException) {
                        QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), paramMovie, "preroll");
                    }
                } else {
                    QTRuntimeException.handleOrThrow(new QTRuntimeException(paramInt), paramMovie, "prePreroll");
                }
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current rate of the Timeable object.
     * @return the playback rate
     * @return {@link quicktime.QTException}
     */
    public float getRate() throws QTException {
        return this.m.getRate();
    }

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
    public void setTime(int paramInt) throws QTException {
        this.m.setTimeValue(paramInt);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current time value of the object.
     * @return the current time.
     * @return {@link quicktime.QTException}
     */
    public int getTime() throws QTException {
        return this.m.getTime();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the duration of the media attached to the player. If
     * the player has no media, or does not know the duration of its media
     * it can return the constant kDurationUnknown.
     * @return the media duration.
     * @return {@link quicktime.QTException}
     */
    public int getDuration() throws QTException {
        return this.m.getDuration();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the time scale of the player - how many ticks per second.
     * @return the current scale.
     * @return {@link quicktime.QTException}
     */
    public int getScale() throws QTException {
        return this.m.getTimeScale();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the volume setting (0 is silent, 1 is unity gain).
     * @return a float
     * @return {@link quicktime.QTException}
     */
    public float getVolume() throws QTException {
        float f = this.m.getVolume();
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
            this.m.setVolume(f * -1.0F);
        } else {
            this.m.setVolume(f);
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
        return this.m.getVolume() < 0.0F;
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
        float f = Math.abs(this.m.getVolume());
        if (paramBoolean) {
            this.m.setVolume(f * -1.0F);
        } else {
            this.m.setVolume(f);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If an application requires the MoviePresenter to explicitly
     * invalidate its TwoDSprite Presenter, or redraw its offscreen image
     * due to some change in the Movie (other than normal playback of the Movie)
     * then it should signify this by calling this method. Then in the normal
     * course of Invalidation or Redrawing the MoviePresenter will do the correct
     * thing. If the movie has a timeline and is playing this will be done automatically.
     *
     * Thus a potential usage of this method is if the MoviePresenter is presenting a
     * QuickTime VR movie that has no real time line, and has programatically changed
     * some characteristic of the display of the Movie. In which case it needs to inform
     * the MoviePresenter that the Movie has changed and it should do the appropriate
     * invalidation or redraw.
     */
    public void movieChanged() {
        this.mChanged = true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns an object that invalidates a sprite. When a DynamicImage
     * is a member of a SWompositor and the DynamicImage presents a
     * changing image (for example a movie), the TwoDSprite that presents
     * that image in the SWCompositor must be invalidated so that it will
     * redraw the Sprites image in the next composit cycle. The Invalidator
     * object is used to perform this function by the SWCompositor if the object
     * itself cannot perform the invalidation.
     * Can return null if the DynamicImage does not require the SWCompositor
     * to invalidate it (or the object is in a state where its image won't change).
     *
     * The Invalidators provided with objects that implement the DynamicImage
     * interface will execute at the scale and period of the Compositor
     * when they are created.
     * @param c the Compositor in which the DynamicImage is a member
     * @param s the TwoDSprite that is presenting the DynamicImage's image in
     *  the Compositor. This is the sprite that must be invalidated.
     * @return an Invalidator or null if invalidation is not required
     * @return {@link quicktime.QTException}
     * @see quicktime.app.display.SWCompositor
     */
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) throws QTException {
        this.spritePresenter = paramTwoDSprite;
        this.m.setActive(true);
        if (!this.drawCompleteListening) {
            this.m.setDrawingCompleteProc(0, this);
            this.drawCompleteListening = true;
        }
        return new MPInvalidator(this.spritePresenter);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When a dynamic image class is removed from a SWCompositor this
     * method is called to allow the class to cleanup any of the invalidation
     * processes it had established in the addedToCompositor call.
     * @param c the SWCompositor it is being removed from
     * @param {@link quicktime.QTException}
     */
    public void removedFromCompositor(SWCompositor paramSWCompositor) throws QTException {
        if (this.listenerList == null) {
            this.m.removeDrawingCompleteProc();
            this.drawCompleteListening = false;
        }
        this.spritePresenter = null;
        if (QDGraphics.scratch.equals(getGWorld())) {
            this.m.setActive(false);
        }
    }

    private boolean drawCompleteListening = false;

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
        if (!this.drawCompleteListening) {
            try {
                this.m.setDrawingCompleteProc(0, this);
                this.drawCompleteListening = true;
            } catch (StdQTException localStdQTException) {
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
            if (this.spritePresenter == null) {
                try {
                    this.m.removeDrawingCompleteProc();
                    this.drawCompleteListening = false;
                } catch (StdQTException localStdQTException) {
                }
            }
            this.listenerList = null;
        }
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * players/MoviePresenter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
