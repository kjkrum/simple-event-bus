package com.chalcodes.event;

/**
 * Handles runtime exceptions propagating from receivers.
 *
 * @author Kevin Krumwiede
 */
public interface ExceptionHandler {
	// TODO consider parameterizing the class
	<E> void onException(Emitter<E> emitter, Receiver<? super E> receiver, E event, RuntimeException e);
}
