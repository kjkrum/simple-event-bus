package com.chalcodes.event;

import javax.annotation.Nullable;

/**
 * Wraps an event that may be null.
 *
 * @author Kevin Krumwiede
 */
public final class NullableEvent<E> {
	@Nullable final E mEvent;

	public NullableEvent(@Nullable final E event) {
		mEvent = event;
	}

	@Nullable
	public E getEvent() {
		return mEvent;
	}

	@Override
	public int hashCode() {
		return mEvent == null ? 0 : mEvent.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return true;
		}
		if(obj instanceof NullableEvent) {
			NullableEvent other = (NullableEvent) obj;
			return mEvent == null ? other.mEvent == null : mEvent.equals(other.mEvent);
		}
		return false;
	}
}
