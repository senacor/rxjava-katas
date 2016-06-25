package com.senacor.tecco.reactive;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * Simple Wrapper to Block main Thread until async execution finished.
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
                print("runtime to complete: %s ms", durationToCompleteInMs == null ? "-" : durationToCompleteInMs);
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public synchronized boolean isComplete() {
        return complete;
    }
}
