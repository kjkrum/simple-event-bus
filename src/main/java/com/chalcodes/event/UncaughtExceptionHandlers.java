package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Factory methods for common implementations of {@link
 * UncaughtExceptionHandler}.
 *
 * @author Kevin Krumwiede
 */
public class UncaughtExceptionHandlers {
	private UncaughtExceptionHandlers() {}

	private static final UncaughtExceptionHandler RETHROW = new UncaughtExceptionHandler() {
		@Override
		public void handle(@Nonnull final EventBus bus,
		                   @Nonnull final EventReceiver receiver,
		                   @Nonnull final RuntimeException exception) {
			throw exception;
		}
	};

	public static <T> UncaughtExceptionHandler<T> rethrow() {
		//noinspection unchecked
		return RETHROW;
	};

	private static final UncaughtExceptionHandler DO_NOTHING = new UncaughtExceptionHandler() {
		@Override
		public void handle(@Nonnull final EventBus bus,
		                   @Nonnull final EventReceiver receiver,
		                   @Nonnull final RuntimeException exception) {
			/* Do nothing. */
		}
	};

	public static <T> UncaughtExceptionHandler<T> doNothing() {
		//noinspection unchecked
		return DO_NOTHING;
	}

	private static final UncaughtExceptionHandler UNREGISTER = new UncaughtExceptionHandler() {
		@Override
		public void handle(@Nonnull final EventBus bus,
		                   @Nonnull final EventReceiver receiver,
		                   @Nonnull final RuntimeException exception) {
			//noinspection unchecked
			bus.unregister(receiver);
		}
	};

	public static <T> UncaughtExceptionHandler<T> unregister() {
		//noinspection unchecked
		return UNREGISTER;
	}

	public static <T> UncaughtExceptionHandler<T> report(@Nonnull final EventPipeline<? super RuntimeException> exceptionPipeline) {
		return new UncaughtExceptionHandler<T>() {
			@Override
			public void handle(@Nonnull final EventBus<T> bus,
			                   @Nonnull final EventReceiver<T> receiver,
			                   @Nonnull final RuntimeException exception) {
				exceptionPipeline.broadcast(exception);
			}
		};
	}

	public static <T> UncaughtExceptionHandler<T> unregisterAndReport(@Nonnull final EventPipeline<? super RuntimeException> exceptionPipeline) {
		return new UncaughtExceptionHandler<T>() {
			@Override
			public void handle(@Nonnull final EventBus<T> bus,
			                   @Nonnull final EventReceiver<T> receiver,
			                   @Nonnull final RuntimeException exception) {
				bus.unregister(receiver);
				exceptionPipeline.broadcast(exception);
			}
		};
	}
}
