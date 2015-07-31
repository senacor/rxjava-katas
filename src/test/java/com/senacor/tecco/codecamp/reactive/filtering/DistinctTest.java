package com.senacor.tecco.codecamp.reactive.filtering;

import org.junit.Test;
import rx.Observable;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.getThreadId;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
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
