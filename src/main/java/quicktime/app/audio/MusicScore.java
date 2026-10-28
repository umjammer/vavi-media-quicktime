package quicktime.app.audio;

import quicktime.QTException;

/**
 * @deprecated
 * This is an interface which defines methods that abstract
 * the usage of a collection of MusicParts which typically
 * make up the collection of instruments that play a music score.
 */
public interface MusicScore extends ExtendedAudioSpec {

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the number of parts.
     * @return the number of parts
     * @throws quicktime.QTException
     */
    int getPartCount() throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the MusicPart responsible for a given part in
     * the score.
     * @param part the part number of the required note channel
     * @return the MusicPart.
     * @throws quicktime.QTException
     */
    MusicPart getPart(int part) throws QTException;
}
