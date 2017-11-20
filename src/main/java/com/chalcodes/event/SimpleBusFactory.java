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
	private final CollectionFactory mCollectionFactory;
	private final ExceptionHandler<E> mExceptionHandler;

	public SimpleBusFactory(final CollectionFactory collectionFactory, final ExceptionHandler<E> exceptionHandler) {
		mCollectionFactory = collectionFactory;
		mExceptionHandler = exceptionHandler;
	}

	@Override
	public SimpleEventBus<E> createOp() {
		return new SimpleEventBus<E>(mCollectionFactory.<Receiver<? super E>>create(), mExceptionHandler);
	}
}
