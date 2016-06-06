package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

/**
 * Produces new instances of {@link SimpleEventBus} or {@link StickyEventBus}.
 *
 * @author Kevin Krumwiede
 */
public class SimpleBusFactory implements BusFactory {
	private final Executor mExecutor;
	private final EventBus<Exception> mExceptionBus;
	private final EventFilter mEventFilter;
	private final boolean mSticky;

	public SimpleBusFactory(@Nonnull final Executor executor,
							@Nullable final EventBus<Exception> exceptionBus,
							final EventFilter eventFilter,
							final boolean sticky) {
		// noinspection ConstantConditions
		if(executor == null) {
			throw new NullPointerException();
		}
		mExecutor = executor;
		mExceptionBus = exceptionBus;
		mEventFilter = eventFilter;
		mSticky = sticky;
	}

	@Nonnull
	@Override
	public EventBus newBus() {
		return mSticky ?
				new StickyEventBus(mExecutor, mExceptionBus, mEventFilter) :
				new SimpleEventBus(mExecutor, mExceptionBus, mEventFilter);
	}
}
