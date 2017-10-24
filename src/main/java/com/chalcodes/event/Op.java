package com.chalcodes.event;

/**
 * TODO javadoc
 *
 * @author Kevin Krumwiede
 */
public interface Op<I, O> extends Receiver<I>, Emitter<O> {}
