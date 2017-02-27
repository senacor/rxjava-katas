package com.senacor.tecco.reactive.example.creating;

import io.reactivex.disposables.Disposable;
import org.junit.Test;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

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
