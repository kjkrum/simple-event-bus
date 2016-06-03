package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A type-safe event receiver.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public interface EventReceiver<T> {
	/**
	 * Called when an event is broadcast.  This method should return quickly.
	 *
	 * @param bus   the source of the event
	 * @param event the event; may be null
	 */
	void onEvent(@Nonnull EventBus<T> bus, @Nullable T event);
}
