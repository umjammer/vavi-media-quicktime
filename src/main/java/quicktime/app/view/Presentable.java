
package quicktime.app.view;

import quicktime.QTException;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.image.Matrix;


interface Presentable {
    void addedTo(Object interest);

    void removedFrom(Object interest);

    void setLocation(int x, int y) throws QTException;

    void setGWorld(QDGraphics cgp) throws QTException;

    QDGraphics getGWorld() throws QTException;

    void setClip(Region reg) throws QTException;

    Region getClip() throws QTException;

    QDDimension getOriginalSize() throws QTException;

    Matrix getMatrix() throws QTException;

    void setMatrix(Matrix matrix) throws QTException;

    void setDisplayBounds(QDRect bounds) throws QTException;

    QDRect getDisplayBounds() throws QTException;

    void redraw(Region invalidRgn) throws QTException;

    boolean isSingleFrame();
}
