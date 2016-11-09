package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps classes to event buses.  This class is thread-safe.
 *
 * @author Kevin Krumwiede
 */
public class ClassBusFactory {
	private final Map<Class<?>, EventBus<?>> mBuses = new HashMap<Class<?>, EventBus<?>>();
	private final BusFactory<?> mFactory;

	/**
	 * Creates a class bus factory with initial mappings.  Other requested
	 * buses are created by the bus factory.
	 *
	 * @param factory the bus factory
	 * @param buses the initial mappings
	 * @throws NullPointerException if factory is null or any key or value in
	 * buses is null
	 */
	public ClassBusFactory(@Nonnull final BusFactory<?> factory,
	                       @Nullable final Map<Class<?>, EventBus<?>> buses) {
		// noinspection ConstantConditions
		if(factory == null) {
			throw new NullPointerException();
		}
		mFactory = factory;
		if(buses != null) {
			mBuses.putAll(buses);
			if(mBuses.containsKey(null) || mBuses.containsValue(null)) {
				throw new NullPointerException();
			}
		}
	}

	/**
	 * Creates a class bus factory with no initial mappings.  All requested
	 * buses are created by the bus factory.
	 *
	 * @param factory the bus factory
	 * @see #ClassBusFactory(BusFactory, Map)
	 */
	public ClassBusFactory(@Nonnull final BusFactory<?> factory) {
		this(factory, null);
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
