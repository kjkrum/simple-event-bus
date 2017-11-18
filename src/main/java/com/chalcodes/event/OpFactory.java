package com.chalcodes.event;

/**
 * TODO javadoc
 *
 * @author Kevin Krumwiede
 */
public interface OpFactory<I, O> {
	Op<I, O> createOp();
}
