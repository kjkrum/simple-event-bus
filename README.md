# simple-event-bus
A type-safe event publication API designed for Android, Swing, and other Java frameworks.  Key classes include the `EventBus` interface and its concrete implementations, `SimpleEventBus` and `StickyEventBus`.  The API is small and highly composable.
 
As indicated by the 0.x version number, this library is in development and the API is subject to change.  The author has used `simple-event-bus` in several Android projects, and plans to declare version 1.0 after using it in at least one non-Android project.

## `SimpleEventBus`
* Events may be broadcast from any thread.
* Events are dispatched by a single-threaded `Executor`.  The executor may simply delegate to the framework's main thread.
* **Event receivers must be registered and unregistered in the executor thread.**
* The order in which receivers are called is determined by the iteration order of sets produced by a `ReceiverSetFactory`.
* Events are delivered to receivers that were registered when the event was broadcast.
* Events in flight will not be delivered to unregistered receivers.
* Runtime exceptions propagating from receivers are handled by an `UncaughtExceptionHandler`.

## `StickyEventBus`
All of the above, plus:

* **Stickiness is a property of the bus, not a property of events.**
* A sticky bus retains a strong reference to the last event broadcast.
* The stuck event is dispatched to all subsequently registered receivers.
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
final Executor executor = new AndroidExecutor();
final EventBus<Foo> fooBus =
        new StickyEventBus<>(executor);
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

Advanced usage:
```java
final Executor executor = new AndroidExecutor();
final EventBus<RuntimeException> exceptionBus =
        new SimpleEventBus<>(executor);
final EventBus<Foo> fooBus =
        new StickyEventBus<>(executor,
                ReceiverSetFactories.treeSetFactory(),
                UncaughtExceptionHandlers.report(exceptionBus));
```
