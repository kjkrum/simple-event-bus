package com.chalcodes.util;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Produces collections of a specific type.
 *
 * @author Kevin Krumwiede
 */
public interface CollectionFactory {
	/**
	 * Creates a new collection containing the elements of the specified
	 * collection, subject to uniqueness constraints of the new collection
	 * type.
	 *
	 * @param <E> the element type
	 * @param original the collection to copy
	 * @return a new collection containing the elements of {@code original}
	 */
	@Nonnull <E> Collection<E> copy(@Nonnull Collection<E> original);
	// TODO rename this from or createFrom?
}
