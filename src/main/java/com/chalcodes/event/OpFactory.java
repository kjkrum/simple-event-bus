package com.chalcodes.event;

/**
 * Creates ops.
 *
 * @author Kevin Krumwiede
 */
public interface OpFactory<I, O> {
	Op<I, O> createOp();
}
