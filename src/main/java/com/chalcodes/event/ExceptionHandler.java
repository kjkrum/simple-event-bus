package com.chalcodes.event;

/**
 * // TODO javadoc
 *
 * @author Kevin Krumwiede
 */
public interface ExceptionHandler {
	// TODO consider parameterizing the class
	<E> void onException(Emitter<E> emitter, Receiver<? super E> receiver, E event, RuntimeException e);
}
