
package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.io.QTFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.GDevice;
import quicktime.qd.Pict;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;
import quicktime.std.comp.Component;
import quicktime.std.movies.Movie;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.UserData;


public final class SequenceGrabber extends Component implements QuickTimeLib {
    private static Object linkage;

    private SGDispatcher upp;

    private boolean hasStarted;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SequenceGrabber.class);
                    }
                });
            }
        }.establish();
    }

    public SequenceGrabber() throws QTException {
        this(0);
    }

    public SequenceGrabber(int paramInt) throws QTException {
        super(1650553447, paramInt);
        initialize();
    }

    public SequenceGrabber(int paramInt1, int paramInt2) throws QTException {
        super(paramInt1, paramInt2);
        initialize();
    }

    private void initialize() throws QTException {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to capture media with security settings when class is signed");
        }

        int i = 0;
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(arrayOfInt1, arrayOfInt2);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            setGWorld(QDGraphics.scratch, null);
            i = SGInitialize(_ID());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
            this.hasStarted = false;
        }

        if (i < 0) {
            try {
                finalize();
            } catch (Throwable localThrowable) {
            }
            StdQTException.checkError(i);
        }
    }

    public void setGWorld(QDGraphics paramQDGraphics, GDevice paramGDevice) throws StdQTException {
        if (paramQDGraphics == null)
            throw new StdQTException(-50);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = SGSetGWorld(_ID(), QTObject.ID(paramQDGraphics), QTObject.ID(paramGDevice));
        }

        StdQTException.checkError(i);
    }

    public QDGraphics getGWorld() throws StdQTException {
        return QDGraphics.fromSequenceGrabber(this);
    }

    public GDevice getGWorldDevice() throws StdQTException {
        return GDevice.fromSequenceGrabber(this);
    }

    public void disposeChannel(SGChannel paramSGChannel) throws StdQTException {
        paramSGChannel.disposeChannel(this);
    }

    public SGOutput newOutput(DataRef paramDataRef, int paramInt) throws StdQTException {
        return new SGOutput(this, QTObject.ID(paramDataRef), paramDataRef.getType(), paramInt);
    }

    public void disposeOutput(SGOutput paramSGOutput) throws QTException {
        paramSGOutput.disposeQTObject();
    }

    boolean isValid() {
        return stillValid();
    }

    public void writeExtendedMovieData(SGChannel paramSGChannel,
                                       int paramInt1,
                                       int paramInt2,
                                       long paramLong,
                                       SGOutput paramSGOutput) throws StdQTException {
        byte[] arrayOfByte = new byte[8];
        setLongInArray(arrayOfByte, 0, paramLong);

        int i = SGWriteExtendedMovieData(_ID(),
                                         QTObject.ID(paramSGChannel),
                                         paramInt1,
                                         paramInt2,
                                         arrayOfByte,
                                         QTObject.ID(paramSGOutput));
        StdQTException.checkError(i);
    }

    public void startPreview() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = SGStartPreview(_ID());
            this.hasStarted = true;
        }
        StdQTException.checkError(i);
    }

    public void startRecord() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SGStartRecord(_ID());
            StdQTException.checkError(i);
            this.hasStarted = true;
        }
    }

    public void idle() throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        if (this.hasStarted) {
            synchronized (QTNative.globalsLock) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(arrayOfInt1, arrayOfInt2);
                i = SGIdle(_ID());
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                    SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
                }
            }
        }
        StdQTException.checkError(i);
    }

    public boolean idleMore() throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        if (this.hasStarted) {
            synchronized (QTNative.globalsLock) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(arrayOfInt1, arrayOfInt2);
                i = SGIdle(_ID());
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                    SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
                }
            }
        }

        if (i == 56135)
            return false;
        StdQTException.checkError(i);
        return true;
    }

    public void update(Region paramRegion) throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(arrayOfInt1, arrayOfInt2);
            i = SGUpdate(_ID(), QTObject.ID(paramRegion));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
            }
        }
        StdQTException.checkError(i);
    }

    public void stop() throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        QDGraphics localQDGraphics = getGWorld();
        this.hasStarted = false;
        synchronized (QTNative.globalsLock) {
            i = SGStop(_ID());
        }
        StdQTException.checkError(i);
    }

    public void pause(int paramInt) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = SGPause(_ID(), (byte) paramInt);
        }
        StdQTException.checkError(i);
    }

    public int getPause() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        int i = SGGetPause(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0];
    }

    public void prepare(boolean paramBoolean1, boolean paramBoolean2) throws StdQTException {
        int i = 0;
        byte b1 = (byte) (paramBoolean1 ? 1 : 0);
        byte b2 = (byte) (paramBoolean2 ? 1 : 0);
        synchronized (QTNative.globalsLock) {
            i = SGPrepare(_ID(), b1, b2);
        }
        StdQTException.checkError(i);
    }

    public void release() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = SGRelease(_ID());
        }
        StdQTException.checkError(i);
    }

    public Movie getMovie() {
        return Movie.fromSequenceGrabber(this);
    }

    public void setSettings(UserData paramUserData) throws StdQTException {
        int i = SGSetSettings(_ID(), QTObject.ID(paramUserData), 0);

        StdQTException.checkError(i);
    }

    public UserData getSettings() throws StdQTException {
        return UserData.fromSequenceGrabber(this);
    }

    public Pict grabPict(QDRect paramQDRect, int paramInt1, int paramInt2) throws QTException {
        return Pict.fromSequenceGrabber(this, paramQDRect, paramInt1, paramInt2);
    }

    public void setDataOutput(QTFile paramQTFile, int paramInt) throws QTException {
        int i = 0;
        if ((paramInt == 32) && (paramQTFile == null)) {
            synchronized (QTNative.globalsLock) {
                i = SGSetDataOutput(_ID(), null, paramInt);
            }
        }
        synchronized (QTNative.globalsLock) {
            i = SGSetDataOutput(_ID(), paramQTFile.getFSSpec(false, 256), paramInt);
        }

        StdQTException.checkError(i);
    }

    public QTFile getDataOutputFile() throws QTException {
        return QTFile.fromSequenceGrabber(this);
    }

    public int getDataOutputFlags() throws QTException {
        byte[] arrayOfByte = new byte[(QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 'F' : 256];
        int[] arrayOfInt = {
            0
        };
        int i = SGGetDataOutput(_ID(), arrayOfByte, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void setDataRef(DataRef paramDataRef, int paramInt) throws StdQTException {
        int i = 0;

        if (paramDataRef != null) {
            synchronized (QTNative.globalsLock) {
                i = SGSetDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), paramInt);
            }
        }

        synchronized (QTNative.globalsLock) {
            i = SGSetDataRef(_ID(), 0, 0, paramInt);
        }

        StdQTException.checkError(i);
    }

    public DataRef getDataRef() throws StdQTException {
        return DataRef.fromSequenceGrabber(this);
    }

    public int getDataRefFlags() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int i = SGGetDataRef(_ID(), arrayOfInt1, arrayOfInt2, arrayOfInt3);

        StdQTException.checkError(i);
        return arrayOfInt3[0];
    }

    public void setMaximumRecordTime(int paramInt) throws StdQTException {
        StdQTException.checkError(SGSetMaximumRecordTime(_ID(), paramInt));
    }

    public int getMaximumRecordTime() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SGGetMaximumRecordTime(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    public int getStorageSpaceRemaining() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SGGetStorageSpaceRemaining(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    public int getTimeRemaining() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SGGetTimeRemaining(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    public boolean isPreviewMode() throws StdQTException {
        byte[] arrayOfByte1 = new byte[1];
        byte[] arrayOfByte2 = new byte[1];
        int i = SGGetMode(_ID(), arrayOfByte1, arrayOfByte2);
        StdQTException.checkError(i);
        return arrayOfByte1[0] != 0;
    }

    public boolean isRecordMode() throws StdQTException {
        byte[] arrayOfByte1 = new byte[1];
        byte[] arrayOfByte2 = new byte[1];
        int i = SGGetMode(_ID(), arrayOfByte1, arrayOfByte2);
        StdQTException.checkError(i);
        return arrayOfByte2[0] != 0;
    }

    public TimeBase getTimeBase() throws StdQTException {
        return TimeBase.fromSequenceGrabber(this);
    }

    public int getIndChannelType(int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SGGetIndChannel(_ID(), (short) paramInt, 0, arrayOfInt));
        return arrayOfInt[0];
    }

    public SGChannel getIndChannel(int paramInt) throws QTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        StdQTException.checkError(SGGetIndChannel(_ID(), (short) paramInt, arrayOfInt1, arrayOfInt2));

        switch (arrayOfInt2[0]) {
        case 1936684398:
            return new SGSoundChannel(arrayOfInt1[0], this);
        case 1952807028:
            return new SGTextChannel(arrayOfInt1[0], this);
        case 1986618469:
            return new SGVideoChannel(arrayOfInt1[0], this);
        case 1836413801:
            return new SGMusicChannel(arrayOfInt1[0], this);
        }
        throw new QTException("Unknown SGChannel - please report bug");
    }

    public void setDataProc(SGDataProc paramSGDataProc) throws StdQTException {
        int i = 0;
        if (paramSGDataProc == null) {
            removeDataProc();
            return;
        }
        if (this.upp != null) {
            removeDataProc();
        }
        this.upp = new SGDispatcher(this, paramSGDataProc);
        synchronized (QTNative.globalsLock) {
            i = SGSetDataProc(_ID(), this.upp.ID(), 0);
        }
        StdQTException.checkError(i);
    }

    public void removeDataProc() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = SGSetDataProc(_ID(), 0, 0);
        }
        StdQTException.checkError(i);
        if (this.upp != null) {
            this.upp.cleanup();
            this.upp = null;
        }
    }

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native int SGInitialize(int paramInt);

    private static native int SGSetGWorld(int paramInt1, int paramInt2, int paramInt3);

    private static native int SGWriteExtendedMovieData(int paramInt1,
                                                       int paramInt2,
                                                       int paramInt3,
                                                       int paramInt4,
                                                       byte[] paramArrayOfByte,
                                                       int paramInt5);

    private static native int SGStartPreview(int paramInt);

    private static native int SGStartRecord(int paramInt);

    private static native int SGIdle(int paramInt);

    private static native int SGUpdate(int paramInt1, int paramInt2);

    private static native int SGStop(int paramInt);

    private static native int SGPause(int paramInt, byte paramByte);

    private static native int SGGetPause(int paramInt, byte[] paramArrayOfByte);

    private static native int SGPrepare(int paramInt, byte paramByte1, byte paramByte2);

    private static native int SGRelease(int paramInt);

    private static native int SGSetSettings(int paramInt1, int paramInt2, int paramInt3);

    private static native int SGSetDataOutput(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    private static native int SGGetDataOutput(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native int SGSetDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int SGGetDataRef(int paramInt,
                                           int[] paramArrayOfInt1,
                                           int[] paramArrayOfInt2,
                                           int[] paramArrayOfInt3);

    private static native int SGSetMaximumRecordTime(int paramInt1, int paramInt2);

    private static native int SGGetMaximumRecordTime(int paramInt, int[] paramArrayOfInt);

    private static native int SGGetStorageSpaceRemaining(int paramInt, int[] paramArrayOfInt);

    private static native int SGGetTimeRemaining(int paramInt, int[] paramArrayOfInt);

    private static native int SGGetMode(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int SGGetGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int SGGrabPict(int paramInt1,
                                         int[] paramArrayOfInt,
                                         byte[] paramArrayOfByte,
                                         short paramShort,
                                         int paramInt2);

    private static native int SGGetIndChannel(int paramInt1, short paramShort, int paramInt2, int[] paramArrayOfInt);

    private static native int SGGetIndChannel(int paramInt, short paramShort, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int SGSetDataProc(int paramInt1, int paramInt2, int paramInt3);

    private static native void setLongInArray(byte[] paramArrayOfByte, int paramInt, long paramLong);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/sg/
 * SequenceGrabber.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
