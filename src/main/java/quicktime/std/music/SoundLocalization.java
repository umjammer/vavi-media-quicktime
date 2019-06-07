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
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /SoundLocalization.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
