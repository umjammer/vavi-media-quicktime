
package quicktime.std.movies;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.MethodClosure;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;


final class IdleDispatcher implements PrimitivesLib, InterfaceLib {
    private static Object linkage;

    private final String methodName;

    private final String methodSig;

    private final int procInfo;

    private IdleMethodClosure mMethodClosure;

    private final NextTaskNeededSooner tns;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(IdleDispatcher.class));
            }
        }.establish();
    }

    IdleDispatcher(NextTaskNeededSooner paramNextTaskNeededSooner) {
        this.methodName = "NewQTNextTaskNeededSoonerCallbackProc";
        this.methodSig = "(III)S";
        this.procInfo = 240;
        this.tns = paramNextTaskNeededSooner;
    }

    private short NewQTNextTaskNeededSoonerCallbackProc(int paramInt1, int paramInt2, int paramInt3) {
        return (short) this.tns.execute(paramInt1, paramInt2);
    }

    int ID() {
        if (this.mMethodClosure == null)
            this.mMethodClosure = new IdleMethodClosure(this, this.methodName, this.methodSig, this.procInfo);
        return this.mMethodClosure.getClosure();
    }

    void cleanupMethodClosure() {
        this.mMethodClosure = null;
    }

    static class IdleMethodClosure extends MethodClosure {
        IdleMethodClosure(Object paramObject, String paramString1, String paramString2, int paramInt) {
            super(paramObject, paramString1, paramString2, paramInt);
        }

        int getClosure() {
            return this.closure;
        }
    }
}
