package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Handles runtime exceptions that propagate from event receivers.
 *
 * @author Kevin Krumwiede
 */
public interface UncaughtExceptionHandler<T> {
	void handle(@Nonnull final EventBus<T> bus,
	            @Nonnull final EventReceiver<T> receiver,
	            @Nonnull final RuntimeException exception);
}
