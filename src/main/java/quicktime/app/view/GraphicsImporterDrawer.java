package quicktime.app.view;

import java.awt.Dimension;
import quicktime.QTException;
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
 * This class represents an image that is stored as a file that
 * can be read and drawn using QuickTime GraphicsImporter component
 * @see {@link quicktime.app.image.GraphicsImporterDrawer}
 */
public class GraphicsImporterDrawer implements Presentable {

    private GraphicsImporter mGraphicsImporter;

    /**
     * This constructor creates an GraphicsImporterDrawer object from an existing GraphicsImporter.
     * @param gi the GraphicsImporter
     */
    public GraphicsImporterDrawer(GraphicsImporter paramGraphicsImporter) {
        this.mGraphicsImporter = paramGraphicsImporter;
    }

    /**
     * This constructor creates an GraphicsImporterDrawer object from a file.
     * @param file the QTFile.
     */
    public GraphicsImporterDrawer(QTFile paramQTFile) throws QTException {
        this.mGraphicsImporter = new GraphicsImporter(paramQTFile);
    }

    /**
     * This constructor creates an GraphicsImporterDrawer object from a data ref.
     * @param dataRef the dataRef that contains the locations of the image data.
     */
    public GraphicsImporterDrawer(DataRef paramDataRef) throws QTException {
        this.mGraphicsImporter = new GraphicsImporter(paramDataRef);
    }

    /**
     * This returns the GraphicsImporter object that is responsible for
     * importing and drawing the picture file.
     */
    public GraphicsImporter getImporter() {
        return this.mGraphicsImporter;
    }

    /**
     * Returns a region that defines the current clipping region for the object.
     * @return the clipping region
     * @return {@link quicktime.QTException}
     */
    public Region getClip() throws QTException {
        return this.mGraphicsImporter.getClip();
    }

    /**
     * Sets a region that will define the clipping region for the object
     * @param reg the new clipping region
     * @param {@link quicktime.std.StdQTException}
     */
    public void setClip(Region paramRegion) throws StdQTException {
        this.mGraphicsImporter.setClip(paramRegion);
    }

    /**
     * This method should return an ImageDescription that describes
     * the image data that the class contains.
     * @return the ImageDescription of an Image
     * @return {@link quicktime.std.StdQTException}
     */
    public ImageDescription getDescription() throws StdQTException {
        return (ImageDescription) this.mGraphicsImporter.getImageDescription().clone();
    }

    /**
     * This method returns the actual image data of the GraphicsImporterDrawer's GraphicImporter.
     * An Exception is throw if there was some problem retrieving the data.
     * @return the image data
     * @return {@link quicktime.QTException}
     */
    public EncodedImage getImage() throws QTException {
        int i = this.mGraphicsImporter.getDataOffset();
        int j = this.mGraphicsImporter.getDataSize();
        RawEncodedImage localRawEncodedImage = new RawEncodedImage(j, true);
        this.mGraphicsImporter.readData(localRawEncodedImage, i, j);
        return localRawEncodedImage;
    }

    /**
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest.
     * @param interest the object that is to be the source of interest for the
     *  the object that implements this interface.
     */
    public void addedTo(Object paramObject) {
    }

    /**
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest.
     * @param interest the object that was the source of interest for the
     *  the object that implements this interface.
     */
    public void removedFrom(Object paramObject) {
    }

    /**
     * QTComponent calls this method to get the destination QDGraphics of its image.
     * @return a QDGraphics object or null
     * @return {@link quicktime.std.StdQTException}
     */
    public QDGraphics getGWorld() throws StdQTException {
        return this.mGraphicsImporter.getGWorld();
    }

    /**
     * QTComponent calls this method to give the image the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a image is unable to set the graphics world it should throw an exception.
     *
     * @param cgp a QDGraphics object
     * @param {@link quicktime.std.StdQTException}
     */
    public void setGWorld(QDGraphics paramQDGraphics) throws StdQTException {
        this.mGraphicsImporter.setGWorld(paramQDGraphics, null);
    }

    /**
     * Deprecated.
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
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size
     * @return {@link quicktime.QTException}
     */
    public QDDimension getOriginalSize() throws QTException {
        QDRect localQDRect = this.mGraphicsImporter.getNaturalBounds();
        return new QDDimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    /**
     * This will return the image's current display transformations
     *
     * @return the display transformations of the image
     * @return {@link quicktime.std.StdQTException}
     */
    public Matrix getMatrix() throws StdQTException {
        return this.mGraphicsImporter.getMatrix();
    }

    /**
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.mGraphicsImporter.setMatrix(paramMatrix);
    }

    /**
     * QTComponent calls this method when it needs to tell the image to adjust its
     * location, width and/or height. All values are in pixels.
     *
     * @param bounds the new size and location.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        this.mGraphicsImporter.setBoundsRect(paramQDRect);
    }

    /**
     * A convenience method to set the position of a Presentable object.
     * @param x the new x location
     * @param y the new y location
     * @param {@link quicktime.QTException}
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(paramInt1);
        localQDRect.setY(paramInt2);
        setDisplayBounds(localQDRect);
    }

    /**
     * QTComponent calls this method when it needs to ask the image for its boundary.
     * All values are in pixels.
     *
     * @return the boundary of the image
     * @return {@link quicktime.std.StdQTException}
     */
    public QDRect getDisplayBounds() throws StdQTException {
        return this.mGraphicsImporter.getBoundsRect();
    }

    /**
     * QTComponent calls this method when the image should redraw itself.
     * If the canvas is able to discern that only a part of the image's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * image should redraw itself entirely.
     * @param invalidRgn the invalidRgn that the image should redraw
     * @param {@link quicktime.QTException}
     */
    public final synchronized void redraw(Region paramRegion) throws QTException {
        if (paramRegion != null) {
            Region localRegion = this.mGraphicsImporter.getClip();
            this.mGraphicsImporter.setClip(paramRegion);
            this.mGraphicsImporter.draw();
            this.mGraphicsImporter.setClip(localRegion);
        } else {
            this.mGraphicsImporter.draw();
        }
    }

    public boolean isSingleFrame() {
        return true;
    }

    /**
     * Determines the graphics mode for an image.
     * @return The graphics transfer mode.
     * @return {@link quicktime.std.StdQTException}
     */
    public GraphicsMode getGraphicsMode() throws StdQTException {
        return this.mGraphicsImporter.getGraphicsMode();
    }

    /**
     * Determines the graphics mode for an image.
     * @return The graphics transfer mode.
     * @return {@link quicktime.std.StdQTException}
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws StdQTException {
        this.mGraphicsImporter.setGraphicsMode(paramGraphicsMode);
    }

    /**
     * Returns a String representation of this object.
     */
    public String toString() {
        return getClass().getName() + "[" + this.mGraphicsImporter + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/view/
 * GraphicsImporterDrawer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
