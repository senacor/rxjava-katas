package com.senacor.codecamp.reactive.example.combining;

import com.senacor.codecamp.reactive.util.WaitMonitor;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class SwitchTest {
    @Test
    public void testSwitchIfEmpty() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.empty();
        Observable<String> stream2 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "second " + value);

        Disposable subscription = stream1.switchIfEmpty(stream2)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(2500, TimeUnit.MILLISECONDS);
        subscription.dispose();
    }

    @Test
    public void testSwitchOnNext() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<Observable<String>> observableObservable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .map(count -> Observable.interval(100, TimeUnit.MILLISECONDS)
                        .map(value -> count + " -> " + value));

        Disposable subscription = Observable.switchOnNext(observableObservable)
                .take(2, TimeUnit.SECONDS)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(2500, TimeUnit.MILLISECONDS);
        subscription.dispose();
    }
}
