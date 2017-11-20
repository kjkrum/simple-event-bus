package com.chalcodes.event;

import com.chalcodes.event.Emitter;
import com.chalcodes.event.Op;
import com.chalcodes.event.Receiver;
import com.chalcodes.event.ops.*;

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

	/* General methods. */

	public static <O> EmitterBuilder<O> from(@Nonnull final Emitter<O> emitter) {
		return new EmitterBuilder<O>(emitter);
	}

	public static <I, O> OpBuilder<I, O> from(@Nonnull final Op<I, O> op) {
		return new OpBuilder<I, O>(op);
	}

	/* Convenience methods. */

	public static <E> OpBuilder<E, E> block(@Nonnull final Filter<E> filter) {
		return pass(Filters.invert(filter));
	}

	public static <E> OpBuilder<E, E> catch_(@Nonnull final Receiver<? super RuntimeException> receiver) {
		return from(new Catch<E>(receiver));
	}

	public static <E> OpBuilder<E, E> changed() {
		return from(new Changed<E>());
	}

	public static <E> OpBuilder<E, E> debounce(final long intervalNanos) {
		return from(new Debounce<E>(intervalNanos));
	}

	public static <E> OpBuilder<E, E> deliverOn(@Nonnull final Executor executor) {
		return from(new DeliverOn<E>(executor));
	}

	public static <E> OpBuilder<E, E> ignore(final int count) {
		return from(new Ignore<E>(count));
	}

	public static <E> OpBuilder<E, E> pass(@Nonnull final Filter<E> filter) {
		return from(new Pass<E>(filter));
	}

	public static <E> OpBuilder<E, E> queueOn(@Nonnull final Executor executor) {
		return from(new QueueOn<E>(executor));
	}

	// TODO all the ops!

	/**
	 * Builds an {@link Emitter} or nothing depending on whether the stream
	 * ends in an {@link Op} or a {@link Receiver}.
	 *
	 * @param <O> the emitter event type
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

		/* General methods. */

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

		public EmitterBuilder<O> block(@Nonnull final Filter<O> filter) {
			return pass(Filters.invert(filter));
		}

		public EmitterBuilder<O> catch_(@Nonnull final Receiver<? super RuntimeException> receiver) {
			return to(new Catch<O>(receiver));
		}

		public EmitterBuilder<O> changed() {
			return to(new Changed<O>());
		}

		public EmitterBuilder<O> debounce(final long intervalNanos) {
			return to(new Debounce<O>(intervalNanos));
		}

		public EmitterBuilder<O> deliverOn(@Nonnull final Executor executor) {
			return to(new DeliverOn<O>(executor));
		}

		public EmitterBuilder<O> ignore(final int count) {
			return to(new Ignore<O>(count));
		}

		public EmitterBuilder<O> pass(@Nonnull final Filter<O> filter) {
			return to(new Pass<O>(filter));
		}

		public EmitterBuilder<O> queueOn(@Nonnull final Executor executor) { return to(new QueueOn<O>(executor)); }

		// TODO all the ops!
	}

	/**
	 * Builds an {@link Op} or a {@link Receiver} depending on which the
	 * stream ends in.
	 *
	 * @param <I> the receiver event type
	 * @param <O> the emitter event type
	 */
	public static class OpBuilder<I, O> extends EmitterBuilder<O> {
		// parameterized this way because O changes with mTail.
		private final Op<I, ?> mHead;

		private OpBuilder(@Nonnull final Op<I, O> op) {
			super(op);
			mHead = op;
		}

		/* General methods. */

		@Override
		public <X> OpBuilder<I, X> to(@Nonnull final Op<O, X> op) {
			return (OpBuilder<I, X>) super.to(op);
		}

		@Override
		public <X> Op<I, X> end(@Nonnull final Op<O, X> op) {
			return to(op).end();
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
		public OpBuilder<I, O> block(@Nonnull final Filter<O> filter) {
			return pass(Filters.invert(filter));
		}

		@Override
		public OpBuilder<I, O> catch_(@Nonnull final Receiver<? super RuntimeException> receiver) {
			return (OpBuilder<I, O>) super.catch_(receiver);
		}

		@Override
		public OpBuilder<I, O> changed() {
			return (OpBuilder<I, O>) super.changed();
		}

		@Override
		public OpBuilder<I, O> debounce(final long intervalNanos) {
			return (OpBuilder<I, O>) super.debounce(intervalNanos);
		}

		@Override
		public OpBuilder<I, O> deliverOn(@Nonnull final Executor executor) {
			return (OpBuilder<I, O>) super.deliverOn(executor);
		}

		@Override
		public OpBuilder<I, O> ignore(final int count) {
			return (OpBuilder<I, O>) super.ignore(count);
		}

		@Override
		public OpBuilder<I, O> pass(@Nonnull final Filter<O> filter) {
			return (OpBuilder<I, O>) super.pass(filter);
		}

		@Override
		public OpBuilder<I, O> queueOn(@Nonnull final Executor executor) {
			return (OpBuilder<I, O>) super.queueOn(executor);
		}

		// TODO all the ops!
	}
}
