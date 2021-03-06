package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Creates sets of event receivers.
 *
 * @author Kevin Krumwiede
 */
public interface ReceiverSetFactory {
	/**
	 * Creates a new set of receivers containing the elements of the current
	 * set.
	 *
	 * @param current the current receivers
	 * @return a new set containing the elements of current
	 */
	@Nonnull <T> Set<EventReceiver<T>> copy(@Nonnull Set<EventReceiver<T>> current);
}
