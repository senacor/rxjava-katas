package com.senacor.tecco.reactive.example.creating;

import io.reactivex.Observable;
import org.junit.Test;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class ThrowTest {
    @Test
    public void testThrow() throws Exception {
        Observable.<String>error(new IllegalStateException("not yet implemented"))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }
}