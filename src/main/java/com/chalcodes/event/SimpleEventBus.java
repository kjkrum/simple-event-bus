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
	protected final Executor mExecutor;
	private final EventBus<Exception> mExceptionBus;
	private final boolean mNullAllowed;
	/** Copy-on-write. */
	@Nonnull private volatile Set<EventReceiver<T>> mReceivers = new HashSet<EventReceiver<T>>();

	/**
	 * Creates a new event bus.  If an exception bus is provided, any
	 * exception thrown by a receiver will be broadcast on it.
	 *
	 * @param executor     the broadcast executor
	 * @param exceptionBus the exception bus; may be null
	 * @param nullAllowed  true if this bus should allow null events;
	 *                     otherwise false
	 * @throws NullPointerException if executor is null
	 */
	public SimpleEventBus(@Nonnull final Executor executor, @Nullable final EventBus<Exception> exceptionBus,
						  final boolean nullAllowed) {
		// noinspection ConstantConditions
		if(executor == null) {
			throw new NullPointerException();
		}
		mExecutor = executor;
		mExceptionBus = exceptionBus;
		mNullAllowed = nullAllowed;
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

	/**
	 * Asynchronously broadcasts an event.
	 *
	 * @param event the event to broadcast
	 * @throws NullPointerException if event is null and this bus does not
	 *                              allow null events
	 */
	@Override
	public void broadcast(@Nullable final T event) {
		if(event == null && !mNullAllowed) {
			throw new NullPointerException();
		}
		final Set<EventReceiver<T>> snapshot = mReceivers;
		mExecutor.execute(new Runnable() {
			final Iterator<EventReceiver<T>> iter = snapshot.iterator();

			@Override
			public void run() {
				while(iter.hasNext()) {
					dispatch(iter.next(), event);
				}
			}
		});
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
