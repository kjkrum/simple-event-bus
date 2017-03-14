package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

/**
 * An event bus that dispatches the last event broadcast to any subsequently
 * registered receiver.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public class StickyEventBus<T> extends SimpleEventBus<T> {
	private final Object mLock = new Object();
	@Nullable private T mStickyEvent;

	public StickyEventBus(@Nonnull final Executor executor,
						  @Nonnull final ReceiverSetFactory<T> receiverSetFactory,
	                      @Nonnull final UncaughtExceptionHandler<T> uncaughtExceptionHandler) {
		super(executor, receiverSetFactory, uncaughtExceptionHandler);
	}

	public StickyEventBus(@Nonnull final Executor executor) {
		super(executor);
	}

	@Override
	public boolean register(@Nonnull final EventReceiver<T> receiver) {
		synchronized(mLock) {
			final boolean added = super.register(receiver);
			if(added && mStickyEvent != null) {
				final T event = mStickyEvent;
				exec(new Runnable() {
					@Override
					public void run() {
						dispatch(receiver, event);
					}
				});
			}
			return added;
		}
	}

	@Override
	public void broadcast(@Nonnull T event) {
		synchronized(mLock) {
			super.broadcast(event);
			mStickyEvent = event;
		}
	}

	/**
	 * Gets the sticky event.  Returns null if there is no sticky event.
	 *
	 * @return the sticky event, or null
	 */
	@Nullable public T getSticky() {
		synchronized(mLock) {
			return mStickyEvent;
		}
	}

	/**
	 * Clears the sticky event.
	 */
	public void clearSticky() {
		synchronized(mLock) {
			mStickyEvent = null;
		}
	}
}
