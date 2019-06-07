package quicktime.app.audio;

import quicktime.std.movies.media.MusicMediaHandler;

/**
 * Deprecated.
 * This class is a specialized version of the generic AudioMediaControl class which
 * provides added capability for dealing specifically with MusicMedia.
 */
public class MusicMediaControl extends AudioMediaControl implements MusicScore {

    private MusicMediaHandler mh;

    static {
        if ((quicktime.QTSession.isCurrentOS(4)) && (quicktime.QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The constructor needs to know which MusicMedia object is being controlled.
     * @param m the MusicMedia object being controlled
     */
    public MusicMediaControl(quicktime.std.movies.media.MusicMedia paramMusicMedia) throws quicktime.QTException {
        super(paramMusicMedia);
        this.mh = ((MusicMediaHandler) this.audioHandler);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the number of parts.
     * @return the number of parts
     * @return {@link quicktime.QTException}
     */
    public int getPartCount() throws quicktime.QTException {
        return this.mh.getIndexedTunePlayer(1).getNumberOfNoteChannels();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the NoteChannel responsible for a given part in
     * the score.
     * @param part the part number of the required note channel
     * @return the NoteChannel requested
     * @return {@link quicktime.QTException}
     */
    public MusicPart getPart(int paramInt) throws quicktime.QTException {
        return new MusicPartControl(this.mh, paramInt);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/audio
 * /MusicMediaControl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
