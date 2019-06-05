
package quicktime.std.movies;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.MethodClosure;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;


final class IdleDispatcher implements PrimitivesLib, InterfaceLib {
    private static Object linkage;

    private String methodName;

    private String methodSig;

    private int procInfo;

    private IdleMethodClosure mMethodClosure;

    private NextTaskNeededSooner tns;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(IdleDispatcher.class);
                    }
                });
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
