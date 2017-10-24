package com.chalcodes.event.ops;

import com.chalcodes.event.Receiver;

import javax.annotation.Nonnull;

/**
 * Catches runtime exceptions propagating from downstream.
 *
 * @author Kevin Krumwiede
 */
public class Catch<E> extends UnicastOp<E,E> {
	private final Receiver<? super RuntimeException> mExceptionReceiver;

	public Catch(final Receiver<? super RuntimeException> exceptionReceiver) {
		mExceptionReceiver = exceptionReceiver;
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