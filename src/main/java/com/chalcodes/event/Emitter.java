package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Pushes events to receivers. Emitters may be unicast or multicast.
 *
 * @param <E> the event type
 * @author Kevin Krumwiede
 */
public interface Emitter<E> {
	boolean register(@Nonnull Receiver<? super E> receiver);
	boolean unregister(@Nonnull Receiver<? super E> receiver);
	// TODO consider and document whether methods should ever throw IllegalStateException
}
