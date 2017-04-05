package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Factory methods for common implementations of {@link
 * UncaughtExceptionHandler}.
 *
 * @author Kevin Krumwiede
 */
public class UncaughtExceptionHandlers {
	private UncaughtExceptionHandlers() { /* Non-instantiable. */ }

	private static final UncaughtExceptionHandler RETHROW = new UncaughtExceptionHandler() {
		@Override
		public boolean handle(@Nonnull final RuntimeException exception) {
			throw exception;
		}
	};

	public static UncaughtExceptionHandler rethrowHandler() {
		return RETHROW;
	};

	private static final UncaughtExceptionHandler DO_NOTHING = new UncaughtExceptionHandler() {
		@Override
		public boolean handle(@Nonnull final RuntimeException exception) {
			return false;
		}
	};

	public static UncaughtExceptionHandler doNothingHandler() {
		return DO_NOTHING;
	}

	private static final UncaughtExceptionHandler UNREGISTER = new UncaughtExceptionHandler() {
		@Override
		public boolean handle(@Nonnull final RuntimeException exception) {
			return true;
		}
	};

	public static UncaughtExceptionHandler unregisterHandler() {
		return UNREGISTER;
	}

	public static UncaughtExceptionHandler reportHandler(@Nonnull final EventPipeline<? super RuntimeException> exceptionPipeline) {
		return new UncaughtExceptionHandler() {
			@Override
			public boolean handle(@Nonnull final RuntimeException exception) {
				exceptionPipeline.broadcast(exception);
				return false;
			}
		};
	}

	public static UncaughtExceptionHandler reportAndUnregisterHandler(@Nonnull final EventPipeline<? super RuntimeException> exceptionPipeline) {
		return new UncaughtExceptionHandler() {
			@Override
			public boolean handle(@Nonnull final RuntimeException exception) {
				exceptionPipeline.broadcast(exception);
				return true;
			}
		};
	}

	public static UncaughtExceptionHandler defaultHandler() {
		return rethrowHandler();
	}
}
