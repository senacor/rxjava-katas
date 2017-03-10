package com.senacor.codecamp.reactive.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Simple Wrapper to Block main Thread until async execution finished.
 * <p>
 * This test helper is needed, when you don't use the UnitTesting features from RxJava/Reactor
 * and your Pipeline is not executed on the main thread. In this case you have to wait in the
 * main thread for the completion. this is the pattern:
 * <p>
 * WaitMonitor waitMonitor = new WaitMonitor();
 * <p>
 * Observable/Flux task ...
 * __.subscribe(next -> ...some OnNext handler,
 * _____________error -> ...some error handler,
 * _____________() -> waitMonitor.complete())
 * monitor.waitFor(5, TimeUnit.SECONDS);
 * <p>
 * The WaitMonitor will block the main thread until waitMonitor.complete() is called (pipeline execution has finished in the expected state)
 * OR the 5 seconds timeout has expired.
 *
 * @author Andreas Keefer
 */
public class WaitMonitor {

    private final CountDownLatch countDownLatch;
    private final long creationTimeStamp = System.currentTimeMillis();
    private Long durationToCompleteInMs = null;
    private boolean complete = false;

    public WaitMonitor(int expectedCompletes) {
        this.countDownLatch = new CountDownLatch(expectedCompletes);
    }

    public WaitMonitor() {
        this(1);
    }

    /**
     * Call this Method in your async code when the async execution has finished
     */
    public synchronized void complete() {
        countDownLatch.countDown();
        complete = true;
        durationToCompleteInMs = System.currentTimeMillis() - creationTimeStamp;
    }

    /**
     * Wait with timeout in your main Thread until #complete() is called.
     *
     * @param timeout timeout
     * @param unit    unit
     */
    public void waitFor(long timeout, TimeUnit unit) {
        try {
            countDownLatch.await(timeout, unit);
            synchronized (this) {
                ReactiveUtil.print("runtime to complete: %s ms", durationToCompleteInMs == null ? "-" : durationToCompleteInMs);
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public synchronized boolean isComplete() {
        return complete;
    }
}
