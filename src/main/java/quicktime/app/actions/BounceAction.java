package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDisplaySpace;
import quicktime.app.display.QTDrawable;
import quicktime.app.image.Transformable;


/**
 * This class provides the capability of moving and bouncing a Transformable
 * object around within the space provided by the QTDrawable space that is
 * the space within which the transformable object is displayed, by the amount specified
 * by the deltaMatrix object.
 *
 * @deprecated
 */
@Deprecated
public class BounceAction extends MatrixAction {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Constructs a BounceAction object.
     *
     * @param scale  the amount with which a second is divided into at a rate of one
     * @param period the number of scale ticks that elapse between invocations of the action.
     * @param space  the space within which the object will be bounced and moved
     * @param t      the target of the bounce action - the object that is moved
     * @param deltaX the amount by which the object is moved in the X dimension
     * @param deltaY the amount by which the object is moved in the Y dimension
     * @since QTJava 6.1
     * @deprecated
     */
    public BounceAction(int scale, int period, QTDrawable space, Transformable t, float deltaX, float deltaY) throws QTException {
        super(scale, period, (QTDisplaySpace) space, t, new TranslateMatrix(deltaX, deltaY));
    }
}
