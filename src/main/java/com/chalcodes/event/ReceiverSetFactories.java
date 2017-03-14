package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Static methods for producing receiver set factories.
 *
 * @author Kevin Krumwiede
 */
public class ReceiverSetFactories {
	// TODO remove 'Factory' from method names?

	private ReceiverSetFactories() { /* Non-instantiable. */ }

	private static final ReceiverSetFactory<Object> HASH_SET_FACTORY = new ReceiverSetFactory<Object>() {
		@Nonnull
		@Override
		public Set<EventReceiver<Object>> newSet(@Nonnull final Set<EventReceiver<Object>> current) {
			return new HashSet<EventReceiver<Object>>(current);
		}
	};

	/**
	 * Produces a receiver set factory that creates {@link HashSet}s.
	 * Receivers will be called in an unspecified order.
	 *
	 * @param <T> the event type
	 * @return the receiver set factory
	 */
	public static <T> ReceiverSetFactory<T> hashSetFactory() {
		//noinspection unchecked
		return (ReceiverSetFactory<T>) HASH_SET_FACTORY;
	}

	private static final ReceiverSetFactory<Object> LINKED_HASH_SET_FACTORY = new ReceiverSetFactory<Object>() {
		@Nonnull
		@Override
		public Set<EventReceiver<Object>> newSet(@Nonnull final Set<EventReceiver<Object>> current) {
			return new LinkedHashSet<EventReceiver<Object>>(current);
		}
	};

	/**
	 * Produces a receiver set factory that creates {@link LinkedHashSet}s.
	 * Receivers will be called in the order they were registered.
	 *
	 * @param <T> the event type
	 * @return the receiver set factory
	 */
	public static <T> ReceiverSetFactory<T> linkedHashSetFactory() {
		//noinspection unchecked
		return (ReceiverSetFactory<T>) LINKED_HASH_SET_FACTORY;
	}

	private static final ReceiverSetFactory<Object> TREE_SET_FACTORY = new ReceiverSetFactory<Object>() {
		@Nonnull
		@Override
		public Set<EventReceiver<Object>> newSet(@Nonnull final Set<EventReceiver<Object>> current) {
			return new LinkedHashSet<EventReceiver<Object>>(current);
		}
	};

	/**
	 * Produces a receiver set factory that creates {@link TreeSet}s.
	 * Receivers will be called in their natural order.  For this to work
	 * properly, all receivers registered on the bus must be mutually {@link
	 * Comparable}, and their ordering must be consistent with equals.
	 *
	 * @param <T> the event type
	 * @return the receiver set factory
	 */
	public static <T> ReceiverSetFactory<T> treeSetFactory() {
		//noinspection unchecked
		return (ReceiverSetFactory<T>) TREE_SET_FACTORY;
	}
}
