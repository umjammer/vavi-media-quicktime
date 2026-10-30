
package quicktime.std.movies;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.MethodClosure;
import quicktime.jdirect.PrimitivesLib;
import quicktime.qd.QDRect;
import quicktime.std.clocks.TimeRecord;
import quicktime.util.QTUtils;
import quicktime.util.StringHandle;


final class MoviesDispatcher implements PrimitivesLib, InterfaceLib {

    private final String methodName;

    private final String methodSig;

    private final int procInfo;

    private MoviesMethodClosure mMethodClosure;

    private boolean dontDoIdle;

    private Movie mov;

    private MovieController movCont;

    private MovieProgress mp;

    private MoviePrePreroll mppr;

    private MovieDrawingComplete mdc;

    private ActionFilter filter;

    private ExecutingWiredAction wa;

    MoviesDispatcher(Movie mov, MovieProgress mp) {
        this.methodName = "movieProgress";
        this.methodSig = "(ISSII)S";
        this.procInfo = 64224;
        this.mp = mp;
        this.mov = mov;
    }

    MoviesDispatcher(Movie mov, MoviePrePreroll mppr) {
        this.methodName = "moviePrePrerollCompleteProc";
        this.methodSig = "(ISI)V";
        this.procInfo = 3776;
        this.mov = mov;
        this.mppr = mppr;
    }

    MoviesDispatcher(Movie mov, MovieDrawingComplete mdc) {
        this.methodName = "movieDrawingCompleteProc";
        this.methodSig = "(II)S";
        this.procInfo = 992;
        this.mov = mov;
        this.mdc = mdc;
    }

    MoviesDispatcher(MovieController movCont, ActionFilter filter, boolean doIdle) {
        this.methodName = "mcFilterProc";
        this.methodSig = "(ISII)I";
        this.procInfo = 16080;
        this.movCont = movCont;
        this.filter = filter;
        this.dontDoIdle = (!doIdle);
    }

    MoviesDispatcher(Movie mov, ExecutingWiredAction wa) {
        this.methodName = "executeWiredActions";
        this.methodSig = "(IIII)S";
        this.procInfo = 16352;
        this.mov = mov;
        this.wa = wa;
    }

    private short movieProgress(int movie, short message, short whatOperation, int percentDone, int refcon) {
        int i = this.mp.execute(this.mov, message, whatOperation, QTUtils.Fix2X(percentDone));
        if (i != 0)
            return (short) i;
        return 0;
    }

    private void moviePrePrerollCompleteProc(int movie, short err, int refcon) {
        this.mppr.execute(this.mov, err);
        this.mMethodClosure = null;
    }

    private short movieDrawingCompleteProc(int movie, int refCon) {
        return (short) this.mdc.execute(this.mov);
    }

    public short executeWiredActions(int movie, int refcon, int flags, int actions) {
        short s = this.wa.execute(this.mov, flags, new AtomContainer(actions, this, false));

        return s;
    }

