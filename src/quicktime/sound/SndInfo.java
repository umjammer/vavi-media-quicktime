
package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.io.OpenFile;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.SoundLib;
import quicktime.util.QTUtils;


public final class SndInfo implements SoundLib, InterfaceLib {
    private static Object linkage;

    public SoundComponentData sndData;

    public int numFrames;

    public int dataOffset;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SndInfo.class);
                    }
                });
            }
        }.establish();
    }

    public static void setupAIFFHeader(OpenFile paramOpenFile,
                                       int paramInt1,
                                       float paramFloat,
                                       int paramInt2,
                                       int paramInt3,
                                       long paramLong1,
                                       long paramLong2) throws SoundException {
        int i = SetupAIFFHeader((short) QTObject.ID(paramOpenFile),
                                (short) paramInt1,
                                QTUtils.X2UFix(paramFloat),
                                (short) paramInt2,
                                paramInt3,
                                (int) paramLong1,
                                (int) paramLong2);

        SoundException.checkError(i);
    }

    public static SndInfo parseAIFFHeader(OpenFile paramOpenFile) throws SoundException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        SoundComponentData localSoundComponentData = new SoundComponentData();
        SoundException.checkError(ParseAIFFHeader((short) QTObject.ID(paramOpenFile),
                                                  localSoundComponentData.getBytes(),
                                                  arrayOfInt1,
                                                  arrayOfInt2));

        return new SndInfo(localSoundComponentData, arrayOfInt1[0], arrayOfInt2[0]);
    }

    SndInfo(SoundComponentData paramSoundComponentData, int paramInt1, int paramInt2) {
        this.sndData = paramSoundComponentData;
        this.numFrames = paramInt1;
        this.dataOffset = paramInt2;
    }

    private static native short ParseAIFFHeader(short paramShort,
                                                byte[] paramArrayOfByte,
                                                int[] paramArrayOfInt1,
                                                int[] paramArrayOfInt2);

    private static native short SetupAIFFHeader(short paramShort1,
                                                short paramShort2,
                                                int paramInt1,
                                                short paramShort3,
                                                int paramInt2,
                                                int paramInt3,
                                                int paramInt4);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/sound/
 * SndInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
