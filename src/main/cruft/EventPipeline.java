package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Broadcasts events.  This is a superinterface of {@link EventBus} with a
 * much weaker contract, allowing it to be used for things like event
 * filtering.
 *
 * @author Kevin Krumwiede
 */
public interface EventPipeline<T> {
	/**
	 * Optionally broadcasts an event.  Under what conditions, when, and to
	 * what receiver the event may be broadcast are deliberately unspecified.
	 * A valid implementation may do nothing.  Implementations should document
	 * their thread safety.
	 *
	 * @param event the event to broadcast
	 */
	void broadcast(@Nonnull T event);
}
