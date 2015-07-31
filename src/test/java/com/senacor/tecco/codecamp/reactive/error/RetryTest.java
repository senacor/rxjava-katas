package com.senacor.tecco.codecamp.reactive.error;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class RetryTest {
    @Test
    public void testRetry() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.timer(100, TimeUnit.MILLISECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.timer(500, TimeUnit.MILLISECONDS)
                .map(value -> {
                    throw new NullPointerException("dooo");
                });
        Observable<String> stream3 = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .map(value -> "3rd " + value);

        Subscription subscription = Observable.merge(stream1, stream2, stream3)
                .retry(1)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(1500, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }

    @Test
    public void testRetryWhen() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.timer(100, TimeUnit.MILLISECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.timer(500, TimeUnit.MILLISECONDS)
                .map(value -> {
                    throw new NullPointerException("dooo");
                });
        Observable<String> stream3 = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .map(value -> "3rd " + value);

        Subscription subscription = Observable.merge(stream1, stream2, stream3)
                .retryWhen(attempts -> attempts.zipWith(Observable.range(1, 2), (n, i) -> i)
                        .flatMap(i -> {
                            print("randomDelay retry by " + i + " second(s)");
                            return Observable.timer(i, TimeUnit.SECONDS);
                        }))
                .subscribe(next -> print("next: %s", next),
                      Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(10000, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }
}
