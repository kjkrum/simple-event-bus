# simple-event-bus
A type-safe event publication API designed for Android, Swing, and other Java frameworks.  Key classes include the `EventBus` interface and its two concrete implementations, `SimpleEventBus` and `StickyEventBus`. The entire API consists of around five interfaces and ten small, composable classes. The author is using `simple-event-bus` in a non-trivial Android project, and will declare the API stable after using it in at least one other project.

## `SimpleEventBus`
* Events may be broadcast from any thread.
* Events are dispatched by a single-threaded `Executor`.  The executor may simply delegate to the framework's main thread.
* **Event receivers should be registered and unregistered only in the executor thread.**
* Receivers are stored in sets, not lists.
* The order in which receivers are called may be controlled by constructing the event bus with a receiver set factory.
* Events are delivered to receivers that were registered when the event was broadcast, and which are still registered when the event is delivered.
* Each event bus may have an exception bus on which it broadcasts runtime exceptions thrown by its receivers.
* A receiver that throws an exception is immediately unregistered.
* Any number of buses can share the same executor, exception bus, and receiver set factory.

## `StickyEventBus`
All of the above, plus:

* Stickiness is a property of the bus, not a property of events.
* A sticky bus retains a strong reference to the last event broadcast.
* The stuck event is dispatched to subsequently registered receivers.
* The stuck event can be cleared.
  
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
        new SimpleEventBus<>(executor, null);
final EventBus<Foo> fooBus =
        new StickyEventBus<>(executor, exceptionBus);
// in main thread
fooBus.register(new EventReceiver<Foo> {
    @Override
    onEvent(@Nonnull final EventBus<Foo> bus, @Nonnull final Foo event) {
        // ...
    }
});
// in any thread
fooBus.broadcast(new Foo(42));
```
In this example, `mFooReceiver` will receive the event if and only if `fooBus` is sticky:
```java
// in any thread
fooBus.broadcast(new Foo(888));
// in main thread, sometime later
fooBus.register(mFooReceiver);
```
In this example, `mFooReceiver` is guaranteed not to receive the event:
```java
// in any thread
fooBus.broadcast(new Foo(13013));
// in main thread, before event is dispatched
fooBus.unregister(mFooReceiver);
```
This guarantee is especially important on Android, where a delayed call to an unregistered receiver could easily lead to a crash.
