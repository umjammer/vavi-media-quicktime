
package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.jdirect.MethodClosure;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.comp.ComponentDescription;
import quicktime.util.QTHandleRef;
import quicktime.util.QTPointerRef;


final class SGDispatcher implements PrimitivesLib, QuickTimeLib {
    private static Object linkage;

    private String methodName;

    private String methodSig;

    private int procInfo;

    private SGDataProc cb;

    private SGMethodClosure mMethodClosure;

    private SequenceGrabber sg;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SGDispatcher.class);
                    }
                });
            }
        }.establish();
    }

    SGDispatcher(SequenceGrabber paramSequenceGrabber, SGDataProc paramSGDataProc) {
        this.methodName = "callbackProc";
        this.methodSig = "(IIIIIISI)S";
        this.procInfo = 3932128;
        this.cb = paramSGDataProc;
        this.sg = paramSequenceGrabber;
    }

    private short callbackProc(int paramInt1,
                               int paramInt2,
                               int paramInt3,
                               int paramInt4,
                               int paramInt5,
                               int paramInt6,
                               short paramShort,
                               int paramInt7) {
        ComponentDescription localComponentDescription = new ComponentDescription();
        int i = GetComponentInfo(paramInt1, localComponentDescription.getBytes(), 0, 0, 0);
        if (i < 0) {
            return (short) i;
        }
        try {
            int j = localComponentDescription.getSubType();
            Object localObject = null;
            if (j == 1936684398) {
                localObject = new SGSoundChannel(paramInt1, this.sg);
            } else if (j == 1986618469) {
                localObject = new SGVideoChannel(paramInt1, this.sg);
            } else if (j == 1836413801) {
                localObject = new SGMusicChannel(paramInt1, this.sg);
            } else if (j == 1952807028) {
                localObject = new SGTextChannel(paramInt1, this.sg);
            } else {
                return -2005;
            }
            SGPtr localSGPtr = new SGPtr(paramInt2, paramInt3, this);

            int k = this.cb.execute((SGChannel) localObject,
                                    localSGPtr,
                                    paramInt4 == 0 ? 0 : getIntFromPointer(paramInt4, 0),
                                    paramInt5,
                                    paramInt6,
                                    paramShort);
            if (paramInt4 != 0) {
                setIntInPointer(paramInt4, 0, k);
            }
            return 0;
        } catch (QTException localQTException) {
            return (short) localQTException.errorCode();
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

    private static native short GetComponentInfo(int paramInt1,
                                                 byte[] paramArrayOfByte,
                                                 int paramInt2,
                                                 int paramInt3,
                                                 int paramInt4);

    private static native void setIntInPointer(int paramInt1, int paramInt2, int paramInt3);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    static class SGMethodClosure extends MethodClosure {
        SGMethodClosure(Object paramObject, String paramString1, String paramString2, int paramInt) {
            super(paramObject, paramString1, paramString2, paramInt);
        }

        int getClosure() {
            return this.closure;
        }
    }

    static class SGPtr extends QTPointerRef {
        SGPtr(int paramInt1, int paramInt2, Object paramObject) {
            super(paramInt1, paramInt2, paramObject);
        }
    }

    static class SGHdl extends QTHandleRef {
        SGHdl(int paramInt) {
            super(paramInt, null, false);
        }
    }
}
