package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * An event bus that dispatches events using an {@link Executor}.  The
 * executor should be single-threaded, and all methods except {@link
 * #broadcast(Object)} should be called in the executor thread.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public class SimpleEventBus<T> implements EventBus<T> {
	@Nonnull protected final Executor mExecutor;
	@Nullable private final EventBus<Exception> mExceptionBus;
	@Nullable private final EventFilter<T> mEventFilter;
	/** Copy-on-write. */
	@Nonnull private volatile Set<EventReceiver<T>> mReceivers = new HashSet<EventReceiver<T>>();

	/**
	 * Creates a new event bus.  If an exception bus is provided, any
	 * exception thrown by a receiver will be broadcast on it.  If an event
	 * filter is provided, it will be applied to every event broadcast.
	 *
	 * @param executor the broadcast executor
	 * @param exceptionBus the exception bus; may be null
	 * @param eventFilter the event filter; may be null
	 * @throws NullPointerException if executor is null
	 */
	public SimpleEventBus(@Nonnull final Executor executor,
						  @Nullable final EventBus<Exception> exceptionBus,
						  @Nullable EventFilter<T> eventFilter) {
		// noinspection ConstantConditions
		if(executor == null) {
			throw new NullPointerException();
		}
		mExecutor = executor;
		mExceptionBus = exceptionBus;
		mEventFilter = eventFilter;
	}

	/**
	 * Creates a new event bus with no event filter.
	 *
	 * @param executor the broadcast executor
	 * @param exceptionBus the exception bus; may be null
	 */
	public SimpleEventBus(@Nonnull final Executor executor,
						  @Nullable final EventBus<Exception> exceptionBus) {
		this(executor, exceptionBus, null);
	}

	@Override
	public EventBus<Exception> getExceptionBus() {
		return mExceptionBus;
	}

	@Override
	public boolean register(@Nonnull final EventReceiver<T> receiver) {
		// noinspection ConstantConditions
		if(receiver == null) {
			throw new NullPointerException();
		}
		if(mReceivers.contains(receiver)) {
			return false;
		}
		else {
			final Set<EventReceiver<T>> tmp = new HashSet<EventReceiver<T>>(mReceivers);
			tmp.add(receiver);
			mReceivers = tmp;
			return true;
		}
	}

	@Override
	public boolean unregister(@Nonnull final EventReceiver<T> receiver) {
		// noinspection ConstantConditions
		if(receiver == null) {
			throw new NullPointerException();
		}
		if(mReceivers.contains(receiver)) {
			final Set<EventReceiver<T>> tmp = new HashSet<EventReceiver<T>>(mReceivers);
			tmp.remove(receiver);
			mReceivers = tmp;
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean broadcast(@Nullable final T event) {
		if(mEventFilter == null || mEventFilter.isAccepted(event)) {
			final Set<EventReceiver<T>> receivers = mReceivers;
			if(!receivers.isEmpty()) {
				mExecutor.execute(new Runnable() {
					final Iterator<EventReceiver<T>> iter = receivers.iterator();

					@Override
					public void run() {
						while(iter.hasNext()) {
							dispatch(iter.next(), event);
						}
					}
				});
			}
			return true;
		}
		return false;
	}

	/**
	 * Dispatches an event only if the receiver is registered.  Unregisters
	 * the receiver if it throws an exception.
	 *
	 * @param receiver the receiver
	 * @param event the event to dispatch
	 */
	protected void dispatch(@Nonnull final EventReceiver<T> receiver, @Nullable final T event) {
		if(mReceivers.contains(receiver)) {
			try {
				receiver.onEvent(this, event);
			} catch(Exception e) {
				unregister(receiver);
				if(mExceptionBus != null) {
					mExceptionBus.broadcast(e);
				}
			}
		}
	}
}
