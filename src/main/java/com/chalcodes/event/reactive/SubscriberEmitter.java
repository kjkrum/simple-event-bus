package com.chalcodes.event.reactive;

import com.chalcodes.event.AbstractEmitter;
import com.chalcodes.event.Emitter;
import com.chalcodes.event.Receiver;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Connects a {@link Receiver} to a {@link Publisher}.
 *
 * @author Kevin Krumwiede
 */
public class SubscriberEmitter<T> extends AbstractEmitter<T> implements Subscriber<T> {
	/* Subscriber */
	private final Receiver<Throwable> mErrorReceiver;
	private Subscription mSubscription;

	public SubscriberEmitter(@Nonnull final Receiver<Throwable> errorReceiver) {
		mErrorReceiver = errorReceiver;
	}

	@Override
	public void onSubscribe(final Subscription subscription) {
		if(mSubscription == null) {
			mSubscription = subscription;
			subscription.request(Long.MAX_VALUE);
		}
		else {
			subscription.cancel();
		}
	}

	@Override
	public void onNext(final T t) {
		if(mReceiver != null) {
			try {
				mReceiver.onEvent(t);
			}
			catch(RuntimeException e) {
				mErrorReceiver.onEvent(e);
			}
		}
	}

	@Override
	public void onError(final Throwable throwable) {
		mSubscription = null;
		mErrorReceiver.onEvent(throwable);
	}

	@Override
	public void onComplete() {
		mSubscription = null;
	}

	/* Nothing to do for Emitter. */
}
