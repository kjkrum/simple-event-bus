package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

/**
 * An event bus that holds a strong reference to the last event broadcast.
 * The sticky event will be dispatched to any subsequently registered
 * receiver.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public class StickyEventBus<T> extends SimpleEventBus<T> {
	private final Object mLock = new Object();
	private boolean mHasSticky;
	private T mStickyEvent;

	public StickyEventBus(@Nonnull final Executor executor,
						   @Nullable final EventBus<Exception> exceptionBus,
						   @Nullable final EventFilter<T> eventFilter) {
		super(executor, exceptionBus, eventFilter);
	}

	public StickyEventBus(@Nonnull final Executor executor,
						   @Nullable final EventBus<Exception> exceptionBus) {
		this(executor, exceptionBus, null);
	}

	@Override
	public boolean register(@Nonnull final EventReceiver<T> receiver) {
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
	public boolean broadcast(@Nullable T event) {
		synchronized(mLock) {
			if(super.broadcast(event)) {
				mHasSticky = true;
				mStickyEvent = event;
				return true;
			}
			return false;
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
