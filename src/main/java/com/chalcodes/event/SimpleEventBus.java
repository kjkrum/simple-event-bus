package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A synchronous multicast op. This class is not thread safe.
 *
 * @author Kevin Krumwiede
 */
public class SimpleEventBus<E> implements Op<E, E> {
	@Nonnull private final Collection<Receiver<? super E>> mReceivers;
	@Nullable private final ExceptionHandler<E> mExceptionHandler;

	public SimpleEventBus(@Nonnull final Collection<Receiver<? super E>> receivers,
	                      @Nullable final ExceptionHandler<E> exceptionHandler) {
		mReceivers = receivers;
		mExceptionHandler = exceptionHandler;
	}

	public SimpleEventBus() {
		this(new ArrayList<Receiver<? super E>>(1), null);
	}

	@Override
	public boolean register(@Nonnull final Receiver<? super E> receiver) {
		return mReceivers.add(receiver);
	}

	@Override
	public boolean unregister(@Nonnull final Receiver<? super E> receiver) {
		return mReceivers.remove(receiver);
	}

	@Override
	public void onEvent(@Nonnull final E event) {
		for(final Receiver<? super E> receiver : mReceivers) {
			dispatch(receiver, event);
		}
	}

	void dispatch(final Receiver<? super E> receiver, final E event) {
		try {
			receiver.onEvent(event);
		}
		catch(RuntimeException e) {
			if(mExceptionHandler == null) {
				throw e;
			}
			mExceptionHandler.onException(this, receiver, event, e);
		}
	}
}
