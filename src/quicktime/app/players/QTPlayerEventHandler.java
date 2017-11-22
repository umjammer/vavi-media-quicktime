
package quicktime.app.players;

import java.util.Vector;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * @deprecated
 */
public abstract class QTPlayerEventHandler {
    private static Vector vec;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public static void registerEventHandlerMaker(QTPlayerEventHandlerMaker paramQTPlayerEventHandlerMaker) {
        if (vec == null)
            vec = new Vector();
        vec.addElement(paramQTPlayerEventHandlerMaker);
    }

    /**
     * @deprecated
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
                QTPlayerEventHandler localQTPlayerEventHandler = ((QTPlayerEventHandlerMaker) localObject).make(paramQTPlayer,
                                                                                                                paramObject);
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
     * @deprecated
     */
    protected abstract void removedFrom();

    /**
     * @deprecated
     */
    protected abstract void addedTo();
}
