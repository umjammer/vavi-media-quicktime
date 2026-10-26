package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTObject;
import quicktime.internal.toolbox.SoundManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;

/**
 * Provides general routines for recording sound
 */
public final class Sound implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(Sound.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Plays a sound from memory.
     * QuickTime::SndPlay
     * @param sndHdl the handle that has the sound data in it
     * @param {@link quicktime.sound.SoundException}
     */
    public static final void play(SndHandle paramSndHandle) throws SoundException {
        SoundException.checkError(SndPlay(0, QTObject.ID(paramSndHandle), (byte) 0));
    }

    private static short SndPlay(int paramInt1, int paramInt2, byte paramByte) {
        return SoundManager.SndPlay(paramInt1, paramInt2, paramByte);
    }
}
