package com.chalcodes.event.ops;

import com.chalcodes.event.AbstractEmitter;
import com.chalcodes.event.Op;

import javax.annotation.Nonnull;

/**
 * Ignores the first <i>n</i> events.
 *
 * @author Kevin Krumwiede
 */
public class Ignore<E> extends AbstractEmitter<E> implements Op<E, E> {
	private final int mIgnoreCount;
	private int mCount;

	public Ignore(final int count) {
		mIgnoreCount = count;
	}

	@Override
	public void onEvent(@Nonnull final E event) {
		if(mCount < mIgnoreCount) {
			++mCount;
		}
		else {
			mReceiver.onEvent(event);
		}
	}
}
