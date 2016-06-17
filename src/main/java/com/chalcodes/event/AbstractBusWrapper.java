package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * A wrapper that forwards to the corresponding methods of another {@link
 * EventBus}.
 *
 * @author Kevin Krumwiede
 */
abstract public class AbstractBusWrapper<T> implements EventBus<T> {
	/**
	 * The wrapped event bus.
	 */
	@Nonnull protected final EventBus<T> mBus;

	/**
	 * Wraps an event bus.
	 *
	 * @param bus the event bus
	 * @throws NullPointerException if bus is null
	 */
	AbstractBusWrapper(@Nonnull final EventBus<T> bus) {
		//noinspection ConstantConditions
		if(bus == null) {
			throw new NullPointerException();
		}
		mBus = bus;
	}

	@Override
	public boolean register(@Nonnull final EventReceiver<T> receiver) {
		return mBus.register(receiver);
	}

	@Override
	public boolean unregister(@Nonnull final EventReceiver<T> receiver) {
		return mBus.unregister(receiver);
	}

	@Override
	public void broadcast(@Nonnull final T event) {
		mBus.broadcast(event);
	}

	@Override
	public void report(@Nonnull final Exception exception) {
		mBus.report(exception);
	}
}
