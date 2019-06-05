
package quicktime.app.spaces;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * @deprecated
 */
public abstract class Sequencer {
    public static final int kNoLooping = 0;
    public static final int kLoopForwards = 1;
    public static final int kLoopPalindrome = 2;
    public static final int kLastFrame = -1;
    public static final int kFirstFrame = 1;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private int looping = 0;

    private int mCurrentFrame = 1;

    private boolean forwards = true;

    /**
     * @deprecated
     */
    public void setLooping(int paramInt) {
        this.looping = paramInt;
        if (this.looping == 1)
            this.forwards = true;
    }

    /**
     * @deprecated
     */
    public int getLooping() {
        return this.looping;
    }

    /**
     * @deprecated
     */
    public int getCurrentFrame() {
        if (this.mCurrentFrame >= size())
            setCurrentFrame(this.mCurrentFrame);
        return this.mCurrentFrame;
    }

    /**
     * @deprecated
     */
    public void setCurrentFrame(int paramInt) {
        int i = size();
        if (paramInt == -1) {
            this.mCurrentFrame = i;
        }
        if (paramInt > i) {
            if (this.mCurrentFrame == i) {
                switch (this.looping) {
                case 1:
                    this.mCurrentFrame = 1;
                    break;
                case 2:
                    this.mCurrentFrame -= 1;
                    this.forwards = false;
                    break;
                }

            } else {
                this.mCurrentFrame = i;
            }
        } else if (paramInt == 0) {
            if (this.mCurrentFrame == 1) {
                switch (this.looping) {
                case 2:
                    this.mCurrentFrame += 1;
                    this.forwards = true;
                    break;
                case 1:
                    this.mCurrentFrame = i;
                    break;
                }

            } else {
                this.mCurrentFrame = 1;
            }
        } else {
            this.mCurrentFrame = paramInt;
        }
    }

    /**
     * @deprecated
     */
    public void setFrameNext() {
        int i;

        if (this.looping == 2) {
            if (this.forwards) {
                i = this.mCurrentFrame + 1;
            } else {
                i = this.mCurrentFrame - 1;
            }
        } else {
            i = this.mCurrentFrame + 1;
        }
        setCurrentFrame(i);
    }

    /**
     * @deprecated
     */
    public void setFramePrevious() {
        if (this.looping == 2) {
            boolean bool = this.forwards;
            int i;
            if (this.forwards) {
                i = this.mCurrentFrame - 1;
                if (i < 1) {
                    bool = false;
                }
            } else {
                i = this.mCurrentFrame + 1;
                if (i > size())
                    bool = true;
            }
            setCurrentFrame(i);
            this.forwards = bool;
            return;
        }

        int i = this.mCurrentFrame - 1;
        setCurrentFrame(i);
    }

    /**
     * @deprecated
     */
    public String toString() {
        return getClass().getName() + "[size=" + size() + "]";
    }

    /**
     * @deprecated
     */
    public abstract int size();
}
