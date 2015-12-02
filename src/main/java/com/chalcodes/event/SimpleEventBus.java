package com.chalcodes.event;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

/**
 * A type-safe event bus.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public class SimpleEventBus<T> implements EventBus<T> {
	protected final Executor mExecutor;
	private final EventBus<Exception> mExceptionBus;
	private final boolean mNullAllowed;
	private final Set<EventReceiver<T>> mReceivers = new CopyOnWriteArraySet<EventReceiver<T>>();

	/**
	 * Creates a new event bus.  The executor should be single-threaded, and
	 * all methods except {@link #broadcast(Object)} should be called in the
	 * executor thread.  If an exception bus is provided, any exception thrown
	 * by a receiver will be broadcast on it.
	 *
	 * @param executor     the broadcast executor
	 * @param exceptionBus the exception bus; may be null
	 * @param nullAllowed  true if this bus should allow null events; otherwise
	 *                     false
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

	/**
	 * Gets the exception bus for this bus.
	 *
	 * @return the exception bus
	 */
	@Override
	public EventBus<Exception> getExceptionBus() {
		return mExceptionBus;
	}

	/**
	 * Immediately registers a receiver.
	 *
	 * @param receiver the receiver to register
	 */
	@Override
	public boolean register(final EventReceiver<T> receiver) {
		if(receiver == null) {
			throw new NullPointerException();
		}
		return mReceivers.add(receiver);
	}

	/**
	 * Immediately unregisters a receiver.  The receiver is guaranteed not to
	 * receive any pending event dispatch.
	 *
	 * @param receiver the receiver to unregister
	 */
	@Override
	public void unregister(final EventReceiver<T> receiver) {
		if(receiver == null) {
			throw new NullPointerException();
		}
		mReceivers.remove(receiver);
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
		mExecutor.execute(new Runnable() {
			final Iterator<EventReceiver<T>> iter = mReceivers.iterator();

			@Override
			public void run() {
				while(iter.hasNext()) {
					dispatch(iter.next(), event);
				}
			}
		});
	}

	protected void dispatch(final EventReceiver<T> receiver, final T event) {
		if(mReceivers.contains(receiver)) {
			try {
				receiver.onEvent(this, event);
			} catch(Exception e) {
				mReceivers.remove(receiver);
				if(mExceptionBus != null) {
					mExceptionBus.broadcast(e);
				}
			}
		}
	}

}
