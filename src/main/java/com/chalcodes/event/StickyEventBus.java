package com.chalcodes.event;

import java.util.concurrent.Executor;

/**
 * An {@link SimpleEventBus} that retains the last event broadcast and dispatches it
 * to any subsequently registered receivers.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public class StickyEventBus<T> extends SimpleEventBus<T> {
	private final Object mLock = new Object();
	private boolean mHasSticky;
	private T mStickyEvent;

	public StickyEventBus(final Executor executor, final EventBus<Exception> exceptionBus,
						  final boolean nullAllowed) {
		super(executor, exceptionBus, nullAllowed);
	}

	@Override
	public boolean register(final EventReceiver<T> receiver) {
		synchronized(mLock) {
			final boolean added = super.register(receiver);
			if(added && mHasSticky) {
				mExecutor.execute(new Runnable() {
					final T sticky = mStickyEvent;

					@Override
					public void run() {
						dispatch(receiver, sticky);
					}
				});
			}
			return added;
		}
	}

	@Override
	public void broadcast(T event) {
		synchronized(mLock) {
			super.broadcast(event);
			mHasSticky = true;
			mStickyEvent = event;
		}
	}

	/**
	 * Tests whether this bus has a sticky event.
	 *
	 * @return true if this bus has a sticky event; otherwise false
	 */
	public boolean hasSticky() {
		synchronized(mLock) {
			return mHasSticky;
		}
	}

	/**
	 * Gets the sticky event.  Only meaningful if {@link #hasSticky()} returns
	 * true.
	 *
	 * @return the sticky event, or null
	 */
	public T getSticky() {
		synchronized(mLock) {
			return mStickyEvent;
		}
	}

	/**
	 * Clears the sticky event.
	 */
	public void clearSticky() {
		synchronized(mLock) {
			mHasSticky = false;
			mStickyEvent = null;
		}
	}
}
