package quicktime.std.qtcomponents;

import quicktime.QTException;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;


/**
 * This class represents the standard compression dialog for sound.
 */
public final class SoundCompressionDialog extends CompressionDialog implements QuickTimeLib {

    /**
     * Opens the CompressionDialog component.
     */
    public SoundCompressionDialog() throws QTException {
        super(1936684398);
    }

    /**
     * Retrieves the current sample rate for a dialog.
     *
     * @return the sample rate
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SCGetInfo()"
     */
    public float getInfoSampleRate() throws StdQTException {
        int[] info = {0};
        StdQTException.checkError(SCGetInfo(_ID(), 1936945780, info));
        return QTUtils.Fix2X(info[0]);
    }

    /**
     * Sets the current sample rate for a dialog.
     *
     * @param rate the new sample rate
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SCSetInfo()"
     */
    public void setInfoSampleRate(float rate) throws StdQTException {
        int[] info = {QTUtils.X2Fix(rate)};
        StdQTException.checkError(SCSetInfo(_ID(), 1936945780, info));
    }

    /**
     * Retrieves the current sample size for a dialog.
     *
     * @return the sample size
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SCGetInfo()"
     */
    public int getInfoSampleSize() throws StdQTException {
        short[] info = {0};
        StdQTException.checkError(SCGetInfo(_ID(), 1936946035, info));
        return info[0];
    }

    /**
     * Sets the current sample size for a dialog.
     *
     * @param size the new sample size
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SCSetInfo()"
     */
    public void setInfoSampleSize(int size) throws StdQTException {
        short[] info = {(short) size};
        StdQTException.checkError(SCSetInfo(_ID(), 1936946035, info));
    }

    /**
     * Retrieves the current channel count for a dialog.
     *
     * @return the channel count
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SCGetInfo()"
     */
    public int getInfoChannelCount() throws StdQTException {
        short[] info = {0};
        StdQTException.checkError(SCGetInfo(_ID(), 1936941923, info));
        return info[0];
    }

    /**
     * Sets the current channel count for a dialog.
     *
     * @param n the new channel count
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SCSetInfo()"
     */
    public void setInfoChannelCount(int n) throws StdQTException {
        short[] info = {(short) n};
        StdQTException.checkError(SCSetInfo(_ID(), 1936941923, info));
    }

    /**
     * Sets the current compression type for a dialog.
     *
     * @param compType the new compression type
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SCSetInfo()"
     */
    public void setInfoCompression(int compType) throws StdQTException {
        int[] info = {compType};
        StdQTException.checkError(SCSetInfo(_ID(), 1936941940, info));
    }

    /**
     * Retrieves the current compression type for a dialog.
     *
     * @return the compression
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SCGetInfo()"
     */
    public int getInfoCompression() throws StdQTException {
        int[] info = {0};
        StdQTException.checkError(SCGetInfo(_ID(), 1936941940, info));
        return info[0];
    }

    /**
     * Sets the current list of compression types for a dialog.
     *
     * @param compTypes the new compression types, if null then show all available compression options.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SCSetInfo()"
     */
    public void setInfoCompressionList(int[] compTypes) throws StdQTException {
        if (compTypes == null) {
            StdQTException.checkError(SCSetInfo(_ID(), 1668577644, (int[]) null));
        } else {
            int i = NewHandle(compTypes.length * 4);
            copyArrayToPointer(compTypes, 0, getIntFromPointer(i, 0), 0, compTypes.length * 4);
            int j = SCSetInfo(_ID(), 1668577644, i);
            DisposeHandle(i);
            StdQTException.checkError(j);
        }
    }

    /**
     * Retrieves the current compression type list for a dialog.
     *
     * @return the compression list
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SCGetInfo()"
     */
    public int[] getInfoCompressionList() throws StdQTException {
        int i = NewHandle(4);
        int[] info = {i};
        int j = SCGetInfo(_ID(), 1668577644, info);
        int k = GetHandleSize(i);
        int[] infoCompressionList = null;
        if (k > 3) {
            infoCompressionList = new int[k / 4];
            copyPointerToArray(getIntFromPointer(i, 0), 0, infoCompressionList, 0, k);
        }
        DisposeHandle(i);
        StdQTException.checkError(j);
        return infoCompressionList;
    }

    private static int SCGetInfo(int ci, int type, int[] info) {
        return ImageCompressionManager.SCGetInfo(ci, type, info);
    }

    private static int SCSetInfo(int ci, int type, int info) {
        return ImageCompressionManager.SCSetInfo(ci, type, info);
    }

    private static int SCSetInfo(int ci, int type, int[] info) {
        return ImageCompressionManager.SCSetInfo(ci, type, info);
    }

    private static int SCGetInfo(int ci, int type, short[] info) {
        return ImageCompressionManager.SCGetInfo(ci, type, info);
    }

    private static int SCSetInfo(int ci, int type, short[] info) {
        return ImageCompressionManager.SCSetInfo(ci, type, info);
    }

    private static int SCGetInfo(int ci, int type, byte[] info) {
        return ImageCompressionManager.SCGetInfo(ci, type, info);
    }

    private static int SCSetInfo(int ci, int type, byte[] info) {
        return ImageCompressionManager.SCSetInfo(ci, type, info);
    }

    private static void copyArrayToPointer(int[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        MemoryManager.copyArrayToPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    private static void DisposeHandle(int h) {
        MemoryManager.DisposeHandle(h);
    }

    private static int NewHandle(int size) {
        return MemoryManager.NewHandle(size);
    }

    private static int GetHandleSize(int h) {
        return MemoryManager.GetHandleSize(h);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, int[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }
}
