package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

/**
 * Creates instances of {@link StickyEventBus}.
 *
 * @author Kevin Krumwiede
 */
public class StickyBusFactory<T> extends AbstractBusFactory<T> {
	public StickyBusFactory(@Nonnull final Executor executor,
	                        @Nonnull final ReceiverSetFactory<T> receiverSetFactory,
	                        @Nonnull final UncaughtExceptionHandler<T> uncaughtExceptionHandler) {
		super(executor, receiverSetFactory, uncaughtExceptionHandler);
	}

	public StickyBusFactory(@Nonnull final Executor executor) {
		this(executor, ReceiverSetFactories.<T>hashSetFactory(), UncaughtExceptionHandlers.<T>rethrow());
	}

	@Nonnull
	@Override
	public StickyEventBus<T> newBus() {
		return new StickyEventBus<T>(mExecutor, mReceiverSetFactory, mUncaughtExceptionHandler);
	}
}
