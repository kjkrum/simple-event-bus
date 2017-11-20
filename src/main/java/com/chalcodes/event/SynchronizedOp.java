package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * A synchronized wrapper around an {@link Op}.
 *
 * @author Kevin Krumwiede
 */
public class SynchronizedOp<I, O> implements Op<I, O> {
	private final Op<I, O> mOp;
	private final Object mLock;

	/**
	 * Creates a new synchronized wrapper around the specified op, using the
	 * specified lock.
	 *
	 * @param op the op
	 * @param lock the lock
	 */
	public SynchronizedOp(@Nonnull final Op<I, O> op, @Nonnull final Object lock) {
		mOp = op;
		mLock = lock;
	}

	/**
	 * Creates a new synchronized wrapper around the specified op, using the
	 * op itself as the lock.
	 *
	 * @param op the op
	 */
	public SynchronizedOp(@Nonnull final Op<I, O> op) {
		this(op, op);
	}

	@Override
	public boolean register(@Nonnull final Receiver<? super O> receiver) {
		synchronized(mLock) {
			return mOp.register(receiver);
		}
	}

	@Override
	public boolean unregister(@Nonnull final Receiver<? super O> receiver) {
		synchronized(mLock) {
			return mOp.unregister(receiver);
		}
	}

	@Override
	public void onEvent(@Nonnull final I event) {
		synchronized(mLock) {
			mOp.onEvent(event);
		}
	}
}
