package com.senacor.tecco.reactive.example.transforming;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class ScanTest {
    @Test
    public void testBuffer() throws Exception {
        Disposable subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                .scan((first, second) -> first + second)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(2000);
        subscription.dispose();
    }
}
