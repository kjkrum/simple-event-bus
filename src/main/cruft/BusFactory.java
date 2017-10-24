package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Creates new event buses.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public interface BusFactory<T> {
	/**
	 * Creates a new event bus.
	 *
	 * @return the new bus
	 */
	@Nonnull EventBus<T> newBus();
}
