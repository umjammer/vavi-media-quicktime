
package quicktime.app.display;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.InterfaceLib;
import quicktime.qd.GDevice;
import quicktime.qd.QDRect;
import quicktime.std.movies.FullScreen;


/**
 * @deprecated
 */
public class FullScreenWindow extends Window {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static Frame fr = null;

    private FullScreen fs;

    private static Frame _getFrame() {
        if (fr == null)
            fr = new Frame();
        return fr;
    }

    /**
     * @deprecated
     */
    public FullScreenWindow() throws QTException {
        this(new FullScreen());
    }

    /**
     * @deprecated
     */
    public FullScreenWindow(FullScreen paramFullScreen) throws QTException {
        this(paramFullScreen, _getFrame());
    }

    /**
     * @deprecated
     */
    public FullScreenWindow(FullScreen paramFullScreen, Frame paramFrame) throws QTException {
        super(paramFrame);
        this.fs = paramFullScreen;
    }

    private int w;

    private int h;

    private int f;

    private GDevice gd;

    /**
     * @deprecated
     */
    protected boolean doNotify = false;

    /**
     * @deprecated
     */
    public FullScreen getFullScreen() {
        return this.fs;
    }

    /**
     * @deprecated
     */
    public Dimension getPreferredSize() {
        try {
            if ((this.w == 0) || (this.h == 0)) {
                Dimension localDimension = this.fs.preflightSize(this.gd, this.w, this.h);
                this.w = localDimension.width;
                this.h = localDimension.height;
            }
        } catch (QTException localQTException) {
        }
        return new Dimension(this.w, this.h);
    }

    /**
     * @deprecated
     */
    public void setPreferredSize(int paramInt1, int paramInt2) {
        this.w = paramInt1;
        this.h = paramInt2;
    }

    /**
     * @deprecated
     */
    public int getFlags() {
        return this.f;
    }

    /**
     * @deprecated
     */
    public void setFlags(int paramInt) {
        this.f = paramInt;
    }

    /**
     * @deprecated
     */
    public void setGDevice(GDevice paramGDevice) {
        this.gd = paramGDevice;
    }

    /**
     * @deprecated
     */
    public GDevice getGDevice() {
        return this.gd;
    }

    /**
     * @deprecated
     */
    public void show() {
        if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
            this.doNotify = true;
            pack();
            try {
                Thread.sleep(100L);
            } catch (InterruptedException localInterruptedException) {
            }
        }
        try {
            Dimension localDimension = this.fs.begin(this.gd, this.w, this.h, this.f);
            setSize(localDimension);
            this.w = localDimension.width;
            this.h = localDimension.height;
            QDRect localQDRect;
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                if ((this.gd != null) && (!this.gd.equals(GDevice.getMain()))) {
                    localQDRect = this.gd.getBounds();
                    if (QTSession.isCurrentOS(4)) {
                        setLocation(localQDRect.getX(), localQDRect.getY() + -22);
                    } else {
                        setLocation(localQDRect.getX() + -5, localQDRect.getY() + -20);
                    }
                } else if (QTSession.isCurrentOS(4)) {
                    setLocation(0, -44);
                } else {
                    setLocation(-5, -40);
                }
            } else {
                if (this.gd != null) {
                    localQDRect = this.gd.getBounds();
                    setLocation(localQDRect.getX(), localQDRect.getY());
                } else {
                    setLocation(0, 0);
                }
                this.doNotify = true;
                pack();
            }

            super.show();
            if (QTSession.isCurrentOS(4))
                requestFocus();
            toFront();
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this.fs, "begin");
        }
    }

    /**
     * @deprecated
     */
    public void hide() {
        this.doNotify = false;
        try {
            this.fs.end();
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this.fs, "end");
        } finally {
            super.hide();

            if (QTSession.isCurrentOS(1)) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException localInterruptedException) {
                }
                QTM.DrawMenuBar();
            }
        }
    }

    /**
     * @deprecated
     */
    public void addNotify() {
        if (this.doNotify) {
            super.addNotify();
        }
    }

    /**
     * @deprecated
     */
    public void pack() {
        if (this.doNotify) {
            super.pack();
        }
    }

    /**
     * @deprecated
     */
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }

    private static final class QTM implements InterfaceLib {
        private static native void DrawMenuBar();
    }
}
