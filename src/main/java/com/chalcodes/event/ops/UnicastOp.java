package com.chalcodes.event.ops;

import com.chalcodes.event.Op;
import com.chalcodes.event.Receiver;

import javax.annotation.Nonnull;

/**
 * TODO javadoc
 *
 * @author Kevin Krumwiede
 */
abstract public class UnicastOp<I, O> implements Op<I, O> {
	protected Receiver<? super O> mReceiver;

	@Override
	public boolean register(@Nonnull final Receiver<? super O> receiver) {
		if(mReceiver == null) {
			mReceiver = receiver;
			return true;
		}
		return false;
	}

	@Override
	public boolean unregister(@Nonnull final Receiver<? super O> receiver) {
		if(receiver.equals(mReceiver)) {
			mReceiver = null;
			return true;
		}
		return false;
	}
}
