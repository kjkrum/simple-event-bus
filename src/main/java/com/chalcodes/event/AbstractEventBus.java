package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * Package-private base for {@code public final} implementations.
 *
 * @author Kevin Krumwiede
 */
abstract class AbstractEventBus<T> implements EventBus<T> {
	@Nonnull private final Executor mExecutor;
	@Nonnull private final ReceiverSetFactory<T> mReceiverSetFactory;
	@Nonnull private final UncaughtExceptionHandler<T> mUncaughtExceptionHandler;
	/**
	 * Copy-on-write collection of receivers.  The collection must never be
	 * modified via this reference.
	 */
	@Nonnull private volatile Set<EventReceiver<T>> mReceivers = Collections.emptySet();

	AbstractEventBus(@Nonnull final Executor executor,
	                      @Nonnull final ReceiverSetFactory<T> receiverSetFactory,
	                      @Nonnull final UncaughtExceptionHandler<T> uncaughtExceptionHandler) {
		//noinspection ConstantConditions - public API
		if(executor == null || receiverSetFactory == null || uncaughtExceptionHandler == null) {
			throw new NullPointerException();
		}
		mExecutor = executor;
		mReceiverSetFactory = receiverSetFactory;
		mUncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	AbstractEventBus(@Nonnull final Executor executor) {
		this(executor, ReceiverSetFactories.<T>defaultFactory(), UncaughtExceptionHandlers.<T>defaultHandler());
	}

	@Override
	public boolean register(@Nonnull final EventReceiver<T> receiver) {
		//noinspection ConstantConditions - public API
		if(receiver == null) {
			throw new NullPointerException();
		}
		if(mReceivers.contains(receiver)) {
			return false;
		}
		else {
			final Set<EventReceiver<T>> tmp = mReceiverSetFactory.newSet(mReceivers);
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
			final Set<EventReceiver<T>> tmp = mReceiverSetFactory.newSet(mReceivers);
			tmp.remove(receiver);
			mReceivers = tmp;
			return true;
		}
		else {
			return false;
		}
	}

	/* Why not copy mReceivers and dispatch to copy.retainAll(mReceivers)?
	 * This would not be exactly the same as testing mReceivers.contains(...)
	 * for each receiver.  The difference is in what would happen if one
	 * receiver unregistered another receiver that is in the retained set
	 * but which has not yet been called.  The unregistered receiver would
	 * still be called, violating the contract of unregister(...). */

	@Override
	public void broadcast(@Nonnull final T event) {
		//noinspection ConstantConditions
		if(event == null) {
			throw new NullPointerException();
		}
		final Set<EventReceiver<T>> receivers = mReceivers;
		if(!receivers.isEmpty()) {
			mExecutor.execute(new Runnable() {
				@Override
				public void run() {
					for(final EventReceiver<T> receiver : receivers) {
						dispatch(receiver, event);
					}
				}
			});
		}
	}

	/**
	 * Dispatches an event only if the receiver is registered.
	 *
	 * @param receiver the receiver
	 * @param event the event to dispatch
	 */
	void dispatch(@Nonnull final EventReceiver<T> receiver, @Nonnull final T event) {
		if(mReceivers.contains(receiver)) {
			try {
				receiver.onEvent(this, event);
			} catch(RuntimeException e) {
				mUncaughtExceptionHandler.handle(this, receiver, e);
			}
		}
	}

	/**
	 * Executes a task on this bus's {@link Executor}.
	 *
	 * @param command the task to execute
	 */
	void exec(@Nonnull final Runnable command) {
		mExecutor.execute(command);
	}
}
