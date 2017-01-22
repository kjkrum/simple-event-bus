package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * An event bus that dispatches events using a single-threaded {@link
 * Executor}.  Receivers should be registered and unregistered only in the
 * executor thread.
 * <p>
 * This class is open to extension, but should be extended with caution.
 * In particular, note that {@link #broadcast(Object)} calls {@link
 * #dispatch(EventReceiver, Object)}, which may in turn call {@link
 * #unregister(EventReceiver)}.
 *
 * @param <T> the event type
 * @author Kevin Krumwiede
 */
public class SimpleEventBus<T> implements EventBus<T> {
	@Nonnull private final Executor mExecutor;
	@Nullable private final EventPipeline<Exception> mExceptionPipeline;
	@Nonnull private final ReceiverSetFactory<T> mReceiverSetFactory;
	@Nonnull private final UncaughtExceptionHandler<T> mUncaughtExceptionHandler;
	/**
	 * Copy-on-write collection of receivers.  The collection must never be
	 * modified via this reference.
	 */
	@Nonnull private volatile Set<EventReceiver<T>> mReceivers = Collections.emptySet();

	/**
	 * Creates a new event bus.  If an exception pipeline is provided, any
	 * exception thrown by a receiver will be broadcast on it.  The order in
	 * which receivers are called is determined by the iteration order of the
	 * sets produced by the receiver set factory.
	 * <p>
	 * Use custom receiver set factories with caution.  If the factory
	 * produces sets that violate the contract of {@link Set}, or leaks the
	 * receiver sets to other parts of the application, the internal state of
	 * the event bus may be compromised, leading to unspecified behavior.
	 *
	 * @param executor the broadcast executor
	 * @param exceptionPipeline the exception pipeline; may be null
	 * @param receiverSetFactory the receiver set factory
	 * @param uncaughtExceptionHandler the uncaught exception handler
	 * @throws NullPointerException if executor, receiverSetFactory, or
	 * uncaughtExceptionHandler is null
	 * @see ReceiverSetFactories
	 */
	public SimpleEventBus(@Nonnull final Executor executor,
						  @Nullable final EventPipeline<Exception> exceptionPipeline,
						  @Nonnull final ReceiverSetFactory<T> receiverSetFactory,
	                      @Nonnull final UncaughtExceptionHandler<T> uncaughtExceptionHandler) {
		//noinspection ConstantConditions - public API
		if(executor == null || receiverSetFactory == null || uncaughtExceptionHandler == null) {
			throw new NullPointerException();
		}
		mExecutor = executor;
		mExceptionPipeline = exceptionPipeline;
		mReceiverSetFactory = receiverSetFactory;
		mUncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	/**
	 * Creates a new event bus.  If an exception pipeline is provided, any
	 * exception thrown by a receiver will be broadcast on it.  The order in
	 * which receivers are called is determined by the iteration order of the
	 * sets produced by the receiver set factory.
	 * <p>
	 * Use custom receiver set factories with caution.  If the factory
	 * produces sets that violate the contract of {@link Set}, or leaks the
	 * receiver sets to other parts of the application, the internal state of
	 * the event bus may be compromised, leading to unspecified behavior.
	 *
	 * @param executor the broadcast executor
	 * @param exceptionPipeline the exception pipeline; may be null
	 * @param receiverSetFactory the receiver set factory
	 * @throws NullPointerException if executor or receiverSetFactory is null
	 * @see ReceiverSetFactories
	 */
	public SimpleEventBus(@Nonnull final Executor executor,
	                      @Nullable final EventPipeline<Exception> exceptionPipeline,
	                      @Nonnull final ReceiverSetFactory<T> receiverSetFactory) {
		this(executor, exceptionPipeline, receiverSetFactory, UncaughtExceptionHandlers.<T>unregisterAndReport());
	}

	/**
	 * Creates a new event bus.  If an exception pipeline is provided, any
	 * exception thrown by a receiver will be broadcast on it.
	 *
	 * @param executor the broadcast executor
	 * @param exceptionPipeline the exception pipeline; may be null
	 * @throws NullPointerException if executor is null
	 */
	public SimpleEventBus(@Nonnull final Executor executor,
						  @Nullable final EventPipeline<Exception> exceptionPipeline) {
		this(executor, exceptionPipeline, ReceiverSetFactories.<T>hashSetFactory());
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
	protected void dispatch(@Nonnull final EventReceiver<T> receiver, @Nonnull final T event) {
		if(mReceivers.contains(receiver)) {
			try {
				receiver.onEvent(this, event);
			} catch(RuntimeException e) {
				mUncaughtExceptionHandler.handle(this, receiver, e);
			}
		}
	}

	@Override
	public void report(@Nonnull final Exception exception) {
		if(mExceptionPipeline != null) {
			mExceptionPipeline.broadcast(exception);
		}
	}

	/**
	 * Executes a task on this bus's {@link Executor}.
	 *
	 * @param command the task to execute
	 */
	protected void exec(@Nonnull final Runnable command) {
		mExecutor.execute(command);
	}

}
