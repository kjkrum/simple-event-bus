package com.chalcodes.event.test;

import com.chalcodes.event.SimpleEventBus;

/**
 * Tests for {@link SimpleEventBus}.
 *
 * @author Kevin Krumwiede
 */
public class SimpleEventBusTest {
//	private static ExecutorService gExecutor;
//
//	@BeforeClass
//	public static void setup() {
//		gExecutor = Executors.newFixedThreadPool(1);
//	}
//
//	@AfterClass
//	public static void teardown() {
//		if(gExecutor != null) {
//			gExecutor.shutdown();
//		}
//	}
//
//	private volatile boolean mEventReceived;
//
//	/**
//	 * A registered receiver receives an event.
//	 */
//	@Test
//	public void registeredReceiver() {
//		final CountDownLatch latch = new CountDownLatch(1);
//		gExecutor.execute(new Runnable() {
//			@Override
//			public void run() {
//				final String testEvent = "test";
//				final EventReceiver<String> receiver = new EventReceiver<String>() {
//					@Override
//					public void onEvent(@Nonnull final EventBus<String> bus, @Nonnull final String event) {
//						if(event.equals(testEvent)) {
//							mEventReceived = true;
//						}
//					}
//				};
//				final EventBus<String> bus = new SimpleEventBus<String>(gExecutor);
//				bus.register(receiver);
//				bus.broadcast(testEvent);
//				gExecutor.execute(new Runnable() {
//					@Override
//					public void run() {
//						latch.countDown();
//					}
//				});
//			}
//		});
//		try {
//			latch.await();
//		}
//		catch(InterruptedException e) {
//			fail("interrupted");
//		}
//		assertTrue(mEventReceived);
//	}
//
//	/**
//	 * A unregistered receiver does not receive an event.
//	 */
//	@Test
//	public void unregisteredReceiver() {
//		final CountDownLatch latch = new CountDownLatch(1);
//		gExecutor.execute(new Runnable() {
//			@Override
//			public void run() {
//				final String testEvent = "test";
//				final EventReceiver<String> receiver = new EventReceiver<String>() {
//					@Override
//					public void onEvent(@Nonnull final EventBus<String> bus, @Nonnull final String event) {
//						if(event.equals(testEvent)) {
//							mEventReceived = true;
//						}
//					}
//				};
//				final EventBus<String> bus = new SimpleEventBus<String>(gExecutor);
//				bus.register(receiver);
//				bus.broadcast(testEvent);
//				bus.unregister(receiver);
//				gExecutor.execute(new Runnable() {
//					@Override
//					public void run() {
//						latch.countDown();
//					}
//				});
//			}
//		});
//		try {
//			latch.await();
//		}
//		catch(InterruptedException e) {
//			fail("interrupted");
//		}
//		assertFalse(mEventReceived);
//	}
//
//	private volatile boolean mExceptionReceived;
//
//	/**
//	 * A receiver that throws an exception is removed, and the exception is
//	 * broadcast on the exception bus.
//	 */
//	@Test
//	public void badReceiver() {
//		final CountDownLatch latch = new CountDownLatch(1);
//		gExecutor.execute(new Runnable() {
//			@Override
//			public void run() {
//				final String testEvent = "test";
//				final EventBus<Exception> exceptionBus = new SimpleEventBus<Exception>(gExecutor);
//				exceptionBus.register(new EventReceiver<Exception>() {
//					@Override
//					public void onEvent(@Nonnull final EventBus<Exception> bus, @Nonnull final Exception event) {
//						mExceptionReceived = true;
//					}
//				});
//				final EventReceiver<String> receiver = new EventReceiver<String>() {
//					private boolean calledBefore;
//
//					@Override
//					public void onEvent(@Nonnull final EventBus<String> bus, @Nonnull final String event) {
//						if(!calledBefore) {
//							calledBefore = true;
//							throw new RuntimeException();
//						}
//						mEventReceived = true;
//					}
//				};
//				final EventBus<String> bus = new SimpleEventBus<String>(gExecutor, exceptionBus);
//				bus.register(receiver);
//				bus.broadcast(testEvent);
//				bus.broadcast(testEvent);
//				gExecutor.execute(new Runnable() {
//					@Override
//					public void run() {
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
//		}
//		catch(InterruptedException e) {
//			fail("interrupted");
//		}
//		assertFalse(mEventReceived);
//		assertTrue(mExceptionReceived);
//	}
//
//	/**
//	 * Registering a null receiver throws a {@code NullPointerException}.
//	 */
//	@Test(expected = NullPointerException.class)
//	public void nullReceiver() {
//		final EventBus<String> bus = new SimpleEventBus<String>(gExecutor);
//		//noinspection ConstantConditions
//		bus.register(null);
//	}
//
//	/**
//	 * Broadcasting a null event throws a {@code NullPointerException}.
//	 */
//	@Test(expected = NullPointerException.class)
//	public void nullEvent() {
//		final EventBus<String> bus = new SimpleEventBus<String>(gExecutor);
//		//noinspection ConstantConditions
//		bus.broadcast(null);
//	}
}