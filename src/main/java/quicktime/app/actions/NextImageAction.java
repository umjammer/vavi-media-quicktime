
package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.image.ImageSequencer;
import quicktime.app.image.ImageSettable;


/**
 * @deprecated
 */
public class NextImageAction extends SequencerAction implements Targetable {
    protected ImageSettable target;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public NextImageAction(int paramInt1,
            int paramInt2,
            ImageSequencer paramImageSequencer,
            ImageSettable paramImageSettable) throws QTException {
        super(paramInt1, paramInt2, paramImageSequencer);
        this.target = paramImageSettable;
    }

    /**
     * @deprecated
     */
    public void setTarget(Object paramObject) {
        this.target = ((ImageSettable) paramObject);
    }

    /**
     * @deprecated
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * @deprecated
     */
    protected void doAction(float paramFloat, int paramInt) throws QTException {
        if (paramFloat > 0.0F) {
            this.seq.setFrameNext();
        } else if (paramFloat < 0.0F) {
            this.seq.setFramePrevious();
        }
        this.target.setImageData(((ImageSequencer) this.seq).getImage(), ((ImageSequencer) this.seq).getDescription());
    }
}
