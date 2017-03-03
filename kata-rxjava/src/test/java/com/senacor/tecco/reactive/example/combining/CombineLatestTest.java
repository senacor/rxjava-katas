package com.senacor.tecco.reactive.example.combining;

import com.senacor.tecco.reactive.util.ReactiveUtil;
import com.senacor.tecco.reactive.util.WaitMonitor;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class CombineLatestTest {
    @Test
    public void testCombineLatest() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Observable<Long> stream1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(8);
        Observable<Long> stream2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .take(8);
        Disposable subscription = Observable.combineLatest(stream1, stream2, (s1, s2) -> "stream1=" + s1 + " stream2=" + s2)
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(5000, TimeUnit.MILLISECONDS);
        subscription.dispose();
    }
}
