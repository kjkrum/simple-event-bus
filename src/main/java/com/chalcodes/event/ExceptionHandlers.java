package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Common exception handlers.
 *
 * @author Kevin Krumwiede
 */
public class ExceptionHandlers {
	private ExceptionHandlers() {}

	private static ExceptionHandler<?> DO_NOTHING = new ExceptionHandler<Object>() {
		@Override
		public void onException(final Emitter<Object> emitter, final Receiver<? super Object> receiver, final Object event, final RuntimeException e) {
			/* Do nothing. */
		}
	};

	public static <T> ExceptionHandler<T> doNothing() {
		//noinspection unchecked
		return (ExceptionHandler<T>) DO_NOTHING;
	}

	private static ExceptionHandler<?> UNREGISTER = new ExceptionHandler<Object>() {
		@Override
		public void onException(final Emitter<Object> emitter, final Receiver<? super Object> receiver, final Object event, final RuntimeException e) {
			emitter.unregister(receiver);
		}
	};

	public static <T> ExceptionHandler<T> unregister() {
		//noinspection unchecked
		return (ExceptionHandler<T>) UNREGISTER;
	}

	public static <T> ExceptionHandler<T> report(@Nonnull final Receiver<? super RuntimeException> exceptionReceiver) {
		return new ExceptionHandler<T>() {
			@Override
			public void onException(final Emitter<T> emitter, final Receiver<? super T> receiver, final T event, final RuntimeException e) {
				exceptionReceiver.onEvent(e);
			}
		};
	}

	public static <T> ExceptionHandler<T> unregisterAndReport(@Nonnull final Receiver<? super RuntimeException> exceptionReceiver) {
		return new ExceptionHandler<T>() {
			@Override
			public void onException(final Emitter<T> emitter, final Receiver<? super T> receiver, final T event, final RuntimeException e) {
				emitter.unregister(receiver);
				exceptionReceiver.onEvent(e);
			}
		};
	}

	// TODO combine method?
}
