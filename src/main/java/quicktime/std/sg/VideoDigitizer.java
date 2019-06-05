
package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;


public final class VideoDigitizer extends Component implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(VideoDigitizer.class);
                    }
                });
            }
        }.establish();
    }

    static VideoDigitizer makeVideoDigitizer(int paramInt, Object paramObject) {
        return paramInt == 0 ? null : new VideoDigitizer(paramInt, paramObject);
    }

    private VideoDigitizer(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public VideoDigitizer() throws QTException {
        this(0);
    }

    public VideoDigitizer(int paramInt) throws QTException {
        super(1986618471, paramInt);
    }

    public int getNumberOfInputs() throws StdQTException {
        short[] arrayOfShort = {
            0
        };
        int i = VDGetNumberOfInputs(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public void setInput(int paramInt) throws StdQTException {
        int i = VDSetInput(_ID(), (short) paramInt);
        StdQTException.checkError(i);
    }

    public int getInput() throws StdQTException {
        short[] arrayOfShort = {
            0
        };
        int i = VDGetInput(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public void setClipRgn(Region paramRegion) throws StdQTException {
        int i = VDSetClipRgn(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(i);
    }

    public void clearClipRgn(Region paramRegion) throws StdQTException {
        int i = VDClearClipRgn(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(i);
    }

    public void setClipState(boolean paramBoolean) throws StdQTException {
        int i = VDSetClipState(_ID(), (short) (paramBoolean ? 1 : 0));
        StdQTException.checkError(i);
    }

    public boolean getClipState() throws StdQTException {
        short[] arrayOfShort = {
            0
        };
        int i = VDGetClipState(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0] != 0;
    }

    public DigitizerInfo getDigitizerInfo() throws StdQTException {
        DigitizerInfo localDigitizerInfo = new DigitizerInfo();

        int i = VDGetDigitizerInfo(_ID(), localDigitizerInfo.getBytes());
        StdQTException.checkError(i);
        return localDigitizerInfo;
    }

    public int getInputFormat(int paramInt) throws StdQTException {
        short[] arrayOfShort = {
            0
        };
        int i = VDGetInputFormat(_ID(), (short) paramInt, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public void setInputStandard(int paramInt) throws StdQTException {
        int i = VDSetInputStandard(_ID(), (short) paramInt);
        StdQTException.checkError(i);
    }

    private static native int VDGetNumberOfInputs(int paramInt, short[] paramArrayOfShort);

    private static native int VDSetInput(int paramInt, short paramShort);

    private static native int VDGetInput(int paramInt, short[] paramArrayOfShort);

    private static native int VDSetClipRgn(int paramInt1, int paramInt2);

    private static native int VDClearClipRgn(int paramInt1, int paramInt2);

    private static native int VDSetClipState(int paramInt, short paramShort);

    private static native int VDGetClipState(int paramInt, short[] paramArrayOfShort);

    private static native int VDGetDigitizerInfo(int paramInt, byte[] paramArrayOfByte);

    private static native int VDGetInputFormat(int paramInt, short paramShort, short[] paramArrayOfShort);

    private static native int VDSetInputStandard(int paramInt, short paramShort);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/sg/
 * VideoDigitizer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
