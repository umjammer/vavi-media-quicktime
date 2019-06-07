package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTPointerRef;

/**
 * CodecNameList corresponds to CodecNameSpecListPtr data structure
 * which contains a list of compressor name structures.
 * refer to CodecNameSpecList in QuickTime
 */
public final class CodecNameList extends QTPointerRef implements QuickTimeLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 6;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(CodecNameList.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Retrieve a list of installed compressor components or a
     * list of installed compressor types.
     * see QuickTime::GetCodecNameList
     * @param show specifies an integer that controls the contents of the list.
     */
    public CodecNameList(int paramInt) throws QTException {
        super(allocate((short) paramInt), kNativeSize, null);
    }

    private static int allocate(short paramShort) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = GetCodecNameList(arrayOfInt, paramShort);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Retrieves the nth element in the codec name list.
     * @param n the index to the list.
     * @param {@link quicktime.std.StdQTException}
     */
    public CodecName getNth(int paramInt) throws StdQTException {
        if (paramInt > getCount()) {
            throw new StdQTException("CodecName.getNth");
        }
        return new CodecName(_ID() + 2 + 44 * paramInt, this);
    }

    /**
     * Retrieves the count field.
     * @return the number of codec names.
     */
    public int getCount() {
        return getShortAt(0);
    }

    private static native short GetCodecNameList(int[] paramArrayOfInt, short paramShort);
}
