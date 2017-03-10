package com.senacor.codecamp.reactive.example.transforming;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class ScanTest {
    @Test
    public void testBuffer() throws Exception {
        Disposable subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                .scan((first, second) -> first + second)
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> ReactiveUtil.print("complete!"));

        Thread.sleep(2000);
        subscription.dispose();
    }
}
