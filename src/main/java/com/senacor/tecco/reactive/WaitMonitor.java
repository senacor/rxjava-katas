package com.senacor.tecco.reactive;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * Simple Wrapper to Block main Thread until async execution finished.
 *
 * @author Andreas Keefer
 */
public class WaitMonitor {

    private final LinkedBlockingQueue<Boolean> queue = new LinkedBlockingQueue<>();
    private final long creationTimeStamp = System.currentTimeMillis();
    private Long durationToCompleteInMs = null;
    private boolean complete = false;
    private int expectedCompletes = 1;

    public WaitMonitor(int expectedCompletes) {
        this.expectedCompletes = expectedCompletes;
    }

    public WaitMonitor() {
    }

    /**
     * Call this Method in your async code when the async execution has finished
     */
    public void complete() {
        try {
            queue.put(true);
            synchronized (this){
                complete = true;
                durationToCompleteInMs = System.currentTimeMillis() - creationTimeStamp;
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Wait with timeout in your main Thread until #complete() is called.
     *
     * @param timeout timeout
     * @param unit    unit
     */
    public void waitFor(long timeout, TimeUnit unit) {
        try {
            for (int i=0; i<expectedCompletes; i++) {
                queue.poll(timeout, unit);
            }
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
