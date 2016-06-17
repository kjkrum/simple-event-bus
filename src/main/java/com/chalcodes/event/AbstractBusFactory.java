package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

/**
 * Simplifies the implementation of {@link BusFactory}.
 *
 * @author Kevin Krumwiede
 */
abstract public class AbstractBusFactory<T> implements BusFactory<T> {
	@Nonnull protected final Executor mExecutor;
	@Nullable protected final EventBus<Exception> mExceptionBus;
	@Nonnull protected final ReceiverSetFactory<T> mReceiverSetFactory;

	protected AbstractBusFactory(@Nonnull final Executor executor,
								 @Nullable final EventBus<Exception> exceptionBus,
								 @Nonnull final ReceiverSetFactory<T> receiverSetFactory) {
		//noinspection ConstantConditions
		if(executor == null || receiverSetFactory == null) {
			throw new NullPointerException();
		}
		mExecutor = executor;
		mExceptionBus = exceptionBus;
		mReceiverSetFactory = receiverSetFactory;
	}
}
