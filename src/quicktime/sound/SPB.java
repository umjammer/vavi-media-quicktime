
package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.util.QTByteObject;
import quicktime.util.QTPointerRef;


public final class SPB extends QTByteObject implements quicktime.jdirect.QuickTimeLib, quicktime.jdirect.PrimitivesLib {
    private static Object linkage;

    public static final int kNativeSize = 38;

    private SPBDevice device;

    private QTPointerRef buffer;

    private SoundRunner runner;

    private quicktime.util.QTPointer realObject;

    static {
        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SPB.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public SPB(SPBDevice paramSPBDevice, int paramInt1, int paramInt2, QTPointerRef paramQTPointerRef) {
        super(38);
        try {
            this.realObject = new quicktime.util.QTPointer(38, true);
        } catch (quicktime.QTException localQTException) {
            throw new quicktime.QTRuntimeException(localQTException);
        }
        setIntAt(0, QTObject.ID(paramSPBDevice));
        setIntInPointer(QTObject.ID(this.realObject), 0, QTObject.ID(paramSPBDevice));

        setIntAt(12, paramQTPointerRef.getSize());
        setIntInPointer(QTObject.ID(this.realObject), 12, paramQTPointerRef.getSize());

        this.device = paramSPBDevice;
        setCount(paramInt1);
        setMilliseconds(paramInt2);
        setBuffer(paramQTPointerRef);
    }

    public void setCompletionProc(SICompletion paramSICompletion) {
        if (this.runner != null)
            this.runner.cleanup();
        if (paramSICompletion == null) {
            setIntAt(20, 0);
            setIntInPointer(QTObject.ID(this.realObject), 20, 0);
            this.runner = null;
        } else {
            this.runner = new SoundRunner(this, paramSICompletion);
            int i = this.runner.getClosure();
            setIntAt(20, i);
            setIntInPointer(QTObject.ID(this.realObject), 20, i);
        }
    }

    public void removeCompletionProc() {
        setCompletionProc(null);
    }

    public int getCount() {
        return getIntFromPointer(QTObject.ID(this.realObject), 4);
    }

    public void setCount(int paramInt) {
        setIntAt(4, paramInt);
        setIntInPointer(QTObject.ID(this.realObject), 4, paramInt);
    }

    public int getBufferLength() {
        return getIntFromPointer(QTObject.ID(this.realObject), 12);
    }

    public int getMilliseconds() {
        return getIntFromPointer(QTObject.ID(this.realObject), 8);
    }

    public void setMilliseconds(int paramInt) {
        setIntAt(8, paramInt);
        setIntInPointer(QTObject.ID(this.realObject), 8, paramInt);
    }

    public SPBDevice getDevice() {
        return this.device;
    }

    public void setBuffer(QTPointerRef paramQTPointerRef) {
        setIntAt(16, QTObject.ID(paramQTPointerRef));
        setIntInPointer(QTObject.ID(this.realObject), 16, QTObject.ID(paramQTPointerRef));
        setIntAt(12, paramQTPointerRef.getSize());
        setIntInPointer(QTObject.ID(this.realObject), 12, paramQTPointerRef.getSize());
        this.buffer = paramQTPointerRef;
    }

    public QTPointerRef getBuffer() {
        return this.buffer;
    }

    public short getError() {
        return getShortFromPointer(QTObject.ID(this.realObject), 32);
    }

    public void record(boolean paramBoolean) throws SoundException {
        int i = 0;
        byte b = (byte) (paramBoolean ? 1 : 0);

        setShortAt(32, (short) 0);
        setShortInPointer(QTObject.ID(this.realObject), 32, (short) 0);
        synchronized (quicktime.jdirect.QTNative.globalsLock) {
            i = SPBRecord(QTObject.ID(this.realObject), b);
        }
        SoundException.checkError(i);
    }

    public void pauseRecording() throws SoundException {
        SoundException.checkError(SPBPauseRecording(getIntFromPointer(QTObject.ID(this.realObject), 0)));
    }

    public void resumeRecording() throws SoundException {
        SoundException.checkError(SPBResumeRecording(getIntFromPointer(QTObject.ID(this.realObject), 0)));
    }

    public void stopRecording() throws SoundException {
        SoundException.checkError(SPBStopRecording(getIntFromPointer(QTObject.ID(this.realObject), 0)));
    }

    public boolean isRecording() throws SoundException {
        getRecordingStatus();
        if (this.recordingStatus[0] > 0)
            return true;
        if (this.recordingStatus[0] == 0)
            return false;
        throw new SoundException(this.recordingStatus[0]);
    }

    public short meterLevel() throws SoundException {
        getRecordingStatus();
        return this.meterLevelValue[0];
    }

    public int totalSamplesToRecord() throws SoundException {
        getRecordingStatus();
        return this.totalSamplesToRecordVal[0];
    }

    public int numberOfSamplesRecorded() throws SoundException {
        getRecordingStatus();
        return this.numberOfSamplesRecordedVal[0];
    }

    public int totalMsecsToRecord() throws SoundException {
        getRecordingStatus();
        return this.totalMsecsToRecordVal[0];
    }

    public int numberOfMsecsRecorded() throws SoundException {
        getRecordingStatus();
        return this.numberOfMsecsRecordedVal[0];
    }

    private short[] recordingStatus = {
        0
    };

    private short[] meterLevelValue = {
        0
    };

    private int[] totalSamplesToRecordVal = {
        0
    };

    private int[] numberOfSamplesRecordedVal = {
        0
    };

    private int[] totalMsecsToRecordVal = {
        0
    };

    private int[] numberOfMsecsRecordedVal = {
        0
    };

    private final void getRecordingStatus() throws SoundException {
        int i = SPBGetRecordingStatus(getIntFromPointer(QTObject.ID(this.realObject), 0),
                                      this.recordingStatus,
                                      this.meterLevelValue,
                                      this.totalSamplesToRecordVal,
                                      this.numberOfSamplesRecordedVal,
                                      this.totalMsecsToRecordVal,
                                      this.numberOfMsecsRecordedVal);

        SoundException.checkError(i);
    }

    private static native short getShortFromPointer(int paramInt1, int paramInt2);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native void setIntInPointer(int paramInt1, int paramInt2, int paramInt3);

    private static native void setShortInPointer(int paramInt1, int paramInt2, short paramShort);

    private static native short SPBRecord(int paramInt, byte paramByte);

    private static native short SPBPauseRecording(int paramInt);

    private static native short SPBResumeRecording(int paramInt);

    private static native short SPBStopRecording(int paramInt);

    private static native short SPBGetRecordingStatus(int paramInt,
                                                      short[] paramArrayOfShort1,
                                                      short[] paramArrayOfShort2,
                                                      int[] paramArrayOfInt1,
                                                      int[] paramArrayOfInt2,
                                                      int[] paramArrayOfInt3,
                                                      int[] paramArrayOfInt4);
}
