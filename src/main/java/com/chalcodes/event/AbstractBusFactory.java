package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

/**
 * Simplifies the implementation of {@link BusFactory}.
 *
 * @author Kevin Krumwiede
 */
abstract public class AbstractBusFactory<T> implements BusFactory<T> {
	@Nonnull protected final Executor mExecutor;
	@Nonnull protected final ReceiverSetFactory<T> mReceiverSetFactory;
	@Nonnull protected final UncaughtExceptionHandler<T> mUncaughtExceptionHandler;

	protected AbstractBusFactory(@Nonnull final Executor executor,
								 @Nonnull final ReceiverSetFactory<T> receiverSetFactory,
	                             @Nonnull final UncaughtExceptionHandler<T> uncaughtExceptionHandler) {
		//noinspection ConstantConditions - accessible API
		if(executor == null || receiverSetFactory == null || uncaughtExceptionHandler == null) {
			throw new NullPointerException();
		}
		mExecutor = executor;
		mReceiverSetFactory = receiverSetFactory;
		mUncaughtExceptionHandler = uncaughtExceptionHandler;
	}
}
