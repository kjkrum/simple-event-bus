# 0.14
The awkward `EventPipeline` interface that emerged in previous versions hinted at the need for a redesign. The API is now even simpler and more composable.

* Many names have been shortened. `EventReceiver` is now simply `Receiver`, `UncaughtExceptionHandler` is now `ExceptionHandler`, etc.
* `Receiver` has taken over the role of `EventPipeline`.
* `Op` has replaced `EventBus`.
* `SimpleEventBus` is now synchronous. For asynchronous delivery, precede it with a `DeliverOn` or `QueueOn` op.
* `SimpleEventBus` can now use any type of receiver collection. (Previous versions only used `Set`.)
* `SimpleEventBus` no longer manages the copy on write semantics of its receiver collection. The old behavior can be replicated by constructing `SimpleEventBus` with a `CopyOnWriteCollection`.
* `SimpleEventBus` is not thread safe. (Previous versions were only partly thread safe.) The library includes thread safe wrappers for `Op` and `StickyOp`.

The changes to `SimpleEventBus` also apply to `StickyEventBus`.

