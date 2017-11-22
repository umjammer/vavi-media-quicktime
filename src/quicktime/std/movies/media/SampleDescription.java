
package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.qtcomponents.MovieImporter;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;


public class SampleDescription extends QTHandleRef implements QuickTimeLib, Cloneable {
    private static Object linkage;

    public static final int kNativeSize = 16;

    private static EndianDescriptor ed;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SampleDescription.class);
                    }
                });
            }
        }.establish();
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = new EndianDescriptor(0);

        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(0, 4, 3);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);

        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(12, 2, 2);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec2);
        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    public static SampleDescription fromMovieImporter(MovieImporter paramMovieImporter) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = MovieImportGetSampleDescription(QTObject.ID(paramMovieImporter), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return new SampleDescription(arrayOfInt1[0], paramMovieImporter, false);
    }

    public SampleDescription(int paramInt) throws QTException {
        this(16, true, paramInt);
    }

    protected SampleDescription(int paramInt, Object paramObject, boolean paramBoolean) {
        super(paramInt, paramObject, paramBoolean);
    }

    protected SampleDescription(int paramInt1, boolean paramBoolean, int paramInt2) throws QTException {
        super(paramInt1, true);
        setIntAt(0, paramInt1);
        setDataFormat(paramInt2);
    }

    public int getSize() {
        return getIntAt(0);
    }

    public void setSize(int paramInt) throws StdQTException {
        boolean bool = isLocked();
        unlock();
        SetHandleSize(_ID(), paramInt);
        StdQTException.checkError(MemError());
        setIntAt(0, paramInt);
        if (bool) {
            lock();
        }
    }

    public int getDataFormat() {
        return getIntAt(4);
    }

    public void setDataFormat(int paramInt) {
        setIntAt(4, paramInt);
    }

    public int getDataRefIndex() {
        return getShortAt(14);
    }

    public void setDataRefIndex(int paramInt) {
        setShortAt(14, (short) paramInt);
    }

    public Object clone() {
        return new SampleDescription(makeAndCopyHandle(), null, false);
    }

    public String toString() {
        return getClass().getName() + "[format=" + QTUtils.fromOSType(getDataFormat()) + "]";
    }

    private static native int MovieImportGetSampleDescription(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void SetHandleSize(int paramInt1, int paramInt2);

    private static native short MemError();
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SampleDescription.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
