package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Abstract base class for synchronous unicast emitters.
 *
 * @author Kevin Krumwiede
 */
abstract public class AbstractEmitter<E> implements Emitter<E> {
	protected Receiver<? super E> mReceiver;

	@Override
	public boolean register(@Nonnull final Receiver<? super E> receiver) {
		if(mReceiver == null) {
			mReceiver = receiver;
			return true;
		}
		return false;
	}

	@Override
	public boolean unregister(@Nonnull final Receiver<? super E> receiver) {
		if(mReceiver != null && mReceiver.equals(receiver)) {
			mReceiver = null;
			return true;
		}
		return false;
	}
}
