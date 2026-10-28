package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.Sequencer;

/**
 * @deprecated
 * This class is an abstract class for actions that interact with some kind of Sequencer.
 * The doAction method is left for the subclass to define so that it can define the type
 * of a Sequencer the action deals with. The constraining conditions of the Sequencer are dealt with by
 * this class.
 */
public abstract class SequencerAction extends PeriodicAction {

    /**
     * @deprecated
     */
    public static final int kConstraintAny = 4;

    /**
     * @deprecated
     */
    public static final int kConstraintFirst = 1;

    /**
     * @deprecated
     */
    public static final int kConstraintLast = 2;

    /**
     * @deprecated
     */
    public static final int kConstraintFirstOrLast = 3;

    /**
     * @deprecated
     * This is the Sequencer the action uses * @deprecated since QTJava 6.1
     */
    protected Sequencer seq;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Create a SequencerAction.
     * @param scale the amount with which a second is divided into at a rate of one
     * @param period the number of scale ticks that elapse between invocations of the action.
     * @param seq the Sequencer.
     */
    protected SequencerAction(int scale, int period, Sequencer seq) throws QTException {
        super(scale, period);
        this.seq = seq;
    }

    private int cons = 0;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Set the constraining condition that determines the cirumstances of when
     * the attached actionable object is triggered.
     * @param cons the constraint
     */
    public void setConstraint(int cons) {
        this.cons = cons;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Gets the constraining condition of the actionable doAction method
     * @return the constraint
     */
    public int getConstraint() {
        return this.cons;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Subclasses should return true if after an action is triggered (the doAction
     * method is called) the action has reached one of its specified constraint conditions
     * and the actionable object should be triggered. How this is defined is determined
     * totally by the subclass.
     * @return true if the actionable object (if set) should be triggered
     */
    @Override
    protected boolean constraintReached() {
        return (this.cons == 4) || ((this.seq.getCurrentFrame() == 1) && ((this.cons | 0x1) != 0)) || ((this.seq.getCurrentFrame() == this.seq.size()) && ((this.cons | 0x2) != 0));
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the objects are equal or false otherwise.
     * @param obj the object to test
     * @return a boolean
     */
    public boolean equals(Object obj) {
        if ((obj instanceof SequencerAction))
            return super.equals(obj);
        return false;
    }
}

