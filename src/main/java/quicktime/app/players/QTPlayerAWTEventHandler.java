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


/**
 * @deprecated
 * When a QTPlayer is addedTo some kind of java.awt.Component the default
 * construction methods of the QTPlayerEventHandler will create an instance
 * of this object.
 *
 * If applications want to provide customised event handling for a QTPlayer
 * they can subclass this object and register their custom event handler
 * with the QTPlayerEventHandler class.
 *
 * The QTPlayerAWTEventHandler will handle mousePressed methods and keyPressed
 * methods.
 */
public class QTPlayerAWTEventHandler extends QTPlayerEventHandler implements MouseListener, KeyListener {

    private WindowAdapter wa;

    private Window parentWindow;

    /**
     * @deprecated
     * The QTPlayer this object handles events for * @deprecated since QTJava 6.1
     */
    protected QTPlayer player;

    /**
     * @deprecated
     * @since QTJava 6.1
     * The java.awt.Component to which the QTPlayer has been added.
     */
    protected Component component;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Construct a QTPlayerAWTEventHandler where the object of interest for
     * events is some kind of java.awt.Component.
     * @param interest the source of events.
     */
    protected QTPlayerAWTEventHandler(QTPlayer paramQTPlayer, Object paramObject) {
        this.player = paramQTPlayer;
        this.component = ((Component) paramObject);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Registers interests in mouse and key events with the java.awt.Component
     */
    @Override
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

                @Override
                public void windowActivated(WindowEvent paramAnonymousWindowEvent) {
                    try {
                        QTPlayerAWTEventHandler.this.player.controller.activate(QTPlayerAWTEventHandler.this.player.getGWorld(), true);
                        QTPlayerAWTEventHandler.this.player.controller.activate();
                        QTPlayerAWTEventHandler.this.player.redraw(null);
                    } catch (QTException localQTException) {
                    }
                }

                @Override
                public void windowDeactivated(WindowEvent paramAnonymousWindowEvent) {
                    synchronized (QTSession.terminationLock()) {
                        if (QTSession.isInitialized()) {
                            try {
                                QTPlayerAWTEventHandler.this.player.controller.deactivate();
                                QTPlayerAWTEventHandler.this.player.controller.activate(QTPlayerAWTEventHandler.this.player.getGWorld(), false);
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
     * @since QTJava 6.1
     * Removes any interests in mouse and key events with the java.awt.Component
     */
    @Override
    protected void removedFrom() {
        this.component.removeMouseListener(this);
        this.component.removeKeyListener(this);
        if (this.parentWindow != null) {
            this.parentWindow.removeWindowListener(this.wa);
            this.parentWindow = null;
            this.wa = null;
        }
    }

    /**
     * @deprecated
     * Key pressed events are passed on to the movie controller * @deprecated since QTJava 6.1
     */
    @Override
    public void keyPressed(KeyEvent paramKeyEvent) {
        try {
            this.player.controller.key(paramKeyEvent.getKeyCode(), paramKeyEvent.getModifiers());
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "keyPressed");
        }
    }

    /**
     * @deprecated
     * Mouse pressed events are passed on to the movie controller * @deprecated since QTJava 6.1
     */
    @Override
    public void mousePressed(MouseEvent paramMouseEvent) {
        try {
            this.player.controller.click(new QDPoint(paramMouseEvent.getX(), paramMouseEvent.getY()), paramMouseEvent.getModifiers());
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mousePressed");
        }
    }

    /**
     * @deprecated
     */
    @Override
    public void keyReleased(KeyEvent paramKeyEvent) {
    }

    /**
     * @deprecated
     */
    @Override
    public void keyTyped(KeyEvent paramKeyEvent) {
    }

    /**
     * @deprecated
     */
    @Override
    public void mouseClicked(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     */
    @Override
    public void mouseEntered(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     */
    @Override
    public void mouseExited(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     */
    @Override
    public void mouseReleased(MouseEvent paramMouseEvent) {
    }
}

