package quicktime.vr;

import quicktime.qd.QDGraphics;

/**
 * This interface is defined by an application to receive callbacks when
 * a QTVRInstance has finished rendering in an offscreen QDGraphics
 * and is ready to be blitted to the screen.
 * @see {@link quicktime.vr.QTVRInstance}
 */
public abstract interface QTVRImagingComplete {

    /**
     * This method is called by MoviesTask when a QTVRInstance has completed
     * rendering and is ready to be blitted to the screen.
     * @param qd the offscreen QDGraphics that the QTVRInstace has rendered into
     * @return an error code or zero if no error.
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
    public abstract int execute(QTVRInstance paramQTVRInstance, QDGraphics paramQDGraphics);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRImagingComplete.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
