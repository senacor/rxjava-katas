package com.senacor.tecco.reactive.example.combining;

import com.senacor.tecco.reactive.util.WaitMonitor;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.util.ReactiveUtil.getThreadId;
import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class JoinTest {
    @Test
    public void testJoin() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Observable<String> stream1 = Observable.interval(150, TimeUnit.MILLISECONDS)
                .take(8)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.interval(10, TimeUnit.MILLISECONDS)
                .take(80)
                .map(value -> "second " + value);

        Disposable subscription = stream1.join(stream2,
                s1 -> Observable.timer(30, TimeUnit.MILLISECONDS),
                s2 -> Observable.timer(5, TimeUnit.MILLISECONDS),
                (s1, s2) -> s1 + " -> " + s2)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(2000, TimeUnit.MILLISECONDS);
        subscription.dispose();
    }

    @Test
    // TODO (ak) 2.0 upgrade: result should be:
    /*
RxComputationThreadPool-1: next: first 0 -> [second 21, second 22, second 23, second 24]
RxComputationThreadPool-1: next: first 1 -> [second 38, second 39, second 40, second 41, second 42]
RxComputationThreadPool-1: next: first 2 -> [second 58, second 59, second 60, second 61, second 62]
RxComputationThreadPool-1: next: first 3 -> [second 78, second 79, second 80, second 81, second 82]
     */
    public void testGroupJoin() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Observable<String> stream1 = Observable.interval(200, TimeUnit.MILLISECONDS)
                .take(5)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.interval(10, TimeUnit.MILLISECONDS)
                .take(110)
                .map(value -> "second " + value);

        Disposable subscription = stream1.groupJoin(stream2,
                s1 -> Observable.timer(50, TimeUnit.MILLISECONDS),
                s2 -> Observable.timer(5, TimeUnit.MILLISECONDS),
                (s1, s2) -> {
                    List<String> groupS2 = s2.toList().blockingGet();
                    return s1 + " -> " + groupS2;
                })
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            System.out.println(getThreadId() + "complete!");
                            monitor.complete();
                        });

        monitor.waitFor(2000, TimeUnit.MILLISECONDS);
        subscription.dispose();
    }
}
