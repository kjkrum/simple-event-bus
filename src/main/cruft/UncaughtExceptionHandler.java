package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Handles runtime exceptions that propagate from event receivers.
 *
 * @author Kevin Krumwiede
 */
public interface UncaughtExceptionHandler {
	/**
	 * Handles a runtime exception.
	 *
	 * @param exception the exception
	 * @return true if the receiver that threw the exception should be
	 * unregistered; otherwise false
	 */
	boolean handle(@Nonnull final RuntimeException exception);
}
