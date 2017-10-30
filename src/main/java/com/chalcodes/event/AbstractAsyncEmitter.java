package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

/**
 * Abstract base class for asynchronous unicast emitters.
 *
 * @author Kevin Krumwiede
 */
abstract public class AbstractAsyncEmitter<E> extends AbstractEmitter<E> {
	@Nonnull protected final Executor mExecutor;

	protected AbstractAsyncEmitter(@Nonnull final Executor executor) {
		mExecutor = executor;
	}
}
