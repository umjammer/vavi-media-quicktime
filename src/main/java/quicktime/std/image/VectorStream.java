
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.qd.QDColor;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.AtomData;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTHandle;


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
        int[] arrayOfInt = {
            0
        };
        int i = CurveCreateVectorStream(QTObject.ID(paramCurve), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public VectorStream(Curve paramCurve) throws QTException {
        super(allocate(paramCurve), null, false);
        this.c = paramCurve;
    }

    public void addPathAtom(QTHandle paramQTHandle) throws QTException {
        int i = CurveAddPathAtomToVectorStream(QTObject.ID(this.c), QTObject.ID(paramQTHandle), _ID());
        StdQTException.checkError(i);
    }

    public void addZeroAtom() throws StdQTException {
        int i = CurveAddZeroAtomToVectorStream(QTObject.ID(this.c), _ID());
        StdQTException.checkError(i);
    }

    public void addAtom(int paramInt, byte[] paramArrayOfByte) throws StdQTException {
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), paramInt, paramArrayOfByte.length, paramArrayOfByte, _ID());
        StdQTException.checkError(i);
    }

    public void addAtom(QDColor paramQDColor) throws QTException {
        EndianDescriptor localEndianDescriptor = QDColor.getEndianDescriptorARGBColor();
        EndianOrder.flipNativeToBigEndian(paramQDColor, 0, localEndianDescriptor);
        byte[] arrayOfByte = paramQDColor.getARGBColor();

        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), 1634887522, arrayOfByte.length, arrayOfByte, _ID());
        StdQTException.checkError(i);
    }

    public void addAtom(int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = {
            EndianOrder.flipNativeToBigEndian32(paramInt2)
        };
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), paramInt1, 4, arrayOfInt, _ID());
        StdQTException.checkError(i);
    }

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

    public void removeAtom(int paramInt) throws StdQTException {
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), paramInt, 0, 0, _ID());
        StdQTException.checkError(i);
    }

    public AtomData getAtomData(int paramInt) throws QTException {
        return AtomData.fromVectorStream(this.c, this, paramInt);
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native int CurveCreateVectorStream(int paramInt, int[] paramArrayOfInt);

    private static native int CurveAddAtomToVectorStream(int paramInt1,
                                                         int paramInt2,
                                                         int paramInt3,
                                                         int paramInt4,
                                                         int paramInt5);

    private static native int CurveAddAtomToVectorStream(int paramInt1,
                                                         int paramInt2,
                                                         int paramInt3,
                                                         int[] paramArrayOfInt,
                                                         int paramInt4);

    private static native int CurveAddAtomToVectorStream(int paramInt1,
                                                         int paramInt2,
                                                         int paramInt3,
                                                         byte[] paramArrayOfByte,
                                                         int paramInt4);

    private static native int CurveAddPathAtomToVectorStream(int paramInt1, int paramInt2, int paramInt3);

    private static native int CurveAddZeroAtomToVectorStream(int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /VectorStream.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
