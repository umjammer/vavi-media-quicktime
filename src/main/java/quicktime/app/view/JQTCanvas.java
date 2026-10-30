
package quicktime.app.view;

import java.awt.AWTEventMulticaster;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageProducer;
import vavi.awt.image.MacDirectColorModel;
import java.awt.image.MemoryImageSource;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import javax.swing.JComponent;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.util.RawEncodedImage;


class JQTCanvas extends JComponent implements DrawingListener, QTJComponent {

    private static final Logger logger = System.getLogger(JQTCanvas.class.getName());

    public static boolean flashScaling = false;

    public static final boolean useMacOSXAcceleration = false;

    transient Presentable client;

    private int initialWidth = 0;

    private int initialHeight = 0;

    private int minWidth = 0;

    private int minHeight = 0;

    private int maxWidth;

    private int maxHeight;

    private int currentWidth;

    private int currentHeight;

    private int currentX;

    private int currentY;

    private transient int prefWidth;

    private transient int prefHeight;

    private transient boolean inited = false;

    private int resizeFlag;

    private final boolean debug = false;

    private final boolean paintlock = false;

    final DirectColorModel colorModel = new DirectColorModel(32, 16711680, 65280, 255, 0);

    public QTImageProducer ip;

    Image image;

    QDGraphics gw = null;

    QDRect gwBounds;

    int[] pixels;

    private ComponentListener clientListener;

    JQTCanvas(Presentable client) throws QTException {
        this.initialWidth = 1;
        this.initialHeight = 1;
        this.minWidth = 1;
        this.minHeight = 1;
        this.minWidth = 1;
        this.minHeight = 1;
        this.maxWidth = Integer.MAX_VALUE;
        this.maxHeight = Integer.MAX_VALUE;
        doSetClient(client,
                    client != null ? client.getDisplayBounds() : null,
                    getMinimumSize(),
                    getMaximumSize(),
                    true);
    }

    @Override
    public MoviePlayer getMoviePlayer() {
        return (this.client instanceof MoviePlayer) ? (MoviePlayer) this.client : null;
    }

    @Override
    public GraphicsImporterDrawer getGraphicsImporterDrawer() {
        return (this.client instanceof GraphicsImporterDrawer) ? (GraphicsImporterDrawer) this.client : null;
    }

    @Override
    public void setGraphicsImporterDrawer(GraphicsImporterDrawer img) throws QTException {
        doSetClient(img,
                    img != null ? img.getDisplayBounds() : null,
                    getMinimumSize(),
                    getMaximumSize(),
                    false);
    }

    @Override
    public void setMoviePlayer(MoviePlayer movPlayer) throws QTException {
        doSetClient(movPlayer,
                    movPlayer != null ? movPlayer.getDisplayBounds() : null,
                    getMinimumSize(),
                    getMaximumSize(),
                    false);
    }

    @Override
    public void paint(Graphics graphics) {
        if ((!this.inited) && (this.client != null)) {
            this.client.addedTo(this);
            this.inited = true;
            super.paint(graphics);
        }

        if (this.image != null) {
            Dimension size = getSize();
            graphics.drawImage(this.image, 0, 0, size.width, size.height, this);
        }
    }

    public void setFlashScaling(boolean flashScaling) {
        flashScaling = flashScaling;
    }

    public boolean isFlashScaling() {
        return flashScaling;
    }

    Image imageFromEncodedImage(RawEncodedImage rawEncodedImage, QDRect rect) {
        rawEncodedImage.copyToArray(0, this.pixels, 0, this.pixels.length);
        MemoryImageSource memoryImageSource = new MemoryImageSource(rect
                .getWidth(), rect.getHeight(), this.colorModel, this.pixels, 0, rawEncodedImage.getRowBytes() / 4);

        return getToolkit().createImage(memoryImageSource);
    }

    private void removeClient() {
        if ((this.client instanceof DrawingNotifier)) {
            ((DrawingNotifier) this.client).removeDrawingListener(this);
        }
        try {
            this.client.setGWorld(QDGraphics.scratch);
        } catch (QTException e) {
        } finally {
            this.client.removedFrom(this);
            this.client = null;
        }
        if (QTSession.isCurrentOS(4))
            this.gw = null;
        if (this.image != null) {
            this.image = null;
        }
        repaint();
    }

    public Dimension getBestSize() {
        return new Dimension(this.initialWidth, this.initialHeight);
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension minimumSize = null;
        minimumSize = new Dimension(this.minWidth, this.minHeight);
        return minimumSize;
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension maximumSize = null;
        maximumSize = new Dimension(32767, 32767);
        return maximumSize;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = null;
        preferredSize = new Dimension(this.prefWidth, this.prefHeight);
        return preferredSize;
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
        this.prefWidth = this.initialWidth;
        this.prefHeight = this.initialHeight;
        logger.log(Level.DEBUG, "setInitialWidthAndHeight " + this.initialWidth + " " + this.initialHeight + " " + this.prefWidth
                               + " " + this.prefHeight);
    }

