package com.chalcodes.event.ops;

import com.chalcodes.event.AbstractEmitter;
import com.chalcodes.event.Op;

import javax.annotation.Nonnull;

/**
 * TODO javadoc
 *
 * @author Kevin Krumwiede
 */
public class Debounce<E> extends AbstractEmitter<E> implements Op<E, E> {
	private static final long NEVER = Long.MIN_VALUE;
	private final long mIntervalNanos;
	private long mLastEmit = NEVER;

	public Debounce(final long intervalNanos) {
		mIntervalNanos = intervalNanos;
	}

	@Override
	public void onEvent(@Nonnull final E event) {
		final long now = System.nanoTime();
		if(now - mLastEmit > mIntervalNanos || mLastEmit == NEVER) {
			mLastEmit = now;
			mReceiver.onEvent(event);
		}
	}
}
