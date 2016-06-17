package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

/**
 * Creates instances of {@link StickyEventBus}.
 *
 * @author Kevin Krumwiede
 */
public class StickyBusFactory<T> extends AbstractBusFactory<T> {
	public StickyBusFactory(@Nonnull final Executor executor,
							@Nullable final EventBus<Exception> exceptionBus,
							@Nonnull final ReceiverSetFactory<T> receiverSetFactory) {
		super(executor, exceptionBus, receiverSetFactory);
	}

	public StickyBusFactory(@Nonnull final Executor executor,
							@Nullable final EventBus<Exception> exceptionBus) {
		this(executor, exceptionBus, ReceiverSetFactories.<T>hashSetFactory());
	}

	@Nonnull
	@Override
	public EventBus<T> newBus() {
		return new StickyEventBus<T>(mExecutor, mExceptionBus, mReceiverSetFactory);
	}
}
