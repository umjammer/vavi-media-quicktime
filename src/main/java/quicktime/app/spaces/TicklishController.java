package quicktime.app.spaces;

import quicktime.app.time.Ticklish;

/**
 * @deprecated
 * A TicklishController is a controller that requires tickling by the
 * Space's Timer whenever the Space itself is tickled.
 * A TicklishController when added to a Space will be added to its
 * tickleList.
 *
 * A TicklishController has no independence of time from its Space.
 * Whenever a Space is tickled the TicklishController is also tickled.
 * A TimeableController on the other hand can have it own independent sense
 * of time and provide its own timing information for the objects it controls.
 * @see quicktime.app.spaces.TimeableController
 */
public interface TicklishController extends Ticklish, Controller {
}
