package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

import static com.chalcodes.event.UncaughtExceptionHandlers.defaultHandler;

/**
 * Creates instances of {@link SimpleEventBus}.
 *
 * @author Kevin Krumwiede
 */
public class SimpleBusFactory<T> extends AbstractBusFactory<T> {
	public SimpleBusFactory(@Nonnull final Executor executor,
							@Nonnull final ReceiverSetFactory<T> receiverSetFactory,
							@Nonnull final UncaughtExceptionHandler uncaughtExceptionHandler) {
		super(executor, receiverSetFactory, uncaughtExceptionHandler);
	}

	public SimpleBusFactory(@Nonnull final Executor executor) {
		this(executor, ReceiverSetFactories.<T>defaultFactory(), defaultHandler());
	}

	@Nonnull
	@Override
	public SimpleEventBus<T> newBus() {
		return new SimpleEventBus<T>(mExecutor, mReceiverSetFactory, mUncaughtExceptionHandler);
	}
}
