
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

    private QTCanvas canvas;

    transient Drawable mClient;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    QTCanvasHelper(QTCanvas paramQTCanvas) {
        this.canvas = paramQTCanvas;
    }

    void doRemoveClient() {
        try {
            if (this.mClient != null) {
                if (QTSession.isInitialized())
                    doSetGWorld(false);
                this.mClient.removedFrom(this.canvas);
            }
        } catch (QTException localQTException) {
        } finally {
            this.mClient = null;
        }
    }

    void doSetClient(Drawable paramDrawable,
                     QDRect paramQDRect,
                     Dimension paramDimension1,
                     Dimension paramDimension2,
                     boolean paramBoolean) throws QTException {
        if (paramDrawable == this.mClient) {
            return;
        }
        if (this.mClient != null) {
            doRemoveClient();
        }
        if (paramDrawable != null) {
            this.mClient = paramDrawable;
            int i = this.canvas.currentWidth;
            int j = this.canvas.currentHeight;
            if (paramBoolean) {
                paramQDRect = paramQDRect != null ? paramQDRect
                                                  : new QDRect(0, 0, this.canvas.getSize().width, this.canvas.getSize().height);

                this.canvas.setInitialWidthAndHeight(paramQDRect, paramDimension1, paramDimension2);
                this.canvas.currentX = paramQDRect.getX();
                this.canvas.currentY = paramQDRect.getY();
                this.canvas.currentWidth = this.canvas.initialWidth;
                this.canvas.currentHeight = this.canvas.initialHeight;

                this.canvas.doParentLayout();
                this.canvas.deferResize = true;
            } else {
                this.canvas.setInitialWidthAndHeight(paramQDRect, this.canvas.getMinimumSize(), this.canvas.getMaximumSize());
                doClientSetBounds();
            }
            try {
                doSetGWorld(this.canvas.isVisible());
                if (this.canvas.isDisplayable()) {
                    this.mClient.addedTo(this.canvas);
                    this.canvas.repaint();
                }
                if (this.canvas.isVisible()) {
                    if ((!this.canvas.isWin) || (!paramBoolean)
                        || ((i == this.canvas.currentWidth) && (j == this.canvas.currentHeight))) {
                        this.canvas.deferRedraw = true;
                    } else
                        this.canvas.repaint();
                }
            } catch (QTException localQTException) {
                this.mClient.removedFrom(this.canvas);
                throw localQTException;
            }
        }
    }

    void doSetGWorld(boolean paramBoolean) throws QTException {
        if ((this.mClient instanceof QTDrawable)) {
            QTDrawable localQTDrawable = (QTDrawable) this.mClient;
            QDGraphics localQDGraphics1;
            if ((paramBoolean) && (this.canvas.isDisplayable())) {
                localQDGraphics1 = this.canvas.getPort();
            } else {
                localQDGraphics1 = QDGraphics.scratch;
            }
            QDGraphics localQDGraphics2 = localQTDrawable.getGWorld();
            if (!localQDGraphics1.equals(localQDGraphics2)) {

                localQTDrawable.setGWorld(localQDGraphics1);
                if (paramBoolean) {
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

    boolean shouldRedrawQTPlayer(Rectangle paramRectangle) {
        if ((!this.canvas.isWin)
            && (((this.mClient instanceof QTPlayer)) || ((this.mClient instanceof GroupDrawable))
                || ((this.mClient instanceof ImageDrawer)))
            && (this.canvas.isShowing()) && (!paramRectangle.equals(this.canvas.getBounds()))) {
            return true;
        }

        return false;
    }

    void doClientSetBounds() throws QTException {
        Point localPoint = this.canvas.getNGLocation();

        this.mClient
                .setDisplayBounds(new QDRect(localPoint.x, localPoint.y, this.canvas.currentWidth, this.canvas.currentHeight));
    }
}


