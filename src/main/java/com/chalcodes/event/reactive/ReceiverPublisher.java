package com.chalcodes.event.reactive;

import com.chalcodes.event.Emitter;
import com.chalcodes.event.Receiver;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import javax.annotation.Nonnull;

/**
 * Connects a {@link Subscriber} to an {@link Emitter}.
 *
 * @author Kevin Krumwiede
 */
public class ReceiverPublisher<T> implements Receiver<T>, Publisher<T> {
	/* Receiver */
	@Override
	public void onEvent(@Nonnull final T event) {
		if(mSubscriber != null) {
			mSubscriber.onNext(event);
		}
	}

	/* Publisher */
	private Subscriber<? super T> mSubscriber;
	private Subscription mSubscription;

	@Override
	public void subscribe(final Subscriber<? super T> subscriber) {
		if(mSubscriber == null) {
			mSubscriber = subscriber;
			mSubscription = new Subscription() {
				@Override
				public void request(final long demand) {
					/* Emitter has no concept of this. */
				}

				@Override
				public void cancel() {
					if(mSubscription == this) {
						mSubscription = null;
						mSubscriber = null;
					}
				}
			};
			mSubscriber.onSubscribe(mSubscription);
		}
		else if(subscriber.equals(mSubscriber)) {
			subscriber.onError(new Exception("already subscribed"));
			mSubscriber = null;
			mSubscription = null;
		}
		else {
			subscriber.onError(new Exception("subscription rejected"));
		}
	}
}
