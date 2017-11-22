
package quicktime.std.movies.media;

import quicktime.std.StdQTException;
import quicktime.std.music.TunePlayer;


public final class MusicMediaHandler extends SoundMediaHandler {
    MusicMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public TunePlayer getIndexedTunePlayer(int paramInt) throws StdQTException {
        return TunePlayer.fromMusicMediaHandler(this, paramInt);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/MusicMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
