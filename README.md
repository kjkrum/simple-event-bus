# simple-event-bus
A type-safe event publication API designed for Android, Swing, and other Java frameworks.  The author is using `simple-event-bus` in a large Android project, and will declare the API stable after using it in at least one other project.

# Overview
Key classes include the `EventBus` interface and its two concrete implementations, `SimpleEventBus` and `StickyEventBus`.  A `StickyEventBus` retains the last event broadcast and dispatches it to any subsequently registered receiver.  Each event bus has an optional exception bus on which receivers may broadcast caught exceptions, and on which the bus will broadcast any runtime exception propagating from its receivers.  Any number of buses can share the same exception bus.
  
`SimpleEventBus` and `StickyEventBus` accept events from any thread and broadcast them using an `Executor` provided to their constructors.  The executor should be single-threaded, and all methods except `broadcast` should be called in the executor thread.  Any number of buses can share the same `Executor`.  A typical pattern would be for the `Executor` to dispatch events on the framework's main thread.

```java
public class AndroidExecutor implements Executor {
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(final Runnable command) {
        mHandler.post(command);
    }
}
```

The `EventBus` interface doesn't preclude an implementation that supports a multi-threaded `Executor`.  The author simply hasn't needed one.

# Examples
This example shows basic usage.
```java
final Executor executor = new AndroidExecutor();
final EventBus<Exception> exceptionBus =
        new SimpleEventBus<>(executor, null, false);
final EventBus<Foo> fooBus =
        new StickyEventBus<>(executor, exceptionBus, false);
fooBus.register(new EventReceiver<Foo> {
    @Override
    onEvent(final EventBus<Foo> bus, final Foo event) {
        // ...
    }
});
fooBus.broadcast(new Foo(42));
```
In this example, `mFooReceiver` will receive the event if and only if `fooBus` is sticky.
```java
fooBus.broadcast(new Foo(888));
fooBus.register(mFooReceiver);
```
Assuming this example is itself running in the executor thread, `mFooReceiver` is guaranteed *not* to receive the event.  This is especially important on Android, where a call to an unregistered receiver in a stopped activity could easily result in a crash.
```java
fooBus.broadcast(new Foo(13013));
fooBus.unregister(mFooReceiver);
```
