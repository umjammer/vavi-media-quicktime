
package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.std.StdQTException;
import quicktime.std.music.MusicData;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;


public final class MusicDescription extends SampleDescription implements InterfaceLib, PrimitivesLib {
    private static Object linkage;

    private static final int kNativeSize = 20;

    private static EndianDescriptor ed;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(MusicDescription.class);
                    }
                });
            }
        }.establish();
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = SampleDescription.makeED();

        EndianFlipSpec localEndianFlipSpec = new EndianFlipSpec(16, 4, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec);
        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    public MusicDescription() throws QTException {
        super(kNativeSize, true, 1836413801);
    }

    private MusicDescription(int paramInt) {
        super(paramInt, null, false);
    }

    public Object clone() {
        return new MusicDescription(makeAndCopyHandle());
    }

    public void setMusicFlags(int paramInt) {
        setIntAt(16, paramInt);
    }

    public int getMusicFlags() {
        return getIntAt(16);
    }

    public MusicData getTuneHeader() throws QTException {
        if (getSize() == 20) {
            return null;
        }
        int i = getSize() - 20;
        MusicData localMusicData = new MusicData(i);
        localMusicData.lock();
        int j = getIntFromPointer(QTObject.ID(localMusicData), 0);
        BlockMove(lockAndDeref(20), j, i);
        if (!isLocked())
            unlock();
        localMusicData.unlock();
        return localMusicData;
    }

    public void setTuneHeader(MusicData paramMusicData) throws StdQTException {
        int i = paramMusicData.getSize();
        setSize(20 + i);
        paramMusicData.lock();
        int j = getIntFromPointer(QTObject.ID(paramMusicData), 0);
        BlockMove(j, lockAndDeref(20), i);
        if (!isLocked())
            unlock();
        paramMusicData.unlock();
    }

    private static native void BlockMove(int paramInt1, int paramInt2, int paramInt3);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/MusicDescription.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
