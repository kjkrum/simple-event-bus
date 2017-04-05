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
* The stuck event can be read, written, and cleared.

# In practice
On Android, `simple-event-bus` competes with `LocalBroadcastManager`.  Advantages of `simple-event-bus` include sticky broadcasts, improved type safety over [stringly typed](http://wiki.c2.com/?StringlyTyped) `Intent` extras, direct broadcast of references without the overhead of marshalling and unmarshalling, and the ability to broadcast objects that do not implement `Parcelable` or `Serializable`.

Maximizing type safety tends to call for a large number of event buses.  For example, a monolithic `BroadcastReceiver` or `OnSharedPreferenceChangeListener` might be replaced by separate buses and receivers for each action or key. The proliferation of receivers inspired the introduction of `RegistrationHelper` to manage them. But `simple-event-bus` does not force type safety on you. If you prefer, you can broadcast actions or preference keys on an `EventBus<String>` with monolithic receivers.

The ability to broadcast objects that are not `Parcelable` or `Serializable` means that events can potentially leak things like `Context`. This danger is minimized by being mindful of the scope of references in events, especially implicit references in anonymous classes. For stricter control, use `final` or instance-controlled event types to ensure that events cannot contain undesirable references.


  
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

Creating a bus with default options:
```java
final EventBus<Foo> fooBus = new SimpleEventBus<>(executor);
```

Creating a bus with advanced options:
```java
final EventPipeline<RuntimeException> exceptionPipeline =
        new SimpleEventBus<>(executor);
final EventBus<Foo> fooBus =
        new StickyEventBus<>(executor, ReceiverSetFactories.treeSetFactory(),
                UncaughtExceptionHandlers.reportFactory(exceptionPipeline));
```

Basic usage:
```java
// in executor thread
fooBus.register(new EventReceiver<Foo> {
    @Override
    onEvent(@Nonnull final EventBus<Foo> bus, @Nonnull final Foo event) {
        // handle event
    }
});
// in any thread
fooBus.broadcast(new Foo(42));
```

A sticky bus can be wired to a value in persistent storage so the rest of the application can interact with the value exclusively through the bus.  Any translation to and from types supported by the storage can be restricted to this bit of code.
```java
final StickyEventBus<Foo> bus = new StickyEventBus<>(executor);
bus.register(new EventReceiver<Foo>() {
	@Override
	public void onEvent(@NonNull final EventBus<Foo> bus, @NonNull final Foo event) {
		/* persist Foo */
	}
});
bus.setEvent(/* load Foo */);
```