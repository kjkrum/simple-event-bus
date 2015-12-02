package com.chalcodes.event;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

/**
 * Tests for {@link EventBus}.
 *
 * @author Kevin Krumwiede
 */
public class EventBusTest {
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
    public void receiverShouldReceiveEvent() {
        final CountDownLatch latch = new CountDownLatch(1);
        final String testEvent = "test";
        gExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final EventBus<String> bus = new EventBus<String>(gExecutor, null, false, false);
                final EventReceiver<String> receiver = new EventReceiver<String>() {
                    @Override
                    public void onEvent(EventBus<String> bus, String event) {
                        if(testEvent.equals(event)) {
                            ++mReceiverCalled;
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
        } catch (InterruptedException e) {
            fail("interrupted");
        }
        assertTrue(mReceiverCalled > 0);
    }

    @Test
    public void receiverShouldReceiveStickyEvent() {
        final CountDownLatch latch = new CountDownLatch(1);
        final String testEvent = "test";
        gExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final EventBus<String> bus = new EventBus<String>(gExecutor, null, true, false);
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
        } catch (InterruptedException e) {
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
                final EventBus<String> bus = new EventBus<String>(gExecutor, null, true, false);
                bus.broadcast(testEvent);
                mEventValue = bus.getSticky();
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            fail("interrupted");
        }
        assertEquals(testEvent, mEventValue);
    }

    @Test
    public void unregisteredReceiverShouldNotReceiveEvent() {
        final CountDownLatch latch = new CountDownLatch(1);
        final String testEvent = "test";
        gExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final EventBus<String> bus = new EventBus<String>(gExecutor, null, false, false);
                final EventReceiver<String> receiver = new EventReceiver<String>() {
                    @Override
                    public void onEvent(EventBus<String> bus, String event) {
                        ++mReceiverCalled;
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
        } catch (InterruptedException e) {
            fail("interrupted");
        }
        assertTrue(mReceiverCalled == 0);
    }

    @Test
    public void unregisteredReceiverShouldNotReceiveStickyEvent() {
        final CountDownLatch latch = new CountDownLatch(1);
        final String testEvent = "test";
        gExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final EventBus<String> bus = new EventBus<String>(gExecutor, null, true, false);
                final EventReceiver<String> receiver = new EventReceiver<String>() {
                    @Override
                    public void onEvent(EventBus<String> bus, String event) {
                        ++mReceiverCalled;
                    }
                };
                bus.broadcast(testEvent);
                // this runs after event becomes sticky
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
        } catch (InterruptedException e) {
            fail("interrupted");
        }
        assertTrue(mReceiverCalled == 0);
    }

    @Test
    public void exceptionShouldBeBroadcast() {
        final CountDownLatch latch = new CountDownLatch(1);
        final String testEvent = "test";
        gExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final EventBus<Exception> exceptionBus =
                        new EventBus<Exception>(gExecutor, null, false, false);
                final EventReceiver<Exception> exceptionReceiver = new EventReceiver<Exception>() {
                    @Override
                    public void onEvent(EventBus<Exception> bus, Exception event) {
                        if(event != null) {
                            ++mReceiverCalled;
                        }
                    }
                };
                exceptionBus.register(exceptionReceiver);
                final EventBus<String> bus = new EventBus<String>(gExecutor, exceptionBus, false, false);
                final EventReceiver<String> badReceiver = new EventReceiver<String>() {
                    @Override
                    public void onEvent(EventBus<String> bus, String event) {
                        throw new RuntimeException();
                    }
                };
                bus.register(badReceiver);
                bus.broadcast(testEvent);
                // test broadcast runs, exception broadcast is queued
                // first task below runs, queues second task
                // exception broadcast runs
                // second task runs, latch counts down
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
        } catch (InterruptedException e) {
            fail("interrupted");
        }
        assertTrue(mReceiverCalled > 0);
    }

    @Test
    public void badReceiverShouldBeRemoved() {
        final CountDownLatch latch = new CountDownLatch(1);
        final String testEvent = "test";
        gExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final EventBus<String> bus = new EventBus<String>(gExecutor, null, false, false);
                final EventReceiver<String> receiver = new EventReceiver<String>() {
                    @Override
                    public void onEvent(EventBus<String> bus, String event) {
                        ++mReceiverCalled;
                        throw new RuntimeException();
                    }
                };
                bus.register(receiver);
                bus.broadcast(testEvent);
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
        } catch (InterruptedException e) {
            fail("interrupted");
        }
        assertTrue(mReceiverCalled == 1);
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
                final EventBus<String> bus = new EventBus<String>(gExecutor, null, true, false);
                bus.broadcast(oldEvent);
                gExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        // oldEvent is now sticky
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
        } catch (InterruptedException e) {
            fail("interrupted");
        }
        assertEquals(newEvent, mEventValue);
    }

}