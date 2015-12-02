# simple-event-bus
A type-safe event bus for Android, Swing, and other Java frameworks.  The author is using `simple-event-bus` in a large Android project, and will declare the API stable after using it in at least one other project.

# Overview
Key classes include the `EventBus` interface and its two concrete implementations, `SimpleEventBus` and `StickyEventBus`.  A `StickyEventBus` retains the last event broadcast and dispatches it to any subsequently registered receiver.  Both types accept events from any thread and broadcast them using an `Executor` provided to their constructors.  The executor should be single-threaded, and all methods except `broadcast` should be called in the executor thread.  The library provides executors for Android, Swing, and JavaFx that simply queue their tasks in their respective frameworks' main threads.
  
Each `EventBus` has an optional exception bus on which receivers may broadcast caught exceptions, and on which it will broadcast any runtime exception propagating from a receiver.  More than one event bus can share the same exception bus.

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
The library includes a per-class event bus factory and a simple factory that produces instances of `SimpleEventBus` or `StickyEventBus` depending on its constructor arguments.
```java
final Executor executor = new AndroidExecutor();
final EventBus<Exception> exceptionBus =
        new EventBus<>(executor, null, false);
final BusFactory busFactory =
        new SimpleBusFactory(executor, exceptionBus, false, true);
final ClassBusFactory classBusFactory =
        new ClassBusFactory(busFactory);
final EventBus<Foo> fooBus = classBusFactory.getBus(Foo.class);
```
In this example, `mFooReceiver` will receive the event if and only if `fooBus` is a `StickyEventBus`.
```java
fooBus.broadcast(new Foo(888));
fooBus.register(mFooReceiver);
```
In this example, `mFooReceiver` is guaranteed not to receive the event.  This is especially important on Android, where a call to an unregistered receiver in a stopped activity could easily result in a crash.
```java
fooBus.broadcast(new Foo(13013));
fooBus.unregister(mFooReceiver);
```
