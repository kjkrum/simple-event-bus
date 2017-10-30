package com.chalcodes.event.ops;

import com.chalcodes.event.AbstractAsyncEmitter;
import com.chalcodes.event.Op;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

/**
 * Delivers each event in a separate task. This is intended for pushing events
 * to single threaded executor that needs to remain responsive, such as a UI
 * framework's main thread. If the executor is multithreaded, the order in
 * which events are delivered is indeterminate.
 *
 * @author Kevin Krumwiede
 */
public class DeliverOn<E> extends AbstractAsyncEmitter<E> implements Op<E, E> {
	public DeliverOn(@Nonnull final Executor executor) {
		super(executor);
	}

	@Override
	public void onEvent(@Nonnull final E event) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
			mReceiver.onEvent(event);
			}
		});
	}
}
