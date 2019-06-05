
package quicktime.io;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.movies.media.DataRef;
import quicktime.util.QTHandleRef;


public final class AliasHandle extends QTHandleRef implements QuickTimeLib {
    private static Object linkage;

    private boolean minimal;

    static {
        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(AliasHandle.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    static AliasHandle fromQTFile(QTFile paramQTFile, boolean paramBoolean) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = QTNewAlias(paramQTFile.getFSSpec(true, 768), arrayOfInt, (byte) (paramBoolean ? 1 : 0));
        QTIOException.checkError(i, paramQTFile.getPath());
        return new AliasHandle(arrayOfInt[0], null, false, paramBoolean);
    }

    static AliasHandle fromTargetQTFile(QTFile paramQTFile1, QTFile paramQTFile2, boolean paramBoolean) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;

        if (paramQTFile1 != null) {
            i = NewAlias(paramQTFile1.getFSSpec(true, 768), paramQTFile2.getFSSpec(true, 768), arrayOfInt);
        } else {
            i = NewAlias(null, paramQTFile2.getFSSpec(true, 768), arrayOfInt);
        }
        QTIOException.checkError(i, paramQTFile2.getPath());
        return new AliasHandle(arrayOfInt[0], null, false, paramBoolean);
    }

    public static AliasHandle fromDataRef(DataRef paramDataRef) throws QTException {
        if (paramDataRef.getType() != 1634494835)
            throw new QTException(-50);
        return new AliasHandle(QTObject.ID(paramDataRef), paramDataRef, paramDataRef.isLocked(), false);
    }

    public static AliasHandle fromArray(byte[] paramArrayOfByte) throws QTException {
        AliasHandle localAliasHandle = new AliasHandle(paramArrayOfByte.length, false);
        localAliasHandle.copyFromArray(0, paramArrayOfByte, 0, paramArrayOfByte.length);
        return localAliasHandle;
    }

    private AliasHandle(int paramInt, boolean paramBoolean) throws QTException {
        super(paramInt, paramBoolean);
    }

    private AliasHandle(int paramInt, Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
        super(paramInt, paramObject, paramBoolean1);
        this.minimal = paramBoolean2;
    }

    public boolean isMinimal() {
        return this.minimal;
    }

    public QTFile resolve() throws QTException {
        return QTFile.resolveAlias(this);
    }

    private static native short QTNewAlias(byte[] paramArrayOfByte, int[] paramArrayOfInt, byte paramByte);

    private static native short NewAlias(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/io/
 * AliasHandle.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
