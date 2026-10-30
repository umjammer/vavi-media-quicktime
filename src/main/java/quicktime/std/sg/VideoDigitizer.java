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

    static VideoDigitizer makeVideoDigitizer(int nativeRep, Object ref) {
        return nativeRep == 0 ? null : new VideoDigitizer(nativeRep, ref);
    }

    private VideoDigitizer(int nativeRep, Object ref) {
        super(nativeRep, ref);
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
        short[] inputs = { 0 };
        int i = VDGetNumberOfInputs(_ID(), inputs);
        StdQTException.checkError(i);
        return inputs[0];
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
        short[] input = { 0 };
        int i = VDGetInput(_ID(), input);
        StdQTException.checkError(i);
        return input[0];
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
        short[] state = { 0 };
        int i = VDGetClipState(_ID(), state);
        StdQTException.checkError(i);
        return state[0] != 0;
    }

    /**
     * Allows your application to determine the
     * capability and status information about a specified video digitizer component.
     * @see "QuickTime::VDGetDigitizerInfo"
     * @return if true clipping is enabled otherwise it isn't.
     * @throws quicktime.std.StdQTException
     */
    public DigitizerInfo getDigitizerInfo() throws StdQTException {
        DigitizerInfo digitizerInfo = new DigitizerInfo();
        int i = VDGetDigitizerInfo(_ID(), digitizerInfo.getBytes());
        StdQTException.checkError(i);
        return digitizerInfo;
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
        short[] format = { 0 };
        int i = VDGetInputFormat(_ID(), (short) input, format);
        StdQTException.checkError(i);
        return format[0];
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

    private static int VDGetNumberOfInputs(int vd, short[] inputs) {
        return SequenceGrabberManager.VDGetNumberOfInputs(vd, inputs);
    }

    private static int VDSetInput(int vd, short input) {
        return SequenceGrabberManager.VDSetInput(vd, input);
    }

    private static int VDGetInput(int vd, short[] input) {
        return SequenceGrabberManager.VDGetInput(vd, input);
    }

    private static int VDSetClipRgn(int vd, int rgn) {
        return SequenceGrabberManager.VDSetClipRgn(vd, rgn);
    }

    private static int VDClearClipRgn(int vd, int rgn) {
        return SequenceGrabberManager.VDClearClipRgn(vd, rgn);
    }

    private static int VDSetClipState(int vd, short state) {
        return SequenceGrabberManager.VDSetClipState(vd, state);
    }

    private static int VDGetClipState(int vd, short[] state) {
        return SequenceGrabberManager.VDGetClipState(vd, state);
    }

    private static int VDGetDigitizerInfo(int vd, byte[] info) {
        return SequenceGrabberManager.VDGetDigitizerInfo(vd, info);
    }

    private static int VDGetInputFormat(int vd, short input, short[] format) {
        return SequenceGrabberManager.VDGetInputFormat(vd, input, format);
    }

    private static int VDSetInputStandard(int vd, short standard) {
        return SequenceGrabberManager.VDSetInputStandard(vd, standard);
    }
}
