
package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.movies.media.TimeCodeMedia;
import quicktime.std.movies.media.UserData;
import quicktime.std.movies.media.VisualMediaHandler;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;


public final class TimeCoder extends VisualMediaHandler implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(TimeCoder.class);
                    }
                });
            }
        }.establish();
    }

    public static TimeCoder fromMedia(TimeCodeMedia paramTimeCodeMedia) throws StdQTException {
        int i = GetMediaHandler(QTObject.ID(paramTimeCodeMedia));
        StdQTException.checkError(GetMoviesError());
        return new TimeCoder(i, paramTimeCodeMedia);
    }

    private TimeCoder(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public TimeCodeInfo getCurrent() throws QTException {
        return QTHandle.fromTimeCoderCurrent(this);
    }

    public TimeCodeInfo getAtTime(int paramInt) throws QTException {
        return QTHandle.fromTimeCoderTime(this, paramInt);
    }

    public String timeCodeToString(TimeCodeDef paramTimeCodeDef, TimeCodeTime paramTimeCodeTime) throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        StdQTException
                .checkError(TCTimeCodeToString(_ID(), paramTimeCodeDef.getBytes(), paramTimeCodeTime.getBytes(), arrayOfByte));

        return new String(arrayOfByte, 1, arrayOfByte[0]);
    }

    public String timeCodeToString(TimeCodeDef paramTimeCodeDef, int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[256];
        int[] arrayOfInt = {
            paramInt
        };
        StdQTException.checkError(TCTimeCodeToString(_ID(), paramTimeCodeDef.getBytes(), arrayOfInt, arrayOfByte));

        return new String(arrayOfByte, 1, arrayOfByte[0]);
    }

    public int toFrameNumber(TimeCodeTime paramTimeCodeTime, TimeCodeDef paramTimeCodeDef) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = TCTimeCodeToFrameNumber(_ID(), paramTimeCodeDef.getBytes(), paramTimeCodeTime.getBytes(), arrayOfInt);

        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public TimeCodeTime toTimeCode(int paramInt, TimeCodeDef paramTimeCodeDef) throws StdQTException {
        TimeCodeTime localTimeCodeTime = new TimeCodeTime();
        int i = TCFrameNumberToTimeCode(_ID(), paramInt, paramTimeCodeDef.getBytes(), localTimeCodeTime.getBytes());

        StdQTException.checkError(i);
        return localTimeCodeTime;
    }

    /**
     * @deprecated
     */
    public QTHandle getSourceRef(TimeCodeDescription paramTimeCodeDescription) throws QTException {
        return QTHandle.fromTimeCoderSource(this, paramTimeCodeDescription);
    }

    public UserData getUserDataSourceRef(TimeCodeDescription paramTimeCodeDescription) throws QTException {
        return UserData.fromTimeCoderSource(this, paramTimeCodeDescription);
    }

    public void setSourceRef(TimeCodeDescription paramTimeCodeDescription, QTHandleRef paramQTHandleRef) throws StdQTException {
        StdQTException.checkError(TCSetSourceRef(_ID(), QTObject.ID(paramTimeCodeDescription), QTObject.ID(paramQTHandleRef)));
    }

    public void setUserDataSourceRef(TimeCodeDescription paramTimeCodeDescription,
                                     UserData paramUserData) throws StdQTException {
        StdQTException.checkError(TCSetSourceRef(_ID(), QTObject.ID(paramTimeCodeDescription), QTObject.ID(paramUserData)));
    }

    public void setFlags(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(TCSetTimeCodeFlags(_ID(), paramInt1, paramInt2));
    }

    public int getFlags() throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(TCGetTimeCodeFlags(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    public void setDisplayOptions(TCTextOptions paramTCTextOptions) throws StdQTException {
        StdQTException.checkError(TCSetDisplayOptions(_ID(), paramTCTextOptions.getBytes()));
    }

    public TCTextOptions getDisplayOptions() throws StdQTException {
        TCTextOptions localTCTextOptions = new TCTextOptions();
        StdQTException.checkError(TCGetDisplayOptions(_ID(), localTCTextOptions.getBytes()));
        return localTCTextOptions;
    }

    private static native int GetMediaHandler(int paramInt);

    private static native short GetMoviesError();

    private static native int TCTimeCodeToString(int paramInt,
                                                 byte[] paramArrayOfByte1,
                                                 byte[] paramArrayOfByte2,
                                                 byte[] paramArrayOfByte3);

    private static native int TCTimeCodeToString(int paramInt,
                                                 byte[] paramArrayOfByte1,
                                                 int[] paramArrayOfInt,
                                                 byte[] paramArrayOfByte2);

    private static native int TCTimeCodeToFrameNumber(int paramInt,
                                                      byte[] paramArrayOfByte1,
                                                      byte[] paramArrayOfByte2,
                                                      int[] paramArrayOfInt);

    private static native int TCFrameNumberToTimeCode(int paramInt1,
                                                      int paramInt2,
                                                      byte[] paramArrayOfByte1,
                                                      byte[] paramArrayOfByte2);

    private static native int TCSetSourceRef(int paramInt1, int paramInt2, int paramInt3);

    private static native int TCSetTimeCodeFlags(int paramInt1, int paramInt2, int paramInt3);

    private static native int TCGetTimeCodeFlags(int paramInt, int[] paramArrayOfInt);

    private static native int TCSetDisplayOptions(int paramInt, byte[] paramArrayOfByte);

    private static native int TCGetDisplayOptions(int paramInt, byte[] paramArrayOfByte);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/TimeCoder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
