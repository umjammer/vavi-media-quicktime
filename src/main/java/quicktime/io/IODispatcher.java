
package quicktime.io;

import quicktime.QTException;
import quicktime.jdirect.MethodClosure;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.MovieProgress;


final class IODispatcher {
    private final String methodName;

    private final String methodSig;

    private final int procInfo;

    private IOMethodClosure mMethodClosure;

    private Movie theCurrentMov;

    private final MovieProgress mp;

    IODispatcher(MovieProgress paramMovieProgress) {
        this.methodName = "movieProgress";
        this.methodSig = "(ISSII)S";
        this.procInfo = 64224;
        this.mp = paramMovieProgress;
    }

    private short movieProgress(int paramInt1, short paramShort1, short paramShort2, int paramInt2, int paramInt3) {
        if (paramInt1 != 0) {
            if (this.theCurrentMov == null) {
                this.theCurrentMov = IOMovie.make(paramInt1, this);
            } else if (quicktime.QTObject.ID(this.theCurrentMov) != paramInt1)
                this.theCurrentMov = IOMovie.make(paramInt1, this);
        }
        return (short) this.mp.execute(this.theCurrentMov, paramShort1, paramShort2, quicktime.util.QTUtils.Fix2X(paramInt2));
    }

    int ID() {
        if (this.mMethodClosure == null)
            this.mMethodClosure = new IOMethodClosure(this, this.methodName, this.methodSig, this.procInfo);
        return this.mMethodClosure.getClosure();
    }

    void cleanupMethodClosure() {
        this.mMethodClosure = null;
    }

    static class IOMethodClosure extends MethodClosure {
        IOMethodClosure(Object paramObject, String paramString1, String paramString2, int paramInt) {
            super(paramObject, paramString1, paramString2, paramInt);
        }

        int getClosure() {
            return this.closure;
        }
    }

    static class IOMovie extends MovieController {
        private IOMovie() throws QTException {
            super(null);
        }

        static Movie make(int paramInt, Object paramObject) {
            return (Movie) MovieController.mm(paramInt, paramObject);
        }
    }
}
