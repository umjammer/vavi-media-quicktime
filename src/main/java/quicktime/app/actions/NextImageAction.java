package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.image.ImageSequencer;
import quicktime.app.image.ImageSettable;

/**
 * @deprecated
 * This class will display the next image of its sequence of images
 * whenever the doAction method is called from the super class
 * tickle method.
 */
public class NextImageAction extends SequencerAction implements Targetable {

    /**
     * @deprecated
     */
    protected ImageSettable target;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates an ImageAction.
     * @param scale the amount with which a second is divided into at a rate of one
     * @param period the number of scale ticks that elapse between invocations of the action.
     * @param images the images that will be cycled through
     * @param target this object is the target of the action.
     */
    public NextImageAction(int scale, int period, ImageSequencer images, ImageSettable target) throws QTException {
        super(scale, period, images);
        this.target = target;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the Target of the NextImageAction which is an ImageSettable object.
     * @param target the new target for the Action
     */
    @Override
    public void setTarget(Object target) {
        this.target = ((ImageSettable) target);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current target of the action.
     * @return the target - an ImageSettable object
     */
    @Override
    public Object getTarget() {
        return this.target;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method does whatever actions are appropriate so that subsequent calls to getImage and getDescription
     * return the correct image data.
     * @param er the actual rate at which the action has been triggered
     * @param t the time in Ticklish.kScale that the action has been triggered
     * @throws quicktime.QTException
     */
    @Override
    protected void doAction(float er, int t) throws QTException {
        if (er > 0.0F) {
            this.seq.setFrameNext();
        } else if (er < 0.0F) {
            this.seq.setFramePrevious();
        }
        this.target.setImageData(((ImageSequencer) this.seq).getImage(), ((ImageSequencer) this.seq).getDescription());
    }
}
