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
public final class StickyEventBus<T> extends AbstractBusWrapper<T> {
	@Nullable private T mStickyEvent;

	public StickyEventBus(@Nonnull final Executor executor,
						  @Nullable final EventBus<Exception> exceptionBus,
						  @Nonnull final ReceiverSetFactory<T> receiverSetFactory) {
		super(new SimpleEventBus<T>(executor, exceptionBus, receiverSetFactory));
	}

	public StickyEventBus(@Nonnull final Executor executor,
						  @Nullable final EventBus<Exception> exceptionBus) {
		this(executor, exceptionBus, ReceiverSetFactories.<T>hashSetFactory());
	}

	@Override
	public boolean register(@Nonnull final EventReceiver<T> receiver) {
		synchronized(mBus) {
			final boolean added = mBus.register(receiver);
			if(added && mStickyEvent != null) {
				final T event = mStickyEvent;
				final SimpleEventBus<T> bus = (SimpleEventBus<T>) mBus;
				bus.mExecutor.execute(new Runnable() {
					@Override
					public void run() {
						bus.dispatch(receiver, event);
					}
				});
			}
			return added;
		}
	}

	@Override
	public void broadcast(@Nonnull T event) {
		synchronized(mBus) {
			mBus.broadcast(event);
			mStickyEvent = event;
		}
	}

	/**
	 * Gets the sticky event.  Returns null if there is no sticky event.
	 *
	 * @return the sticky event, or null
	 */
	@Nullable public T getSticky() {
		synchronized(mBus) {
			return mStickyEvent;
		}
	}

	/**
	 * Clears the sticky event.
	 */
	public void clearSticky() {
		synchronized(mBus) {
			mStickyEvent = null;
		}
	}
}
