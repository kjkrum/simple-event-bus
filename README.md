# Simple Event Bus
A type safe event publication API for Java.  As indicated by the 0.x version number, the API is in development and subject to change. Version 0.14 is substantially different from previous versions. See the [change log](CHANGES.md). As always, it's perfect this time for sure!

## Overview
The core API is tiny and highly composable. There are only three main interfaces: `Receiver<I>`, `Emitter<O>`, and their combination, `Op<I,O>`. Ops can emit zero or more events for each event they receive, so they can do just about anything.

### `SimpleEventBus`
* Synchronous multicast `Op<E,E>`.
* Receivers can be stored in any type of collection.
* Receiver priority, fairness, etc., is determined by the iteration order of the collection.
* Any receiver collection can be wrapped in a `CopyOnWriteCollection` so receivers can be unregistered during event delivery.
* Exception handling is composable. Misbehaving receivers can be prevented from breaking the iteration of the receiver collection, and can be removed or retained as required.

### `StickyEventBus`
All of the above, plus:

* Retains a reference to the last event emitted and delivers it to any subsequently registered receiver.
* The stuck event can be cleared.
* The stuck event can be set without notifying receivers. This has some clever applications.

## Compared to Reactive Streams
[Event buses are dead.](https://github.com/codepath/android_guides/issues/151) Long live event buses!

If you think Reactive Streams is cool but find its popular implementations a bit overwhelming, you're not alone. **Simple Event Bus** is far easier to understand and implement. There are no complex rules to follow when writing your own ops.

### Pushback

Reactive Streams has a concept called *pushback*. When subscribed, a subscriber will not receive events until it issues a request. Requests cannot be revoked, so a subscriber can only "push back" by not requesting more. In essence, push with pushback is a fancy name for potentially asynchronous pull.

The idea behind pushback was to allow subscribers to throttle the delivery of events. But this only makes sense when events are being generated internally (i.e., not in response to user input), events are buffered, and buffer capacity must be restricted. Otherwise, pushback has no advantage over a simple unbounded queue.

Some implementations of Reactive Streams don't even use pushback. Their subscribers request everything their publishers can send, so they degenerate to plain old push. Despite this, they've been used successfully in high performance applications. Pushback adds significant complexity for dubious value.

### Metadata

Reactive Streams subscribers have methods for signaling metadata like the end of a stream. **Simple Event Bus** receivers only have a single method to receive events. Though metadata callbacks are probably more useful than pushback, they still add complexity for a feature that is not always needed. If you need to signal metadata, make your event type a visitor on whatever interface you require.

### Features
RxJava includes far more operators than **Simple Event Bus**. I've only written a few that I personally needed. But again, they're very simple to add yourself. I'll add more as I need them, or when I feel like mining RxJava for ideas.

### Compatibility
If you're not sure whether **Simple Event Bus** or Reactive Streams better suits your needs, you can easily combine them later using classes in this library. The two APIs are conceptually similar enough that you could probably replace one with the other without too much trouble.

## Examples
**Simple Event Bus** uses ordinary executors instead of adding another layer of abstraction. You'll need to provide your own executor if you want to deliver events on some framework's main thread:

```java
public class AndroidExecutor implements Executor {
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(final Runnable command) {
        mHandler.post(command);
    }
}
```

An op like this could be created by a DI framework, then exposed to producers as a `Receiver` and to consumers as an `Emitter`:
```java
final Op<E,E> bus = Stream.deliverOn(new AndroidExecutor()).end(new SimpleEventBus<E>());
```

<!--

## In Practice

Maximizing type safety tends to call for a large number of event buses.  For example, a monolithic `BroadcastReceiver` or `OnSharedPreferenceChangeListener` might be replaced by separate buses and receivers for each action or key. The proliferation of receivers inspired the introduction of `RegistrationHelper` to manage them. But `simple-event-bus` does not force type safety on you. If you prefer, you can broadcast actions or preference keys on an `EventBus<String>` with monolithic receivers.

The ability to broadcast objects that are not `Parcelable` or `Serializable` means that events can potentially leak things like `Context`. This danger is minimized by being mindful of the scope of references in events, especially implicit references in anonymous classes. For stricter control, use `final` or instance-controlled event types to ensure that events are not leaky.


## Examples


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

-->