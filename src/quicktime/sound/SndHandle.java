
package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.jdirect.SoundLib;
import quicktime.util.QTHandle;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;


public final class SndHandle extends QTHandle implements QuickTimeLib, SoundLib {
    private static Object linkage;

    private QTPointerRef buf;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SndHandle.class);
                    }
                });
            }
        }.establish();
    }

    public SndHandle() throws QTException {
        super(200, false);
    }

    public SndHandle(int paramInt, boolean paramBoolean) throws QTException {
        super(paramInt, paramBoolean);
    }

    public SndHandle(int paramInt1, float paramFloat, int paramInt2, int paramInt3) throws QTException {
        this(paramInt1, paramFloat, paramInt2, paramInt3, 60);
    }

    public SndHandle(int paramInt1, float paramFloat, int paramInt2, int paramInt3, int paramInt4) throws QTException {
        super(100, false);
        int i = setupHeader(paramInt1, paramFloat, paramInt2, paramInt3, paramInt4, 0);
        setSize(i);
    }

    public int setupHeader(int paramInt1, float paramFloat, int paramInt2, int paramInt3, int paramInt4) throws SoundException {
        return setupHeader(paramInt1, paramFloat, paramInt2, paramInt3, 60, paramInt4);
    }

    public int setupHeader(int paramInt1,
                           float paramFloat,
                           int paramInt2,
                           int paramInt3,
                           int paramInt4,
                           int paramInt5) throws SoundException {
        short[] arrayOfShort = {
            0
        };
        SoundException.checkError(SetupSndHeader(_ID(),
                                                 (short) paramInt1,
                                                 QTUtils.X2UFix(paramFloat),
                                                 (short) paramInt2,
                                                 paramInt3,
                                                 (short) paramInt4,
                                                 paramInt5,
                                                 arrayOfShort));
        return arrayOfShort[0];
    }

    public final int getSoundHeaderOffset() throws SoundException {
        int[] arrayOfInt = {
            0
        };
        SoundException.checkError(GetSoundHeaderOffset(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    public QTPointerRef getSoundData() throws QTException {
        if (this.buf != null) {
            return this.buf;
        }

        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        SoundComponentData localSoundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), localSoundComponentData.getBytes(), arrayOfInt1, arrayOfInt2));

        if (arrayOfInt2[0] >= getSize())
            throw new QTException("No data in SoundHandle yet");
        lock();
        return toQTPointer(arrayOfInt2[0], getSize() - arrayOfInt2[0]);
    }

    public void appendSoundData(QTPointerRef paramQTPointerRef) throws QTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        SoundComponentData localSoundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), localSoundComponentData.getBytes(), arrayOfInt1, arrayOfInt2));

        if (paramQTPointerRef != null) {
            setSize(arrayOfInt2[0] + paramQTPointerRef.getSize());
            BlockMove(QTObject.ID(paramQTPointerRef), lockAndDeref(arrayOfInt2[0]), paramQTPointerRef.getSize());
        } else {
            setSize(getSoundHeaderOffset());
        }
        this.buf = null;
        setIntAt(getSoundHeaderOffset(), 0);
    }

    public void appendSoundBuffer(int paramInt) throws QTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        SoundComponentData localSoundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), localSoundComponentData.getBytes(), arrayOfInt1, arrayOfInt2));

        setSize(arrayOfInt2[0] + paramInt);
        this.buf = null;
        setIntAt(getSoundHeaderOffset(), 0);
    }

    public void setSoundData(QTPointerRef paramQTPointerRef) throws QTException {
        int i = getSoundHeaderOffset();
        setIntAt(i, QTObject.ID(paramQTPointerRef));
        this.buf = paramQTPointerRef;
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        SoundComponentData localSoundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), localSoundComponentData.getBytes(), arrayOfInt1, arrayOfInt2));
        setSize(arrayOfInt2[0]);
    }

    public final SndInfo parseSndHeader() throws SoundException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        SoundComponentData localSoundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), localSoundComponentData.getBytes(), arrayOfInt1, arrayOfInt2));

        return new SndInfo(localSoundComponentData, arrayOfInt1[0], arrayOfInt2[0]);
    }

    private static native short SetupSndHeader(int paramInt1,
                                               short paramShort1,
                                               int paramInt2,
                                               short paramShort2,
                                               int paramInt3,
                                               short paramShort3,
                                               int paramInt4,
                                               short[] paramArrayOfShort);

    private static native short GetSoundHeaderOffset(int paramInt, int[] paramArrayOfInt);

    private static native void BlockMove(int paramInt1, int paramInt2, int paramInt3);

    private static native short ParseSndHeader(int paramInt,
                                               byte[] paramArrayOfByte,
                                               int[] paramArrayOfInt1,
                                               int[] paramArrayOfInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/sound/
 * SndHandle.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
