package com.chalcodes.event.test;

import com.chalcodes.event.EventFilters;

/**
 * Tests for {@link EventFilters}.
 *
 * @author Kevin Krumwiede
 */
public class EventFiltersTest {
//	private static final Executor gExecutor = new Executor() {
//		@Override
//		public void execute(@Nonnull final Runnable command) {
//			command.run();
//		}
//	};
//
//	private static final String TEST_EVENT = "foo";
//	private String mEvent;
//
//	@Test
//	public void testDiscardNull() {
//		final EventBus<String> bus =
//				new SimpleEventBus<String>(gExecutor, null, EventFilters.<String>discardNull());
//		final EventReceiver<String> receiver = new EventReceiver<String>() {
//			@Override
//			public void onEvent(@Nonnull final EventBus<String> bus, final String event) {
//				if(event == null) {
//					fail("receiver should not receive null");
//				}
//				mEvent = event;
//			}
//		};
//		bus.register(receiver);
//		bus.broadcast(TEST_EVENT);
//		assertEquals("should have received test event", TEST_EVENT, mEvent);
//		bus.broadcast(null);
//	}
//
//	@Test(expected = NullPointerException.class)
//	public void testThrowNull() {
//		final EventBus<String> bus =
//				new SimpleEventBus<String>(gExecutor, null, EventFilters.<String>throwNull());
//		final EventReceiver<String> receiver = new EventReceiver<String>() {
//			@Override
//			public void onEvent(@Nonnull final EventBus<String> bus, final String event) {
//				if(event == null) {
//					fail("receiver should not receive null");
//				}
//				mEvent = event;
//			}
//		};
//		bus.register(receiver);
//		bus.broadcast(TEST_EVENT);
//		assertEquals("should have received test event", TEST_EVENT, mEvent);
//		bus.broadcast(null);
//	}
}
