package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * A factory for per-class event buses.  This class is thread-safe.
 *
 * @author Kevin Krumwiede
 */
public class ClassBusFactory {
	private final Map<Class<?>, EventBus<?>> mBuses = new HashMap<Class<?>, EventBus<?>>();
	private final BusFactory<?> mFactory;

	public ClassBusFactory(@Nonnull final BusFactory<?> factory) {
		// noinspection ConstantConditions
		if(factory == null) {
			throw new NullPointerException();
		}
		mFactory = factory;
	}

	/**
	 * Gets or creates a bus for events of the specified class.  Always
	 * returns the same instance for the same argument.
	 *
	 * @param klass the class of the event type
	 * @param <T> the event type
	 * @return the event bus
	 */
	public <T> EventBus<T> getBus(@Nonnull final Class<T> klass) {
		// noinspection ConstantConditions
		if(klass == null) {
			throw new NullPointerException();
		}
		synchronized(mBuses) {
			if(mBuses.containsKey(klass)) {
				//noinspection unchecked
				return (EventBus<T>) mBuses.get(klass);
			} else {
				final EventBus<?> bus = mFactory.newBus();
				mBuses.put(klass, bus);
				//noinspection unchecked
				return (EventBus<T>) bus;
			}
		}
	}
}
