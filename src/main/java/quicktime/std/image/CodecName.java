package quicktime.std.image;

import quicktime.util.QTPointerRef;
import quicktime.util.StringHandle;
import quicktime.util.UtilException;

/**
 * CodecName class corresponds to the CodecNameSpec structure.
 * It defines a compressor name structure.
 * refer to CodecNameSpec in QuickTime
 */
public final class CodecName extends QTPointerRef {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 44;

    CodecName(int paramInt, Object paramObject) {
        super(paramInt, 44, paramObject);
    }

    /**
     * Get the compressor type.
     * @return the compressor type.
     */
    public int getCType() {
        return getIntAt(4);
    }

    /**
     * Returns the type name in string.
     * @return the type name.
     * @throws quicktime.util.UtilException
     */
    public String getTypeName() throws UtilException {
        return getPStringAt(8);
    }

    /**
     * Get the CodecComponent.
     * @return the CodecComponent.
     */
    public CodecComponent getCodecComponent() {
        return new CodecComponent(getIntAt(0));
    }

    /**
     * Get the name of the compressor component.
     * @return the name.
     */
    public StringHandle getName() {
        return StringHandle.fromCodecName(this);
    }

    /**
     * Returns the String representation of this Codec Name.
     */
    public String toString() {
        return getClass().getName() + "[codec=" + getCodecComponent() + ",cType=" + getCType() + ",name=" + getName() + "]";
    }
}
