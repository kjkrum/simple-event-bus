package com.chalcodes.util;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Common collection factories.
 *
 * @author Kevin Krumwiede
 */
public class CollectionFactories {
	CollectionFactories() {}

	private static final CollectionFactory HASH_SET = new CollectionFactory() {
		@Nonnull
		@Override
		public <E> Collection<E> create() {
			return new HashSet<E>();
		}

		@Nonnull
		@Override
		public <E> Collection<E> createFrom(@Nonnull final Collection<E> original) {
			return new HashSet<E>(original);
		}
	};

	/**
	 * Returns a collection factory that creates instances of {@link HashSet}.
	 *
	 * @return a {@link HashSet} factory
	 */
	@Nonnull public static CollectionFactory hashSet() {
		return HASH_SET;
	}

	private static final CollectionFactory LINKED_HASH_SET = new CollectionFactory() {
		@Nonnull
		@Override
		public <E> Collection<E> create() {
			return new LinkedHashSet<E>();
		}

		@Nonnull
		@Override
		public <E> Collection<E> createFrom(@Nonnull final Collection<E> original) {
			return new LinkedHashSet<E>(original);
		}
	};

	/**
	 * Returns a collection factory that creates instances of {@link
	 * LinkedHashSet}.
	 *
	 * @return a {@link LinkedHashSet} factory
	 */
	@Nonnull public static CollectionFactory linkedHashSet() {
		return LINKED_HASH_SET;
	}

	private static final CollectionFactory TREE_SET = new CollectionFactory() {
		@Nonnull
		@Override
		public <E> Collection<E> create() {
			return new TreeSet<E>();
		}

		@Nonnull
		@Override
		public <E> Collection<E> createFrom(@Nonnull final Collection<E> original) {
			return new TreeSet<E>(original);
		}
	};

	/**
	 * Returns a collection factory that creates instances of {@link TreeSet}.
	 *
	 * @return a {@link TreeSet} factory
	 */
	@Nonnull public static CollectionFactory treeSet() {
		return TREE_SET;
	}

	private static final CollectionFactory COPY_ON_WRITE_ARRAY_LIST = new CollectionFactory() {
		@Nonnull
		@Override
		public <E> Collection<E> create() {
			return new CopyOnWriteArrayList<E>();
		}

		@Nonnull
		@Override
		public <E> Collection<E> createFrom(@Nonnull final Collection<E> original) {
			return new CopyOnWriteArrayList<E>(original);
		}
	};

	/**
	 * Returns a collection factory that creates instances of {@link
	 * CopyOnWriteArrayList}. This is for use in other factories. Using this
	 * as the collection factory of a {@link CopyOnWriteCollection} would be
	 * redundant.
	 *
	 * @return a {@link CopyOnWriteArrayList} factory
	 */
	@Nonnull public static CollectionFactory copyOnWriteArrayList() {
		return COPY_ON_WRITE_ARRAY_LIST;
	}

	private static final CollectionFactory COPY_ON_WRITE_ARRAY_SET = new CollectionFactory() {
		@Nonnull
		@Override
		public <E> Collection<E> create() {
			return new CopyOnWriteArraySet<E>();
		}

		@Nonnull
		@Override
		public <E> Collection<E> createFrom(@Nonnull final Collection<E> original) {
			return new CopyOnWriteArraySet<E>(original);
		}
	};

	/**
	 * Returns a collection factory that creates instances of {@link
	 * CopyOnWriteArraySet}. This is for use in other factories. Using this
	 * as the collection factory of a {@link CopyOnWriteCollection} would be
	 * redundant.
	 *
	 * @return a {@link CopyOnWriteArraySet} factory
	 */
	@Nonnull public static CollectionFactory copyOnWriteArraySet() {
		return COPY_ON_WRITE_ARRAY_SET;
	}

	/**
	 * Returns a collection factory that creates instances of {@link
	 * CopyOnWriteCollection} that use the specified collection factory. This
	 * is for use in other factories. Using this as the collection factory of
	 * a {@link CopyOnWriteCollection} would be redundant.
	 *
	 * @return a {@link CopyOnWriteCollection} factory
	 */
	@Nonnull public static CollectionFactory copyOnWriteCollection(@Nonnull final CollectionFactory factory) {
		return new CollectionFactory() {
			@Nonnull
			@Override
			public <E> Collection<E> create() {
				return new CopyOnWriteCollection<E>(factory);
			}

			@Nonnull
			@Override
			public <E> Collection<E> createFrom(@Nonnull final Collection<E> original) {
				return new CopyOnWriteCollection<E>(factory);
			}
		};
	}
}
