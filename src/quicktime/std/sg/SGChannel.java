
package quicktime.std.sg;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.movies.media.UserData;
import quicktime.util.QTUtils;


public abstract class SGChannel extends Component implements QuickTimeLib {
    private static Object linkage;

    private SequenceGrabber owner;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SGChannel.class);
                    }
                });
            }
        }.establish();
    }

    SGChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
        setOwner(paramSequenceGrabber);
    }

    SGChannel(SequenceGrabber paramSequenceGrabber, int paramInt) throws StdQTException {
        super(allocate(paramSequenceGrabber, paramInt), paramSequenceGrabber);
        setOwner(paramSequenceGrabber);
    }

    private static int allocate(SequenceGrabber paramSequenceGrabber, int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SGNewChannel(QTObject.ID(paramSequenceGrabber), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    private void setOwner(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        if (paramSequenceGrabber == null)
            throw new StdQTException(-50);
        this.owner = paramSequenceGrabber;
    }

    void disposeChannel(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        if (this.owner != paramSequenceGrabber) {
            throw new StdQTException(-50);
        }
        StdQTException.checkError(SGDisposeChannel(QTObject.ID(this.owner), _ID()));

        setNR(0);
        this.owner = null;
    }

    public final SampleDescription getSampleDescription() throws QTException {
        SampleDescription localSampleDescription = makeDescription();
        int i = SGGetChannelSampleDescription(_ID(), QTObject.ID(localSampleDescription));
        StdQTException.checkError(i);
        return localSampleDescription;
    }

    public SequenceGrabber getSequenceGrabber() {
        return this.owner;
    }

    public void settingsDialog() throws StdQTException {
        final int[] arrayOfInt = null;
        final int i = _ID();

        int j;

        if (QTSession.isCurrentOS(4)) {
            j = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                public Object invoke() {
                    int l = SGChannel.SGSettingsDialog(QTObject.ID(SGChannel.this.owner),
                                                       i,
                                                       (short) 0,
                                                       arrayOfInt,
                                                       0,
                                                       0,
                                                       0);

                    return l;
                }
            })).intValue();
            StdQTException.checkError(j);
        } else {
            synchronized (QTNative.globalsLock) {
                j = SGSettingsDialog(QTObject.ID(this.owner), _ID(), (short) 0, arrayOfInt, 0, 0, 0);

                StdQTException.checkError(j);
            }
        }
    }

    public void settingsDialog(int paramInt, Component[] paramArrayOfComponent) throws StdQTException {
        int i = 0;
        final int j = i;
        int[] arrayOfInt1 = null;
        final int[] arrayOfInt2 = arrayOfInt1;
        final int k = paramInt;
        int m = 0;
        final int n = _ID();

        if (paramArrayOfComponent != null) {
            i = paramArrayOfComponent.length;
            arrayOfInt1 = new int[i];
            for (int i1 = 0; i1 < i; i1++) {
                arrayOfInt1[i1] = QTObject.ID(paramArrayOfComponent[i1]);
            }
        }
        if (QTSession.isCurrentOS(4)) {
            int o = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                public Object invoke() {
                    int l = SGChannel.SGSettingsDialog(QTObject.ID(SGChannel.this.owner),
                                                       n,
                                                       (short) j,
                                                       arrayOfInt2,
                                                       k,
                                                       0,
                                                       0);

                    return l;
                }
            })).intValue();
            StdQTException.checkError(o);
        } else {
            synchronized (QTNative.globalsLock) {
                int o = SGSettingsDialog(QTObject.ID(this.owner), _ID(), (short) j, arrayOfInt1, paramInt, 0, 0);

                StdQTException.checkError(o);
            }
        }
    }

    public void setDataSourceName(String paramString, int paramInt) throws StdQTException {
        int i = SGChannelSetDataSourceName(_ID(), QTUtils.String2PString(paramString, 255), paramInt);

        StdQTException.checkError(i);
    }

    public SGDataSourceInfo getDataSourceName() throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[256];

        int i = SGChannelGetDataSourceName(_ID(), arrayOfByte, arrayOfInt);

        StdQTException.checkError(i);

        return new SGDataSourceInfo(QTUtils.PString2String(arrayOfByte, 0), arrayOfInt[0]);
    }

    public void setSettings(UserData paramUserData) throws StdQTException {
        int i = SGSetChannelSettings(QTObject.ID(this.owner), _ID(), QTObject.ID(paramUserData), 0);

        StdQTException.checkError(i);
    }

    public UserData getSettings() throws StdQTException {
        return UserData.fromSGChannel(this);
    }

    public void setUsage(int paramInt) throws StdQTException {
        StdQTException.checkError(SGSetChannelUsage(_ID(), paramInt));
    }

    public int getUsage() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SGGetChannelUsage(_ID(), arrayOfInt));

        return arrayOfInt[0];
    }

    public void setPlayFlags(int paramInt) throws StdQTException {
        StdQTException.checkError(SGSetChannelPlayFlags(_ID(), paramInt));
    }

    public int getPlayFlags() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SGGetChannelPlayFlags(_ID(), arrayOfInt));

        return arrayOfInt[0];
    }

    public void setDevice(String paramString) throws StdQTException {
        StdQTException.checkError(SGSetChannelDevice(_ID(), QTUtils.String2PString(paramString, 255)));
    }

    public SGDeviceList getDeviceList(int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        paramInt ^= 0x1;
        int i = SGGetChannelDeviceList(_ID(), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return new SGDeviceList(arrayOfInt[0], null, this.owner);
    }

    public int getTimeScale() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = SGGetChannelTimeScale(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void setOutput(SGOutput paramSGOutput) throws StdQTException {
        paramSGOutput.setChannel(this);
    }

    abstract SampleDescription makeDescription() throws QTException;

    private static native int SGSetChannelDevice(int paramInt, byte[] paramArrayOfByte);

    private static native int SGNewChannel(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int SGSettingsDialog(int paramInt1,
                                               int paramInt2,
                                               short paramShort,
                                               int[] paramArrayOfInt,
                                               int paramInt3,
                                               int paramInt4,
                                               int paramInt5);

    private static native int SGChannelSetDataSourceName(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    private static native int SGChannelGetDataSourceName(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native int SGSetChannelSettings(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int SGInitChannel(int paramInt1, int paramInt2);

    private static native int SGSetChannelUsage(int paramInt1, int paramInt2);

    private static native int SGGetChannelUsage(int paramInt, int[] paramArrayOfInt);

    private static native int SGSetChannelPlayFlags(int paramInt1, int paramInt2);

    private static native int SGGetChannelPlayFlags(int paramInt, int[] paramArrayOfInt);

    private static native int SGGetChannelDeviceList(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int SGGetChannelSampleDescription(int paramInt1, int paramInt2);

    private static native int SGGetChannelTimeScale(int paramInt, int[] paramArrayOfInt);

    private static native int SGDisposeChannel(int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/sg/
 * SGChannel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
