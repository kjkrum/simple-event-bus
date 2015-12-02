package com.chalcodes.event;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

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

	/* Values set during tests, to be checked by the thread running the test. */
	private volatile int mReceiverCalled;
	private volatile String mEventValue;

	@Test
	public void receiverShouldReceiveStickyEvent() {
		final CountDownLatch latch = new CountDownLatch(1);
		final String testEvent = "test";
		gExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final SimpleEventBus<String> bus = new StickyEventBus<String>(gExecutor, null, false);
				final EventReceiver<String> receiver = new EventReceiver<String>() {
					@Override
					public void onEvent(EventBus<String> bus, String event) {
						if(testEvent.equals(event)) {
							++mReceiverCalled;
						}
					}
				};
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
		assertTrue(mReceiverCalled > 0);
	}

	@Test
	public void eventShouldBecomeStickyImmediately() {
		final CountDownLatch latch = new CountDownLatch(1);
		final String testEvent = "test";
		gExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final StickyEventBus<String> bus = new StickyEventBus<String>(gExecutor, null, false);
				bus.broadcast(testEvent);
				mEventValue = bus.getSticky();
				latch.countDown();
			}
		});
		try {
			latch.await();
		} catch(InterruptedException e) {
			fail("interrupted");
		}
		assertEquals(testEvent, mEventValue);
	}

	@Test
	public void unregisteredReceiverShouldNotReceiveStickyEvent() {
		final CountDownLatch latch = new CountDownLatch(1);
		final String testEvent = "test";
		gExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final SimpleEventBus<String> bus = new StickyEventBus<String>(gExecutor, null, false);
				final EventReceiver<String> receiver = new EventReceiver<String>() {
					@Override
					public void onEvent(EventBus<String> bus, String event) {
						++mReceiverCalled;
					}
				};
				bus.broadcast(testEvent);
				// queuing this is no longer necessary, but shouldn't make any difference
				gExecutor.execute(new Runnable() {
					@Override
					public void run() {
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
			}
		});
		try {
			latch.await();
		} catch(InterruptedException e) {
			fail("interrupted");
		}
		assertTrue(mReceiverCalled == 0);
	}

	// https://github.com/kjkrum/simple-event-bus/issues/1
	@Test
	public void issueOne() {
		final CountDownLatch latch = new CountDownLatch(1);
		final String oldEvent = "foo";
		final String newEvent = "bar";
		gExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final SimpleEventBus<String> bus = new StickyEventBus<String>(gExecutor, null, false);
				bus.broadcast(oldEvent);
				// queuing this is no longer necessary, but shouldn't make any difference
				gExecutor.execute(new Runnable() {
					@Override
					public void run() {
						bus.broadcast(newEvent);
						bus.register(new EventReceiver<String>() {
							@Override
							public void onEvent(EventBus<String> bus, String event) {
								mEventValue = event;
							}
						});
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
		assertEquals(newEvent, mEventValue);
	}

}