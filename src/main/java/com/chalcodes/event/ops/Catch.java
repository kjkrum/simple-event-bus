package com.chalcodes.event.ops;

import com.chalcodes.event.Op;
import com.chalcodes.event.Receiver;
import com.chalcodes.event.AbstractEmitter;

import javax.annotation.Nonnull;

/**
 * Catches runtime exceptions propagating from downstream.
 *
 * @author Kevin Krumwiede
 */
public class Catch<E> extends AbstractEmitter<E> implements Op<E, E> {
	/* Not an ExceptionHandler because exceptions may have distant origins. */
	private final Receiver<? super RuntimeException> mExceptionReceiver;

	public Catch(@Nonnull final Receiver<? super RuntimeException> receiver) {
		mExceptionReceiver = receiver;
	}

	@Override
	public void onEvent(@Nonnull final E event) {
		try {
			mReceiver.onEvent(event);
		}
		catch(RuntimeException e) {
			mExceptionReceiver.onEvent(e);
		}
	}
}
