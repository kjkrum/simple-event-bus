package com.chalcodes.event.test;

import com.chalcodes.event.EventBus;
import com.chalcodes.event.EventReceiver;
import com.chalcodes.event.StickyEventBus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link StickyEventBus}.
 *
 * @author Kevin Krumwiede
 */
public class StickyEventBusTest {
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

	private volatile boolean mEventReceived;

	/**
	 * A receiver registered after a broadcast receives the event.
	 */
	@Test
	public void registerAfterBroadcast() {
		final CountDownLatch latch = new CountDownLatch(1);
		gExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final String testEvent = "test";
				final EventReceiver<String> receiver = new EventReceiver<String>() {
					@Override
					public void onEvent(@Nonnull EventBus<String> bus, @Nonnull String event) {
						if(event.equals(testEvent)) {
							mEventReceived = true;
						}
					}
				};
				final EventBus<String> bus = new StickyEventBus<String>(gExecutor, null);
				bus.broadcast(testEvent);
				bus.register(receiver);
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
		assertTrue(mEventReceived);
	}

	/**
	 * A receiver registered and unregistered after a broadcast does not
	 * receive an event.
	 */
	@Test
	public void registerAndUnregisterAfterBroadcast() {
		final CountDownLatch latch = new CountDownLatch(1);
		gExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final String testEvent = "test";
				final EventReceiver<String> receiver = new EventReceiver<String>() {
					@Override
					public void onEvent(@Nonnull EventBus<String> bus, @Nonnull String event) {
							mEventReceived = true;
					}
				};
				final EventBus<String> bus = new StickyEventBus<String>(gExecutor, null);
				bus.broadcast(testEvent);
				bus.register(receiver);
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
		assertFalse(mEventReceived);
	}


//	/**
//	 * An event becomes sticky immediately, even without a receiver.
//	 */
//	@Test
//	public void eventBecomesSticky() {
//		final CountDownLatch latch = new CountDownLatch(1);
//		final String testEvent = "test";
//		gExecutor.execute(new Runnable() {
//			@Override
//			public void run() {
//				final StickyEventBus<String> bus = new StickyEventBus<String>(gExecutor, null);
//				bus.broadcast(testEvent);
//				mEventValue = bus.getSticky();
//				latch.countDown();
//			}
//		});
//		try {
//			latch.await();
//		} catch(InterruptedException e) {
//			fail("interrupted");
//		}
//		assertEquals(testEvent, mEventValue);
//	}
//
//	/**
//	 * Tests for regression of <a
//	 * href="https://github.com/kjkrum/simple-event-bus/issues/1">issue
//	 * #1</a>.
//	 */
//	@Test
//	public void issue1() {
//		final CountDownLatch latch = new CountDownLatch(1);
//		final String oldEvent = "foo";
//		final String newEvent = "bar";
//		gExecutor.execute(new Runnable() {
//			@Override
//			public void run() {
//				final SimpleEventBus<String> bus = new StickyEventBus<String>(gExecutor, null);
//				bus.broadcast(oldEvent);
//				gExecutor.execute(new Runnable() {
//					@Override
//					public void run() {
//						bus.broadcast(newEvent);
//						bus.register(new EventReceiver<String>() {
//							@Override
//							public void onEvent(@Nonnull EventBus<String> bus, String event) {
//								mEventValue = event;
//							}
//						});
//						gExecutor.execute(new Runnable() {
//							@Override
//							public void run() {
//								latch.countDown();
//							}
//						});
//					}
//				});
//			}
//		});
//		try {
//			latch.await();
//		} catch(InterruptedException e) {
//			fail("interrupted");
//		}
//		assertEquals(newEvent, mEventValue);
//	}
}