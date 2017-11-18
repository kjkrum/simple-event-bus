package com.chalcodes.event;

import com.chalcodes.util.CollectionFactory;

/**
 * TODO javadoc
 *
 * @author Kevin Krumwiede
 */
public class StickyBusFactory<E> extends SimpleBusFactory<E> implements StickyOpFactory<E, E> {
	public StickyBusFactory(final CollectionFactory collectionFactory, final ExceptionHandler<E> exceptionHandler) {
		super(collectionFactory, exceptionHandler);
	}

	@Override
	public StickyOp<E, E> createOp() {
		return new StickyEventBus<E>(mCollectionFactory.<Receiver<? super E>>create(), mExceptionHandler);
	}
}
