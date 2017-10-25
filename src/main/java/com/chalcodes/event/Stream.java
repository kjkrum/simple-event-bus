package com.chalcodes.event;

import com.chalcodes.event.Emitter;
import com.chalcodes.event.Op;
import com.chalcodes.event.Receiver;
import com.chalcodes.event.StickyOp;
import com.chalcodes.event.ops.Catch;
import com.chalcodes.event.ops.DeliverOn;
import com.chalcodes.event.ops.QueueOn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

/**
 * Fluent builders for sequences of emitters, ops, and receivers. Builders are
 * not thread safe.
 *
 * @author Kevin Krumwiede
 */
// TODO wildcard types everywhere
public class Stream {
	private Stream() {}

	/**
	 * Builds an {@link Emitter} or nothing depending on whether the
	 * stream terminates in an {@link Op} or a {@link Receiver}.
	 *
	 * @param <O>
	 */
	public static class EmitterBuilder<O> {
		Emitter<O> mTail;
		private boolean mTerminated;

		private EmitterBuilder(@Nonnull final Emitter<O> emitter) {
			mTail = emitter;
		}

		private void checkTerminated() {
			if(mTerminated) {
				throw new IllegalStateException("this builder has already been terminated");
			}
		}

		private void terminate() {
			checkTerminated();
			mTerminated = true;
		}

		private void append(@Nonnull final Receiver<O> receiver) {
			if(!mTail.register(receiver)) {
				throw new IllegalStateException("the receiver could not be registered");
			}
		}

		public <X> EmitterBuilder<X> to(@Nonnull final Op<O, X> op) {
			checkTerminated();
			append(op);
			// noinspection unchecked - setting mTail changes O
			final EmitterBuilder<X> _this = (EmitterBuilder<X>) this;
			_this.mTail = op;
			return _this;
		}

		public <X> Emitter<X> end(@Nonnull final Op<O, X> op) {
			return to(op).end();
		}

		public Object end(@Nonnull final Receiver<O> receiver) {
			terminate();
			append(receiver);
			return null;
		}

		public Emitter<O> end() {
			terminate();
			return mTail;
		}

		/* Convenience methods. */

		public EmitterBuilder<O> catch_(@Nonnull final Receiver<? super RuntimeException> receiver) {
			return to(new Catch<O>(receiver));
		}

		public EmitterBuilder<O> deliverOn(@Nonnull final Executor executor) {
			return to(new DeliverOn<O>(executor));
		}

		public EmitterBuilder<O> queueOn(@Nonnull final Executor executor) { return to(new QueueOn<O>(executor)); }
	}

	/**
	 * Builds an {@link Op} or a {@link Receiver} depending on which the
	 * stream terminates in.
	 *
	 * @param <I>
	 * @param <O>
	 */
	public static class OpBuilder<I, O> extends EmitterBuilder<O> {
		// parameterized this way because O changes with mTail.
		private final Op<I, ?> mHead;

		private OpBuilder(@Nonnull final Op<I, O> op) {
			super(op);
			mHead = op;
		}

		@Override
		public <X> OpBuilder<I, X> to(@Nonnull final Op<O, X> op) {
			return (OpBuilder<I, X>) super.to(op);
		}

		@Override
		public <X> Op<I, X> end(@Nonnull final Op<O, X> op) {
			return to(op).end();
		}

		public <X> StickyOp<I, X> end(@Nonnull final StickyOp<O, X> op) {
			end((Op) op);
			return new StickyOp<I, X>() {
				@Override
				public boolean register(@Nonnull final Receiver<? super X> receiver) {
					return op.register(receiver);
				}

				@Override
				public boolean unregister(@Nonnull final Receiver<? super X> receiver) {
					return op.unregister(receiver);
				}

				@Override
				public void onEvent(@Nonnull final I event) {
					mHead.onEvent(event);
				}

				@Nullable
				@Override
				public X setEvent(@Nullable final X event) {
					return op.setEvent(event);
				}
			};
		}

		@Override
		public Receiver<I> end(@Nonnull final Receiver<O> receiver) {
			super.end(receiver);
			return mHead;
		}

		@Override
		public Op<I, O> end() {
			super.end();
			if(mHead == mTail) {
				//noinspection unchecked - ? is O in this case
				return (Op<I, O>) mHead;
			}
			return new Op<I, O>() {
				@Override
				public boolean register(@Nonnull final Receiver<? super O> receiver) {
					return mTail.register(receiver);
				}

				@Override
				public boolean unregister(@Nonnull final Receiver<? super O> receiver) {
					return mTail.unregister(receiver);
				}

				@Override
				public void onEvent(@Nonnull final I event) {
					mHead.onEvent(event);
				}
			};
		}

		/* Convenience methods. */

		@Override
		public OpBuilder<I, O> catch_(@Nonnull final Receiver<? super RuntimeException> receiver) {
			return (OpBuilder<I, O>) super.catch_(receiver);
		}

		@Override
		public OpBuilder<I, O> deliverOn(@Nonnull final Executor executor) {
			return (OpBuilder<I, O>) super.deliverOn(executor);
		}

		@Override
		public OpBuilder<I, O> queueOn(@Nonnull final Executor executor) {
			return (OpBuilder<I, O>) super.queueOn(executor);
		}
	}

	/* Static factory methods. */

	public static <O> EmitterBuilder<O> from(@Nonnull final Emitter<O> emitter) {
		return new EmitterBuilder<O>(emitter);
	}

	public static <I, O> OpBuilder<I, O> from(@Nonnull final Op<I, O> op) {
		return new OpBuilder<I, O>(op);
	}

	/* Static convenience methods. */

	public static <E> OpBuilder<E, E> catch_(@Nonnull final Receiver<? super RuntimeException> receiver) {
		return from(new Catch<E>(receiver));
	}

	public static <E> OpBuilder<E, E> deliverOn(@Nonnull final Executor executor) {
		return from(new DeliverOn<E>(executor));
	}

	public static <E> OpBuilder<E, E> queueOn(@Nonnull final Executor executor) {
		return from(new QueueOn<E>(executor));
	}
}
