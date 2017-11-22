
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
 */
public class GraphicsImporterDrawer implements QTDrawable, Compositable {
    private GraphicsImporter mGraphicsImporter;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public GraphicsImporterDrawer(GraphicsImporter paramGraphicsImporter) {
        this.mGraphicsImporter = paramGraphicsImporter;
    }

    /**
     * @deprecated
     */
    public GraphicsImporterDrawer(QTFile paramQTFile) throws QTException {
        this.mGraphicsImporter = new GraphicsImporter(paramQTFile);
    }

    /**
     * @deprecated
     */
    public GraphicsImporterDrawer(DataRef paramDataRef) throws QTException {
        this.mGraphicsImporter = new GraphicsImporter(paramDataRef);
    }

    /**
     * @deprecated
     */
    public GraphicsImporter getImporter() {
        return this.mGraphicsImporter;
    }

    /**
     * @deprecated
     */
    public Region getClip() throws QTException {
        return this.mGraphicsImporter.getClip();
    }

    /**
     * @deprecated
     */
    public void setClip(Region paramRegion) throws StdQTException {
        this.mGraphicsImporter.setClip(paramRegion);
    }

    /**
     * @deprecated
     */
    public ImageDescription getDescription() throws StdQTException {
        return (ImageDescription) this.mGraphicsImporter.getImageDescription().clone();
    }

    /**
     * @deprecated
     */
    public EncodedImage getImage() throws QTException {
        int i = this.mGraphicsImporter.getDataOffset();
        int j = this.mGraphicsImporter.getDataSize();
        RawEncodedImage localRawEncodedImage = new RawEncodedImage(j, true);

        this.mGraphicsImporter.readData(localRawEncodedImage, i, j);
        return localRawEncodedImage;
    }

    /**
     * @deprecated
     */
    public QDGraphics getGWorld() throws StdQTException {
        return this.mGraphicsImporter.getGWorld();
    }

    /**
     * @deprecated
     */
    public void setGWorld(QDGraphics paramQDGraphics) throws StdQTException {
        this.mGraphicsImporter.setGWorld(paramQDGraphics, null);
    }

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        QDRect localQDRect = this.mGraphicsImporter.getNaturalBounds();
        return new Dimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    /**
     * @deprecated
     */
    public QDDimension getOriginalSize() throws QTException {
        QDRect localQDRect = this.mGraphicsImporter.getNaturalBounds();
        return new QDDimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    /**
     * @deprecated
     */
    public Matrix getMatrix() throws StdQTException {
        return this.mGraphicsImporter.getMatrix();
    }

    /**
     * @deprecated
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.mGraphicsImporter.setMatrix(paramMatrix);
    }

    /**
     * @deprecated
     */
    public void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        this.mGraphicsImporter.setBoundsRect(paramQDRect);
    }

    /**
     * @deprecated
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(paramInt1);
        localQDRect.setY(paramInt2);
        setDisplayBounds(localQDRect);
    }

    /**
     * @deprecated
     */
    public QDRect getDisplayBounds() throws StdQTException {
        return this.mGraphicsImporter.getBoundsRect();
    }

    /**
     * @deprecated
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

    /**
     * @deprecated
     */
    public GraphicsMode getGraphicsMode() throws StdQTException {
        return this.mGraphicsImporter.getGraphicsMode();
    }

    /**
     * @deprecated
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws StdQTException {
        this.mGraphicsImporter.setGraphicsMode(paramGraphicsMode);
    }

    public String toString() {
        return getClass().getName() + "[" + this.mGraphicsImporter + "]";
    }

    /**
     * @deprecated
     */
    public void addedTo(Object paramObject) {
    }

    /**
     * @deprecated
     */
    public void removedFrom(Object paramObject) {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /GraphicsImporterDrawer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
