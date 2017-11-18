package com.chalcodes.event;

/**
 * Creates sticky ops.
 *
 * @author Kevin Krumwiede
 */
public interface StickyOpFactory<I, O> extends OpFactory<I, O> {
	@Override
	StickyOp<I, O> createOp();
}
