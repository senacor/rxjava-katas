package com.senacor.tecco.codecamp.reactive.creating;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import rx.Observable;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
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
