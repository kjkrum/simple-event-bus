package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * An event bus that dispatches the last event broadcast to any subsequently
 * registered receiver.  Receivers must be registered and unregistered only in
 * the executor thread.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public final class StickyEventBus<T> extends AbstractEventBus<T> {
	private final Object mLock = new Object();
	@Nullable private T mEvent;

	/**
	 * Creates an event bus.  The order in which receivers are called is
	 * determined by the iteration order of the sets produced by the receiver
	 * set factory.
	 * <p>
	 * Use custom receiver set factories with caution.  If the factory
	 * produces sets that violate the contract of {@link Set}, or leaks the
	 * receiver sets to other parts of the application, the internal state of
	 * the event bus may be compromised, leading to unspecified behavior.
	 *
	 * @param executor the broadcast executor
	 * @param receiverSetFactory the receiver set factory
	 * @param uncaughtExceptionHandler the uncaught exception handler
	 * @throws NullPointerException if executor, receiverSetFactory, or
	 * uncaughtExceptionHandler is null
	 * @see ReceiverSetFactories
	 * @see UncaughtExceptionHandlers
	 */
	public StickyEventBus(@Nonnull final Executor executor,
						  @Nonnull final ReceiverSetFactory receiverSetFactory,
	                      @Nonnull final UncaughtExceptionHandler uncaughtExceptionHandler) {
		super(executor, receiverSetFactory, uncaughtExceptionHandler);
	}

	/**
	 * Creates an event bus with a receiver set factory that produces hash
	 * sets and an uncaught exception handler that rethrows exceptions.
	 *
	 * @param executor the broadcast executor
	 * @throws NullPointerException if executor is null
	 */
	public StickyEventBus(@Nonnull final Executor executor) {
		super(executor);
	}

	@Override
	public boolean register(@Nonnull final EventReceiver<T> receiver) {
		synchronized(mLock) {
			final boolean added = super.register(receiver);
			if(added && mEvent != null) {
				final T event = mEvent;
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
			mEvent = event;
		}
	}

	/**
	 * Gets the stuck event.  Returns {@code null} if there is no stuck event.
	 *
	 * @return the stuck event, or null
	 */
	@Nullable public T getEvent() {
		synchronized(mLock) {
			return mEvent;
		}
	}

	/**
	 * Sets the stuck event without broadcasting it.  The stuck event may be
	 * cleared by setting it to {@code null}.
	 */
	public void setEvent(@Nullable final T event) {
		synchronized(mLock) {
			mEvent = event;
		}
	}
}
