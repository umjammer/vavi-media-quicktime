
package quicktime.std.image;

import quicktime.util.QTPointerRef;
import quicktime.util.StringHandle;
import quicktime.util.UtilException;


public final class CodecName extends QTPointerRef {
    public static final int kNativeSize = 44;

    CodecName(int paramInt, Object paramObject) {
        super(paramInt, 44, paramObject);
    }

    public int getCType() {
        return getIntAt(4);
    }

    public String getTypeName() throws UtilException {
        return getPStringAt(8);
    }

    public CodecComponent getCodecComponent() {
        return new CodecComponent(getIntAt(0));
    }

    public StringHandle getName() {
        return StringHandle.fromCodecName(this);
    }

    public String toString() {
        return getClass().getName() + "[codec=" + getCodecComponent() + ",cType=" + getCType() + ",name=" + getName() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /CodecName.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
