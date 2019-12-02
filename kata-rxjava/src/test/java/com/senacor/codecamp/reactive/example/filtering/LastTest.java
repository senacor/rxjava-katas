package com.senacor.codecamp.reactive.example.filtering;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class LastTest {
    @Test
    public void testLast() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .lastElement()
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }

    @Test
    public void testLastOrDefault() throws Exception {
        Observable.empty()
                .last(99)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace);
    }
}
