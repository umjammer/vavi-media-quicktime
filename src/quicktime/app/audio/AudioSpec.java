
package quicktime.app.audio;

import quicktime.QTException;


/**
 * @deprecated
 */
public abstract interface AudioSpec {
    public abstract float getVolume() throws QTException;

    public abstract void setVolume(float paramFloat) throws QTException;

    public abstract boolean isMuted() throws QTException;

    /**
     * @deprecated
     */
    public abstract void setMuted(boolean paramBoolean) throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/audio
 * /AudioSpec.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
