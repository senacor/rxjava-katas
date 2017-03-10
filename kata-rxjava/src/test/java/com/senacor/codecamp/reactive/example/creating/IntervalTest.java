package com.senacor.codecamp.reactive.example.creating;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class IntervalTest {
    @Test
    public void testInterval() throws Exception {
        Disposable subscription = Observable.<String>interval(200, TimeUnit.MILLISECONDS)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(1000);
        subscription.dispose();
    }
}
