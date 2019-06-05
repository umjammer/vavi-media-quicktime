
package quicktime.app.players;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.qd.QDPoint;
import quicktime.std.movies.MovieController;


/**
 * @deprecated
 */
public class QTPlayerAWTEventHandler extends QTPlayerEventHandler implements MouseListener, KeyListener {
    private WindowAdapter wa;

    private Window parentWindow;

    protected QTPlayer player;

    /**
     * @deprecated
     */
    protected Component component;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    protected QTPlayerAWTEventHandler(QTPlayer paramQTPlayer, Object paramObject) {
        this.player = paramQTPlayer;
        this.component = ((Component) paramObject);
    }

    /**
     * @deprecated
     */
    protected void addedTo() {
        this.component.addMouseListener(this);
        this.component.addKeyListener(this);

        Object localObject1 = this.component.getParent();
        Object localObject2 = localObject1;
        while (localObject2 != null) {
            localObject1 = localObject2;
            if ((localObject1 instanceof Window))
                break;
            localObject2 = ((Container) localObject1).getParent();
        }
        if ((localObject1 instanceof Window)) {
            this.parentWindow = ((Window) localObject1);
            this.wa = new WindowAdapter() {
                public void windowActivated(WindowEvent paramAnonymousWindowEvent) {
                    try {
                        QTPlayerAWTEventHandler.this.player.controller.activate(QTPlayerAWTEventHandler.this.player.getGWorld(),
                                                                                true);
                        QTPlayerAWTEventHandler.this.player.controller.activate();
                        QTPlayerAWTEventHandler.this.player.redraw(null);
                    } catch (QTException localQTException) {
                    }
                }

                public void windowDeactivated(WindowEvent paramAnonymousWindowEvent) {
                    synchronized (QTSession.terminationLock()) {
                        if (QTSession.isInitialized()) {
                            try {
                                QTPlayerAWTEventHandler.this.player.controller.deactivate();
                                QTPlayerAWTEventHandler.this.player.controller
                                        .activate(QTPlayerAWTEventHandler.this.player.getGWorld(), false);
                                QTPlayerAWTEventHandler.this.player.redraw(null);
                            } catch (QTException localQTException) {
                            }
                        } else {
                            QTPlayerAWTEventHandler.this.player.removedFrom(QTPlayerAWTEventHandler.this.component);
                        }
                    }
                }
            };
            this.parentWindow.addWindowListener(this.wa);
        }
    }

    /**
     * @deprecated
     */
    protected void removedFrom() {
        this.component.removeMouseListener(this);
        this.component.removeKeyListener(this);

        if (this.parentWindow != null) {
            this.parentWindow.removeWindowListener(this.wa);
            this.parentWindow = null;
            this.wa = null;
        }
    }

    public void keyPressed(KeyEvent paramKeyEvent) {
        try {
            this.player.controller.key(paramKeyEvent.getKeyCode(), paramKeyEvent.getModifiers());
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "keyPressed");
        }
    }

    public void mousePressed(MouseEvent paramMouseEvent) {
        try {
            this.player.controller.click(new QDPoint(paramMouseEvent.getX(), paramMouseEvent.getY()),
                                         paramMouseEvent.getModifiers());
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mousePressed");
        }
    }

    public void keyReleased(KeyEvent paramKeyEvent) {
    }

    public void keyTyped(KeyEvent paramKeyEvent) {
    }

    public void mouseClicked(MouseEvent paramMouseEvent) {
    }

    public void mouseEntered(MouseEvent paramMouseEvent) {
    }

    public void mouseExited(MouseEvent paramMouseEvent) {
    }

    public void mouseReleased(MouseEvent paramMouseEvent) {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * players/QTPlayerAWTEventHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
