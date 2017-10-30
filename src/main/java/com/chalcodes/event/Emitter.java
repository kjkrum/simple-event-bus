package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Pushes events to receivers. Emitters may be unicast or multicast.
 *
 * @param <E> the event type
 * @author Kevin Krumwiede
 */
public interface Emitter<E> {
	/**
	 * Registers a receiver.
	 *
	 * @param receiver the receiver to register
	 * @return true if the receiver was registered; otherwise false
	 */
	boolean register(@Nonnull Receiver<? super E> receiver);

	/**
	 * Unregisters a receiver.
	 *
	 * @param receiver the receiver to unregister
	 * @return true if the receiver was unregistered; otherwise false
	 */
	boolean unregister(@Nonnull Receiver<? super E> receiver);
}
