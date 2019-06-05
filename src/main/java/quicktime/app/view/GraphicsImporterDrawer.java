
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


public class GraphicsImporterDrawer implements Presentable {
    private GraphicsImporter mGraphicsImporter;

    public GraphicsImporterDrawer(GraphicsImporter paramGraphicsImporter) {
        this.mGraphicsImporter = paramGraphicsImporter;
    }

    public GraphicsImporterDrawer(QTFile paramQTFile) throws QTException {
        this.mGraphicsImporter = new GraphicsImporter(paramQTFile);
    }

    public GraphicsImporterDrawer(DataRef paramDataRef) throws QTException {
        this.mGraphicsImporter = new GraphicsImporter(paramDataRef);
    }

    public GraphicsImporter getImporter() {
        return this.mGraphicsImporter;
    }

    public Region getClip() throws QTException {
        return this.mGraphicsImporter.getClip();
    }

    public void setClip(Region paramRegion) throws StdQTException {
        this.mGraphicsImporter.setClip(paramRegion);
    }

    public ImageDescription getDescription() throws StdQTException {
        return (ImageDescription) this.mGraphicsImporter.getImageDescription().clone();
    }

    public EncodedImage getImage() throws QTException {
        int i = this.mGraphicsImporter.getDataOffset();
        int j = this.mGraphicsImporter.getDataSize();
        RawEncodedImage localRawEncodedImage = new RawEncodedImage(j, true);

        this.mGraphicsImporter.readData(localRawEncodedImage, i, j);
        return localRawEncodedImage;
    }

    public void addedTo(Object paramObject) {
    }

    public void removedFrom(Object paramObject) {
    }

    public QDGraphics getGWorld() throws StdQTException {
        return this.mGraphicsImporter.getGWorld();
    }

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

    public QDDimension getOriginalSize() throws QTException {
        QDRect localQDRect = this.mGraphicsImporter.getNaturalBounds();
        return new QDDimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    public Matrix getMatrix() throws StdQTException {
        return this.mGraphicsImporter.getMatrix();
    }

    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.mGraphicsImporter.setMatrix(paramMatrix);
    }

    public void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        this.mGraphicsImporter.setBoundsRect(paramQDRect);
    }

    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(paramInt1);
        localQDRect.setY(paramInt2);
        setDisplayBounds(localQDRect);
    }

    public QDRect getDisplayBounds() throws StdQTException {
        return this.mGraphicsImporter.getBoundsRect();
    }

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

    public GraphicsMode getGraphicsMode() throws StdQTException {
        return this.mGraphicsImporter.getGraphicsMode();
    }

    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws StdQTException {
        this.mGraphicsImporter.setGraphicsMode(paramGraphicsMode);
    }

    public String toString() {
        return getClass().getName() + "[" + this.mGraphicsImporter + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/view/
 * GraphicsImporterDrawer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
