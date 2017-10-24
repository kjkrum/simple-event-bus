package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * An event bus that dispatches events using a single-threaded {@link
 * Executor}.  Receivers must be registered and unregistered only in the
 * executor thread.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public final class SimpleEventBus<T> extends AbstractEventBus<T> {
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
	public SimpleEventBus(@Nonnull final Executor executor,
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
	public SimpleEventBus(@Nonnull final Executor executor) {
		super(executor);
	}
}
