package com.chalcodes.event;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory for per-class event buses.  This class is thread-safe.
 *
 * @author Kevin Krumwiede
 */
public class ClassBusFactory {
	private final Map<Class<?>, EventBus<?>> mBuses = new HashMap<Class<?>, EventBus<?>>();
	private final BusFactory mFactory;

	public ClassBusFactory(final BusFactory factory) {
		if(factory == null) {
			throw new NullPointerException();
		}
		mFactory = factory;
	}

	@SuppressWarnings("unchecked")
	public <T> EventBus<T> getBus(Class<T> klass) {
		synchronized(mBuses) {
			if(mBuses.containsKey(klass)) {
				return (EventBus<T>) mBuses.get(klass);
			} else {
				final EventBus<T> bus = mFactory.newBus();
				mBuses.put(klass, bus);
				return bus;
			}
		}
	}
}
