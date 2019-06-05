
package quicktime.app.audio;

import quicktime.std.movies.media.MusicMediaHandler;


/**
 * @deprecated
 */
public class MusicMediaControl extends AudioMediaControl implements MusicScore {
    private MusicMediaHandler mh;

    static {
        if ((quicktime.QTSession.isCurrentOS(4)) && (quicktime.QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public MusicMediaControl(quicktime.std.movies.media.MusicMedia paramMusicMedia) throws quicktime.QTException {
        super(paramMusicMedia);
        this.mh = ((MusicMediaHandler) this.audioHandler);
    }

    /**
     * @deprecated
     */
    public int getPartCount() throws quicktime.QTException {
        return this.mh.getIndexedTunePlayer(1).getNumberOfNoteChannels();
    }

    /**
     * @deprecated
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
