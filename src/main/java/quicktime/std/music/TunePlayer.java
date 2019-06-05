
package quicktime.std.music;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;
import quicktime.std.comp.Component;
import quicktime.std.movies.media.MusicMediaHandler;
import quicktime.util.QTUtils;


public final class TunePlayer extends Component implements QuickTimeLib {
    private static Object linkage;

    Object noteChannels;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(TunePlayer.class);
                    }
                });
            }
        }.establish();
    }

    public static TunePlayer fromMusicMediaHandler(MusicMediaHandler paramMusicMediaHandler,
                                                   int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(MusicMediaGetIndexedTunePlayer(QTObject.ID(paramMusicMediaHandler), paramInt, arrayOfInt));

        return new TunePlayer(arrayOfInt[0], paramMusicMediaHandler);
    }

    private TunePlayer(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public TunePlayer() throws QTException {
        super(1953853029, 0);
    }

    public void setHeader(MusicData paramMusicData) throws StdQTException {
        paramMusicData.lock();
        int i = getIntFromPointer(QTObject.ID(paramMusicData), 0);
        StdQTException.checkError(TuneSetHeader(_ID(), i));
    }

    public TimeBase getTimeBase() throws StdQTException {
        return TimeBase.fromTunePlayer(this);
    }

    public void setTimeScale(int paramInt) throws StdQTException {
        StdQTException.checkError(TuneSetTimeScale(_ID(), paramInt));
    }

    public int getTimeScale() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(TuneGetTimeScale(_ID(), arrayOfInt));

        return arrayOfInt[0];
    }

    public int getNumberOfNoteChannels() throws StdQTException {
        int i = TuneGetIndexedNoteChannel(_ID(), 0, null);
        if (i < 0)
            throw new StdQTException(i);
        return i;
    }

    public NoteChannel getIndexedNoteChannel(int paramInt) throws StdQTException {
        if (paramInt > 0) {
            int[] arrayOfInt = {
                0
            };
            int i = TuneGetIndexedNoteChannel(_ID(), paramInt, arrayOfInt);
            if (i < 0)
                throw new StdQTException(i);
            return new NoteChannel(getNoteAllocator(), arrayOfInt[0], this);
        }
        throw new StdQTException(63461);
    }

    public void queue(MusicData paramMusicData,
                      float paramFloat,
                      int paramInt1,
                      int paramInt2,
                      int paramInt3) throws StdQTException {
        paramMusicData.lock();
        int i = getIntFromPointer(QTObject.ID(paramMusicData), 0);
        StdQTException.checkError(TuneQueue(_ID(), i, QTUtils.X2Fix(paramFloat), paramInt1, paramInt2, paramInt3, 0, 0));
    }

    public void instant(TuneStatus paramTuneStatus, int paramInt) throws StdQTException {
        StdQTException.checkError(TuneInstant(_ID(), paramTuneStatus.getTuneDataPtr(), paramInt));
    }

    public TuneStatus getStatus() throws StdQTException {
        TuneStatus localTuneStatus = new TuneStatus();
        StdQTException.checkError(TuneGetStatus(_ID(), localTuneStatus.getBytes()));

        return localTuneStatus;
    }

    public void stop() throws StdQTException {
        StdQTException.checkError(TuneStop(_ID(), 0));
    }

    public void setVolume(float paramFloat) throws StdQTException {
        StdQTException.checkError(TuneSetVolume(_ID(), QTUtils.X2Fix(paramFloat)));
    }

    public float getVolume() throws StdQTException {
        int i = TuneGetVolume(_ID());
        if (i < 0)
            throw new StdQTException(i);
        return QTUtils.Fix2X(i);
    }

    public void preroll() throws StdQTException {
        StdQTException.checkError(TunePreroll(_ID()));
    }

    public void unroll() throws StdQTException {
        StdQTException.checkError(TuneUnroll(_ID()));
    }

    public void setNoteChannels(NoteChannel[] paramArrayOfNoteChannel) throws StdQTException {
        int i = paramArrayOfNoteChannel.length;
        int[] arrayOfInt = new int[i];
        for (int j = 0; j < i; j++)
            arrayOfInt[j] = QTObject.ID(paramArrayOfNoteChannel[j]);
        StdQTException.checkError(TuneSetNoteChannels(_ID(), i, arrayOfInt, 0, 0));

        this.noteChannels = paramArrayOfNoteChannel;
    }

    public void setPartTranspose(int paramInt1, float paramFloat, int paramInt2) throws StdQTException {
        StdQTException.checkError(TuneSetPartTranspose(_ID(), paramInt1, QTUtils.X2Fix(paramFloat), paramInt2));
    }

    public NoteAllocator getNoteAllocator() {
        return new NoteAllocator(TuneGetNoteAllocator(_ID()), this);
    }

    public void setSofter(boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(TuneSetSofter(_ID(), paramBoolean ? 1 : 0));
    }

    public void task() throws StdQTException {
        StdQTException.checkError(TuneTask(_ID()));
    }

    public void setBalance(int paramInt) throws StdQTException {
        StdQTException.checkError(TuneSetBalance(_ID(), paramInt));
    }

    public void setSoundLocalization(SoundLocalization paramSoundLocalization) throws StdQTException {
        StdQTException.checkError(TuneSetSoundLocalization(_ID(), QTObject.ID(paramSoundLocalization)));
    }

    public void setHeaderWithSize(MusicData paramMusicData, int paramInt) throws StdQTException {
        paramMusicData.lock();
        int i = getIntFromPointer(QTObject.ID(paramMusicData), 0);
        StdQTException.checkError(TuneSetHeaderWithSize(_ID(), i, paramInt));
    }

    public void setPartMix(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2) throws StdQTException {
        StdQTException.checkError(TuneSetPartMix(_ID(),
                                                 paramInt1,
                                                 QTUtils.X2Fix(paramFloat1),
                                                 QTUtils.X2Fix(paramFloat2),
                                                 paramInt2));
    }

    public MixStateInfo getPartMix(int paramInt) throws StdQTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        int[] arrayOfInt3 = {
            0
        };
        StdQTException.checkError(TuneGetPartMix(_ID(), paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3));

        return new MixStateInfo(paramInt, QTUtils.Fix2X(arrayOfInt1[0]), QTUtils.Fix2X(arrayOfInt2[0]), arrayOfInt3[0]);
    }

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native int MusicMediaGetIndexedTunePlayer(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int TuneSetHeader(int paramInt1, int paramInt2);

    private static native int TuneSetTimeScale(int paramInt1, int paramInt2);

    private static native int TuneGetTimeScale(int paramInt, int[] paramArrayOfInt);

    private static native int TuneGetIndexedNoteChannel(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int TuneQueue(int paramInt1,
                                        int paramInt2,
                                        int paramInt3,
                                        int paramInt4,
                                        int paramInt5,
                                        int paramInt6,
                                        int paramInt7,
                                        int paramInt8);

    private static native int TuneInstant(int paramInt1, int paramInt2, int paramInt3);

    private static native int TuneGetStatus(int paramInt, byte[] paramArrayOfByte);

    private static native int TuneStop(int paramInt1, int paramInt2);

    private static native int TuneSetVolume(int paramInt1, int paramInt2);

    private static native int TuneGetVolume(int paramInt);

    private static native int TunePreroll(int paramInt);

    private static native int TuneUnroll(int paramInt);

    private static native int TuneSetNoteChannels(int paramInt1,
                                                  int paramInt2,
                                                  int[] paramArrayOfInt,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native int TuneSetPartTranspose(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int TuneGetNoteAllocator(int paramInt);

    private static native int TuneSetSofter(int paramInt1, int paramInt2);

    private static native int TuneTask(int paramInt);

    private static native int TuneSetBalance(int paramInt1, int paramInt2);

    private static native int TuneSetSoundLocalization(int paramInt1, int paramInt2);

    private static native int TuneSetHeaderWithSize(int paramInt1, int paramInt2, int paramInt3);

    private static native int TuneSetPartMix(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native int TuneGetPartMix(int paramInt1,
                                             int paramInt2,
                                             int[] paramArrayOfInt1,
                                             int[] paramArrayOfInt2,
                                             int[] paramArrayOfInt3);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /TunePlayer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
