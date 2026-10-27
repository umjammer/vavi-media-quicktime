
package quicktime.streaming;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.MethodClosure;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.QTUtils;


final class StreamDispatcher implements PrimitivesLib {
    private static Object linkage;

    private final String methodName;

    private final String methodSig;

    private final int procInfo;

    private StreamNotifyClosure mMethodClosure;

    private final StreamNotifyProc notifyExecute;

    static {
        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(StreamDispatcher.class));
            }
        }
        new PrivelegedAction().establish();
    }

    StreamDispatcher(StreamNotifyProc paramStreamNotifyProc) {
        this.methodName = "notifyProc";
        this.methodSig = "(IIII)I";
        this.procInfo = 16368;
        this.notifyExecute = paramStreamNotifyProc;
    }

    private int notifyProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        int i;

        int j;

        int m;

        int n;
        switch (paramInt2) {
        case 1835365473:
        case 1853189228:
        case 1885630318:
        case 1886544738:
        case 1886544740:
        case 1886544744:
            return this.notifyExecute.execute(paramInt2);
        case 1885430635:
        case 1886806379:
        case 1935762283:
        case 1937007458:
        case 1937007460:
        case 2019648363:
            return this.notifyExecute.execute(paramInt2, new Stream(paramInt3, this));
        case 1852863091:
        case 2020635251:
            return this.notifyExecute.execute(paramInt2, new Presentation(paramInt3, this));

        case 1701999136:
            i = getIntFromPointer(paramInt3, 0);
            j = getIntFromPointer(paramInt3, 4);
            return this.notifyExecute.execute(paramInt1, paramInt2, getStringFromPointer(i), j);

        case 1937008494:
        case 1937010295:
            return this.notifyExecute.execute(paramInt2, new Stream(paramInt3, this));
        case 1937007464:
            i = getIntFromPointer(paramInt3, 0);

            return this.notifyExecute.execute(paramInt2, new Stream(i, this), null);
        case 1937006964:
            return this.notifyExecute.execute(paramInt2, new StatusParams(paramInt3, this));

        case 1852143472:
            return this.notifyExecute.execute(paramInt2);
        case 1970433056:
            j = getIntFromPointer(paramInt3, 0);
            int k = getIntFromPointer(paramInt3, 4);
            byte[] arrayOfByte = new byte[j];
            copyPointerToArray(k, 0, arrayOfByte, 0, j);
            return this.notifyExecute.execute(paramInt2, QTUtils.CString2String(arrayOfByte, 0));

        case 1685418593:
            m = getIntFromPointer(paramInt3, 0);
            n = getIntFromPointer(paramInt3, 4);
            long l = getLongFromPointer(paramInt3, 8);
            return this.notifyExecute.execute(paramInt2, m, n, l);

        case 1651990892:
            m = getIntFromPointer(paramInt3, 0);
            n = getIntFromPointer(paramInt3, 4);
            return this.notifyExecute.execute(paramInt2, m, n);
        }

        return 0;
    }

    private String getStringFromPointer(int paramInt) {
        if (paramInt != 0) {
            int i = 0;
            int j = 0;
            while (getByteFromPointer(paramInt, j++) != 0) {
                i++;
            }
            byte[] arrayOfByte = new byte[i];
            copyPointerToArray(paramInt, 0, arrayOfByte, 0, i);
            return QTUtils.CString2String(arrayOfByte, 0);
        }
        return "";
    }

    int ID() {
        if (this.mMethodClosure == null)
            this.mMethodClosure = new StreamNotifyClosure(this, this.methodName, this.methodSig, this.procInfo);
        return this.mMethodClosure.getClosure();
    }

    void cleanup() {
        this.mMethodClosure = null;
    }

    private static byte getIntFromPointer(int paramInt1, int paramInt2) {
        return (byte) MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    static class StreamNotifyClosure extends MethodClosure {
        StreamNotifyClosure(Object paramObject, String paramString1, String paramString2, int paramInt) {
            super(paramObject, paramString1, paramString2, paramInt);
        }

        int getClosure() {
            return this.closure;
        }
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4);
    }

    private static long getLongFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getLongFromPointer(paramInt1, paramInt2);
    }

    private static byte getByteFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getByteFromPointer(paramInt1, paramInt2);
    }
}
