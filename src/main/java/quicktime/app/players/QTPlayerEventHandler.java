package quicktime.app.players;

import java.util.Vector;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * When a QTPlayer is addedTo some kind of display space it receives
 * an addedTo (or removedFrom) notification. At this point the QTPlayer
 * will dispatch to the QTPlayerEventHandler to return a QTPlayerEventHandler
 * which will handle all of the events that are generated from the source of interest
 * that the player has been added to.
 * <p>
 * Applications can install custom event handler for the QTPlayer and in this construction
 * process custom event handlers will be made first. If not custom event handlers are appropriate
 * and a default handler exists it will be made and returned. The first event handler that is
 * found will be returned as the event handler for any given QTPlayer.
 * <p>
 * The QTPlayerEventHandler is expected to handle all events for the QTPlayer.
 *
 * @deprecated
 */
public abstract class QTPlayerEventHandler {

    private static Vector<QTPlayerEventHandlerMaker> vec;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Register a custom event handler maker that will make an EventDispatcher
     * based on the type of event interest for the QTPlayer
     *
     * @since QTJava 6.1
     * @deprecated
     */
    public static void registerEventHandlerMaker(QTPlayerEventHandlerMaker qtPlayerEventHandlerMaker) {
        if (vec == null)
            vec = new Vector<>();
        vec.addElement(qtPlayerEventHandlerMaker);
    }

    /**
     * Remove a custom event handler maker.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    public static void removeEventHandlerMaker(QTPlayerEventHandlerMaker qtPlayerEventHandlerMaker) {
        if (vec != null)
            vec.removeElement(qtPlayerEventHandlerMaker);
        if (vec.isEmpty()) {
            vec = null;
        }
    }

    static QTPlayerEventHandler make(QTPlayer player, Object addedToInterest) {
        if (vec != null) {
            for (int i = 0; i < vec.size(); i++) {
                QTPlayerEventHandlerMaker qtPlayerEventHandlerMaker = vec.elementAt(i);
                QTPlayerEventHandler qtPlayerEventHandler = qtPlayerEventHandlerMaker.make(player, addedToInterest);
                if (qtPlayerEventHandler != null) {
                    return qtPlayerEventHandler;
                }
            }
        }
        Class<?> superclass = addedToInterest.getClass();
        String name = superclass.getName();
        while (!superclass.getName().equals("java.lang.Object")) {
            name = superclass.getName();
            superclass = superclass.getSuperclass();
        }
        if (name.equals("java.awt.Component")) {
            return new QTPlayerAWTEventHandler(player, addedToInterest);
        }
        return null;
    }

    /**
     * When the QTPlayer is removed from its addedTo interest its event Handler
     * is notified and should remove any interests it has declared for events
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected abstract void removedFrom();

    /**
     * When the QTPlayer is added to its addedTo interest its event Handler
     * is notified and should add any interests it requires to receive
     * events.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected abstract void addedTo();
}
