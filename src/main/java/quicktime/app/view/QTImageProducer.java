package quicktime.app.view;

import java.awt.Dimension;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import quicktime.QTException;
import quicktime.app.time.Timeable;
import quicktime.jdirect.QTNative;
import quicktime.qd.PixMap;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.util.RawEncodedImage;

/**
 * The QTImageProducer implements the java.awt.image.ImageProducer interface for a
 * single source QuickTime object. The QuickTime source can be a single frame (ie. still
 * image) or a mutliple frame source such as a movie, etc.
 * The QTImageProducer can produce images for multiple ImageConsumers.
 *
 * The isRedrawing() method returns true if the source QT object is an object that
 * requires redrawing. In this case the ImageObserver
 * of this Producer should explicitly call the redraw() method and repaint the
 * resultant Image to see any changes in the source of the pixel data. For instance
 * a typical movie will be a sequence of images over time, and for consequent frames
 * to be seen the producer must be redraw. The act of redrawing the producer will also
 * result in the registered consumers being notified and given the changed pixels.
 * Redrawing can also be optimised to only redraw those pixels within a specified region.
 *
 * When the source is originally added to the Producer it is drawn. If the source is not
 * or does not require redrawing then consequent calls
 * to redraw will have no effect as the source pixels have not changed. If
 * the redrawing characteristics of the Presentable source change then
 * you must call setRedrawing(true) to tell the producer that it should redraw the source's
 * pixel data before the redraw method is called. By default an QTImageProduce is seen as
 * potentially presenting dynamic or changing image data.
 */
public class QTImageProducer implements ImageProducer {

    private static final boolean debug = false;

    private static long msecs = 0L;

    private static final boolean profile = false;

    private static boolean requiresRedrawing(Presentable paramPresentable) {
        return ((paramPresentable instanceof Timeable)) || (!paramPresentable.isSingleFrame());
    }

    private Dimension size;

    private int rowInts;

    private QDGraphics gw;

    private PixMap pm;

    private int[] pixels;

    private Presentable qtSource;

    /**
     * Creates an ImageProducer from the specified qtSource. The initialSize of the
     * qtSource should be specified to get the best pixel resolution of the source
     * material.
     * @param qtSource the source of pixel data for the ImageProducer.
     * @param initSize the intial size of the source data
     */
    public QTImageProducer(MoviePlayer paramMoviePlayer, Dimension paramDimension) throws QTException {
        this((Presentable) paramMoviePlayer, paramDimension);
    }

    /**
     * Creates an ImageProducer from the specified qtSource. The initialSize of the
     * qtSource should be specified to get the best pixel resolution of the source
     * material.
     * @param qtSource the source of pixel data for the ImageProducer.
     * @param initSize the intial size of the source data
     */
    public QTImageProducer(GraphicsImporterDrawer paramGraphicsImporterDrawer, Dimension paramDimension) throws QTException {
        this((Presentable) paramGraphicsImporterDrawer, paramDimension);
    }

    QTImageProducer(Presentable paramPresentable, Dimension paramDimension) throws QTException {
        this.qtSource = paramPresentable;
        setRedrawing(requiresRedrawing(paramPresentable));
        this.singleFrame = (!isRedrawing());
        this.size = paramDimension;
        this.gw = new QDGraphics(new QDRect(this.size));
        paramPresentable.setDisplayBounds(new QDRect(this.size));
        paramPresentable.setGWorld(this.gw);
        this.pm = this.gw.getPixMap();
        this.rowInts = (this.pm.getRowBytes() / 4);
        this.pixels = new int[this.rowInts * this.size.height];
        paramPresentable.redraw(null);
        copyPixels();
        if (isSingleFrame()) {
            this.gw.disposeQTObject();
            this.gw = null;
            this.pm = null;
            this.qtSource = null;
        }
    }

    /**
     * The offscreen gworld used by source to draw its content
     * @return the offscreen gworld used by the qtsource
     */
    public QDGraphics getGWorld() {
        return this.gw;
    }

    private ColorModel cm = new DirectColorModel(32, 16711680, 65280, 255, 0);

    private Hashtable props = new Hashtable();

    private Vector consumers = new Vector();

    private boolean doesRedraw;

    private boolean singleFrame;

    /**
     * Returns true if the client only has a single frame to render and thus
     * will never need to re-acquire or redraw its image. Certain client objects
     * are able to optimise their resources knowing this constraint.
     */
    public boolean isSingleFrame() {
        return this.singleFrame;
    }

    public synchronized void addConsumer(ImageConsumer paramImageConsumer) {
        if (isConsumer(paramImageConsumer)) {
            return;
        }
        try {
            this.consumers.addElement(paramImageConsumer);
            paramImageConsumer.setDimensions(this.size.width, this.size.height);
            if (isConsumer(paramImageConsumer)) {
                paramImageConsumer.setProperties(this.props);
            } else {
                return;
            }
            if (isConsumer(paramImageConsumer)) {
                paramImageConsumer.setColorModel(this.cm);
            } else {
                return;
            }
            if (isConsumer(paramImageConsumer)) {
                int i = 14;
                if (isSingleFrame())
                    i |= 0x10;
                paramImageConsumer.setHints(i);
            }
        } catch (Exception localException) {
            if (isConsumer(paramImageConsumer)) {
                paramImageConsumer.imageComplete(1);
            }
        }
    }

