package com.chalcodes.event;

/**
 * Filters the events accepted by an {@link EventBus}.  Rejected events may be
 * silently discarded, or the filter may throw a {@link RuntimeException} to
 * make {@link EventBus#broadcast(Object) broadcast} fail-fast.  This reduces
 * or eliminates the need for event validation in receivers.
 *
 * @author Kevin Krumwiede
 */
public interface EventFilter<T> {
	/**
	 * Tests whether this filter accepts an event.  This method must be
	 * reentrant if events may be broadcast from more than one thread.
	 *
	 * @param event the event to inspect
	 * @return true if the event is accepted; otherwise false
	 * @throws RuntimeException if broadcast should throw an exception
	 */
	boolean isAccepted(T event);
}
