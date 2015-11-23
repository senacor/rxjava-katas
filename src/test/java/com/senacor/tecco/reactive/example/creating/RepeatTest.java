package com.senacor.tecco.reactive.example.creating;

import org.junit.Test;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class RepeatTest {

    @Test
    public void testRepeat() throws Exception {
        Subscription subscription = Observable.just("foo", "bar", "foobar")
                .repeat(Schedulers.computation())
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(1000);
        subscription.unsubscribe();
    }

    @Test
    public void testRepeatWithCount() throws Exception {
        Observable.just("foo", "bar", "foobar")
                .repeat(2)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }
}
