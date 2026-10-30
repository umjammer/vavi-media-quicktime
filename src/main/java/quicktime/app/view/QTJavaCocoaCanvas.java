
package quicktime.app.view;

import com.apple.eawt.CocoaComponent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.jdirect.QTNative;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.sg.SGVideoChannel;


class QTJavaCocoaCanvas extends CocoaComponent implements QTComponent, KeyListener, MouseListener {

    private static final Logger logger = System.getLogger(QTJavaCocoaCanvas.class.getName());

    private static final int kSetMovie = 0;
    private static final int kSetImage = 1;
    private static final int kSetMovieController = 2;
    private static final int kSetSeqGrabber = 3;

    static {
        if (!QTSession.isCurrentOS(4))
            throw new QTRuntimeException("OS not supported");
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() < 65540)) {
            throw new QTRuntimeException("Java version not supported");
        }

        String str = "/System/Library/QuickTimeJava/QuickTimeJava.bundle/Contents/Resources/libCocoaInterface.jnilib";
        System.load(str);

        initGlobalLock(QTNative.globalsLock);
    }

    private int qtjavaView = 0;

    private boolean addDone = false;

    private final boolean viewShown = false;

    private Movie movie;

    private MovieController controller;

    private GraphicsImporter image;

    private SGVideoChannel sgChannel;

    private int currentX;

    private int currentY;

    private int currentWidth;

    private int currentHeight;

    private int minWidth;

    private int minHeight;

    private int maxWidth;

    private int maxHeight;

    private final QDRect oldbounds = null;

    transient int initialWidth;

    transient int initialHeight;

    private transient int prefWidth;

    private transient int prefHeight;

    private boolean performanceResize = false;

    private final boolean QTMOVIEVIEW = false;

    private WindowAdapter wa;

    private Window parentWindow;

    public int createNSView() {
        QDRect rect = new QDRect(0, 0, 1, 1);
        Object localObject = null;
        if (this.movie != null) {
            try {
                if (this.controller != null) {
                    try {
                        rect = this.controller.getBounds();
                        logger.log(Level.DEBUG, "createNSView controller " + this.controller + "bounds " + rect);
                    } catch (QTException _) {
                    }
                } else {
                    rect = this.movie.getBounds();
                }
                setInitialWidthAndHeight(rect, getMinimumSize(), getMaximumSize());
            } catch (StdQTException _) {
            }
            return this.qtjavaView = createNSViewNative(rect.getWidthF(),
                                                        rect.getHeightF(),
                                                        QTObject.ID(this.movie),
                                                        this.controller != null ? QTObject.ID(this.controller)
                                                                                : null);
        }

        if (this.image != null) {
            try {
                rect = this.image.getBoundsRect();
                setInitialWidthAndHeight(rect, getMinimumSize(), getMaximumSize());
            } catch (StdQTException _) {
            }
            return this.qtjavaView = createImageNSViewNative(rect.getWidth(),
                                                             rect.getHeight(),
                    QTObject.ID(this.image));
        }
        if (this.sgChannel != null) {
            try {
                if ((this.sgChannel instanceof SGVideoChannel))
                    rect = this.sgChannel.getBounds();
                setInitialWidthAndHeight(rect, getMinimumSize(), getMaximumSize());
                logger.log(Level.DEBUG, "***createNSView sgChannel " + this.sgChannel + ", " + rect);
            } catch (StdQTException _) {
            }
            return this.qtjavaView = createSequenceGrabberNSView(rect.getWidth(),
                                                                 rect.getHeight(),
                    QTObject.ID(this.sgChannel.getSequenceGrabber()));
        }
        return 0;
    }

    QTJavaCocoaCanvas(MovieController controller) throws QTException {
        this();
        this.movie = controller.getMovie();
        this.controller = controller;
        logger.log(Level.DEBUG, "QTJavaCocoaCanvas " + this.movie + ", " + this.controller);
    }

    QTJavaCocoaCanvas(Movie movie) {
        this();
        this.movie = movie;
        this.controller = null;
        this.performanceResize = false;
        logger.log(Level.DEBUG, "QTJavaCocoaCanvas " + this.movie);
    }

    QTJavaCocoaCanvas(GraphicsImporter image) {
        this();
        this.image = image;
        logger.log(Level.DEBUG, "QTJavaCocoaCanvas " + image);
    }

    QTJavaCocoaCanvas(SGVideoChannel sgChannel) {
        this();
        this.sgChannel = sgChannel;
        logger.log(Level.DEBUG, "QTJavaCocoaCanvas " + this.sgChannel);
    }

    private QTJavaCocoaCanvas() {
        this.initialWidth = 1;
        this.initialHeight = 1;
        this.minWidth = 1;
        this.minHeight = 1;
        this.maxWidth = Integer.MAX_VALUE;
        this.maxHeight = Integer.MAX_VALUE;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (!this.addDone) {
            logger.log(Level.DEBUG, "addNotify qtjavaView " + this.qtjavaView);
            addedTo();
            QTSession.addActiveCanvas(this);
            this.addDone = true;
        }
        if (isVisible()) {
            repaint();
        }
    }

    @Override
    public synchronized void removeNotify() {
        logger.log(Level.DEBUG, "removeNotify:");
        if ((this.addDone) && (QTSession.isInitialized())) {
            try {
                if (this.movie != null)
                    doRemoveDrawable();
                QTSession.removeActiveCanvas(this);
                logger.log(Level.DEBUG, "removeNotify:");
                this.addDone = false;
            } finally {
                super.removeNotify();
            }
        } else
            super.removeNotify();
    }

    private void doParentLayout() {
        logger.log(Level.DEBUG, "doParentLayout:" + getParent());
        Container parent = getParent();
        if (parent != null) {
            invalidate();
            parent.validate();
        }

        super.setBounds(this.currentX, this.currentY, this.currentWidth, this.currentHeight);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        logger.log(Level.DEBUG, "setBounds: " + width + "," + height);
        super.setBounds(x, y, width, height);
    }

    private void setCurrentXY(int x, int y, int width, int height) {
        int i = width - this.currentWidth;
        if (i < 0) {
            i = 0;
        }

        int j = height - this.currentHeight;
        if (j < 0) {
            j = 0;
        }
        if (this.performanceResize) {
            this.currentX = (x + (int) (0.5F * i));
            this.currentY = (y + (int) (0.5F * j));
        } else {
            this.currentX = x;
            this.currentY = y;
        }
        logger.log(Level.DEBUG, "setCurrentXY " + this.currentX + " " + this.currentY);
    }

    @Override
    public float getAlignmentX() {
        return this.performanceResize ? 0.5F : 0.0F;
    }

    @Override
    public float getAlignmentY() {
        return this.performanceResize ? 0.5F : 0.0F;
    }

    private void setCurrentWidthHeight(int currentWidth, int currentHeight) {
        if (currentWidth < this.minWidth) {
            currentWidth = this.minWidth;
        } else if (currentWidth > this.maxWidth)
            currentWidth = this.maxWidth;
        if (currentHeight < this.minHeight) {
            currentHeight = this.minHeight;
        } else if (currentHeight > this.maxHeight) {
            currentHeight = this.maxHeight;
        }
        if (this.performanceResize) {
            setPerformanceResize(currentWidth, currentHeight);
        } else {
            this.currentWidth = currentWidth;
            this.currentHeight = currentHeight;
        }

        logger.log(Level.DEBUG, "setCurrentWidthHeight( " + this.currentWidth + ", " + this.currentHeight + ", " + this.minWidth
                               + ", " + this.minHeight);
    }

    private void setInitialWidthAndHeight(QDRect rect, Dimension minSize, Dimension maxSize) {
        this.initialWidth = rect.getWidth();
        this.initialHeight = rect.getHeight();

        this.minWidth = Math.max(minSize.width, 0);
        this.minHeight = Math.max(minSize.height, 0);
        this.maxWidth = Math.max(maxSize.width, this.minWidth);
        this.maxHeight = Math.max(maxSize.height, this.minHeight);
        if (this.initialWidth < this.minWidth) {
            this.initialWidth = this.minWidth;
        } else if (this.initialWidth > this.maxWidth) {
            this.initialWidth = this.maxWidth;
        }
        if (this.initialHeight < this.minHeight) {
            this.initialHeight = this.minHeight;
        } else if (this.initialHeight > this.maxHeight) {
            this.initialHeight = this.maxHeight;
        }
        this.prefWidth = this.initialWidth;
        this.prefHeight = this.initialHeight;
        logger.log(Level.DEBUG, "setInitialWidthAndHeight " + this.initialWidth + " " + this.initialHeight + " " + this.prefWidth
                               + " " + this.prefHeight);
    }

    private void setPerformanceResize(int i, int j) {
        boolean flag = false;
label0:
        for (int k1 = 2; !flag; k1 *= 2) {
            if (i < initialWidth || j < initialHeight) {
                int k = initialWidth / k1;
                int i1 = initialHeight / k1;
                if (k > i || i1 > j)
                    continue;
                flag = true;
                boolean flag1 = false;
                currentWidth = k;
                currentHeight = i1;
                do {
                    if (!flag1)
                        continue label0;
                    if (currentWidth >= minWidth && currentHeight >= minHeight)
                        flag1 = true;
                    currentWidth *= 2;
                    currentHeight *= 2;
                } while (true);
            }
            int l = initialWidth * k1;
            int j1 = initialHeight * k1;
            if (l < i && j1 < j)
                continue;
            flag = true;
            if (l > i || j1 > j) {
                l /= 2;
                j1 /= 2;
            }
            boolean flag2 = false;
            currentWidth = l;
            currentHeight = j1;
            do {
                if (!flag2)
                    continue label0;
                if (currentWidth <= maxWidth && currentHeight <= maxHeight)
                    flag2 = true;
                currentWidth /= 2;
                currentHeight /= 2;
            } while (true);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = null;
        preferredSize = new Dimension(this.prefWidth, this.prefHeight);
        logger.log(Level.DEBUG, "getPreferredSize " + preferredSize);
        return preferredSize;
    }

    public Dimension getBestSize() {
        Dimension bestSize = null;
        bestSize = new Dimension(this.initialWidth, this.initialHeight);
        logger.log(Level.DEBUG, "getBestSize " + bestSize);
        return bestSize;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension minimumSize = null;
        minimumSize = new Dimension(this.minWidth, this.minHeight);
        logger.log(Level.DEBUG, "getMinimumSize " + minimumSize);
        return minimumSize;
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension maximumSize = null;
        maximumSize = new Dimension(32767, 32767);
        logger.log(Level.DEBUG, "getMaximumSize " + maximumSize);
        return maximumSize;
    }

    private void setFullScreen() {
        setFullScreen((byte) 1);
    }

    public void removeMovie() throws QTException {
        if (this.movie != null) {
            this.movie.setRate(0.0F);
            this.movie.stop();
        }
    }

    @Override
    public Movie getMovie() {
        return this.movie;
    }

    @Override
    public MovieController getMovieController() {
        return this.controller;
    }

    @Override
    public void setMovieController(MovieController movieController) throws QTException {
        QDRect rect = null;
        if (movieController == this.controller) {
            return;
        }
        doRemoveDrawable();

        this.controller = movieController;
        this.movie = movieController.getMovie();

        if (movieController != null) {
            try {
                rect = movieController.getBounds();
                if ((this.qtjavaView != 0) && (isValid())) {
                    sendMessage(2, QTObject.ID(movieController));
                    setInitialWidthAndHeight(rect, getMinimumSize(), getMaximumSize());
                }
            } catch (QTException _) {
            }
            if ((this.qtjavaView != 0) && (isValid())) {
                addedTo();
            }
        }
    }

    @Override
    public void setMovie(Movie mov) throws QTException {
        QDRect rect = null;
        if (mov == this.movie) {
            return;
        }
        logger.log(Level.DEBUG, "setMovie " + mov);
        doRemoveDrawable();

        this.movie = mov;
        if (mov != null) {
            rect = this.movie.getBounds();
            if ((this.qtjavaView != 0) && (isValid()))
                sendMessage(0, QTObject.ID(this.movie));
            setInitialWidthAndHeight(rect, getMinimumSize(), getMaximumSize());
        }
    }

    public void setSGChannel(SGVideoChannel sgChannel) throws QTException {
        QDRect rect = new QDRect(0, 0, 1, 1);
        if (sgChannel == this.sgChannel) {
            return;
        }
        logger.log(Level.DEBUG, "setSGChannel " + this.sgChannel);
        if (this.sgChannel != null) {
            doRemoveDrawable();
        }
        if (sgChannel != null) {
            this.sgChannel = sgChannel;
            if ((this.sgChannel instanceof SGVideoChannel))
                rect = this.sgChannel.getBounds();
            if ((this.qtjavaView != 0) && (isValid()))
                sendMessage(3, QTObject.ID(this.sgChannel.getSequenceGrabber()));
            setInitialWidthAndHeight(rect, getMinimumSize(), getMaximumSize());
        }
    }

    @Override
    public Component asComponent() {
        return this;
    }

    void doRemoveDrawable() {
        QDGraphics qdGraphics = null;
        logger.log(Level.DEBUG, "doRemoveDrawable:");
        try {
            qdGraphics = QDGraphics.scratch;
            if (this.movie != null && QTSession.isInitialized()) {
                this.movie.setRate(0.0F);
                this.movie.stop();
            }

            if ((this.qtjavaView != 0) && (isValid())) {
                if (this.movie != null) {
                    if (this.controller != null)
                        sendMessage(2, null);
                    if (this.movie != null)
                        sendMessage(0, null);
                }
                if (this.image != null) {
                    sendMessage(1, null);
                }
            }
        } catch (QTException e) {
            e.printStackTrace();
        } finally {
            if (this.controller != null)
                removedFrom();
            logger.log(Level.DEBUG, "finally:");
            this.movie = null;
            this.controller = null;
            this.image = null;
        }
    }

    @Override
    public GraphicsImporter getImage() {
        return this.image;
    }

    @Override
    public void setImage(GraphicsImporter img) {
        QDRect rect = null;
        if (this.image == img) {
            return;
        }
        doRemoveDrawable();

        this.image = img;
        if (img != null) {
            try {
                rect = this.image.getBoundsRect();
                setInitialWidthAndHeight(rect, getMinimumSize(), getMaximumSize());
            } catch (StdQTException e) {
            }
            if ((this.qtjavaView != 0) && (isValid()))
                sendMessage(1, QTObject.ID(this.image));
            doParentLayout();
        }
        repaint();
    }

    protected void addedTo() {
        try {
            logger.log(Level.DEBUG, "addedTo " + this.controller.getPort());
        } catch (StdQTException _) {
        }
        if (this.controller != null) {
            addKeyListener(this);
        }
        Object parentWindow = getParent();
        Object parent = parentWindow;
        while (parent != null) {
            parentWindow = parent;
            if ((parentWindow instanceof Window))
                break;
            parent = ((Container) parentWindow).getParent();
        }
        if (parentWindow != null)
            ((Container) parentWindow).addKeyListener(this);
        try {
            if (this.controller != null) {
                this.controller.setKeysEnabled(true);
            }
        } catch (StdQTException _) {
        }
        if ((parentWindow instanceof Window)) {
            this.parentWindow = ((Window) parentWindow);
            this.wa = new WindowAdapter() {
                @Override
                public void windowActivated(WindowEvent e) {
                    try {
                        if (QTJavaCocoaCanvas.this.controller != null) {
                            QTJavaCocoaCanvas.this.controller.activate(QTJavaCocoaCanvas.this.controller.getPort(), true);
                            QTJavaCocoaCanvas.this.controller.activate();
                        }
                        QTJavaCocoaCanvas.this.redraw(null);
                    } catch (QTException _) {
                    }
                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                    synchronized (QTSession.terminationLock()) {
                        if (QTSession.isInitialized()) {
                            try {
                                if (QTJavaCocoaCanvas.this.controller != null) {
                                    QTJavaCocoaCanvas.this.controller.deactivate();
                                    QTJavaCocoaCanvas.this.controller.activate(QTJavaCocoaCanvas.this.controller.getPort(),
                                                                               false);
                                }
                                QTJavaCocoaCanvas.this.redraw(null);
                            } catch (QTException _) {
                            }
                        } else {
                            QTJavaCocoaCanvas.this.removedFrom();
                        }
                    }
                }
            };
            this.parentWindow.addWindowListener(this.wa);
        }
    }

    protected void removedFrom() {
        removeKeyListener(this);
        if (getParent() != null)
            getParent().removeKeyListener(this);
        if (this.parentWindow != null) {
            this.parentWindow.removeWindowListener(this.wa);
            this.parentWindow = null;
            this.wa = null;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            logger.log(Level.DEBUG, "Key pressed " + e);
            if (this.controller != null) {
                boolean bool = this.controller.key(e.getKeyCode(), e.getModifiers());
            }
        } catch (QTException ex) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(ex), this, "keyPressed");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        logger.log(Level.DEBUG, "mousePressed " + e);
        try {
            Object parent = getParent();
            Object localObject2 = parent;
            while (localObject2 != null) {
                logger.log(Level.DEBUG, localObject2 + " getLocation " + ((Container) localObject2).getLocation() + " getBounds "
                                       + ((Container) localObject2).getBounds());
                if (((localObject2 instanceof Window)) || ((localObject2 instanceof Frame)))
                    break;
                parent = localObject2;
                localObject2 = ((Container) parent).getParent();
            }
            logger.log(Level.DEBUG, parent + " getLocation " + ((Container) parent).getLocation() + " getBounds "
                                   + ((Container) parent).getBounds());
            logger.log(Level.DEBUG, e.getX() + ((Container) parent).getLocation().x + getLocation().x
                               + ", "
                               + (e.getY()
                                  + (((Container) parent).getLocation().y == 22 ? ((Container) parent)
                                          .getLocation().y : ((Container) parent).getLocation().y
                                                             - 22)
                                  + getLocation().y));

            if (this.controller != null)
                this.controller.click(
                                      new QDPoint(e.getX() + ((Container) parent).getLocation().x
                                                  + getLocation().x,
                                                  ((Container) parent).getLocation().y != 22
                                                                                                   ? e.getY()
                                                                                                     + (((Container) parent)
                                                                                                             .getLocation().y
                                                                                                        - 22)
                                                                                                     + getLocation().y
                                                                                                   : e
                                                                                                           .getY()),
                                      e.getModifiers());
        } catch (QTException ex) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(ex), this, "mousePressed");
        }
    }

    private void redraw(Region invalidRgn) throws QTException {
        logger.log(Level.DEBUG, "RD:");
        if (invalidRgn != null) {
            try {
                if (this.controller != null) {
                    this.controller.invalidate(this.controller.getPort(), invalidRgn);
                    this.controller.idle();
                }
            } catch (StdQTException e) {
                if (this.controller != null) {
                    this.controller.draw();
                }
            }
        } else if (this.controller != null) {
            this.controller.draw();
        }
    }

    private static void initGlobalLock(Object lock) {
        PlatformManager.initGlobalLock(lock);
    }

    private static void initGlobalOffScreen(Object offScreen) {
        PlatformManager.initGlobalOffScreen(offScreen);
    }

    private void setFullScreen(byte fullScreen) {
        PlatformManager.setFullScreen(fullScreen);
    }

    private void setMovie(int movie, Object movieObject) {
        PlatformManager.setMovie(movie, movieObject);
    }

    private void setMovieController(int controller, Object controllerObject) {
        PlatformManager.setMovieController(controller, controllerObject);
    }

    private int createNSViewNative(float width, float height, Object movieView, Object callback) {
        return PlatformManager.createNSViewNative(width, height, movieView, callback);
    }

    private int createImageNSViewNative(int width, int height, Object callback) {
        return PlatformManager.createImageNSViewNative(width, height, callback);
    }

    private int createSequenceGrabberNSView(int width, int height, Object callback) {
        return PlatformManager.createSequenceGrabberNSView(width, height, callback);
    }

    private Object getMovieController(int movie) {
        return PlatformManager.getMovieController(movie);
    }

    private void removeMovie(int movie) {
        PlatformManager.removeMovie(movie);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
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
}
