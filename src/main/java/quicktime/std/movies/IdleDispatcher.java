
package quicktime.std.movies;

import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.MethodClosure;
import quicktime.jdirect.PrimitivesLib;


final class IdleDispatcher implements PrimitivesLib, InterfaceLib {

    private final String methodName;

    private final String methodSig;

    private final int procInfo;

    private IdleMethodClosure mMethodClosure;

    private final NextTaskNeededSooner tns;

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
