
package quicktime.streaming;

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


public class MediaParams extends QTPointerRef {
    public static final int kNativeSize = 80;

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

    public MediaParams(QDDimension paramQDDimension, QDGraphics paramQDGraphics) throws QTException {
        this();
        setWidth(paramQDDimension.getWidth());
        setHeight(paramQDDimension.getHeight());
        setGWorld(paramQDGraphics);
    }

    public MediaParams(QDDimension paramQDDimension,
            Matrix paramMatrix,
            QDGraphics paramQDGraphics,
            int paramInt1,
            int paramInt2,
            int paramInt3,
            int paramInt4) throws QTException {
        this(paramQDDimension, paramQDGraphics);
        setMatrix(paramMatrix);
        setLeftVolume(paramInt1);
        setRightVolume(paramInt2);
        setBassLevel(paramInt3);
        setTrebleLevel(paramInt4);
    }

    private QTPointer bands = null;

    private QDGraphics g = null;

    private GDevice gd = null;

    private Region rgn = null;

    public void setWidth(float paramFloat) {
        setIntAt(0, QTUtils.X2Fix(paramFloat));
    }

    public float getWidth() {
        return QTUtils.Fix2X(getIntAt(0));
    }

    public void setHeight(float paramFloat) {
        setIntAt(4, QTUtils.X2Fix(paramFloat));
    }

    public float getHeight() {
        return QTUtils.Fix2X(getIntAt(4));
    }

    public void setMatrix(Matrix paramMatrix) {
        setBytesAt(8, 36, paramMatrix.getBytes(), 0);
    }

    public Matrix getMatrix() {
        Matrix localMatrix = new Matrix();
        getBytesAt(8, 36, localMatrix.getBytes(), 0);
        return localMatrix;
    }

    public void setGWorld(QDGraphics paramQDGraphics) {
        this.g = paramQDGraphics;
        setIntAt(44, QTObject.ID(paramQDGraphics));
    }

    public QDGraphics getGWorld() {
        return this.g;
    }

    public void setGDHandle(GDevice paramGDevice) {
        this.gd = paramGDevice;
        setIntAt(48, QTObject.ID(paramGDevice));
    }

    public GDevice getGDHandle() {
        return this.gd;
    }

    public void setClip(Region paramRegion) {
        this.rgn = paramRegion;
        setIntAt(52, QTObject.ID(paramRegion));
    }

    public Region getClip() {
        return this.rgn;
    }

    public void setGraphicsMode(int paramInt) {
        setShortAt(56, (short) paramInt);
    }

    public int getGraphicsMode() {
        return getShortAt(56);
    }

    public void setOpColor(QDColor paramQDColor) {
        setBytesAt(58, 6, paramQDColor.getBytes(), 0);
    }

    public QDColor getOpColor() {
        QDColor localQDColor = new QDColor();
        getBytesAt(58, 6, localQDColor.getBytes(), 0);
        return localQDColor;
    }

    public void setLeftVolume(int paramInt) {
        setShortAt(64, (short) paramInt);
    }

    public int getLeftVolume() {
        return getShortAt(64);
    }

    public void setRightVolume(int paramInt) {
        setShortAt(66, (short) paramInt);
    }

    public int getRightVolume() {
        return getShortAt(66);
    }

    public void setVolumes(int paramInt1, int paramInt2) {
        setLeftVolume(paramInt1);
        setRightVolume(paramInt2);
    }

    public void setBassLevel(int paramInt) {
        setShortAt(68, (short) paramInt);
    }

    public int getBassLevel() {
        return getShortAt(68);
    }

    public void setTrebleLevel(int paramInt) {
        setShortAt(70, (short) paramInt);
    }

    public int getTrebleLevel() {
        return getShortAt(70);
    }

    public void setFrequencyBands(QTPointer paramQTPointer) throws QTException {
        setShortAt(72, (short) paramQTPointer.getSize());
        this.bands = paramQTPointer;
        setIntAt(74, QTObject.ID(this.bands));
    }

    public int getFrequencyBandsCount() {
        return getShortAt(72);
    }

    public byte[] getFrequencyBands() {
        if (this.bands != null)
            return this.bands.getBytes();
        return null;
    }

    public void setLevelMeteringEnabled(boolean paramBoolean) {
        setByteAt(78, (byte) (paramBoolean ? 1 : 0));
    }

    public boolean isLevelMeteringEnabled() {
        return getByteAt(78) == 1;
    }

    public String toString() {
        return getClass().getName() + "[width=" + getWidth() + ",height=" + getHeight() + ",matrix=" + getMatrix() + ",\n"
               + "gWorld=" + getGWorld() + ",gdHandle=" + getGDHandle() + ",clip=" + getClip() + ",graphicsMode="
               + getGraphicsMode() + ",opColor=" + getOpColor() + ",\n" + "leftVolume=" + getLeftVolume() + ",rightVolume="
               + getRightVolume() + ",bassLevel=" + getBassLevel() + ",trebleLevel=" + getTrebleLevel()
               + ",frequencyBandsCount=" + getFrequencyBandsCount() + ",frequencyBands=" + getFrequencyBands()
               + ",levelMeteringEnabled=" + isLevelMeteringEnabled() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /MediaParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
