
package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.std.StdQTException;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.movies.media.TextDescription;
import quicktime.util.QTUtils;


public final class SGTextChannel extends VisualChannel implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SGTextChannel.class);
                    }
                });
            }
        }.establish();
    }

    SGTextChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
    }

    public SGTextChannel(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramSequenceGrabber, 1952807028);
    }

    final SampleDescription makeDescription() throws QTException {
        return new TextDescription();
    }

    public final TextDescription getTextDescription() throws QTException {
        return (TextDescription) getSampleDescription();
    }

    public void setFontName(String paramString) throws StdQTException {
        int i = SGSetFontName(_ID(), QTUtils.String2PString(paramString, 255));
        StdQTException.checkError(i);
    }

    public void setFontSize(int paramInt) throws StdQTException {
        int i = SGSetFontSize(_ID(), (short) paramInt);
        StdQTException.checkError(i);
    }

    public void setForeColor(QDColor paramQDColor) throws StdQTException {
        int i = SGSetTextForeColor(_ID(), paramQDColor.getRGBColor());
        StdQTException.checkError(i);
    }

    public void setBackColor(QDColor paramQDColor) throws StdQTException {
        int i = SGSetTextBackColor(_ID(), paramQDColor.getRGBColor());
        StdQTException.checkError(i);
    }

    public void setJustification(int paramInt) throws StdQTException {
        int i = SGSetJustification(_ID(), (short) paramInt);
        StdQTException.checkError(i);
    }

    public int getReturnToSpaceValue() throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = SGGetTextReturnToSpaceValue(_ID(), arrayOfShort);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public void setReturnToSpaceValue(int paramInt) throws StdQTException {
        int i = SGSetTextReturnToSpaceValue(_ID(), (short) paramInt);
        StdQTException.checkError(i);
    }

    private static native int SGSetFontName(int paramInt, byte[] paramArrayOfByte);

    private static native int SGSetFontSize(int paramInt, short paramShort);

    private static native int SGSetTextForeColor(int paramInt, byte[] paramArrayOfByte);

    private static native int SGSetTextBackColor(int paramInt, byte[] paramArrayOfByte);

    private static native int SGSetJustification(int paramInt, short paramShort);

    private static native int SGGetTextReturnToSpaceValue(int paramInt, short[] paramArrayOfShort);

    private static native int SGSetTextReturnToSpaceValue(int paramInt, short paramShort);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/sg/
 * SGTextChannel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
