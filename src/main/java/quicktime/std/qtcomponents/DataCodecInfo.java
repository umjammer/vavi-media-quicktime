package quicktime.std.qtcomponents;

/**
 * An object of this class is returned after a DataCodecCompressor.compress call
 * @see "quicktime.std.qtcomponents.DataCodecCompressor#compress()"
 */
public class DataCodecInfo {

    /**
     * Contains the size of the compressed data that was created, in bytes.
     */
    public final int actualDstSize;

    /**
     * Contains the number of bytes that should be added to the
     * decompression buffer size if decompression occurs in place.
     */
    public final int decompressSlop;

    DataCodecInfo(int actualDstSize, int decompressSlop) {
        this.actualDstSize = actualDstSize;
        this.decompressSlop = decompressSlop;
    }
}
