package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A synchronized wrapper around a {@link StickyOp}.
 *
 * @author Kevin Krumwiede
 */
public class SynchronizedStickyOp<I, O> extends SynchronizedOp<I, O> implements StickyOp<I, O> {

	public SynchronizedStickyOp(@Nonnull final StickyOp<I, O> op, @Nonnull final Object lock) {
		super(op, lock);
	}

	public SynchronizedStickyOp(@Nonnull final StickyOp<I, O> op) {
		super(op);
	}

	@Nullable
	@Override
	public O setEvent(@Nullable final O event) {
		synchronized(mLock) {
			// noinspection unchecked
			return ((StickyOp<I, O>) mOp).setEvent(event);
		}
	};
}
