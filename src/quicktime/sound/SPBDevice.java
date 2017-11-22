
package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.sg.SGSoundChannel;
import quicktime.util.QTUtils;


public final class SPBDevice extends QTObject implements QuickTimeLib {
    private static Object linkage;

    static {
        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SPBDevice.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public static String getIndexedDevice(int paramInt) throws SoundException {
        byte[] arrayOfByte = new byte[256];
        int[] arrayOfInt = {
            0
        };
        int i = SPBGetIndexedDevice((short) paramInt, arrayOfByte, arrayOfInt);
        if (i == 65315)
            return null;
        DisposeHandle(arrayOfInt[0]);
        SoundException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    public static SPBDevice fromSoundChannel(SGSoundChannel paramSGSoundChannel) throws StdQTException {
        int i = SGGetSoundInputDriver(QTObject.ID(paramSGSoundChannel));
        return new SPBDevice(i, paramSGSoundChannel);
    }

    public SPBDevice(String paramString, int paramInt) throws SoundException {
        super(allocate(paramString, paramInt), null);
    }

    private static int allocate(String paramString, int paramInt) throws SoundException {
        int[] arrayOfInt = {
            0
        };

        if ((paramInt == 1) && (QTSession.hasSecurityRestrictions())) {
            throw new SecurityException("Only able to record sound with security settings when class is signed");
        }

        SoundException.checkError(SPBOpenDevice(paramString == null ? null : QTUtils.String2PString(paramString, 255),
                                                (short) paramInt,
                                                arrayOfInt));

        return arrayOfInt[0];
    }

    private SPBDevice(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    private void getDeviceInfo(int paramInt, byte[] paramArrayOfByte) throws SoundException {
        SoundException.checkError(SPBGetDeviceInfo(_ID(), paramInt, paramArrayOfByte));
    }

    private void getDeviceInfo(int paramInt1, int paramInt2) throws SoundException {
        SoundException.checkError(SPBGetDeviceInfo(_ID(), paramInt1, paramInt2));
    }

    private void getDeviceInfo(int paramInt, short[] paramArrayOfShort) throws SoundException {
        SoundException.checkError(SPBGetDeviceInfo(_ID(), paramInt, paramArrayOfShort));
    }

    private void getDeviceInfo(int paramInt, int[] paramArrayOfInt) throws SoundException {
        SoundException.checkError(SPBGetDeviceInfo(_ID(), paramInt, paramArrayOfInt));
    }

    private void setDeviceInfo(int paramInt, short[] paramArrayOfShort) throws SoundException {
        SoundException.checkError(SPBSetDeviceInfo(_ID(), paramInt, paramArrayOfShort));
    }

    private void setDeviceInfo(int paramInt, int[] paramArrayOfInt) throws SoundException {
        SoundException.checkError(SPBSetDeviceInfo(_ID(), paramInt, paramArrayOfInt));
    }

    public int millisecondsToBytes(int paramInt) throws SoundException {
        int[] arrayOfInt = {
            paramInt
        };
        SoundException.checkError(SPBMillisecondsToBytes(_ID(), arrayOfInt));

        return arrayOfInt[0];
    }

    public int bytesToMilliseconds(int paramInt) throws SoundException {
        int[] arrayOfInt = {
            paramInt
        };
        SoundException.checkError(SPBBytesToMilliseconds(_ID(), arrayOfInt));

        return arrayOfInt[0];
    }

    public void setLevelMeterOnOff(boolean paramBoolean) throws SoundException {
        short[] arrayOfShort = {
            (short) (paramBoolean ? 1 : 0)
        };
        setDeviceInfo(1819108724, arrayOfShort);
    }

    public boolean getLevelMeterOnOff() throws SoundException {
        short[] arrayOfShort = {
            0, 0
        };
        getDeviceInfo(1819108724, arrayOfShort);
        return arrayOfShort[0] != 0;
    }

    public int getLevelMeterLevel() throws SoundException {
        short[] arrayOfShort = {
            0, 0
        };
        getDeviceInfo(1819108724, arrayOfShort);
        return arrayOfShort[1];
    }

    public void setAutomaticGainControl(boolean paramBoolean) throws SoundException {
        short[] arrayOfShort = {
            (short) (paramBoolean ? 1 : 0)
        };
        setDeviceInfo(1634165536, arrayOfShort);
    }

    public boolean getAutomaticGainControl() throws SoundException {
        short[] arrayOfShort = {
            0
        };
        getDeviceInfo(1634165536, arrayOfShort);
        return arrayOfShort[0] != 0;
    }

    public void setInputGain(float paramFloat) throws SoundException {
        int[] arrayOfInt = {
            QTUtils.X2Fix(paramFloat)
        };
        setDeviceInfo(1734437230, arrayOfInt);
    }

    public float getInputGain() throws SoundException {
        int[] arrayOfInt = {
            0
        };
        getDeviceInfo(1734437230, arrayOfInt);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    public int getInputSource() throws SoundException {
        short[] arrayOfShort = {
            0
        };
        getDeviceInfo(1936684402, arrayOfShort);
        return arrayOfShort[0];
    }

    public void setInputSource(int paramInt) throws SoundException {
        short[] arrayOfShort = {
            (short) paramInt
        };
        setDeviceInfo(1936684402, arrayOfShort);
    }

    public String[] getInputSourceNames() throws SoundException {
        byte[] arrayOfByte1 = new byte[4];

        getDeviceInfo(1936613741, arrayOfByte1);
        int j = getIntFromArray(arrayOfByte1, 0);

        int i = getShortFromHandle(j, 0);
        String[] arrayOfString = new String[i];
        int k = 0;

        for (int m = 0; m < i; m++) {
            int n = getByteFromHandle(j, k + 2);
            byte[] arrayOfByte2 = new byte[n + 1];
            copyPointerToArray(getIntFromPointer(j, 0), k + 2, arrayOfByte2, 0, n + 1);
            arrayOfString[m] = QTUtils.PString2String(arrayOfByte2, 0);
            k = k + n + 1;
        }
        DisposeHandle(j);
        return arrayOfString;
    }

    public int getChannelAvailable() throws SoundException {
        short[] arrayOfShort = {
            0
        };
        getDeviceInfo(1667785078, arrayOfShort);
        return arrayOfShort[0];
    }

    public int getNumberChannels() throws SoundException {
        short[] arrayOfShort = {
            0
        };
        getDeviceInfo(1667785070, arrayOfShort);
        return arrayOfShort[0];
    }

    public void setNumberChannels(int paramInt) throws SoundException {
        short[] arrayOfShort = {
            (short) paramInt
        };
        setDeviceInfo(1667785070, arrayOfShort);
    }

    public int getPlayThruOnOff() throws SoundException {
        short[] arrayOfShort = {
            0
        };
        getDeviceInfo(1886155880, arrayOfShort);
        return arrayOfShort[0];
    }

    public void setPlayThruOnOff(int paramInt) throws SoundException {
        short[] arrayOfShort = {
            (short) paramInt
        };
        setDeviceInfo(1886155880, arrayOfShort);
    }

    public void setSampleRate(float paramFloat) throws SoundException {
        int[] arrayOfInt = {
            QTUtils.X2UFix(paramFloat)
        };
        setDeviceInfo(1936875892, arrayOfInt);
    }

    public float getSampleRate() throws SoundException {
        int[] arrayOfInt = {
            0
        };
        getDeviceInfo(1936875892, arrayOfInt);
        return QTUtils.UFix2X(arrayOfInt[0]);
    }

    public float[] getSampleRateAvailable() throws SoundException {
        byte[] arrayOfByte = new byte[6];
        getDeviceInfo(1936941430, arrayOfByte);
        int i = getShortFromArray(arrayOfByte, 0);
        float[] arrayOfFloat = null;
        if (i == 0) {
            arrayOfFloat = new float[2];
        } else {
            arrayOfFloat = new float[i];
        }
        for (int j = 0; j < arrayOfFloat.length; j++) {
            int k = getIntFromHandle(getIntFromArray(arrayOfByte, 2), j * 4);
            arrayOfFloat[j] = QTUtils.UFix2X(k);
        }

        DisposeHandle(getIntFromArray(arrayOfByte, 2));
        return arrayOfFloat;
    }

    public void setSampleSize(int paramInt) throws SoundException {
        short[] arrayOfShort = {
            (short) paramInt
        };
        setDeviceInfo(1936943482, arrayOfShort);
    }

    public int getSampleSize() throws SoundException {
        short[] arrayOfShort = {
            0
        };
        getDeviceInfo(1936943482, arrayOfShort);
        return arrayOfShort[0];
    }

    public int[] getSampleSizeAvailable() throws SoundException {
        byte[] arrayOfByte = new byte[6];
        getDeviceInfo(1936941430, arrayOfByte);
        int i = getShortFromArray(arrayOfByte, 0);
        int[] arrayOfInt = new int[i];
        for (int j = 0; j < i; j++)
            arrayOfInt[j] = getShortFromHandle(getIntFromArray(arrayOfByte, 2), j * 2);
        DisposeHandle(getIntFromArray(arrayOfByte, 2));
        return arrayOfInt;
    }

    public void setStereoInputGain(float paramFloat1, float paramFloat2) throws SoundException {
        int[] arrayOfInt = {
            QTUtils.X2Fix(paramFloat1), QTUtils.X2Fix(paramFloat2)
        };
        setDeviceInfo(1936154985, arrayOfInt);
    }

    public float getStereoInputGainLeft() throws SoundException {
        int[] arrayOfInt = {
            0, 0
        };
        getDeviceInfo(1936154985, arrayOfInt);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    public float getStereoInputGainRight() throws SoundException {
        int[] arrayOfInt = {
            0, 0
        };
        getDeviceInfo(1936154985, arrayOfInt);
        return QTUtils.Fix2X(arrayOfInt[1]);
    }

    public boolean getContinuousRecording() throws SoundException {
        short[] arrayOfShort = {
            0
        };
        getDeviceInfo(1668247156, arrayOfShort);
        return arrayOfShort[0] == 1;
    }

    public void setContinuousRecording(boolean paramBoolean) throws SoundException {
        short[] arrayOfShort = {
            (short) (paramBoolean ? 1 : 0)
        };
        setDeviceInfo(1668247156, arrayOfShort);
    }

    public void setCompressionType(int paramInt) throws SoundException {
        int[] arrayOfInt = {
            paramInt
        };
        setDeviceInfo(1668246896, arrayOfInt);
    }

    public int getCompressionType() throws SoundException {
        int[] arrayOfInt = {
            0
        };
        getDeviceInfo(1668246896, arrayOfInt);
        return arrayOfInt[0];
    }

    public int[] getCompressionAvailable() throws SoundException {
        byte[] arrayOfByte = new byte[6];
        getDeviceInfo(1668112758, arrayOfByte);
        int i = getShortFromArray(arrayOfByte, 0);
        int[] arrayOfInt = new int[i];
        for (int j = 0; j < i; j++)
            arrayOfInt[j] = getIntFromHandle(getIntFromArray(arrayOfByte, 2), j * 4);
        DisposeHandle(getIntFromArray(arrayOfByte, 2));
        return arrayOfInt;
    }

    public boolean hasOptionsDialog() throws SoundException {
        short[] arrayOfShort = {
            0
        };
        getDeviceInfo(1869640804, arrayOfShort);
        return arrayOfShort[0] != 0;
    }

    public void showOptionsDialog() throws SoundException {
        int[] arrayOfInt = {
            0
        };
        setDeviceInfo(1869640804, arrayOfInt);
    }

    public String getName() throws SoundException {
        byte[] arrayOfByte = new byte[256];
        getDeviceInfo(1851878757, arrayOfByte);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    public int[] getActiveLevels() throws SoundException {
        int i = getNumberChannels();
        int j = NewPtr(i * 2);
        getDeviceInfo(1819107683, j);
        int[] arrayOfInt = new int[i];
        for (int k = 0; k < i; k++)
            arrayOfInt[k] = getShortFromPointer(j, k * 2);
        DisposePtr(j);
        return arrayOfInt;
    }

    public String toString() {
        try {
            return getClass().getName() + "[name=" + getName() + "]";
        } catch (QTException localQTException) {
        }
        return super.toString();
    }

    private static native int SGGetSoundInputDriver(int paramInt);

    private static native short SPBOpenDevice(byte[] paramArrayOfByte, short paramShort, int[] paramArrayOfInt);

    private static native short SPBGetDeviceInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short SPBSetDeviceInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short SPBGetDeviceInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native short SPBSetDeviceInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native short SPBGetDeviceInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short SPBGetDeviceInfo(int paramInt1, int paramInt2, int paramInt3);

    private static native short SPBMillisecondsToBytes(int paramInt, int[] paramArrayOfInt);

    private static native short SPBBytesToMilliseconds(int paramInt, int[] paramArrayOfInt);

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native byte getByteFromHandle(int paramInt1, int paramInt2);

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  byte[] paramArrayOfByte,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native short getShortFromPointer(int paramInt1, int paramInt2);

    private static native int getIntFromHandle(int paramInt1, int paramInt2);

    private static native void DisposeHandle(int paramInt);

    private static native short getShortFromHandle(int paramInt1, int paramInt2);

    private static native short SPBGetIndexedDevice(short paramShort, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native int NewPtr(int paramInt);

    private static native void DisposePtr(int paramInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/sound/
 * SPBDevice.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
