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
public class SwitchTest {
    @Test
    public void testSwitchIfEmpty() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.empty();
        Observable<String> stream2 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "second " + value);

        Subscription subscription = stream1.switchIfEmpty(stream2)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(2500, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }

    @Test
    public void testSwitchOnNext() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<Observable<String>> observableObservable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .map(count -> Observable.interval(100, TimeUnit.MILLISECONDS)
                        .map(value -> count + " -> " + value));

        Subscription subscription = Observable.switchOnNext(observableObservable)
                .take(2, TimeUnit.SECONDS)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(2500, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }
}
