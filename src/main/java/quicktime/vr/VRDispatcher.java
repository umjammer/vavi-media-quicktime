
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

    VRDispatcher(QTVRInstance vr, QTVRLeavingNode ln) {
        this.methodName = "leavingProc";
        this.methodSig = "(IIIII)S";
        this.procInfo = 65504;
        this.ln = ln;
        this.vr = vr;
    }

    VRDispatcher(QTVRInstance vr, QTVRMouseOverHotSpot hs) {
        this.methodName = "hotSpotProc";
        this.methodSig = "(IIII)S";
        this.procInfo = 16352;
        this.hs = hs;
        this.vr = vr;
    }

    VRDispatcher(QTVRInstance vr, QTVRInterceptor ip) {
        this.methodName = "interceptProc";
        this.methodSig = "(IIII)V";
        this.procInfo = 16320;
        this.ip = ip;
        this.vr = vr;
    }

    VRDispatcher(QTVRInstance vr, QTVRImagingComplete ic) {
        this.methodName = "imagingComplete";
        this.methodSig = "(II)S";
        this.procInfo = 992;
        this.ic = ic;
        this.vr = vr;
    }

    private short enteringProc(int qtvr, int nodeID, int refCon) {
        return (short) this.en.execute(this.vr, nodeID);
    }

    private short leavingProc(int qtvr, int fromNodeID, int toNodeID, int cancel, int refCon) {
        boolean[] arrayOfBoolean = new boolean[1];
        short s = (short) this.ln.execute(this.vr, fromNodeID, toNodeID, arrayOfBoolean);
        if (arrayOfBoolean[0] != false) {
            setByteInPointer(cancel, 0, (byte) 1);
        } else {
            setByteInPointer(cancel, 0, (byte) 0);
        }
        return s;
    }

    private short hotSpotProc(int qtvr, int hotSpotID, int flags, int refCon) {
        return (short) this.hs.execute(this.vr, hotSpotID, flags);
    }

    private short imagingComplete(int qtvr, int refCon) {
        return (short) this.ic.execute(this.vr, quicktime.qd.QDGraphics.getPort());
    }

    private void interceptProc(int qtvr, int qtvrMsg, int refCon, int cancel) {
        boolean bool = false;
        int i = getIntFromPointer(qtvrMsg, 4);
        bool = switch (i) {
            case 8192, 8193, 8194, 8195, 8202, 8203 ->
                    this.ip.execute(this.vr, new DVRInterceptRecord(qtvrMsg, this));
            default -> bool;
        };

        if (bool) {
            setByteInPointer(cancel, 0, (byte) 1);
        } else {
            setByteInPointer(cancel, 0, (byte) 0);
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

    private static void setByteInPointer(int ptr, int offset, byte value) {
        MemoryManager.setByteInPointer(ptr, offset, value);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    static class VRMethodClosure extends MethodClosure {
        VRMethodClosure(Object target, String methodName, String descriptor, int procInfo) {
            super(target, methodName, descriptor, procInfo);
        }

        int getClosure() {
            return this.closure;
        }
    }

    static class DVRInterceptRecord extends QTVRInterceptRecord {
        DVRInterceptRecord(int nativeRep, Object ref) {
            super(nativeRep, ref);
        }
    }
}
