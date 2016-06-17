package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Filters events based on arbitrary criteria.
 *
 * @author Kevin Krumwiede
 */
public interface EventFilter<T> {
	/**
	 * Tests whether an event is accepted by this filter.  May throw a {@link
	 * RuntimeException} if rejection should result in fail-fast behavior.
	 *
	 * @param event the event to test
	 * @return true if the event is accepted; otherwise false
	 */
	boolean accepts(@Nonnull T event);
}
