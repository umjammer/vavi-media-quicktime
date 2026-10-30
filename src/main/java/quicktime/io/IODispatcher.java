
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

    IODispatcher(MovieProgress mp) {
        this.methodName = "movieProgress";
        this.methodSig = "(ISSII)S";
        this.procInfo = 64224;
        this.mp = mp;
    }

    private short movieProgress(int movie, short message, short whatOperation, int percentDone, int refcon) {
        if (movie != 0) {
            if (this.theCurrentMov == null) {
                this.theCurrentMov = IOMovie.make(movie, this);
            } else if (quicktime.QTObject.ID(this.theCurrentMov) != movie)
                this.theCurrentMov = IOMovie.make(movie, this);
        }
        return (short) this.mp.execute(this.theCurrentMov, message, whatOperation, quicktime.util.QTUtils.Fix2X(percentDone));
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
        IOMethodClosure(Object target, String methodName, String descriptor, int procInfo) {
            super(target, methodName, descriptor, procInfo);
        }

        int getClosure() {
            return this.closure;
        }
    }

    static class IOMovie extends MovieController {
        private IOMovie() throws QTException {
            super(null);
        }

        static Movie make(int time, Object ref) {
            return (Movie) MovieController.mm(time, ref);
        }
    }
}
