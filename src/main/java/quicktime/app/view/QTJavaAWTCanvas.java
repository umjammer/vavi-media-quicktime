
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

    public QTJavaAWTCanvas(SGVideoChannel paramSGVideoChannel) throws QTException {
        this();
        this.channel = paramSGVideoChannel;
        setSGChannel(paramSGVideoChannel);
    }

    QTJavaAWTCanvas(Movie paramMovie) throws QTException {
        this();
        setMovie(paramMovie);
    }

    QTJavaAWTCanvas(GraphicsImporter paramGraphicsImporter) throws QTException {
        this();
        setImage(paramGraphicsImporter);
    }

    QTJavaAWTCanvas(MovieController paramMovieController) throws QTException {
        this();
        setMovieController(paramMovieController);
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

    public void displayChanged(int paramInt1, int paramInt2) {
        Rectangle localRectangle = new Rectangle(getLocation(), new Dimension(this.initialWidth, this.initialHeight));
        this.initialWidth = Math.max(paramInt1, 0);
        this.initialHeight = Math.max(paramInt2, 0);
        if (!this.appHasSetPreferredSize) {
            this.prefWidth = this.initialWidth;
            this.prefHeight = this.initialHeight;
        }
    }

    public final QDGraphics getPort() throws QTUnknownOSException, NativeGraphicsException {
        QDGraphics localQDGraphics = null;
        if (isVisible()) {
            localQDGraphics = this.ng != null ? this.ng.getGWorld() : null;
        }
        return localQDGraphics == null ? QDGraphics.scratch : localQDGraphics;
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

    public final QDPoint translatePoint(int paramInt1, int paramInt2) throws QTUnknownOSException, NativeGraphicsException {
        Point localPoint = getNGLocation();

        return new QDPoint(paramInt1 + localPoint.x, paramInt2 + localPoint.y);
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
    public synchronized void setMovieController(MovieController paramMovieController) throws QTException {
        paramMovieController.getMovie().setGWorld(QDGraphics.scratch, null);
        this.qtcHelper.doSetMovieController(paramMovieController,
                                            paramMovieController != null ? paramMovieController.getBounds() : null,
                                            getMinimumSize(),
                                            getMaximumSize(),
                                            false);
    }

    @Override
    public synchronized void setMovie(Movie paramMovie) throws QTException {
        this.qtcHelper.doSetMovie(paramMovie,
                                  paramMovie != null ? paramMovie.getBounds() : null,
                                  getMinimumSize(),
                                  getMaximumSize(),
                                  false);
    }

    @Override
    public synchronized void setImage(GraphicsImporter paramGraphicsImporter) throws QTException {
        this.qtcHelper.doSetGraphicsImporter(paramGraphicsImporter,
                                             paramGraphicsImporter != null ? paramGraphicsImporter.getBoundsRect() : null,
                                             getMinimumSize(),
                                             getMaximumSize(),
                                             false);
    }

    public synchronized void setSGChannel(SGVideoChannel paramSGVideoChannel) throws QTException {
        this.grabber = paramSGVideoChannel.getSequenceGrabber();
        this.qtcHelper.doSetSequenceGrabber(this.grabber,
                                            this.grabber != null ? paramSGVideoChannel.getBounds() : null,
                                            getMinimumSize(),
                                            getMaximumSize(),
                                            false);
    }

    void setInitialWidthAndHeight(QDRect paramQDRect, Dimension paramDimension1, Dimension paramDimension2) {
        this.initialWidth = paramQDRect.getWidth();
        this.initialHeight = paramQDRect.getHeight();

        this.minWidth = Math.max(paramDimension1.width, 0);
        this.minHeight = Math.max(paramDimension1.height, 0);
        this.maxWidth = Math.max(paramDimension2.width, this.minWidth);
        this.maxHeight = Math.max(paramDimension2.height, this.minHeight);
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
        Container localContainer = getParent();
        if (localContainer != null) {
            invalidate();
            localContainer.validate();
            return;
        }
        super.reshape(this.currentX, this.currentY, this.currentWidth, this.currentHeight);
    }

    @Override
    public void setMinimumSize(Dimension paramDimension) {
        if (paramDimension == null) {
            setMinimumSize(0, 0);
        } else {
            setMinimumSize(paramDimension.width, paramDimension.height);
        }
    }

    public void setMinimumSize(int paramInt1, int paramInt2) {
        this.minWidth = Math.max(paramInt1, 0);
        this.minHeight = Math.max(paramInt2, 0);
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
    public void setPreferredSize(Dimension paramDimension) {
        if (paramDimension == null) {
            setPreferredSize(this.initialWidth, this.initialHeight);
        } else {
            setPreferredSize(paramDimension.width, paramDimension.height);
        }
    }

    public void setPreferredSize(int paramInt1, int paramInt2) {
        if (paramInt1 < 0) {
            this.prefWidth = this.initialWidth;
            this.appHasSetPreferredSize = false;
        } else {
            this.prefWidth = paramInt1;
            this.appHasSetPreferredSize = true;
        }

        if (paramInt2 < 0) {
            this.prefHeight = this.initialHeight;
            this.appHasSetPreferredSize = false;
        } else {
            this.prefHeight = paramInt2;
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
    public void setMaximumSize(Dimension paramDimension) {
        if (paramDimension == null) {
            Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
            setMaximumSize(localDimension.width, localDimension.height);
        } else {
            setMaximumSize(paramDimension.width, paramDimension.height);
        }
    }

    public void setMaximumSize(int paramInt1, int paramInt2) {
        this.maxWidth = Math.max(paramInt1, 0);
        this.maxHeight = Math.max(paramInt2, 0);
        if ((this.currentWidth > this.maxWidth) || (this.currentHeight > this.maxHeight)) {
            doParentLayout();
        }
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(this.maxWidth, this.maxHeight);
    }

    private void setCurrentWidthHeight(int paramInt1, int paramInt2) {
        if (paramInt1 < this.minWidth) {
            paramInt1 = this.minWidth;
        } else if (paramInt1 > this.maxWidth)
            paramInt1 = this.maxWidth;
        if (paramInt2 < this.minHeight) {
            paramInt2 = this.minHeight;
        } else if (paramInt2 > this.maxHeight) {
            paramInt2 = this.maxHeight;
        }
        this.currentWidth = paramInt1;
        this.currentHeight = paramInt2;
    }

    private void setCurrentXY(int paramInt1, int paramInt2) {
        this.currentX = paramInt1;
        this.currentY = paramInt2;
    }

    @Override
    public void setSize(Dimension paramDimension) {
        setSize(paramDimension.width, paramDimension.height);
    }

    @Override
    public void setSize(int paramInt1, int paramInt2) {
        setBounds(this.currentX, this.currentY, paramInt1, paramInt2);
    }

    /**
     * @deprecated
     */
    @Override
    public final void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    @Override
    public final synchronized void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        boolean bool = isValid();

        int i = this.currentWidth;
        int j = this.currentHeight;
        setCurrentWidthHeight(paramInt3, paramInt4);
        int k = (i != this.currentWidth) || (j != this.currentHeight) ? 1 : 0;

        int m = this.currentX;
        int n = this.currentY;
        setCurrentXY(paramInt1, paramInt2);

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
    public void paint(Graphics paramGraphics) {
        if ((isValid()) && (this.qtcHelper != null)) {
            QDGraphics localQDGraphics = null;
            Region localRegion = null;
            try {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                    localQDGraphics = getPort();
                    localRegion = localQDGraphics.getClip();
                    localQDGraphics.clipRect();
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
            } catch (QTException localQTException) {
            } finally {
                if ((localQDGraphics != null) && (localRegion != null))
                    localQDGraphics.setClip(localRegion);
            }
        } else {
            doParentLayout();
            repaint();
        }
    }

    private final int countRegionErr = 0;

    ComponentListener clientListener;

    @Override
    public void update(Graphics paramGraphics) {
        super.update(paramGraphics);
        if (QTSession.isCurrentOS(4)) {
            paint(paramGraphics);
        } else if (this.deferRedraw) {
            paint(paramGraphics);
        }
    }

    @Override
    public synchronized void setVisible(boolean paramBoolean) {
        super.setVisible(paramBoolean);
        try {
            if (paramBoolean) {
                this.qtcHelper.doSetGWorld(true);
                doParentLayout();
            } else {
                this.qtcHelper.doSetGWorld(false);
                Container localContainer = getParent();
                if (localContainer != null) {
                    localContainer.repaint();
                }
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this.qtcHelper, "setGWorld");
        }
    }

    public void addQTClientListener(ComponentListener paramComponentListener) {
        this.clientListener = AWTEventMulticaster.add(this.clientListener, paramComponentListener);
    }

    public void removeQTClientListener(ComponentListener paramComponentListener) {
        this.clientListener = AWTEventMulticaster.remove(this.clientListener, paramComponentListener);
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
            } catch (QTException localQTException) {
                this.qtcHelper.doRemoveClient();
                throw new QTRuntimeException(localQTException);
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
                } catch (QTException localQTException) {
                    this.qtcHelper.doRemoveClient();
                    throw new QTRuntimeException(localQTException);
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
