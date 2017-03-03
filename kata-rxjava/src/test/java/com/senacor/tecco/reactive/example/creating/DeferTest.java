package com.senacor.tecco.reactive.example.creating;

import io.reactivex.Observable;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class DeferTest {

    @Test
    public void testDefer() throws Exception {
        Observable.defer(() -> {
            if (RandomUtils.nextBoolean()) {
                return Observable.just("true", "true", "true");
            } else {
                return Observable.just("false");
            }
        }).subscribe(next -> print("next: %s", next),
                Throwable::printStackTrace,
                () -> print("complete!"));
    }
}
