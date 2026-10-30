
package quicktime.std.sg;

import quicktime.QTException;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.MethodClosure;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.comp.ComponentDescription;
import quicktime.util.QTHandleRef;
import quicktime.util.QTPointerRef;


final class SGDispatcher implements PrimitivesLib, QuickTimeLib {

    private final String methodName;

    private final String methodSig;

    private final int procInfo;

    private final SGDataProc cb;

    private SGMethodClosure mMethodClosure;

    private final SequenceGrabber sg;

    SGDispatcher(SequenceGrabber sg, SGDataProc cb) {
        this.methodName = "callbackProc";
        this.methodSig = "(IIIIIISI)S";
        this.procInfo = 3932128;
        this.cb = cb;
        this.sg = sg;
    }

    private short callbackProc(int component,
                               int nativeRep,
                               int size,
                               int ptr,
                               int chRefCon,
                               int time,
                               short writeType,
                               int refCon) {
        ComponentDescription componentDescription = new ComponentDescription();
        int i = GetComponentInfo(component, componentDescription.getBytes(), 0, 0, 0);
        if (i < 0) {
            return (short) i;
        }
        try {
            int j = componentDescription.getSubType();
            SGChannel chan = null;
            if (j == 1936684398) {
                chan = new SGSoundChannel(component, this.sg);
            } else if (j == 1986618469) {
                chan = new SGVideoChannel(component, this.sg);
            } else if (j == 1836413801) {
                chan = new SGMusicChannel(component, this.sg);
            } else if (j == 1952807028) {
                chan = new SGTextChannel(component, this.sg);
            } else {
                return -2005;
            }
            SGPtr sgPtr = new SGPtr(nativeRep, size, this);

            int k = this.cb.execute(chan,
                    sgPtr,
                    ptr == 0 ? 0 : getIntFromPointer(ptr, 0),
                    chRefCon,
                    time,
                    writeType);
            if (ptr != 0) {
                setIntInPointer(ptr, 0, k);
            }
            return 0;
        } catch (QTException e) {
            return (short) e.errorCode();
        }
    }

    int ID() {
        if (this.mMethodClosure == null)
            this.mMethodClosure = new SGMethodClosure(this, this.methodName, this.methodSig, this.procInfo);
        return this.mMethodClosure.getClosure();
    }

    void cleanup() {
        this.mMethodClosure = null;
    }

    private static short GetComponentInfo(int component, byte[] desc, int nameHandle, int infoHandle, int iconHandle) {
        return ComponentManager.GetComponentInfo(component, desc, nameHandle, infoHandle, iconHandle);
    }

    private static void setIntInPointer(int ptr, int offset, int value) {
        MemoryManager.setIntInPointer(ptr, offset, value);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    static class SGMethodClosure extends MethodClosure {

        SGMethodClosure(Object target, String methodName, String descriptor, int procInfo) {
            super(target, methodName, descriptor, procInfo);
        }

        int getClosure() {
            return this.closure;
        }
    }

    static class SGPtr extends QTPointerRef {

        SGPtr(int nativeRep, int size, Object ref) {
            super(nativeRep, size, ref);
        }
    }

    static class SGHdl extends QTHandleRef {

        SGHdl(int nativeRep) {
            super(nativeRep, null, false);
        }
    }
}
