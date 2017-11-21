package com.chalcodes.util;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Creates collections containing the elements of other collections. The new
 * collections may be of different types than the originals. Implementations
 * may even return different types depending on criteria such as size.
 *
 * @author Kevin Krumwiede
 */
public interface CollectionFactory {
	/**
	 * Creates a new empty collection.
	 *
	 * @param <E> the element type
	 * @return a new empty collection
	 */
	@Nonnull <E> Collection<E> create();

	/**
	 * Creates a new collection containing the elements of the specified
	 * collection, subject to uniqueness constraints of the new collection
	 * type.
	 *
	 * @param <E> the element type
	 * @param original the original collection
	 * @return a new collection containing the elements of {@code original}
	 */
	@Nonnull <E> Collection<E> createFrom(@Nonnull Collection<E> original);
}
