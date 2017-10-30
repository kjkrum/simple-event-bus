package com.chalcodes.util;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Common collection factories.
 *
 * @author Kevin Krumwiede
 */
public class CollectionFactories {
	CollectionFactories() {}

	private static final CollectionFactory ARRAY_LIST = new CollectionFactory() {
		@Nonnull
		@Override
		public <E> Collection<E> copy(@Nonnull final Collection<E> original) {
			return new ArrayList<E>(original);
		}
	};

	public static CollectionFactory arrayList() {
		return ARRAY_LIST;
	}

	// TODO more
}
