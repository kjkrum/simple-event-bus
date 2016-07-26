package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * A type-safe event bus.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public interface EventBus<T> extends EventPipeline<T> {
	/**
	 * Registers a receiver.
	 *
	 * @param receiver the receiver to register
	 * @return true if the receiver was registered; otherwise false
	 */
	boolean register(@Nonnull EventReceiver<T> receiver);

	/**
	 * Unregisters a receiver.  The receiver is guaranteed not to receive
	 * events until it is registered again.
	 *
	 * @param receiver the receiver to unregister
	 * @return true if the receiver was unregistered; otherwise false
	 */
	boolean unregister(@Nonnull EventReceiver<T> receiver);

	/**
	 * Broadcasts an event to receivers that are registered at the time this
	 * method is called, excluding any that are unregistered before the
	 * dispatch occurs.  Implementations should be thread-safe.
	 *
	 * @param event the event to broadcast
	 */
	@Override
	void broadcast(@Nonnull T event);

	/**
	 * Reports an exception from a receiver.
	 *
	 * @param exception the exception
	 */
	void report(@Nonnull Exception exception);
}
