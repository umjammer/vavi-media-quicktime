
package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.SoundLib;
import quicktime.std.clocks.Clock;
import quicktime.std.movies.media.LevelMeterInfo;
import quicktime.std.movies.media.MediaEQSpectrumBands;
import quicktime.util.QTUtils;


public final class SndChannel extends QTObject implements InterfaceLib, SoundLib {
    private static Object linkage;

    private static SoundRunner tempUPP;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SndChannel.class);
                    }
                });
            }
        }.establish();
    }

    public SndChannel() throws QTException {
        this(5, 192);
    }

    public SndChannel(int paramInt1, int paramInt2) throws QTException {
        super(allocate((short) paramInt1, paramInt2));
    }

    public SndChannel(SoundCallBack paramSoundCallBack) throws QTException {
        this(5, 192, paramSoundCallBack);
    }

    public SndChannel(int paramInt1, int paramInt2, SoundCallBack paramSoundCallBack) throws QTException {
        super(allocate((short) paramInt1, paramInt2, paramSoundCallBack));
        if (paramSoundCallBack != null) {
            this.sndUPP = tempUPP;
            this.sndUPP.setSndChannel(this);
        }
        tempUPP = null;
    }

    private static int allocate(short paramShort, int paramInt, SoundCallBack paramSoundCallBack) throws QTException {
        int[] arrayOfInt = {
            0
        };

        if (paramSoundCallBack != null) {
            tempUPP = new SoundRunner(paramSoundCallBack);
            SoundException.checkError(SndNewChannel(arrayOfInt, paramShort, paramInt, tempUPP.getClosure()));
        } else {
            SoundException.checkError(SndNewChannel(arrayOfInt, paramShort, paramInt, 0));
        }

        return arrayOfInt[0];
    }

    private static int allocate(short paramShort, int paramInt) throws SoundException {
        int[] arrayOfInt = {
            0
        };
        SoundException.checkError(SndNewChannel(arrayOfInt, paramShort, paramInt, 0));

        return arrayOfInt[0];
    }

    private boolean quietNow = false;

    private byte[] scratchArray;

    private SoundRunner sndUPP;

    protected void _dispose() throws QTException {
        if (this.sndUPP != null) {
            this.sndUPP.cleanup();
            this.sndUPP = null;
        }
    }

    public boolean isQuietNow() {
        return this.quietNow;
    }

    public void setQuietNow(boolean paramBoolean) {
        this.quietNow = paramBoolean;
    }

    public void play(SndHandle paramSndHandle) throws SoundException {
        SoundException.checkError(SndPlay(_ID(), QTObject.ID(paramSndHandle), (byte) 1));
    }

    public boolean isFinished() throws SoundException {
        if (this.scratchArray == null)
            this.scratchArray = new byte[24];
        int i = SndChannelStatus(_ID(), (short) 24, this.scratchArray);
        SoundException.checkError(i);
        return (this.scratchArray[12] == 0) && (this.scratchArray[14] == 0);
    }

    public boolean isPaused() throws SoundException {
        if (this.scratchArray == null)
            this.scratchArray = new byte[24];
        int i = SndChannelStatus(_ID(), (short) 24, this.scratchArray);
        SoundException.checkError(i);
        return this.scratchArray[14] != 0;
    }

    public SCStatus status() throws SoundException {
        SCStatus localSCStatus = new SCStatus();
        int i = SndChannelStatus(_ID(), (short) 24, localSCStatus.getBytes());
        SoundException.checkError(i);
        return localSCStatus;
    }

    public void doCommand(SndCommand paramSndCommand) throws SoundException {
        doCommand(paramSndCommand, true);
    }

    public void doCommand(SndCommand paramSndCommand, boolean paramBoolean) throws SoundException {
        SoundException.checkError(SndDoCommand(_ID(), QTObject.ID(paramSndCommand), (byte) (paramBoolean ? 1 : 0)));
    }

    public void doImmediate(SndCommand paramSndCommand) throws SoundException {
        SoundException.checkError(SndDoImmediate(_ID(), QTObject.ID(paramSndCommand)));
    }

    public boolean getLevelMeterOnOff() throws SoundException {
        byte[] arrayOfByte = {
            0
        };
        SoundException.checkError(SndGetInfo(_ID(), 1819108724, arrayOfByte));

        return arrayOfByte[0] != 0;
    }

    public void setLevelMeterOnOff(boolean paramBoolean) throws SoundException {
        SoundException.checkError(SndSetInfo(_ID(), 1819108724, (byte) (paramBoolean ? 1 : 0)));
    }

    public void enableClock(boolean paramBoolean) throws SoundException {
        SoundException.checkError(SndSetInfo(_ID(), 1935895659, (byte) (paramBoolean ? 1 : 0)));
    }

    public Clock getClock() throws SoundException {
        return Clock.fromSndChannel(this);
    }

    public LevelMeterInfo getActiveLevels() throws SoundException {
        LevelMeterInfo localLevelMeterInfo = new LevelMeterInfo();
        int i = SndGetInfo(_ID(), 1819107683, localLevelMeterInfo.getBytes());
        return localLevelMeterInfo;
    }

    public MediaEQSpectrumBands getEQSpectrumBands(int paramInt) throws SoundException {
        MediaEQSpectrumBands localMediaEQSpectrumBands = new MediaEQSpectrumBands(paramInt);
        SoundException.checkError(SndGetInfo(_ID(), 1701933922, localMediaEQSpectrumBands.getBytes()));

        return localMediaEQSpectrumBands;
    }

    public void setEQSpectrumBands(MediaEQSpectrumBands paramMediaEQSpectrumBands) throws SoundException {
        SoundException.checkError(SndSetInfo(_ID(), 1701933922, paramMediaEQSpectrumBands.getBytes()));
    }

    public int[] getEQSpectrumLevels(int paramInt) throws SoundException {
        byte[] arrayOfByte = new byte[paramInt];
        SoundException.checkError(SndGetInfo(_ID(), 1701932150, arrayOfByte));

        int[] arrayOfInt = new int[paramInt];
        for (int i = 0; i < arrayOfInt.length; i++) {
            arrayOfInt[i] = QTUtils.UByte2Int(arrayOfByte[i]);
        }
        return arrayOfInt;
    }

    public boolean getEQToneControlOnOff() throws SoundException {
        byte[] arrayOfByte = {
            0
        };
        SoundException.checkError(SndGetInfo(_ID(), 1701934179, arrayOfByte));

        return arrayOfByte[0] != 0;
    }

    public void setEQToneControlOnOff(boolean paramBoolean) throws SoundException {
        byte[] arrayOfByte = {
            0
        };
        SoundException.checkError(SndSetInfo(_ID(), 1701934179, (byte) (paramBoolean ? 1 : 0)));
    }

    public void setEQToneControlGain(int paramInt1, int paramInt2) throws SoundException {
        int i = QTUtils.X2ShortFix(paramInt1);
        int j = QTUtils.X2ShortFix(paramInt2);
        int k = j << 16 | i & 0xFFFF;
        SoundException.checkError(SndSetInfo(_ID(), 1701934183, k));
    }

    public int[] getEQToneControlGain() throws SoundException {
        int[] arrayOfInt = new int[2];
        SoundException.checkError(SndGetInfo(_ID(), 1701934183, arrayOfInt));

        int i = arrayOfInt[0];
        arrayOfInt[0] = (i | 0xFFFF);
        arrayOfInt[1] = (i >>> 16);
        return arrayOfInt;
    }

    private static native short SndNewChannel(int[] paramArrayOfInt, short paramShort, int paramInt1, int paramInt2);

    private static native short SndPlay(int paramInt1, int paramInt2, byte paramByte);

    private static native short SndChannelStatus(int paramInt, short paramShort, byte[] paramArrayOfByte);

    private static native short SndDoCommand(int paramInt1, int paramInt2, byte paramByte);

    private static native short SndDoImmediate(int paramInt1, int paramInt2);

    private static native short SndSetInfo(int paramInt1, int paramInt2, byte paramByte);

    private static native short SndSetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short SndSetInfo(int paramInt1, int paramInt2, int paramInt3);

    private static native short SndGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short SndGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/sound/
 * SndChannel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
