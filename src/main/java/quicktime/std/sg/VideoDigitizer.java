package quicktime.std.sg;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.SequenceGrabberManager;
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

    static VideoDigitizer makeVideoDigitizer(int paramInt, Object paramObject) {
        return paramInt == 0 ? null : new VideoDigitizer(paramInt, paramObject);
    }

    private VideoDigitizer(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * Opens the specified video component,'vidg', with any subType.
     * SGPanels should not be accessable by the public.
     * @see "QuickTime::OpenADefaultComponent"
     */
    public VideoDigitizer() throws QTException {
        this(0);
    }

    /**
     * Opens the specified video component,'vidg', with the given subType.
     * SGPanels should not be accessable by the public.
     * @see "QuickTime::OpenADefaultComponent"
     * @param subType The video component sub type
     */
    public VideoDigitizer(int subType) throws QTException {
        super(1986618471, subType);
    }

    /**
     * Returns the number of input sources that a video digitizer component supports.
     * @see "QuickTime::VDGetNumberOfInputs"
     * @return the number of inputs
     * @throws quicktime.std.StdQTException
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
     * @see "QuickTime::VDSetInput"
     * @param input the input video source.
     * @throws quicktime.std.StdQTException
     */
    public void setInput(int input) throws StdQTException {
        int i = VDSetInput(_ID(), (short) input);
        StdQTException.checkError(i);
    }

    /**
     * Returns the index that identifies the currently active input video source.
     * @see "QuickTime::VDGetInput"
     * @return the index for the current input device
     * @throws quicktime.std.StdQTException
     */
    public int getInput() throws StdQTException {
        short[] arrayOfShort = { 0 };
        int i = VDGetInput(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Allows you to define a clipping region.
     * @see "QuickTime::VDSetClipRgn"
     * @param clipRegion specifies the clip region
     * @throws quicktime.std.StdQTException
     */
    public void setClipRgn(Region clipRegion) throws StdQTException {
        int i = VDSetClipRgn(_ID(), QTObject.ID(clipRegion));
        StdQTException.checkError(i);
    }

    /**
     * Allows you to disable all or part of a clipping region that was previously set.
     * @see "QuickTime::VDClearClipRgn"
     * @param clipRegion specifies the clipping region to clear. This must correspond to all
     *  or part of a previously set clip region.
     * @throws quicktime.std.StdQTException
     */
    public void clearClipRgn(Region clipRegion) throws StdQTException {
        int i = VDClearClipRgn(_ID(), QTObject.ID(clipRegion));
        StdQTException.checkError(i);
    }

    /**
     * Allows your application to control whether clipping is enabled or disabled.
     * @see "QuickTime::VDSetClipState"
     * @param clipEnable if true clipping is enabled, otherwise it is disabled.
     * @throws quicktime.std.StdQTException
     */
    public void setClipState(boolean clipEnable) throws StdQTException {
        int i = VDSetClipState(_ID(), (short) (clipEnable ? 1 : 0));
        StdQTException.checkError(i);
    }

    /**
     * Allows your application to determine if clipping is enabled or not.
     * @see "QuickTime::VDGetClipState"
     * @return if true clipping is enabled otherwise it isn't.
     * @throws quicktime.std.StdQTException
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
     * @see "QuickTime::VDGetDigitizerInfo"
     * @return if true clipping is enabled otherwise it isn't.
     * @throws quicktime.std.StdQTException
     */
    public DigitizerInfo getDigitizerInfo() throws StdQTException {
        DigitizerInfo localDigitizerInfo = new DigitizerInfo();
        int i = VDGetDigitizerInfo(_ID(), localDigitizerInfo.getBytes());
        StdQTException.checkError(i);
        return localDigitizerInfo;
    }

    /**
     * Determines the format of the video signal provided by a specified video input source.
     * @see "QuickTime::VDGetInputFormat"
     * @param input The input video source for this request. Video digitizer components number video sources sequentially, starting at 0. So, to request
     *  information about the first video source, an application sets this parameter to 0. Applications can get the number of video sources supported by a
     *  video digitizer component by calling getNumberOfInputs ().
     * @return the video format of the specified input source.
     * @throws quicktime.std.StdQTException
     */
    public int getInputFormat(int input) throws StdQTException {
        short[] arrayOfShort = { 0 };
        int i = VDGetInputFormat(_ID(), (short) input, arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Specifies the input signaling standard to digitize.
     * @see "QuickTime::VDSetInputStandard"
     * @param input the input signaling standard. (ntscIn, palIn, secamIn)
     * @throws quicktime.std.StdQTException
     */
    public void setInputStandard(int input) throws StdQTException {
        int i = VDSetInputStandard(_ID(), (short) input);
        StdQTException.checkError(i);
    }

    private static int VDGetNumberOfInputs(int paramInt, short[] paramArrayOfShort) {
        return SequenceGrabberManager.VDGetNumberOfInputs(paramInt, paramArrayOfShort);
    }

    private static int VDSetInput(int paramInt, short paramShort) {
        return SequenceGrabberManager.VDSetInput(paramInt, paramShort);
    }

    private static int VDGetInput(int paramInt, short[] paramArrayOfShort) {
        return SequenceGrabberManager.VDGetInput(paramInt, paramArrayOfShort);
    }

    private static int VDSetClipRgn(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.VDSetClipRgn(paramInt1, paramInt2);
    }

    private static int VDClearClipRgn(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.VDClearClipRgn(paramInt1, paramInt2);
    }

    private static int VDSetClipState(int paramInt, short paramShort) {
        return SequenceGrabberManager.VDSetClipState(paramInt, paramShort);
    }

    private static int VDGetClipState(int paramInt, short[] paramArrayOfShort) {
        return SequenceGrabberManager.VDGetClipState(paramInt, paramArrayOfShort);
    }

    private static int VDGetDigitizerInfo(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.VDGetDigitizerInfo(paramInt, paramArrayOfByte);
    }

    private static int VDGetInputFormat(int paramInt, short paramShort, short[] paramArrayOfShort) {
        return SequenceGrabberManager.VDGetInputFormat(paramInt, paramShort, paramArrayOfShort);
    }

    private static int VDSetInputStandard(int paramInt, short paramShort) {
        return SequenceGrabberManager.VDSetInputStandard(paramInt, paramShort);
    }
}

