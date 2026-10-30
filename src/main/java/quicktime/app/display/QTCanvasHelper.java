
package quicktime.app.display;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.image.ImageDrawer;
import quicktime.app.players.QTPlayer;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.util.QTUtils;


/**
 * @deprecated
 */
class QTCanvasHelper {
    private static final boolean debug = false;

    private final QTCanvas canvas;

    transient Drawable mClient;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    QTCanvasHelper(QTCanvas canvas) {
        this.canvas = canvas;
    }

    void doRemoveClient() {
        try {
            if (this.mClient != null) {
                if (QTSession.isInitialized())
                    doSetGWorld(false);
                this.mClient.removedFrom(this.canvas);
            }
        } catch (QTException e) {
        } finally {
            this.mClient = null;
        }
    }

    void doSetClient(Drawable client,
                     QDRect rect,
                     Dimension minSize,
                     Dimension maxSize,
                     boolean doLayout) throws QTException {
        if (client == this.mClient) {
            return;
        }
        if (this.mClient != null) {
            doRemoveClient();
        }
        if (client != null) {
            this.mClient = client;
            int i = this.canvas.currentWidth;
            int j = this.canvas.currentHeight;
            if (doLayout) {
                rect = rect != null ? rect
                                                  : new QDRect(0, 0, this.canvas.getSize().width, this.canvas.getSize().height);

                this.canvas.setInitialWidthAndHeight(rect, minSize, maxSize);
                this.canvas.currentX = rect.getX();
                this.canvas.currentY = rect.getY();
                this.canvas.currentWidth = this.canvas.initialWidth;
                this.canvas.currentHeight = this.canvas.initialHeight;

                this.canvas.doParentLayout();
                this.canvas.deferResize = true;
            } else {
                this.canvas.setInitialWidthAndHeight(rect, this.canvas.getMinimumSize(), this.canvas.getMaximumSize());
                doClientSetBounds();
            }
            try {
                doSetGWorld(this.canvas.isVisible());
                if (this.canvas.isDisplayable()) {
                    this.mClient.addedTo(this.canvas);
                    this.canvas.repaint();
                }
                if (this.canvas.isVisible()) {
                    if ((!this.canvas.isWin) || (!doLayout)
                        || ((i == this.canvas.currentWidth) && (j == this.canvas.currentHeight))) {
                        this.canvas.deferRedraw = true;
                    } else
                        this.canvas.repaint();
                }
            } catch (QTException e) {
                this.mClient.removedFrom(this.canvas);
                throw e;
            }
        }
    }

    void doSetGWorld(boolean displayable) throws QTException {
        if ((this.mClient instanceof QTDrawable qtDrawable)) {
            QDGraphics port;
            if ((displayable) && (this.canvas.isDisplayable())) {
                port = this.canvas.getPort();
            } else {
                port = QDGraphics.scratch;
            }
            QDGraphics gWorld = qtDrawable.getGWorld();
            if (!port.equals(gWorld)) {

                qtDrawable.setGWorld(port);
                if (displayable) {
                    QTUtils.checkFreeMemory();
                    if (this.canvas.clientListener != null) {
                        this.canvas.clientListener.componentShown(new ComponentEvent(this.canvas, 102));
                    }
                } else if (this.canvas.clientListener != null) {
                    this.canvas.clientListener.componentHidden(new ComponentEvent(this.canvas, 103));
                }
            }
        }
    }

    boolean shouldRedrawQTPlayer(Rectangle rectangle) {
        if ((!this.canvas.isWin)
            && (((this.mClient instanceof QTPlayer)) || ((this.mClient instanceof GroupDrawable))
                || ((this.mClient instanceof ImageDrawer)))
            && (this.canvas.isShowing()) && (!rectangle.equals(this.canvas.getBounds()))) {
            return true;
        }

        return false;
    }

    void doClientSetBounds() throws QTException {
        Point ngLocation = this.canvas.getNGLocation();

        this.mClient
                .setDisplayBounds(new QDRect(ngLocation.x, ngLocation.y, this.canvas.currentWidth, this.canvas.currentHeight));
    }
}
