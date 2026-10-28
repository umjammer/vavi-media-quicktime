package quicktime.qd;

import quicktime.QTObject;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;


/**
 * Refer to GDevice in QuickDraw
 */
public final class GDevice extends QTHandleRef implements QuickTimeLib {

    private static final int singleDevicesBit = 0;

    private static final int dontMatchSeedsBit = 1;

    private static final int allDevicesBit = 2;

    /**
     * Returns the current device of the sequence grabber.
     * @see "QuickTime::SGGetGWorld"
     * @param sg the SequenceGrabber object
     * @return The current device of the sequence grabber.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.sg.SequenceGrabber#getGWorldDevice()
     */
    public static GDevice fromSequenceGrabber(SequenceGrabber sg) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = SGGetGWorld(QTObject.ID(sg), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return new GDevice(arrayOfInt2[0]);
    }

    private GDevice(int paramInt) {
        super(paramInt, new Object(), false);
    }

    /**
     * Returns the current graphic device.
     * @see "QuickDraw::GetGDevice"
     * @return a graphics device
     */
    public static GDevice get() {
        int i = GetGDevice();
        return new GDevice(i);
    }

    /**
     * Returns the first graphic device in the device list.
     * @see "QuickDraw::GetDeviceList"
     * @return a graphics device
     */
    public static GDevice getList() {
        int i = GetDeviceList();
        return new GDevice(i);
    }

    /**
     * Returns the graphic device for the main screen.
     * @see "QuickDraw::GetMainDevice"
     * @return a graphics device
     */
    public static GDevice getMain() {
        int i = GetMainDevice();
        return new GDevice(i);
    }

    /**
     * Returns the graphic device with the greatest pixel depth.
     * @see "QuickDraw::GetMaxDevice"
     * @param globalRect a rectangle in global coordinates that
     *   intersects the devices you are searching
     * @return a graphics device
     */
    public static GDevice getMax(QDRect globalRect) {
        int i = GetMaxDevice(globalRect.getRect());
        return new GDevice(i);
    }

    /**
     * Initialises the instance of the Graphics Device. If the device has a driver
     * then the attributes will be set by this call, if not the attributes should be
     * set with setAttributes as this call will do nothing. You should never need to
     * call this method directly.
     * @see "QuickDraw::InitGDevice"
     * @param mode the device configuration mode
     */
    public void init(int paramInt1, int mode) {
        InitGDevice((short) paramInt1, mode, _ID());
    }

    /**
     * Set the attributes of a graphics device.
     * @see "QuickDraw::SetDeviceAttribute"
     * @param attribute use the constants
     * @param value if true attribute is turned on
     */
    public void setAttribute(int attribute, boolean value) {
        SetDeviceAttribute(_ID(), (short) attribute, (byte) (value ? 1 : 0));
    }

    /**
     * Sets this GDevice as the current device.
     * @see "QuickDraw::SetGDevice"
     */
    public void set() {
        SetGDevice(_ID());
    }

    /**
     * Returns the graphic device that is the next device from this in the device list.
     * @see "QuickDraw::GetNextDevice"
     * @return a graphics device
     */
    public GDevice getNext() {
        int i = GetNextDevice(_ID());
        return i == 0 ? null : new GDevice(i);
    }

    /**
     * Determines whether the flag bit for an attribute has been set in the flags field
     * of the device.
     * @see "QuickDraw::TestDeviceAttribute"
     * @param attribute the attribute to test for
     * @return true if the attribute is set otherwise false.
     */
    public boolean testAttribute(int attribute) {
        int i = TestDeviceAttribute(_ID(), (short) attribute);
        return i != 0;
    }

    /**
     * Returns the PixMap associated with this gDevice
     * @return a PixMap for the GDevice
     */
    public PixMap getPixMap() {
        return PixMap.fromGDevice(this);
    }

