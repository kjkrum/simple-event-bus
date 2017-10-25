package com.chalcodes.event.ops;

import com.chalcodes.event.AsyncUnicastOp;
import org.jctools.queues.MpscLinkedQueue;

import javax.annotation.Nonnull;
import java.util.AbstractQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO javadoc
 *
 * @author Kevin Krumwiede
 */
public final class QueueOn<E> extends AsyncUnicastOp<E, E> {
	private final AbstractQueue<E> mQueue = MpscLinkedQueue.newMpscLinkedQueue();
	private final AtomicInteger mCounter = new AtomicInteger();

	public QueueOn(@Nonnull final Executor executor) {
		super(executor);
	}

	/**
	 * Queues an event and calls {@link #drain()}. If this method throws any
	 * of the exceptions listed for {@link AbstractQueue#add(E)}, the event
	 * was not placed in the queue.
	 *
	 * @param event the event
	 */
	@Override
	public void onEvent(@Nonnull final E event) {
		mQueue.add(event);
		drain();
	}

	/**
	 * Executes a task to drain the queue, if such a task is not already
	 * running or pending. If this method throws {@link
	 * RejectedExecutionException}, events remain in the queue for possible
	 * delivery the next time this method is called. Whether this condition is
	 * recoverable depends on the executor.
	 */
	public void drain() {
		// see http://akarnokd.blogspot.com/2015/05/operator-concurrency-primitives_11.html
		if(mCounter.getAndIncrement() == 0) {
			try {
				mExecutor.execute(new Runnable() {
					@Override
					public void run() {
						do {
							mCounter.set(1);
							E event;
							while((event = mQueue.poll()) != null) {
								try {
									mReceiver.onEvent(event);
								}
								catch(RuntimeException e) {
									mCounter.set(0);
									throw e;
								}
							}
						}
						while(mCounter.decrementAndGet() != 0);
					}
				});
			}
			catch(RejectedExecutionException e) {
				mCounter.set(0);
				throw e;
			}
		}
	}
}
