package quicktime.std.image;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
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

    private final Curve c;

    private static int allocate(Curve effect) throws QTException {
        int[] stream = { 0 };
        int i = CurveCreateVectorStream(QTObject.ID(effect), stream);
        StdQTException.checkError(i);
        return stream[0];
    }

    /**
     * Used to create an empty vector data stream.
     * @see "QuickTime::CurveCreateVectorStream"
     * @param c the curve component
     */
    public VectorStream(Curve c) throws QTException {
        super(allocate(c), null, false);
        this.c = c;
    }

    /**
     * Used  to add a path to a vector data stream.
     * @see "QuickTime::CurveAddPathAtomToVectorStream"
     * @param pathData Contains a handle to the data for the path.
     * @throws quicktime.QTException
     */
    public void addPathAtom(QTHandle pathData) throws QTException {
        int i = CurveAddPathAtomToVectorStream(QTObject.ID(this.c), QTObject.ID(pathData), _ID());
        StdQTException.checkError(i);
    }

    /**
     * Used  to add a kCurveEndAtom to a vector data stream. This atom marks the end of the vector data stream.
     * @see "QuickTime::CurveAddZeroAtomToVectorStream"
     */
    public void addZeroAtom() throws StdQTException {
        int i = CurveAddZeroAtomToVectorStream(QTObject.ID(this.c), _ID());
        StdQTException.checkError(i);
    }

    /**
     * Used  to add an atom to a vector data stream. When using this call make sure that the
     * byte array passed in has all its bytes flipped to BigEndian .
     * @see "QuickTime::CurveAddAtomToVectorStream"
     * @param atomType Specifies the type of atom to add to the vector data stream.
     * @param atomData the data for the atom.The EndianFlipping should be done by the application
     * @throws quicktime.std.StdQTException
     */
    public void addAtom(int atomType, byte[] atomData) throws StdQTException {
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), atomType, atomData.length, atomData, _ID());
        StdQTException.checkError(i);
    }

    /**
     * Used  to add an atom of type kCurveGradientRecordAtom to a vector data stream.
     * @see "QuickTime::CurveAddAtomToVectorStream"
     */
    public void addAtom(QDColor color) throws QTException {
        EndianDescriptor ed = QDColor.getEndianDescriptorARGBColor();
        EndianOrder.flipNativeToBigEndian(color, 0, ed);
        byte[] data = color.getARGBColor();
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), 1634887522, data.length, data, _ID());
        StdQTException.checkError(i);
    }

    /**
     * Used  to add an atom to a vector data stream.
     * @see "QuickTime::CurveAddAtomToVectorStream"
     * @param atomType Specifies the type of atom to add to the vector data stream.
     * @param atomData the data for the atom.
     * @throws quicktime.std.StdQTException
     */
    public void addAtom(int atomType, int atomData) throws StdQTException {
        int[] data = { EndianOrder.flipNativeToBigEndian32(atomData) };
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), atomType, 4, data, _ID());
        StdQTException.checkError(i);
    }

    /**
     * Used  to add an atom of type kCurvePathAtom to a vector data stream. By default this has one contour per path.
     * This is used to add points that are all on curve or off curve.
     * @see "QuickTime::CurveAddAtomToVectorStream"
     * @param onCurve if points are on curve set to true.
     * @param points Specifies countour points that are  to be added to the vector data stream.
     * @throws quicktime.std.StdQTException
     */
    public void addAtom(boolean onCurve, QDPoint[] points) throws StdQTException {
        int[] data = new int[points.length * 2 + 3];
        data[0] = 1;
        data[1] = points.length;
        data[2] = (onCurve ? 1 : 0);
        for (int i = 0; i < points.length; i++) {
            byte[] gxPoint = points[i].getGXPoint();
            int j = getIntFromArray(gxPoint, 0);
            int k = getIntFromArray(gxPoint, 4);
            data[(3 + 2 * i)] = j;
            data[(4 + 2 * i)] = k;
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = EndianOrder.flipNativeToBigEndian32(data[i]);
        }
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), 1885434984, data.length * 4, data, _ID());
        StdQTException.checkError(i);
    }

    public void addAtom(QDColor[] qdColors) throws QTException {
        byte[] data = new byte[qdColors.length * 12];
        for (int i = 0; i < qdColors.length; i++) {
            QDColor color = qdColors[i];
            EndianDescriptor ed = QDColor.getEndianDescriptorGradientColor();
            byte[] gradientColor = color.getGradientColor();
            EndianOrder.flipNativeToBigEndian(gradientColor, 0, ed);
            System.arraycopy(gradientColor, 0, data, 12 * i, gradientColor.length);
        }
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), 1735549284, data.length, data, _ID());
        StdQTException.checkError(i);
    }

    /**
     * Used  to remove and atom of given type ex. kCurveGradientRecordAtom can be disabled
     * by making this call so that all atoms added after this atom do not have
     * Gradient.
     * @see "QuickTime::CurveAddAtomToVectorStream"
     * @param atomType contains the type of atom to be removed .
     * @throws quicktime.std.StdQTException
     */
    public void removeAtom(int atomType) throws StdQTException {
        int i = CurveAddAtomToVectorStream(QTObject.ID(this.c), atomType, 0, 0, _ID());
        StdQTException.checkError(i);
    }

    /**
     * This method is used to find the first atom of a specified type within a vector data stream and get its data.
     * @see "QuickTime::CurveGetAtomDataFromVectorStream"
     * @param atomType Specifies the type of atom to find.
     * @return the atom's data.
     * @throws quicktime.QTException
     */
    public AtomData getAtomData(int atomType) throws QTException {
        return AtomData.fromVectorStream(this.c, this, atomType);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }

    private static int CurveCreateVectorStream(int effect, int[] stream) {
        return ImageCompressionManager.CurveCreateVectorStream(effect, stream);
    }

    private static int CurveAddAtomToVectorStream(int effect, int atomType, int dataSize, int data, int stream) {
        return ImageCompressionManager.CurveAddAtomToVectorStream(effect, atomType, dataSize, data, stream);
    }

    private static int CurveAddAtomToVectorStream(int effect, int atomType, int dataSize, int[] data, int stream) {
        return ImageCompressionManager.CurveAddAtomToVectorStream(effect, atomType, dataSize, data, stream);
    }

    private static int CurveAddAtomToVectorStream(int effect, int atomType, int dataSize, byte[] data, int stream) {
        return ImageCompressionManager.CurveAddAtomToVectorStream(effect, atomType, dataSize, data, stream);
    }

    private static int CurveAddPathAtomToVectorStream(int effect, int pathData, int stream) {
        return ImageCompressionManager.CurveAddPathAtomToVectorStream(effect, pathData, stream);
    }

    private static int CurveAddZeroAtomToVectorStream(int effect, int stream) {
        return ImageCompressionManager.CurveAddZeroAtomToVectorStream(effect, stream);
    }
}
