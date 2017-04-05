package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Factory methods for common implementations of {@link ReceiverSetFactory}.
 *
 * @author Kevin Krumwiede
 */
public class ReceiverSetFactories {
	private ReceiverSetFactories() { /* Non-instantiable. */ }

	private static final ReceiverSetFactory HASH_SET_FACTORY = new ReceiverSetFactory() {
		@Nonnull
		@Override
		public <T> Set<EventReceiver<T>> copy(@Nonnull final Set<EventReceiver<T>> current) {
			return new HashSet<EventReceiver<T>>(current);
		}
	};

	/**
	 * Produces a receiver set factory that creates {@link HashSet}s.
	 * Receivers will be called in an unspecified order.
	 *
	 * @return the receiver set factory
	 */
	public static ReceiverSetFactory hashSetFactory() {
		return HASH_SET_FACTORY;
	}

	private static final ReceiverSetFactory LINKED_HASH_SET_FACTORY = new ReceiverSetFactory() {
		@Nonnull
		@Override
		public <T> Set<EventReceiver<T>> copy(@Nonnull final Set<EventReceiver<T>> current) {
			return new LinkedHashSet<EventReceiver<T>>(current);
		}
	};

	/**
	 * Produces a receiver set factory that creates {@link LinkedHashSet}s.
	 * Receivers will be called in the order they were registered.
	 *
	 * @return the receiver set factory
	 */
	public static ReceiverSetFactory linkedHashSetFactory() {
		return LINKED_HASH_SET_FACTORY;
	}

	private static final ReceiverSetFactory TREE_SET_FACTORY = new ReceiverSetFactory() {
		@Nonnull
		@Override
		public <T> Set<EventReceiver<T>> copy(@Nonnull final Set<EventReceiver<T>> current) {
			return new TreeSet<EventReceiver<T>>(current);
		}
	};

	/**
	 * Produces a receiver set factory that creates {@link TreeSet}s.
	 * Receivers will be called in their natural order.  For this to work
	 * properly, all receivers registered on the bus must be mutually {@link
	 * Comparable}, and their ordering must be consistent with equals.
	 *
	 * @return the receiver set factory
	 */
	public static ReceiverSetFactory treeSetFactory() {
		return TREE_SET_FACTORY;
	}

	/**
	 * Gets the suggested default implementation.
	 *
	 * @return the default implementation
	 */
	public static ReceiverSetFactory defaultFactory() {
		return hashSetFactory();
	}
}
