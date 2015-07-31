package com.senacor.tecco.codecamp.reactive.creating;

import com.google.common.util.concurrent.Futures;
import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.concurrent.*;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class FromTest {

    @Test
    public void testFromArray() throws Exception {
        String[] array = {"1", "2"};
        Observable.from(array)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }

    @Test
    public void testFromIterable() throws Exception {
        Iterable<String> iterable = Arrays.asList("1", "2");
        Observable.from(iterable)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }

    @Test
    public void testFromFutureAlreadyDone() throws Exception {
        Future<String> future = Futures.immediateFuture("immediateFuture");
        Observable.from(future)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }

    @Test
    public void testFromFutureCancelled() throws Exception {
        Future<String> future = Futures.immediateCancelledFuture();
        Observable.from(future, 1, TimeUnit.SECONDS)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }

    @Test
    public void testFromFutureWithTimeout() throws Exception {
        final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        Future<String> future = new Future<String>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public String get() throws InterruptedException, ExecutionException {
                return queue.poll(5, TimeUnit.SECONDS);
            }

            @Override
            public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return queue.poll(timeout, unit);
            }
        };
        Observable.from(future, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
        print("sleeping 1000ms...");
        Thread.sleep(1000);
        queue.put("with timeout");
    }

    @Test
    public void testFromFutureWithScheduler() throws Exception {
        final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        Future<String> future = new Future<String>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public String get() throws InterruptedException, ExecutionException {
                return queue.poll(5, TimeUnit.SECONDS);
            }

            @Override
            public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return queue.poll(timeout, unit);
            }
        };

        Observable.from(future, Schedulers.io())
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
        print("sleeping 1000ms...");
        Thread.sleep(1000);
        queue.put("with scheduler");
    }
}