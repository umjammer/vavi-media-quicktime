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

    private static int allocate(int minSources, int maxSources, int getOptions) throws StdQTException {
        int[] returnedList = { 0 };
        StdQTException.checkError(QTGetEffectsList(returnedList, minSources, maxSources, getOptions));
        return returnedList[0];
    }

    private static short QTGetEffectsList(int[] returnedList, int minSources, int maxSources, int getOptions) {
        return EffectsManager.QTGetEffectsList(returnedList, minSources, maxSources, getOptions);
    }
}
