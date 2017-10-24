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

// TODO suggest visitor pattern
// note that things like complete, error, or anything else you can imagine can
// be signalled by making your event type a visitor on any interface you want
