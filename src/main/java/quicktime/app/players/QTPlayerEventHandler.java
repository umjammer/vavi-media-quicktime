package quicktime.app.players;

import java.util.Vector;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * Deprecated.
 * When a QTPlayer is addedTo some kind of display space it receives
 * an addedTo (or removedFrom) notification. At this point the QTPlayer
 * will dispatch to the QTPlayerEventHandler to return a QTPlayerEventHandler
 * which will handle all of the events that are generated from the source of interest
 * that the player has been added to.
 *
 * Applications can install custom event handler for the QTPlayer and in this construction
 * process custom event handlers will be made first. If not custom event handlers are appropriate
 * and a default handler exists it will be made and returned. The first event handler that is
 * found will be returned as the event handler for any given QTPlayer.
 *
 * The QTPlayerEventHandler is expected to handle all events for the QTPlayer.
 */
public abstract class QTPlayerEventHandler {

    private static Vector vec;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Register a custom event handler maker that will make an EventDispatcher
     * based on the type of event interest for the QTPlayer
     */
    public static void registerEventHandlerMaker(QTPlayerEventHandlerMaker paramQTPlayerEventHandlerMaker) {
        if (vec == null)
            vec = new Vector();
        vec.addElement(paramQTPlayerEventHandlerMaker);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Remove a custom event handler maker.
     */
    public static void removeEventHandlerMaker(QTPlayerEventHandlerMaker paramQTPlayerEventHandlerMaker) {
        if (vec != null)
            vec.removeElement(paramQTPlayerEventHandlerMaker);
        if (vec.size() == 0) {
            vec = null;
        }
    }

    static QTPlayerEventHandler make(QTPlayer paramQTPlayer, Object paramObject) {
        if (vec != null) {
            for (int i = 0; i < vec.size(); i++) {
                Object localObject = (QTPlayerEventHandlerMaker) vec.elementAt(i);
                QTPlayerEventHandler localQTPlayerEventHandler = ((QTPlayerEventHandlerMaker) localObject).make(paramQTPlayer, paramObject);
                if (localQTPlayerEventHandler != null) {
                    return localQTPlayerEventHandler;
                }
            }
        }
        Class localClass = paramObject.getClass();
        Object localObject = localClass.getName();
        while (!localClass.getName().equals("java.lang.Object")) {
            localObject = localClass.getName();
            localClass = localClass.getSuperclass();
        }
        if (((String) localObject).equals("java.awt.Component")) {
            return new QTPlayerAWTEventHandler(paramQTPlayer, paramObject);
        }
        return null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When the QTPlayer is removed from its addedTo interest its event Handler
     * is notified and should remove any interests it has declared for events
     */
    protected abstract void removedFrom();

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When the QTPlayer is added to its addedTo interest its event Handler
     * is notified and should add any interests it requires to receive
     * events.
     */
    protected abstract void addedTo();
}
