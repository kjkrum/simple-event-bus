package com.chalcodes.event;

/**
 * Factory methods for event filters.
 *
 * @author Kevin Krumwiede
 */
public class EventFilters {
	private EventFilters() {}

	private static final EventFilter THROW_NULL = new EventFilter() {
		@Override
		public boolean isAccepted(final Object event) {
			if(event == null) {
				throw new NullPointerException();
			}
			return true;
		}
	};

	/**
	 * Produces an event filter that throws {@link NullPointerException} if an
	 * event is null.
	 *
	 * @param <T> the event type
	 * @return the event filter
	 */
	public static <T> EventFilter<T> throwNull() {
		// noinspection unchecked
		return (EventFilter<T>) THROW_NULL;
	}

	private static final EventFilter DISCARD_NULL = new EventFilter() {
		@Override
		public boolean isAccepted(final Object event) {
			return  event != null;
		}
	};

	/**
	 * Produces an event filter that discards null events.
	 *
	 * @param <T> the event type
	 * @return the event filter
	 */
	public static <T> EventFilter<T> discardNull() {
		// noinspection unchecked
		return (EventFilter<T>) DISCARD_NULL;
	}
}
