package com.chalcodes.util.test;

import com.chalcodes.util.CollectionFactories;
import com.chalcodes.util.CopyOnWriteCollection;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CopyOnWriteCollectionTest {
	private Collection<Integer> mCopyOnWriteCollection = new CopyOnWriteCollection<Integer>(CollectionFactories.hashSet());

	@Test
	public void iterator() {
		mCopyOnWriteCollection.addAll(Arrays.asList(0, 1, 2));
		final boolean[] returned = new boolean[3];
		for(final Integer i : mCopyOnWriteCollection) {
			returned[i] = true;
		}
		for(final boolean b : returned) {
			assertTrue(b);
		}
	}

	/* Interleaved iteration and modification demonstrating copy on write behavior of each mutator. */

	@Test
	public void add() throws Exception {
		Iterator<Integer> iter = mCopyOnWriteCollection.iterator();
		// trigger copy
		mCopyOnWriteCollection.add(0);
		// existing iterator unaffected
		int count = 0;
		while(iter.hasNext()) {
			iter.next();
			++count;
		}
		assertEquals(0, count);
		// new iterator reflects add
		iter = mCopyOnWriteCollection.iterator();
		while(iter.hasNext()) {
			iter.next();
			++count;
		}
		assertEquals(1, count);
	}

	@Test
	public void addAll() throws Exception {
		Iterator<Integer> iter = mCopyOnWriteCollection.iterator();
		// trigger copy
		mCopyOnWriteCollection.addAll(Collections.singleton(0));
		// existing iterator unaffected
		int count = 0;
		while(iter.hasNext()) {
			iter.next();
			++count;
		}
		assertEquals(0, count);
		// new iterator reflects add
		iter = mCopyOnWriteCollection.iterator();
		while(iter.hasNext()) {
			iter.next();
			++count;
		}
		assertEquals(1, count);
	}

	@Test
	public void remove() throws Exception {
		mCopyOnWriteCollection.add(0);
		Iterator<Integer> iter = mCopyOnWriteCollection.iterator();
		// trigger copy
		mCopyOnWriteCollection.remove(0);
		// existing iterator unaffected
		int count = 0;
		while(iter.hasNext()) {
			iter.next();
			++count;
		}
		assertEquals(1, count);
		// new iterator reflects remove
		iter = mCopyOnWriteCollection.iterator();
		count = 0;
		while(iter.hasNext()) {
			iter.next();
			++count;
		}
		assertEquals(0, count);
	}

	@Test
	public void removeAll() throws Exception {
		mCopyOnWriteCollection.addAll(Arrays.asList(0, 1, 2));
		Iterator<Integer> iter = mCopyOnWriteCollection.iterator();
		// trigger copy
		mCopyOnWriteCollection.removeAll(Collections.singleton(0));
		// existing iterator unaffected
		int count = 0;
		while(iter.hasNext()) {
			iter.next();
			++count;
		}
		assertEquals(3, count);
		// new iterator reflects remove
		iter = mCopyOnWriteCollection.iterator();
		count = 0;
		while(iter.hasNext()) {
			iter.next();
			++count;
		}
		assertEquals(2, count);
	}

	@Test
	public void retainAll() throws Exception {
		mCopyOnWriteCollection.addAll(Arrays.asList(0, 1, 2));
		Iterator<Integer> iter = mCopyOnWriteCollection.iterator();
		// trigger copy
		mCopyOnWriteCollection.retainAll(Collections.singleton(0));
		// existing iterator unaffected
		int count = 0;
		while(iter.hasNext()) {
			iter.next();
			++count;
		}
		assertEquals(3, count);
		// new iterator reflects retain
		iter = mCopyOnWriteCollection.iterator();
		count = 0;
		while(iter.hasNext()) {
			iter.next();
			++count;
		}
		assertEquals(1, count);
	}

	@Test
	public void clear() throws Exception {
		mCopyOnWriteCollection.add(0);
		Iterator<Integer> iter = mCopyOnWriteCollection.iterator();
		// trigger copy
		mCopyOnWriteCollection.clear();
		// existing iterator unaffected
		int count = 0;
		while(iter.hasNext()) {
			iter.next();
			++count;
		}
		assertEquals(1, count);
		// new iterator reflects clear
		iter = mCopyOnWriteCollection.iterator();
		count = 0;
		while(iter.hasNext()) {
			iter.next();
			++count;
		}
		assertEquals(0, count);
	}
}