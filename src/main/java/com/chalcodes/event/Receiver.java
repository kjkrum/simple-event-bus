package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Receives events.
 *
 * @param <E> the event type
 * @author Kevin Krumwiede
 */
public interface Receiver<E> {
	void onEvent(@Nonnull E event);
}
