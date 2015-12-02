package com.chalcodes.event;

/**
 * Creates new {@link SimpleEventBus} instances.
 *
 * @author Kevin Krumwiede
 */
public interface BusFactory {
	/**
	 * Creates a new event bus.
	 *
	 * @return the new bus
	 */
	EventBus newBus();
}
