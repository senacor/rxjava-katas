package com.senacor.tecco.reactive.example.creating;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class RepeatTest {

    @Test
    public void testRepeat() throws Exception {
        Disposable subscription = Observable.just("foo", "bar", "foobar")
                .repeat()
                .subscribeOn(Schedulers.computation())
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Thread.sleep(1000);
        subscription.dispose();
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