    private int mcFilterProc(int mc, short action, int params, int refCon) {
        if ((action == 1) && (this.dontDoIdle)) {
            return 0;
        }
        boolean bool1 = false;
        Object localObject;
        AFStringHandle afStringHandle1;
        int[] dstHandle;
        int j;
        int i;
        switch (action) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 26:
        case 50:
        case 58:
        case 61:
        case 77:
        case 86:
            bool1 = this.filter.execute(this.movCont, action);
            break;
        case 8:
        case 55:
        case 83:
        case 84:
            bool1 = this.filter.execute(this.movCont, action, QTUtils.Fix2X(params));
            break;
        case 14:
        case 82:
            bool1 = this.filter.execute(this.movCont, action, QTUtils.ShortFix2X((short) params));
            break;
        case 15:
            if (params != 0) {
                localObject = new float[] {
                    QTUtils.ShortFix2X(getShortFromPointer(params, 0))
                };
                bool1 = this.filter.execute(this.movCont, action, (float[]) localObject);
                if (bool1)
                    setShortInPointer(params, 0, QTUtils.X2ShortFix(((float[]) localObject)[0]));
            }
            break;
        case 42:
            if (params != 0) {
                localObject = new float[] {
                    QTUtils.Fix2X(getIntFromPointer(params, 0))
                };
                bool1 = this.filter.execute(this.movCont, action, (float[]) localObject);
                if (bool1) {
                    setIntInPointer(params, 0, QTUtils.X2Fix(((float[]) localObject)[0]));
                }
            }
            break;
        case 21:
        case 23:
        case 32:
        case 34:
        case 36:
        case 40:
        case 48:
        case 52:
        case 57:
            bool1 = this.filter.execute(this.movCont, action, params != 0);
            break;

        case 22:
        case 24:
        case 33:
        case 35:
        case 37:
        case 41:
        case 43:
        case 44:
        case 51:
        case 56:
        case 92:
            if (params != 0) {
                localObject = new boolean[] {
                    getByteFromPointer(params, 0) != 0 ? true : false
                };
                bool1 = this.filter.execute(this.movCont, action, (boolean[]) localObject);
                if (bool1) {
                    setByteInPointer(params, 0, (byte) (((boolean[]) localObject)[0] != false ? 1 : 0));
                }
            }
            break;
        case 25:
        case 49:
            if (params != 0) {
                localObject = new QDRect();
                copyPointerToArray(params, 0, ((QDRect) localObject).getRect(), 0, ((QDRect) localObject).getRect().length);
                bool1 = this.filter.execute(this.movCont, (QDRect) localObject);
            }
            break;
        case 18:
        case 38:
        case 85:
            bool1 = this.filter.execute(this.movCont, action, params);
            break;
        case 39:
        case 91:
            if (params != 0) {
                localObject = new int[] {
                    getIntFromPointer(params, 0)
                };
                bool1 = this.filter.execute(this.movCont, action, (int[]) localObject);
                if (bool1) {
                    setIntInPointer(params, 0, ((int[]) localObject)[0]);
                }
            }
            break;
        case 12:
        case 29:
        case 30:
        case 53:
        case 54:
            if (params != 0) {
                bool1 = this.filter.execute(this.movCont, action, new AFTimeRecord(params, this));
            }
            break;
        case 63:
        case 64:
            if (params != 0) {
                bool1 = this.filter.execute(this.movCont, action, new ResolvedQTEventSpec(params, this));
            }
            break;
        case 59:
        case 76:
            if (params != 0) {
                boolean locked = false;
                afStringHandle1 = new AFStringHandle(params, this, locked, null);
                bool1 = this.filter.execute(this.movCont, action, afStringHandle1);
            }
            break;
        case 68:
            if (params != 0) {
                dstHandle = new int[] {
                    0
                };
                PtrToHand(params, dstHandle, getByteFromPointer(params, 0) + 1);
                afStringHandle1 = new AFStringHandle(dstHandle[0], null, true, null);
                bool1 = this.filter.execute(this.movCont, action, afStringHandle1);
            }
            break;
        case 69:
            if (params != 0) {
                dstHandle = new int[] {
                    0
                };
                j = 0;
                int k = getIntFromPointer(params, 4);

                if (k != 0) {
                    while (getByteFromPointer(k, j++) != 0) {
                    }

                    PtrToHand(k, dstHandle, j);
                    AFStringHandle afStringHandle2 = new AFStringHandle(dstHandle[0], null, false, null);
                    bool1 = this.filter.execute(this.movCont,
                                                action,
                                                getIntFromPointer(params, 0),
                                                afStringHandle2);
                }
            }
            break;

        case 67:
            bool1 = this.filter.execute(this.movCont,
                                        action,
                                        new quicktime.qd.QDPoint(params & 0xFFFF, params >>> 16));
            break;
        case 72:
            if (params != 0) {
                i = getIntFromPointer(params, 4);
                bool1 = this.filter.execute(this.movCont,
                                            action,
                                            new AtomContainer(getIntFromPointer(params, 0), this, false),
                                            i == 0 ? null : new Atom(i));
            }
            break;
        case 70:
            if (params != 0) {
                bool1 = this.filter.execute(this.movCont, action, new QTGetExternalMovieRecord(params, this));
            }
            break;
        case 71:
            if (params != 0) {
                i = getIntFromPointer(params, 0);
                j = getByteFromPointer(i, 0);

                byte[] arrayOfByte = new byte[j];
                copyPointerToArray(i, 1, arrayOfByte, 0, j);
                bool1 = this.filter.execute(this.movCont,
                                            action,
                                            new AFTimeRecord(params + 4, this),
                                            new String(arrayOfByte));
            }
            break;
        }
        return bool1 ? 1 : 0;
    }

    int ID() {
        if (this.mMethodClosure == null)
            this.mMethodClosure = new MoviesMethodClosure(this, this.methodName, this.methodSig, this.procInfo);
        return this.mMethodClosure.getClosure();
    }

    void cleanupMethodClosure() {
        this.mMethodClosure = null;
    }

    private static short getShortFromPointer(int ptr, int offset) {
        return MemoryManager.getShortFromPointer(ptr, offset);
    }

    private static void setShortInPointer(int ptr, int offset, short value) {
        MemoryManager.setShortInPointer(ptr, offset, value);
    }

    static class MoviesMethodClosure extends MethodClosure {
        MoviesMethodClosure(Object target, String methodName, String descriptor, int procInfo) {
            super(target, methodName, descriptor, procInfo);
        }

        int getClosure() {
            return this.closure;
        }
    }

    private static void setIntInPointer(int ptr, int offset, int value) {
        MemoryManager.setIntInPointer(ptr, offset, value);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    static class AFTimeRecord extends TimeRecord {
        AFTimeRecord(int nativeRep, Object ref) {
            super(nativeRep, ref);
        }
    }

    private static byte getByteFromPointer(int ptr, int offset) {
        return MemoryManager.getByteFromPointer(ptr, offset);
    }

    static class AFStringHandle extends StringHandle {
        AFStringHandle(int nativeRep, Object ref, boolean locked, MoviesDispatcher outer) {
            this(nativeRep, ref, locked);
        }

        private AFStringHandle(int nativeRep, Object ref, boolean locked) {
            super(nativeRep, ref, locked ? 2 : 1);
        }
    }

    private static void setByteInPointer(int ptr, int offset, byte value) {
        MemoryManager.setByteInPointer(ptr, offset, value);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static short PtrToHand(int src, int[] dstHandle, int size) {
        return MemoryManager.PtrToHand(src, dstHandle, size);
    }
}
