
package quicktime.app.audio;

import quicktime.QTException;


/**
 * @deprecated
 */
public abstract interface MusicScore extends ExtendedAudioSpec {
    /**
     * @deprecated
     */
    public abstract int getPartCount() throws QTException;

    /**
     * @deprecated
     */
    public abstract MusicPart getPart(int paramInt) throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/audio
 * /MusicScore.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
