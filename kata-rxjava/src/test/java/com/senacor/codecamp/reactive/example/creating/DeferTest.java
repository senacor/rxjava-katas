package com.senacor.codecamp.reactive.example.creating;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.reactivex.Observable;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

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
        }).subscribe(next -> ReactiveUtil.print("next: %s", next),
                Throwable::printStackTrace,
                () -> ReactiveUtil.print("complete!"));
    }
}
