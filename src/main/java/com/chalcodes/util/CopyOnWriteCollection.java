package com.chalcodes.util;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * A {@link Collection} wrapper that copies its delegate on modification.
 *
 * @author Kevin Krumwiede
 */
public class CopyOnWriteCollection<E> implements Collection<E> {
	private final CollectionFactory mCopyFactory;
	private Collection<E> mCollection = Collections.emptyList();

	public CopyOnWriteCollection(final CollectionFactory copyFactory) {
		mCopyFactory = copyFactory;
	}

	public CopyOnWriteCollection() {
		this(CollectionFactories.arrayList());
	}

	@Override
	public boolean add(final E e) {
		final Collection<E> copy = mCopyFactory.copy(mCollection);
		final boolean changed = copy.add(e);
		if(changed) {
			mCollection = copy;
		}
		return changed;
	}

	@Override
	public boolean remove(final Object o) {
		final Collection<E> copy = mCopyFactory.copy(mCollection);
		final boolean changed = copy.remove(o);
		if(changed) {
			mCollection = copy;
		}
		return changed;
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		final Collection<E> copy = mCopyFactory.copy(mCollection);
		final boolean changed = copy.addAll(c);
		if(changed) {
			mCollection = copy;
		}
		return changed;
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		final Collection<E> copy = mCopyFactory.copy(mCollection);
		final boolean changed = copy.removeAll(c);
		if(changed) {
			mCollection = copy;
		}
		return changed;
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		final Collection<E> copy = mCopyFactory.copy(mCollection);
		final boolean changed = copy.retainAll(c);
		if(changed) {
			mCollection = copy;
		}
		return changed;
	}

	@Override
	public void clear() {
		mCollection = Collections.emptyList();
	}

	@Nonnull
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private final Iterator<E> iter = mCollection.iterator();

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public E next() {
				return iter.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/* Boring delegates. */

	@Override
	public int size() {
		return mCollection.size();
	}

	@Override
	public boolean isEmpty() {
		return mCollection.isEmpty();
	}

	@Override
	public boolean contains(final Object o) {
		return mCollection.contains(o);
	}

	@Nonnull
	@Override
	public Object[] toArray() {
		return mCollection.toArray();
	}

	@Nonnull
	@Override
	public <T> T[] toArray(@Nonnull final T[] a) {
		//noinspection SuspiciousToArrayCall - not sure if this is right
		return mCollection.toArray(a);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return mCollection.containsAll(c);
	}
}
