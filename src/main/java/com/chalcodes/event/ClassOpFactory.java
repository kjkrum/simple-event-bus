package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates ops by class.
 *
 * @author Kevin Krumwiede
 */
public class ClassOpFactory {
	private final OpFactory mOpFactory;
	private final Map<Class, Op> mCache = new HashMap<Class, Op>();

	public ClassOpFactory(@Nonnull final OpFactory<?, ?> opFactory) {
		mOpFactory = opFactory;
	}

	public boolean hasOp(Class<?> klass) {
		return mCache.containsKey(klass);
	}

	public <E> Op<E, E> getOp(Class<E> klass) {
		//noinspection unchecked
		Op<E, E> op = (Op<E, E>) mCache.get(klass);
		if(op == null) {
			//noinspection unchecked
			op = (Op<E, E>) mOpFactory.createOp();
			mCache.put(klass, op);
		}
		return op;
	}
}
