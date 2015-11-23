package com.senacor.tecco.reactive.example.creating;

import org.junit.Test;
import rx.Observable;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class EmptyTest {

    @Test
    public void testEmpty() throws Exception {
        Observable.<String>empty()
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }
}