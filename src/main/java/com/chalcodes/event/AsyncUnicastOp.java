package com.chalcodes.event;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

/**
 * TODO javadoc
 *
 * @author Kevin Krumwiede
 */
abstract public class AsyncUnicastOp<I, O> extends UnicastOp<I, O> {
	@Nonnull protected final Executor mExecutor;

	protected AsyncUnicastOp(@Nonnull final Executor executor) {
		mExecutor = executor;
	}
}
