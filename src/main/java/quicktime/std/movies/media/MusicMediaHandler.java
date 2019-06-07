package quicktime.std.movies.media;

import quicktime.std.StdQTException;
import quicktime.std.music.TunePlayer;

public final class MusicMediaHandler extends SoundMediaHandler {

    MusicMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * Returns the TunePlayer that is responsible for rendering the music data that is contained
     * within a particular music track. If the movie or this media is edited in any way then the
     * returned TunePlayer becomes invalid and must be got again.
     * QuickTime::MusicMediaGetIndexedTunePlayer()
     * @param index the index within the media.
     * @param {@link quicktime.std.StdQTException}
     */
    public TunePlayer getIndexedTunePlayer(int paramInt) throws StdQTException {
        return TunePlayer.fromMusicMediaHandler(this, paramInt);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/MusicMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
