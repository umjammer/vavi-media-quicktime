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
 * @deprecated
 */
public class QTPlayerAWTEventHandler extends QTPlayerEventHandler implements MouseListener, KeyListener {

    private WindowAdapter wa;

    private Window parentWindow;

    /**
     * The QTPlayer this object handles events for * @deprecated since QTJava 6.1
     * @deprecated
     */
    protected QTPlayer player;

    /**
     * The java.awt.Component to which the QTPlayer has been added.
     * @deprecated
     * @since QTJava 6.1
     */
    protected Component component;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Construct a QTPlayerAWTEventHandler where the object of interest for
     * events is some kind of java.awt.Component.
     * @param interest the source of events.
     * @deprecated
     * @since QTJava 6.1
     */
    protected QTPlayerAWTEventHandler(QTPlayer player, Object interest) {
        this.player = player;
        this.component = ((Component) interest);
    }

    /**
     * Registers interests in mouse and key events with the java.awt.Component
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    protected void addedTo() {
        this.component.addMouseListener(this);
        this.component.addKeyListener(this);
        Object parentWindow = this.component.getParent();
        Object parent = parentWindow;
        while (parent != null) {
            parentWindow = parent;
            if ((parentWindow instanceof Window))
                break;
            parent = ((Container) parentWindow).getParent();
        }
        if ((parentWindow instanceof Window)) {
            this.parentWindow = ((Window) parentWindow);
            this.wa = new WindowAdapter() {

                @Override
                public void windowActivated(WindowEvent e) {
                    try {
                        QTPlayerAWTEventHandler.this.player.controller.activate(QTPlayerAWTEventHandler.this.player.getGWorld(), true);
                        QTPlayerAWTEventHandler.this.player.controller.activate();
                        QTPlayerAWTEventHandler.this.player.redraw(null);
                    } catch (QTException ex) {
                    }
                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                    synchronized (QTSession.terminationLock()) {
                        if (QTSession.isInitialized()) {
                            try {
                                QTPlayerAWTEventHandler.this.player.controller.deactivate();
                                QTPlayerAWTEventHandler.this.player.controller.activate(QTPlayerAWTEventHandler.this.player.getGWorld(), false);
                                QTPlayerAWTEventHandler.this.player.redraw(null);
                            } catch (QTException ex) {
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
     * Removes any interests in mouse and key events with the java.awt.Component
     * @deprecated
     * @since QTJava 6.1
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
     * Key pressed events are passed on to the movie controller * @deprecated since QTJava 6.1
     * @deprecated
     */
    @Override
    public void keyPressed(KeyEvent e) {
        try {
            this.player.controller.key(e.getKeyCode(), e.getModifiers());
        } catch (QTException ex) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(ex), this, "keyPressed");
        }
    }

    /**
     * Mouse pressed events are passed on to the movie controller * @deprecated since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mousePressed(MouseEvent e) {
        try {
            this.player.controller.click(new QDPoint(e.getX(), e.getY()), e.getModifiers());
        } catch (QTException ex) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(ex), this, "mousePressed");
        }
    }

    /**
     * @deprecated
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * @deprecated
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * @deprecated
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * @deprecated
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * @deprecated
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * @deprecated
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
