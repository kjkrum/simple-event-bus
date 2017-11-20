package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps classes to lazily initialized ops. This class is not thread safe.
 *
 * @author Kevin Krumwiede
 */
public class ClassOpMap {
	private final OpFactory mOpFactory;
	private final Map<Class, Op> mCache = new HashMap<Class, Op>();

	public ClassOpMap(@Nonnull final OpFactory<?, ?> opFactory) {
		mOpFactory = opFactory;
	}

	@Nonnull public <E> Op<E, E> getOp(@Nonnull Class<E> klass) {
		Op op = mCache.get(klass);
		if(op == null) {
			op = mOpFactory.createOp();
			mCache.put(klass, op);
		}
		//noinspection unchecked
		return op;
	}
}
