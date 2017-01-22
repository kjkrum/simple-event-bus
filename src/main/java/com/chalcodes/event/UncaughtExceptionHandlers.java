package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * TODO javadoc
 *
 * @author Kevin Krumwiede
 */
public class UncaughtExceptionHandlers {
	private UncaughtExceptionHandlers() {}

	private static final UncaughtExceptionHandler UNREGISTER_AND_REPORT =
			new UncaughtExceptionHandler() {
				@Override
				public void handle(@Nonnull final EventBus bus,
				                   @Nonnull final EventReceiver receiver,
				                   @Nonnull final Exception exception) {
					//noinspection unchecked
					bus.unregister(receiver);
					bus.report(exception);
				}
			};

	/**
	 * Returns an uncaught exception handler that unregisters the receiver and
	 * reports the exception.  This was the hard-coded behavior before the
	 * introduction of {@link UncaughtExceptionHandler}.
	 *
	 * @param <T> the event type
	 * @return the uncaught exception handler
	 */
	public static <T> UncaughtExceptionHandler<T> unregisterAndReport() {
		//noinspection unchecked
		return UNREGISTER_AND_REPORT;
	}

	private static final UncaughtExceptionHandler REPORT_ONLY =
			new UncaughtExceptionHandler() {
				@Override
				public void handle(@Nonnull final EventBus bus,
				                   @Nonnull final EventReceiver receiver,
				                   @Nonnull final Exception exception) {
					bus.report(exception);
				}
			};

	/**
	 * Returns an uncaught exception handler that reports the exception.
	 *
	 * @param <T> the event type
	 * @return the uncaught exception handler
	 */
	public static <T> UncaughtExceptionHandler<T> report() {
		//noinspection unchecked
		return REPORT_ONLY;
	}
}
