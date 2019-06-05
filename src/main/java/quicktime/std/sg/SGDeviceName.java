
package quicktime.std.sg;

import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public class SGDeviceName extends QTByteObject {
    public static final int kNativeSize = 80;

    SGDeviceName() {
        super(80);
    }

    public String getName() {
        return QTUtils.PString2String(getBytes(), 0);
    }

    public int getFlags() {
        return getIntAt(68);
    }

    public String toString() {
        return getClass().getName() + "[flags=" + getFlags() + ",name=" + getName() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/sg/
 * SGDeviceName.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
