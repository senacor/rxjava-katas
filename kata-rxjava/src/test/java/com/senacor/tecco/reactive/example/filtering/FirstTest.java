package com.senacor.tecco.reactive.example.filtering;

import io.reactivex.Observable;
import org.junit.Test;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class FirstTest {
    @Test
    public void testFirst() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .firstElement()
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }

    @Test
    public void testFirstOrDefault() throws Exception {
        Observable.empty()
                .first(99)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace);
    }
}
