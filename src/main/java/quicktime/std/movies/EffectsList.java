package quicktime.std.movies;

import quicktime.internal.toolbox.EffectsManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;


/**
 * This class creates an AtomContainer that contains the necessary
 * information for displaying a list of effects
 */
public final class EffectsList extends AtomContainer implements QuickTimeLib {

    /**
     * Create an effects list from the supplied parameters.
     * @see "QuickTime::QTGetEffectsList"
     * @param minSources minimum number of sources
     * @param maxSources maximum number of sources
     * @param getOptions 0, StdQTConstants.elOptionsIncludeNoneInList
     */
    public EffectsList(int minSources, int maxSources, int getOptions) throws StdQTException {
        super(allocate(minSources, maxSources, getOptions), null, false);
    }

    private static int allocate(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(QTGetEffectsList(arrayOfInt, paramInt1, paramInt2, paramInt3));
        return arrayOfInt[0];
    }

    private static short QTGetEffectsList(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3) {
        return EffectsManager.QTGetEffectsList(paramArrayOfInt, paramInt1, paramInt2, paramInt3);
    }
}
