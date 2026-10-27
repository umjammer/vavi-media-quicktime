
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

    QTCompDispatcher(MovieExporter paramMovieExporter) {
        this.methodName = "mpDefaultProc";
        this.methodSig = "(ISSII)S";
        this.procInfo = 64224;
    }

    QTCompDispatcher(MovieImporter paramMovieImporter) {
        this.methodName = "mpDefaultProc";
        this.methodSig = "(ISSII)S";
        this.procInfo = 64224;
    }

    QTCompDispatcher(MovieProgress paramMovieProgress) {
        this.methodName = "movieProgress";
        this.methodSig = "(ISSII)S";
        this.procInfo = 64224;
        this.mp = paramMovieProgress;
    }

    private short mpDefaultProc(int paramInt1, short paramShort1, short paramShort2, int paramInt2, int paramInt3) {
        return 0;
    }

    private short movieProgress(int paramInt1, short paramShort1, short paramShort2, int paramInt2, int paramInt3) {
        return (short) this.mp.execute(this.theCurrentMov, paramShort1, paramShort2, QTUtils.Fix2X(paramInt2));
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
        QTCompMethodClosure(Object paramObject, String paramString1, String paramString2, int paramInt) {
            super(paramObject, paramString1, paramString2, paramInt);
        }

        int getClosure() {
            return this.closure;
        }
    }
}