    /**
     * Returns the global Bounds associated with this gDevice
     * @return a QDRect
     */
    public QDRect getBounds() {
        byte[] arrayOfByte = new byte[8];
        getBytesAt(34, 8, arrayOfByte, 0);
        return QDRect.fromArray(arrayOfByte, 8);
    }

    /**
     * Returns the closest possible scaling that a particular screen
     * device can be set to in a given pixel depth
     * @see "QuickDraw::GDHasScale"
     * @param depth specifies the pixel depth of the screen device
     * @param scale the desired scale value
     * @return the closest scale available for the given depth
     * @throws quicktime.std.StdQTException
     */
    public float hasScale(int depth, float scale) throws StdQTException {
        int[] arrayOfInt = { QTUtils.X2Fix(scale) };
        StdQTException.checkError(GDHasScale(_ID(), (short) depth, arrayOfInt));
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    /**
     * Returns the current scale of the screen graphics device
     * @see "QuickDraw::GDGetScale"
     * @return the scale of the current device
     * @throws quicktime.std.StdQTException
     */
    public float getScale() throws StdQTException {
        int[] arrayOfInt = { 0 };
        short[] arrayOfShort = { 0 };
        StdQTException.checkError(GDGetScale(_ID(), arrayOfInt, arrayOfShort));
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    /**
     * Returns the current scale of the screen graphics device
     * @see "QuickDraw::GDGetScale"
     * @return the flags of the current device
     * @throws quicktime.std.StdQTException
     */
    public int getFlags() throws StdQTException {
        int[] arrayOfInt = { 0 };
        short[] arrayOfShort = { 0 };
        StdQTException.checkError(GDGetScale(_ID(), arrayOfInt, arrayOfShort));
        return arrayOfShort[0];
    }

    /**
     * Sets the current scale of the screen graphics device
     * @see "QuickDraw::GDSetScale"
     * @param scale the desired scale value
     * @throws quicktime.std.StdQTException
     */
    public void setScale(int scale, int flags) throws StdQTException {
        StdQTException.checkError(GDSetScale(_ID(), QTUtils.X2Fix(scale), (short) flags));
    }

    private static int SGGetGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return SequenceGrabberManager.SGGetGWorld(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int GetGDevice() {
        return QuickDrawManager.GetGDevice();
    }

    private static int GetDeviceList() {
        return QuickDrawManager.GetDeviceList();
    }

    private static int GetMainDevice() {
        return QuickDrawManager.GetMainDevice();
    }

    private static int GetMaxDevice(byte[] paramArrayOfByte) {
        return QuickDrawManager.GetMaxDevice(paramArrayOfByte);
    }

    private static void InitGDevice(short paramShort, int mode, int paramInt2) {
        QuickDrawManager.InitGDevice(paramShort, mode, paramInt2);
    }

    private static void SetDeviceAttribute(int paramInt, short paramShort, byte paramByte) {
        QuickDrawManager.SetDeviceAttribute(paramInt, paramShort, paramByte);
    }

    private static void SetGDevice(int paramInt) {
        QuickDrawManager.SetGDevice(paramInt);
    }

    private static int GetNextDevice(int paramInt) {
        return QuickDrawManager.GetNextDevice(paramInt);
    }

    private static int TestDeviceAttribute(int paramInt, short paramShort) {
        return QuickDrawManager.TestDeviceAttribute(paramInt, paramShort);
    }

    private static short GDHasScale(int paramInt, short paramShort, int[] paramArrayOfInt) {
        return QuickDrawManager.GDHasScale(paramInt, paramShort, paramArrayOfInt);
    }

    private static short GDGetScale(int paramInt, int[] paramArrayOfInt, short[] paramArrayOfShort) {
        return QuickDrawManager.GDGetScale(paramInt, paramArrayOfInt, paramArrayOfShort);
    }

    private static short GDSetScale(int gd, int scale, short flags) {
        return QuickDrawManager.GDSetScale(gd, scale, flags);
    }
}
