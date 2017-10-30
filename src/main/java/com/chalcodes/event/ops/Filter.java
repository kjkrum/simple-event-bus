package com.chalcodes.event.ops;

import javax.annotation.Nonnull;

/**
 * Matches events based on arbitrary criteria.
 *
 * @author Kevin Krumwiede
 */
public interface Filter<T> {
	/**
	 * Tests whether an event matches this filter.
	 *
	 * @param event the event to test
	 * @return true if the event matches; otherwise false
	 */
	boolean matches(@Nonnull T event);
}
