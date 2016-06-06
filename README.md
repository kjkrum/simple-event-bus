# simple-event-bus
A type-safe event publication API designed for Android, Swing, and other Java frameworks.  Key classes include the `EventBus` interface and its two concrete implementations, `SimpleEventBus` and `StickyEventBus`.
  
The author is using `simple-event-bus` in a non-trivial Android project, and will declare the API stable after using it in at least one other project.

## `SimpleEventBus`
* Events may be broadcast from any thread.
* Events may be synchronously filtered when broadcast.  For example, buses may prohibit null events.
* Events are dispatched by a single-threaded `Executor`.  The executor may simply delegate to the framework's main thread.
* **Receivers should be registered and unregistered only in the executor thread.**
* The collection of receivers is a set, not a list.
* Events are dispatched to every receiver that was registered when the event was broadcast, and which is still registered when the event is delivered.
* Each bus may have an exception bus on which it broadcasts runtime exceptions thrown by receivers.
* Any receiver that throws an exception is immediately unregistered.
* Any number of buses can share the same executor and exception bus.

## `StickyEventBus`
* Stickiness is a property of the bus, not a property of events.
* A sticky bus retains a strong reference to the last event broadcast.
* The sticky event is dispatched to newly registered receivers.
* The sticky event can be cleared.  
  
# Examples
A typical executor:
```java
public class AndroidExecutor implements Executor {
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(final Runnable command) {
        mHandler.post(command);
    }
}
```

Basic usage:
```java
// in any thread
final Executor executor = new AndroidExecutor();
final EventBus<Exception> exceptionBus =
        new SimpleEventBus<>(executor, null, null);
final EventBus<Foo> fooBus =
        new StickyEventBus<>(executor, exceptionBus, EventFilters.<Foo>discardNull());
// in main thread
fooBus.register(new EventReceiver<Foo> {
    @Override
    onEvent(@Nonnull final EventBus<Foo> bus, final Foo event) {
        // ...
    }
});
// in any thread
fooBus.broadcast(new Foo(42));
```
`mFooReceiver` will receive the event if and only if `fooBus` is sticky:
```java
// in main thread
fooBus.broadcast(new Foo(888));
fooBus.register(mFooReceiver);
```
`mFooReceiver` is guaranteed *not* to receive the event:
```java
// in main thread
fooBus.broadcast(new Foo(13013));
fooBus.unregister(mFooReceiver);
```
This guarantee is especially important on Android, where a delayed call to an unregistered receiver could easily lead to a crash.