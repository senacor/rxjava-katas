package com.senacor.codecamp.reactive.example.creating;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.Test;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

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
