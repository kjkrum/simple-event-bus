package com.chalcodes.event.ops;

import com.chalcodes.event.AbstractEmitter;
import com.chalcodes.event.StickyOp;

import javax.annotation.Nonnull;

/**
 * Delivers events that are not equal to the previous event.
 *
 * @author Kevin Krumwiede
 */
public class Changed<E> extends AbstractEmitter<E> implements StickyOp<E, E> {
	private E mLastEmitted;

	@Override
	public void onEvent(@Nonnull final E event) {
		if(!event.equals(mLastEmitted)) {
			mLastEmitted = event;
			mReceiver.onEvent(event);
		}
	}

	@Override
	public void removeEvents() {
		mLastEmitted = null;
	}
}
