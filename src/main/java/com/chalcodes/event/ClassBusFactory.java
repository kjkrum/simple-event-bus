package com.chalcodes.event;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * A factory for per-class event buses.  This class is thread-safe.
 *
 * @author Kevin Krumwiede
 */
public class ClassBusFactory {
    private final Map<Class<?>, EventBus<?>> mBuses = new HashMap<Class<?>, EventBus<?>>();
    private final Executor mExecutor;
    private final EventBus<Exception> mExceptionBus;
    private final boolean mSticky;
    private final boolean mNullAllowed;

    public ClassBusFactory(final Executor executor, final EventBus<Exception> exceptionBus,
                           final boolean sticky, final boolean nullAllowed) {
        if(executor == null) {
            throw new NullPointerException();
        }
        mExecutor = executor;
        mExceptionBus = exceptionBus;
        mSticky = sticky;
        mNullAllowed = nullAllowed;
    }

    @SuppressWarnings("unchecked")
    public <T> EventBus<T> getBus(Class<T> klass) {
        synchronized(mBuses) {
            if(mBuses.containsKey(klass)) {
                return (EventBus<T>) mBuses.get(klass);
            }
            else {
                final EventBus<T> bus = new EventBus(mExecutor, mExceptionBus, mSticky, mNullAllowed);
                mBuses.put(klass, bus);
                return bus;
            }
        }
    }
}
