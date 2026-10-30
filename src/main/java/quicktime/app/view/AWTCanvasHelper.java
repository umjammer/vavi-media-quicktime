
package quicktime.app.view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.time.TaskThread;
import quicktime.app.time.Tasking;
import quicktime.qd.QDException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.MoviePrePreroll;
import quicktime.std.sg.SGVideoChannel;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.QTUtils;


class AWTCanvasHelper extends Tasking implements java.awt.event.MouseListener, KeyListener {
    private static final boolean debug = false;

    private TaskThread tt;

    private boolean deferTasking = false;

    private static final TaskThread sgTasker = new TaskThread("Default SG Tasker", 20);

    private final QTJavaAWTCanvas canvas;

    AWTCanvasHelper(QTJavaAWTCanvas canvas) {
        this.canvas = canvas;
    }

    private Window parentWindow;

    private WindowAdapter wa;

    @Override
    public final void task() throws QTException {
        if (this.canvas.movieController != null) {
            this.canvas.movieController.idle();
        } else if (this.canvas.movie != null) {
            this.canvas.movie.task(0);
        } else if ((this.canvas.grabber != null) && (!this.canvas.grabber.idleMore())) {

            stopTasking();
            this.canvas.grabber.stop();
        }
    }

    private float lastRate = 0.0F;

    void setRate(float rate) throws StdQTException {
        if (this.canvas.movieController != null) {
            if (rate != 0.0F) {
                this.canvas.movieController.prerollAndPlay(rate);
            } else
                this.canvas.movieController.play(rate);
        } else if (this.canvas.movie != null) {
            synchronized (this) {
                this.lastRate = rate;
            }
            if (rate == 0.0F) {
                this.canvas.movie.stop();
            } else
                this.canvas.movie.prePreroll(this.canvas.movie.getTime(), rate, new PrerollCallback(rate));
        }
    }

    class PrerollCallback implements MoviePrePreroll {
        private final float r;

        PrerollCallback(float rate) {
            this.r = rate;
        }

        @Override
        public void execute(Movie m, int err) {
            synchronized (this) {
                if (AWTCanvasHelper.this.lastRate == 0.0F) {
                    return;
                }
                if (err == 0) {
                    try {
                        m.preroll(m.getTime(), this.r);
                        m.setRate(this.r);
                    } catch (QTException e) {
                        QTRuntimeException.handleOrThrow(new QTRuntimeException(e), m, "preroll");
                    }
                } else {
                    QTRuntimeException.handleOrThrow(new QTRuntimeException(err), m, "prePreroll");
                }
            }
        }
    }

    protected void addedTo() {
        if (this.canvas.movieController != null) {
            this.canvas.addMouseListener(this);
            this.canvas.addKeyListener(this);
        }
        Object parentWindow = this.canvas.getParent();
        Object parent = parentWindow;
        while (parent != null) {
            parentWindow = parent;
            if ((parentWindow instanceof Window))
                break;
            parent = ((Container) parentWindow).getParent();
        }
        if ((parentWindow instanceof Window)) {
            this.parentWindow = ((Window) parentWindow);
            this.wa = new WindowAdapter() {
                @Override
                public void windowActivated(WindowEvent e) {
                    if (QTSession.isInitialized())
                        try {
                            if (AWTCanvasHelper.this.canvas.movieController != null) {

                                AWTCanvasHelper.this.canvas.movieController.activate(AWTCanvasHelper.this.getGWorld(), true);
                                AWTCanvasHelper.this.canvas.movieController.activate();
                            } else if (AWTCanvasHelper.this.canvas.movie != null) {

                                AWTCanvasHelper.this.canvas.movie.setActive(true);
                            }
                            AWTCanvasHelper.this.redraw(null);
                        } catch (QTException ex) {
                        }
                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                    if (QTSession.isInitialized()) {
                        try {
                            if (AWTCanvasHelper.this.canvas.movieController != null) {
                                AWTCanvasHelper.this.canvas.movieController.deactivate();
                                AWTCanvasHelper.this.canvas.movieController.activate(AWTCanvasHelper.this.getGWorld(), false);
                            }
                            AWTCanvasHelper.this.redraw(null);

                        } catch (QTException ex) {
                        }
                    } else {
                        AWTCanvasHelper.this.removedFrom();
                    }
                }
            };
            this.parentWindow.addWindowListener(this.wa);
        }
    }

