package com.chalcodes.event;

import javax.annotation.Nullable;

/**
 * An op that retains a reference to the last event emitted and delivers it to
 * any subsequently registered receiver.
 *
 * @author Kevin Krumwiede
 */
public interface StickyOp<I, O> extends Op<I, O> {
	/**
	 * Sets or clears the sticky event without notifying registered receivers.
	 *
	 * @param event the new event, or null to clear
	 * @return the previous event, or null
	 */
	@Nullable
	O setEvent(@Nullable O event);

	// TODO get/set/clear? or just clear and an Ignore op?
}
