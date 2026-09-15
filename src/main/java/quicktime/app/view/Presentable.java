
package quicktime.app.view;

import quicktime.QTException;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.image.Matrix;


abstract interface Presentable {
    public abstract void addedTo(Object paramObject);

    public abstract void removedFrom(Object paramObject);

    public abstract void setLocation(int paramInt1, int paramInt2) throws QTException;

    public abstract void setGWorld(QDGraphics paramQDGraphics) throws QTException;

    public abstract QDGraphics getGWorld() throws QTException;

    public abstract void setClip(Region paramRegion) throws QTException;

    public abstract Region getClip() throws QTException;

    public abstract QDDimension getOriginalSize() throws QTException;

    public abstract Matrix getMatrix() throws QTException;

    public abstract void setMatrix(Matrix paramMatrix) throws QTException;

    public abstract void setDisplayBounds(QDRect paramQDRect) throws QTException;

    public abstract QDRect getDisplayBounds() throws QTException;

    public abstract void redraw(Region paramRegion) throws QTException;

    public abstract boolean isSingleFrame();
}


