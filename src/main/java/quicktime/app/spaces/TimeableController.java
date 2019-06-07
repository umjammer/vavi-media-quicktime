package quicktime.app.spaces;

import quicktime.app.time.Timeable;

/**
 * Deprecated.
 * A TimeableController is a controller that provides its own TimeBase
 * thus allowing it to have an independent sense of time from the Space
 * to which it is attached. When a TimableController is added to a Space
 * its TimeBase is slaved to the TimeBase of the Space - however this still
 * means that the Controller can have some independence of Time, but its
 * effectiveRate will depend on the rate of the Space. This enables
 * a synchronization to be maintained between the sense of time of a
 * parent Space and its TimeableController, whilst still allowing the
 * TimeableController to provide its own rate and control of its time-line.
 */
public abstract interface TimeableController extends Timeable, Controller {
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * spaces/TimeableController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
