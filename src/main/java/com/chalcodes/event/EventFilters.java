package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Static methods related to event filters.
 *
 * @author Kevin Krumwiede
 */
public class EventFilters {
	private EventFilters() { /* Non-instantiable. */ }

	/**
	 * Wraps an {@link EventBus} in a proxy that tests each event with an
	 * {@link EventFilter} before broadcast.  If events are broadcast from
	 * more than one thread, the filter must be thread-safe.  Events for which
	 * {@link EventFilter#accepts(Object) accepts} returns false are silently
	 * discarded.  If {@code accepts} throws an exception, it propagates from
	 * {@link EventBus#broadcast(Object) broadcast}.
	 *
	 * @param bus the event bus to wrap
	 * @param filter the filter to apply
	 * @param <T> the event type
	 * @return the wrapper
	 */
	public static <T> EventBus<T> filterBus(@Nonnull final EventBus<T> bus, @Nonnull final EventFilter<T> filter) {
		//noinspection ConstantConditions
		if(filter == null) {
			throw new NullPointerException();
		}
		return new AbstractBusWrapper<T>(bus) {
			@Override
			public void broadcast(@Nonnull final T event) {
				if(filter.accepts(event)) {
					bus.broadcast(event);
				}
			}
		};
	}

	/**
	 * Combines two or more event filters.  Filters are tested in the order
	 * provided.  If an event does not match one of the filters, it is not
	 * tested by the remaining filters.
	 *
	 * @param first the first event filter
	 * @param second the second event filter
	 * @param more additional event filters
	 * @param <T> the event type
	 * @return the combined event filter
	 * @throws NullPointerException if more is null or any filter is null
	 */
	public static <T> EventFilter<T> combine(@Nonnull final EventFilter<T> first,
											 @Nonnull final EventFilter<T> second,
											 @Nonnull final EventFilter<T>... more) {
		return new EventFilter<T>() {
			private final EventFilter[] mFilters;

			{
				final EventFilter[] tmp = new EventFilter[more.length + 2];
				tmp[0] = first;
				tmp[1] = second;
				if(more.length > 0) {
					System.arraycopy(more, 0, tmp, 0, more.length);
				}
				for(final EventFilter filter : tmp) {
					if(filter == null) {
						throw new NullPointerException();
					}
				}
				mFilters = tmp;
			}

			@Override
			public boolean accepts(@Nonnull final T event) {
				for(final EventFilter filter : mFilters) {
					//noinspection unchecked
					if(!filter.accepts(event)) {
						return false;
					}
				}
				return true;
			}
		};
	}
}
