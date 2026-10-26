package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QTNative;
import quicktime.qd.QDColor;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.AtomData;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTHandle;

/**
 * This class represents a vector data stream for QuickTime Vector component. It
 * holds a stream of Atoms which constructs the vector to be drawn. This class
 * is used to add the data atoms which are either the paths or the attributes
 * of the path. A ZeroAtom at the end of the stream marks the end of datastream.
 * It should be noted that all the addAtom calls which need the data to be Endian flipped
 * is done internally by the class, unless explicitly mentioned.
 */
public class VectorStream extends AtomContainer implements quicktime.jdirect.QuickTimeLib, quicktime.jdirect.PrimitivesLib {

    private static Object linkage;

    private Curve c;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(VectorStream.class);
                    }
                });
            }
        }.establish();
    }

    private static int allocate(Curve paramCurve) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = CurveCreateVectorStream(QTObject.ID(paramCurve), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Used to create an empty vector data stream.
     * QuickTime::CurveCreateVectorStream
     * @param c the curve component
     */
    public VectorStream(Curve paramCurve) throws QTException {
        super(allocate(paramCurve), null, false);
        this.c = paramCurve;
    }

    /**
     * Used  to add a path to a vector data stream.
     * QuickTime::CurveAddPathAtomToVectorStream
     * @param pathData Contains a handle to the data for the path.
     * @param {@link quicktime.QTException}
     */
    public void addPathAtom(QTHandle paramQTHandle) throws QTException {
        int i = CurveAddPathAtomToVectorStream(QTObject.ID(this.c), QTObject.ID(paramQTHandle), _ID());
        StdQTException.checkError(i);
    }

    /**
     * Used  to add a kCurveEndAtom to a vector data stream. This atom marks the end of the vector data stream.
     * QuickTime::CurveAddZeroAtomToVectorStream
     */
    public void addZeroAtom() throws StdQTException {
        int i = CurveAddZeroAtomToVectorStream(QTObject.ID(this.c), _ID());
        StdQTException.checkError(i);
    }

    /**
     * Used  to add an atom to a vector data stream. When using this call make sure that the
     * byte array passed in has all its bytes flipped to BigEndian .
     * QuickTime::CurveAddAtomToVectorStream
     * @param atomType Specifies the type of atom to add to the vector data stream.
     * @param atomData the data for the atom.The EndianFlipping should be done by the application
     * @param {@link quicktime.std.StdQTException}
     */
    public void addAtom(int paramInt, byte[] paramArrayOfByte) throws StdQTException {
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), paramInt, paramArrayOfByte.length, paramArrayOfByte, _ID());
        StdQTException.checkError(i);
    }

    /**
     * Used  to add an atom of type kCurveGradientRecordAtom to a vector data stream.
     * QuickTime::CurveAddAtomToVectorStream
     */
    public void addAtom(QDColor paramQDColor) throws QTException {
        EndianDescriptor localEndianDescriptor = QDColor.getEndianDescriptorARGBColor();
        EndianOrder.flipNativeToBigEndian(paramQDColor, 0, localEndianDescriptor);
        byte[] arrayOfByte = paramQDColor.getARGBColor();
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), 1634887522, arrayOfByte.length, arrayOfByte, _ID());
        StdQTException.checkError(i);
    }

    /**
     * Used  to add an atom to a vector data stream.
     * QuickTime::CurveAddAtomToVectorStream
     * @param atomType Specifies the type of atom to add to the vector data stream.
     * @param atomData the data for the atom.
     * @param {@link quicktime.std.StdQTException}
     */
    public void addAtom(int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = { EndianOrder.flipNativeToBigEndian32(paramInt2) };
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), paramInt1, 4, arrayOfInt, _ID());
        StdQTException.checkError(i);
    }

    /**
     * Used  to add an atom of type kCurvePathAtom to a vector data stream. By default this has one contour per path.
     * This is used to add points that are all on curve or off curve.
     * QuickTime::CurveAddAtomToVectorStream
     * @param onCurve if points are on curve set to true.
     * @param points Specifies countour points that are  to be added to the vector data stream.
     * @param {@link quicktime.std.StdQTException}
     */
    public void addAtom(boolean paramBoolean, QDPoint[] paramArrayOfQDPoint) throws StdQTException {
        int[] arrayOfInt = new int[paramArrayOfQDPoint.length * 2 + 3];
        arrayOfInt[0] = 1;
        arrayOfInt[1] = paramArrayOfQDPoint.length;
        arrayOfInt[2] = (paramBoolean ? 1 : 0);
        for (int i = 0; i < paramArrayOfQDPoint.length; i++) {
            byte[] arrayOfByte = paramArrayOfQDPoint[i].getGXPoint();
            int j = getIntFromArray(arrayOfByte, 0);
            int k = getIntFromArray(arrayOfByte, 4);
            arrayOfInt[(3 + 2 * i)] = j;
            arrayOfInt[(4 + 2 * i)] = k;
        }
        for (int i = 0; i < arrayOfInt.length; i++) {
            arrayOfInt[i] = EndianOrder.flipNativeToBigEndian32(arrayOfInt[i]);
        }
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), 1885434984, arrayOfInt.length * 4, arrayOfInt, _ID());
        StdQTException.checkError(i);
    }

    public void addAtom(QDColor[] paramArrayOfQDColor) throws QTException {
        byte[] arrayOfByte1 = new byte[paramArrayOfQDColor.length * 12];
        for (int i = 0; i < paramArrayOfQDColor.length; i++) {
            QDColor localQDColor = paramArrayOfQDColor[i];
            EndianDescriptor localEndianDescriptor = QDColor.getEndianDescriptorGradientColor();
            byte[] arrayOfByte2 = localQDColor.getGradientColor();
            EndianOrder.flipNativeToBigEndian(arrayOfByte2, 0, localEndianDescriptor);
            System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 12 * i, arrayOfByte2.length);
        }
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), 1735549284, arrayOfByte1.length, arrayOfByte1, _ID());
        StdQTException.checkError(i);
    }

    /**
     * Used  to remove and atom of given type ex. kCurveGradientRecordAtom can be disabled
     * by making this call so that all atoms added after this atom do not have
     * Gradient.
     * QuickTime::CurveAddAtomToVectorStream
     * @param atomType contains the type of atom to be removed .
     * @param {@link quicktime.std.StdQTException}
     */
    public void removeAtom(int paramInt) throws StdQTException {
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), paramInt, 0, 0, _ID());
        StdQTException.checkError(i);
    }

    /**
     * This method is used to find the first atom of a specified type within a vector data stream and get its data.
     * QuickTime::CurveGetAtomDataFromVectorStream
     * @param atomType Specifies the type of atom to find.
     * @return the atom's data.
     * @return {@link quicktime.QTException}
     */
    public AtomData getAtomData(int paramInt) throws QTException {
        return AtomData.fromVectorStream(this.c, this, paramInt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }

    private static int CurveCreateVectorStream(int paramInt, int[] paramArrayOfInt) {
        return ImageCompressionManager.CurveCreateVectorStream(paramInt, paramArrayOfInt);
    }

    private static int CurveAddAtomToVectorStream(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return ImageCompressionManager.CurveAddAtomToVectorStream(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static int CurveAddAtomToVectorStream(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt, int paramInt4) {
        return ImageCompressionManager.CurveAddAtomToVectorStream(paramInt1, paramInt2, paramInt3, paramArrayOfInt, paramInt4);
    }

    private static int CurveAddAtomToVectorStream(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int paramInt4) {
        return ImageCompressionManager.CurveAddAtomToVectorStream(paramInt1, paramInt2, paramInt3, paramArrayOfByte, paramInt4);
    }

    private static int CurveAddPathAtomToVectorStream(int paramInt1, int paramInt2, int paramInt3) {
        return ImageCompressionManager.CurveAddPathAtomToVectorStream(paramInt1, paramInt2, paramInt3);
    }

    private static int CurveAddZeroAtomToVectorStream(int paramInt1, int paramInt2) {
        return ImageCompressionManager.CurveAddZeroAtomToVectorStream(paramInt1, paramInt2);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /VectorStream.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
