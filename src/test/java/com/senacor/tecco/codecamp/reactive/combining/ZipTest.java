package com.senacor.tecco.codecamp.reactive.combining;

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
public class ZipTest {

    @Test
    public void testZip() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "second " + value);

        Subscription subscription = Observable.zip(stream1, stream2, (s1, s2) -> "{" + s1 + " + " + s2 + "}")
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(2500, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }

    @Test
    public void testZipWith() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "second " + value);

        Subscription subscription = stream1.zipWith(stream2, (s1, s2) -> "{" + s1 + " + " + s2 + "}")
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(2500, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }

}
