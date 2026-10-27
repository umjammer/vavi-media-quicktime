package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;

public final class QTVRMediaHandler extends MediaHandler implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(QTVRMediaHandler.class));
            }
        }.establish();
    }

    QTVRMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }
}
