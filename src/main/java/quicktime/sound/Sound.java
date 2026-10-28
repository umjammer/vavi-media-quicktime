package quicktime.sound;

import quicktime.QTObject;
import quicktime.internal.toolbox.SoundManager;
import quicktime.jdirect.QuickTimeLib;


/**
 * Provides general routines for recording sound
 */
public final class Sound implements QuickTimeLib {

    /**
     * Plays a sound from memory.
     *
     * @param sndHdl the handle that has the sound data in it
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndPlay"
     */
    public static void play(SndHandle sndHdl) throws SoundException {
        SoundException.checkError(SndPlay(0, QTObject.ID(sndHdl), (byte) 0));
    }

    private static short SndPlay(int paramInt1, int paramInt2, byte paramByte) {
        return SoundManager.SndPlay(paramInt1, paramInt2, paramByte);
    }
}
