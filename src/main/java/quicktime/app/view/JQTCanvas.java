
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

    public static boolean useMacOSXAcceleration = false;

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

    DirectColorModel colorModel = new DirectColorModel(32, 16711680, 65280, 255, 0);

    public QTImageProducer ip;

    Image image;

    QDGraphics gw = null;

    QDRect gwBounds;

    int[] pixels;

    private ComponentListener clientListener;

    JQTCanvas(Presentable paramPresentable) throws QTException {
        this.initialWidth = 1;
        this.initialHeight = 1;
        this.minWidth = 1;
        this.minHeight = 1;
        this.minWidth = 1;
        this.minHeight = 1;
        this.maxWidth = Integer.MAX_VALUE;
        this.maxHeight = Integer.MAX_VALUE;
        doSetClient(paramPresentable,
                    paramPresentable != null ? paramPresentable.getDisplayBounds() : null,
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
    public void setGraphicsImporterDrawer(GraphicsImporterDrawer paramGraphicsImporterDrawer) throws QTException {
        doSetClient(paramGraphicsImporterDrawer,
                    paramGraphicsImporterDrawer != null ? paramGraphicsImporterDrawer.getDisplayBounds() : null,
                    getMinimumSize(),
                    getMaximumSize(),
                    false);
    }

    @Override
    public void setMoviePlayer(MoviePlayer paramMoviePlayer) throws QTException {
        doSetClient(paramMoviePlayer,
                    paramMoviePlayer != null ? paramMoviePlayer.getDisplayBounds() : null,
                    getMinimumSize(),
                    getMaximumSize(),
                    false);
    }

    @Override
    public void paint(Graphics paramGraphics) {
        if ((!this.inited) && (this.client != null)) {
            this.client.addedTo(this);
            this.inited = true;
            super.paint(paramGraphics);
        }

        if (this.image != null) {
            Dimension localDimension = getSize();
            paramGraphics.drawImage(this.image, 0, 0, localDimension.width, localDimension.height, this);
        }
    }

    public void setFlashScaling(boolean paramBoolean) {
        flashScaling = paramBoolean;
    }

    public boolean isFlashScaling() {
        return flashScaling;
    }

    Image imageFromEncodedImage(RawEncodedImage paramRawEncodedImage, QDRect paramQDRect) {
        paramRawEncodedImage.copyToArray(0, this.pixels, 0, this.pixels.length);
        MemoryImageSource localMemoryImageSource = new MemoryImageSource(paramQDRect
                .getWidth(), paramQDRect.getHeight(), this.colorModel, this.pixels, 0, paramRawEncodedImage.getRowBytes() / 4);

        return getToolkit().createImage(localMemoryImageSource);
    }

    private void removeClient() {
        if ((this.client instanceof DrawingNotifier)) {
            ((DrawingNotifier) this.client).removeDrawingListener(this);
        }
        try {
            this.client.setGWorld(QDGraphics.scratch);
        } catch (QTException localQTException) {
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
        Dimension localDimension = null;
        localDimension = new Dimension(this.minWidth, this.minHeight);
        return localDimension;
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension localDimension = null;
        localDimension = new Dimension(32767, 32767);
        return localDimension;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension localDimension = null;
        localDimension = new Dimension(this.prefWidth, this.prefHeight);
        return localDimension;
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
        this.prefWidth = this.initialWidth;
        this.prefHeight = this.initialHeight;
        logger.log(Level.DEBUG, "setInitialWidthAndHeight " + this.initialWidth + " " + this.initialHeight + " " + this.prefWidth
                               + " " + this.prefHeight);
    }

    private void doSetClient(Presentable paramPresentable,
                             QDRect paramQDRect,
                             Dimension paramDimension1,
                             Dimension paramDimension2,
                             boolean paramBoolean) throws QTException {
        if (paramPresentable == this.client) {
            return;
        }
        if (this.client != null) {
            removeClient();
        }
        if (paramPresentable != null) {
            this.client = paramPresentable;

            if (paramBoolean) {
                setPreferredSize(new Dimension(paramQDRect.getWidth(), paramQDRect.getHeight()));
                setInitialWidthAndHeight(paramQDRect, paramDimension1, paramDimension2);
                this.currentX = paramQDRect.getX();
                this.currentY = paramQDRect.getY();
                this.currentWidth = this.initialWidth;
                this.currentHeight = this.initialHeight;

                doParentLayout();
            }
            setPreferredSize(new Dimension(paramQDRect.getWidth(), paramQDRect.getHeight()));
            setInitialWidthAndHeight(paramQDRect, paramDimension1, paramDimension2);
            this.currentX = paramQDRect.getX();
            this.currentY = paramQDRect.getY();
            this.currentWidth = this.initialWidth;
            this.currentHeight = this.initialHeight;

            doParentLayout();

            this.client.redraw(null);
            this.ip = new QTImageProducer(this.client, new Dimension(paramQDRect.getWidth(), paramQDRect.getHeight()));
            this.image = createImage(this.ip);
            prepareImage(this.image, this);
            if ((this.client instanceof DrawingNotifier))
                ((DrawingNotifier) this.client).addDrawingListener(this);
        }
        repaint();
    }

    @Override
    public Image createImage(ImageProducer paramImageProducer) {
        if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
            boolean bool = false;

            QTImageProducer localQTImageProducer = (QTImageProducer) paramImageProducer;

            Dimension localDimension = localQTImageProducer.getSize();
            QDGraphics localQDGraphics = localQTImageProducer.getGWorld();
            this.gw = localQDGraphics;

            BufferedImage localBufferedImage = (BufferedImage) MacDirectColorModel
                    .GetWrappedGWorldImage(QTObject.ID(localQDGraphics), 32, localDimension.width, localDimension.height, bool);

            return localBufferedImage;
        }

        return super.createImage(paramImageProducer);
    }

    @Override
    public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        if (this.debug) {
            logger.log(Level.DEBUG, "setBounds( " + paramInt1 + ", " + paramInt2 + ", " + paramInt3 + ", " + paramInt4 + ")");
        }
        Dimension localDimension = setCurrentWidthHeight(paramInt3, paramInt4);
        if (flashScaling) {
            setPreferredSize(localDimension);
            this.initialWidth = paramInt3;
            this.initialHeight = paramInt4;
            try {
                this.client.redraw(null);
                this.ip = new QTImageProducer(this.client, localDimension);
                this.image = createImage(this.ip);
                prepareImage(this.image, this);
            } catch (QTException localQTException) {
                localQTException.printStackTrace();
            }
            repaint();
        }

        this.currentX = paramInt1;
        this.currentY = paramInt2;

        super.reshape(this.currentX, this.currentY, localDimension.width, localDimension.height);
    }

    /**
     * @deprecated
     */
    @Override
    public final void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        logger.log(Level.DEBUG, "reshape( " + paramInt1 + ", " + paramInt2 + ", " + paramInt3 + ", " + paramInt4 + ")");
        setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private void doParentLayout() {
        logger.log(Level.DEBUG, "doParentLayout()");
        Container localContainer = getParent();
        if (localContainer != null) {
            invalidate();
            localContainer.validate();
            return;
        }
        super.reshape(this.currentX, this.currentY, this.currentWidth, this.currentHeight);
    }

    private Dimension setCurrentWidthHeight(int paramInt1, int paramInt2) {
        if (this.debug) {
            logger.log(Level.DEBUG, "setCurrentWidthHeight( " + paramInt1 + ", " + paramInt2 + ", " + this.minWidth + ", "
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
        this.currentWidth = paramInt1;
        this.currentHeight = paramInt2;
        return new Dimension(this.currentWidth, this.currentHeight);
    }

    @Override
    public void drawingComplete(Presentable paramPresentable) {
        if ((QTSession.isCurrentOS(4)) && (useMacOSXAcceleration)) {
            repaint();
            return;
        }
        try {
            this.ip.updateConsumers(null);
        } catch (QTException localQTException) {
            localQTException.printStackTrace();
        }
    }

    @Override
    public JComponent asJComponent() {
        return this;
    }

    public void addQTClientListener(ComponentListener paramComponentListener) {
        this.clientListener = AWTEventMulticaster.add(this.clientListener, paramComponentListener);
    }

    public void removeQTClientListener(ComponentListener paramComponentListener) {
        this.clientListener = AWTEventMulticaster.remove(this.clientListener, paramComponentListener);
    }
}
