package com.senacor.tecco.codecamp.reactive.creating;

import org.junit.Test;
import rx.Observable;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class NeverTest {

    @Test
    public void testNever() throws Exception {
        Observable.<String>never()
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }
}