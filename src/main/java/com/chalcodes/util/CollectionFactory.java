package com.chalcodes.util;

import java.util.Collection;

/**
 * TODO javadoc
 *
 * @author Kevin Krumwiede
 */
public interface CollectionFactory {
	<E> Collection<E> copy(Collection<E> original);
}
