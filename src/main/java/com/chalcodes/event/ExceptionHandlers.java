package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Common exception handlers.
 *
 * @author Kevin Krumwiede
 */
public class ExceptionHandlers {
	private ExceptionHandlers() {}

	private static ExceptionHandler DO_NOTHING = new ExceptionHandler() {
		@Override
		public <E> void onException(final Emitter<E> emitter, final Receiver<? super E> receiver, final E event, final RuntimeException e) {
			/* Do nothing. */
		}
	};

	public static ExceptionHandler doNothing() {
		return DO_NOTHING;
	}

	private static ExceptionHandler UNREGISTER = new ExceptionHandler() {
		@Override
		public <E> void onException(final Emitter<E> emitter, final Receiver<? super E> receiver, final E event, final RuntimeException e) {
			emitter.unregister(receiver);
		}
	};

	public static ExceptionHandler unregister() {
		return UNREGISTER;
	}

	public static ExceptionHandler report(@Nonnull final Receiver<? super RuntimeException> exceptionReceiver) {
		return new ExceptionHandler() {
			@Override
			public <E> void onException(final Emitter<E> emitter, final Receiver<? super E> receiver, final E event, final RuntimeException e) {
				exceptionReceiver.onEvent(e);
			}
		};
	}

	public static ExceptionHandler unregisterAndReport(@Nonnull final Receiver<? super RuntimeException> exceptionReceiver) {
		return new ExceptionHandler() {
			@Override
			public <E> void onException(final Emitter<E> emitter, final Receiver<? super E> receiver, final E event, final RuntimeException e) {
				emitter.unregister(receiver);
				exceptionReceiver.onEvent(e);
			}
		};
	}
}
