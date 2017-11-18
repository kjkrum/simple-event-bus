package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * A synchronized wrapper around an {@link Op}.
 *
 * @author Kevin Krumwiede
 */
public class SynchronizedOp<I, O> implements Op<I, O> {
	final Op<I, O> mOp;
	final Object mLock;

	public SynchronizedOp(@Nonnull final Op<I, O> op, @Nonnull final Object lock) {
		mOp = op;
		mLock = lock;
	}

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
