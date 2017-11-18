package com.chalcodes.event;

/**
 * TODO javadoc
 *
 * @author Kevin Krumwiede
 */
public interface StickyOpFactory<I, O> extends OpFactory<I, O> {
	@Override
	StickyOp<I, O> createOp();
}
