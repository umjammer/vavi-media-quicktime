package quicktime.app.audio;

import quicktime.std.movies.media.MusicMediaHandler;

/**
 * @deprecated
 * This class is a specialized version of the generic AudioMediaControl class which
 * provides added capability for dealing specifically with MusicMedia.
 */
public class MusicMediaControl extends AudioMediaControl implements MusicScore {

    private final MusicMediaHandler mh;

    static {
        if ((quicktime.QTSession.isCurrentOS(4)) && (quicktime.QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * The constructor needs to know which MusicMedia object is being controlled.
     * @param m the MusicMedia object being controlled
     */
    public MusicMediaControl(quicktime.std.movies.media.MusicMedia paramMusicMedia) throws quicktime.QTException {
        super(paramMusicMedia);
        this.mh = ((MusicMediaHandler) this.audioHandler);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the number of parts.
     * @return the number of parts
     * @throws quicktime.QTException
     */
    @Override
    public int getPartCount() throws quicktime.QTException {
        return this.mh.getIndexedTunePlayer(1).getNumberOfNoteChannels();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the NoteChannel responsible for a given part in
     * the score.
     * @param part the part number of the required note channel
     * @return the NoteChannel requested
     * @throws quicktime.QTException
     */
    @Override
    public MusicPart getPart(int paramInt) throws quicktime.QTException {
        return new MusicPartControl(this.mh, paramInt);
    }
}
