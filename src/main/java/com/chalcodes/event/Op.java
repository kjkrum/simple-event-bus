package com.chalcodes.event;

/**
 * Processes events. An op may emit zero or more events for each event it
 * receives.
 *
 * @author Kevin Krumwiede
 */
public interface Op<I, O> extends Receiver<I>, Emitter<O> {}
