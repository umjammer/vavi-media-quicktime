package quicktime.app.image;

import java.awt.Dimension;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDrawable;
import quicktime.io.QTFile;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.std.movies.media.DataRef;
import quicktime.util.EncodedImage;
import quicktime.util.RawEncodedImage;

/**
 * @deprecated
 * This class represents an image that is stored as a file that
 * can be read and drawn using QuickTime GraphicsImporter component
 * @see quicktime.app.image.GraphicsImporterDrawer
 */
public class GraphicsImporterDrawer implements QTDrawable, Compositable {

    private final GraphicsImporter mGraphicsImporter;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This constructor creates an GraphicsImporterDrawer object from an existing GraphicsImporter.
     * @param gi the GraphicsImporter
     */
    public GraphicsImporterDrawer(GraphicsImporter gi) {
        this.mGraphicsImporter = gi;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This constructor creates an GraphicsImporterDrawer object from a file.
     * @param file the QTFile.
     */
    public GraphicsImporterDrawer(QTFile file) throws QTException {
        this.mGraphicsImporter = new GraphicsImporter(file);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This constructor creates an GraphicsImporterDrawer object from a data ref.
     * @param dataRef the dataRef that contains the locations of the image data.
     */
    public GraphicsImporterDrawer(DataRef dataRef) throws QTException {
        this.mGraphicsImporter = new GraphicsImporter(dataRef);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This returns the GraphicsImporter object that is responsible for
     * importing and drawing the picture file.
     */
    public GraphicsImporter getImporter() {
        return this.mGraphicsImporter;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns a region that defines the current clipping region for the object.
     * @return the clipping region
     * @throws quicktime.QTException
     */
    @Override
    public Region getClip() throws QTException {
        return this.mGraphicsImporter.getClip();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets a region that will define the clipping region for the object
     * @param reg the new clipping region
     * @throws quicktime.std.StdQTException
     */
    @Override
    public void setClip(Region reg) throws StdQTException {
        this.mGraphicsImporter.setClip(reg);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method should return an ImageDescription that describes
     * the image data that the class contains.
     * @return the ImageDescription of an Image
     * @throws quicktime.std.StdQTException
     */
    @Override
    public ImageDescription getDescription() throws StdQTException {
        return (ImageDescription) this.mGraphicsImporter.getImageDescription().clone();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the actual image data of the GraphicsImporterDrawer's GraphicImporter.
     * An Exception is throw if there was some problem retrieving the data.
     * @return the image data
     * @throws quicktime.QTException
     */
    @Override
    public EncodedImage getImage() throws QTException {
        int i = this.mGraphicsImporter.getDataOffset();
        int j = this.mGraphicsImporter.getDataSize();
        RawEncodedImage localRawEncodedImage = new RawEncodedImage(j, true);
        this.mGraphicsImporter.readData(localRawEncodedImage, i, j);
        return localRawEncodedImage;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     * @throws quicktime.std.StdQTException
     */
    @Override
    public QDGraphics getGWorld() throws StdQTException {
        return this.mGraphicsImporter.getGWorld();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param cgp a QDGraphics object
     * @throws quicktime.std.StdQTException
     */
    @Override
    public void setGWorld(QDGraphics cgp) throws StdQTException {
        this.mGraphicsImporter.setGWorld(cgp, null);
    }

    /**
     * @deprecated
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        QDRect localQDRect = this.mGraphicsImporter.getNaturalBounds();
        return new Dimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size
     * @throws quicktime.QTException
     */
    @Override
    public QDDimension getOriginalSize() throws QTException {
        QDRect localQDRect = this.mGraphicsImporter.getNaturalBounds();
        return new QDDimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This will return the client's current display transformations
     *
     * @return the display transformations of the client
     * @throws quicktime.std.StdQTException
     */
    @Override
    public Matrix getMatrix() throws StdQTException {
        return this.mGraphicsImporter.getMatrix();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     */
    @Override
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.mGraphicsImporter.setMatrix(paramMatrix);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method when it needs to tell the client to adjust its
     * location, width and/or height. All values are in pixels.
     *
     * @param bounds the new size and location.
     * @throws quicktime.std.StdQTException
     */
    @Override
    public void setDisplayBounds(QDRect bounds) throws StdQTException {
        this.mGraphicsImporter.setBoundsRect(bounds);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * A convenience method to set the position of a QTDrawable object.
     * @param x the new x location
     * @param y the new y location
     * @throws quicktime.QTException
     */
    @Override
    public void setLocation(int x, int y) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(x);
        localQDRect.setY(y);
        setDisplayBounds(localQDRect);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method when it needs to ask the client for its boundary.
     * All values are in pixels.
     *
     * @return the boundary of the client
     * @throws quicktime.std.StdQTException
     */
    @Override
    public QDRect getDisplayBounds() throws StdQTException {
        return this.mGraphicsImporter.getBoundsRect();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method when the client should redraw itself.
     * If the canvas is able to discern that only a part of the client's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * client should redraw itself entirely.
     * @param invalidRgn the invalidRgn that the client should redraw
     * @throws quicktime.QTException
     */
    @Override
    public final synchronized void redraw(Region invalidRgn) throws QTException {
        if (invalidRgn != null) {
            Region localRegion = this.mGraphicsImporter.getClip();
            this.mGraphicsImporter.setClip(invalidRgn);
            this.mGraphicsImporter.draw();
            this.mGraphicsImporter.setClip(localRegion);
        } else {
            this.mGraphicsImporter.draw();
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Determines the graphics mode for an image.
     * @return The graphics transfer mode.
     * @throws quicktime.std.StdQTException
     */
    @Override
    public GraphicsMode getGraphicsMode() throws StdQTException {
        return this.mGraphicsImporter.getGraphicsMode();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Determines the graphics mode for an image.
     * @param gMode Specifies the graphics mode.
     * @return The graphics transfer mode.
     * @throws quicktime.std.StdQTException
     */
    @Override
    public void setGraphicsMode(GraphicsMode gMode) throws StdQTException {
        this.mGraphicsImporter.setGraphicsMode(gMode);
    }

    /**
     * @deprecated
     * Returns a String representation of this object.* @deprecated since QTJava 6.1
     */
    public String toString() {
        return getClass().getName() + "[" + this.mGraphicsImporter + "]";
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest.
     * @param interest the object that is to be the source of interest for the
     *  the object that implements this interface.
     */
    @Override
    public void addedTo(Object interest) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest.
     * @param interest the object that was the source of interest for the
     *  the object that implements this interface.
     */
    @Override
    public void removedFrom(Object interest) {
    }
}
