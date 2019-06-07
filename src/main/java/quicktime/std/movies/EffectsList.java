package quicktime.std.movies;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;

/**
 * This class creates an AtomContainer that contains the necessary
 * information for displaying a list of effects
 */
public final class EffectsList extends AtomContainer implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(EffectsList.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Create an effects list from the supplied parameters.
     * QuickTime::QTGetEffectsList
     * @param minSources minimum number of sources
     * @param maxSources maximum number of sources
     * @param getOptions 0, StdQTConstants.elOptionsIncludeNoneInList
     */
    public EffectsList(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        super(allocate(paramInt1, paramInt2, paramInt3), null, false);
    }

    private static int allocate(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(QTGetEffectsList(arrayOfInt, paramInt1, paramInt2, paramInt3));
        return arrayOfInt[0];
    }

    private static native short QTGetEffectsList(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3);
}
