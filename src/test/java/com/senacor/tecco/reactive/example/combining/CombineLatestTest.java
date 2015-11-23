package com.senacor.tecco.reactive.example.combining;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.WaitMonitor;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class CombineLatestTest {
    @Test
    public void testCombineLatest() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Observable<Long> stream1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(8);
        Observable<Long> stream2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .take(8);
        Subscription subscription = Observable.combineLatest(stream1, stream2, (s1, s2) -> "stream1=" + s1 + " stream2=" + s2)
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(5000, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }
}
