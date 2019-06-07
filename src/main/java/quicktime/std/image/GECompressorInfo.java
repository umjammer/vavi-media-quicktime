package quicktime.std.image;

import quicktime.std.movies.AtomContainer;

public class GECompressorInfo {

    /**
     * canUseCompressor true if Graphics Exporter uses compressor
     */
    public boolean canUseCompressor;

    /**
     * atomContainer contains information related to the compression operation
     */
    public AtomContainer atomContainer;

    /**
     * This constructor creates a GECompressorInfo object.
     * @param canUseCompressor true if Graphics Exporter uses compressor
     * @param atomContainer contains information related to the compression operation
     */
    public GECompressorInfo(boolean paramBoolean, AtomContainer paramAtomContainer) {
        paramBoolean = paramBoolean;
        paramAtomContainer = paramAtomContainer;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /GECompressorInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
