package quicktime.app.spaces;

import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * This interface defines operations that can control the positioning of
 * a cursor through a sequence of data.
 * Generally a sequencer will define a get method to retrieve the data
 * at the current frame. The type of object returned in a get method
 * is specific to the type of sequencer.
 * @deprecated
 */
public abstract class Sequencer {

    /**
     * @deprecated
     */
    public static final int kNoLooping = 0;

    /**
     * @deprecated
     */
    public static final int kLoopForwards = 1;

    /**
     * @deprecated
     */
    public static final int kLoopPalindrome = 2;

    /**
     * @deprecated
     */
    public static final int kLastFrame = -1;

    /**
     * @deprecated
     */
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
     * Sets the type of looping behaviour of the Sequencer.
     * @param flag the looping flag
     * @see #kNoLooping
     * @see #kLoopForwards
     * @see #kLoopPalindrome
     * @deprecated
     * @since QTJava 6.1
     */
    public void setLooping(int flag) {
        this.looping = flag;
        if (this.looping == 1)
            this.forwards = true;
    }

    /**
     * Returns the type of looping behaviour of the Sequencer.
     * @return the looping behaviour flag
     * @deprecated
     * @since QTJava 6.1
     */
    public int getLooping() {
        return this.looping;
    }

    /**
     * Returns the current frame number
     * @deprecated
     * @since QTJava 6.1
     */
    public int getCurrentFrame() {
        if (this.mCurrentFrame >= size())
            setCurrentFrame(this.mCurrentFrame);
        return this.mCurrentFrame;
    }

    /**
     * Sets the current frame number. This will observe
     * correct behaviour given the current state
     * of the image object it refers to - it will loop, etc.
     * @param frameNumber the new current frame number to set to.
     * @deprecated
     * @since QTJava 6.1
     */
    public void setCurrentFrame(int frameNumber) {
        int i = size();
        if (frameNumber == -1) {
            this.mCurrentFrame = i;
        }
        if (frameNumber > i) {
            if (this.mCurrentFrame == i) {
                switch(this.looping) {
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
        } else if (frameNumber == 0) {
            if (this.mCurrentFrame == 1) {
                switch(this.looping) {
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
            this.mCurrentFrame = frameNumber;
        }
    }

    /**
     * Sets the current frame number to the next frame.
     * @deprecated
     * @since QTJava 6.1
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
     * Sets the current frame number to the previous frame.
     * Calling previousFrame where the index of the frame is
     * currently the first index and looping is set to normal has no effect.
     * @deprecated
     * @since QTJava 6.1
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
     * Returns a string representation of this class.
     * @deprecated
     * @since QTJava 6.1
     */
    public String toString() {
        return getClass().getName() + "[size=" + size() + "]";
    }

    /**
     * Returns the number of frames of the data
     * which the sequencer is sequencing.
     * @return the current number of frames of the sequencers data sequence
     * @deprecated
     * @since QTJava 6.1
     */
    public abstract int size();
}
