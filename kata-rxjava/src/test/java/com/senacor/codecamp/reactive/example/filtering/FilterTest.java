package com.senacor.codecamp.reactive.example.filtering;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class FilterTest {

    @Test
    public void testFilter() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(value -> value % 2 == 0)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }
}
