
package quicktime.app.view;

import quicktime.QTException;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.image.Matrix;


interface Presentable {
    void addedTo(Object paramObject);

    void removedFrom(Object paramObject);

    void setLocation(int paramInt1, int paramInt2) throws QTException;

    void setGWorld(QDGraphics paramQDGraphics) throws QTException;

    QDGraphics getGWorld() throws QTException;

    void setClip(Region paramRegion) throws QTException;

    Region getClip() throws QTException;

    QDDimension getOriginalSize() throws QTException;

    Matrix getMatrix() throws QTException;

    void setMatrix(Matrix paramMatrix) throws QTException;

    void setDisplayBounds(QDRect paramQDRect) throws QTException;

    QDRect getDisplayBounds() throws QTException;

    void redraw(Region paramRegion) throws QTException;

    boolean isSingleFrame();
}
