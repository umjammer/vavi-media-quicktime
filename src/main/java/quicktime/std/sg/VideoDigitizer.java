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

/**
 * The video digitizing channel.
 * The VideoDigitizer implements methods for a corresponding QuickTime
 * ComponentInstance structure.
 * refer to VideoDigitizer component in QuickTime
 */
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

    /**
     * Opens the specified video component,'vidg', with any subType.
     * SGPanels should not be accessable by the public.
     * QuickTime::OpenADefaultComponent
     */
    public VideoDigitizer() throws QTException {
        this(0);
    }

    /**
     * Opens the specified video component,'vidg', with the given subType.
     * SGPanels should not be accessable by the public.
     * QuickTime::OpenADefaultComponent
     * @param subType The video component sub type
     */
    public VideoDigitizer(int paramInt) throws QTException {
        super(1986618471, paramInt);
    }

    /**
     * Returns the number of input sources that a video digitizer component supports.
     * QuickTime::VDGetNumberOfInputs
     * @return the number of inputs
     * @return {@link quicktime.std.StdQTException}
     */
    public int getNumberOfInputs() throws StdQTException {
        short[] arrayOfShort = { 0 };
        int i = VDGetNumberOfInputs(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Allows applications to select an input video source for a video digitizer component.
     * This is a 0 based index.
     * QuickTime::VDSetInput
     * @param input the input video source.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInput(int paramInt) throws StdQTException {
        int i = VDSetInput(_ID(), (short) paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Returns the index that identifies the currently active input video source.
     * QuickTime::VDGetInput
     * @return the index for the current input device
     * @return {@link quicktime.std.StdQTException}
     */
    public int getInput() throws StdQTException {
        short[] arrayOfShort = { 0 };
        int i = VDGetInput(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Allows you to define a clipping region.
     * QuickTime::VDSetClipRgn
     * @param clipRegion specifies the clip region
     * @param {@link quicktime.std.StdQTException}
     */
    public void setClipRgn(Region paramRegion) throws StdQTException {
        int i = VDSetClipRgn(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(i);
    }

    /**
     * Allows you to disable all or part of a clipping region that was previously set.
     * QuickTime::VDClearClipRgn
     * @param clipRegion specifies the clipping region to clear. This must correspond to all
     *  or part of a previously set clip region.
     * @param {@link quicktime.std.StdQTException}
     */
    public void clearClipRgn(Region paramRegion) throws StdQTException {
        int i = VDClearClipRgn(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(i);
    }

    /**
     * Allows your application to control whether clipping is enabled or disabled.
     * QuickTime::VDSetClipState
     * @param clipEnable if true clipping is enabled, otherwise it is disabled.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setClipState(boolean paramBoolean) throws StdQTException {
        int i = VDSetClipState(_ID(), (short) (paramBoolean ? 1 : 0));
        StdQTException.checkError(i);
    }

    /**
     * Allows your application to determine if clipping is enabled or not.
     * QuickTime::VDGetClipState
     * @return if true clipping is enabled otherwise it isn't.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean getClipState() throws StdQTException {
        short[] arrayOfShort = { 0 };
        int i = VDGetClipState(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0] != 0;
    }

    /**
     * Allows your application to determine the
     * capability and status information about a specified video digitizer component.
     * QuickTime::VDGetDigitizerInfo
     * @return if true clipping is enabled otherwise it isn't.
     * @return {@link quicktime.std.StdQTException}
     */
    public DigitizerInfo getDigitizerInfo() throws StdQTException {
        DigitizerInfo localDigitizerInfo = new DigitizerInfo();
        int i = VDGetDigitizerInfo(_ID(), localDigitizerInfo.getBytes());
        StdQTException.checkError(i);
        return localDigitizerInfo;
    }

    /**
     * Determines the format of the video signal provided by a specified video input source.
     * QuickTime::VDGetInputFormat
     * @param input The input video source for this request. Video digitizer components number video sources sequentially, starting at 0. So, to request
     *  information about the first video source, an application sets this parameter to 0. Applications can get the number of video sources supported by a
     *  video digitizer component by calling getNumberOfInputs ().
     * @return the video format of the specified input source.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getInputFormat(int paramInt) throws StdQTException {
        short[] arrayOfShort = { 0 };
        int i = VDGetInputFormat(_ID(), (short) paramInt, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Specifies the input signaling standard to digitize.
     * QuickTime::VDSetInputStandard
     * @param input the input signaling standard. (ntscIn, palIn, secamIn)
     * @param {@link quicktime.std.StdQTException}
     */
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
