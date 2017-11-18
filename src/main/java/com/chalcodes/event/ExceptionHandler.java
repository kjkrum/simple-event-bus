package com.chalcodes.event;

/**
 * Handles runtime exceptions propagating from receivers. This allows a
 * multicast {@link Emitter} to handle exceptions without interrupting the
 * iteration of its receiver collection. Implementations that do not depend on
 * the type of {@code E} can be safely cast to whatever type is required.
 *
 * @param <E> the event type
 * @author Kevin Krumwiede
 */
public interface ExceptionHandler<E> {
	void onException(Emitter<E> emitter, Receiver<? super E> receiver, E event, RuntimeException e);
}
