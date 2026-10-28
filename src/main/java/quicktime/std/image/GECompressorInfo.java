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
    public GECompressorInfo(boolean canUseCompressor, AtomContainer atomContainer) {
        this.canUseCompressor = canUseCompressor;
        this.atomContainer = atomContainer;
    }
}
