package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

import static com.chalcodes.event.ReceiverSetFactories.defaultFactory;
import static com.chalcodes.event.UncaughtExceptionHandlers.defaultHandler;

/**
 * Creates instances of {@link StickyEventBus}.
 *
 * @author Kevin Krumwiede
 */
public class StickyBusFactory<T> extends AbstractBusFactory<T> {
	public StickyBusFactory(@Nonnull final Executor executor,
	                        @Nonnull final ReceiverSetFactory receiverSetFactory,
	                        @Nonnull final UncaughtExceptionHandler uncaughtExceptionHandler) {
		super(executor, receiverSetFactory, uncaughtExceptionHandler);
	}

	public StickyBusFactory(@Nonnull final Executor executor) {
		this(executor, defaultFactory(), defaultHandler());
	}

	@Nonnull
	@Override
	public StickyEventBus<T> newBus() {
		return new StickyEventBus<T>(mExecutor, mReceiverSetFactory, mUncaughtExceptionHandler);
	}
}
