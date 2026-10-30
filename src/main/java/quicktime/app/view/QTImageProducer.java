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

    private static final long msecs = 0L;

    private static final boolean profile = false;

    private static boolean requiresRedrawing(Presentable presentable) {
        return ((presentable instanceof Timeable)) || (!presentable.isSingleFrame());
    }

    private final Dimension size;

    private final int rowInts;

    private QDGraphics gw;

    private PixMap pm;

    private final int[] pixels;

    private Presentable qtSource;

    /**
     * Creates an ImageProducer from the specified qtSource. The initialSize of the
     * qtSource should be specified to get the best pixel resolution of the source
     * material.
     * @param qtSource the source of pixel data for the ImageProducer.
     * @param initSize the intial size of the source data
     */
    public QTImageProducer(MoviePlayer qtSource, Dimension initSize) throws QTException {
        this((Presentable) qtSource, initSize);
    }

    /**
     * Creates an ImageProducer from the specified qtSource. The initialSize of the
     * qtSource should be specified to get the best pixel resolution of the source
     * material.
     * @param qtSource the source of pixel data for the ImageProducer.
     * @param initSize the intial size of the source data
     */
    public QTImageProducer(GraphicsImporterDrawer qtSource, Dimension initSize) throws QTException {
        this((Presentable) qtSource, initSize);
    }

    QTImageProducer(Presentable qtSource, Dimension size) throws QTException {
        this.qtSource = qtSource;
        setRedrawing(requiresRedrawing(qtSource));
        this.singleFrame = (!isRedrawing());
        this.size = size;
        this.gw = new QDGraphics(new QDRect(this.size));
        qtSource.setDisplayBounds(new QDRect(this.size));
        qtSource.setGWorld(this.gw);
        this.pm = this.gw.getPixMap();
        this.rowInts = (this.pm.getRowBytes() / 4);
        this.pixels = new int[this.rowInts * this.size.height];
        qtSource.redraw(null);
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

    private final ColorModel cm = new DirectColorModel(32, 16711680, 65280, 255, 0);

    private final Hashtable<String, String> props = new Hashtable<>();

    private final Vector<ImageConsumer> consumers = new Vector<>();

    private boolean doesRedraw;

    private final boolean singleFrame;

    /**
     * Returns true if the client only has a single frame to render and thus
     * will never need to re-acquire or redraw its image. Certain client objects
     * are able to optimise their resources knowing this constraint.
     */
    public boolean isSingleFrame() {
        return this.singleFrame;
    }

    /**
     * This method is used to register an ImageConsumer with the
     * ImageProducer for access to the image data during a later
     * reconstruction of the Image.  The ImageProducer may, at its
     * discretion, start delivering the image data to the consumer
     * using the ImageConsumer interface immediately, or when the
     * next available image reconstruction is triggered by a call
     * to the startProduction method.
     * @see quicktime.app.view.QTImageProducer#startProduction(java.awt.image.ImageConsumer)
     */
    @Override
    public synchronized void addConsumer(ImageConsumer imageConsumer) {
        if (isConsumer(imageConsumer)) {
            return;
        }
        try {
            this.consumers.addElement(imageConsumer);
            imageConsumer.setDimensions(this.size.width, this.size.height);
            if (isConsumer(imageConsumer)) {
                imageConsumer.setProperties(this.props);
            } else {
                return;
            }
            if (isConsumer(imageConsumer)) {
                imageConsumer.setColorModel(this.cm);
            } else {
                return;
            }
            if (isConsumer(imageConsumer)) {
                int i = 14;
                if (isSingleFrame())
                    i |= 0x10;
                imageConsumer.setHints(i);
            }
        } catch (Exception e) {
            if (isConsumer(imageConsumer)) {
                imageConsumer.imageComplete(1);
            }
        }
    }

    /**
     * This method determines if a given ImageConsumer object
     * is currently registered with this ImageProducer as one
     * of its consumers.
     */
    @Override
    public synchronized boolean isConsumer(ImageConsumer imageConsumer) {
        return this.consumers.contains(imageConsumer);
    }

    /**
     * This method removes the given ImageConsumer object
     * from the list of consumers currently registered to
     * receive image data.  It is not considered an error
     * to remove a consumer that is not currently registered.
     * The ImageProducer should stop sending data to this
     * consumer as soon as is feasible.
     */
    @Override
    public synchronized void removeConsumer(ImageConsumer imageConsumer) {
        this.consumers.removeElement(imageConsumer);
    }

    /**
     * This method both registers the given ImageConsumer object
     * as a consumer and starts an immediate reconstruction of
     * the image data which will then be delivered to this
     * consumer and any other consumer which may have already
     * been registered with the producer.  This method differs
     * from the addConsumer method in that a reproduction of
     * the image data should be triggered as soon as possible.
     * @see quicktime.app.view.QTImageProducer#addConsumer(java.awt.image.ImageConsumer)
     */
    @Override
    public void startProduction(ImageConsumer imageConsumer) {
        synchronized (QTNative.globalsLock) {
            synchronized (this) {
                try {
                    addConsumer(imageConsumer);
                    if (isConsumer(imageConsumer)) {
                        imageConsumer.setPixels(0, 0, this.size.width, this.size.height, this.cm, this.pixels, 0, this.rowInts);
                    } else {
                        return;
                    }
                    if (isConsumer(imageConsumer)) {
                        if (isSingleFrame()) {
                            imageConsumer.imageComplete(3);
                            if (isConsumer(imageConsumer)) {
                                imageConsumer.imageComplete(1);
                                removeConsumer(imageConsumer);
                            }
                        } else {
                            imageConsumer.imageComplete(2);
                        }
                    }
                } catch (Exception e) {
                    if (isConsumer(imageConsumer)) {
                        imageConsumer.imageComplete(1);
                    }
                }
            }
        }
    }

    /**
     * This method is used by an ImageConsumer to request that
     * the ImageProducer attempt to resend the image data one
     * more time in TOPDOWNLEFTRIGHT order so that higher
     * quality conversion algorithms which depend on receiving
     * pixels in order can be used to produce a better output
     * version of the image.  The ImageProducer is free to
     * ignore this call if it cannot resend the data in that
     * order.  If the data can be resent, then the ImageProducer
     * should respond by executing the following minimum set of
     * ImageConsumer method calls:
     * <pre>
     * ic.setHints(TOPDOWNLEFTRIGHT | < otherhints >);
     * ic.setPixels(...); // As many times as needed
     * ic.imageComplete();
     * </pre>
     * @see ImageConsumer#setHints(int)
     */
    @Override
    public void requestTopDownLeftRightResend(ImageConsumer imageConsumer) {
    }

    private synchronized void copyPixels() throws QTException {
        RawEncodedImage pixelData = this.pm.getPixelData();
        pixelData.copyToArray(0, this.pixels, 0, this.pixels.length);
    }

    /**
     * This method is called to redraw the QuickTime image source and
     * update any of the current ImageConsumers with the new pixels that have been generated.
     * This update will only be done if the redrawing flag is true.
     * @param invalidRgn if null the whole image is redrawn and all of the pixels
     *  are grabbed and sent to the consumers. If not null then the qtSource
     *  invalidates the region specified, which can result in a faster draw. Also only those
     *  pixels that are within the bounds of the region are sent to the consumers.
     * @throws quicktime.QTException
     */
    public synchronized void redraw(Region invalidRgn) throws QTException {
        if ((!this.consumers.isEmpty()) || (!isSingleFrame())) {
            this.qtSource.redraw(null);
        } else {
            return;
        }
        updateConsumers(invalidRgn);
    }

    /**
     * This method can be called by an application to have the QTImageProducer to update the pixel
     * data for each of the registered ImageConsumers when the source Presentable object has drawn.
     * Thus an application can either explicitly draw the Presentable source itself then call this method
     * or can receive notification by the Presentable source when it has drawn, and use this method to
     * update the registered ImageConsumers.
     * @param invalidRgn enter null to copy all pixels, or specify a region that specifies the changed pixel data,
     *  which can economize the copying of the pixel data to only those areas that have changed.
     * @throws quicktime.QTException
     */
    public synchronized void updateConsumers(Region invalidRgn) throws QTException {
        if (!isSingleFrame())
            copyPixels();
        int m = 0;
        int i;
        int j;
        int k;
        if (invalidRgn != null) {
            QDRect rgnBBox = invalidRgn.getRgnBBox();
            i = rgnBBox.getX();
            j = rgnBBox.getY();
            k = rgnBBox.getWidth();
            m = rgnBBox.getHeight();
        } else {
            i = 0;
            j = 0;
            k = this.size.width;
            m = this.size.height;
        }
        Enumeration<ImageConsumer> enumeration = this.consumers.elements();
        while (enumeration.hasMoreElements()) {
            ImageConsumer imageConsumer = enumeration.nextElement();
            imageConsumer.setPixels(i, j, k, m, this.cm, this.pixels, 0, this.rowInts);
            if (isConsumer(imageConsumer)) {
                imageConsumer.imageComplete(2);
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
    public synchronized void setRedrawing(boolean redrawFlag) {
        this.doesRedraw = redrawFlag;
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
