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
public class MergeTest {

    @Test
    public void testMergeWith() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "second " + value);

        Disposable subscription = stream1.mergeWith(stream2)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(2500, TimeUnit.MILLISECONDS);
        subscription.dispose();
    }

    @Test
    public void testMerge() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "second " + value);
        Observable<String> stream3 = Observable.interval(250, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "3rd " + value);

        Disposable subscription = Observable.merge(stream1, stream2, stream3)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(2500, TimeUnit.MILLISECONDS);
        subscription.dispose();
    }
}
