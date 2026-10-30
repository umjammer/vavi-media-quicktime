package quicktime.std.image;

import quicktime.QTException;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTPointerRef;


/**
 * CodecNameList corresponds to CodecNameSpecListPtr data structure
 * which contains a list of compressor name structures.
 * refer to CodecNameSpecList in QuickTime
 */
public final class CodecNameList extends QTPointerRef implements QuickTimeLib {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 6;

    /**
     * Retrieve a list of installed compressor components or a
     * list of installed compressor types.
     * @see "see QuickTime::GetCodecNameList"
     * @param show specifies an integer that controls the contents of the list.
     */
    public CodecNameList(int show) throws QTException {
        super(allocate((short) show), kNativeSize, null);
    }

    private static int allocate(short showAll) throws StdQTException {
        int[] list = { 0 };
        int i = GetCodecNameList(list, showAll);
        StdQTException.checkError(i);
        return list[0];
    }

    /**
     * Retrieves the nth element in the codec name list.
     * @param n the index to the list.
     * @throws quicktime.std.StdQTException
     */
    public CodecName getNth(int n) throws StdQTException {
        if (n > getCount()) {
            throw new StdQTException("CodecName.getNth");
        }
        return new CodecName(_ID() + 2 + 44 * n, this);
    }

    /**
     * Retrieves the count field.
     * @return the number of codec names.
     */
    public int getCount() {
        return getShortAt(0);
    }

    private static short GetCodecNameList(int[] list, short showAll) {
        return ImageCompressionManager.GetCodecNameList(list, showAll);
    }
}
