
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

    MoviesDispatcher(Movie paramMovie, MovieProgress paramMovieProgress) {
        this.methodName = "movieProgress";
        this.methodSig = "(ISSII)S";
        this.procInfo = 64224;
        this.mp = paramMovieProgress;
        this.mov = paramMovie;
    }

    MoviesDispatcher(Movie paramMovie, MoviePrePreroll paramMoviePrePreroll) {
        this.methodName = "moviePrePrerollCompleteProc";
        this.methodSig = "(ISI)V";
        this.procInfo = 3776;
        this.mov = paramMovie;
        this.mppr = paramMoviePrePreroll;
    }

    MoviesDispatcher(Movie paramMovie, MovieDrawingComplete paramMovieDrawingComplete) {
        this.methodName = "movieDrawingCompleteProc";
        this.methodSig = "(II)S";
        this.procInfo = 992;
        this.mov = paramMovie;
        this.mdc = paramMovieDrawingComplete;
    }

    MoviesDispatcher(MovieController paramMovieController, ActionFilter paramActionFilter, boolean paramBoolean) {
        this.methodName = "mcFilterProc";
        this.methodSig = "(ISII)I";
        this.procInfo = 16080;
        this.movCont = paramMovieController;
        this.filter = paramActionFilter;
        this.dontDoIdle = (!paramBoolean);
    }

    MoviesDispatcher(Movie paramMovie, ExecutingWiredAction paramExecutingWiredAction) {
        this.methodName = "executeWiredActions";
        this.methodSig = "(IIII)S";
        this.procInfo = 16352;
        this.mov = paramMovie;
        this.wa = paramExecutingWiredAction;
    }

    private short movieProgress(int paramInt1, short paramShort1, short paramShort2, int paramInt2, int paramInt3) {
        int i = this.mp.execute(this.mov, paramShort1, paramShort2, QTUtils.Fix2X(paramInt2));
        if (i != 0)
            return (short) i;
        return 0;
    }

    private void moviePrePrerollCompleteProc(int paramInt1, short paramShort, int paramInt2) {
        this.mppr.execute(this.mov, paramShort);
        this.mMethodClosure = null;
    }

    private short movieDrawingCompleteProc(int paramInt1, int paramInt2) {
        return (short) this.mdc.execute(this.mov);
    }

    public short executeWiredActions(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        short s = this.wa.execute(this.mov, paramInt3, new AtomContainer(paramInt4, this, false));

        return s;
    }

    private int mcFilterProc(int paramInt1, short paramShort, int paramInt2, int paramInt3) {
        if ((paramShort == 1) && (this.dontDoIdle)) {
            return 0;
        }
        boolean bool1 = false;
        Object localObject;
        AFStringHandle localAFStringHandle1;
        int[] arrayOfInt;
        int j;
        int i;
        switch (paramShort) {
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
            bool1 = this.filter.execute(this.movCont, paramShort);
            break;
        case 8:
        case 55:
        case 83:
        case 84:
            bool1 = this.filter.execute(this.movCont, paramShort, QTUtils.Fix2X(paramInt2));
            break;
        case 14:
        case 82:
            bool1 = this.filter.execute(this.movCont, paramShort, QTUtils.ShortFix2X((short) paramInt2));
            break;
        case 15:
            if (paramInt2 != 0) {
                localObject = new float[] {
                    QTUtils.ShortFix2X(getShortFromPointer(paramInt2, 0))
                };
                bool1 = this.filter.execute(this.movCont, paramShort, (float[]) localObject);
                if (bool1)
                    setShortInPointer(paramInt2, 0, QTUtils.X2ShortFix(((float[]) localObject)[0]));
            }
            break;
        case 42:
            if (paramInt2 != 0) {
                localObject = new float[] {
                    QTUtils.Fix2X(getIntFromPointer(paramInt2, 0))
                };
                bool1 = this.filter.execute(this.movCont, paramShort, (float[]) localObject);
                if (bool1) {
                    setIntInPointer(paramInt2, 0, QTUtils.X2Fix(((float[]) localObject)[0]));
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
            bool1 = this.filter.execute(this.movCont, paramShort, paramInt2 != 0);
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
            if (paramInt2 != 0) {
                localObject = new boolean[] {
                    getByteFromPointer(paramInt2, 0) != 0 ? true : false
                };
                bool1 = this.filter.execute(this.movCont, paramShort, (boolean[]) localObject);
                if (bool1) {
                    setByteInPointer(paramInt2, 0, (byte) (((boolean[]) localObject)[0] != false ? 1 : 0));
                }
            }
            break;
        case 25:
        case 49:
            if (paramInt2 != 0) {
                localObject = new QDRect();
                copyPointerToArray(paramInt2, 0, ((QDRect) localObject).getRect(), 0, ((QDRect) localObject).getRect().length);
                bool1 = this.filter.execute(this.movCont, (QDRect) localObject);
            }
            break;
        case 18:
        case 38:
        case 85:
            bool1 = this.filter.execute(this.movCont, paramShort, paramInt2);
            break;
        case 39:
        case 91:
            if (paramInt2 != 0) {
                localObject = new int[] {
                    getIntFromPointer(paramInt2, 0)
                };
                bool1 = this.filter.execute(this.movCont, paramShort, (int[]) localObject);
                if (bool1) {
                    setIntInPointer(paramInt2, 0, ((int[]) localObject)[0]);
                }
            }
            break;
        case 12:
        case 29:
        case 30:
        case 53:
        case 54:
            if (paramInt2 != 0) {
                bool1 = this.filter.execute(this.movCont, paramShort, new AFTimeRecord(paramInt2, this));
            }
            break;
        case 63:
        case 64:
            if (paramInt2 != 0) {
                bool1 = this.filter.execute(this.movCont, paramShort, new ResolvedQTEventSpec(paramInt2, this));
            }
            break;
        case 59:
        case 76:
            if (paramInt2 != 0) {
                boolean bool2 = false;
                localAFStringHandle1 = new AFStringHandle(paramInt2, this, bool2, null);
                bool1 = this.filter.execute(this.movCont, paramShort, localAFStringHandle1);
            }
            break;
        case 68:
            if (paramInt2 != 0) {
                arrayOfInt = new int[] {
                    0
                };
                PtrToHand(paramInt2, arrayOfInt, getByteFromPointer(paramInt2, 0) + 1);
                localAFStringHandle1 = new AFStringHandle(arrayOfInt[0], null, true, null);
                bool1 = this.filter.execute(this.movCont, paramShort, localAFStringHandle1);
            }
            break;
        case 69:
            if (paramInt2 != 0) {
                arrayOfInt = new int[] {
                    0
                };
                j = 0;
                int k = getIntFromPointer(paramInt2, 4);

                if (k != 0) {
                    while (getByteFromPointer(k, j++) != 0) {
                    }

                    PtrToHand(k, arrayOfInt, j);
                    AFStringHandle localAFStringHandle2 = new AFStringHandle(arrayOfInt[0], null, false, null);
                    bool1 = this.filter.execute(this.movCont,
                                                paramShort,
                                                getIntFromPointer(paramInt2, 0),
                                                localAFStringHandle2);
                }
            }
            break;

        case 67:
            bool1 = this.filter.execute(this.movCont,
                                        paramShort,
                                        new quicktime.qd.QDPoint(paramInt2 & 0xFFFF, paramInt2 >>> 16));
            break;
        case 72:
            if (paramInt2 != 0) {
                i = getIntFromPointer(paramInt2, 4);
                bool1 = this.filter.execute(this.movCont,
                                            paramShort,
                                            new AtomContainer(getIntFromPointer(paramInt2, 0), this, false),
                                            i == 0 ? null : new Atom(i));
            }
            break;
        case 70:
            if (paramInt2 != 0) {
                bool1 = this.filter.execute(this.movCont, paramShort, new QTGetExternalMovieRecord(paramInt2, this));
            }
            break;
        case 71:
            if (paramInt2 != 0) {
                i = getIntFromPointer(paramInt2, 0);
                j = getByteFromPointer(i, 0);

                byte[] arrayOfByte = new byte[j];
                copyPointerToArray(i, 1, arrayOfByte, 0, j);
                bool1 = this.filter.execute(this.movCont,
                                            paramShort,
                                            new AFTimeRecord(paramInt2 + 4, this),
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

    private static short getShortFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getShortFromPointer(paramInt1, paramInt2);
    }

    private static void setShortInPointer(int paramInt1, int paramInt2, short paramShort) {
        MemoryManager.setShortInPointer(paramInt1, paramInt2, paramShort);
    }

    static class MoviesMethodClosure extends MethodClosure {
        MoviesMethodClosure(Object paramObject, String paramString1, String paramString2, int paramInt) {
            super(paramObject, paramString1, paramString2, paramInt);
        }

        int getClosure() {
            return this.closure;
        }
    }

    private static void setIntInPointer(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.setIntInPointer(paramInt1, paramInt2, paramInt3);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    static class AFTimeRecord extends TimeRecord {
        AFTimeRecord(int paramInt, Object paramObject) {
            super(paramInt, paramObject);
        }
    }

    private static byte getByteFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getByteFromPointer(paramInt1, paramInt2);
    }

    static class AFStringHandle extends StringHandle {
        AFStringHandle(int paramInt, Object paramObject, boolean paramBoolean, MoviesDispatcher param1) {
            this(paramInt, paramObject, paramBoolean);
        }

        private AFStringHandle(int paramInt, Object paramObject, boolean paramBoolean) {
            super(paramInt, paramObject, paramBoolean ? 2 : 1);
        }
    }

    private static void setByteInPointer(int paramInt1, int paramInt2, byte paramByte) {
        MemoryManager.setByteInPointer(paramInt1, paramInt2, paramByte);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4);
    }

    private static short PtrToHand(int paramInt1, int[] paramArrayOfInt, int paramInt2) {
        return MemoryManager.PtrToHand(paramInt1, paramArrayOfInt, paramInt2);
    }
}
