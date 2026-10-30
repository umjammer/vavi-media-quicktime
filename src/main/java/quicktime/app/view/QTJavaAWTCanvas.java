
package quicktime.app.view;

import java.awt.AWTEventMulticaster;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentListener;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.QTUnknownOSException;
import quicktime.jdirect.QTNative;
import quicktime.qd.NativeGraphics;
import quicktime.qd.NativeGraphicsException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.sg.SGChannel;
import quicktime.std.sg.SGVideoChannel;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.QTUtils;


class QTJavaAWTCanvas extends Canvas implements QTComponent {
    private static boolean MRJWorkaroundFlag;

    private static final boolean debug = false;

    static {
        if (QTSession.isCurrentOS(2)) {
            MRJWorkaroundFlag = false;
        } else if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
            String str = System.getProperty("java.version");
            int i = Character.getNumericValue(str.charAt(0));

            int j = 0;
            int k = 0;
            int m = 0;

            for (int n = 1; n < str.length(); n++) {
                char c = str.charAt(n);
                if (Character.isDigit(c)) {
                    if (m == 0) {
                        j = Character.getNumericValue(c);
                        m = 1;
                    } else {
                        k = Character.getNumericValue(c);
                    }
                }
            }

            if ((i == 1) && (j == 1) && (k < 8)) {
                MRJWorkaroundFlag = true;
            } else {
                MRJWorkaroundFlag = false;
            }
        }
    }

    private static int canvasCount = 0;

    transient int numCanvas;

    private transient Vector mControllers;

    private AWTCanvasHelper qtcHelper;

    public QTJavaAWTCanvas(SGVideoChannel channel) throws QTException {
        this();
        this.channel = channel;
        setSGChannel(channel);
    }

    QTJavaAWTCanvas(Movie mov) throws QTException {
        this();
        setMovie(mov);
    }

    QTJavaAWTCanvas(GraphicsImporter img) throws QTException {
        this();
        setImage(img);
    }

    QTJavaAWTCanvas(MovieController movieController) throws QTException {
        this();
        setMovieController(movieController);
    }

    private QTJavaAWTCanvas() {
        this.initialWidth = 1;
        this.initialHeight = 1;
        this.minWidth = 1;
        this.minHeight = 1;
        this.maxWidth = Integer.MAX_VALUE;
        this.maxHeight = Integer.MAX_VALUE;
        this.numCanvas = (canvasCount++);

        this.qtcHelper = new AWTCanvasHelper(this);
    }

    private boolean doRedrawForQTPlayer = false;

    transient int initialWidth;

    transient int initialHeight;

    int currentX;

    int currentY;

    private int minWidth;

    private int minHeight;

    private int maxWidth;

    private int maxHeight;

    private transient int prefWidth;

    private transient int prefHeight;

    int currentWidth;

    int currentHeight;

    transient NativeGraphics ng = null;

    private final transient long msecs = 0L;

    transient boolean deferResize = false;

    private transient boolean appHasSetPreferredSize = false;

    final boolean isWin = QTSession.isCurrentOS(2);

    private boolean addDone = false;

    boolean deferRedraw = false;

    Movie movie;

    MovieController movieController;

    GraphicsImporter graphicsImporter;

    SequenceGrabber grabber;

    SGChannel channel;

    private boolean moved;

    public void displayChanged(int width, int height) {
        Rectangle rectangle = new Rectangle(getLocation(), new Dimension(this.initialWidth, this.initialHeight));
        this.initialWidth = Math.max(width, 0);
        this.initialHeight = Math.max(height, 0);
        if (!this.appHasSetPreferredSize) {
            this.prefWidth = this.initialWidth;
            this.prefHeight = this.initialHeight;
        }
    }

    public final QDGraphics getPort() throws QTUnknownOSException, NativeGraphicsException {
        QDGraphics qdGraphics = null;
        if (isVisible()) {
            qdGraphics = this.ng != null ? this.ng.getGWorld() : null;
        }
        return qdGraphics == null ? QDGraphics.scratch : qdGraphics;
    }

    public final Point getNGLocation() throws QTUnknownOSException, NativeGraphicsException {
        if (this.qtcHelper != null) {
            return this.ng != null ? this.ng.getLocation() : new Point(0, 0);
        }
        return getLocation();
    }

    public final NativeGraphics getNativeGraphics() {
        return this.ng;
    }

    public final QDPoint translatePoint(int x, int y) throws QTUnknownOSException, NativeGraphicsException {
        Point ngLocation = getNGLocation();

        return new QDPoint(x + ngLocation.x, y + ngLocation.y);
    }

    @Override
    public Movie getMovie() {
        return this.movie;
    }

    @Override
    public MovieController getMovieController() {
        return this.movieController;
    }

    @Override
    public GraphicsImporter getImage() {
        return this.graphicsImporter;
    }

    @Override
    public synchronized void setMovieController(MovieController movieController) throws QTException {
        movieController.getMovie().setGWorld(QDGraphics.scratch, null);
        this.qtcHelper.doSetMovieController(movieController,
                                            movieController != null ? movieController.getBounds() : null,
                                            getMinimumSize(),
                                            getMaximumSize(),
                                            false);
    }

    @Override
    public synchronized void setMovie(Movie mov) throws QTException {
        this.qtcHelper.doSetMovie(mov,
                                  mov != null ? mov.getBounds() : null,
                                  getMinimumSize(),
                                  getMaximumSize(),
                                  false);
    }

    @Override
    public synchronized void setImage(GraphicsImporter img) throws QTException {
        this.qtcHelper.doSetGraphicsImporter(img,
                                             img != null ? img.getBoundsRect() : null,
                                             getMinimumSize(),
                                             getMaximumSize(),
                                             false);
    }

    public synchronized void setSGChannel(SGVideoChannel sgChannel) throws QTException {
        this.grabber = sgChannel.getSequenceGrabber();
        this.qtcHelper.doSetSequenceGrabber(this.grabber,
                                            this.grabber != null ? sgChannel.getBounds() : null,
                                            getMinimumSize(),
                                            getMaximumSize(),
                                            false);
    }

    void setInitialWidthAndHeight(QDRect rect, Dimension minSize, Dimension maxSize) {
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
        if (!this.appHasSetPreferredSize) {
            this.prefWidth = this.initialWidth;
            this.prefHeight = this.initialHeight;
        }
    }

    final void doParentLayout() {
        Container parent = getParent();
        if (parent != null) {
            invalidate();
            parent.validate();
            return;
        }
        super.reshape(this.currentX, this.currentY, this.currentWidth, this.currentHeight);
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        if (minimumSize == null) {
            setMinimumSize(0, 0);
        } else {
            setMinimumSize(minimumSize.width, minimumSize.height);
        }
    }

    public void setMinimumSize(int width, int height) {
        this.minWidth = Math.max(width, 0);
        this.minHeight = Math.max(height, 0);
        if ((this.currentWidth < this.minWidth) || (this.currentHeight < this.minHeight)) {
            doParentLayout();
        }
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(this.minWidth, this.minHeight);
    }

    public Dimension getBestSize() {
        return new Dimension(this.initialWidth, this.initialHeight);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.prefWidth, this.prefHeight);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if (preferredSize == null) {
            setPreferredSize(this.initialWidth, this.initialHeight);
        } else {
            setPreferredSize(preferredSize.width, preferredSize.height);
        }
    }

    public void setPreferredSize(int width, int height) {
        if (width < 0) {
            this.prefWidth = this.initialWidth;
            this.appHasSetPreferredSize = false;
        } else {
            this.prefWidth = width;
            this.appHasSetPreferredSize = true;
        }

        if (height < 0) {
            this.prefHeight = this.initialHeight;
            this.appHasSetPreferredSize = false;
        } else {
            this.prefHeight = height;
            this.appHasSetPreferredSize = true;
        }

        if ((this.currentWidth != this.prefWidth) || (this.currentHeight != this.prefHeight)) {
            doParentLayout();
        }
    }

    @Override
    public boolean isFocusTraversable() {
        return true;
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        if (maximumSize == null) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setMaximumSize(screenSize.width, screenSize.height);
        } else {
            setMaximumSize(maximumSize.width, maximumSize.height);
        }
    }

    public void setMaximumSize(int width, int height) {
        this.maxWidth = Math.max(width, 0);
        this.maxHeight = Math.max(height, 0);
        if ((this.currentWidth > this.maxWidth) || (this.currentHeight > this.maxHeight)) {
            doParentLayout();
        }
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(this.maxWidth, this.maxHeight);
    }

    private void setCurrentWidthHeight(int width, int height) {
        if (width < this.minWidth) {
            width = this.minWidth;
        } else if (width > this.maxWidth)
            width = this.maxWidth;
        if (height < this.minHeight) {
            height = this.minHeight;
        } else if (height > this.maxHeight) {
            height = this.maxHeight;
        }
        this.currentWidth = width;
        this.currentHeight = height;
    }

    private void setCurrentXY(int x, int y) {
        this.currentX = x;
        this.currentY = y;
    }

    @Override
    public void setSize(Dimension size) {
        setSize(size.width, size.height);
    }

    @Override
    public void setSize(int width, int height) {
        setBounds(this.currentX, this.currentY, width, height);
    }

    /**
     * @deprecated
     */
    @Override
    public final void reshape(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
    }

    @Override
    public final synchronized void setBounds(int x, int y, int width, int height) {
        boolean bool = isValid();

        int i = this.currentWidth;
        int j = this.currentHeight;
        setCurrentWidthHeight(width, height);
        int k = (i != this.currentWidth) || (j != this.currentHeight) ? 1 : 0;

        int m = this.currentX;
        int n = this.currentY;
        setCurrentXY(x, y);

        super.reshape(this.currentX, this.currentY, this.currentWidth, this.currentHeight);

        if (this.movie != null) {
            this.deferResize = ((this.deferResize) || (k != 0));
            if (this.deferResize) {
                this.doRedrawForQTPlayer = ((!this.isWin) && (!bool) && (isShowing()));
            }

            this.moved = ((m != this.currentX) || (n != this.currentY) || (this.moved));
        }
    }

    @Override
    public void paint(Graphics graphics) {
        if ((isValid()) && (this.qtcHelper != null)) {
            QDGraphics port = null;
            Region clip = null;
            try {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                    port = getPort();
                    clip = port.getClip();
                    port.clipRect();
                }
                if ((this.movie != null) || (this.graphicsImporter != null) || (this.grabber != null)) {
                    this.qtcHelper.doSetBounds();
                    if (!this.qtcHelper.getGWorld().equals(QDGraphics.scratch)) {
                        QTUtils.checkFreeMemory();
                        this.qtcHelper.redraw(null);
                    } else if (isShowing()) {
                        QTUtils.checkFreeMemory();
                        this.qtcHelper.redraw(null);
                    }
                }
            } catch (QTException e) {
            } finally {
                if ((port != null) && (clip != null))
                    port.setClip(clip);
            }
        } else {
            doParentLayout();
            repaint();
        }
    }

    private final int countRegionErr = 0;

    ComponentListener clientListener;

    @Override
    public void update(Graphics graphics) {
        super.update(graphics);
        if (QTSession.isCurrentOS(4)) {
            paint(graphics);
        } else if (this.deferRedraw) {
            paint(graphics);
        }
    }

    @Override
    public synchronized void setVisible(boolean visible) {
        super.setVisible(visible);
        try {
            if (visible) {
                this.qtcHelper.doSetGWorld(true);
                doParentLayout();
            } else {
                this.qtcHelper.doSetGWorld(false);
                Container parent = getParent();
                if (parent != null) {
                    parent.repaint();
                }
            }
        } catch (QTException e) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(e), this.qtcHelper, "setGWorld");
        }
    }

    public void addQTClientListener(ComponentListener componentListener) {
        this.clientListener = AWTEventMulticaster.add(this.clientListener, componentListener);
    }

    public void removeQTClientListener(ComponentListener componentListener) {
        this.clientListener = AWTEventMulticaster.remove(this.clientListener, componentListener);
    }

    @Override
    public synchronized void addNotify() {
        super.addNotify();
        if (!this.addDone) {
            try {
                synchronized (QTNative.globalsLock) {
                    this.ng = NativeGraphics.getContext(this);
                    if ((this.movie != null) || (this.graphicsImporter != null) || (this.grabber != null)) {
                        this.qtcHelper.doSetGWorld(isVisible());
                        this.qtcHelper.addedTo();
                        if (isVisible()) {
                            if (!this.isWin) {
                                this.deferRedraw = true;
                            } else
                                repaint();
                        }
                    }
                    QTSession.addActiveCanvas(this);
                }
            } catch (QTException e) {
                this.qtcHelper.doRemoveClient();
                throw new QTRuntimeException(e);
            }
            this.addDone = true;
        }
    }

    @Override
    public synchronized void removeNotify() {
        synchronized (QTSession.terminationLock()) {
            if ((this.addDone) && (QTSession.isInitialized())) {
                try {
                    this.qtcHelper.doSetGWorld(false);
                    this.qtcHelper.removedFrom();
                    QTSession.removeActiveCanvas(this);
                } catch (QTException e) {
                    this.qtcHelper.doRemoveClient();
                    throw new QTRuntimeException(e);
                } finally {
                    if ((QTSession.isCurrentOS(2)) && (this.ng != null)) {
                        synchronized (QTNative.globalsLock) {
                            QTJavaJNIAWTCanvas.RemoveNewContext(this.ng);
                        }
                    }

                    this.ng = null;
                    this.addDone = false;
                    super.removeNotify();
                }
            } else {
                super.removeNotify();
            }
        }
    }

    @Override
    public Component asComponent() {
        return this;
    }

    public String toString() {
        return getClass().getName() + "_" + this.numCanvas + "_" + "[Location=" + getLocation() + ",Size=" + getSize() + "]";
    }

    protected void dispose() throws Throwable {
        try {
            removeNotify();
        } finally {
            this.movie = null;
            this.movieController = null;
            this.graphicsImporter = null;
            this.grabber = null;
            this.channel = null;
            this.qtcHelper = null;
        }
    }
}
