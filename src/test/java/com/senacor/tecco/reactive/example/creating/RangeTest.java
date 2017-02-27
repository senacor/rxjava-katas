package com.senacor.tecco.reactive.example.creating;

import io.reactivex.Observable;
import org.junit.Test;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class RangeTest {
    @Test
    public void testRange() throws Exception {
        Observable.range(4, 6)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }
}
