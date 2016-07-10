package com.chalcodes.event;

import javax.annotation.Nullable;

/**
 * Wrapper to facilitate the publication of null events.  If null is a
 * meaningful event value, an {@code EventBus<T>} may be changed to an {@code
 * EventBus<EventWrapper<T>>}.  However, this requires the creation of an
 * additional object for every event.  Whenever possible, it is preferable to
 * declare a non-null constant to represent null.
 *
 * @author Kevin Krumwiede
 */
public class EventWrapper<T> {
	@Nullable private T mEvent;

	public EventWrapper(@Nullable T event) {
		mEvent = event;
	}

	@Nullable public T unwrap() {
		return mEvent;
	}

	private static final EventWrapper<Object> NULL_EVENT = new EventWrapper<Object>(null);

	public static <T> EventWrapper<T> nullEvent() {
		//noinspection unchecked
		return (EventWrapper<T>) NULL_EVENT;
	}
}
