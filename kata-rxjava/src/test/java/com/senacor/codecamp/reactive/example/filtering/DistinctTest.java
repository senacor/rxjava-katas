package com.senacor.codecamp.reactive.example.filtering;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class DistinctTest {
    @Test
    public void testDistinct() throws Exception {
        Observable.just(1, 2, 2, 3, 1, 4, 5, 6, 7, 5)
                .distinct()
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }
}
