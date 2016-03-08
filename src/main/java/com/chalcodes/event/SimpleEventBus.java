package com.chalcodes.event;

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
	private volatile Set<EventReceiver<T>> mReceivers;

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
	public SimpleEventBus(final Executor executor, final EventBus<Exception> exceptionBus,
						  final boolean nullAllowed) {
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
	public boolean register(final EventReceiver<T> receiver) {
		if(receiver == null) {
			throw new NullPointerException();
		}
		if(mReceivers == null) {
			final Set<EventReceiver<T>> tmp = new HashSet<EventReceiver<T>>();
			tmp.add(receiver);
			mReceivers = tmp;
			return true;
		}
		else if(mReceivers.contains(receiver)) {
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
	public boolean unregister(final EventReceiver<T> receiver) {
		if(receiver == null) {
			throw new NullPointerException();
		}
		if(mReceivers != null && mReceivers.contains(receiver)) {
			if(mReceivers.size() == 1) {
				mReceivers = null;
				return true;
			}
			else {
				final Set<EventReceiver<T>> tmp = new HashSet<EventReceiver<T>>(mReceivers);
				tmp.remove(receiver);
				mReceivers = tmp;
				return true;
			}
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
	public void broadcast(final T event) {
		if(event == null && !mNullAllowed) {
			throw new NullPointerException();
		}
		final Set<EventReceiver<T>> snapshot = mReceivers;
		if(snapshot != null) {
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
	}

	/**
	 * Dispatches an event only if the receiver is registered.  Unregisters
	 * the receiver if it throws an exception.
	 *
	 * @param receiver the receiver
	 * @param event the event to dispatch
	 */
	protected void dispatch(final EventReceiver<T> receiver, final T event) {
		if(mReceivers != null && mReceivers.contains(receiver)) {
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
