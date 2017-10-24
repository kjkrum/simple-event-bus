package com.chalcodes.event.ops;

import com.chalcodes.event.Emitter;
import com.chalcodes.event.ExceptionHandler;
import com.chalcodes.event.Receiver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

/**
 * Delivers each event in a separate task. This is intended for pushing events
 * to single threaded executor that needs to remain responsive, such as a UI
 * framework's main thread. If the executor is multithreaded, the order in
 * which events are delivered is indeterminate.
 *
 * @author Kevin Krumwiede
 */

// TODO document that this is single delivery
public class DeliverOn<E> extends AsyncUnicastOp<E,E> {

	public DeliverOn(@Nonnull final Executor executor,
	                 @Nullable final Receiver<? super RuntimeException> exceptionReceiver) {
		super(executor, exceptionReceiver);
	}

	public DeliverOn(@Nonnull final Executor executor) {
		this(executor, null);
	}

	@Override
	public void onEvent(@Nonnull final E event) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					mReceiver.onEvent(event);
				}
				catch(RuntimeException e) {
					if(mExceptionReceiver == null) {
						throw e;
					}
					mExceptionReceiver.onEvent(e);
				}
			}
		});
	}
}
