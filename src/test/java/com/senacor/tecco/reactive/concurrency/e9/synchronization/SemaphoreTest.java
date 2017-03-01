package com.senacor.tecco.reactive.concurrency.e9.synchronization;

import java.util.concurrent.Semaphore;

/**
 * Created by mmenzel on 08.02.2016.
 */
public class SemaphoreTest {

    Semaphore binary = new Semaphore(1);

    public static void main(String args[]) {
        final SemaphoreTest test = new SemaphoreTest();
        new Thread(() -> test.mutualExclusion()).start();

        new Thread(() -> test.mutualExclusion()).start();

    }

    private void mutualExclusion() {
        try {
           binary.acquire();

            //mutual exclusive region
            System.out.println(Thread.currentThread().getName() + " inside mutual exclusive region");
            Thread.sleep(4000);

        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } finally {
            binary.release();
            System.out.println(Thread.currentThread().getName() + " outside of mutual exclusive region");
        }
    }

}