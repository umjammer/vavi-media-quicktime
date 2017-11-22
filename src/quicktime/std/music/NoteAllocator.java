
package quicktime.std.music;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.Pict;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.movies.Track;
import quicktime.util.QTUtils;


public final class NoteAllocator extends Component implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(NoteAllocator.class);
                    }
                });
            }
        }.establish();
    }

    static NoteAllocator defaultNA = null;

    int res;

    public static NoteAllocator getDefault() throws QTException {
        if (defaultNA == null) {
            defaultNA = new NoteAllocator();
        } else if (!defaultNA.stillValid()) {
            defaultNA = new NoteAllocator();
        }
        return defaultNA;
    }

    public NoteAllocator() throws QTException {
        this(0);
    }

    public NoteAllocator(int paramInt) throws QTException {
        super(1852798049, paramInt);
    }

    NoteAllocator(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public int numNoteChannels() throws StdQTException {
        int i = NAGetIndNoteChannel(_ID(), 0, null, null);
        if (i < 0)
            StdQTException.checkError(i);
        return i;
    }

    public NoteChannel getIndNoteChannel(int paramInt) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = NAGetIndNoteChannel(_ID(), paramInt, arrayOfInt1, arrayOfInt2);
        if (i < 0)
            StdQTException.checkError(i);
        return new NoteChannel(this, arrayOfInt1[0], this);
    }

    public void task() throws StdQTException {
        StdQTException.checkError(NATask(_ID()));
    }

    public void pickArrangement(String paramString1, Track paramTrack, String paramString2) throws StdQTException {
        final String str1 = paramString1;
        final Track localTrack = paramTrack;
        final String str2 = paramString2;
        final int i = _ID();

        if (QTSession.isCurrentOS(4)) {
            this.res = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                public Object invoke() {
                    NoteAllocator.this.res = NoteAllocator.NAPickArrangement(i,
                                                                             0,
                                                                             QTUtils.String2PString(str1, 255),
                                                                             0,
                                                                             0,
                                                                             QTObject.ID(localTrack),
                                                                             QTUtils.String2PString(str2, 255));

                    return new Integer(NoteAllocator.this.res);
                }
            })).intValue();
            StdQTException.checkError(this.res);
        } else {
            synchronized (QTNative.globalsLock) {
                StdQTException.checkError(NAPickArrangement(_ID(),
                                                            0,
                                                            QTUtils.String2PString(paramString1, 255),
                                                            0,
                                                            0,
                                                            QTObject.ID(paramTrack),
                                                            QTUtils.String2PString(paramString2, 255)));
            }
        }
    }

    public void copyrightDialog(Pict paramPict,
                                String paramString1,
                                String paramString2,
                                String paramString3,
                                String paramString4) throws StdQTException {
        final String str1 = paramString1;
        final String str2 = paramString2;
        final String str3 = paramString3;
        final Pict localPict = paramPict;
        final String str4 = paramString4;
        final int i = _ID();

        if (QTSession.isCurrentOS(4)) {
            this.res = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                public Object invoke() {
                    NoteAllocator.this.res = NoteAllocator.NACopyrightDialog(i,
                                                                             QTObject.ID(localPict),
                                                                             QTUtils.String2PString(str1, 255),
                                                                             QTUtils.String2PString(str2, 255),
                                                                             QTUtils.String2PString(str3, 255),
                                                                             QTUtils.String2PString(str4, 255),
                                                                             0,
                                                                             0);

                    return new Integer(NoteAllocator.this.res);
                }
            })).intValue();
            StdQTException.checkError(this.res);
        } else {
            synchronized (QTNative.globalsLock) {
                StdQTException.checkError(NACopyrightDialog(_ID(),
                                                            QTObject.ID(paramPict),
                                                            QTUtils.String2PString(paramString1, 255),
                                                            QTUtils.String2PString(paramString2, 255),
                                                            QTUtils.String2PString(paramString3, 255),
                                                            QTUtils.String2PString(paramString4, 255),
                                                            0,
                                                            0));
            }
        }
    }

    public int numMusicComponents() throws StdQTException {
        int i = NAGetRegisteredMusicDevice(_ID(), 0, null, null, null, null);
        if (i < 0)
            StdQTException.checkError(i);
        return i;
    }

    public String registerMusicDevice_SoundBank(QTFile paramQTFile) throws QTException {
        byte[] arrayOfByte = new byte[32];
        StdQTException
                .checkError(NARegisterMusicDevice(_ID(), 0, arrayOfByte, new SynthesizerConnections(paramQTFile).getBytes()));

        return QTUtils.PString2String(arrayOfByte, 0);
    }

    public void registerMusicDevice(int paramInt,
                                    String paramString,
                                    SynthesizerConnections paramSynthesizerConnections) throws StdQTException {
        StdQTException.checkError(NARegisterMusicDevice(_ID(),
                                                        paramInt,
                                                        QTUtils.String2PString(paramString, 31),
                                                        paramSynthesizerConnections.getBytes()));
    }

    public void unregisterMusicDevice(int paramInt) throws StdQTException {
        StdQTException.checkError(NAUnregisterMusicDevice(_ID(), paramInt));
    }

    public MusicComponentInfo getRegisteredMusicDevice(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int[] arrayOfInt1 = new int[1];
        SynthesizerConnections localSynthesizerConnections = new SynthesizerConnections();
        int[] arrayOfInt2 = new int[1];
        int i = NAGetRegisteredMusicDevice(_ID(),
                                           paramInt,
                                           arrayOfInt1,
                                           arrayOfByte,
                                           localSynthesizerConnections.getBytes(),
                                           arrayOfInt2);
        if (i < 0)
            StdQTException.checkError(i);
        MusicComponentInfo localMusicComponentInfo = new MusicComponentInfo();
        localMusicComponentInfo.synthType = arrayOfInt1[0];
        localMusicComponentInfo.name = new String(arrayOfByte, 1, arrayOfByte[0]);
        localMusicComponentInfo.mc = new MusicComponent(arrayOfInt2[0], null);
        localMusicComponentInfo.connections = localSynthesizerConnections;
        return localMusicComponentInfo;
    }

    public SynthesizerConnections getDefaultMIDIInput() throws StdQTException {
        SynthesizerConnections localSynthesizerConnections = new SynthesizerConnections();
        StdQTException.checkError(NAGetDefaultMIDIInput(_ID(), localSynthesizerConnections.getBytes()));
        return localSynthesizerConnections;
    }

    public void setDefaultMIDIInput(SynthesizerConnections paramSynthesizerConnections) throws StdQTException {
        StdQTException.checkError(NASetDefaultMIDIInput(_ID(), paramSynthesizerConnections.getBytes()));
    }

    public void saveMusicConfiguration() throws StdQTException {
        StdQTException.checkError(NASaveMusicConfiguration(_ID()));
    }

    public QTMIDIPortList getMIDIInPorts() throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NAGetMIDIPorts(_ID(), arrayOfInt, null));

        return new QTMIDIPortList(arrayOfInt[0]);
    }

    public QTMIDIPortList getMIDIOutPorts() throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NAGetMIDIPorts(_ID(), null, arrayOfInt));

        return new QTMIDIPortList(arrayOfInt[0]);
    }

    public void loseDefaultMIDIInput() throws StdQTException {
        StdQTException.checkError(NALoseDefaultMIDIInput(_ID()));
    }

    private static native int NAGetIndNoteChannel(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int NATask(int paramInt);

    private static native int NAPickArrangement(int paramInt1,
                                                int paramInt2,
                                                byte[] paramArrayOfByte1,
                                                int paramInt3,
                                                int paramInt4,
                                                int paramInt5,
                                                byte[] paramArrayOfByte2);

    private static native int NACopyrightDialog(int paramInt1,
                                                int paramInt2,
                                                byte[] paramArrayOfByte1,
                                                byte[] paramArrayOfByte2,
                                                byte[] paramArrayOfByte3,
                                                byte[] paramArrayOfByte4,
                                                int paramInt3,
                                                int paramInt4);

    private static native int NAGetRegisteredMusicDevice(int paramInt1,
                                                         int paramInt2,
                                                         int[] paramArrayOfInt1,
                                                         byte[] paramArrayOfByte1,
                                                         byte[] paramArrayOfByte2,
                                                         int[] paramArrayOfInt2);

    private static native int NARegisterMusicDevice(int paramInt1,
                                                    int paramInt2,
                                                    byte[] paramArrayOfByte1,
                                                    byte[] paramArrayOfByte2);

    private static native int NAUnregisterMusicDevice(int paramInt1, int paramInt2);

    private static native int NAGetDefaultMIDIInput(int paramInt, byte[] paramArrayOfByte);

    private static native int NASetDefaultMIDIInput(int paramInt, byte[] paramArrayOfByte);

    private static native int NASaveMusicConfiguration(int paramInt);

    private static native int NAGetMIDIPorts(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int NALoseDefaultMIDIInput(int paramInt);
}
