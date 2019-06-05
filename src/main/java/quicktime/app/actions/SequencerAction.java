
package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.Sequencer;


/**
 * @deprecated
 */
public abstract class SequencerAction extends PeriodicAction {
    public static final int kConstraintAny = 4;

    public static final int kConstraintFirst = 1;

    public static final int kConstraintLast = 2;

    public static final int kConstraintFirstOrLast = 3;

    protected Sequencer seq;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    protected SequencerAction(int paramInt1, int paramInt2, Sequencer paramSequencer) throws QTException {
        super(paramInt1, paramInt2);
        this.seq = paramSequencer;
    }

    private int cons = 0;

    /**
     * @deprecated
     */
    public void setConstraint(int paramInt) {
        this.cons = paramInt;
    }

    /**
     * @deprecated
     */
    public int getConstraint() {
        return this.cons;
    }

    /**
     * @deprecated
     */
    protected boolean constraintReached() {
        return (this.cons == 4) || ((this.seq.getCurrentFrame() == 1) && ((this.cons | 0x1) != 0))
               || ((this.seq.getCurrentFrame() == this.seq.size()) && ((this.cons | 0x2) != 0));
    }

    /**
     * @deprecated
     */
    public boolean equals(Object paramObject) {
        if ((paramObject instanceof SequencerAction))
            return super.equals(paramObject);
        return false;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/SequencerAction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
