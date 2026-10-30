
package quicktime.app.view;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import quicktime.QTSession;
import quicktime.internal.toolbox.PlatformManager;


final class QTJavaJNIAWTCanvas {

    private static final Logger logger = System.getLogger(QTJavaJNIAWTCanvas.class.getName());

    static void RemoveNewContext(Object context) {
        PlatformManager.RemoveNewContext(context);
    }

    static {

        if (QTSession.isCurrentOS(2)) {
            if (QTSession.getJavaVersion() == 65537) {
                System.loadLibrary("QTJava");
            } else {
                try {
                    System.loadLibrary("QTJava");
                } catch (Exception e) {
                    logger.log(Level.ERROR, e.getMessage(), e);
                }
            }
        }
    }
}
