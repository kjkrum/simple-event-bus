package com.chalcodes.event;

import com.chalcodes.util.CollectionFactory;

import java.util.Collection;
import java.util.Collections;

/**
 * An {@code OpFactory} that creates instances of {@link SimpleEventBus}.
 *
 * @author Kevin Krumwiede
 */
public class SimpleBusFactory<E> implements OpFactory<E, E> {
	final CollectionFactory mCollectionFactory;
	final ExceptionHandler<E> mExceptionHandler;

	public SimpleBusFactory(final CollectionFactory collectionFactory, final ExceptionHandler<E> exceptionHandler) {
		mCollectionFactory = collectionFactory;
		mExceptionHandler = exceptionHandler;
	}

	@Override
	public Op<E, E> createOp() {
		return new SimpleEventBus<E>(mCollectionFactory.<Receiver<? super E>>create(), mExceptionHandler);
	}
}
