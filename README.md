# simple-event-bus
A type-safe event bus for Android, Swing, and other Java frameworks.  The author is using `simple-event-bus` in a large Android project, and will declare the API stable after using it in at least one other project.

# Key features
An `EventBus` receives events from any thread and broadcasts them using an `Executor` provided to its constructor.  The broadcast executor should be single-threaded, and all methods except `broadcast` should be called in the executor thread.  The library provides executors for Android, Swing, and JavaFx that simply queue their broadcasts in their respective frameworks' main threads.
  
Each `EventBus` has an optional exception bus on which receivers may broadcast caught exceptions, and on which it will broadcast any runtime exception propagating from a receiver.  If a bus is sticky, it retains the last event broadcast and dispatches it to any subsequently registered receiver.

# Examples
This example shows basic usage.  The use of `ClassBusFactory` is optional.  You can create your own factories or instantiate `EventBus` directly.
```java
final Executor executor = new AndroidExecutor();
final EventBus<Exception> exceptionBus =
    new EventBus<>(executor, null, false, false);
final ClassBusFactory factory =
    new ClassBusFactory(executor, exceptionBus, false, false);
final EventBus<Foo> fooBus = factory.getBus(Foo.class);
fooBus.register(new EventReceiver<Foo> {
  @Override
  onEvent(final EventBus<Foo> bus, final Foo event) {
    // ...
  }
});
fooBus.broadcast(new Foo(42));
```
In this example, the receiver will receive the event because broadcast is asynchronous but register is synchronous.  If the bus is sticky, the receiver will receive the event only once because it doesn't become stuck until it is dispatched.
```java
fooBus.broadcast(new Foo(69));
fooBus.register(new EventReceiver<Foo> {
  @Override
  onEvent(final EventBus<Foo> bus, final Foo event) {
    // ...
  }
});
```
In this example, the receiver will not receive the event because unregister is synchronous.  This is especially important on Android, where a call to an unregistered receiver in a stopped activity could crash the app.
```java
fooBus.broadcast(new Foo(13013));
fooBus.unregister(mFooReceiver);
```
