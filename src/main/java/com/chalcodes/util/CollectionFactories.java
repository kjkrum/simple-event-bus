package com.chalcodes.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO javadoc
 *
 * @author Kevin Krumwiede
 */
public class CollectionFactories {
	CollectionFactories() {}

	private static final CollectionFactory ARRAY_LIST = new CollectionFactory() {
		@Override
		public <E> Collection<E> copy(final Collection<E> original) {
			return new ArrayList<E>(original);
		}
	};

	public static CollectionFactory arrayList() {
		return ARRAY_LIST;
	}

	// TODO more
}
