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
	@Nonnull protected final ReceiverSetFactory mReceiverSetFactory;
	@Nonnull protected final UncaughtExceptionHandler mUncaughtExceptionHandler;

	protected AbstractBusFactory(@Nonnull final Executor executor,
								 @Nonnull final ReceiverSetFactory receiverSetFactory,
	                             @Nonnull final UncaughtExceptionHandler uncaughtExceptionHandler) {
		//noinspection ConstantConditions - accessible API
		if(executor == null || receiverSetFactory == null || uncaughtExceptionHandler == null) {
			throw new NullPointerException();
		}
		mExecutor = executor;
		mReceiverSetFactory = receiverSetFactory;
		mUncaughtExceptionHandler = uncaughtExceptionHandler;
	}
}
