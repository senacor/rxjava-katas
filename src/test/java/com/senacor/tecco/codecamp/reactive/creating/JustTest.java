package com.senacor.tecco.codecamp.reactive.creating;

import org.junit.Test;
import rx.Observable;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class JustTest {

    @Test
    public void testJust() throws Exception {
        Observable.just("first")
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

        Observable.just("first", "second", "3rd", "...")
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }
}