package quicktime.std.music;

import quicktime.QTException;
import quicktime.util.QTHandleRef;

/**
 * The Sound Localization class contains data that locates a sound in pseudo 3 dimensional space.
 */
public final class SoundLocalization extends QTHandleRef {

    /**
     * This constructor creates an empty sound localisation
     */
    public SoundLocalization() throws QTException {
        super(32, true);
    }
}
