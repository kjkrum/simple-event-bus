package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Associates {@link EventReceiver} registrations with arbitrary keys,
 * allowing all registrations associated with a key to be removed at once.
 * Holds weak references to keys, buses, and receivers.
 *
 * @param <K> the type of key
 *
 * @author Kevin Krumwiede
 */
public class RegistrationHelper<K> {
	private final Map<Object, Collection<Registration<?>>> mMap =
			new WeakHashMap<Object, Collection<Registration<?>>>();

	// TODO allow association of existing registrations?
	// multiple helpers (or even the same helper) could have multiple
	// associations of a registration with the same or different keys.
	// this is probably okay since registrations use weak references and
	// it's harmless to unregister a receiver that is not registered.
	// the only concern is possible confusion if multiple helpers have the
	// ability to unregister the same receiver. but it's ultimately up to the
	// client to appropriately limit the scope of references to receivers.

	/**
	 * Registers a receiver on an event bus and associates the registration
	 * with a key. Does nothing if the receiver is already registered.
	 *
	 * @param key the key to associate with the registration
	 * @param bus the event bus
	 * @param receiver an event receiver that is not registered
	 * @param <T> the event type of the bus and receiver
	 * @return true if registration was successful; otherwise false
	 */
	public <T> boolean register(@Nonnull final K key,
	                            @Nonnull final EventBus<T> bus,
	                            @Nonnull final EventReceiver<T> receiver) {
		final boolean registered = bus.register(receiver);
		if(registered) {
			Collection<Registration<?>> registrations = mMap.get(key);
			if(registrations == null) {
				registrations = new LinkedList<Registration<?>>();
				mMap.put(key, registrations);
			}
			registrations.add(new Registration<T>(bus, receiver));
		}
		return registered;
	}

	/**
	 * Removes all registrations associated with a key.
	 *
	 * @param key the key
	 */
	public void unregisterAll(@Nonnull final K key) {
		final Collection<Registration<?>> registrations = mMap.get(key);
		if(registrations != null) {
			for(final Registration reg : registrations) {
				reg.unregister();
			}
			mMap.remove(key);
		}
	}

	private static class Registration<T> {
		final WeakReference<EventBus<T>> mBus;
		final WeakReference<EventReceiver<T>> mReceiver;

		Registration(@Nonnull final EventBus<T> bus, @Nonnull final EventReceiver<T> receiver) {
			mBus = new WeakReference<EventBus<T>>(bus);
			mReceiver = new WeakReference<EventReceiver<T>>(receiver);
		}

		void unregister() {
			final EventBus<T> bus = mBus.get();
			final EventReceiver<T> receiver = mReceiver.get();
			if(bus != null && receiver != null) {
				bus.unregister(receiver);
			}
		}
	}
}
