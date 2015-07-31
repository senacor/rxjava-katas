package com.senacor.tecco.codecamp.reactive.combining;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
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

        Subscription subscription = stream1.join(stream2,
                s1 -> Observable.timer(30, TimeUnit.MILLISECONDS),
                s2 -> Observable.timer(5, TimeUnit.MILLISECONDS),
                (s1, s2) -> s1 + " -> " + s2)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(2000, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }

    @Test
    public void testGroupJoin() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Observable<String> stream1 = Observable.interval(200, TimeUnit.MILLISECONDS)
                .take(5)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.interval(10, TimeUnit.MILLISECONDS)
                .take(110)
                .map(value -> "second " + value);

        Subscription subscription = stream1.groupJoin(stream2,
                s1 -> Observable.timer(50, TimeUnit.MILLISECONDS),
                s2 -> Observable.timer(5, TimeUnit.MILLISECONDS),
                (s1, s2) -> {
                    List<String> groupS2 = s2.toList().toBlocking().firstOrDefault(Collections.emptyList());
                    return s1 + " -> " + groupS2;
                })
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> {
                            System.out.println(getThreadId() + "complete!");
                            monitor.complete();
                        });

        monitor.waitFor(2000, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }
}
