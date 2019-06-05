
package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDisplaySpace;
import quicktime.app.display.QTDrawable;
import quicktime.app.image.Transformable;


/**
 * @deprecated
 */
public class BounceAction extends MatrixAction {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public BounceAction(int paramInt1,
            int paramInt2,
            QTDrawable paramQTDrawable,
            Transformable paramTransformable,
            float paramFloat1,
            float paramFloat2) throws QTException {
        super(paramInt1,
              paramInt2,
              (QTDisplaySpace) paramQTDrawable,
              paramTransformable,
              new TranslateMatrix(paramFloat1, paramFloat2));
    }
}