    protected void removedFrom() {
        if (this.canvas.movieController != null) {
            this.canvas.removeMouseListener(this);
            this.canvas.removeKeyListener(this);
        }

        if (this.parentWindow != null) {
            this.parentWindow.removeWindowListener(this.wa);
            this.parentWindow = null;
            this.wa = null;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            if (this.canvas.movieController != null) {

                this.canvas.movieController.key(e.getKeyCode(), e.getModifiers());
            }
        } catch (QTException ex) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(ex), this, "keyPressed");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            if (this.canvas.movieController != null) {

                this.canvas.movieController.click(new quicktime.qd.QDPoint(e.getX(), e.getY()),
                                                  e.getModifiers());
            }
        } catch (QTException ex) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(ex), this, "mousePressed");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    void doRemoveClient() {
        try {
            if ((this.canvas.movieController != null) || (this.canvas.movie != null)
                || (this.canvas.graphicsImporter != null)) {
                if (QTSession.isInitialized())
                    doSetGWorld(false);
                removedFrom();
            }
        } catch (QTException e) {
        } finally {
            this.canvas.movieController = null;
            this.canvas.graphicsImporter = null;
            this.canvas.movie = null;
        }
    }

    void doSetSequenceGrabber(SequenceGrabber sequenceGrabber,
                              QDRect rect,
                              Dimension minSize,
                              Dimension maxSize,
                              boolean doLayout) throws QTException {
        setDefaultTasker(sgTasker);
        this.canvas.grabber.setGWorld(QDGraphics.scratch, null);
        this.canvas.setInitialWidthAndHeight(rect, this.canvas.getMinimumSize(), this.canvas.getMaximumSize());
        int i = this.canvas.currentWidth;
        int j = this.canvas.currentHeight;
        doSetBounds();

        if (sequenceGrabber != null) {
            try {
                doSetGWorld(this.canvas.isVisible());
                if (this.canvas.isDisplayable()) {
                    addedTo();
                    this.canvas.repaint();
                }
                if (this.canvas.isVisible()) {
                    if ((!this.canvas.isWin) || (!doLayout)
                        || ((i == this.canvas.currentWidth) && (j == this.canvas.currentHeight))) {
                        this.canvas.deferRedraw = true;
                    } else
                        this.canvas.repaint();
                }
            } catch (QTException e) {
                removedFrom();
                throw e;
            }
        } else {
            this.canvas.repaint();
        }
    }

    void doSetGraphicsImporter(GraphicsImporter graphicsImporter,
                               QDRect rect,
                               Dimension minSize,
                               Dimension maxSize,
                               boolean doLayout) throws QTException {
        doRemoveClient();

        if (graphicsImporter != null) {
            int i = this.canvas.currentWidth;
            int j = this.canvas.currentHeight;
            this.canvas.graphicsImporter = graphicsImporter;

            this.canvas.setInitialWidthAndHeight(rect, this.canvas.getMinimumSize(), this.canvas.getMaximumSize());
            doSetBounds();
            try {
                doSetGWorld(this.canvas.isVisible());
                if (this.canvas.isDisplayable()) {
                    addedTo();
                    this.canvas.repaint();
                }
                if (this.canvas.isVisible()) {
                    if ((!this.canvas.isWin) || (!doLayout)
                        || ((i == this.canvas.currentWidth) && (j == this.canvas.currentHeight))) {
                        this.canvas.deferRedraw = true;
                    } else
                        this.canvas.repaint();
                }
            } catch (QTException e) {
                removedFrom();
                throw e;
            }
        } else {
            this.canvas.repaint();
        }
    }

    void doSetMovieController(MovieController movieController,
                              QDRect rect,
                              Dimension minSize,
                              Dimension maxSize,
                              boolean doLayout) throws QTException {
        if (movieController == this.canvas.movieController) {
            return;
        }

        doRemoveClient();

        if (movieController != null) {
            int i = this.canvas.currentWidth;
            int j = this.canvas.currentHeight;
            this.canvas.movieController = movieController;
            this.canvas.movie = movieController.getMovie();

            this.canvas.setInitialWidthAndHeight(rect, this.canvas.getMinimumSize(), this.canvas.getMaximumSize());
            doSetBounds();
            try {
                doSetGWorld(this.canvas.isVisible());
                this.canvas.movieController.setKeysEnabled(true);
                if (this.canvas.isDisplayable()) {
                    addedTo();
                    this.canvas.repaint();
                }
                if (this.canvas.isVisible()) {
                    if ((!this.canvas.isWin) || (!doLayout)
                        || ((i == this.canvas.currentWidth) && (j == this.canvas.currentHeight))) {
                        this.canvas.deferRedraw = true;
                    } else
                        this.canvas.repaint();
                }
            } catch (QTException e) {
                removedFrom();
                throw e;
            }
        } else {
            this.canvas.repaint();
        }
    }

    void doSetMovie(Movie movie,
                    QDRect rect,
                    Dimension minSize,
                    Dimension maxSize,
                    boolean doLayout) throws QTException {
        if (movie == this.canvas.movie) {
            return;
        }

        doRemoveClient();

        if (movie != null) {
            this.canvas.movie = movie;
            int i = this.canvas.currentWidth;
            int j = this.canvas.currentHeight;

            this.canvas.setInitialWidthAndHeight(rect, this.canvas.getMinimumSize(), this.canvas.getMaximumSize());
            doSetBounds();
            try {
                doSetGWorld(this.canvas.isVisible());
                if (this.canvas.isDisplayable()) {
                    addedTo();
                    this.canvas.repaint();
                }
                if (this.canvas.isVisible()) {
                    if ((!this.canvas.isWin) || (!doLayout)
                        || ((i == this.canvas.currentWidth) && (j == this.canvas.currentHeight))) {
                        this.canvas.deferRedraw = true;
                    } else
                        this.canvas.repaint();
                }
            } catch (QTException e) {
                removedFrom();
                throw e;
            }
        } else {
            this.canvas.repaint();
        }
    }

    synchronized void setControllerGWorld(MovieController movieController, QDGraphics grafPtr) throws QDException,
                                                                                                            StdQTException {
        this.canvas.movieController.getMovie().setGWorld(QDGraphics.scratch, null);
        if (this.canvas.movieController.getVisible()) {
            this.canvas.movieController.setPort(grafPtr);
        } else {
            this.canvas.movieController.getMovie().setGWorld(grafPtr, null);
        }

        if (!QDGraphics.scratch.equals(grafPtr)) {
            startTasking();

            movieController.getMovie().setActive(true);
        } else {
            setRate(0.0F);
            stopTasking();
            movieController.getMovie().setActive(false);
        }
    }

    synchronized void setMovieGWorld(QDGraphics movieGWorld) throws QDException, StdQTException {
        this.canvas.movie.setGWorld(movieGWorld, null);
        if (QDGraphics.scratch.equals(movieGWorld)) {
            stopTasking();
            setRate(0.0F);
            this.canvas.movie.setActive(false);
        } else {
            startTasking();
            this.canvas.movie.setActive(true);
        }
    }

    void doSetGWorld(boolean displayable) throws QTException {
        QDGraphics port;
        if ((displayable) && (this.canvas.isDisplayable())) {
            port = this.canvas.getPort();
        } else {
            port = QDGraphics.scratch;
        }
        if (this.canvas.movieController != null) {
            MovieController movieController = this.canvas.movieController;
            QDGraphics qdGraphics2 = movieController.getPort();
            if (!port.equals(qdGraphics2)) {
                setControllerGWorld(movieController, port);
            }
        } else if (this.canvas.movie != null) {
            setMovieGWorld(port);
        } else if (this.canvas.graphicsImporter != null) {
            this.canvas.graphicsImporter.setGWorld(port, null);
        } else if (this.canvas.grabber != null) {
            this.canvas.grabber.setGWorld(port, null);
        }

        if (displayable) {
            QTUtils.checkFreeMemory();
            if (this.canvas.clientListener != null) {
                this.canvas.clientListener.componentShown(new ComponentEvent(this.canvas, 102));
            }
        } else if (this.canvas.clientListener != null) {
            this.canvas.clientListener.componentHidden(new ComponentEvent(this.canvas, 103));
        }
    }

    boolean shouldRedrawQTPlayer(Rectangle rectangle) {
        if ((!this.canvas.isWin) && (this.canvas.isShowing()) && (!rectangle.equals(this.canvas.getBounds()))) {
            return true;
        }

        return false;
    }

    final void redraw(Region invalidRgn) throws QTException {
        if (this.canvas.movieController != null) {
            if (invalidRgn != null) {
                try {
                    this.canvas.movieController.invalidate(getGWorld(), invalidRgn);
                } catch (StdQTException e) {
                    this.canvas.movieController.draw();
                }
            } else {
                this.canvas.movieController.draw();
                this.canvas.movieController.idle();
            }
        } else if (this.canvas.movie != null) {
            if (invalidRgn != null) {
                this.canvas.movie.invalidateRegion(invalidRgn);
            } else
                this.canvas.movie.invalidateRegion(new Region(this.canvas.movie.getBounds()));
            this.canvas.movie.update();
            this.canvas.movie.task(0);
        } else {
            Region clip;
            if (this.canvas.graphicsImporter != null) {
                if (invalidRgn != null) {
                    clip = this.canvas.graphicsImporter.getClip();
                    this.canvas.graphicsImporter.setClip(invalidRgn);
                    this.canvas.graphicsImporter.draw();
                    this.canvas.graphicsImporter.setClip(clip);
                } else {
                    this.canvas.graphicsImporter.draw();
                }
            } else if (this.canvas.grabber != null) {
                if (invalidRgn != null) {
                    if ((this.canvas.channel instanceof SGVideoChannel)) {
                        clip = ((SGVideoChannel) this.canvas.channel).getClip();
                        ((SGVideoChannel) this.canvas.channel).setClip(invalidRgn);
                        this.canvas.grabber.update(invalidRgn);
                        ((SGVideoChannel) this.canvas.channel).setClip(clip);
                    }
                } else {
                    this.canvas.grabber.update(null);
                }
            }
        }
    }

    public QDGraphics getGWorld() throws StdQTException {
        if (this.canvas.movieController != null)
            return this.canvas.movieController.getPort();
        if (this.canvas.movie != null)
            return this.canvas.movie.getGWorld();
        if (this.canvas.graphicsImporter != null)
            return this.canvas.graphicsImporter.getGWorld();
        if (this.canvas.grabber != null) {
            return this.canvas.grabber.getGWorld();
        }

        return QDGraphics.scratch;
    }

    void doSetBounds() throws QTException {
        Point ngLocation = this.canvas.getNGLocation();

        if (this.canvas.movie != null) {
            if (this.canvas.movieController != null) {
                if ((this.canvas.movieController.getVisible()) && (this.canvas.isVisible()))
                    this.canvas.movieController
                            .setBounds(new QDRect(ngLocation.x,
                                                  ngLocation.y,
                                                  this.canvas.currentWidth,
                                                  this.canvas.currentHeight));
            } else
                this.canvas.movie
                        .setBounds(new QDRect(ngLocation.x, ngLocation.y, this.canvas.currentWidth, this.canvas.currentHeight));
        } else if (this.canvas.graphicsImporter != null) {
            this.canvas.graphicsImporter
                    .setBoundsRect(new QDRect(ngLocation.x, ngLocation.y, this.canvas.currentWidth, this.canvas.currentHeight));
        } else if (this.canvas.channel != null) {
            ((SGVideoChannel) this.canvas.channel)
                    .setBounds(new QDRect(ngLocation.x, ngLocation.y, this.canvas.currentWidth, this.canvas.currentHeight));
            if (!QDGraphics.scratch.equals(getGWorld())) {
                this.deferTasking = false;
                startTasking();
            }
        }
    }
}
