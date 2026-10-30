package quicktime.streaming;

import java.util.Arrays;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.qd.GDevice;
import quicktime.qd.QDColor;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.Region;
import quicktime.std.image.Matrix;
import quicktime.util.QTPointer;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;

/**
 * This class provides a collection of parameters that specify the media contents of a presentation object
 */
public class MediaParams extends QTPointerRef {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 80;

    /**
     * Constructs the class with a reasonable default values:
     * Width and Height are set to 0
     * Matrix is set to the Identity Matrix
     * The gdHandle is set to the Main Device
     * The graphicsMode is set to ditherCopy
     * The opColor is set to black
     * Right and left volumes are set to their maximums
     * LevelMeteringEnabled is set to false
     */
    public MediaParams() throws QTException {
        super(80, true);
        setMatrix(new Matrix());
        setGDHandle(GDevice.getMain());
        setGraphicsMode(64);
        setOpColor(QDColor.black);
        setLeftVolume(256);
        setRightVolume(256);
        setLevelMeteringEnabled(false);
    }

    /**
     * Creates a MediaParams object from a dimension and gWorld
     * @param dim QDDimension representing height and width of media object
     * @param g QDGraphics object representing the gWorld where the Media will be displayed
     */
    public MediaParams(QDDimension dim, QDGraphics g) throws QTException {
        this();
        setWidth(dim.getWidth());
        setHeight(dim.getHeight());
        setGWorld(g);
    }

    /**
     * Creates a MediaParams object
     * @param dim QDDimension representing height and width of media object
     * @param matrix Matrix associated with the video presenter
     * @param g QDGraphics object representing the gWorld where the Media will be displayed
     * @param lVol left volume of the audio
     * @param rVol right volume of the audio
     * @param bassLvl bass level of the audio
     * @param trebLvl treble level of the audio
     */
    public MediaParams(QDDimension dim, Matrix matrix, QDGraphics g, int lVol, int rVol, int bassLvl, int trebLvl) throws QTException {
        this(dim, g);
        setMatrix(matrix);
        setLeftVolume(lVol);
        setRightVolume(rVol);
        setBassLevel(bassLvl);
        setTrebleLevel(trebLvl);
    }

    private QTPointer bands = null;

    private QDGraphics g = null;

    private GDevice gd = null;

    private Region rgn = null;

    /**
     * Sets the width of the video component
     * @param w width
     */
    public void setWidth(float w) {
        setIntAt(0, QTUtils.X2Fix(w));
    }

    /**
     * gets the width of the video component
     */
    public float getWidth() {
        return QTUtils.Fix2X(getIntAt(0));
    }

    /**
     * Sets the height of the video component
     */
    public void setHeight(float height) {
        setIntAt(4, QTUtils.X2Fix(height));
    }

    /**
     * gets the height of the video component
     */
    public float getHeight() {
        return QTUtils.Fix2X(getIntAt(4));
    }

    /**
     * Sets the matrix associated with the video component
     * @param m Matrix
     */
    public void setMatrix(Matrix m) {
        setBytesAt(8, 36, m.getBytes(), 0);
    }

    /**
     * gets the matrix of the video component
     */
    public Matrix getMatrix() {
        Matrix matrix = new Matrix();
        getBytesAt(8, 36, matrix.getBytes(), 0);
        return matrix;
    }

    /**
     * Sets the offscreen graphics world used by the video media
     * @param g offscreen graphics world
     */
    public void setGWorld(QDGraphics g) {
        this.g = g;
        setIntAt(44, QTObject.ID(g));
    }

    /**
     * gets the graphics world of the video component
     */
    public QDGraphics getGWorld() {
        return this.g;
    }

    /**
     * Sets the graphics device used by the video media
     * @param gdh graphics device
     */
    public void setGDHandle(GDevice gdh) {
        this.gd = gdh;
        setIntAt(48, QTObject.ID(gdh));
    }

    /**
     * gets the graphics device associated with the video component
     */
    public GDevice getGDHandle() {
        return this.gd;
    }

    /**
     * Sets the clip region of the video component
     * @param clip video clip region
     */
    public void setClip(Region clip) {
        this.rgn = clip;
        setIntAt(52, QTObject.ID(clip));
    }