    public synchronized boolean isConsumer(ImageConsumer paramImageConsumer) {
        return this.consumers.contains(paramImageConsumer);
    }

    public synchronized void removeConsumer(ImageConsumer paramImageConsumer) {
        this.consumers.removeElement(paramImageConsumer);
    }

    public void startProduction(ImageConsumer paramImageConsumer) {
        synchronized (QTNative.globalsLock) {
            synchronized (this) {
                try {
                    addConsumer(paramImageConsumer);
                    if (isConsumer(paramImageConsumer)) {
                        paramImageConsumer.setPixels(0, 0, this.size.width, this.size.height, this.cm, this.pixels, 0, this.rowInts);
                    } else {
                        return;
                    }
                    if (isConsumer(paramImageConsumer)) {
                        if (isSingleFrame()) {
                            paramImageConsumer.imageComplete(3);
                            if (isConsumer(paramImageConsumer)) {
                                paramImageConsumer.imageComplete(1);
                                removeConsumer(paramImageConsumer);
                            }
                        } else {
                            paramImageConsumer.imageComplete(2);
                        }
                    }
                } catch (Exception localException) {
                    if (isConsumer(paramImageConsumer)) {
                        paramImageConsumer.imageComplete(1);
                    }
                }
            }
        }
    }

    public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer) {
    }

    private synchronized void copyPixels() throws QTException {
        RawEncodedImage localRawEncodedImage = this.pm.getPixelData();
        localRawEncodedImage.copyToArray(0, this.pixels, 0, this.pixels.length);
    }

    /**
     * This method is called to redraw the QuickTime image source and
     * update any of the current ImageConsumers with the new pixels that have been generated.
     * This update will only be done if the redrawing flag is true.
     * @param invalidRgn if null the whole image is redrawn and all of the pixels
     *  are grabbed and sent to the consumers. If not null then the qtSource
     *  invalidates the region specified, which can result in a faster draw. Also only those
     *  pixels that are within the bounds of the region are sent to the consumers.
     * @param {@link quicktime.QTException}
     */
    public synchronized void redraw(Region paramRegion) throws QTException {
        if ((!this.consumers.isEmpty()) || (!isSingleFrame())) {
            this.qtSource.redraw(null);
        } else {
            return;
        }
        updateConsumers(paramRegion);
    }

    /**
     * This method can be called by an application to have the QTImageProducer to update the pixel
     * data for each of the registered ImageConsumers when the source Presentable object has drawn.
     * Thus an application can either explicitly draw the Presentable source itself then call this method
     * or can receive notification by the Presentable source when it has drawn, and use this method to
     * update the registered ImageConsumers.
     * @param invalidRgn enter null to copy all pixels, or specify a region that specifies the changed pixel data,
     *  which can econimize the copying of the pixel data to only those areas that have changed.
     * @param {@link quicktime.QTException}
     */
    public synchronized void updateConsumers(Region paramRegion) throws QTException {
        if (!isSingleFrame())
            copyPixels();
        int m = 0;
        int i;
        int j;
        int k;
        if (paramRegion != null) {
            Object localObject = paramRegion.getRgnBBox();
            i = ((QDRect) localObject).getX();
            j = ((QDRect) localObject).getY();
            k = ((QDRect) localObject).getWidth();
            m = ((QDRect) localObject).getHeight();
        } else {
            i = 0;
            j = 0;
            k = this.size.width;
            m = this.size.height;
        }
        Object localObject = this.consumers.elements();
        while (((Enumeration) localObject).hasMoreElements()) {
            ImageConsumer localImageConsumer = (ImageConsumer) ((Enumeration) localObject).nextElement();
            localImageConsumer.setPixels(i, j, k, m, this.cm, this.pixels, 0, this.rowInts);
            if (isConsumer(localImageConsumer)) {
                localImageConsumer.imageComplete(2);
            }
        }
        this.pixels[0] = 0;
    }

    /**
     * If true then the redraw method of the object will fetch new image data
     * from its image source.
     * @param redrawFlag boolean determining whether the current image is redrawn (false)
     *  of fresh or updated image data is first retrieved (true).
     */
    public synchronized void setRedrawing(boolean paramBoolean) {
        this.doesRedraw = paramBoolean;
    }

    /**
     * Returns true if the image data may be altered or different upon different calls
     * of the redraw method of the Presentable interface
     */
    public boolean isRedrawing() {
        return this.doesRedraw;
    }

    /**
     * This returns the rendered size of the qtSource pixel data as specified in the constructor.
     */
    public Dimension getSize() {
        return new Dimension(this.size);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/view/
 * QTImageProducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
