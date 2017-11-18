package com.chalcodes.util;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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

	public static CollectionFactory hashSet() {
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

	public static CollectionFactory linkedHashSet() {
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

	public static CollectionFactory treeSet() {
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
	 * For op factories. Using this as the {@code CollectionFactory} of a
	 * {@link CopyOnWriteCollection} would be redundant.
	 *
	 * @return
	 */
	public static CollectionFactory copyOnWriteArrayList() {
		return COPY_ON_WRITE_ARRAY_LIST;
	}

	/**
	 * For op factories. Using this as the {@code CollectionFactory} of a
	 * {@link CopyOnWriteCollection} would be redundant.
	 *
	 * @return
	 */
	public static CollectionFactory copyOnWriteCollection(@Nonnull final CollectionFactory factory) {
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
