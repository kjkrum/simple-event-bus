package com.chalcodes.event;

import com.chalcodes.util.CollectionFactory;

/**
 * An {@code OpFactory} that creates instances of {@link StickyEventBus}.
 *
 * @author Kevin Krumwiede
 */
public class StickyBusFactory<E> implements OpFactory<E, E> {
	private final CollectionFactory mCollectionFactory;
	private final ExceptionHandler<E> mExceptionHandler;

	public StickyBusFactory(final CollectionFactory collectionFactory, final ExceptionHandler<E> exceptionHandler, final CollectionFactory collectionFactory1, final ExceptionHandler<E> exceptionHandler1) {
		mCollectionFactory = collectionFactory1;
		mExceptionHandler = exceptionHandler1;
	}

	@Override
	public StickyEventBus<E> createOp() {
		return new StickyEventBus<E>(mCollectionFactory.<Receiver<? super E>>create(), mExceptionHandler);
	}
}
