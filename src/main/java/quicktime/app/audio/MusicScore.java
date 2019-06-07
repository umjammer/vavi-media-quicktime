package quicktime.app.audio;

import quicktime.QTException;

/**
 * Deprecated.
 * This is an interface which defines methods that abstract
 * the usage of a collection of MusicParts which typically
 * make up the collection of instruments that play a music score.
 */
public abstract interface MusicScore extends ExtendedAudioSpec {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the number of parts.
     * @return the number of parts
     * @return {@link quicktime.QTException}
     */
    public abstract int getPartCount() throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the MusicPart responsible for a given part in
     * the score.
     * @param part the part number of the required note channel
     * @return the MusicPart.
     * @return {@link quicktime.QTException}
     */
    public abstract MusicPart getPart(int paramInt) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/audio
 * /MusicScore.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
