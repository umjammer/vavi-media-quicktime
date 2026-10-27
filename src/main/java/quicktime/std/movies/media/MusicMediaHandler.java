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
     * @see "QuickTime::MusicMediaGetIndexedTunePlayer()"
     * @param index the index within the media.
     * @throws quicktime.std.StdQTException
     */
    public TunePlayer getIndexedTunePlayer(int paramInt) throws StdQTException {
        return TunePlayer.fromMusicMediaHandler(this, paramInt);
    }
}

