package com.chalcodes.event.ops;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Static methods related to event filters.
 *
 * @author Kevin Krumwiede
 */
public class Filters {
	private Filters() { /* Non-instantiable. */ }

	/**
	 * Combines two or more filters. Filters are tested in the order provided.
	 * If an event does not match one of the filters, it will not be tested by
	 * the remaining filters. The combined filter is thread safe if and only
	 * if all its constituent filters are thread safe.
	 *
	 * @param first the first event filter
	 * @param second the second event filter
	 * @param more additional event filters
	 * @param <E> the event type
	 * @return the combined event filter
	 * @throws NullPointerException if more is null or any filter is null
	 */
	public static <E> Filter<E> combine(@Nonnull final Filter<? super E> first,
	                                    @Nonnull final Filter<? super E> second,
	                                    @Nonnull final Filter<? super E>... more) {
		final List<Filter<? super E>> filters = new LinkedList<Filter<? super E>>();
		filters.add(first);
		filters.add(second);
		Collections.addAll(filters, more);
		return combine(filters);
	}

	public static <E> Filter<E> combine(@Nonnull final Collection<Filter<? super E>> filters) {
		return new Filter<E>() {
			private final List<Filter<? super E>> mFilters = new ArrayList<Filter<? super E>>(filters);

			@Override
			public boolean matches(@Nonnull final E event) {
				for(final Filter<? super E> filter : mFilters) {
					if(!filter.matches(event)) {
						return false;
					}
				}
				return true;
			}
		};
	}

	public static <E> Filter<E> invert(@Nonnull final Filter<? super E> filter) {
		return new Filter<E>() {
			@Override
			public boolean matches(@Nonnull final E event) {
				return !filter.matches(event);
			}
		};
	}
}
