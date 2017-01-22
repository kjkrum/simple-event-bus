package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

/**
 * Creates instances of {@link SimpleEventBus}.
 *
 * @author Kevin Krumwiede
 */
public class SimpleBusFactory<T> extends AbstractBusFactory<T> {
	public SimpleBusFactory(@Nonnull final Executor executor,
							@Nullable final EventBus<Exception> exceptionBus,
							@Nonnull final ReceiverSetFactory<T> receiverSetFactory,
							@Nonnull final UncaughtExceptionHandler<T> uncaughtExceptionHandler) {
		super(executor, exceptionBus, receiverSetFactory, uncaughtExceptionHandler);
	}

	public SimpleBusFactory(@Nonnull final Executor executor,
	                        @Nullable final EventBus<Exception> exceptionBus,
	                        @Nonnull final ReceiverSetFactory<T> receiverSetFactory) {
		this(executor, exceptionBus, receiverSetFactory, UncaughtExceptionHandlers.<T>unregisterAndReport());
	}

	public SimpleBusFactory(@Nonnull final Executor executor,
							@Nullable final EventBus<Exception> exceptionBus) {
		this(executor, exceptionBus, ReceiverSetFactories.<T>hashSetFactory());
	}

	@Nonnull
	@Override
	public EventBus<T> newBus() {
		return new SimpleEventBus<T>(mExecutor, mExceptionBus, mReceiverSetFactory);
	}
}
