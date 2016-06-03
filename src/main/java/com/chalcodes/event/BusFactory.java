package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Creates new event buses.
 *
 * @author Kevin Krumwiede
 */
public interface BusFactory {
	/**
	 * Creates a new event bus.
	 *
	 * @return the new bus
	 */
	@Nonnull EventBus newBus();
}
