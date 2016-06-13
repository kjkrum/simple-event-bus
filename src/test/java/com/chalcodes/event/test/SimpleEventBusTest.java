package com.chalcodes.event.test;

import com.chalcodes.event.EventBus;
import com.chalcodes.event.EventFilter;
import com.chalcodes.event.EventFilters;
import com.chalcodes.event.EventReceiver;
import com.chalcodes.event.SimpleEventBus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link SimpleEventBus}.
 *
 * @author Kevin Krumwiede
 */
public class SimpleEventBusTest {
	private static ExecutorService gExecutor;

	@BeforeClass
	public static void setup() {
		gExecutor = Executors.newFixedThreadPool(1);
	}

	@AfterClass
	public static void teardown() {
		if(gExecutor != null) {
			gExecutor.shutdown();
		}
	}

	private volatile int mReceivedCount;

	/**
	 * Generic filter test.
	 *
	 * @param testEvent the event to broadcast
	 * @param filter the event filter to apply
	 * @param receivedCount what {@link #mReceivedCount} should be after the test
	 */
	private void test(@Nullable final Object testEvent, @Nullable final EventFilter<Object> filter, final int receivedCount) {
		final CountDownLatch latch = new CountDownLatch(1);
		gExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final SimpleEventBus<Object> bus = new SimpleEventBus<Object>(gExecutor, null, filter);
				final EventReceiver<Object> receiver = new EventReceiver<Object>() {
					@Override
					public void onEvent(@Nonnull EventBus<Object> bus, Object event) {
						if(equal(testEvent, event)) {
							++mReceivedCount;
						}
					}
				};
				bus.register(receiver);
				bus.broadcast(testEvent);
				gExecutor.execute(new Runnable() {
					@Override
					public void run() {
						latch.countDown();
					}
				});
			}
		});
		try {
			latch.await();
		}
		catch(InterruptedException e) {
			fail("interrupted");
		}
		assertTrue(mReceivedCount == receivedCount);
	}

	// Objects.equals(...) requires 1.7
	private static boolean equal(Object obj0, Object obj1) {
		return obj0 == null ? obj1 == null : obj0.equals(obj1);
	}

	/**
	 * A bus with no filter accepts any event.
	 */
	@Test
	public void noFilter() {
		test(null, null, 1);
		test("test", null, 2);
	}

	/**
	 * A bus with a discard null filter only accepts non-null events.
	 */
	@Test
	public void discardNullFilter() {
		test(null, EventFilters.discardNull(), 0);
		test("test", EventFilters.discardNull(), 1);
	}

	/**
	 * A bus with an accepting filter accepts any event.
	 */
	@Test
	public void acceptFilter() {
		final EventFilter<Object> filter = new EventFilter<Object>() {
			@Override
			public boolean isAccepted(final Object event) {
				return true;
			}
		};
		test(null, filter, 1);
		test("test", filter, 2);
	}

	/**
	 * An unregistered receiver is not called even if an event is queued for it.
	 */
	@Test
	public void unregisteredReceiver() {
		final CountDownLatch latch = new CountDownLatch(1);
		final String testEvent = "test";
		gExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final SimpleEventBus<String> bus = new SimpleEventBus<String>(gExecutor, null);
				final EventReceiver<String> receiver = new EventReceiver<String>() {
					@Override
					public void onEvent(@Nonnull EventBus<String> bus, String event) {
						++mReceivedCount;
					}
				};
				bus.register(receiver);
				bus.broadcast(testEvent);
				bus.unregister(receiver);
				gExecutor.execute(new Runnable() {
					@Override
					public void run() {
						latch.countDown();
					}
				});
			}
		});
		try {
			latch.await();
		} catch(InterruptedException e) {
			fail("interrupted");
		}
		assertTrue(mReceivedCount == 0);
	}

	private volatile int mExceptionReceivedCount;

	/**
	 * A receiver that throws an exception is removed, and the exception is
	 * broadcast on the exception bus.
	 */
	@Test
	public void badReceiver() {
		final CountDownLatch latch = new CountDownLatch(1);
		final String testEvent = "test";
		gExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final SimpleEventBus<Exception> exceptionBus =
						new SimpleEventBus<Exception>(gExecutor, null, EventFilters.<Exception>discardNull());
				final EventReceiver<Exception> exceptionReceiver = new EventReceiver<Exception>() {
					@Override
					public void onEvent(@Nonnull EventBus<Exception> bus, Exception event) {
						if(event != null) {
							++mExceptionReceivedCount;
						}
					}
				};
				exceptionBus.register(exceptionReceiver);
				final SimpleEventBus<String> bus = new SimpleEventBus<String>(gExecutor, exceptionBus, null);
				final EventReceiver<String> badReceiver = new EventReceiver<String>() {
					@Override
					public void onEvent(@Nonnull EventBus<String> bus, String event) {
						++mReceivedCount;
						throw new RuntimeException();
					}
				};
				bus.register(badReceiver);
				bus.broadcast(testEvent);
				bus.broadcast(testEvent);
				gExecutor.execute(new Runnable() {
					@Override
					public void run() {
						gExecutor.execute(new Runnable() {
							@Override
							public void run() {
								latch.countDown();
							}
						});
					}
				});
			}
		});
		try {
			latch.await();
		} catch(InterruptedException e) {
			fail("interrupted");
		}
		assertTrue(mReceivedCount == 1);
		assertTrue(mExceptionReceivedCount == 1);
	}

	private volatile int mBroadcastCount;

	/**
	 * Broadcast does the right thing even when there are no receivers.
	 */
	@Test
	public void noReceivers() {
		final CountDownLatch latch = new CountDownLatch(1);
		final String testEvent = "test";
		gExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final SimpleEventBus<String> bus = new SimpleEventBus<String>(gExecutor, null, EventFilters.<String>discardNull());
				if(bus.broadcast(testEvent)) ++mBroadcastCount;
				if(bus.broadcast(null)) ++mBroadcastCount;
				gExecutor.execute(new Runnable() {
					@Override
					public void run() {
						latch.countDown();
					}
				});
			}
		});
		try {
			latch.await();
		} catch(InterruptedException e) {
			fail("interrupted");
		}
		assertTrue(mBroadcastCount == 1);
	}
}