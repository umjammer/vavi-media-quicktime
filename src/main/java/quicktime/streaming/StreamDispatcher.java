
package quicktime.streaming;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.MethodClosure;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.QTUtils;


final class StreamDispatcher implements PrimitivesLib {

    private final String methodName;

    private final String methodSig;

    private final int procInfo;

    private StreamNotifyClosure mMethodClosure;

    private final StreamNotifyProc notifyExecute;

    StreamDispatcher(StreamNotifyProc notifyExecute) {
        this.methodName = "notifyProc";
        this.methodSig = "(IIII)I";
        this.procInfo = 16368;
        this.notifyExecute = notifyExecute;
    }

    private int notifyProc(int err, int type, int params, int refCon) {
        int i;

        int j;

        int m;

        int n;
        switch (type) {
        case 1835365473:
        case 1853189228:
        case 1885630318:
        case 1886544738:
        case 1886544740:
        case 1886544744:
            return this.notifyExecute.execute(type);
        case 1885430635:
        case 1886806379:
        case 1935762283:
        case 1937007458:
        case 1937007460:
        case 2019648363:
            return this.notifyExecute.execute(type, new Stream(params, this));
        case 1852863091:
        case 2020635251:
            return this.notifyExecute.execute(type, new Presentation(params, this));

        case 1701999136:
            i = getIntFromPointer(params, 0);
            j = getIntFromPointer(params, 4);
            return this.notifyExecute.execute(err, type, getStringFromPointer(i), j);

        case 1937008494:
        case 1937010295:
            return this.notifyExecute.execute(type, new Stream(params, this));
        case 1937007464:
            i = getIntFromPointer(params, 0);

            return this.notifyExecute.execute(type, new Stream(i, this), null);
        case 1937006964:
            return this.notifyExecute.execute(type, new StatusParams(params, this));

        case 1852143472:
            return this.notifyExecute.execute(type);
        case 1970433056:
            j = getIntFromPointer(params, 0);
            int k = getIntFromPointer(params, 4);
            byte[] cString = new byte[j];
            copyPointerToArray(k, 0, cString, 0, j);
            return this.notifyExecute.execute(type, QTUtils.CString2String(cString, 0));

        case 1685418593:
            m = getIntFromPointer(params, 0);
            n = getIntFromPointer(params, 4);
            long l = getLongFromPointer(params, 8);
            return this.notifyExecute.execute(type, m, n, l);

        case 1651990892:
            m = getIntFromPointer(params, 0);
            n = getIntFromPointer(params, 4);
            return this.notifyExecute.execute(type, m, n);
        }

        return 0;
    }

    private String getStringFromPointer(int ptr) {
        if (ptr != 0) {
            int i = 0;
            int j = 0;
            while (getByteFromPointer(ptr, j++) != 0) {
                i++;
            }
            byte[] cString = new byte[i];
            copyPointerToArray(ptr, 0, cString, 0, i);
            return QTUtils.CString2String(cString, 0);
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

    private static byte getIntFromPointer(int ptr, int offset) {
        return (byte) MemoryManager.getIntFromPointer(ptr, offset);
    }

    static class StreamNotifyClosure extends MethodClosure {
        StreamNotifyClosure(Object target, String methodName, String descriptor, int procInfo) {
            super(target, methodName, descriptor, procInfo);
        }

        int getClosure() {
            return this.closure;
        }
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static long getLongFromPointer(int ptr, int offset) {
        return MemoryManager.getLongFromPointer(ptr, offset);
    }

    private static byte getByteFromPointer(int ptr, int offset) {
        return MemoryManager.getByteFromPointer(ptr, offset);
    }
}
