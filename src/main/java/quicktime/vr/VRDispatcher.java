
package quicktime.vr;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.MethodClosure;
import quicktime.jdirect.PrimitivesLib;


final class VRDispatcher implements PrimitivesLib {

    private final String methodName;

    private final String methodSig;

    private final int procInfo;

    private VRMethodClosure mMethodClosure;

    private final QTVRInstance vr;

    private QTVREnteringNode en;

    private QTVRLeavingNode ln;

    private QTVRMouseOverHotSpot hs;

    private QTVRImagingComplete ic;

    private QTVRInterceptor ip;

    VRDispatcher(QTVRInstance vr, QTVREnteringNode en) {
        this.methodName = "enteringProc";
        this.methodSig = "(III)S";
        this.procInfo = 4064;
        this.en = en;
        this.vr = vr;
    }

    VRDispatcher(QTVRInstance paramQTVRInstance, QTVRLeavingNode paramQTVRLeavingNode) {
        this.methodName = "leavingProc";
        this.methodSig = "(IIIII)S";
        this.procInfo = 65504;
        this.ln = paramQTVRLeavingNode;
        this.vr = paramQTVRInstance;
    }

    VRDispatcher(QTVRInstance paramQTVRInstance, QTVRMouseOverHotSpot paramQTVRMouseOverHotSpot) {
        this.methodName = "hotSpotProc";
        this.methodSig = "(IIII)S";
        this.procInfo = 16352;
        this.hs = paramQTVRMouseOverHotSpot;
        this.vr = paramQTVRInstance;
    }

    VRDispatcher(QTVRInstance paramQTVRInstance, QTVRInterceptor paramQTVRInterceptor) {
        this.methodName = "interceptProc";
        this.methodSig = "(IIII)V";
        this.procInfo = 16320;
        this.ip = paramQTVRInterceptor;
        this.vr = paramQTVRInstance;
    }

    VRDispatcher(QTVRInstance paramQTVRInstance, QTVRImagingComplete paramQTVRImagingComplete) {
        this.methodName = "imagingComplete";
        this.methodSig = "(II)S";
        this.procInfo = 992;
        this.ic = paramQTVRImagingComplete;
        this.vr = paramQTVRInstance;
    }

    private short enteringProc(int paramInt1, int paramInt2, int paramInt3) {
        return (short) this.en.execute(this.vr, paramInt2);
    }

    private short leavingProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        boolean[] arrayOfBoolean = new boolean[1];
        short s = (short) this.ln.execute(this.vr, paramInt2, paramInt3, arrayOfBoolean);
        if (arrayOfBoolean[0] != false) {
            setByteInPointer(paramInt4, 0, (byte) 1);
        } else {
            setByteInPointer(paramInt4, 0, (byte) 0);
        }
        return s;
    }

    private short hotSpotProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return (short) this.hs.execute(this.vr, paramInt2, paramInt3);
    }

    private short imagingComplete(int paramInt1, int paramInt2) {
        return (short) this.ic.execute(this.vr, quicktime.qd.QDGraphics.getPort());
    }

    private void interceptProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        boolean bool = false;
        int i = getIntFromPointer(paramInt2, 4);
        bool = switch (i) {
            case 8192, 8193, 8194, 8195, 8202, 8203 ->
                    this.ip.execute(this.vr, new DVRInterceptRecord(paramInt2, this));
            default -> bool;
        };

        if (bool) {
            setByteInPointer(paramInt4, 0, (byte) 1);
        } else {
            setByteInPointer(paramInt4, 0, (byte) 0);
        }
    }

    int ID() {
        if (this.mMethodClosure == null)
            this.mMethodClosure = new VRMethodClosure(this, this.methodName, this.methodSig, this.procInfo);
        return this.mMethodClosure.getClosure();
    }

    void cleanupMethodClosure() {
        this.mMethodClosure = null;
    }

    private static void setByteInPointer(int paramInt1, int paramInt2, byte paramByte) {
        MemoryManager.setByteInPointer(paramInt1, paramInt2, paramByte);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    static class VRMethodClosure extends MethodClosure {
        VRMethodClosure(Object paramObject, String paramString1, String paramString2, int paramInt) {
            super(paramObject, paramString1, paramString2, paramInt);
        }

        int getClosure() {
            return this.closure;
        }
    }

    static class DVRInterceptRecord extends QTVRInterceptRecord {
        DVRInterceptRecord(int paramInt, Object paramObject) {
            super(paramInt, paramObject);
        }
    }
}
