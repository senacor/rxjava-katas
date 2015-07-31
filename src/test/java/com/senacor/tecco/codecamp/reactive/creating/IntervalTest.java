package com.senacor.tecco.codecamp.reactive.creating;

import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class IntervalTest {
    @Test
    public void testInterval() throws Exception {
        Subscription subscription = Observable.<String>interval(200, TimeUnit.MILLISECONDS)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(3000);
        subscription.unsubscribe();
    }
}
