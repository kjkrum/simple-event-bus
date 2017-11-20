package com.chalcodes.event;

/**
 * An op that may retain references to events.
 *
 * @author Kevin Krumwiede
 */
public interface StickyOp<I, O> extends Op<I, O> {
	/**
	 * Removes references to events.
	 */
	void removeEvents();

	// TODO consider removing this class
	// the original idea was to allow events to be cleared to prevent leaks.
	// but it might be better to design programs so streams have the same scope as the events they carry.
}
