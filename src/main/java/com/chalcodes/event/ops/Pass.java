package com.chalcodes.event.ops;

import com.chalcodes.event.AbstractEmitter;
import com.chalcodes.event.Op;

import javax.annotation.Nonnull;

/**
 * Delivers events match a {@link Filter}. Events that do not match are
 * silently ignored.
 *
 * @author Kevin Krumwiede
 */
public class Pass<E> extends AbstractEmitter<E> implements Op<E,E> {
	private final Filter<E> mFilter;

	public Pass(@Nonnull final Filter<E> filter) {
		mFilter = filter;
	}

	@Override
	public void onEvent(@Nonnull final E event) {
		if(mFilter.matches(event)) {
			mReceiver.onEvent(event);
		}
	}
}
