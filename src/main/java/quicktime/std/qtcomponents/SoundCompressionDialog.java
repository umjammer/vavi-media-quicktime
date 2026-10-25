package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;

/**
 * This class represents the standard compression dialog for sound.
 */
public final class SoundCompressionDialog extends CompressionDialog implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SoundCompressionDialog.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Opens the CompressionDialog component.
     */
    public SoundCompressionDialog() throws QTException {
        super(1936684398);
    }

    /**
     * Retrieves the current sample rate for a dialog.
     * QuickTime::SCGetInfo()
     * @return the sample rate
     * @return {@link quicktime.std.StdQTException}
     */
    public float getInfoSampleRate() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1936945780, arrayOfInt));
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    /**
     * Sets the current sample rate for a dialog.
     * QuickTime::SCSetInfo()
     * @param rate the new sample rate
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInfoSampleRate(float paramFloat) throws StdQTException {
        int[] arrayOfInt = { QTUtils.X2Fix(paramFloat) };
        StdQTException.checkError(SCSetInfo(_ID(), 1936945780, arrayOfInt));
    }

    /**
     * Retrieves the current sample size for a dialog.
     * QuickTime::SCGetInfo()
     * @return the sample size
     * @return {@link quicktime.std.StdQTException}
     */
    public int getInfoSampleSize() throws StdQTException {
        short[] arrayOfShort = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1936946035, arrayOfShort));
        return arrayOfShort[0];
    }

    /**
     * Sets the current sample size for a dialog.
     * QuickTime::SCSetInfo()
     * @param size the new sample size
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInfoSampleSize(int paramInt) throws StdQTException {
        short[] arrayOfShort = { (short) paramInt };
        StdQTException.checkError(SCSetInfo(_ID(), 1936946035, arrayOfShort));
    }

    /**
     * Retrieves the current channel count for a dialog.
     * QuickTime::SCGetInfo()
     * @return the channel count
     * @return {@link quicktime.std.StdQTException}
     */
    public int getInfoChannelCount() throws StdQTException {
        short[] arrayOfShort = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1936941923, arrayOfShort));
        return arrayOfShort[0];
    }

    /**
     * Sets the current channel count for a dialog.
     * QuickTime::SCSetInfo()
     * @param n the new channel count
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInfoChannelCount(int paramInt) throws StdQTException {
        short[] arrayOfShort = { (short) paramInt };
        StdQTException.checkError(SCSetInfo(_ID(), 1936941923, arrayOfShort));
    }

    /**
     * Sets the current compression type for a dialog.
     * QuickTime::SCSetInfo()
     * @param compType the new compression type
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInfoCompression(int paramInt) throws StdQTException {
        int[] arrayOfInt = { paramInt };
        StdQTException.checkError(SCSetInfo(_ID(), 1936941940, arrayOfInt));
    }

    /**
     * Retrieves the current compression type for a dialog.
     * QuickTime::SCGetInfo()
     * @return the compression
     * @return {@link quicktime.std.StdQTException}
     */
    public int getInfoCompression() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1936941940, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Sets the current list of compression types for a dialog.
     * QuickTime::SCSetInfo()
     * @param compTypes the new compression types, if null then show all available compression options.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInfoCompressionList(int[] paramArrayOfInt) throws StdQTException {
        if (paramArrayOfInt == null) {
            StdQTException.checkError(SCSetInfo(_ID(), 1668577644, (int[]) null));
        } else {
            int i = NewHandle(paramArrayOfInt.length * 4);
            copyArrayToPointer(paramArrayOfInt, 0, getIntFromPointer(i, 0), 0, paramArrayOfInt.length * 4);
            int j = SCSetInfo(_ID(), 1668577644, i);
            DisposeHandle(i);
            StdQTException.checkError(j);
        }
    }

    /**
     * Retrieves the current compression type list for a dialog.
     * QuickTime::SCGetInfo()
     * @return the compression list
     * @return {@link quicktime.std.StdQTException}
     */
    public int[] getInfoCompressionList() throws StdQTException {
        int i = NewHandle(4);
        int[] arrayOfInt1 = { i };
        int j = SCGetInfo(_ID(), 1668577644, arrayOfInt1);
        int k = GetHandleSize(i);
        int[] arrayOfInt2 = null;
        if (k > 3) {
            arrayOfInt2 = new int[k / 4];
            copyPointerToArray(getIntFromPointer(i, 0), 0, arrayOfInt2, 0, k);
        }
        DisposeHandle(i);
        StdQTException.checkError(j);
        return arrayOfInt2;
    }

    private static int SCGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ImageCompressionManager.SCGetInfo(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int SCSetInfo(int paramInt1, int paramInt2, int paramInt3) {
        return ImageCompressionManager.SCSetInfo(paramInt1, paramInt2, paramInt3);
    }

    private static int SCSetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ImageCompressionManager.SCSetInfo(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int SCGetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        return ImageCompressionManager.SCGetInfo(paramInt1, paramInt2, paramArrayOfShort);
    }

    private static int SCSetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        return ImageCompressionManager.SCSetInfo(paramInt1, paramInt2, paramArrayOfShort);
    }

    private static int SCGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return ImageCompressionManager.SCGetInfo(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int SCSetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return ImageCompressionManager.SCSetInfo(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static void copyArrayToPointer(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void DisposeHandle(int paramInt) {
        MemoryManager.DisposeHandle(paramInt);
    }

    private static int NewHandle(int paramInt) {
        return MemoryManager.NewHandle(paramInt);
    }

    private static int GetHandleSize(int paramInt) {
        return MemoryManager.GetHandleSize(paramInt);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }
}

