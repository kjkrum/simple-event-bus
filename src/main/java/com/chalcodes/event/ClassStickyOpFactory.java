package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * Creates sticky ops by class.
 *
 * @author Kevin Krumwiede
 */
public class ClassStickyOpFactory extends ClassOpFactory {
	public ClassStickyOpFactory(@Nonnull final StickyOpFactory<?, ?> opFactory) {
		super(opFactory);
	}

	@Override
	public <E> StickyOp<E, E> getOp(final Class<E> klass) {
		return (StickyOp<E, E>) super.getOp(klass);
	}
}
