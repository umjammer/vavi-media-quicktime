
package quicktime.std.qtcomponents;

import quicktime.jdirect.MethodClosure;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieProgress;
import quicktime.util.QTUtils;


final class QTCompDispatcher {
    private final String methodName;

    private final String methodSig;

    private final int procInfo;

    private QTCompMethodClosure mMethodClosure;

    Movie theCurrentMov;

    private MovieProgress mp;

    QTCompDispatcher(MovieExporter movieExporter) {
        this.methodName = "mpDefaultProc";
        this.methodSig = "(ISSII)S";
        this.procInfo = 64224;
    }

    QTCompDispatcher(MovieImporter movieImporter) {
        this.methodName = "mpDefaultProc";
        this.methodSig = "(ISSII)S";
        this.procInfo = 64224;
    }

    QTCompDispatcher(MovieProgress mp) {
        this.methodName = "movieProgress";
        this.methodSig = "(ISSII)S";
        this.procInfo = 64224;
        this.mp = mp;
    }

    private short mpDefaultProc(int movie, short message, short whatOperation, int percentDone, int refcon) {
        return 0;
    }

    private short movieProgress(int movie, short message, short whatOperation, int percentDone, int refcon) {
        return (short) this.mp.execute(this.theCurrentMov, message, whatOperation, QTUtils.Fix2X(percentDone));
    }

    int ID() {
        if (this.mMethodClosure == null)
            this.mMethodClosure = new QTCompMethodClosure(this, this.methodName, this.methodSig, this.procInfo);
        return this.mMethodClosure.getClosure();
    }

    void cleanupMethodClosure() {
        this.mMethodClosure = null;
    }

    static class QTCompMethodClosure extends MethodClosure {
        QTCompMethodClosure(Object target, String methodName, String descriptor, int procInfo) {
            super(target, methodName, descriptor, procInfo);
        }

        int getClosure() {
            return this.closure;
        }
    }
}
