package com.chalcodes.event;

import java.util.concurrent.Executor;

/**
 * Contains a static event bus factory.  This class is thread-safe.
 *
 * @author Kevin Krumwiede
 */
public class SimpleEventBus {
    private static final Object gLock = new Object();
    private static ClassBusFactory gFactory;

    /**
     * Initializes the static bus factory.
     *
     * @param executor the broadcast executor
     * @param exceptionBus the exception bus; may be null
     * @param sticky true if buses should be sticky; otherwise false
     * @param nullAllowed true if buses should allow null events; otherwise false
     * @throws IllegalStateException if the factory is already initialized
     */
    public static void init(final Executor executor, final EventBus<Exception> exceptionBus,
                            final boolean sticky, final boolean nullAllowed) {
        synchronized(gLock) {
            if (gFactory != null) {
                throw new IllegalStateException();
            }
            gFactory = new ClassBusFactory(executor, exceptionBus, sticky, nullAllowed);
        }
    }

    /**
     * Gets the event bus for a class.
     *
     * @param klass the class
     * @param <T> the event type
     * @return the event bus
     * @throws IllegalStateException if the factory has not been initialized
     * @see #init(Executor, EventBus, boolean, boolean)
     */
    public static <T> EventBus<T> getBus(final Class<T> klass) {
        synchronized(gLock) {
            if (gFactory == null) {
                throw new IllegalStateException();
            }
            return gFactory.getBus(klass);
        }
    }

    private SimpleEventBus() {}
}
