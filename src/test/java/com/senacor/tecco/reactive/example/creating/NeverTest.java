package com.senacor.tecco.reactive.example.creating;

import io.reactivex.Observable;
import org.junit.Test;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
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