    /**
     * gets the clip region of the video component
     */
    public Region getClip() {
        return this.rgn;
    }

    /**
     * Sets the graphics display mode of the video component
     * @param graphicsMode graphics mode
     */
    public void setGraphicsMode(int graphicsMode) {
        setShortAt(56, (short) graphicsMode);
    }

    /**
     * gets the graphics mode of the video component
     */
    public int getGraphicsMode() {
        return getShortAt(56);
    }

    /**
     * Sets the operand color associated with the graphics mode of the video component
     * @param color operand color
     */
    public void setOpColor(QDColor color) {
        setBytesAt(58, 6, color.getBytes(), 0);
    }

    /**
     * gets the operand color of the graphics mode
     */
    public QDColor getOpColor() {
        QDColor opColor = new QDColor();
        getBytesAt(58, 6, opColor.getBytes(), 0);
        return opColor;
    }

    /**
     * Sets the left volume of the sound
     * @param leftVol left volume
     */
    public void setLeftVolume(int leftVol) {
        setShortAt(64, (short) leftVol);
    }

    /**
     * gets the left volume of the sound media
     */
    public int getLeftVolume() {
        return getShortAt(64);
    }

    /**
     * Sets the right volume of the sound
     * @param rightVol right volume
     */
    public void setRightVolume(int rightVol) {
        setShortAt(66, (short) rightVol);
    }

    /**
     * gets the right volume of the sound media
     */
    public int getRightVolume() {
        return getShortAt(66);
    }

    /**
     * Sets the right and left volumes of the sound media
     * @param left left volume
     * @param right right volume
     */
    public void setVolumes(int left, int right) {
        setLeftVolume(left);
        setRightVolume(right);
    }

    /**
     * Sets the bass level of the sound media
     * @param bassLvl base level
     */
    public void setBassLevel(int bassLvl) {
        setShortAt(68, (short) bassLvl);
    }

    /**
     * gets the bass level of the sound media
     */
    public int getBassLevel() {
        return getShortAt(68);
    }

    /**
     * Sets the treble level of the sound media
     * @param trebleLvl treble level
     */
    public void setTrebleLevel(int trebleLvl) {
        setShortAt(70, (short) trebleLvl);
    }

    /**
     * gets the treble level of the sound media
     */
    public int getTrebleLevel() {
        return getShortAt(70);
    }

    /**
     * Sets the frequency bands assoicated with the sound media
     * @param bands frequency bands
     * @throws quicktime.QTException
     */
    public void setFrequencyBands(QTPointer bands) throws QTException {
        setShortAt(72, (short) bands.getSize());
        this.bands = bands;
        setIntAt(74, QTObject.ID(this.bands));
    }

    /**
     * gets the number of frequency bands used by the sound media
     */
    public int getFrequencyBandsCount() {
        return getShortAt(72);
    }

    /**
     * gets the frequency bands associated with the sound media
     */
    public byte[] getFrequencyBands() {
        if (this.bands != null)
            return this.bands.getBytes();
        return null;
    }

    /**
     * Determines whethere the level metering is enabled or disabled
     * @param meteringEnabled true if metering is enabled
     */
    public void setLevelMeteringEnabled(boolean meteringEnabled) {
        setByteAt(78, (byte) (meteringEnabled ? 1 : 0));
    }

    /**
     * returns true if level metering is enabled
     */
    public boolean isLevelMeteringEnabled() {
        return getByteAt(78) == 1;
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[width=" + getWidth() + ",height=" + getHeight() + ",matrix=" + getMatrix() + ",\n" + "gWorld=" + getGWorld() + ",gdHandle=" + getGDHandle() + ",clip=" + getClip() + ",graphicsMode=" + getGraphicsMode() + ",opColor=" + getOpColor() + ",\n" + "leftVolume=" + getLeftVolume() + ",rightVolume=" + getRightVolume() + ",bassLevel=" + getBassLevel() + ",trebleLevel=" + getTrebleLevel() + ",frequencyBandsCount=" + getFrequencyBandsCount() + ",frequencyBands=" + Arrays.toString(getFrequencyBands()) + ",levelMeteringEnabled=" + isLevelMeteringEnabled() + "]";
    }
}
