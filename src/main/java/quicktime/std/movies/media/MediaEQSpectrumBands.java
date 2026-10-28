package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

/**
 * This represents the Equalizer Spectrum of Bands. It contains the
 * count of the bands and the frequencies of each band. To retrieve
 * these values you need to pass in the expected count of bands. The
 * value returned would be the actual count of the bands and the count
 * frequencies array which you passed in.
 */
public final class MediaEQSpectrumBands extends QTByteObject implements PrimitivesLib, InterfaceLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(MediaEQSpectrumBands.class));
            }
        }.establish();
    }

    /**
     * @param count the count of bands
     */
    public MediaEQSpectrumBands(int count) {
        super(6);
        setShortAt(0, (short) count);
        setIntAt(2, NewPtrClear(count * 4));
    }

    /**
     * @return the count of bands
     */
    public short getCount() {
        return getShortAt(0);
    }

    /**
     * @param whichBand the band to query.
     * @return the frequency at specified band
     */
    public float getFrequency(int whichBand) {
        int i = getIntAt(2);
        return QTUtils.UFix2X(getIntFromPointer(i, whichBand * 4));
    }

    /**
     * @param whichBand the band to set.
     * @param freq the frequency at specified band
     */
    public void setFrequency(int whichBand, float freq) {
        int i = getIntAt(2);
        setIntInPointer(i, whichBand * 4, QTUtils.X2UFix(freq));
    }

    /**
     * Returns a string representation of this object
     * @return a String represenation of this object
     */
    public String toString() {
        StringBuilder str = new StringBuilder(getClass().getName() + "[count=" + getCount());
        if (getCount() > 0) {
            str.append(",frequencies=[");
            for (int i = 0; i < getCount() - 1; i++) str.append(getFrequency(i)).append(",");
            str.append(getFrequency(getCount() - 1));
            str.append("]]");
        } else {
            str.append("]");
        }
        return str.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        DisposePtr(getIntAt(2));
        super.finalize();
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static void setIntInPointer(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.setIntInPointer(paramInt1, paramInt2, paramInt3);
    }

    private static int NewPtrClear(int paramInt) {
        return MemoryManager.NewPtrClear(paramInt);
    }

    private static void DisposePtr(int paramInt) {
        MemoryManager.DisposePtr(paramInt);
    }
}
