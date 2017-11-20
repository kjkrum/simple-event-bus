package com.chalcodes.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * A synchronous multicast op that retains a reference to the last event
 * emitted and delivers it to any subsequently registered receiver. This class
 * is not thread safe.
 *
 * @author Kevin Krumwiede
 */
public class StickyEventBus<E> extends SimpleEventBus<E> implements StickyOp<E, E> {
	private E mEvent;

	public StickyEventBus(@Nonnull final Collection<Receiver<? super E>> receivers,
	                      @Nullable final ExceptionHandler<E> exceptionHandler) {
		super(receivers, exceptionHandler);
	}

	public StickyEventBus() {
		super();
	}

	@Override
	public boolean register(@Nonnull final Receiver<? super E> receiver) {
		final boolean changed = super.register(receiver);
		if(changed && mEvent != null) {
			dispatch(receiver, mEvent);
		}
		return changed;
	}

	@Override
	public void onEvent(@Nonnull final E event) {
		mEvent = event;
		super.onEvent(event);
	}

	@Override
	public void removeEvents() {
		mEvent = null;
	}
}
