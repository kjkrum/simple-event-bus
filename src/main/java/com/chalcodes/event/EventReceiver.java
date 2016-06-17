package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * A type-safe event receiver.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public interface EventReceiver<T> {
	/**
	 * Receives an event.  This method should return quickly.
	 *
	 * @param bus   the source of the event
	 * @param event the event
	 */
	void onEvent(@Nonnull EventBus<T> bus, @Nonnull T event);
}
