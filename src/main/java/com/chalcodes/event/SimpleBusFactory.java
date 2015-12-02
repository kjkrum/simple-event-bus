package com.chalcodes.event;

import java.util.concurrent.Executor;

/**
 * A factory that produces instances of {@link SimpleEventBus} or
 * {@link StickyEventBus}.
 *
 * @author Kevin Krumwiede
 */
public class SimpleBusFactory implements BusFactory {
	private final Executor mExecutor;
	private final EventBus<Exception> mExceptionBus;
	private final boolean mNullAllowed;
	private final boolean mSticky;

	public SimpleBusFactory(final Executor executor, final EventBus<Exception> exceptionBus,
							final boolean nullAllowed, final boolean sticky) {
		if(executor == null) {
			throw new NullPointerException();
		}
		mExecutor = executor;
		mExceptionBus = exceptionBus;
		mNullAllowed = nullAllowed;
		mSticky = sticky;
	}

	@Override
	public EventBus newBus() {
		return mSticky ?
				new StickyEventBus(mExecutor, mExceptionBus, mNullAllowed) :
				new SimpleEventBus(mExecutor, mExceptionBus, mNullAllowed);
	}
}
