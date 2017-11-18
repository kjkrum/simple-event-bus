package com.chalcodes.event;

import javax.annotation.Nonnull;

/**
 * TODO javadoc
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