    private void doSetClient(Presentable client,
                             QDRect rect,
                             Dimension minSize,
                             Dimension maxSize,
                             boolean doLayout) throws QTException {
        if (client == this.client) {
            return;
        }
        if (this.client != null) {
            removeClient();
        }
        if (client != null) {
            this.client = client;

            if (doLayout) {
                setPreferredSize(new Dimension(rect.getWidth(), rect.getHeight()));
                setInitialWidthAndHeight(rect, minSize, maxSize);
                this.currentX = rect.getX();
                this.currentY = rect.getY();
                this.currentWidth = this.initialWidth;
                this.currentHeight = this.initialHeight;

                doParentLayout();
            }
            setPreferredSize(new Dimension(rect.getWidth(), rect.getHeight()));
            setInitialWidthAndHeight(rect, minSize, maxSize);
            this.currentX = rect.getX();
            this.currentY = rect.getY();
            this.currentWidth = this.initialWidth;
            this.currentHeight = this.initialHeight;

            doParentLayout();

            this.client.redraw(null);
            this.ip = new QTImageProducer(this.client, new Dimension(rect.getWidth(), rect.getHeight()));
            this.image = createImage(this.ip);
            prepareImage(this.image, this);
            if ((this.client instanceof DrawingNotifier))
                ((DrawingNotifier) this.client).addDrawingListener(this);
        }
        repaint();
    }

    @Override
    public Image createImage(ImageProducer imageProducer) {
        if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
            boolean bool = false;

            QTImageProducer qtImageProducer = (QTImageProducer) imageProducer;

            Dimension size = qtImageProducer.getSize();
            QDGraphics gw = qtImageProducer.getGWorld();
            this.gw = gw;

            BufferedImage image = (BufferedImage) MacDirectColorModel
                    .GetWrappedGWorldImage(QTObject.ID(gw), 32, size.width, size.height, bool);

            return image;
        }

        return super.createImage(imageProducer);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        if (this.debug) {
            logger.log(Level.DEBUG, "setBounds( " + x + ", " + y + ", " + width + ", " + height + ")");
        }
        Dimension dimension = setCurrentWidthHeight(width, height);
        if (flashScaling) {
            setPreferredSize(dimension);
            this.initialWidth = width;
            this.initialHeight = height;
            try {
                this.client.redraw(null);
                this.ip = new QTImageProducer(this.client, dimension);
                this.image = createImage(this.ip);
                prepareImage(this.image, this);
            } catch (QTException e) {
                e.printStackTrace();
            }
            repaint();
        }

        this.currentX = x;
        this.currentY = y;

        super.reshape(this.currentX, this.currentY, dimension.width, dimension.height);
    }

    /**
     * @deprecated
     */
    @Override
    public final void reshape(int x, int y, int width, int height) {
        logger.log(Level.DEBUG, "reshape( " + x + ", " + y + ", " + width + ", " + height + ")");
        setBounds(x, y, width, height);
    }

    private void doParentLayout() {
        logger.log(Level.DEBUG, "doParentLayout()");
        Container parent = getParent();
        if (parent != null) {
            invalidate();
            parent.validate();
            return;
        }
        super.reshape(this.currentX, this.currentY, this.currentWidth, this.currentHeight);
    }

    private Dimension setCurrentWidthHeight(int width, int height) {
        if (this.debug) {
            logger.log(Level.DEBUG, "setCurrentWidthHeight( " + width + ", " + height + ", " + this.minWidth + ", "
                               + this.minHeight);
        }

        if (this.currentWidth < this.minWidth) {
            this.currentWidth = this.minWidth;
        } else if (this.currentWidth > this.maxWidth)
            this.currentWidth = this.maxWidth;
        if (this.currentHeight < this.minHeight) {
            this.currentHeight = this.minHeight;
        } else if (this.currentHeight > this.maxHeight) {
            this.currentHeight = this.maxHeight;
        }
        this.currentWidth = width;
        this.currentHeight = height;
        return new Dimension(this.currentWidth, this.currentHeight);
    }

    @Override
    public void drawingComplete(Presentable presentable) {
        if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
            repaint();
            return;
        }
        try {
            this.ip.updateConsumers(null);
        } catch (QTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JComponent asJComponent() {
        return this;
    }

    public void addQTClientListener(ComponentListener componentListener) {
        this.clientListener = AWTEventMulticaster.add(this.clientListener, componentListener);
    }

    public void removeQTClientListener(ComponentListener componentListener) {
        this.clientListener = AWTEventMulticaster.remove(this.clientListener, componentListener);
    }
}
