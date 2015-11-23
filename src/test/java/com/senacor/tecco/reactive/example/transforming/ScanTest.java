package com.senacor.tecco.reactive.example.transforming;

import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class ScanTest {
    @Test
    public void testBuffer() throws Exception {
        Subscription subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                .scan((first, second) -> first + second)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(2000);
        subscription.unsubscribe();
    }
}
