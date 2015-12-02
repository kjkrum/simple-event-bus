package com.chalcodes.event;

/**
 * A type-safe event bus.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public interface EventBus<T> {
	EventBus<Exception> getExceptionBus();

	boolean register(EventReceiver<T> receiver);

	void unregister(EventReceiver<T> receiver);

	void broadcast(T event);
}
